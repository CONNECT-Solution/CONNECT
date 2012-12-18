package gov.hhs.fha.nhinc.event;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

public class EventDescriptionJSONDecorator implements JSONString, EventDescription {

    private static final String ACTION = "action";
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

    private static final Logger LOG = Logger.getLogger(EventDescriptionJSONDecorator.class);

    public EventDescriptionJSONDecorator(EventDescription description) {
        this.description = description;
        this.jsonObject = new JSONObject();
        constructJSONObject();
    }

    private void constructJSONObject() {
        try {
            addMessageId(description);
            addResponseMsgId(description);
            addAction(description);
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

    private void addAction(EventDescription description) throws JSONException {
        addToJSON(ACTION, description.getAction());
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
    public String getAction() {
        return description.getAction();
    }

    @Override
    public List<String> getResponseMsgIdList() {
        return description.getResponseMsgIdList();
    }

}