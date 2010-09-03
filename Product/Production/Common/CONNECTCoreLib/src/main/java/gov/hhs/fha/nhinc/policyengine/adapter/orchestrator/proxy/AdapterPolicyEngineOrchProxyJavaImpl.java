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
import gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.AdapterPolicyEngineOrchestratorImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This is the concrete implementation for the Java based call to the
 * AdapterPolicyEngineOrchestrator.
 *
 * @author Les Westberg
 */
public class AdapterPolicyEngineOrchProxyJavaImpl implements AdapterPolicyEngineOrchProxy
{
    private Log log = null;

    public AdapterPolicyEngineOrchProxyJavaImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    /**
     * Given a request to check the access policy, this service will interface
     * with the Adapter PEP to determine if access is to be granted or denied.
     *
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion)
    {
        log.debug("Begin AdapterPolicyEngineOrchProxyJavaImpl.checkPolicy");
        CheckPolicyResponseType oResponse = new CheckPolicyResponseType();

        AdapterPolicyEngineOrchestratorImpl oOrchestratorImpl = new AdapterPolicyEngineOrchestratorImpl();

        try
        {
            oResponse = oOrchestratorImpl.checkPolicy(checkPolicyRequest, assertion);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPolicyEngineOrchProxyJavaImpl.checkPolicy.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        log.debug("End AdapterPolicyEngineOrchProxyJavaImpl.checkPolicy");
        return oResponse;
    }

}
