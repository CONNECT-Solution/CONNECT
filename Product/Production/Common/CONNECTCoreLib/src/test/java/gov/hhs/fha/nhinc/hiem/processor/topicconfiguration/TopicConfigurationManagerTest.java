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
package gov.hhs.fha.nhinc.hiem.processor.topicconfiguration;

import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationManager;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Element;
import static org.junit.Assert.*;

/**
 * 
 * @author rayj
 */
public class TopicConfigurationManagerTest {

    public TopicConfigurationManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private TopicConfigurationManager topicConfigurationManagerFactory() {
        TopicConfigurationManager topicConfigurationManager = TopicConfigurationManager.getInstance();
        TopicConfigurationEntry topicConfigEntry;

        // topicConfigEntry = new TopicConfiguration();
        // topicConfigEntry.setTopic("<wsnt:Topic xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">nhin:SomeOtherTopic1</wsnt:Topic>");
        // topicConfigEntry.setSupported(false);
        // topicConfigurationManager.AddEntry(topicConfigEntry);
        //
        // topicConfigEntry = new TopicConfiguration();
        // topicConfigEntry.setTopic("<wsnt:Topic xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">nhin:SomeTopic</wsnt:Topic>");
        // topicConfigEntry.setSupported(true);
        // topicConfigurationManager.AddEntry(topicConfigEntry);
        //
        // topicConfigEntry = new TopicConfiguration();
        // topicConfigEntry.setTopic("<wsnt:Topic xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">nhin:PatientCentricTopic</wsnt:Topic>");
        // topicConfigEntry.setSupported(true);
        // topicConfigEntry.setPatientCentric(true);
        // topicConfigEntry.setPatientIdentifierFormat("HL7Encoded");
        // topicConfigEntry.setPatientIdentifierNotifyLocation("//myxpathstatement");
        // topicConfigEntry.setPatientIdentifierSubscribeLocation("//myxpathotherstatement");
        //
        // topicConfigurationManager.AddEntry(topicConfigEntry);
        //
        // topicConfigEntry = new TopicConfiguration();
        // topicConfigEntry.setTopic("<wsnt:Topic xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">nhin:SomeOtherTopic2</wsnt:Topic>");
        // topicConfigEntry.setSupported(false);
        // topicConfigurationManager.AddEntry(topicConfigEntry);

        return topicConfigurationManager;
    }

    @Ignore
    // Move this test to Integration test suite
    @Test
    public void FindMatch() throws Exception {
        TopicConfigurationManager topicConfigurationManager = topicConfigurationManagerFactory();
        TopicConfigurationEntry topicConfigEntry;

        Element topic = XmlUtility
                .convertXmlToElement("<wsnt:Topic xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:n=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">SomeTopic</wsnt:Topic>");
        topicConfigEntry = topicConfigurationManager.getTopicConfiguration(topic);
        assertNotNull(topicConfigEntry);
        assertEquals(true, topicConfigEntry.isSupported());
    }

    @Ignore
    // Move this test to Integration test suite
    @Test
    public void NoMatch() throws Exception {
        TopicConfigurationManager topicConfigurationManager = TopicConfigurationManager.getInstance();
        TopicConfigurationEntry topicConfigEntry;

        Element topic = XmlUtility
                .convertXmlToElement("<wsnt:Topic xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:n=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">NoTopic</wsnt:Topic>");
        topicConfigEntry = topicConfigurationManager.getTopicConfiguration(topic);
        assertNull(topicConfigEntry);
    }
}