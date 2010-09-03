/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.nhin.notify;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Collections;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 *
 * @author Neil Webb
 */
public class NotifyHeaderHandler implements SOAPHandler<SOAPMessageContext> {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(NotifyHeaderHandler.class);

    @SuppressWarnings("unchecked")
    public Set<QName> getHeaders() {
        return Collections.EMPTY_SET;
    }

    public boolean handleMessage(SOAPMessageContext context) {
        new gov.hhs.fha.nhinc.hiem.dte.SoapUtil().extractReferenceParameters(context, NhincConstants.HIEM_NOTIFY_SOAP_HDR_ATTR_TAG);
        return true;
    }

    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    public void close(MessageContext context) {
    }

//    private void extractReferenceParameters(SOAPMessageContext context)
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
//            servletRequest.setAttribute(NhincConstants.HIEM_NOTIFY_SOAP_HDR_ATTR_TAG, soapMessage);
//        }
//    }
}
