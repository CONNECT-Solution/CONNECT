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
import gov.hhs.fha.nhinc.directconfig.dao.helpers.DaoUtils;
import gov.hhs.fha.nhinc.directconfig.entity.Address;
import gov.hhs.fha.nhinc.directconfig.entity.Domain;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import gov.hhs.fha.nhinc.directconfig.exception.ConfigurationStoreException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

/**
 * Implementing class for Address DAO methods.
 *
 * @author ppyette
 */
@Repository
public class AddressDaoImpl implements AddressDao {

    private static final Log log = LogFactory.getLog(AddressDaoImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public int count() {
        Session session = null;

        int count = 0;

        try {
            session = DaoUtils.getSession();

            count = ((Long) session.createQuery("SELECT count(*) FROM Address").uniqueResult()).intValue();

        } catch (Exception e) {
            log.error("Error in getting the count: ", e);
        } finally {
            DaoUtils.closeSession(session);
        }

        log.debug("Address Count: " + count);

        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Address address) {
        Session session = null;
        Transaction tx = null;

        if (address != null) {
            address.setId(null);
            address.setCreateTime(Calendar.getInstance());

            try {
                session = DaoUtils.getSession();

                log.debug("Saving address");

                tx = session.beginTransaction();
                session.save(address);
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
    public void update(Address address) {
        Session session = null;
        Transaction tx = null;

        Address entity;

        if (address != null) {
            try {
                session = DaoUtils.getSession();

                log.debug("Retrieving address: " + address.getEmailAddress());

                tx = session.beginTransaction();

                entity = (Address) session.get(Address.class, address.getId());

                entity.setDisplayName(address.getDisplayName());
                entity.setEndpoint(address.getEndpoint());
                entity.setEmailAddress(address.getEmailAddress());
                entity.setType(address.getType());
                entity.setStatus(address.getStatus());
                entity.setUpdateTime(Calendar.getInstance());

                log.debug("Merging address");

                session.merge(entity);
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
    public void save(Address item) {
        update(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String name) {
        if (name != null) {
            Session session = null;
            Transaction tx = null;
            Query query;

            int count = 0;

            try {
                session = DaoUtils.getSession();

                tx = session.beginTransaction();

                query = session.createQuery("DELETE FROM Address a WHERE UPPER(a.emailAddress) = :emailAddress");
                query.setParameter("emailAddress", name.toUpperCase(Locale.getDefault()));

                count = query.executeUpdate();
                tx.commit();

            } catch (Exception e) {
                DaoUtils.rollbackTransaction(tx, e);
                throw new ConfigurationStoreException(e);
            } finally {
                DaoUtils.closeSession(session);
            }

            log.debug("Deleted " + count + " Addresses");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Address> listAddresses(String name, int count) {
        // Unused by Direct RI
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Address get(String name) {
        Address result = null;

        Session session = null;
        Query query;

        if (name != null) {
            try {
                session = DaoUtils.getSession();

                query = session
                        .createQuery("SELECT DISTINCT a from Address a WHERE UPPER(a.emailAddress) = :emailAddress");

                result = (Address) query.setParameter("emailAddress", name.toUpperCase(Locale.getDefault()))
                        .uniqueResult();

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
    public List<Address> listAddresses(List<String> names, EntityStatus status) {
        List<Address> results = null;

        Session session = null;
        Query query;

        try {
            session = DaoUtils.getSession();

            if (CollectionUtils.isNotEmpty(names)) {
                query = session.getNamedQuery("getAddress");

                query.setParameterList("emailList", names);
            } else {
                query = session.getNamedQuery("getAddressByStatus");
            }

            query.setParameter("status", status == null ? status : status.ordinal());

            results = query.list();

            if (results == null) {
                results = new ArrayList<>();
            }

            log.debug("Addresses found: " + results.size());

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
    public List<Address> getByDomain(Domain domain, EntityStatus status) {
        List<Address> results = null;

        Session session = null;
        Query query;

        Long domainId = null;

        try {
            session = DaoUtils.getSession();

            query = session.getNamedQuery("getAddressByDomain");

            if (domain != null) {
                domainId = domain.getId();
            }

            query.setParameter("domainId", domainId);
            query.setParameter("status", status == null ? status : status.ordinal());

            results = query.list();

            if (results == null) {
                results = new ArrayList<>();
            }

            log.debug("Addresses found: " + results.size());
        } finally {
            DaoUtils.closeSession(session);
        }

        return results;
    }
}
