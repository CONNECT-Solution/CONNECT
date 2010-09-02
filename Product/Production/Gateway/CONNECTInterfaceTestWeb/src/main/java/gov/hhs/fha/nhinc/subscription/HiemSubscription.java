/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subscription;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.oasis_open.docs.wsn.bw_2.InvalidFilterFault;
import org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault;
import org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault;
import org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NotificationProducerService", portName = "NotificationProducerPort", endpointInterface = "org.oasis_open.docs.wsn.bw_2.NotificationProducer", targetNamespace = "http://docs.oasis-open.org/wsn/bw-2", wsdlLocation = "WEB-INF/wsdl/HiemNotify/NhinSubscription.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class HiemSubscription
{
    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(org.oasis_open.docs.wsn.b_2.Subscribe subscribeRequest) throws InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, InvalidMessageContentExpressionFault, ResourceUnknownFault, TopicExpressionDialectUnknownFault, SubscribeCreationFailedFault, NotifyMessageNotSupportedFault, InvalidFilterFault, UnacceptableInitialTerminationTimeFault, TopicNotSupportedFault, UnsupportedPolicyRequestFault, UnrecognizedPolicyRequestFault
    {
        return HiemSubscriptionImpl.subscribe(subscribeRequest, context);
    }

}
