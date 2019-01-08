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
package gov.hhs.fha.nhinc.docrepository.adapter.dao;

import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import gov.hhs.fha.nhinc.util.GenericDBUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data access object class for EventCode data.
 *
 * @author Neil Webb, msw
 */

public class EventCodeDao {

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(EventCodeDao.class);

    /**
     * The Constant EBXML_EVENT_CODE_LIST.
     */
    private static final String EBXML_EVENT_CODE_LIST = "$XDSDocumentEntryEventCodeList";

    /**
     * Event code query.
     *
     * @param slots the slots
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<EventCode> eventCodeQuery(List<SlotType1> slots) {
        List<EventCode> eventCodes = null;
        List<String> eventCodesList = new ArrayList<>();
        List<String> eventCodeSchemeList = new ArrayList<>();
        Session sess = null;
        try {
            sess = getSession();

            if (sess != null) {
                Criteria criteria = sess.createCriteria(EventCode.class);
                DetachedCriteria subCriteria = DetachedCriteria.forClass(EventCode.class);
                Criterion andCondition = Restrictions.conjunction();
                Disjunction orCondition = Restrictions.disjunction();
                String[] alias = new String[1];
                alias[0] = "documentid";
                Type[] types = new Type[1];
                types[0] = org.hibernate.type.StandardBasicTypes.INTEGER;
                List<String> classCodes;
                List<String> orValues;
                int eventCodeSlotSize = 0;
                HashMap<String, String> hashMap = new HashMap<>();
                if (slots != null) {
                    for (SlotType1 slot : slots) {

                        boolean slotContainsName = slot.getName() != null && slot.getName().length() > 0;
                        boolean slotHasValues = slot.getValueList() != null && slot.getValueList().getValue() != null
                            && !slot.getValueList().getValue().isEmpty();

                        if (slotContainsName && slotHasValues && slot.getName().equals(EBXML_EVENT_CODE_LIST)) {
                            eventCodeSlotSize++;
                            ValueListType valueListType = slot.getValueList();
                            List<String> slotValues = valueListType.getValue();
                            classCodes = new ArrayList<>();
                            for (int j = 0; j < slotValues.size(); j++) {
                                parseParamFormattedString(slotValues.get(j), classCodes);
                                if (slotValues.get(j).contains(",")) {
                                    orValues = Arrays.asList(slotValues.get(j).split("\\,"));
                                    for (int l = 0; l < orValues.size(); l++) {
                                        String innereventCode = getEventCode(classCodes.get(l), "eventCode");
                                        String innereventCodeScheme = getEventCode(classCodes.get(l),
                                            "eventCodeScheme");
                                        andCondition = Restrictions.and(Restrictions.eq("eventCode", innereventCode),
                                            Restrictions.eq("eventCodeScheme", innereventCodeScheme));
                                        orCondition.add(andCondition);
                                        eventCodesList.add(innereventCode);
                                        eventCodeSchemeList.add(innereventCodeScheme);
                                        hashMap.put(innereventCode + "^^" + innereventCodeScheme,
                                            Integer.toString(eventCodeSlotSize));
                                    }
                                } else {
                                    String eventCode = getEventCode(classCodes.get(j), "eventCode");
                                    String eventCodeScheme = getEventCode(classCodes.get(j), "eventCodeScheme");
                                    orCondition.add(Restrictions.and(Restrictions.eq("eventCode", eventCode),
                                        Restrictions.eq("eventCodeScheme", eventCodeScheme)));
                                    eventCodesList.add(eventCode);
                                    eventCodeSchemeList.add(eventCodeScheme);
                                    hashMap.put(eventCode + "^^" + eventCodeScheme,
                                        Integer.toString(eventCodeSlotSize));
                                }
                            }

                        }
                    }
                }
                String groupBy = "documentid having count(*) >= " + eventCodeSlotSize;
                subCriteria.add(orCondition);

                subCriteria.setProjection(Projections.projectionList()
                    .add(Projections.sqlGroupProjection("documentid", groupBy, alias, types)));

                criteria.add(Subqueries.propertyIn("document", subCriteria));
                criteria.addOrder(Order.asc("document"));
                eventCodes = criteria.list();
                List<Long> documentIds;
                documentIds = getDocumentIds(eventCodes);
                List<Long> uniqueDocumentIds;
                uniqueDocumentIds = getUniqueDocumentIds(documentIds);
                boolean present;
                List<Long> documentNotPresent = new ArrayList<>();
                for (int i = 0; i < uniqueDocumentIds.size(); i++) {
                    present = documentInAllSlots(eventCodes, eventCodeSlotSize, hashMap, uniqueDocumentIds.get(i));
                    if (!present) {
                        documentNotPresent.add(uniqueDocumentIds.get(i));
                    }
                }
                eventCodes = resultEventCodesList(documentNotPresent, eventCodes);
                eventCodes = eventCodes == null ? new ArrayList<EventCode>() : eventCodes;

            } else {
                LOG.error("Session was null");
            }
        } finally {
            GenericDBUtils.closeSession(sess);
        }
        return eventCodes;
    }

    /**
     * Gets the document ids.
     *
     * @param eventCodes the event codes
     * @return the document ids
     */
    protected List<Long> getDocumentIds(List<EventCode> eventCodes) {
        List<Long> documentIds = new ArrayList<>();
        for (int i = 0; i < eventCodes.size(); i++) {
            documentIds.add(eventCodes.get(i).getDocument().getDocumentid());
        }
        return documentIds;
    }

