/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subscription.repository.service;

import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractor;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;

/**
 *
 * @author rayj
 */
@Ignore
public class RootTopicExtractorTest {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(RootTopicExtractorTest.class);

    public RootTopicExtractorTest() {
    }

//    public Node xmlToNode(String xmlSource) throws ParserConfigurationException, SAXException, IOException {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document doc = builder.parse(new InputSource(new StringReader(xmlSource)));
//        return (Node) doc.getDocumentElement();
//    }
    @Test
    public void ExtractSimpleRootTopicFromTopicExpressionNoNamespace() throws Exception {
        String topicExpressionXml = "<wsnt:TopicExpression xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">SomeTopic</wsnt:TopicExpression> ";
        String expectedRootTopic = "SomeTopic";
        executeTopicTest(topicExpressionXml, expectedRootTopic);
    }

    @Test
    public void ExtractSimpleRootTopicFromTopicExpression() throws Exception {
        String topicExpressionXml = "<wsnt:TopicExpression xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">nhin:SomeTopic</wsnt:TopicExpression> ";
        String expectedRootTopic = "{http://www.hhs.gov/healthit/nhin}SomeTopic";
        executeTopicTest(topicExpressionXml, expectedRootTopic);
    }

    @Test
    public void ExtractSimpleRootTopicFromTopicExpressionWithNamespaceInParent() throws Exception {
        String topicExpressionXml = "<wsnt:TopicExpression xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">PrefixDefinedInHeader:SomeTopic</wsnt:TopicExpression> ";
        String expectedRootTopic = "{urn:myNamespace}SomeTopic";
        executeTopicTest(topicExpressionXml, expectedRootTopic);
    }

    @Test
    public void ExtractRootTopicFromConcreteTopicExpression() throws Exception {
        String topicExpressionXml = "<wsnt:TopicExpression xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete\">nhin:parentTopic/nhin:childTopic</wsnt:TopicExpression>";
        String expectedRootTopic = "{http://www.hhs.gov/healthit/nhin}parentTopic/{http://www.hhs.gov/healthit/nhin}childTopic";
        executeTopicTest(topicExpressionXml, expectedRootTopic);
    }

    @Test
    public void ExtractRootTopicFromConcreteTopicExpressionWithDifferentNamespaces() throws Exception {
        String topicExpressionXml = "<wsnt:TopicExpression xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete\">nhin:parentTopic/PrefixDefinedInHeader:childTopic</wsnt:TopicExpression>";
        String expectedRootTopic = "{http://www.hhs.gov/healthit/nhin}parentTopic/{urn:myNamespace}childTopic";
        executeTopicTest(topicExpressionXml, expectedRootTopic);
    }

    @Test
    public void ExtractRootTopicFromFullTopicExpression() throws Exception {
        String topicExpressionXml = "<wsnt:TopicExpression xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Full\">nhin:parentTopic/*</wsnt:TopicExpression>";
        String expectedRootTopic = "{http://www.hhs.gov/healthit/nhin}parentTopic";
        executeTopicTest(topicExpressionXml, expectedRootTopic);
    }

    @Test
    public void ExtractNullTopicExpression() throws Exception {
        String topicExpressionXml = "";
        String expectedRootTopic = null;
        executeTopicTest(topicExpressionXml, expectedRootTopic);
    }

    @Test
    public void ExtractDocumentTopicFromV1Format() throws Exception {
        String expectedRootTopic = "{urn:gov.hhs.fha.nhinc.hiemtopic}document";
        String subscribe =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
                "<wsnt:Subscribe xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:PrefixDefinedInHeader=\"urn:myNamespace\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:nhin-cdc=\"http://www.hhs.gov/healthit/nhin/cdc\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://docs.oasis-open.org/wsn/b-2 schemas/docs.oasis-open.org/wsn/b-2.xsd\"> " +
                "	<wsnt:ConsumerReference> " +
                "		<wsa:Address>https://remotegateway/NotificationConsumerService</wsa:Address> " +
                "		<wsa:ReferenceParameters> " +
                "			<nhin:UserAddress xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">user@remotegateway</nhin:UserAddress> " +
                "			<prefix:CustomInformation xmlns:prefix=\"http://remotegateway/somenamespace\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">Custom Data</prefix:CustomInformation> " +
                "		</wsa:ReferenceParameters> " +
                "	</wsnt:ConsumerReference> " +
                "	<rim:AdhocQuery id=\"urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\">" +
                "	   <rim:Slot name=\"$XDSDocumentEntryPatientId\"> " +
                "	      <rim:ValueList> " +
                "	         <rim:Value>222498764^^^&amp;2.16.840.1.113883.3.18.103&amp;ISO</rim:Value> " +
                "	      </rim:ValueList> " +
                "	   </rim:Slot> " +
                "      <rim:Slot name=\"$XDSDocumentEntryClassCode\">  " +
                "	      <rim:ValueList> " +
                "	         <rim:Value>('XNHIN-CONSENT')</rim:Value> " +
                "	      </rim:ValueList> " +
                "	   </rim:Slot> " +
                "   </rim:AdhocQuery> " +
                "</wsnt:Subscribe>";
        executeTest(subscribe, expectedRootTopic);
    }

    private void executeTopicTest(String topicExpression, String expectedRootTopic) throws SubscriptionRepositoryException {
        String subscribe =
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

        subscribe = subscribe.replace("{topicExpression}", topicExpression);
        executeTest(subscribe, expectedRootTopic);
    }

    private void executeTest(String subscribe, String expectedRootTopic) throws SubscriptionRepositoryException {
        RootTopicExtractor rootTopicExtractor = new RootTopicExtractor();
        String extractedRootTopic = null;
        extractedRootTopic = rootTopicExtractor.extractRootTopicFromSubscribeXml(subscribe);
        log.debug("extracted root topic: " + extractedRootTopic);
        log.debug("expected root topic: " + expectedRootTopic);
        assertEquals(expectedRootTopic, extractedRootTopic);
    }
}