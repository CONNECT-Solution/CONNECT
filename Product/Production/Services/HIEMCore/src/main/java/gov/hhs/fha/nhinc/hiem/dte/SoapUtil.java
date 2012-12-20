/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.hiem.dte;

import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

import java.io.ByteArrayOutputStream;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Element;
import org.apache.log4j.Logger;

/**
 *
 *
 * @author Neil Webb
 */
public class SoapUtil {

    private static final Logger LOG = Logger.getLogger(SoapUtil.class);

    /**
     * @param context
     * @param attributeName
     */
    public void saveSoapMessageToContext(SOAPMessageContext context, String attributeName) {
        LOG.debug("******** In handleMessage() *************");
        SOAPMessage soapMessage = null;
        String soapMessageText = null;
        try {
            if (context != null) {
                LOG.debug("******** Context was not null *************");
                soapMessage = context.getMessage();
                LOG.debug("******** After getMessage *************");

                if (soapMessage != null) {
                    LOG.debug("******** Attempting to write out SOAP message *************");
                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        soapMessage.writeTo(bos);
                        soapMessageText = bos.toString();
                        LOG.debug("Captured soap message: " + soapMessageText);
                    } catch (Throwable t) {
                        LOG.debug("Exception writing out the message");
                        t.printStackTrace();
                    }
                } else {
                    LOG.debug("SOAPMessage was null");
                }
            } else {
                LOG.debug("SOAPMessageContext was null.");
            }
        } catch (Throwable t) {
            LOG.debug("Error logging the SOAP message: " + t.getMessage());
            t.printStackTrace();
        }
        if (soapMessage != null) {
            javax.servlet.http.HttpServletRequest servletRequest = (javax.servlet.http.HttpServletRequest) context
                    .get(MessageContext.SERVLET_REQUEST);
            servletRequest.setAttribute(attributeName, soapMessage);
        }
    }

    private SOAPMessage extractSoapMessageObject(WebServiceContext context, String attributeName) {
        SOAPMessage soapMessage = null;
        if (context != null) {
            MessageContext msgContext = context.getMessageContext();
            if (msgContext != null) {
                javax.servlet.http.HttpServletRequest servletRequest = (javax.servlet.http.HttpServletRequest) msgContext
                        .get(MessageContext.SERVLET_REQUEST);
                soapMessage = (SOAPMessage) servletRequest.getAttribute(attributeName);
            }
        }
        return soapMessage;
    }

    public String extractSoapMessage(WebServiceContext context, String attributeName) {
        String extractedMessage = null;

        SOAPMessage soapMessage = extractSoapMessageObject(context, attributeName);
        if (soapMessage != null) {
            LOG.debug("******** Attempting to write out SOAP message *************");
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                soapMessage.writeTo(bos);
                extractedMessage = new String(bos.toByteArray());
                LOG.debug("Extracted soap message: " + extractedMessage);
            } catch (Throwable t) {
                LOG.error("Exception writing out the message", t);
            }
        } else {
            LOG.debug("SOAPMessage was null");
        }
        return extractedMessage;
    }

    public Element extractSoapMessageElement(WebServiceContext context, String attributeName) {
        String extractedMessage = extractSoapMessage(context, attributeName);

        Element messageElement = null;
        try {
            messageElement = XmlUtility.convertXmlToElement(extractedMessage);
        } catch (Exception ex) {
            LOG.error("failed to convert soap xml to element", ex);
        }
        return messageElement;
    }

    /**
     * @param WSBindingProvider port
     * @param ReferenceParametersElements referenceParametersElements
     */
    public SOAPHeader extractSoapHeader(WebServiceContext context, String attributeName) throws SOAPException {
        SOAPHeader header = null;
        SOAPMessage message = extractSoapMessageObject(context, attributeName);
        if (message != null) {
            header = message.getSOAPHeader();
        }
        return header;
    }    
}
