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
package gov.hhs.fha.nhinc.event;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventDescriptionJSONDecorator implements JSONString, EventDescription {

    private static final String VERSION = "version";
    private static final String RESPONSE_ID = "response_ids";
    private static final String MESSAGE_ID = "message_id";
    private static final String TIMESTAMP = "timestamp";
    private static final String SERVICE_TYPE = "service_type";
    private static final String TRANSACTION_ID = "transaction_id";
    private static final String PAYLOAD_TYPES = "payload_types";
    private static final String PAYLOAD_SIZES = "payload_sizes";
    private static final String INITIATING_HCID = "initiating_hcid";
    private static final String RESPONDING_HCIDS = "responding_hcids";
    private static final String NPI = "npi";
    private static final String STATUSES = "statuses";
    private static final String ERROR_CODES = "error_codes";

    private final EventDescription description;
    private final JSONObject jsonObject;

    private static final Logger LOG = LoggerFactory.getLogger(EventDescriptionJSONDecorator.class);

    public EventDescriptionJSONDecorator(EventDescription description) {
        this.description = description;
        jsonObject = new JSONObject();
        constructJSONObject();
    }

    private void constructJSONObject() {
        try {
            addMessageId(description);
            addResponseMsgId(description);
            addVersion(description);
            addTimeStamp(description);
            addServiceType(description);
            addTransactionId(description);
            addPayloadTypes(description);
            addPayloadSizes(description);
            addInitiatingHCID(description);
            addRespondingHCIDs(description);
            addNPI(description);
            addStatuses(description);
            addErrorCodes(description);
        } catch (JSONException e) {
            LOG.error("failed to serialize event description as JSON", e);
        }
    }

    private void addMessageId(EventDescription description) throws JSONException {
        addToJSON(MESSAGE_ID, description.getMessageId());
    }

    private void addResponseMsgId(EventDescription description) throws JSONException {
        addToJSON(RESPONSE_ID, description.getResponseMsgIdList());
    }

    private void addVersion(EventDescription description) throws JSONException {
        addToJSON(VERSION, description.getVersion());
    }

    private void addTimeStamp(EventDescription description) throws JSONException {
        addToJSON(TIMESTAMP, description.getTimeStamp());
    }

    private void addServiceType(EventDescription description) throws JSONException {
        addToJSON(SERVICE_TYPE, description.getServiceType());
    }

    private void addTransactionId(EventDescription description) throws JSONException {
        addToJSON(TRANSACTION_ID, description.getTransactionId());
    }

    private void addPayloadTypes(EventDescription description) throws JSONException {
        addToJSON(PAYLOAD_TYPES, description.getPayloadTypes());
    }

    private void addPayloadSizes(EventDescription description) throws JSONException {
        addToJSON(PAYLOAD_SIZES, description.getPayloadSizes());
    }

    private void addInitiatingHCID(EventDescription description) throws JSONException {
        addToJSON(INITIATING_HCID, description.getInitiatingHCID());
    }

    private void addRespondingHCIDs(EventDescription description) throws JSONException {
        addToJSON(RESPONDING_HCIDS, description.getRespondingHCIDs());
    }

    private void addNPI(EventDescription description) throws JSONException {
        addToJSON(NPI, description.getNPI());
    }

    private void addStatuses(EventDescription description) throws JSONException {
        addToJSON(STATUSES, description.getStatuses());
    }

    private void addErrorCodes(EventDescription description) throws JSONException {
        addToJSON(ERROR_CODES, description.getErrorCodes());
    }

    private void addToJSON(String key, Object value) throws JSONException {
        if (value != null) {
            jsonObject.put(key, value);
        }
    }

    @Override
    public String toJSONString() {
        return jsonObject.toString();
    }

    @Override
    public String getMessageId() {
        return description.getMessageId();
    }

    @Override
    public String getTimeStamp() {
        return description.getTimeStamp();
    }

    @Override
    public String getServiceType() {
        return description.getServiceType();
    }

    @Override
    public String getTransactionId() {
        return description.getTransactionId();
    }

    @Override
    public List<String> getPayloadTypes() {
        return description.getPayloadTypes();
    }

    @Override
    public List<String> getPayloadSizes() {
        return description.getPayloadSizes();
    }

    @Override
    public String getInitiatingHCID() {
        return description.getInitiatingHCID();
    }

    @Override
    public List<String> getRespondingHCIDs() {
        return description.getRespondingHCIDs();
    }

    @Override
    public String getNPI() {
        return description.getNPI();
    }

    @Override
    public List<String> getStatuses() {
        return description.getStatuses();
    }

    @Override
    public List<String> getErrorCodes() {
        return description.getErrorCodes();
    }

    @Override
    public String getVersion() {
        return description.getVersion();
    }

    @Override
    public List<String> getResponseMsgIdList() {
        return description.getResponseMsgIdList();
    }

}
