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
package gov.hhs.fha.nhinc.event.dao;

import static gov.hhs.fha.nhinc.event.model.EventDTO.OMIT_DESCRIPTION;
import static gov.hhs.fha.nhinc.util.GenericDBUtils.closeSession;
import static gov.hhs.fha.nhinc.util.GenericDBUtils.findAllBy;
import static gov.hhs.fha.nhinc.util.GenericDBUtils.readBy;
import static gov.hhs.fha.nhinc.util.GenericDBUtils.save;

import gov.hhs.fha.nhinc.event.model.DatabaseEvent;
import gov.hhs.fha.nhinc.event.model.EventDTO;
import gov.hhs.fha.nhinc.event.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object which logs events in the database.
 */
public class DatabaseEventLoggerDao {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseEventLoggerDao.class);

    private static final String EVENT_TYPE_NAME = "eventName";
    private static final String DATE_NAME = "eventTime";
    private static final String SERVICE_TYPE = "serviceType";

    private static class SingletonHolder {

        public static final DatabaseEventLoggerDao INSTANCE = new DatabaseEventLoggerDao();

        private SingletonHolder() {
        }
    }

    public static DatabaseEventLoggerDao getInstance() {
        LOG.debug("getInstance()...");
        return SingletonHolder.INSTANCE;
    }

    public boolean insertEvent(final DatabaseEvent databaseEvent) {
        return save(getSession(), databaseEvent);
    }

    /**
     * Hibernate Query for event counts for a given Event type grouping the provided column names
     *
     * @param eventType a list of event types to include, or null for all event types
     * @return List of Object[] with [0] the count, [n] the provided grpProjections[n]
     */
    public List getCounts(final List<String> eventTypes, String... grpProjections) {

        List results = new ArrayList<>();
        Session session = null;
        try {
            session = getSession();

            ProjectionList projList = Projections.projectionList();
            projList.add(Projections.rowCount());

            for (String projection : grpProjections) {
                projList.add(Projections.groupProperty(projection));
            }

            Criteria criteria = session.createCriteria(DatabaseEvent.class);
            if (CollectionUtils.isEmpty(eventTypes)) {
                criteria.add(Restrictions.in(EVENT_TYPE_NAME, eventTypes));
            }

            results = criteria.setProjection(projList).list();
        } finally {
            closeSession(session);
        }

        return results;
    }

    public DatabaseEvent getLatestEvent(final String eventType) {
        Session session = null;
        try {
            session = getSession();
            return (DatabaseEvent) session.createCriteria(DatabaseEvent.class)
                .add(Restrictions.eq(EVENT_TYPE_NAME, eventType)).addOrder(Order.desc(DATE_NAME)).setMaxResults(1)
                .uniqueResult();
        } finally {
            closeSession(session);
        }
    }

    public EventDTO getEventById(Long id) {
        return EventDTO.convertFrom(readBy(getSession(), DatabaseEvent.class, id));
    }


    public Set<String> getExceptions() {
        Set<String> exceptions = new HashSet<>();
        for (EventDTO event : getAllFailureMessages(null, null, null, null)) {
            exceptions.add(event.getExceptionType());
        }
        return exceptions;
    }


    public List<EventDTO> getAllFailureMessages(String serviceType, String exceptionType, Date startDate,
        Date endDate) {
        return filterEventsByException(
            findAllBy(getSession(), DatabaseEvent.class, buildCriterionFor(serviceType, startDate, endDate)),
            exceptionType);
    }

    protected SessionFactory getSessionFactory() {
        SessionFactory fact = null;
        HibernateUtil util = HibernateUtilFactory.getEventHibernateUtil();
        if (util != null) {
            fact = util.getSessionFactory();
        }
        return fact;
    }

    protected Session getSession() {
        return getSessionFactory().openSession();
    }

    private static List<EventDTO> filterEventsByException(List<DatabaseEvent> events, String exceptionType) {
        LinkedList<EventDTO> list = new LinkedList<>();

        for (DatabaseEvent event : events) {
            EventDTO dto = EventDTO.convertFrom(event, OMIT_DESCRIPTION);
            if (StringUtils.isNotBlank(exceptionType)) {
                if (exceptionType.equals(dto.getExceptionType())) {
                    list.add(dto); // filter-list
                }
            } else {
                list.add(dto); // all-list
            }
        }
        return list;
    }

    private static List<Criterion> buildCriterionFor(String serviceType, Date startDate, Date endDate) {
        List<Criterion> critList = new LinkedList<>();

        critList.add(Restrictions.eq(EVENT_TYPE_NAME, "MESSAGE_PROCESSING_FAILED"));

        if (startDate != null) {
            critList.add(Restrictions.ge(DATE_NAME, startDate));
        }

        if (endDate != null) {
            critList.add(Restrictions.le(DATE_NAME, endDate));
        }

        if (StringUtils.isNotBlank(serviceType)) {
            critList.add(Restrictions.eq(SERVICE_TYPE, serviceType));
        }
        return critList;
    }
}
