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
package gov.hhs.fha.nhinc.admingui.hibernate.dao;

import gov.hhs.fha.nhinc.admingui.hibernate.util.HibernateUtil;
import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.services.exception.UserLoginException;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.RolePreference;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserRole;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author msw
 *
 */
@Service
public class UserLoginDAOImpl implements UserLoginDAO {

    @Autowired
    HibernateUtil hibernateUtil;
    private static final Logger LOG = LoggerFactory.getLogger(UserLoginDAOImpl.class);

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.hibernate.dao.UserLoginDAO#login(gov.hhs.fha .nhinc.admingui.model.Login)
     */
    @Override
    public UserLogin login(Login login) {
        Session session = null;
        UserLogin userLogin = null;
        Query query;

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            query = session.createQuery("from UserLogin where userName = :userName");
            query.setParameter("userName", login.getUserName());
            userLogin = (UserLogin) query.uniqueResult();
        } catch (HibernateException e) {
            LOG.error("Exception during query execution by: {}", e.getMessage(), e);
        } finally {
            closeSession(session, false);
        }

        return userLogin;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.hibernate.dao.UserLoginDAO#createUser(gov.hhs.fha
     * .nhinc.admingui.services.persistence.jpa.entity.UserLogin)
     */
    @Override
    public boolean createUser(UserLogin createUser) throws UserLoginException {
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.persist(createUser);
            LOG.info("create user record Inserted successfully from dao impl...");
            tx.commit();
        } catch (HibernateException e) {
            result = false;
            transactionRollback(tx);
            LOG.error("Exception during insertion caused by :{}", e.getMessage(), e);
            throw new UserLoginException("Could not create new user " + createUser.getUserName(), e);
        } finally {
            closeSession(session, false);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.hibernate.dao.UserLoginDAO#getRole(long)
     */
    @Override
    public UserRole getRole(long role) {
        Session session = null;
        UserRole result = null;

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            result = (UserRole) session.createCriteria(UserRole.class).add(Restrictions.eq("roleId", role))
                .uniqueResult();
        } catch (HibernateException e) {
            LOG.error("Could not get role: {}", e.getLocalizedMessage(), e);
        } finally {
            closeSession(session, false);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.hibernate.dao.UserLoginDAO#getAllRoles()
     */
    @Override
    public List<UserRole> getAllRoles() {
        Session session = null;

        List<UserRole> roles = null;

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            roles = session.createCriteria(UserRole.class).list();
        } catch (HibernateException e) {
            LOG.error("Could not get roles: {}", e.getLocalizedMessage(), e);
        } finally {
            closeSession(session, false);
        }

        return roles;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.hibernate.dao.UserLoginDAO#updatePreferences(gov.hhs.fha
     * .nhinc.admingui.services.persistence.jpa.entity.RolePreference)
     */
    @Override
    public boolean updatePreference(RolePreference preference) {
        Session session = null;
        Transaction tx = null;
        boolean updated = false;

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.update(preference);
            tx.commit();
            updated = true;
        } catch (HibernateException e) {
            LOG.error("Could not update preferences: {}", e.getLocalizedMessage(), e);
            transactionRollback(tx);
            updated = false;
        } finally {
            closeSession(session, false);
        }

        return updated;
    }

    @Override
    public void deleteUser(UserLogin user) throws UserLoginException {
        Session session = null;
        Transaction tx = null;

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.delete(user);
            tx.commit();
        } catch (HibernateException e) {
            transactionRollback(tx);
            LOG.error("Unable to delete user: {}", e.getLocalizedMessage(), e);
            throw new UserLoginException("Unable to delete user: " + e.getLocalizedMessage());
        } finally {
            closeSession(session, true);
        }
    }

    @Override
    public List<UserLogin> getAllUsers() {
        Session session = null;

        List<UserLogin> users = null;

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            users = session.createCriteria(UserLogin.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        } catch (HibernateException e) {
            LOG.error("Could not retrieve users: {}", e.getLocalizedMessage(), e);
        } finally {
            closeSession(session, false);
        }

        return users;
    }

    private void transactionRollback(Transaction tx) {
        if (tx != null) {
            tx.rollback();
        }
    }

    private void closeSession(Session session, boolean flush) {
        if (session != null) {
            if (flush) {
                session.flush();
            }
            session.close();
        }
    }
}
