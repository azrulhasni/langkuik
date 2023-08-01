/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.dao;

import com.azrul.langkuik.custom.priority.Priority;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import com.azrul.langkuik.framework.dao.filter.FilterRelation;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.framework.dao.params.FindRelationParameter;
import com.azrul.langkuik.framework.dao.params.QueryParams;
import com.azrul.langkuik.framework.dao.query.FindAnyEntityQuery;
import com.azrul.langkuik.framework.dao.query.FindRelationQuery;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import com.azrul.langkuik.framework.exception.EntityIsUsedException;
import com.azrul.langkuik.framework.exception.QueryException;
import com.azrul.langkuik.framework.relationship.RelationManager;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.relationship.RelationManagerFactory;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.entity.IdGenerator;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.framework.user.UserProfile;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.Transactional;
import org.hibernate.envers.Audited;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 *
 * @author azrulm
 * @param <T>
 *
 */
@Repository
@Transactional
public class DataAccessObjectImpl<T> implements DataAccessObject<T>, Serializable {

    @PersistenceUnit()
    private EntityManagerFactory emf;

    @Autowired
    private FieldUtils fieldUtils;

    @Autowired
    private RelationUtils relationUtils;

    @Autowired
    private EntityUtils entityUtils;

    @Autowired
    private RelationManagerFactory relationMgrFactory;


    private Class<T> classOfEntity;

    public DataAccessObjectImpl() {
    }

    public DataAccessObjectImpl(Class<T> daoClass) {
        this.classOfEntity = daoClass;
    }

    //no persistence
    private Optional<T> createNew(Class<T> clazz,
            boolean withId,
            String creatorId,
            Optional<String> tenantId,
            Optional<String> enumPath,
            Optional<String> startEventId,
            Optional<String> startEventDesc,
            Status status,
            EntityManager em) {
        final T bean = entityUtils.createNewObject(clazz);
        Castor.<T, WorkElement>given(bean)
                .castItTo(WorkElement.class)
                .thenDo(ext -> {
                    ext.setCreator(creatorId);
                    ext.getWorkflowInfo().getOwners().add(creatorId);
                    ext.getWorkflowInfo().setStartEventId(startEventId.get());
                    ext.getWorkflowInfo().setStartEventDescription(startEventDesc.orElse(null));
                    ext.getWorkflowInfo().setWorklist(startEventId.get());
                    ext.setTenant(tenantId.orElse(null));
                    ext.setStatus(status);
                    ext.getWorkflowInfo().setWorklistUpdateTime(LocalDateTime.now());
                    ext.setPriority(Priority.NONE);
                }).go();

        Castor.<T, Element>given(bean)
                .castItTo(Element.class)
                .thenDo(ext -> {
                    ext.setCreator(creatorId);
                    ext.setTenant(tenantId.orElse(null));
                    ext.setStatus(status);

                }).go();

        //find an Id
        if (withId == true) {
            for (Field field : fieldUtils.getAllFields(clazz).values()) {
                if (field.getAnnotation(Id.class) != null) {
                    IdGenerator generator = new IdGenerator();
                    generator = em.merge(generator);
                    em.flush();
                    fieldUtils.setValue(bean, field, generator.getValue());
                    Castor.<T, Element>given(bean)
                            .castItTo(Element.class)
                            .thenDo(ext -> {

                                //if root, init enum path
                                ext.initEnumPath();

                                //if not root, update enumpath
                                enumPath.ifPresent(ep -> {
                                    ext.updateEnumPath(ep);
                                });
                            }).go();
                    break;
                }
            }
        }

        return Optional.of(bean);

    }

