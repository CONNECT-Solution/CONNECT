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
package gov.hhs.fha.nhinc.policyengine.adapter.proxy;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import gov.hhs.fha.nhinc.adapterpolicyenginesecured.AdapterPolicyEngineSecuredPortType;
import gov.hhs.fha.nhinc.callback.openSAML.CertificateManager;
import gov.hhs.fha.nhinc.callback.openSAML.CertificateManagerImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

/**
 * 
 * @author Neil Webb
 */
public class PolicyEngineProxyWebServiceSecuredImpl implements PolicyEngineProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterpolicyenginesecured";
    private static final String SERVICE_LOCAL_PART = "AdapterPolicyEngineSecured";
    private static final String WSDL_FILE = "AdapterPolicyEngineSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adapterpolicyenginesecured:body";
    private static final String CLIENT_POLICY_ENGINE_SPRING_FILE = "PolicyEngine-client.xml";
    private static final String POLICY_ENGINE_BEAN = "adapterPolicyEngineSecuredPortType";

    private WebServiceProxyHelper oProxyHelper = null;

    public PolicyEngineProxyWebServiceSecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    private AdapterPolicyEngineSecuredPortType getPort(String url, String wsAddressingAction, AssertionType assertion) {
        AdapterPolicyEngineSecuredPortType port = null;

        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");
            // port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART),
            // AdapterPolicyEngineSecuredPortType.class);
            port = createPort(assertion);

            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url,
                    NhincConstants.POLICY_ENGINE_ACTION, wsAddressingAction, assertion);
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

    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion) {
        log.debug("Begin PolicyEngineWebServiceProxySecuredImpl.checkPolicy");
        CheckPolicyResponseType response = null;

        try {
            log.debug("Before target system URL look up.");
            String serviceName = NhincConstants.POLICYENGINE_SERVICE_SECURED_NAME;
            String url = oProxyHelper.getAdapterEndPointFromConnectionManager(serviceName);
            log.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);

            if (NullChecker.isNotNullish(url)) {
                CheckPolicyRequestSecuredType securedRequest = new CheckPolicyRequestSecuredType();
                if (checkPolicyRequest != null) {
                    securedRequest.setRequest(checkPolicyRequest.getRequest());
                }
                AdapterPolicyEngineSecuredPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);
                response = (CheckPolicyResponseType) oProxyHelper.invokePort(port,
                        AdapterPolicyEngineSecuredPortType.class, "checkPolicy", securedRequest);
            } else {
                log.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        } catch (Exception ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.POLICYENGINE_SERVICE_NAME
                    + " for local home community");
            log.error(ex.getMessage());
        }

        log.debug("End PolicyEngineWebServiceProxySecuredImpl.checkPolicy");
        return response;
    }

    private AdapterPolicyEngineSecuredPortType createPort(AssertionType assertion) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { CLIENT_POLICY_ENGINE_SPRING_FILE });
        AdapterPolicyEngineSecuredPortType port = (AdapterPolicyEngineSecuredPortType) context
                .getBean(POLICY_ENGINE_BEAN);
           
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
        
        
        
        return port;

    }
}
