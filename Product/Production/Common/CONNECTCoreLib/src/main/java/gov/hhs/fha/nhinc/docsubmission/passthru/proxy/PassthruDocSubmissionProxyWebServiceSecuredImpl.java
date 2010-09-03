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

package gov.hhs.fha.nhinc.docsubmission.passthru.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.ProxyXDRSecuredPortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class PassthruDocSubmissionProxyWebServiceSecuredImpl implements PassthruDocSubmissionProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincproxyxdrsecured";
    private static final String SERVICE_LOCAL_PART = "ProxyXDRSecured_Service";
    private static final String PORT_LOCAL_PART = "ProxyXDRSecured_Port";
    private static final String WSDL_FILE = "NhincProxyXDRSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincproxyxdrsecured:ProvideAndRegisterDocumentSet-b";
    private WebServiceProxyHelper oProxyHelper = null;

    public PassthruDocSubmissionProxyWebServiceSecuredImpl() {
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
    protected ProxyXDRSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        ProxyXDRSecuredPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), ProxyXDRSecuredPortType.class);
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

    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion, NhinTargetSystemType target) {
        log.debug("Begin provideAndRegisterDocumentSetB");
        RegistryResponseType response = null;

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.NHINC_PROXY_XDR_SECURED_SERVICE_NAME);
            ProxyXDRSecuredPortType port = getPort(url, NhincConstants.DOC_QUERY_ACTION, WS_ADDRESSING_ACTION, assertion);

            if (body == null) {
                log.error("Message was null");
            }
            else if (target == null) {
                log.error("target was null");
            } else if (port == null) {
                log.error("port was null");
            } else {
                RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
                request.setProvideAndRegisterDocumentSetRequest(body);
                request.setNhinTargetSystem(target);

                response = (RegistryResponseType) oProxyHelper.invokePort(port, ProxyXDRSecuredPortType.class, "provideAndRegisterDocumentSetB", request);
            }
        } catch (Exception ex) {
            log.error("Error calling provideAndRegisterDocumentSetB: " + ex.getMessage(), ex);
        }

        log.debug("End provideAndRegisterDocumentSetB");
        return response;
    }

}
