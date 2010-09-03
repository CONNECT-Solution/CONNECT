/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.entitysubjectdiscovery.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.entitysubjectdiscoverysecured.EntitySubjectDiscoverySecured;
import gov.hhs.fha.nhinc.entitysubjectdiscoverysecured.EntitySubjectDiscoverySecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201301UVSecuredRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVSecuredRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201304UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201304UVSecuredRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVSecuredRequestType;

public class EntitySubjectDiscoveryWebServiceProxy implements EntitySubjectDiscoveryProxy {

    private static Log log = LogFactory.getLog(EntitySubjectDiscoveryWebServiceProxy.class);
    static EntitySubjectDiscoverySecured entityService = null;
    private String endpointURL;
    private static String ENTITY_SUBJECT_DISCOVERY_SERVICE_NAME = "entitysubjectdiscoverysecured";
    private static String ENTITY_SUBJECT_DISCOVERY_SERVICE_URL = "https://localhost:8181/NhinConnect/EntitySubjectDiscoverySecured";

    private EntitySubjectDiscoverySecuredPortType getEntitySubjectDiscoveryPort(AssertionType assertIn) throws ConnectionManagerException {
        if (entityService == null) {
            entityService = new EntitySubjectDiscoverySecured();
        }
        EntitySubjectDiscoverySecuredPortType entitySecuredPort = entityService.getEntitySubjectDiscoverySecuredPortSoap();
        try {
            // Get the real endpoint URL for this service.
            endpointURL = ConnectionManagerCache.getLocalEndpointURLByServiceName(ENTITY_SUBJECT_DISCOVERY_SERVICE_NAME);

            if ((endpointURL == null) ||
                    (endpointURL.length() <= 0)) {
                endpointURL = ENTITY_SUBJECT_DISCOVERY_SERVICE_URL;
            }
            log.info("Setting endpoint address to Entity Subject Discovery Service to " + endpointURL);
            ((BindingProvider) entitySecuredPort).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map samlMap = tokenCreator.CreateRequestContext(assertIn, endpointURL, NhincConstants.SUBJECT_DISCOVERY_ACTION);

            Map requestContext = ((BindingProvider) entitySecuredPort).getRequestContext();
            requestContext.putAll(samlMap);
        } catch (Exception ex) {
            String message = "Failed to retrieve a handle to the Entity Subject Discovery web service.  Error: " +
                    ex.getMessage();
            log.error(message, ex);
            throw new ConnectionManagerException();
        }

        return entitySecuredPort;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PIXConsumerPRPAIN201301UVRequestType request) {

        MCCIIN000002UV01 response = new MCCIIN000002UV01();
        try {
            EntitySubjectDiscoverySecuredPortType entitySecuredPort = getEntitySubjectDiscoveryPort(request.getAssertion());

            PIXConsumerPRPAIN201301UVSecuredRequestType securedReq = new PIXConsumerPRPAIN201301UVSecuredRequestType();
            securedReq.setNhinTargetCommunities(request.getNhinTargetCommunities());
            securedReq.setPRPAIN201301UV02(request.getPRPAIN201301UV02());
            response = entitySecuredPort.pixConsumerPRPAIN201301UV(securedReq);

        } catch (Exception ex) {
            String message = "Error occurred calling EntitySubjectDiscoveryWebServiceProxy.pixConsumerPRPAIN201301UV.  Error: " +
                    ex.getMessage();
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }
        return response;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201302UV(PIXConsumerPRPAIN201302UVRequestType request) {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();
        try {
            EntitySubjectDiscoverySecuredPortType entitySecuredPort = getEntitySubjectDiscoveryPort(request.getAssertion());

            PIXConsumerPRPAIN201302UVSecuredRequestType securedReq = new PIXConsumerPRPAIN201302UVSecuredRequestType();
            securedReq.setNhinTargetCommunities(request.getNhinTargetCommunities());
            securedReq.setPRPAIN201302UV02(request.getPRPAIN201302UV02());
            response = entitySecuredPort.pixConsumerPRPAIN201302UV(securedReq);

        } catch (Exception ex) {
            String message = "Error occurred calling EntitySubjectDiscoveryWebServiceProxy.pixConsumerPRPAIN201302UV.  Error: " +
                    ex.getMessage();
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }
        return response;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201304UV(PIXConsumerPRPAIN201304UVRequestType request) {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();
        try {
            EntitySubjectDiscoverySecuredPortType entitySecuredPort = getEntitySubjectDiscoveryPort(request.getAssertion());

            PIXConsumerPRPAIN201304UVSecuredRequestType securedReq = new PIXConsumerPRPAIN201304UVSecuredRequestType();
            securedReq.setNhinTargetCommunities(request.getNhinTargetCommunities());
            securedReq.setPRPAIN201304UV02(request.getPRPAIN201304UV02());
            response = entitySecuredPort.pixConsumerPRPAIN201304UV(securedReq);

        } catch (Exception ex) {
            String message = "Error occurred calling EntitySubjectDiscoveryWebServiceProxy.pixConsumerPRPAIN201304UV.  Error: " +
                    ex.getMessage();
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }
        return response;
    }

    public PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(PIXConsumerPRPAIN201309UVRequestType request) {
        PIXConsumerPRPAIN201309UVResponseType response = new PIXConsumerPRPAIN201309UVResponseType();
        try {
            EntitySubjectDiscoverySecuredPortType entitySecuredPort = getEntitySubjectDiscoveryPort(request.getAssertion());

            PIXConsumerPRPAIN201309UVSecuredRequestType securedReq = new PIXConsumerPRPAIN201309UVSecuredRequestType();
            securedReq.setNhinTargetCommunities(request.getNhinTargetCommunities());
            securedReq.setPRPAIN201309UV02(request.getPRPAIN201309UV02());
            response = entitySecuredPort.pixConsumerPRPAIN201309UV(securedReq);

        } catch (Exception ex) {
            String message = "Error occurred calling EntitySubjectDiscoveryWebServiceProxy.pixConsumerPRPAIN201309UV.  Error: " +
                    ex.getMessage();
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }
        return response;
    }
}
