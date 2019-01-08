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
package gov.hhs.fha.nhinc.directconfig.dao.helpers;

import gov.hhs.fha.nhinc.directconfig.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hibernate utility class for DirectConfig classes
 *
 */
public class DaoUtils {

    private static final Log LOG = LogFactory.getLog(DaoUtils.class);
    private static HibernateUtil hibernateUtil;

    private DaoUtils() {
    }

    /**
     * Singleton class that holds the ClassPathXmlApplicationContext
     *
     * @author drfernan
     *
     */
    private static class ClassPathSingleton {

        public static final ClassPathXmlApplicationContext CONTEXT = new ClassPathXmlApplicationContext(
            new String[] { "classpath:CONNECT-context.xml" });

        private ClassPathSingleton() {
        }
    }

    /**
     * Method that returns the Transaction HibernateUtil
     *
     * @return
     */
    public static HibernateUtil getHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address transactionHibernateUtil " + context.getId());
        try {
            if (hibernateUtil == null) {
                hibernateUtil = context.getBean(NhincConstants.DIRECT_CONFIG_HIBERNATE_BEAN, HibernateUtil.class);
            }
        } catch (BeansException e) {
            LOG.error("Error retrieving the directConfig.persistence.HibernateUtil bean: ", e);
        }
        return hibernateUtil;
    }

    /**
     * Opens and returns a Session, using the Hibernate SessionFactory.
     *
     * @return the opened Session.
     */
    public static Session getSession() {
        Session session = null;
        HibernateUtil hibernateUtil = getHibernateUtil();
        if (hibernateUtil != null) {
            SessionFactory fact = hibernateUtil.getSessionFactory();
            if (fact != null) {
                session = fact.openSession();
            } else {
                LOG.error("No Session available - SessionFactory is null.");
            }
        }
        return session;
    }

    /**
     * Attempt (safely) to close the Session.
     *
     * @param session
     */
    public static void closeSession(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (HibernateException e) {
                LOG.error("Failed to close session: ", e);
            }
        }
    }

    /**
     * Attempt (safely) to rollback the Transaction.
     *
     * @param tx
     * @param ex
     */
    public static void rollbackTransaction(Transaction tx, Exception ex) {
        if (tx != null) {
            try {
                LOG.error("Failed to commit transaction, attempting rollback...", ex);
                tx.rollback();
            } catch (HibernateException e) {
                LOG.error("Failed to rollback transaction: ", e);
            }
        } else {
            LOG.warn("Cannot roll back, transaction is null");
        }
    }
}