    @Override
    public void delete(Collection<T> entities) throws EntityIsUsedException {
        if (entities.isEmpty()) {
            return;
        }

        List<Class<?>> managedClasses = entityUtils.getAllEntitiesDescendantFrom(Element.class, emf);
        managedClasses.remove(WorkElement.class);
        managedClasses.remove(Element.class);

        EntityManager em = emf.createEntityManager();

        SearchSession ftem = Search.session(em);

        try {
            em.getTransaction().begin();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            for (T entity : entities) {
                Castor.<T, Element>given(entity)
                        .castItTo(Element.class)
                        .thenDo(ext -> {
                            deleteInCascade(managedClasses, cb, ext, em, ftem);
                        }).go();
            }
            em.getTransaction().commit();
        } catch (PersistenceException pe) {
            em.getTransaction().rollback();
            Logger.getLogger(DataAccessObjectImpl.class.getName()).log(Level.SEVERE, null, pe);
            if (ConstraintViolationException.class.equals(pe.getCause().getClass())) {
                EntityIsUsedException e = new EntityIsUsedException();
                e.initCause(pe);
                throw e;
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.getLogger(DataAccessObjectImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        em.close();
    }

    @Override
    public void retire(Collection<T> entities) {
        if (entities.isEmpty()) {
            return;
        }

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            for (T entity : entities) {
                Castor.<T, Element>given(entity)
                        .castItTo(Element.class)
                        .thenDo(ext -> {
                            ext.setStatus(Status.RETIRED);
                        }).go();
                save(em, entity);
            }

            em.getTransaction().commit();
        } catch (PersistenceException pe) {
            em.getTransaction().rollback();
            Logger.getLogger(DataAccessObjectImpl.class.getName()).log(Level.SEVERE, null, pe);
        }
        em.close();
    }

    @Override
    public void unretire(Collection<T> entities) {
        if (entities.isEmpty()) {
            return;
        }

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            for (T entity : entities) {
                Castor.<T, Element>given(entity)
                        .castItTo(Element.class)
                        .thenDo(ext -> {
                            ext.setStatus(Status.DONE);
                        }).go();
                save(em, entity);
            }

            em.getTransaction().commit();
        } catch (PersistenceException pe) {
            em.getTransaction().rollback();
            Logger.getLogger(DataAccessObjectImpl.class.getName()).log(Level.SEVERE, null, pe);
        }
        em.close();
    }

    private List<T> deleteInCascade(List<Class<?>> managedClasses, CriteriaBuilder cb, Element element, EntityManager em, SearchSession ftem) {
        List<T> toBeDeleted = new ArrayList<>();
        for (Class<?> tclass : managedClasses) {
            CriteriaQuery cq = cb.createQuery(tclass);
            Root root = cq.from(tclass);
            cq.select(root).where(cb.or(
                    cb.like(root.get("enumPath"), element.getEnumPath() + "%"),
                    cb.equal(root.get("id"), element.getId())
            ));
            toBeDeleted.addAll(em.createQuery(cq).getResultList());

        }
        if (!toBeDeleted.isEmpty()) {
            for (var e : toBeDeleted) {
                Castor.<T, Element>given(e)
                        .castItTo(Element.class)
                        .thenDo(ext -> {
                            ext.setStatus(Status.CANCELLED);
                            if (ext.getClass().isAnnotationPresent(Indexed.class)) {
                                ftem.indexingPlan().addOrUpdate(ext);
                            }
                            em.merge(ext);

                        }).go();
            }
        }
        return toBeDeleted;
    }

    @Override
    public Optional<T> save(T newObject) {

        EntityManager em = emf.createEntityManager();
        T savedObject = null;
        try {
            em.getTransaction().begin();
            savedObject = save(em, newObject);
            em.getTransaction().commit();
            return Optional.of(savedObject);

        } catch (Exception e) {
            Logger.getLogger(DataAccessObjectImpl.class.getName()).log(Level.SEVERE, null, e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {

            em.close();
        }
        return Optional.empty();
    }

    @Override
    public Optional<T> createAndSave(Class<T> c,
            Optional<String> tenantId,
            Optional<String> parentEnumPath,
            Optional<String> startEventId,
            Optional<String> startEventDesc,
            Status status,
            String creatorId) {
        EntityManager em = emf.createEntityManager();
        try {

            em.getTransaction().begin();
            Optional<T> obean = createNew(c,
                    true,
                    creatorId,
                    tenantId,
                    parentEnumPath,
                    startEventId,
                    startEventDesc,
                    status,
                    em);

            T bean = saveNew(em, obean.orElseThrow());
            em.getTransaction().commit();
            return Optional.of(bean);
        } catch (Exception e) {
            Logger.getLogger(DataAccessObjectImpl.class.getName()).log(Level.SEVERE, null, e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

        } finally {
            em.close();

        }
        return Optional.empty();
    }

    private <Z> Z saveNew(EntityManager em1, Z bean) {
        em1.persist(bean);
        em1.flush();
        if (bean.getClass().isAnnotationPresent(Indexed.class)) {
            SearchSession ftem = Search.session(em1);
            ftem.indexingPlan().addOrUpdate(bean);
        }
        return bean;
    }

    private <Z> Z save(EntityManager em1, Z bean) {
        bean = em1.merge(bean);
        em1.flush();
        if (bean.getClass().isAnnotationPresent(Indexed.class)) {
            SearchSession ftem = Search.session(em1);
            ftem.indexingPlan().addOrUpdate(bean);
        }
        return bean;
    }

    @Override
    public <P> Collection<T> runQuery(DAOQuery<P, T> query,
            List<Dual<String[], Boolean>> sortFieldsAsc,
            Optional<Integer> startIndex,
            Optional<Integer> offset,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) throws QueryException {
        EntityManager em = emf.createEntityManager();
        return query.doQuery(em, sortFieldsAsc, startIndex, offset, tenantId, oQueryParams);
    }

    @Override
    public <P> Long countQueryResult(DAOQuery<P, T> query,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) throws QueryException {
        EntityManager em = emf.createEntityManager();
        return query.count(em, tenantId, oQueryParams);

    }

    public <P> Collection<T> searchResultAlreadyInParent(Collection<T> searchResult,
            Class<T> daoClass,
            P parentObject,
            String parentToChildrenField) {
        EntityManager em2 = emf.createEntityManager();

        String currentIdField = entityUtils.getIdentifierFieldName(daoClass, em2);

        Collection searchResultIds = new ArrayList();
        for (T a : searchResult) {
            fieldUtils.getValue(a, currentIdField).ifPresent(idField -> searchResultIds.add(idField));
        }

        CriteriaBuilder cb = em2.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<T> criteria = cb.createQuery(daoClass);
        Root parent = criteria.from(parentObject.getClass());
        Join join = parent.join(parentToChildrenField);

        criteria.select(join).where(cb.and(cb.equal(parent, parentObject), join.get(currentIdField).in(searchResultIds)));
        List<T> filteredSearchResult = em2.createQuery(criteria).getResultList();

        return filteredSearchResult;
    }

    @Override
    public <P> Set<Dual<P, T>> saveAndAssociate(Set<T> newBeans,
            P parentBean,
            String parentToNewBeanField,
            Consumer<P> onCommit) {
        return (Set<Dual<P, T>>) relationUtils.getRelationClass((Class<P>) parentBean.getClass(), parentToNewBeanField).map(childClass -> {
            RelationManager<P, T> relationManager = relationMgrFactory.create((Class<P>) parentBean.getClass(), childClass);
            EntityManager em = emf.createEntityManager();
            Set<Dual<P, T>> results = new HashSet<>();
            SearchSession ftem = Search.session(em);
            try {
                em.getTransaction().begin();
                for (T bean : newBeans) {
                    Dual<P, T> newBeansFromDB = linkOneObject(em, bean, parentBean, relationManager, parentToNewBeanField, ftem);
                    results.add(newBeansFromDB);
                }
                em.getTransaction().commit();
                if (!results.isEmpty()) {
                    onCommit.accept(parentBean);
                }
                return results;
            } catch (Exception e) {
                Logger.getLogger(DataAccessObjectImpl.class.getName()).log(Level.SEVERE, null, e);
                Logger.getLogger(DataAccessObjectImpl.class.getName()).log(Level.SEVERE, null, e);
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            } finally {
                em.close();
            }
            return Set.of();
        }).orElse(Set.of());
    }

    @Override
    public <P> Optional<Dual<P, T>> createAssociateAndSave(Class<T> childClass,
            P parentBean,
            String parentToNewBeanField,
            Optional<String> tenantId,
            Optional<String> startEventId,
            Optional<String> startEventDesc,
            Status status,
            String creatorId) {

        RelationManager<P, T> relationManager = relationMgrFactory.create((Class<P>) parentBean.getClass(), childClass);

        EntityManager em = emf.createEntityManager();
        SearchSession ftem = Search.session(em);
        try {
            Optional<String> parentEnumPath = Castor.<P, Element>given(parentBean)
                    .castItTo(Element.class)
                    .thenDo(p -> {
                        return p.getEnumPath();
                    }).go();
            em.getTransaction().begin();
            Optional<T> newBean = createNew(childClass,
                    true,
                    creatorId,
                    tenantId,
                    parentEnumPath,
                    startEventId,
                    startEventDesc,
                    status,
                    em);

            //T newBeanFromDB = save(em, newBean);
            Dual<P, T> newBeansFromDB = linkOneObject(em, newBean.orElseThrow(), parentBean, relationManager, parentToNewBeanField, ftem);
            //save(em,newBeansFromDB.getFirst());
            em.getTransaction().commit();
            return Optional.of(newBeansFromDB);
        } catch (Exception e) {
            Logger.getLogger(DataAccessObjectImpl.class.getName()).log(Level.SEVERE, null, e);
            Logger.getLogger(DataAccessObjectImpl.class.getName()).log(Level.SEVERE, null, e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        return Optional.empty();
    }

    @Override
    public <P> Optional<Dual<P, T>> saveAndAssociate(T newBean, P parentBean, String parentToNewBeanField) {
        return saveAndAssociate(newBean, parentBean, parentToNewBeanField, p -> {
        });
    }

    @Override
    public <P> Optional<Dual<P, T>> saveAndAssociate(T newBean, P parentBean, String parentToNewBeanField, Consumer<P> onCommit) {
        if (newBean == null
                || parentBean == null
                || parentToNewBeanField == null) {
            return Optional.empty();
        }
        Class<T> childClass = (Class<T>) newBean.getClass();
        RelationManager<P, T> relationManager = relationMgrFactory.create((Class<P>) parentBean.getClass(), childClass);

        EntityManager em = emf.createEntityManager();
        SearchSession ftem = Search.session(em);
        try {
            em.getTransaction().begin();
            Dual<P, T> newBeansFromDB = linkOneObject(em, newBean, parentBean, relationManager, parentToNewBeanField, ftem);

            em.getTransaction().commit();
            onCommit.accept(parentBean);
            return Optional.of(newBeansFromDB);
        } catch (Exception e) {
            Logger.getLogger(DataAccessObjectImpl.class.getName()).log(Level.SEVERE, null, e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        return Optional.empty();
    }

    private <P> Dual<P, T> linkOneObject(EntityManager em1,
            T newBean, P parentBean,
            RelationManager<P, T> relationManager,
            String parentToNewBeanField,
            SearchSession ftem) {

        T newBeanFromDB = em1.merge(newBean);
        //P parentBeanFromDB = em1.merge(parentBean);
        P parentBeanFromDB = refresh(em1, parentBean).orElseThrow();
        relationManager.link(parentBeanFromDB, newBeanFromDB, parentToNewBeanField);
        newBeanFromDB = save(em1, newBeanFromDB);
        parentBeanFromDB = save(em1, parentBeanFromDB);

        em1.flush();

        return new Dual(parentBeanFromDB, newBeanFromDB);
    }

    private <E> Optional<E> refresh(EntityManager em, final E entity) {
        Object idValue = entityUtils.getIdentifierValue(entity, em);
        if (idValue == null) {
            return Optional.empty(); //cannot refresh something that does exist not in DB
        }
        try {
            E parentBeanFromDB = em.find((Class<E>) entity.getClass(), idValue);
            return Optional.of(parentBeanFromDB);
        } catch (Exception e) {
            //object does not exist
            return Optional.empty();
        }
    }

    @Override
    public <P> Optional<P> unlinkAndDelete(FindRelationParameter<P, T> feParam, Collection<T> oldBeans, Consumer2Inputs<P, T> onCommit) {

        P parentBean = feParam.getParentObject();
        String parentToNewBeanField = feParam.getParentToCurrentField();

        List<Class<?>> managedClasses = entityUtils.getAllEntitiesDescendantFrom(Element.class, emf);
        managedClasses.remove(WorkElement.class);
        managedClasses.remove(Element.class);

        return (Optional<P>) relationUtils.getRelationClass(parentBean.getClass(), parentToNewBeanField).map(childClass -> {
            RelationManager relationManager = relationMgrFactory.create(parentBean.getClass(), childClass);
            EntityManager em = emf.createEntityManager();
            SearchSession ftem = Search.session(em);

            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                em.getTransaction().begin();
                List oldBeansFromDB = new ArrayList();
                P parentBeanFromDB = refresh(em, parentBean).orElseThrow();
                for (T oldBean : oldBeans) {
                    T oldBeanFromDB = refresh(em, oldBean).orElseThrow();
                    relationManager.unlink(parentBeanFromDB, oldBeanFromDB, parentToNewBeanField);
                    oldBeansFromDB.add(oldBeanFromDB);
                    Castor.<T, Element>given(oldBeanFromDB)
                            .castItTo(Element.class)
                            .thenDo(ext -> {
                                deleteInCascade(managedClasses, cb, ext, em, ftem);
                            }).go();
                    em.merge(oldBeanFromDB);
                }
                em.merge(parentBeanFromDB);

                em.flush();

                em.getTransaction().commit();
                for (T oldBean : oldBeans) {
                    onCommit.accept(parentBean, oldBean);
                }
                return Optional.of(parentBeanFromDB);
            } catch (Exception e) {
                Logger.getLogger(DataAccessObjectImpl.class.getName()).log(Level.SEVERE, null, e);
                em.getTransaction().rollback();
            } finally {
                em.close();
            }
            return Optional.empty();
        }).orElse(Optional.empty());
    }

    public <P> Optional<P> unlink(FindRelationParameter<P, T> frParam, Collection<T> oldBeans, Consumer2Inputs<P, T> onCommit) {
        P parentBean = frParam.getParentObject();
        String parentToCurrentField = frParam.getParentToCurrentField();

        EntityManager em = emf.createEntityManager();
        return (Optional<P>) relationUtils.getRelationClass(parentBean.getClass(), parentToCurrentField).map(childClass -> {
            RelationManager relationManager = relationMgrFactory.create(parentBean.getClass(), childClass);

            SearchSession ftem = Search.session(em);

            try {
                em.getTransaction().begin();
                List<T> oldBeansFromDB = new ArrayList<>();
                P parentBeanFromDB = refresh(em, parentBean).orElseThrow();
                for (T oldBean : oldBeans) {
                    T oldBeanFromDB = refresh(em, oldBean).orElseThrow();
                    relationManager.unlink(parentBeanFromDB, oldBeanFromDB, parentToCurrentField);
                    oldBeansFromDB.add(oldBeanFromDB);
                    em.merge(oldBeanFromDB);
                }
                em.merge(parentBeanFromDB);

                em.flush();

                if (parentBeanFromDB.getClass().isAnnotationPresent(Indexed.class)) {
                    ftem.indexingPlan().addOrUpdate(parentBeanFromDB);
                }

                for (T oldBeanFromDB : oldBeansFromDB) {
                    if (oldBeanFromDB.getClass().isAnnotationPresent(Indexed.class)) {
                        ftem.indexingPlan().addOrUpdate(oldBeanFromDB);
                    }
                }
                em.getTransaction().commit();
                for (T oldBean : oldBeans) {
                    onCommit.accept(parentBean, oldBean);
                }
                return Optional.of(parentBeanFromDB);
            } catch (Exception e) {
                Logger.getLogger(DataAccessObjectImpl.class.getName()).log(Level.SEVERE, null, e);
                em.getTransaction().rollback();
            } finally {
                em.close();
            }
            return Optional.empty();
        }).orElse(Optional.empty());
    }

    public boolean isAuditable(Class aclass) {
        if (aclass.isAnnotationPresent(Audited.class)) {
            return true;
        }
        for (Field field : fieldUtils.getAllFields(aclass).values()) {
            if (field.getAnnotation(Audited.class) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the classOfEntity
     */
    public Class<T> getClassOfEntity() {
        return classOfEntity;
    }

    /**
     * @param classOfEntity the classOfEntity to set
     */
    public void setClassOfEntity(Class<T> classOfEntity) {
        this.classOfEntity = classOfEntity;
    }

    public void massIndex() {
        EntityManager em = emf.createEntityManager();

        Metamodel mm = emf.getMetamodel();

        SearchSession ftem = Search.session(em);

        MassIndexer massIndexer = ftem.massIndexer();

        massIndexer.purgeAllOnStart(true);
        try {
            massIndexer.startAndWait();
            // txtentityManager.
        } catch (InterruptedException e) {
            Logger.getLogger("mass reindexing interrupted: " + e.getMessage());
        } finally {
            ftem.indexingPlan().execute();
        }
    }

    public <P> Long countQueryResult(DAOQuery<P, T> query,
            QueryParams queryParams) throws QueryException {
        return countQueryResult(query,
                Optional.empty(),
                Optional.ofNullable(queryParams));
    }

    public <P> Collection<T> runQuery(DAOQuery<P, T> query,
            Optional<String> tenantId) throws QueryException {
        return runQuery(query,
                new ArrayList<>(),
                Optional.of(0),
                Optional.empty(),
                tenantId,
                Optional.empty());
    }

    public <P> Collection<T> runQuery(DAOQuery<P, T> query,
            Optional<String> tenantId,
            Optional<QueryParams> queryParams) throws QueryException {
        return runQuery(query,
                new ArrayList<>(),
                Optional.of(0),
                Optional.empty(),
                tenantId,
                queryParams);
    }

   

}
