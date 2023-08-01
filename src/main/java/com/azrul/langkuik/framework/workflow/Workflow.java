/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.workflow;

import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.workflow.model.Activity;
import com.azrul.langkuik.framework.workflow.model.HumanActivity;
import com.azrul.langkuik.framework.workflow.model.StartEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author azrul
 */
public interface Workflow<T extends WorkElement> {
    T run(T currentEntity, boolean isError);
    Map<String,List<HumanActivity>> getRoleActivityMap();
    Boolean isActivityAccessibleByRoles(String activity, Set<String> roles);
    Boolean isActivityAccessibleByRoles(Activity activity, Set<String> roles);
    Set<String> whoCanStart();
    Set<String> whoCanStart(T currentEntity);
    Boolean isApprovalActivity(Activity activity);
    Boolean isApprovalActivity(String activity);
    Set<StartEvent> getStartEvents();
    Boolean isStartEvent(Activity activity);
    Boolean isStartEvent(String activityId);
    Boolean isActivitySLAExpired(HumanActivity activity, LocalDateTime workSLAUpdateTime);
    Boolean isActivitySLAExpired(String activity, LocalDateTime workSLAUpdateTime);
}
