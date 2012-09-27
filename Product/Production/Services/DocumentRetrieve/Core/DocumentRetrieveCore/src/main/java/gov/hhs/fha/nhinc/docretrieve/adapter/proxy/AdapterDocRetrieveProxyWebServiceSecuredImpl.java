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
package gov.hhs.fha.nhinc.docretrieve.adapter.proxy;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.adapterdocretrievesecured.AdapterDocRetrieveSecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.service.AdapterDocRetrieveSecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

/**
 * This is the secured web service implementation of the Adapter Doc Retrieve component proxy.
 *
 * @author Neil Webb, Les Westberg
 */
public class AdapterDocRetrieveProxyWebServiceSecuredImpl implements AdapterDocRetrieveProxy {

    private Log log = null;
    private final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Default constructor.
     */
    public AdapterDocRetrieveProxyWebServiceSecuredImpl() {
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

    /**
     * Retrieve the document(s)
     *
     * @param request The identifiers for the document(s) to be retrieved.
     * @param assertion The assertion data.
     * @return The document(s) that were retrieved.
     */
    @Override
    public RetrieveDocumentSetResponseType retrieveDocumentSet(RetrieveDocumentSetRequestType request,
            AssertionType assertion) {
        String url = null;
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        String sServiceName = NhincConstants.ADAPTER_DOC_RETRIEVE_SECURED_SERVICE_NAME;

        try {
            if (request != null) {
                log.debug("Before target system URL look up.");

                url = oProxyHelper.getAdapterEndPointFromConnectionManager(sServiceName);
                log.debug("After target system URL look up. URL for service: " + sServiceName + " is: " + url);

                if (NullChecker.isNotNullish(url)) {
                    ServicePortDescriptor<AdapterDocRetrieveSecuredPortType> portDescriptor = new AdapterDocRetrieveSecuredServicePortDescriptor();
                    CONNECTClient<AdapterDocRetrieveSecuredPortType> client = getCONNECTClientSecured(portDescriptor,
                            url, assertion);
                    response = (RetrieveDocumentSetResponseType) client.invokePort(
                            AdapterDocRetrieveSecuredPortType.class, "respondingGatewayCrossGatewayRetrieve", request);
                } else {
                    log.error("Failed to call the web service (" + sServiceName + ").  The URL is null.");
                }
            } else {
                log.error("Failed to call the web service (" + sServiceName + ").  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web service (" + sServiceName + ").  An unexpected exception occurred.  "
                    + "Exception: " + e.getMessage(), e);
        }

        return response;
    }

    protected CONNECTClient<AdapterDocRetrieveSecuredPortType> getCONNECTClientSecured(
            ServicePortDescriptor<AdapterDocRetrieveSecuredPortType> portDescriptor, String url, AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, url, assertion);
    }
}
