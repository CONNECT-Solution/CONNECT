/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/DateTimeFilterProcessor.java,v 1.2 2005/03/28 18:06:59 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.DateTimeFilterType;
import org.oasis.ebxml.registry.bindings.query.FilterType;


/**
 * A FilterProcessor for dateTime valued attributes
 * 
 * @author Farrukh Najmi
 *
 */
class DateTimeFilterProcessor extends SimpleFilterProcessor {
        
    public DateTimeFilterProcessor(FilterQueryProcessor parentQueryProcessor, FilterType filter) throws RegistryException {
        super(parentQueryProcessor, filter);
    }   
    
    protected String getValue() throws RegistryException {
        String value = "";
        
        Calendar calendar = ((DateTimeFilterType)filter).getValue();
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

        //??The timestamp is being truncated to work around a bug in PostgreSQL 7.2.2 JDBC driver
        value = timestamp.toString().substring(0, 19);
                
        return value;
    }
    
}
