package gov.hhs.fha.nhinc.event;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class EventDescriptionJSONWriterTest {

    @Test
    public void testSimpleCase() throws JSONException {

        EventDescriptionJSONDecorator jsonDescription = new EventDescriptionJSONDecorator(description);

        JSONAssert.assertEquals(expectedJSON, jsonDescription.toJSONString(), false);
    }

    public String expectedJSON = "{\"action\":\"myAction\",\"response_ids\":[\"111-111-1111\",\"222-222-22222\"]}";

    public EventDescription description = new EventDescription() {
        @Override
        public String getTransactionId() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getTimeStamp() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<String> getStatuses() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getServiceType() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<String> getResponseMsgIdList() {
            // TODO Auto-generated method stub
            return Arrays.asList("111-111-1111", "222-222-22222");
        }

        @Override
        public List<String> getRespondingHCIDs() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<String> getPayloadTypes() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<String> getPayloadSizes() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getNPI() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getMessageId() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getInitiatingHCID() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<String> getErrorCodes() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getAction() {
            // TODO Auto-generated method stub
            return "myAction";
        }
    };
}
