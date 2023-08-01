/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.views.common;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author azrul
 */
@Route("")
public class LandingView extends Div{
    
    @Value( "${application.lgFullurl}" )
    private String fullUrl;

    
    public LandingView(){
       VerticalLayout layout = new VerticalLayout();
        layout.setHeightFull();
        //layout.getStyle().set("border", "1px solid #9E9E9E");
        
        Button btnLogin =  new Button("Login");
        btnLogin.setId("btnLogin");
        btnLogin.addClickListener(e->{
           UI.getCurrent().getPage().executeJs("window.open(\""+fullUrl+"/main\", \"_self\");");
        });
        layout.add(btnLogin);
        layout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER,
                btnLogin);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        this.add(layout);

    }
    
}
