/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/TelephoneNumberDAO.java,v 1.24 2006/08/24 20:42:29 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

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
import org.oasis.ebxml.registry.bindings.rim.OrganizationType;
import org.oasis.ebxml.registry.bindings.rim.TelephoneNumberType;
import org.oasis.ebxml.registry.bindings.rim.UserType;


/**
 *
 * @author  kwalsh
 * @version
 */
class TelephoneNumberDAO extends AbstractDAO {
    private static final Log log = LogFactory.getLog(TelephoneNumberDAO.class);

    /**
     * Use this constructor only.
     */
    TelephoneNumberDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "TelephoneNumber";
    }

    public String getTableName() {
        return getTableNameStatic();
    }

    /**
    *         @param registryObjects is a List of Organizations or Users
    *         @throws RegistryException if the RegistryObject is not Organization or User,
    *         or it has SQLException when inserting their PostalAddress
    */
    public void insert(List registryObjects)
        throws RegistryException {
        Statement stmt = null;

        if (registryObjects.size() == 0) {
            return;
        }

        try {
            stmt = context.getConnection().createStatement();

            Iterator rosIter = registryObjects.iterator();

            while (rosIter.hasNext()) {
                Object ro = rosIter.next();
                String parentId = null;
                Iterator telephoneNumbers;

                if (ro instanceof OrganizationType) {
                    OrganizationType org = (OrganizationType) ro;
                    telephoneNumbers = org.getTelephoneNumber().iterator();
                    parentId = org.getId();
                } else if (ro instanceof UserType) {
                    UserType user = (UserType) ro;
                    telephoneNumbers = user.getTelephoneNumber().iterator();
                    parentId = user.getId();
                } else {
                    throw new RegistryException(ServerResourceBundle.getInstance().getString("message.incorrectRegistryObject"));
                }

                while (telephoneNumbers.hasNext()) {
                    TelephoneNumberType telephoneNumber = (TelephoneNumberType) telephoneNumbers.next();

                    String areaCode = telephoneNumber.getAreaCode();

                    if (areaCode != null) {
                        areaCode = "'" + areaCode + "'";
                    }

                    String countryCode = telephoneNumber.getCountryCode();

                    if (countryCode != null) {
                        countryCode = "'" + countryCode + "'";
                    }

                    String extension = telephoneNumber.getExtension();

                    if (extension != null) {
                        extension = "'" + extension + "'";
                    }

                    String number = telephoneNumber.getNumber();

                    if (number != null) {
                        number = "'" + number + "'";
                    }

                    String phoneType = telephoneNumber.getPhoneType();

                    if (phoneType != null) {
                        phoneType = "'" + phoneType + "'";
                    }

                    String str = "INSERT INTO TelephoneNumber " + "VALUES( " +
                        areaCode + ", " + countryCode + ", " + extension +
                        ", " + number + ", " + phoneType + ", " +
                        "'" + parentId + "' )";

                    log.trace("SQL = " + str);
                    stmt.addBatch(str);
                }
            }

            stmt.executeBatch();
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }
    }

    /**
         * Does a bulk insert of a Collection of objects that match the type for this persister.
         *
         */
    public void insert(String parentId,
        List telephoneNumbers) throws RegistryException {
        Statement stmt = null;

        if (telephoneNumbers.size() == 0) {
            return;
        }

        log.debug(ServerResourceBundle.getInstance().getString("message.InsertingTelephoneNumbersSize", 
		                                                       new Object[]{new Integer(telephoneNumbers.size())}));

        try {
            stmt = context.getConnection().createStatement();

            Iterator iter = telephoneNumbers.iterator();

            while (iter.hasNext()) {
                TelephoneNumberType telephoneNumber = (TelephoneNumberType) iter.next();

                //Log.print(Log.TRACE, 8, "\tDATABASE EVENT: storing TelephoneNumber " );
                String areaCode = telephoneNumber.getAreaCode();

                if (areaCode != null) {
                    areaCode = "'" + areaCode + "'";
                }

                String countryCode = telephoneNumber.getCountryCode();

                if (countryCode != null) {
                    countryCode = "'" + countryCode + "'";
                }

                String extension = telephoneNumber.getExtension();

                if (extension != null) {
                    extension = "'" + extension + "'";
                }

                String number = telephoneNumber.getNumber();

                if (number != null) {
                    number = "'" + number + "'";
                }

                String phoneType = telephoneNumber.getPhoneType();

                if (phoneType != null) {
                    phoneType = "'" + phoneType + "'";
                }

                String str = "INSERT INTO TelephoneNumber " + "VALUES( " +
                    areaCode + ", " + countryCode + ", " + extension + ", " +
                    number + ", " + phoneType + ", " + "'" +
                    parentId + "' )";

                log.trace("SQL = " + str);
                stmt.addBatch(str);
            }

            if (telephoneNumbers.size() > 0) {
                stmt.executeBatch();
            }
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }
    }

    protected void loadObject( Object obj,
        ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof TelephoneNumberType)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.TelephoneNumberTypeExpected",
                        new Object[]{obj}));
            }

            TelephoneNumberType phone = (TelephoneNumberType) obj;

            String areaCode = rs.getString("areaCode");
            phone.setAreaCode(areaCode);

            String countryCode = rs.getString("countryCode");
            phone.setCountryCode(countryCode);

            String extension = rs.getString("extension");
            phone.setExtension(extension);

            String number = rs.getString("number_");
            phone.setNumber(number);

            String phoneType = rs.getString("phoneType");
            phone.setPhoneType(phoneType);
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        TelephoneNumberType obj = bu.rimFac.createTelephoneNumberType();
        
        return obj;
    }
}
