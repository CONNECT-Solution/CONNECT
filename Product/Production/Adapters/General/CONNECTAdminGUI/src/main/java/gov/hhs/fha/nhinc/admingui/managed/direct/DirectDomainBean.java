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
import gov.hhs.fha.nhinc.admingui.model.direct.DirectDomain;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectTrustBundle;
import gov.hhs.fha.nhinc.admingui.services.DirectService;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
    
    private String addressEndpoint;
    private String addressType;
    
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
    
    public void editDomain(){
        selectedDomain.setDomainName(domainName);
        selectedDomain.setDomainStatus(domainStatus);
        directService.updateDomain(selectedDomain);
    }
    
    public void addAddress(){
        selectedDomain.addAddress(selectedAddress);
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
    
    
}
