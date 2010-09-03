/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxydocretrievedeferredrequest.NhincProxyDocRetrieveDeferredRequestPortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author Sai Valluripalli
 */
public class PassthruDocRetrieveDeferredReqProxyWebServiceUnsecuredImpl implements PassthruDocRetrieveDeferredReqProxy {

    private Log log = null;
    private boolean debugEnabled = false;
    private WebServiceProxyHelper oProxyHelper = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincproxydocretrievedeferredrequest";
    private static final String SERVICE_LOCAL_PART = "NhincProxyDocRetrieveDeferredRequest";
    private static final String PORT_LOCAL_PART = "NhincProxyDocRetrieveDeferredRequestPortSoap";
    private static final String WSDL_FILE = "NhincProxyDocRetrieveDeferredReq.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincproxydocretrievedeferredrequest:CrossGatewayRetrieveRequestMessage";

    /**
     * default constructor
     */
    public PassthruDocRetrieveDeferredReqProxyWebServiceUnsecuredImpl() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
        oProxyHelper = createWebServiceProxyHelper();
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     *
     * @return WebServiceProxyHelper
     */
    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     *
     * @param request
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType target) {
        if (debugEnabled) {
            log.debug("-- Begin NhincProxyDocRetrieveDeferredReqUnsecuredWebServiceImpl.crossGatewayRetrieveRequest() --");
        }
        DocRetrieveAcknowledgementType ack = null;
        String url = null;
        try {
            url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_UNSECURED_REQUEST);
            NhincProxyDocRetrieveDeferredRequestPortType port = getPort(url, NhincConstants.DOCRETRIEVE_DEFERRED_ACTION, WS_ADDRESSING_ACTION, assertion);
            RespondingGatewayCrossGatewayRetrieveRequestType req = new RespondingGatewayCrossGatewayRetrieveRequestType();
            req.setAssertion(assertion);
            req.setNhinTargetSystem(target);
            req.setRetrieveDocumentSetRequest(request);
            ack = (DocRetrieveAcknowledgementType) oProxyHelper.invokePort(port, NhincProxyDocRetrieveDeferredRequestPortType.class, "crossGatewayRetrieveRequest", req);
        } catch (Exception e) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_UNSECURED_REQUEST);
            log.error(e.getMessage());
            ack = new DocRetrieveAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG);
            ack.setMessage(regResp);
        }
        if (debugEnabled) {
            log.debug("-- End NhincProxyDocRetrieveDeferredReqUnsecuredWebServiceImpl.crossGatewayRetrieveRequest() --");
        }

        return ack;
    }

    /**
     * 
     * @param url
     * @param serviceAction
     * @param wsAddressingAction
     * @param assertion
     * @return NhincProxyDocRetrieveDeferredRequestPortType
     */
    protected NhincProxyDocRetrieveDeferredRequestPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        NhincProxyDocRetrieveDeferredRequestPortType port = null;

        Service service = getService();
        if (service != null) {
            if (debugEnabled) {
                log.debug("Obtained service - creating port.");
            }
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincProxyDocRetrieveDeferredRequestPortType.class);
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
}
