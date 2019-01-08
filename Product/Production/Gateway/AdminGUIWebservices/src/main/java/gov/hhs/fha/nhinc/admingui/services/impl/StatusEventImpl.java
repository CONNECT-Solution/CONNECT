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
package gov.hhs.fha.nhinc.admingui.services.impl;

import gov.hhs.fha.nhinc.admingui.services.StatusEvent;
import gov.hhs.fha.nhinc.event.dao.DatabaseEventLoggerDao;
import gov.hhs.fha.nhinc.event.model.EventCount;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author jassmit
 */
public class StatusEventImpl implements StatusEvent {

    private static final String EVENT_SERVICETYPE_NAME = "serviceType";
    private static final String EVENT_TYPE_NAME = "eventName";

    private HashMap<String, EventCount> eventCounts = new HashMap<>();
    private DatabaseEventLoggerDao eventDao;

    private static final String PD_SERVICE_TYPE = "Patient Discovery";
    private static final String PD_DEF_REQ_SERVICE_TYPE = "Patient Discovery Deferred Request";
    private static final String PD_DEF_RESP_SERVICE_TYPE = "Patient Discovery Deferred Response";
    private static final String DS_SERVICE_TYPE = "Document Submission";
    private static final String DS_DEF_REQ_SERVICE_TYPE = "Document Submission Deferred Request";
    private static final String DS_DEF_RESP_SERVICE_TYPE = "Document Submission Deferred Response";
    private static final String PD_MPI_SERVICE_TYPE = "Patient Discovery MPI";

    public static final String INBOUND_EVENT_TYPE = "END_INBOUND_MESSAGE";
    public static final String OUTBOUND_EVENT_TYPE = "END_INVOCATION_TO_NWHIN";


    @Override
    public void setCounts() {
        List results = getEventLoggerDao()
            .getCounts(Arrays.asList(INBOUND_EVENT_TYPE, OUTBOUND_EVENT_TYPE), EVENT_SERVICETYPE_NAME, EVENT_TYPE_NAME);

        eventCounts = setEvents(results);
    }

    @Override
    public long getTotalInboundRequests() {
        long count = 0;
        for (EventCount counts : eventCounts.values()) {
            count += counts.getInbound();
        }
        return count;
    }

    @Override
    public long getTotalOutboundRequests() {
        long count = 0;
        for (EventCount counts : eventCounts.values()) {
            count += counts.getOutbound();
        }
        return count;
    }

    @Override
    public Map<String, Long> getInboundEventCounts() {
        Map<String, Long> counts = new HashMap<>();
        for (Entry<String, EventCount> count : eventCounts.entrySet()) {
            counts.put(count.getKey(), count.getValue().getInbound());
        }
        return counts;
    }

    @Override
    public Map<String, Long> getOutboundEventCounts() {
        Map<String, Long> counts = new HashMap<>();
        for (Entry<String, EventCount> count : eventCounts.entrySet()) {
            counts.put(count.getKey(), count.getValue().getOutbound());
        }
        return counts;
    }

    @Override
    public Map<String, EventCount> getEventCounts() {
        return eventCounts;
    }

    protected DatabaseEventLoggerDao getEventLoggerDao() {
        if(eventDao == null) {
            eventDao = new DatabaseEventLoggerDao();
        }
        return eventDao;
    }

    private HashMap<String, EventCount> setEvents(List results) {

        eventCounts = new HashMap<>();

        for (Object result : results) {
            if (result instanceof Object[] && ((Object[]) result).length == 3) {
                Object[] resultArray = (Object[]) result;
                Long count = (Long) resultArray[0];
                String serviceType = convertServiceType((String) resultArray[1]);

                // Skip processing if we are supposed to ignore it.
                if (serviceType == null) {
                    continue;
                }

                String messageType = (String) resultArray[2];


                EventCount eventCount = eventCounts.get(serviceType);
                if (eventCount == null) {
                    eventCount = new EventCount(serviceType);
                    eventCounts.put(serviceType, eventCount);
                }

                // Add the current row's count to the existing count.
                // That way any conversions will add to the correct count and not overwrite it.
                if (INBOUND_EVENT_TYPE.equals(messageType)) {
                    eventCount.setInbound(eventCount.getInbound() + count);
                } else if (OUTBOUND_EVENT_TYPE.equals(messageType)) {
                    eventCount.setOutbound(eventCount.getOutbound() + count);
                }
            }
        }

        return eventCounts;
    }

    /**
     * Performs any service type matching to another service type. Should be used to
     * transform Deferred request and response types to their proper non-deferred service.
     *
     * This method will also be responsible for returning null if the service should be ignored
     * If there is no conversion to be done, this method will return the original string.
     *
     * @param String to be converted
     * @return converted String, or NULL if it is to be ignored.
     */
    private static String convertServiceType(String serviceType) {
        if (isPDServiceType(serviceType)) {
            return PD_SERVICE_TYPE;
        } else if (isDSServiceType(serviceType)) {
            return DS_SERVICE_TYPE;
        } else if (isIgnored(serviceType)) {
            return null;
        }
        return serviceType;
    }

    private static boolean isPDServiceType(String serviceType) {
        return PD_SERVICE_TYPE.equalsIgnoreCase(serviceType) || PD_DEF_REQ_SERVICE_TYPE.equalsIgnoreCase(serviceType)
            || PD_DEF_RESP_SERVICE_TYPE.equalsIgnoreCase(serviceType);
    }

    private static boolean isDSServiceType(String serviceType) {
        return DS_SERVICE_TYPE.equalsIgnoreCase(serviceType) || DS_DEF_REQ_SERVICE_TYPE.equalsIgnoreCase(serviceType)
            || DS_DEF_RESP_SERVICE_TYPE.equalsIgnoreCase(serviceType);
    }

    private static boolean isIgnored(String serviceType) {
        return PD_MPI_SERVICE_TYPE.equalsIgnoreCase(serviceType);
    }

}
