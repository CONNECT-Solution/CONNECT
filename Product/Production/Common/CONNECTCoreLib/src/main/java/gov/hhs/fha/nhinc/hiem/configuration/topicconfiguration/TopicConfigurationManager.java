/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration;

import gov.hhs.fha.nhinc.hiem.processor.faults.ConfigurationException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.util.StringUtil;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractor;
import gov.hhs.fha.nhinc.subscription.repository.topicfilter.ITopicComparison;
import gov.hhs.fha.nhinc.subscription.repository.topicfilter.TopicComparisonFactory;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author rayj
 */
public class TopicConfigurationManager {

    private static Log log = LogFactory.getLog(TopicConfigurationManager.class);
    private static TopicConfigurationManager topicConfigurationManager;
    private static TopicConfigurations topicConfigurations = new TopicConfigurations();
    private static final String CRLF = System.getProperty("line.separator");

    // Variables for managing the topic configuration XML file.
    //-------------------------------------------------------
    private static final String TOPIC_CONFIG_XML_FILE_NAME = "hiemTopicConfiguration.xml";
    private static String m_sPropertyFileDir = "";
    private static String m_sInternalXMLFileDir = "";
    private static String m_sFileSeparator = System.getProperty("file.separator");
    private static final String m_sFailedEnvVarMessage = "Unable to access environment variable: NHINC_PROPERTIES_DIR.";
    private static boolean m_bFailedToLoadEnvVar = false;
    private static boolean m_bInternalLoaded = false;


    static {
        String sValue = PropertyAccessor.getPropertyFileLocation();
        if ((sValue != null) && (sValue.length() > 0)) {
            // Set it up so that we always have a "/" at the end - in case
            //------------------------------------------------------------
            if ((sValue.endsWith("/")) || (sValue.endsWith("\\"))) {
                m_sPropertyFileDir = sValue;
            } else {
                m_sPropertyFileDir = sValue + m_sFileSeparator;
            }

            m_sInternalXMLFileDir = m_sPropertyFileDir + TOPIC_CONFIG_XML_FILE_NAME;
        } else {
            log.error(m_sFailedEnvVarMessage);
            m_bFailedToLoadEnvVar = true;
        }
    }

    private TopicConfigurationManager() {
    }

    public static TopicConfigurationManager getInstance() {
        if (topicConfigurationManager == null) {
            topicConfigurationManager = new TopicConfigurationManager();
        }
        return topicConfigurationManager;
    }

    public void AddEntry(TopicConfigurationEntry topicConfig) {
        topicConfigurations.getTopicConfigurations().add(topicConfig);
    }

    public TopicConfigurationEntry getTopicConfiguration(Element TopicNode) throws ConfigurationException {
        TopicConfigurationEntry match = null;

        log.info("finding topic configuration for node='" + XmlUtility.serializeNodeIgnoreFaults(TopicNode) + "'");

        try {
            loadTopicConfigurations();
        } catch (Exception ex) {
            log.error("failed to load topic configuration. [" + ex.getMessage() + "]");
            throw new ConfigurationException("Unable to load topic configuration", ex);
        }

        for (TopicConfigurationEntry topicConfig : topicConfigurations.getTopicConfigurations()) {
            try {
                String topic = topicConfig.getTopic();
                log.debug("comparing to config topic [topicConfig.topic='" + topic + "']");

                Element configurationTopicNode = XmlUtility.convertXmlToElement(topic);
                log.debug("config topic converted to Element and then reseralized '" + XmlUtility.serializeNodeIgnoreFaults(configurationTopicNode) + "'");

                RootTopicExtractor rootTopicExtractor = new RootTopicExtractor();
                String dialect = rootTopicExtractor.getDialectFromTopicExpression(topic);
                log.debug("topic config dialect='" + dialect + "'");

                log.debug("get ITopicComparison strategy");
                ITopicComparison topicComparisonStrategy = TopicComparisonFactory.getTopicComparisonStrategy(dialect);

                log.debug("checking if message topic meets matching criteria for this topic config");
                if (topicComparisonStrategy.MeetsCriteria(configurationTopicNode, TopicNode)) {
                    log.debug("match");
                    match = topicConfig;
                    break;
                } else {
                    log.debug("no match");
                }
            } catch (Exception ex) {
                throw new ConfigurationException("Error occurred determining topic configuration ", ex);
            }
        }

        log.debug("complete getTopicConfiguration.  Found match? " + (match != null));
        return match;
    }

    private static void loadTopicConfigurations() throws ConfigurationException {
        // We can only proceed if we know where the files are...
        //--------------------------------------------------------
        if (m_bFailedToLoadEnvVar) {
            throw new ConfigurationException("Failed to load environment variable for configuration directory");
        }

        String sTopicConfigXml = "";

        try {
            sTopicConfigXml = StringUtil.readTextFile(m_sInternalXMLFileDir);
            File fInternalFile = new File(m_sInternalXMLFileDir);
        } catch (Exception e) {
            String sErrorMessage = "Failed to read from file: '" + m_sInternalXMLFileDir + "'.  Error: " + e.getMessage();
            log.error(sErrorMessage);
            throw new ConfigurationException(sErrorMessage, e);
        }

        log.debug("Setting internal connection cache to be: " + CRLF + sTopicConfigXml);

        topicConfigurations = TopicConfigurationsXML.deserialize(sTopicConfigXml);

        if (topicConfigurations != null) {
            m_bInternalLoaded = true;
        } else {
            log.warn("No topic configuration information was found in: " + m_sInternalXMLFileDir);
        }
    }
}
