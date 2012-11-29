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
package gov.hhs.fha.nhinc.direct.edge.proxy;

import javax.mail.internet.MimeMessage;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.direct.edge.proxy.service.DirectEdgeSoapServicePortDescriptor;
import gov.hhs.fha.nhinc.direct.transform.MimeMessageTransformer;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.DocumentRepositoryPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nhindirect.stagent.MessageEnvelope;
import org.nhindirect.stagent.mail.Message;

/**
 * 
 * @author jhoppesc
 */
public class DirectEdgeProxySoapImpl implements DirectEdgeProxy {
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = null;

    public DirectEdgeProxySoapImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    public RegistryResponseType provideAndRegisterDocumentSetB(MessageEnvelope messageEnvelope) {
        log.debug("Begin provideAndRegisterDocumentSetB");
        RegistryResponseType response = null;

        try {
            Message message = messageEnvelope.getMessage();
            ProvideAndRegisterDocumentSetRequestType prdsrt = null;
            
            if (message instanceof MimeMessage) {
                MimeMessageTransformer transformer = new MimeMessageTransformer();

                prdsrt = transformer.transform(message);
            } else {
                log.warn("MimeMessage was expected but not recieved.");
            }
            
            String url = oProxyHelper.getAdapterEndPointFromConnectionManager(NhincConstants.DIRECT_SOAP_EDGE_SERVICE_NAME);
            if (NullChecker.isNotNullish(url)) {
                
                AdapterProvideAndRegisterDocumentSetRequestType request = new AdapterProvideAndRegisterDocumentSetRequestType();
                request.setProvideAndRegisterDocumentSetRequest(prdsrt);
                request.setAssertion(null);
                
                ServicePortDescriptor<DocumentRepositoryPortType> portDescriptor = new DirectEdgeSoapServicePortDescriptor();
                
                CONNECTClient<DocumentRepositoryPortType> client = CONNECTClientFactory.getInstance()
                        .getCONNECTClientUnsecured(portDescriptor, url, null);
                
                response = (RegistryResponseType) client.invokePort(DocumentRepositoryPortType.class,
                        "provideAndRegisterDocumentSetb", request);
                            
            } else {
                log.error("Failed to call the web service (" + NhincConstants.DIRECT_SOAP_EDGE_SERVICE_NAME
                        + ").  The URL is null.");
            }
        } catch (Exception ex) {
            log.error("Error sending Adapter Doc Submission Unsecured message: " + ex.getMessage(), ex);
            response = new RegistryResponseType();
            response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        }

        log.debug("End provideAndRegisterDocumentSetB");
        return response;
    }

}
