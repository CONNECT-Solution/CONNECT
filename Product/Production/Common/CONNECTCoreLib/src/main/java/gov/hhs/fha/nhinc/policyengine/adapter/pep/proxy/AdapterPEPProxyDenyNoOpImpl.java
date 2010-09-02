/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pep.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is a "NoOp" implementation of the AdapterPEPProxy interface.
 */
public class AdapterPEPProxyDenyNoOpImpl implements AdapterPEPProxy
{
    private Log log = null;

    public AdapterPEPProxyDenyNoOpImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    /**
     * NO-OP implementation of the checkPolicy operation returns "Deny"
     *
     * @param checkPolicyRequest The xacml request to check defined policy
     * @return The xacml response which contains the access denied
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion)
    {
        log.debug("Begin AdapterPEPProxyDenyNoOpImpl.checkPolicy");
        CheckPolicyResponseType denyPolicyResponse = new CheckPolicyResponseType();
        ResponseType denyResponse = new ResponseType();
        ResultType result = new ResultType();
        result.setDecision(DecisionType.DENY);
        denyResponse.getResult().add(result);
        denyPolicyResponse.setResponse(denyResponse);
        log.debug("End AdapterPEPProxyDenyNoOpImpl.checkPolicy");
        return denyPolicyResponse;
    }
}
