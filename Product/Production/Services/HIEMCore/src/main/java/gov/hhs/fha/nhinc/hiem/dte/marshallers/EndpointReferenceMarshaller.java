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
package gov.hhs.fha.nhinc.hiem.dte.marshallers;

import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Element;

/**
 * 
 * @author rayj
 */
public class EndpointReferenceMarshaller {
    private static final Logger LOG = Logger.getLogger(Marshaller.class);
    private static final String EndpointReferenceContextPath = "org.w3._2005._08.addressing";

    public Element marshal(EndpointReferenceType object) {
        org.w3._2005._08.addressing.ObjectFactory addrObjFact = new org.w3._2005._08.addressing.ObjectFactory();
        JAXBElement<EndpointReferenceType> jaxb = addrObjFact.createEndpointReference(object);
        Marshaller marshaller = new Marshaller();
        Element element = marshaller.marshal(jaxb, EndpointReferenceContextPath);
        return element;
    }

    public EndpointReferenceType unmarshal(Element element, String contextPath) {
        EndpointReferenceType endpointReference = null;
        Marshaller marshaller = new Marshaller();
        Object object = marshaller.unmarshal(element, contextPath);
        if (object instanceof EndpointReferenceType) {
            endpointReference = (EndpointReferenceType) object;
        } else if (object instanceof JAXBElement) {
            JAXBElement jaxb = (JAXBElement) object;
            Object jaxbValue = jaxb.getValue();
            if (jaxbValue instanceof EndpointReferenceType) {
                endpointReference = (EndpointReferenceType) jaxbValue;
            }
        }
        return endpointReference;
    }

    public EndpointReferenceType unmarshal(String xml, String contextPath) {
        Element element = null;
        try {
            element = XmlUtility.convertXmlToElement(xml);
        } catch (Exception ex) {
            LOG.warn("failed to parse xml", ex);
        }
        return unmarshal(element, contextPath);
    }

    public EndpointReferenceType unmarshal(Element element) {
        return unmarshal(element, EndpointReferenceContextPath);
    }
}
