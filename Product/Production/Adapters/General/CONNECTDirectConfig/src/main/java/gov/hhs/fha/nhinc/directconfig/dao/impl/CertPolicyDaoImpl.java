package gov.hhs.fha.nhinc.directconfig.dao.impl;

import gov.hhs.fha.nhinc.directconfig.dao.CertPolicyDao;
import gov.hhs.fha.nhinc.directconfig.dao.DomainDao;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicy;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroup;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroupDomainReltn;
//import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroupReltn;
//import gov.hhs.fha.nhinc.directconfig.entity.Domain;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.CertPolicyUse;
import gov.hhs.fha.nhinc.directconfig.exception.ConfigurationStoreException;

//import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
//import java.util.Locale;

//import javax.persistence.NoResultException;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
//import org.hibernate.Query;
import org.nhindirect.policy.PolicyLexicon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CertPolicyDaoImpl implements CertPolicyDao
{
    @Autowired
    protected SessionFactory sessionFactory;

    @Autowired
    protected DomainDao domainDao;

    //private static final Log log = LogFactory.getLog(CertPolicyDaoImpl.class);

    public CertPolicyDaoImpl()
    {

    }

	//@SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
	public Collection<CertPolicy> getPolicies() throws ConfigurationStoreException
	{
//        try
//        {
//	        final Query select = sessionFactory.getCurrentSession().createQuery("SELECT cp from CertPolicy cp");
//
//	        final Collection<CertPolicy> rs = select.list();
//	        if (rs.size() == 0)
//	        	return Collections.emptyList();
//
//	        return rs;
//        }
//      	catch (Exception e)
//    	{
//    		throw new ConfigurationStoreException("Failed to execute certificate policy DAO query.", e);
//    	}

		return Collections.emptyList();
	}

	@Override
    @Transactional(readOnly = true)
	public CertPolicy getPolicyByName(String policyName) throws ConfigurationStoreException
	{
//        try
//        {
//            final Query select = sessionFactory.getCurrentSession().createQuery("SELECT cp from CertPolicy cp WHERE UPPER(cp.policyName) = ?");
//            select.setParameter(0, policyName.toUpperCase(Locale.getDefault()));
//
//            final CertPolicy rs = (CertPolicy)select.uniqueResult();
//
//	        return rs;
//        }
//        catch (NoResultException e)
//        {
//        	return null;
//        }
//      	catch (Exception e)
//    	{
//    		throw new ConfigurationStoreException("Failed to execute certificate policy DAO query.", e);
//    	}

		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public CertPolicy getPolicyById(long id) throws ConfigurationStoreException
	{
//        try
//        {
//            final Query select = sessionFactory.getCurrentSession().createQuery("SELECT cp from CertPolicy cp WHERE cp.id = ?");
//            select.setParameter(0, id);
//
//            final CertPolicy rs = (CertPolicy)select.uniqueResult();
//
//	        return rs;
//        }
//        catch (NoResultException e)
//        {
//        	return null;
//        }
//      	catch (Exception e)
//    	{
//    		throw new ConfigurationStoreException("Failed to execute certificate policy DAO query.", e);
//    	}

		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public void addPolicy(CertPolicy policy) throws ConfigurationStoreException
	{
//    	try
//    	{
//			final CertPolicy existingPolicy = this.getPolicyByName(policy.getPolicyName());
//			if (existingPolicy != null)
//				throw new ConfigurationStoreException("Certificate policy " + policy.getPolicyName() + " already exists");
//
//          policy.setId(null);
//			policy.setCreateTime(Calendar.getInstance(Locale.getDefault()));
//
//			sessionFactory.getCurrentSession().persist(policy);
//    	}
//    	catch (ConfigurationStoreException cse)
//    	{
//    		throw cse;
//    	}
//    	///CLOVER:OFF
//    	catch (Exception e)
//    	{
//    		throw new ConfigurationStoreException("Failed to add certificate policy " + policy.getPolicyName(), e);
//    	}
//    	///CLOVER:ON
	}

	@Override
	@Transactional(readOnly = false)
	public void deletePolicies(long[] policyIds) throws ConfigurationStoreException
	{
//        if (policyIds == null || policyIds.length == 0)
//        	return;
//
//        for (long id : policyIds)
//        {
//        	try
//        	{
//        		final CertPolicy bundle = this.getPolicyById(id);
//
//        		this.removePolicyUseFromGroups(id);
//
//        		sessionFactory.getCurrentSession().delete(bundle);
//        	}
//        	catch (ConfigurationStoreException e)
//        	{
//        		log.warn(e.getMessage(), e);
//        	}
//
//        }
//
	}

	/**
	 * {@inheritDoc}
	 */
    @Transactional(readOnly = false)
	public void removePolicyUseFromGroups(long policyId) throws ConfigurationStoreException
	{
//		// make sure the trust bundle exists
//		final CertPolicy policy = this.getPolicyById(policyId);
//		if (policy == null)
//			throw new ConfigurationStoreException("Certificate policy with id " + policyId + " does not exist");
//
//		try
//		{
//			final Query delete = sessionFactory.getCurrentSession().createQuery("DELETE from CertPolicyGroupReltn cpr where cpr.certPolicy  = ?");
//
//	        delete.setParameter(0, policy);
//	        delete.executeUpdate();
//		}
//		catch (Exception e)
//		{
//			throw new ConfigurationStoreException("Failed to remove policy id " + policyId + " from all groups" , e);
//		}
	}

	@Override
	@Transactional(readOnly = false)
	public void updatePolicyAttributes(long id, String policyName,
			PolicyLexicon lexicon, byte[] policyData) throws ConfigurationStoreException
	{
//    	try
//    	{
//			final CertPolicy existingPolicy = this.getPolicyById(id);
//			if (existingPolicy == null)
//				throw new ConfigurationStoreException("Policy does not exist");
//
//			if (policyData != null && policyData.length > 0)
//				existingPolicy.setPolicyData(policyData);
//
//
//			if (policyName != null && !policyName.isEmpty())
//				existingPolicy.setPolicyName(policyName);
//
//			if (lexicon != null)
//				existingPolicy.setLexicon(lexicon);
//
//			sessionFactory.getCurrentSession().persist(existingPolicy);
//    	}
//    	catch (ConfigurationStoreException cse)
//    	{
//    		throw cse;
//    	}
//    	///CLOVER:OFF
//    	catch (Exception e)
//    	{
//    		throw new ConfigurationStoreException("Failed to update certificate policy attributes.", e);
//    	}
//    	///CLOVER:ON
//
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<CertPolicyGroup> getPolicyGroups() throws ConfigurationStoreException
	{
//        try
//        {
//	        final Query select = sessionFactory.getCurrentSession().createQuery("SELECT cpg from CertPolicyGroup cpg");
//
//	        @SuppressWarnings("unchecked")
//			final Collection<CertPolicyGroup> rs = select.list();
//	        if (rs.size() == 0)
//	        	return Collections.emptyList();
//
//            // load relationships now as they were deferred by lazy loading
//            for (CertPolicyGroup group : rs)
//            	group.getCertPolicyGroupReltn().size();
//
//	        return rs;
//        }
//      	catch (Exception e)
//    	{
//    		throw new ConfigurationStoreException("Failed to execute certificate policy group DAO query.", e);
//    	}

		return Collections.emptyList();
	}

	@Override
	@Transactional(readOnly = true)
	public CertPolicyGroup getPolicyGroupByName(String policyGroupName) throws ConfigurationStoreException
	{
//        try
//        {
//            final Query select = sessionFactory.getCurrentSession().createQuery("SELECT cpg from CertPolicyGroup cpg WHERE UPPER(cpg.policyGroupName) = ?");
//            select.setParameter(0, policyGroupName.toUpperCase(Locale.getDefault()));
//
//            final CertPolicyGroup rs = (CertPolicyGroup)select.uniqueResult();
//
//            // load relationships now as they were deferred by lazy loading
//            rs.getCertPolicyGroupReltn().size();
//
//	        return rs;
//        }
//        catch (NoResultException e)
//        {
//        	return null;
//        }
//      	catch (Exception e)
//    	{
//    		throw new ConfigurationStoreException("Failed to execute certificate policy group DAO query.", e);
//    	}

		return new CertPolicyGroup();
	}

	@Override
	@Transactional(readOnly = true)
	public CertPolicyGroup getPolicyGroupById(long id) throws ConfigurationStoreException
	{
//        try
//        {
//            final Query select = sessionFactory.getCurrentSession().createQuery("SELECT cpg from CertPolicyGroup cpg WHERE cpg.id = ?");
//            select.setParameter(0, id);
//
//            final CertPolicyGroup rs = (CertPolicyGroup)select.uniqueResult();
//
//            // load relationships now as they were deferred by lazy loading
//            rs.getCertPolicyGroupReltn().size();
//
//	        return rs;
//        }
//        catch (NoResultException e)
//        {
//        	return null;
//        }
//      	catch (Exception e)
//    	{
//    		throw new ConfigurationStoreException("Failed to execute certificate policy group DAO query.", e);
//    	}

		return new CertPolicyGroup();
	}

	@Override
	@Transactional(readOnly = false)
	public void addPolicyGroup(CertPolicyGroup group) throws ConfigurationStoreException
	{
//    	try
//    	{
//			final CertPolicyGroup existingPolicyGroup = this.getPolicyGroupByName(group.getPolicyGroupName());
//			if (existingPolicyGroup != null)
//				throw new ConfigurationStoreException("Certificate policy group " + group.getPolicyGroupName() + " already exists");
//
//			group.setId(null);
//			group.setCreateTime(Calendar.getInstance(Locale.getDefault()));
//
//			sessionFactory.getCurrentSession().persist(group);
//    	}
//    	catch (ConfigurationStoreException cse)
//    	{
//    		throw cse;
//    	}
//    	///CLOVER:OFF
//    	catch (Exception e)
//    	{
//    		throw new ConfigurationStoreException("Failed to add certificate policy group " + group.getPolicyGroupName(), e);
//    	}
//    	///CLOVER:ON
//
	}

	@Override
	@Transactional(readOnly = false)
	public void deletePolicyGroups(long[] groupIds) throws ConfigurationStoreException
	{
//        if (groupIds == null || groupIds.length == 0)
//        	return;
//
//        for (long id : groupIds)
//        {
//        	try
//        	{
//        		final CertPolicyGroup bundle = this.getPolicyGroupById(id);
//
//        		sessionFactory.getCurrentSession().delete(bundle);
//        	}
//        	catch (ConfigurationStoreException e)
//        	{
//        		log.warn(e.getMessage(), e);
//        	}
//        }
//
	}

	@Override
	@Transactional(readOnly = false)
	public void updateGroupAttributes(long id, String groupName) throws ConfigurationStoreException
	{
//    	try
//    	{
//			final CertPolicyGroup existingPolicyGroup = this.getPolicyGroupById(id);
//			if (existingPolicyGroup == null)
//				throw new ConfigurationStoreException("Policy group does not exist");
//
//
//			if (groupName != null && !groupName.isEmpty())
//				existingPolicyGroup.setPolicyGroupName(groupName);
//
//			sessionFactory.getCurrentSession().persist(existingPolicyGroup);
//    	}
//    	catch (ConfigurationStoreException cse)
//    	{
//    		throw cse;
//    	}
//    	///CLOVER:OFF
//    	catch (Exception e)
//    	{
//    		throw new ConfigurationStoreException("Failed to update certificate policy attributes.", e);
//    	}
//    	///CLOVER:ON
//
	}

	@Override
	@Transactional(readOnly = false)
	public void addPolicyUseToGroup(long groupId, long policyId,
			CertPolicyUse policyUse, boolean incoming, boolean outgoing) throws ConfigurationStoreException
	{
//		// make sure the policy exists
//		final CertPolicyGroup policyGroup = this.getPolicyGroupById(groupId);
//		if (policyGroup == null)
//			throw new ConfigurationStoreException("Policy group with id " + groupId + " does not exist");
//
//		// make sure the policy exists
//		final CertPolicy policy = this.getPolicyById(policyId);
//		if (policy == null)
//			throw new ConfigurationStoreException("Policy with id " + policyId + " does not exist");
//
//		try
//		{
//			final CertPolicyGroupReltn reltn = new CertPolicyGroupReltn();
//			reltn.setCertPolicy(policy);
//			reltn.setCertPolicyGroup(policyGroup);
//			reltn.setPolicyUse(policyUse);
//			reltn.setIncoming(incoming);
//			reltn.setOutgoing(outgoing);
//
//			policyGroup.getCertPolicyGroupReltn().add(reltn);
//			sessionFactory.getCurrentSession().persist(policyGroup);
//		}
//		catch (Exception e)
//		{
//			throw new ConfigurationStoreException("Failed to add policy use to policy group.", e);
//		}
	}

	@Override
	@Transactional(readOnly = false)
	public void removePolicyUseFromGroup(long policyGroupReltnId) throws ConfigurationStoreException
	{
//        try
//        {
//            final Query select = sessionFactory.getCurrentSession().createQuery("DELETE from CertPolicyGroupReltn cpr WHERE cpr.id = ?");
//            select.setParameter(0, policyGroupReltnId);
//
//            select.executeUpdate();
//        }
//        catch (NoResultException e)
//        {
//        	throw new ConfigurationStoreException("Policy group reltn with id " + policyGroupReltnId + " does not exist");
//        }
//      	catch (Exception e)
//    	{
//    		throw new ConfigurationStoreException("Failed to remove policy use from policy group.", e);
//    	}
	}

	@Override
    @Transactional(readOnly = false)
	public void associatePolicyGroupToDomain(long domainId, long policyGroupId)
			throws ConfigurationStoreException
	{
//		// make sure the domain exists
//		final Domain domain = domainDao.getDomain(domainId);
//		if (domain == null)
//			throw new ConfigurationStoreException("Domain with id " + domainId + " does not exist");
//
//		// make sure the policy group exists
//		final CertPolicyGroup policyGroup = this.getPolicyGroupById(policyGroupId);
//		if (policyGroup == null)
//			throw new ConfigurationStoreException("Policy group with id " + policyGroup + " does not exist");
//
//		try
//		{
//			final CertPolicyGroupDomainReltn policyGroupDomainAssoc = new CertPolicyGroupDomainReltn();
//			policyGroupDomainAssoc.setDomain(domain);
//			policyGroupDomainAssoc.setCertPolicyGroup(policyGroup);
//
//			sessionFactory.getCurrentSession().persist(policyGroupDomainAssoc);
//		}
//		catch (Exception e)
//		{
//			throw new ConfigurationStoreException("Failed to associate policy group to domain.", e);
//		}
	}

	@Override
    @Transactional(readOnly = false)
	public void disassociatePolicyGroupFromDomain(long domainId, long policyGroupId) throws ConfigurationStoreException
	{
//		// make sure the domain exists
//		final Domain domain = domainDao.getDomain(domainId);
//		if (domain == null)
//			throw new ConfigurationStoreException("Domain with id " + domainId + " does not exist");
//
//		// make sure the policy group exists
//		final CertPolicyGroup policyGroup = this.getPolicyGroupById(policyGroupId);
//		if (policyGroup == null)
//			throw new ConfigurationStoreException("Policy group with id " + policyGroup + " does not exist");
//
//		try
//		{
//			final Query select = sessionFactory.getCurrentSession().createQuery("SELECT cpr from CertPolicyGroupDomainReltn cpr where cpr.domain  = ? " +
//	        		" and cpr.certPolicyGroup = ? ");
//
//	        select.setParameter(0, domain);
//	        select.setParameter(1, policyGroup);
//
//	        final CertPolicyGroupDomainReltn reltn = (CertPolicyGroupDomainReltn)select.uniqueResult();
//
//	        sessionFactory.getCurrentSession().delete(reltn);
//		}
//		catch (NoResultException e)
//		{
//			throw new ConfigurationStoreException("Association between domain id " + domainId + " and policy group id "
//					 + policyGroupId + " does not exist", e);
//		}
//		catch (Exception e)
//		{
//			throw new ConfigurationStoreException("Failed to delete policy group from domain relation.", e);
//		}
	}

	@Override
    @Transactional(readOnly = false)
	public void disassociatePolicyGroupsFromDomain(long domainId) throws ConfigurationStoreException
	{
//		// make sure the domain exists
//		final Domain domain = domainDao.getDomain(domainId);
//		if (domain == null)
//			throw new ConfigurationStoreException("Domain with id " + domainId + " does not exist");
//
//		try
//		{
//			final Query delete = sessionFactory.getCurrentSession().createQuery("DELETE from CertPolicyGroupDomainReltn cpr where cpr.domain  = ?");
//
//	        delete.setParameter(0, domain);
//	        delete.executeUpdate();
//		}
//		catch (Exception e)
//		{
//			throw new ConfigurationStoreException("Failed to dissaccociate group policies from domain id ." + domainId, e);
//		}
//
	}

	@Override
    @Transactional(readOnly = false)
	public void disassociatePolicyGroupFromDomains(long policyGroupId) throws ConfigurationStoreException
	{
//		// make sure the policy group exists
//		final CertPolicyGroup policyGroup = this.getPolicyGroupById(policyGroupId);
//		if (policyGroup == null)
//			throw new ConfigurationStoreException("Policy group with id " + policyGroupId + " does not exist");
//
//		try
//		{
//			final Query delete = sessionFactory.getCurrentSession().createQuery("DELETE from CertPolicyGroupDomainReltn cpr where cpr.certPolicyGroup  = ?");
//
//	        delete.setParameter(0, policyGroup);
//	        delete.executeUpdate();
//		}
//		catch (Exception e)
//		{
//			throw new ConfigurationStoreException("Failed to dissaccociate domains from policy group id ." + policyGroupId, e);
//		}
	}

	//@SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
	public Collection<CertPolicyGroupDomainReltn> getPolicyGroupDomainReltns() throws ConfigurationStoreException
	{
//        try
//        {
//	        final Query select = sessionFactory.getCurrentSession().createQuery("SELECT cpdr from CertPolicyGroupDomainReltn cpdr");
//
//	        final Collection<CertPolicyGroupDomainReltn> rs = select.list();
//	        if (rs.size() == 0)
//	        	return Collections.emptyList();
//
//	        for (CertPolicyGroupDomainReltn reltn : rs)
//	        {
//                if (!reltn.getCertPolicyGroup().getCertPolicyGroupReltn().isEmpty())
//                	for (CertPolicyGroupReltn groupReltn : reltn.getCertPolicyGroup().getCertPolicyGroupReltn())
//                		groupReltn.getCertPolicy().getPolicyData();
//	        }
//
//	        return rs;
//        }
//      	catch (Exception e)
//    	{
//    		throw new ConfigurationStoreException("Failed to execute certificate policy DAO query.", e);
//    	}

		return Collections.emptyList();
	}

	//@SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
	public Collection<CertPolicyGroupDomainReltn> getPolicyGroupsByDomain(long domainId) throws ConfigurationStoreException
	{
		// make sure the domain exists
//		final Domain domain = domainDao.getDomain(domainId);
//		if (domain == null)
//			throw new ConfigurationStoreException("Domain with id " + domainId + " does not exist");
//
//		Collection<CertPolicyGroupDomainReltn> retVal = null;
//        try
//        {
//	        final Query select = sessionFactory.getCurrentSession().createQuery("SELECT cpr from CertPolicyGroupDomainReltn cpr where cpr.domain = ?");
//	        select.setParameter(0, domain);
//
//	        retVal = (Collection<CertPolicyGroupDomainReltn>)select.list();
//	        if (retVal.size() == 0)
//	        	return Collections.emptyList();
//
//	        for (CertPolicyGroupDomainReltn reltn : retVal)
//	        {
//                if (!reltn.getCertPolicyGroup().getCertPolicyGroupReltn().isEmpty())
//                	for (CertPolicyGroupReltn groupReltn : reltn.getCertPolicyGroup().getCertPolicyGroupReltn())
//                		groupReltn.getCertPolicy().getPolicyData();
//	        }
//
//        }
//      	catch (Exception e)
//    	{
//    		throw new ConfigurationStoreException("Failed to execute policy group to domain relation DAO query.", e);
//    	}
//
//		return retVal;

		return Collections.emptyList();
	}
}
