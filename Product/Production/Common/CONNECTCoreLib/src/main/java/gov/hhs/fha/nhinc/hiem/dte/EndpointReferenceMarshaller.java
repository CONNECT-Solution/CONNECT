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
package gov.hhs.fha.nhinc.hiem.dte;

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

    private static final String EndpointReferenceContextPath = "org.w3._2005._08.addressing";

    public Element marshal(EndpointReferenceType object) {
        org.w3._2005._08.addressing.ObjectFactory addrObjFact = new org.w3._2005._08.addressing.ObjectFactory();
        JAXBElement<EndpointReferenceType> jaxb = addrObjFact.createEndpointReference(object);
        MarshallerHelper marshaller = new MarshallerHelper();
        Element element = marshaller.marshal(jaxb, EndpointReferenceContextPath);
        return element;
    }

    public EndpointReferenceType unmarshal(Element element) {
        EndpointReferenceType endpointReference = null;
        MarshallerHelper marshaller = new MarshallerHelper();
        Object object = marshaller.unmarshal(element, EndpointReferenceContextPath);
        if (object instanceof EndpointReferenceType) {
            endpointReference = (EndpointReferenceType) object;
        } else if (object instanceof JAXBElement) {
            JAXBElement jaxb = (JAXBElement) object;
            Object jaxbValue = jaxb.getValue();
            if (jaxbValue instanceof EndpointReferenceType) {
                endpointReference = (EndpointReferenceType) object;
            }
        }
        return endpointReference;
    }
}
