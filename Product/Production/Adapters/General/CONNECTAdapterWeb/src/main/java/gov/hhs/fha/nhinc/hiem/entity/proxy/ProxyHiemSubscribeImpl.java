/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.entity.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxyNotificationProducerSecured;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxyNotificationProducerSecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.SubscribeRequestSecuredType;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
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

/**
 *
 * @author dunnek
 */
public class ProxyHiemSubscribeImpl
{

    private static NhincProxyNotificationProducerSecured service = new NhincProxyNotificationProducerSecured();
    //private static NhincProxyNotificationProducerSecured service;
    private static Log log = LogFactory.getLog(ProxyHiemSubscribeImpl.class);

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonproxy.SubscribeRequestType request) throws InvalidMessageContentExpressionFault, TopicExpressionDialectUnknownFault, InvalidProducerPropertiesExpressionFault, InvalidFilterFault, UnrecognizedPolicyRequestFault, TopicNotSupportedFault, UnsupportedPolicyRequestFault, InvalidTopicExpressionFault, ResourceUnknownFault, UnacceptableInitialTerminationTimeFault, SubscribeCreationFailedFault, NotifyMessageNotSupportedFault
    {
        SubscribeResponse result = null;

        log.debug("Begin Proxy Subscribe");

        String url = getURL();
        NhincProxyNotificationProducerSecuredPortType port = getPort(url);

        AssertionType assertIn = request.getAssertion();

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED);
        ((BindingProvider) port).getRequestContext().putAll(requestContext);

        SubscribeRequestSecuredType securedRequest = new SubscribeRequestSecuredType();

        securedRequest.setSubscribe(request.getSubscribe());
        securedRequest.setNhinTargetSystem(request.getNhinTargetSystem());
        result = port.subscribe(securedRequest);

        return result;
    }

    private String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.HIEM_SUBSCRIBE_PROXY_SERVICE_NAME_SECURED);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    private NhincProxyNotificationProducerSecuredPortType getPort(String url)
    {
        NhincProxyNotificationProducerSecuredPortType port = service.getNhincProxyNotificationProducerPortSoap();

        log.info("Setting endpoint address to Proxy Subscribe Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
