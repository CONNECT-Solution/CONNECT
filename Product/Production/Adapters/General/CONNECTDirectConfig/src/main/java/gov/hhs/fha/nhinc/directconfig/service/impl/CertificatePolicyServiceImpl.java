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

import gov.hhs.fha.nhinc.directconfig.dao.CertPolicyDao;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicy;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroup;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroupDomainReltn;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.CertPolicyUse;
import gov.hhs.fha.nhinc.directconfig.service.CertificatePolicyService;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationServiceException;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nhindirect.policy.PolicyLexicon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Service("certPolicySvc")
@WebService(endpointInterface = "gov.hhs.fha.nhinc.directconfig.service.CertificatePolicyService", portName = "ConfigurationServiceImplPort", targetNamespace = "http://nhind.org/config")
public class CertificatePolicyServiceImpl extends SpringBeanAutowiringSupport implements CertificatePolicyService {
    private static final Log log = LogFactory.getLog(CertificatePolicyServiceImpl.class);

    @Autowired
    private CertPolicyDao dao;

    /**
     * Initialization method.
     */
    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        log.info("CertificatePolicyServiceImpl initialized");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<CertPolicy> getPolicies() throws ConfigurationServiceException {
        return dao.getPolicies();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CertPolicy getPolicyByName(String policyName) throws ConfigurationServiceException {
        return dao.getPolicyByName(policyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CertPolicy getPolicyById(long id) throws ConfigurationServiceException {
        return dao.getPolicyById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPolicy(CertPolicy policy) throws ConfigurationServiceException {
        dao.addPolicy(policy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePolicies(long[] policyIds) throws ConfigurationServiceException {
        dao.deletePolicies(policyIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePolicyAttributes(long id, String policyName, PolicyLexicon lexicon, byte[] policyData)
            throws ConfigurationServiceException {

        dao.updatePolicyAttributes(id, policyName, lexicon, policyData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<CertPolicyGroup> getPolicyGroups() throws ConfigurationServiceException {
        return dao.getPolicyGroups();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CertPolicyGroup getPolicyGroupByName(String policyGroupName) throws ConfigurationServiceException {
        return dao.getPolicyGroupByName(policyGroupName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CertPolicyGroup getPolicyGroupById(long id) throws ConfigurationServiceException {
        return dao.getPolicyGroupById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPolicyGroup(CertPolicyGroup group) throws ConfigurationServiceException {
        dao.addPolicyGroup(group);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePolicyGroups(long[] groupIds) throws ConfigurationServiceException {
        dao.deletePolicyGroups(groupIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateGroupAttributes(long id, String groupName) throws ConfigurationServiceException {
        dao.updateGroupAttributes(id, groupName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPolicyUseToGroup(long groupId, long policyId, CertPolicyUse policyUse, boolean incoming,
            boolean outgoing) throws ConfigurationServiceException {

        dao.addPolicyUseToGroup(groupId, policyId, policyUse, incoming, outgoing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePolicyUseFromGroup(long policyGroupReltnId) throws ConfigurationServiceException {
        dao.removePolicyUseFromGroup(policyGroupReltnId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void associatePolicyGroupToDomain(long domainId, long policyGroupId) throws ConfigurationServiceException {
        dao.associatePolicyGroupToDomain(domainId, policyGroupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disassociatePolicyGroupFromDomain(long domainId, long policyGroupId)
            throws ConfigurationServiceException {

        dao.disassociatePolicyGroupFromDomain(domainId, policyGroupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disassociatePolicyGroupsFromDomain(long domainId) throws ConfigurationServiceException {
        dao.disassociatePolicyGroupsFromDomain(domainId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disassociatePolicyGroupFromDomains(long policyGroupId) throws ConfigurationServiceException {
        dao.disassociatePolicyGroupFromDomains(policyGroupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<CertPolicyGroupDomainReltn> getPolicyGroupDomainReltns() throws ConfigurationServiceException {
        return dao.getPolicyGroupDomainReltns();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<CertPolicyGroupDomainReltn> getPolicyGroupsByDomain(long domainId)
            throws ConfigurationServiceException {

        return dao.getPolicyGroupsByDomain(domainId);
    }
}
