/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org. All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/event/SubscriptionMatcher.java,v 1.16 2006/03/21 19:45:50 doballve Exp $
 *
 * ====================================================================
 */
package org.freebxml.omar.server.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.freebxml.omar.common.BindingUtility;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.persistence.PersistenceManager;
import org.freebxml.omar.server.persistence.PersistenceManagerFactory;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.query.ResponseOptionType;
import org.oasis.ebxml.registry.bindings.query.ReturnType;
import org.oasis.ebxml.registry.bindings.rim.AdhocQueryType;
import org.oasis.ebxml.registry.bindings.rim.AuditableEventType;
import org.oasis.ebxml.registry.bindings.rim.QueryExpressionType;
import org.oasis.ebxml.registry.bindings.rim.SubscriptionType;


/**
 * Given an AuditableEvent find all Subscriptions whhose selectors potentially match the AuditableEvent. This avoids having to check every single subsription and is an important scalability design element.
 *
 * TODO: Reliable delivery and retries.
 * @author <a href="mailto:Farrukh.Najmi@Sun.COM">Farrukh S. Najmi</a>
 */
public class SubscriptionMatcher {
    
    PersistenceManager pm = PersistenceManagerFactory.getInstance().getPersistenceManager();
    ResponseOptionType responseOption = null;
    List objectRefs = new ArrayList();
    
    HashMap queryToObjectsMap = new HashMap();

    SubscriptionMatcher() throws RegistryException {
        try {
            responseOption = BindingUtility.getInstance().queryFac.createResponseOption();
            responseOption.setReturnType(ReturnType.LEAF_CLASS);
        }
        catch (javax.xml.bind.JAXBException e) {
            throw new RegistryException(e);
        }
    }
            
    /*
     * Gets the Map of Subscriptions that definitely match the specified event.
     * Map.Entry key = subscription object
     * Map.Entry value = subscribed objects Collection
     */
    HashMap getMatchedSubscriptionsMap(ServerRequestContext context, AuditableEventType ae) throws RegistryException {
        
        HashMap matchedSubscriptionsMap = new HashMap();
        
        queryToObjectsMap.clear();
       
        List matchedQuerys = getMatchedQuerys(context, ae);

        if (matchedQuerys.size() > 0) {
            StringBuffer ids = BindingUtility.getInstance().getIdListFromRegistryObjects(matchedQuerys);

            //Get all Subscriptions that use the matched querys as selectors        
            String query = "SELECT s.* FROM Subscription s WHERE s.selector IN ( " + ids + " )";
            List objectRefs = new ArrayList();
            List matchedSubscriptions = pm.executeSQLQuery(context, query, responseOption, "Subscription", objectRefs);
            
            Iterator iter = matchedSubscriptions.iterator();
            while (iter.hasNext()) {
                SubscriptionType subscription = (SubscriptionType)iter.next();
                
                matchedSubscriptionsMap.put(subscription, queryToObjectsMap.get(subscription.getSelector()));
            }
        }
        
        return matchedSubscriptionsMap;
    }
    
    /**
     * Gets the List of AdhocQuery that match the specified event. 
     * Initialized the matchedQueryMap with queries matching the event
     * as keys and objects matching the query as values.
     *
     */
    private List getMatchedQuerys(ServerRequestContext context, AuditableEventType ae) throws RegistryException {
        List matchedQuerys = new ArrayList();
        
        List targetQuerys = getTargetQuerys(context, ae);
        List affectedObjects = ae.getAffectedObjects().getObjectRef();
        
        Iterator iter = targetQuerys.iterator();
        while (iter.hasNext()) {
            AdhocQueryType query = (AdhocQueryType)iter.next();
            
            if (queryMatches(context, query, ae.getId(), affectedObjects)) {
                matchedQuerys.add(query);                
            }
        }
        return matchedQuerys;
    }
    
    /*
     * Determines whether a specified target (potentially matching) query 
     * actually matches the list of affectedObjects or not.
     *
     */
    private boolean queryMatches(ServerRequestContext context, AdhocQueryType query, String currentEventId, List affectedObjects) throws RegistryException {
        boolean match = false;        
        
        QueryExpressionType queryExp = query.getQueryExpression();
        String queryLang = queryExp.getQueryLanguage();
        if (queryLang.equals(BindingUtility.CANONICAL_QUERY_LANGUAGE_ID_SQL_92)) {
            String queryStr = (String)queryExp.getContent().get(0);

            //Special parameter replacement for $currentEventId
            queryStr = queryStr.replaceAll("\\$currentEventId", currentEventId);
            
            //Get objects that match the selector query (selectedObjects)
            List selectedObjects = pm.executeSQLQuery(context, queryStr, responseOption, "RegistryObject", objectRefs);
            
            //match is true if the affectedObjects  are a sub-set of the selectedObjects
            List selectedObjectIds = BindingUtility.getInstance().getIdsFromRegistryObjects(selectedObjects);
            List affectedObjectIds = BindingUtility.getInstance().getIdsFromRegistryObjects(affectedObjects);
            
            if (selectedObjectIds.size() > 0) {
                Iterator iter = affectedObjectIds.iterator();
                while (iter.hasNext()) {
                    if (selectedObjectIds.contains(iter.next())) {
                        match = true;
                        break;
                    }                    
                }
            }
            
            //Now remember which objects matched this query
            queryToObjectsMap.put(query.getId(), selectedObjects);
        }
        else {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.onlySQLQuerySupported"));
        }
        
        return match;
    }
    
    
    /*
     * Gets the List of AdhocQueries that potentially match the specifid event.
     * This is an essential filtering mechanism to achieve scalability by narrowing
     * the number of Subscriptions to test for a match.
     * <p>
     * Gets all AdhocQuerys that have event type and primary partition 
     * matching this event.
     *
     * TODO: We need to get the List of objectType for affectedObjects somehow
     * to help the filtering. This is a spec issue at the moment because AuditableEvent
     * only contains ObjectRefs and not the actual objects.
     *
     * </p>
     */
    private List getTargetQuerys(ServerRequestContext context, AuditableEventType ae) throws RegistryException {   
        List querys= null;
        
        try {
            String eventType = ae.getEventType();

            ResponseOptionType responseOption = BindingUtility.getInstance().queryFac.createResponseOption();
            responseOption.setReturnType(ReturnType.LEAF_CLASS);
            List objectRefs = new ArrayList();

            //TODO: Filter down further based upon primary partitions in future to scale better.

            //Get those AdhocQuerys that match the events eventType or have no eventType specified
            String query = "SELECT q.* FROM AdhocQuery q, Subscription s WHERE q.id = s.selector AND ((q.query LIKE '%eventType%=%" + eventType + "%') OR (q.query NOT LIKE '%eventType%=%'))";
            querys = pm.executeSQLQuery(context, query, responseOption, "AdhocQuery", objectRefs);

        }
        catch (javax.xml.bind.JAXBException e) {
            throw new RegistryException(e);
        }
        
        return querys;
    }
    
}
