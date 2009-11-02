/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/QueryManagerImpl.java,v 1.78 2008/02/28 17:11:40 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.UnsupportedCapabilityException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import org.freebxml.omar.common.CanonicalConstants;
import org.freebxml.omar.common.CanonicalSchemes;
import org.freebxml.omar.common.CommonResourceBundle;
import org.freebxml.omar.common.RepositoryItem;
import org.freebxml.omar.common.spi.QueryManager;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.exceptions.ObjectNotFoundException;
import org.freebxml.omar.common.spi.QueryPlugin;
import org.freebxml.omar.common.spi.RequestContext;
import org.freebxml.omar.server.cache.ServerCache;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.common.IterativeQueryParams;
/* HIEOS/BHT (REMOVED):
import org.freebxml.omar.server.cms.CMSManager;
import org.freebxml.omar.server.cms.CMSManagerImpl;
*/
import org.freebxml.omar.server.persistence.PersistenceManager;
import org.freebxml.omar.server.persistence.PersistenceManagerFactory;
import org.freebxml.omar.server.plugin.AbstractPluginManager;
/* HIEOS/BHT (REMOVED):
import org.freebxml.omar.server.query.federation.FederatedQueryManager;
 */
import org.freebxml.omar.server.repository.RepositoryManager;
import org.freebxml.omar.server.repository.RepositoryManagerFactory;
import org.freebxml.omar.server.security.authentication.AuthenticationServiceImpl;
/* HIEOS/BHT (REMOVED):
import org.freebxml.omar.server.security.authorization.AuthorizationResult;
import org.freebxml.omar.server.security.authorization.AuthorizationServiceImpl;
 */
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.query.AdhocQueryRequest;
import org.oasis.ebxml.registry.bindings.query.AdhocQueryRequestType;
import org.oasis.ebxml.registry.bindings.query.AdhocQueryResponseType;
import org.oasis.ebxml.registry.bindings.query.AdhocQueryResponse;
import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.ResponseOptionType;
import org.oasis.ebxml.registry.bindings.query.ReturnType;
import org.oasis.ebxml.registry.bindings.rim.AdhocQueryType;
import org.oasis.ebxml.registry.bindings.rim.ClassificationNode;
import org.oasis.ebxml.registry.bindings.rim.ClassificationScheme;
import org.oasis.ebxml.registry.bindings.rim.ExtrinsicObjectType;
import org.oasis.ebxml.registry.bindings.rim.IdentifiableType;
import org.oasis.ebxml.registry.bindings.rim.InternationalStringType;
import org.oasis.ebxml.registry.bindings.rim.LocalizedString;
import org.oasis.ebxml.registry.bindings.rim.QueryExpressionType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectListType;
import org.oasis.ebxml.registry.bindings.rim.RegistryPackage;
import org.oasis.ebxml.registry.bindings.rim.RegistryPackageType;
import org.oasis.ebxml.registry.bindings.rim.SlotListType;
import org.oasis.ebxml.registry.bindings.rim.SlotType1;
import org.oasis.ebxml.registry.bindings.rim.UserType;
import org.oasis.ebxml.registry.bindings.rim.Value;


/**
 * Implements the QueryManager interface for ebXML Registry as defined by ebRS spec.
 *
 * @author <a href="mailto:Farrukh.Najmi@Sun.COM">Farrukh S. Najmi</a>
 */
public class QueryManagerImpl extends AbstractPluginManager implements QueryManager {

    private static QueryManagerImpl instance = null;
    
    /* The logger */
    private static Log log = LogFactory.getLog(QueryManagerImpl.class.getName());
    
    /**
     * @directed
     */
    private org.freebxml.omar.server.query.filter.RRFilterQueryProcessor filterQueryProcessor =
        org.freebxml.omar.server.query.filter.RRFilterQueryProcessor.getInstance();

    /**
     * @directed
     */
    private org.freebxml.omar.server.query.sql.SQLQueryProcessor sqlQueryProcessor =
        org.freebxml.omar.server.query.sql.SQLQueryProcessor.getInstance();
    
    private RepositoryManager rm = RepositoryManagerFactory.getInstance().getRepositoryManager();

    /* HIEOS/BHT: REMOVED
    private FederatedQueryManager fqm = null;
    */
    
    //The prefix for properties that configure a QueryPlugin class
    public static final String QUERY_PLUGIN_PROPERTY_PREFIX = "omar.server.query.plugin";

    //The prefix for properties that configure a QueryFilterPlugin class
    public static final String QUERY_FILTER_PLUGIN_PROPERTY_PREFIX = "omar.server.query.filter.plugin";
    
    //Key: queryId value: QueryPlugin instance
    Map queryPluginsMap = new HashMap();
    
    boolean permitAllRead = Boolean.
        valueOf(RegistryProperties.getInstance().
                getProperty("omar.security.authorization.enableOverride.permitAllRead",
                            "true")).booleanValue();
    
