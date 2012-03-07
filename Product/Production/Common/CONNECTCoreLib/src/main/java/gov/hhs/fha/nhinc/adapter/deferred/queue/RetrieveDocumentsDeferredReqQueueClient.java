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
package gov.hhs.fha.nhinc.adapter.deferred.queue;

import gov.hhs.fha.nhinc.adapterdocretrievereqqueueprocess.AdapterDocRetrieveDeferredReqQueueProcessPortType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.gateway.adapterdocretrievereqqueueprocess.DocRetrieveDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.adapterdocretrievereqqueueprocess.DocRetrieveDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.gateway.adapterdocretrievereqqueueprocess.SuccessOrFailType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author richard.ettema
 */
public class RetrieveDocumentsDeferredReqQueueClient {

    private static final Log log = LogFactory.getLog(RetrieveDocumentsDeferredReqQueueClient.class);

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterdocretrievereqqueueprocess";
    private static final String SERVICE_LOCAL_PART = "AdapterDocRetrieveDeferredReqQueueProcess";
    private static final String PORT_LOCAL_PART = "AdapterDocRetrieveDeferredReqQueueProcessPort";
    private static final String WSDL_FILE = "AdapterDocRetrieveDeferredReqQueueProcess.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adapterdocretrievereqqueueprocess:ProcessDocRetrieveDeferredReqQueue";
    private static final String SERVICE_NAME = NhincConstants.ADAPTER_DOC_RETRIEVE_DEFERRED_REQ_QUEUE_PROCESS_SERVICE_NAME;
        
    private WebServiceProxyHelper proxyHelper;

    /**
     * Default constructor
     */
    public RetrieveDocumentsDeferredReqQueueClient() {
        proxyHelper = new WebServiceProxyHelper();
    }

    /**
     * Send queue process request for a deferred doc retrieve queue record
     * 
     * @param messageId
     * @return queue process response
     */
    public DocRetrieveDeferredReqQueueProcessResponseType processDocRetrieveDeferredReqQueue(String messageId) {

        String msgText = "";

        DocRetrieveDeferredReqQueueProcessResponseType response = new DocRetrieveDeferredReqQueueProcessResponseType();
        SuccessOrFailType sof = new SuccessOrFailType();
        sof.setSuccess(Boolean.FALSE);
        response.setSuccessOrFail(sof);

        try {
            String endpointURL = getUrl(SERVICE_NAME);

            if (NullChecker.isNotNullish(endpointURL)) {
                AdapterDocRetrieveDeferredReqQueueProcessPortType port = getPort(endpointURL, SERVICE_NAME);
                
                BindingProvider bp = (BindingProvider) port;
                // (Optional) Configure RequestContext with endpoint's URL
                Map<String, Object> rc = bp.getRequestContext();
                rc.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);

                DocRetrieveDeferredReqQueueProcessRequestType request = new DocRetrieveDeferredReqQueueProcessRequestType();
                request.setMessageId(messageId);
                response = (DocRetrieveDeferredReqQueueProcessResponseType) proxyHelper.invokePort(
                        port, AdapterDocRetrieveDeferredReqQueueProcessPortType.class,
                        "processDocRetrieveDeferredReqQueue", request);
            } else {
                msgText = "Endpoint URL not found for local home community and service name ["
                        + NhincConstants.ADAPTER_DOC_RETRIEVE_DEFERRED_REQ_QUEUE_PROCESS_SERVICE_NAME + "]";
                log.error(msgText);
                response.setResponse(msgText);
            }
        } catch (PropertyAccessException ex) {
            msgText = "Exception accessing gateway property for home community";
            log.error(msgText, ex);
            response.setResponse(msgText);
        } catch (Exception ex) {
            msgText = "Exception occurred during deferred retrieve documents queue processing";
            log.error(msgText, ex);
            response.setResponse(msgText);
        }

        return response;
    }
    
    private Service getService() {
        try {

            return proxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
        } catch (Throwable t) {
            log.error("Error creating service: " + t.getMessage(), t);
        }
        return null;
    }
    
    protected String getUrl(String serviceName) throws ConnectionManagerException {
        return proxyHelper.getAdapterEndPointFromConnectionManager(serviceName);
    }

    /**
     * This method retrieves and initializes the port.
     * 
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected AdapterDocRetrieveDeferredReqQueueProcessPortType getPort(String url, String serviceName) {
        AdapterDocRetrieveDeferredReqQueueProcessPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterDocRetrieveDeferredReqQueueProcessPortType.class);
            proxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, WS_ADDRESSING_ACTION, null);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

}
