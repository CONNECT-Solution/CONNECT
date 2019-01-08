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
package gov.hhs.fha.nhinc.direct.edge.proxy;

import gov.hhs.fha.nhinc.direct.DirectException;
import gov.hhs.fha.nhinc.direct.edge.proxy.service.DirectEdgeSoapServicePortDescriptor;
import gov.hhs.fha.nhinc.direct.transform.MimeMessageTransformer;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.fha.nhinc.xdcommon.XDCommonResponseHelper;
import ihe.iti.xds_b._2007.DocumentRepositoryPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.mail.internet.MimeMessage;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jhoppesc
 */
/**
 * @author mweaver
 *
 */
public class DirectEdgeProxySoapImpl implements DirectEdgeProxy {

    private static final Logger LOG = LoggerFactory.getLogger(DirectEdgeProxySoapImpl.class);
    private final WebServiceProxyHelper oProxyHelper;

    /**
     * @param oProxyHelper web service proxy helper for soap edge clients.
     */
    public DirectEdgeProxySoapImpl(WebServiceProxyHelper oProxyHelper) {
        this.oProxyHelper = oProxyHelper;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxy#provideAndRegisterDocumentSetB
     * (javax.mail.internet.MimeMessage)
     */
    @Override
    public RegistryResponseType provideAndRegisterDocumentSetB(MimeMessage message) {
        LOG.debug("Begin provideAndRegisterDocumentSetB");
        RegistryResponseType response = null;

        try {
            ProvideAndRegisterDocumentSetRequestType prdsrt = null;

            if (message instanceof MimeMessage) {
                prdsrt = new MimeMessageTransformer().transform(message);
            } else {
                LOG.warn("MimeMessage was expected but not recieved.");
            }

            String url = oProxyHelper
                .getAdapterEndPointFromConnectionManager(NhincConstants.DIRECT_SOAP_EDGE_SERVICE_NAME);
            if (NullChecker.isNotNullish(url)) {

                ServicePortDescriptor<DocumentRepositoryPortType> portDescriptor
                = new DirectEdgeSoapServicePortDescriptor();

                CONNECTClient<DocumentRepositoryPortType> client = getClient(portDescriptor, url);

                response = (RegistryResponseType) client.invokePort(DocumentRepositoryPortType.class,
                    "documentRepositoryProvideAndRegisterDocumentSetB", prdsrt);
            } else {
                handleError("Failed to call the web service (" + NhincConstants.DIRECT_SOAP_EDGE_SERVICE_NAME
                    + ").  The URL is null.", message);
            }
        } catch (Exception ex) {
            handleError("Error sending Adapter Doc Submission Unsecured message: ", ex, message);
        }

        LOG.debug("End provideAndRegisterDocumentSetB");
        return response;
    }

    private void handleError(String errorMessage, MimeMessage mimeMessage) {
        handleError(errorMessage, null, mimeMessage);
    }

    private static void handleError(String errorMessage, Throwable e, MimeMessage mimeMessage) {
        XDCommonResponseHelper helper = new XDCommonResponseHelper();
        if (e != null) {
            LOG.error(helper.createError(errorMessage + e.getLocalizedMessage()).toString());
            throw new DirectException(errorMessage, e, mimeMessage);
        } else {
            LOG.error(helper.createError(errorMessage).toString());
            throw new DirectException(errorMessage, mimeMessage);
        }
    }

    /**
     * @param portDescriptor description object representing the WSDL port for the service to be called.
     * @param url endpoint url to be called.
     * @return a client for a DocumentRepositoryPortType WSDL port based on the descriptor and url.
     */
    protected CONNECTClient<DocumentRepositoryPortType> getClient(
        ServicePortDescriptor<DocumentRepositoryPortType> portDescriptor, String url) {

        return CONNECTClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, null);
    }
}
