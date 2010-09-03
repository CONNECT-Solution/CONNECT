/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.subjectdiscovery;

import gov.hhs.fha.nhinc.adaptersubjectdiscoverysecured.AdapterSubjectDiscoverySecured;
import gov.hhs.fha.nhinc.adaptersubjectdiscoverysecured.AdapterSubjectDiscoverySecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201304UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType;

/**
 *
 * @author jhoppesc
 */
public class AdapterSubjectDiscoveryImpl {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterSubjectDiscoveryImpl.class);
    private static AdapterSubjectDiscoverySecured service = new AdapterSubjectDiscoverySecured();


    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PIXConsumerPRPAIN201301UVRequestType pixConsumerPRPAIN201301UVRequest) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        try
        {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME);
            AdapterSubjectDiscoverySecuredPortType port = getPort(url);

            AssertionType assertIn = pixConsumerPRPAIN201301UVRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            ack = port.pixConsumerPRPAIN201301UV(pixConsumerPRPAIN201301UVRequest);
        }
        catch (Exception ex)
        {
            log.error("Error calling adapter subject discovery secured service: " + ex.getMessage(), ex);
        }

        return ack;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201302UV(PIXConsumerPRPAIN201302UVRequestType pixConsumerPRPAIN201302UVRequest) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

                try
        {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME);
            AdapterSubjectDiscoverySecuredPortType port = getPort(url);

            AssertionType assertIn = pixConsumerPRPAIN201302UVRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            com.services.nhinc.schema.auditmessage.FindAuditEventsType request = new com.services.nhinc.schema.auditmessage.FindAuditEventsType();

            ack = port.pixConsumerPRPAIN201302UV(pixConsumerPRPAIN201302UVRequest);
        }
        catch (Exception ex)
        {
            log.error("Error calling adapter subject discovery secured service: " + ex.getMessage(), ex);
        }

        return ack;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201304UV(PIXConsumerPRPAIN201304UVRequestType pixConsumerPRPAIN201304UVRequest) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        return ack;
    }

    public PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(PIXConsumerPRPAIN201309UVRequestType pixConsumerPRPAIN201309UVRequest) {
        PIXConsumerPRPAIN201309UVResponseType response = new PIXConsumerPRPAIN201309UVResponseType();

                try
        {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME);
            AdapterSubjectDiscoverySecuredPortType port = getPort(url);

            AssertionType assertIn = pixConsumerPRPAIN201309UVRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            com.services.nhinc.schema.auditmessage.FindAuditEventsType request = new com.services.nhinc.schema.auditmessage.FindAuditEventsType();

            response = port.pixConsumerPRPAIN201309UV(pixConsumerPRPAIN201309UVRequest);
        }
        catch (Exception ex)
        {
            log.error("Error calling adapter subject discovery secured service: " + ex.getMessage(), ex);
        }

        return response;
    }

    private AdapterSubjectDiscoverySecuredPortType  getPort(String url) {
        AdapterSubjectDiscoverySecuredPortType  port = service.getAdapterSubjectDiscoverySecuredPortSoap();

        log.info("Setting endpoint address to Adapter Audit Query Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
