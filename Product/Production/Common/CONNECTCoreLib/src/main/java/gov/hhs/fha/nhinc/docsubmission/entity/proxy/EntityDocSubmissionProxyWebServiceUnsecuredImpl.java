/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.entitypatientdiscoveryasyncreq.EntityPatientDiscoveryAsyncReqPortType;
import gov.hhs.fha.nhinc.nhincentityxdr.EntityXDRPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

public class EntityDocSubmissionProxyWebServiceUnsecuredImpl implements EntityDocSubmissionProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincentityxdr";
    private static final String SERVICE_LOCAL_PART = "EntityXDR_Service";
    private static final String PORT_LOCAL_PART = "EntityXDR_Port";
    private static final String WSDL_FILE = "EntityXDR.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincentityxdr:ProvideAndRegisterDocumentSet-b";
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityDocSubmissionProxyWebServiceUnsecuredImpl() {
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
    protected EntityXDRPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        EntityXDRPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityXDRPortType.class);
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

    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType message,
            AssertionType assertion, NhinTargetCommunitiesType targets, UrlInfoType urlInfo)
    {
        log.debug("Begin EntityDocSubmissionProxyWebServiceUnsecuredImpl.provideAndRegisterDocumentSetB");
        RegistryResponseType response = new RegistryResponseType();

        try
        {
                String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ENTITY_XDR_SERVICE_NAME);
                EntityXDRPortType port = getPort(url, NhincConstants.XDR_ACTION, WS_ADDRESSING_ACTION, assertion);

                if (port != null)
                {
                    RespondingGatewayProvideAndRegisterDocumentSetRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetRequestType();
                    request.setNhinTargetCommunities(targets);
                    request.setProvideAndRegisterDocumentSetRequest(message);
                    request.setUrl(urlInfo);
                    request.setAssertion(assertion);
                    response = (RegistryResponseType) oProxyHelper.invokePort(port, EntityXDRPortType.class, "provideAndRegisterDocumentSetB", request);
                } else
                {
                    log.error("EntityXDRPortType is null");
                }
        } catch (Exception ex)
        {
            log.error("Error calling provideAndRegisterDocumentSetB: " + ex.getMessage(), ex);
        }

        log.debug("End EntityDocSubmissionProxyWebServiceUnsecuredImpl.provideAndRegisterDocumentSetB");
        return response;
    }
}
