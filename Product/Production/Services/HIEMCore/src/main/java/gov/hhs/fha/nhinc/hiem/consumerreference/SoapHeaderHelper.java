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

package gov.hhs.fha.nhinc.hiem.consumerreference;

import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author akong
 *
 */
public class SoapHeaderHelper {
    
    private static Log log = LogFactory.getLog(SoapHeaderHelper.class);
    
    public SoapMessageElements getSoapHeaderElements(WebServiceContext context) {
        return getSoapHeaderElements(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
    }
    
    public SoapMessageElements getSoapHeaderElements(WebServiceContext context, String messageContextAttributeName) {
        SoapMessageElements referenceParameters = null;
        SOAPHeader header = null;
        try {
            log.debug("extract soapheader");
            SoapUtil soaputil = new SoapUtil();
            header = soaputil.extractSoapHeader(context, messageContextAttributeName);
            log.debug("Header:"+header.toString());
        } catch (SOAPException ex) {
            log.error("failed to extract soapheader", ex);
        }

        log.debug(XmlUtility.formatElementForLogging("soapheader", header));

        if (header != null) {
            log.debug("converting soap header to reference parameters elements");
            referenceParameters = getSoapHeaderElements(header);
            log.debug("extracted " + referenceParameters.getElements().size() + " reference parameter(s)");
        } else {
            log.warn("unable to extract soap header - assume no reference parameters");
        }
        return referenceParameters;
    }

    public SoapMessageElements getSoapHeaderElements(Element soapHeader) {
        log.debug("initialize reference parameters from soap header");
        SoapMessageElements elements = new SoapMessageElements();
        if (soapHeader != null) {
            for (int i = 0; i < soapHeader.getChildNodes().getLength(); i++) {
                Node node = soapHeader.getChildNodes().item(i);
                if (node instanceof Element) {
                    Element singleSoapHeader = (Element) node;
                    log.debug("single soap header: [" + XmlUtility.serializeElementIgnoreFaults(singleSoapHeader) + "]");
                    elements.getElements().add(singleSoapHeader);
                }
            }
        }
        log.debug("reference parameters elements found = " + elements.getElements().size());
        for ( Element element : elements.getElements())
           log.debug("Elements are :"+element.toString());
        return elements;
    }

    public SoapMessageElements getSoapHeaderElementsFromSoapMessage(Element soapMessage) {
        SoapMessageElements elements = null;
        if (soapMessage != null) {
            Element soapHeader = XmlUtility.getSingleChildElement(soapMessage,
                    "http://schemas.xmlsoap.org/soap/envelope/", "Header");
            elements = getSoapHeaderElements(soapHeader);
        }
        return elements;
    }
}
