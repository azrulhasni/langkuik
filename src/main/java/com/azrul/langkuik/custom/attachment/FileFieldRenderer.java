/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.attachment;

import com.azrul.langkuik.custom.CustomFieldRenderer;
import com.azrul.langkuik.framework.minio.MinioService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author azrul
 */
public class FileFieldRenderer<P, L> implements CustomFieldRenderer<P, L> {

    @Autowired
    private MinioService minioService;

    @Override
    public Optional<Component> createInForm(P bean, String fieldName, String displayName, Map<String, AbstractField> fieldsInForm) {
        return Optional.empty();
    }

    @Override
    public Optional<Component> createInTable(P bean, String fieldName) {
        Attachment attachment = (Attachment) bean;
        if (attachment.getFileName() == null) {
            return Optional.of(new Label());
        }
        Anchor download = new Anchor(
                new StreamResource(
                        attachment.getFileName(),
                        new InputStreamFactory() {
                    @Override
                    public InputStream createInputStream() {
                        return minioService.get(attachment.getRelativeLocation());
                    }
                }),"");
        download.getElement().setAttribute("download", true);
        download.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
        return Optional.of(download);
    }

}
