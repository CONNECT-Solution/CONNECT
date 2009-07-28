package gov.hhs.fha.nhinc.hiem.util;

import javax.xml.ws.WebServiceContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.ws.handler.MessageContext;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.NodeList;

/**
 *
 *
 * @author Neil Webb
 */
public class SoapUtil
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SoapUtil.class);

//    public void extractReferenceParameters(SOAPMessageContext context, String attributeName)
//    {
//        log.debug("******** In handleMessage() *************");
//        SOAPMessage soapMessage = null;
//        String soapMessageText = null;
//        try
//        {
//            if(context != null)
//            {
//                log.debug("******** Context was not null *************");
//                soapMessage = context.getMessage();
//                log.debug("******** After getMessage *************");
//
//                if(soapMessage != null)
//                {
//                    log.debug("******** Attempting to write out SOAP message *************");
//                    try
//                    {
//                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                        soapMessage.writeTo(bos);
//                        soapMessageText = bos.toString();
//                        log.debug("Captured soap message: " + soapMessageText);
//                    }
//                    catch (Throwable t)
//                    {
//                        log.debug("Exception writing out the message");
//                        t.printStackTrace();
//                    }
//                }
//                else
//                {
//                    log.debug("SOAPMessage was null");
//                }
//            }
//            else
//            {
//                log.debug("SOAPMessageContext was null.");
//            }
//        }
//        catch(Throwable t)
//        {
//            log.debug("Error logging the SOAP message: " + t.getMessage());
//            t.printStackTrace();
//        }
//        if(soapMessage != null)
//        {
//            @SuppressWarnings("unchecked")
//            javax.servlet.http.HttpServletRequest servletRequest = (javax.servlet.http.HttpServletRequest)context.get(MessageContext.SERVLET_REQUEST);
//            servletRequest.setAttribute(attributeName, soapMessage);
//        }
//    }
//
//    private SOAPMessage extractSoapMessageObject(WebServiceContext context, String attributeName)
//    {
//        SOAPMessage soapMessage = null;
//        if(context != null)
//        {
//            MessageContext msgContext = context.getMessageContext();
//            if(msgContext != null)
//            {
//                javax.servlet.http.HttpServletRequest servletRequest = (javax.servlet.http.HttpServletRequest)msgContext.get(MessageContext.SERVLET_REQUEST);
//                soapMessage = (SOAPMessage)servletRequest.getAttribute(attributeName);
//            }
//        }
//        return soapMessage;
//    }

//    public String extractSoapMessage(WebServiceContext context, String attributeName)
//    {
//        String extractedMessage = null;
//
//        SOAPMessage soapMessage = extractSoapMessageObject(context, attributeName);
//        if(soapMessage != null)
//        {
//            log.debug("******** Attempting to write out SOAP message *************");
//            try
//            {
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                soapMessage.writeTo(bos);
//                extractedMessage = new String(bos.toByteArray());
//                log.debug("Extracted soap message: " + extractedMessage);
//            }
//            catch (Throwable t)
//            {
//                log.debug("Exception writing out the message");
//                t.printStackTrace();
//            }
//        }
//        else
//        {
//            log.debug("SOAPMessage was null");
//        }
//        return extractedMessage;
//    }
//
//    private Element findElement(NodeList nodes, String elementName)
//    {
//        Element e = null;
//        if((nodes != null) && (elementName != null))
//        {
//            for(int i = 0; i < nodes.getLength(); i++)
//            {
//                Node node = nodes.item(i);
//                if((node != null) && (node instanceof Element))
//                {
//                    Element tmpElement = (Element)node;
//                    String tmpElementName = tmpElement.getLocalName();
//                    if(elementName.equalsIgnoreCase(tmpElementName))
//                    {
////                        tmpElement.
//                        e = tmpElement;
//                    }
//                    else if ((node.getChildNodes() != null) && (node.getChildNodes().getLength() > 0))
//                    {
//                        e = findElement(node.getChildNodes(), elementName);
//                    }
//                    if(e != null)
//                    {
//                        break;
//                    }
//                }
//            }
//        }
//        return e;
//    }

//    public Element extractFirstElement(WebServiceContext context, String attributeName, String elementName)
//    {
//        Element firstElement = null;
//        SOAPMessage soapMessage = extractSoapMessageObject(context, attributeName);
//        if(soapMessage != null)
//        {
//            try
//            {
//                // Dump body
//                SOAPBody soapBody = soapMessage.getSOAPBody();
//                // Dump namespaces
//                Iterator namespaceIter = soapBody.getVisibleNamespacePrefixes();
//                if(namespaceIter != null)
//                {
//                    while(namespaceIter.hasNext())
//                    {
//                        String namespacePrefix = (String)namespaceIter.next();
//                        String namespace = soapBody.getNamespaceURI(namespacePrefix);
//                        log.debug("Namespace - " + namespacePrefix + ":" + namespace);
//                    }
//                }
//
////                NodeList childNodes = soapBody.getChildNodes();
//                firstElement = findElement(soapBody.getChildNodes(), elementName);
//
//                // Get first element (should be Subscribe and dump
////                Node subscribeNode = soapBody.getFirstChild();
////                if((subscribeNode != null) && (subscribeNode instanceof Element))
////                {
////                    String bNamespaceVal = subscribeNode.lookupNamespaceURI("b");
////                    log.debug("The namespace value for b: " + bNamespaceVal);
//
////                    NamedNodeMap subAttrMap = subscribeNode.getAttributes();
////                    if(subAttrMap != null)
////                    {
////                        int length = subAttrMap.getLength();
////                        for(int i = 0; i < length; i++)
////                        {
////                            Node attrNode = subAttrMap.item(i);
////                            log.debug("Attribute = " + attrNode.getNodeName() + ":" + attrNode.getNodeValue());
////                        }
////                        Attr testAttr = subscribeNode.getOwnerDocument().createAttribute("xmlns:b");
////                        testAttr.setNodeValue("http://docs.oasis-open.org/wsn/b-2");
////                        subAttrMap.setNamedItem(testAttr);
////                        testAttr = subscribeNode.getOwnerDocument().createAttribute("xmlns:b");
////                        testAttr.setNodeValue("http://docs.oasis-open.org/wsn/b-2");
////                        subAttrMap.setNamedItem(testAttr);
////                    }
//
//
////                    OutputFormat outputFormat = new OutputFormat(soapBody.getOwnerDocument());
////                    outputFormat.setIndenting(true);
////                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
////                    XMLSerializer serializer = new XMLSerializer(bos, outputFormat);
////                    Element subscribeElement = (Element)subscribeNode;
//
////                    try
////                    {
////                        serializer.serialize(targetElement);
////                    }
////                    catch (IOException ioe)
////                    {
////                        log.error("IOException serializing message: " + ioe.getMessage(), ioe);
////                    }
////                    rawMessage = bos.toString();
////                    log.debug("Serialized message: " + rawMessage);
//
////                }
////                else
////                {
////                    log.debug("Subscribe node invalid");
////                }
//            }
//            catch (SOAPException ex)
//            {
//                log.error("SOAPException reading soap body: " + ex.getMessage(), ex);
//            }
//
//        }
//        return firstElement;
//    }

}
