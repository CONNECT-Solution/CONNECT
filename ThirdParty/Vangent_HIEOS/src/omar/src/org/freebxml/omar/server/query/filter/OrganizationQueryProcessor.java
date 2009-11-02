/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/OrganizationQueryProcessor.java,v 1.9 2005/03/29 09:22:57 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import java.util.Iterator;
import java.util.List;
import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.FilterType;
import org.oasis.ebxml.registry.bindings.query.OrganizationQueryType;
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
class OrganizationQueryProcessor extends RegistryObjectQueryProcessor {
    
    public OrganizationQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
    
    
    /**
     * Builds the SQL predicates for the primary and additional filters for this FilterQuery
     *
     * SELECT ro.* FROM RegistryObject ro, VersionInfo v WHERE (ro.status == "<status>") AND (v.parent = ro.id AND v.versionName = "1.2");
     *
     */
    protected String processFilters() throws RegistryException {
        //Processing of primary filter if any are done by super class
        String filterPredicates = super.processFilters();        
        
        //AddressFilter
        //SELECT p.* FROM Person p, PostalAddress a WHERE ((a.parent = p.id) AND (a ...));         
        List addressFilters = ((OrganizationQueryType)filterQuery).getAddressFilter();
        if (addressFilters != null) {
            Iterator iter = addressFilters.iterator();
            while (iter.hasNext()) {
                FilterType filter = (FilterType)iter.next();
                String addressAlias = getAliasForTable("PostalAddress");
                FilterProcessor filterProcessor = (FilterProcessor)FilterProcessor.newInstance(this, filter);
                filterProcessor.setAlias(addressAlias);
                String filterPred = filterProcessor.process() + " ";
                String relationshipPred = "(" + alias + ".id = " + addressAlias + ".parent) ";
                filterPredicates = appendPredicate(filterPredicates, filterPred, relationshipPred);
            }            
        }        
        
        //TelephoneNumberFilter
        //SELECT p.* FROM Person p, TelephoneNumber t WHERE ((t.parent = p.id) AND (t ...));         
        List phoneFilters = ((OrganizationQueryType)filterQuery).getTelephoneNumberFilter();
        if (phoneFilters != null) {
            Iterator iter = phoneFilters.iterator();
            while (iter.hasNext()) {
                FilterType filter = (FilterType)iter.next();
                String phoneAlias = getAliasForTable("TelephoneNumber");
                FilterProcessor filterProcessor = (FilterProcessor)FilterProcessor.newInstance(this, filter);
                filterProcessor.setAlias(phoneAlias);
                String filterPred = filterProcessor.process() + " ";
                String relationshipPred = "(" + alias + ".id = " + phoneAlias + ".parent) ";
                filterPredicates = appendPredicate(filterPredicates, filterPred, relationshipPred);
            }            
        }        
        
        //EmailAddressFilter
        //SELECT p.* FROM Person p, EmailAddress a WHERE ((a.parent = p.id) AND (a ...));         
        List emailFilters = ((OrganizationQueryType)filterQuery).getEmailAddressFilter();
        if (emailFilters != null) {
            Iterator iter = emailFilters.iterator();
            while (iter.hasNext()) {
                FilterType filter = (FilterType)iter.next();
                String emailAlias = getAliasForTable("EmailAddress");
                FilterProcessor filterProcessor = (FilterProcessor)FilterProcessor.newInstance(this, filter);
                filterProcessor.setAlias(emailAlias);
                String filterPred = filterProcessor.process() + " ";
                String relationshipPred = "(" + alias + ".id = " + emailAlias + ".parent) ";
                filterPredicates = appendPredicate(filterPredicates, filterPred, relationshipPred);
            }            
        }
        
        
        return filterPredicates;
    }
    
    /**
     * Processes branches, if any, of this RegistryObjectQuery and
     * builds the SQL clauses for those branches.
     *
     * Avoid Nested SELECT due to performance issue and lack of support on some dbs.
     * Use style (1) while avoiding style(2) below:
     *
     * 1. SELECT ro.* FROM RegistryObject ro, Name nm WHERE (nm.parent = ro.id AND nm.value LIKE "%sun%");
     * 2. SELECT ro.* FROM RegistryObject ro WHERE (ro.id IN (SELECT nm.parent From Name_ nm WHERE nm.value LIKE "%sun%"));
     *
     */
    protected String processBranches() throws RegistryException {
        String branchPredicate = super.processBranches();
                
        return branchPredicate;
    }
    
    /**
     * Builds the SQL clauses for the sub-queries of this FilterQuery.
     *
     * SELECT ro.* FROM RegistryObject ro WHERE ro.id IN (SELECT c.id FROM Classification c WHERE c.registryObject = ro.id AND c.value = "somevalue");
     *
     */
    protected String processSubQueries() throws RegistryException {
        String subQueryPredicate = super.processSubQueries();
                
        //ParentQuery        
        //SELECT o.* FROM Organization o, Organization p WHERE ((o.parent = p.id) AND (ro ...));
        RegistryObjectQueryType roQuery = ((OrganizationQueryType)filterQuery).getParentQuery();
         
        if (roQuery != null) {         
            RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);

            String parentAlias = getAliasForTable("Organization"); 
            roQueryProcessor.setAlias(parentAlias);
            
            String parentQueryPred = roQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".parent = " + parentAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, parentQueryPred, relationShipPred);            
        }        
        
        //ChildOrganizationQuerys
        List childQueries = ((OrganizationQueryType)filterQuery).getChildOrganizationQuery();
         
        if (childQueries != null) {
         
            Iterator iter = childQueries.iterator();
            while (iter.hasNext()) {
                FilterQueryType subQuery = (FilterQueryType)iter.next();
         
                ClassificationNodeQueryProcessor childQueryProcessor = (ClassificationNodeQueryProcessor)FilterQueryProcessor.newInstance(this, subQuery);
                String childAlias = getAliasForTable("Organization"); 
                childQueryProcessor.setAlias(childAlias);
                
                String childPred = childQueryProcessor.process() + " ";
                
                String relationShipPred = "(" + alias + ".id = " + childAlias + ".parent) ";
                subQueryPredicate = appendPredicate(subQueryPredicate, childPred, relationShipPred);            
            }
        }        
        
        //PrimaryContactQuery
        //SELECT ro.* FROM Organization o, User_ u WHERE ((o.primaryContact = u.id) AND (u ...))
        roQuery = ((OrganizationQueryType)filterQuery).getPrimaryContactQuery();
         
        if (roQuery != null) {         
            RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);

            String userAlias = getAliasForTable("User_"); 
            roQueryProcessor.setAlias(userAlias);
            
            String userQueryPred = roQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".primaryContact = " + userAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, userQueryPred, relationShipPred);            
        }
        
        
        return subQueryPredicate;
    }
    
}
