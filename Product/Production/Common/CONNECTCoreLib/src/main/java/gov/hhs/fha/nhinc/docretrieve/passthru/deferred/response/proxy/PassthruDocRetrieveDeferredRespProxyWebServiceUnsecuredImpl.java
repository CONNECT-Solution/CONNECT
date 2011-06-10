/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxydocretrieveresponse.NhincProxyDocRetrieveDeferredResponsePortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author Sai Valluripalli
 */
public class PassthruDocRetrieveDeferredRespProxyWebServiceUnsecuredImpl implements PassthruDocRetrieveDeferredRespProxy {

    private Log log = null;
    private boolean debugEnabled = false;
    private WebServiceProxyHelper oProxyHelper = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincproxydocretrieveresponse";
    private static final String SERVICE_LOCAL_PART = "NhincProxyDocRetrieveDeferredResponse";
    private static final String PORT_LOCAL_PART = "NhincProxyDocRetrieveDeferredResponsePortSoap";
    private static final String WSDL_FILE = "NhincProxyDocRetrieveDeferredResp.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincproxydocretrieveresponse:CrossGatewayRetrieveResponseMessage";

    /**
     * default constructor
     */
    public PassthruDocRetrieveDeferredRespProxyWebServiceUnsecuredImpl() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
        oProxyHelper = createWebServiceProxyHelper();
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
     * @return Log
     */
    private Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     *
     * @param retrieveDocumentSetResponse
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetRequestType request, RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetSystemType target) {
        if (debugEnabled) {
            log.debug("-- Begin NhincProxyDocRetrieveDeferredRespUnsecuredWebServiceImpl.crossGatewayRetrieveResponse(...) --");
        }
        DocRetrieveAcknowledgementType ack = null;
        String url = null;
        try {
            url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_UNSECURED_RESPONSE);
            NhincProxyDocRetrieveDeferredResponsePortType port = getPort(url, NhincConstants.DOCRETRIEVE_DEFERRED_ACTION, WS_ADDRESSING_ACTION, assertion);
            RespondingGatewayCrossGatewayRetrieveResponseType resp = new RespondingGatewayCrossGatewayRetrieveResponseType();
            resp.setAssertion(assertion);
            resp.setNhinTargetSystem(target);
            resp.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
            ack = (DocRetrieveAcknowledgementType) oProxyHelper.invokePort(port, NhincProxyDocRetrieveDeferredResponsePortType.class, "crossGatewayRetrieveResponse", resp);

        } catch (Exception ex) {
            log.error(ex);
        }

        if (debugEnabled) {
            log.debug("-- End NhincProxyDocRetrieveDeferredRespUnsecuredWebServiceImpl.crossGatewayRetrieveResponse(...) --");
        }
        return ack;
    }

    /**
     * 
     * @param url
     * @param serviceAction
     * @param wsAddressingAction
     * @param assertion
     * @return NhincProxyDocRetrieveDeferredResponsePortType
     */
    private NhincProxyDocRetrieveDeferredResponsePortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {

        NhincProxyDocRetrieveDeferredResponsePortType port = null;
        Service service = getService();
        if (service != null) {
            if (debugEnabled) {
                log.debug("Obtained service - creating port.");
            }
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincProxyDocRetrieveDeferredResponsePortType.class);
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
