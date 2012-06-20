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
package gov.hhs.fha.nhinc.docrepositoryadapter.proxy;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.DocumentRepositoryPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * 
 * 
 * @author Neil Webb
 */
public class AdapterDocumentRepositoryWebServiceProxy implements AdapterDocumentRepositoryProxy {

    private static String ADAPTER_PROPERTY_FILE_NAME = "adapter";
    private static String XDS_HOME_COMMUNITY_ID_PROPERTY = "XDSbHomeCommunityId";
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(AdapterDocumentRepositoryWebServiceProxy.class);

    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "DocumentRepository_Service";
    private static final String PORT_LOCAL_PART = "DocumentRepository_Port_Soap";
    private static final String WSDL_FILE = "AdapterComponentDocRepository.wsdl";
    private WebServiceProxyHelper oProxyHelper = null;

    public RetrieveDocumentSetResponseType retrieveDocumentSet(RetrieveDocumentSetRequestType request) {
        RetrieveDocumentSetResponseType response = null;
        try { // Call Web Service Operation
            String url = null;

            String xdsbHomeCommunityId = PropertyAccessor.getInstance().getProperty(ADAPTER_PROPERTY_FILE_NAME,
                    XDS_HOME_COMMUNITY_ID_PROPERTY);
            if (log.isDebugEnabled()) {
                log.debug("Value of " + XDS_HOME_COMMUNITY_ID_PROPERTY + " retrieved from the "
                        + ADAPTER_PROPERTY_FILE_NAME + ".properties file: " + xdsbHomeCommunityId);
            }
            if (NullChecker.isNotNullish(xdsbHomeCommunityId)) {
                url = oProxyHelper.getAdapterEndPointFromConnectionManager(xdsbHomeCommunityId,
                        NhincConstants.ADAPTER_DOC_REPOSITORY_SERVICE_NAME);
            }

            DocumentRepositoryPortType port = getPort(url);

            response = (RetrieveDocumentSetResponseType) oProxyHelper.invokePort(port,
                    DocumentRepositoryPortType.class, "documentRepositoryRetrieveDocumentSet", request);

        } catch (Exception ex) {
            log.error("Error sending message to the adapter document repository: " + ex.getMessage(), ex);
        }

        return response;
    }

    public RegistryResponseType provideAndRegisterDocumentSet(ProvideAndRegisterDocumentSetRequestType request) {
        RegistryResponseType result = null;
        try { // Call Web Service Operation

            String url = null;

            String xdsbHomeCommunityId = PropertyAccessor.getInstance().getProperty(ADAPTER_PROPERTY_FILE_NAME,
                    XDS_HOME_COMMUNITY_ID_PROPERTY);
            if (xdsbHomeCommunityId != null && !xdsbHomeCommunityId.equals("")) {
                url = oProxyHelper.getAdapterEndPointFromConnectionManager(xdsbHomeCommunityId,
                        NhincConstants.ADAPTER_DOC_REPOSITORY_SERVICE_NAME);
            }

            DocumentRepositoryPortType port = getPort(url);

            result = (RegistryResponseType) oProxyHelper.invokePort(port, DocumentRepositoryPortType.class,
                    "documentRepositoryProvideAndRegisterDocumentSetB", request);

        } catch (Exception ex) {
            log.error("Error sending message to the adapter document repository: " + ex.getMessage(), ex);
        }
        return result;
    }

    private DocumentRepositoryPortType getPort(String url) {
        DocumentRepositoryPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), DocumentRepositoryPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, null, null);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;

    }

    /**
     * Retrieve the service class for this web service.
     * 
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

}
