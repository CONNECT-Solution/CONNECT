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

import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class WsntUnsubscribeMarshaller {

    private static final String ContextPath = "org.oasis_open.docs.wsn.b_2";

    public Element marshal(Unsubscribe object) {
        return new Marshaller().marshal(object, ContextPath);
    }

    public Unsubscribe unmarshal(Element element) {
        return (Unsubscribe) new Marshaller().unmarshal(element, ContextPath);
    }
}
