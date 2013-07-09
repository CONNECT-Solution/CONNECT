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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.SessionImpl;
import org.hibernate.loader.OuterJoinLoader;
import org.hibernate.loader.criteria.CriteriaLoader;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.hibernate.type.Type;

import com.sun.xml.xsom.impl.scd.Iterators.Map;

/**
 * Data access object class for EventCode data
 * 
 * @author Neil Webb
 */
public class EventCodeDao {
    private static final Logger LOG = Logger.getLogger(EventCodeDao.class);
    private static final String EBXML_EVENT_CODE_LIST = "$XDSDocumentEntryEventCodeList";

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
     * @param eventCode EventCode record to delete.
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


    @SuppressWarnings("unchecked")
    public List<EventCode> eventCodeQuery(List<SlotType1> slots) {
        List<EventCode> eventCodes = null;
        List<String> eventCodesList = new ArrayList<String>();
        List<String> eventCodeSchemeList = new ArrayList<String>();
        String sql = null;
        Session sess = null;
        try {
            SessionFactory fact = getSessionFactory();
            if (fact != null) {
                sess = getSession(fact);
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(EventCode.class);
                    DetachedCriteria subCriteria = DetachedCriteria.forClass(EventCode.class);
                    Criterion andCondition = Restrictions.conjunction();
                    Disjunction orCondition = Restrictions.disjunction();
                    String[] alias = new String[1];
                    alias[0] = "documentid";
                    Type[] types = new Type[1];
                    types[0] = Hibernate.INTEGER;
                    List<String> classCodes = null;
                    List<String> orValues = new ArrayList<String>();
                    int eventCodeSlotSize = 0;
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    if (slots != null) {
                        for (SlotType1 slot : slots) {
                            if ((slot.getName() != null) && (slot.getName().length() > 0)
                                    && (slot.getValueList() != null) && (slot.getValueList().getValue() != null)
                                    && (slot.getValueList().getValue().size() > 0)) {
                                if (slot.getName().equals(EBXML_EVENT_CODE_LIST)) {
                                    eventCodeSlotSize++;
                                    ValueListType valueListType = slot.getValueList();
                                    List<String> slotValues = valueListType.getValue();
                                    classCodes = new ArrayList<String>();
                                    for (int j = 0; j < slotValues.size(); j++) {
                                        parseParamFormattedString(slotValues.get(j), classCodes);
                                        if (slotValues.get(j).contains(",")) {
                                            orValues = Arrays.asList(slotValues.get(j).split("\\,"));
                                            for (int l = 0; l < orValues.size(); l++) {
                                                String innereventCode = getEventCode(classCodes.get(l), "eventCode");
                                                String innereventCodeScheme = getEventCode(classCodes.get(l),
                                                        "eventCodeScheme");
                                                andCondition = Restrictions.and(
                                                        Restrictions.eq("eventCode", innereventCode),
                                                        Restrictions.eq("eventCodeScheme", innereventCodeScheme));
                                                orCondition.add(andCondition);
                                                eventCodesList.add(innereventCode);
                                                eventCodeSchemeList.add(innereventCodeScheme);
                                                hashMap.put((innereventCode+"^^"+innereventCodeScheme), Integer.toString(eventCodeSlotSize));
                                            }
                                        } else {
                                            String eventCode = getEventCode(classCodes.get(j), "eventCode");
                                            String eventCodeScheme = getEventCode(classCodes.get(j), "eventCodeScheme");
                                            orCondition.add(Restrictions.and(Restrictions.eq("eventCode", eventCode),
                                                    Restrictions.eq("eventCodeScheme", eventCodeScheme)));
                                            eventCodesList.add(eventCode);
                                            eventCodeSchemeList.add(eventCodeScheme);
                                            hashMap.put((eventCode+"^^"+eventCodeScheme), Integer.toString(eventCodeSlotSize));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    String groupBy = "documentid" + " having " + "count(*) >= " + eventCodeSlotSize;
                    subCriteria.add(orCondition);

                    subCriteria.setProjection(Projections.projectionList().add(
                            Projections.sqlGroupProjection("documentid", groupBy, alias, types)));

                    criteria.add(Subqueries.propertyIn("document", subCriteria));
                    criteria.addOrder(Order.asc("document"));
                    sql = toSql(sess, criteria);
                    eventCodes = criteria.list();
                    List<Long> DocumentIds = new ArrayList<Long>();
                    DocumentIds = getDocumentIds(eventCodes);
                    List<Long> uniqueDocumentIds = new ArrayList<Long>();
                    uniqueDocumentIds = getUniqueDocumentIds(DocumentIds);
                    boolean present = false;
                    List<Long> documentNotPresent = new ArrayList<Long>();
                    for (int i = 0; i < uniqueDocumentIds.size(); i++) {
                        present = documentInAllSlots(eventCodes, eventCodeSlotSize, hashMap, uniqueDocumentIds.get(i));
                        if (!present) {
                            documentNotPresent.add(uniqueDocumentIds.get(i));
                        }
                    }
                    eventCodes = resultEventCodesList(documentNotPresent, eventCodes);
                    if (eventCodes == null) {
                        eventCodes = new ArrayList<EventCode>();
                    }
                } else {
                    LOG.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                LOG.error("Session factory was null");
            }
        } finally

        {
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
    
    protected List<Long> getDocumentIds(List<EventCode> eventCodes) {
        List<Long> DocumentIds = new ArrayList<Long>();
        for (int i = 0; i < eventCodes.size(); i++) {
            DocumentIds.add(eventCodes.get(i).getDocument().getDocumentid());
        }
        return DocumentIds;
    }
    
    private List<Long> getUniqueDocumentIds(List<Long> DocumentIds) {
        Set<Long> uniqueDocumentRef = new HashSet<Long>(DocumentIds);
        DocumentIds.clear();
        DocumentIds.addAll(uniqueDocumentRef);
        return DocumentIds;
    }

    /**
     * @param documentNotPresent
     * @param eventCodes
     * @return
     */
    private List<EventCode> resultEventCodesList(List<Long> documentNotPresent, List<EventCode> eventCodes) {

        for (int i = 0; i < documentNotPresent.size(); i++) {
            int eventCodesSize = eventCodes.size();
            for (int j = 0; j < eventCodesSize; j++) {
                int index = eventCodes.size() - 1;
                if (eventCodes.get(index).getDocument().getDocumentid().equals(documentNotPresent.get(i))) {
                    eventCodes.remove(index);
                }
            }
        }
        return eventCodes;
    }

    /**
     * @param eventCodes
     * @param eventCodeSlotSize
     * @param hashMap
     * @return
     */
    private boolean documentInAllSlots(List<EventCode> eventCodes, int eventCodeSlotSize,
            HashMap<String, String> hashMap, Long documentId) {
        boolean slotsPresent = false;
        for (int i = 1; i <= eventCodeSlotSize; i++) {
            slotsPresent = findDocumentId(hashMap, documentId, eventCodes, i);
            if (slotsPresent) {
                continue;
            } else {
                slotsPresent = false;
                return slotsPresent;
            }
        }
        return slotsPresent;
    }

    protected boolean findDocumentId(HashMap<String, String> hashMap, Long documentId, List<EventCode> eventCodes,
            int slotIndex) {
        java.util.Iterator<Entry<String, String>> entries = hashMap.entrySet().iterator();
        boolean doucmentPresent = false;
        while (entries.hasNext()) {
            Entry<String, String> entry = entries.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            for (int j = 0; j < eventCodes.size(); j++) {
                if ((slotIndex == Integer.parseInt(value)) && ((eventCodes.get(j).getEventCode()+"^^"
                  +eventCodes.get(j).getEventCodeScheme()).equals(key))) {
                    Long extractedDocumentid = eventCodes.get(j).getDocument().getDocumentid();
                    if (extractedDocumentid.equals(documentId)) {
                        doucmentPresent = true;
                    }
                }

            }
        }
        return doucmentPresent;
    }

    protected boolean eventCodeFound(List<String> eventCodesList, String eventCode, List<EventCode> eventCodes) {
        boolean contains = false;
        for (int i = 0; i < eventCodesList.size(); i++) {
            if (eventCodesList.get(i).equals(eventCode)) {
                contains = true;
            }
        }
        return contains;

    }

    protected String toSql(Session session, Criteria criteria) {
        String sql = null;
        try {
            CriteriaImpl c = (CriteriaImpl) criteria;
            SessionImpl s = (SessionImpl) c.getSession();
            SessionFactoryImplementor factory = (SessionFactoryImplementor) s.getSessionFactory();
            String[] implementors = factory.getImplementors(c.getEntityOrClassName());
            CriteriaLoader loader = new CriteriaLoader((OuterJoinLoadable) factory.getEntityPersister(implementors[0]),
                    factory, c, implementors[0], s.getEnabledFilters());
            Field f = OuterJoinLoader.class.getDeclaredField("sql");
            f.setAccessible(true);
            sql = (String) f.get(loader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sql;
    }

    private String getEventCode(String eventCodeParam, String paramName) {
        String[] eventCodeList = null;
        String separate = "\\^\\^";
        eventCodeList = (eventCodeParam.split(separate));
        if (paramName.equalsIgnoreCase("eventCode")) {
            return eventCodeList[0];
        } else {
            return eventCodeList[1];
        }
    }

    public void parseParamFormattedString(String paramFormattedString, List<String> resultCollection) {
        if ((paramFormattedString != null) && (resultCollection != null)) {
            if (paramFormattedString.startsWith("(")) {
                String working = paramFormattedString.substring(1);
                int endIndex = working.indexOf(")");
                if (endIndex != -1) {
                    working = working.substring(0, endIndex);
                }
                String[] multiValueString = working.split(",");
                if (multiValueString != null) {
                    for (int i = 0; i < multiValueString.length; i++) {
                        String singleValue = multiValueString[i];
                        if (singleValue != null) {
                            singleValue = singleValue.trim();
                            if (singleValue.startsWith("'")) {
                                singleValue = singleValue.substring(1);
                                int endTickIndex = singleValue.indexOf("'");
                                if (endTickIndex != -1) {
                                    singleValue = singleValue.substring(0, endTickIndex);
                                }
                            }
                        }
                        resultCollection.add(singleValue);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Added single value: " + singleValue + " to query parameters");
                        }
                    }
                }
            } else {
                resultCollection.add(paramFormattedString);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No wrapper on status - adding status: " + paramFormattedString + " to query parameters");
                }
            }
        }
    }
}
