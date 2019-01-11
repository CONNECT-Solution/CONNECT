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

import gov.hhs.fha.nhinc.directconfig.entity.Address;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicy;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroup;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroupDomainReltn;
import gov.hhs.fha.nhinc.directconfig.entity.Certificate;
import gov.hhs.fha.nhinc.directconfig.entity.DNSRecord;
import gov.hhs.fha.nhinc.directconfig.entity.Domain;
import gov.hhs.fha.nhinc.directconfig.entity.Setting;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundle;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundleDomainReltn;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.BundleRefreshError;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.CertPolicyUse;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
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
import gov.hhs.fha.nhinc.directconfig.service.helpers.CertificateGetOptions;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.AddAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.AddAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.AddDomain;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.AddDomainResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchor;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchorResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchorsForOwner;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchorsForOwnerResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetIncomingAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetIncomingAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetOutgoingAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetOutgoingAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.ListAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.ListAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.RemoveAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.RemoveAnchorsForOwner;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.RemoveAnchorsForOwnerResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.RemoveAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.SetAnchorStatusForOwner;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.SetAnchorStatusForOwnerResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.UpdateDomain;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.UpdateDomainResponse;
import java.util.Calendar;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.jws.WebService;
import javax.xml.ws.FaultAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nhindirect.policy.PolicyLexicon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Implements the single Service Endpoint Interface. Delegates everything to the individual service implementations.
 */

@Service
@WebService(endpointInterface = "gov.hhs.fha.nhinc.directconfig.service.ConfigurationService", portName = "ConfigurationServiceImplPort", targetNamespace = "http://nhind.org/config")
public class ConfigurationServiceImpl extends SpringBeanAutowiringSupport implements ConfigurationService {

    private static Log log = LogFactory.getLog(ConfigurationServiceImpl.class);

    @Autowired
    private DomainService domainSvc;

    @Autowired
    private AddressService addressSvc;

    @Autowired
    private CertificateService certSvc;

    @Autowired
    private AnchorService anchorSvc;

    @Autowired
    private SettingService settingSvc;

    @Autowired
    private TrustBundleService trustBundleSvc;

    @Autowired
    private CertificatePolicyService certPolicySvc;

    @Autowired
    private DNSService dnsSvc;

