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

package gov.hhs.fha.nhinc.xmlCommon;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class NamespaceContextMapperFromElement implements NamespaceContext {
    private Element element = null;

    public NamespaceContextMapperFromElement(Element element) {
        this.element = element;
    }

    public String getNamespaceURI(String prefix) {
        return element.lookupNamespaceURI(prefix);
    }

    // This method isn't necessary for XPath processing.
    public String getPrefix(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // This method isn't necessary for XPath processing.
    public Iterator getPrefixes(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
