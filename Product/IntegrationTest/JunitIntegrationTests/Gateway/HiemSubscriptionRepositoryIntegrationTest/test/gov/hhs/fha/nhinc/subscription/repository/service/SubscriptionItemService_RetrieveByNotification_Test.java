/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subscription.repository.service;

import gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionItem;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3._2005._08.addressing.EndpointReferenceType;

import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

/**
 *
 * @author rayj
 */
public class SubscriptionItemService_RetrieveByNotification_Test {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SubscriptionItemService_RetrieveByNotification_Test.class);

    public SubscriptionItemService_RetrieveByNotification_Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void RetrieveBySimpleTopic() throws Exception {
        String topicXml = "<wsnt:Topic xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple\">SomeTopic</wsnt:Topic> ";
        String topicExpressionXml = topicXml.replace("wsnt:Topic", "wsnt:TopicExpression");
        executeSingleMatchTest(topicExpressionXml, topicXml);
    }

    @Test
    public void RetrieveByTopic() throws Exception {
        String topicXml = "<wsnt:Topic xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple\">nhin:SomeTopic</wsnt:Topic> ";
        String topicExpressionXml = topicXml.replace("wsnt:Topic", "wsnt:TopicExpression");
        executeSingleMatchTest(topicExpressionXml, topicXml);
    }

    @Test
    public void RetrieveByTopicWithNamespaceInParent() throws Exception {
        String topicXml = "<wsnt:Topic xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple\">PrefixDefinedInHeader:SomeTopic</wsnt:Topic> ";
        String topicExpressionXml = topicXml.replace("wsnt:Topic", "wsnt:TopicExpression");
        executeSingleMatchTest(topicExpressionXml, topicXml);
    }

    @Test
    public void RetrieveByConcreteTopic() throws Exception {
        String topicXml = "<wsnt:Topic xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete\">nhin:ParentTopic/nhin:ChildTopic</wsnt:Topic> ";
        String topicExpressionXml = topicXml.replace("wsnt:Topic", "wsnt:TopicExpression");
        executeSingleMatchTest(topicExpressionXml, topicXml);
    }

    @Test
    public void RetrieveByFullTopic() throws Exception {
        String topicXml = "<wsnt:Topic xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Full\">nhin:ParentTopic/nhin:ChildTopic</wsnt:Topic> ";
        String topicExpressionXml = "<wsnt:TopicExpression xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Full\">nhin:ParentTopic/*</wsnt:TopicExpression> ";
        executeSingleMatchTest(topicExpressionXml, topicXml);
    }

//    @Test
//    public void RetrieveByFullTopicHandleChildFilter() throws Exception {
//        String notificationTopicXml = "<wsnt:Topic xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Full\">nhin:ParentTopic/nhin:ChildTopic</wsnt:Topic> ";
//        String subscriptionTopicExpressionXml;
//
//        List<String> expectedMatchingSubscriptionTopicExpressionXmlList = new ArrayList<String>();
//        subscriptionTopicExpressionXml = "<wsnt:TopicExpression xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Full\">nhin:ParentTopic/*</wsnt:TopicExpression> ";
//        expectedMatchingSubscriptionTopicExpressionXmlList.add(subscriptionTopicExpressionXml);
//
//        subscriptionTopicExpressionXml = "<wsnt:TopicExpression xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Full\">nhin:ParentTopic/nhin:ChildTopic</wsnt:TopicExpression> ";
//        expectedMatchingSubscriptionTopicExpressionXmlList.add(subscriptionTopicExpressionXml);
//
//        List<String> expectedNotMatchingSubscriptionTopicExpressionXmlList = new ArrayList<String>();
//        subscriptionTopicExpressionXml = "<wsnt:TopicExpression xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Full\">nhin:ParentTopic/nhin:NoMatchTopic</wsnt:TopicExpression> ";
//        expectedNotMatchingSubscriptionTopicExpressionXmlList.add(subscriptionTopicExpressionXml);
//
//        executeMatchTest(expectedMatchingSubscriptionTopicExpressionXmlList, expectedNotMatchingSubscriptionTopicExpressionXmlList, notificationTopicXml);
//
//    }

