/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/InternationalStringBranchProcessor.java,v 1.8 2005/03/28 18:06:59 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import java.util.Iterator;
import java.util.List;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.FilterType;
import org.oasis.ebxml.registry.bindings.query.InternationalStringBranchType;


/**
 * BranchProcessor for processing InternationStringBranchTypes.
 * Processes a InternationStringBranchType and converts it to an SQL predicate
 * using the process() method.
 *
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
class InternationalStringBranchProcessor extends BranchProcessor {
    
    public InternationalStringBranchProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
            
    /**
     * Builds the SQL predicates for the primary and additional filters for this Branch
     *
     * SELECT ro.* FROM RegistryObject ro, VersionInfo v WHERE (ro.status == "<status>") AND (v.parent = ro.id AND v.versionName = "1.2");
     *
     */
    protected String processFilters() throws RegistryException {
        //Processing of primary filter if any are done by super class
        String filterPredicates = super.processFilters();
        
        //Process secondary filters if any
        List localizedStringFilters = ((InternationalStringBranchType)filterQuery).getLocalizedStringFilter();
        Iterator iter = localizedStringFilters.iterator();
        while (iter.hasNext()) {
            FilterType localizedStringFilter = (FilterType)iter.next();
            if (localizedStringFilter != null) {            
                FilterProcessor localizedStringFilterProcessor = FilterProcessor.newInstance(this, localizedStringFilter);
                                
                filterPredicates = appendPredicate(filterPredicates, localizedStringFilterProcessor.process() + " ");
            }     
        }
                        
        return filterPredicates;
    }
    
    /**
     * Gets the primary key column name for a RegistryObject
     */
    String getPrimaryKeyColumn() {
        //TODO: Primary key can consist of multiple columns (parent, lang). How to handle this?
        return "parent";        
    }
}
