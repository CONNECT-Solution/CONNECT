/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/common/ServerRequestContext.java,v 1.21 2006/12/12 20:52:48 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Locale;

import javax.xml.bind.JAXBException;
import javax.xml.registry.InvalidRequestException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import javax.xml.registry.JAXRException;
import org.freebxml.omar.common.CommonRequestContext;
import org.freebxml.omar.common.spi.QueryManager;
import org.freebxml.omar.common.spi.QueryManagerFactory;
import org.freebxml.omar.common.ReferenceInfo;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.CommonResourceBundle;
import org.freebxml.omar.common.RepositoryItem;
import org.freebxml.omar.common.exceptions.ObjectNotFoundException;
import org.freebxml.omar.common.spi.RequestContext;
import org.freebxml.omar.server.cache.ServerCache;
/* HIEOS/BHT (REMOVED):
import org.freebxml.omar.server.event.EventManager;
import org.freebxml.omar.server.event.EventManagerFactory;
import org.freebxml.omar.server.lcm.replication.ReplicationManager;
*/
import org.freebxml.omar.server.lcm.versioning.VersionProcessor;
import org.freebxml.omar.server.persistence.PersistenceManager;
import org.freebxml.omar.server.persistence.PersistenceManagerFactory;
import org.freebxml.omar.server.plugin.RequestInterceptorManager;
import org.freebxml.omar.server.security.authentication.AuthenticationServiceImpl;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.lcm.SubmitObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.UpdateObjectsRequest;
import org.oasis.ebxml.registry.bindings.query.AdhocQueryRequest;
import org.oasis.ebxml.registry.bindings.query.AdhocQueryResponseType;
import org.oasis.ebxml.registry.bindings.query.ResponseOption;

import org.oasis.ebxml.registry.bindings.query.ResponseOptionType;
import org.oasis.ebxml.registry.bindings.query.ReturnType;
import org.oasis.ebxml.registry.bindings.rim.AdhocQueryType;
import org.oasis.ebxml.registry.bindings.rim.AuditableEventType;
import org.oasis.ebxml.registry.bindings.rim.ClassificationNodeType;
import org.oasis.ebxml.registry.bindings.rim.ClassificationSchemeType;
import org.oasis.ebxml.registry.bindings.rim.ExtrinsicObjectType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefListType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.UserType;
import org.oasis.ebxml.registry.bindings.rs.RegistryErrorListType;
import org.oasis.ebxml.registry.bindings.rs.RegistryRequestType;

/**
 * Keeps track of the state and context for a client request
 * as it makes its way through the server.
 *
 * @author  Farrukh S. Najmi
 */
public class ServerRequestContext extends CommonRequestContext {

    private Log log = LogFactory.getLog(this.getClass());
    private static BindingUtility bu = BindingUtility.getInstance();
    private static PersistenceManager pm = PersistenceManagerFactory.getInstance().getPersistenceManager();
    private static QueryManager qm = QueryManagerFactory.getInstance().getQueryManager();
    //Map of top level Identifiable objects within the request with id keys and IdentifiableType values
    private Map topLevelObjectsMap = new HashMap();
    //Ids of subset of submittedObjects that are new and not pre-existing in registry
    private Set newSubmittedObjectIds = null;
    //New versions of RegistryObjects that are a subset of topLevelObjects that were created by Versioning feature
    private Map newROVersionMap = new HashMap();
    //New versions of RepositoryItems that were created by Versioning feature
    private Map newRIVersionMap = new HashMap();
    //Map of composed RegistryObjects within the request with id keys and RegistryObjectType values
    private Map composedObjectsMap = new HashMap();
    //Map of all submitted RegistryObjects objects within the request with id keys and IdentifiableType values
    //includes composedObjects
    //Set of all RegistryObject ids referenced from submitted (top level + composed) objects
    private Set referencedInfos = null;
    //Set of solved id references for this request
    private SortedSet checkedRefs = new TreeSet();
    //Map of RegistryObject owners with RO id keys and ownerId string values
    private SortedMap fetchedOwners = new TreeMap();
    //Map of submitted RegistryObjects with RO id keys and RegistryObjectType values
    private Map submittedObjectsMap = new HashMap();
    //Map of ObjectRefs within the request with id keys and ObjectRef values
    private Map objectRefsMap = new HashMap();
    //Maps temporary id key to permanent id value
    private Map idMap = new HashMap();
    //Used only by QueryManagerImpl to pass results of a query for read access control check.
    private List queryResults = new ArrayList();
    //Short lived memory used only in handling special queries related to cache based optimizations.
    private List specialQueryResults = null;
    //Short lived memory used only in handling stored query invocation
    private List storedQueryParams = new ArrayList();
    ;
    //The RegistryErrorList for this request
    private RegistryErrorListType errorList = null;
    //Tracks those associations that are being confirmed
    private Map confirmationAssociations = new HashMap();
    private Locale localeOfCaller = Locale.getDefault();
    //Begin former DAOContext members
    private Connection connection = null;
    private AuditableEventType createEvent;
    private AuditableEventType updateEvent;
    private AuditableEventType versionEvent;
    private AuditableEventType setStatusEvent;
    private AuditableEventType approveEvent;
    private AuditableEventType deprecateEvent;
    private AuditableEventType unDeprecateEvent;
    private AuditableEventType deleteEvent;
    private AuditableEventType relocateEvent;
    private ResponseOptionType responseOption;
    private ArrayList objectRefs;
    //Consolidation of events of all types above into a single List. This is initialized in saveAuditableEVents()
    private List auditableEvents = new ArrayList();
    private Map affectedObjectsMap = new HashMap();
    //Map from id to lid for existing objects in registry that are either submitted or referenced in this request
    private Map idToLidMap = new HashMap();
    //The queryId for a request that is a parameterized query invocation
    private String queryId;
    private Map queryParamsMap = null;
    //true if user has RegistryAdministrator role
    private Boolean isAdmin = null;

