/**
 * Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services. All
 * rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. Neither the name of the United States Government nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package gov.hhs.fha.nhinc.admingui.managed.direct;

import gov.hhs.fha.nhinc.admingui.managed.direct.helpers.CertContainer;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectCertificate;
import gov.hhs.fha.nhinc.admingui.services.DirectService;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.nhind.config.AddCertificates;
import org.nhind.config.Certificate;
import org.nhind.config.CertificateGetOptions;
import org.nhind.config.EntityStatus;
import org.nhind.config.ListCertificates;
import org.nhind.config.RemoveCertificates;
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
    private String certStatus;

    private DirectCertificate selectedCert;

    public List<DirectCertificate> getCertificates() {
        ListCertificates cert = new ListCertificates();
        
        cert.setLastCertificateId(0);
        cert.setMaxResutls(0);
        cert.setOptions(new CertificateGetOptions());
        
        List<DirectCertificate> certList = new ArrayList<DirectCertificate>();
        List<Certificate> certsResponse = directService.listCertificate(cert);

        for (Certificate c : certsResponse) {
            CertContainer cc = new CertContainer(c.getData());            
            certList.add(new DirectCertificate(c, cc.getThumbprint()));
        }

        return certList;
    }

    public void deleteCertificate() {
        RemoveCertificates removeCert = new RemoveCertificates();
        removeCert.getCertificateIds().add(selectedCert.getId());

        selectedCert = null;
        directService.deleteCertificate(removeCert);
    }

    public void certFileUpload(FileUploadEvent event) {
        certFile = event.getFile();
    }

    public void addCertificate() {
        AddCertificates addCert = new AddCertificates();
        Certificate certificate = new Certificate();
        certificate.setData(certFile.getContents());
        certificate.setStatus(EntityStatus.NEW);
        addCert.getCerts().add(certificate);
        directService.addCertificate(addCert);
    }

    public UploadedFile getCertFile() {
        return certFile;
    }

    public void setCertFile(UploadedFile certFile) {
        this.certFile = certFile;
    }

    public String getCertStatus() {
        return certStatus;
    }

    public void setCertStatus(String certStatus) {
        this.certStatus = certStatus;
    }

    public DirectCertificate getSelectedCert() {
        return selectedCert;
    }

    public void setSelectedCert(DirectCertificate selectedCert) {
        this.selectedCert = selectedCert;
    }
}
