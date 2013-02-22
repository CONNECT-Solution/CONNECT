package gov.hhs.fha.nhinc.docrepository.adapter.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EventCodeParamTest {

	private EventCodeParam getNewEventCodeParam(){
		EventCodeParam eventCodeParam = new EventCodeParam();
		
		String eventCode = "event code";
		String eventCodeScheme = "event code scheme";
		
		eventCodeParam.setEventCode(eventCode);
		eventCodeParam.setEventCodeScheme(eventCodeScheme);
		
		return eventCodeParam;
	}
	
	@Test
	public void equalsTest(){
		EventCodeParam param1 = getNewEventCodeParam();
		EventCodeParam param2 = getNewEventCodeParam();
		
		assertTrue(param1.equals(param2));
	}
	
	@Test
	public void hashCodeTest(){
		String eventCode = "event code";
		EventCodeParam eventCodeParam = new EventCodeParam();
		eventCodeParam.setEventCode(eventCode);
		
		assertEquals(eventCodeParam.hashCode(), eventCode.hashCode());
	}
}
