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
package gov.hhs.fha.nhinc.hiem._20.subscription.repository;

import gov.hhs.fha.nhinc.subscription.repository.SubscriptionRepositoryException;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Sai Valluripalli
 */
@WebService(endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentsubscriptionrepository.NhincComponentSubscriptionRepositoryPortType")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class GatewaySubscriptionRepository {
    private static Log log = LogFactory.getLog(GatewaySubscriptionRepository.class);

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType storeSubscription(
            gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType subscriptionItem) {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType subscriptionReference = null;
        try {
            subscriptionReference = new SubscriptionRepositoryHelper().storeSubscription(subscriptionItem);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            // Return empty reference until fault handling is implemented
            subscriptionReference = new gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType();
        }
        return subscriptionReference;
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType deleteSubscription(
            gov.hhs.fha.nhinc.common.subscription.DeleteSubscriptionMessageRequestType deleteSubscriptionMessage) {
        gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType ack = null;
        try {
            if (deleteSubscriptionMessage != null) {
                ack = new SubscriptionRepositoryHelper().deleteSubscription(deleteSubscriptionMessage
                        .getSubscriptionReference());
            }
        } catch (SubscriptionRepositoryException ex) {
            log.error(ex.getMessage(), ex);
            // Create an ack here until proper fault handling is established.
            ack = new gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType();
        }
        return ack;
    }

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType retrieveByCriteria(
            gov.hhs.fha.nhinc.common.subscription.SubscriptionCriteriaType subscriptionCriteria) {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType subscriptionItems = null;
        try {
            subscriptionItems = new SubscriptionRepositoryHelper().retrieveByCriteria(subscriptionCriteria);
        } catch (SubscriptionRepositoryException ex) {
            log.error(ex.getMessage(), ex);
            // Create empty response until fault handling is added
            subscriptionItems = new gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType();
        }
        return subscriptionItems;
    }

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType retrieveBySubscriptionReference(
            gov.hhs.fha.nhinc.common.subscription.RetrieveBySubscriptionReferenceRequestMessageType retrieveBySubscriptionReferenceRequest) {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType subscriptionItem = null;
        try {
            if (retrieveBySubscriptionReferenceRequest != null) {
                subscriptionItem = new SubscriptionRepositoryHelper()
                        .retrieveBySubscriptionReference(retrieveBySubscriptionReferenceRequest
                                .getSubscriptionReference());
            }
        } catch (SubscriptionRepositoryException ex) {
            log.error(ex.getMessage(), ex);
            // Create empty response until fault handling is added
            subscriptionItem = new gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType();
        }
        return subscriptionItem;
    }

}
