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
package gov.hhs.fha.nhinc.docregistry.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docregistry.adapter.proxy.description.AdapterComponentDocRegistryServicePortDescriptor;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.DocumentRegistryPortType;
import javax.xml.ws.WebServiceException;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author svalluripalli
 */
public class AdapterComponentDocRegistryProxyWebServiceUnsecuredImpl implements AdapterComponentDocRegistryProxy {
    private static final Logger LOG = LoggerFactory
            .getLogger(AdapterComponentDocRegistryProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterComponentDocRegistryProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    public static ServicePortDescriptor<DocumentRegistryPortType> getServicePortDescriptor() {
        return new AdapterComponentDocRegistryServicePortDescriptor();
    }

    /**
     *
     * @param request
     * @return AdhocQueryResponse
     */
    @Override
    public AdhocQueryResponse registryStoredQuery(final AdhocQueryRequest msg, final AssertionType assertion) {
        LOG.debug("Begin registryStoredQuery");
        AdhocQueryResponse response = null;

        try {
            final String url = oProxyHelper
                    .getAdapterEndPointFromConnectionManager(NhincConstants.ADAPTER_DOC_REGISTRY_SERVICE_NAME);
            if (NullChecker.isNotNullish(url)) {

                if (msg == null) {
                    throw new IllegalArgumentException("Request Message must be provided");
                } else {
                    final ServicePortDescriptor<DocumentRegistryPortType> portDescriptor = getServicePortDescriptor();

                    final CONNECTClient<DocumentRegistryPortType> client = CONNECTClientFactory.getInstance()
                            .getCONNECTClientUnsecured(portDescriptor, url, assertion);

                    response = (AdhocQueryResponse) client.invokePort(DocumentRegistryPortType.class,
                            "documentRegistryRegistryStoredQuery", msg);
                }
            } else {
                throw new WebServiceException("Could not determine URL for Doc Registry Adapter endpoint");
            }
        } catch (final Exception ex) {
            response = new AdhocQueryResponse();
            response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
            response.setRegistryObjectList(new RegistryObjectListType());

            final RegistryError registryError = new RegistryError();
            registryError.setCodeContext("Processing Adapter Doc Query document query");
            registryError.setErrorCode("XDSRegistryError");
            registryError.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
            response.setRegistryErrorList(new RegistryErrorList());
            response.getRegistryErrorList().getRegistryError().add(registryError);

            throw new ErrorEventException(ex, response, "Unable to call Doc Query Adapter");
        }

        LOG.debug("End registryStoredQuery");
        return response;
    }
}