//    @Test
//    public void RetrieveByFullTopic() throws Exception {
//        String topicXml = "<wsnt:Topic xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Full\">nhin:ParentTopic/nhin:ChildTopic</wsnt:Topic> ";
//        String topicExpressionXml = "<wsnt:TopicExpression xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Full\">nhin:ParentTopic/*</wsnt:TopicExpression> ";
//        executeSingleMatchTest(topicExpressionXml, topicXml);
//    }
    private EndpointReferenceType storeSubscribe(String topicExpressionXml, String producer) throws SubscriptionRepositoryException {
        SubscriptionItem subscriptionItem = createSubscriptionItem(topicExpressionXml, producer);
        SubscriptionRepositoryService service = new SubscriptionRepositoryService();
        EndpointReferenceType subscriptionReference = service.saveSubscriptionToConnect(subscriptionItem);
        assertNotNull("Subscription reference was null", subscriptionReference);
        return subscriptionReference;
    }

    private Element createNotificationMessageElement(String topicXml) throws Exception {
        String notificationMessage =
                "<wsnt:NotificationMessage xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:PrefixDefinedInHeader=\"urn:myNamespace\"> " +
                "   {topic} " +
                "   <wsnt:Message> " +
                "      <nhin:MyNode xmlns:nhin=\"urn:nhin\"> " +
                "         some data " +
                "      </nhin:MyNode> " +
                "   </wsnt:Message> " +
                "</wsnt:NotificationMessage>";
        notificationMessage = notificationMessage.replace("{topic}", topicXml);
        Element notificationMessageElement = convertXmlToElement(notificationMessage);
        return notificationMessageElement;
    }

    private void clearRepository() {
        SubscriptionRepositoryService service = new SubscriptionRepositoryService();
        service.emptyRepository();
        assertEquals("Subscription repository was not empty before start", 0, service.subscriptionCount());
    }

    private void executeSingleMatchTest(String expectedMatchingSubscriptionTopicExpressionXml, String notificationTopicXml) throws Exception {
        String otherTopicExpressionXml = "<wsnt:TopicExpression xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple\">nhin:SomeOtherTopic</wsnt:TopicExpression> ";

        List<String> expectedMatchingSubscriptionTopicExpressionXmlList = new ArrayList<String>();
        expectedMatchingSubscriptionTopicExpressionXmlList.add(expectedMatchingSubscriptionTopicExpressionXml);

        List<String> expectedNotMatchingSubscriptionTopicExpressionXmlList = new ArrayList<String>();
        expectedNotMatchingSubscriptionTopicExpressionXmlList.add(otherTopicExpressionXml);

        executeMatchTest(expectedMatchingSubscriptionTopicExpressionXmlList, expectedNotMatchingSubscriptionTopicExpressionXmlList, notificationTopicXml);
    }

    private void executeMatchTest(List<String> expectedMatchingSubscriptionTopicExpressionXml, List<String> expectedNotMatchingSubscriptionTopicExpressionXml, String notificationTopicXml) throws Exception {
        log.debug("Begin executeMatchTest");

        clearRepository();

        for (String topicExpression : expectedMatchingSubscriptionTopicExpressionXml) {
            storeSubscribe(topicExpression, "ADAPTER");
        }
        for (String topicExpression : expectedNotMatchingSubscriptionTopicExpressionXml) {
            storeSubscribe(topicExpression, "ADAPTER");
        }

        Element notificationMessageElement = createNotificationMessageElement(notificationTopicXml);

        SubscriptionRepositoryService service = new SubscriptionRepositoryService();
        List<SubscriptionItem> subscriptionItems = service.RetrieveByNotificationMessage(notificationMessageElement, "ADAPTER");

        assertNotNull("Subscription item was null", subscriptionItems);
        assertEquals(expectedMatchingSubscriptionTopicExpressionXml.size(), subscriptionItems.size());
        log.debug("End executeMatchTest");
    }

    private SubscriptionItem createSubscriptionItem(String topicExpressionXml, String producer) {
        String SUBSCRIBE_XML =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
                "<wsnt:Subscribe xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:PrefixDefinedInHeader=\"urn:myNamespace\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:nhin-cdc=\"http://www.hhs.gov/healthit/nhin/cdc\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://docs.oasis-open.org/wsn/b-2 schemas/docs.oasis-open.org/wsn/b-2.xsd\"> " +
                "	<wsnt:ConsumerReference> " +
                "		<wsa:Address>https://remotegateway/NotificationConsumerService</wsa:Address> " +
                "		<wsa:ReferenceParameters> " +
                "			<nhin:UserAddress xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">user@remotegateway</nhin:UserAddress> " +
                "			<prefix:CustomInformation xmlns:prefix=\"http://remotegateway/somenamespace\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">Custom Data</prefix:CustomInformation> " +
                "		</wsa:ReferenceParameters> " +
                "	</wsnt:ConsumerReference> " +
                "	<wsnt:Filter> " +
                "		{topicExpression} " +
                "		<wsnt:MessageContent Dialect=\"http://www.w3.ord/TR/1999/REC-xpath-19991116\" xmlns:x='urn:somenamespace'>//x:SomeXpathStatement</wsnt:MessageContent> " +
                "	</wsnt:Filter> " +
                "</wsnt:Subscribe>";

        SubscriptionItem subscriptionItem = new SubscriptionItem();

        SUBSCRIBE_XML = SUBSCRIBE_XML.replace("{topicExpression}", topicExpressionXml);
        subscriptionItem.setSubscribeXML(SUBSCRIBE_XML);
        subscriptionItem.setSubscriptionReferenceXML(null);
        subscriptionItem.setRootTopic(null);
        subscriptionItem.setParentSubscriptionReferenceXML(null);
        subscriptionItem.setConsumer(null);
        subscriptionItem.setProducer(producer);
        subscriptionItem.setTargets(null);
        subscriptionItem.setCreationDate(null);
        return subscriptionItem;
    }

    private Element convertXmlToElement(String xml) throws Exception {
        log.debug("In convertXmlToElement");
        Element element = null;
        if (xml != null) {
            try {
                System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMImplementationSourceImpl");
                DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
                DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
                LSParser parser = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, "http://www.w3.org/2001/XMLSchema");
                LSInput lsInput = impl.createLSInput();
                lsInput.setStringData(xml);
                Document doc = parser.parse(lsInput);
                if (doc != null) {
                    element = doc.getDocumentElement();
                }
            } catch (Throwable t) {
                log.error("Error parsing root topic: " + t.getMessage(), t);
                throw new Exception("Failed to parse root topic: " + t.getMessage(), t);
            }
        }
        return element;
    }
}