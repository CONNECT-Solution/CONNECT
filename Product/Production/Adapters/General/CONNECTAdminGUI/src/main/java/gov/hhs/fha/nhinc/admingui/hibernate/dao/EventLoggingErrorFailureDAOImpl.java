/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.event.model.DatabaseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author vimehta
 *
 */
@Service
public class EventLoggingErrorFailureDAOImpl implements EventLoggingErrorFailureDAO {

    @Autowired
    HibernateUtil hibernateUtil;
    private static final Logger LOG = LoggerFactory.getLogger(EventLoggingErrorFailureDAOImpl.class);

    private static final String EVENT_TIME = "eventTime";
    private static final String EVENT_NAME = "eventName";
    private static final String SERVICE_TYPE = "serviceType";
    private List<String> exceptions = null;
    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.admingui.hibernate.dao.EventLoggingErrorFailureDAO#
     * getFailureMessageById(java.lang.Long)
     */
    @Override
    public DatabaseEvent getFailureMessageById(Long id) {
        Session session = null;
        DatabaseEvent event = null;
        try {
            session = getSession();
            event = (DatabaseEvent) session.createCriteria(DatabaseEvent.class)
                    .add(Restrictions.eq("MessageId", id)).uniqueResult();
        } finally {
            closeSession(session, false);
        }

        return event;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.admingui.hibernate.dao.EventLoggingErrorFailureDAO#
     * getExceptions()
     */
    @Override
    public List<String> getExceptions() {
        exceptions = new ArrayList<>();
        List<DatabaseEvent> events = getAllFailureMessages("", null, null);
        for (DatabaseEvent event : events) {
            try {
                JSONObject jObject = new JSONObject(event);
                exceptions.add(jObject.getString("exceptionClass").replaceAll("class", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return exceptions;
    }

    private static void closeSession(Session session, boolean flush) {
        if (session != null) {
            if (flush) {
                session.flush();
            }
            session.close();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.admingui.hibernate.dao.EventLoggingErrorFailureDAO#
     * getAllFailureMessages(java.lang.String, java.util.Date, java.util.Date)
     */
    @Override
    public List<DatabaseEvent> getAllFailureMessages(String serviceType, Date startDate, Date endDate) {
        Session session = null;
        List<DatabaseEvent> events = null;

        try {
            session = getSession();
            Criteria criteria = session.createCriteria(DatabaseEvent.class);

            criteria.add(Restrictions.eq(EVENT_NAME, "MESSAGE_PROCESSING_FAILED"));

            if (startDate != null) {
                criteria.add(Restrictions.ge(EVENT_TIME, startDate));
            }

            if (endDate != null) {
                criteria.add(Restrictions.le(EVENT_TIME, endDate));
            }

            if (StringUtils.isNotBlank(serviceType)) {
                criteria.add(Restrictions.eq(SERVICE_TYPE, serviceType));
            }


        } catch (HibernateException e) {
            LOG.error("Exception getting failure messages caused by :{}", e.getLocalizedMessage(), e);
        } finally {
            closeSession(session, false);
        }

        return events;
    }

    /**
     * Returns a new session from AsyncMessages HibernateUtil
     *
     * @return
     */
    protected Session getSession() {
        return hibernateUtil.getSessionFactory().openSession();
    }
}
