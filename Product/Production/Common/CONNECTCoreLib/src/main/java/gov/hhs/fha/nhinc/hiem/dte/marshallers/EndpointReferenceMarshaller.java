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

import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class EndpointReferenceMarshaller {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(Marshaller.class);
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
            log.warn("failed to parse xml", ex);
        }
        return unmarshal(element, contextPath);
    }


    public EndpointReferenceType unmarshal(Element element) {
      return unmarshal(element, EndpointReferenceContextPath);
    }
}
