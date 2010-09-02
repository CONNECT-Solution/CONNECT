/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This is a concrete implementation of the AdapterPolicyEngineOrchestrator interface
 * that always returns a deny.
 *
 * @author Les Westberg
 */
public class AdapterPolicyEngineOrchProxyDenyNoOpImpl implements AdapterPolicyEngineOrchProxy
{
    private Log log = null;

    public AdapterPolicyEngineOrchProxyDenyNoOpImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    /**
     * Given a request to check the access policy, this service will always
     * return a deny response.
     *
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion)
    {
        log.debug("Begin AdapterPolicyEngineOrchProxyDenyNoOpImpl.checkPolicy");
        CheckPolicyResponseType oPolicyResponse = new CheckPolicyResponseType();
        ResponseType oResponse = new ResponseType();
        ResultType oResult = new ResultType();
        oResult.setDecision(DecisionType.DENY);
        oResponse.getResult().add(oResult);
        oPolicyResponse.setResponse(oResponse);
        log.debug("End AdapterPolicyEngineOrchProxyDenyNoOpImpl.checkPolicy");
        return oPolicyResponse;
    }
}
