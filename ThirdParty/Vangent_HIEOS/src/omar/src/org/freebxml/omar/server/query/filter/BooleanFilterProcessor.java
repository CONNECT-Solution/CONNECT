/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/BooleanFilterProcessor.java,v 1.2 2005/03/28 18:06:58 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.BooleanFilterType;
import org.oasis.ebxml.registry.bindings.query.FilterType;


/**
 * A FilterProcessor for boolean valued attributes
 * 
 * @author Farrukh Najmi
 *
 */
class BooleanFilterProcessor extends SimpleFilterProcessor {
    public BooleanFilterProcessor(FilterQueryProcessor parentQueryProcessor, FilterType filter) throws RegistryException {
        super(parentQueryProcessor, filter);
    }   
    
    protected String getValue() throws RegistryException {
        String value = Boolean.valueOf(((BooleanFilterType)filter).isValue()).toString();
                        
        return value;
    }
}
