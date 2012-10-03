/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.subscription.repository.data;

import java.util.Date;

/**
 * Data class for a subscription record.
 * 
 * @author Neil Webb
 */
public class HiemSubscriptionItem {
    private String subscribeXML;
    private String subscriptionReferenceXML;
    private String rootTopic;
    private String parentSubscriptionReferenceXML;
    private String consumer;
    private String producer;
    private String targets;
    private Date creationDate;
    private SubscriptionStorageItem storageObject; // todo: is this a good idea.. can we achieve this another way? can
                                                   // we "hide" it?

    public String getSubscribeXML() {
        return subscribeXML;
    }

    public void setSubscribeXML(String subscribeXML) {
        this.subscribeXML = subscribeXML;
    }

    public String getSubscriptionReferenceXML() {
        return subscriptionReferenceXML;
    }

    public void setSubscriptionReferenceXML(String subscriptionReferenceXML) {
        this.subscriptionReferenceXML = subscriptionReferenceXML;
    }

    public String getRootTopic() {
        return rootTopic;
    }

    public void setRootTopic(String rootTopic) {
        this.rootTopic = rootTopic;
    }

    public String getParentSubscriptionReferenceXML() {
        return parentSubscriptionReferenceXML;
    }

    public void setParentSubscriptionReferenceXML(String parentSubscriptionReferenceXML) {
        this.parentSubscriptionReferenceXML = parentSubscriptionReferenceXML;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getTargets() {
        return targets;
    }

    public void setTargets(String targets) {
        this.targets = targets;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
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
