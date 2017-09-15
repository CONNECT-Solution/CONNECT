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
package gov.hhs.fha.nhinc.admingui.services.impl;

import gov.hhs.fha.nhinc.admingui.services.StatusEvent;
import gov.hhs.fha.nhinc.event.dao.DatabaseEventLoggerDao;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author jassmit
 */
public class StatusEventImpl implements StatusEvent {

    private static final String EVENT_SERVICETYPE_NAME = "serviceType";
    private HashMap<String, Integer> inboundCounts = new HashMap<>();
    private HashMap<String, Integer> outboundCounts = new HashMap<>();
    private DatabaseEventLoggerDao eventDao;

    private static final String PD_SERVICE_TYPE = "Patient Discovery";
    private static final String PD_DEF_REQ_SERVICE_TYPE = "Patient Discovery Deferred Request";
    private static final String PD_DEF_RESP_SERVICE_TYPE = "Patient Discovery Deferred Response";
    private static final String DQ_SERVICE_TYPE = "Document Query";
    private static final String DR_SERVICE_TYPE = "Retrieve Document";
    private static final String DS_SERVICE_TYPE = "Document Submission";
    private static final String DS_DEF_REQ_SERVICE_TYPE = "Document Submission Deferred Request";
    private static final String DS_DEF_RESP_SERVICE_TYPE = "Document Submission Deferred Response";
    
    public static final String INBOUND_EVENT_TYPE = "END_INBOUND_MESSAGE";
    public static final String OUTBOUND_EVENT_TYPE = "END_INVOCATION_TO_NWHIN";

    @Override
    public void setCounts() {
        inboundCounts.clear();
        outboundCounts.clear();
        List inboundResults = getEventLoggerDao().getCounts(INBOUND_EVENT_TYPE, EVENT_SERVICETYPE_NAME);
        List outboundResults = getEventLoggerDao().getCounts(OUTBOUND_EVENT_TYPE, EVENT_SERVICETYPE_NAME);
        
        inboundCounts = setEvents(inboundResults);
        outboundCounts = setEvents(outboundResults);
    }
    
    @Override
    public long getTotalInboundRequests() {
        long count = 0;
        List inboundList = getEventLoggerDao().getCounts(INBOUND_EVENT_TYPE, ArrayUtils.EMPTY_STRING_ARRAY);
        if(!CollectionUtils.isEmpty(inboundList)) {
            count = (long) inboundList.get(0);
        }
        return count;
    }
    
    @Override
    public long getTotatOutboundRequests() {
        long count = 0;
        List outboundList = getEventLoggerDao().getCounts(OUTBOUND_EVENT_TYPE, ArrayUtils.EMPTY_STRING_ARRAY);
        if(!CollectionUtils.isEmpty(outboundList)) {
            count = (long) outboundList.get(0);
        }
        return count;
    }
    
    @Override
    public HashMap<String, Integer> getServiceList() {
        List serviceList = getEventLoggerDao().getCounts(null, EVENT_SERVICETYPE_NAME);
        return setEvents(serviceList);
    }

    @Override
    public HashMap<String, Integer> getInboundEventCounts() {
        return inboundCounts;
    }

    @Override
    public HashMap<String, Integer> getOutboundEventCounts() {
        return outboundCounts;
    }

    protected DatabaseEventLoggerDao getEventLoggerDao() {
        if(eventDao == null) {
            eventDao = new DatabaseEventLoggerDao();
        }
        return eventDao;
    }

    private HashMap<String, Integer> setEvents(List results) {
        int patientDiscoveryCount = 0;
        int x12Count = 0;
        int docSubmissionCount = 0;
        int docQueryCount = 0;
        int docRetrieveCount = 0;

        for (Object result : results) {
            if (result instanceof Object[] && ((Object[]) result).length == 2) {
                Object[] resultArray = (Object[]) result;
                Long count = (Long) resultArray[0];
                String serviceType = (String) resultArray[1];

                if (serviceType.equalsIgnoreCase(PD_SERVICE_TYPE) || serviceType.equalsIgnoreCase(PD_DEF_REQ_SERVICE_TYPE)
                        || serviceType.equalsIgnoreCase(PD_DEF_RESP_SERVICE_TYPE)) {
                    patientDiscoveryCount += count;
                } else if (serviceType.equalsIgnoreCase(DQ_SERVICE_TYPE)) {
                    docQueryCount += count;
                } else if (serviceType.equalsIgnoreCase(DR_SERVICE_TYPE)) {
                    docRetrieveCount += count;
                } else if (serviceType.equalsIgnoreCase(DS_SERVICE_TYPE) || serviceType.equalsIgnoreCase(DS_DEF_REQ_SERVICE_TYPE)
                        || serviceType.equalsIgnoreCase(DS_DEF_RESP_SERVICE_TYPE)) {
                    docSubmissionCount += count;
                }
            }
        }

        HashMap<String, Integer> events = new HashMap<>();
        events.put(PD_SERVICE_TYPE, patientDiscoveryCount);
        events.put(DQ_SERVICE_TYPE, docQueryCount);
        events.put(DS_SERVICE_TYPE, docSubmissionCount);
        events.put(DR_SERVICE_TYPE, docRetrieveCount);
        
        return events;
    }

}
