package gov.hhs.fha.nhinc.subscription.repository.service;

import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import java.util.List;
import org.junit.Ignore;
import org.w3._2005._08.addressing.EndpointReferenceType;

/**
 *
 * @author Neil Webb
 */
@Ignore
public class SubscriptionItemServiceTest {

    @Test
    public void placeholdertest() {
    }

    //@Test
    public void testRead() {
        System.out.println("Begin testRead");
        try {
            HiemSubscriptionRepositoryService service = new HiemSubscriptionRepositoryService();
            service.emptyRepository();
            assertEquals("Subscription repository was not empty before start.", 0, service.subscriptionCount());

            HiemSubscriptionItem subscriptionItem = createBaseSubscriptionItem();
//            String subRefXml =
//"<b:SubscriptionReference xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:b=\"http://docs.oasis-open.org/wsn/b-2\">" +
//"  <wsa:Address>https://158.147.185.174:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
//"  <wsa:ReferenceParameters>" +
//"	 <nhin:SubscriptionId xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\">402880c120d3ac0d0120d3ac0e230001</nhin:SubscriptionId>" +
//"  </wsa:ReferenceParameters>" +
//"</b:SubscriptionReference>";
            String subRefXml =
                    "<b:SubscriptionReference xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:b=\"http://docs.oasis-open.org/wsn/b-2\">" +
                    "  <wsa:Address>https://158.147.185.174:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                    "  <wsa:ReferenceParameters>" +
                    "	 <nhin:SubscriptionId xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\">402880c120d3ac0d0120d3ac00010001</nhin:SubscriptionId>" +
                    "  </wsa:ReferenceParameters>" +
                    "</b:SubscriptionReference>";
            EndpointReferenceType subscriptionReference = service.saveSubscriptionToConnect(subscriptionItem);
            assertNotNull("Subscription reference was null", subscriptionReference);

//            subscriptionItem = service.retreiveBySubscriptionReference(SubscriptionStorageItemServiceTest.SUBSCRIPTION_REFERENCE_XML);
            subscriptionItem = service.retrieveByLocalSubscriptionReference(subRefXml);
            assertNotNull("Subscription item was null", subscriptionItem);
        } catch (Throwable t) {
            System.out.println("Exception encountered in testRead: " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testRead");
    }

//    @Test
//    public void testReadByParentSubRef()
//    {
//        System.out.println("Begin testReadByParentSubRef");
//        try
//        {
//            String subRefXml =
//"<b:SubscriptionReference xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:b=\"http://docs.oasis-open.org/wsn/b-2\">" +
//"  <wsa:Address>https://158.147.185.174:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
//"  <wsa:ReferenceParameters>" +
//"	 <nhin:SubscriptionId xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\">777666555</nhin:SubscriptionId>" +
//"  </wsa:ReferenceParameters>" +
//"</b:SubscriptionReference>";
//            SubscriptionRepositoryService service = new SubscriptionRepositoryService();
//            List<SubscriptionItem> subscriptionItems = service.retrieveByParentSubscriptionReference(subRefXml);
//            assertNotNull("Subscription item list was null", subscriptionItems);
//            assertFalse("Empty response", subscriptionItems.isEmpty());
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Exception encountered in testReadByParentSubRef: " + t.getMessage());
//            t.printStackTrace();
//            fail(t.getMessage());
//        }
//        System.out.println("End testReadByParentSubRef");
//    }
    private HiemSubscriptionItem createBaseSubscriptionItem() {
        HiemSubscriptionItem subscriptionItem = new HiemSubscriptionItem();
//        subscriptionItem.setSubscribeXML(SubscriptionStorageItemServiceTest.SUBSCRIBE_XML);
//        subscriptionItem.setSubscriptionReferenceXML(SubscriptionStorageItemServiceTest.SUBSCRIPTION_REFERENCE_XML);
//        subscriptionItem.setRootTopic(SubscriptionStorageItemServiceTest.ROOT_TOPIC);
//        subscriptionItem.setParentSubscriptionReferenceXML(SubscriptionStorageItemServiceTest.PARENT_SUBSCRIPTION_REFERENCE_XML);
//        subscriptionItem.setConsumer(SubscriptionStorageItemServiceTest.CONSUMER);
//        subscriptionItem.setProducer(SubscriptionStorageItemServiceTest.PRODUCER);
//        subscriptionItem.setTargets(SubscriptionStorageItemServiceTest.TARGETS);
//        subscriptionItem.setCreationDate(SubscriptionStorageItemServiceTest.CREATION_DATE);
        return subscriptionItem;
    }
}