/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.attachment;

import com.azrul.langkuik.custom.EventToOpenOtherComponent;
import com.azrul.langkuik.framework.standard.LangkuikMultiFileBuffer;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.framework.dao.params.FindRelationParameter;
import com.azrul.langkuik.framework.dao.query.FindRelationQuery;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.minio.MinioService;
import com.azrul.langkuik.framework.relationship.RelationManager;
import com.azrul.langkuik.framework.relationship.RelationManagerFactory;
import com.azrul.langkuik.framework.relationship.RelationMemento;
import com.azrul.langkuik.framework.rule.PojoRule;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.views.common.ConfirmationDialog;
import com.azrul.langkuik.views.pojo.PojoTableFactory;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.VaadinSession;
import elemental.json.Json;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import com.azrul.langkuik.framework.dao.filter.FilterRelation;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.rule.AddDeleteRelationRule;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.views.pojo.PojoViewState;
import com.azrul.langkuik.views.table.SearchPanel;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.azrul.langkuik.custom.subform.SubFormRenderer;
import org.apache.commons.collections4.MultiValuedMap;

/**
 *
 * @author azrul
 */
public class AttachmentsRenderer<P, R extends WorkElement> implements SubFormRenderer<P, R, AttachmentLayout> {

    @Value("${application.lgElementPerPageAttachment}")
    private int LIMIT;

    @Value("${application.lgDateFormat:yyyy-MM-dd}")
    private String dateFormat;

    @Value("${application.lgAttachmentMaxFileSize}")
    private int attachmentMaxFileSize;

    @Value("${application.lgAttachmentBaseMinioDir}")
    private String attachmentBaseMinioDir;

    @Autowired
    private DataAccessObject dao;

    @Autowired
    private FieldUtils fieldUtils;

    @Autowired
    private RelationUtils relationUtils;

    @Autowired
    private PojoTableFactory pojoTableFactory;

    @Autowired
    private RelationManagerFactory relationMgrFactory;

    @Autowired
    private MinioService minioService;

    @Autowired
    @Qualifier("CanEditEntityRule")
    private PojoRule canEditRule;

    @Autowired
    @Qualifier("CanAddDeleteRelationsRule")
    private AddDeleteRelationRule canAddDeleteRelationsRule;

    private final Upload upload;

    private RelationMemento memento;

    public AttachmentsRenderer() {
        LangkuikMultiFileBuffer buffer = new LangkuikMultiFileBuffer();
        upload = new Upload(buffer);
    }