    boolean bypassCMS = false;
    /* HIEOS/BHT (REMOVED):
    CMSManager cmsm = new CMSManagerImpl(); //CMSManagerFactory.getInstance().getContentManagementServiceManager();
    */
    boolean fetchChildObjsSrv = Boolean.valueOf(RegistryProperties.getInstance()
                    .getProperty("org.freebxml.omar.server.query.fetchChildObjects", "false")).booleanValue();
    
    protected QueryManagerImpl() {
        bypassCMS = Boolean.valueOf(RegistryProperties.getInstance()
            .getProperty("org.freebxml.omar.server.query.bypassCMS", "true")).booleanValue();
        
        //Create and cache QueryPlugins
        getQueryPlugins();
        
        //Create and cache QueryFilterPlugins
        getQueryFilterPlugins();
    }

    public synchronized static QueryManagerImpl getInstance() {
        if (instance == null) {
            instance = new QueryManagerImpl();
        }

        return instance;
    }

    /**
     * submitAdhocQuery
     */
    public AdhocQueryResponseType submitAdhocQuery(RequestContext context)
        throws RegistryException {
        org.oasis.ebxml.registry.bindings.query.AdhocQueryResponse ahqr = null;
        context = ServerRequestContext.convert(context);
        AdhocQueryRequestType req = (AdhocQueryRequestType)((ServerRequestContext)context).getCurrentRegistryRequest();
        org.oasis.ebxml.registry.bindings.query.ResponseOptionType responseOption = req.getResponseOption();
        ReturnType returnType = responseOption.getReturnType();
        
        UserType user = ((ServerRequestContext)context).getUser();
        
        //The result of the query
        RegistryObjectListType rolt= null;

        try {
            HashMap slotsMap = bu.getSlotsFromRequest(req);
            ahqr = null;
            
            //Process request for the case where it is a parameterized invocation of a stored query
            processForParameterizedQuery((ServerRequestContext)context);    
            
            //TODO: May need a better way than checking getSpecialQueryResults() to know if specialQuery was called.
            if (((ServerRequestContext)context).getSpecialQueryResults() != null) {
                ahqr = processForSpecialQueryResults((ServerRequestContext)context);
            } else {            
                //Check if it is a federated query and process it using FederatedQueryManager if so.
                /* HIEOS/BHT: REMOVED
                 boolean isFederated = req.isFederated();
                if (isFederated) {
                    //Initialize lazily. Otherwise we have an infinite create loop
                    if (fqm == null) {
                        fqm = FederatedQueryManager.getInstance();
                    }
                    ahqr = fqm.submitAdhocQuery((ServerRequestContext)context);
                } else */ {
                    int startIndex = req.getStartIndex().intValue();
                    int maxResults = req.getMaxResults().intValue();
                    IterativeQueryParams paramHolder = new IterativeQueryParams(startIndex, maxResults);

                    org.oasis.ebxml.registry.bindings.rim.AdhocQueryType adhocQuery = req.getAdhocQuery();
                    HashMap querySlotsMap = bu.getSlotsFromRegistryObject(adhocQuery);

                    QueryExpressionType queryExp = adhocQuery.getQueryExpression();
                    String queryLang = queryExp.getQueryLanguage();
                    if (queryLang.equals(BindingUtility.CANONICAL_QUERY_LANGUAGE_ID_SQL_92)) {
                        String queryStr = (String)queryExp.getContent().get(0);
                        queryStr = replaceSpecialVariables(user, queryStr);

                        //See if query slot specifies bypassSQLParser
                        boolean bypassSQLParserForQuery = false;
                        String bypassSQLParserForQueryStr = (String)querySlotsMap
                                .get("org.freebxml.omar.server.query.sql.SQLQueryProcessor.bypassSQLParser");
                        if (bypassSQLParserForQueryStr != null) {
                            bypassSQLParserForQuery = Boolean.valueOf(bypassSQLParserForQueryStr).booleanValue();
                        }
                        
                        rolt = sqlQueryProcessor.executeQuery((ServerRequestContext)context, user,
                                queryStr, responseOption, paramHolder, bypassSQLParserForQuery);
                        
                        processDepthParameter((ServerRequestContext)context);
                        
                        ahqr = BindingUtility.getInstance().queryFac.createAdhocQueryResponse();
                        ahqr.setRegistryObjectList(rolt);
                        ahqr.setStatus(BindingUtility.CANONICAL_RESPONSE_STATUS_TYPE_ID_Success);
                        ahqr.setStartIndex(BigInteger.valueOf(paramHolder.startIndex));
                        ahqr.setTotalResultCount(BigInteger.valueOf(paramHolder.totalResultCount));                        
                    } else if (queryLang.equals(BindingUtility.CANONICAL_QUERY_LANGUAGE_ID_ebRSFilterQuery)) {
                        FilterQueryType filterQuery = (FilterQueryType)queryExp.getContent().get(0);
                        rolt = filterQueryProcessor.executeQuery(((ServerRequestContext)context), user, 
                                filterQuery, responseOption, paramHolder);
                        
                        ahqr = BindingUtility.getInstance().queryFac.createAdhocQueryResponse();
                        ahqr.setRegistryObjectList(rolt);
                        ahqr.setStatus(BindingUtility.CANONICAL_RESPONSE_STATUS_TYPE_ID_Success);
                        ahqr.setStartIndex(BigInteger.valueOf(paramHolder.startIndex));
                        ahqr.setTotalResultCount(BigInteger.valueOf(paramHolder.totalResultCount));
                        
                    } else {
                        throw new UnsupportedCapabilityException(
                            "Unsupported Query Language: ClassificationNode id: " + queryLang);
                    }
                }        
            }
            
            // fetch child objects
            if (fetchChildObjsSrv) {
                boolean fetchChildObjsClt = Boolean.valueOf((String)slotsMap
                        .get(CanonicalConstants.CANONICAL_SLOT_GET_CHILD_OBJECTS)).booleanValue();
                if (fetchChildObjsClt) {
                    fetchChildObjects(ahqr.getRegistryObjectList().getIdentifiable(), 
                            (ServerRequestContext)context, responseOption);
                }
            }

            /** HIEOS/BHT (REMOVED):
            //Add repositoryItems to repositoryItemMap if so requested in responseOption
            if (returnType == returnType.LEAF_CLASS_WITH_REPOSITORY_ITEM) {                               
                addRepositoryItems(ahqr.getRegistryObjectList().getIdentifiable(), context);
            }

            if (!bypassCMS) {
                //Now perform any Role-Based Content Filtering on query results                
                ((ServerRequestContext)context).getQueryResults().clear();
                ((ServerRequestContext)context).getQueryResults().addAll(ahqr.getRegistryObjectList().getIdentifiable());
                cmsm.invokeServices(((ServerRequestContext)context));
                ahqr.getRegistryObjectList().getIdentifiable().clear();
                ahqr.getRegistryObjectList().getIdentifiable().addAll(((ServerRequestContext)context).getQueryResults());
                ((ServerRequestContext)context).getQueryResults().clear();
            }
             */
        } catch (RegistryException e) {
            ((ServerRequestContext)context).rollback();
            throw e;
        } catch (Exception e) {
            ((ServerRequestContext)context).rollback();
            throw new RegistryException(e);
        }
        
        removeObjectsDeniedAccess(((ServerRequestContext)context), ahqr.getRegistryObjectList().getIdentifiable());
        
        if (isQueryFilterRequestBeingMade((ServerRequestContext)context)) {
            // Handle filter query requests
            processForQueryFilterPlugins((ServerRequestContext)context);
            // Filter queries produce special query results
            ahqr = processForSpecialQueryResults((ServerRequestContext)context);
        }
        
        ((ServerRequestContext)context).commit();
        ahqr.setRequestId(req.getId());        
        return ahqr;
    }
    
