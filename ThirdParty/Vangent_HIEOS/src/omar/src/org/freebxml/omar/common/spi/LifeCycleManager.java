/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/spi/LifeCycleManager.java,v 1.2 2006/03/02 15:21:31 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.spi;

import java.util.HashMap;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.RegistryResponseHolder;

import org.oasis.ebxml.registry.bindings.lcm.ApproveObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.DeprecateObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.RelocateObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.RemoveObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.SubmitObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.UndeprecateObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.UpdateObjectsRequest;
import org.oasis.ebxml.registry.bindings.rim.UserType;
import org.oasis.ebxml.registry.bindings.rs.RegistryRequest;
import org.oasis.ebxml.registry.bindings.rs.RegistryResponse;

public interface LifeCycleManager {
    
    /**
     * Submits one or more RegistryObjects and one or more repository items.
     *
     * @param idToRepositoryItemMap is a HashMap with key that is id of a RegistryObject and value that is a repository item in form of a javax.activation.DataHandler instance.
     */
    public RegistryResponse submitObjects(RequestContext context)  throws RegistryException;

    /** Approves one or more previously submitted objects */
    public RegistryResponse approveObjects(RequestContext context) throws RegistryException;

    /** Sets the status of specified objects. This is an extension request that will be adde to ebRR 3.1?? */
    public RegistryResponse setStatusOnObjects(RequestContext context) throws RegistryException;
    
    /** Deprecates one or more previously submitted objects */
    public RegistryResponse deprecateObjects(RequestContext context) throws RegistryException;

    /** Deprecates one or more previously submitted objects */
    public RegistryResponse unDeprecateObjects(RequestContext context) throws RegistryException;
    
    public RegistryResponse updateObjects(RequestContext context)
        throws RegistryException;

    /** Removes one or more previously submitted objects from the registry */
    public RegistryResponse removeObjects(RequestContext context) throws RegistryException;
    
    /** Approves one or more previously submitted objects */
    public RegistryResponse relocateObjects(RequestContext context) throws RegistryException;
    
    /** Sends an impl specific protocol extension request. */
    public RegistryResponseHolder extensionRequest(RequestContext context) throws RegistryException;
    
}
