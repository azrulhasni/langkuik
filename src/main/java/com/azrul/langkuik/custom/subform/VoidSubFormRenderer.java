/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.subform;

import com.azrul.langkuik.custom.subform.SubFormRenderer;
import com.azrul.langkuik.views.pojo.PojoViewState;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.commons.collections4.MultiValuedMap;

/**
 *
 * @author azrul
 */
public class VoidSubFormRenderer implements SubFormRenderer {

   
//
//    @Override
//    public void beforeSaveCallBack() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void beforeSubmitCallBack() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public Optional render(Object root, Object parent, String relationName, Optional oParentState, Map eventToOpenOtherComponent, Map relationMementos, Consumer onCommit, Consumer onDelete) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean preCondition(Object root, Object parent, String relationName, Optional oParentState) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    



  
    

   
    
}
