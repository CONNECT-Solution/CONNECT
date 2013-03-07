/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.asyncmsgs.dao;

import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.QueryDeferredQueueRequestType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test class for AsyncMsgRecordDao
 *
 * @author nsubramanyan
 */
public class AsyncMsgRecordDaoTest {

    public AsyncMsgRecordDaoTest() {
    }
    private final Session session = mock(Session.class);
    private Transaction transaction;
    private AsyncMsgRecordDao asyncMsgRecordDao;

    @Before
    public void setUp() {
        transaction = mock(Transaction.class);
        asyncMsgRecordDao = new AsyncMsgRecordDao() {
            @Override
            protected Session getSession() {
                return session;
            }
        };
        when(session.beginTransaction()).thenReturn(transaction);
    }

    /**
     * Test of queryByMessageIdAndDirection method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testQueryByMessageIdAndDirection() {
        System.out.println("queryByMessageIdAndDirection");
        //insert two AsyncMsgRecord records with MessageID and Direction
        //Row 1
        AsyncMsgRecord asyncMsgRecord1 = new AsyncMsgRecord();
        final String MESSAGEID_1 = "111";
        asyncMsgRecord1.setMessageId(MESSAGEID_1);
        final String DIRECTION_1 = "Right";
        asyncMsgRecord1.setDirection(DIRECTION_1);
        final String COMMUNITYID_1 = "1.1";
        asyncMsgRecord1.setCommunityId(COMMUNITYID_1);
        //Row 2
        AsyncMsgRecord asyncMsgRecord2 = new AsyncMsgRecord();
        asyncMsgRecord2.setMessageId(MESSAGEID_1);
        asyncMsgRecord2.setDirection(DIRECTION_1);
        final String COMMUNITYID_2 = "2.2";
        asyncMsgRecord2.setCommunityId(COMMUNITYID_2);
        //create the asyncMsgRecord list
        List<AsyncMsgRecord> asyncRecords = new ArrayList<AsyncMsgRecord>();
        asyncRecords.add(asyncMsgRecord1);
        asyncRecords.add(asyncMsgRecord2);

        Query query = mock(Query.class);
        when(session.getNamedQuery(Mockito.anyString())).thenReturn(query);
        when(query.list()).thenReturn(asyncRecords);

        //Criteria criteria = mock(Criteria.class);
        //when(session.createCriteria(AsyncMsgRecord.class)).thenReturn(criteria);
        //when(session.createQuery(Mockito.anyString())).thenReturn(query);
        List<AsyncMsgRecord> resultRows = asyncMsgRecordDao.queryByMessageIdAndDirection(MESSAGEID_1, DIRECTION_1);
        assertEquals(resultRows.size(), 2);
        assertEquals(resultRows.get(0).getCommunityId(), COMMUNITYID_1);
        assertEquals(resultRows.get(1).getCommunityId(), COMMUNITYID_2);
    }

    /**
     * Test of queryByMessageIdAndServiceName method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testQueryByMessageIdAndServiceName() {
        System.out.println("queryByMessageIdAndServiceName");
        //insert a AsyncMsgRecord record with MessageID and Service Name
        AsyncMsgRecord asyncMsgRecord = new AsyncMsgRecord();
        final String MESSAGEID = "111";
        asyncMsgRecord.setMessageId(MESSAGEID);
        final String SERVICE_NAME = "PatientDiscovery";
        asyncMsgRecord.setDirection(SERVICE_NAME);
        //create the asyncMsgRecord list
        List<AsyncMsgRecord> asyncRecords = new ArrayList<AsyncMsgRecord>();
        asyncRecords.add(asyncMsgRecord);

        //crete the Query for Named Query used in the service
        Query query = mock(Query.class);
        when(session.getNamedQuery(Mockito.anyString())).thenReturn(query);
        when(query.list()).thenReturn(asyncRecords);
        //test the service
        List resultRows = asyncMsgRecordDao.queryByMessageIdAndServiceName(MESSAGEID, SERVICE_NAME);
        assertEquals(resultRows.size(), 1);
    }

    /**
     * Test of queryByTime method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testQueryByTime() {
        System.out.println("queryByTime");
        //create the asyncMsgRecord list
        List<AsyncMsgRecord> asyncRecords = new ArrayList<AsyncMsgRecord>();

        //create 4 records 
        //row 1
        AsyncMsgRecord asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecord.setMessageId("1.1");
        asyncMsgRecord.setDirection("Sevice1");
        asyncMsgRecord.setCreationTime(new Date());
        asyncRecords.add(asyncMsgRecord);
        //row2
        asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecord.setMessageId("2.1");
        asyncMsgRecord.setDirection("Sevice2");
        asyncMsgRecord.setCreationTime(new Date());
        asyncRecords.add(asyncMsgRecord);
        //row3
        asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecord.setMessageId("3.1");
        asyncMsgRecord.setDirection("Sevice3");
        asyncMsgRecord.setCreationTime(new Date());
        asyncRecords.add(asyncMsgRecord);
        //row4
        asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecord.setMessageId("4.1");
        asyncMsgRecord.setDirection("Sevice4");
        asyncMsgRecord.setCreationTime(new Date());
        asyncRecords.add(asyncMsgRecord);
        //pass this timestamp value to the service
        Date timestamp = new Date();
        //crete the Query for Named Query used in the service
        Query query = mock(Query.class);
        when(session.getNamedQuery(Mockito.anyString())).thenReturn(query);
        when(query.list()).thenReturn(asyncRecords);

        List returnedrows = asyncMsgRecordDao.queryByTime(timestamp);
        assertEquals(returnedrows.size(), 4);
    }

    /**
     * Test of queryForExpired method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testQueryForExpired() {
        System.out.println("queryForExpired");
        //create the asyncMsgRecord list
        List<AsyncMsgRecord> asyncRecords = new ArrayList<AsyncMsgRecord>();

        //create 4 records 
        //row 1
        AsyncMsgRecord asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecord.setMessageId("1.1");
        asyncMsgRecord.setDirection("Sevice1");
        asyncMsgRecord.setCreationTime(new Date());
        asyncRecords.add(asyncMsgRecord);
        //row2
        asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecord.setMessageId("2.1");
        asyncMsgRecord.setDirection("Sevice2");
        asyncMsgRecord.setCreationTime(new Date());
        asyncRecords.add(asyncMsgRecord);
        //row3
        asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecord.setMessageId("3.1");
        asyncMsgRecord.setDirection("Sevice3");
        asyncMsgRecord.setCreationTime(new Date());
        asyncRecords.add(asyncMsgRecord);
        //row4
        asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecord.setMessageId("4.1");
        asyncMsgRecord.setDirection("Sevice4");
        asyncMsgRecord.setCreationTime(new Date());
        asyncRecords.add(asyncMsgRecord);
        //pass this timestamp value to the service
        Date timestamp = new Date();
        //crete the Query for Named Query used in the service
        Query query = mock(Query.class);
        when(session.getNamedQuery(Mockito.anyString())).thenReturn(query);
        when(query.list()).thenReturn(asyncRecords);

        List returnedrows = asyncMsgRecordDao.queryForExpired(timestamp);
        assertEquals(returnedrows.size(), 4);
    }

    /**
     * Test of queryForDeferredQueueProcessing method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testQueryForDeferredQueueProcessing() {
        System.out.println("queryForDeferredQueueProcessing");
        //create the asyncMsgRecord list
        List<AsyncMsgRecord> asyncRecords = new ArrayList<AsyncMsgRecord>();

        //create 3 records 
        //row 1
        AsyncMsgRecord asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecord.setMessageId("1.1");
        asyncMsgRecord.setDirection("Sevice1");
        asyncMsgRecord.setCreationTime(new Date());
        asyncRecords.add(asyncMsgRecord);
        //row2
        asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecord.setMessageId("2.1");
        asyncMsgRecord.setDirection("Sevice2");
        asyncMsgRecord.setCreationTime(new Date());
        asyncRecords.add(asyncMsgRecord);
        //row3
        asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecord.setMessageId("3.1");
        asyncMsgRecord.setDirection("Sevice3");
        asyncMsgRecord.setCreationTime(new Date());
        asyncRecords.add(asyncMsgRecord);

        //crete the Query for Named Query used in the service
        Query query = mock(Query.class);
        when(session.getNamedQuery(Mockito.anyString())).thenReturn(query);
        when(query.list()).thenReturn(asyncRecords);

        List returnedRows = asyncMsgRecordDao.queryForDeferredQueueProcessing();
        assertEquals(asyncRecords, returnedRows);
    }

    /**
     * Test of queryForDeferredQueueSelected method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testQueryForDeferredQueueSelected() {
        System.out.println("queryForDeferredQueueSelected");
        //create the asyncMsgRecord list
        List<AsyncMsgRecord> asyncRecords = new ArrayList<AsyncMsgRecord>();

        //create 3 records 
        //row 1
        AsyncMsgRecord asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecord.setMessageId("1.1");
        asyncMsgRecord.setDirection("Sevice1");
        asyncMsgRecord.setCreationTime(new Date());
        asyncRecords.add(asyncMsgRecord);
        //row2
        asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecord.setMessageId("2.1");
        asyncMsgRecord.setDirection("Sevice2");
        asyncMsgRecord.setCreationTime(new Date());
        asyncRecords.add(asyncMsgRecord);

        //crete the Query object for the Named Query used in the service
        Query query = mock(Query.class);
        when(session.getNamedQuery(Mockito.anyString())).thenReturn(query);
        when(query.list()).thenReturn(asyncRecords);

        List returnedRows = asyncMsgRecordDao.queryForDeferredQueueSelected();
        assertEquals(asyncRecords, returnedRows);
    }

    /**
     * Test of queryByCriteria method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testQueryByCriteria() {
        System.out.println("queryByCriteria");
        QueryDeferredQueueRequestType queryCriteria = new QueryDeferredQueueRequestType();
        queryCriteria.setCreationBeginTime(long2Gregorian((new Date()).getTime()));
        queryCriteria.setCreationEndTime(long2Gregorian((new Date()).getTime()));
        queryCriteria.setDirection("Test");
        queryCriteria.setResponseBeginTime(long2Gregorian((new Date()).getTime()));
        queryCriteria.setResponseEndTime(long2Gregorian((new Date()).getTime()));
        queryCriteria.setResponseType("DQ");

        QueryDeferredQueueRequestType queryCriteria1 = new QueryDeferredQueueRequestType();

        //crete the Query object for the Named Query used in the service
        Query query = mock(Query.class);
        when(session.getNamedQuery(Mockito.anyString())).thenReturn(query);
        //create the asyncMsgRecord list
        List<AsyncMsgRecord> asyncRecords = new ArrayList<AsyncMsgRecord>();
        AsyncMsgRecord asyncMsgRecord1 = new AsyncMsgRecord();
        AsyncMsgRecord asyncMsgRecord2 = new AsyncMsgRecord();
        asyncRecords.add(asyncMsgRecord1);
        asyncRecords.add(asyncMsgRecord2);
        Criteria criteria = mock(Criteria.class);

        when(session.createCriteria(AsyncMsgRecord.class)).thenReturn(criteria);
        when(criteria.list()).thenReturn(asyncRecords);

        List<AsyncMsgRecord> result = asyncMsgRecordDao.queryByCriteria(queryCriteria);
        assertEquals(asyncRecords, result);
        //pass no values
        result = asyncMsgRecordDao.queryByCriteria(queryCriteria1);
        assertNull(result);
    }

    /**
     * Test of insertRecords method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testInsertRecords() {
        System.out.println("insertRecords");
        AsyncMsgRecord asyncMsgRecord1 = new AsyncMsgRecord();
        AsyncMsgRecord asyncMsgRecord2 = new AsyncMsgRecord();
        List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<AsyncMsgRecord>();
        asyncMsgRecs.add(asyncMsgRecord1);
        asyncMsgRecs.add(asyncMsgRecord2);
        boolean result = asyncMsgRecordDao.insertRecords(asyncMsgRecs);
        assertEquals(true, result);
    }

    /**
     * Test of save method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testSave_AsyncMsgRecord() {
        System.out.println("save");
        AsyncMsgRecord asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecordDao.save(asyncMsgRecord);
        verify(session).saveOrUpdate(asyncMsgRecord);
    }

    /**
     * Test of save method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testSave_List() {
        System.out.println("save");
        AsyncMsgRecord asyncMsgRecord1 = new AsyncMsgRecord();
        asyncMsgRecord1.setCommunityId("1.1");
        AsyncMsgRecord asyncMsgRecord2 = new AsyncMsgRecord();
        asyncMsgRecord2.setCommunityId("2.2");
        List<AsyncMsgRecord> asyncMsgRecords = new ArrayList<AsyncMsgRecord>();
        asyncMsgRecords.add(asyncMsgRecord1);
        asyncMsgRecords.add(asyncMsgRecord2);
        asyncMsgRecordDao.save(asyncMsgRecords);
        //loop through and verify if each record saved 
        for (int i = 0; i < asyncMsgRecords.size(); i++) {
            verify(session).saveOrUpdate(asyncMsgRecords.get(i));
        }
    }

    /**
     * Test of delete method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        AsyncMsgRecord asyncMsgRecord = new AsyncMsgRecord();
        asyncMsgRecordDao.delete(asyncMsgRecord);
        verify(session).delete(asyncMsgRecord);
    }

    /**
     * Test of checkExpiration method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testCheckExpiration() throws PropertyAccessException {
        System.out.println("checkExpiration");

        AsyncMsgRecord asyncMsgRecord1 = new AsyncMsgRecord();
        asyncMsgRecord1.setCommunityId("1.1");
        asyncMsgRecord1.setDuration(Long.valueOf(0));
        asyncMsgRecord1.setStatus(asyncMsgRecordDao.QUEUE_STATUS_EXPIRED);
        AsyncMsgRecord asyncMsgRecord2 = new AsyncMsgRecord();
        asyncMsgRecord2.setCommunityId("2.2");
        asyncMsgRecord2.setDuration(Long.valueOf(0));
        asyncMsgRecord2.setStatus(AsyncMsgRecordDao.QUEUE_STATUS_EXPIRED);

        final List<AsyncMsgRecord> asyncMsgRecords = new ArrayList<AsyncMsgRecord>();
        asyncMsgRecords.add(asyncMsgRecord1);
        asyncMsgRecords.add(asyncMsgRecord2);
        AsyncMsgRecordDao asyncMsgRecordDao = new AsyncMsgRecordDao() {
            @Override
            protected Session getSession() {
                return session;
            }

            @Override
            public List<AsyncMsgRecord> queryForExpired(Date timestamp) {
                return asyncMsgRecords;
            }

            @Override
            public void save(List<AsyncMsgRecord> asyncMsgRecs) {
                //do nothing
            }
        };
        asyncMsgRecordDao.checkExpiration();
        //verify if the rows are updated
        //loop through and verify if each record saved 
        for (int i = 0; i < asyncMsgRecords.size(); i++) {
            verify(session).saveOrUpdate(asyncMsgRecords.get(i));
        }
    }

    private static XMLGregorianCalendar long2Gregorian(long date) {
        XMLGregorianCalendar xmlCal = null;

        DatatypeFactory dataTypeFactory;
        try {
            dataTypeFactory = DatatypeFactory.newInstance();
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date);
            xmlCal = dataTypeFactory.newXMLGregorianCalendar(gc);
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        return xmlCal;
    }
}