    private AdhocQueryResponse processForSpecialQueryResults(ServerRequestContext context) 
        throws RegistryException {
        AdhocQueryResponse ahqr = null;
        try {
            //Must have been Optimization for a special query like "urn:oasis:names:tc:ebxml-regrep:query:FindObjectByIdAndType"
            RegistryObjectListType rolt = BindingUtility.getInstance().rimFac.createRegistryObjectList();
            rolt.getIdentifiable().addAll(((ServerRequestContext)context).getSpecialQueryResults());
            ((ServerRequestContext)context).setSpecialQueryResults(null);

            ahqr = BindingUtility.getInstance().queryFac.createAdhocQueryResponse();
            ahqr.setRegistryObjectList(rolt);
            ahqr.setStatus(BindingUtility.CANONICAL_RESPONSE_STATUS_TYPE_ID_Success);
            ahqr.setStartIndex(BigInteger.valueOf(0));
            ahqr.setTotalResultCount(BigInteger.valueOf(((ServerRequestContext)context).getQueryResults().size()));    
        } catch (Throwable t) {
            throw new RegistryException(t);
        }
        return ahqr;
    }
    
    private boolean isQueryFilterRequestBeingMade(ServerRequestContext context) {
        boolean isQueryFilterRequestBeingMade = false;
        Map map = context.getQueryParamsMap();
        if (map != null) {
            Object obj = map.get("$queryFilterIds");
            if (obj != null) {
                isQueryFilterRequestBeingMade = true;
            }
        }
        return isQueryFilterRequestBeingMade;
    }
    
