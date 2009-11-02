/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/OrganizationDAO.java,v 1.28 2006/08/24 20:42:29 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.ObjectRef;
import org.oasis.ebxml.registry.bindings.rim.Organization;
import org.oasis.ebxml.registry.bindings.rim.OrganizationType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;


/**
 *
 * @author Farrukh S. Najmi
 * @author Adrian Chong
 * @version
 */
class OrganizationDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(OrganizationDAO.class);

    /**
     * Use this constructor only.
     */
    OrganizationDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "Organization";
    }

    public String getTableName() {
        return getTableNameStatic();
    }

    protected void deleteComposedObjects(Object object)  throws RegistryException {
        RegistryObjectType ro = (RegistryObjectType)object;
            
        super.deleteComposedObjects(ro);
        
        String parentId = ro.getId();
        PostalAddressDAO postalAddressDAO = new PostalAddressDAO(context);
        postalAddressDAO.setParent(object);
        postalAddressDAO.deleteByParent();
        
        EmailAddressDAO emailAddressDAO = new EmailAddressDAO(context);
        emailAddressDAO.setParent(object);
        emailAddressDAO.deleteByParent();
        
        TelephoneNumberDAO telephoneNumberDAO = new TelephoneNumberDAO(context);
        telephoneNumberDAO.setParent(object);
        telephoneNumberDAO.deleteByParent();
    }
    
    protected void insertComposedObjects(Object object)  throws RegistryException {
        OrganizationType ro = (OrganizationType)object;
            
        super.insertComposedObjects(ro);
        
        String parentId = ro.getId();
        PostalAddressDAO postalAddressDAO = new PostalAddressDAO(context);
        postalAddressDAO.insert(parentId, ro.getAddress());
        
        EmailAddressDAO emailAddressDAO = new EmailAddressDAO(context);
        emailAddressDAO.insert(parentId, ro.getEmailAddress());
        
        TelephoneNumberDAO telephoneNumberDAO = new TelephoneNumberDAO(context);
        telephoneNumberDAO.insert(parentId, ro.getTelephoneNumber());
    }
    
    /**
     * Returns the SQL fragment string needed by insert or update statements 
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object ro) throws RegistryException {

        OrganizationType org = (OrganizationType)ro;
            
        String stmtFragment = null;
        String parentId = org.getParent();

        if (parentId != null) {
            parentId = "'" + parentId + "'";
        }

        String primaryContactId = org.getPrimaryContact();
        
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO Organization " +
                super.getSQLStatementFragment(ro) +
                    ", " + parentId + 
                    ", '" + primaryContactId + 
                    "' ) ";
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE Organization SET " +
                super.getSQLStatementFragment(ro) +
                    ", parent=" + parentId + 
                    ", primaryContact='" + primaryContactId + 
                    "' WHERE id = '" + ((RegistryObjectType)ro).getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }
    
    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof Organization)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.OrganizationExpected",
                        new Object[]{obj}));
            }

            Organization org = (Organization) obj;
            super.loadObject( obj, rs);

            String parentId = rs.getString("parent");

            if (parentId != null) {
                ObjectRef or = bu.rimFac.createObjectRef();
                or.setId(parentId);
                context.getObjectRefs().add(or);
                org.setParent(parentId);
            }

            String primaryContactId = rs.getString("primaryContact");

            ObjectRef or = bu.rimFac.createObjectRef();
            or.setId(primaryContactId);
            context.getObjectRefs().add(or);
            org.setPrimaryContact(primaryContactId);

            PostalAddressDAO postalAddressDAO = new PostalAddressDAO(context);
            postalAddressDAO.setParent(org);
            List addresses = postalAddressDAO.getByParent();
            if ((addresses != null) && (addresses.size() > 0)) {
                org.getAddress().addAll(addresses);
            }

            TelephoneNumberDAO telephoneNumberDAO = new TelephoneNumberDAO(context);
            telephoneNumberDAO.setParent(org);
            List phones = telephoneNumberDAO.getByParent();
            if (phones != null) {
                org.getTelephoneNumber().addAll(phones);
            }
            
            EmailAddressDAO emailAddressDAO = new EmailAddressDAO(context);
            emailAddressDAO.setParent(org);
            List emails = emailAddressDAO.getByParent();
            if (emails != null) {
                org.getEmailAddress().addAll(emails);
            }
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
        Organization obj = bu.rimFac.createOrganization();
        
        return obj;
    }
}
