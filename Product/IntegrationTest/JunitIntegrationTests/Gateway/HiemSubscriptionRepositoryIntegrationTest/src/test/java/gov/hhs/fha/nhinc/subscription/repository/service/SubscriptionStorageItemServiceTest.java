package gov.hhs.fha.nhinc.subscription.repository.service;

import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionStorageItem;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Test class for SubscriptionStorageItemService.
 *
 * @author Neil Webb
 */
public class SubscriptionStorageItemServiceTest
{
    private static final String SUBSCRIPTION_ID = "402880c120d3ac0d0120d3ac00010001";
    static final String SUBSCRIBE_XML = "subscribe xml";
    static final String SUBSCRIPTION_REFERENCE_XML =
"<b:SubscriptionReference xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:b=\"http://docs.oasis-open.org/wsn/b-2\">" +
"  <wsa:Address>https://158.147.185.174:8181/CONNECTGateway/NhinService/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
"  <wsa:ReferenceParameters>" +
"	 <nhin:SubscriptionId xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\">" + SUBSCRIPTION_ID + "</nhin:SubscriptionId>" +
"  </wsa:ReferenceParameters>" +
"</b:SubscriptionReference>";
    static final String ROOT_TOPIC = "doc";
    private static final String PARENT_SUBSCRIPTION_ID = "777666555";
    static final String PARENT_SUBSCRIPTION_REFERENCE_XML = "parent sub ref";
    static final String CONSUMER = "consumer1";
    static final String PRODUCER = "producer1";
    private static final String PATIENT_ID = "patient123";
    private static final String PATIENT_ASSIGNING_AUTHORITY = "1.1.1";
    static final String TARGETS = "targets";
    static final Date CREATION_DATE;

    static
    {
        Date creationDate = null;
        try
        {
            creationDate = new SimpleDateFormat("YYYYMMdd").parse("20080304");
        }
        catch(Throwable t)
        {
            //eat
        }
        CREATION_DATE = creationDate;
    }

