/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.services.impl;

import static gov.hhs.fha.nhinc.admingui.jee.jsf.UserAuthorizationListener.USER_INFO_SESSION_ATTRIBUTE;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.JKS_TYPE;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.TRUST_STORE_KEY;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.TRUST_STORE_PASSWORD_KEY;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.TRUST_STORE_TYPE_KEY;

import gov.hhs.fha.nhinc.admingui.services.CertificateManagerService;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;
import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateDTO;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManager;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl;
import gov.hhs.fha.nhinc.callback.opensaml.X509CertificateHelper;
import gov.hhs.fha.nhinc.common.configadmin.ConfigAssertionType;
import gov.hhs.fha.nhinc.common.configadmin.DeleteCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.EditCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.EditCertificateRequestType;
import gov.hhs.fha.nhinc.common.configadmin.ImportCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ImportCertificateRequestType;
import gov.hhs.fha.nhinc.common.configadmin.ListCertificateType;
import gov.hhs.fha.nhinc.common.configadmin.ListKeyStoresRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListKeyStoresResponseMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListTrustStoresRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListTrustStoresResponseMessageType;
import gov.hhs.fha.nhinc.common.configadmin.SimpleCertificateResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.configadmin.EntityConfigAdminPortType;
import gov.hhs.fha.nhinc.configuration.ConfigAdminPortDescriptor;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class CertificateManagerServiceImpl implements CertificateManagerService {

    private static final Logger LOG = LoggerFactory.getLogger(CertificateManagerServiceImpl.class);
    private final CertificateManager cmHelper = CertificateManagerImpl.getInstance();
    private final X509CertificateHelper x509CertificateHelper = new X509CertificateHelper();

    private final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    @Override
    public List<CertificateDTO> fetchKeyStores() throws CertificateManagerException {
        try {
            return listKeyStore(SamlConstants.ADMIN_CERT_LIST_KEYSTORE);
        } catch (Exception ex) {
            LOG.error("Unable to get certificate details", ex.getLocalizedMessage(), ex);
            throw new CertificateManagerException("Unable to get certificate details.");
        }

    }

    @Override
    public List<CertificateDTO> fetchTrustStores() throws CertificateManagerException {
        try {
            return listTrustStore(SamlConstants.ADMIN_CERT_LIST_TRUSTSTORE);
        } catch (Exception ex) {
            LOG.error("Unable to get certificate details", ex.getLocalizedMessage(), ex);
            throw new CertificateManagerException("Unable to get certificate details.");
        }
    }

    @Override
    public String getKeyStoreLocation() {
        return cmHelper.getKeyStoreLocation();
    }

    @Override
    public String getTrustStoreLocation() {
        return cmHelper.getTrustStoreLocation();
    }

    @Override
    public List<CertificateDTO> refreshKeyStores() {
        try {
            return listTrustStore(SamlConstants.ADMIN_CERT_LIST_KEYSTORE);
        } catch (Exception e) {
            LOG.error("Unable to get certificate details", e.getLocalizedMessage(), e);
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public CertificateDTO createCertificate(byte[] data) {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(data);
            X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
            return x509CertificateHelper.buildCertificate(cert);
        } catch (CertificateException ex) {
            LOG.error("Unable to extract a valid X509 certificate {}", ex.getLocalizedMessage(), ex);
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException ex) {
                LOG.error("Unable to close the inputstream {}", ex.getLocalizedMessage(), ex);
            }
        }
        return null;
    }

    @Override
    public List<CertificateDTO> refreshTrustStores() {
        try {
            return listTrustStore(SamlConstants.ADMIN_CERT_LIST_TRUSTSTORE);
        } catch (Exception e) {
            LOG.error("Unable to get certificate details", e.getLocalizedMessage(), e);
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * Checks if alias already exists in the truststore
     *
     * @param cert
     * @return
     */
    @Override
    public boolean isAliasInUse(String alias, List<CertificateDTO> certs) {
        for (CertificateDTO trustCert : certs) {
            if (trustCert.getAlias().equalsIgnoreCase(alias)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLeafOnlyCertificate(CertificateDTO cert) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * import a given certificate into the TrustStore
     *
     * @param cert
     * @throws gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException
     */
    @Override
    public void importCertificate(CertificateDTO cert) throws CertificateManagerException {
        final Map<String, String> trustStoreProperties = cmHelper.getTrustStoreSystemProperties();
        String storeType = trustStoreProperties.get(TRUST_STORE_TYPE_KEY);
        final String storeLoc = trustStoreProperties.get(TRUST_STORE_KEY);
        final String passkey = trustStoreProperties.get(TRUST_STORE_PASSWORD_KEY);
        if (storeType == null) {
            LOG.warn("{} is not defined. Switch to use JKS by default", TRUST_STORE_TYPE_KEY);
            storeType = JKS_TYPE;
        }

        if (StringUtils.isNotBlank(passkey) && !(JKS_TYPE.equals(storeType) && storeLoc == null) && cert != null) {
            addCertificateToTrustStore(cert);
        } else {
            LOG.info("importCertificate -- validation failed");
            if (JKS_TYPE.equals(storeType) && storeLoc == null) {
                LOG.error("{} is not defined.", TRUST_STORE_KEY);
            }
            if (StringUtils.isBlank(passkey)) {
                LOG.error("invalid password");
            }
            if (null == cert) {
                LOG.error("Certificate is null");
            }
        }
    }


    private boolean addCertificateToTrustStore(CertificateDTO cert) throws CertificateManagerException {
        try {
            ImportCertificateRequestMessageType requestMessage = createImportCertRequest(cert);
            SimpleCertificateResponseMessageType response = (SimpleCertificateResponseMessageType) getClient()
                .invokePort(EntityConfigAdminPortType.class, SamlConstants.ADMIN_CERT_IMPORT, requestMessage);
            return response.isStatus();
        } catch (Exception ex) {
            throw new CertificateManagerException("Error sending import request message.", ex);
        }
    }

    @Override
    public boolean deleteCertificateFromTrustStore(String alias) throws
    CertificateManagerException {
        DeleteCertificateRequestMessageType request = new DeleteCertificateRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setAlias(alias);
        try {
            SimpleCertificateResponseMessageType response = (SimpleCertificateResponseMessageType) getClient()
                .invokePort(EntityConfigAdminPortType.class, SamlConstants.ADMIN_CERT_DELETE, request);
            return response.isStatus();
        } catch (Exception e) {
            throw new CertificateManagerException("Error deleting the selected certificate.", e);
        }
    }

    @Override
    public boolean validateTrustStorePassKey(String passkey) {
        return cmHelper.getTrustStoreSystemProperties().get(TRUST_STORE_PASSWORD_KEY).equals(passkey);
    }

    private CONNECTClient<EntityConfigAdminPortType> getClient() throws Exception {

        String url = oProxyHelper
            .getAdapterEndPointFromConnectionManager(NhincConstants.ENTITY_CONFIG_ADMIN_SERVICE_NAME);

        ServicePortDescriptor<EntityConfigAdminPortType> portDescriptor = new ConfigAdminPortDescriptor();

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url,
            new AssertionType());
    }

    private ImportCertificateRequestMessageType createImportCertRequest(CertificateDTO cert) throws CertificateEncodingException {
        ImportCertificateRequestMessageType message = new ImportCertificateRequestMessageType();
        ConfigAssertionType assertion = buildConfigAssertion();
        DataHandler data = cmHelper.transformToHandler(cert.getX509Cert().getEncoded());

        ImportCertificateRequestType request = new ImportCertificateRequestType();
        request.setAlias(cert.getAlias());
        request.setCertData(data);

        message.setConfigAssertion(assertion);
        message.setImportCertRequest(request);
        return message;
    }

    private List<CertificateDTO> listTrustStore(String portName) throws Exception {
        ListTrustStoresRequestMessageType message = new ListTrustStoresRequestMessageType();
        ConfigAssertionType assertion = buildConfigAssertion();
        message.setConfigAssertion(assertion);

        ListTrustStoresResponseMessageType response = (ListTrustStoresResponseMessageType) getClient()
            .invokePort(EntityConfigAdminPortType.class, portName, message);
        return soapResponseToDTO(response.getCertList());
    }

    private List<CertificateDTO> listKeyStore(String portName) throws Exception {
        ListKeyStoresRequestMessageType message = new ListKeyStoresRequestMessageType();
        ConfigAssertionType assertion = buildConfigAssertion();
        message.setConfigAssertion(assertion);

        ListKeyStoresResponseMessageType response = (ListKeyStoresResponseMessageType) getClient()
            .invokePort(EntityConfigAdminPortType.class, portName, message);
        return soapResponseToDTO(response.getCertList());
    }

    /**
     * @param response
     * @return
     * @throws CertificateManagerException
     */
    private List<CertificateDTO> soapResponseToDTO(List<ListCertificateType> certList)
        throws CertificateManagerException {
        List<CertificateDTO> certs = new ArrayList<>();
        long id = 0;
        for (ListCertificateType cert : certList) {
            CertificateDTO dto = x509CertificateHelper
                .buildCertificate(cmHelper.getCertificateFromByteCode(cert.getCertData()));
            dto.setId(id++);
            dto.setAlias(cert.getAlias());
            certs.add(dto);
        }
        return certs;
    }


    private ConfigAssertionType buildConfigAssertion() {
        ConfigAssertionType assertion = new ConfigAssertionType();
        UserLogin user = getUser();
        if (user != null) {
            UserType configUser = new UserType();
            configUser.setUserName(user.getUserName());
            assertion.setUserInfo(configUser);
        }
        assertion.setConfigInstance(new DateTime().toString());
        assertion.setAuthMethod(SamlConstants.ADMIN_AUTH_METHOD);

        return assertion;
    }

    private UserLogin getUser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null && facesContext.getViewRoot() != null) {
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
            if (session != null) {
                return (UserLogin) session.getAttribute(USER_INFO_SESSION_ATTRIBUTE);
            }
        }
        return null;
    }

    @Override
    public boolean updateCertificate(String oldAlias, CertificateDTO cert)
        throws CertificateManagerException {
        SimpleCertificateResponseMessageType response = new SimpleCertificateResponseMessageType();
        try {
            EditCertificateRequestMessageType requestMessage = new EditCertificateRequestMessageType();
            EditCertificateRequestType certRequestParam = new EditCertificateRequestType();
            ConfigAssertionType assertion = buildConfigAssertion();
            DataHandler data = cmHelper.transformToHandler(cert.getX509Cert().getEncoded());

            certRequestParam.setOldAlias(oldAlias);
            certRequestParam.setNewAlias(cert.getAlias());

            requestMessage.setEditCertRequest(certRequestParam);
            requestMessage.setConfigAssertion(assertion);

            response = (SimpleCertificateResponseMessageType) getClient()
                .invokePort(EntityConfigAdminPortType.class, SamlConstants.ADMIN_CERT_EDIT, requestMessage);

        } catch (Exception ex) {
            response.setStatus(false);
            response.setMessage(ex.getLocalizedMessage());
            throw new CertificateManagerException("Error sending import request message.", ex);
        }

        return response.isStatus();
    }
}
