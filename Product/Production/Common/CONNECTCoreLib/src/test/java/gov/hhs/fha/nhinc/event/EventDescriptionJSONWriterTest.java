package gov.hhs.fha.nhinc.event;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class EventDescriptionJSONWriterTest {

    
    @Test
    public void testSimpleCase() {
        
        EventDescriptionJSONDecorator jsonDescription =  new EventDescriptionJSONDecorator(description);
        
        assertEquals("the generate json isn't correct", expectedJSON, jsonDescription.toJSONString());
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
        public String getStatus() {
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
        public String getRespondingHCID() {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public String getPayloadType() {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public String getPayloadSize() {
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
        public String getErrorCode() {
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
