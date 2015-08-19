/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.audit;

import java.util.HashMap;
import java.util.Map;

/*
 *
 * @author achidamb
 */
public class AuditTransformDataBuilder {

    public Map<String, String> eventIdCodeMap;
    public Map<String, String> eventCodeSystemMap;
    public Map<String, String> eventDisplayNameRequestorMap;
    public Map<String, String> eventDisplayNameResponderMap;
    public Map<String, String> eventTypeCodeMap;
    public Map<String, String> eventTypeCodeSystemMap;
    public Map<String, String> eventTypeCodeDisplayNameMap;
    public Map<String, String> eventActionCodeRequestorMap;
    public Map<String, String> eventActionCodeResponderMap;

    private AuditTransformDataBuilder() {
        eventIdCodeMap = createEventIdCodeMap();
        eventCodeSystemMap = createEventCodeSystemMap();
        eventDisplayNameRequestorMap = createEventDisplayNameRequestorMap();
        eventDisplayNameResponderMap = createEventDisplayNameResponderMap();
        eventTypeCodeMap = createEventTypeCodeMap();
        eventTypeCodeSystemMap = createEventTypeCodeSystemMap();
        eventTypeCodeDisplayNameMap = createEventTypeCodeDisplayNameMap();
        eventActionCodeRequestorMap = createEventActionCodeRequestorMap();
        eventActionCodeResponderMap = createEventActionCodeResponderMap();
    }

    private Map<String, String> createEventIdCodeMap() {
        eventIdCodeMap = new HashMap();
        eventIdCodeMap.put("PatientDiscovery", "110112");
        return eventIdCodeMap;
    }

    /**
     *
     * @param serviceName
     * @return
     */
    public String getEventIdCode(String serviceName) {
        if (eventIdCodeMap.containsKey(serviceName)) {
            return eventIdCodeMap.get(serviceName);
        }
        return null;
    }

    private Map<String, String> createEventCodeSystemMap() {
        eventCodeSystemMap = new HashMap();
        eventCodeSystemMap.put("PatientDiscovery", "DCM");
        return eventCodeSystemMap;
    }

    public String getEventCodeSystem(String serviceName) {
        if (eventCodeSystemMap.containsKey(serviceName)) {
            return eventCodeSystemMap.get(serviceName);
        }
        return null;
    }

    private Map<String, String> createEventDisplayNameRequestorMap() {
        eventDisplayNameRequestorMap = new HashMap();
        eventDisplayNameRequestorMap.put("PatientDiscovery", "Query");
        return eventDisplayNameRequestorMap;
    }

    public String getEventDisplayNameRequestor(String serviceName) {
        if (eventDisplayNameRequestorMap.containsKey(serviceName)) {
            return eventDisplayNameRequestorMap.get(serviceName);
        }
        return null;
    }

    private Map<String, String> createEventDisplayNameResponderMap() {
        eventDisplayNameResponderMap = new HashMap();
        eventDisplayNameResponderMap.put("PatientDiscovery", "Query");
        return eventDisplayNameResponderMap;
    }

    public String getEventDisplayNameResponder(String serviceName) {
        if (eventDisplayNameResponderMap.containsKey(serviceName)) {
            return eventDisplayNameResponderMap.get(serviceName);
        }
        return null;
    }

    private Map<String, String> createEventTypeCodeMap() {
        eventTypeCodeMap = new HashMap();
        eventTypeCodeMap.put("PatientDiscovery", "ITI-55");
        return eventTypeCodeMap;
    }

    public String getEventTypeCode(String serviceName) {
        if (eventTypeCodeMap.containsKey(serviceName)) {
            return eventTypeCodeMap.get(serviceName);
        }
        return null;
    }

    private Map<String, String> createEventTypeCodeSystemMap() {
        eventTypeCodeSystemMap = new HashMap();
        eventTypeCodeSystemMap.put("PatientDiscovery", "IHE Transactions");
        return eventTypeCodeSystemMap;
    }

    public String getEventTypeCodeSystem(String serviceName) {
        if (eventTypeCodeSystemMap.containsKey(serviceName)) {
            return eventTypeCodeSystemMap.get(serviceName);
        }
        return null;
    }

    private Map<String, String> createEventTypeCodeDisplayNameMap() {
        eventTypeCodeDisplayNameMap = new HashMap();
        eventTypeCodeDisplayNameMap.put("PatientDiscovery", "Cross Gateway Patient Discovery");
        return eventTypeCodeDisplayNameMap;
    }

    public String getEventTypeCodeDisplayName(String serviceName) {
        if (eventTypeCodeDisplayNameMap.containsKey(serviceName)) {
            return eventTypeCodeDisplayNameMap.get(serviceName);
        }
        return null;
    }

    private Map<String, String> createEventActionCodeRequestorMap() {
        eventActionCodeRequestorMap = new HashMap();
        eventActionCodeRequestorMap.put("PatientDiscovery", "E");
        return eventActionCodeRequestorMap;
    }

    public String getEventActionCodeRequestor(String serviceName) {
        if (eventActionCodeRequestorMap.containsKey(serviceName)) {
            return eventActionCodeRequestorMap.get(serviceName);
        }
        return null;
    }

    private Map<String, String> createEventActionCodeResponderMap() {
        eventActionCodeResponderMap = new HashMap();
        eventActionCodeResponderMap.put("PatientDiscovery", "E");
        return eventActionCodeResponderMap;
    }

    public String getEventActionCodeResponder(String serviceName) {
        if (eventActionCodeResponderMap.containsKey(serviceName)) {
            return eventActionCodeResponderMap.get(serviceName);
        }
        return null;
    }

    private static class AuditTransformDataBuilderHolder {

        private static final AuditTransformDataBuilder INSTANCE = new AuditTransformDataBuilder();
    }

    /**
     *
     * @return
     */
    public static AuditTransformDataBuilder getInstance() {
        return AuditTransformDataBuilderHolder.INSTANCE;
    }
}
