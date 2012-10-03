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

import org.oasis_open.docs.wsn.b_2.TopicExpressionType;
import javax.xml.bind.JAXBElement;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import java.io.StringReader;

/**
 * 
 * @author dunnek
 */
public class TopicMarshaller {

    private static final String ContextPath = "org.oasis_open.docs.wsn.b_2";

    public org.w3c.dom.Element marshal(TopicExpressionType object) {
        org.oasis_open.docs.wsn.b_2.ObjectFactory objectFactory = new org.oasis_open.docs.wsn.b_2.ObjectFactory();
        JAXBElement<TopicExpressionType> jaxb = objectFactory.createTopic(object);
        Marshaller marshaller = new Marshaller();
        Element element = marshaller.marshal(jaxb, ContextPath);
        return element;
    }

    public TopicExpressionType unmarshal(String xml) {
        return unmarshal(convertStringToElement(xml));
    }

    public TopicExpressionType unmarshal(Element element) {
        Marshaller marshaller = new Marshaller();
        return (TopicExpressionType) marshaller.unmarshallJaxbElement(element, ContextPath);
    }

    private Element convertStringToElement(String xml) {
        javax.xml.parsers.DocumentBuilderFactory dbf;
        org.w3c.dom.Document doc = null;

        try {
            dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (Exception ex) {

        }

        return doc.getDocumentElement();
    }
}
