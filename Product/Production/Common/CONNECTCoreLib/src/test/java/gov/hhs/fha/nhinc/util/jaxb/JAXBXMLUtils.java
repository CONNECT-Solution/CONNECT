/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.util.jaxb;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author sopawar
 */
public class JAXBXMLUtils
{
    JAXBContext context = null;

    public JAXBXMLUtils()
    {

    }

    /**
     * This method takes in an XML String and returns an JAXB object with get and set methods.
     * parameters: xmlStr - The passed in XML String
     * parameters: validate - Flag if the validation has to be enabled or not
     */
    public Object parseXML(String xmlStr, String namespace) throws JAXBException
    {
        if (xmlStr == null ||  xmlStr.isEmpty())
        {
            throw new JAXBException("XML passed for parsing is Null or Empty.");
        }

        // create a JAXBContext capable of handling classes generated into
        JAXBContext jc = getJAXBContext(namespace);
        Unmarshaller u = jc.createUnmarshaller();

        Object obj = u.unmarshal(new StreamSource(new StringReader(xmlStr)));
        return obj;
    }

    /**
     *  This method takes in a JAXB object and returns an XMLString
     *  parameters: Object - The JAXB object which has to converted to xml
     */
    public String getXML(Object obj, String namespace) throws JAXBException
    {
        if (obj == null)
        {
            throw new JAXBException("The object passed to parse is null.");
        }

        JAXBContext jc = getJAXBContext(namespace);
        Marshaller m = jc.createMarshaller();

        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

        StringWriter strWrite = new StringWriter();
        m.marshal(obj, strWrite);

        return strWrite.toString();
    }


    private JAXBContext getJAXBContext(String nameSpace) throws JAXBException
    {
        if (context == null)
        {
            context = JAXBContext.newInstance(getDefaultPackageName(nameSpace));
        }
        return context;
    }


    private String getDefaultPackageName(String nameSpace)
    {
        StringBuffer result = new StringBuffer();

        /*URL is easy to use since it does the tokenizing for the namespace and also provides error checks*/
        URL nameSpaceToURL;

        if (!nameSpace.startsWith("http"))
        {
            return nameSpace;
        }

        try {
            nameSpaceToURL = new URL(nameSpace);

            //get the hostname
            String hostToken = nameSpaceToURL.getHost();

            /*
                This part of the code reverses the hostname.
             */
            StringTokenizer tokens = new StringTokenizer(hostToken, ".", false);
            String packageName = "";
            boolean firstElementFlag = true;
            while (tokens.hasMoreElements())
            {
                String element = (String) tokens.nextElement();
                if (firstElementFlag)
                {
                    packageName = element.toLowerCase();
                    firstElementFlag = false;
                }
                else
                {
                    packageName = element.toLowerCase() + "." + packageName;
                }
            }

            //append the path to the packageName from above
            result = result.append(packageName).append(nameSpaceToURL.getPath().toLowerCase().replace('/', '.'));
            //System.out.println("Package Name: " + result.toString());
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return result.toString();
    }
}
