/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.async;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;

/**
 *
 * @author JHOPPESC
 */
public class AsyncMessageHandler implements SOAPHandler<SOAPMessageContext> {

    private static final String WSA_PREFIX = "wsa";
    private static final String WSA_NS = "http://www.w3.org/2005/08/addressing";

    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    public boolean handleMessage(SOAPMessageContext messageContext) {
        Boolean outboundProperty = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        try {
            SOAPMessage oMessage = messageContext.getMessage();
            SOAPHeader oHeader = oMessage.getSOAPHeader();

            if (outboundProperty.booleanValue()) {

                if (messageContext.containsKey(NhincConstants.ASYNC_MSG_TYPE_PROP) == true) {
                    String msgType = (String) messageContext.get(NhincConstants.ASYNC_MSG_TYPE_PROP);

                    if (msgType.contentEquals(NhincConstants.ASYNC_REQUEST_MSG_TYPE_VAL)) {
                        System.out.println("Detected an asynchronous request message");
                        // Override the Message Id field
                        String messageId = null;

                        if (messageContext.containsKey(NhincConstants.ASYNC_MESSAGE_ID_PROP) == true) {
                            messageId = (String) messageContext.get(NhincConstants.ASYNC_MESSAGE_ID_PROP);

                            System.out.println("Setting message ID to " + messageId);

                            // Steps that need to be performed
                            SOAPElement oMessageIdElem = getFirstChild(oHeader, "MessageID", WSA_NS);
                            if (oMessageIdElem != null) {
                                oMessageIdElem.setTextContent(messageId);
                            }
                        }
                    } else if (msgType.contentEquals(NhincConstants.ASYNC_RESPONSE_MSG_TYPE_VAL)) {
                        System.out.println("Detected an asynchronous response message");
                        // Override the Relates To Id field
                        String relatesToId = null;

                        if (messageContext.containsKey(NhincConstants.ASYNC_RELATES_TO_PROP) == true) {
                            relatesToId = (String) messageContext.get(NhincConstants.ASYNC_RELATES_TO_PROP);

                            System.out.println("Setting relates to ID to " + relatesToId);

                            // Steps that need to be performed
                            SOAPElement relatesToElem = oHeader.addChildElement("RelatesTo", WSA_PREFIX, WSA_NS);
                            relatesToElem.setTextContent(relatesToId);
                        }
                    } else {
                        System.out.println("Detected an synchronous request message");
                    }
                } else {
                    System.out.println("Detected an synchronous request message");
                }

            } else {
                // Do nothing for an inbound message
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
        SOAPElement result = null;
        QName qname = new QName(ns, name);
        Iterator iter = header.getChildElements(qname);
        if (iter.hasNext()) {
            result = (SOAPElement) iter.next();
        }
        return result;
    }
}
