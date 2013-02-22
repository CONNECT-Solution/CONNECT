package gov.hhs.fha.nhinc.docrepository.adapter.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCodeParam;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

public class EventCodeDaoTest {

	private final Session session = mock(Session.class);
	private Transaction transaction;
	private EventCodeDao eventCodeDao;
	private SessionFactory sessionFactory;
	
	@Before
	public void setUp(){
		transaction = mock(Transaction.class);
		sessionFactory = mock(SessionFactory.class);
		
		eventCodeDao = new EventCodeDao(){
			@Override
			protected Session getSession(SessionFactory sessionFactory){
				return session;
			}
			
			@Override
			protected SessionFactory getSessionFactory(){
				return sessionFactory;
			}
		};
		
		when(session.beginTransaction()).thenReturn(transaction);
	}
	
	@Test
	public void deleteTest(){
		EventCode eventCode = new EventCode();
		
		eventCodeDao.delete(eventCode);
		
		verify(session).delete(eventCode);
	}
	
	@Test
	public void eventCodeQueryTest(){
		List<EventCode> eventCodeList = new ArrayList<EventCode>();
		EventCode eventCode = new EventCode();
		final long EVENT_CODE_ID = 12345;
		eventCode.setEventCodeId(EVENT_CODE_ID);
		eventCodeList.add(eventCode);
		EventCodeParam eventCodeParam = new EventCodeParam();
		eventCodeParam.setEventCode("Event Code");
		eventCodeParam.setEventCodeScheme("Event Code Scheme");
		Criteria criteria = mock(Criteria.class);
		
		when(session.createCriteria(EventCode.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(eventCodeList);
		
		List<EventCode> resultEventCodes = eventCodeDao.eventCodeQuery(eventCodeParam);
		
		assertEquals(resultEventCodes.size(), 1);
		assertEquals(resultEventCodes.get(0).getEventCodeId(), 
				(Long) EVENT_CODE_ID);
		
	}
}
