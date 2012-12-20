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
package gov.hhs.fha.nhinc.common.ftaconfigmanager;

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
import org.apache.log4j.Logger;

/**
 * 
 * @author dunnek
 */
public class FTAConfigurationHelper {
    public static final String FTA_CONFIG_FILE = "FTAConfiguration.xml";
    private static final Logger LOG = Logger.getLogger(FTAConfigurationHelper.class);

    public static FTAConfiguration loadFTAConfiguration() {
        FTAConfiguration result;

        String propertyDir = PropertyAccessor.getInstance().getPropertyFileLocation();

        LOG.debug(propertyDir);
        result = loadFTAConfiguration(propertyDir, FTA_CONFIG_FILE);

        LOG.debug("Successfully loaded configuration.");
        LOG.debug("returned " + result.getInboundChannels().size() + " Inbound channels, "
                + result.getOutboundChannels().size() + " Outbound channels");
        return result;
    }

    public static FTAConfiguration loadFTAConfiguration(File file) {
        FTAConfiguration result = new FTAConfiguration();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            LOG.debug("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("InboundChannels");

            result.setInboundChannels(loadChannels(doc.getElementsByTagName("InboundChannels")));
            result.setOutboundChannels(loadChannels(doc.getElementsByTagName("OutboundChannels")));

        } catch (Exception e) {
            LOG.error("unable to load FTAConfiguration file", e);
            e.printStackTrace();
        }

        return result;
    }

    public static FTAConfiguration loadFTAConfiguration(String path, String fileName) {
        FTAConfiguration result = null;

        LOG.debug("loadFTAConfiguration");
        LOG.debug(fileName);
        try {
            File file = new File(path, fileName);
            result = loadFTAConfiguration(file);
        } catch (Exception e) {
            LOG.error("unable to load FTAConfiguration file", e);
            e.printStackTrace();
        }

        return result;
    }

    public static FTAConfiguration loadFTAConfiguration(String fileName) {
        FTAConfiguration result = null;
        LOG.debug("loadFTAConfiguration");
        LOG.debug(fileName);
        try {
            File file = new File(fileName);
            result = loadFTAConfiguration(file);
        } catch (Exception e) {
            LOG.error("unable to load FTAConfiguration file", e);
            e.printStackTrace();
        }
        return result;
    }

    public static FTAChannel getChannelByTopic(List<FTAChannel> channels, String topic) {
        FTAChannel result = null;

        for (FTAChannel channel : channels) {
            if (channel.getTopic().equals(topic)) {
                result = channel;
            }
        }

        return result;
    }

    private static List<FTAChannel> loadChannels(NodeList channelNode) {
        List<FTAChannel> result = new ArrayList<FTAChannel>();

        Node channels = channelNode.item(0);

        LOG.debug("loading " + channels.getChildNodes().getLength() + " channels");
        ;
        for (int s = 0; s < channels.getChildNodes().getLength(); s++) {
            Node node = channels.getChildNodes().item(s);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                System.out.println(element.getNodeName());

                NodeList channelList = element.getChildNodes();

                FTAChannel newChannel = new FTAChannel();

                for (int x = 0; x < channelList.getLength(); x++) {
                    // Element childList = (Element)channelList.item(x);

                    if (channelList.item(x).getNodeType() == Node.ELEMENT_NODE) {
                        String nodeValue = "";
                        String nodeName = "";

                        if (channelList.item(x) != null) {
                            nodeValue = channelList.item(x).getTextContent().trim();
                            nodeName = channelList.item(x).getNodeName();
                        }
                        System.out.println(nodeName);

                        if (nodeName.equalsIgnoreCase("type")) {
                            newChannel.setType(nodeValue);
                        } else if (nodeName.equalsIgnoreCase("topic")) {
                            newChannel.setTopic(nodeValue);
                        } else if (nodeName.equalsIgnoreCase("location")) {
                            newChannel.setLocation(nodeValue);
                        } else if (nodeName.equalsIgnoreCase("additionalElements")) {
                            newChannel.setAdditionalElements(loadAdditionalElements(channelList.item(x)));
                        }
                    }
                }

                result.add(newChannel);
            }

        }
        return result;
    }

    private static List<FTAElement> loadAdditionalElements(Node elements) {
        List<FTAElement> result = new ArrayList<FTAElement>();

        for (int s = 0; s < elements.getChildNodes().getLength(); s++) {
            Node node = elements.getChildNodes().item(s);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                FTAElement element = new FTAElement();

                element.setName(node.getNodeName());
                element.setValue(node.getTextContent());

                result.add(element);
            }
        }
        return result;

    }

}
