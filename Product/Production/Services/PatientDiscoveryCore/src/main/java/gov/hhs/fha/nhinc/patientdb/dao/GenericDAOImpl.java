/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientdb.dao;

import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtilFactory;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author PVenkatakrishnan
 *
 */
public class GenericDAOImpl<T> implements GenericDAO<T> {

    protected Class<T> entityClass;
    private static final Logger LOG = LoggerFactory.getLogger(AddressDAO.class);

    Session session = null;
    Transaction tx = null;

    @Override
    public boolean create(T t) {
        boolean result = true;
        try{
            session = getSessionFactory().openSession();
            tx = session.beginTransaction();
            LOG.info("Inserting Record...");
            session.persist(t);
            LOG.info("Address Inserted seccussfully...");
            tx.commit();
        } catch (HibernateException | NullPointerException e) {
            result = false;
            if (tx != null) {
                tx.rollback();
            }
            LOG.error("Exception during insertion caused by : {}", e.getMessage(), e);
        } finally {
            // Actual record insertion will happen at this step
            if (session != null) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session: {}", e.getMessage(), e);
                }

            }
        }
        LOG.debug("GenericDaoJpaImpl.create() - End");
        return result;
    }

    @Override
    public T read(Object id, Class<T> objectType) {
        List<T> queryList = null;
        T foundRecord = null;
        try {
            session = getSessionFactory().openSession();
            tx = session.beginTransaction();
            LOG.info("Reading Record...");
            Criteria aCriteria = session.createCriteria(objectType);
            aCriteria.add(Expression.eq("id", id));
            queryList = aCriteria.list();
            if (CollectionUtils.isNotEmpty(queryList)) {
                foundRecord = queryList.get(0);
            }
        } catch (HibernateException | NullPointerException e) {
            LOG.error("Exception during read occured due to : {}", e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                try {
                    session.flush();
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session after a read: {}", e.getMessage(), e);
                }
            }
        }
        LOG.debug("GenericDaoJpaImpl.read() - End");
        return foundRecord;
    }

    @Override
    public boolean update(T t) {
        boolean result = true;
        LOG.debug("GenericDaoJpaImpl.update() - Begin");

        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            LOG.info("Updating Record...");
            session.saveOrUpdate(t);
            LOG.info("Patient Updated seccussfully...");
            tx.commit();
        } catch (HibernateException | NullPointerException e) {
            result = false;
            if (tx != null) {
                tx.rollback();
            }
            LOG.error("Exception during update caused by : {}", e.getMessage(), e);
        } finally {
            // Actual Patient update will happen at this step
            if (session != null) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session after an update: {}", e.getMessage(), e);
                }
            }
        }

        LOG.debug("GenericDaoJpaImpl.update() - End");
        return result;
    }

    @Override
    public void delete(T t) {
        try {
            session = getSessionFactory().openSession();
            LOG.info("Deleting Record...");

            // Delete the Patient record
            session.delete(t);
        } catch (HibernateException | NullPointerException e) {
            LOG.error("Exception during delete occured due to : {}", e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                try {
                    session.flush();
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session after a delete: {}", e.getMessage(), e);
                }
            }
        }
        LOG.debug("GenericDaoJpaImpl.delete() - End");
    }

    protected SessionFactory getSessionFactory() {
        SessionFactory fact = null;
        HibernateUtil util = HibernateUtilFactory.getPatientDiscHibernateUtil();
        if (util != null) {
            fact = util.getSessionFactory();
        }
        return fact;
    }
}
