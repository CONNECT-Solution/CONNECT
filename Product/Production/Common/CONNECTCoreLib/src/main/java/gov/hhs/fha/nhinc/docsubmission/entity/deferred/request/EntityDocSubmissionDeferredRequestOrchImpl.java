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

package gov.hhs.fha.nhinc.docsubmission.entity.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.passthru.deferred.request.proxy.PassthruDocSubmissionDeferredRequestProxy;
import gov.hhs.fha.nhinc.docsubmission.passthru.deferred.request.proxy.PassthruDocSubmissionDeferredRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.lift.file.manager.LiFTFileManager;
import gov.hhs.fha.nhinc.lift.payload.builder.LiFTPayloadBuilder;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class EntityDocSubmissionDeferredRequestOrchImpl {
    private Log log = null;
    private XDRAuditLogger auditLogger = null;

    public EntityDocSubmissionDeferredRequestOrchImpl()
    {
        log = createLogger();
        auditLogger = createAuditLogger();
    }


    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {
        log.info("Begin provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, AssertionType)");
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        response.setMessage(regResp);
        String errMsg = null;
        boolean errorOccurred = false;
        String guid = null;

        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        provideAndRegisterRequestRequest.setNhinTargetCommunities(targets);
        provideAndRegisterRequestRequest.setProvideAndRegisterDocumentSetRequest(request);
        provideAndRegisterRequestRequest.setUrl(urlInfo);

        logRequest(provideAndRegisterRequestRequest , assertion);

        if (provideAndRegisterRequestRequest != null &&
                provideAndRegisterRequestRequest.getNhinTargetCommunities() != null &&
                NullChecker.isNotNullish(provideAndRegisterRequestRequest.getNhinTargetCommunities().getNhinTargetCommunity()) &&
                provideAndRegisterRequestRequest.getNhinTargetCommunities().getNhinTargetCommunity().get(0) != null &&
                provideAndRegisterRequestRequest.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity() != null &&
                NullChecker.isNotNullish(provideAndRegisterRequestRequest.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId())) {

            if (checkPolicy(provideAndRegisterRequestRequest, assertion)) {
                log.info("Policy check successful");

                NhinTargetSystemType targetSystemType = new NhinTargetSystemType();
                targetSystemType.setHomeCommunity(provideAndRegisterRequestRequest.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity());

                // If this is LiFT Message then check to see if the target supports LiFT and if it does
                // insert the LiFT Payload into the request message
                if (isLiftMessage(provideAndRegisterRequestRequest) && checkLiftProperty()) {
                    log.debug("Local Gateway is configured to use LIFT");

                    // Check the connection manager to see if the target community supports LiFT
                    if (doesTargetSupportLift(targetSystemType.getHomeCommunity().getHomeCommunityId())) {
                        log.debug("Target Community supports LIFT");

                        // Lift is supported by both the sides, now generate the payload
                        guid = generateLiFTPayload(provideAndRegisterRequestRequest, assertion);
                        if (guid != null) {
                            // LiFT Payload was successfully generated now copy the file
                            if (copyFileToFileServer(provideAndRegisterRequestRequest.getUrl().getUrl(), guid) == false) {
                                errMsg = "Failed to copy file to the file server";
                                log.error(errMsg);
                                regResp.setStatus(errMsg);
                                errorOccurred = true;
                            }
                        } else {
                            errMsg = "Failed to generate LiFT Payload";
                            log.error(errMsg);
                            regResp.setStatus(errMsg);
                            errorOccurred = true;
                        }
                    } else {
                        errMsg = "Target does not support LiFT, but a LiFT transfer was specified";
                        log.error(errMsg);
                        regResp.setStatus(errMsg);
                        errorOccurred = true;
                    }
                }

                if (errorOccurred == false) {
                    gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType proxyRequest = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
                    proxyRequest.setProvideAndRegisterDocumentSetRequest(provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest());
                    proxyRequest.setNhinTargetSystem(targetSystemType);

                    log.debug("Sending request from entity service to NHIN proxy service");
                    response = callNhinXDRRequestProxy(proxyRequest, assertion);
                }
            } else {
                errMsg = "Policy check unsuccessful";
                log.error(errMsg);
                regResp.setStatus(errMsg);

            }
        }

        logResponse(response, assertion);

        log.info("End provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, AssertionType)");
        return response;
    }

    protected XDRAcknowledgementType callNhinXDRRequestProxy(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, AssertionType assertion)
    {
        log.debug("Begin provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, AssertionType)");

        PassthruDocSubmissionDeferredRequestProxyObjectFactory factory = new PassthruDocSubmissionDeferredRequestProxyObjectFactory();
        PassthruDocSubmissionDeferredRequestProxy proxy = factory.getPassthruDocSubmissionDeferredRequestProxy();

        log.debug("Calling NHIN proxy");
        XDRAcknowledgementType response = proxy.provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest(), assertion, provideAndRegisterRequestRequest.getNhinTargetSystem());

        log.debug("End provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, AssertionType)");
        return response;
    }

    protected void logRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
        log.debug("Begin logRequest");
        auditLogger.auditEntityXDR(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        log.debug("End logRequest");
    }

    protected void logResponse(XDRAcknowledgementType response, AssertionType assertion) {
        log.debug("Beging logResponse");
        auditLogger.auditEntityAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.XDR_REQUEST_ACTION);
        log.debug("End logResponse");
    }

    /**
     * The method will determine if the input message contains a LiFT request
     * @param request  The input message
     * @return  true if the url field of the request message is specified, otherwise will return false
     */
    private boolean isLiftMessage(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {
        boolean result = false;

        // Check to see if a url was provided in the message and if LiFT is supported
        if (request.getUrl() != null &&
                NullChecker.isNotNullish(request.getUrl().getUrl()) &&
                NullChecker.isNotNullish(request.getUrl().getId())) {
            result = true;
        }

        log.debug("isLiftMessage returning: " + result);
        return result;
    }

    /**
     * This method will place the LiFT payload in the Document Submission Request
     * @param request  The message that contains the Document Submission Request
     * @param assertion  The Assertion Object containing assertion information
     * @return  true if the payload was successfully inserted, other false if an error occurs
     */
    protected String generateLiFTPayload(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
        // Call out to the LiFT Payload Builder Library to build the LiFT Payload
        LiFTPayloadBuilder payloadBuilder = new LiFTPayloadBuilder();
        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        urlInfoList.add(request.getUrl());

        return payloadBuilder.buildLiFTPayload(request.getProvideAndRegisterDocumentSetRequest(), assertion, urlInfoList);
    }

    /**
     * This method will copy a file to a destination
     * @param url  URL to the file that will be copied
     * @param destination  Destination in which the file should be copied to
     * @return  true if the copy was successful, otherwise false if an error occurred
     */
    protected boolean copyFileToFileServer(String url, String destination) {
        // Copy the file to the file server
        LiFTFileManager fileManager = new LiFTFileManager();
        return fileManager.copyFile(url, destination);
    }

    /**
     * This method returns the value of the property that determines whether LiFT transfers are enabled or not
     * @return  true if LiFT Transforms are enabled, false if they are not
     */
    protected boolean checkLiftProperty() {
        boolean result = false;

        // Check the property file to see if LiFT is supported
        try {
            result = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_ENABLED_PROPERTY_NAME);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.LIFT_ENABLED_PROPERTY_NAME + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        log.debug("Obtained value of the " + NhincConstants.LIFT_ENABLED_PROPERTY_NAME + "property: " + result);
        return result;
    }

    protected boolean doesTargetSupportLift(String hcid) {
        // Check with the connection management component to see if LiFT is supported by the target community
        boolean result = false;

        try {
            result = ConnectionManagerCache.liftProtocolSupportedForHomeCommunity(hcid, "HTTPS", NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME);
        }
        catch (ConnectionManagerException ex) {
            log.error(ex.getMessage());
        }

        return result;
    }

    protected XDRAuditLogger createAuditLogger() {
        return new XDRAuditLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }



    protected boolean checkPolicy(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
        log.debug("Begin checkPolicy");
        boolean bPolicyOk = false;

        if (request != null &&
                request.getNhinTargetCommunities() != null &&
                NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity()) &&
                request.getNhinTargetCommunities().getNhinTargetCommunity().get(0) != null &&
                request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity() != null &&
                NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId())) {

            SubjectHelper subjHelp = new SubjectHelper();
            String senderHCID = subjHelp.determineSendingHomeCommunityId(assertion.getHomeCommunity(), assertion);
            String receiverHCID = request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId();
            String direction = NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION;
            log.debug("Checking the policy engine for the " + direction + " request from " + senderHCID + " to " + receiverHCID);

            //return true if 'permit' returned, false otherwise
            XDRPolicyChecker policyChecker = new XDRPolicyChecker();
            bPolicyOk = policyChecker.checkXDRRequestPolicy(request.getProvideAndRegisterDocumentSetRequest(), assertion, senderHCID, receiverHCID, direction);
        } else {
            log.warn("EntityXDRRequestSecuredImpl check on policy requires a non null receiving home community ID specified in the RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType");
        }
        log.debug("EntityXDRRequestSecuredImpl check on policy returns: " + bPolicyOk);
        return bPolicyOk;
    }



}
