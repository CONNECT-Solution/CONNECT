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

import gov.hhs.fha.nhinc.admingui.model.direct.DirectAddress;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectAnchor;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectDomain;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectTrustBundle;
import gov.hhs.fha.nhinc.admingui.services.DirectService;
import java.io.File;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
    
    private String domainName;
    private String domainStatus;
    private String domainPostmaster;
    private String domainTrustBundle;
    
    private DirectDomain selectedDomain;
    private DirectAddress selectedAddress;
    private DirectAnchor selectedAnchor;
    
    private String addressEndpoint;
    private String addressType;
    
    private UploadedFile anchorCert;
    private boolean anchorIncoming = true;
    private boolean anchorOutgoing = true;
    private String anchorStatus;
    
    public List<DirectDomain> getDomains(){
        return directService.getDomains();
    }
    
    public List<DirectTrustBundle> getTrustBundles(){
        return directService.getTrustBundles();
    }
    
    public void deleteDomain(){
        directService.deleteDomain(selectedDomain);
    }
    
    public void addDomain(){
        DirectDomain domain = new DirectDomain();
        domain.setDomainName(domainName);
        domain.setDomainStatus(domainStatus);
        domain.setDomainPostMaster(domainPostmaster);
        directService.addDomain(domain);
    }
    
    public void showEdit(){
        if(selectedDomain != null){
            RequestContext.getCurrentInstance().execute("domainEditDlg.show()");
        }
    }
    
    public void editDomain(){
        directService.updateDomain(selectedDomain);
    }
    
    public void addAddress(){
        DirectAddress address = new DirectAddress();
        address.setEndpoint(addressEndpoint);
        address.setType(addressType);
        selectedDomain.addAddress(address);
        directService.updateDomain(selectedDomain);
    }
    
    public void deleteAddress(){
        for(int i = 0; i < selectedDomain.getAddresses().size(); i++){
            if(selectedDomain.getAddresses().get(i).getEndpoint().equals(selectedAddress.getEndpoint())){
                selectedDomain.getAddresses().remove(i);
                directService.updateDomain(selectedDomain);
                break;
            }
        }
    }
    
    public void anchorFileUpload(FileUploadEvent event){
        anchorCert = event.getFile();
    }
    
    public void addAnchor(){
        DirectAnchor anchor = new DirectAnchor();
        anchor.setName(anchorCert.getFileName());
        anchor.setCertificate(anchorCert.getContents());
        anchor.setIncoming(anchorIncoming);
        anchor.setOutgoing(anchorOutgoing);
        anchor.setStatus(anchorStatus);
        selectedDomain.addAnchor(anchor);
        directService.updateDomain(selectedDomain);
    }
    
    public void deleteAnchor(){
        for(int i = 0; i < selectedDomain.getAnchors().size(); i++){
            if(selectedDomain.getAnchors().get(i).getName().equals(selectedAnchor.getName())){
                selectedDomain.getAnchors().remove(i);
                directService.updateDomain(selectedDomain);
                break;
            }
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

    public DirectDomain getSelectedDomain() {
        return selectedDomain;
    }

    public void setSelectedDomain(DirectDomain selectedDomain) {
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
    
    
}
