package gov.hhs.fha.nhinc.repository.util;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Utility class for XML processing
 * 
 * @author Neil Webb
 */
public class XmlUtil
{
    public static Document getDocumentFromString(String xmlString)
        throws Exception
    {
        DocumentBuilderFactory oDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        try
        {
            // Do not load the DTD
            oDocumentBuilderFactory.setAttribute(
                "http://apache.org/xml/features/nonvalidating/load-external-dtd",
                Boolean.FALSE);
        }
        catch (IllegalArgumentException e)
        {
        }
        DocumentBuilder oDocumentBuilder = oDocumentBuilderFactory.newDocumentBuilder();

        InputSource inputSource = new InputSource(new StringReader(xmlString));
        return oDocumentBuilder.parse(inputSource);
    }
    
    public static Document getDocumentFromFile(String absolutePath)
        throws Exception
    {
        DocumentBuilderFactory oDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder oDocumentBuilder = oDocumentBuilderFactory.newDocumentBuilder();

        Document oDocument = null;

        oDocument = oDocumentBuilder.parse(new File(absolutePath));

        return oDocument;
    }
    
}