    /** Creates a new instance of RequestContext */
    public ServerRequestContext(String contextId, RegistryRequestType request) throws RegistryException {
        super(contextId, request);

        //Call RequestInterceptors
        //Only intercept top level requests.
        /* HIEOS/BHT (REMOVED):
        if (request != null) {
            RequestInterceptorManager.getInstance().preProcessRequest(this);
        }
        */

        try {
            setErrorList(BindingUtility.getInstance().rsFac.createRegistryErrorList());

            objectRefs = new ArrayList();

        } catch (JAXBException e) {
            throw new RegistryException(e);
        }
    }

    private void createEvents() throws RegistryException {
        try {
            UserType user = getUser();

            if (user != null) {
                createEvent = bu.rimFac.createAuditableEvent();
                createEvent.setEventType(BindingUtility.CANONICAL_EVENT_TYPE_ID_Created);
                createEvent.setId(org.freebxml.omar.common.Utility.getInstance().createId());
                createEvent.setRequestId("//TODO");
                createEvent.setUser(user.getId());
                ObjectRefListType createRefList = bu.rimFac.createObjectRefListType();
                createEvent.setAffectedObjects(createRefList);

                updateEvent = bu.rimFac.createAuditableEvent();
                updateEvent.setEventType(BindingUtility.CANONICAL_EVENT_TYPE_ID_Updated);
                updateEvent.setId(org.freebxml.omar.common.Utility.getInstance().createId());
                updateEvent.setRequestId("//TODO");
                updateEvent.setUser(user.getId());
                ObjectRefListType updateRefList = bu.rimFac.createObjectRefListType();
                updateEvent.setAffectedObjects(updateRefList);

                versionEvent = bu.rimFac.createAuditableEvent();
                versionEvent.setEventType(BindingUtility.CANONICAL_EVENT_TYPE_ID_Versioned);
                versionEvent.setId(org.freebxml.omar.common.Utility.getInstance().createId());
                versionEvent.setRequestId("//TODO");
                versionEvent.setUser(user.getId());
                ObjectRefListType versionRefList = bu.rimFac.createObjectRefListType();
                versionEvent.setAffectedObjects(versionRefList);

                setStatusEvent = bu.rimFac.createAuditableEvent();
                setStatusEvent.setId(org.freebxml.omar.common.Utility.getInstance().createId());
                setStatusEvent.setRequestId("//TODO");
                setStatusEvent.setUser(user.getId());
                ObjectRefListType setStatusRefList = bu.rimFac.createObjectRefListType();
                setStatusEvent.setAffectedObjects(setStatusRefList);

                approveEvent = bu.rimFac.createAuditableEvent();
                approveEvent.setEventType(BindingUtility.CANONICAL_EVENT_TYPE_ID_Approved);
                approveEvent.setId(org.freebxml.omar.common.Utility.getInstance().createId());
                approveEvent.setRequestId("//TODO");
                approveEvent.setUser(user.getId());
                ObjectRefListType approveRefList = bu.rimFac.createObjectRefListType();
                approveEvent.setAffectedObjects(approveRefList);

                deprecateEvent = bu.rimFac.createAuditableEvent();
                deprecateEvent.setEventType(BindingUtility.CANONICAL_EVENT_TYPE_ID_Deprecated);
                deprecateEvent.setId(org.freebxml.omar.common.Utility.getInstance().createId());
                deprecateEvent.setRequestId("//TODO");
                deprecateEvent.setUser(user.getId());
                ObjectRefListType deprecateRefList = bu.rimFac.createObjectRefListType();
                deprecateEvent.setAffectedObjects(deprecateRefList);

                unDeprecateEvent = bu.rimFac.createAuditableEvent();
                unDeprecateEvent.setEventType(BindingUtility.CANONICAL_EVENT_TYPE_ID_Undeprecated);
                unDeprecateEvent.setId(org.freebxml.omar.common.Utility.getInstance().createId());
                unDeprecateEvent.setRequestId("//TODO");
                unDeprecateEvent.setUser(user.getId());
                ObjectRefListType unDeprecateRefList = bu.rimFac.createObjectRefListType();
                unDeprecateEvent.setAffectedObjects(unDeprecateRefList);

                deleteEvent = bu.rimFac.createAuditableEvent();
                deleteEvent.setEventType(BindingUtility.CANONICAL_EVENT_TYPE_ID_Deleted);
                deleteEvent.setId(org.freebxml.omar.common.Utility.getInstance().createId());
                deleteEvent.setRequestId("//TODO");
                deleteEvent.setUser(user.getId());
                ObjectRefListType deleteRefList = bu.rimFac.createObjectRefListType();
                deleteEvent.setAffectedObjects(deleteRefList);

                relocateEvent = bu.rimFac.createAuditableEvent();
                relocateEvent.setEventType(BindingUtility.CANONICAL_EVENT_TYPE_ID_Relocated);
                relocateEvent.setId(org.freebxml.omar.common.Utility.getInstance().createId());
                relocateEvent.setRequestId("//TODO");
                relocateEvent.setUser(user.getId());
                ObjectRefListType relocateRefList = bu.rimFac.createObjectRefListType();
                relocateEvent.setAffectedObjects(relocateRefList);
            }
        } catch (JAXBException e) {
            throw new RegistryException(e);
        }
    }

