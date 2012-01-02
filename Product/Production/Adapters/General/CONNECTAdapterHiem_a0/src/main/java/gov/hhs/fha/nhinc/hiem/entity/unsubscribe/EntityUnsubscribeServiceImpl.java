/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.entity.unsubscribe;

import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import com.sun.xml.ws.developer.WSBindingProvider;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.UnsubscribeRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntitySubscriptionManagerSecured;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntitySubscriptionManagerSecuredPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author dunnek
 */
public class EntityUnsubscribeServiceImpl
{

    private static Log log = LogFactory.getLog(EntityUnsubscribeServiceImpl.class);
    private static EntitySubscriptionManagerSecured service = new EntitySubscriptionManagerSecured();

    public UnsubscribeResponse unsubscribe(UnsubscribeRequestType unsubscribeRequest, WebServiceContext context) throws gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault, gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault, ResourceUnknownFault, UnableToDestroySubscriptionFault
    {
        log.debug("begin unsubscribe");
        UnsubscribeResponse result = null;

        String url = getURL();
        EntitySubscriptionManagerSecuredPortType port = getPort(url);

        AssertionType assertIn = unsubscribeRequest.getAssertion();

        log.debug("extracting reference parameters from soap header");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
        log.debug("extracted reference parameters from soap header");

        SoapUtil soapUtil = new SoapUtil();
        soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED);
        ((BindingProvider) port).getRequestContext().putAll(requestContext);

        try
        {
            result = port.unsubscribe(unsubscribeRequest.getUnsubscribe());
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.ResourceUnknownFault ex)
        {
            throw new ResourceUnknownFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnableToDestroySubscriptionFault ex)
        {
            throw new UnableToDestroySubscriptionFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }

        log.debug("end proxy unsubscribe");
        return result;
    }

    private String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.HIEM_UNSUBSCRIBE_ENTITY_SERVICE_NAME_SECURED);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    private EntitySubscriptionManagerSecuredPortType getPort(String url)
    {
        EntitySubscriptionManagerSecuredPortType port = service.getEntitySubscriptionManagerSecuredPortSoap();

        log.info("Setting endpoint address to Entity SubscriptionManager Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
