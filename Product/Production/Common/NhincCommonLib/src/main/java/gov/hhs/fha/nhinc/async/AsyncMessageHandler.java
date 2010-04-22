/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.async;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Collections;
import java.util.Set;
import javax.xml.namespace.QName;
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
                        // TODO:  Add code to set the message id
                        // Override the Message Id field
                        String messageId = null;

                        // if (messageContext.containsKey(NhincConstants.ASYNC_MESSAGE_ID_PROP) == true) {
                        //     messageId = (String)messageContext.get(NhincConstants.ASYNC_MESSAGE_ID_PROP);
                        // }
                        // System.out.println("Setting message ID to " + messageId);

                        // Steps that need to be performed
                        //   1.  Create Nhin header soap elements
                        //   2.  Create any child soap elements within the Nhin header
                        //   3.  Set the text content with the message id
                    } else if (msgType.contentEquals(NhincConstants.ASYNC_RESPONSE_MSG_TYPE_VAL)) {
                        System.out.println("Detected an asynchronous response message");
                        // TODO:  Add code to set the relates to id
                        // Override the Relates To Id field
                        String relatesToId = null;

                        // if (messageContext.containsKey(NhincConstants.ASYNC_RELATES_TO_PROP) == true) {
                        //     relatesToId = (String)messageContext.get(NhincConstants.ASYNC_RELATES_TO_PROP);
                        // }
                        // System.out.println("Setting relates to ID to " + relatesToId);

                        // Steps that need to be performed
                        //   1.  Create Nhin header soap elements
                        //   2.  Create any child soap elements within the Nhin header
                        //   3.  Set the text content with the relates to id
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
}
