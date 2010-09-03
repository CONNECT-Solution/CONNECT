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

package gov.hhs.fha.nhinc.docsubmission.nhin.proxy;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ihe.iti.xdr._2007.DocumentRepositoryXDRPortType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author dunnek
 */
public class NhinDocSubmissionProxyWebServiceSecuredImpl implements NhinDocSubmissionProxy{
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xdr:2007";
    private static final String SERVICE_LOCAL_PART = "DocumentRepositoryXDR_Service";
    private static final String PORT_LOCAL_PART = "DocumentRepositoryXDR_Port_Soap";
    private static final String WSDL_FILE = "NhinXDR.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:xdr:2007:ProvideAndRegisterDocumentSet-b";
    private WebServiceProxyHelper oProxyHelper = null;

    public NhinDocSubmissionProxyWebServiceSecuredImpl() {
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
    protected DocumentRepositoryXDRPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        DocumentRepositoryXDRPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), DocumentRepositoryXDRPortType.class);
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

    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType request,
            AssertionType assertion, NhinTargetSystemType targetSystem)
    {
        log.debug("Begin provideAndRegisterDocumentSetB");
        RegistryResponseType response = new RegistryResponseType();

        try
        {
            String url = oProxyHelper.getUrlFromTargetSystem(targetSystem, NhincConstants.NHINC_XDR_SERVICE_NAME);
            DocumentRepositoryXDRPortType port = getPort(url, NhincConstants.XDR_ACTION, WS_ADDRESSING_ACTION, assertion);

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
                response = (RegistryResponseType)oProxyHelper.invokePort(port, DocumentRepositoryXDRPortType.class, "documentRepositoryProvideAndRegisterDocumentSetB", request);
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling documentRepositoryProvideAndRegisterDocumentSetB: " + ex.getMessage(), ex);
        }

        log.debug("End provideAndRegisterDocumentSetB");
        return response;

        
    }

}
