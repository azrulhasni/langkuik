/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azrul.langkuik.custom.bizConfig;
import com.azrul.langkuik.views.common.MainView;
import com.azrul.langkuik.views.table.TableView;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 *
 * @author azrul
 */
@Route(value = "configuration", layout = MainView.class)
@PageTitle("Configuration")
public class ConfigMDV extends TableView<Config>{
    public ConfigMDV(){
        super(Config.class, TableView.Mode.MAIN);
    }
}


