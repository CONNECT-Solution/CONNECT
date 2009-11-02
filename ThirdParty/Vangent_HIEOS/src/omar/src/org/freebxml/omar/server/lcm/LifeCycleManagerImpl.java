/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/lcm/LifeCycleManagerImpl.java,v 1.78 2007/11/20 13:32:42 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.lcm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBException;
import javax.xml.registry.InvalidRequestException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import org.freebxml.omar.common.CanonicalSchemes;
//import org.freebxml.omar.common.CommonProperties;
import org.freebxml.omar.common.spi.LifeCycleManager;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.RegistryResponseHolder;
import org.freebxml.omar.common.RepositoryItem;
import org.freebxml.omar.common.UUIDFactory;
import org.freebxml.omar.common.exceptions.ObjectsNotFoundException;
import org.freebxml.omar.common.exceptions.QuotaExceededException;
import org.freebxml.omar.common.exceptions.UnauthorizedRequestException;
import org.freebxml.omar.common.spi.RequestContext;
/* HIEOS/BHT (REMOVED):
import org.freebxml.omar.server.cms.CMSManager;
import org.freebxml.omar.server.cms.CMSManagerImpl;
*/
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.lcm.quota.QuotaServiceImpl;
import org.freebxml.omar.server.lcm.relocation.RelocationProcessor;
import org.freebxml.omar.server.repository.RepositoryItemKey;
import org.freebxml.omar.server.repository.RepositoryManager;
import org.freebxml.omar.server.repository.RepositoryManagerFactory;
import org.freebxml.omar.server.security.authentication.AuthenticationServiceImpl;
/* HIEOS/BHT (REMOVED):
import org.freebxml.omar.server.security.authentication.CertificateAuthority;
import org.freebxml.omar.server.security.authorization.AuthorizationResult;
import org.freebxml.omar.server.security.authorization.AuthorizationServiceImpl;
 */
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.lcm.ApproveObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.DeprecateObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.RelocateObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.RemoveObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.SetStatusOnObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.SubmitObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.UndeprecateObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.UpdateObjectsRequest;
import org.oasis.ebxml.registry.bindings.rim.Association;
import org.oasis.ebxml.registry.bindings.rim.AssociationType1;
import org.oasis.ebxml.registry.bindings.rim.ExtrinsicObjectType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectListType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.RegistryPackage;
import org.oasis.ebxml.registry.bindings.rim.RegistryPackageType;
import org.oasis.ebxml.registry.bindings.rim.UserType;
import org.oasis.ebxml.registry.bindings.rs.RegistryRequestType;
import org.oasis.ebxml.registry.bindings.rs.RegistryResponse;

/**
 * Implementation of the LifeCycleManager interface
 * @see
 * @author Farrukh S. Najmi
 * @author Adrian Chong
 *
 * TODO: Replace exception-handling code with calls to
 * util.createRegistryResponseFromThrowable() where appropriate.
 */
public class LifeCycleManagerImpl implements LifeCycleManager {

    /**
     * @link
     * @shapeType PatternLink
     * @pattern Singleton
     * @supplierRole Singleton factory
     */

    /*# private LifeCycleManagerImpl _objectManagerImpl; */
    private static LifeCycleManagerImpl instance = null;
    private static BindingUtility bu = BindingUtility.getInstance();
    private AuthenticationServiceImpl ac = AuthenticationServiceImpl.getInstance();
    /**
     *
     * @associates <{org.freebxml.omar.server.persistence.PersistenceManagerImpl}>
     */
    org.freebxml.omar.server.persistence.PersistenceManager pm = org.freebxml.omar.server.persistence.PersistenceManagerFactory.getInstance().getPersistenceManager();
    /**
     *
     * @associates <{org.freebxml.omar.common.QueryManagerImpl}>
     */
    org.freebxml.omar.common.spi.QueryManager qm = org.freebxml.omar.common.spi.QueryManagerFactory.getInstance().getQueryManager();
    QuotaServiceImpl qs = QuotaServiceImpl.getInstance();
    org.freebxml.omar.server.common.Utility util = org.freebxml.omar.server.common.Utility.getInstance();
    RepositoryManager rm = RepositoryManagerFactory.getInstance().getRepositoryManager();
    UUIDFactory uf = UUIDFactory.getInstance();
    boolean bypassCMS = false;
    /* HIEOS/BHT (REMOVED):
    CMSManager cmsm = new CMSManagerImpl(); //CMSManagerFactory.getInstance().getContentManagementServiceManager();
    */
    private static final Log log = LogFactory.getLog(LifeCycleManagerImpl.class);

    protected LifeCycleManagerImpl() {
        bypassCMS = Boolean.valueOf(RegistryProperties.getInstance().getProperty("org.freebxml.omar.server.lcm.bypassCMS", "false")).booleanValue();

    }

    public synchronized static LifeCycleManagerImpl getInstance() {
        if (instance == null) {
            instance = new LifeCycleManagerImpl();
        }

        return instance;
    }

