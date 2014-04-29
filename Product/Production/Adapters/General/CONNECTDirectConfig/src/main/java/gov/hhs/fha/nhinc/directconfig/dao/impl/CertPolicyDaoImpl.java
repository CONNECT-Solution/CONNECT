package gov.hhs.fha.nhinc.directconfig.dao.impl;

import gov.hhs.fha.nhinc.directconfig.dao.CertPolicyDao;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicy;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroup;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroupDomainReltn;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.CertPolicyUse;
import gov.hhs.fha.nhinc.directconfig.exception.ConfigurationStoreException;

import java.util.Collection;

import org.apache.commons.lang.NotImplementedException;
import org.nhindirect.policy.PolicyLexicon;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CertPolicyDaoImpl implements CertPolicyDao {

    public CertPolicyDaoImpl() {
    }

    @Override
    public Collection<CertPolicy> getPolicies() throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public CertPolicy getPolicyByName(String policyName) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public CertPolicy getPolicyById(long id) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public void addPolicy(CertPolicy policy) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public void deletePolicies(long[] policyIds) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public void updatePolicyAttributes(long id, String policyName,
            PolicyLexicon lexicon, byte[] policyData) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public Collection<CertPolicyGroup> getPolicyGroups() throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public CertPolicyGroup getPolicyGroupByName(String policyGroupName) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public CertPolicyGroup getPolicyGroupById(long id) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public void addPolicyGroup(CertPolicyGroup group) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public void deletePolicyGroups(long[] groupIds) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public void updateGroupAttributes(long id, String groupName) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    @Transactional(readOnly = false)
    public void addPolicyUseToGroup(long groupId, long policyId, CertPolicyUse policyUse, boolean incoming,
            boolean outgoing) throws ConfigurationStoreException {

        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public void removePolicyUseFromGroup(long policyGroupReltnId) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public void associatePolicyGroupToDomain(long domainId, long policyGroupId) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public void disassociatePolicyGroupFromDomain(long domainId, long policyGroupId) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());    }

    @Override
    public void disassociatePolicyGroupsFromDomain(long domainId) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public void disassociatePolicyGroupFromDomains(long policyGroupId) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public Collection<CertPolicyGroupDomainReltn> getPolicyGroupDomainReltns() throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    @Override
    public Collection<CertPolicyGroupDomainReltn> getPolicyGroupsByDomain(long domainId) throws ConfigurationStoreException {
        throw new ConfigurationStoreException(new NotImplementedException());
    }
}
