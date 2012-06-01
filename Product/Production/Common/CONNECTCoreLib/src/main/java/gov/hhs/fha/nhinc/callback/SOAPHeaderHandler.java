/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.callback;

import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rhalfert
 */
public class SOAPHeaderHandler implements SOAPHandler<SOAPMessageContext>{

    private static Log log = LogFactory.getLog(SOAPHeaderHandler.class);
    private static final String WSA_PREFIX = "wsa";
    private static final String WSA_NS = "http://www.w3.org/2005/08/addressing";

    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    public boolean handleMessage(SOAPMessageContext messageContext) {
        log.debug("Entering AsynchMessageHandler.handleMessage");

        Boolean outboundProperty = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        try {
            SOAPMessage oMessage = messageContext.getMessage();
            SOAPHeader oHeader = oMessage.getSOAPHeader();

            if (outboundProperty.booleanValue()) {
                     // Override the Message Id field
                    String messageId = null;

                    if (messageContext.containsKey("com.sun.xml.ws.addressing.response.messageID") != true) {
                        messageId = (String) messageContext.get("com.sun.xml.ws.addressing.response.messageID");

                        if(NullChecker.isNullish(messageId)){
                            messageId = AsyncMessageIdCreator.generateMessageId();
                        } else if(illegalUUID(messageId)){
                            messageId = "urn:" + messageId;
                        }

                        System.out.println("Setting message ID to " + messageId);
                        
                        // Steps that need to be performed
                        SOAPElement oMessageIdElem = getFirstChild(oHeader, "MessageID", WSA_NS);
                        if (oMessageIdElem != null) {
                            oMessageIdElem.setTextContent(messageId);
                        }else{
                            SOAPFactory soapFactory = SOAPFactory.newInstance();
                            oMessageIdElem = soapFactory.createElement("MessageID", "", WSA_NS);
                            oMessageIdElem.setTextContent(messageId);
                            oHeader.addChildElement(oMessageIdElem);
                        }
                    }
                   
                } else {
                    System.out.println("Will not adjust messageID on inbound request");
                }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    public void close(MessageContext context) {
        // Do nothing
    }

    private static SOAPElement getFirstChild(SOAPHeader header, String name,
            String ns) {
        if(header == null || header.getChildNodes().getLength() == 0) return null;
        
        SOAPElement result = null;
        QName qname = new QName(ns, name);
        Iterator iter = header.getChildElements(qname);
        if (iter.hasNext()) {
            result = (SOAPElement) iter.next();
        }
        return result;
    }

    private boolean illegalUUID(String messageId) {
        return messageId.trim().startsWith("uuid:");
    }
}
