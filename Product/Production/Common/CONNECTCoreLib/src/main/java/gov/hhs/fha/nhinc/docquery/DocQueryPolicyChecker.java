/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestMessageType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResponseMessageType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResultEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

/**
 *
 * @author JHOPPESC
 */
public class DocQueryPolicyChecker {

  /**
   * Checks to see if the security policy will permit the query to be executed.
   * @param message The AdhocQuery request message.
   * @return Returns true if the security policy permits the query; false if denied.
   */
  public boolean checkIncomingPolicy(AdhocQueryRequest message, AssertionType assertion) {
    //convert the request message to an object recognized by the policy engine
    AdhocQueryRequestMessageType request = new AdhocQueryRequestMessageType();
    request.setAssertion(assertion);
    request.setAdhocQueryRequest(message);

    AdhocQueryRequestEventType policyCheckReq = new AdhocQueryRequestEventType();
    policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
    policyCheckReq.setMessage(request);

    CheckPolicyRequestType policyReq = buildPolicyRequest(policyCheckReq, assertion);

    return checkPolicy(policyReq, assertion);
  }

  public boolean checkOutgoingPolicy(AdhocQueryRequest message, AssertionType assertion) {
    //convert the request message to an object recognized by the policy engine
    AdhocQueryRequestMessageType request = new AdhocQueryRequestMessageType();
    request.setAssertion(assertion);
    request.setAdhocQueryRequest(message);

    AdhocQueryRequestEventType policyCheckReq = new AdhocQueryRequestEventType();
    policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
    policyCheckReq.setMessage(request);

    CheckPolicyRequestType policyReq = buildPolicyRequest(policyCheckReq, assertion);

    return checkPolicy(policyReq, assertion);
  }

  public boolean checkIncomingResponsePolicy(AdhocQueryResponse message, AssertionType assertion) {
    //convert the request message to an object recognized by the policy engine
    AdhocQueryResponseMessageType request = new AdhocQueryResponseMessageType();
    request.setAssertion(assertion);
    request.setAdhocQueryResponse(message);

    AdhocQueryResultEventType policyCheckReq = new AdhocQueryResultEventType();
    policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
    policyCheckReq.setMessage(request);

    CheckPolicyRequestType policyReq = buildPolicyRequest(policyCheckReq, assertion);

    return checkPolicy(policyReq, assertion);
  }

  public boolean checkOutgoingResponsePolicy(AdhocQueryResponse message, AssertionType assertion, HomeCommunityType receiverHcid) {
    //convert the request message to an object recognized by the policy engine
    AdhocQueryResponseMessageType request = new AdhocQueryResponseMessageType();
    request.setAssertion(assertion);
    request.setAdhocQueryResponse(message);

    AdhocQueryResultEventType policyCheckReq = new AdhocQueryResultEventType();
    policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
    policyCheckReq.setMessage(request);
    policyCheckReq.setReceivingHomeCommunity(receiverHcid);

    CheckPolicyRequestType policyReq = buildPolicyRequest(policyCheckReq, assertion);

    //call the policy engine to check the permission on the request
    return checkPolicy(policyReq, assertion);
  }

  public boolean checkOutgoingRequestPolicy(AdhocQueryRequest message, AssertionType assertion, HomeCommunityType receiverHcid) {
    //convert the request message to an object recognized by the policy engine
    AdhocQueryRequestMessageType request = new AdhocQueryRequestMessageType();
    request.setAssertion(assertion);
    request.setAdhocQueryRequest(message);

    AdhocQueryRequestEventType policyCheckReq = new AdhocQueryRequestEventType();
    policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
    policyCheckReq.setMessage(request);
    policyCheckReq.setReceivingHomeCommunity(receiverHcid);

    CheckPolicyRequestType policyReq = buildPolicyRequest(policyCheckReq, assertion);
    
    //call the policy engine to check the permission on the request
    return checkPolicy(policyReq, assertion);
  }

  protected CheckPolicyRequestType buildPolicyRequest(AdhocQueryRequestEventType policyCheckReq, AssertionType assertion) {
    //call the policy engine to check the permission on the request
    CheckPolicyRequestType policyReq = getPolicyChecker().checkPolicyAdhocQuery(policyCheckReq);
    policyReq.setAssertion(assertion);
    return policyReq;
  }

  protected CheckPolicyRequestType buildPolicyRequest(AdhocQueryResultEventType policyCheckResult, AssertionType assertion) {
    //call the policy engine to check the permission on the request
    CheckPolicyRequestType policyReq = getPolicyChecker().checkPolicyAdhocQueryResponse(policyCheckResult);
    policyReq.setAssertion(assertion);
    return policyReq;
  }

  /**
   * check the policy engine's response, return true if response = permit
   * @param policyCheckReq
   * @param assertion
   * @return return true if response = permit
   */
  protected boolean checkPolicy(CheckPolicyRequestType policyReq, AssertionType assertion) {
    CheckPolicyResponseType policyResp = getPolicyEngine().checkPolicy(policyReq, assertion);
    return validatePolicyResponse(policyResp);
  }

  protected PolicyEngineChecker getPolicyChecker() {
    return new PolicyEngineChecker();
  }

  protected PolicyEngineProxy getPolicyEngine() {
    return (new PolicyEngineProxyObjectFactory()).getPolicyEngineProxy();
  }

  protected boolean validatePolicyResponse(CheckPolicyResponseType policyResp) {
    return policyResp.getResponse() != null 
            && NullChecker.isNotNullish(policyResp.getResponse().getResult())
            && policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT;
  }
}
