/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManager;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.docretrieve.MessageGenerator;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.nhin.proxy.description.NhinDocRetrieveServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.xdcommon.XDCommonResponseHelper;
import gov.hhs.fha.nhinc.xdcommon.XDCommonResponseHelper.ErrorCodes;
import ihe.iti.xds_b._2007.RespondingGatewayRetrievePortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author Neil Webb
 */
public class NhinDocRetrieveProxyWebServiceSecuredImpl implements NhinDocRetrieveProxy {

    private static final Logger LOG = LoggerFactory.getLogger(NhinDocRetrieveProxyWebServiceSecuredImpl.class);

    /**
     * Retrieve the document(s) specified in the request.
     *
     * @param request The identifier(s) of the document(s) to be retrieved.
     * @param targetSystem The target system where the message is being sent to.
     * @return The document(s) that were retrieved.
     */
    @NwhinInvocationEvent(beforeBuilder = RetrieveDocumentSetRequestTypeDescriptionBuilder.class, afterReturningBuilder = RetrieveDocumentSetResponseTypeDescriptionBuilder.class, serviceType = "Retrieve Document", version = "")
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem,
            GATEWAY_API_LEVEL level) {
        String url = null;
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        String sServiceName = NhincConstants.DOC_RETRIEVE_SERVICE_NAME;

        try {
            if (request != null) {
                LOG.debug("Before target system URL look up.");
                url = getUrl(targetSystem, sServiceName, level);
                LOG.debug("After target system URL look up. URL for service: " + sServiceName + " is: " + url);

                if (NullChecker.isNotNullish(url)) {
                    ServicePortDescriptor<RespondingGatewayRetrievePortType> portDescriptor = getServicePortDescriptor(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);

                    CONNECTClient<RespondingGatewayRetrievePortType> client = getCONNECTClientSecured(portDescriptor,
                            assertion, url, targetSystem);
                    client.enableMtom();

                    response = (RetrieveDocumentSetResponseType) client.invokePort(
                            RespondingGatewayRetrievePortType.class, "respondingGatewayCrossGatewayRetrieve", request);
                } else {
                    LOG.error("Failed to call the web service (" + sServiceName + ").  The URL is null.");
                }
            } else {
                LOG.error("Failed to call the web service (" + sServiceName + ").  The input parameter is null.");
            }
        } catch (ConnectionManagerException e) {
            XDCommonResponseHelper helper = new XDCommonResponseHelper();
            RegistryResponseType error = helper.createError(e.getLocalizedMessage(), ErrorCodes.XDSRepositoryError, NhincConstants.INIT_MULTISPEC_LOC_ENTITY_DR);

            response = new RetrieveDocumentSetResponseType();
            response.setRegistryResponse(error);

        } catch (Exception e) {
            LOG.error("Failed to call the web service (" + sServiceName + ").  An unexpected exception occurred.  "
                    + "Exception: " + e.getMessage(), e);

            response = MessageGenerator.getInstance().createRegistryResponseError(
                    "Adapter Document Retrieve Processing");
        }

        return response;
    }

    /**
     * @param targetSystem
     * @param sServiceName
     * @param level
     * @param level
     * @return
     * @throws Exception
     * @throws ConnectionManagerException
     * @throws IllegalArgumentException
     */
    protected String getUrl(NhinTargetSystemType targetSystem, String sServiceName, GATEWAY_API_LEVEL level)
            throws IllegalArgumentException, ConnectionManagerException, Exception {
        if (StringUtils.isBlank(targetSystem.getUseSpecVersion())) {
            throw new IllegalArgumentException("Required specification version guidance was null.");
        }
        UDDI_SPEC_VERSION version = UDDI_SPEC_VERSION.fromString(targetSystem.getUseSpecVersion());
        return getCMInstance().getEndpointURLByServiceNameSpecVersion(
                targetSystem.getHomeCommunity().getHomeCommunityId(), sServiceName, version);
    }

    protected ConnectionManager getCMInstance() {
        return ConnectionManagerCache.getInstance();
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

    /**
     * @param portDescriptor
     * @param assertion
     * @param url
     * @param target
     * @return
     */
    protected CONNECTClient<RespondingGatewayRetrievePortType> getCONNECTClientSecured(
            ServicePortDescriptor<RespondingGatewayRetrievePortType> portDescriptor, AssertionType assertion,
            String url, NhinTargetSystemType target) {
        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, assertion, url,
                target.getHomeCommunity().getHomeCommunityId(), NhincConstants.DOC_RETRIEVE_SERVICE_NAME);
    }
}
