/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidFilterFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidMessageContentExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidProducerPropertiesExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidTopicExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NotifyMessageNotSupportedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.SubscribeCreationFailedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.TopicExpressionDialectUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.TopicNotSupportedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnacceptableInitialTerminationTimeFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnrecognizedPolicyRequestFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnsupportedPolicyRequestFault;
import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyNotificationProducerSecured", portName = "NhincProxyNotificationProducerPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxyNotificationProducerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/ProxyHiemSubscribeSecured/NhincProxySubscriptionManagementSecured.wsdl")
@HandlerChain(file = "ProxyHiemSubscribeHeaderHandler.xml")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class ProxyHiemSubscribeSecured
{
    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonproxy.SubscribeRequestSecuredType subscribeRequestSecured) throws InvalidMessageContentExpressionFault, UnacceptableInitialTerminationTimeFault, InvalidTopicExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidFilterFault, NotifyMessageNotSupportedFault, TopicNotSupportedFault, TopicExpressionDialectUnknownFault, ResourceUnknownFault, SubscribeCreationFailedFault, UnsupportedPolicyRequestFault, UnrecognizedPolicyRequestFault
    {
        ProxyHiemSubscribeImpl hiemSubscribeImpl = new ProxyHiemSubscribeImpl();
        try
        {
            return hiemSubscribeImpl.subscribe(subscribeRequestSecured, context);
        }
        catch (org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault ex)
        {
            throw new NotifyMessageNotSupportedFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault ex)
        {
            throw new UnacceptableInitialTerminationTimeFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault ex)
        {
            throw new InvalidTopicExpressionFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault ex)
        {
            throw new UnrecognizedPolicyRequestFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault ex)
        {
            throw new UnsupportedPolicyRequestFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault ex)
        {
            throw new InvalidProducerPropertiesExpressionFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault ex)
        {
            throw new TopicNotSupportedFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault ex)
        {
            throw new SubscribeCreationFailedFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault ex)
        {
            throw new TopicExpressionDialectUnknownFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.InvalidFilterFault ex)
        {
            throw new InvalidFilterFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault ex)
        {
            throw new InvalidMessageContentExpressionFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex)
        {
            throw new ResourceUnknownFault(ex.getMessage(), null);
        }
    }

}
