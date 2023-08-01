/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.workflow.Workflow;
import java.util.Optional;

/**
 *
 * @author azrul
 */
public interface PojoRule {
   public <T extends Element> Boolean compute(Optional<T> owork, Object... otherParams);
}
