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

package gov.hhs.fha.nhinc.directconfig.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import gov.hhs.fha.nhinc.directconfig.entity.Address;
import gov.hhs.fha.nhinc.directconfig.entity.Domain;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import gov.hhs.fha.nhinc.directconfig.exception.ConfigurationStoreException;
import gov.hhs.fha.nhinc.directconfig.dao.AddressDao;
import gov.hhs.fha.nhinc.directconfig.dao.DomainDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default Spring/JPA implemenation
 *
 * @author ppyette
 */
@Service
public class DomainDaoImpl implements DomainDao {
    @Autowired
    protected SessionFactory sessionFactory;

    @Autowired
    protected AddressDao addressDao;

    private static final Log log = LogFactory.getLog(DomainDaoImpl.class);

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.DomainDao#count()
     */
    @Transactional(readOnly = true)
    public int count() {
        log.debug("Enter");
        Long result = (Long) sessionFactory.getCurrentSession().createQuery("select count(d) from Domain d").uniqueResult();

        log.debug("Exit: " + result.intValue());
        return result.intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.DomainDao#add(gov.hhs.fha.nhinc.directconfig.entity.Domain)
     */
    @Transactional(readOnly = false)
    public void add(Domain item) {
        log.debug("Enter");

        if (item.getDomainName() == null || item.getDomainName().isEmpty()) {
            throw new ConfigurationStoreException("Domain name cannot be empty or null");
        }
        
        // Save and clear Address information until the Domain is saved.
        // This is really something that JPA should be doing, but doesn't seem
        // to work.
        if (item != null) {
            Collection<Address> addresses = item.getAddresses();

            item.setAddresses(null);

            item.setCreateTime(Calendar.getInstance());
            item.setUpdateTime(item.getCreateTime());

            log.debug("Calling JPA to persist the Domain");

            sessionFactory.getCurrentSession().persist(item);
            sessionFactory.getCurrentSession().flush();

            log.debug("Persisted the bare Domain");

            boolean needUpdate = false;

            if ((addresses != null) && (addresses.size() > 0)) {
                item.setAddresses(addresses);
                needUpdate = true;
            }

            if (needUpdate) {
                log.debug("Updating the domain with Address info");
                update(item);
            }

            log.debug("Returned from JPA: Domain ID=" + item.getId());
        }

        log.debug("Exit");
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.DomainDao#update(gov.hhs.fha.nhinc.directconfig.entity.Domain)
     */
    @Transactional(readOnly = false)
    public void update(Domain item) {
        log.debug("Enter");

        if (item != null) {
            item.setUpdateTime(Calendar.getInstance());

            for (Address address : item.getAddresses()) {
                if ((address.getId() == null) || (address.getId().longValue() == 0)) {
                    log.debug("Adding " + address.toString() + " to database");
                    addressDao.add(address);
                }
            }

            log.debug("Calling JPA to perform update...");
            sessionFactory.getCurrentSession().merge(item);
        }

        log.debug("Exit");
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.DomainDao#save(gov.hhs.fha.nhinc.directconfig.entity.Domain)
     */
    @Transactional(readOnly = false)
    public void save(Domain item) {
        update(item);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.DomainDao#delete(java.lang.String)
     */
    @Transactional(readOnly = false)
    public void delete(String name) {
        log.debug("Enter");

        // delete addresses first if they exist
        final Domain domain = getDomainByName(name);

        if (domain != null) {
            disassociateTrustBundlesFromDomain(domain.getId());

            sessionFactory.getCurrentSession().delete(domain);
        } else  {
            log.warn("No domain matching the name: " + name + " found.  Unable to delete.");
        }

        log.debug("Exit");
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.DomainDao#delete(java.lang.String)
     */
    @Transactional(readOnly = false)
    public void delete(Long anId) {
        log.debug("Enter");

        final Domain domain = getDomain(anId);

        if (domain != null) {
            disassociateTrustBundlesFromDomain(domain.getId());

            sessionFactory.getCurrentSession().delete(domain);
        } else  {
           log.warn("No domain matching the id: " + anId + " found.  Unable to delete.");
        }

        log.debug("Exit");
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.DomainDao#getDomainByName(java.lang.String)
     */
    @Transactional(readOnly = true)
    public Domain getDomainByName(String name) {
        log.debug("Enter");

        Domain result = null;

        if (name != null) {
            Query select = sessionFactory.getCurrentSession().createQuery("SELECT DISTINCT d from Domain d WHERE UPPER(d.domainName) = ?");
            Query paramQuery = select.setParameter(0, name.toUpperCase(Locale.getDefault()));

            if (paramQuery.list().size() > 0) {
                result = (Domain) paramQuery.uniqueResult();
            }
        }

        log.debug("Exit");
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.DomainDao#getDomains(java.lang.String, gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
     *
     * Convert the list of names into a String to be used in an IN clause (i.e.
     * {"One", "Two", "Three"} --> ('One', 'Two', 'Three'))
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Domain> getDomains(List<String> names, EntityStatus status) {
        log.debug("Enter");

        List<Domain> result = null;
        Query select = null;
        
        if (names != null) {
            StringBuffer nameList = new StringBuffer("(");
            
            for (String aName : names) {
                if (nameList.length() > 1) {
                    nameList.append(", ");
                }
                
                nameList.append("'").append(aName.toUpperCase(Locale.getDefault())).append("'");
            }
            
            nameList.append(")");
            String query = "SELECT d from Domain d WHERE UPPER(d.domainName) IN " + nameList.toString();

            if (status != null) {
                select = sessionFactory.getCurrentSession().createQuery(query + " AND d.status = ?");
                select.setParameter(0, status);
            } else {
                select = sessionFactory.getCurrentSession().createQuery(query);
            }
        } else {
            if (status != null) {
                select = sessionFactory.getCurrentSession().createQuery("SELECT d from Domain d WHERE d.status = ?");
                select.setParameter(0, status);
            } else {
                select = sessionFactory.getCurrentSession().createQuery("SELECT d from Domain d");
            }

        }

        @SuppressWarnings("rawtypes")
        List rs = select.list();
        if ((rs.size() != 0) && (rs.get(0) instanceof Domain)) {
            result = (List<Domain>) rs;
        } else {
            result = new ArrayList<Domain>();
        }

        log.debug("Exit");
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.DomainDao#listDomains(java.lang.String, int)
     */
    // TODO I'm not sure if this is doing the right thing. I suspect that the
    // real intent is to do some kind of db paging
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Domain> listDomains(String name, int count) {
        log.debug("Enter");

        List<Domain> result = null;
        Query select = null;
        
        if (name != null) {
            select = sessionFactory.getCurrentSession().createQuery("SELECT d from Domain d WHERE UPPER(d.domainName) = ?");
            select.setParameter(0, name.toUpperCase(Locale.getDefault()));
        } else {
            select = sessionFactory.getCurrentSession().createQuery("SELECT d from Domain d");
        }

        // assuming that a count of zero really means no limit
        if (count > 0) {
            select.setMaxResults(count);
        }

        @SuppressWarnings("rawtypes")
        List rs = select.list();
        if ((rs.size() != 0) && (rs.get(0) instanceof Domain)) {
            result = (List<Domain>) rs;
        }

        log.debug("Exit");
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.DomainDao#searchDomain(java.lang.String, gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Domain> searchDomain(String name, EntityStatus status) {

        log.debug("Enter");

        List<Domain> result = null;
        StringBuffer query = new StringBuffer("");
        Query select = null;
        if (name != null) {
            String search = name.replace('*', '%').toUpperCase(Locale.getDefault());
            search.replace('?', '_');
            query.append("SELECT d from Domain d WHERE UPPER(d.domainName) LIKE ? ");
            if (status != null) {
                query.append("AND d.status = ?");
                select = sessionFactory.getCurrentSession().createQuery(query.toString());
                select.setParameter(0, search);
                select.setParameter(1, status);
            } else {
                select = sessionFactory.getCurrentSession().createQuery(query.toString());
                select.setParameter(0, search);
            }
        } else {
            if (status != null) {
                query.append("SELECT d from Domain d WHERE d.status LIKE ?");
                select = sessionFactory.getCurrentSession().createQuery(query.toString());
                select.setParameter(0, status);
            } else {
                select = sessionFactory.getCurrentSession().createQuery("SELECT d from Domain d");
            }

        }

        result = (List<Domain>) select.list();
        if (result == null) {
            result = new ArrayList<Domain>();
        }

        log.debug("Exit");
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.DomainDao#getDomain(java.lang.Long)
     */
    @Transactional(readOnly = true)
    public Domain getDomain(Long id) {
        log.debug("Enter");

        Domain result = null;
        if ((id != null) && (id.longValue() > 0)) {
            result = (Domain)sessionFactory.getCurrentSession().get(Domain.class, id);
        }

        log.debug("Exit");
        return result;
    }

    /**
     * Set the value of addressDao.
     * @param aDao The value of addressDao.
     */
    public void setAddressDao(AddressDao aDao) {
        addressDao = aDao;
    }

    protected void disassociateTrustBundlesFromDomain(long domainId) throws ConfigurationStoreException {
        final TrustBundleDaoImpl dao = new TrustBundleDaoImpl();
        
        dao.disassociateTrustBundlesFromDomain(domainId);
    }
}
