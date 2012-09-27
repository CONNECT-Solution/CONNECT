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
package gov.hhs.fha.nhinc.docsubmission.adapter.component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 *
 * @author dunnek
 */
public class ConfigurationManager {
    public static final String XDR_CONFIG_FILE = "XDRConfiguration.xml";
    private static Log log = null;

    public ConfigurationManager() {
        log = createLogger();
    }

    public Config loadConfiguration() {
        Config result;

        String propertyDir = PropertyAccessor.getInstance().getPropertyFileLocation();

        result = loadConfiguration(propertyDir, XDR_CONFIG_FILE);

        return result;
    }

    public Config loadConfiguration(String path, String fileName) {
        Config result = null;

        try {
            File file = new File(path, fileName);
            result = loadConfiguration(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public Config loadConfiguration(File file) {
        Config result = new Config();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeLst = doc.getElementsByTagName("RoutingInformation");
            result.setRoutingInfo(loadRoutingInfo(nodeLst));

        } catch (Exception e) {
            log.error("unable to load FTAConfiguration file", e);
            e.printStackTrace();
        }

        return result;
    }

    private List<RoutingConfig> loadRoutingInfo(NodeList list) {
        ArrayList<RoutingConfig> result = new ArrayList<RoutingConfig>();
        Node channels = list.item(0);

        log.debug("loading " + channels.getChildNodes().getLength() + " channels");
        ;
        for (int s = 0; s < channels.getChildNodes().getLength(); s++) {
            Node node = channels.getChildNodes().item(s);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                System.out.println(element.getNodeName());
                if (element.getNodeName().equalsIgnoreCase("RoutingConfig")) {
                    RoutingConfig item = new RoutingConfig();
                    for (int x = 0; x < element.getChildNodes().getLength(); x++) {
                        String nodeName = element.getChildNodes().item(x).getNodeName();
                        String nodeValue = element.getChildNodes().item(x).getTextContent();

                        if (nodeName.equalsIgnoreCase("Recipient")) {
                            item.setRecepient(nodeValue);
                        } else if (nodeName.equalsIgnoreCase("Bean")) {
                            item.setBean(nodeValue);
                        }
                    }
                    result.add(item);
                }
            }
        }

        return result;
    }

    /**
     * @return the Logger
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
}
