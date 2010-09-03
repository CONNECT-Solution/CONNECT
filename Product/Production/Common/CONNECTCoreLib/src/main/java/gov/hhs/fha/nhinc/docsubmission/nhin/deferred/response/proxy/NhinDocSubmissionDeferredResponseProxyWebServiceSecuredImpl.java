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

package gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xdr._2007.XDRDeferredResponsePortType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl implements NhinDocSubmissionDeferredResponseProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xdr:2007";
    private static final String SERVICE_LOCAL_PART = "XDRDeferredResponse_Service";
    private static final String PORT_LOCAL_PART = "XDRDeferredResponse_Port_Soap";
    private static final String WSDL_FILE = "NhinXDRDeferredResponse.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:xdr:2007:Deferred:XDRResponseInputMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected XDRDeferredResponsePortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        XDRDeferredResponsePortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), XDRDeferredResponsePortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBDeferredResponse(RegistryResponseType request, AssertionType assertion, NhinTargetSystemType target) {
        log.debug("Begin provideAndRegisterDocumentSetBDeferredResponse");
        XDRAcknowledgementType response = null;

        try
        {
            String url = oProxyHelper.getUrlFromTargetSystem(target, NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME);
            XDRDeferredResponsePortType port = getPort(url, NhincConstants.XDR_ACTION, WS_ADDRESSING_ACTION, assertion);

            if(request == null)
            {
                log.error("Message was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                response = (XDRAcknowledgementType)oProxyHelper.invokePort(port, XDRDeferredResponsePortType.class, "provideAndRegisterDocumentSetBDeferredResponse", request);
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling provideAndRegisterDocumentSetBDeferredResponse: " + ex.getMessage(), ex);
            response = new XDRAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);
            response.setMessage(regResp);
        }

        log.debug("End provideAndRegisterDocumentSetBDeferredResponse");
        return response;
    }

}
