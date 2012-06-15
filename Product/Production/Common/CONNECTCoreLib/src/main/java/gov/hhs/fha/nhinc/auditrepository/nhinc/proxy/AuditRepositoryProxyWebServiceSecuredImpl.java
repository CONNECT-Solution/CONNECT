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
package gov.hhs.fha.nhinc.auditrepository.nhinc.proxy;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Map;

import gov.hhs.fha.nhinc.callback.openSAML.CertificateManager;
import gov.hhs.fha.nhinc.callback.openSAML.CertificateManagerImpl;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author Jon Hoppesch
 */
public class AuditRepositoryProxyWebServiceSecuredImpl implements AuditRepositoryProxy {
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhinccomponentauditrepository";
    private static final String SERVICE_LOCAL_PART = "AuditRepositoryManagerSecuredService";
    private static final String PORT_LOCAL_PART = "AuditRepositoryManagerSecuredPort";
    private static final String WSDL_FILE = "NhincComponentAuditRepositorySecured.wsdl";
    private static final String WS_ADDRESSING_ACTION_LOG = "urn:gov:hhs:fha:nhinc:nhinccomponentauditrepository:LogEventSecuredRequest";
    private static final String WS_ADDRESSING_ACTION_FIND = "urn:gov:hhs:fha:nhinc:nhinccomponentauditrepository:QueryAuditEventsSecuredRequest";
    private Log log = null;

    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    public AuditRepositoryProxyWebServiceSecuredImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public FindCommunitiesAndAuditEventsResponseType auditQuery(FindCommunitiesAndAuditEventsRequestType request) {
        String url = null;
        FindCommunitiesAndAuditEventsResponseType result = new FindCommunitiesAndAuditEventsResponseType();

        try {
            if (request != null) {

                log.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME);
                log.debug("After target system URL look up. URL for service: "
                        + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME + " is: " + url);
                if (NullChecker.isNotNullish(url)) {
                    AuditRepositoryManagerSecuredPortType port = getPort(url,
                            NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME, WS_ADDRESSING_ACTION_FIND,
                            request.getAssertion());
                    result = (FindCommunitiesAndAuditEventsResponseType) oProxyHelper.invokePort(port,
                            AuditRepositoryManagerSecuredPortType.class, "queryAuditEvents", request);
                } else {
                    log.error("Failed to call the web service (" + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME
                            + ").  The URL is null.");
                }
            }
        } catch (Exception e) {
            log.error("Failed to call the web service (" + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME
                    + ").  An unexpected exception occurred.  " + "Exception: " + e.getMessage(), e);
        }

        return result;
    }

    public AcknowledgementType auditLog(LogEventRequestType request, AssertionType assertion) {
        log.debug("Entering AuditRepositoryProxyWebServiceSecured.auditLog(...)");
        String url = null;
        AcknowledgementType result = new AcknowledgementType();
        LogEventSecureRequestType secureRequest = new LogEventSecureRequestType();
        if (request.getAuditMessage() == null) {
            log.error("Audit Request is null");
        }
        secureRequest.setAuditMessage(request.getAuditMessage());
        secureRequest.setDirection(request.getDirection());
        secureRequest.setInterface(request.getInterface());

        try {
            if (request != null) {

                log.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME);
                log.debug("After target system URL look up. URL for service: "
                        + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME + " is: " + url);
                if (NullChecker.isNotNullish(url)) {
                    AuditRepositoryManagerSecuredPortType port = getPort(url,
                            NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME, WS_ADDRESSING_ACTION_LOG, assertion);
                    result = (AcknowledgementType) oProxyHelper.invokePort(port,
                            AuditRepositoryManagerSecuredPortType.class, "logEvent", secureRequest);
                } else {
                    log.error("Failed to call the web service (" + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME
                            + ").  The URL is null.");
                }
            }
        } catch (Exception e) {
            log.error("Failed to call the web service (" + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME
                    + ").  An unexpected exception occurred.  " + "Exception: " + e.getMessage(), e);
        }

        log.debug("In AuditRepositoryProxyWebServiceSecured.auditLog(...) - completed called to ConnectionManager to retrieve endpoint.");

        return result;
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

    /**
     * This method retrieves and initializes the port.
     * 
     * @param url The URL for the web service.
     * @param serviceAction The action for the web service.
     * @param wsAddressingAction The action assigned to the input parameter for the web service operation.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected AuditRepositoryManagerSecuredPortType getPort(String url, String serviceAction,
            String wsAddressingAction, AssertionType assertion) {
        
        AuditRepositoryManagerSecuredPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");
            
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "AuditRepositoryManager-client-beans.xml" });
            port = (AuditRepositoryManagerSecuredPortType)context.getBean("auditSecuredPortType");
            
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

}
