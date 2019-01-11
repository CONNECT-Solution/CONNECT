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

package gov.hhs.fha.nhinc.directconfig.dao.impl;

import gov.hhs.fha.nhinc.directconfig.dao.AddressDao;
import gov.hhs.fha.nhinc.directconfig.dao.DomainDao;
import gov.hhs.fha.nhinc.directconfig.dao.helpers.DaoUtils;
import gov.hhs.fha.nhinc.directconfig.entity.Address;
import gov.hhs.fha.nhinc.directconfig.entity.Domain;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import gov.hhs.fha.nhinc.directconfig.exception.ConfigurationStoreException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * Default Spring/JPA implemenation
 * In case the parameter passes as null, do not convert into empty String to allow AutomatedDirectTest
 * SoapUI test to finish
 * @author ppyette
 */
@Repository
public class DomainDaoImpl implements DomainDao {

    @Autowired
    protected AddressDao addressDao;

    private static final Log log = LogFactory.getLog(DomainDaoImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public int count() {
        Session session = null;

        int count = 0;

        try {
            session = DaoUtils.getSession();

            count = ((Long) session.createQuery("SELECT count(*) FROM Domain").uniqueResult()).intValue();

        } finally {
            DaoUtils.closeSession(session);
        }

        log.debug("Domain Count: " + count);

        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Domain item) {
        if (item != null) {
            if (item.getDomainName() == null || item.getDomainName().isEmpty()) {
                throw new ConfigurationStoreException("Domain name cannot be empty or null");
            }

            // Direct RI Comment: Save and clear Address information until the Domain is saved.
            // This is really something that JPA should be doing, but doesn't seem to work.
            Collection<Address> addresses = item.getAddresses();

            item.setAddresses(null);

            item.setId(null);
            item.setCreateTime(Calendar.getInstance());
            item.setUpdateTime(item.getCreateTime());

            Session session = null;
            Transaction tx = null;

            try {
                session = DaoUtils.getSession();

                tx = session.beginTransaction();
                session.persist(item);
                tx.commit();

                saveAddresses(addresses);

            } catch (Exception e) {
                DaoUtils.rollbackTransaction(tx, e);
                throw new ConfigurationStoreException(e);
            } finally {
                DaoUtils.closeSession(session);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Domain item) {
        if (item != null) {
            item.setUpdateTime(Calendar.getInstance());

            Session session = null;
            Transaction tx = null;

            try {
                saveAddresses(item.getAddresses());

                session = DaoUtils.getSession();

                tx = session.beginTransaction();
                session.merge(item);
                tx.commit();

            } catch (Exception e) {
                DaoUtils.rollbackTransaction(tx, e);
                throw new ConfigurationStoreException(e);
            } finally {
                DaoUtils.closeSession(session);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Domain item) {
        update(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String name) {
        final Domain domain = getDomainByName(name);

        if (domain != null) {
            Session session = null;
            Transaction tx = null;

            try {
                session = DaoUtils.getSession();

                tx = session.beginTransaction();
                session.delete(domain);
                tx.commit();

            } catch (Exception e) {
                DaoUtils.rollbackTransaction(tx, e);
                throw new ConfigurationStoreException(e);
            } finally {
                DaoUtils.closeSession(session);
            }
        } else {
            log.warn("Unable to delete: No matching domain found.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long anId) {
        final Domain domain = getDomain(anId);

        if (domain != null) {
            Session session = null;
            Transaction tx = null;

            try {
                session = DaoUtils.getSession();

                tx = session.beginTransaction();
                session.delete(domain);
                tx.commit();

            } catch (Exception e) {
                DaoUtils.rollbackTransaction(tx, e);
                throw new ConfigurationStoreException(e);
            } finally {
                DaoUtils.closeSession(session);
            }
        } else {
            log.warn("No domain matching the id: " + anId + " found.  Unable to delete.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Domain getDomainByName(String name) {
        Domain result = null;

        if (name != null) {
            Session session = null;
            Query query;

            try {
                session = DaoUtils.getSession();

                query = session.createQuery("SELECT DISTINCT d FROM Domain d WHERE UPPER(d.domainName) = :domainName");

                query.setParameter("domainName", name.toUpperCase(Locale.getDefault()));

                result = (Domain) query.uniqueResult();

            } finally {
                DaoUtils.closeSession(session);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Domain> getDomains(List<String> names, EntityStatus status) {
        List<Domain> results = null;

        Session session = null;
        Query query;

        try {
            session = DaoUtils.getSession();

            if (CollectionUtils.isNotEmpty(names)) {
                query = session.getNamedQuery("getDomains");

                query.setParameterList("nameList", names);
            } else {
                query = session.getNamedQuery("getDomainsByStatus");
            }

            query.setParameter("status", status == null ? status : status.ordinal());

            results = query.list();
        } finally {
            DaoUtils.closeSession(session);
        }

        if (results == null) {
            results = new ArrayList<>();
        }

        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Domain> listDomains(String name, int count) {
        // Direct RI Comment: Use by Direct third party component
        //(org.nhindirect.gateway.smtp.config.WSSmtpAgentConfig), it will pass null,maxcount
        List<Domain> results = null;
        Query query;

        Session session = null;

        try {
            session = DaoUtils.getSession();
            log.debug("Calling listDomain");
            query = session.getNamedQuery("getDomainsByName");
            query.setParameter("domainName", StringUtils.hasText(name) ? name.toUpperCase(Locale.getDefault()): null);

            // Direct RI Comment:
            // assuming that a count of zero really means no limit
            if (count > 0) {
                query.setMaxResults(count);
            }

            results = query.list();

            if (results.isEmpty()) {
                results = null;
            }
        } finally {
            DaoUtils.closeSession(session);
        }

        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Domain> searchDomain(String name, EntityStatus status) {
        List<Domain> result = null;

        Session session = null;
        Query query;

        String fuzzyName = null;

        try {
            session = DaoUtils.getSession();

            query = session.getNamedQuery("searchDomains");

            if (name != null) {
                fuzzyName = name.toUpperCase(Locale.getDefault()).replace('*', '%').replace('?', '_');
            }

            query.setParameter("domainName", fuzzyName);
            query.setParameter("status", status == null ? status : status.ordinal());

            result = query.list();

            if (result == null) {
                result = new ArrayList<>();
            }
        } finally {
            DaoUtils.closeSession(session);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Domain getDomain(Long id) {
        Domain result = null;

        if (id != null) {
            Session session = null;

            try {
                session = DaoUtils.getSession();

                result = (Domain) session.get(Domain.class, id);

            } finally {
                DaoUtils.closeSession(session);
            }
        }

        return result;
    }

    protected void disassociateTrustBundlesFromDomain(Long domainId) throws ConfigurationStoreException {
        final TrustBundleDaoImpl dao = new TrustBundleDaoImpl();

        dao.disassociateTrustBundlesFromDomain(domainId);
    }

    private void saveAddresses(Collection<Address> addresses) {
        for (Address address : addresses) {
            if (address.getId() == null) {
                addressDao.add(address);
            }
        }
    }
}
