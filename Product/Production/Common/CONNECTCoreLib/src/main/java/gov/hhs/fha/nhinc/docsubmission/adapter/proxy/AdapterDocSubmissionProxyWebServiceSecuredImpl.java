/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.adapter.proxy;

import gov.hhs.fha.nhinc.adapterxdrsecured.AdapterXDRSecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *
 * @author Neil Webb
 */
public class AdapterDocSubmissionProxyWebServiceSecuredImpl implements AdapterDocSubmissionProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterxdrsecured";
    private static final String SERVICE_LOCAL_PART = "AdapterXDRSecured_Service";
    private static final String PORT_LOCAL_PART = "AdapterXDRSecured_Port";
    private static final String WSDL_FILE = "AdapterXDRSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adapterxdrsecured:ProvideAndRegisterDocumentSet-b";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterDocSubmissionProxyWebServiceSecuredImpl() {
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
    protected AdapterXDRSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        AdapterXDRSecuredPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterXDRSecuredPortType.class);
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

    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType msg, AssertionType assertion) {
        RegistryResponseType response = null;

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ADAPTER_XDR_SECURED_SERVICE_NAME);
            AdapterXDRSecuredPortType port = getPort(url, NhincConstants.XDR_ACTION, WS_ADDRESSING_ACTION, assertion);

            if (msg == null) {
                log.error("Message was null");
            } else if (port == null) {
                log.error("port was null");
            } else {
                response = (RegistryResponseType) oProxyHelper.invokePort(port, AdapterXDRSecuredPortType.class, "provideAndRegisterDocumentSetb", msg);
            }
        } catch (Exception ex) {
            log.error("Error sending Adapter Doc Submission Secured message: " + ex.getMessage(), ex);
            response = new RegistryResponseType();
            response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        }

        return response;
    }
}
