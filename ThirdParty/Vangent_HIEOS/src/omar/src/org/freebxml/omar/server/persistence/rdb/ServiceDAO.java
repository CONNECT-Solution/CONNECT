/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/ServiceDAO.java,v 1.23 2005/11/21 04:27:52 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.Service;
import org.oasis.ebxml.registry.bindings.rim.ServiceBindingType;
import org.oasis.ebxml.registry.bindings.rim.ServiceType;


/**
 *
 * @author  kwalsh
 * @author Adrian Chong
 * @version
 */
class ServiceDAO extends RegistryObjectDAO {
    
    /**
     * Use this constructor only.
     */
    ServiceDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "Service";
    }

    public String getTableName() {
        return getTableNameStatic();
    }

    protected void deleteComposedObjects(Object object)  throws RegistryException {
        RegistryObjectType ro = (RegistryObjectType)object;
            
        super.deleteComposedObjects(ro);
        
        ServiceBindingDAO serviceBindingDAO = new ServiceBindingDAO(context);
        serviceBindingDAO.setParent(ro);
        serviceBindingDAO.deleteByParent();
    }
    
    protected void insertComposedObjects(Object object)  throws RegistryException {
        RegistryObjectType ro = (RegistryObjectType)object;
            
        super.insertComposedObjects(ro);
        
        //TODO: Need to pass ServiceBinding and Service
        ServiceType service = (ServiceType)ro;
        ServiceBindingDAO serviceBindingDAO = new ServiceBindingDAO(context);
        serviceBindingDAO.setParent(ro);
        serviceBindingDAO.insert(service.getServiceBinding());        
    }
    
    /**
     * Returns the SQL fragment string needed by insert or update statements 
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object ro) throws RegistryException {

        ServiceType serviceBinding = (ServiceType)ro;
            
        String stmtFragment = null;
                        
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO Service " +
                super.getSQLStatementFragment(ro) +
                    " ) ";
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE Service SET " +
                super.getSQLStatementFragment(ro) +
                    " WHERE id = '" + ((RegistryObjectType)ro).getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }    
    
    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        if (!(obj instanceof Service)) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.ServiceExpected",
                    new Object[]{obj}));
        }

        Service service = (Service) obj;

        super.loadObject( service, rs);

        boolean returnComposedObjects = context.getResponseOption().isReturnComposedObjects();

        if (returnComposedObjects) {
            ServiceBindingDAO serviceBindingDAO = new ServiceBindingDAO(context);
            serviceBindingDAO.setParent(service);
            List serviceBindings = serviceBindingDAO.getByParent();
            Iterator iter = serviceBindings.iterator();

            while (iter.hasNext()) {
                ServiceBindingType sb = (ServiceBindingType) iter.next();
                service.getServiceBinding().add(sb);
            }
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        Service obj = bu.rimFac.createService();
        
        return obj;
    }
}
