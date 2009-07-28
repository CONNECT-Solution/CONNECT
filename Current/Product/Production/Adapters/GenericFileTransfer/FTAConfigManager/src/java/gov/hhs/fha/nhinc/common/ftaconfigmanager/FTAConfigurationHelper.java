/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class FTAConfigurationHelper
{
    public static final String FTA_CONFIG_FILE = "FTAConfiguration.xml";
    private static Log log = LogFactory.getLog(FTAConfigurationHelper.class);

    public static FTAConfiguration loadFTAConfiguration()
    {
        FTAConfiguration result;
        
        String propertyDir = PropertyAccessor.getPropertyFileLocation();

        log.debug(propertyDir);
        result = loadFTAConfiguration(propertyDir, FTA_CONFIG_FILE);

        log.debug("Successfully loaded configuration.");
        log.debug("returned " + result.getInboundChannels().size() + " Inbound channels, " +
                result.getOutboundChannels().size() + " Outbound channels");
        return result;
    }
        public static FTAConfiguration loadFTAConfiguration(File file)
        {
            FTAConfiguration result = new FTAConfiguration();
            try
            {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(file);
                doc.getDocumentElement().normalize();
                log.debug("Root element " + doc.getDocumentElement().getNodeName());
                NodeList nodeLst = doc.getElementsByTagName("InboundChannels");

                result.setInboundChannels(loadChannels(doc.getElementsByTagName("InboundChannels")));
                result.setOutboundChannels(loadChannels(doc.getElementsByTagName("OutboundChannels")));

            }
            catch (Exception e)
            {
                log.error("unable to load FTAConfiguration file", e);
                e.printStackTrace();
            }

            return result;
        }
    public static FTAConfiguration loadFTAConfiguration(String path, String fileName)
    {
        FTAConfiguration result = null;

        log.debug("loadFTAConfiguration");
        log.debug(fileName);
        try {
            File file = new File(path, fileName);
            result = loadFTAConfiguration(file);
        }
        catch (Exception e)
        {
            log.error("unable to load FTAConfiguration file", e);
            e.printStackTrace();
        }

        return result;
    }
    public static FTAConfiguration loadFTAConfiguration(String fileName)
    {
        FTAConfiguration result = null;
        log.debug("loadFTAConfiguration");
        log.debug(fileName);
        try {
            File file = new File(fileName);
            result = loadFTAConfiguration(file);
        }
        catch (Exception e) 
        {
            log.error("unable to load FTAConfiguration file", e);
            e.printStackTrace();
        }
        return result;
    }

    public static FTAChannel getChannelByTopic(List<FTAChannel> channels, String topic)
    {
        FTAChannel result = null;

        for (FTAChannel channel:channels)
        {
            if (channel.getTopic().equals(topic))
            {
                result = channel;
            }
        }

        return result;
    }
    private static List<FTAChannel> loadChannels(NodeList channelNode)
    {
        List<FTAChannel> result = new ArrayList<FTAChannel>();

        Node channels = channelNode.item(0);

        log.debug("loading " + channels.getChildNodes().getLength() + " channels" );;
        for (int s = 0; s < channels.getChildNodes().getLength(); s++) 
        {
            Node node = channels.getChildNodes().item(s);
            if(node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element)node;
                System.out.println(element.getNodeName());
                
                NodeList channelList = element.getChildNodes();

                FTAChannel newChannel = new FTAChannel();

                for(int x=0; x<channelList.getLength();x++)
                {
                    //Element childList = (Element)channelList.item(x);

                    if(channelList.item(x).getNodeType() == Node.ELEMENT_NODE)
                    {
                        String nodeValue = "";
                        String nodeName = "";

                        if (channelList.item(x) != null)
                        {
                            nodeValue = channelList.item(x).getTextContent().trim();
                            nodeName = channelList.item(x).getNodeName();
                        }
                        System.out.println(nodeName);

                        if(nodeName.equalsIgnoreCase("type"))
                        {
                            newChannel.setType(nodeValue);
                        }
                        else if(nodeName.equalsIgnoreCase("topic"))
                        {
                           newChannel.setTopic(nodeValue);
                        }
                        else if (nodeName.equalsIgnoreCase("location"))
                        {
                            newChannel.setLocation(nodeValue);
                        }
                        else if (nodeName.equalsIgnoreCase("additionalElements"))
                        {
                            newChannel.setAdditionalElements(loadAdditionalElements(channelList.item(x)));
                        }
                    }
                }

                result.add(newChannel);
            }
           
        }
        return result;
    }
    private static List<FTAElement> loadAdditionalElements(Node elements)
    {
        List<FTAElement> result = new ArrayList<FTAElement>();

        for (int s = 0; s < elements.getChildNodes().getLength(); s++)
        {
            Node node = elements.getChildNodes().item(s);
            if(node.getNodeType() == Node.ELEMENT_NODE)
            {
                FTAElement element = new FTAElement();

                element.setName(node.getNodeName());
                element.setValue(node.getTextContent());

                result.add(element);
            }
        }
        return result;

    }

}
