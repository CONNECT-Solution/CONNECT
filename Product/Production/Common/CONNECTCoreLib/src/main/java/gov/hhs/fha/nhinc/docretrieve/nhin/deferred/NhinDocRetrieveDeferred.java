/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.nhin.deferred;

import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredPolicyChecker;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by
 * User: ralph
 * Date: Aug 6, 2010
 * Time: 3:25:58 PM
 */
public class NhinDocRetrieveDeferred {

    private static Log log = LogFactory.getLog(NhinDocRetrieveDeferred.class);

    /**
     * Creates an audit log message and writes it to the audit log.
     *
     * @param auditLogMsg  the message to write.
     * @param assertion  assertion details associated with the message.
     * @return AcknowledgementType
     */
    protected AcknowledgementType auditMessage(LogEventRequestType auditLogMsg, AssertionType assertion) {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();

        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();

        return proxy.auditLog(auditLogMsg, assertion);
    }

    /**
     * Checks to see if the document retrieve service is enabled.
     *
     * @return True is returned if the service is enabled, false is returned if it is not enabled.
     */
    protected boolean isServiceEnabled(String property) {
        boolean serviceEnabled = false;
        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                                                                 property);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " +
                    property +
                    " from property file " + NhincConstants.GATEWAY_PROPERTY_FILE + ": " + ex.getMessage(), ex);
        }

        return serviceEnabled;
    }

    /**
     * Performs a policy check on the response.
     *
     * @param nhinResponse  The request to be checked.
     * @param assertion Assertion data associated with the response.
     * @param targetCommunity  The community id where the request is being sent.
     * @return  True is the request is allowed and false if it is not.
     */
    protected boolean isPolicyValidForResponse(RetrieveDocumentSetResponseType request,
                                  AssertionType assertion, HomeCommunityType targetCommunity) {

        DocRetrieveDeferredPolicyChecker        policyChecker = new DocRetrieveDeferredPolicyChecker();
        boolean isValid = false;

        isValid = policyChecker.checkOutgoingPolicy(request, assertion, targetCommunity.getHomeCommunityId());

        return isValid;
    }

    /**
     * Performs a policy check on the request.
     *
     * @param oEachNhinRequest  The request to be checked.
     * @param oAssertion Assertion data associated with the request.
     * @param targetCommunity  The community id where the request is being sent.
     * @return  True is the request is allowed and false if it is not.
     */
    protected boolean isPolicyValidForRequest(RetrieveDocumentSetRequestType nhinRequest,
                                  AssertionType assertion, HomeCommunityType targetCommunity) {
        boolean isValid = false;
        DocRetrieveEventType checkPolicy = new DocRetrieveEventType();
        DocRetrieveMessageType checkPolicyMessage = new DocRetrieveMessageType();
        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        CheckPolicyRequestType policyReq;
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp;

        checkPolicyMessage.setRetrieveDocumentSetRequest(nhinRequest);
        checkPolicyMessage.setAssertion(assertion);

        checkPolicy.setMessage(checkPolicyMessage);
        checkPolicy.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        checkPolicy.setInterface(NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        checkPolicy.setReceivingHomeCommunity(targetCommunity);

        policyReq = policyChecker.checkPolicyDocRetrieve(checkPolicy);
        policyResp = policyProxy.checkPolicy(policyReq, assertion);

        /* if response='permit' */
        if (policyResp.getResponse().getResult().get(0).getDecision().value().equalsIgnoreCase(NhincConstants.POLICY_PERMIT)) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Checks to see if the service has been configured for "pass through" mode.
     *
     * @return  True if "pass through" mode is enabled and false if it is not.
     */
    protected boolean isInPassThroughMode(String property) {
        boolean passThroughModeEnabled = false;

        try {
            passThroughModeEnabled =
                    PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                                                        property);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " +
                    property +
                    " from property file " + NhincConstants.GATEWAY_PROPERTY_FILE + ": " + ex.getMessage(), ex);
        }

        return passThroughModeEnabled;
    }

    /**
     * Creates an acknowledgement response that indicates success.
     *
     * @param codeContext
     * @return RetrieveDocumentSetResponseType
     */
    protected DocRetrieveAcknowledgementType createSuccessResponse(String  status) {
        DocRetrieveAcknowledgementType response;
        RegistryResponseType responseType;


        response = new DocRetrieveAcknowledgementType();
        responseType = new RegistryResponseType();
        response.setMessage(responseType);
        responseType.setStatus(status);

        return response;
    }

    /**
     * Creates an acknowledgement response that indicates an error has occurred.
     *
     * @param codeContext
     * @return RetrieveDocumentSetResponseType
     */
    protected DocRetrieveAcknowledgementType createErrorResponse(String codeContext, String  status) {
        DocRetrieveAcknowledgementType response = new DocRetrieveAcknowledgementType();
        RegistryResponseType responseType = new RegistryResponseType();

        response.setMessage(responseType);
        responseType.setStatus(status);


        return response;
    }

}
