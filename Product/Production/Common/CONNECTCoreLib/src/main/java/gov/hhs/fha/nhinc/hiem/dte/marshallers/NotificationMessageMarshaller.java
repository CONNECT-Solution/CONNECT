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

import javax.xml.bind.JAXBElement;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class NotificationMessageMarshaller {
    private static final String ContextPath = "org.oasis_open.docs.wsn.b_2";

    public Element marshal(NotificationMessageHolderType object) {
        org.oasis_open.docs.wsn.b_2.ObjectFactory objectFactory = new org.oasis_open.docs.wsn.b_2.ObjectFactory();
        JAXBElement<NotificationMessageHolderType> jaxb = objectFactory.createNotificationMessage(object);
        Marshaller marshaller = new Marshaller();
        Element element = marshaller.marshal(jaxb, ContextPath);
        return element;
    }

    public NotificationMessageHolderType unmarshal(Element element) {
        NotificationMessageHolderType unmarshalledObject=null;
        Marshaller marshaller = new Marshaller();
        Object object = marshaller.unmarshal(element, ContextPath);
        if (object instanceof NotificationMessageHolderType) {
            unmarshalledObject = (NotificationMessageHolderType) object;
        } else if (object instanceof JAXBElement) {
            JAXBElement jaxb = (JAXBElement) object;
            Object jaxbValue = jaxb.getValue();
            if (jaxbValue instanceof NotificationMessageHolderType) {
                unmarshalledObject = (NotificationMessageHolderType) jaxbValue;
            }
        }
        return unmarshalledObject;

    }

}
