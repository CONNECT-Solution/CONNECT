/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nhinc.wsn;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author webbn
 */
public class SubscribeHeaderHandler implements SOAPHandler<SOAPMessageContext>{
private static Log log = LogFactory.getLog(SubscribeHeaderHandler.class);

    @SuppressWarnings("unchecked")
    public Set<QName> getHeaders()
    {
        return Collections.EMPTY_SET;
    }

    public boolean handleMessage(SOAPMessageContext context)
    {
        extractReferenceParameters(context);
        return true;
    }

    public boolean handleFault(SOAPMessageContext context)
    {
        return true;
    }

    public void close(MessageContext context)
    {
    }

    private void extractReferenceParameters(SOAPMessageContext context)
    {
        log.debug("******** In handleMessage() *************");
        SOAPMessage soapMessage = null;
        String soapMessageText = null;
        try
        {
            if(context != null)
            {
                log.debug("******** Context was not null *************");
                soapMessage = context.getMessage();
                log.debug("******** After getMessage *************");

                if(soapMessage != null)
                {
                    log.debug("******** Attempting to write out SOAP message *************");
                    try
                    {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        soapMessage.writeTo(bos);
                        soapMessageText = bos.toString();
                        log.debug("Captured soap message: " + soapMessageText);
                    }
                    catch (Throwable t)
                    {
                        log.debug("Exception writing out the message");
                        t.printStackTrace();
                    }
                }
                else
                {
                    log.debug("SOAPMessage was null");
                }
            }
            else
            {
                log.debug("SOAPMessageContext was null.");
            }
        }
        catch(Throwable t)
        {
            log.debug("Error logging the SOAP message: " + t.getMessage());
            t.printStackTrace();
        }
        if(soapMessage != null)
        {
            @SuppressWarnings("unchecked")
            javax.servlet.http.HttpServletRequest servletRequest = (javax.servlet.http.HttpServletRequest)context.get(MessageContext.SERVLET_REQUEST);
            servletRequest.setAttribute("subscribeSoapMessage", soapMessage);
        }
    }

    private void processNode(Node node, SOAPMessageContext context)
    {
        if(node != null)
        {
            log.debug("Node name: " + node.getNodeName());
            log.debug("Node local name: " + node.getLocalName());
            log.debug("Node prefix: " + node.getPrefix());
            log.debug("Node namespace URI: " + node.getNamespaceURI());
            log.debug("Node value: " + node.getNodeValue());
            log.debug("Node text content: " + node.getTextContent());
            
            if("SubscriptionId".equalsIgnoreCase(node.getLocalName()))
            {
                String subscriptionId = node.getTextContent();
                
                @SuppressWarnings("unchecked")
                Map<String, List<String>> msgCtxMap = (Map<String, List<String>>)context.get(MessageContext.HTTP_REQUEST_HEADERS);
                if(msgCtxMap != null)
                {
                    List<String> refParams = new ArrayList<String>();
                    refParams.add(subscriptionId);
                    msgCtxMap.put("Subscriptionid", refParams);
                    log.debug("Added subscription id ###############################");
                }
            }
            if(node.hasChildNodes())
            {
                NodeList childNodes = node.getChildNodes();
                log.debug("Node has " + childNodes.getLength() + " child nodes");
                for(int i = 0; i < childNodes.getLength(); i++)
                {
                    Node childNode = childNodes.item(i);
                    processNode(childNode, context);
                }
            }
        }
    }
}
