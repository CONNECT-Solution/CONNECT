/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.subscription.repository.service;

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractor;

import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author rayj
 */
@Ignore
public class RootTopicExtractorTest {

    public RootTopicExtractorTest() {
    }

    // public Node xmlToNode(String xmlSource) throws ParserConfigurationException, SAXException, IOException {
    // DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    // DocumentBuilder builder = factory.newDocumentBuilder();
    // Document doc = builder.parse(new InputSource(new StringReader(xmlSource)));
    // return (Node) doc.getDocumentElement();
    // }
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
        String subscribe = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
                + "<wsnt:Subscribe xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:PrefixDefinedInHeader=\"urn:myNamespace\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:nhin-cdc=\"http://www.hhs.gov/healthit/nhin/cdc\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://docs.oasis-open.org/wsn/b-2 schemas/docs.oasis-open.org/wsn/b-2.xsd\"> "
                + "	<wsnt:ConsumerReference> "
                + "		<wsa:Address>https://remotegateway/NotificationConsumerService</wsa:Address> "
                + "		<wsa:ReferenceParameters> "
                + "			<nhin:UserAddress xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">user@remotegateway</nhin:UserAddress> "
                + "			<prefix:CustomInformation xmlns:prefix=\"http://remotegateway/somenamespace\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">Custom Data</prefix:CustomInformation> "
                + "		</wsa:ReferenceParameters> "
                + "	</wsnt:ConsumerReference> "
                + "	<rim:AdhocQuery id=\"urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\">"
                + "	   <rim:Slot name=\"$XDSDocumentEntryPatientId\"> " + "	      <rim:ValueList> "
                + "	         <rim:Value>222498764^^^&amp;2.16.840.1.113883.3.18.103&amp;ISO</rim:Value> "
                + "	      </rim:ValueList> " + "	   </rim:Slot> "
                + "      <rim:Slot name=\"$XDSDocumentEntryClassCode\">  " + "	      <rim:ValueList> "
                + "	         <rim:Value>('XNHIN-CONSENT')</rim:Value> " + "	      </rim:ValueList> "
                + "	   </rim:Slot> " + "   </rim:AdhocQuery> " + "</wsnt:Subscribe>";
        executeTest(subscribe, expectedRootTopic);
    }

    private void executeTopicTest(String topicExpression, String expectedRootTopic)
            throws SubscriptionRepositoryException {
        String subscribe = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
                + "<wsnt:Subscribe xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:PrefixDefinedInHeader=\"urn:myNamespace\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:nhin-cdc=\"http://www.hhs.gov/healthit/nhin/cdc\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://docs.oasis-open.org/wsn/b-2 schemas/docs.oasis-open.org/wsn/b-2.xsd\"> "
                + "	<wsnt:ConsumerReference> "
                + "		<wsa:Address>https://remotegateway/NotificationConsumerService</wsa:Address> "
                + "		<wsa:ReferenceParameters> "
                + "			<nhin:UserAddress xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">user@remotegateway</nhin:UserAddress> "
                + "			<prefix:CustomInformation xmlns:prefix=\"http://remotegateway/somenamespace\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">Custom Data</prefix:CustomInformation> "
                + "		</wsa:ReferenceParameters> "
                + "	</wsnt:ConsumerReference> "
                + "	<wsnt:Filter> "
                + "		{topicExpression} "
                + "		<wsnt:MessageContent Dialect=\"http://www.w3.ord/TR/1999/REC-xpath-19991116\" xmlns:x='urn:somenamespace'>//x:SomeXpathStatement</wsnt:MessageContent> "
                + "	</wsnt:Filter> " + "</wsnt:Subscribe>";

        subscribe = subscribe.replace("{topicExpression}", topicExpression);
        executeTest(subscribe, expectedRootTopic);
    }

    private void executeTest(String subscribe, String expectedRootTopic) throws SubscriptionRepositoryException {
        RootTopicExtractor rootTopicExtractor = new RootTopicExtractor();
        String extractedRootTopic = null;
        extractedRootTopic = rootTopicExtractor.extractRootTopicFromSubscribeXml(subscribe);
        System.out.println("extracted root topic: " + extractedRootTopic);
        System.out.println("expected root topic: " + expectedRootTopic);
        assertEquals(expectedRootTopic, extractedRootTopic);
    }
}