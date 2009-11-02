/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/ClassificationSchemeQueryProcessor.java,v 1.9 2005/03/29 09:22:56 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import java.util.Iterator;
import java.util.List;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.ClassificationSchemeQueryType;
import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.RegistryObjectQueryType;


/**
 * Processor for ClassificationSchemeQuerys.
 * Processes a ClassificationSchemeQueryType and converts it to an SQL Query string
 * using the process() method.
 *
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
class ClassificationSchemeQueryProcessor extends RegistryObjectQueryProcessor {
    
    public ClassificationSchemeQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
    
    
    /**
     * Builds the SQL clauses for the sub-queries of this FilterQuery.
     *
     * SELECT ro.* FROM RegistryObject ro WHERE ro.id IN (SELECT c.id FROM Classification c WHERE c.registryObject = ro.id AND c.value = "somevalue");
     *
     */
    protected String processSubQueries() throws RegistryException {
        String subQueryPredicate = super.processSubQueries();
                
        //ChildrenQuerys
        List childQueries = ((ClassificationSchemeQueryType)filterQuery).getChildrenQuery();
         
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
        
        
        //NodeTypeQuery
        //SELECT ro.* FROM ClassScheme s, ClassificationNode n WHERE ((s.nodeType = n.id) AND (n ...))
        RegistryObjectQueryType roQuery = ((ClassificationSchemeQueryType)filterQuery).getNodeTypeQuery();
         
        if (roQuery != null) {         
            RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);

            String nodeAlias = getAliasForTable("ClassificationNode"); 
            roQueryProcessor.setAlias(nodeAlias);
            
            String nodeQueryPred = roQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".nodeType = " + nodeAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, nodeQueryPred, relationShipPred);            
        }
        return subQueryPredicate;
    }
    
}
