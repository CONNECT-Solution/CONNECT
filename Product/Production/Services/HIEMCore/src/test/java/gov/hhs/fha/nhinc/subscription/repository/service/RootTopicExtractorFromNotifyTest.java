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

import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractor;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

/**
 * 
 * @author rayj
 */
@Ignore
public class RootTopicExtractorFromNotifyTest {

    public RootTopicExtractorFromNotifyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void ExtractSimpleTopicNoNamespace() throws Exception {
        String TopicXml = "<wsnt:Topic xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">SomeTopic</wsnt:Topic> ";
        String expectedRootTopic = "SomeTopic";
        executeTopicTest(TopicXml, expectedRootTopic);
    }

    @Test
    public void ExtractSimpleRootTopicFromTopic() throws Exception {
        String TopicXml = "<wsnt:Topic xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">nhin:SomeTopic</wsnt:Topic> ";
        String expectedRootTopic = "{http://www.hhs.gov/healthit/nhin}SomeTopic";
        executeTopicTest(TopicXml, expectedRootTopic);
    }

    @Test
    public void ExtractSimpleRootTopicFromTopicWithNamespaceInParent() throws Exception {
        String TopicXml = "<wsnt:Topic xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">PrefixDefinedInHeader:SomeTopic</wsnt:Topic> ";
        String expectedRootTopic = "{urn:myNamespace}SomeTopic";
        executeTopicTest(TopicXml, expectedRootTopic);
    }

    @Test
    public void ExtractRootTopicFromConcreteTopic() throws Exception {
        String TopicXml = "<wsnt:Topic xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete\">nhin:parentTopic/nhin:childTopic</wsnt:Topic>";
        String expectedRootTopic = "{http://www.hhs.gov/healthit/nhin}parentTopic/{http://www.hhs.gov/healthit/nhin}childTopic";
        executeTopicTest(TopicXml, expectedRootTopic);
    }

    @Test
    public void ExtractRootTopicFromConcreteTopicWithDifferentNamespaces() throws Exception {
        String TopicXml = "<wsnt:Topic xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete\">nhin:parentTopic/PrefixDefinedInHeader:childTopic</wsnt:Topic>";
        String expectedRootTopic = "{http://www.hhs.gov/healthit/nhin}parentTopic/{urn:myNamespace}childTopic";
        executeTopicTest(TopicXml, expectedRootTopic);
    }

    @Test
    public void ExtractDocumentTopicFromV1Format() throws Exception {
        String expectedRootTopic = "{urn:gov.hhs.fha.nhinc.hiemtopic}document";
        String notificationMessage = "<wsnt:NotificationMessage xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:PrefixDefinedInHeader=\"urn:myNamespace\"> "
                + "   <wsnt:Message> "
                + "			<ihe:RetrieveDocumentSetRequest xmlns:ihe=\"urn:ihe:iti:xds-wsnt:2007\"> "
                + "				<ihe:DocumentRequest> "
                + "					<ihe:HomeCommunityId>2.16.840.1.113883.3.18.103</ihe:HomeCommunityId> "
                + "					<ihe:RepositoryUniqueId>2.16.840.1.113883.3.18.103.12</ihe:RepositoryUniqueId> "
                + "					<ihe:DocumentUniqueId>20cf14fb-b65c-4c8c-a54d-b0cca8341234</ihe:DocumentUniqueId> "
                + "				</ihe:DocumentRequest> "
                + "			</ihe:RetrieveDocumentSetRequest> "
                + "   </wsnt:Message> "
                + "</wsnt:NotificationMessage>";
        executeTest(notificationMessage, expectedRootTopic);
    }

    private void executeTopicTest(String topic, String expectedRootTopic) throws SubscriptionRepositoryException,
            Exception {
        String notificationMessage = "<wsnt:NotificationMessage xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:PrefixDefinedInHeader=\"urn:myNamespace\"> "
                + "   {topic} "
                + "   <wsnt:Message> "
                + "      <nhin:MyNode xmlns:nhin=\"urn:nhin\"> "
                + "         some data "
                + "      </nhin:MyNode> "
                + "   </wsnt:Message> "
                + "</wsnt:NotificationMessage>";
        notificationMessage = notificationMessage.replace("{topic}", topic);
        executeTest(notificationMessage, expectedRootTopic);
    }

    private void executeTest(String notificationMessage, String expectedRootTopic)
            throws SubscriptionRepositoryException, Exception {
        Element notificationMessageElement = XmlUtility.convertXmlToElement(notificationMessage);
        RootTopicExtractor rootTopicExtractor = new RootTopicExtractor();
        String extractedRootTopic = null;
        extractedRootTopic = rootTopicExtractor.extractRootTopicFromNotificationMessageXml(notificationMessage);
        System.out.println("extracted root topic (xml): " + extractedRootTopic);
        System.out.println("expected root topic       : " + expectedRootTopic);
        assertEquals(expectedRootTopic, extractedRootTopic);
        extractedRootTopic = rootTopicExtractor
                .extractRootTopicFromNotificationMessageElement(notificationMessageElement);
        System.out.println("extracted root topic (element): " + extractedRootTopic);
        System.out.println("expected root topic           : " + expectedRootTopic);
        assertEquals(expectedRootTopic, extractedRootTopic);
    }
}