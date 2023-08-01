/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.workflow.Workflow;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author azrul
 */
public interface EntityRule {

    public Boolean compute(Optional<WebEntity> oWebEntity, Object... otherParams);
}
