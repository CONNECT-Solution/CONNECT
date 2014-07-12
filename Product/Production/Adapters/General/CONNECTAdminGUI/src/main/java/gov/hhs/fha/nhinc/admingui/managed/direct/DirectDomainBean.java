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
import gov.hhs.fha.nhinc.admingui.model.direct.DirectAddress;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectAnchor;
import gov.hhs.fha.nhinc.admingui.services.DirectService;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.nhind.config.common.AddAnchor;
import org.nhind.config.common.AddDomain;
import org.nhind.config.common.Anchor;
import org.nhind.config.common.CertificateGetOptions;
import org.nhind.config.common.Domain;
import org.nhind.config.common.EntityStatus;
import org.nhind.config.common.GetAnchorsForOwner;
import org.nhind.config.common.RemoveAnchors;
import org.nhind.config.common.TrustBundle;
import org.nhind.config.common.TrustBundleDomainReltn;
import org.nhind.config.common.UpdateDomain;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jasonasmith
 */
@ManagedBean(name = "directDomainBean")
@ViewScoped
@Component
public class DirectDomainBean {

    @Autowired
    private DirectService directService;

    private Domain selectedDomain;
    private DirectAddress selectedAddress;
    private DirectAnchor selectedAnchor;
    private TrustBundle selectedTrustBundle;

    private List<String> bundlesToAdd;

    private UploadedFile anchorCert;

    // TODO: Unused vars below?
    private String domainName;
    private String domainStatus;
    private String domainPostmaster;
    private String domainTrustBundle;

    private boolean anchorIncoming = true;
    private boolean anchorOutgoing = true;
    private String anchorStatus;

    private String addressEndpoint;
    private String addressType;

    public List<Domain> getDomains() {
        return directService.getDomains();
    }

    public List<TrustBundle> getTrustBundles() {
        List<TrustBundleDomainReltn> bundleRelations = directService.getTrustBundlesByDomain(selectedDomain.getId(), false);
        List<TrustBundle> bundles = new ArrayList<TrustBundle>();

        if (bundleRelations != null) {
            for (TrustBundleDomainReltn tbdr : bundleRelations) {
                bundles.add(tbdr.getTrustBundle());
            }
        }

        return bundles;
    }

    public List<TrustBundle> getAvailableTrustBundles() {
        List<TrustBundle> bundles = directService.getTrustBundles(false);

        if (bundles != null) {
            for (TrustBundle tb : getTrustBundles()) {
                bundles.remove(tb);
            }
        }

        return bundles;
    }

    public List<String> getAvailableTrustBundleNames() {
        List<String> bundleNames = new ArrayList<String> ();

        for (TrustBundle tb : getAvailableTrustBundles()) {
            bundleNames.add(tb.getBundleName());
        }

        return bundleNames;
    }

    public void deleteDomain() {
        directService.deleteDomain(selectedDomain);
    }

    public void addDomain() {
        AddDomain addDomain = new AddDomain();
        Domain domain = new Domain();
        domain.setDomainName(domainName);
        domain.setStatus(EntityStatus.NEW);
        domain.setPostMasterEmail(domainPostmaster);
        addDomain.setDomain(domain);

        directService.addDomain(addDomain);
    }

    public void showEdit() {
        if (selectedDomain != null) {
            RequestContext.getCurrentInstance().execute("domainEditDlg.show()");
        }
    }

    public void editDomain() {
        UpdateDomain updateDomain = new UpdateDomain();
        updateDomain.setDomain(selectedDomain);
        directService.updateDomain(updateDomain);
    }

    public void addAddress() {

    }

    public void deleteAddress() {

    }

    public void anchorFileUpload(FileUploadEvent event) {
        anchorCert = event.getFile();
    }

    public void addAnchor() {
        if (anchorCert != null) {
            AddAnchor addAnchor = new AddAnchor();
            Anchor anchor = new Anchor();

            anchor.setData(anchorCert.getContents());
            anchor.setOwner(getSelectedDomain().getDomainName());
            anchor.setIncoming(isAnchorIncoming());
            anchor.setOutgoing(isAnchorOutgoing());
            anchor.setStatus(EntityStatus.valueOf(getAnchorStatus()));

            addAnchor.getAnchor().add(anchor);

            directService.addAnchor(addAnchor);
        }
    }

