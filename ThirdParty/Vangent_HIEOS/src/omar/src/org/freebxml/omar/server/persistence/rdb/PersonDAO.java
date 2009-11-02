/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/PersonDAO.java,v 1.8 2006/08/24 20:42:29 farrukh_najmi Exp $
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
import org.oasis.ebxml.registry.bindings.rim.Person;
import org.oasis.ebxml.registry.bindings.rim.PersonNameType;
import org.oasis.ebxml.registry.bindings.rim.PersonType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;


/**
 *
 * @author Farrukh S. Najmi
 * @version
 */
class PersonDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(PersonDAO.class);

    /**
     * Use this constructor only.
     */
    PersonDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "Person";
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
        PersonType ro = (PersonType)object;
            
        super.insertComposedObjects(object);
        
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

        PersonType person = (PersonType)ro;
            
        String stmtFragment = null;
                
        PersonNameType personName = person.getPersonName();
        String firstName = personName.getFirstName();

        if (firstName != null) {
            firstName = "'" + firstName + "'";
        }

        String middleName = personName.getMiddleName();

        if (middleName != null) {
            middleName = "'" + middleName + "'";
        }

        String lastName = personName.getLastName();

        if (lastName != null) {
            lastName = "'" + lastName + "'";
        }
        
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO " + getTableName() + " " +
                super.getSQLStatementFragment(ro) +
                    ", " + firstName +  
                    ", " + middleName + 
                    ", " + lastName +
                    " ) ";
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE " + getTableName() + " SET " +
                super.getSQLStatementFragment(ro) +
                    ", personName_firstName=" + firstName + 
                    ", personName_middleName=" + middleName + 
                    ", personName_lastName=" + lastName + 
                    " WHERE id = '" + ((RegistryObjectType)ro).getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }    

    protected void loadObject( Object obj,
        ResultSet rs)
        throws RegistryException {
        try {
            if (!(obj instanceof org.oasis.ebxml.registry.bindings.rim.PersonType)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.PersonExpected",
                        new Object[]{obj}));
            }

            PersonType person = (PersonType) obj;
            super.loadObject( obj, rs);

            String firstName = rs.getString("personName_firstName");
            String middleName = rs.getString("personName_middleName");
            String lastName = rs.getString("personName_lastName");

            PersonNameType pn = bu.rimFac.createPersonName();
            pn.setFirstName(firstName);
            pn.setMiddleName(middleName);
            pn.setLastName(lastName);

            person.setPersonName(pn);

            PostalAddressDAO postalAddressDAO = new PostalAddressDAO(context);
            postalAddressDAO.setParent(person);
            List addresses = postalAddressDAO.getByParent();
            if (addresses != null) {
                person.getAddress().addAll(addresses);
            }

            TelephoneNumberDAO telephoneNumberDAO = new TelephoneNumberDAO(context);
            telephoneNumberDAO.setParent(person);
            List phones = telephoneNumberDAO.getByParent();
            if (phones != null) {
                person.getTelephoneNumber().addAll(phones);
            }

            EmailAddressDAO emailAddressDAO = new EmailAddressDAO(context);
            emailAddressDAO.setParent(person);
            List emails = emailAddressDAO.getByParent();
            if (emails != null) {
                person.getEmailAddress().addAll(emails);
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
        Person obj = bu.rimFac.createPerson();
        
        return obj;
    }
}
