/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.dte;

import static org.junit.Assert.assertNotNull;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class NotifyBuilderTest {

    public NotifyBuilderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void BuildNotifyFromSubscribe() throws Exception {
        String subcriptionReferenceXml = "<?xml version='1.0' encoding='UTF-8'?> " +
                "<wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'> " +
                "  <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address> " +
                "  <wsa:ReferenceParameters> " +
                "    <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'>9a8e8cf0-633f-4237-ad93-a56ed65e1a02</nhin:SubscriptionId> " +
                "  </wsa:ReferenceParameters> " +
                "</wsnt:SubscriptionReference> ";
        Element subscriptionReference = XmlUtility.convertXmlToElement(subcriptionReferenceXml);

        String notificationMessageXml = "" +
                "<wsnt:NotificationMessage xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'> " +
                "   <wsnt:Topic Dialect='http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple' xmlns:nhin='urn:nhin'>SomeTopic</wsnt:Topic> " +
                "   <wsnt:Message> " +
                "      <myPrefix:MyData xmlns:myPrefix='urn:someone'> " +
                "      	<myPrefix:Stuff>hello world</myPrefix:Stuff> " +
                "      </myPrefix:MyData> " +
                "   </wsnt:Message> " +
                "</wsnt:NotificationMessage>";
        Element notificationMessage = XmlUtility.convertXmlToElement(notificationMessageXml);

        NotifyBuilder builder = new NotifyBuilder();
        Notify builtNotify = builder.buildNotifyFromSubscribe(notificationMessage, subscriptionReference);
        
        assertNotNull(builtNotify);
        
        List<NotificationMessageHolderType> builtNotificationMessage = builtNotify.getNotificationMessage();
        assertNotNull(builtNotificationMessage);
        
        assertNotNull(builtNotificationMessage.get(0).getTopic());
        assertNotNull(builtNotificationMessage.get(0).getMessage());
        assertNotNull(builtNotificationMessage.get(0).getSubscriptionReference());
    }
}