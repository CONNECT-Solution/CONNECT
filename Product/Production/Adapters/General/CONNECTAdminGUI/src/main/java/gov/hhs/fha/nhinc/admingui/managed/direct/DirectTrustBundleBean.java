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

import gov.hhs.fha.nhinc.admingui.managed.TabBean;
import gov.hhs.fha.nhinc.admingui.services.DirectService;
import gov.hhs.fha.nhinc.direct.config.Certificate;
import gov.hhs.fha.nhinc.direct.config.TrustBundle;
import gov.hhs.fha.nhinc.direct.config.TrustBundleAnchor;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jasonasmith
 */
@ManagedBean(name = "directTrustBundleBean")
@ViewScoped
@Component
public class DirectTrustBundleBean {

    @Autowired
    private DirectService directService;

    private String tbName;
    private String tbUrl;
    private String tbRefreshInterval;

    private UploadedFile tbCert;

    private TrustBundle selectedTb;
    private List<TrustBundle> trustbundles;

    /**
     *
     * @return
     */
    public List<TrustBundle> getTrustBundles() {
        if (trustbundles == null) {
            refreshTrustBundle();
        }
        return trustbundles;
    }

    /**
     *
     */
    public void deleteTrustBundle() {
        List<Long> ids = new ArrayList<>();
        ids.add(selectedTb.getId());

        directService.disassociateTrustBundleFromDomains(selectedTb.getId());
        directService.deleteTrustBundles(ids);

        TabBean tabs = (TabBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tabBean");

        if (tabs != null) {
            tabs.setDirectTabIndex(3);
        }
        refreshTrustBundle();
    }

    /**
     *
     * @param event
     */
    public void addTrustBundle(ActionEvent event) {
        int refreshValue = 0;

        if (tbRefreshInterval != null && tbRefreshInterval.length() > 0) {
            refreshValue = Integer.parseInt(tbRefreshInterval);
        }

        TrustBundle tb = new TrustBundle();
        tb.setBundleName(tbName);
        tb.setBundleURL(tbUrl);
        tb.setRefreshInterval(refreshValue);

        if (tbCert != null) {
            tb.setSigningCertificateData(tbCert.getContents());
            tbCert = null;
        }

        directService.addTrustBundle(tb);
        refreshTrustBundle();
        tbName = null;
        tbUrl = null;
        tbRefreshInterval = null;
    }

    /**
     *
     * @param event
     */
    public void editTrustBundle(ActionEvent event) {
        Certificate cert = null;

        if (selectedTb.getSigningCertificateData() != null) {
            cert = new Certificate();
            cert.setData(selectedTb.getSigningCertificateData());
        }
        directService.updateTrustBundle(selectedTb.getId(), selectedTb.getBundleName(), selectedTb.getBundleURL(), cert,
                selectedTb.getRefreshInterval());
    }

    public void refreshBundle(ActionEvent event) {
        if (selectedTb != null) {
            directService.refreshTrustBundle(selectedTb.getId());
            refreshTrustBundle();
        }
    }

    /**
     *
     * @return
     */
    public List<TrustBundleAnchor> getSelectedTrustBundleAnchors() {
        if (selectedTb != null) {
            return selectedTb.getTrustBundleAnchors();
        }
        return null;
    }

    /**
     *
     */
    public void showEdit() {
        if (selectedTb != null) {
            RequestContext.getCurrentInstance().execute("PF('tbEditDlg').show()");
        }
    }

    /**
     *
     */
    public void showTrustBundleAnchors() {
        if (selectedTb != null) {
            RequestContext.getCurrentInstance().execute("PF('tbAnchorDlg').show()");
        }
    }

    /**
     *
     */
    public void showDelConfirm() {
        if (selectedTb != null) {
            RequestContext.getCurrentInstance().execute("PF('tbConfirmDelDlg').show()");
        }
    }

    /**
     *
     */
    protected void refreshTrustBundle() {
        trustbundles = directService.getTrustBundles(true);
    }

    /**
     *
     * @return
     */
    public String getTbName() {
        return tbName;
    }

    /**
     *
     * @param tbName
     */
    public void setTbName(String tbName) {
        this.tbName = tbName;
    }

    /**
     *
     * @return
     */
    public String getTbUrl() {
        return tbUrl;
    }

    /**
     *
     * @param tbUrl
     */
    public void setTbUrl(String tbUrl) {
        this.tbUrl = tbUrl;
    }

    /**
     *
     * @return
     */
    public String getTbRefreshInterval() {
        return tbRefreshInterval;
    }

    /**
     *
     * @param tbRefreshInterval
     */
    public void setTbRefreshInterval(String tbRefreshInterval) {
        this.tbRefreshInterval = tbRefreshInterval;
    }

    /**
     *
     * @return
     */
    public UploadedFile getTbCert() {
        return tbCert;
    }

    /**
     *
     * @param tbCert
     */
    public void setTbCert(UploadedFile tbCert) {
        this.tbCert = tbCert;
    }

    /**
     *
     * @param event
     */
    public void tbCertUpload(FileUploadEvent event) {
        tbCert = event.getFile();
    }

    /**
     *
     * @return
     */
    public TrustBundle getSelectedTb() {
        return selectedTb;
    }

    /**
     *
     * @param selectedTb
     */
    public void setSelectedTb(TrustBundle selectedTb) {
        this.selectedTb = selectedTb;
    }
}
