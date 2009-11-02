/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/AssociationQueryProcessor.java,v 1.9 2005/03/29 09:22:56 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.AssociationQueryType;
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
class AssociationQueryProcessor extends RegistryObjectQueryProcessor {
    
    public AssociationQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
            
    /**
     * Builds the SQL clauses for the sub-queries of this FilterQuery.
     *
     * SELECT ro.* FROM Association a, RegistryObject src ((a.sourceObject = src.id) AND (src.id == "srcId"));
     *
     */
    protected String processSubQueries() throws RegistryException {
        String subQueryPredicate = super.processSubQueries();
                
        //AssociationTypeQuerys       
        RegistryObjectQueryType assTypeQuery = ((AssociationQueryType)filterQuery).getAssociationTypeQuery();
         
        if (assTypeQuery != null) {
                  
            RegistryObjectQueryProcessor subQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, assTypeQuery);
            String assTypeAlias = getAliasForTable("ClassificationNode"); 
            subQueryProcessor.setAlias(assTypeAlias);

            String assTypeQueryPred = subQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".associationType = " + assTypeAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, assTypeQueryPred, relationShipPred);            
        }
        
        //SourceObjectQuerys       
        RegistryObjectQueryType srcQuery = ((AssociationQueryType)filterQuery).getSourceObjectQuery();
         
        if (srcQuery != null) {
                  
            RegistryObjectQueryProcessor subQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, srcQuery);
            String srcAlias = getAliasForTable("RegistryObject"); 
            subQueryProcessor.setAlias(srcAlias);

            String srcQueryPred = subQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".sourceObject = " + srcAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, srcQueryPred, relationShipPred);            
        }
        
        //TargetObjectQuerys       
        RegistryObjectQueryType targetQuery = ((AssociationQueryType)filterQuery).getTargetObjectQuery();
         
        if (targetQuery != null) {
                  
            RegistryObjectQueryProcessor subQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, targetQuery);
            String targetAlias = getAliasForTable("RegistryObject"); 
            subQueryProcessor.setAlias(targetAlias);

            String targetQueryPred = subQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".targetObject = " + targetAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, targetQueryPred, relationShipPred);            
        }        
        
        return subQueryPredicate;
    }
    
}