    @Override
    public Optional<AttachmentLayout> render(R root,
            P parent,
            String relationName,
            Optional<PojoViewState> oParentState,
            Map<Integer,EventToOpenOtherComponent> eventToOpenOtherComponent,
            Map<String, RelationMemento> relationMementos,
            Consumer<RelationMemento> onCommit,
            Consumer<RelationMemento> onDelete) {

        AttachmentLayout attachmentLayout = new AttachmentLayout();

        return relationUtils.getWebRelation(parent.getClass(), relationName).map(webRelation -> {

            final String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
            final String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");

            final Span spanErrorMsg = new Span();

            HorizontalLayout firstLayerLayout = new HorizontalLayout();

            FindRelationQuery query = new FindRelationQuery(
                    new FindRelationParameter(parent, relationName));
            Collection<AttachmentsContainer> attchs = dao.runQuery(query,
                    new ArrayList<>(),
                    Optional.of(0),
                    Optional.of(LIMIT),
                    Optional.of(tenant),
                    Optional.empty());
            AttachmentsContainer atts = null;
            if (attchs.isEmpty()) {

                Optional<Dual<P, AttachmentsContainer>> oDualAttachments = dao.createAssociateAndSave(AttachmentsContainer.class,
                        parent,
                        relationName,
                        Optional.of(tenant),
                        Optional.empty(),
                        Optional.empty(),
                        Status.DONE,
                        userIdentifier);

                Dual<P, AttachmentsContainer> dualAttachments = oDualAttachments.orElseThrow();
                atts = dualAttachments.getSecond();

                RelationManager relationMgr = relationMgrFactory.create(parent.getClass(), AttachmentsContainer.class);
                relationMgr.link(parent, atts, relationName);//link the attachemts object back to

            } else {
                atts = attchs.iterator().next();
            }
            final AttachmentsContainer attachments = atts;

            if (attachments != null) {

                upload.setMaxFileSize(attachmentMaxFileSize);
                upload.setMaxFiles(1); //upload 1 at a time
                upload.setAutoUpload(true);
                upload.setDropAllowed(false);
                upload.setId("uploadAttachment");

                firstLayerLayout.add(spanErrorMsg);

                SearchPanel<Attachment> searchPanel = new SearchPanel("Search",Attachment.class);
                searchPanel.setSearchListener(ev -> {
                    if (ev.getSearchFieldValue() == null) {
                        memento.setSearchText(Optional.empty());
                    } else if (ev.getSearchFieldValue().isBlank()
                            || ev.getSearchFieldValue().isEmpty()) {
                        memento.setSearchText(Optional.empty());
                    } else {
                        memento.setSearchText(Optional.of(ev.getSearchFieldValue()));
                    }
                    pojoTableFactory.searchAndRedrawTable(memento);
                });

                VerticalLayout uploadButtonBox = new VerticalLayout();
                firstLayerLayout.add(upload);
                uploadButtonBox.add(firstLayerLayout);
                Details searchDetails = new Details("Search attachment", searchPanel);
                uploadButtonBox.add(searchDetails);

                AndFilters andFilters = AndFilters.build(
                        QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.CANCELLED),
                        QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.RETIRED)
                );

                memento = relationMementos.getOrDefault("_attachments",
                        pojoTableFactory.createRelationMemento(
                                Optional.empty(),
                                attachments,
                                "_attachments",
                                Optional.of(andFilters),
                                200,
                                Optional.ofNullable(Integer.valueOf(webRelation.minCount())),
                                Optional.ofNullable(Integer.valueOf(webRelation.maxCount())),
                                "Attachment", List.of()));

                //relationMementos.put("_attachments", memento);

                pojoTableFactory.createTable(attachmentLayout,
                        memento,
                        Optional.of(uploadButtonBox),
                        e -> {
                        });

                memento.setBtnAddLinkNew(upload);
                memento.setBtnDeleteUnlink(attachmentLayout);
                Button btnDelete = createDeleteButton(
                        root,
                        memento,
                        attachments,
                        userIdentifier,
                        onDelete);
                firstLayerLayout.add(btnDelete);
                memento.setBtnDeleteUnlink(btnDelete);

                Optional<String> parentEnumPath
                        = Castor.<AttachmentsContainer, Element>given(attachments)
                                .castItTo(Element.class)
                                .thenDo(p -> {
                                    return p.getEnumPath();
                                }).go();

                upload.addSucceededListener(event -> {
                    spanErrorMsg.setText("");
                    Optional<Attachment> oattachment = dao.createAndSave(Attachment.class,
                            Optional.of(tenant),
                            parentEnumPath,
                            Optional.empty(),
                            Optional.empty(),
                            Status.DONE,
                            userIdentifier);

                    Attachment attachment = oattachment.orElseThrow();
                    attachment.setFileName(event.getFileName());
                    attachment.setMimeType(event.getMIMEType());
                    String path = "/" + attachmentBaseMinioDir + "/" + attachment.getTenant() + "/" + attachment.getId();
                    InputStream is = ((LangkuikMultiFileBuffer) upload.getReceiver()).getInputStream(event.getFileName());
                    minioService.save(is, event.getFileName(), path).ifPresent(relativePath -> {
                        attachment.setRelativeLocation(relativePath);
                    });
                    dao.saveAndAssociate(attachment,
                            attachments,
                            "_attachments",
                            p -> {
                                onCommit.accept(memento);
                            });

                    pojoTableFactory.searchAndRedrawTable(memento);
                    upload.getElement().setPropertyJson("files", Json.createArray()); //clear upload

                });

                upload.addFileRejectedListener(e -> {
                    //e.getSource().
                    spanErrorMsg.setText(e.getErrorMessage());
                    //errorMsgs.add(e.getErrorMessage());
                });
                upload.addFailedListener(e -> {
                    spanErrorMsg.add("Error uploading the file:" + e.getFileName());
                });
                //VerticalLayout uploadLayout = new VerticalLayout();

                attachmentLayout.setOnEnable((enable) -> {
                    firstLayerLayout.setVisible(enable);
                });
            }
            return Optional.of(attachmentLayout);
        }).orElse(Optional.of(new AttachmentLayout()));
    }

    private Button createDeleteButton(R root,
            RelationMemento relationMemento,
            AttachmentsContainer attachments,
            final String username,
            Consumer<RelationMemento> onDelete) {
        Button btnDelete = new Button("Unlink/Delete", e -> {

            ConfirmationDialog confirmDeleteDialog = new ConfirmationDialog(
                    "Confirm unlinking/deletion",
                    "Are you sure you want to unlink/delete this item?",
                    "Delete/Unlink",
                    (ce) -> {
                        Grid grid = relationMemento.getGrid();
                        Set itemsToBeDeleted = grid.getSelectedItems();
                        List<String> cannotBeDeleted = new ArrayList<>();

                        if (canEditRule.compute(Optional.of(root))
                        && canAddDeleteRelationsRule.compute(
                                Optional.of(root),
                                Optional.of(attachments),
                                relationMemento.getRelationName(),
                                Optional.of(relationMemento.getChildClass()),
                                itemsToBeDeleted)) {
                            FindRelationParameter param = new FindRelationParameter(attachments,
                                    relationMemento.getRelationName());
                            dao.unlinkAndDelete(param, itemsToBeDeleted, (p, c) -> {
                                onDelete.accept(relationMemento);
                                minioService.delete(((Attachment) c).getRelativeLocation());
                            });

                            pojoTableFactory.searchAndRedrawTable(relationMemento);
                        } else {
                            itemsToBeDeleted.stream().forEach(a -> cannotBeDeleted.add(((Attachment) a).getFileName()));
                        }
                        if (!cannotBeDeleted.isEmpty()) {
                            StringBuilder textHtml = new StringBuilder("<div><div>The items below cannot be deleted</div> <ul>");

                            for (String item : cannotBeDeleted) {
                                textHtml.append("<li>" + item + "</li>");
                            }
                            textHtml.append("</ul></div>");
                            Html hc = new Html(textHtml.toString());

                            Notification errorNotif = new Notification(hc);
                            errorNotif.setPosition(Notification.Position.TOP_START);
                            errorNotif.setDuration(3000);
                            errorNotif.open();
                        }
                    },
                    "Cancel", (ce) -> {
                    });
            confirmDeleteDialog.open();

        });
        btnDelete.setId("btnDelete");
        return btnDelete;
    }

    @Override
    public Boolean preCondition(R root, P parent, String relationName, Optional<PojoViewState> oParentState) {
        return true;
    }

}
