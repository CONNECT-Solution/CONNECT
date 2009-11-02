/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/ClassificationQueryProcessor.java,v 1.9 2005/03/29 09:22:56 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.ClassificationQueryType;
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
class ClassificationQueryProcessor extends RegistryObjectQueryProcessor {
    
    public ClassificationQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
            
    /**
     * Builds the SQL clauses for the sub-queries of this FilterQuery.
     *
     *
     */
    protected String processSubQueries() throws RegistryException {
        String subQueryPredicate = super.processSubQueries();
                
        //ClassificationSchemeQuery        
        //SELECT ro.* FROM Classification c, ClassificationScheme s WHERE ((c.classificationScheme = s.id) AND (s.id = "id"))
        RegistryObjectQueryType roQuery = ((ClassificationQueryType)filterQuery).getClassificationSchemeQuery();
         
        if (roQuery != null) {         
            RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);

            String schemeAlias = getAliasForTable("ClassScheme"); 
            roQueryProcessor.setAlias(schemeAlias);
            
            String schemeQueryPred = roQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".classificationScheme = " + schemeAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, schemeQueryPred, relationShipPred);            
        }
                
        //ClassifiedObjectQuery        
        //SELECT ro.* FROM Classification c, RegistryObject ro WHERE ((c.classifiedObject = ro.id) AND (ro.id = "id"))
        roQuery = ((ClassificationQueryType)filterQuery).getClassifiedObjectQuery();
         
        if (roQuery != null) {         
            RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);

            String roAlias = getAliasForTable("RegistryObject"); 
            roQueryProcessor.setAlias(roAlias);
            
            String roQueryPred = roQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".classifiedObject = " + roAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, roQueryPred, relationShipPred);            
        }
                
        //ClassificationNodeQuery        
        //SELECT ro.* FROM Classification c, ClassificationNode n WHERE ((c.classificationNode = n.id) AND (n ...))
        roQuery = ((ClassificationQueryType)filterQuery).getClassificationNodeQuery();
         
        if (roQuery != null) {         
            RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);

            String nodeAlias = getAliasForTable("ClassificationNode"); 
            roQueryProcessor.setAlias(nodeAlias);
            
            String nodeQueryPred = roQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".classificationNode = " + nodeAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, nodeQueryPred, relationShipPred);            
        }
        
        return subQueryPredicate;
    }
    
}
