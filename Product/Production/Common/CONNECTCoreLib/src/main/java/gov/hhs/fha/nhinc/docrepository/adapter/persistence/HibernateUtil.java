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
package gov.hhs.fha.nhinc.docrepository.adapter.persistence;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import gov.hhs.fha.nhinc.properties.HibernateAccessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to obtain hibernate connections.
 *
 * @author Neil Webb
 */
public class HibernateUtil {

    private SessionFactory sessionFactory;
    private static final Logger LOG = LoggerFactory.getLogger(HibernateUtil.class);

    /**
     * Method builds the Hibernate SessionFactory.
     */
    public void buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            if (sessionFactory == null || sessionFactory.isClosed()) {
                sessionFactory = new Configuration().configure()
                    .buildSessionFactory(new StandardServiceRegistryBuilder().configure(getConfigFile()).build());
            }
        } catch (HibernateException he) {
            // Make sure you log the exception, as it might be swallowed
            LOG.error("Initial SessionFactory creation failed. {}", he);
            throw new ExceptionInInitializerError(he);
        }
    }

    /**
     * Method closes the Hibernate SessionFactory
     */
    public void closeSessionFactory() {
        try {
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        } catch (HibernateException he) {
            LOG.error("Error occurred during closing the sessionFactory: {}", he.getLocalizedMessage(), he);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    protected static File getConfigFile() {
        File result = null;

        try {
            result = HibernateAccessor.getInstance().getHibernateFile(NhincConstants.HIBERNATE_DOCUMENT_REPOSITORY);
        } catch (PropertyAccessException ex) {
            LOG.error("Unable to load {} {}", NhincConstants.HIBERNATE_DOCUMENT_REPOSITORY, ex.getMessage(), ex);
        }

        return result;

    }

    public static void closeSession(Session session) {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (HibernateException ex) {
                LOG.error("There an error while closing the hibernate-session-close: {} ", ex.getMessage(), ex);
            }
        }
    }

    public static Session getSession() {
        return HibernateUtilFactory.getDocRepoHibernateUtil().getSessionFactory().openSession();
    }

    public static <T> boolean save(T entity) {
        boolean result = false;
        Session session = null;
        Transaction tx = null;
        try {
            session = getSession();// .openSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(entity);
            tx.commit();
            result = true;
        } catch (HibernateException | NullPointerException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.error("Exception during save caused by : {}", e.getMessage(), e);
        } finally {
            closeSession(session);
        }
        return result;
    }

    public static <T> boolean delete(T entity) {
        boolean result = false;
        Session session = null;
        Transaction tx = null;
        try {
            session = getSession();// .openSession();
            tx = session.beginTransaction();
            session.delete(entity);
            tx.commit();
            result = true;
        } catch (HibernateException | NullPointerException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.error("Exception during delete caused by : {}", e.getMessage(), e);
        } finally {
            closeSession(session);
        }
        return result;
    }

    public static <T> T readBy(Class<T> clazz, Long id) {
        T entity = null;
        Session sess = null;
        try {
            sess = getSession();// .openSession();
            entity = sess.get(clazz, id);
        } catch (HibernateException | NullPointerException e) {
            LOG.error("Exception during read caused by : {}", e.getMessage(), e);
        } finally {
            HibernateUtil.closeSession(sess);
        }
        return entity;
    }

    public static <T> List<T> findAll(Class<T> clazz) {
        return findAllBy(clazz, null);
    }

    public static <T> List<T> findAllBy(Class<T> clazz, Criterion criterion) {
        List<T> queryList = new ArrayList<>();
        Session session = null;
        try {
            session = getSession();// .openSession();
            Criteria aCriteria = session.createCriteria(clazz);
            if (criterion != null) {
                aCriteria.add(criterion);
            }
            queryList = aCriteria.list();
        } catch (HibernateException | NullPointerException e) {
            LOG.error("Exception during findAllBy occured due to : {}", e.getMessage(), e);
        } finally {
            closeSession(session);
        }
        return queryList;
    }
}
