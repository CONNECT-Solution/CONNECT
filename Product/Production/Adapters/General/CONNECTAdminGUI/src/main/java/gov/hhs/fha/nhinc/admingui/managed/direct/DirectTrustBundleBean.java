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

import gov.hhs.fha.nhinc.admingui.managed.TabBean;
import gov.hhs.fha.nhinc.admingui.services.DirectService;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.nhind.config.common.Certificate;
import org.nhind.config.common.DeleteTrustBundles;
import org.nhind.config.common.GetTrustBundles;
import org.nhind.config.common.TrustBundle;
import org.nhind.config.common.UpdateTrustBundleAttributes;
import org.primefaces.context.RequestContext;
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

    public List<TrustBundle> getTrustBundles() {
        GetTrustBundles gtb = new GetTrustBundles();
        gtb.setFetchAnchors(true);
        return directService.getTrustBundles(gtb);
    }

    public void deleteTrustBundle() {
        DeleteTrustBundles dtb = new DeleteTrustBundles();
        dtb.getTrustBundleIds().add(selectedTb.getId());
        directService.deleteTrustBundle(dtb);
        TabBean tabs = (TabBean) FacesContext.getCurrentInstance().
            getExternalContext().getSessionMap().get("tabBean");

        if (tabs != null) {
            tabs.setDirectTabIndex(3);
        }
    }

    /**
     *
     */
    public void addTrustBundle() {
        TrustBundle tb = new TrustBundle();
        tb.setBundleName(tbName);
        tb.setBundleURL(tbUrl);
        int refreshIntervalue = 0;
        if (null != tbRefreshInterval && tbRefreshInterval.length() > 0) {
            refreshIntervalue = Integer.parseInt(tbRefreshInterval);
        }
        tb.setRefreshInterval(refreshIntervalue);
        directService.addTrustBundle(tb);
    }

    public void editTrustBundle() {
        UpdateTrustBundleAttributes utba = new UpdateTrustBundleAttributes();
        utba.setTrustBundleId(selectedTb.getId());
        utba.setTrustBundleName(selectedTb.getBundleName());
        utba.setTrustBundleRefreshInterval(selectedTb.getRefreshInterval());
        if (null != selectedTb.getSigningCertificateData() && selectedTb.getSigningCertificateData().length > 0) {
            Certificate crt = new Certificate();
            crt.setData(selectedTb.getSigningCertificateData());
            utba.setSigningCert(crt);
        }
        utba.setTrustBundleURL(selectedTb.getBundleURL());
        directService.updateTrustBundle(utba);
    }

    public void showEdit() {
        if (selectedTb != null) {
            RequestContext.getCurrentInstance().execute("tbEditDlg.show()");
        }
    }

    public void showDelConfirm() {
        if (selectedTb != null) {
            RequestContext.getCurrentInstance().execute("tbConfirmDelDlg.show()");
        }
    }

    public String getTbName() {
        return tbName;
    }

    public void setTbName(String tbName) {
        this.tbName = tbName;
    }

    public String getTbUrl() {
        return tbUrl;
    }

    public void setTbUrl(String tbUrl) {
        this.tbUrl = tbUrl;
    }

    public String getTbRefreshInterval() {
        return tbRefreshInterval;
    }

    public void setTbRefreshInterval(String tbRefreshInterval) {
        this.tbRefreshInterval = tbRefreshInterval;
    }

    public UploadedFile getTbCert() {
        return tbCert;
    }

    public void setTbCert(UploadedFile tbCert) {
        this.tbCert = tbCert;
    }

    public TrustBundle getSelectedTb() {
        return selectedTb;
    }

    public void setSelectedTb(TrustBundle selectedTb) {
        this.selectedTb = selectedTb;
    }

}
