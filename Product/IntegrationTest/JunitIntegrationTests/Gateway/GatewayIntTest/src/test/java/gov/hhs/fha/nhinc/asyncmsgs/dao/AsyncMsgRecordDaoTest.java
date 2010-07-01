/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.asyncmsgs.dao;

import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author JHOPPESC
 */
public class AsyncMsgRecordDaoTest {

    public AsyncMsgRecord rec1 = new AsyncMsgRecord();
    public AsyncMsgRecord rec2 = new AsyncMsgRecord();
    public AsyncMsgRecord rec3 = new AsyncMsgRecord();
    public List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<AsyncMsgRecord>();

    public AsyncMsgRecordDaoTest() {
        rec1.setMessageId("uuid:1234567890");
        rec1.setCreationTime(new Date());
        rec1.setServiceName("PatientDiscovery");
        rec2.setMessageId("uuid:1111111111");
        rec2.setCreationTime(new Date());
        rec2.setServiceName("PatientDiscovery");
        rec3.setMessageId("uuid:2222222222");
        rec3.setCreationTime(new Date());
        rec3.setServiceName("PatientDiscovery");

        asyncMsgRecs.add(rec1);
        asyncMsgRecs.add(rec2);
        asyncMsgRecs.add(rec3);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.setProperty("nhinc.properties.dir", System.getenv("NHINC_PROPERTIES_DIR"));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of insertRecords method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testInsertRecords() {
        System.out.println("testInsertRecords");

        System.out.println("Inserting " + asyncMsgRecs.size() + " into the database");

        AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

        init(instance);

        boolean expResult = true;

        boolean result = instance.insertRecords(asyncMsgRecs);

        assertEquals(expResult, result);
    }

    /**
     * Test of queryByMessageId method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testQueryByMessageId() {
        System.out.println("testQueryByMessageId");

        String messageId = "uuid:1111111111";
        AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

        System.out.println("Attempting to retrieve rec2");
        List<AsyncMsgRecord> result = instance.queryByMessageId(messageId);

        assertEquals(1, result.size());
        assertEquals(rec2.getMessageId(), result.get(0).getMessageId());
        assertEquals(rec2.getServiceName(), result.get(0).getServiceName());

    }
    /**
     * Test of queryByMessageIdAndServiceName method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testQueryByMessageIdAndServiceName() {
        System.out.println("queryByMessageIdAndServiceName");
        String messageId = "uuid:1234567890";
        String serviceName = "PatientDiscovery";
        AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

        List<AsyncMsgRecord> result = instance.queryByMessageIdAndServiceName(messageId, serviceName);
        assertEquals(1, result.size());
        assertEquals(rec1.getMessageId(), result.get(0).getMessageId());
        assertEquals(rec1.getServiceName(), result.get(0).getServiceName());
    }

    /**
     * Test of queryByTime method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testQueryByTime() {
        System.out.println("queryByTime");

        AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

        List<AsyncMsgRecord> result = instance.queryByTime(new Date());
        assertEquals(3, result.size());
    }

    /**
     * Test of delete method, of class AsyncMsgRecordDao.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");

        AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

        List<AsyncMsgRecord> result = instance.queryByMessageId(rec1.getMessageId());
        assertEquals(1, result.size());
        instance.delete(result.get(0));
        result = instance.queryByMessageId(rec1.getMessageId());
        assertEquals(0, result.size());

        result = instance.queryByMessageId(rec2.getMessageId());
        assertEquals(1, result.size());
        instance.delete(result.get(0));
        result = instance.queryByMessageId(rec2.getMessageId());
        assertEquals(0, result.size());

        result = instance.queryByMessageId(rec3.getMessageId());
        assertEquals(1, result.size());
        instance.delete(result.get(0));
        result = instance.queryByMessageId(rec3.getMessageId());
        assertEquals(0, result.size());
    }

    private void init (AsyncMsgRecordDao dao) {
        List<AsyncMsgRecord> result = dao.queryByTime(new Date());

        for (AsyncMsgRecord rec : result) {
            dao.delete(rec);
        }
    }
}
