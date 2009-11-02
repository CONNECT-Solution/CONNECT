/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/ClassificationNodeQueryProcessor.java,v 1.11 2005/03/29 09:22:56 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import java.util.Iterator;
import java.util.List;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.ClassificationNodeQueryType;
import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.RegistryObjectQueryType;


/**
 * Processor for ExternalIdentifierQuerys.
 * Processes a ExternalIdentifierQueryType and converts it to an SQL Query string
 * using the process() method.
 *
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
class ClassificationNodeQueryProcessor extends RegistryObjectQueryProcessor {
    
    public ClassificationNodeQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
                
    /**
     * Builds the SQL clauses for the sub-queries of this FilterQuery.
     *
     *
     */
    protected String processSubQueries() throws RegistryException {
        String subQueryPredicate = super.processSubQueries();
                
        //ParentQuery        
        //SELECT n.* FROM ClassificationNode n, RegistryObject ro WHERE ((n.parent = ro.id) AND (ro ...));
        RegistryObjectQueryType roQuery = ((ClassificationNodeQueryType)filterQuery).getParentQuery();
         
        if (roQuery != null) {         
            RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);

            String parentAlias = getAliasForTable("RegistryObject"); 
            roQueryProcessor.setAlias(parentAlias);
            
            String parentQueryPred = roQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".parent = " + parentAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, parentQueryPred, relationShipPred);            
        }        
        
        //ChildrenQuerys
        List childQueries = ((ClassificationNodeQueryType)filterQuery).getChildrenQuery();
         
        if (childQueries != null) {
         
            Iterator iter = childQueries.iterator();
            while (iter.hasNext()) {
                FilterQueryType subQuery = (FilterQueryType)iter.next();
         
                ClassificationNodeQueryProcessor childQueryProcessor = (ClassificationNodeQueryProcessor)FilterQueryProcessor.newInstance(this, subQuery);
                String alias = getAliasForTable("ClassificationNode"); 
                childQueryProcessor.setAlias(alias);
                childQueryProcessor.setForeignKeyColumn("parent");
                
                String childPred = childQueryProcessor.process() + " ";
                subQueryPredicate = appendPredicate(subQueryPredicate, childPred);
            }
        }        
        
        return subQueryPredicate;
    }
    
}
