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

package gov.hhs.fha.nhinc.adaptersubscription;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.SubscribeRequestType;

/**
 *
 * @author svalluripalli
 */
public class AdapterSubscriptionHelper {
    private static Log log = LogFactory.getLog(AdapterSubscriptionHelper.class);
    public static SubscribeResponse subscribe(SubscribeRequestType subscribeRequest) {
        log.debug("Begin validating Subscription Request");
        SubscribeResponse subscribeResponse = null;
        if(subscribeRequest != null){
            log.info("The Subscribe message has been received");
            subscribeResponse = new SubscribeResponse();
        }
        log.debug("End validating Subscription Request");
        return subscribeResponse;
    }
}
