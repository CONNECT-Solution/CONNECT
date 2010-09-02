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

package gov.hhs.fha.nhinc.dte;

import java.io.ByteArrayOutputStream;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.handler.MessageContext;
import javax.xml.soap.SOAPMessage;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.w3c.dom.Element;
/**
 *
 * @author rayj
 */

//todo: delete and use version Neil is writting
public class WebServiceContextHelper {
        private static Log log = LogFactory.getLog(WebServiceContextHelper.class);

        //todo: throw exception
    public  Element extractSoapMessage(WebServiceContext context)
    {
        String extractedMessage = null;
        @SuppressWarnings("unchecked")
        MessageContext msgContext = context.getMessageContext();
        if(msgContext != null)
        {
            javax.servlet.http.HttpServletRequest servletRequest = (javax.servlet.http.HttpServletRequest)msgContext.get(MessageContext.SERVLET_REQUEST);
            SOAPMessage soapMessage = (SOAPMessage)servletRequest.getAttribute("subscribeSoapMessage");
            if(soapMessage != null)
            {
                log.debug("******** Attempting to write out SOAP message *************");
                try
                {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    soapMessage.writeTo(bos);
                    extractedMessage = new String(bos.toByteArray());
                    log.debug("Extracted soap message: " + extractedMessage);
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

        Element messageElement=null;
        try {
            messageElement = XmlUtility.convertXmlToElement(extractedMessage);
        } catch (Exception ex) {
            log.error("failed to convert soap xml to element", ex);
        }
        return messageElement;
    }
}
