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
    private Element convertStringToElement(String xml)
    {
        javax.xml.parsers.DocumentBuilderFactory dbf;
        org.w3c.dom.Document doc = null;

        try
        {
            dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        }
        catch (Exception ex)
        {

        }

        return doc.getDocumentElement();
    }
}

