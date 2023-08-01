/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.standard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author azrul
 */
public class Castor<FROM, TO> {

    private Class<TO> toClass = null;
    private Class<FROM> fromClass = null;
    private FROM fromObject = null;
    private List<FROM> fromObjects = new ArrayList<>();
    private Consumer<TO> toConsumer = null;
    private Runnable toRunnable = null;
    private Function<TO, ?> toFunction = null;
    private Supplier<TO> toSupplier = null;
    private Runnable orElseRunnable = null;
    private Supplier<?> orElseSupplier = null;

    public static <FROM, TO> Castor<FROM, TO> given(FROM o) {
        Castor<FROM, TO> castor = new Castor<>();
        if (o!=null){
            castor.setFromClass((Class<FROM>) o.getClass());
            castor.setFromObject(o);
        }
        return castor;
    }

    public static <FROM, TO> Castor<FROM, TO> given(FROM... o) {
        
        Castor<FROM, TO> castor = new Castor<>();
        if (o.length >= 1) {
            castor.setFromClass((Class<FROM>) o[0].getClass());
            castor.fromObjects.addAll(Arrays.asList(o));
        }
        return castor;
    }
    
    public static <FROM, TO> Castor<FROM, TO> given(Set<FROM> o) {
        Castor<FROM, TO> castor = new Castor<>();
        if (o.size() >= 1) {
            castor.setFromClass((Class<FROM>) o.iterator().next().getClass());
            castor.fromObjects.addAll(o);
        }
        return castor;
    }

    public static <FROM, TO> Castor<FROM, TO> given(Class<FROM> o) {
        Castor<FROM, TO> castor = new Castor<>();
        if (o!=null){
            castor.setFromClass(o);
        }
        return castor;
    }

    public Castor<FROM, TO> castItTo(Class<TO> tclass) {
        this.setToClass(tclass);
        return this;
    }

    public Castor<FROM, TO> thenDo(Consumer<TO> todo) {
        this.setToConsumer(todo);
        return this;
    }

    public Castor<FROM, TO> thenDo(Function<TO, ?> todo) {
        this.setToFunction(todo);
        return this;
    }

    public Castor<FROM, TO> thenDo(Runnable todo) {
        this.setToRunnable(todo);
        return this;
    }
    
    public Castor<FROM, TO> thenDo(Supplier<TO> todo) {
        this.setToSupplier(todo);
        return this;
    }

    public Castor<FROM, TO> failingWhichDo(Runnable toDoOrElse) {
        this.setOrElseRunnable(toDoOrElse);
        return this;
    }

    public Castor<FROM, TO> failingWhichDo(Supplier toDoOrElse) {
        this.setOrElseSupplier(toDoOrElse);
        return this;
    }

    public <V> Optional<V> go() {
        if (getToFunction() != null
                && getToClass() != null
                && getToClass().isAssignableFrom(getFromClass())
                && getFromObject() != null) {
            var tInput = getToClass().cast(getFromObject());
            return Optional.of((V) getToFunction().apply(tInput));
        } else if (getToConsumer() != null
                && getToClass() != null
                && getToClass().isAssignableFrom(getFromClass())
                && getFromObject() != null) {
            var tInput = getToClass().cast(getFromObject());
            getToConsumer().accept(tInput);
            return Optional.empty();
        } else if (getToSupplier() != null
                && getToClass() != null
                && getToClass().isAssignableFrom(getFromClass())
                && getFromObject() != null) {
            return Optional.of((V)getToSupplier().get());
        }else if (getToFunction() != null
                && getToClass() != null
                && getToClass().isAssignableFrom(getFromClass())
                && getFromObjects() != null) {
            List toList = new ArrayList<>();
            for (var o : getFromObjects()) {
                var tInput = getToClass().cast(getFromObject());
                toList.add(getToFunction().apply(tInput));
            }
            return Optional.of((V) toList.toArray());
        } else if (getToConsumer() != null
                && getToClass() != null
                && getToClass().isAssignableFrom(getFromClass())
                && getFromObjects() != null) {
            for (var o : getFromObjects()) {
                var tInput = getToClass().cast(getFromObject());
                getToConsumer().accept(tInput);
            }
            return Optional.empty();
        } else if (getToConsumer() != null
                && getToClass() != null
                && getToClass().isAssignableFrom(getFromClass())
                && getFromClass() != null) {
            getToConsumer().accept(null);
            return Optional.empty();
        } else if (getToRunnable() != null
                && getToClass() != null
                && getToClass().isAssignableFrom(getFromClass())) {
            getToRunnable().run();
            return Optional.empty();
        } else {
            if (getOrElseRunnable() != null) {
                getOrElseRunnable().run();
                return Optional.empty();
            } else {
                if (getOrElseSupplier() != null) {
                    return Optional.of((V) getOrElseSupplier().get());
                } else {
                    return Optional.empty();
                }
            }
        }

    }

