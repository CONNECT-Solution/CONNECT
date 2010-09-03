/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.adapterpolicyengineorchestrator.AdapterPolicyEngineOrchestratorPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the concrete implementation for the web service based call to the
 * AdapterPolicyEngineOrchestrator.
 *
 * @author Les Westberg
 */
public class AdapterPolicyEngineOrchProxyWebServiceUnsecuredImpl implements AdapterPolicyEngineOrchProxy
{
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterpolicyengineorchestrator";
    private static final String SERVICE_LOCAL_PART = "AdapterPolicyEngineOrchestrator";
    private static final String PORT_LOCAL_PART = "AdapterPolicyEngineOrchestratorPortSoap";
    private static final String WSDL_FILE = "AdapterPolicyEngineOrchestrator.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adapterpolicyengineorchestrator:CheckPolicyRequest";

    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterPolicyEngineOrchProxyWebServiceUnsecuredImpl()
    {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @param wsAddressingAction The action assigned to the input parameter for the web service operation.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected AdapterPolicyEngineOrchestratorPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
    {
        AdapterPolicyEngineOrchestratorPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterPolicyEngineOrchestratorPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService()
    {
        if (cachedService == null)
        {
            try
            {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
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
        log.debug("Begin AdapterPolicyEngineOrchProxyWebServiceUnsecuredImpl.checkPolicy");
        CheckPolicyResponseType oResponse = new CheckPolicyResponseType();
        String serviceName = NhincConstants.ADAPTER_POLICY_ENGINE_ORCHESTRATOR_SERVICE_NAME;

        try
        {
            log.debug("Before target system URL look up.");
            String url = oProxyHelper.getUrlLocalHomeCommunity(serviceName);
            if(log.isDebugEnabled())
            {
                log.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);
            }

            if (NullChecker.isNotNullish(url))
            {
                AdapterPolicyEngineOrchestratorPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);
                oResponse = (CheckPolicyResponseType)oProxyHelper.invokePort(port, AdapterPolicyEngineOrchestratorPortType.class, "checkPolicy", checkPolicyRequest);
            }
            else
            {
                log.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPolicyEngineOrchProxyWebServiceUnsecuredImpl.checkPolicy.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        log.debug("End AdapterPolicyEngineOrchProxyWebServiceUnsecuredImpl.checkPolicy");
        return oResponse;
    }

}
