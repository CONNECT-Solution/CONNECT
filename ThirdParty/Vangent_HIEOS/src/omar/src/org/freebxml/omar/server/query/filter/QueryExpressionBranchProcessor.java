/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/QueryExpressionBranchProcessor.java,v 1.2 2005/03/28 18:06:59 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.FilterQueryType;


/**
 * BranchProcessor for processing InternationStringBranchTypes.
 * Processes a InternationStringBranchType and converts it to an SQL predicate
 * using the process() method.
 *
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
class QueryExpressionBranchProcessor extends BranchProcessor {
    
    public QueryExpressionBranchProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
    
    /**
     * Gets the primary key column name for a RegistryObject
     */
    String getPrimaryKeyColumn() {
        //TODO: Primary key can consist of multiple columns (parent, name, sequenceId). How to handle this?
        return "parent";        
    }
    
}