    /**
     * Gets the RegistryObject associated with the specified id.
     * First looks in submittedObjectsMap in case it is a new object being submitted.
     * Next look in ServerCache in case it was previously fetched from registry.
     * Finally looks in registry.
     */
    public RegistryObjectType getRegistryObject(String id, String tableName)
            throws RegistryException {

        return getRegistryObject(id, tableName, false);
    }

    /**
     * Gets the RegistryObject associated with the specified id.
     * First looks in submittedObjectsMap in case it is a new object being submitted.
     * This is only done if requireExisting is false.
     *
     * Next look in ServerCache in case it was previously fetched from registry.
     * ServerCache will look in registry if not found in cache.
     */
    public RegistryObjectType getRegistryObject(String id, String tableName, boolean requireExisting)
            throws RegistryException {

        RegistryObjectType ro = null;

        if (!requireExisting) {
            //If request is submit or update then get ro from context
            if ((this.getRegistryRequestStack().size() > 0) && ((this.getCurrentRegistryRequest() instanceof SubmitObjectsRequest) || (this.getCurrentRegistryRequest() instanceof UpdateObjectsRequest))) {
                //First look in submitted objects.
                ro = (RegistryObjectType) getSubmittedObjectsMap().get(id);
            }
        }
        if (ro == null) {
            //Next look in registry via the ObjectCache
            ro = ServerCache.getInstance().getRegistryObject(this, id, tableName);

            if (ro == null) {
                throw new ObjectNotFoundException(id, tableName);
            }
        }

        return ro;
    }

    /**
     *
     * Removes object matching specified id from all the various maps.
     */
    public void remove(String id) {
        getTopLevelObjectsMap().remove(id);
        getSubmittedObjectsMap().remove(id);
        getComposedObjectsMap().remove(id);
        getIdMap().remove(id);
    }