    /*
     * This method is used to process any configured QueryFilter plugins
     */
    private void processForQueryFilterPlugins(ServerRequestContext context) 
        throws RegistryException {
        try {
            Map paramsMap = context.getQueryParamsMap();
            if (paramsMap != null) {
                Object obj = context.getQueryParamsMap().get("$queryFilterIds");
                if (obj != null) {
                    if (obj instanceof String) {
                        String filterId = (String)obj;
                        QueryPlugin plugin = (QueryPlugin)queryPluginsMap.get(filterId);
                        plugin.processRequest(context);
                    } else if (obj instanceof Collection) {
                        Collection filterIds = (Collection)obj;
                        Iterator filterItr = filterIds.iterator();
                        while (filterItr.hasNext()) {
                            QueryPlugin plugin = (QueryPlugin)filterItr.next();
                            plugin.processRequest(context);
                        }
                    } else {
                        String msg = ServerResourceBundle.getInstance()
                                                         .getString("invalidFilterQueryParamter", new Object[] {obj.getClass().getName()});
                        throw new RegistryException(msg);
                    }
                }
            }
        } catch (RegistryException re) {
            throw re;
        } catch (Throwable t) {
            throw new RegistryException(t);
        }
    }
    
    /**
     * Recursively fetches child objects of ClassificationSchemes, ClassificationNodes and RegistryPackages
     */
    private void fetchChildObjects(List objList, ServerRequestContext context, ResponseOptionType responseOption) throws RegistryException, JAXBException {

        PersistenceManager pm = PersistenceManagerFactory.getInstance().getPersistenceManager();
        List results = null;
        String sqlQuery = "";

        for (int i = 0; i < objList.size(); i++) {
            ArrayList queryParams = new ArrayList();
            Object identifiable = objList.get(i);

            if (identifiable instanceof ClassificationScheme) {
                ClassificationScheme cs = (ClassificationScheme)identifiable;
                sqlQuery = "SELECT * FROM classificationnode WHERE parent= ?";
                log.trace("Executing query: '" + sqlQuery + "'");
                queryParams.add(cs.getId());
                results = pm.executeSQLQuery(context, sqlQuery, queryParams, responseOption, "classificationnode", new ArrayList());

                for (int j = 0; j < results.size(); j++) {
                    ClassificationNode cn = (ClassificationNode)results.get(j);
                    cs.getClassificationNode().add(cn);
                }

                fetchChildObjects(results, context, responseOption);

            } else if (identifiable instanceof ClassificationNode) {
                ClassificationNode cn = (ClassificationNode)identifiable;
                sqlQuery = "SELECT * FROM classificationnode WHERE parent= ?";
                log.trace("Executing query: '" + sqlQuery + "'");
                queryParams.add(cn.getId());
                results = pm.executeSQLQuery(context, sqlQuery, queryParams, responseOption, "classificationnode", new ArrayList());

                for (int j = 0; j < results.size(); j++) {
                    ClassificationNode newCn = (ClassificationNode)results.get(j);
                    cn.getClassificationNode().add(newCn);
                }

                fetchChildObjects(results, context, responseOption);

            } else if (identifiable instanceof RegistryPackage) {
                RegistryPackage rp = (RegistryPackage)identifiable;
                sqlQuery = "SELECT * FROM registryobject WHERE id in ";
                sqlQuery += "(SELECT ass.targetobject FROM association ass WHERE ass.sourceobject= ? and ";
                sqlQuery += "ass.associationType= ?)";
                log.trace("Executing query: '" + sqlQuery + "'");
                queryParams.add(rp.getId());
                queryParams.add(CanonicalSchemes.CANONICAL_ASSOCIATION_TYPE_ID_HasMember);
                results = pm.executeSQLQuery(context, sqlQuery, queryParams, responseOption, "registryobject", new ArrayList());

                if (results.size() > 0) {
                    rp.setRegistryObjectList(BindingUtility.getInstance().rimFac.createRegistryObjectList());

                    for (int j = 0; j < results.size(); j++) {
                        rp.getRegistryObjectList().getIdentifiable().add(results.get(j));
                    }
                }

                fetchChildObjects(results, context, responseOption);
            }
        }
    }
    
    /**
     * Recursively adds RepositoryItems of the ExtrinsicObjects in the <code>regObjs</code> list.
     */
    private void addRepositoryItems(List regObjs, RequestContext context) throws RegistryException {
        for (int i = 0; i < regObjs.size(); i++) {
            Object obj = regObjs.get(i);
            
            if (obj instanceof ExtrinsicObjectType) {
                try {
                    ExtrinsicObjectType eo = (ExtrinsicObjectType)obj;
                    String id = eo.getId();
                    RepositoryItem repositoryItem = RepositoryManagerFactory.getInstance()
                    .getRepositoryManager().getRepositoryItem(id);
                    context.getRepositoryItemsMap().put(id, repositoryItem);
                } catch (ObjectNotFoundException onfe) {
                    // ignore, ExtrinsicObject had no RepositoryItem
                }
                
            } else if (obj instanceof RegistryPackage) {
                RegistryPackage rp = (RegistryPackage)obj;
                if (fetchChildObjsSrv && rp.getRegistryObjectList() != null 
                        && rp.getRegistryObjectList().getIdentifiable().size() > 0) {
                    addRepositoryItems(rp.getRegistryObjectList().getIdentifiable(), context);
                }
            }
        }
    }
    
