/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.unsubscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author webbn
 */
public class HiemUnsubscribeSoapHeaderHandler implements SOAPHandler<SOAPMessageContext>
{
    private static Log log = LogFactory.getLog(HiemUnsubscribeSoapHeaderHandler.class);

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
        try
        {
            String subscriptionId = null;
            if(context != null)
            {
                log.debug("******** Context was not null *************");
                SOAPMessage soapMessage = context.getMessage();
                log.debug("******** After getMessage *************");

                if(soapMessage != null)
                {
                    log.debug("******** Attempting to write out SOAP message *************");
                    try
                    {
                        soapMessage.writeTo(System.out);
                        log.debug("******** After call to write out SOAP message *************");
                        
                        SOAPHeader soapHeader = soapMessage.getSOAPHeader();
                        if(soapHeader != null)
                        {
                            NodeList headerNodes = soapHeader.getChildNodes();
                            log.debug("Header node count: " + headerNodes.getLength());
                            if(headerNodes != null)
                            {
                                for(int i = 0; i < headerNodes.getLength(); i++)
                                {
                                    Node node = headerNodes.item(i);
                                    processNode(node, context);
                                }
                            }
                            else
                            {
                                log.debug("Header nodes list was null");
                            }
                        }
                        else
                        {
                            log.debug("Soap header was null");
                        }
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
//            if(subscriptionId != null)
//            {
//                log.debug("Setting subscription id ----------------");
//                @SuppressWarnings("unchecked")
//                Map<String, List<String>> msgCtxMap = (Map<String, List<String>>)context.get(MessageContext.HTTP_REQUEST_HEADERS);
//                if(msgCtxMap != null)
//                {
//                    List<String> refParams = new ArrayList<String>();
//                    refParams.add(subscriptionId);
//                    msgCtxMap.put("Subscriptionid", refParams);
//                    log.debug("Added subscription id ###############################");
//                }
//            }
        }
        catch(Throwable t)
        {
            log.debug("Error logging the SOAP message: " + t.getMessage());
            t.printStackTrace();
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
