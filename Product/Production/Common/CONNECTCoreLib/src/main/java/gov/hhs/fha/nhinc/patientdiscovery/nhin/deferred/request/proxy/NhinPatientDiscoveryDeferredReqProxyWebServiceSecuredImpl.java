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
package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xcpd._2009.RespondingGatewayDeferredRequestPortType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

/**
 *
 * @author Jon Hoppesch
 */
public class NhinPatientDiscoveryDeferredReqProxyWebServiceSecuredImpl implements NhinPatientDiscoveryDeferredReqProxy {

    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xcpd:2009";
    private static final String SERVICE_LOCAL_PART = "RespondingGatewayDeferredRequest_Service";
    private static final String PORT_LOCAL_PART = "RespondingGatewayDeferredRequest_Port";
    private static final String WSDL_FILE = "NhinPatientDiscoveryDeferredRequest.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:hl7-org:v3:PRPA_IN201305UV02:Deferred:CrossGatewayPatientDiscovery";
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Default constructor.
     */
    public NhinPatientDiscoveryDeferredReqProxyWebServiceSecuredImpl()
    {
        log = createLogger();
    }

    /**
     * Creates the log object for logging.
     *
     * @return The log object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
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
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @param serviceAction The action for the web service.
     * @param wsAddressingAction The action assigned to the input parameter for the web service operation.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected RespondingGatewayDeferredRequestPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        RespondingGatewayDeferredRequestPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayDeferredRequestPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }


    public MCCIIN000002UV01 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 request, AssertionType assertion, NhinTargetSystemType target) {
        String url = null;
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        try
        {
            if (request != null)
            {
                log.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlFromTargetSystem(target, NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME);
                log.debug("After target system URL look up. URL for service: " + NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME + " is: " + url);

                if (NullChecker.isNotNullish(url))
                {
                    RespondingGatewayDeferredRequestPortType port = getPort(url, NhincConstants.PATIENT_DISCOVERY_ACTION, WS_ADDRESSING_ACTION, assertion);
                    response = (MCCIIN000002UV01) oProxyHelper.invokePort(port, RespondingGatewayDeferredRequestPortType.class, "respondingGatewayDeferredPRPAIN201305UV02", request);
                }
                else
                {
                    log.error("Failed to call the web service (" + NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME + ").  The URL is null.");
                }
            }
            else
            {
                log.error("Failed to call the web service (" + NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME + ").  The input parameter is null.");
            }
        }
        catch (Exception e)
        {
            log.error("Failed to call the web service (" + NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME + ").  An unexpected exception occurred.  " +
                      "Exception: " + e.getMessage(), e);
        }

        return response;
    }
}
