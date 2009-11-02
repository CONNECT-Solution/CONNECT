/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/CompoundFilterProcessor.java,v 1.2 2005/03/28 18:06:59 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.CompoundFilterType;
import org.oasis.ebxml.registry.bindings.query.FilterType;

/**
 * FilterProcessor subtype for processing CompoundFilters
 * A CompoundFilter contains two sub-filters that are
 * combined using a logicalOPerator like "AND" or "OR".
 * 
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
class CompoundFilterProcessor extends FilterProcessor {
    
    public CompoundFilterProcessor(FilterQueryProcessor parentQueryProcessor, FilterType filter) throws RegistryException {
        super(parentQueryProcessor, filter);
    }
    
    
    /**
     * Builds the SQL predicates for the CompoundFilter.
     * Processes left and right filter and combine their predicates using logicalOperator.
     */
    public String processInternal() throws RegistryException {        
        String filterPredicate = null;
        
        FilterType leftFilter = ((CompoundFilterType)filter).getLeftFilter();
        FilterProcessor leftFilterProcessor = FilterProcessor.newInstance(parentQueryProcessor, leftFilter);
        
        FilterType rightFilter = ((CompoundFilterType)filter).getRightFilter();
        FilterProcessor rightFilterProcessor = FilterProcessor.newInstance(parentQueryProcessor, rightFilter);
        String logicalOp = ((CompoundFilterType)filter).getLogicalOperator().toString();
        
        filterPredicate = " (" + leftFilterProcessor.process() + " " + logicalOp + " " + rightFilterProcessor.process() + ") ";
        
        return filterPredicate;
   }            
}
