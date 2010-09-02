/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.entity.subscribe;

import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidFilterFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidMessageContentExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidProducerPropertiesExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidTopicExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.NotifyMessageNotSupportedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.SubscribeCreationFailedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.TopicExpressionDialectUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.TopicNotSupportedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnacceptableInitialTerminationTimeFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnrecognizedPolicyRequestFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnsupportedPolicyRequestFault;
import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityNotificationProducerSecured", portName = "EntityNotificationProducerSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntityNotificationProducerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubscriptionmanagementsecured", wsdlLocation = "WEB-INF/wsdl/EntitySubscribeSecuredService/EntitySubscriptionManagementSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntitySubscribeSoapHeaderHandler.xml")
public class EntitySubscribeSecuredService
{
    @Resource
    private WebServiceContext context;

    public gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentResponseType subscribeDocument(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentRequestSecuredType subscribeDocumentRequestSecured)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestSecuredType subscribeRequestSecured) throws UnrecognizedPolicyRequestFault, InvalidProducerPropertiesExpressionFault, ResourceUnknownFault, NotifyMessageNotSupportedFault, TopicNotSupportedFault, InvalidTopicExpressionFault, SubscribeCreationFailedFault, UnsupportedPolicyRequestFault, UnacceptableInitialTerminationTimeFault, InvalidMessageContentExpressionFault, InvalidFilterFault, TopicExpressionDialectUnknownFault
    {
		return new EntitySubscribeServiceImpl().subscribe(subscribeRequestSecured, context);
    }

}
