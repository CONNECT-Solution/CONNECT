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
package gov.hhs.fha.nhinc.assemblymanager.utils;

import java.io.StringWriter;
import java.util.List;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import nux.xom.pool.XOMUtil;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.POCDMT000040Person;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;

/**
 * 
 * @author kim
 */
public class XMLUtil {
    private static Log log = LogFactory.getLog(XMLUtil.class);

    public static byte[] toCanonicalXMLBytes(POCDMT000040ClinicalDocument pObject) throws JAXBException {
        ObjectFactory factory = new ObjectFactory();
        JAXBContextHandler oHandler = new JAXBContextHandler();
        JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
        Marshaller oMarshaller = oJaxbContext.createMarshaller();
        // JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
        // Marshaller marshaller = jc.createMarshaller();
        nu.xom.Document xmlDocument = XOMUtil.jaxbMarshal(oMarshaller, factory.createClinicalDocument(pObject));
        return XOMUtil.toCanonicalXML(xmlDocument);
    }

    public static String toCanonicalXMLString(POCDMT000040ClinicalDocument pObject) throws JAXBException {
        ObjectFactory factory = new ObjectFactory();
        JAXBContextHandler oHandler = new JAXBContextHandler();
        JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
        Marshaller oMarshaller = oJaxbContext.createMarshaller();
        // JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
        // Marshaller marshaller = jc.createMarshaller();
        nu.xom.Document xmlDocument = XOMUtil.jaxbMarshal(oMarshaller, factory.createClinicalDocument(pObject));
        return new String(XOMUtil.toCanonicalXML(xmlDocument));
    }

    public static String toPrettyXML(POCDMT000040ClinicalDocument pObject) {
        try {
            ObjectFactory factory = new ObjectFactory();
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller oMarshaller = oJaxbContext.createMarshaller();
            // JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
            // Marshaller marshaller = jc.createMarshaller();
            nu.xom.Document xmlDocument = XOMUtil.jaxbMarshal(oMarshaller, factory.createClinicalDocument(pObject));
            return XOMUtil.toPrettyXML(xmlDocument);
        } catch (JAXBException ex) {
            return "Unparsable - " + ex.getMessage();
        }
    }

    public static String toPrettyXML(CareRecordQUPCIN043200UV01ResponseType pObject) {
        try {
            ObjectFactory factory = new ObjectFactory();
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller oMarshaller = oJaxbContext.createMarshaller();
            // JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
            // Marshaller marshaller = jc.createMarshaller();
            nu.xom.Document xmlDocument = XOMUtil.jaxbMarshal(oMarshaller,
                    factory.createCareRecordQUPCIN043200UV01Response(pObject));
            return XOMUtil.toPrettyXML(xmlDocument);
        } catch (JAXBException ex) {
            return "Unparsable - " + ex.getMessage();
        }
    }

    public static Element createElement(String tag) throws Exception {
        DocumentBuilderFactory oDOMFactory = null;
        DocumentBuilder oDOMBuilder = null;
        org.w3c.dom.Document oDOMDocument = null;

        oDOMFactory = DocumentBuilderFactory.newInstance();
        // oDOMFactory.setNamespaceAware(true);
        oDOMBuilder = oDOMFactory.newDocumentBuilder();
        oDOMDocument = oDOMBuilder.newDocument();

        Element oElement = oDOMDocument.createElement(tag);

        return oElement;
    }

    public static void setName(List<ENExplicit> names, POCDMT000040Person person) {
        // org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        PNExplicit personName = null;

        if (names.size() > 0) {
            ENExplicit name = names.get(0);
            personName = HL7DataTransformHelper.convertENtoPN(name);
        }

        person.getName().add(personName);
    }

    public static org.w3c.dom.Element marshalToElement(javax.xml.bind.JAXBElement o) throws Exception {
        Document doc = null;
        if (o == null) {
            return null;
        }

        JAXBContextHandler oHandler = new JAXBContextHandler();
        JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
        Marshaller oMarshaller = oJaxbContext.createMarshaller();
        // JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
        java.io.StringWriter sw = new StringWriter();
        // Marshaller m = jc.createMarshaller();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.newDocument();
        oMarshaller.marshal(o, doc);
        return (doc == null ? null : doc.getDocumentElement());
    }

    public static String toCanonicalXML(JAXBElement a) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
        java.io.StringWriter sw = new StringWriter();
        Marshaller marshaller = jc.createMarshaller();
        nu.xom.Document xmlDocument = XOMUtil.jaxbMarshal(marshaller, a);
        return new String(XOMUtil.toCanonicalXML(xmlDocument));
    }
}
