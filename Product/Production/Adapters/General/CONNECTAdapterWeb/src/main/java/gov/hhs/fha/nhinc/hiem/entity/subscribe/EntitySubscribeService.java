/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.entity.subscribe;

import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidFilterFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidMessageContentExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidProducerPropertiesExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidTopicExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.NotifyMessageNotSupportedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.SubscribeCreationFailedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.TopicExpressionDialectUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.TopicNotSupportedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnacceptableInitialTerminationTimeFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnrecognizedPolicyRequestFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnsupportedPolicyRequestFault;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityNotificationProducer", portName = "EntityNotificationProducerPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntityNotificationProducerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/EntitySubscribeService/EntitySubscriptionManagement.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntitySubscribeService {

    @Resource
    private WebServiceContext context;

    public gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentResponseType subscribeDocument(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentRequestType subscribeDocumentRequest) {
        return new EntitySubscribeServiceImpl().subscribeDocument(subscribeDocumentRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeCdcBioPackageResponseType subscribeCdcBioPackage(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeCdcBioPackageRequestType subscribeCdcBioPackageRequest) {
        return new EntitySubscribeServiceImpl().subscribeCdcBioPackage(subscribeCdcBioPackageRequest);
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestType subscribeRequest) throws UnrecognizedPolicyRequestFault, InvalidMessageContentExpressionFault, UnsupportedPolicyRequestFault, TopicNotSupportedFault, ResourceUnknownFault, NotifyMessageNotSupportedFault, InvalidTopicExpressionFault, InvalidFilterFault, SubscribeCreationFailedFault, InvalidProducerPropertiesExpressionFault, TopicExpressionDialectUnknownFault, UnacceptableInitialTerminationTimeFault {
        return new EntitySubscribeServiceImpl().subscribe(subscribeRequest, context);
    }

}