    public List<DirectAnchor> getAnchors() {
        GetAnchorsForOwner getAnchorsForOwner = new GetAnchorsForOwner();
        getAnchorsForOwner.setOwner(getSelectedDomain().getDomainName());
        getAnchorsForOwner.setOptions(new CertificateGetOptions());

        List<DirectAnchor> anchorList = new ArrayList<DirectAnchor>();
        List<Anchor> anchorsResponse = directService.getAnchorsForOwner(getAnchorsForOwner);

        for (Anchor a : anchorsResponse) {
            CertContainer cc = new CertContainer(a.getData());
            anchorList.add(new DirectAnchor(a, cc.getTrustedEntityName()));
        }

        return anchorList;
    }

    public void deleteAnchor() {
        RemoveAnchors removeAnchors = new RemoveAnchors();
        removeAnchors.getAnchorId().add(getSelectedAnchor().getId());

        selectedAnchor = null;
        directService.deleteAnchor(removeAnchors);
    }

    public void addTrustBundles() {
        if (bundlesToAdd != null && bundlesToAdd.size() > 0) {
            List<TrustBundle> bundles = new ArrayList<TrustBundle> ();

            for (String bundleName : bundlesToAdd) {
                bundles.add(directService.getTrustBundleByName(bundleName));
            }

            for (TrustBundle tb : bundles) {
                bundlesToAdd = new ArrayList<String> ();
                // TODO: incoming & outgoing should be set by user via UI
                directService.associateTrustBundleToDomain(selectedDomain.getId(), tb.getId(), true, true);
            }
        }
    }

    public void disassociateTrustBundle() {
        if (selectedTrustBundle != null) {
            directService.disassociateTrustBundleFromDomain(selectedDomain.getId(), selectedTrustBundle.getId());
        }
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainStatus() {
        return domainStatus;
    }

    public void setDomainStatus(String domainStatus) {
        this.domainStatus = domainStatus;
    }

    public String getDomainPostmaster() {
        return domainPostmaster;
    }

    public void setDomainPostmaster(String domainPostmaster) {
        this.domainPostmaster = domainPostmaster;
    }

    public String getDomainTrustBundle() {
        return domainTrustBundle;
    }

    public void setDomainTrustBundle(String domainTrustBundle) {
        this.domainTrustBundle = domainTrustBundle;
    }

    public Domain getSelectedDomain() {
        return selectedDomain;
    }

    public void setSelectedDomain(Domain selectedDomain) {
        this.selectedDomain = selectedDomain;
    }

    public DirectAddress getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(DirectAddress selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public String getAddressEndpoint() {
        return addressEndpoint;
    }

    public void setAddressEndpoint(String addressEndpoint) {
        this.addressEndpoint = addressEndpoint;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public DirectAnchor getSelectedAnchor() {
        return selectedAnchor;
    }

    public void setSelectedAnchor(DirectAnchor selectedAnchor) {
        this.selectedAnchor = selectedAnchor;
    }

    public boolean isAnchorIncoming() {
        return anchorIncoming;
    }

    public void setAnchorIncoming(boolean anchorIncoming) {
        this.anchorIncoming = anchorIncoming;
    }

    public boolean isAnchorOutgoing() {
        return anchorOutgoing;
    }

    public void setAnchorOutgoing(boolean anchorOutgoing) {
        this.anchorOutgoing = anchorOutgoing;
    }

    public String getAnchorStatus() {
        return anchorStatus;
    }

    public void setAnchorStatus(String anchorStatus) {
        this.anchorStatus = anchorStatus;
    }

    public TrustBundle getSelectedTrustBundle() {
        return selectedTrustBundle;
    }

    public void setSelectedTrustBundle(TrustBundle selectedTrustBundle) {
        this.selectedTrustBundle = selectedTrustBundle;
    }

    public List<String> getBundlesToAdd() {
        return bundlesToAdd;
    }

    public void setBundlesToAdd(List<String> bundlesToAdd) {
        this.bundlesToAdd = bundlesToAdd;
    }
}
