/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nhinc.wsn.helpers;

import javax.xml.namespace.QName;
import javax.xml.transform.dom.DOMResult;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.w3c.dom.*;

/**
 *
 * @author rayj
 */
public class XmlHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(XmlHelper.class);

    public static String getNodeValue(Node node) {
        String nodeValue = "";
        if (node != null) {
            nodeValue = node.getTextContent();
            if (nodeValue != null) {
                nodeValue = nodeValue.trim();
            }
        }
        return nodeValue;
    }

    public static String getNodeValue(Document document, String elementName) {
        return getNodeValue(getSingleNode(document, elementName));
    }

    public static NodeList getNodeList(Document document, String elementName) {
        NodeList nodeList = null;
        if (document != null) {
            nodeList = document.getElementsByTagName(elementName);
        }
        return nodeList;
    }

    public static Node getSingleNode(Document document, String elementName) {
        Node node = null;
        NodeList nodeList = getNodeList(document, elementName);
        if ((nodeList != null) && (nodeList.getLength() > 0)) {
            node = nodeList.item(0);
        }
        return node;
    }

    public static NodeList getSingleNodeChildren(Document document, String elementName) {
        NodeList childrenNodeList = null;
        Node parentNode = null;
        NodeList parentNodeList = getNodeList(document, elementName);
        if ((parentNodeList != null) && (parentNodeList.getLength() > 0)) {
            parentNode = parentNodeList.item(0);
        }
        if (parentNode != null) {
            childrenNodeList = parentNode.getChildNodes();
        }
        return childrenNodeList;
    }

    public static Document transformEndpointReferenceToDocument(W3CEndpointReference endpointRef) {
        DOMResult domresult = new DOMResult();
        endpointRef.writeTo(domresult);
        Node endpointNode = domresult.getNode();

        Document endpointNodeDoc = (Document) endpointNode;
        return endpointNodeDoc;
    }
}

