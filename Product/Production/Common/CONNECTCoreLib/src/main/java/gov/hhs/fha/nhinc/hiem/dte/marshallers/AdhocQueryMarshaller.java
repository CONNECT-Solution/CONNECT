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

import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class AdhocQueryMarshaller {
    private static final String  ContextPath = "oasis.names.tc.ebxml_regrep.xsd.rim._3";

    public Element marshal(AdhocQueryType object) {
        return new Marshaller().marshal(object, ContextPath);
    }

    public AdhocQueryType unmarshal(Element element) {
        return (AdhocQueryType) new Marshaller().unmarshallJaxbElement(element, ContextPath);
    }

}
