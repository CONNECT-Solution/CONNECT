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
import gov.hhs.fha.nhinc.admingui.model.direct.DirectAnchor;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectTrustBundle;
import gov.hhs.fha.nhinc.admingui.services.DirectService;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.nhind.config.AddAnchor;
import org.nhind.config.AddDomain;
import org.nhind.config.Address;
import org.nhind.config.Anchor;
import org.nhind.config.CertificateGetOptions;
import org.nhind.config.Domain;
import org.nhind.config.EntityStatus;
import org.nhind.config.GetAnchorsForOwner;
import org.nhind.config.RemoveAnchors;
import org.nhind.config.TrustBundle;
import org.nhind.config.TrustBundleDomainReltn;
import org.nhind.config.UpdateDomain;
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
    private Address selectedAddress;
    private DirectAnchor selectedAnchor;
    private DirectTrustBundle selectedTrustBundle;

    private List<Domain> domains;
    private List<DirectAnchor> anchors;
    private List<DirectTrustBundle> associatedTrustBundles;
    private List<String> unassociatedTrustBundleNames;

    private List<String> namesOfBundlesToAdd;
    private UploadedFile anchorCert;

    private String domainName;
    private String domainPostmaster;
    private String domainTrustBundle;

    private boolean anchorIncoming = true;
    private boolean anchorOutgoing = true;
    private String anchorStatus;

    private boolean bundleIncoming = true;
    private boolean bundleOutgoing = true;

    private String addressName;
    private String addressEmail;
    
    public List<Domain> getDomains() {
        if (domains == null) {
            refreshDomains();
        }

        return domains;
    }

    public void deleteDomain() {
        if(domains.size() > 1){
            directService.disassociateTrustBundlesFromDomain(selectedDomain.getId());
            directService.deleteDomain(selectedDomain);
            refreshDomains();
        }else {
            FacesContext.getCurrentInstance().addMessage("domainDeleteError",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Delete Denied. Must always have one active domain."));
        }
        selectedDomain = null;
    }

    public void addDomain(ActionEvent event) {
        AddDomain addDomain = new AddDomain();
        Domain domain = new Domain();
        domain.setDomainName(domainName);
        domain.setStatus(EntityStatus.ENABLED);
        domain.setPostMasterEmail(domainPostmaster);
        addDomain.setDomain(domain);

        directService.addDomain(addDomain);
        refreshDomains();
        this.domainName = null;
        this.domainPostmaster = null;
    }

    public void showEdit() {
        if (selectedDomain != null) {
            resetBean();
            RequestContext.getCurrentInstance().execute("domainEditDlg.show()");
        }
    }

    public void editDomain(ActionEvent event) {
        UpdateDomain updateDomain = new UpdateDomain();
        updateDomain.setDomain(selectedDomain);
        directService.updateDomain(updateDomain);
        selectedDomain = null;
        refreshDomains();
    }

    protected void refreshDomains() {
        domains = directService.getDomains();
    }

    public List<Address> getAddresses() {
        return selectedDomain.getAddress();
    }

    public void addAddress() {
        if (NullChecker.isNotNullish(addressName) && NullChecker.isNotNullish(addressEmail)) {
            Address currentAddress = new Address();
            currentAddress.setDisplayName(addressName);
            currentAddress.setEmailAddress(addressEmail);
            selectedDomain.getAddress().add(currentAddress);

            UpdateDomain updateDomain = new UpdateDomain();
            updateDomain.setDomain(selectedDomain);
            directService.updateDomain(updateDomain);
        }
        addressName = null;
        addressEmail = null;
    }

    public void deleteAddress() {
        if (selectedAddress != null) {
            boolean removed = directService.removeAddress(selectedAddress.getEmailAddress());

            if (removed) {
                List<Address> addresses = selectedDomain.getAddress();
                for (int i = 0; i < addresses.size(); i++) {
                    if (addresses.get(i).getEmailAddress().equals(selectedAddress.getEmailAddress())) {
                        addresses.remove(i);
                        break;
                    }
                }
            }
        }
        selectedAddress = null;
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
            refreshAnchors();
        }
    }

    public List<DirectAnchor> getAnchors() {
        if (anchors == null) {
            refreshAnchors();
        }

        return anchors;
    }

    public void deleteAnchor() {
        RemoveAnchors removeAnchors = new RemoveAnchors();
        removeAnchors.getAnchorId().add(getSelectedAnchor().getId());

        directService.deleteAnchor(removeAnchors);
        selectedAnchor = null;
        refreshAnchors();
    }

    protected void refreshAnchors() {
        GetAnchorsForOwner getAnchorsForOwner = new GetAnchorsForOwner();
        getAnchorsForOwner.setOwner(getSelectedDomain().getDomainName());
        getAnchorsForOwner.setOptions(new CertificateGetOptions());

        anchors = new ArrayList<DirectAnchor>();
        List<Anchor> anchorsResponse = directService.getAnchorsForOwner(getAnchorsForOwner);

        for (Anchor a : anchorsResponse) {
            CertContainer cc = new CertContainer(a.getData());
            anchors.add(new DirectAnchor(a, cc.getTrustedEntityName()));
        }
    }

    public void addTrustBundles() {
        if (namesOfBundlesToAdd != null && namesOfBundlesToAdd.size() > 0) {
            List<TrustBundle> bundlesToAdd = new ArrayList<TrustBundle>();

            for (String bundleName : namesOfBundlesToAdd) {
                TrustBundle tb = directService.getTrustBundleByName(bundleName);
                unassociatedTrustBundleNames.remove(tb.getBundleName());
                bundlesToAdd.add(tb);
            }

            namesOfBundlesToAdd = new ArrayList<String>();

            for (TrustBundle tb : bundlesToAdd) {
                directService.associateTrustBundleToDomain(selectedDomain.getId(), tb.getId(), bundleIncoming,
                        bundleOutgoing);
                unassociatedTrustBundleNames.remove(tb.getBundleName());
            }

            refreshTrustBundles();
        }
    }

    public void disassociateTrustBundle() {
        if (selectedTrustBundle != null) {
            unassociatedTrustBundleNames.add(selectedTrustBundle.getBundleName());
            directService.disassociateTrustBundleFromDomain(selectedDomain.getId(), selectedTrustBundle.getId());
            selectedTrustBundle = null;
            refreshTrustBundles();
        }
    }

    public List<DirectTrustBundle> getAssociatedTrustBundles() {
        if (associatedTrustBundles == null) {
            refreshTrustBundles();
        }

        return associatedTrustBundles;
    }

    public List<String> getUnassociatedTrustBundleNames() {
        if (unassociatedTrustBundleNames == null) {
            unassociatedTrustBundleNames = new ArrayList<String>();

            for (TrustBundle tb : directService.getTrustBundles(false)) {
                unassociatedTrustBundleNames.add(tb.getBundleName());
            }

            for (DirectTrustBundle tb : getAssociatedTrustBundles()) {
                unassociatedTrustBundleNames.remove(tb.getBundleName());
            }
        }

        Collections.sort(unassociatedTrustBundleNames);
        return unassociatedTrustBundleNames;
    }

    protected void refreshTrustBundles() {
        List<TrustBundleDomainReltn> bundleRelations = directService.getTrustBundlesByDomain(selectedDomain.getId(),
                false);
        associatedTrustBundles = new ArrayList<DirectTrustBundle>();

        if (bundleRelations != null) {
            for (TrustBundleDomainReltn tbdr : bundleRelations) {
                DirectTrustBundle dtb = new DirectTrustBundle(tbdr.getTrustBundle(), tbdr.isIncoming(),
                        tbdr.isOutgoing());
                associatedTrustBundles.add(dtb);
            }
        }
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
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

    public Address getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(Address selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
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

    public DirectTrustBundle getSelectedTrustBundle() {
        return selectedTrustBundle;
    }

    public void setSelectedTrustBundle(DirectTrustBundle selectedTrustBundle) {
        this.selectedTrustBundle = selectedTrustBundle;
    }

    public List<String> getNamesOfBundlesToAdd() {
        return namesOfBundlesToAdd;
    }

    public void setNamesOfBundlesToAdd(List<String> namesOfBundlesToAdd) {
        this.namesOfBundlesToAdd = namesOfBundlesToAdd;
    }

    public void setUnassociatedTrustBundleNames(List<String> unassociatedBundleNames) {
        this.unassociatedTrustBundleNames = unassociatedBundleNames;
    }

    public boolean isBundleIncoming() {
        return bundleIncoming;
    }

    public void setBundleIncoming(boolean bundleIncoming) {
        this.bundleIncoming = bundleIncoming;
    }

    public boolean isBundleOutgoing() {
        return bundleOutgoing;
    }

    public void setBundleOutgoing(boolean bundleOutgoing) {
        this.bundleOutgoing = bundleOutgoing;
    }

    protected List<String> getTrustBundleNames(List<DirectTrustBundle> bundles) {
        List<String> bundleNames = new ArrayList<String>();

        for (DirectTrustBundle tb : bundles) {
            bundleNames.add(tb.getBundleName());
        }

        return bundleNames;
    }

    protected void resetBean() {
        selectedAddress = null;
        selectedAnchor = null;
        selectedTrustBundle = null;

        domains = null;
        anchors = null;
        associatedTrustBundles = null;

        namesOfBundlesToAdd = null;
        unassociatedTrustBundleNames = null;
        anchorCert = null;

        domainName = null;
        domainPostmaster = null;
        domainTrustBundle = null;

        anchorIncoming = true;
        anchorOutgoing = true;
        anchorStatus = null;

        bundleIncoming = true;
        bundleOutgoing = true;

        addressName = null;
        addressEmail = null;
    }

    public String getAddressEmail() {
        return addressEmail;
    }

    public void setAddressEmail(String addressEmail) {
        this.addressEmail = addressEmail;
    }
    
}
