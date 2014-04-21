package gov.hhs.fha.nhinc.directconfig.service.impl;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.directconfig.service.CertificatePolicyService;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationServiceException;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicy;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroup;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroupDomainReltn;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.CertPolicyUse;
import gov.hhs.fha.nhinc.directconfig.dao.CertPolicyDao;

import org.nhindirect.policy.PolicyLexicon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Service
@WebService(endpointInterface = "gov.hhs.fha.nhinc.directconfig.service.CertificatePolicyService")
public class CertificatePolicyServiceImpl extends SpringBeanAutowiringSupport implements CertificatePolicyService
{
    private static final Log log = LogFactory.getLog(CertificatePolicyServiceImpl.class);
    
    @Autowired
    private CertPolicyDao dao;
    
    /**
	 * Initialization method.
	 */
    @PostConstruct
    public void init() 
    {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        log.info("CertificatePolicyServiceImpl initialized");
    }
    
	@Override
	public Collection<CertPolicy> getPolicies() throws ConfigurationServiceException 
	{
		return dao.getPolicies();
	}

	@Override
	public CertPolicy getPolicyByName(String policyName) throws ConfigurationServiceException 
	{
		return dao.getPolicyByName(policyName);
	}

	@Override
	public CertPolicy getPolicyById(long id) throws ConfigurationServiceException 
	{
		return dao.getPolicyById(id);
	}

	@Override
	public void addPolicy(CertPolicy policy) throws ConfigurationServiceException 
	{
		dao.addPolicy(policy);
	}

	@Override
	public void deletePolicies(long[] policyIds) throws ConfigurationServiceException 
	{
		dao.deletePolicies(policyIds);
	}

	@Override
	public void updatePolicyAttributes(long id, String policyName,
			PolicyLexicon lexicon, byte[] policyData) throws ConfigurationServiceException 
	{
		dao.updatePolicyAttributes(id, policyName, lexicon, policyData);
	}

	@Override
	public Collection<CertPolicyGroup> getPolicyGroups() throws ConfigurationServiceException 
	{
		return dao.getPolicyGroups();
	}

	@Override
	public CertPolicyGroup getPolicyGroupByName(String policyGroupName) throws ConfigurationServiceException 
	{
		return dao.getPolicyGroupByName(policyGroupName);
	}

	@Override
	public CertPolicyGroup getPolicyGroupById(long id) throws ConfigurationServiceException 
	{
		return dao.getPolicyGroupById(id);
	}

	@Override
	public void addPolicyGroup(CertPolicyGroup group) throws ConfigurationServiceException 
	{
		dao.addPolicyGroup(group);
	}

	@Override
	public void deletePolicyGroups(long[] groupIds) throws ConfigurationServiceException 
	{
		dao.deletePolicyGroups(groupIds);
	}

	@Override
	public void updateGroupAttributes(long id, String groupName) throws ConfigurationServiceException 
	{	
		dao.updateGroupAttributes(id, groupName);
	}

	@Override
	public void addPolicyUseToGroup(long groupId, long policyId, CertPolicyUse policyUse,
			boolean incoming, boolean outgoing) throws ConfigurationServiceException 
	{	
		dao.addPolicyUseToGroup(groupId, policyId, policyUse, incoming, outgoing);
	}

	@Override
	public void removePolicyUseFromGroup(long policyGroupReltnId) throws ConfigurationServiceException 
	{
		dao.removePolicyUseFromGroup(policyGroupReltnId);
	}

	@Override
	public void associatePolicyGroupToDomain(long domainId,long policyGroupId) throws ConfigurationServiceException 
	{	
		dao.associatePolicyGroupToDomain(domainId, policyGroupId);
	}

	@Override
	public void disassociatePolicyGroupFromDomain(long domainId, long policyGroupId) throws ConfigurationServiceException 
	{
		dao.disassociatePolicyGroupFromDomain(domainId, policyGroupId);
	}

	@Override
	public void disassociatePolicyGroupsFromDomain(long domainId) throws ConfigurationServiceException 
	{	
		dao.disassociatePolicyGroupsFromDomain(domainId);
	}

	@Override
	public void disassociatePolicyGroupFromDomains(long policyGroupId) throws ConfigurationServiceException 
	{
		dao.disassociatePolicyGroupFromDomains(policyGroupId);
	}

	@Override
	public Collection<CertPolicyGroupDomainReltn> getPolicyGroupDomainReltns() throws ConfigurationServiceException
	{
		return dao.getPolicyGroupDomainReltns();
	}
	
	@Override
	public Collection<CertPolicyGroupDomainReltn> getPolicyGroupsByDomain(long domainId) throws ConfigurationServiceException 
	{
		return dao.getPolicyGroupsByDomain(domainId);
	}   
}
