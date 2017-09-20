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
package gov.hhs.fha.nhinc.admingui.managed;

import gov.hhs.fha.nhinc.admingui.event.model.Certificate;
import gov.hhs.fha.nhinc.admingui.services.CertificateManagerService;
import gov.hhs.fha.nhinc.admingui.services.impl.CertificateManagerServiceImpl;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author tjafri
 */
@ManagedBean(name = "certificateBean")
@ViewScoped
public class CertficateBean {

    private List<Certificate> keystores;
    private List<Certificate> truststores;
    private final CertificateManagerService service;
    private Certificate selectedCertificate;
    private String keyStoreLocation;
    private String trustStoreLocation;
    private String keyStoreMsg;
    private String trustStoreMsg;
    private static final String KEYSTORE_REFRESH_MSG = "KeyStore listing refreshed";
    private static final String TRUSTSTORE_REFRESH_MSG = "TrustStore listing refreshed";

    public CertficateBean() {
        service = new CertificateManagerServiceImpl();
        fetchKeyStore();
        fetchTrustStore();
    }

    public String getKeyStoreLocation() {
        keyStoreLocation = service.getKeyStoreLocation();
        return keyStoreLocation;
    }

    public String getTrustStoreLocation() {
        trustStoreLocation = service.getTrustStoreLocation();
        return trustStoreLocation;
    }

    public List<Certificate> getKeystores() {
        return keystores;
    }

    public Certificate getSelectedCertificate() {
        return selectedCertificate;
    }

    public void setSelectedCertificate(Certificate selectedCertificate) {
        this.selectedCertificate = selectedCertificate;
    }

    public void refreshKeyStore() {
        keystores = service.refreshKeyStores();
        FacesContext.getCurrentInstance().addMessage(keyStoreMsg, new FacesMessage(FacesMessage.SEVERITY_INFO,
            "INFO", KEYSTORE_REFRESH_MSG));
    }

    public void refreshTrustStore() {
        truststores = service.refreshTrustStores();
        FacesContext.getCurrentInstance().addMessage(trustStoreMsg, new FacesMessage(FacesMessage.SEVERITY_INFO,
            "INFO", TRUSTSTORE_REFRESH_MSG));
    }

    public void deleteCertificate() {
        throw new UnsupportedOperationException();
    }

    public List<Certificate> getTruststores() {
        return truststores;
    }

    public String getKeyStoreMsg() {
        return keyStoreMsg;
    }

    public void setKeyStoreMsg(String keyStoreMsg) {
        this.keyStoreMsg = keyStoreMsg;
    }

    public String getTrustStoreMsg() {
        return trustStoreMsg;
    }

    public void setTrustStoreMsg(String trustStoreMsg) {
        this.trustStoreMsg = trustStoreMsg;
    }

    private void fetchKeyStore() {
        keystores = service.fetchKeyStores();
    }

    private void fetchTrustStore() {
        truststores = service.fetchTrustStores();
    }
}
