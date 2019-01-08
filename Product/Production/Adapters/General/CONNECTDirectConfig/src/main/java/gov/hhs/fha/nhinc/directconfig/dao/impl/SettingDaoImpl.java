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
package gov.hhs.fha.nhinc.directconfig.dao.impl;

import gov.hhs.fha.nhinc.directconfig.dao.SettingDao;
import gov.hhs.fha.nhinc.directconfig.dao.helpers.DaoUtils;
import gov.hhs.fha.nhinc.directconfig.entity.Setting;
import gov.hhs.fha.nhinc.directconfig.exception.ConfigurationStoreException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

/**
 * Implementing class for Setting DAO methods.
 *
 * @author Greg Meyer
 */
@Repository
public class SettingDaoImpl implements SettingDao {

    private static final Log log = LogFactory.getLog(SettingDaoImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(String name, String value) {
        if (name != null && !name.isEmpty() && value != null) {
            Session session = null;
            Transaction tx = null;

            // make sure this setting doesn't already exist
            if (CollectionUtils.isNotEmpty(getByNames(Arrays.asList(name)))) {
                throw new ConfigurationStoreException("Setting " + name + " already exists.");
            }

            Setting setting = new Setting();

            setting.setId(null);
            setting.setCreateTime(Calendar.getInstance());

            setting.setName(name);
            setting.setValue(value);

            try {
                session = DaoUtils.getSession();

                tx = session.beginTransaction();
                session.persist(setting);
                tx.commit();

                log.debug("Setting added successfully");
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
    public void delete(Collection<String> names) {
        if (CollectionUtils.isNotEmpty(names)) {
            Session session = null;
            Transaction tx = null;
            Query query;

            int count;

            try {
                session = DaoUtils.getSession();

                tx = session.beginTransaction();

                query = session.createQuery("DELETE FROM Setting s WHERE UPPER(s.name) IN (:names)");
                query.setParameterList("names", names);

                count = query.executeUpdate();
                tx.commit();

                log.debug("Exit: " + count + " setting records deleted");

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
    @SuppressWarnings("unchecked")
    public Collection<Setting> getAll() {
        List<Setting> results = null;

        Session session = null;

        try {
            session = DaoUtils.getSession();

            results = session.getNamedQuery("getAllSettings").list();

            if (results == null) {
                results = Collections.emptyList();
            }

            log.debug("Settings found: " + results.size());
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
    public Collection<Setting> getByNames(Collection<String> names) {
        Collection<Setting> results = null;

        if (CollectionUtils.isEmpty(names)) {
            results = getAll();
        } else {
            Session session = null;
            Query query;

            try {
                session = DaoUtils.getSession();

                query = session.getNamedQuery("getSettings");
                query.setParameterList("nameList", names);

                results = query.list();

                if (results == null) {
                    results = Collections.emptyList();
                }

                log.debug("Settings found: " + results.size());
            } finally {
                DaoUtils.closeSession(session);
            }
        }

        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(String name, String value) {
        if (name != null && !name.isEmpty()) {
            Collection<Setting> settings = getByNames(Arrays.asList(name));

            Session session = null;
            Transaction tx = null;

            try {
                session = DaoUtils.getSession();

                tx = session.beginTransaction();

                for (Setting setting : settings) {
                    setting.setValue(value);
                    setting.setUpdateTime(Calendar.getInstance());
                    session.merge(setting);
                }

                tx.commit();

            } catch (Exception e) {
                DaoUtils.rollbackTransaction(tx, e);
                throw new ConfigurationStoreException(e);
            } finally {
                DaoUtils.closeSession(session);
            }
        }
    }
}
