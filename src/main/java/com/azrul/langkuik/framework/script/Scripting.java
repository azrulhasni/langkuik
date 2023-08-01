/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azrul.langkuik.framework.script;

import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.workflow.Workflow;

/**
 *
 * @author azrul
 */
public interface Scripting {
     public <T extends WorkElement> void runScript(T work, String script,  Workflow workflow);
}
