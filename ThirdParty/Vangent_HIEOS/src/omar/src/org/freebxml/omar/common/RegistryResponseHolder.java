/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/RegistryResponseHolder.java,v 1.3 2005/02/23 22:56:47 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.AdhocQueryResponseType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectListType;
import org.oasis.ebxml.registry.bindings.rs.RegistryErrorListType;
import org.oasis.ebxml.registry.bindings.rs.RegistryErrorType;
import org.oasis.ebxml.registry.bindings.rs.RegistryResponseType;


/**
 * Class Declaration for Class1
 *
 */
public class RegistryResponseHolder {
    private ArrayList collection = new ArrayList();
    private RegistryResponseType ebResponse = null;
    private HashMap responseAttachments = null;

    /**
     * Construct an empty successful BulkResponse
     */
    private RegistryResponseHolder()  {
    }

    public RegistryResponseHolder(RegistryResponseType ebResponse, HashMap responseAttachments) {
        this.ebResponse = ebResponse;
        this.responseAttachments = responseAttachments;
    }

    /**
     * Get the RegistryException(s) Collection in case of partial commit.
     * Caller thread will block here if result is not yet available.
     * Return null if result is available and there is no RegistryException(s).
     *
     * <p><DL><DT><B>Capability Level: 0 </B></DL>
     *
     */
    public List getExceptions() throws RegistryException {
        ArrayList registryExceptions = new ArrayList();
        
        RegistryErrorListType errList = ebResponse.getRegistryErrorList();

        if (errList != null) {
            List errs = errList.getRegistryError();
            Iterator iter = errs.iterator();

            while (iter.hasNext()) {
                Object obj = iter.next();
                RegistryErrorType error = (RegistryErrorType) obj;

                //TODO: Need to add additional error info to exception somehow
                registryExceptions.add(new RegistryException(error.getValue()));
            }
        }
        return registryExceptions;
    }

    public List getCollection() throws RegistryException {
        List roList = null;
        
        if (ebResponse instanceof AdhocQueryResponseType) {
            AdhocQueryResponseType ahqResp = (AdhocQueryResponseType)ebResponse;
            RegistryObjectListType rolt = ahqResp.getRegistryObjectList();
            roList = rolt.getIdentifiable();
        }

        return roList;
    }
    
    public RegistryResponseType getRegistryResponse() {
        return ebResponse;
    }
    
    
    public HashMap getAttachmentsMap() {
        return responseAttachments;
    }
    
}