    /**
     * Checks each object including composed objects.
     */
    public void checkObjects() throws RegistryException {
        try {
            //Process ObjectRefs and create local replicas of any remote ObjectRefs
            createReplicasOfRemoteObjectRefs();

            //Get all submitted objects including composed objects that are part of the submission
            //so that they can be used to resolve references
            getSubmittedObjectsMap().putAll(getTopLevelObjectsMap());

            Set composedObjects = bu.getComposedRegistryObjects(getTopLevelObjectsMap().values(), -1);
            getSubmittedObjectsMap().putAll(bu.getRegistryObjectMap(composedObjects));

            pm.updateIdToLidMap(this, getSubmittedObjectsMap().keySet(), "RegistryObject");

            getNewSubmittedObjectIds();

            //Check id of each object (top level or composed)
            Iterator iter = getSubmittedObjectsMap().values().iterator();
            while (iter.hasNext()) {
                RegistryObjectType ro = (RegistryObjectType) iter.next();

                //AuditableEvents are not allowed to be submitted by clients
                if (ro instanceof AuditableEventType) {
                    throw new InvalidRequestException(ServerResourceBundle.getInstance().getString("message.auditableEventsNotAllowed"));
                }

                checkId(ro);
            }

            //Get RegistryObjects referenced by submittedObjects.
            this.getReferenceInfos();

            //Append the references to the IdToLidMap

            iter = this.referencedInfos.iterator();
            Set referencedIds = new HashSet();
            while (iter.hasNext()) {
                ReferenceInfo refInfo = (ReferenceInfo) iter.next();
                referencedIds.add(refInfo.targetObject);
            }

            pm.updateIdToLidMap(this, referencedIds, "RegistryObject");


            //Iterate over idMap and replace keys in various structures that use id as key
            //that are based on temporary ids with their permanent id.
            iter = getIdMap().keySet().iterator();
            while (iter.hasNext()) {
                String idOld = (String) iter.next();
                String idNew = (String) getIdMap().get(idOld);

                //replace in all RequestContext data structures
                Object obj = getTopLevelObjectsMap().remove(idOld);
                if (obj != null) {
                    getTopLevelObjectsMap().put(idNew, obj);
                }
                obj = getSubmittedObjectsMap().remove(idOld);
                if (obj != null) {
                    getSubmittedObjectsMap().put(idNew, obj);
                }
                if (getNewSubmittedObjectIds().remove(idOld)) {
                    getNewSubmittedObjectIds().add(idNew);
                }

                RepositoryItem ri = (RepositoryItem) getRepositoryItemsMap().remove(idOld);
                if (ri != null) {
                    ri.setId(idNew);
                    getRepositoryItemsMap().put(idNew, ri);
                }
            }

            //Now replace any old versions of RegistryObjects with new versions
            iter = getNewROVersionMap().keySet().iterator();
            while (iter.hasNext()) {
                RegistryObjectType roOld = (RegistryObjectType) iter.next();
                RegistryObjectType roNew = (RegistryObjectType) getNewROVersionMap().get(roOld);

                //replace in all data structures
                getSubmittedObjectsMap().remove(roOld.getId());
                getSubmittedObjectsMap().put(roNew.getId(), roNew);
                getTopLevelObjectsMap().remove(roOld.getId());
                getTopLevelObjectsMap().put(roNew.getId(), roNew);
            }

            //Now replace any old versions of RepositoryItems with new versions
            iter = getNewRIVersionMap().keySet().iterator();
            while (iter.hasNext()) {
                RepositoryItem riOld = (RepositoryItem) iter.next();
                RepositoryItem riNew = (RepositoryItem) getNewRIVersionMap().get(riOld);

                //replace in all RequestContext data structures
                getRepositoryItemsMap().remove(riOld.getId());
                getRepositoryItemsMap().put(riNew.getId(), riNew);
            }

            //resolve references from each object
            resolveObjectReferences();
        } catch (JAXRException e) {
            throw new RegistryException(e);
        }
    }

    private void createReplicasOfRemoteObjectRefs() throws RegistryException {
        /* HIEOS/BHT: REMOVED
        Iterator iter = getObjectRefsMap().values().iterator();
        while (iter.hasNext()) {
            ObjectRefType ref = (ObjectRefType) iter.next();
            ReplicationManager replMgr = ReplicationManager.getInstance();
            if (replMgr.isRemoteObjectRef(ref)) {
                //This is a Remote ObjectRef. Resolve reference by creating a replica
                replMgr.createReplica(this, ref);
            }
        }
         */
    }

    /**
     * Check if id is a proper UUID. If not make a proper UUID based URN and add
     * a mapping in idMap between old and new Id.
     *
     * @param submittedIds The ArrayList holding ids of all objects (including composed objects) submitted.
     *
     * @param idMap The HashMap with old temporary id to new permanent id mapping.
     *
     * @throws UUIDNotUniqueException if any UUID is not unique within a
     * SubmitObjectsRequest
     */
    private void checkId(RegistryObjectType ro)
            throws RegistryException {
        String id = ro.getId();

        org.freebxml.omar.common.Utility util = org.freebxml.omar.common.Utility.getInstance();
        if (!util.isValidRegistryId(id)) {
            // Generate permanent id for this temporary id
            String newId = util.createId();
            ro.setId(newId);
            getIdMap().put(id, newId);
        }

        VersionProcessor vp = new VersionProcessor(this);
        vp.checkRegistryObjectLid(ro);

        /* HIEOS/BHT (REMOVED):
        boolean needToVersionRepositoryItem = false;
        if (ro instanceof ExtrinsicObjectType) {
            ExtrinsicObjectType eo = (ExtrinsicObjectType) ro;
            needToVersionRepositoryItem = vp.needToVersionRepositoryItem(eo, (RepositoryItem) getRepositoryItemsMap().get(id));

            if (needToVersionRepositoryItem) {
                //Following will also create new version of eo implicitly
                vp.createRepositoryItemVersion(eo);
            }
        }

        //Only version RegistryObject if RepositoryItem was not versioned
        //This is because RegistryObject is implictly versioned during
        //versioning of RepositoryItem
        if (!needToVersionRepositoryItem) {
            boolean needToVersionRegistryObject = vp.needToVersionRegistryObject(ro);
            if (needToVersionRegistryObject) {
                vp.createRegistryObjectVersion(ro);
            }
        }
        */
    }

