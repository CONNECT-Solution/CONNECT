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

import java.util.Calendar;
import java.util.Collection;

import javax.jws.WebService;
import javax.xml.ws.FaultAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.directconfig.service.AddressService;
import gov.hhs.fha.nhinc.directconfig.service.AnchorService;
import gov.hhs.fha.nhinc.directconfig.service.CertificateService;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationFault;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationService;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationServiceException;
import gov.hhs.fha.nhinc.directconfig.service.DomainService;
import gov.hhs.fha.nhinc.directconfig.service.SettingService;
import gov.hhs.fha.nhinc.directconfig.service.TrustBundleService;
import gov.hhs.fha.nhinc.directconfig.entity.Address;
import gov.hhs.fha.nhinc.directconfig.entity.Anchor;
import gov.hhs.fha.nhinc.directconfig.entity.Certificate;
import gov.hhs.fha.nhinc.directconfig.entity.Domain;
import gov.hhs.fha.nhinc.directconfig.entity.Setting;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundle;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundleDomainReltn;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.BundleRefreshError;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implements the single Service Endpoint Interface. Delegates everything to the
 * individual service implementations.
 */
//
//@WebService(endpointInterface = "gov.hhs.fha.nhinc.directconfig.service.ConfigurationService")
@org.springframework.stereotype.Service
@WebService(endpointInterface="gov.hhs.fha.nhinc.directconfig.service.ConfigurationService", 
targetNamespace="http://nhind.org/config", 
serviceName="ConfigurationService", 
portName="ConfigurationService", 
wsdlLocation="wsdl/ConfigurationService.wsdl")
public class ConfigurationServiceImpl extends org.springframework.web.context.support.SpringBeanAutowiringSupport implements ConfigurationService {


    private static Log log = LogFactory.getLog(ConfigurationServiceImpl.class);

    private DomainService domainSvc;

    private AddressService addressSvc;

    private CertificateService certSvc;

    private AnchorService anchorSvc;

    private SettingService settingSvc;
    
    private TrustBundleService trustBundleSvc;
  
    /**
     * Initialization method.
     */
    public void init() {
        log.info("ConfigurationService initialized");
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AddressService#addAddress(java.util.List)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addAddress(Collection<Address> address) throws ConfigurationServiceException {
        addressSvc.addAddress(address);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AddressService#updateAddress(gov.hhs.fha.nhinc.directconfig.entity.Address)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void updateAddress(Address address) throws ConfigurationServiceException {
        addressSvc.updateAddress(address);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AddressService#getAddressCount()
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public int getAddressCount() throws ConfigurationServiceException {
        return addressSvc.getAddressCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AddressService#getAddress(java.util.List, gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
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
     * @see gov.hhs.fha.nhinc.directconfig.service.AddressService#removeAddress(java.lang.String)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeAddress(String addressName) throws ConfigurationServiceException {
        addressSvc.removeAddress(addressName);

    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AddressService#listAddresss(java.lang.String, int)
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
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#addDomain(gov.hhs.fha.nhinc.directconfig.entity.Domain)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addDomain(Domain domain) throws ConfigurationServiceException {
        domainSvc.addDomain(domain);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#updateDomain(gov.hhs.fha.nhinc.directconfig.entity.Domain)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void updateDomain(Domain domain) throws ConfigurationServiceException {
        domainSvc.updateDomain(domain);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#getDomainCount()
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public int getDomainCount() throws ConfigurationServiceException {
        return domainSvc.getDomainCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#getDomains(java.util.Collection, gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
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
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#removeDomain(java.lang.String)
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
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#removeDomainById(java.lang.Long)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeDomainById(Long domainId) throws ConfigurationServiceException {
        domainSvc.removeDomainById(domainId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#listDomains(java.lang.String, int)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Domain> listDomains(String lastDomainName, int maxResults) throws ConfigurationServiceException {
        return domainSvc.listDomains(lastDomainName, maxResults);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#searchDomain(java.lang.String, gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Domain> searchDomain(String domain, EntityStatus status) {
        return domainSvc.searchDomain(domain, status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#getDomain(java.lang.Long)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Domain getDomain(Long id) {
        return domainSvc.getDomain(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.CertificateService#addCertificates(java.util.Collection)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addCertificates(Collection<Certificate> certs) throws ConfigurationServiceException {
        certSvc.addCertificates(certs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.CertificateService#setCertificateStatus(java.util.Collection, gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
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
     * @see gov.hhs.fha.nhinc.directconfig.service.CertificateService#setCertificateStatusForOwner(java.lang.String, gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void setCertificateStatusForOwner(String owner, EntityStatus status) throws ConfigurationServiceException {
        certSvc.setCertificateStatusForOwner(owner, status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.CertificateService#removeCertificates(java.util.Collection)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeCertificates(Collection<Long> certificateIds) throws ConfigurationServiceException {
        certSvc.removeCertificates(certificateIds);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.CertificateService#removeCertificatesForOwner(java.lang.String)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeCertificatesForOwner(String owner) throws ConfigurationServiceException {
        certSvc.removeCertificatesForOwner(owner);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.CertificateService#contains(java.security.cert.Certificate)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public boolean contains(Certificate cert) {
        return certSvc.contains(cert);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#addAnchors(java.util.List)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addAnchors(Collection<Anchor> anchors) throws ConfigurationServiceException {
        anchorSvc.addAnchors(anchors);

    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#setAnchorStatusForOwner(java.lang.String, gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void setAnchorStatusForOwner(String owner, EntityStatus status) throws ConfigurationServiceException {
        anchorSvc.setAnchorStatusForOwner(owner, status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#removeAnchors(java.util.List)
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeAnchors(Collection<Long> anchorIds) throws ConfigurationServiceException {
        anchorSvc.removeAnchors(anchorIds);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#removeAnchorsForOwner(java.lang.String)
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
    
    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.CertificateService#getCertificate(java.lang.String, java.lang.String, gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
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
     * @see gov.hhs.fha.nhinc.directconfig.service.CertificateService#getCertificates(java.util.Collection, gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
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
     * @see gov.hhs.fha.nhinc.directconfig.service.CertificateService#getCertificatesForOwner(java.lang.String, gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
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
     * @see gov.hhs.fha.nhinc.directconfig.service.CertificateService#listCertificates(long, int, gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
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
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#getAnchor(java.lang.String, java.lang.String, gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
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
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#getAnchors(java.util.Collection, gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
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
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#getAnchorsForOwner(java.lang.String, gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
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
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#getIncomingAnchors(java.lang.String, gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
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
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#getOutgoingAnchors(java.lang.String, gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
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
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#listAnchors(java.lang.Long, int, gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
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
}
