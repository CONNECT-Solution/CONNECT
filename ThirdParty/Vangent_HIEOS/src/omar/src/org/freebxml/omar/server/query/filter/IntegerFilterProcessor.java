/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/IntegerFilterProcessor.java,v 1.3 2005/03/29 12:15:50 doballve Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.FilterType;
import org.oasis.ebxml.registry.bindings.query.IntegerFilterType;


/**
 * A FilterProcessor for Integer valued attributes
 * 
 * @author Farrukh Najmi
 *
 */
class IntegerFilterProcessor extends SimpleFilterProcessor {
        
    public IntegerFilterProcessor(FilterQueryProcessor parentQueryProcessor, FilterType filter) throws RegistryException {
        super(parentQueryProcessor, filter);
    }   
    
    protected String getValue() throws RegistryException {
        String value = String.valueOf((((IntegerFilterType)filter).getValue()).floatValue());
                        
        return value;
    }
    
}
