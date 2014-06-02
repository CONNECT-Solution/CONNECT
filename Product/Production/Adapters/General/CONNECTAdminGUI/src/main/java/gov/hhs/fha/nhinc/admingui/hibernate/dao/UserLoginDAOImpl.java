/*
 *  Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above
 *        copyright notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of the United States Government nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.admingui.hibernate.dao;

import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.RolePreference;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserRole;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author msw
 *
 */
@Service
public class UserLoginDAOImpl implements UserLoginDAO {

    private static final Logger LOG = Logger.getLogger(UserLoginDAOImpl.class);
    
    @Autowired
    private SessionFactory sessionFactory;

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.admingui.hibernate.dao.UserLoginDAO#login(gov.hhs.fha.nhinc.admingui.model.Login)
     */
    @Override
    public UserLogin login(Login login) {
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery("from UserLogin where userName = :userName");
        query.setParameter("userName", login.getUserName());
        return (UserLogin) query.list().get(0);
    }

    /**
     *
     * @param createUser the create user
     * @return true if successful
     */
    @Override
    public boolean createUser(UserLogin createUser) {

        Session session = null;
        Transaction tx = null;
        boolean result = true;
        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();
            session.persist(createUser);
            LOG.info("create user record Inserted successfully from dao impl...");
            tx.commit();

        } catch (HibernateException e) {
            result = false;
            transactionRollback(tx);
            LOG.error("Exception during insertion caused by :" + e.getMessage(), e);
        } finally {
            closeSession(session, false);
        }
        return result;
    }
    
    @Override
    public UserRole getRole(long role) {
        Session session = null;
        UserRole result = null;
        
        try {
            session = this.sessionFactory.openSession();
            result = (UserRole) session.createCriteria(UserRole.class).add(Restrictions.eq("roleId", role)).uniqueResult();
        }catch(HibernateException e){
            LOG.error(e, e);
        }finally {
            closeSession(session, false);
        }
        
        return result;
    }
    
    @Override
    public List<UserRole> getAllRoles(){
        Session session = null;
        
        List<UserRole> roles = null;
        
        try {
            session = this.sessionFactory.openSession();
            roles = session.createCriteria(UserRole.class).list();
        }catch(HibernateException e){
            LOG.error(e, e);
        }finally {
            closeSession(session, false);
        }
        
        return roles;
    }
    
    @Override
    public List<RolePreference> getPreferences(UserRole role) {
        Session session = null;
        
        List<RolePreference> preferences = null;
        
        try {
            session = this.sessionFactory.openSession();
            
            preferences = session.createCriteria(RolePreference.class).add(Restrictions.eq("userRole", role)).list();
        }catch(HibernateException e){
            LOG.error(e, e);
        }finally {
            closeSession(session, false);
        }
        
        return preferences;
    }
    
    
    @Override
    public boolean updatePreference(RolePreference preference) {
        Session session = null;
        Transaction tx = null;
        boolean updated = false;
        
        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();
            
            session.update(preference);
            tx.commit();
            updated = true;
        }catch(HibernateException e){
            LOG.error(e, e);
            transactionRollback(tx);
            updated = false;
        }finally {
            closeSession(session, false);
        }
        
        return updated;
    }


    /**
     *
     * @param tx the transaction
     */
    private void transactionRollback(Transaction tx) {
        if (tx != null) {
            tx.rollback();
        }
    }

    /**
     *
     * @param session the session
     * @param flush the boolean
     */
    private void closeSession(Session session, boolean flush) {
        if (session != null) {
            if (flush) {
                session.flush();
            }
            session.close();
        }
    }

    
}
