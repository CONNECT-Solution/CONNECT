/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/ServiceBindingDAO.java,v 1.29 2006/08/24 20:42:29 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.exceptions.UnresolvedURLsException;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.common.Utility;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.IdentifiableType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRef;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.ServiceBinding;
import org.oasis.ebxml.registry.bindings.rim.ServiceBindingType;
import org.oasis.ebxml.registry.bindings.rim.SpecificationLink;


/**
 * Maps to a BindingTemplate in UDDI.
 *
 * @see <{Concept}>
 * @author Farrukh S. Najmi
 * @author Kathy Walsh
 * @author Adrian Chong
 */
class ServiceBindingDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(ServiceBindingDAO.class);

    /**
     * Use this constructor only.
     */
    ServiceBindingDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "ServiceBinding";
    }

    public String getTableName() {
        return getTableNameStatic();
    }

    protected void prepareToInsert(Object object) throws RegistryException {        
        ServiceBindingType ro = (ServiceBindingType)object;
        super.prepareToInsert(object);
        validateURI(ro);
    }
                
    protected void prepareToUpdate(Object object) throws RegistryException {        
        ServiceBinding ro = (ServiceBinding)object;
        super.prepareToUpdate(object);
        validateURI(ro);
    }
    
    protected void validateURI(ServiceBindingType object) throws RegistryException {        
        
        // check those ExternalLink with http url
        String check = RegistryProperties.getInstance().getProperty("omar.persistence.rdb.ServiceBindingDAO.checkURLs");

        if (check.equalsIgnoreCase("true")) {
            ArrayList objs = new ArrayList();
            objs.add(object);
            List invalidExtLinks = Utility.getInstance().validateURIs(objs);

            if (invalidExtLinks.size() > 0) {
                throw new UnresolvedURLsException(invalidExtLinks);
            }
        }

    }
                
    protected void deleteComposedObjects(Object object)  throws RegistryException {
        RegistryObjectType ro = (RegistryObjectType)object;
            
        super.deleteComposedObjects(ro);
        
        SpecificationLinkDAO specLinkDAO = new SpecificationLinkDAO(context);
        specLinkDAO.setParent(object);
        specLinkDAO.deleteByParent();
    }
    
    protected void insertComposedObjects(Object object)  throws RegistryException {
        RegistryObjectType ro = (RegistryObjectType)object;
            
        super.insertComposedObjects(ro);
        
        ServiceBindingType serviceBinding = (ServiceBindingType)ro;
        SpecificationLinkDAO specLinkDAO = new SpecificationLinkDAO(context);
        specLinkDAO.setParent(object);
        specLinkDAO.insert(serviceBinding.getSpecificationLink());        
    }
    
    /**
     * Returns the SQL fragment string needed by insert or update statements 
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object ro) throws RegistryException {

        ServiceBindingType serviceBinding = (ServiceBindingType)ro;
            
        String stmtFragment = null;
                
        String serviceId = serviceBinding.getService();
        if (serviceId == null) {            
            serviceId = ((IdentifiableType)parent).getId();
        }
        
        if (serviceId == null) {            
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.serviceBindingHasNoParentService",
                    new Object[]{serviceBinding.getId()}));
        }
        
        String accessURI = serviceBinding.getAccessURI();
        if (accessURI != null) {
            accessURI = "'" + accessURI + "'";
        }

        
        String targetBindingId = serviceBinding.getTargetBinding();

        if (targetBindingId != null) {
            targetBindingId = "'" + targetBindingId + "'";
        }


        
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO ServiceBinding " +
                super.getSQLStatementFragment(ro) +
                    ", '" + serviceId +  
                    "', " + accessURI + 
                    ", " + targetBindingId +
                    " ) ";
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE ServiceBinding SET " +
                super.getSQLStatementFragment(ro) +
                    ", service='" + serviceId + 
                    "', accessURI=" + accessURI + 
                    ", targetBinding=" + targetBindingId + 
                    " WHERE id = '" + ((RegistryObjectType)ro).getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }    

    protected String checkServiceBindingReferences( 
        String serviceBindingId) throws RegistryException {
        String referencingServiceBindingId = null;
        PreparedStatement stmt = null;

        try {
            String sql = "SELECT id FROM ServiceBinding WHERE targetBinding=? AND targetBinding IS NOT NULL";
            stmt = context.getConnection().prepareStatement(sql);
            stmt.setString(1, serviceBindingId);
            log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                referencingServiceBindingId = rs.getString(1);
            }

            return referencingServiceBindingId;
        } catch (SQLException e) {
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }
    }

    /*
     * Gets the column name that is foreign key ref into parent table.
     * Must be overridden by derived class if it is not 'parent'
     */
    protected String getParentAttribute() {
        return "service";
    }
    

    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        if (!(obj instanceof ServiceBinding)) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.ServiceBindingExpected",
                    new Object[]{obj}));
        }

        ServiceBinding serviceBinding = (ServiceBinding) obj;

        super.loadObject( serviceBinding, rs);

        String accessUri = null;

        try {
            accessUri = rs.getString("accessuri");
            serviceBinding.setAccessURI(accessUri);

            String targetBindingId = rs.getString("targetBinding");

            if (targetBindingId != null) {
                ObjectRef targetBinding = bu.rimFac.createObjectRef();
                context.getObjectRefs().add(targetBinding);
                targetBinding.setId(targetBindingId);
                serviceBinding.setTargetBinding(targetBindingId);
            }

            String serviceId = rs.getString("service");

            if (serviceId != null) {
                ObjectRef service = bu.rimFac.createObjectRef();
                context.getObjectRefs().add(service);
                service.setId(serviceId);
                serviceBinding.setService(serviceId);
            }
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } catch (JAXBException j) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), j);
            throw new RegistryException(j);
        }

        boolean returnComposedObjects = context.getResponseOption().isReturnComposedObjects();

        if (returnComposedObjects) {
            SpecificationLinkDAO specificationLinkDAO = new SpecificationLinkDAO(context);
            specificationLinkDAO.setParent(serviceBinding);
            List specLinks = specificationLinkDAO.getByParent();
            Iterator iter = specLinks.iterator();

            while (iter.hasNext()) {
                SpecificationLink sl = (SpecificationLink) iter.next();
                serviceBinding.getSpecificationLink().add(sl);
            }
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        ServiceBinding obj = bu.rimFac.createServiceBinding();
        
        return obj;
    }
    
}
