/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subscription.repository;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincComponentSubscriptionRepositoryService", portName = "NhincComponentSubscriptionRepositoryPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentsubscriptionrepository.NhincComponentSubscriptionRepositoryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentsubscriptionrepository", wsdlLocation = "WEB-INF/wsdl/GatewaySubscriptionRepository/NhincComponentSubscriptionRepository.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class GatewaySubscriptionRepository
{
    private static Log log = LogFactory.getLog(GatewaySubscriptionRepository.class);

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType storeSubscription(gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType subscriptionItem)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType subscriptionReference = null;
        try
        {
            subscriptionReference = new SubscriptionRepositoryHelper().storeSubscription(subscriptionItem);
        }
        catch(Throwable t)
        {
            log.error(t.getMessage(), t);
            // Return empty reference until fault handling is implemented
            subscriptionReference = new gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType();
        }
        return subscriptionReference;
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType deleteSubscription(gov.hhs.fha.nhinc.common.subscription.DeleteSubscriptionMessageRequestType deleteSubscriptionMessage)
    {
        gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType ack = null;
        try
        {
            if(deleteSubscriptionMessage != null)
            {
                ack = new SubscriptionRepositoryHelper().deleteSubscription(deleteSubscriptionMessage.getSubscriptionReference());
            }
        }
        catch (SubscriptionRepositoryException ex)
        {
            log.error(ex.getMessage(), ex);
            // Create an ack here until proper fault handling is established.
            ack = new gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType();
        }
        return ack;
    }

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType retrieveByCriteria(gov.hhs.fha.nhinc.common.subscription.SubscriptionCriteriaType subscriptionCriteria)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType subscriptionItems = null;
        try
        {
            subscriptionItems = new SubscriptionRepositoryHelper().retrieveByCriteria(subscriptionCriteria);
        }
        catch (SubscriptionRepositoryException ex)
        {
            log.error(ex.getMessage(), ex);
            // Create empty response until fault handling is added
            subscriptionItems = new gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType();
        }
        return subscriptionItems;
    }

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType retrieveBySubscriptionReference(gov.hhs.fha.nhinc.common.subscription.RetrieveBySubscriptionReferenceRequestMessageType retrieveBySubscriptionReferenceRequest)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType subscriptionItem = null;
        try
        {
            if(retrieveBySubscriptionReferenceRequest != null)
            {
                subscriptionItem = new SubscriptionRepositoryHelper().retrieveBySubscriptionReference(retrieveBySubscriptionReferenceRequest.getSubscriptionReference());
            }
        }
        catch (SubscriptionRepositoryException ex)
        {
            log.error(ex.getMessage(), ex);
            // Create empty response until fault handling is added
            subscriptionItem = new gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType();
        }
        return subscriptionItem;
    }

}
