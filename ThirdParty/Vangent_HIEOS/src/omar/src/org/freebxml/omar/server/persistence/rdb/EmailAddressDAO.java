/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/EmailAddressDAO.java,v 1.25 2006/08/24 20:42:29 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.EmailAddress;
import org.oasis.ebxml.registry.bindings.rim.EmailAddressType;
import org.oasis.ebxml.registry.bindings.rim.UserType;


/**
 * Represents an email address
 *
 * @see <{User}>
 * @author Farrukh S. Najmi
 * @author Adrian Chong
 */
class EmailAddressDAO extends AbstractDAO {
    private static final Log log = LogFactory.getLog(EmailAddressDAO.class);
    
    /**
     * Use this constructor only.
     */
    EmailAddressDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "EmailAddress";
    }

    public String getTableName() {
        return getTableNameStatic();
    }

    public void insert(List users)
        throws RegistryException {
        // log.info(ServerResourceBundle.getInstance().getString("message.InsertingEmailAddresss", new Object[]{new Integer(emailAddresss.size())}));
        if (users.size() == 0) {
            return;
        }

        Statement stmt = null;

        try {
            Iterator usersIter = users.iterator();
            stmt = context.getConnection().createStatement();

            while (usersIter.hasNext()) {
                UserType user = (UserType) usersIter.next();

                if (log.isDebugEnabled()) {
                    try {
                        StringWriter writer = new StringWriter();
                        bu.rimFac.createMarshaller()
                            .marshal(user, writer);
                        log.debug("Inserting user: " + writer.getBuffer().toString());
                    } catch (Exception e) {
                        log.debug("Failed to marshal user: ", e);
                    }
                }
                
                String parentId = user.getId();

                List emails = user.getEmailAddress();
                Iterator emailsIter = emails.iterator();

                while (emailsIter.hasNext()) {
                    //Log.print(Log.TRACE, 8, "\tDATABASE EVENT: storing EmailAddress " );
                    Object obj = emailsIter.next();

                    EmailAddressType emailAddress = (EmailAddressType) obj;

                    String address = emailAddress.getAddress();

                    String type = emailAddress.getType();

                    if (type != null) {
                        type = "'" + type + "'";
                    }

                    String str = "INSERT INTO " + getTableName() + " VALUES( " +
                        "'" + address + "', " + type + ", " + "'" + parentId +
                        "' )";
                    log.trace("SQL = " + str);
                    stmt.addBatch(str);
                }
            }

            if (users.size() > 0) {
                stmt.executeBatch();
            }
        } catch (SQLException e) {
            RegistryException exception = new RegistryException(e);
            throw exception;
        } finally {
            closeStatement(stmt);
        }
    }

    /**
     * Does a bulk insert of a Collection of objects that match the type for this persister.
     *
     */
    public void insert(String parentId,
        List emailAddresss) throws RegistryException {
        log.debug(ServerResourceBundle.getInstance().getString("message.InsertingEmailAddresss", new Object[]{new Integer(emailAddresss.size())}));

        if (emailAddresss.size() == 0) {
            return;
        }

        Statement stmt = null;

        try {
            stmt = context.getConnection().createStatement();

            Iterator iter = emailAddresss.iterator();

            while (iter.hasNext()) {
                EmailAddressType emailAddress = (EmailAddressType) iter.next();

                //Log.print(Log.TRACE, 8, "\tDATABASE EVENT: storing EmailAddress " );
                String address = emailAddress.getAddress();

                String type = emailAddress.getType();

                if (type != null) {
                    type = "'" + type + "'";
                }

                String str = "INSERT INTO EmailAddress " + "VALUES( " + "'" +
                    address + "', " + type + ", " + "'" + parentId + "' )";
                log.trace("SQL = " + str);
                stmt.addBatch(str);
            }

            if (emailAddresss.size() > 0) {
                stmt.executeBatch();
            }
        } catch (SQLException e) {
            RegistryException exception = new RegistryException(e);
            throw exception;
        } finally {
            closeStatement(stmt);
        }
    }

    /**
     * Does a bulk update of a Collection of objects that match the type for this persister.
     *
     */
    public void update(String parentId,
        List emailAddresss) throws RegistryException {
        log.debug(ServerResourceBundle.getInstance().getString("message.UpdatingEmailAddresss", new Object[]{new Integer(emailAddresss.size())}));

        Statement stmt = null;

        try {
            stmt = context.getConnection().createStatement();

            Iterator iter = emailAddresss.iterator();

            while (iter.hasNext()) {
                EmailAddress emailAddress = (EmailAddress) iter.next();

                String address = emailAddress.getAddress();

                String type = emailAddress.getType();

                if (type != null) {
                    type = "'" + type + "'";
                }

                String str = "UPDATE EmailAddress SET " + 
                    //"accesControlPolicy = null, " +
                    "SET address = '" + address + "', " + "SET type = " + type +
                    " WHERE parent = '" + parentId + "' ";
                log.trace("SQL = " + str);
                stmt.addBatch(str);
            }

            stmt.executeBatch();
        } catch (SQLException e) {
            RegistryException exception = new RegistryException(e);
            throw exception;
        } finally {
            closeStatement(stmt);
        }
    }

    protected void loadObject(Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof EmailAddressType)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.EmailAddressTypeExpected",
                        new Object[]{obj}));
            }

            EmailAddressType addr = (EmailAddressType) obj;

            String address = rs.getString("address");
            addr.setAddress(address);

            String type = rs.getString("type");
            addr.setType(type);
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        EmailAddress obj = bu.rimFac.createEmailAddress();
        
        return obj;
    }
}
