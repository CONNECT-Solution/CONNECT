package gov.hhs.fha.nhinc.event;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

public class EventDescriptionJSONDecorator implements JSONString, EventDescription {

    private static final String ACTION = "action";
    private static final String RESPONSE_ID = "response_ids";

    private EventDescription description;
    private JSONObject jsonObject;

    private static Log log = LogFactory.getLog(EventDescriptionJSONDecorator.class);

    public EventDescriptionJSONDecorator(EventDescription description) {
        this.description = description;
        this.jsonObject = new JSONObject();
        constructJSONObject();
    }

    private void constructJSONObject() {
        try {
            addResponseMsgId(description);
            addAction(description);
        } catch (JSONException e) {
            log.error("failed to serialize event description as JSON", e);
        }
    }

    private void addResponseMsgId(EventDescription description) throws JSONException {
        jsonObject.put(RESPONSE_ID, description.getResponseMsgIdList());
    }

    private void addAction(EventDescription description) throws JSONException {
        jsonObject.put(ACTION, description.getAction());
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