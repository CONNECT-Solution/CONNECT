/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/AuditableEventQueryProcessor.java,v 1.9 2005/03/29 09:22:56 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.AuditableEventQueryType;
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
class AuditableEventQueryProcessor extends RegistryObjectQueryProcessor {
    
    public AuditableEventQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
    
    
    /**
     * Builds the SQL clauses for the sub-queries of this FilterQuery.
     *
     */
    protected String processSubQueries() throws RegistryException {
        String subQueryPredicate = super.processSubQueries();

        /*
        //RegistryObjectQuerys
        List roQueries = ((AuditableEventQueryType)filterQuery).getAffectedObjectQuery();
         
        if (roQueries != null) {
         
            Iterator iter = roQueries.iterator();
            while (iter.hasNext()) {
                FilterQueryType subQuery = (FilterQueryType)iter.next();
         
                RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, subQuery);
                String roAlias = getAliasForTable("RegistryObject"); 
                roQueryProcessor.setAlias(roAlias);
                
                String roPred = roQueryProcessor.process() + " ";
                
                String relationShipPred = "(" + alias + ".id = " + nodeAlias + ".id) ";
                subQueryPredicate = appendPredicate(subQueryPredicate, roPred, relationShipPred);            
            }
        }
         */
        
        //eventTypeQuery
        //SELECT ro.* FROM AuditableEvent e, ClassificationNode n WHERE ((e.eventTypeType = n.id) AND (n ...))
        RegistryObjectQueryType roQuery = ((AuditableEventQueryType)filterQuery).getEventTypeQuery();
         
        if (roQuery != null) {         
            RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);

            String nodeAlias = getAliasForTable("ClassificationNode"); 
            roQueryProcessor.setAlias(nodeAlias);
            
            String nodeQueryPred = roQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".eventType = " + nodeAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, nodeQueryPred, relationShipPred);            
        }
        
        //UserQuery
        //SELECT ro.* FROM AuditableEvent e, User_ u WHERE ((e.user_ = u.id) AND (u ...))
        roQuery = ((AuditableEventQueryType)filterQuery).getUserQuery();
         
        if (roQuery != null) {         
            RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);

            String userAlias = getAliasForTable("User_"); 
            roQueryProcessor.setAlias(userAlias);
            
            String userQueryPred = roQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".user_ = " + userAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, userQueryPred, relationShipPred);            
        }
        
        return subQueryPredicate;
    }
    
}
