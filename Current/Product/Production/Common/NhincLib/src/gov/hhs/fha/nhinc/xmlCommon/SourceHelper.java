/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.xmlCommon;

import java.io.ByteArrayOutputStream;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Node;

/**
 *
 * @author rayj
 */
public class SourceHelper {

    public static Node SourceToXml(Source source) throws Exception {
        //todo: find more efficient way to transform to node
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        javax.xml.transform.Result streamResult = new javax.xml.transform.stream.StreamResult(bos);

        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(source, streamResult);

        String resultXml = new String(bos.toByteArray());
        Node resultNode = XmlUtility.convertXmlToElement(resultXml);
        return resultNode;
    }
    public static Source XmlToSource(Node node) throws Exception {
        Source source = new DOMSource(node);
        return source;
    }
}
