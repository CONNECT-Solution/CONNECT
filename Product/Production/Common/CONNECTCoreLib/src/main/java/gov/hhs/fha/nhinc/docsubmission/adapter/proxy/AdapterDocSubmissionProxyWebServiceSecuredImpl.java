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
package gov.hhs.fha.nhinc.docsubmission.adapter.proxy;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Map;

import gov.hhs.fha.nhinc.adapterxdrsecured.AdapterXDRSecuredPortType;
import gov.hhs.fha.nhinc.callback.openSAML.CertificateManager;
import gov.hhs.fha.nhinc.callback.openSAML.CertificateManagerImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xdr._2007.DocumentRepositoryXDRPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * 
 * @author Neil Webb
 */
public class AdapterDocSubmissionProxyWebServiceSecuredImpl implements AdapterDocSubmissionProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterxdrsecured";
    private static final String SERVICE_LOCAL_PART = "AdapterXDRSecured_Service";
    private static final String PORT_LOCAL_PART = "AdapterXDRSecured_Port";
    private static final String WSDL_FILE = "AdapterXDRSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adapterxdrsecured:ProvideAndRegisterDocumentSet-b";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterDocSubmissionProxyWebServiceSecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     * 
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected AdapterXDRSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction,
            AssertionType assertion) {
        AdapterXDRSecuredPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "DocumentSubmission_20-client-beans.xml" });
            port = (AdapterXDRSecuredPortType)context.getBean("adapterDocumentSubmissionPortType");
            
            HTTPConduit httpConduit = (HTTPConduit) ClientProxy.getClient(port).getConduit();
            TLSClientParameters tlsCP = new TLSClientParameters();
            tlsCP.setDisableCNCheck(true);
            
            CertificateManager cm = CertificateManagerImpl.getInstance();
            
            try {
                KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                String password = System.getProperty("javax.net.ssl.keyStorePassword");
                keyFactory.init(cm.getKeyStore(), password.toCharArray()); 
                KeyManager[] km = keyFactory.getKeyManagers(); 
                tlsCP.setKeyManagers(km); 
                
                TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()); 
                trustFactory.init(cm.getTrustStore()); 
                TrustManager[] tm = trustFactory.getTrustManagers(); 
                tlsCP.setTrustManagers(tm); 
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (KeyStoreException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            httpConduit.setTlsClientParameters(tlsCP);

            Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
            requestContext.put("assertion", assertion);
            
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction,
                    wsAddressingAction, assertion);
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

    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType msg,
            AssertionType assertion) {
        RegistryResponseType response = null;

        try {
            String url = oProxyHelper
                    .getAdapterEndPointFromConnectionManager(NhincConstants.ADAPTER_XDR_SECURED_SERVICE_NAME);
            if (NullChecker.isNotNullish(url)) {
                AdapterXDRSecuredPortType port = getPort(url, NhincConstants.XDR_ACTION, WS_ADDRESSING_ACTION,
                        assertion);

                if (msg == null) {
                    log.error("Message was null");
                } else if (port == null) {
                    log.error("port was null");
                } else {
                    response = (RegistryResponseType) oProxyHelper.invokePort(port, AdapterXDRSecuredPortType.class,
                            "provideAndRegisterDocumentSetb", msg);
                }
            } else {
                log.error("Failed to call the web service (" + NhincConstants.ADAPTER_XDR_SECURED_SERVICE_NAME
                        + ").  The URL is null.");
            }
        } catch (Exception ex) {
            log.error("Error sending Adapter Doc Submission Secured message: " + ex.getMessage(), ex);
            response = new RegistryResponseType();
            response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        }

        return response;
    }
}
