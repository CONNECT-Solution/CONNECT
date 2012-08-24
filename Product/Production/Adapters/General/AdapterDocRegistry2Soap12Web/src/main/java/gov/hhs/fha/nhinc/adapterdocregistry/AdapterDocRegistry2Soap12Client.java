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
package gov.hhs.fha.nhinc.adapterdocregistry;

import ihe.iti.xds_b._2007.DocumentRegistryPortType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.adapterdocregistry.adapter.proxy.AdapterDocRegistry2Soap12PortDescriptor;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

/**
 * This class calls a SOAP 1.2 enabled document registry given a SOAP 1.1 registry stored query request message.
 *
 * @author Anand Sastry
 */
public class AdapterDocRegistry2Soap12Client {
    private static Log log = LogFactory.getLog(AdapterDocRegistry2Soap12Client.class);
    private static String ADAPTER_XDS_REG_DEFAULT_SERVICE_URL = "http://localhost:8080/axis2/services/xdsregistryb";

    // CONSTANTS - Were not created in NhincConstants to simplify provisioning of this component
    // as an adapter between CONNECT and SOAP 1.2 Registry.
    public static final String WS_REGISTRY_STORED_QUERY_ACTION = "urn:ihe:iti:2007:RegistryStoredQuery";
    public static final String ADAPTER_XDS_REG_SERVICE_NAME = "adapterxdsbdocregistrysoap12";

    /**
     * Default constructor.
     */
    public AdapterDocRegistry2Soap12Client() {

    }

    /**
     * This method connects to a soap 1.2 enabled document registry and retrieves metadata.
     *
     * @param body A AdhocQueryRequest object containing key criteria to query for registry metadata.
     *
     * @return Returns a AdhocQueryResponse containing the desired metadata.
     *
     */

    public AdhocQueryResponse documentRegistryRegistryStoredQuery(AdhocQueryRequest body) {

        AdhocQueryResponse response = null;

        log.debug("Entering AdapterDocRegistry2Soap12Client.documentRegistryRegistryStoredQuery() method");

        try {
            // get a connection to the soap 1.2 registryStoreQuery document web service
            ServicePortDescriptor<DocumentRegistryPortType> portDescriptor = new AdapterDocRegistry2Soap12PortDescriptor();

            CONNECTClient<DocumentRegistryPortType> client = getCONNECTClientUnsecured(portDescriptor,
                    WS_REGISTRY_STORED_QUERY_ACTION);


            // call the soap 1.2 retrieve document web service
            response = (AdhocQueryResponse) client.invokePort(DocumentRegistryPortType.class,"documentRegistryRegisterDocumentSetB",null);
            log.debug("RetrieveDocumentSetRequest Response = " + ((response != null) ? response.getStatus() : "null"));
        } catch (Exception e) {
            String sErrorMessage = "Failed to execute registry stored query from the soap 1.2 web service.  Error: "
                    + e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        log.debug("Leaving AdapterDocRegistry2Soap12Client.documentRegistryRegistryStoredQuery() method");
        return response;
    }

    protected CONNECTClient<DocumentRegistryPortType> getCONNECTClientUnsecured(
            ServicePortDescriptor<DocumentRegistryPortType> portDescriptor, String url) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, null);
    }
}
