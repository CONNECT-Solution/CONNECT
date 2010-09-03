/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import oasis.names.tc.xacml._2_0.context.schema.os.ActionType;

/**
 *
 * @author rayj
 */
public class ActionHelper {

    private static final String ActionAttributeId = "urn:oasis:names:tc:xacml:1.0:action:action-id";

    public static ActionType actionFactory(String actionValue) {
        ActionType action = new ActionType();
        return actionFactory(action, actionValue);
    }

    public static ActionType actionFactory(ActionType action, String actionValue) {
        if (action == null) {
            action = new ActionType();
        }
        AttributeHelper attrHelper = new AttributeHelper();
        action.getAttribute().add(attrHelper.attributeFactory(ActionAttributeId, Constants.DataTypeString, (Object) actionValue));
        return action;
    }
}
