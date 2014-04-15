/* 
Copyright (c) 2010, NHIN Direct Project
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
   in the documentation and/or other materials provided with the distribution.  
3. Neither the name of the The NHIN Direct Project (nhindirect.org) nor the names of its contributors may be used to endorse or promote 
   products derived from this software without specific prior written permission.
   
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
THE POSSIBILITY OF SUCH DAMAGE.
 */

package gov.hhs.fha.nhinc.directconfig.service.impl;

import gov.hhs.fha.nhinc.directconfig.service.AddressService;
import gov.hhs.fha.nhinc.directconfig.service.AnchorService;
import gov.hhs.fha.nhinc.directconfig.service.CertificatePolicyService;
import gov.hhs.fha.nhinc.directconfig.service.CertificateService;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationFault;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationService;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationServiceException;
import gov.hhs.fha.nhinc.directconfig.service.DNSService;
import gov.hhs.fha.nhinc.directconfig.service.DomainService;
import gov.hhs.fha.nhinc.directconfig.service.SettingService;
import gov.hhs.fha.nhinc.directconfig.service.TrustBundleService;

import java.util.Calendar;
import java.util.Collection;

import javax.jws.WebService;
import javax.xml.ws.FaultAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.nhindirect.config.store.Address;
import org.nhindirect.config.store.Anchor;
import org.nhindirect.config.store.BundleRefreshError;
import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupDomainReltn;
import org.nhindirect.config.store.CertPolicyUse;
import org.nhindirect.config.store.Certificate;
import org.nhindirect.config.store.DNSRecord;
import org.nhindirect.config.store.Domain;
import org.nhindirect.config.store.EntityStatus;
import org.nhindirect.config.store.Setting;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleDomainReltn;
import org.nhindirect.policy.PolicyLexicon;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implements the single Service Endpoint Interface. Delegates everything to the
 * individual service implementations.
 */
@WebService(endpointInterface = "org.nhindirect.config.service.ConfigurationService")
public class ConfigurationServiceImpl implements ConfigurationService {


	private static Log log = LogFactory.getLog(ConfigurationServiceImpl.class);

    private DomainService domainSvc;

    private AddressService addressSvc;

    private CertificateService certSvc;

    private AnchorService anchorSvc;

    private SettingService settingSvc;
    
    private DNSService dnsSvc;
    
    private TrustBundleService trustBundleSvc;
  
    private CertificatePolicyService certPolicySvc;
    
