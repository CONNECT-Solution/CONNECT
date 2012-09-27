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
package gov.hhs.fha.nhinc.docretrieve.nhin.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docrepository.DocumentProcessHelper;
import gov.hhs.fha.nhinc.docretrieve.nhin.proxy.description.NhinDocRetrieveServicePortDescriptor;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.RespondingGatewayRetrievePortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import java.sql.Timestamp;

import javax.xml.ws.BindingProvider;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 
 * @author Neil Webb
 */
public class NhinDocRetrieveProxyWebServiceSecuredImpl implements NhinDocRetrieveProxy {
    
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Default constructor.
     */
    public NhinDocRetrieveProxyWebServiceSecuredImpl() {
        log = createLogger();
    }

    /**
     * Creates the log object for logging.
     * 
     * @return The log object.
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected DocumentProcessHelper getDocumentProcessHelper() {
        return new DocumentProcessHelper();
    }

    /**
     * Retrieve the document(s) specified in the request.
     * 
     * @param request The identifier(s) of the document(s) to be retrieved.
     * @param targetSystem The target system where the message is being sent to.
     * @return The document(s) that were retrieved.
     */
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem,
            GATEWAY_API_LEVEL level) {
        String url = null;
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        String sServiceName = NhincConstants.DOC_RETRIEVE_SERVICE_NAME;

        try {
            if (request != null) {
                log.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlFromTargetSystemByGatewayAPILevel(targetSystem, sServiceName, level);
                log.debug("After target system URL look up. URL for service: " + sServiceName + " is: " + url);

                if (NullChecker.isNotNullish(url)) {
                    ServicePortDescriptor<RespondingGatewayRetrievePortType> portDescriptor = getServicePortDescriptor(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);

                    CONNECTClient<RespondingGatewayRetrievePortType> client = CONNECTClientFactory.getInstance()
                            .getCONNECTClientSecured(portDescriptor, url, assertion);

                    // Log the start of the performance record
                    String targetHomeCommunityId = HomeCommunityMap.getCommunityIdFromTargetSystem(targetSystem);
                    Timestamp starttime = new Timestamp(System.currentTimeMillis());
                    Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime,
                            NhincConstants.DOC_RETRIEVE_SERVICE_NAME, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, targetHomeCommunityId);

                    WebServiceProxyHelper wsHelper = new WebServiceProxyHelper();
                    wsHelper.addTargetCommunity((BindingProvider) client.getPort(), targetSystem);
                    wsHelper.addServiceName((BindingProvider) client.getPort(), 
                            NhincConstants.DOC_RETRIEVE_SERVICE_NAME);

                    response = (RetrieveDocumentSetResponseType) client.invokePort(
                            RespondingGatewayRetrievePortType.class, "respondingGatewayCrossGatewayRetrieve", request);

                    // Log the end of the performance record
                    Timestamp stoptime = new Timestamp(System.currentTimeMillis());
                    PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);
                } else {
                    log.error("Failed to call the web service (" + sServiceName + ").  The URL is null.");
                }
            } else {
                log.error("Failed to call the web service (" + sServiceName + ").  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web service (" + sServiceName + ").  An unexpected exception occurred.  "
                    + "Exception: " + e.getMessage(), e);
            RegistryResponseType regResp = new RegistryResponseType();

            regResp.setStatus(DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);

            RegistryError registryError = new RegistryError();
            registryError.setCodeContext("Processing Adapter Doc Query document retrieve");
            registryError.setErrorCode("XDSRepostoryError");
            registryError.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
            if (regResp.getRegistryErrorList() == null) {
                regResp.setRegistryErrorList(new RegistryErrorList());
            }
            regResp.getRegistryErrorList().getRegistryError().add(registryError);
            response.setRegistryResponse(regResp);
        }

        return response;
    }
    
    public ServicePortDescriptor<RespondingGatewayRetrievePortType> getServicePortDescriptor(
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        switch (apiLevel) {
        case LEVEL_g0:
            return new NhinDocRetrieveServicePortDescriptor();
        default:
            return new NhinDocRetrieveServicePortDescriptor();
        }
    }
}
