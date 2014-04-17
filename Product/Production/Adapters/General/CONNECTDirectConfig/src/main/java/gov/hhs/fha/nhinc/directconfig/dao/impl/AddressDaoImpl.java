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
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.Query;

import gov.hhs.fha.nhinc.directconfig.entity.Address;
import gov.hhs.fha.nhinc.directconfig.entity.Domain;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import gov.hhs.fha.nhinc.directconfig.dao.AddressDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementing class for Address DAO methods.
 *
 * @author ppyette
 */
//@Repository
@Service
public class AddressDaoImpl implements AddressDao {

    @Autowired
    protected SessionFactory sessionFactory;

    private static final Log log = LogFactory.getLog(AddressDaoImpl.class);

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AddressDao#count()
     */
    @Transactional(readOnly = true)
    public int count() {
        log.debug("Enter");
        Long result = (Long) sessionFactory.getCurrentSession().createQuery("select count(d) from Address a").uniqueResult();

        log.debug("Exit: " + result.intValue());
        return result.intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AddressDao#add(gov.hhs.fha.nhinc.directconfig.entity.Address)
     */
    @Transactional(readOnly = false)
    public void add(Address item) {
        log.debug("Enter");

        if (item != null) {
            item.setCreateTime(Calendar.getInstance());
            item.setUpdateTime(item.getCreateTime());
            sessionFactory.getCurrentSession().persist(item);
        }

        log.debug("Exit");
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AddressDao#update(gov.hhs.fha.nhinc.directconfig.entity.Address)
     */
    @Transactional(readOnly = false)
    public void update(Address item) {
        log.debug("Enter");

        if (item != null) {
            Address inDb = (Address)sessionFactory.getCurrentSession().get(Address.class, item.getId());
            
            inDb.setDisplayName(item.getDisplayName());
            inDb.setEndpoint(item.getEndpoint());
            inDb.setEmailAddress(item.getEmailAddress());
            inDb.setType(item.getType());
            inDb.setStatus(item.getStatus());
            inDb.setUpdateTime(Calendar.getInstance());
            
            sessionFactory.getCurrentSession().merge(inDb);
        }

        log.debug("Exit");
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AddressDao#save(gov.hhs.fha.nhinc.directconfig.entity.Address)
     */
    @Transactional(readOnly = false)
    public void save(Address item) {
        update(item);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AddressDao#delete(java.lang.String)
     */
    @Transactional(readOnly = false)
    public void delete(String name) {
        log.debug("Enter");

        int count = 0;
        if (name != null) {
            Query delete = sessionFactory.getCurrentSession().createQuery("DELETE FROM Address a WHERE UPPER(a.emailAddress) = ?1");
            delete.setParameter(1, name.toUpperCase(Locale.getDefault()));
            count = delete.executeUpdate();
        }

        log.debug("Exit: " + count + " records deleted");
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AddressDao#listAddresses(java.lang.String, int)
     */
    @Transactional(readOnly = true)
    public List<Address> listAddresses(String name, int count) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AddressDao#get(java.lang.String)
     */
    @Transactional(readOnly = true)
    public Address get(String name) {
        log.debug("Enter");

        Address result = null;

        if (name != null) {
            Query select = sessionFactory.getCurrentSession().createQuery("SELECT DISTINCT a from Address a d WHERE UPPER(a.emailAddress) = ?1");
            result = (Address) select.setParameter(1, name.toUpperCase(Locale.getDefault())).uniqueResult();
        }

        log.debug("Exit");
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AddressDao#listAddresses(java.util.List, gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Address> listAddresses(List<String> names, EntityStatus status) {
        log.debug("Enter");

        List<Address> result = null;
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
            String query = "SELECT a from Address a WHERE UPPER(a.emailAddress) IN " + nameList.toString();

            if (status != null) {
                select = sessionFactory.getCurrentSession().createQuery(query + " AND a.status = ?1");
                select.setParameter(1, status);
            } else {
                select = sessionFactory.getCurrentSession().createQuery(query);
            }
        } else {
            if (status != null) {
                select = sessionFactory.getCurrentSession().createQuery("SELECT a from Address a WHERE a.status = ?1");
                select.setParameter(1, status);
            } else {
                select = sessionFactory.getCurrentSession().createQuery("SELECT a from Address a");
            }
        }

        @SuppressWarnings("rawtypes")
        List rs = select.list();
        if ((rs.size() != 0) && (rs.get(0) instanceof Address)) {
            result = (List<Address>) rs;
        } else {
            result = new ArrayList<Address>();
        }

        log.debug("Exit");
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AddressDao#getByDomain(gov.hhs.fha.nhinc.directconfig.entity.Domain, gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Address> getByDomain(Domain domain, EntityStatus status) {
        log.debug("Enter");

        List<Address> result = null;
        Query select = null;
        
        if (domain != null) {
            String query = "SELECT a from Address a WHERE a.domain = ?1";

            if (status != null) {
                select = sessionFactory.getCurrentSession().createQuery(query + " AND a.status = ?2");
                select.setParameter(1, domain);
                select.setParameter(2, status);
            } else {
                select = sessionFactory.getCurrentSession().createQuery(query);
                select.setParameter(1, domain);
            }
        } else {
            if (status != null) {
                select = sessionFactory.getCurrentSession().createQuery("SELECT a from Address a WHERE a.status = ?1");
                select.setParameter(1, status);
            } else {
                select = sessionFactory.getCurrentSession().createQuery("SELECT a from Address a");
            }
        }

        @SuppressWarnings("rawtypes")
        List rs = select.list();
        if ((rs.size() != 0) && (rs.get(0) instanceof Address)) {
            result = (List<Address>) rs;
        } else {
            result = new ArrayList<Address>();
        }

        log.debug("Exit");
        return result;
    }
}
