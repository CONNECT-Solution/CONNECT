/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.dte;

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import javax.xml.ws.handler.MessageContext;
import javax.xml.soap.SOAPMessage;
import gov.hhs.fha.nhinc.hiem.processor.nhin.NhinSubscribeProcessor;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
