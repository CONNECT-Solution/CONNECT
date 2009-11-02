/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/PersonQueryProcessor.java,v 1.3 2005/03/29 09:22:57 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import java.util.Iterator;
import java.util.List;
import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.FilterType;
import org.oasis.ebxml.registry.bindings.query.PersonQueryType;


/**
 * Processor for ExternalIdentifierQuerys.
 * Processes a ExternalIdentifierQueryType and converts it to an SQL Query string
 * using the process() method.
 *
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
class PersonQueryProcessor extends RegistryObjectQueryProcessor {
    
    public PersonQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
    
    
    /**
     * Builds the SQL predicates for the primary and additional filters for this FilterQuery
     *
     */
    protected String processFilters() throws RegistryException {
        //Processing of primary filter if any are done by super class
        String filterPredicates = super.processFilters();        
                
        //AddressFilter
        //SELECT p.* FROM Person p, PostalAddress a WHERE ((a.parent = p.id) AND (a ...));         
        List addressFilters = ((PersonQueryType)filterQuery).getAddressFilter();
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
        
        //PersonNameFilter is special case since PersoName table is flattened into Person table
        //Treat like PrimaryFilter
        //SELECT p.* FROM Person p, PersonName n WHERE ((n.parent = p.id) AND (n ...));         
        FilterType pnameFilter = ((PersonQueryType)filterQuery).getPersonNameFilter();
        if (pnameFilter != null) {
            FilterProcessor filterProcessor = (FilterProcessor)FilterProcessor.newInstance(this, pnameFilter);
            String filterPred = filterProcessor.process() + " ";
            filterPredicates = appendPredicate(filterPredicates, filterPred);                        
        }        
        
        //TelephoneNumberFilter
        //SELECT p.* FROM Person p, TelephoneNumber t WHERE ((t.parent = p.id) AND (t ...));         
        List phoneFilters = ((PersonQueryType)filterQuery).getTelephoneNumberFilter();
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
        List emailFilters = ((PersonQueryType)filterQuery).getEmailAddressFilter();
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
    
}
