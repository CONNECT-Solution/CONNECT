/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subscription.repository.data;

import java.util.Date;

/**
 * Data class for a subscription record.
 * 
 * @author Neil Webb
 */
public class HiemSubscriptionItem
{
    private String subscribeXML;
    private String subscriptionReferenceXML;
    private String rootTopic;
    private String parentSubscriptionReferenceXML;
    private String consumer;
    private String producer;
    private String targets;
    private Date creationDate;
    private SubscriptionStorageItem storageObject ;  //todo: is this a good idea.. can we achieve this another way?  can we "hide" it?

    public String getSubscribeXML()
    {
        return subscribeXML;
    }

    public void setSubscribeXML(String subscribeXML)
    {
        this.subscribeXML = subscribeXML;
    }

    public String getSubscriptionReferenceXML()
    {
        return subscriptionReferenceXML;
    }

    public void setSubscriptionReferenceXML(String subscriptionReferenceXML)
    {
        this.subscriptionReferenceXML = subscriptionReferenceXML;
    }

    public String getRootTopic()
    {
        return rootTopic;
    }

    public void setRootTopic(String rootTopic)
    {
        this.rootTopic = rootTopic;
    }

    public String getParentSubscriptionReferenceXML()
    {
        return parentSubscriptionReferenceXML;
    }

    public void setParentSubscriptionReferenceXML(String parentSubscriptionReferenceXML)
    {
        this.parentSubscriptionReferenceXML = parentSubscriptionReferenceXML;
    }

    public String getConsumer()
    {
        return consumer;
    }

    public void setConsumer(String consumer)
    {
        this.consumer = consumer;
    }

    public String getProducer()
    {
        return producer;
    }

    public void setProducer(String producer)
    {
        this.producer = producer;
    }

    public String getTargets()
    {
        return targets;
    }

    public void setTargets(String targets)
    {
        this.targets = targets;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    /**
     * @return the storageObject
     */
    public SubscriptionStorageItem getStorageObject() {
        return storageObject;
    }

    /**
     * @param storageObject the storageObject to set
     */
    public void setStorageObject(SubscriptionStorageItem storageObject) {
        this.storageObject = storageObject;
    }

}
