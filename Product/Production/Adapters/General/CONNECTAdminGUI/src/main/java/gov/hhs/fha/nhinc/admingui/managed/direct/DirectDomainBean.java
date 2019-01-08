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
import gov.hhs.fha.nhinc.admingui.model.direct.DirectAnchor;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectTrustBundle;
import gov.hhs.fha.nhinc.admingui.services.DirectService;
import gov.hhs.fha.nhinc.admingui.services.exception.DomainException;
import gov.hhs.fha.nhinc.direct.config.AddAnchor;
import gov.hhs.fha.nhinc.direct.config.AddDomain;
import gov.hhs.fha.nhinc.direct.config.Address;
import gov.hhs.fha.nhinc.direct.config.Anchor;
import gov.hhs.fha.nhinc.direct.config.CertificateGetOptions;
import gov.hhs.fha.nhinc.direct.config.Domain;
import gov.hhs.fha.nhinc.direct.config.EntityStatus;
import gov.hhs.fha.nhinc.direct.config.GetAnchorsForOwner;
import gov.hhs.fha.nhinc.direct.config.RemoveAnchors;
import gov.hhs.fha.nhinc.direct.config.TrustBundle;
import gov.hhs.fha.nhinc.direct.config.TrustBundleDomainReltn;
import gov.hhs.fha.nhinc.direct.config.UpdateDomain;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(DirectDomainBean.class);

    /**
     *
     * @return
     */
    public List<Domain> getDomains() {
        if (domains == null) {
            refreshDomains();
        }

        return domains;
    }

    /**
     *
     */
    public void deleteDomain() {
        if (domains.size() > 1) {
            directService.disassociateTrustBundlesFromDomain(selectedDomain.getId());
            directService.deleteDomain(selectedDomain);
            refreshDomains();
        } else {
            FacesContext.getCurrentInstance().addMessage("domainDeleteError", new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "Delete Denied. Must always have one active domain.", ""));
        }
        selectedDomain = null;
    }

    /**
     *
     * @param event
     */
    public void addDomain(ActionEvent event) {
        AddDomain addDomain = new AddDomain();
        Domain domain = new Domain();
        domain.setDomainName(domainName);
        domain.setStatus(EntityStatus.ENABLED);
        domain.setPostMasterEmail(domainPostmaster);
        addDomain.setDomain(domain);

        try {
            directService.addDomain(addDomain);
            refreshDomains();
        } catch (DomainException domainException) {
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().addMessage("domainAddErrors", new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "Cannot add domain: " + domainException.getLocalizedMessage(), ""));
            LOG.error("Error creating domain: {}", domainException.getLocalizedMessage(), domainException);
        }

        domainName = null;
        domainPostmaster = null;
    }

    /**
     *
     */
    public void showEdit() {
        if (selectedDomain != null) {
            resetBean();
            RequestContext.getCurrentInstance().execute("PF('domainEditDlg').show()");
        }
    }

    /**
     *
     * @param event
     */
    public void editDomain(ActionEvent event) {
        UpdateDomain updateDomain = new UpdateDomain();
        updateDomain.setDomain(selectedDomain);
        try {
            directService.updateDomain(updateDomain);
            selectedDomain = null;
            refreshDomains();
        } catch (DomainException domainException) {
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().addMessage("domainEditErrors", new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "Cannot update domain: " + domainException.getLocalizedMessage(), ""));
            LOG.error("Error updating domain: {}", domainException.getLocalizedMessage(), domainException);
        }
    }

    /**
     *
     */
    protected void refreshDomains() {
        domains = directService.getDomains();
    }

    /**
     *
     * @return
     */
    public List<Address> getAddresses() {
        return selectedDomain.getAddress();
    }

    /**
     *
     */
    public void addAddress() {
        if (NullChecker.isNotNullish(addressName) && NullChecker.isNotNullish(addressEmail)) {
            Address currentAddress = new Address();
            currentAddress.setDisplayName(addressName);
            currentAddress.setEmailAddress(addressEmail);
            selectedDomain.getAddress().add(currentAddress);

            UpdateDomain updateDomain = new UpdateDomain();
            updateDomain.setDomain(selectedDomain);
            try {
                directService.updateDomain(updateDomain);
            } catch (DomainException domainException) {
                LOG.error("Error updating domain: {}", domainException.getLocalizedMessage(), domainException);
            }
        }
        addressName = null;
        addressEmail = null;
    }

    /**
     *
     */
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

    /**
     *
     * @param event
     */
    public void anchorFileUpload(FileUploadEvent event) {
        anchorCert = event.getFile();
    }

    /**
     *
     */
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

    /**
     *
     * @return
     */
    public List<DirectAnchor> getAnchors() {
        if (anchors == null) {
            refreshAnchors();
        }

        return anchors;
    }

    /**
     *
     */
    public void deleteAnchor() {
        RemoveAnchors removeAnchors = new RemoveAnchors();
        removeAnchors.getAnchorId().add(getSelectedAnchor().getId());

        directService.deleteAnchor(removeAnchors);
        selectedAnchor = null;
        refreshAnchors();
    }

    /**
     *
     */
    protected void refreshAnchors() {
        GetAnchorsForOwner getAnchorsForOwner = new GetAnchorsForOwner();
        getAnchorsForOwner.setOwner(getSelectedDomain().getDomainName());
        getAnchorsForOwner.setOptions(new CertificateGetOptions());

        anchors = new ArrayList<>();
        List<Anchor> anchorsResponse = directService.getAnchorsForOwner(getAnchorsForOwner);

        for (Anchor a : anchorsResponse) {
            CertContainer cc = new CertContainer(a.getData());
            anchors.add(new DirectAnchor(a, cc.getTrustedEntityName()));
        }
    }

    /**
     *
     * @return
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     *
     * @param domainName
     */
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    /**
     *
     * @return
     */
    public String getDomainPostmaster() {
        return domainPostmaster;
    }

    /**
     *
     * @param domainPostmaster
     */
    public void setDomainPostmaster(String domainPostmaster) {
        this.domainPostmaster = domainPostmaster;
    }

    /**
     *
     * @return
     */
    public String getDomainTrustBundle() {
        return domainTrustBundle;
    }

    /**
     *
     * @param domainTrustBundle
     */
    public void setDomainTrustBundle(String domainTrustBundle) {
        this.domainTrustBundle = domainTrustBundle;
    }

    /**
     *
     * @return
     */
    public Domain getSelectedDomain() {
        return selectedDomain;
    }

    /**
     *
     * @param selectedDomain
     */
    public void setSelectedDomain(Domain selectedDomain) {
        this.selectedDomain = selectedDomain;
    }

    /**
     *
     * @return
     */
    public Address getSelectedAddress() {
        return selectedAddress;
    }

    /**
     *
     * @param selectedAddress
     */
    public void setSelectedAddress(Address selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    /**
     *
     * @return
     */
    public String getAddressName() {
        return addressName;
    }

    /**
     *
     * @param addressName
     */
    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    /**
     *
     * @return
     */
    public DirectAnchor getSelectedAnchor() {
        return selectedAnchor;
    }

    /**
     *
     * @param selectedAnchor
     */
    public void setSelectedAnchor(DirectAnchor selectedAnchor) {
        this.selectedAnchor = selectedAnchor;
    }

    /**
     *
     * @return
     */
    public boolean isAnchorIncoming() {
        return anchorIncoming;
    }

    /**
     *
     * @param anchorIncoming
     */
    public void setAnchorIncoming(boolean anchorIncoming) {
        this.anchorIncoming = anchorIncoming;
    }

    /**
     *
     * @return
     */
    public boolean isAnchorOutgoing() {
        return anchorOutgoing;
    }

    /**
     *
     * @param anchorOutgoing
     */
    public void setAnchorOutgoing(boolean anchorOutgoing) {
        this.anchorOutgoing = anchorOutgoing;
    }

    /**
     *
     * @return
     */
    public String getAnchorStatus() {
        return anchorStatus;
    }

    /**
     *
     * @param anchorStatus
     */
    public void setAnchorStatus(String anchorStatus) {
        this.anchorStatus = anchorStatus;
    }

    /**
     *
     * @return
     */
    public DirectTrustBundle getSelectedTrustBundle() {
        return selectedTrustBundle;
    }

    /**
     *
     * @param selectedTrustBundle
     */
    public void setSelectedTrustBundle(DirectTrustBundle selectedTrustBundle) {
        this.selectedTrustBundle = selectedTrustBundle;
    }

    public void setTrustBundlesForSelectedDomain(SelectEvent event) {
        Domain domain = (Domain) event.getObject();
        refreshTrustBundles(domain.getId());
    }

    /**
     *
     */
    public void addTrustBundles() {
        if (CollectionUtils.isNotEmpty(namesOfBundlesToAdd)) {
            for (String bundleName : namesOfBundlesToAdd) {
                TrustBundle tb = directService.getTrustBundleByName(bundleName);
                directService.associateTrustBundleToDomain(selectedDomain.getId(), tb.getId(), bundleIncoming,
                    bundleOutgoing);
            }

            namesOfBundlesToAdd.clear();

            refreshTrustBundles(selectedDomain.getId());
        }
    }

    /**
     *
     */
    public void disassociateTrustBundle() {
        if (selectedTrustBundle != null) {
            directService.disassociateTrustBundleFromDomain(selectedDomain.getId(), selectedTrustBundle.getId());
            selectedTrustBundle = null;
            refreshTrustBundles(selectedDomain.getId());
        }
    }

    /**
     *
     * @return
     */
    public List<DirectTrustBundle> getAssociatedTrustBundles() {
        refreshTrustBundles(selectedDomain.getId());
        return associatedTrustBundles;
    }

    /**
     *
     * @return
     */
    public List<String> getUnassociatedTrustBundleNames() {
        refreshTrustBundles(selectedDomain.getId());
        return unassociatedTrustBundleNames;
    }

    /**
     *
     * @param id
     */
    protected void refreshTrustBundles(long id) {
        try {
            List<TrustBundleDomainReltn> bundleRelations = directService.getTrustBundlesByDomain(id, false);
            associatedTrustBundles = new ArrayList<>();

            if (bundleRelations != null) {
                for (TrustBundleDomainReltn tbdr : bundleRelations) {
                    DirectTrustBundle dtb = new DirectTrustBundle(tbdr.getTrustBundle(), tbdr.isIncoming(),
                        tbdr.isOutgoing());
                    associatedTrustBundles.add(dtb);
                }
            }

            unassociatedTrustBundleNames = new ArrayList<>();
            for (TrustBundle tb : directService.getTrustBundles(false)) {
                unassociatedTrustBundleNames.add(tb.getBundleName());
            }

            for (DirectTrustBundle tb : associatedTrustBundles) {
                unassociatedTrustBundleNames.remove(tb.getBundleName());
            }
        } catch (Exception ex) {
            LOG.error("Unable to refresh trust bundles: {}", ex.getLocalizedMessage(), ex);
        }
    }

    /**
     *
     * @return
     */
    public List<String> getNamesOfBundlesToAdd() {
        return namesOfBundlesToAdd;
    }

    /**
     *
     * @param namesOfBundlesToAdd
     */
    public void setNamesOfBundlesToAdd(List<String> namesOfBundlesToAdd) {
        this.namesOfBundlesToAdd = namesOfBundlesToAdd;
    }

    /**
     *
     * @return
     */
    public boolean isBundleIncoming() {
        return bundleIncoming;
    }

    /**
     *
     * @param bundleIncoming
     */
    public void setBundleIncoming(boolean bundleIncoming) {
        this.bundleIncoming = bundleIncoming;
    }

    /**
     *
     * @return
     */
    public boolean isBundleOutgoing() {
        return bundleOutgoing;
    }

    /**
     *
     * @param bundleOutgoing
     */
    public void setBundleOutgoing(boolean bundleOutgoing) {
        this.bundleOutgoing = bundleOutgoing;
    }

    /**
     *
     */
    protected void resetBean() {
        selectedAddress = null;
        selectedAnchor = null;
        selectedTrustBundle = null;

        domains = null;
        anchors = null;
        anchorCert = null;

        domainName = null;
        domainPostmaster = null;
        domainTrustBundle = null;

        anchorIncoming = true;
        anchorOutgoing = true;
        anchorStatus = null;

        associatedTrustBundles = null;
        unassociatedTrustBundleNames = null;
        namesOfBundlesToAdd = null;
        bundleIncoming = true;
        bundleOutgoing = true;

        addressName = null;
        addressEmail = null;
    }

    /**
     *
     * @return
     */
    public String getAddressEmail() {
        return addressEmail;
    }

    /**
     *
     * @param addressEmail
     */
    public void setAddressEmail(String addressEmail) {
        this.addressEmail = addressEmail;
    }
}
