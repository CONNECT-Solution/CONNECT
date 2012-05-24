/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.gateway.executorservice;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import ihe.iti.xds_b._2007.RespondingGatewayQueryService;
import ihe.iti.xds_b._2007.RespondingGatewayQueryPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.entity.EntityDocQueryHelper;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.transform.document.DocumentQueryTransform;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;

import java.util.Map;
import java.net.URL;

import javax.xml.ws.BindingProvider;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implements the Nhin DocQuery web service client that calls this web service Defines the specific generics to be used
 * as follows Target is a gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType object Request is an
 * oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest object Response is an
 * oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse object
 * 
 * @author paul.eftis
 */
public class DQClient<Target extends QualifiedSubjectIdentifierType, Request extends AdhocQueryRequest, Response extends ResponseWrapper>
        implements WebServiceClient<Target, Request, Response> {

    private static Log log = LogFactory.getLog(DQClient.class);

    private static RespondingGatewayQueryService serviceInstance = null;
    private static final Object DQSYNC = new Object();

    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    private String sLocalHomeCommunity = null;
    private String sLocalAssigningAuthorityId = null;
    private String sUniquePatientId = null;
    private String sTransactionId = null;
    private AssertionType oAssertion;
    private AdhocQueryRequest oOriginalQueryRequest;

    public DQClient(String transactionId, AssertionType assertion, AdhocQueryRequest originalQueryRequest,
            String localAssigningAuthorityId, String uniquePatientId) {

        super();
        sLocalHomeCommunity = getLocalHomeCommunityId();
        oOriginalQueryRequest = originalQueryRequest;
        sTransactionId = transactionId;
        oAssertion = assertion;
        sLocalAssigningAuthorityId = localAssigningAuthorityId;
        sUniquePatientId = uniquePatientId;
        if ((oAssertion.getUniquePatientId() != null) && (oAssertion.getUniquePatientId().size() < 1)) {
            oAssertion.getUniquePatientId().add(sUniquePatientId);
        }

    }

    // implement singleton pattern using double null check pattern
    private static RespondingGatewayQueryService getWebServiceInstance() throws Exception {
        if (serviceInstance != null) {
            return serviceInstance;
        } else {
            log.debug("PDClient retrieving web service client from wsdl");
            synchronized (DQSYNC) {
                if (serviceInstance == null) {
                    try {
                        serviceInstance = new RespondingGatewayQueryService();
                    } catch (Exception e) {
                        throw e;
                    }
                }
            }
            return serviceInstance;
        }
    }

    private String getLocalHomeCommunityId() {
        String sHomeCommunity = null;

        if (sLocalHomeCommunity != null) {
            sHomeCommunity = sLocalHomeCommunity;
        } else {
            try {
                sHomeCommunity = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                        NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
        return sHomeCommunity;
    }

    /**
     * Implements all connect logic to generate web service call Updates AdhocQueryRequest $XDSPatientId with the
     * ^^^aaId for the target Checks policy through PolicyEngineProxy (if policy false returns AdhocQueryResponse with
     * error set
     * 
     * Note that web service client timeouts set to InitServlet.getTimeoutValues().get("DQConnectTimeout") and
     * InitServlet.getTimeoutValues().get("DQRequestTimeout"))
     * 
     * @param t is QualifiedSubjectIdentifierType target which is used to get url to call
     * @param r is AdhocQueryRequest request to send in web service call
     * @return Response is AdhocQueryResponse returned
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    @Override
    public Response callWebService(Target t, Request r) throws Exception {
        log.debug(Thread.currentThread().getName() + " DQClient::callWebService");
        ResponseWrapper resp = null;
        AdhocQueryRequest adhocQueryRequest = null;
        AdhocQueryResponse adhocQueryResponse = null;
        try {
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType docQuery = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType();
            NhinTargetSystemType targetSystem = new NhinTargetSystemType();
            String assigningAuthority = t.getAssigningAuthorityIdentifier();

            HomeCommunityType targetCommunity = new EntityDocQueryHelper().lookupHomeCommunityId(assigningAuthority,
                    sLocalAssigningAuthorityId, sLocalHomeCommunity);
            if (targetCommunity != null) {
                targetSystem.setHomeCommunity(targetCommunity);
            }
            docQuery.setNhinTargetSystem(targetSystem);
            // Replace the patient id in the document query message
            DocumentQueryTransform transform = new DocumentQueryTransform();
            adhocQueryRequest = transform.replaceAdhocQueryPatientId(cloneRequest(oOriginalQueryRequest),
                    sLocalHomeCommunity, t.getAssigningAuthorityIdentifier(), t.getSubjectIdentifier());
            docQuery.setAdhocQueryRequest(adhocQueryRequest);

            if (isValidPolicy(adhocQueryRequest, oAssertion, targetCommunity)) {
                // Audit the Document Query Request Message sent on the Nhin Interface
                DocQueryAuditLog auditLog = new DocQueryAuditLog();
                AcknowledgementType ack = auditLog.auditDQRequest(adhocQueryRequest, oAssertion,
                        NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

                String serviceAddress = oProxyHelper.getUrlFromTargetSystemByGatewayAPILevel(targetSystem,
                        NhincConstants.DOC_QUERY_SERVICE_NAME, GATEWAY_API_LEVEL.LEVEL_g0);
                RespondingGatewayQueryPortType servicePort = getWebServiceInstance()
                        .getRespondingGatewayQueryPortSoap();
                Map requestContext = ((BindingProvider) servicePort).getRequestContext();
                requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceAddress);
                // set the urlconnection timeout and read timeout for the web service call
                // requestContext.put(NhincConstants.CONNECT_TIMEOUT_NAME,
                // ExecutorServiceHelper.getInstance().getTimeoutValues().
                // get(NhincConstants.DOC_QUERY_CONNECT_TIMEOUT));
                // requestContext.put(NhincConstants.REQUEST_TIMEOUT_NAME,
                // ExecutorServiceHelper.getInstance().getTimeoutValues().
                // get(NhincConstants.DOC_QUERY_REQUEST_TIMEOUT));
                // set saml assertion on requestContext
                Map samlMap = (new SamlTokenCreator()).CreateRequestContext(oAssertion, serviceAddress,
                        NhincConstants.DOC_QUERY_ACTION);
                requestContext.putAll(samlMap);

                log.debug(Thread.currentThread().getName() + " calling serviceAddress=" + serviceAddress + " and hcid="
                        + targetSystem.getHomeCommunity().getHomeCommunityId());
                adhocQueryResponse = servicePort.respondingGatewayCrossGatewayQuery(adhocQueryRequest);
            } else {
                adhocQueryResponse = new AdhocQueryResponse();
                RegistryErrorList regErrList = new RegistryErrorList();
                RegistryError regErr = new RegistryError();
                regErrList.getRegistryError().add(regErr);
                regErr.setCodeContext("Policy Check Failed");
                regErr.setErrorCode("XDSRepositoryError");
                regErr.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
                adhocQueryResponse.setRegistryErrorList(regErrList);
                adhocQueryResponse.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
            }
        } catch (Exception e) {
            ExecutorServiceHelper.getInstance().outputCompleteException(e);
            // throw the exception back out and will be caught/handled by CallableRequest
            throw e;
        } finally {
            resp = new ResponseWrapper(t, adhocQueryRequest, adhocQueryResponse);
        }
        return (Response) resp;
    }

    /**
     * Policy Check verification done here....from connect code
     * 
     * @param queryRequest
     * @param assertion
     * @return boolean
     */
    private boolean isValidPolicy(AdhocQueryRequest queryRequest, AssertionType assertion,
            HomeCommunityType targetCommunity) {
        boolean isValid = false;
        AdhocQueryRequestEventType checkPolicy = new AdhocQueryRequestEventType();
        AdhocQueryRequestMessageType checkPolicyMessage = new AdhocQueryRequestMessageType();
        checkPolicyMessage.setAdhocQueryRequest(queryRequest);
        checkPolicyMessage.setAssertion(assertion);
        checkPolicy.setMessage(checkPolicyMessage);
        checkPolicy.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        checkPolicy.setInterface(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        checkPolicy.setReceivingHomeCommunity(targetCommunity);
        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyAdhocQuery(checkPolicy);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);
        /* if response='permit' */
        if (policyResp.getResponse().getResult().get(0).getDecision().value().equals(NhincConstants.POLICY_PERMIT)) {
            isValid = true;
        }
        return isValid;
    }

    private AdhocQueryRequest cloneRequest(AdhocQueryRequest request) {
        AdhocQueryRequest newRequest = new AdhocQueryRequest();
        newRequest.setAdhocQuery(request.getAdhocQuery());
        newRequest.setComment(request.getComment());
        newRequest.setFederated(request.isFederated());
        newRequest.setFederation(request.getFederation());
        newRequest.setId(request.getId());
        newRequest.setMaxResults(request.getMaxResults());
        newRequest.setRequestSlotList(request.getRequestSlotList());
        newRequest.setResponseOption(request.getResponseOption());
        newRequest.setStartIndex(request.getStartIndex());
        return newRequest;
    }

}
