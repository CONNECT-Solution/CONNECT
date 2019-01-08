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
package gov.hhs.fha.nhinc.admingui.managed.direct;

import gov.hhs.fha.nhinc.admingui.managed.direct.helpers.CertContainer;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectCertificate;
import gov.hhs.fha.nhinc.admingui.services.DirectService;
import gov.hhs.fha.nhinc.direct.config.AddCertificates;
import gov.hhs.fha.nhinc.direct.config.Certificate;
import gov.hhs.fha.nhinc.direct.config.CertificateGetOptions;
import gov.hhs.fha.nhinc.direct.config.EntityStatus;
import gov.hhs.fha.nhinc.direct.config.ListCertificates;
import gov.hhs.fha.nhinc.direct.config.RemoveCertificates;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jasonasmith
 */
@ManagedBean(name = "directCertBean")
@ViewScoped
@Component
public class DirectCertBean {

    @Autowired
    private DirectService directService;

    private UploadedFile certFile;

    private DirectCertificate selectedCert;

    private List<DirectCertificate> directCertificate;

    /**
     *
     * @return
     */
    public List<DirectCertificate> getCertificates() {
        if (directCertificate == null) {
            refreshCertificates();
        }
        return directCertificate;
    }

    /**
     *
     */
    public void deleteCertificate() {
        RemoveCertificates removeCert = new RemoveCertificates();
        removeCert.getCertificateIds().add(selectedCert.getId());

        selectedCert = null;
        directService.deleteCertificate(removeCert);
        refreshCertificates();
    }

    /**
     *
     * @param event
     */
    public void certFileUpload(FileUploadEvent event) {
        certFile = event.getFile();
    }

    /**
     *
     * @param event
     */
    public void addCertificate(ActionEvent event) {
        if (certFile != null) {
            AddCertificates addCert = new AddCertificates();
            Certificate certificate = new Certificate();
            certificate.setData(certFile.getContents());
            certificate.setStatus(EntityStatus.ENABLED);
            addCert.getCerts().add(certificate);
            directService.addCertificate(addCert);
            refreshCertificates();
        } else {
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().addMessage("certificateAddError", new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "ERROR", "Choose and upload a certificate before submitting."));
        }
        certFile = null;
    }

    /**
     *
     * @return
     */
    public UploadedFile getCertFile() {
        return certFile;
    }

    /**
     *
     * @param certFile
     */
    public void setCertFile(UploadedFile certFile) {
        this.certFile = certFile;
    }

    /**
     *
     */
    protected void refreshCertificates() {
        ListCertificates cert = new ListCertificates();

        cert.setLastCertificateId(0);
        cert.setMaxResutls(0);
        cert.setOptions(new CertificateGetOptions());

        directCertificate = new ArrayList<>();
        List<Certificate> certsResponse = directService.listCertificate(cert);

        for (Certificate c : certsResponse) {
            CertContainer cc = new CertContainer(c.getData());
            directCertificate.add(new DirectCertificate(c, cc.getThumbprint()));
        }
    }

    /**
     *
     * @return
     */
    public DirectCertificate getSelectedCert() {
        return selectedCert;
    }

    /**
     *
     * @param selectedCert
     */
    public void setSelectedCert(DirectCertificate selectedCert) {
        this.selectedCert = selectedCert;
    }
}
