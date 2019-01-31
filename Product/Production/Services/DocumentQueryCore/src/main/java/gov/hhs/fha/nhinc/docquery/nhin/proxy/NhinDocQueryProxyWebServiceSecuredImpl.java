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
package gov.hhs.fha.nhinc.docquery.nhin.proxy;

import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.description.NhinDocQueryServicePortDescriptor;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.fha.nhinc.xdcommon.XDCommonResponseHelper;
import gov.hhs.fha.nhinc.xdcommon.XDCommonResponseHelper.ErrorCodes;
import ihe.iti.xds_b._2007.RespondingGatewayQueryPortType;
import javax.xml.ws.WebServiceException;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the component proxy for calling the NHIN doc query web service.
 *
 * @author jhoppesc, Les Westberg
 */
public class NhinDocQueryProxyWebServiceSecuredImpl implements NhinDocQueryProxy {

    private static final String UNABLE_TO_CALL = "Unable to call Nhin Service";
    private static final Logger LOG = LoggerFactory.getLogger(NhinDocQueryProxyWebServiceSecuredImpl.class);

    /**
     * @return WebServiceProxyHelper.
     */
    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * @param apiLevel The gateway apiLevel to be implemented for webService (g0,g1)
     * @return NhinDocQueryServicePortDescriptor Comprises of NameSpaceUri,ServiceName,Port,WSDLFile and
     * WSAddressingAction.
     */
    public ServicePortDescriptor<RespondingGatewayQueryPortType> getServicePortDescriptor(
        final NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        return new NhinDocQueryServicePortDescriptor();
    }

    public CONNECTClient<RespondingGatewayQueryPortType> getCONNECTClientSecured(
        final ServicePortDescriptor<RespondingGatewayQueryPortType> portDescriptor, final AssertionType assertion,
        final String url, final NhinTargetSystemType target) {
        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, assertion, url,
            target, NhincConstants.DOC_QUERY_SERVICE_NAME);
    }

    protected ExchangeManager getCMInstance() {
        return ExchangeManager.getInstance();
    }

    /**
     * Calls the respondingGatewayCrossGatewayQuery method of the web service.
     *
     * @param request The AdhocQuery Request received.
     * @param assertion Assertion received.
     * @param target NhinTarget community to forward DocQuery Request.
     * @throws Exception Throws Exception.
     * @return The AdhocQUery response from the web service.
     */
    @NwhinInvocationEvent(beforeBuilder = AdhocQueryRequestDescriptionBuilder.class, afterReturningBuilder
        = AdhocQueryResponseDescriptionBuilder.class, serviceType = "Document Query", version = "")
    @Override
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(final AdhocQueryRequest request,
        final AssertionType assertion, final NhinTargetSystemType target) throws Exception {
        AdhocQueryResponse response = null;
        try {
            String url = target.getUrl();
            if (NullChecker.isNullish(url)) {
                if (StringUtils.isBlank(target.getUseSpecVersion())) {
                    throw new IllegalArgumentException("Required specification version guidance was null.");
                }
                final UDDI_SPEC_VERSION version = UDDI_SPEC_VERSION.fromString(target.getUseSpecVersion());
                url = getCMInstance().getEndpointURL(
                    target.getHomeCommunity().getHomeCommunityId(), NhincConstants.DOC_QUERY_SERVICE_NAME, version,
                    target.getExchangeName());
                LOG.debug("After target system URL look up. URL for service: {} is: {}" ,NhincConstants.DOC_QUERY_SERVICE_NAME, url);
            }

            final ServicePortDescriptor<RespondingGatewayQueryPortType> portDescriptor = getServicePortDescriptor(
                NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);

            final CONNECTClient<RespondingGatewayQueryPortType> client = getCONNECTClientSecured(portDescriptor,
                assertion, url, target);

            response = (AdhocQueryResponse) client.invokePort(RespondingGatewayQueryPortType.class,
                "respondingGatewayCrossGatewayQuery", request);

        } catch (final ExchangeManagerException e) {
            final XDCommonResponseHelper helper = new XDCommonResponseHelper();
            final RegistryResponseType registryError = helper.createError(e.getLocalizedMessage(),
                ErrorCodes.XDSRepositoryError, NhincConstants.INIT_MULTISPEC_LOC_ENTITY_DQ);

            response = new AdhocQueryResponse();
            response.setRegistryObjectList(new RegistryObjectListType());
            response.setStatus(registryError.getStatus());
            response.setRegistryErrorList(registryError.getRegistryErrorList());

            throw new ErrorEventException(e, response, UNABLE_TO_CALL);

        } catch (final WebServiceException wse) {
            final XDCommonResponseHelper helper = new XDCommonResponseHelper();
            final String endpointAvailableError = NhincConstants.INIT_MULTISPEC_ERROR_NO_ENDPOINT_AVAILABLE
                + target.getHomeCommunity().getHomeCommunityId() + ".";
            final RegistryResponseType registryError = helper.createError(endpointAvailableError,
                ErrorCodes.XDSRegistryError, NhincConstants.INIT_MULTISPEC_LOC_ENTITY_DQ);
            response = new AdhocQueryResponse();
            response.setRegistryObjectList(new RegistryObjectListType());
            response.setStatus(registryError.getStatus());
            response.setRegistryErrorList(registryError.getRegistryErrorList());

            throw new ErrorEventException(wse, response, UNABLE_TO_CALL);

        } catch (final Exception ex) {
            throw new ErrorEventException(ex, UNABLE_TO_CALL);
        }
        return response;
    }
}