    /*
     * Resolves each ObjectRef within the specified objects.
     *
     * @param obj the object whose reference attribute are being checked for being resolvable.
     *
     *
     */
    private void resolveObjectReferences()
            throws RegistryException {

        try {
            //Get Set of ids for objects referenced from obj
            Set refInfos = this.referencedInfos;

            //Check that each ref is resolvable
            Iterator iter = refInfos.iterator();
            Set unresolvedRefIds = new HashSet();
            while (iter.hasNext()) {
                ReferenceInfo refInfo = (ReferenceInfo) iter.next();
                String refId = refInfo.targetObject;

                // only check referenced id once per request
                if (getCheckedRefs().contains(refId)) {
                    continue;
                } else {
                    getCheckedRefs().add(refId);
                }

                ObjectRefType ref = (ObjectRefType) getObjectRefsMap().get(refId);

                //Remote references already resolved by creating local replica by now
                //First check if resolved within submittedIds
                if (!(getSubmittedObjectsMap().containsKey(refId))) {
                    //ref not resolved within submitted objects

                    //See if exists in the registry
                    if (!(getIdToLidMap().keySet().contains(refId))) {
                        unresolvedRefIds.add(refId);
                    }

                }
            }

            if (unresolvedRefIds.size() > 0) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.unresolvedReferences",
                        new Object[]{unresolvedRefIds}));
            }
        } catch (JAXRException e) {
            throw new RegistryException(e);
        }
    }

    public ResponseOptionType getResponseOption() throws RegistryException {
        try {
            if (responseOption == null) {
                responseOption = bu.queryFac.createResponseOption();
                responseOption.setReturnType(ReturnType.LEAF_CLASS);
                responseOption.setReturnComposedObjects(true);
            }
        } catch (JAXBException e) {
            throw new RegistryException(e);
        }
        return responseOption;
    }

    public void setResponseOption(ResponseOptionType responseOption) {
        this.responseOption = responseOption;
    }

    /**
     * Gets ObjectRefs from result of the AdhocQuery specified (if any).
     *
     */
    public List getObjectsRefsFromQueryResults(AdhocQueryType query) throws RegistryException, JAXBException {
        List orefs = new ArrayList();

        try {
            if (query != null) {
                AdhocQueryRequest req = bu.queryFac.createAdhocQueryRequest();
                req.setId(org.freebxml.omar.common.Utility.getInstance().createId());
                req.setAdhocQuery(query);

                ResponseOption ro = bu.queryFac.createResponseOption();
                ro.setReturnComposedObjects(false);
                ro.setReturnType(ReturnType.OBJECT_REF);
                req.setResponseOption(ro);
                this.pushRegistryRequest(req);
                AdhocQueryResponseType resp = qm.submitAdhocQuery(this);
                orefs.addAll(resp.getRegistryObjectList().getIdentifiable());
            }
        } finally {
            if (query != null) {
                this.popRegistryRequest();
            }
        }

        return orefs;
    }

    public List getObjectRefs() {
        return objectRefs;
    }

    public AuditableEventType getCreateEvent() {
        return createEvent;
    }

    public AuditableEventType getUpdateEvent() {
        return updateEvent;
    }

    public AuditableEventType getVersionEvent() {
        return versionEvent;
    }

    public AuditableEventType getSetStatusEvent() {
        return setStatusEvent;
    }

    public AuditableEventType getApproveEvent() {
        return approveEvent;
    }

    public AuditableEventType getDeprecateEvent() {
        return deprecateEvent;
    }

    public AuditableEventType getUnDeprecateEvent() {
        return unDeprecateEvent;
    }

    public AuditableEventType getDeleteEvent() {
        return deleteEvent;
    }

    public AuditableEventType getRelocateEvent() {
        return relocateEvent;
    }

    public Connection getConnection() throws RegistryException {
        if (connection == null) {
            connection = pm.getConnection(this);
        }
        return connection;
    }

    private void saveAuditableEvents() throws RegistryException {
        UserType user = getUser();

        if (user != null) {
            auditableEvents.clear();
            Calendar timeNow = Calendar.getInstance();

            //create events during relocate event should be ignored
            if (eventOccured(getRelocateEvent())) {
                getRelocateEvent().setTimestamp(timeNow);
                removeDuplicateAffectedObjects(getRelocateEvent());
                auditableEvents.add(getRelocateEvent());
            } else if (eventOccured(getCreateEvent())) {
                getCreateEvent().setTimestamp(timeNow);
                removeDuplicateAffectedObjects(getCreateEvent());
                auditableEvents.add(getCreateEvent());
            }

            //Delete during update should be ignored as they are an impl artifact
            if (eventOccured(getUpdateEvent())) {
                getUpdateEvent().setTimestamp(timeNow);
                removeDuplicateAffectedObjects(getUpdateEvent());
                auditableEvents.add(getUpdateEvent());
            } else if (eventOccured(getDeleteEvent())) {
                getDeleteEvent().setTimestamp(timeNow);
                removeDuplicateAffectedObjects(getDeleteEvent());
                auditableEvents.add(getDeleteEvent());
            }

            if (eventOccured(getSetStatusEvent())) {
                getSetStatusEvent().setTimestamp(timeNow);
                removeDuplicateAffectedObjects(getSetStatusEvent());
                auditableEvents.add(getSetStatusEvent());
            }

            if (eventOccured(getApproveEvent())) {
                getApproveEvent().setTimestamp(timeNow);
                removeDuplicateAffectedObjects(getApproveEvent());
                auditableEvents.add(getApproveEvent());
            }

            if (eventOccured(getDeprecateEvent())) {
                getDeprecateEvent().setTimestamp(timeNow);
                removeDuplicateAffectedObjects(getDeprecateEvent());
                auditableEvents.add(getDeprecateEvent());
            }

            if (eventOccured(getUnDeprecateEvent())) {
                getUnDeprecateEvent().setTimestamp(timeNow);
                removeDuplicateAffectedObjects(getUnDeprecateEvent());
                auditableEvents.add(getUnDeprecateEvent());
            }

            if (eventOccured(getVersionEvent())) {
                getVersionEvent().setTimestamp(timeNow);
                removeDuplicateAffectedObjects(getVersionEvent());
                auditableEvents.add(getVersionEvent());
            }

            if (auditableEvents.size() > 0) {
                getCreateEvent().setTimestamp(timeNow);
                pm.insert(this, auditableEvents);
            }
        }
    }

    /**
     * Delete of composed objects such as ClassificationNodes within Schemes
     * can result in duplicate ObjectRefs being deleted.
     */
    private void removeDuplicateAffectedObjects(AuditableEventType ae) {
        HashSet ids = new HashSet();
        HashSet duplicateObjectRefs = new HashSet();

        //Determine duplicate ObjectRefs
        Iterator iter = ae.getAffectedObjects().getObjectRef().iterator();
        while (iter.hasNext()) {
            ObjectRefType oref = (ObjectRefType) iter.next();
            String id = oref.getId();
            if (ids.contains(id)) {
                duplicateObjectRefs.add(oref);
            } else {
                ids.add(id);
            }
        }

        //Now remove duplicate ObjectRefs
        iter = duplicateObjectRefs.iterator();
        while (iter.hasNext()) {
            ae.getAffectedObjects().getObjectRef().remove(iter.next());
        }

    }

    private boolean eventOccured(AuditableEventType ae) {
        boolean occured = false;

        if ((ae.getAffectedObjects() != null) &&
                (ae.getAffectedObjects().getObjectRef() != null) &&
                (ae.getAffectedObjects().getObjectRef().size() > 0)) {

            occured = true;
        }

        return occured;

    }

    /* REMOVED (HIEOS/BHT):
    private void sendEventsToEventManager() {
        EventManager eventManager = EventManagerFactory.getInstance().getEventManager();
        UserType user = getUser();

        if (user != null) {
            if (eventOccured(getCreateEvent())) {
                eventManager.onEvent(this, getCreateEvent());
            }

            if (eventOccured(getVersionEvent())) {
                eventManager.onEvent(this, getVersionEvent());
            }
            if (eventOccured(getSetStatusEvent())) {
                eventManager.onEvent(this, getSetStatusEvent());
            }
            if (eventOccured(getApproveEvent())) {
                eventManager.onEvent(this, getApproveEvent());
            }
            if (eventOccured(getDeprecateEvent())) {
                eventManager.onEvent(this, getDeprecateEvent());
            }
            if (eventOccured(getUnDeprecateEvent())) {
                eventManager.onEvent(this, getUnDeprecateEvent());
            }

            //Delete during update should be ignored as they are an impl artifact
            if (eventOccured(getUpdateEvent())) {
                eventManager.onEvent(this, getUpdateEvent());
            } else if (eventOccured(getDeleteEvent())) {
                eventManager.onEvent(this, getDeleteEvent());
            }
        }
    }
    */

    /*
     * Called to commit the transaction
     * Saves auditable events for this transaction prior to commit.
     * Notifies EventManager after commit.
     */
    public void commit() throws RegistryException {
        //Dont commit unless this is the last request in stack.
        if ((connection != null) && (getRegistryRequestStack().size() <= 1)) {
            try {
                //Save auditable events prior to commit

                /* HIEOS/BHT (REMOVED)
                saveAuditableEvents();
                */

                //Only commit if LCM_DO_NOT_COMMIT is unspecified or false
                String dontCommit = null;
                if (getRegistryRequestStack().size() > 0) {
                    HashMap slotsMap = bu.getSlotsFromRequest(this.getCurrentRegistryRequest());
                    dontCommit = (String) slotsMap.get(BindingUtility.CANONICAL_SLOT_LCM_DO_NOT_COMMIT);
                }
                if ((dontCommit == null) || (dontCommit.equalsIgnoreCase("false"))) {
                    connection.commit();
                    pm.releaseConnection(this, connection);
                    connection = null;
                    //New connection can be created in sendEventsToEventManager() which must be released
                    try {
                        /* HIEOS/BHT (REMOVED):
                        sendEventsToEventManager();
                         */
                        updateCache();
                    } catch (Exception e) {
                        rollback();
                        log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
                    }
                    if (connection != null) {
                        connection.commit();
                        pm.releaseConnection(this, connection);
                        connection = null;
                    }
                } else {
                    rollback();
                }

            } catch (RegistryException e) {
                rollback();
                throw e;
            } catch (JAXBException e) {
                rollback();
                throw new RegistryException(e);
            } catch (SQLException e) {
                rollback();
                throw new RegistryException(e);
            }

            //Call RequestInterceptors
            if (getRegistryRequestStack().size() == 1) {
                //Only intercept top level requests.
                //Still causing infinite recursion and StackOverflow
                //RequestInterceptorManager.getInstance().postProcessRequest(this);
            }
        }
    }

    private void updateCache() {
        Iterator iter = auditableEvents.iterator();
        while (iter.hasNext()) {
            AuditableEventType ae = (AuditableEventType) iter.next();

            //Update the cache for these objects.
            ServerCache.getInstance().onEvent(this, ae);
        }
    }

    public void rollback() throws RegistryException {
        try {
            //Dont rollback if there are multiple requests on the requestStack
            if ((connection != null) && (getRegistryRequestStack().size() <= 1)) {
                connection.rollback();
                pm.releaseConnection(this, connection);
                connection = null;
            }
        } catch (SQLException e) {
        }
    }

    public void setUser(UserType user) throws RegistryException {
        if ((getUser() != user) || (createEvent == null)) {
            super.setUser(user);
            createEvents();
        }
    }

    public Map getTopLevelObjectsMap() {
        return topLevelObjectsMap;
    }

    public Set getNewSubmittedObjectIds() {
        if (newSubmittedObjectIds == null) {
            newSubmittedObjectIds = new HashSet();
            newSubmittedObjectIds = getIdsNotInRegistry(getSubmittedObjectsMap().keySet());
        }

        return newSubmittedObjectIds;
    }

    /*
     * Gets the subset of ids that do not match ids of objects that are already in registry
     */
    public Set getIdsNotInRegistry(Set ids) {
        Set idsNotInRegistry = new HashSet();

        Iterator iter = ids.iterator();
        while (iter.hasNext()) {
            Object id = iter.next();
            if (!(getIdToLidMap().keySet().contains(id))) {
                idsNotInRegistry.add(id);
            }
        }

        return idsNotInRegistry;
    }

    public Map getNewROVersionMap() {
        return newROVersionMap;
    }

    public Map getNewRIVersionMap() {
        return newRIVersionMap;
    }

    public Map getComposedObjectsMap() {
        return composedObjectsMap;
    }

    public SortedSet getCheckedRefs() {
        return checkedRefs;
    }

    public SortedMap getFetchedOwners() {
        return fetchedOwners;
    }

    public Map getSubmittedObjectsMap() {
        return submittedObjectsMap;
    }

    public Map getObjectRefsMap() {
        return objectRefsMap;
    }

    public Map getIdMap() {
        return idMap;
    }

    public RegistryErrorListType getErrorList() {
        return errorList;
    }

    private void setErrorList(RegistryErrorListType errorList) {
        this.errorList = errorList;
    }

    public Map getConfirmationAssociations() {
        return confirmationAssociations;
    }

    public Locale getLocale() {
        if (localeOfCaller == null) {
            HashMap slotsMap = null;
            String localeStr = null;
            try {
                slotsMap = bu.getSlotsFromRequest(this.getCurrentRegistryRequest());
                localeStr = (String) slotsMap.get(CommonResourceBundle.LOCALE);
            } catch (Throwable t) {
                log.error(ServerResourceBundle.getInstance().getString("message.CouldNotGetSlotsFromTheRequest"), t);
            }
            localeOfCaller = CommonResourceBundle.getInstance().parseLocale(localeStr);
        }
        return localeOfCaller;
    }

    public List getQueryResults() {
        return queryResults;
    }

    public void setQueryResults(List queryResults) {
        //Need to create a copy because if the List param is a SingletonList
        //then remove() method does not work on it during filtering of objects
        //that are not authorized to be seen by requestor in QueryManager.
        this.queryResults = new ArrayList();
        this.queryResults.addAll(queryResults);
    }

    /**
     * If context is not a ServerRequestContext then convert it to ServerRequestContext.
     * This is used in XXManagerLocalProxy classes to convert a ClientRequestContext to a ServerRequestContext.
     *
     * @return the ServerRequestContext
     */
    public static ServerRequestContext convert(RequestContext context) throws RegistryException {
        ServerRequestContext serverContext = null;

        if (context instanceof ServerRequestContext) {
            serverContext = (ServerRequestContext) context;
        } else {
            RegistryRequestType req = null;
            if (context.getRegistryRequestStack().size() > 0) {
                req = context.getCurrentRegistryRequest();
            }
            serverContext = new ServerRequestContext(context.getId(), req);
            serverContext.setUser(context.getUser());
            serverContext.setRepositoryItemsMap(context.getRepositoryItemsMap());
        }

        return serverContext;
    }

    public List getSpecialQueryResults() {
        return specialQueryResults;
    }

    public void setSpecialQueryResults(List specialQueryResults) {
        this.specialQueryResults = specialQueryResults;
    }

    public Map getAffectedObjectsMap() {
        return affectedObjectsMap;
    }

    public void addAffectedObjectToAuditableEvent(AuditableEventType ae, RegistryObjectType ro) throws RegistryException {
        /* HIEOS/BHT (REMOVED): Left interface to minimize changes to DAO classes.
        try {
            ObjectRefType ref = BindingUtility.getInstance().rimFac.createObjectRef();
            ref.setId(ro.getId());
            ae.getAffectedObjects().getObjectRef().add(ref);
            affectedObjectsMap.put(ro.getId(), ro);
        } catch (JAXBException e) {
            throw new RegistryException(e);
        }
        */
    }

    public void addAffectedObjectsToAuditableEvent(AuditableEventType ae, ObjectRefListType orefList) throws RegistryException {
        /* HIEOS/BHT (REMOVED): Left interface to minimize changes to DAO classes.
        ae.getAffectedObjects().getObjectRef().addAll(orefList.getObjectRef());
        for (Iterator it = orefList.getObjectRef().iterator(); it.hasNext();) {
            String id = ((ObjectRefType) it.next()).getId();
            RegistryObjectType ro = getRegistryObject(id, "RegistryObject");
            affectedObjectsMap.put(id, ro);
        }
        */
    }

    public List getStoredQueryParams() {
        return storedQueryParams;
    }

    public Map getIdToLidMap() {
        return idToLidMap;
    }

    public Set getReferenceInfos() throws RegistryException {
        if (referencedInfos == null) {
            try {
                referencedInfos = new HashSet();

                Iterator iter = getSubmittedObjectsMap().entrySet().iterator();
                while (iter.hasNext()) {
                    Object o = ((java.util.Map.Entry) iter.next()).getValue();

                    if (o instanceof RegistryObjectType) {
                        RegistryObjectType ro = (RegistryObjectType) o;
                        //Get Set of ids for objects referenced from obj
                        Set refInfos = bu.getObjectRefsInRegistryObject(ro, getIdMap(), new HashSet(), -1);

                        Iterator refInfosIter = refInfos.iterator();
                        while (refInfosIter.hasNext()) {
                            referencedInfos.add(refInfosIter.next());
                        }
                    }
                }
            } catch (JAXRException e) {
                throw new RegistryException(e);
            }
        }

        return referencedInfos;
    }

    public void checkClassificationNodeRefConstraint(String nodeId, String expectedSchemeId, String attributeName) throws RegistryException {
        ClassificationSchemeType expectedScheme = null;
        try {
            //Check that objectType is the id of a ClassificationNode in ObjectType scheme
            expectedScheme = (ClassificationSchemeType) ServerCache.getInstance().getRegistryObject(this, expectedSchemeId, "ClassScheme");

            ClassificationNodeType node = (ClassificationNodeType) this.getRegistryObject(nodeId, "ClassificationNode");
            String path = node.getPath();
            String schemePath = "/" + expectedScheme.getId();

            if ((path != null) && (path.length() > 0)) {
                if (!path.startsWith(schemePath)) {
                    throw new RegistryException(ServerResourceBundle.getInstance().getString("message.notTheExpectedTypeOfNode",
                            new Object[]{attributeName, expectedScheme.getId(), nodeId}));
                }
            }
        } catch (ObjectNotFoundException e) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.notTheExpectedTypeOfNode",
                    new Object[]{attributeName, expectedScheme.getId(), nodeId}));

        }
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public Map getQueryParamsMap() {
        return queryParamsMap;
    }

    public void setQueryParamsMap(Map queryParamsMap) {
        this.queryParamsMap = queryParamsMap;
    }

    public boolean isRegistryAdministrator() throws RegistryException {
        if (isAdmin == null) {
            UserType user = getUser();
            if (user != null) {
                isAdmin = Boolean.valueOf(AuthenticationServiceImpl.getInstance().hasRegistryAdministratorRole(user));
            }
        }

        return isAdmin.booleanValue();
    }
}
