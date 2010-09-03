/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.processor.nhin.handler;

import gov.hhs.fha.nhinc.hiem.processor.common.SubscriptionStorage;
import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.ls.LSException;

/**
 * Base subscription handler
 * 
 * @author Neil Webb
 */
abstract class BaseSubscriptionHandler implements SubscriptionHandler {

    protected Log log = null;
    protected SubscriptionStorage subscriptionStorage;

    public BaseSubscriptionHandler() {
        subscriptionStorage = new SubscriptionStorage();
        log = LogFactory.getLog(getClass());
        log.debug("Constructor called");
    }

    abstract public SubscribeResponse handleSubscribe(Element subscribe ) throws SubscribeCreationFailedFault;

    protected EndpointReferenceType storeSubscriptionItem(HiemSubscriptionItem subscriptionItem) {
        log.debug("In storeSubscriptionItem");
        return subscriptionStorage.storeSubscriptionItem(subscriptionItem);
    }

    protected HiemSubscriptionItem createSubscriptionItem(Element subscribe, String producer, String consumer) throws  SubscribeCreationFailedFault {
        
        String rawSubscribe;
        try {
            rawSubscribe = XmlUtility.serializeElement(subscribe);
        } catch (Exception ex) {
            throw new SoapFaultFactory().getMalformedSubscribe("Unable to serialize subscribe element", ex) ;
        }

        HiemSubscriptionItem subscription = null;
        if (subscribe != null) {
            log.debug("Creating subscription item");
            subscription = new HiemSubscriptionItem();

            subscription.setSubscribeXML(rawSubscribe);
            // TODO: finish
            subscription.setProducer(producer);
            subscription.setConsumer(consumer);
            log.debug("Finished creating subscription item");
        } else {
            log.debug("Subscribe message was null in createSubscriptionItem");
        }
        return subscription;
    }
}