    /**
     * Submits one or more RegistryObjects and one or more repository items.
     * <br>
     * <br>
     * Note: One more special feature that is not in the RS spec. version 2.
     * The SubmitObjectsRequest allows updating objects.If a object of a particular
     * id already exist, it is updated instead of trying to be inserted.
     * @param idToRepositoryItemMap is a HashMap with key that is id of a RegistryObject and value that is a RepositoryItem instance.
     */
    public RegistryResponse submitObjects(RequestContext context) throws RegistryException {
        context = ServerRequestContext.convert(context);
        SubmitObjectsRequest req = (SubmitObjectsRequest) context.getCurrentRegistryRequest();
        UserType user = context.getUser();
        Map idToRepositoryItemMap = context.getRepositoryItemsMap();
        String errorCodeContext = "LifeCycleManagerImpl.submitObjects";
        String errorCode = "unknown";

        RegistryResponse resp = null;
        try {
            calculateEffectiveUser(((ServerRequestContext) context));
            RegistryObjectListType objs = req.getRegistryObjectList();

            // insert member objects of RegistryPackages
            int objsSize = objs.getIdentifiable().size();
            for (int i = 0; i < objsSize; i++) {
                Object identObj = objs.getIdentifiable().get(i);

                if (identObj instanceof RegistryPackage) {
                    insertPackageMembers(objs, (RegistryPackage) identObj);
                }
            }

            //Split Identifiables by RegistryObjects and ObjectRefs
            bu.getObjectRefsAndRegistryObjects(objs, ((ServerRequestContext) context).getTopLevelObjectsMap(), ((ServerRequestContext) context).getObjectRefsMap());

            processConfirmationAssociations(((ServerRequestContext) context));

            ((ServerRequestContext) context).checkObjects();

            //Auth check must be after checkObjects as it needs objectRefsMap etc.
            //for doing Access Control on references

            checkAuthorizedAll(((ServerRequestContext) context));

            /* HIEOS/BHT (REMOVED):
            if (!bypassCMS) {
                //Now perform any content cataloging and validation for ExtrinsicObjects
                log.trace(ServerResourceBundle.getInstance().getString("message.CallingInvokeServicesAt", new Object[]{new Long(System.currentTimeMillis())}));
                cmsm.invokeServices(((ServerRequestContext) context));
                log.trace(ServerResourceBundle.getInstance().getString("message.DoneCallingInvokeServicesAt", new Object[]{new Long(System.currentTimeMillis())}));
            }
             */

            /*
             * For RegistryObjects, the DAO will take care which objects already
             * exist and update them instead
             */
            log.trace(ServerResourceBundle.getInstance().getString("message.CallingPminsertAt", new Object[]{new Long(System.currentTimeMillis())}));
            ArrayList list = new ArrayList();
            list.addAll(((ServerRequestContext) context).getTopLevelObjectsMap().values());
            pm.insert(((ServerRequestContext) context), list);

            log.trace(ServerResourceBundle.getInstance().getString("message.DoneCallingPminsertAt", new Object[]{new Long(System.currentTimeMillis())}));

            resp = bu.rsFac.createRegistryResponse();
            resp.setStatus(BindingUtility.CANONICAL_RESPONSE_STATUS_TYPE_ID_Success);

            if (((ServerRequestContext) context).getErrorList().getRegistryError().size() > 0) {
                // warning exists
                resp.setRegistryErrorList(((ServerRequestContext) context).getErrorList());
            }

            //Must be after CMS since CMS could generate more repository items.
            if ((((ServerRequestContext) context).getRepositoryItemsMap() != null) &&
                    (!(((ServerRequestContext) context).getRepositoryItemsMap().isEmpty()))) {
                submitRepositoryItems(((ServerRequestContext) context));
            }

        } catch (RegistryException e) {
            ((ServerRequestContext) context).rollback();
            throw e;
        } catch (IllegalStateException e) {
            //?? This is a JAXR spec bug that we do not send an UnauthorizedRequestException
            ((ServerRequestContext) context).rollback();
            throw e;
        } catch (Exception e) {
            ((ServerRequestContext) context).rollback();
            throw new RegistryException(e);
        }

        ((ServerRequestContext) context).commit();
        return resp;
    }

    /**
     * Iterates through the members of the RegistryPackage <code>regPkg</code>,
     * creates and adds a 'HasMember' association for each member 
     * of the RegistryPackages to the RegistryObjectList <code>regObjs</code> together with the member.
     * 
     * @param regObjs RegistryObjectList to append RegistryPackage members and their associations.
     * @param regPkg Package to get members from.
     * @throws javax.xml.bind.JAXBException 
     */
    private void insertPackageMembers(RegistryObjectListType regObjs, RegistryPackageType regPkg) throws JAXBException {
        if (regPkg.getRegistryObjectList() != null && regPkg.getRegistryObjectList().getIdentifiable().size() > 0) {

            for (int j = 0; j < regPkg.getRegistryObjectList().getIdentifiable().size(); j++) {
                Object obj = regPkg.getRegistryObjectList().getIdentifiable().get(j);

                if (obj instanceof RegistryPackageType) {
                    insertPackageMembers(regObjs, (RegistryPackageType) obj);
                }

                if (obj instanceof RegistryObjectType) {
                    RegistryObjectType ro = (RegistryObjectType) obj;
                    String assId = org.freebxml.omar.common.Utility.getInstance().createId();
                    Association asso = BindingUtility.getInstance().rimFac.createAssociation();
                    asso.setId(assId);
                    asso.setLid(assId);
                    asso.setAssociationType(CanonicalSchemes.CANONICAL_ASSOCIATION_TYPE_ID_HasMember);
                    asso.setSourceObject(regPkg.getId());
                    asso.setTargetObject(ro.getId());
                    regObjs.getIdentifiable().add(ro);
                    regObjs.getIdentifiable().add(asso);
                }
            }
            regPkg.getRegistryObjectList().getIdentifiable().clear();
        }
    }

