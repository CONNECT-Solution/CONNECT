/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.processor.common;

import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import org.oasis_open.docs.wsn.b_2.Subscribe;

/**
 * Utility for subscription item objects.
 * 
 * @author Neil Webb
 */
public class SubscriptionItemUtil
{
    /**
     * Create a subscription item from subscription info
     *
     * @param subscribe Subscription message
     * @param rawSubscribeXml Optional subscribe XML. If not provided, the subscribe will be serialized to an XML string.
     * @param parentSubscriptionReferenceXML Parent subscription reference
     * @param consumer Notification consumer
     * @param producer Notification producer
     * @param targets Optional NHIN targets if this is a targeted entity subscribe
     * @return Subscription item
     */
    public HiemSubscriptionItem createSubscriptionItem(Subscribe subscribe, String rawSubscribeXml, String parentSubscriptionReferenceXML, String consumer, String producer, String targets)
    {
        HiemSubscriptionItem subscriptionItem = null;
        if(subscribe != null)
        {
            subscriptionItem = new HiemSubscriptionItem();

            if(rawSubscribeXml != null)
            {
                subscriptionItem.setSubscribeXML(rawSubscribeXml);
            }
            else
            {
                subscriptionItem.setSubscribeXML(marshalSubscribe(subscribe));
            }
            subscriptionItem.setSubscriptionReferenceXML(extractSubscriptionReference(subscribe));
            subscriptionItem.setRootTopic(extractRootTopic(subscribe));
            subscriptionItem.setParentSubscriptionReferenceXML(parentSubscriptionReferenceXML);
            subscriptionItem.setConsumer(consumer);
            subscriptionItem.setProducer(producer);
            subscriptionItem.setTargets(targets);
        }
        return subscriptionItem;
    }

    private String marshalSubscribe(Subscribe subscribe)
    {
        String subscribeXml = null;
        if(subscribe != null)
        {
            // TODO: Finish
        }
        return subscribeXml;
    }

    private String extractRootTopic(Subscribe subscribe)
    {
        String rootTopic = null;
        if(subscribe != null)
        {
            // TODO: Finish
        }
        return rootTopic;
    }

    private String extractSubscriptionReference(Subscribe subscribe)
    {
        String subscriptionReference = null;
        if(subscribe != null)
        {
            // TODO: Finish
        }
        return subscriptionReference;
    }
}
