/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.dte;

import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import gov.hhs.fha.nhinc.xmlCommon.XpathHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import static org.junit.Assert.*;

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
        Element builtNotify = builder.buildNotifyFromSubscribe(notificationMessage, subscriptionReference);

        assertNotNull(builtNotify);
        assertEquals("Notify", builtNotify.getLocalName());

        Element builtNotificationMessage = XmlUtility.getSingleChildElement(builtNotify, "http://docs.oasis-open.org/wsn/b-2", "NotificationMessage");
        assertNotNull(builtNotificationMessage);
        assertNotNull(XmlUtility.getSingleChildElement(builtNotificationMessage, "http://docs.oasis-open.org/wsn/b-2", "Topic"));
        assertNotNull(XmlUtility.getSingleChildElement(builtNotificationMessage, "http://docs.oasis-open.org/wsn/b-2", "Message"));

        Element builtSubscriptionReference = XmlUtility.getSingleChildElement(builtNotificationMessage, "http://docs.oasis-open.org/wsn/b-2", "SubscriptionReference");
        assertNotNull(builtSubscriptionReference);
    }
}