    /**
     * Removes any objects that the user doesnot have athorization to see.
     */
    private void removeObjectsDeniedAccess(ServerRequestContext context, List objs) throws RegistryException {
        /* HIEOS/BHT: REMOVED
            context.setQueryResults(objs);

            if (permitAllRead) {
                //Optimization: permit auth override to permit all reads
                return;
            }
            AdhocQueryRequestType req = (AdhocQueryRequestType)context.getCurrentRegistryRequest();
            
            // Remove any objects from the ad-hoc query result set that the
            // user is not permitted to see.
            
            AuthorizationResult authRes = 
                AuthorizationServiceImpl.getInstance().checkAuthorization(((ServerRequestContext)context));
            
            if (authRes.getDeniedResources().size() > 0) {
                if (isIterativeQuery(req)) {                      
                    int size = objs.size();
                    for (int i = 0; i < size; i++) {
                        IdentifiableType identifiableObject = 
                            (IdentifiableType)objs.get(i);
                        String id = identifiableObject.getId();
                        if (authRes.getDeniedResources().contains(id)) {
                            objs.remove(i);
                            // 'Replace denied resource with placeholder object
                            // Use lightweight object, RegistryPackage
                            // Workaround for bug - bugster id: 6239592
                            RegistryPackageType placeHolderRo = BindingUtility.getInstance()
                                                                              .rimFac
                                                                              .createRegistryPackage();
                            placeHolderRo.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage");
                            InternationalStringType iString = BindingUtility.getInstance()
                                                                             .rimFac
                                                                             .createInternationalStringType();
                            LocalizedString lString = BindingUtility.getInstance()
                                                                    .rimFac
                                                                    .createLocalizedString();
                            // We'll i18n this string soon
                            lString.setValue("(Access Denied)");
                            lString.setLang("en-US");
                            lString.setCharset("UTF-8");
                            iString.getLocalizedString().add(lString);
                            placeHolderRo.setName(iString);
                            objs.add(i, placeHolderRo);

                            //TODO: Should not expose an actual id to unauthorized clients
                            placeHolderRo.setId(id);
                            placeHolderRo.setLid(id);
                        }
                    }
                } else {
                    Iterator queryResultsIter = objs.iterator();
                    while (queryResultsIter.hasNext()) {
                        IdentifiableType identifiableObject =
                            (IdentifiableType)queryResultsIter.next();                           
                        String id = identifiableObject.getId();
                        if (authRes.getDeniedResources().contains(id)) {
                             queryResultsIter.remove();
                        }
                    }
                }
             }
        } catch (JAXBException e) {
            throw new RegistryException(e);
        }
        */
        context.setQueryResults(objs);
    }
    
    /**
     * Process optional impl specific depth paramater specified as request Slot.
     * If present, prefetches referenced objects up to specified depth level.
     * Depth = 0 (default) implies only fetch matched objects.
     * Depth = n implies, also fetch all objects referenced by matched objects upto depth of n
     * Depth = -1 implies, also fetch all objects referenced by matched objects upto any level.
     * Direct and indirect circular references are handled to avoid infinite loop.
     */
    private void processDepthParameter(ServerRequestContext context) throws RegistryException {
    }
    
    private boolean isIterativeQuery(AdhocQueryRequestType req) {
        if (req.getMaxResults().intValue() == -1) {
            return false;
        } else {
            return true;
        }                   
    }
        
    /** 
     * Extracts the queryId and parameters from request and stores it in the context for later use
     * by QueryPlugin.
     *
     */ 
    private void getQueryParameters(ServerRequestContext context) throws RegistryException {
        AdhocQueryRequestType newReq = (AdhocQueryRequestType)((ServerRequestContext)context).getCurrentRegistryRequest();
        
        SlotListType slotList = newReq.getRequestSlotList();
        
        if (slotList != null) {
            List slots = slotList.getSlot();

            HashMap queryParamsMap = new HashMap();
            Iterator iter = slots.iterator();
            while (iter.hasNext()) {
                SlotType1 slot = (SlotType1)iter.next();
                String slotName = slot.getName();
                if (slotName.equals(BindingUtility.CANONICAL_SLOT_QUERY_ID)) {
                    Value value = (Value)(slot.getValueList().getValue()).get(0);
                    context.setQueryId(value.getValue());
                } else if (slotName.startsWith("$")) {
                    List vlist = slot.getValueList().getValue();
                    int vListSize = vlist.size();
                    if (vListSize == 1) {
                        String paramValue = ((Value)vlist.get(0)).getValue();
                        queryParamsMap.put(slotName, paramValue);
                    } else if (vListSize > 1) {
                        // Need to support a Collection of Strings
                        Collection stringCollection = new ArrayList(vListSize);
                        Iterator valItr = vlist.iterator();
                        while (valItr.hasNext()) {
                            Value value = (Value)valItr.next();
                            stringCollection.add(value.getValue());
                        }
                        queryParamsMap.put(slotName, stringCollection);
                    }
                }            
            }
            
            context.setQueryParamsMap(queryParamsMap);
        }        
    }
    