    /**
     * Processes Associations looking for Associations that already exist and are being
     * submitted by source or target owner and are identical in state to existing
     * Association in registry.    
     *
     * ebXML Registry 3.0 hasber discarded association confirmation in favour of 
     * Role Based access control. However, freebXML Registry supports it in an 
     * impl specific manner as this is required by JAXR 1.0 API.
     * This SHOULD be removed once JAXR 2.0 no longer requires it for ebXML Registry
     * in future.
     *
     * The processing updates the Association to add a special Impl specific slot to 
     * remember the confirmation state change.
     * 
     * TODO: Need to do unconfirm when src or target owner removes an Association they had
     * previously confirmed.
     */
    private void processConfirmationAssociations(ServerRequestContext context) throws RegistryException {

        try {
            //Make a copy to avoid ConcurrentModificationException
            ArrayList topLevelObjects = new ArrayList(((ServerRequestContext) context).getTopLevelObjectsMap().values());
            Iterator iter = topLevelObjects.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (obj instanceof AssociationType1) {
                    AssociationType1 ass = (AssociationType1) obj;
                    HashMap slotsMap = bu.getSlotsFromRegistryObject(ass);
                    String beingConfirmed = (String) slotsMap.remove(bu.IMPL_SLOT_ASSOCIATION_IS_BEING_CONFIRMED);
                    if ((beingConfirmed != null) && (beingConfirmed.equalsIgnoreCase("true"))) {
                        //Need to set slotMap again
                        ass.getSlot().clear();
                        bu.addSlotsToRegistryObject(ass, slotsMap);

                        ((ServerRequestContext) context).getConfirmationAssociations().put(ass.getId(), ass);
                    }
                }
            }
        } catch (javax.xml.bind.JAXBException e) {
            throw new RegistryException(e);
        }
    }

    /**
     * Submits any RepositoryItems in ((ServerRequestContext)context).
     *
     * <p>Checks quotas and fixes IDs as necessary.
     *
     * @param context a <code>RequestContext</code>
     */
    private void submitRepositoryItems(ServerRequestContext context)
            throws QuotaExceededException, RegistryException {
        qs.checkQuota(((ServerRequestContext) context).getUser().getId());

        //fix ri ID to match
        //first ExtrinsicObject (in case where ri is submitted without id)
        //only works for submission of one ri and one ExtrinsicObject
        //correctRepositoryItemId(((ServerRequestContext)context).topLevelObjectsMap.values(),
        //			((ServerRequestContext)context).repositoryItemsMap);

        // It will select which repository items already exist and update
        Map idToNotExistingItemsMap = updateExistingRepositoryItems(((ServerRequestContext) context));
        storeRepositoryItems(((ServerRequestContext) context), idToNotExistingItemsMap);
    }

    /**
     * Stores the repository items in idToRepositoryItemMap in the repository
     * @throws RegistryException when the items already exist
     */
    private void storeRepositoryItems(ServerRequestContext context,
            Map idToRepositoryItemMap)
            throws RegistryException {
        if (idToRepositoryItemMap != null) {
            Collection keySet = idToRepositoryItemMap.keySet();

            if (keySet != null) {
                Iterator iter = keySet.iterator();

                while (iter.hasNext()) {
                    String id = (String) iter.next();
                    RepositoryItem ri =
                            (RepositoryItem) idToRepositoryItemMap.get(id);

                    DataHandler dh = ri.getDataHandler();


                    // Inserting the repository item
                    rm.insert(((ServerRequestContext) context), ri);
                }
            }
        }
    }

    /**
     * Calculates the effective user to be used as the identity of the requestor.
     * Implements ability to re-assign user to a different user than the caller
     * if:
     *
     * a) The actual caller is a RegistryAdministrator role, and
     * b) The request specifies the CANONICAL_SLOT_LCM_OWNER.
     *
     */
    void calculateEffectiveUser(ServerRequestContext context) throws RegistryException {
        try {
            UserType caller = ((ServerRequestContext) context).getUser();

            //See if CANONICAL_SLOT_LCM_OWNER defined on request
            HashMap slotsMap = bu.getSlotsFromRequest(((ServerRequestContext) context).getCurrentRegistryRequest());
            String effectiveUserId = (String) slotsMap.get(bu.CANONICAL_SLOT_LCM_OWNER);
            if (effectiveUserId != null) {
                if (ac.hasRegistryAdministratorRole(caller)) {
                    UserType effectiveUser = (UserType) pm.getRegistryObject(((ServerRequestContext) context), effectiveUserId, "User");
                    if (effectiveUser == null) {
                        throw new RegistryException(ServerResourceBundle.getInstance().getString("message.specifiedUserNotOwner",
                                new Object[]{effectiveUserId}));
                    }
                    ((ServerRequestContext) context).setUser(effectiveUser);
                } else {
                    throw new InvalidRequestException(ServerResourceBundle.getInstance().getString("message.requestSlotInvalid",
                            new Object[]{bu.CANONICAL_SLOT_LCM_OWNER}));
                }
            }
        } catch (javax.xml.bind.JAXBException e) {
            throw new RegistryException(e);
        } catch (InvalidRequestException e) {
            throw new RegistryException(e);
        }
    }

    /**
     * Updates any RepositoryItems in ((ServerRequestContext)context).
     *
     * <p>Checks quotas and fixes IDs as necessary.
     *
     * @param context a <code>RequestContext</code>
     */
    private void updateRepositoryItems(ServerRequestContext context)
            throws QuotaExceededException, RegistryException {
        qs.checkQuota(((ServerRequestContext) context).getUser().getId());

        //fix ri ID to match
        //first ExtrinsicObject (in case where ri is submitted
        //without id) only works for submission of one ri and one
        //ExtrinsicObject
        //correctRepositoryItemId(((ServerRequestContext)context).topLevelObjectsMap.values(),
        //			((ServerRequestContext)context).repositoryItemsMap);

        updateRepositoryItems(((ServerRequestContext) context), ((ServerRequestContext) context).getRepositoryItemsMap());
    }

    /**
     * It should be called by submitObjects() to update existing Repository Items
     * @return HashMap of id To RepositoryItem, which are not existing
     */
    private Map updateExistingRepositoryItems(ServerRequestContext context)
            throws RegistryException {

        // Create two maps to store existing and non-existing items
        HashMap notExistItems = new HashMap();
        HashMap existingItems = new HashMap();

        Iterator itemsIdsIter = ((ServerRequestContext) context).getRepositoryItemsMap().keySet().iterator();
        while (itemsIdsIter.hasNext()) {
            String id = (String) itemsIdsIter.next();
            ExtrinsicObjectType eo = (ExtrinsicObjectType) ((ServerRequestContext) context).getSubmittedObjectsMap().get(id);
            boolean exists = rm.itemExists(new RepositoryItemKey(eo.getLid(), eo.getContentVersionInfo().getVersionName()));

            if (exists) {
                existingItems.put(id, ((ServerRequestContext) context).getRepositoryItemsMap().get(id));
            } else {
                notExistItems.put(id, ((ServerRequestContext) context).getRepositoryItemsMap().get(id));
            }
        }

        updateRepositoryItems(((ServerRequestContext) context), existingItems);

        return notExistItems;
    }

    /** Approves one or more previously submitted objects */
    public RegistryResponse approveObjects(RequestContext context) throws RegistryException {
        context = ServerRequestContext.convert(context);
        ApproveObjectsRequest req = (ApproveObjectsRequest) context.getCurrentRegistryRequest();
        UserType user = context.getUser();
        String errorCodeContext = "LifeCycleManagerImpl.approveObjects";
        String errorCode = "unknown";
        RegistryResponse resp = null;
        try {
            context = new ServerRequestContext("LifeCycleManagerImpl.approveObjects", req);
            ((ServerRequestContext) context).setUser(user);

            checkAuthorizedAll(((ServerRequestContext) context));

            List idList = new java.util.ArrayList();
            //Add explicitly specified oref params
            List orefs = bu.getObjectRefsFromObjectRefList(req.getObjectRefList());

            //Append those orefs specified via ad hoc query param
            orefs.addAll(((ServerRequestContext) context).getObjectsRefsFromQueryResults(req.getAdhocQuery()));

            Iterator orefsIter = orefs.iterator();

            while (orefsIter.hasNext()) {
                ObjectRefType oref = (ObjectRefType) orefsIter.next();
                idList.add(oref.getId());
            }

            pm.updateStatus(((ServerRequestContext) context), idList,
                    bu.CANONICAL_STATUS_TYPE_ID_Approved);
            resp = bu.rsFac.createRegistryResponse();
            resp.setStatus(BindingUtility.CANONICAL_RESPONSE_STATUS_TYPE_ID_Success);

            if (((ServerRequestContext) context).getErrorList().getRegistryError().size() > 0) {
                // warning exists
                resp.setRegistryErrorList(((ServerRequestContext) context).getErrorList());
            }
        } catch (RegistryException e) {
            ((ServerRequestContext) context).rollback();
            throw e;
        } catch (Exception e) {
            ((ServerRequestContext) context).rollback();
            throw new RegistryException(e);
        }

        ((ServerRequestContext) context).commit();
        return resp;
    }

    /**
     * @throws RegistryException when the Repository items do not exist
     */
    private void updateRepositoryItems(ServerRequestContext context,
            Map idToRepositoryItemMap)
            throws RegistryException {
        if (idToRepositoryItemMap != null) {
            Collection keySet = idToRepositoryItemMap.keySet();

            if (keySet != null) {
                Iterator iter = keySet.iterator();

                while (iter.hasNext()) {
                    String id = (String) iter.next();
                    RepositoryItem roNew = (RepositoryItem) idToRepositoryItemMap.get(id);

                    // Updating the repository item
                    rm.update(((ServerRequestContext) context), roNew);
                }
            }
        }
    }

    public RegistryResponse updateObjects(RequestContext context) throws RegistryException {
        context = ServerRequestContext.convert(context);
        RegistryResponse resp = null;
        UpdateObjectsRequest req = (UpdateObjectsRequest) ((ServerRequestContext) context).getCurrentRegistryRequest();
        Map idToRepositoryMap = ((ServerRequestContext) context).getRepositoryItemsMap();
        UserType user = ((ServerRequestContext) context).getUser();

        try {
            calculateEffectiveUser(((ServerRequestContext) context));

            ((ServerRequestContext) context).setRepositoryItemsMap(idToRepositoryMap);

            RegistryObjectListType objs = req.getRegistryObjectList();

            //Split Identifiables by RegistryObjects and ObjectRefs
            bu.getObjectRefsAndRegistryObjects(objs, ((ServerRequestContext) context).getTopLevelObjectsMap(), ((ServerRequestContext) context).getObjectRefsMap());


            ((ServerRequestContext) context).checkObjects();

            //Auth check must be after checkObjects as it needs
            //objectRefsMap etc. for doing Access Control on
            //references
            checkAuthorizedAll(((ServerRequestContext) context));

            if ((((ServerRequestContext) context).getRepositoryItemsMap() != null) &&
                    (!(((ServerRequestContext) context).getRepositoryItemsMap().isEmpty()))) {
                updateRepositoryItems(((ServerRequestContext) context), ((ServerRequestContext) context).getRepositoryItemsMap());
            }

            ArrayList list = new ArrayList();
            list.addAll(((ServerRequestContext) context).getTopLevelObjectsMap().values());
            pm.update(((ServerRequestContext) context), list);

            resp = bu.rsFac.createRegistryResponse();
            resp.setStatus(BindingUtility.CANONICAL_RESPONSE_STATUS_TYPE_ID_Success);

            if (((ServerRequestContext) context).getErrorList().getRegistryError().size() > 0) {
                // warning exists
                resp.setRegistryErrorList(((ServerRequestContext) context).getErrorList());
            }
        } catch (RegistryException e) {
            ((ServerRequestContext) context).rollback();
            throw e;
        } catch (IllegalStateException e) {
            //?? This is a JAXR spec bug that we do not send an UnauthorizedRequestException
            ((ServerRequestContext) context).rollback();
            throw e;
        } catch (Exception e) {
            ((ServerRequestContext) context).rollback();
            throw new RegistryException(e);
        }

        ((ServerRequestContext) context).commit();
        return resp;
    }

    /** Sets the status of specified objects. This is an extension request that will be adde to ebRR 3.1?? */
    public RegistryResponse setStatusOnObjects(RequestContext context) throws RegistryException {
        context = ServerRequestContext.convert(context);
        RegistryResponse resp = null;
        SetStatusOnObjectsRequest req = (SetStatusOnObjectsRequest) ((ServerRequestContext) context).getCurrentRegistryRequest();
        Map idToRepositoryMap = ((ServerRequestContext) context).getRepositoryItemsMap();
        UserType user = ((ServerRequestContext) context).getUser();

        try {
            checkAuthorizedAll(((ServerRequestContext) context));

            List idList = new java.util.ArrayList();
            //Add explicitly specified oref params
            List orefs = bu.getObjectRefsFromObjectRefList(req.getObjectRefList());

            //Append those orefs specified via ad hoc query param
            orefs.addAll(((ServerRequestContext) context).getObjectsRefsFromQueryResults(req.getAdhocQuery()));

            Iterator orefsIter = orefs.iterator();

            while (orefsIter.hasNext()) {
                ObjectRefType oref = (ObjectRefType) orefsIter.next();
                idList.add(oref.getId());
            }

            String statusId = req.getStatus();
            pm.updateStatus(((ServerRequestContext) context), idList, statusId);
            resp = bu.rsFac.createRegistryResponse();
            resp.setStatus(BindingUtility.CANONICAL_RESPONSE_STATUS_TYPE_ID_Success);

            if (((ServerRequestContext) context).getErrorList().getRegistryError().size() > 0) {
                // warning exists
                resp.setRegistryErrorList(((ServerRequestContext) context).getErrorList());
            }
        } catch (RegistryException e) {
            ((ServerRequestContext) context).rollback();
            throw e;
        } catch (Exception e) {
            ((ServerRequestContext) context).rollback();
            throw new RegistryException(e);
        }

        ((ServerRequestContext) context).commit();
        return resp;
    }

    /** Deprecates one or more previously submitted objects */
    public RegistryResponse deprecateObjects(RequestContext context) throws RegistryException {
        context = ServerRequestContext.convert(context);
        RegistryResponse resp = null;
        DeprecateObjectsRequest req = (DeprecateObjectsRequest) ((ServerRequestContext) context).getCurrentRegistryRequest();
        Map idToRepositoryMap = ((ServerRequestContext) context).getRepositoryItemsMap();
        UserType user = ((ServerRequestContext) context).getUser();

        try {
            checkAuthorizedAll(((ServerRequestContext) context));

            List idList = new java.util.ArrayList();
            //Add explicitly specified oref params
            List orefs = bu.getObjectRefsFromObjectRefList(req.getObjectRefList());

            //Append those orefs specified via ad hoc query param
            orefs.addAll(((ServerRequestContext) context).getObjectsRefsFromQueryResults(req.getAdhocQuery()));

            Iterator orefsIter = orefs.iterator();

            while (orefsIter.hasNext()) {
                ObjectRefType oref = (ObjectRefType) orefsIter.next();
                idList.add(oref.getId());
            }

            pm.updateStatus(((ServerRequestContext) context), idList,
                    bu.CANONICAL_STATUS_TYPE_ID_Deprecated);
            resp = bu.rsFac.createRegistryResponse();
            resp.setStatus(BindingUtility.CANONICAL_RESPONSE_STATUS_TYPE_ID_Success);

            if (((ServerRequestContext) context).getErrorList().getRegistryError().size() > 0) {
                // warning exists
                resp.setRegistryErrorList(((ServerRequestContext) context).getErrorList());
            }
        } catch (RegistryException e) {
            ((ServerRequestContext) context).rollback();
            throw e;
        } catch (Exception e) {
            ((ServerRequestContext) context).rollback();
            throw new RegistryException(e);
        }

        ((ServerRequestContext) context).commit();
        return resp;
    }

    public RegistryResponse unDeprecateObjects(RequestContext context) throws RegistryException {
        context = ServerRequestContext.convert(context);
        RegistryResponse resp = null;
        UndeprecateObjectsRequest req = (UndeprecateObjectsRequest) ((ServerRequestContext) context).getCurrentRegistryRequest();
        Map idToRepositoryMap = ((ServerRequestContext) context).getRepositoryItemsMap();
        UserType user = ((ServerRequestContext) context).getUser();

        try {
            checkAuthorizedAll(((ServerRequestContext) context));

            List idList = new java.util.ArrayList();
            //Add explicitly specified oref params
            List orefs = bu.getObjectRefsFromObjectRefList(req.getObjectRefList());

            //Append those orefs specified via ad hoc query param
            orefs.addAll(((ServerRequestContext) context).getObjectsRefsFromQueryResults(req.getAdhocQuery()));

            Iterator orefsIter = orefs.iterator();

            while (orefsIter.hasNext()) {
                ObjectRefType oref = (ObjectRefType) orefsIter.next();
                idList.add(oref.getId());
            }

            pm.updateStatus(((ServerRequestContext) context), idList,
                    bu.CANONICAL_STATUS_TYPE_ID_Submitted);
            resp = bu.rsFac.createRegistryResponse();
            resp.setStatus(BindingUtility.CANONICAL_RESPONSE_STATUS_TYPE_ID_Success);

            if (((ServerRequestContext) context).getErrorList().getRegistryError().size() > 0) {
                // warning exists
                resp.setRegistryErrorList(((ServerRequestContext) context).getErrorList());
            }
        } catch (RegistryException e) {
            ((ServerRequestContext) context).rollback();
            throw e;
        } catch (Exception e) {
            ((ServerRequestContext) context).rollback();
            throw new RegistryException(e);
        }

        ((ServerRequestContext) context).commit();
        return resp;
    }

    /**
     * Removes one or more previously submitted objects from the registry. If the
     * deletionScope is "DeleteRepositoryItemOnly", it will assume all the
     * ObjectRef under ObjectRefList is referencing repository items. If the
     * deletionScope is "DeleteAll", the reference may be either RegistryObject
     * or repository item. In both case, if the referenced object cannot be found,
     * RegistryResponse with errors list will be returned.
     */
    public RegistryResponse removeObjects(RequestContext context) throws RegistryException {
        context = ServerRequestContext.convert(context);
        ServerRequestContext _context = (ServerRequestContext) context;
        RegistryResponse resp = null;
        RemoveObjectsRequest req = (RemoveObjectsRequest) _context.getCurrentRegistryRequest();
        Map idToRepositoryMap = _context.getRepositoryItemsMap();
        UserType user = _context.getUser();

        //This request option instructs the server to delete objects even if references exist to them
        boolean forceDelete = false;

        //This request option instructs the server to also delete the network of objects reachable by
        //reference from the objects being deleted. This option is not implemented yet.
        boolean cascadeDelete = false;

        try {
            //Get relevant request slots if any
            HashMap requestSlots = bu.getSlotsFromRequest(req);

            if (requestSlots.containsKey(bu.CANONICAL_SLOT_DELETE_MODE_FORCE)) {
                String val = (String) requestSlots.get(bu.CANONICAL_SLOT_DELETE_MODE_FORCE);
                if (val.trim().equalsIgnoreCase("true")) {
                    forceDelete = true;
                }
            }

            if (requestSlots.containsKey(bu.CANONICAL_SLOT_DELETE_MODE_CASCADE)) {
                String val = (String) requestSlots.get(bu.CANONICAL_SLOT_DELETE_MODE_CASCADE);
                if (val.trim().equalsIgnoreCase("true")) {
                    cascadeDelete = true;
                }
            }

            List orefs = null;
            if (req.getObjectRefList() == null) {
                org.oasis.ebxml.registry.bindings.rim.ObjectRefList orList = bu.rimFac.createObjectRefList();
                req.setObjectRefList(orList);
            }

            //Add explicitly specified oref params
            orefs = bu.getObjectRefsFromObjectRefList(req.getObjectRefList());

            //Append those orefs specified via ad hoc query param
            orefs.addAll(_context.getObjectsRefsFromQueryResults(req.getAdhocQuery()));

            Iterator orefsIter = orefs.iterator();

            while (orefsIter.hasNext()) {
                ObjectRefType oref = (ObjectRefType) orefsIter.next();
                _context.getObjectRefsMap().put(oref.getId(), oref);
            }

            List idList = new ArrayList(_context.getObjectRefsMap().keySet());
            pm.updateIdToLidMap(_context, _context.getObjectRefsMap().keySet(), "RegistryObject");
            Set idsNotInRegistry = _context.getIdsNotInRegistry(_context.getObjectRefsMap().keySet());
            if (idsNotInRegistry.size() > 0) {
                throw new ObjectsNotFoundException(new ArrayList(idsNotInRegistry));
            }
            checkAuthorizedAll(_context);

            String deletionScope = BindingUtility.CANONICAL_DELETION_SCOPE_TYPE_ID_DeleteAll;

            if (req.getDeletionScope() != null) {
                deletionScope = req.getDeletionScope();
            }

            //DeletionScope=DeleteRepositoryItemOnly. If any repository item
            //does not exist, it will stop
            if (deletionScope.equals(BindingUtility.CANONICAL_DELETION_SCOPE_TYPE_ID_DeleteRepositoryItemOnly)) {
                List notExist = rm.itemsExist(idList);

                if (notExist.size() > 0) {
                    throw new ObjectsNotFoundException(notExist);
                }

                //Should RepositoryItem be deleted even when referencesExist??
                //if (!forceDelete) {
                //  pm.checkIfReferencesExist((ServerRequestContext) context, idList);
                //}
                rm.delete(idList);
            } else if (deletionScope.equals(BindingUtility.CANONICAL_DELETION_SCOPE_TYPE_ID_DeleteAll)) {
                //find out which id is not an id of a repository item (i.e.
                //referencing RO only
                List nonItemsIds = rm.itemsExist(idList);

                //find out which id is an id of a repository item
                List itemsIds = new java.util.ArrayList();
                Iterator idListIt = idList.iterator();

                while (idListIt.hasNext()) {
                    Object id = idListIt.next();

                    if (!nonItemsIds.contains(id)) {
                        itemsIds.add(id);
                    }
                }


                if (!forceDelete) {
                    pm.checkIfReferencesExist((ServerRequestContext) context, idList);
                }

                // Delete the repository items
                rm.delete(itemsIds);

                //Delete all ROs with the ids
                pm.delete(_context, orefs);
            } else {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.undefinedDeletionScope"));
            }

            resp = bu.rsFac.createRegistryResponse();
            resp.setStatus(BindingUtility.CANONICAL_RESPONSE_STATUS_TYPE_ID_Success);
        } catch (RegistryException e) {
            ((ServerRequestContext) context).rollback();
            throw e;
        } catch (Exception e) {
            ((ServerRequestContext) context).rollback();
            throw new RegistryException(e);
        }

        ((ServerRequestContext) context).commit();
        return resp;
    }

    /** Sends an impl specific protocol extension request. */
    public RegistryResponseHolder extensionRequest(RequestContext context) throws RegistryException {
        /* HIEOS/BHT (REMOVED/STUBBED OUT):
        context = ServerRequestContext.convert(context);
        RegistryResponseHolder respHolder = null;
        RegistryRequestType req = (RegistryRequestType) ((ServerRequestContext) context).getCurrentRegistryRequest();
        Map idToRepositoryMap = ((ServerRequestContext) context).getRepositoryItemsMap();
        UserType user = ((ServerRequestContext) context).getUser();

        try {
            HashMap slotsMap = bu.getSlotsFromRequest(req);
            String signCertProtocol = (String) slotsMap.get(bu.FREEBXML_REGISTRY_PROTOCOL_SIGNCERT);
            if ("true".equalsIgnoreCase(signCertProtocol)) {
                respHolder = CertificateAuthority.getInstance().signCertificateRequest(user, req, idToRepositoryMap);
            } else {
                throw new RegistryException("Unknown extensionRequest");
            }
        } catch (RegistryException e) {
            throw e;
        } catch (Exception e) {
            throw new RegistryException(e);
        }

        return respHolder;
         */
        return null;
    }

    /*
     * Fix Repository item's ID to match ID in first
     * associated ExtrinsicObject. (in case where ri
     * is submitted without id or id doesn't match id
     * of ExtrinsicObject).
     * Currently Only works for submission of one
     * Repository item and its associated ExtrinsicObject.
     * Called in updateObjects() and submitObjects().
     *
     * Commented as it is trying to fix an error condition and caused
     * problems in VersioningTest.
    private void correctRepositoryItemId(Collection objs,
    Map idToRepositoryItemMap) {
    if (objs.size() == 1) {
    Object obj = objs.iterator().next();

    if (obj instanceof RegistryEntryType) {
    RegistryEntryType firstRe = (RegistryEntryType) obj;

    if ((firstRe != null) && firstRe instanceof ExtrinsicObject) {
    String correctId = firstRe.getId();

    if (idToRepositoryItemMap.size() == 1) {
    Iterator attachIter = idToRepositoryItemMap.keySet()
    .iterator();
    String attachIdKey = (String) attachIter.next();
    RepositoryItem attachRi = (RepositoryItem) idToRepositoryItemMap.get(attachIdKey);
    String attachId = attachRi.getId();

    if ((correctId != null) && !correctId.equals(attachId)) {
    System.err.println(
    "[LifeCycleManager::correctRepositoryItemId()]" +
    " RepositoryItem id [" + attachId +
    "] does not match Registry Object id [" +
    correctId + "]");
    System.err.println(
    "[LifeCycleManager::correctRepositoryItemId()] " +
    " Updating RepositoryItem id to " + correctId);

    //remove old key
    idToRepositoryItemMap.remove(attachRi.getId());
    attachRi.setId(correctId);

    //add new key and ri
    idToRepositoryItemMap.put(correctId, attachRi);
    }
    }
    }
    }
    }
    }
     */
    public RegistryResponse relocateObjects(RequestContext context) throws RegistryException {
        context = ServerRequestContext.convert(context);
        RegistryResponse resp = null;
        RelocateObjectsRequest req = (RelocateObjectsRequest) ((ServerRequestContext) context).getCurrentRegistryRequest();
        Map idToRepositoryMap = ((ServerRequestContext) context).getRepositoryItemsMap();
        UserType user = ((ServerRequestContext) context).getUser();

        try {
            RelocationProcessor relocationMgr = new RelocationProcessor(((ServerRequestContext) context));
            resp = relocationMgr.relocateObjects();
        } catch (Exception e) {
            ((ServerRequestContext) context).rollback();
            throw new RegistryException(e);
        }

        ((ServerRequestContext) context).commit();
        return resp;

    }

    /**
     * Dumps the old and new IDs in idMap.
     *
     * @param idMap a <code>HashMap</code> value
     */
    private void dumpIdMap(HashMap idMap) {
        Collection ids = idMap.keySet();
        Iterator idsIter = ids.iterator();

        while (idsIter.hasNext()) {
            String key = (String) idsIter.next();
            log.trace(key + "    " + idMap.get(key));
        }

        log.trace(ServerResourceBundle.getInstance().getString("message.ObjectsFound", new Object[]{new Integer(ids.size())}));
    }

    /**
     * Checks that the user in the current context is authorized to do
     * everything necessary to process the current request.
     *
     * @param context a <code>RequestContext</code> value
     * @exception UnauthorizedRequestException if an error occurs
     * @exception RegistryException if an error occurs
     */
    private void checkAuthorizedAll(ServerRequestContext context)
            throws UnauthorizedRequestException, RegistryException {

        /* HIEOS/BHT (NOOP):
        boolean noRegRequired = Boolean.valueOf(CommonProperties.getInstance().getProperty("omar.common.noUserRegistrationRequired", "false")).booleanValue();

        if (!noRegRequired) {
            checkAuthorized(((ServerRequestContext) context),
                    AuthorizationResult.PERMIT_NONE | AuthorizationResult.PERMIT_SOME);
        }
        */
    }

    /**
     * Checks that the user in the current context is authorized to do
     * everything except the specified authorization levels.
     *
     * @param context a <code>RequestContext</code> value
     * @param throwExceptionOn Flags specifying when to throw exceptions
     * @exception UnauthorizedRequestException if an error occurs
     * @exception RegistryException if an error occurs
     */
    private void checkAuthorized(ServerRequestContext context,
            int throwExceptionOn)
            throws UnauthorizedRequestException, RegistryException {
        /* HIEOS/BHT (NOOP):
        AuthorizationResult authRes =
                AuthorizationServiceImpl.getInstance().checkAuthorization(((ServerRequestContext) context));
        authRes.throwExceptionOn(throwExceptionOn);
        */
    }
}