    @Test
    public void testEmptyDatabase()
    {
        System.out.println("Start testEmptyDatabase");
        try
        {
            SubscriptionStorageItemService service = new SubscriptionStorageItemService();

            SubscriptionStorageItem subscriptionItem = createBaseSubscriptionItem();
            service.save(subscriptionItem);

            // Verify count
            assertTrue("Subscription repository was empty", (service.subscriptionCount() > 0));

            // Empty repository
            service.emptyRepository();
            assertEquals("Subscription repository not empty", 0, service.subscriptionCount());
        }
        catch(Throwable t)
        {
            System.out.println("Error in testStorage():" + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testEmptyDatabase");
    }

    @Test
    public void testStorage()
    {
        System.out.println("Start testStorage");
        try
        {
            String recordId = null;
            SubscriptionStorageItemService service = new SubscriptionStorageItemService();

            // Empty the repository to start in a clean state
            service.emptyRepository();
            assertEquals("Subscription repository not empty", 0, service.subscriptionCount());

            SubscriptionStorageItem subscriptionItem = createBaseSubscriptionItem();
            service.save(subscriptionItem);

            // Validate identifier
            recordId = subscriptionItem.getRecordId();
            assertNotNull("Record id was null", recordId);

            // Retrieval
            subscriptionItem = service.findById(recordId);
            assertNotNull("Retrieved subscription was null", subscriptionItem);

            assertEquals("Subscription id",SUBSCRIPTION_ID, subscriptionItem.getSubscriptionId());
            assertEquals("Subscribe xml",SUBSCRIBE_XML,subscriptionItem.getSubscribeXML());
            assertEquals("Subscription reference xml",SUBSCRIPTION_REFERENCE_XML ,subscriptionItem.getSubscriptionReferenceXML());
            assertEquals("Root topic",ROOT_TOPIC ,subscriptionItem.getRootTopic());
            assertEquals("Parent subscription id",PARENT_SUBSCRIPTION_ID ,subscriptionItem.getParentSubscriptionId());
            assertEquals("Parent subscription reference id",PARENT_SUBSCRIPTION_REFERENCE_XML ,subscriptionItem.getParentSubscriptionReferenceXML());
            assertEquals("Consumer",CONSUMER ,subscriptionItem.getConsumer());
            assertEquals("Producer",PRODUCER ,subscriptionItem.getProducer());
            assertEquals("Patient ID",PATIENT_ID ,subscriptionItem.getPatientId());
            assertEquals("Patient assigning authority",PATIENT_ASSIGNING_AUTHORITY ,subscriptionItem.getPatientAssigningAuthority());
            assertEquals("Targets",TARGETS ,subscriptionItem.getTargets());
            assertEquals("Creation date",CREATION_DATE ,subscriptionItem.getCreationDate());

            // Retrieval by subscription id
            List<SubscriptionStorageItem> subscriptions = service.findBySubscriptionId(SUBSCRIPTION_ID);
            assertNotNull("Retrieved subscription list by subscription id was null", subscriptions);
            assertFalse("Subscription list retrieved by subscription id was empty", subscriptions.isEmpty());

            // Retrieve by subscriptionReference
//            String subRefXml =
//"<b:SubscriptionReference xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:b=\"http://docs.oasis-open.org/wsn/b-2\">" +
//"  <wsa:Address>https://158.147.185.174:8181/CONNECTGateway/NhinService/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
//"  <wsa:ReferenceParameters>" +
//"	 <nhin:SubscriptionId xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\">" + SUBSCRIPTION_ID + "</nhin:SubscriptionId>" +
//"  </wsa:ReferenceParameters>" +
//"</b:SubscriptionReference>";
//            subscriptionItem = service.retreiveBySubscriptionReference(subRefXml);
            subscriptionItem = service.retrieveByLocalSubscriptionReference(SUBSCRIPTION_REFERENCE_XML);
            assertNotNull("Retrieved subscription by subscription reference was null", subscriptionItem);

            // Count
            int subscriptionCount = service.subscriptionCount();
            assertTrue("Subscription count less than one", (subscriptionCount > 0));

            // Delete
            service.delete(subscriptionItem);

            // Find by id to verify delete
            subscriptionItem = service.findById(recordId);
            assertNull("Subscription was not null after delete", subscriptionItem);

        }
        catch(Throwable t)
        {
            System.out.println("Error in testStorage():" + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testStorage");
    }

    //@Test
    public void testParseSubscriptionId()
    {
        System.out.println("Begin testParseSubscriptionId");
        try
        {
            String subRefXml =
"<b:SubscriptionReference xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:b=\"http://docs.oasis-open.org/wsn/b-2\">" +
"  <wsa:Address>https://158.147.185.174:8181/CONNECTGateway/NhinService/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
"  <wsa:ReferenceParameters>" +
"	 <nhin:SubscriptionId xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\">50b564b2:11ff766dd39:-7fb5</nhin:SubscriptionId>" +
"  </wsa:ReferenceParameters>" +
"</b:SubscriptionReference>";
            SubscriptionStorageItemService service = new SubscriptionStorageItemService();
            String subscriptionId = SubscriptionIdHelper.extractSubscriptionIdFromSubscriptionReferenceXml(subRefXml) ;
            assertEquals("Subscription id was incorrect", "50b564b2:11ff766dd39:-7fb5", subscriptionId);
        }
        catch(Throwable t)
        {
            System.out.println("Exception encountered in testParseSubscriptionId: " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testParseSubscriptionId");
    }

    //@Test
    public void testParseSubscriptionIdw()
    {
        System.out.println("Begin testParseSubscriptionId");
        try
        {
            String subRefXml =
"<SubscriptionReference>" +
"  <Address>https://158.147.185.174:8181/CONNECTGateway/NhinService/SubscriptionManagerService/HiemUnsubscribe</Address>" +
"  <ReferenceParameters>" +
"	 <SubscriptionId>50b564b2:11ff766dd39:-7fb5</SubscriptionId>" +
"  </ReferenceParameters>" +
"</SubscriptionReference>";
            SubscriptionStorageItemService service = new SubscriptionStorageItemService();
            String subscriptionId = SubscriptionIdHelper.extractSubscriptionIdFromSubscriptionReferenceXml(subRefXml)  ;
            assertEquals("Subscription id was incorrect", "50b564b2:11ff766dd39:-7fb5", subscriptionId);
        }
        catch(Throwable t)
        {
            System.out.println("Exception encountered in testParseSubscriptionId: " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testParseSubscriptionId");
    }

    private SubscriptionStorageItem createBaseSubscriptionItem()
    {
        SubscriptionStorageItem subscriptionItem = new SubscriptionStorageItem();
        subscriptionItem.setSubscriptionId(SUBSCRIPTION_ID);
        subscriptionItem.setSubscribeXML(SUBSCRIBE_XML);
        subscriptionItem.setSubscriptionReferenceXML(SUBSCRIPTION_REFERENCE_XML);
        subscriptionItem.setRootTopic(ROOT_TOPIC);
        subscriptionItem.setParentSubscriptionId(PARENT_SUBSCRIPTION_ID);
        subscriptionItem.setParentSubscriptionReferenceXML(PARENT_SUBSCRIPTION_REFERENCE_XML);
        subscriptionItem.setConsumer(CONSUMER);
        subscriptionItem.setProducer(PRODUCER);
        subscriptionItem.setPatientId(PATIENT_ID);
        subscriptionItem.setPatientAssigningAuthority(PATIENT_ASSIGNING_AUTHORITY);
        subscriptionItem.setTargets(TARGETS);
        subscriptionItem.setCreationDate(CREATION_DATE);
        return subscriptionItem;
    }

}