    /**
     * Creates and caches all QueryPlugins
     */
    private void getQueryPlugins() {
        RegistryProperties props = RegistryProperties.getInstance();
        Iterator propsIter = props.getPropertyNamesStartingWith(QUERY_PLUGIN_PROPERTY_PREFIX);

        while (propsIter.hasNext()) {
            String prop = (String) propsIter.next();
            String queryId = prop.substring(QUERY_PLUGIN_PROPERTY_PREFIX.length()+1);
            String pluginClassName = props.getProperty(prop);
            
            try {
                Object plugin = createPluginInstance(pluginClassName);
                if (!(plugin instanceof QueryPlugin)) {
                    throw new JAXRException(CommonResourceBundle.getInstance().getString("message.unexpectedObjectType",
                        new String[] {plugin.getClass().toString(), QueryPlugin.class.toString()}));
                }
                queryPluginsMap.put(queryId, plugin);
            } catch (Exception e) {
                log.error(e);
            }
        }
        
    }
    
    /**
     * Creates and caches all QueryFilterPlugins
     */
    private void getQueryFilterPlugins() {
        RegistryProperties props = RegistryProperties.getInstance();
        Iterator propsIter = props.getPropertyNamesStartingWith(QUERY_FILTER_PLUGIN_PROPERTY_PREFIX);

        while (propsIter.hasNext()) {
            String prop = (String) propsIter.next();
            String queryId = prop.substring(QUERY_FILTER_PLUGIN_PROPERTY_PREFIX.length()+1);
            String pluginClassName = props.getProperty(prop);
            
            try {
                Object plugin = createPluginInstance(pluginClassName);
                if (!(plugin instanceof QueryPlugin)) {
                    throw new JAXRException(CommonResourceBundle.getInstance().getString("message.unexpectedObjectType",
                        new String[] {plugin.getClass().toString(), QueryPlugin.class.toString()}));
                }
                queryPluginsMap.put(queryId, plugin);
            } catch (Exception e) {
                log.error(e);
            }
        }
        
    }    
    
    /**
     * Gets the QueryPlugin that can process this query.
     *
     * @return the QueryPlugin if a match is found, otherwise return null 
     */
    QueryPlugin getQueryPlugin(ServerRequestContext context) throws RegistryException {
        QueryPlugin plugin = null;
        
        String queryId = context.getQueryId();
        if (queryId != null) {
            Object o  = queryPluginsMap.get(queryId);
            if (o instanceof QueryPlugin) {
                plugin = (QueryPlugin)o;
            }
        }
        
        return plugin;
    }
    
        
    /**
     * Checks if supplied query is a parameterized query. If not return the same query.
     * If stored parametreized query then return a new query after fetching the specified parameterized
     * query from registry, replacing its positional parameters with suppliued parameters.
     * If special parameterized query then invoke special query and set its results on context.getSpecialQueryResults().
     * If neither not a parameterized query at all then simply return the original request.
     */
    private void processForParameterizedQuery(ServerRequestContext context) throws RegistryException {
        
        //First check if it is a 
        AdhocQueryRequestType newReq = (AdhocQueryRequestType)((ServerRequestContext)context).getCurrentRegistryRequest();
        
        getQueryParameters(context);
        String queryId = context.getQueryId();
        Map queryParamsMap = context.getQueryParamsMap();
        
        SlotListType slotList = newReq.getRequestSlotList();
        
        //If queryId is not null then get the AdhocQuery from registry, plug the parameters
        //and set it as newReq
        if (queryId != null) {
            //This is a parameterized query
            
            QueryPlugin plugin = getQueryPlugin(context);
            
            if (plugin != null) {
                //Found a plugin for this queryId. invoke it
                plugin.processRequest(context);
            } else {
                //Must be a stored query since no plugin was found.
                
                //TODO: Assumes SQLQuery. Needs to support FilterQuery
                AdhocQueryType adhocQuery = (AdhocQueryType)ServerCache.getInstance().getRegistryObject(context, queryId, "AdhocQuery");

                if (adhocQuery == null) {
                    throw new ObjectNotFoundException(queryId, "AdhocQuery");
                }

                try {
                    //Need to make a copy of the query before plugQueryParameters as the query may be cached and should not be modified in place.
                    adhocQuery = (AdhocQueryType)BindingUtility.getInstance().cloneRegistryObject(adhocQuery);
                    adhocQuery = plugQueryParameters(adhocQuery, context.getQueryParamsMap(), context.getStoredQueryParams());
                    newReq.setAdhocQuery(adhocQuery);                
                }
                catch (JAXBException e) {
                    throw new RegistryException(e);
                }
                catch (JAXRException e) {
                    throw new RegistryException(e);
                }                
            }            
        }        
    }
        
