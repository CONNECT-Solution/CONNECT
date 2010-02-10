/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

//        topicConfigEntry = new TopicConfiguration();
//        topicConfigEntry.setTopic("<wsnt:Topic xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">nhin:SomeOtherTopic1</wsnt:Topic>");
//        topicConfigEntry.setSupported(false);
//        topicConfigurationManager.AddEntry(topicConfigEntry);
//
//        topicConfigEntry = new TopicConfiguration();
//        topicConfigEntry.setTopic("<wsnt:Topic xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">nhin:SomeTopic</wsnt:Topic>");
//        topicConfigEntry.setSupported(true);
//        topicConfigurationManager.AddEntry(topicConfigEntry);
//
//        topicConfigEntry = new TopicConfiguration();
//        topicConfigEntry.setTopic("<wsnt:Topic xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">nhin:PatientCentricTopic</wsnt:Topic>");
//        topicConfigEntry.setSupported(true);
//        topicConfigEntry.setPatientCentric(true);
//        topicConfigEntry.setPatientIdentifierFormat("HL7Encoded");
//        topicConfigEntry.setPatientIdentifierNotifyLocation("//myxpathstatement");
//        topicConfigEntry.setPatientIdentifierSubscribeLocation("//myxpathotherstatement");
//
//        topicConfigurationManager.AddEntry(topicConfigEntry);
//
//        topicConfigEntry = new TopicConfiguration();
//        topicConfigEntry.setTopic("<wsnt:Topic xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">nhin:SomeOtherTopic2</wsnt:Topic>");
//        topicConfigEntry.setSupported(false);
//        topicConfigurationManager.AddEntry(topicConfigEntry);

        return topicConfigurationManager;
    }
    @Ignore //Move this test to Integration test suite
    @Test
    public void FindMatch() throws Exception {
        TopicConfigurationManager topicConfigurationManager = topicConfigurationManagerFactory();
        TopicConfigurationEntry topicConfigEntry;

        Element topic =   XmlUtility.convertXmlToElement("<wsnt:Topic xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:n=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">SomeTopic</wsnt:Topic>");
        topicConfigEntry = topicConfigurationManager.getTopicConfiguration(topic);
        assertNotNull(topicConfigEntry);
        assertEquals(true, topicConfigEntry.isSupported());
    }

    @Ignore //Move this test to Integration test suite
    @Test
    public void NoMatch() throws Exception {
        TopicConfigurationManager topicConfigurationManager = TopicConfigurationManager.getInstance();
        TopicConfigurationEntry topicConfigEntry;

        Element topic =   XmlUtility.convertXmlToElement("<wsnt:Topic xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:n=\"http://www.hhs.gov/healthit/nhin\" Dialect=\"http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple\">NoTopic</wsnt:Topic>");
        topicConfigEntry = topicConfigurationManager.getTopicConfiguration(topic);
        assertNull(topicConfigEntry);
    }
}