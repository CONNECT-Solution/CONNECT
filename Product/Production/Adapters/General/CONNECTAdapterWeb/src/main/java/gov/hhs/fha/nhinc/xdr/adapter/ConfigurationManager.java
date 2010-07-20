/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.adapter;
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
    public static final String XDR_CONFIG_FILE = "XDRConfiguration.xml";
    private static Log log = null;

    public ConfigurationManager()
    {
        log = createLogger();
    }
    public Config loadConfiguration()
    {
        Config result;

        String propertyDir = PropertyAccessor.getPropertyFileLocation();


        result = loadConfiguration(propertyDir, XDR_CONFIG_FILE);


        return result;
    }
    public  Config loadConfiguration(String path, String fileName)
    {
        Config result = null;


        try {
            File file = new File(path, fileName);
            result = loadConfiguration(file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }
        public Config loadConfiguration(File file)
        {
            Config result = new Config();

            try
            {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(file);
                doc.getDocumentElement().normalize();
                
                NodeList nodeLst = doc.getElementsByTagName("RoutingInformation");
                result.setRoutingInfo(loadRoutingInfo(nodeLst));

            }
            catch (Exception e)
            {
                log.error("unable to load FTAConfiguration file", e);
                e.printStackTrace();
            }

            return result;
        }
    private List<RoutingConfig> loadRoutingInfo(NodeList list)
    {
        ArrayList<RoutingConfig> result = new ArrayList<RoutingConfig>();
        Node channels = list.item(0);

        log.debug("loading " + channels.getChildNodes().getLength() + " channels" );;
        for (int s = 0; s < channels.getChildNodes().getLength(); s++)
        {
            Node node = channels.getChildNodes().item(s);
            if(node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element)node;
                System.out.println(element.getNodeName());
                if(element.getNodeName().equalsIgnoreCase("RoutingConfig"))
                {
                    RoutingConfig item = new RoutingConfig();
                    for(int x = 0; x < element.getChildNodes().getLength(); x++)
                    {
                        String nodeName = element.getChildNodes().item(x).getNodeName();
                        String nodeValue = element.getChildNodes().item(x).getTextContent();
                        
                        if(nodeName.equalsIgnoreCase("Recipient"))
                        {
                            item.setRecepient(nodeValue);
                        }
                        else if(nodeName.equalsIgnoreCase("Bean"))
                        {
                            item.setBean(nodeValue);
                        }
                    }
                    result.add(item);
                }
            }
        }

        return result;
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
}