    /**
     * Replaces named parameters in an SQLQuery with ?
     * while placing corresponding positional paramValues in queryParams List.
     *
     * @param query the AdhocQuery to be executed
     * @param queryParamsMap the Map in which each entry is a query param name/value pair
     * @param positionalParamValues a List of param values for each positional parameter in the PrepareStatment for the query
     */
    private AdhocQueryType plugQueryParameters(AdhocQueryType query, Map queryParamsMap, List positionalParamValues) throws JAXBException {        
        AdhocQueryType newQuery = query;
        positionalParamValues.clear();    //Start with empty list
        
        //Get the queryString
        QueryExpressionType queryExp = query.getQueryExpression();
        String queryLang = queryExp.getQueryLanguage();
        String queryStr = (String)queryExp.getContent().get(0);
        String newQueryStr = new String(queryStr);
        log.debug("queryStr=" + queryStr);
            
        //Now replace parameterNames in queryString with ? 
        //and add corresponding paramValue to quereyParams List
        //If a paramName is used more than once in queryString then
        //add corresponding paramValue multiple times in appropriate 
        //position in positionalParamValues list
        //This would be easier with JDBC 3.0 nameParameters but support
        //is optional in JDBC 3.0 drivers and therefor not reliable.
        Iterator iter = queryParamsMap.keySet().iterator();        
        
        Map paramIndexToValueMap = new TreeMap();
        
        //Process each $paramName by replacing it with ? in queryStr
        //and placing its paramValue in corresponding index on positionalParamValues
        while (iter.hasNext()) {
            String paramName = (String)iter.next();
            String paramValue = queryParamsMap.get(paramName).toString();

	    // Pattern to match $paramName optionally surrounded by single
	    // quotes.  First OR clause matches the longer option -- with
	    // surrounding single quotes.  Second OR clause matches
	    // isolated parameter name.
	    //
	    // Some parameter names are prefixes of others ($considerPort
	    // and $considerPortType for example) and this complicates the
	    // second part.  Must ensure we match the name only if it is
	    // not a prefix.  The zero-width negative lookahead checks this
	    // case and does not mess up the replacement.  Similar
	    // zero-width negative lookbehind at start avoids matching (for
	    // example) '$binding.transportType%' since that case would
	    // result in '?%' and a parameter the underlying SQL parser
	    // won't find.  NOTE: Not checking for all parameters "buried"
	    // in SQL string literals.
	    //
	    // TODO: The currently-used parameter name characters are
	    // [a-zA-Z0-9.].  This goes beyond both [a-zA-Z0-9] from
	    // [ebRS], section 6.3.1.1 and [a-zA-Z0-9_$#] previously in
	    // SQLParser.jj.  Rule below and latest SQLParser.jj <VARIABLE>
	    // production are at least consistent.  Should '.' be removed
	    // from parameter names in WS Profile?  Should both '.' and '_'
	    // be disallowed in all parameter names?
            
            //Use Utility method as following is not in JDK 1.4
            //String quotedParamName = Pattern.quote(paramName);
	    String quotedParamName = org.freebxml.omar.common.Utility.getInstance().quote(paramName);
	    String paramNamePattern = "('" + quotedParamName + "')|" +
		"((?<!')" + quotedParamName + "(?![a-zA-Z0-9._]))";
            Pattern p = Pattern.compile(paramNamePattern);
            Matcher m = p.matcher(queryStr);
            
            //Remember start index of each occurance of paramName
            boolean found = false;
            while (m.find()) {
		found = true;
		paramIndexToValueMap.put(new Integer(m.start()), paramValue);
            }

	    // Replace substrings matching paramNamePattern with ?  Must be
	    // done using separate matcher and string to keep
	    // paramIndexToValueMap sorted
	    if (found) {
		newQueryStr = p.matcher(newQueryStr).replaceAll("?");
	    }
        }                
        
        if (paramIndexToValueMap.size() > 0) {
            positionalParamValues.addAll(paramIndexToValueMap.values());
        }
        
        
        //Now re-constitute as query
        queryExp.getContent().clear();
        queryExp.getContent().add(newQueryStr);        
        
        return newQuery;
    }


