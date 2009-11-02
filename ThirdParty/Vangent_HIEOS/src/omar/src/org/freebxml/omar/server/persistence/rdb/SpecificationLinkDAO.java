/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/SpecificationLinkDAO.java,v 1.26 2006/08/24 20:42:29 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.util.List;
import java.util.Iterator;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.ObjectRef;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.ServiceBindingType;
import org.oasis.ebxml.registry.bindings.rim.SpecificationLink;
import org.oasis.ebxml.registry.bindings.rim.SpecificationLinkType;
import org.oasis.ebxml.registry.bindings.rim.UsageDescription;
import org.oasis.ebxml.registry.bindings.rim.UsageParameter;

/**
 * Represents a link or reference to a technical specification used within a SpecificationLinkBinding.
 * It serves the same purpose as the union of tModelInstanceInfo and instanceDetails in
 * UDDI.
 *
 * <p><DL><DT><B>Capability Level: 0 </B><DD>This interface is required to be implemented by JAXR Providers at or above capability level 0.</DL>
 *
 * @see <{Concept}>
 * @author Farrukh S. Najmi
 */
class SpecificationLinkDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(SpecificationLinkDAO.class);

    /**
     * Use this constructor only.
     */
    SpecificationLinkDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "SpecificationLink";
    }

    public String getTableName() {
        return getTableNameStatic();
    }
   
    protected void deleteComposedObjects(Object object)  throws RegistryException {
        RegistryObjectType ro = (RegistryObjectType)object;
            
        super.deleteComposedObjects(ro);
        
        //Delete description atribute for the specified objects
        UsageDescriptionDAO usageDescriptionDAO = new UsageDescriptionDAO(context);
        usageDescriptionDAO.setParent(ro);
        usageDescriptionDAO.deleteByParent();

        UsageParameterDAO usageParameterDAO = new UsageParameterDAO(context);
        usageParameterDAO.setParent(ro);
        usageParameterDAO.deleteByParent();
    }
    
    protected void insertComposedObjects(Object object)  throws RegistryException {
        RegistryObjectType ro = (RegistryObjectType)object;
            
        super.insertComposedObjects(ro);
        
        SpecificationLinkType specLink = (SpecificationLinkType)ro;
        String id = specLink.getId();
        
        UsageDescriptionDAO usageDescriptionDAO = new UsageDescriptionDAO(context);
        usageDescriptionDAO.insert(id, specLink.getUsageDescription());

        UsageParameterDAO usageParameterDAO = new UsageParameterDAO(context);
        usageParameterDAO.insert(id, specLink.getUsageParameter());
    }
    
    /**
     * Returns the SQL fragment string needed by insert or update statements 
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object ro) throws RegistryException {

        SpecificationLinkType specLink = (SpecificationLinkType)ro;
            
        String stmtFragment = null;
                
        String serviceBinding = specLink.getServiceBinding();
        if (serviceBinding == null) {
            if (parent != null) {
                serviceBinding = ((ServiceBindingType)parent).getId();
            }
            else {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.specificationLinkHasNoParentServiceBinding",
                        new Object[]{specLink.getId()}));
            }
        }
        
        String specificationObject = specLink.getSpecificationObject();
        if (specificationObject == null) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.specificationLinkHasNoPreentSpecificationLink",
                    new Object[]{specLink.getId()}));
        }
         
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO SpecificationLink " +
                super.getSQLStatementFragment(ro) +
                    ", '" + serviceBinding +  
                    "', '" + specificationObject + 
                    "' ) ";
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE SpecificationLink SET " +
                super.getSQLStatementFragment(ro) +
                    ", serviceBinding='" + serviceBinding + 
                    "', specificationObject='" + specificationObject + 
                    "' WHERE id = '" + ((RegistryObjectType)ro).getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }    

    /*
     * Gets the column name that is foreign key ref into parent table.
     * Must be overridden by derived class if it is not 'parent'
     */
    protected String getParentAttribute() {
        return "serviceBinding";
    }
    
    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        if (!(obj instanceof SpecificationLink)) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.SpecificationLinkExpected",
                    new Object[]{obj}));
        }

        SpecificationLink specLink = (SpecificationLink) obj;

        super.loadObject( specLink, rs);

        try {
            String specificationObjectId = rs.getString("specificationObject");
            specLink.setSpecificationObject(specificationObjectId);
            ObjectRef ref = bu.rimFac.createObjectRef();
            ref.setId(specificationObjectId);
            context.getObjectRefs().add(ref);
            
            
            String serviceBindingId = rs.getString("serviceBinding");
            if (serviceBindingId != null) {
                ObjectRef serviceBinding = bu.rimFac.createObjectRef();
                context.getObjectRefs().add(serviceBinding);
                serviceBinding.setId(serviceBindingId);
                specLink.setServiceBinding(serviceBindingId);
            }
            String specLinkId = rs.getString("id");
            if (specLinkId != null) {
               UsageDescriptionDAO usageDescriptionDAO = new UsageDescriptionDAO(context);
               usageDescriptionDAO.setParent(specLinkId);
               UsageDescription usagesDescription = usageDescriptionDAO.getUsageDescriptionByParent(specLinkId);
               if(usagesDescription != null){ 
                   specLink.setUsageDescription(usagesDescription);
               }
               UsageParameterDAO usageParameterDAO = new UsageParameterDAO(context);
               usageParameterDAO.setParent(specLinkId);
               
               List usageParameters = usageParameterDAO.getUsageParametersByParent(specLinkId);
               
               if (usageParameters != null) {
                   Iterator iter = usageParameters.iterator();

                   while (iter.hasNext()) {
                        String usageParam = (String) iter.next();
                        specLink.getUsageParameter().add(usageParam);
                   }
               }
            }
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } catch (JAXBException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        SpecificationLink obj = bu.rimFac.createSpecificationLink();
        
        return obj;
    }
}