    /**
     * Initialization method.
     */
    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        log.info("ConfigurationService initialized");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addAddress(Collection<Address> address) throws ConfigurationServiceException {
        addressSvc.addAddress(address);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void updateAddress(Address address) throws ConfigurationServiceException {
        addressSvc.updateAddress(address);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public int getAddressCount() throws ConfigurationServiceException {
        return addressSvc.getAddressCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Address> getAddress(Collection<String> addressNames, EntityStatus status)
            throws ConfigurationServiceException {

        return addressSvc.getAddress(addressNames, status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeAddress(String addressName) throws ConfigurationServiceException {
        addressSvc.removeAddress(addressName);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Address> listAddresss(String lastAddressName, int maxResults)
            throws ConfigurationServiceException {

        return addressSvc.listAddresss(lastAddressName, maxResults);
    }

    /**
     * {@inheritDoc}
     *
     * @param domain
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public AddDomainResponse addDomain(AddDomain domain) throws ConfigurationServiceException {
        domainSvc.addDomain(domain);
        return new AddDomainResponse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public UpdateDomainResponse updateDomain(UpdateDomain domain) throws ConfigurationServiceException {
        domainSvc.updateDomain(domain);
        return new UpdateDomainResponse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public int getDomainCount() throws ConfigurationServiceException {
        return domainSvc.getDomainCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Domain> getDomains(Collection<String> domainNames, EntityStatus status)
            throws ConfigurationServiceException {

        return domainSvc.getDomains(domainNames, status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    @FaultAction(className = ConfigurationFault.class)
    public void removeDomain(String domainName) throws ConfigurationServiceException {
        domainSvc.removeDomain(domainName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeDomainById(Long domainId) throws ConfigurationServiceException {
        domainSvc.removeDomainById(domainId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Domain> listDomains(String lastDomainName, int maxResults) throws ConfigurationServiceException {
        return domainSvc.listDomains(lastDomainName, maxResults);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Domain> searchDomain(String domain, EntityStatus status) {
        return domainSvc.searchDomain(domain, status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Domain getDomain(Long id) {
        return domainSvc.getDomain(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addCertificates(Collection<Certificate> certs) throws ConfigurationServiceException {
        certSvc.addCertificates(certs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void setCertificateStatus(Collection<Long> certificateIds, EntityStatus status)
            throws ConfigurationServiceException {

        certSvc.setCertificateStatus(certificateIds, status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void setCertificateStatusForOwner(String owner, EntityStatus status) throws ConfigurationServiceException {
        certSvc.setCertificateStatusForOwner(owner, status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeCertificates(Collection<Long> certificateIds) throws ConfigurationServiceException {
        certSvc.removeCertificates(certificateIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeCertificatesForOwner(String owner) throws ConfigurationServiceException {
        certSvc.removeCertificatesForOwner(owner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public boolean contains(Certificate cert) {
        return certSvc.contains(cert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public AddAnchorsResponse addAnchors(AddAnchors addAnchors) throws ConfigurationServiceException {
        return anchorSvc.addAnchors(addAnchors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public SetAnchorStatusForOwnerResponse setAnchorStatusForOwner(SetAnchorStatusForOwner setAnchorStatusForOwner)
            throws ConfigurationServiceException {

        return anchorSvc.setAnchorStatusForOwner(setAnchorStatusForOwner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public RemoveAnchorsResponse removeAnchors(RemoveAnchors removeAnchors) throws ConfigurationServiceException {
        return anchorSvc.removeAnchors(removeAnchors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public RemoveAnchorsForOwnerResponse removeAnchorsForOwner(RemoveAnchorsForOwner removeAnchorsForOwner)
            throws ConfigurationServiceException {

        return anchorSvc.removeAnchorsForOwner(removeAnchorsForOwner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Certificate getCertificate(String owner, String thumbprint, CertificateGetOptions options)
            throws ConfigurationServiceException {

        return certSvc.getCertificate(owner, thumbprint, options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Certificate> getCertificates(Collection<Long> certIds, CertificateGetOptions options)
            throws ConfigurationServiceException {

        return certSvc.getCertificates(certIds, options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Certificate> getCertificatesForOwner(String owner, CertificateGetOptions options)
            throws ConfigurationServiceException {

        return certSvc.getCertificatesForOwner(owner, options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Certificate> listCertificates(long lastCertificateId, int maxResults,
            CertificateGetOptions options) throws ConfigurationServiceException {

        return certSvc.listCertificates(lastCertificateId, maxResults, options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public GetAnchorResponse getAnchor(GetAnchor getAnchor) throws ConfigurationServiceException {
        return anchorSvc.getAnchor(getAnchor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public GetAnchorsResponse getAnchors(GetAnchors getAnchors) throws ConfigurationServiceException {
        return anchorSvc.getAnchors(getAnchors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public GetAnchorsForOwnerResponse getAnchorsForOwner(GetAnchorsForOwner getAnchorsForOwner)
            throws ConfigurationServiceException {

        return anchorSvc.getAnchorsForOwner(getAnchorsForOwner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public GetIncomingAnchorsResponse getIncomingAnchors(GetIncomingAnchors getIncomingAnchors)
            throws ConfigurationServiceException {

        return anchorSvc.getIncomingAnchors(getIncomingAnchors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public GetOutgoingAnchorsResponse getOutgoingAnchors(GetOutgoingAnchors getOutgoingAnchors)
            throws ConfigurationServiceException {

        return anchorSvc.getOutgoingAnchors(getOutgoingAnchors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public ListAnchorsResponse listAnchors(ListAnchors listAnchors) throws ConfigurationServiceException {
        return anchorSvc.listAnchors(listAnchors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addSetting(String name, String value) throws ConfigurationServiceException {
        settingSvc.addSetting(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Setting> getAllSettings() throws ConfigurationServiceException {
        return settingSvc.getAllSettings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Setting getSettingByName(String name) throws ConfigurationServiceException {
        return settingSvc.getSettingByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<Setting> getSettingsByNames(Collection<String> names) throws ConfigurationServiceException {
        return settingSvc.getSettingsByNames(names);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void updateSetting(String name, String value) throws ConfigurationServiceException {
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
    public Collection<TrustBundle> getTrustBundles(boolean fetchAnchors) throws ConfigurationServiceException {
        return trustBundleSvc.getTrustBundles(fetchAnchors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public TrustBundle getTrustBundleByName(String bundleName) throws ConfigurationServiceException {
        return trustBundleSvc.getTrustBundleByName(bundleName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public TrustBundle getTrustBundleById(long id) throws ConfigurationServiceException {
        return trustBundleSvc.getTrustBundleById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addTrustBundle(TrustBundle bundle) throws ConfigurationServiceException {
        trustBundleSvc.addTrustBundle(bundle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void refreshTrustBundle(long id) throws ConfigurationServiceException {
        trustBundleSvc.refreshTrustBundle(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void updateLastUpdateError(long trustBundleId, Calendar attemptTime, BundleRefreshError error)
            throws ConfigurationServiceException {

        trustBundleSvc.updateLastUpdateError(trustBundleId, attemptTime, error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void deleteTrustBundles(long[] trustBundleIds) throws ConfigurationServiceException {
        trustBundleSvc.deleteTrustBundles(trustBundleIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void updateTrustBundleSigningCertificate(long trustBundleId, Certificate signingCert)
            throws ConfigurationServiceException {

        trustBundleSvc.updateTrustBundleSigningCertificate(trustBundleId, signingCert);
    }

    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void updateTrustBundleAttributes(long trustBundleId, String bundleName, String bundleUrl,
            Certificate signingCert, int refreshInterval) throws ConfigurationServiceException {

        trustBundleSvc.updateTrustBundleAttributes(trustBundleId, bundleName, bundleUrl, signingCert, refreshInterval);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void associateTrustBundleToDomain(long domainId, long trustBundleId, boolean incoming, boolean outgoing)
            throws ConfigurationServiceException {

        trustBundleSvc.associateTrustBundleToDomain(domainId, trustBundleId, incoming, outgoing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void disassociateTrustBundleFromDomain(long domainId, long trustBundleId)
            throws ConfigurationServiceException {

        trustBundleSvc.disassociateTrustBundleFromDomain(domainId, trustBundleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void disassociateTrustBundlesFromDomain(long domainId) throws ConfigurationServiceException {
        trustBundleSvc.disassociateTrustBundlesFromDomain(domainId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void disassociateTrustBundleFromDomains(long trustBundleId) throws ConfigurationServiceException {
        trustBundleSvc.disassociateTrustBundleFromDomains(trustBundleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<TrustBundleDomainReltn> getTrustBundlesByDomain(long domainId, boolean fetchAnchors)
            throws ConfigurationServiceException {

        return trustBundleSvc.getTrustBundlesByDomain(domainId, fetchAnchors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<CertPolicy> getPolicies() throws ConfigurationServiceException {
        return certPolicySvc.getPolicies();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public CertPolicy getPolicyByName(String policyName) throws ConfigurationServiceException {
        return certPolicySvc.getPolicyByName(policyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public CertPolicy getPolicyById(long id) throws ConfigurationServiceException {
        return certPolicySvc.getPolicyById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addPolicy(CertPolicy policy) throws ConfigurationServiceException {
        certPolicySvc.addPolicy(policy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void deletePolicies(long[] policyIds) throws ConfigurationServiceException {
        certPolicySvc.deletePolicies(policyIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void updatePolicyAttributes(long id, String policyName, PolicyLexicon lexicon, byte[] policyData)
            throws ConfigurationServiceException {

        certPolicySvc.updatePolicyAttributes(id, policyName, lexicon, policyData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<CertPolicyGroup> getPolicyGroups() throws ConfigurationServiceException {
        return certPolicySvc.getPolicyGroups();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public CertPolicyGroup getPolicyGroupByName(String policyGroupName) throws ConfigurationServiceException {
        return certPolicySvc.getPolicyGroupByName(policyGroupName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public CertPolicyGroup getPolicyGroupById(long id) throws ConfigurationServiceException {
        return certPolicySvc.getPolicyGroupById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addPolicyGroup(CertPolicyGroup group) throws ConfigurationServiceException {
        certPolicySvc.addPolicyGroup(group);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void deletePolicyGroups(long[] groupIds) throws ConfigurationServiceException {
        certPolicySvc.deletePolicyGroups(groupIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void updateGroupAttributes(long id, String groupName) throws ConfigurationServiceException {
        certPolicySvc.updateGroupAttributes(id, groupName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addPolicyUseToGroup(long groupId, long policyId, CertPolicyUse policyUse, boolean incoming,
            boolean outgoing) throws ConfigurationServiceException {

        certPolicySvc.addPolicyUseToGroup(groupId, policyId, policyUse, incoming, outgoing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removePolicyUseFromGroup(long policyGroupReltnId) throws ConfigurationServiceException {
        certPolicySvc.removePolicyUseFromGroup(policyGroupReltnId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void associatePolicyGroupToDomain(long domainId, long policyGroupId) throws ConfigurationServiceException {
        certPolicySvc.associatePolicyGroupToDomain(domainId, policyGroupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void disassociatePolicyGroupFromDomain(long domainId, long policyGroupId)
            throws ConfigurationServiceException {

        certPolicySvc.disassociatePolicyGroupFromDomain(domainId, policyGroupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void disassociatePolicyGroupsFromDomain(long domainId) throws ConfigurationServiceException {
        certPolicySvc.disassociatePolicyGroupsFromDomain(domainId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void disassociatePolicyGroupFromDomains(long policyGroupId) throws ConfigurationServiceException {
        certPolicySvc.disassociatePolicyGroupFromDomains(policyGroupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<CertPolicyGroupDomainReltn> getPolicyGroupDomainReltns() throws ConfigurationServiceException {
        return certPolicySvc.getPolicyGroupDomainReltns();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<CertPolicyGroupDomainReltn> getPolicyGroupsByDomain(long domainId)
            throws ConfigurationServiceException {

        return certPolicySvc.getPolicyGroupsByDomain(domainId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void addDNS(Collection<DNSRecord> records) throws ConfigurationServiceException {
        dnsSvc.addDNS(records);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<DNSRecord> getDNSByName(String name) throws ConfigurationServiceException {
        return dnsSvc.getDNSByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<DNSRecord> getDNSByNameAndType(String name, int type) throws ConfigurationServiceException {
        return dnsSvc.getDNSByNameAndType(name, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public DNSRecord getDNSByRecordId(long recordId) throws ConfigurationServiceException {
        return dnsSvc.getDNSByRecordId(recordId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<DNSRecord> getDNSByRecordIds(long[] recordIds) throws ConfigurationServiceException {
        return dnsSvc.getDNSByRecordIds(recordIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public Collection<DNSRecord> getDNSByType(int type) throws ConfigurationServiceException {
        return dnsSvc.getDNSByType(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public int getDNSCount() throws ConfigurationServiceException {
        return dnsSvc.getDNSCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeDNS(Collection<DNSRecord> records) throws ConfigurationServiceException {
        dnsSvc.removeDNS(records);
    }

    @Override
    public void removeDNSByRecordId(long recordId) throws ConfigurationServiceException {
        dnsSvc.removeDNSByRecordId(recordId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void removeDNSByRecordIds(long[] recordIds) throws ConfigurationServiceException {
        dnsSvc.removeDNSByRecordIds(recordIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FaultAction(className = ConfigurationFault.class)
    public void updateDNS(long recordId, DNSRecord record) throws ConfigurationServiceException {
        dnsSvc.updateDNS(recordId, record);
    }
}