    /**
     * Initialization method.
     */
    public void init() {
        log.info("ConfigurationService initialized");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AddressService#addAddress(java.util.List)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addAddress(Collection<Address> address) throws ConfigurationServiceException {
        addressSvc.addAddress(address);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AddressService#updateAddress(org.nhindirect.config.store.Address)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void updateAddress(Address address) throws ConfigurationServiceException {
        addressSvc.updateAddress(address);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AddressService#getAddressCount()
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public int getAddressCount() throws ConfigurationServiceException {
        return addressSvc.getAddressCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AddressService#getAddress(java.util.List, org.nhindirect.config.store.EntityStatus)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Address> getAddress(Collection<String> addressNames, EntityStatus status)
            throws ConfigurationServiceException {
        return addressSvc.getAddress(addressNames, status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AddressService#removeAddress(java.lang.String)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeAddress(String addressName) throws ConfigurationServiceException {
        addressSvc.removeAddress(addressName);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AddressService#listAddresss(java.lang.String, int)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Address> listAddresss(String lastAddressName, int maxResults)
            throws ConfigurationServiceException {
        return addressSvc.listAddresss(lastAddressName, maxResults);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.DomainService#addDomain(org.nhindirect.config.store.Domain)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addDomain(Domain domain) throws ConfigurationServiceException {
        domainSvc.addDomain(domain);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.DomainService#updateDomain(org.nhindirect.config.store.Domain)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void updateDomain(Domain domain) throws ConfigurationServiceException {
        domainSvc.updateDomain(domain);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.DomainService#getDomainCount()
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public int getDomainCount() throws ConfigurationServiceException {
        return domainSvc.getDomainCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.DomainService#getDomains(java.util.Collection, org.nhindirect.config.store.EntityStatus)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Domain> getDomains(Collection<String> domainNames, EntityStatus status)
            throws ConfigurationServiceException {
        return domainSvc.getDomains(domainNames, status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.DomainService#removeDomain(java.lang.String)
     */
    @Override
    @Deprecated
    @FaultAction(className = ConfigurationFault.class)
    public void removeDomain(String domainName) throws ConfigurationServiceException {
        domainSvc.removeDomain(domainName);
    }
    

    /* 
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.DomainService#removeDomainById(java.lang.Long)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeDomainById(Long domainId) throws ConfigurationServiceException {
        domainSvc.removeDomainById(domainId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.DomainService#listDomains(java.lang.String, int)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Domain> listDomains(String lastDomainName, int maxResults) throws ConfigurationServiceException {
        return domainSvc.listDomains(lastDomainName, maxResults);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.DomainService#searchDomain(java.lang.String, org.nhindirect.config.store.EntityStatus)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Domain> searchDomain(String domain, EntityStatus status) {
        return domainSvc.searchDomain(domain, status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.DomainService#getDomain(java.lang.Long)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Domain getDomain(Long id) {
        return domainSvc.getDomain(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#addCertificates(java.util.Collection)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addCertificates(Collection<Certificate> certs) throws ConfigurationServiceException {
        certSvc.addCertificates(certs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#setCertificateStatus(java.util.Collection, org.nhindirect.config.store.EntityStatus)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void setCertificateStatus(Collection<Long> certificateIds, EntityStatus status)
            throws ConfigurationServiceException {
        certSvc.setCertificateStatus(certificateIds, status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#setCertificateStatusForOwner(java.lang.String, org.nhindirect.config.store.EntityStatus)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void setCertificateStatusForOwner(String owner, EntityStatus status) throws ConfigurationServiceException {
        certSvc.setCertificateStatusForOwner(owner, status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#removeCertificates(java.util.Collection)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeCertificates(Collection<Long> certificateIds) throws ConfigurationServiceException {
        certSvc.removeCertificates(certificateIds);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#removeCertificatesForOwner(java.lang.String)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeCertificatesForOwner(String owner) throws ConfigurationServiceException {
        certSvc.removeCertificatesForOwner(owner);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#contains(java.security.cert.Certificate)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public boolean contains(Certificate cert) {
        return certSvc.contains(cert);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#addAnchors(java.util.List)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addAnchors(Collection<Anchor> anchors) throws ConfigurationServiceException {
        anchorSvc.addAnchors(anchors);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#setAnchorStatusForOwner(java.lang.String, org.nhindirect.config.store.EntityStatus)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void setAnchorStatusForOwner(String owner, EntityStatus status) throws ConfigurationServiceException {
        anchorSvc.setAnchorStatusForOwner(owner, status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#removeAnchors(java.util.List)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeAnchors(Collection<Long> anchorIds) throws ConfigurationServiceException {
        anchorSvc.removeAnchors(anchorIds);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#removeAnchorsForOwner(java.lang.String)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeAnchorsForOwner(String owner) throws ConfigurationServiceException {
        anchorSvc.removeAnchorsForOwner(owner);
    }

    /**
     * Get the DomainService object.
     * 
     * @return the DomainService object.
     */
    public DomainService getDomainSvc() {
        return domainSvc;
    }

    /**
     * Set the DomainService object.
     * 
     * @param domainSvc
     *            The DomainService object.
     */
    @Autowired
    public void setDomainSvc(DomainService domainSvc) {
        this.domainSvc = domainSvc;
    }

    /**
     * Get the AddressService object.
     * 
     * @return the AddressService object.
     */
    public AddressService getAddressSvc() {
        return addressSvc;
    }

    /**
     * Set the AddressService object.
     * 
     * @param addressSvc
     *            The ADdressService object.
     */
    @Autowired
    public void setAddressSvc(AddressService addressSvc) {
        this.addressSvc = addressSvc;
    }

    /**
     * Get the CertificateService object.
     * 
     * @return the CertificateService object.
     */
    public CertificateService getCertSvc() {
        return certSvc;
    }

    /**
     * Set the CertificateService object.
     * 
     * @param certSvc
     *            The CertificateService object.
     */
    @Autowired
    public void setCertSvc(CertificateService certSvc) {
        this.certSvc = certSvc;
    }

    /**
     * Get the SettingService object.
     * 
     * @return the SettingService object.
     */
    public SettingService getSettingSvc() {
        return settingSvc;
    }

    /**
     * Set the SettingService object.
     * 
     * @param settingSvc
     *            The SettingService object.
     */
    @Autowired
    public void setSettingSvc(SettingService settingSvc) {
        this.settingSvc = settingSvc;
    }

    /**
     * Get the AnchorService object.
     * 
     * @return the AnchorService object.
     */
    public AnchorService getAnchorSvc() {
        return anchorSvc;
    }

    /**
     * Set the AnchorService object.
     * 
     * @param anchorSvc
     *            The AnchorService object.
     */
    @Autowired
    public void setAnchorSvc(AnchorService anchorSvc) {
        this.anchorSvc = anchorSvc;
    }    
    
    /**
     * Get the DNSService object.
     * 
     * @return the DNSService object.
     */
    public DNSService getDNSSvc() {
        return dnsSvc;
    }

    /**
     * Set the DNSService object.
     * 
     * @param anchorSvc
     *            The DNSService object.
     */
    @Autowired
    public void setDNSSvc(DNSService dnsSvc) {
        this.dnsSvc = dnsSvc;
    }       
    
    /**
     * Get the TrustBundleService object.
     * 
     * @return the TrustBundleService object.
     */
    public TrustBundleService getTrustBundleSvc() {
        return trustBundleSvc;
    }

    /**
     * Set the TrustBundleService object.
     * 
     * @param trustBundleSvc
     *            The TrustBundleService object.
     */
    @Autowired
    public void setTrustBundleSvc(TrustBundleService trustBundleSvc) {
        this.trustBundleSvc = trustBundleSvc;
    }   
    
    /**
     * Get the CertificatePolicyService object.
     * 
     * @return the CertificatePolicyService object.
     */
    public CertificatePolicyService getCertificatePolicySvc() {
        return certPolicySvc;
    }

    /**
     * Set the CertificatePolicyService object.
     * 
     * @param certPolicySvc
     *            The CertificatePolicyService object.
     */
    @Autowired
    public void setCertificatePolicySvc(CertificatePolicyService certPolicySvc) {
        this.certPolicySvc = certPolicySvc;
    }   
    
    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#getCertificate(java.lang.String, java.lang.String, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Certificate getCertificate(String owner, String thumbprint, CertificateGetOptions options)
            throws ConfigurationServiceException {
        return certSvc.getCertificate(owner, thumbprint, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#getCertificates(java.util.Collection, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Certificate> getCertificates(Collection<Long> certIds, CertificateGetOptions options)
            throws ConfigurationServiceException {
        return certSvc.getCertificates(certIds, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#getCertificatesForOwner(java.lang.String, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Certificate> getCertificatesForOwner(String owner, CertificateGetOptions options)
            throws ConfigurationServiceException {
        return certSvc.getCertificatesForOwner(owner, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#listCertificates(long, int, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Certificate> listCertificates(long lastCertificateId, int maxResults,
            CertificateGetOptions options) throws ConfigurationServiceException {
        return certSvc.listCertificates(lastCertificateId, maxResults, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#getAnchor(java.lang.String, java.lang.String, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Anchor getAnchor(String owner, String thumbprint, CertificateGetOptions options)
            throws ConfigurationServiceException {
        return anchorSvc.getAnchor(owner, thumbprint, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#getAnchors(java.util.Collection, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Anchor> getAnchors(Collection<Long> anchorIds, CertificateGetOptions options)
            throws ConfigurationServiceException {
        return anchorSvc.getAnchors(anchorIds, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#getAnchorsForOwner(java.lang.String, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Anchor> getAnchorsForOwner(String owner, CertificateGetOptions options)
            throws ConfigurationServiceException {
        return anchorSvc.getAnchorsForOwner(owner, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#getIncomingAnchors(java.lang.String, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Anchor> getIncomingAnchors(String owner, CertificateGetOptions options)
            throws ConfigurationServiceException {
        return anchorSvc.getIncomingAnchors(owner, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#getOutgoingAnchors(java.lang.String, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Anchor> getOutgoingAnchors(String owner, CertificateGetOptions options)
            throws ConfigurationServiceException {
        return anchorSvc.getOutgoingAnchors(owner, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#listAnchors(java.lang.Long, int, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Anchor> listAnchors(Long lastAnchorID, int maxResults, CertificateGetOptions options)
            throws ConfigurationServiceException {
        return anchorSvc.listAnchors(lastAnchorID, maxResults, options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
	public void addSetting(String name, String value)
			throws ConfigurationServiceException {
    	settingSvc.addSetting(name, value);
		
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public Collection<Setting> getAllSettings()
			throws ConfigurationServiceException {
		return settingSvc.getAllSettings();
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public Setting getSettingByName(String name)
			throws ConfigurationServiceException {
		return settingSvc.getSettingByName(name);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public Collection<Setting> getSettingsByNames(Collection<String> names)
			throws ConfigurationServiceException {
		return settingSvc.getSettingsByNames(names);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void updateSetting(String name, String value)
			throws ConfigurationServiceException {
		settingSvc.updateSetting(name, value);
		
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void deleteSetting(Collection<String> names) throws ConfigurationServiceException {
		settingSvc.deleteSetting(names);
		
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void addDNS(Collection<DNSRecord> records)
			throws ConfigurationServiceException 
	{
		dnsSvc.addDNS(records);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public Collection<DNSRecord> getDNSByName(String name)
			throws ConfigurationServiceException 
	{
		return dnsSvc.getDNSByName(name);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public Collection<DNSRecord> getDNSByNameAndType(String name, int type)
			throws ConfigurationServiceException 
	{
		return dnsSvc.getDNSByNameAndType(name, type);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public DNSRecord getDNSByRecordId(long recordId)
			throws ConfigurationServiceException 
	{
		return dnsSvc.getDNSByRecordId(recordId);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public Collection<DNSRecord> getDNSByRecordIds(long[] recordIds)
			throws ConfigurationServiceException 
	{
		return dnsSvc.getDNSByRecordIds(recordIds);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public Collection<DNSRecord> getDNSByType(int type)
			throws ConfigurationServiceException 
	{
		return dnsSvc.getDNSByType(type);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public int getDNSCount() throws ConfigurationServiceException 
	{
		return dnsSvc.getDNSCount();
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void removeDNS(Collection<DNSRecord> records)
			throws ConfigurationServiceException 
	{
		dnsSvc.removeDNS(records);
	}

	@Override
	public void removeDNSByRecordId(long recordId)
			throws ConfigurationServiceException 
	{
		dnsSvc.removeDNSByRecordId(recordId);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void removeDNSByRecordIds(long[] recordIds)
			throws ConfigurationServiceException 
	{
		dnsSvc.removeDNSByRecordIds(recordIds);
		
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void updateDNS(long recordId, DNSRecord record)
			throws ConfigurationServiceException 
	{
		dnsSvc.updateDNS(recordId, record);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public Collection<TrustBundle> getTrustBundles(boolean fetchAnchors)
			throws ConfigurationServiceException 
	{
		return trustBundleSvc.getTrustBundles(fetchAnchors);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public TrustBundle getTrustBundleByName(String bundleName)
			throws ConfigurationServiceException 
	{
		return trustBundleSvc.getTrustBundleByName(bundleName);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public TrustBundle getTrustBundleById(long id)
			throws ConfigurationServiceException 
	{
		return trustBundleSvc.getTrustBundleById(id);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void addTrustBundle(TrustBundle bundle)
			throws ConfigurationServiceException 
	{
		trustBundleSvc.addTrustBundle(bundle);	
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void refreshTrustBundle(long id)
			throws ConfigurationServiceException 
	{
		trustBundleSvc.refreshTrustBundle(id);	
	}


    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void updateLastUpdateError(long trustBundleId, Calendar attemptTime,
			BundleRefreshError error) throws ConfigurationServiceException 
	{
		trustBundleSvc.updateLastUpdateError(trustBundleId, attemptTime, error);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void deleteTrustBundles(long[] trustBundleIds)
			throws ConfigurationServiceException 
	{
		trustBundleSvc.deleteTrustBundles(trustBundleIds);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void updateTrustBundleSigningCertificate(long trustBundleId,
			Certificate signingCert) throws ConfigurationServiceException 
	{
		trustBundleSvc.updateTrustBundleSigningCertificate(trustBundleId, signingCert);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void updateTrustBundleAttributes(long trustBundleId, String bundleName, String bundleUrl, Certificate signingCert,
			 int refreshInterval) throws ConfigurationServiceException
	{
		trustBundleSvc.updateTrustBundleAttributes(trustBundleId, bundleName, bundleUrl, signingCert, refreshInterval);
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void associateTrustBundleToDomain(long domainId, long trustBundleId, boolean incoming,
    		boolean outgoing)
			throws ConfigurationServiceException 
	{
		trustBundleSvc.associateTrustBundleToDomain(domainId, trustBundleId, incoming, outgoing);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void disassociateTrustBundleFromDomain(long domainId,
			long trustBundleId) throws ConfigurationServiceException 
	{
		trustBundleSvc.disassociateTrustBundleFromDomain(domainId, trustBundleId);	
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void disassociateTrustBundlesFromDomain(long domainId)
			throws ConfigurationServiceException 
	{
		trustBundleSvc.disassociateTrustBundlesFromDomain(domainId);	
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void disassociateTrustBundleFromDomains(long trustBundleId)
			throws ConfigurationServiceException 
	{
		trustBundleSvc.disassociateTrustBundleFromDomains(trustBundleId);	
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public Collection<TrustBundleDomainReltn> getTrustBundlesByDomain(long domainId,
			boolean fetchAnchors) throws ConfigurationServiceException 
	{
		return trustBundleSvc.getTrustBundlesByDomain(domainId, fetchAnchors);
	}   
	
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public Collection<CertPolicy> getPolicies() throws ConfigurationServiceException 
	{
		return certPolicySvc.getPolicies();
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public CertPolicy getPolicyByName(String policyName) throws ConfigurationServiceException 
	{
		return certPolicySvc.getPolicyByName(policyName);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public CertPolicy getPolicyById(long id) throws ConfigurationServiceException 
	{
		return certPolicySvc.getPolicyById(id);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void addPolicy(CertPolicy policy) throws ConfigurationServiceException 
	{
		certPolicySvc.addPolicy(policy);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void deletePolicies(long[] policyIds) throws ConfigurationServiceException 
	{
		certPolicySvc.deletePolicies(policyIds);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void updatePolicyAttributes(long id, String policyName,
			PolicyLexicon lexicon, byte[] policyData) throws ConfigurationServiceException 
	{
		certPolicySvc.updatePolicyAttributes(id, policyName, lexicon, policyData);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public Collection<CertPolicyGroup> getPolicyGroups() throws ConfigurationServiceException 
	{
		return certPolicySvc.getPolicyGroups();
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public CertPolicyGroup getPolicyGroupByName(String policyGroupName) throws ConfigurationServiceException 
	{
		return certPolicySvc.getPolicyGroupByName(policyGroupName);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public CertPolicyGroup getPolicyGroupById(long id) throws ConfigurationServiceException 
	{
		return certPolicySvc.getPolicyGroupById(id);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void addPolicyGroup(CertPolicyGroup group) throws ConfigurationServiceException 
	{
		certPolicySvc.addPolicyGroup(group);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void deletePolicyGroups(long[] groupIds) throws ConfigurationServiceException 
	{
		certPolicySvc.deletePolicyGroups(groupIds);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void updateGroupAttributes(long id, String groupName) throws ConfigurationServiceException 
	{	
		certPolicySvc.updateGroupAttributes(id, groupName);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void addPolicyUseToGroup(long groupId, long policyId, CertPolicyUse policyUse,
			boolean incoming, boolean outgoing) throws ConfigurationServiceException 
	{	
		certPolicySvc.addPolicyUseToGroup(groupId, policyId, policyUse, incoming, outgoing);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void removePolicyUseFromGroup(long policyGroupReltnId) throws ConfigurationServiceException 
	{
		certPolicySvc.removePolicyUseFromGroup(policyGroupReltnId);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void associatePolicyGroupToDomain(long domainId,long policyGroupId) throws ConfigurationServiceException 
	{	
		certPolicySvc.associatePolicyGroupToDomain(domainId, policyGroupId);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void disassociatePolicyGroupFromDomain(long domainId, long policyGroupId) throws ConfigurationServiceException 
	{
		certPolicySvc.disassociatePolicyGroupFromDomain(domainId, policyGroupId);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void disassociatePolicyGroupsFromDomain(long domainId) throws ConfigurationServiceException 
	{	
		certPolicySvc.disassociatePolicyGroupsFromDomain(domainId);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public void disassociatePolicyGroupFromDomains(long policyGroupId) throws ConfigurationServiceException 
	{
		certPolicySvc.disassociatePolicyGroupFromDomains(policyGroupId);
	}

	@Override
	@FaultAction(className = ConfigurationFault.class)
	public Collection<CertPolicyGroupDomainReltn> getPolicyGroupDomainReltns() throws ConfigurationServiceException
	{
		return certPolicySvc.getPolicyGroupDomainReltns();
	}
	
	@Override
	@FaultAction(className = ConfigurationFault.class)
	public Collection<CertPolicyGroupDomainReltn> getPolicyGroupsByDomain(long domainId) throws ConfigurationServiceException 
	{
		return certPolicySvc.getPolicyGroupsByDomain(domainId);
	}   
}
