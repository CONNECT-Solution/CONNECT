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

import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManager;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.description.NhinDocQueryServicePortDescriptor;
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
import org.apache.log4j.Logger;

/**
 * This class is the component proxy for calling the NHIN doc query web service.
 *
 * @author jhoppesc, Les Westberg
 */
public class NhinDocQueryProxyWebServiceSecuredImpl implements NhinDocQueryProxy {

    private static final Logger LOG = Logger.getLogger(NhinDocQueryProxyWebServiceSecuredImpl.class);

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
        NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        switch (apiLevel) {
            case LEVEL_g0:
                return new NhinDocQueryServicePortDescriptor();
            default:
                return new NhinDocQueryServicePortDescriptor();
        }
    }

    public CONNECTClient<RespondingGatewayQueryPortType> getCONNECTClientSecured(
        ServicePortDescriptor<RespondingGatewayQueryPortType> portDescriptor, AssertionType assertion, String url,
        NhinTargetSystemType target) {
        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, assertion, url,
            target.getHomeCommunity().getHomeCommunityId(), NhincConstants.DOC_QUERY_SERVICE_NAME);
    }

    protected ConnectionManager getCMInstance() {
        return ConnectionManagerCache.getInstance();
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
    @NwhinInvocationEvent(beforeBuilder = AdhocQueryRequestDescriptionBuilder.class, afterReturningBuilder = AdhocQueryResponseDescriptionBuilder.class, serviceType = "Document Query", version = "")
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest request, AssertionType assertion,
        NhinTargetSystemType target) throws Exception {
        AdhocQueryResponse response = null;
        try {
            String url = target.getUrl();
            if (NullChecker.isNullish(url)) {
                if (StringUtils.isBlank(target.getUseSpecVersion())) {
                    throw new Exception("Required specification version guidance was null.");
                }
                UDDI_SPEC_VERSION version = UDDI_SPEC_VERSION.fromString(target.getUseSpecVersion());
                url = getCMInstance().getEndpointURLByServiceNameSpecVersion(
                    target.getHomeCommunity().getHomeCommunityId(), NhincConstants.DOC_QUERY_SERVICE_NAME, version);
                LOG.debug("After target system URL look up. URL for service: " + NhincConstants.DOC_QUERY_SERVICE_NAME
                    + " is: " + url);
            }

            ServicePortDescriptor<RespondingGatewayQueryPortType> portDescriptor = getServicePortDescriptor(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);

            CONNECTClient<RespondingGatewayQueryPortType> client = getCONNECTClientSecured(portDescriptor, assertion,
                url, target);

            response = (AdhocQueryResponse) client.invokePort(RespondingGatewayQueryPortType.class,
                "respondingGatewayCrossGatewayQuery", request);

        } catch (ConnectionManagerException e) {
            XDCommonResponseHelper helper = new XDCommonResponseHelper();
            RegistryResponseType registryError = helper.createError(e.getLocalizedMessage(),
                ErrorCodes.XDSRepositoryError, NhincConstants.INIT_MULTISPEC_LOC_ENTITY_DR);

            response = new AdhocQueryResponse();
            response.setRegistryObjectList(new RegistryObjectListType());
            response.setStatus(registryError.getStatus());
            response.setRegistryErrorList(registryError.getRegistryErrorList());

        } catch (WebServiceException wse) {
            XDCommonResponseHelper helper = new XDCommonResponseHelper();
            String endpointAvailableError = NhincConstants.INIT_MULTISPEC_ERROR_NO_ENDPOINT_AVAILABLE + target.getHomeCommunity().getHomeCommunityId()+".";
            RegistryResponseType registryError = helper.createError(endpointAvailableError,
                ErrorCodes.XDSRegistryError, NhincConstants.INIT_MULTISPEC_LOC_ENTITY_DQ);
            response = new AdhocQueryResponse();
            response.setRegistryObjectList(new RegistryObjectListType());
            response.setStatus(registryError.getStatus());
            response.setRegistryErrorList(registryError.getRegistryErrorList());

        } catch (Exception ex) {
            LOG.error("Error calling respondingGatewayCrossGatewayQuery: " + ex.getMessage(), ex);
            throw ex;
        }
        return response;
    }
}
