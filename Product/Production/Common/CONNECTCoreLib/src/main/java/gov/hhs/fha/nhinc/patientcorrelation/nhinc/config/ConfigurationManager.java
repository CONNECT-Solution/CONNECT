/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.config;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author dunnek
 */
public class ConfigurationManager {

    public static final String FTA_CONFIG_FILE = "PCConfiguration.xml";
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationManager.class);

    public ExpirationConfiguration loadExpirationConfiguration() {
        ExpirationConfiguration result;
        LOG.debug("begin loadExpirationConfiguration()");
        String propertyDir = PropertyAccessor.getInstance().getPropertyFileLocation();

        LOG.debug("Property Directory: " + propertyDir);
        result = loadExpirationConfiguration(propertyDir, FTA_CONFIG_FILE);

        return result;
    }

    public ExpirationConfiguration loadExpirationConfiguration(String dir, String fileName) {
        ExpirationConfiguration result = null;
        LOG.debug("loadExpirationConfiguration");
        LOG.debug("fileName = " + fileName);
        try {
            File file = new File(dir, fileName);
            result = loadExpirationConfiguration(file);
        } catch (Exception e) {
            LOG.error("unable to load PCConfiguration file: " + e.getLocalizedMessage(), e);
        }
        return result;
    }

    public ExpirationConfiguration loadExpirationConfiguration(String fileName) {
        ExpirationConfiguration result = null;
        LOG.debug("loadExpirationConfiguration");
        LOG.debug("fileName = " + fileName);
        try {
            File file = new File(fileName);
            result = loadExpirationConfiguration(file);
        } catch (Exception e) {
            LOG.error("unable to load PCConfiguration file: " + e.getLocalizedMessage(), e);
        }
        return result;
    }

    public ExpirationConfiguration loadExpirationConfiguration(File file) {
        LOG.debug("loadExpirationConfiguration(File)");
        ExpirationConfiguration result = null;
        int defaultConfiguration = -1;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            
            final String feature = "http://xml.org/sax/features/external-general-entities";
            dbf.setFeature(feature, false);

            // For Xerces 2
            final String feature2 = "http://apache.org/xml/features/disallow-doctype-decl";
            dbf.setFeature(feature2, true);

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            LOG.debug("Root element " + doc.getDocumentElement().getNodeName());

            String defaultUnits = "";
            Node defaultDurationNode = doc.getElementsByTagName("expirations").item(0).getAttributes()
                    .getNamedItem("defaultDuration");
            Node defaultUnitNode = doc.getElementsByTagName("expirations").item(0).getAttributes()
                    .getNamedItem("defaultUnits");

            if (defaultDurationNode != null) {
                defaultConfiguration = Integer.parseInt(defaultDurationNode.getTextContent());
            }
            if (defaultUnitNode != null) {
                defaultUnits = defaultUnitNode.getTextContent();
            }

            result = new ExpirationConfiguration(defaultConfiguration, defaultUnits);

            NodeList nodeLst = doc.getElementsByTagName("expiration");

            for (int x = 0; x < nodeLst.getLength(); x++) {
                Node item = nodeLst.item(x);
                String aa = item.getAttributes().getNamedItem("assigningAuthority").getTextContent();
                String units = item.getAttributes().getNamedItem("unit").getTextContent();
                String duration = item.getTextContent();

                Expiration expItem = new Expiration(aa, units, Integer.parseInt(duration));

                result.getExpirations().add(expItem);
            }
        } catch (IOException e) {
            LOG.error("unable to load PCConfiguration file", e);
        } catch (NumberFormatException e) {
            LOG.error("unable to load PCConfiguration file", e);
        } catch (ParserConfigurationException e) {
            LOG.error("unable to load PCConfiguration file", e);
        } catch (DOMException e) {
            LOG.error("unable to load PCConfiguration file", e);
        } catch (SAXException e) {
            LOG.error("unable to load PCConfiguration file", e);
        }

        return result;
    }

    public Expiration loadConfiguration(ExpirationConfiguration config, String assigningAuthority) {
        Expiration result = null;

        if (config != null) {
            for (Expiration item : config.getExpirations()) {
                if (item.getAssigningAuthority().equalsIgnoreCase(assigningAuthority)) {
                    result = item;
                }
            }

            if (result == null && config.getDefaultDuration() >= 0
                    && StringUtils.isNotEmpty(config.getDefaultUnits())) {
                result = new Expiration("", config.getDefaultUnits(), config.getDefaultDuration());
            }

        }

        return result;
    }

}