    private Castor() {

    }

    /**
     * @return the toClass
     */
    private Class<TO> getToClass() {
        return toClass;
    }

    /**
     * @param toClass the toClass to set
     */
    private void setToClass(Class<TO> toClass) {
        this.toClass = toClass;
    }

    /**
     * @return the fromClass
     */
    private Class<FROM> getFromClass() {
        return fromClass;
    }

    /**
     * @param fromClass the fromClass to set
     */
    private void setFromClass(Class<FROM> fromClass) {
        this.fromClass = fromClass;
    }

    /**
     * @return the fromObject
     */
    private FROM getFromObject() {
        return fromObject;
    }

    /**
     * @param fromObject the fromObject to set
     */
    private void setFromObject(FROM fromObject) {
        this.fromObject = fromObject;
    }

    /**
     * @return the toConsumer
     */
    private Consumer<TO> getToConsumer() {
        return toConsumer;
    }

    /**
     * @param toConsumer the toConsumer to set
     */
    private void setToConsumer(Consumer<TO> toConsumer) {
        this.toConsumer = toConsumer;
    }

    /**
     * @return the toRunnable
     */
    private Runnable getToRunnable() {
        return toRunnable;
    }

    /**
     * @param toRunnable the toRunnable to set
     */
    private void setToRunnable(Runnable toRunnable) {
        this.toRunnable = toRunnable;
    }

    /**
     * @return the toFunction
     */
    private Function<TO, ?> getToFunction() {
        return toFunction;
    }

    /**
     * @param toFunction the toFunction to set
     */
    private void setToFunction(Function<TO, ?> toFunction) {
        this.toFunction = toFunction;
    }

    /**
     * @return the orElseRunnable
     */
    private Runnable getOrElseRunnable() {
        return orElseRunnable;
    }

    /**
     * @param orElseRunnable the orElseRunnable to set
     */
    private void setOrElseRunnable(Runnable orElseRunnable) {
        this.orElseRunnable = orElseRunnable;
    }

    /**
     * @return the orElseSupplier
     */
    private Supplier<?> getOrElseSupplier() {
        return orElseSupplier;
    }

    /**
     * @param orElseSupplier the orElseSupplier to set
     */
    private void setOrElseSupplier(Supplier<?> orElseSupplier) {
        this.orElseSupplier = orElseSupplier;
    }

    /**
     * @return the fromObjects
     */
    private List<FROM> getFromObjects() {
        return fromObjects;
    }

    /**
     * @param fromObjects the fromObjects to set
     */
    private void setFromObjects(List<FROM> fromObjects) {
        this.fromObjects = fromObjects;
    }

    /**
     * @return the toSupplier
     */
    public Supplier<TO> getToSupplier() {
        return toSupplier;
    }

    /**
     * @param toSupplier the toSupplier to set
     */
    public void setToSupplier(Supplier<TO> toSupplier) {
        this.toSupplier = toSupplier;
    }

}