    /**
     * Replaces special environment variables within specified query string.
     */
    private String replaceSpecialVariables(UserType user, String query) {
        String newQuery = query;

        //Replace $currentUser 
        if (user != null) {
            newQuery = newQuery.replaceAll("\\$currentUser", "'"+user.getId()+"'");
        }

        //Replace $currentTime       
        Timestamp currentTime = new Timestamp(Calendar.getInstance().getTimeInMillis());

        //??The timestamp is being truncated to work around a bug in PostgreSQL 7.2.2 JDBC driver
        String currentTimeStr = currentTime.toString().substring(0, 19);
        newQuery = newQuery.replaceAll("\\$currentTime", currentTimeStr);

        return newQuery;
    }
    
    /**
     * Gets RegistryObject matching specified id. 
     * This method is added for the REST
     *
     */
    public RegistryObjectType getRegistryObject(
        RequestContext context, String id) throws RegistryException {
        
        return getRegistryObject(context, id, "RegistryObject");
    }
    
    public RegistryObjectType getRegistryObject(RequestContext context,
        String id, String typeName) throws RegistryException {
        RegistryObjectType ro = null;
        ServerRequestContext serverContext = ServerRequestContext.convert(context);
        boolean doCommit = false;
        
        try {
            //Code in AuthorizationServiceImpl and else where expects a request so make one up
            AdhocQueryRequest req = BindingUtility.getInstance().createAdhocQueryRequest("SELECT * FROM DummyTable");
            serverContext.pushRegistryRequest(req);
            
            UserType user = serverContext.getUser();
            
            typeName = org.freebxml.omar.common.Utility.getInstance().mapTableName(typeName);
            ro = ServerCache.getInstance().getRegistryObject(serverContext, id, typeName);


            //Avoid overhead of access control checks for RegistryOperator internal user
            /* HIEOS/BHT: REMOVED
            if ((null != ro) && ((null == user) || (!user.getId().equalsIgnoreCase(AuthenticationServiceImpl.ALIAS_REGISTRY_OPERATOR)))) {
                List roList = new ArrayList();
                roList.add(ro);
                removeObjectsDeniedAccess(serverContext, roList);
                if (!(roList.contains(ro))) {
                    ro = null;
                }
            }
            */
            doCommit = true;
        } catch (JAXBException e) {
            log.error(e, e);    //Can't happen
        } finally {
            if (context != serverContext) {
                if (doCommit) {
                    serverContext.commit();
                } else {
                    serverContext.rollback();
                }
            } else {
                serverContext.popRegistryRequest();
            }
        }
        
        return ro;
    }
    
    /**
     * This method is added for the REST
     * It returns the RepositroyItem give a UUID
     * 
     */
    public RepositoryItem getRepositoryItem(    
        RequestContext context, String id) throws RegistryException {
        
        RepositoryItem ri = null;
        ServerRequestContext serverContext = ServerRequestContext.convert(context);
        boolean doCommit = false;
        
        try {
            UserType user = serverContext.getUser();
            //Code in AuthorizationServiceImpl and else where expects a request so make one up
            AdhocQueryRequest req = BindingUtility.getInstance().createAdhocQueryRequest("SELECT * FROM DummyTable");
            serverContext.pushRegistryRequest(req);
            
            //Following call is to force access control check which is
            //already implemented in getRegistryObject(...)
            RegistryObjectType ro = getRegistryObject(serverContext, id);
            if (ro != null) {                                                
                ri = rm.getRepositoryItem(id);
            }
            doCommit = true;
        } catch (JAXBException e) {
            log.error(e, e);    //Can't happen
        } finally {
            if (context != serverContext) {
                if (doCommit) {
                    serverContext.commit();
                } else {
                    serverContext.rollback();
                }
            } else {
                serverContext.popRegistryRequest();
            }
        }
        
        return ri;
    }
    
    
    /**
     * Gets the RegistryObjects referenced by specified RegistryObject.
     *
     * @param ro specifies the RegistryObject whose referenced objects are being sought.
     * @param depth specifies depth of fetch. -1 implies fetch all levels. 1 implies fetch immediate referenced objects. 
     *
    public Set getReferencedRegistryObjects(RegistryObjectType ro, int depth) throws RegistryException {
        HashSet referencedObjects = new HashSet();                
        
        try {
            HashMap idMap = new HashMap();
            Set immediateObjectRefs = BindingUtility.getInstance().getObjectRefsInRegistryObject(ro, idMap);

            --depth;

            //Get each immediately referenced RegistryObject
            Iterator iter = immediateObjectRefs.iterator();
            while (iter.hasNext()) {
                String ref = (String)iter.next();
                RegistryObjectType obj = getRegistryObject(ref);
                
                referencedObjects.add(obj);

                //If depth != 0 then recurse and get referenced objects for obj
                if (depth != 0) {
                    referencedObjects.addAll(getReferencedRegistryObjects(obj, depth));
                }
            }
        } catch (JAXRException e) {
            throw new RegistryException(e);
        }
                        
        return referencedObjects;
    }*/

    public UserType getUser(X509Certificate cert) throws RegistryException {
        return AuthenticationServiceImpl.getInstance().getUserFromCertificate(cert);
    }
        
}
