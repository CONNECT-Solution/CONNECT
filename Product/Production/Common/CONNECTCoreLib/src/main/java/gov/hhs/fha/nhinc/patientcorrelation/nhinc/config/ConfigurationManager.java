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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.config;

import gov.hhs.fha.nhinc.properties.*;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author dunnek
 */
public class ConfigurationManager {
    public static final String FTA_CONFIG_FILE = "PCConfiguration.xml";
    private Log log = null; // LogFactory.getLog(ConfigurationManager.class);

    public ConfigurationManager() {
        log = createLogger();
    }

    public ExpirationConfiguration loadExpirationConfiguration() {
        ExpirationConfiguration result = null;
        log.debug("begin loadExpirationConfiguration()");
        String propertyDir = PropertyAccessor.getInstance().getPropertyFileLocation();

        log.debug("Property Directory: " + propertyDir);
        result = loadExpirationConfiguration(propertyDir, FTA_CONFIG_FILE);

        return result;
    }

    public ExpirationConfiguration loadExpirationConfiguration(String dir, String fileName) {
        ExpirationConfiguration result = null;
        log.debug("loadExpirationConfiguration");
        log.debug("fileName = " + fileName);
        try {
            File file = new File(dir, fileName);
            result = loadExpirationConfiguration(file);
        } catch (Exception e) {
            log.error("unable to load PCConfiguration file", e);
            e.printStackTrace();
        }
        return result;
    }

    public ExpirationConfiguration loadExpirationConfiguration(String fileName) {
        ExpirationConfiguration result = null;
        log.debug("loadExpirationConfiguration");
        log.debug("fileName = " + fileName);
        try {
            File file = new File(fileName);
            result = loadExpirationConfiguration(file);
        } catch (Exception e) {
            log.error("unable to load PCConfiguration file", e);
            e.printStackTrace();
        }
        return result;
    }

    public ExpirationConfiguration loadExpirationConfiguration(File file) {
        log.debug("loadExpirationConfiguration(File)");
        ExpirationConfiguration result = null;
        int defaultConfiguration = -1;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            log.debug("Root element " + doc.getDocumentElement().getNodeName());

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
        } catch (Exception e) {
            log.error("unable to load PCConfiguration file", e);
            e.printStackTrace();
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

            if (result == null) {
                if (config.getDefaultDuration() >= 0 && config.getDefaultUnits().length() > 0) {
                    result = new Expiration("", config.getDefaultUnits(), config.getDefaultDuration());
                }
            }

        }

        return result;
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
}
