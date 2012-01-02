/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.nhinhiem.proxy.unsubscribe.NhinHiemUnsubscribeProxy;
import gov.hhs.fha.nhinc.nhinhiem.proxy.unsubscribe.NhinHiemUnsubscribeProxyObjectFactory;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.w3c.dom.Element;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;

/**
 *
 * @author rayj
 */
public class ProxyHiemUnsubscribeImpl
{

    private static Log log = LogFactory.getLog(ProxyHiemUnsubscribeImpl.class);

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(gov.hhs.fha.nhinc.common.nhinccommonproxy.UnsubscribeRequestType unsubscribeRequest, WebServiceContext context) throws UnableToDestroySubscriptionFault, ResourceUnknownFault
    {
        log.debug("Entering ProxyHiemUnsubscribeImpl.unsubscribe...");

        SoapUtil soaputil = new SoapUtil();

        log.debug("extracting unsubscribe");
        Unsubscribe unsubscribe = unsubscribeRequest.getUnsubscribe();
        WsntUnsubscribeMarshaller wsntUnsubscribeMarshaller = new WsntUnsubscribeMarshaller();
        Element unsubscribeElement = wsntUnsubscribeMarshaller.marshal(unsubscribe);
        //Element unsubscribeElement = soaputil.extractFirstElement(context, "unsubscribeSoapMessage", "Unsubscribe");
        log.debug(XmlUtility.formatElementForLogging("unsubscribe", unsubscribeElement));

        log.debug("extracting target");
        NhinTargetSystemType target = unsubscribeRequest.getNhinTargetSystem();
        log.debug("extracted target");

        log.debug("extracting assertion");
        AssertionType assertion = unsubscribeRequest.getAssertion();
        log.debug("extracted assertion");

        log.debug("extracting consumer reference elements");
        ReferenceParametersHelper consumerReferenceHelper = new ReferenceParametersHelper();
        ReferenceParametersElements consumerReferenceElements = consumerReferenceHelper.createReferenceParameterElements(context, "unsubscribeSoapMessage");
        log.debug("extracted consumer reference elements");

        UnsubscribeResponse response = null;
        NhinHiemUnsubscribeProxyObjectFactory factory = new NhinHiemUnsubscribeProxyObjectFactory();
        NhinHiemUnsubscribeProxy proxy = factory.getNhinHiemSubscribeProxy();
        try
        {
            log.debug("invoke unsubscribe nhin component proxy");
            Element responseElement = proxy.unsubscribe(unsubscribeElement, consumerReferenceElements, assertion, target);
            log.debug("invoked unsubscribe nhin component proxy");
            log.debug("unmarshall unsubscribe response to object");
            WsntUnsubscribeResponseMarshaller marshaller = new WsntUnsubscribeResponseMarshaller();
            response = marshaller.unmarshal(responseElement);
            log.debug("unmarshalled unsubscribe response to object");
        }
        catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex)
        {
            log.error("error occurred", ex);
            //todo: throw proper exception
            response = new UnsubscribeResponse();
            response.getAny().add(ex);
        }
        catch (org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault ex)
        {
            log.error("error occurred", ex);
            //todo: throw proper exception
            response = new UnsubscribeResponse();
            response.getAny().add(ex);
        }

        log.debug("Exiting ProxyHiemUnsubscribeImpl.unsubscribe...");
        return response;
    }

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(gov.hhs.fha.nhinc.common.nhinccommonproxy.UnsubscribeRequestSecuredType unsubscribeRequest, WebServiceContext context) throws UnableToDestroySubscriptionFault, ResourceUnknownFault
    {
        log.debug("Entering ProxyHiemUnsubscribeImpl.unsubscribe...");

        SoapUtil soaputil = new SoapUtil();

        log.debug("extracting unsubscribe");
        Unsubscribe unsubscribe = unsubscribeRequest.getUnsubscribe();
        WsntUnsubscribeMarshaller wsntUnsubscribeMarshaller = new WsntUnsubscribeMarshaller();
        Element unsubscribeElement = wsntUnsubscribeMarshaller.marshal(unsubscribe);
        //Element unsubscribeElement = soaputil.extractFirstElement(context, "unsubscribeSoapMessage", "Unsubscribe");
        log.debug(XmlUtility.formatElementForLogging("unsubscribe", unsubscribeElement));

        log.debug("extracting target");
        NhinTargetSystemType target = unsubscribeRequest.getNhinTargetSystem();
        log.debug("extracted target");

        log.debug("extracting assertion");
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        log.debug("extracted assertion");

        log.debug("extracting consumer reference elements");
        ReferenceParametersHelper consumerReferenceHelper = new ReferenceParametersHelper();
        ReferenceParametersElements consumerReferenceElements = consumerReferenceHelper.createReferenceParameterElements(context, "unsubscribeSoapMessage");
        log.debug("extracted consumer reference elements");

        UnsubscribeResponse response = null;
        NhinHiemUnsubscribeProxyObjectFactory factory = new NhinHiemUnsubscribeProxyObjectFactory();
        NhinHiemUnsubscribeProxy proxy = factory.getNhinHiemSubscribeProxy();
        try
        {
            log.debug("invoke unsubscribe nhin component proxy");
            Element responseElement = proxy.unsubscribe(unsubscribeElement, consumerReferenceElements, assertion, target);
            log.debug("invoked unsubscribe nhin component proxy");
            log.debug("unmarshall unsubscribe response to object");
            WsntUnsubscribeResponseMarshaller marshaller = new WsntUnsubscribeResponseMarshaller();
            response = marshaller.unmarshal(responseElement);
            log.debug("unmarshalled unsubscribe response to object");
        }
        catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex)
        {
            log.error("error occurred", ex);
            //todo: throw proper exception
            response = new UnsubscribeResponse();
            response.getAny().add(ex);
        }
        catch (org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault ex)
        {
            log.error("error occurred", ex);
            //todo: throw proper exception
            response = new UnsubscribeResponse();
            response.getAny().add(ex);
        }

        log.debug("Exiting ProxyHiemUnsubscribeImpl.unsubscribe...");
        return response;
    }
}
