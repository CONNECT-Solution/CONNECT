/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/lcm/relocation/RelocationProcessor.java,v 1.8 2005/11/28 20:17:49 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.lcm.relocation;

import java.util.ArrayList;
import java.util.List;

import org.freebxml.omar.common.BindingUtility;
import org.freebxml.omar.common.spi.QueryManager;
import org.freebxml.omar.common.spi.QueryManagerFactory;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.exceptions.AuthorizationException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.persistence.PersistenceManager;
import org.freebxml.omar.server.persistence.PersistenceManagerFactory;
import org.freebxml.omar.server.security.authentication.AuthenticationServiceImpl;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.lcm.RelocateObjectsRequest;
import org.oasis.ebxml.registry.bindings.query.AdhocQueryRequestType;
import org.oasis.ebxml.registry.bindings.query.AdhocQueryResponseType;
import org.oasis.ebxml.registry.bindings.query.ResponseOption;
import org.oasis.ebxml.registry.bindings.query.ReturnType;
import org.oasis.ebxml.registry.bindings.rim.AdhocQueryType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectListType;
import org.oasis.ebxml.registry.bindings.rim.UserType;
import org.oasis.ebxml.registry.bindings.rs.RegistryErrorListType;
import org.oasis.ebxml.registry.bindings.rs.RegistryResponse;

/**
 * Processes an object relocation request.
 * Designed for one time use.
 * 
 * @author Farrukh S. Najmi
 * 
 */
public class RelocationProcessor {
    private static BindingUtility bu = BindingUtility.getInstance();
    private QueryManager qm = QueryManagerFactory.getInstance().getQueryManager();
    private org.freebxml.omar.server.common.Utility util = org.freebxml.omar.server.common.Utility.getInstance();
    private AuthenticationServiceImpl ac = AuthenticationServiceImpl.getInstance();
    private PersistenceManager pm = PersistenceManagerFactory.getInstance().getPersistenceManager();
    
    private ServerRequestContext context = null;
    boolean isAdmin = false;
    RegistryResponse resp = null;
    AdhocQueryType ahq = null;
    ObjectRefType srcRegistryRef = null;
    ObjectRefType destRegistryRef = null;
    ObjectRefType ownerAtSourceRef = null;
    ObjectRefType ownerAtDestRef = null;
    
    List objectsToRelocate = null;
    
    /** Creates a new instance of RelocationManagerImpl */
    public RelocationProcessor(ServerRequestContext context) {
        try {
            this.context = context;
            RelocateObjectsRequest req = (RelocateObjectsRequest)context.getCurrentRegistryRequest();
            isAdmin = ac.hasRegistryAdministratorRole(context.getUser());

            resp = bu.rsFac.createRegistryResponse();

            ahq = req.getAdhocQuery();
            srcRegistryRef = req.getSourceRegistry();
            destRegistryRef = req.getDestinationRegistry();
            ownerAtSourceRef = req.getOwnerAtSource();
            ownerAtDestRef = req.getOwnerAtDestination();

            resp.setStatus(BindingUtility.CANONICAL_RESPONSE_STATUS_TYPE_ID_Success);
        }
        catch (RegistryException e) {
            resp = util.createRegistryResponseFromThrowable(e,
                    "LifeCycleManagerImpl.relocateObjects", "Unknown");

            // Append any warnings
            List errs = context.getErrorList().getRegistryError();
            RegistryErrorListType newEl = resp.getRegistryErrorList();
            newEl.getRegistryError().addAll(errs);
        } catch (javax.xml.bind.JAXBException e) {
            e.printStackTrace();
        } catch (javax.xml.registry.JAXRException e) {
            e.printStackTrace();
        }    
    }
        
    public RegistryResponse relocateObjects() {

        try {
            
            if ((destRegistryRef == null) || (srcRegistryRef.getId().equals(destRegistryRef.getId()))) {
                //This is an owner reassignment within same registry
                
                if (isAdmin) {
                    //If local owner reassignment initiated by an admin then just do it
                    //No need to involve ownerAtDestination
                    reAssignOwnerImmediately();
                } else {
                    throw new AuthorizationException(ServerResourceBundle.getInstance().getString("message.ownerReassignmentFailed",
                            new Object[]{context.getUser().getId()}));
                }

            }

            if (context.getErrorList().getRegistryError().size() > 0) {
                // warning exists
                resp.setRegistryErrorList(context.getErrorList());
            }
        } catch (RegistryException e) {
            resp = util.createRegistryResponseFromThrowable(e,
                    "LifeCycleManagerImpl.relocateObjects", "Unknown");

            // Append any warnings
            List errs = context.getErrorList().getRegistryError();
            RegistryErrorListType newEl = resp.getRegistryErrorList();
            newEl.getRegistryError().addAll(errs);       
        } catch (javax.xml.registry.JAXRException e) {
            e.printStackTrace();
        }

        return resp;
    }    
    
    /**
     * Called if an admin requests a local owner reAssignement.
     * This is the simplest case and can be done synchronously
     * in same method invocation without any involvement of
     * ownerAtDestination.
     */
    public void reAssignOwnerImmediately() throws RegistryException {
        getObjectsToRelocate();
        context.setUser((UserType)pm.getRegistryObject(context, ownerAtDestRef));
        pm.changeOwner(context, objectsToRelocate);            
    }
    
    
    private void getObjectsToRelocate() throws RegistryException {
        objectsToRelocate = new ArrayList();
        try {                        
            AdhocQueryRequestType ahqReq = bu.queryFac.createAdhocQueryRequest();
            ahqReq.setId(org.freebxml.omar.common.Utility.getInstance().createId());
            ResponseOption ro = bu.queryFac.createResponseOption();
            ro.setReturnComposedObjects(true);
            ro.setReturnType(ReturnType.LEAF_CLASS);
            ahqReq.setResponseOption(ro);            
            ahqReq.setAdhocQuery(ahq);

            context.pushRegistryRequest(ahqReq);
            AdhocQueryResponseType ahqResp = qm.submitAdhocQuery(context);
            
            RegistryObjectListType rol = ahqResp.getRegistryObjectList();            
            objectsToRelocate.addAll(rol.getIdentifiable());
        }
        catch (javax.xml.bind.JAXBException e) {
            throw new RegistryException(e);
        } finally {
            context.popRegistryRequest();
        }
    }
}
