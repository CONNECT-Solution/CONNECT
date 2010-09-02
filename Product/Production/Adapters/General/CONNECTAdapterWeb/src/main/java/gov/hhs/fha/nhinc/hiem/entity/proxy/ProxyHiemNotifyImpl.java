/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.hiem.dte.SoapUtil; 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.nhinhiem.proxy.notify.NhinHiemNotifyProxy;
import gov.hhs.fha.nhinc.nhinhiem.proxy.notify.NhinHiemNotifyProxyObjectFactory;
import javax.xml.ws.WebServiceContext;
import org.w3c.dom.Element;

/**
 *
 * @author dunnek
 */
public class ProxyHiemNotifyImpl
{

    private static Log log = LogFactory.getLog(ProxyHiemNotifyImpl.class);
//    private static NhincProxyNotificationConsumerSecured service = new NhincProxyNotificationConsumerSecured();

    public void notify(gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestType request, WebServiceContext context)
    {
        log.debug("Begin Proxy Notify");

        try
        {
//            String url = getURL();
//            NhincProxyNotificationConsumerSecuredPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();

            log.debug("extracting reference parameters from soap header");
            ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
            ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
            log.debug("extracted reference parameters from soap header");

//            SamlTokenCreator tokenCreator = new SamlTokenCreator();
//            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED);
//            ((BindingProvider) port).getRequestContext().putAll(requestContext);

//            NotifyRequestSecuredType securedRequest = new NotifyRequestSecuredType();
//
//            securedRequest.setNotify(request.getNotify());
//            securedRequest.setNhinTargetSystem(request.getNhinTargetSystem());

//            log.debug("##### About to call checkPolicy");
//            checkPolicy(request.getNotify(), request.getAssertion());
//            log.debug("Sending notify message to secured interface");
//            port.notify(securedRequest);

//            log.debug("Marshalling notify message");
//            WsntSubscribeMarshaller notifyMarshaller = new WsntSubscribeMarshaller();
//            Element notifyElement = notifyMarshaller.marshalNotifyRequest(request.getNotify());
            
            Element notifyElement = new SoapUtil().extractFirstElement(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE, "Notify");

            NhinHiemNotifyProxyObjectFactory hiemNotifyFactory = new NhinHiemNotifyProxyObjectFactory();
            NhinHiemNotifyProxy proxy = hiemNotifyFactory.getNhinHiemNotifyProxy();

            log.debug("Sending notify message to NHIN Proxy");
            proxy.notify(notifyElement, referenceParametersElements, assertIn, request.getNhinTargetSystem());

        }
        catch(Throwable t)
        {
            log.error("Error processing notify message: " + t.getMessage(), t);
            // TODO: Handle this error appropriately.
        }

    }

//    private String getURL()
//    {
//        String url = "";
//
//        try
//        {
//            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.HIEM_NOTIFY_PROXY_SERVICE_NAME_SECURED);
//        }
//        catch (Exception ex)
//        {
//            log.error(ex.getMessage(), ex);
//        }
//
//        return url;
//    }
//
//    private NhincProxyNotificationConsumerSecuredPortType getPort(String url)
//    {
//        NhincProxyNotificationConsumerSecuredPortType port = service.getNhincProxyNotificationConsumerPortSoap11();
//
//        log.info("Setting endpoint address to Proxy Unsubscribe Secured Service to " + url);
//        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
//
//        return port;
//    }

//    private boolean checkPolicy(Notify notify, AssertionType assertion) {
//        log.debug("In NhinHiemNotifyWebServiceProxy.checkPolicy");
//        boolean policyIsValid = false;
//
//        NotifyEventType policyCheckReq = new NotifyEventType();
//        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
//        gov.hhs.fha.nhinc.common.eventcommon.NotifyMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.NotifyMessageType();
//        request.setAssertion(assertion);
//        request.setNotify(notify);
//        policyCheckReq.setMessage(request);
//
//        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
//        CheckPolicyRequestType policyReq = policyChecker.checkPolicyNotify(policyCheckReq);
//        policyReq.setAssertion(assertion);
//        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
//        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
//
//        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq);
//
//        if (policyResp.getResponse() != null &&
//                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
//                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
//            policyIsValid = true;
//        }
//
//        log.debug("Finished NhinHiemNotifyWebServiceProxy.checkPolicy - valid: " + policyIsValid);
//        return policyIsValid;
//    }

}
