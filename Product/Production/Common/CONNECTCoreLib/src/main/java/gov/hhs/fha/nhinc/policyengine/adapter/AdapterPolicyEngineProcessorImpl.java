/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;

import gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.proxy.AdapterPolicyEngineOrchProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.proxy.AdapterPolicyEngineOrchProxyObjectFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is the implementation of the AdapterPolicyEngine.   It is the entry
 * point from the Gateway to the Adapter Policy Engine.
 *
 * @author Les Westberg
 */
public class AdapterPolicyEngineProcessorImpl
{
    private static Log log = LogFactory.getLog(AdapterPolicyEngineProcessorImpl.class);

    /**
     * Given a request to check the access policy, this service will interface
     * with the Adapter PEP to determine if access is to be granted or denied.
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion)
        throws AdapterPolicyEngineException
    {
        CheckPolicyResponseType oCheckPolicyResp = new CheckPolicyResponseType();

        AdapterPolicyEngineOrchProxyObjectFactory oFactory = new AdapterPolicyEngineOrchProxyObjectFactory();
        AdapterPolicyEngineOrchProxy oOrchProxy = oFactory.getAdapterPolicyEngineOrchProxy();

        try
        {
            if(oOrchProxy != null)
            {
                log.debug("AdapterPolicyEngineOrchProxy selected: " + oOrchProxy.getClass());
                oCheckPolicyResp = oOrchProxy.checkPolicy(checkPolicyRequest, assertion);
            }
            else
            {
                throw new Exception("AdapterPolicyEngineOrchProxy was null.");
            }
        }
        catch (Exception e)
        {
            String sMessage = "Error occurred calling AdapterPolicyEngineOrchProxy.checkPolicy.  Error: " +
                e.getMessage();
            log.error(sMessage, e);
            throw new AdapterPolicyEngineException(sMessage, e);
        }
        return oCheckPolicyResp;
    }
}
