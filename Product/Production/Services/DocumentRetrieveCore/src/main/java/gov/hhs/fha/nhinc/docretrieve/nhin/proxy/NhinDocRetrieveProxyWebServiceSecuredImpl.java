/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.docretrieve.MessageGenerator;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.nhin.proxy.description.NhinDocRetrieveServicePortDescriptor;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
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
import javax.xml.ws.WebServiceException;
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
     * @param assertion
     * @param targetSystem The target system where the message is being sent to.
     * @param level
     * @return The document(s) that were retrieved.
     */
    @NwhinInvocationEvent(beforeBuilder = RetrieveDocumentSetRequestTypeDescriptionBuilder.class, afterReturningBuilder
        = RetrieveDocumentSetResponseTypeDescriptionBuilder.class, serviceType = "Retrieve Document", version = "")
    @Override
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
        final RetrieveDocumentSetRequestType request, final AssertionType assertion,
        final NhinTargetSystemType targetSystem, final GATEWAY_API_LEVEL level) {

        String url;
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        final String sServiceName = NhincConstants.DOC_RETRIEVE_SERVICE_NAME;

        try {
            if (request != null) {
                LOG.debug("Before target system URL look up.");
                url = getUrl(targetSystem, sServiceName, level);
                LOG.debug("After target system URL look up. URL for service: " + sServiceName + " is: " + url);

                if (NullChecker.isNotNullish(url)) {
                    final ServicePortDescriptor<RespondingGatewayRetrievePortType> portDescriptor
                        = getServicePortDescriptor(
                            NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);

                    final CONNECTClient<RespondingGatewayRetrievePortType> client = getCONNECTClientSecured(
                        portDescriptor, assertion, url, targetSystem);
                    client.enableMtom();

                    response = (RetrieveDocumentSetResponseType) client.invokePort(
                        RespondingGatewayRetrievePortType.class, "respondingGatewayCrossGatewayRetrieve", request);
                } else {
                    throw new WebServiceException("Could not determine URL for Nhin Doc Retrieve endpoint");
                }
            } else {
                throw new IllegalArgumentException("Request Message must be provided");
            }
        } catch (final ExchangeManagerException e) {
            final XDCommonResponseHelper helper = new XDCommonResponseHelper();
            final RegistryResponseType error = helper.createError(e.getLocalizedMessage(),
                ErrorCodes.XDSRepositoryError, NhincConstants.INIT_MULTISPEC_LOC_ENTITY_DR);

            response = new RetrieveDocumentSetResponseType();
            response.setRegistryResponse(error);
            throw new ErrorEventException(e, response, "Unable to call Nhin Doc Retrieve");
        } catch (final Exception e) {
            response = MessageGenerator.getInstance()
                .createRegistryResponseError("Nhin Document Retrieve Processing");
            throw new ErrorEventException(e, response, "Unable to call Nhin Doc Retrieve");
        }

        return response;
    }

    /**
     * @param targetSystem
     * @param sServiceName
     * @param level
     * @return
     * @throws Exception
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     * @throws IllegalArgumentException
     */
    protected String getUrl(final NhinTargetSystemType targetSystem, final String sServiceName,
        final GATEWAY_API_LEVEL level) throws IllegalArgumentException, Exception {
        if (StringUtils.isBlank(targetSystem.getUseSpecVersion())) {
            throw new IllegalArgumentException("Required specification version guidance was null.");
        }
        final UDDI_SPEC_VERSION version = UDDI_SPEC_VERSION.fromString(targetSystem.getUseSpecVersion());
        return getCMInstance().getEndpointURL(
            targetSystem.getHomeCommunity().getHomeCommunityId(), sServiceName, version, targetSystem.getExchangeName());
    }

    protected ExchangeManager getCMInstance() {
        return ExchangeManager.getInstance();
    }

    public ServicePortDescriptor<RespondingGatewayRetrievePortType> getServicePortDescriptor(
        final NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        return new NhinDocRetrieveServicePortDescriptor();
    }

    /**
     * @param portDescriptor
     * @param assertion
     * @param url
     * @param target
     * @return
     */
    protected CONNECTClient<RespondingGatewayRetrievePortType> getCONNECTClientSecured(
        final ServicePortDescriptor<RespondingGatewayRetrievePortType> portDescriptor,
        final AssertionType assertion, final String url, final NhinTargetSystemType target) {
        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, assertion, url,
            target, NhincConstants.DOC_RETRIEVE_SERVICE_NAME);
    }
}