    /**
     * Gets the unique document ids.
     *
     * @param documentIds the document ids
     * @return the unique document ids
     */
    private List<Long> getUniqueDocumentIds(List<Long> documentIds) {
        Set<Long> uniqueDocumentRef = new HashSet<>(documentIds);
        documentIds.clear();
        documentIds.addAll(uniqueDocumentRef);
        return documentIds;
    }

    /**
     * Result event codes list.
     *
     * @param documentNotPresent the document not present
     * @param eventCodes the event codes
     * @return the list
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
     * Document in all slots.
     *
     * @param eventCodes the event codes
     * @param eventCodeSlotSize the event code slot size
     * @param hashMap the hash map
     * @param documentId the document id
     * @return true, if successful
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

    /**
     * Find document id.
     *
     * @param hashMap the hash map
     * @param documentId the document id
     * @param eventCodes the event codes
     * @param slotIndex the slot index
     * @return true, if successful
     */
    protected boolean findDocumentId(HashMap<String, String> hashMap, Long documentId, List<EventCode> eventCodes,
        int slotIndex) {
        java.util.Iterator<Entry<String, String>> entries = hashMap.entrySet().iterator();
        boolean doucmentPresent = false;
        while (entries.hasNext()) {
            Entry<String, String> entry = entries.next();
            String key = entry.getKey();
            String value = entry.getValue();
            for (int j = 0; j < eventCodes.size(); j++) {
                if (slotIndex == Integer.parseInt(value)
                    && (eventCodes.get(j).getEventCode() + "^^" + eventCodes.get(j).getEventCodeScheme())
                    .equals(key)) {
                    Long extractedDocumentid = eventCodes.get(j).getDocument().getDocumentid();
                    if (extractedDocumentid.equals(documentId)) {
                        doucmentPresent = true;
                    }
                }

            }
        }
        return doucmentPresent;
    }

    /**
     * Event code found.
     *
     * @param eventCodesList the event codes list
     * @param eventCode the event code
     * @param eventCodes the event codes
     * @return true, if successful
     */
    protected boolean eventCodeFound(List<String> eventCodesList, String eventCode, List<EventCode> eventCodes) {
        boolean contains = false;
        for (int i = 0; i < eventCodesList.size(); i++) {
            if (eventCodesList.get(i).equals(eventCode)) {
                contains = true;
            }
        }
        return contains;

    }

    /**
     * Gets the event code.
     *
     * @param eventCodeParam the event code param
     * @param paramName the param name
     * @return the event code
     */
    private String getEventCode(String eventCodeParam, String paramName) {
        String[] eventCodeList;
        String separate = "\\^\\^";
        eventCodeList = eventCodeParam.split(separate);
        if ("eventCode".equalsIgnoreCase(paramName)) {
            return eventCodeList[0];
        } else {
            return eventCodeList[1];
        }
    }

    /**
     * Parses the param formatted string.
     *
     * @param paramFormattedString the param formatted string
     * @param resultCollection the result collection
     */
    public void parseParamFormattedString(String paramFormattedString, List<String> resultCollection) {
        if (paramFormattedString != null && resultCollection != null) {
            if (paramFormattedString.startsWith("(")) {
                String working = paramFormattedString.substring(1);
                int endIndex = working.indexOf(")");
                if (endIndex != -1) {
                    working = working.substring(0, endIndex);
                }
                String[] multiValueString = working.split(",");
                if (multiValueString != null) {
                    for (String element : multiValueString) {
                        String singleValue = element;
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
                            LOG.debug("Added single value: {} to query parameters", singleValue);
                        }
                    }
                }
            } else {
                resultCollection.add(paramFormattedString);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No wrapper on status - adding status: {} to query parameters", paramFormattedString);
                }
            }
        }
    }

    public List<EventCode> findAll() {
        return GenericDBUtils.findAll(getSession(), EventCode.class);
    }

    public List<EventCode> findAllBy(Long documentId) {
        return GenericDBUtils.findAllBy(getSession(), EventCode.class,
            Expression.eq("document.documentid", documentId));
    }

    public boolean save(EventCode eventCode) {
        return GenericDBUtils.save(getSession(), eventCode);
    }

    public EventCode findById(Long eventCodeId) {
        return GenericDBUtils.readBy(getSession(), EventCode.class, eventCodeId);
    }

    public boolean delete(EventCode eventCode) {
        return GenericDBUtils.delete(getSession(), eventCode);
    }

    protected Session getSession() {
        Session session = null;
        try {
            session = HibernateUtilFactory.getDocRepoHibernateUtil().getSessionFactory().openSession();
        } catch (HibernateException e) {
            LOG.error("Fail to openSession: {}, {}", e.getMessage(), e);
        }
        return session;
    }

}
