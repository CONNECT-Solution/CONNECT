/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/ExternalIdentifierDAO.java,v 1.25 2006/08/24 20:42:29 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.ExternalIdentifier;
import org.oasis.ebxml.registry.bindings.rim.ExternalIdentifierType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRef;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;


/**
 * ExternalIdentifier instances provide the additional identifier information
 * to RegistryObject such as DUNS number, Social Security Number, or an alias
 * name of the organization.  The attribute name inherited from Object is
 * used to contain the identification scheme (Social Security Number, etc),
 * and the attribute value contains the actual information. Each RegistryObject
 * may have 0 or more ExternalIdentifier.
 *
 * @see <{RegistryObject}>
 * @author Farrukh S. Najmi
 * @author Adrian Chong
 */
class ExternalIdentifierDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(ExternalIdentifierDAO.class);

    /**
     * Use this constructor only.
     */
    ExternalIdentifierDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "ExternalIdentifier";
    }

    public String getTableName() {
        return getTableNameStatic();
    }

    /**
     * Returns the SQL fragment string needed by insert or update statements 
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object ro) throws RegistryException {

        ExternalIdentifierType  externalIdentifier = ( ExternalIdentifierType)ro;
            
        String stmtFragment = null;
               
        String  registryObject =  externalIdentifier.getRegistryObject();

        String identificationScheme =  externalIdentifier.getIdentificationScheme();

        String value =  externalIdentifier.getValue();
        
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO  ExternalIdentifier " +
                super.getSQLStatementFragment(ro) +
                    ", '" +  registryObject + 
                    "', '" + identificationScheme + 
                    "', '" + value + 
                    "' ) ";            
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE  ExternalIdentifier SET " +
                super.getSQLStatementFragment(ro) +
                    ",  registryObject='" +  registryObject + 
                    "',  identificationScheme='" + identificationScheme + 
                    "', value='" + value + 
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
        return "registryObject";
    }
    
    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof org.oasis.ebxml.registry.bindings.rim.ExternalIdentifier)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.ExternalIdentifierTypeExpected",
                        new Object[]{obj}));
            }

            ExternalIdentifier ei = (ExternalIdentifier) obj;
            super.loadObject(obj, rs);

            String schemeId = rs.getString("identificationScheme");

            if (schemeId != null) {
                ObjectRef or = bu.rimFac.createObjectRef();
                or.setId(schemeId);
                context.getObjectRefs().add(or);
            }
            
            ei.setIdentificationScheme(schemeId);

            String registryObjectId = rs.getString("registryObject");
            if (registryObjectId != null) {
                ObjectRef registryObject = bu.rimFac.createObjectRef();
                context.getObjectRefs().add(registryObject);
                registryObject.setId(registryObjectId);
                ei.setRegistryObject(registryObjectId);
            }
            
            String value = rs.getString("value");
            ei.setValue(value);
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } catch (JAXBException j) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), j);
            throw new RegistryException(j);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        ExternalIdentifier obj = bu.rimFac.createExternalIdentifier();
        
        return obj;
    }
}
