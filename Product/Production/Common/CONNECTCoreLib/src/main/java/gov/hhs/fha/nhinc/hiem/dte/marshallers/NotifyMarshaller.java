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

import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class NotifyMarshaller {
    private static final String ContextPath = "org.oasis_open.docs.wsn.b_2";

    public Element marshal(Notify object) {
        return new Marshaller().marshal(object, ContextPath);
    }

    public Notify unmarshal(Element element) {
        return (Notify) new Marshaller().unmarshal(element, ContextPath);
    }

}
