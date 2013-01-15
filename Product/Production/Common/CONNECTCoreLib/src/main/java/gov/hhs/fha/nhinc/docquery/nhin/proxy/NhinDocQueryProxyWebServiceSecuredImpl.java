/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.docquery.nhin.proxy;

import ihe.iti.xds_b._2007.RespondingGatewayQueryPortType;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.docrepository.DocumentProcessHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.ws.BindingProvider;

/**
 * This class is the component proxy for calling the NHIN doc query web service.
 *
 * @author jhoppesc, Les Westberg
 */
public class NhinDocQueryProxyWebServiceSecuredImpl implements NhinDocQueryProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_Query_Service";
    private static final String PORT_LOCAL_PART = "RespondingGateway_Query_Port_Soap";
    private static final String WSDL_FILE = "NhinDocQuery.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:2007:CrossGatewayQuery";
    private final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    public NhinDocQueryProxyWebServiceSecuredImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected DocumentProcessHelper getDocumentProcessHelper() {
        return new DocumentProcessHelper();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @param serviceAction The action for the web service.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected RespondingGatewayQueryPortType getPort(String url, String serviceAction, String wsAddressingAction,
            AssertionType assertion) {
        RespondingGatewayQueryPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayQueryPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction,
                    wsAddressingAction, assertion);
            oProxyHelper.setPortTimeoutByService((javax.xml.ws.BindingProvider) port,
                    NhincConstants.DOC_QUERY_SERVICE_NAME);
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

    /**
     * Calls the respondingGatewayCrossGatewayQuery method of the web service.
     *
     * @param request The information for the web service.
     * @return The response from the web service.
     */
    @Override
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest request, AssertionType assertion,
            NhinTargetSystemType target) throws Exception {
        AdhocQueryResponse response = new AdhocQueryResponse();

        try {
            String url = target.getUrl();
            if (NullChecker.isNullish(url)) {
                url = ConnectionManagerCache.getInstance().getDefaultEndpointURLByServiceName(
                        target.getHomeCommunity().getHomeCommunityId(), NhincConstants.DOC_QUERY_SERVICE_NAME);
                log.debug("After target system URL look up. URL for service: " + NhincConstants.DOC_QUERY_SERVICE_NAME
                        + " is: " + url);
            }

            RespondingGatewayQueryPortType port = getPort(url, NhincConstants.DOC_QUERY_SERVICE_NAME, WS_ADDRESSING_ACTION,
                    assertion);
            WebServiceProxyHelper wsHelper = new WebServiceProxyHelper();
            wsHelper.addTargetCommunity(((BindingProvider)port), target);

            if (request == null) {
                log.error("Message was null");
            } else if (assertion == null) {
                log.error("AssertionType was null");
            } else if (port == null) {
                log.error("port was null");
            } else {
                String uniquePatientId = "";
                if (assertion != null && assertion.getUniquePatientId() != null
                        && assertion.getUniquePatientId().size() > 0) {
                    uniquePatientId = assertion.getUniquePatientId().get(0);
                }

                // Log the start of the performance record
                String targetHomeCommunityId = HomeCommunityMap.getCommunityIdFromTargetSystem(target);
                PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(
                        NhincConstants.DOC_QUERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                        NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, targetHomeCommunityId);

                response = (AdhocQueryResponse) oProxyHelper.invokePort(port, RespondingGatewayQueryPortType.class,
                        "respondingGatewayCrossGatewayQuery", request);

                // Check for Demo Mode
                if (DocumentProcessHelper.isDemoOperationModeEnabled()) {
                    log.debug("CONNECT Demo Operation Mode Enabled");
                    DocumentProcessHelper documentProcessHelper = getDocumentProcessHelper();

                    // Demo mode enabled, process AdhocQueryResponse to save document metadata to the CONNECT default
                    // document repository
                    documentProcessHelper.documentRepositoryProvideAndRegisterDocumentSet(response, uniquePatientId);
                } else {
                    log.debug("CONNECT Demo Operation Mode Disabled");
                }

                // Log the end of the performance record
                PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(
                        NhincConstants.DOC_QUERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                        NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, targetHomeCommunityId);
            }
        } catch (Exception ex) {
            log.error("Error calling respondingGatewayCrossGatewayQuery: " + ex.getMessage(), ex);
            throw ex;
            // RegistryErrorList registryErrorList = new RegistryErrorList();
            //
            // RegistryError registryError = new RegistryError();
            // registryError.setCodeContext("Processing Adapter Doc Query document query");
            // registryError.setErrorCode("XDSRepostoryError");
            // registryError.setSeverity("Error");
            // registryErrorList.getRegistryError().add(registryError);
            // response.setRegistryErrorList(registryErrorList);
        }
        return response;
    }
}
