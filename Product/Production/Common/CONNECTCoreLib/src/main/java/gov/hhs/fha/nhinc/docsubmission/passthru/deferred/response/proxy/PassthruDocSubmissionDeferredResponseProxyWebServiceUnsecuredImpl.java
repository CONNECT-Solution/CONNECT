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

package gov.hhs.fha.nhinc.docsubmission.passthru.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxyxdr.async.response.ProxyXDRAsyncResponsePortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class PassthruDocSubmissionDeferredResponseProxyWebServiceUnsecuredImpl implements PassthruDocSubmissionDeferredResponseProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincproxyxdr:async:response";
    private static final String SERVICE_LOCAL_PART = "ProxyXDRAsyncResponse_Service";
    private static final String PORT_LOCAL_PART = "ProxyXDRAsyncResponse_Port";
    private static final String WSDL_FILE = "NhincProxyXDRResponse.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincproxyxdr:async:response:ProvideAndRegisterDocumentSet-bAsyncResponse_Request";
    private WebServiceProxyHelper oProxyHelper = null;

    public PassthruDocSubmissionDeferredResponseProxyWebServiceUnsecuredImpl() {
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
    protected ProxyXDRAsyncResponsePortType getPort(String url, String wsAddressingAction, AssertionType assertion) {
        ProxyXDRAsyncResponsePortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), ProxyXDRAsyncResponsePortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
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

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
        log.debug("Begin provideAndRegisterDocumentSetBResponse");
        XDRAcknowledgementType response = null;

        try
        {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.NHINC_PROXY_XDR_RESPONSE_SERVICE_NAME);
            ProxyXDRAsyncResponsePortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);

            if(request == null)
            {
                log.error("Message was null");
            }
            else if (assertion == null)
            {
                log.error("assertion was null");
            }
            else if (targetSystem == null)
            {
                log.error("targets was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType msg = new RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType();
                msg.setRegistryResponse(request);
                msg.setAssertion(assertion);
                msg.setNhinTargetSystem(targetSystem);

                response = (XDRAcknowledgementType)oProxyHelper.invokePort(port, ProxyXDRAsyncResponsePortType.class, "provideAndRegisterDocumentSetBAsyncResponse", msg);
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling provideAndRegisterDocumentSetBAsyncResponse: " + ex.getMessage(), ex);
            response = new XDRAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);
            response.setMessage(regResp);
        }

        log.debug("End provideAndRegisterDocumentSetBResponse");
        return response;
    }

}
