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
package gov.hhs.fha.nhinc.admingui.services.impl;

import static gov.hhs.fha.nhinc.admingui.jee.jsf.UserAuthorizationListener.USER_INFO_SESSION_ATTRIBUTE;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.JKS_TYPE;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.TRUST_STORE_KEY;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.TRUST_STORE_PASSWORD_KEY;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.TRUST_STORE_TYPE_KEY;

import gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants;
import gov.hhs.fha.nhinc.admingui.services.CertificateManagerService;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;
import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateDTO;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManager;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateUtil;
import gov.hhs.fha.nhinc.callback.opensaml.X509CertificateHelper;
import gov.hhs.fha.nhinc.common.configadmin.CreateCSRRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.CreateCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.DeleteCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.EditCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.EditCertificateRequestType;
import gov.hhs.fha.nhinc.common.configadmin.ImportCertificateChainRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ImportCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ImportCertificateRequestType;
import gov.hhs.fha.nhinc.common.configadmin.ListCertificateType;
import gov.hhs.fha.nhinc.common.configadmin.ListCertificatesResponseMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListChainOfTrustRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListKeyStoresRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListKeyStoresResponseMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListTrustStoresRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListTrustStoresResponseMessageType;
import gov.hhs.fha.nhinc.common.configadmin.SimpleCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.SimpleCertificateResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.ConfigAssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.configadmin.EntityConfigAdminPortType;
import gov.hhs.fha.nhinc.configuration.ConfigAdminPortDescriptor;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.util.SHA2PasswordUtil;
import gov.hhs.fha.nhinc.util.UtilException;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class CertificateManagerServiceImpl implements CertificateManagerService {

    private static final String UTF_8 = "UTF-8";
    private static final Logger LOG = LoggerFactory.getLogger(CertificateManagerServiceImpl.class);
    private final CertificateManager cmHelper = CertificateManagerImpl.getInstance();
    private final X509CertificateHelper x509CertificateHelper = new X509CertificateHelper();
    private final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();
    private static final String UNABLE_TO_GET_CERTIFICATE = "Unable to get certificate details";

    @Override
    public List<CertificateDTO> fetchKeyStores() throws CertificateManagerException {
        try {
            return listKeyStore(AdminWSConstants.ADMIN_CERT_LIST_KEYSTORE);
        } catch (Exception ex) {
            LOG.error(UNABLE_TO_GET_CERTIFICATE, ex.getLocalizedMessage(), ex);
            throw new CertificateManagerException(UNABLE_TO_GET_CERTIFICATE);
        }

    }

    @Override
    public List<CertificateDTO> fetchTrustStores() throws CertificateManagerException {
        try {
            return listTrustStore(AdminWSConstants.ADMIN_CERT_LIST_TRUSTSTORE, false);
        } catch (Exception ex) {
            LOG.error(UNABLE_TO_GET_CERTIFICATE, ex.getLocalizedMessage(), ex);
            throw new CertificateManagerException(UNABLE_TO_GET_CERTIFICATE);
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
    public List<CertificateDTO> refreshKeyStores() throws CertificateManagerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CertificateDTO createCertificate(byte[] data) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
            X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
            return x509CertificateHelper.buildCertificate(cert);
        } catch (CertificateException | IOException ex) {
            LOG.error("Unable to extract a valid X509 certificate {}", ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    @Override
    public List<CertificateDTO> refreshTrustStores(boolean refreshCache) throws CertificateManagerException {
        try {
            return listTrustStore(AdminWSConstants.ADMIN_CERT_LIST_TRUSTSTORE, refreshCache);
        } catch (Exception e) {
            LOG.error(UNABLE_TO_GET_CERTIFICATE, e.getLocalizedMessage(), e);
            throw new CertificateManagerException(UNABLE_TO_GET_CERTIFICATE);
        }
    }

    /**
     * Checks if alias already exists in the truststore
     *
     * @param alias
     * @param certs
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
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    /**
     * import a given certificate into the TrustStore
     *
     * @param cert
     * @param refreshCache
     * @throws gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException
     */
    @Override
    public boolean importCertificate(CertificateDTO cert, boolean refreshCache, String hashToken)
        throws CertificateManagerException {
        final Map<String, String> trustStoreProperties = cmHelper.getTrustStoreSystemProperties();
        String storeType = trustStoreProperties.get(TRUST_STORE_TYPE_KEY);
        final String storeLoc = trustStoreProperties.get(TRUST_STORE_KEY);
        final String passkey = trustStoreProperties.get(TRUST_STORE_PASSWORD_KEY);
        boolean importStatus = false;
        if (storeType == null) {
            LOG.warn("{} is not defined. Switch to use JKS by default", TRUST_STORE_TYPE_KEY);
            storeType = JKS_TYPE;
        }

        if (StringUtils.isNotBlank(passkey) && !(JKS_TYPE.equals(storeType) && storeLoc == null) && cert != null) {
            importStatus = addCertificateToTrustStore(cert, refreshCache, hashToken);
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
        return importStatus;
    }

    private boolean addCertificateToTrustStore(CertificateDTO cert, boolean refreshCache, String hashToken)
        throws CertificateManagerException {
        boolean importStatus = false;
        try {
            ImportCertificateRequestMessageType requestMessage = createImportCertRequest(cert, refreshCache, hashToken);
            SimpleCertificateResponseMessageType response = (SimpleCertificateResponseMessageType) getClient()
                .invokePort(EntityConfigAdminPortType.class, AdminWSConstants.ADMIN_CERT_IMPORT, requestMessage);
            importStatus = response.isStatus();
        } catch (Exception ex) {
            throw new CertificateManagerException("Error sending import request message.", ex);
        }
        return importStatus;
    }

    private ImportCertificateRequestMessageType createImportCertRequest(CertificateDTO cert, boolean refreshCache,
        String hashToken) throws CertificateEncodingException, CertificateManagerException {
        ImportCertificateRequestMessageType message = new ImportCertificateRequestMessageType();
        ConfigAssertionType assertion = buildConfigAssertion();
        DataHandler data = cmHelper.transformToHandler(cert.getX509Cert().getEncoded());

        ImportCertificateRequestType request = new ImportCertificateRequestType();
        request.setAlias(cert.getAlias());
        request.setRefreshCache(refreshCache);
        request.setCertData(data);
        request.setHashToken(hashToken);

        message.setConfigAssertion(assertion);
        message.setImportCertRequest(request);
        return message;
    }

    @Override
    public SimpleCertificateResponseMessageType deleteCertificateFromTrustStore(String alias, String hashToken) throws
    CertificateManagerException {
        DeleteCertificateRequestMessageType request = new DeleteCertificateRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setHashToken(hashToken);
        request.setAlias(alias);
        try {
            return (SimpleCertificateResponseMessageType) getClient()
                .invokePort(EntityConfigAdminPortType.class, AdminWSConstants.ADMIN_CERT_DELETE, request);
        } catch (Exception e) {
            throw new CertificateManagerException("Error deleting the selected certificate.", e);
        }
    }

    private CONNECTClient<EntityConfigAdminPortType> getClient() throws Exception {
        String url = oProxyHelper
            .getAdapterEndPointFromConnectionManager(AdminWSConstants.ENTITY_CONFIG_ADMIN_SERVICE_NAME);
        ServicePortDescriptor<EntityConfigAdminPortType> portDescriptor = new ConfigAdminPortDescriptor();
        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url,
            new AssertionType());
    }

    private List<CertificateDTO> listTrustStore(String portName, boolean refreshCache)
        throws CertificateManagerException {
        ListTrustStoresRequestMessageType message = new ListTrustStoresRequestMessageType();
        ConfigAssertionType assertion = buildConfigAssertion();
        message.setConfigAssertion(assertion);
        message.setRefreshCertCache(refreshCache);

        ListTrustStoresResponseMessageType response;
        try {
            response = (ListTrustStoresResponseMessageType) getClient().invokePort(EntityConfigAdminPortType.class,
                portName, message);
        } catch (Exception e) {
            throw new CertificateManagerException("Error while fetching certificates.", e);
        }
        return soapResponseToDTO(response.getCertList());
    }

    private List<CertificateDTO> listKeyStore(String portName) throws CertificateManagerException {
        ListKeyStoresRequestMessageType message = new ListKeyStoresRequestMessageType();
        ConfigAssertionType assertion = buildConfigAssertion();
        message.setConfigAssertion(assertion);

        ListKeyStoresResponseMessageType response;
        try {
            response = (ListKeyStoresResponseMessageType) getClient().invokePort(EntityConfigAdminPortType.class,
                portName, message);
        } catch (Exception e) {
            throw new CertificateManagerException("Error while fetching certificates.", e);
        }
        return soapResponseToDTO(response.getCertList());
    }

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

    private static ConfigAssertionType buildConfigAssertion() {
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

    private static UserLogin getUser() {
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
    public boolean updateCertificate(String oldAlias, CertificateDTO cert, String hashToken)
        throws CertificateManagerException {
        SimpleCertificateResponseMessageType response = new SimpleCertificateResponseMessageType();
        try {
            EditCertificateRequestMessageType requestMessage = new EditCertificateRequestMessageType();
            EditCertificateRequestType certRequestParam = new EditCertificateRequestType();
            certRequestParam.setHashToken(hashToken);
            ConfigAssertionType assertion = buildConfigAssertion();

            certRequestParam.setOldAlias(oldAlias);
            certRequestParam.setNewAlias(cert.getAlias());

            requestMessage.setEditCertRequest(certRequestParam);
            requestMessage.setConfigAssertion(assertion);

            response = (SimpleCertificateResponseMessageType) getClient().invokePort(EntityConfigAdminPortType.class,
                AdminWSConstants.ADMIN_CERT_EDIT, requestMessage);

        } catch (Exception ex) {
            response.setStatus(false);
            response.setMessage(ex.getLocalizedMessage());
            throw new CertificateManagerException("Error sending import request message.", ex);
        }

        return response.isStatus();
    }

    @Override
    public String getHashToken(String trustStorePasskey) throws CertificateManagerException {
        UserLogin user = getUser();
        String hashToken = null;
        try {
            if (user != null) {
                hashToken = new String(
                    SHA2PasswordUtil.calculateHash(user.getUserName().getBytes(), trustStorePasskey.getBytes()), UTF_8);
            }
        } catch (UtilException | UnsupportedEncodingException e) {
            throw new CertificateManagerException("Error while calculating user pass hash token.", e);
        }
        return hashToken;
    }

    @Override
    public List<CertificateDTO> listChainOfTrust(String alias) throws CertificateManagerException {
        ListChainOfTrustRequestMessageType message = new ListChainOfTrustRequestMessageType();
        message.setConfigAssertion(buildConfigAssertion());
        message.setAlias(alias);

        ListCertificatesResponseMessageType response;
        try {
            response = (ListCertificatesResponseMessageType) getClient().invokePort(EntityConfigAdminPortType.class,
                AdminWSConstants.ADMIN_CERT_LIST_CHAINOFTRUST, message);
        } catch (Exception e) {
            throw new CertificateManagerException("Error while fetching certificates-chain.", e);
        }
        return sortChainCertificate(response.getCertList(), alias);
    }

    private List<CertificateDTO> sortChainCertificate(List<ListCertificateType> soapList, String alias) throws CertificateManagerException{
        List<CertificateDTO> tempList = soapResponseToDTO(soapList);
        List<CertificateDTO> retList = new LinkedList<>();

        CertificateDTO child = addCertByAlias(retList, tempList, alias);

        while(null != child){
            child = addCertByChild(retList, tempList, child);
        }
        return retList;

    }

    private CertificateDTO addCertByAlias(List<CertificateDTO> toList, List<CertificateDTO> fromList, String alias){
        for(CertificateDTO cert : fromList){
            if (StringUtils.isNotEmpty(cert.getAlias()) && StringUtils.isNotEmpty(alias)
                && cert.getAlias().equals(alias)) {
                toList.add(0, cert);
                fromList.remove(cert);
                return cert;
            }
        }
        return null;
    }

    private CertificateDTO addCertByChild(List<CertificateDTO> toList, List<CertificateDTO> fromList, CertificateDTO child){
        if (null != child && CollectionUtils.isNotEmpty(fromList)) {
            for(CertificateDTO cert : fromList){
                if (child.getAuthorityKeyID().equals(cert.getSubjectKeyID())) {
                    toList.add(0, cert);
                    fromList.remove(cert);
                    return cert;
                }
            }
        }

        return null;
    }

    @Override
    public SimpleCertificateResponseMessageType deleteTempKeystore() {
        try {
            SimpleCertificateRequestMessageType request = new SimpleCertificateRequestMessageType();
            request.setConfigAssertion(buildConfigAssertion());
            return (SimpleCertificateResponseMessageType) getClient().invokePort(EntityConfigAdminPortType.class,
                AdminWSConstants.ADMIN_CERT_DELETE_TEMPKEYSTORE, request);
        } catch (Exception ex) {
            LOG.error("error requesting gateway alias: {}", ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    @Override
    public boolean createCertificate(String alias, String commonName, String organizationalUnit,
        String organization, String countryName) {
        boolean status = false;
        try {
            CreateCertificateRequestMessageType request = new CreateCertificateRequestMessageType();
            request.setAlias(alias);
            request.setCommonName(commonName);
            request.setOrganizationalUnit(organizationalUnit);
            request.setOrganization(organization);
            request.setCountryName(countryName);
            request.setConfigAssertion(buildConfigAssertion());
            SimpleCertificateResponseMessageType response = (SimpleCertificateResponseMessageType) getClient()
                .invokePort(EntityConfigAdminPortType.class, AdminWSConstants.ADMIN_CERT_CREATE_CERTIFICATE, request);
            return response.isStatus();
        } catch (Exception ex) {
            LOG.error("error creating certificate for: {}", alias, ex);
        }
        return status;
    }

    @Override
    public SimpleCertificateResponseMessageType createCSR(String alias) {
        try {
            CreateCSRRequestMessageType request = new CreateCSRRequestMessageType();
            request.setAlias(alias);
            request.setConfigAssertion(buildConfigAssertion());
            return (SimpleCertificateResponseMessageType) getClient().invokePort(EntityConfigAdminPortType.class,
                AdminWSConstants.ADMIN_CERT_CREATE_CSR, request);
        } catch (Exception ex) {
            LOG.error("error creating CSR for: {}", alias, ex);
        }
        return null;
    }

    @Override
    public SimpleCertificateResponseMessageType importToKeystore(String alias, UploadedFile serverFile,
        Map<String, UploadedFile> intermediateFiles, UploadedFile rootFile) {
        try {
            ImportCertificateChainRequestMessageType request = buildRequestCertificateChain(alias, serverFile,
                intermediateFiles, rootFile);
            return (SimpleCertificateResponseMessageType) getClient().invokePort(EntityConfigAdminPortType.class,
                AdminWSConstants.ADMIN_CERT_IMPORT_TOKEYSTORE, request);
        } catch (Exception ex) {
            LOG.error("error importing to keystore for: {}", alias, ex);
        }
        return null;
    }

    @Override
    public SimpleCertificateResponseMessageType completeImportWizard() {
        try {
            SimpleCertificateRequestMessageType request = new SimpleCertificateRequestMessageType();
            request.setConfigAssertion(buildConfigAssertion());
            return (SimpleCertificateResponseMessageType) getClient().invokePort(EntityConfigAdminPortType.class,
                AdminWSConstants.ADMIN_CERT_COMPLETE_IMPORTWIZARD, request);
        } catch (Exception ex) {
            LOG.error("Error occured while calling completeImportWizard webservice: {}", ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    @Override
    public SimpleCertificateResponseMessageType listTemporaryAlias() {
        try {
            SimpleCertificateRequestMessageType request = new SimpleCertificateRequestMessageType();
            request.setConfigAssertion(buildConfigAssertion());
            return (SimpleCertificateResponseMessageType) getClient().invokePort(EntityConfigAdminPortType.class,
                AdminWSConstants.ADMIN_CERT_LIST_TEMPORARYALIAS, request);
        } catch (Exception ex) {
            LOG.error("Error occured while calling listTemporaryAlias webservice: {}", ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    private static ImportCertificateChainRequestMessageType buildRequestCertificateChain(String alias,
        UploadedFile serverFile, Map<String, UploadedFile> intermediateFiles, UploadedFile rootFile) {
        ImportCertificateChainRequestMessageType request = new ImportCertificateChainRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        if(StringUtils.isNotBlank(alias)) {
            request.setAlias(alias);
        }
        if (null != serverFile) {
            request.setServerCert(CertificateUtil.getDataHandlerForPem(serverFile.getContents()));
        }
        if (MapUtils.isNotEmpty(intermediateFiles) && null != rootFile) {
            request.setRootCert(CertificateUtil.getDataHandlerForPem(rootFile.getContents()));
            for (UploadedFile item : intermediateFiles.values()) {
                request.getIntermediateList().add(CertificateUtil.getDataHandlerForPem(item.getContents()));
            }
        }
        return request;
    }

    @Override
    public SimpleCertificateResponseMessageType undoImportKeystore(String alias,
        Map<String, UploadedFile> intermediateFiles, UploadedFile rootFile) {
        try {
            ImportCertificateChainRequestMessageType request = buildRequestCertificateChain(alias, null,
                intermediateFiles, rootFile);
            return (SimpleCertificateResponseMessageType) getClient().invokePort(EntityConfigAdminPortType.class,
                AdminWSConstants.ADMIN_CERT_UNDO_IMPORTKEYSTORE, request);
        } catch (Exception ex) {
            LOG.error("Error occured while calling undoImportKeystore webservice: {}", ex.getLocalizedMessage(), ex);
        }
        return null;
    }

}
