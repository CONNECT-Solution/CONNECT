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
package gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration;

import gov.hhs.fha.nhinc.hiem.processor.faults.ConfigurationException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractor;
import gov.hhs.fha.nhinc.subscription.repository.topicfilter.ITopicComparison;
import gov.hhs.fha.nhinc.subscription.repository.topicfilter.TopicComparisonFactory;
import gov.hhs.fha.nhinc.util.StringUtil;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

import java.io.File;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

/**
 * 
 * @author rayj
 */
public class TopicConfigurationManager {

    private static final Logger LOG = Logger.getLogger(TopicConfigurationManager.class);
    private static TopicConfigurationManager topicConfigurationManager;
    private static TopicConfigurations topicConfigurations = new TopicConfigurations();
    private static final String CRLF = System.getProperty("line.separator");

    // Variables for managing the topic configuration XML file.
    // -------------------------------------------------------
    private static final String TOPIC_CONFIG_XML_FILE_NAME = "hiemTopicConfiguration.xml";
    private static String m_sPropertyFileDir = "";
    private static String m_sInternalXMLFileDir = "";
    private static String m_sFileSeparator = System.getProperty("file.separator");
    private static final String m_sFailedEnvVarMessage = "Unable to access system variable: nhinc.properties.dir.";
    private static boolean m_bFailedToLoadEnvVar = false;
    
    static {
        String sValue = PropertyAccessor.getInstance().getPropertyFileLocation();
        if ((sValue != null) && (sValue.length() > 0)) {
            // Set it up so that we always have a "/" at the end - in case
            // ------------------------------------------------------------
            if ((sValue.endsWith("/")) || (sValue.endsWith("\\"))) {
                m_sPropertyFileDir = sValue;
            } else {
                m_sPropertyFileDir = sValue + m_sFileSeparator;
            }

            m_sInternalXMLFileDir = m_sPropertyFileDir + TOPIC_CONFIG_XML_FILE_NAME;
        } else {
            LOG.error(m_sFailedEnvVarMessage);
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

        LOG.info("finding topic configuration for node='" + XmlUtility.serializeNodeIgnoreFaults(TopicNode) + "'");

        try {
            loadTopicConfigurations();
        } catch (Exception ex) {
        	LOG.error("failed to load topic configuration. [" + ex.getMessage() + "]", ex);
            throw new ConfigurationException("Unable to load topic configuration", ex);
        }

        for (TopicConfigurationEntry topicConfig : topicConfigurations.getTopicConfigurations()) {
            try {
                String topic = topicConfig.getTopic();
                LOG.debug("comparing to config topic [topicConfig.topic='" + topic + "']");

                Element configurationTopicNode = XmlUtility.convertXmlToElement(topic);
                LOG.debug("config topic converted to Element and then reseralized '"
                        + XmlUtility.serializeNodeIgnoreFaults(configurationTopicNode) + "'");

                RootTopicExtractor rootTopicExtractor = new RootTopicExtractor();
                String dialect = rootTopicExtractor.getDialectFromTopicExpression(topic);
                LOG.debug("topic config dialect='" + dialect + "'");

                LOG.debug("get ITopicComparison strategy");
                ITopicComparison topicComparisonStrategy = TopicComparisonFactory.getTopicComparisonStrategy(dialect);

                LOG.debug("checking if message topic meets matching criteria for this topic config");
                if (topicComparisonStrategy.MeetsCriteria(configurationTopicNode, TopicNode)) {
                    LOG.debug("match");
                    match = topicConfig;
                    break;
                } else {
                    LOG.debug("no match");
                }
            } catch (Exception ex) {
                throw new ConfigurationException("Error occurred determining topic configuration ", ex);
            }
        }

        LOG.debug("complete getTopicConfiguration.  Found match? " + (match != null));
        return match;
    }

    private static void loadTopicConfigurations() throws ConfigurationException {
        // We can only proceed if we know where the files are...
        // --------------------------------------------------------
        if (m_bFailedToLoadEnvVar) {
            throw new ConfigurationException("Failed to load environment variable for configuration directory");
        }

        String sTopicConfigXml = "";

        try {
            sTopicConfigXml = StringUtil.readTextFile(m_sInternalXMLFileDir);
            File fInternalFile = new File(m_sInternalXMLFileDir);
        } catch (Exception e) {
            String sErrorMessage = "Failed to read from file: '" + m_sInternalXMLFileDir + "'.  Error: "
                    + e.getMessage();
            LOG.error(sErrorMessage);
            throw new ConfigurationException(sErrorMessage, e);
        }

        LOG.debug("Setting internal connection cache to be: " + CRLF + sTopicConfigXml);

        topicConfigurations = TopicConfigurationsXML.deserialize(sTopicConfigXml);

        if (topicConfigurations != null) {
        } else {
            LOG.warn("No topic configuration information was found in: " + m_sInternalXMLFileDir);
        }
    }
}
