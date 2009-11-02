/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/StringFilterProcessor.java,v 1.2 2005/03/28 18:06:59 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.FilterType;
import org.oasis.ebxml.registry.bindings.query.StringFilterType;


/**
 * A FilterProcessor for string valued attributes
 * 
 * @author Farrukh Najmi
 *
 */
class StringFilterProcessor extends SimpleFilterProcessor {
        
    public StringFilterProcessor(FilterQueryProcessor parentQueryProcessor, FilterType filter) throws RegistryException {
        super(parentQueryProcessor, filter);
    }   
    
    protected String getValue() throws RegistryException {
        String value = "'" + ((StringFilterType)filter).getValue() + "'";
                        
        return value;
    }
    
}
