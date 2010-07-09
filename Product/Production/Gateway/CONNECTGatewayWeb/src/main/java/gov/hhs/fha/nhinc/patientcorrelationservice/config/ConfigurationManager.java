/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelationservice.config;
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
    private Log log = null; //LogFactory.getLog(ConfigurationManager.class);

    public ConfigurationManager()
    {
        log = createLogger();
    }
     public ExpirationConfiguration loadExpirationConfiguration()
     {
         ExpirationConfiguration result = null;
        log.debug("begin loadExpirationConfiguration()");
        String propertyDir = PropertyAccessor.getPropertyFileLocation();

        log.debug("Property Directory: " + propertyDir);
        result = loadExpirationConfiguration(propertyDir, FTA_CONFIG_FILE);

        return result;
     }
     public ExpirationConfiguration loadExpirationConfiguration(String dir, String fileName)
     {
        ExpirationConfiguration result = null;
        log.debug("loadExpirationConfiguration");
        log.debug("fileName = " + fileName);
        try {
            File file = new File(dir, fileName);
            result = loadExpirationConfiguration(file);
        }
        catch (Exception e)
        {
            log.error("unable to load PCConfiguration file", e);
            e.printStackTrace();
        }
        return result;
     }
     public ExpirationConfiguration loadExpirationConfiguration(String fileName)
     {
        ExpirationConfiguration result = null;
        log.debug("loadExpirationConfiguration");
        log.debug("fileName = " + fileName);
        try {
            File file = new File(fileName);
            result = loadExpirationConfiguration(file);
        }
        catch (Exception e)
        {
            log.error("unable to load PCConfiguration file", e);
            e.printStackTrace();
        }
        return result;
     }
     public ExpirationConfiguration loadExpirationConfiguration(File file)
     {
         log.debug("loadExpirationConfiguration(File)");
            ExpirationConfiguration result = null;
            int defaultConfiguration = -1;
            
            try
            {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(file);
                doc.getDocumentElement().normalize();

                
                log.debug("Root element " + doc.getDocumentElement().getNodeName());
                
                
                String defaultUnits = "";
                Node defaultDurationNode = doc.getElementsByTagName("expirations").item(0).getAttributes().getNamedItem("defaultDuration");
                Node defaultUnitNode = doc.getElementsByTagName("expirations").item(0).getAttributes().getNamedItem("defaultUnits");

                if(defaultDurationNode != null)
                {
                    defaultConfiguration = Integer.parseInt(defaultDurationNode.getTextContent());
                }
                if(defaultUnitNode != null)
                {
                    defaultUnits = defaultUnitNode.getTextContent();
                }

                
                result = new ExpirationConfiguration(defaultConfiguration, defaultUnits);

                NodeList nodeLst = doc.getElementsByTagName("expiration");

                for(int x = 0; x<nodeLst.getLength(); x++)
                {
                    Node item = nodeLst.item(x);
                    String aa = item.getAttributes().getNamedItem("assigningAuthority").getTextContent();
                    String units = item.getAttributes().getNamedItem("unit").getTextContent();
                    String duration = item.getTextContent();

                    Expiration expItem = new Expiration(aa, units, Integer.parseInt(duration));

                    result.getExpirations().add(expItem);
                }
            }
            catch (Exception e)
            {
                log.error("unable to load PCConfiguration file", e);
                e.printStackTrace();
            }

            return result;
     }

     public Expiration loadConfiguration(ExpirationConfiguration config, String assigningAuthority)
     {
         Expiration result = null;

         if(config != null)
         {
             for(Expiration item : config.getExpirations())
             {
                if (item.getAssigningAuthority().equalsIgnoreCase(assigningAuthority))
                {
                    result = item;
                }
             }

             if(result == null)
             {
                 if(config.getDefaultDuration() >=0 && config.getDefaultUnits().length() > 0)
                 {
                     result = new Expiration("", config.getDefaultUnits(), config.getDefaultDuration());
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
