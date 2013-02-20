/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.docrepository.adapter.dao;

import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCodeParam;
import gov.hhs.fha.nhinc.docrepository.adapter.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 * Data access object class for EventCode data
 * 
 * @author Neil Webb
 */
public class EventCodeDao {
    private static final Logger LOG = Logger.getLogger(EventCodeDao.class);

    protected SessionFactory getSessionFactory() {
        return HibernateUtil.getSessionFactory();
    }

    protected Session getSession(SessionFactory sessionFactory) {
        Session session = null;
        if (sessionFactory != null) {
            session = sessionFactory.openSession();
        }
        return session;
    }

    /**
     * Delete an event code record.
     * 
     * @param eventCode
     *            EventCode record to delete.
     */
    public void delete(EventCode eventCode) {
        Session sess = null;
        Transaction trans = null;
        try {
            SessionFactory fact = getSessionFactory();
            if (fact != null) {
                sess = getSession(fact);
                if (sess != null) {
                    trans = sess.beginTransaction();
                    sess.delete(eventCode);
                } else {
                    LOG.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                LOG.error("Session factory was null");
            }
        } finally {
            if (trans != null) {
                try {
                    trans.commit();
                } catch (Throwable t) {
                    LOG.error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    LOG.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
    }

    /**
     * Retrieves all event codes for a given event code value
     * 
     * @param eventCodeParam
     *            Event code query parameter
     * @return EventCode list
     */
    @SuppressWarnings("unchecked")
    public List<EventCode> eventCodeQuery(EventCodeParam eventCodeParam) {
        List<EventCode> eventCodes = null;

        Session sess = null;
        try {
            SessionFactory fact = getSessionFactory();
            if (fact != null) {
                sess = getSession(fact);
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(EventCode.class);
                    boolean performQuery = false;
                    if ((eventCodeParam != null) && NullChecker.isNotNullish(eventCodeParam.getEventCode())) {
                        criteria.add(Restrictions.eq("eventCode", eventCodeParam.getEventCode()));
                        performQuery = true;
                    }
                    if ((eventCodeParam != null) && NullChecker.isNotNullish(eventCodeParam.getEventCodeScheme())) {
                        criteria.add(Restrictions.eq("eventCodeScheme", eventCodeParam.getEventCodeScheme()));
                        performQuery = true;
                    }

                    if (performQuery) {
                        eventCodes = criteria.list();
                    } else {
                        eventCodes = new ArrayList<EventCode>();
                    }
                } else {
                    LOG.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                LOG.error("Session factory was null");
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    LOG.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return eventCodes;
    }
}
