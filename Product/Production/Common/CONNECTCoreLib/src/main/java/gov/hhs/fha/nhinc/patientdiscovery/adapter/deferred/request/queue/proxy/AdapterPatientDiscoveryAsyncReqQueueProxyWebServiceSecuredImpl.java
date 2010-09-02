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
package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.queue.proxy;

import gov.hhs.fha.nhinc.adapterpatientdiscoverysecuredasyncreqqueue.AdapterPatientDiscoverySecuredAsyncReqQueuePortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;
import javax.xml.ws.Service;
import org.hl7.v3.PRPAIN201305UV02;


/**
 *
 * @author JHOPPESC, Les westberg
 */
public class AdapterPatientDiscoveryAsyncReqQueueProxyWebServiceSecuredImpl implements AdapterPatientDiscoveryAsyncReqQueueProxy
{
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoverysecuredasyncreqqueue";
    private static final String SERVICE_LOCAL_PART = "AdapterPatientDiscoverySecuredAsyncReqQueue";
    private static final String PORT_LOCAL_PART = "AdapterPatientDiscoverySecuredAsyncReqQueuePortSoap";
    private static final String WSDL_FILE = "AdapterPatientDiscoverySecuredAsyncReqQueue.wsdl";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":" + "AddPatientDiscoveryAsyncReqRequestMessage";
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Default constructor.
     */
    public AdapterPatientDiscoveryAsyncReqQueueProxyWebServiceSecuredImpl()
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
     * This calls the secured web service for this method.
     *
     * @param body The message to be sent to the web service.
     * @param assertion The assertion information to go with the message.
     * @return The response from the web service.
     */
    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(PRPAIN201305UV02 request, AssertionType assertion)
//    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.PRPAIN201305UV02 body, AssertionType assertion)
    {
        String url = null;
        MCCIIN000002UV01 response = new MCCIIN000002UV01();
        String sServiceName = NhincConstants.PATIENT_DISCOVERY_ADAPTER_SECURED_ASYNC_REQ_QUEUE_SERVICE_NAME;

        try
        {
            if (request != null)
            {
                log.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlLocalHomeCommunity(sServiceName);
                log.debug("After target system URL look up. URL for service: " + sServiceName + " is: " + url);

                if (NullChecker.isNotNullish(url))
                {
                    RespondingGatewayPRPAIN201305UV02SecuredRequestType wrappedRequest = new RespondingGatewayPRPAIN201305UV02SecuredRequestType();
                    wrappedRequest.setPRPAIN201305UV02(request);
                    wrappedRequest.setNhinTargetCommunities(null);

                    AdapterPatientDiscoverySecuredAsyncReqQueuePortType port = getPort(url, NhincConstants.PATIENT_DISCOVERY_ACTION, WS_ADDRESSING_ACTION, assertion);
                    response = (MCCIIN000002UV01) oProxyHelper.invokePort(port, AdapterPatientDiscoverySecuredAsyncReqQueuePortType.class, "addPatientDiscoveryAsyncReq", wrappedRequest);
                }
                else
                {
                    log.error("Failed to call the web service (" + sServiceName + ").  The URL is null.");
                }
            }
            else
            {
                log.error("Failed to call the web service (" + sServiceName + ").  The input parameter is null.");
            }
        }
        catch (Exception e)
        {
            log.error("Failed to call the web service (" + sServiceName + ").  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }

        return response;
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
    protected AdapterPatientDiscoverySecuredAsyncReqQueuePortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        AdapterPatientDiscoverySecuredAsyncReqQueuePortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterPatientDiscoverySecuredAsyncReqQueuePortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
}
