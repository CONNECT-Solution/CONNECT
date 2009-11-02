/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/PostalAddressDAO.java,v 1.23 2006/08/24 20:42:29 farrukh_najmi Exp $
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
import org.oasis.ebxml.registry.bindings.rim.PostalAddressType;
import org.oasis.ebxml.registry.bindings.rim.UserType;


/**
 *
 * @author Farrukh S. Najmi
 * @version
 */
class PostalAddressDAO extends AbstractDAO {
    private static final Log log = LogFactory.getLog(PostalAddressDAO.class);

    /**
     * Use this constructor only.
     */
    PostalAddressDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "PostalAddress";
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
            /*
            String sqlStr = "INSERT INTO " + getTableName() +
                              " VALUES( " +
                                                           "?, " + // city
                                                           "?, " + // country
                                                           "?, " + // postalCode
                                                           "?, " + // state
                                                           "?, " + // street
                                                           "?, " + // streetNum
                                                           "?)"; // Parent id

            PreparedStatement pstmt = context.getConnection().prepareStatement(sqlStr);
            */
            stmt = context.getConnection().createStatement();

            Iterator rosIter = registryObjects.iterator();

            while (rosIter.hasNext()) {
                Object ro = rosIter.next();
                String parentId = null;
                PostalAddressType postalAddress = null;

                if (ro instanceof OrganizationType) {
                    OrganizationType org = (OrganizationType) ro;
                    postalAddress = (PostalAddressType)org.getAddress().get(0);
                    parentId = org.getId();
                } else if (ro instanceof UserType) {
                    UserType user = (UserType) ro;

                    //TODO: Save extra addresses, if required
                    postalAddress = (PostalAddressType) user.getAddress().get(0);
                    parentId = user.getId();
                } else {
                    throw new RegistryException(ServerResourceBundle.getInstance().getString("message.incorrectRegistryObject"));
                }

                /*
                stmt.setString(1, postalAddress.getCity());
                                stmt.setString(2, postalAddress.getCountry());
                stmt.setString(3, postalAddress.getPostalCode());
                stmt.setString(4, postalAddress.getStateOrProvince());
                stmt.setString(5, postalAddress.getStreet());
                stmt.setString(6, postalAddress.getStreetNumber());
                stmt.setString(7, org.getId());
                stmt.addBatch();*/
                String city = postalAddress.getCity();

                if (city != null) {
                    city = "'" + city + "'";
                }

                String country = postalAddress.getCountry();

                if (country != null) {
                    country = "'" + country + "'";
                }

                String postalCode = postalAddress.getPostalCode();

                if (postalCode != null) {
                    postalCode = "'" + postalCode + "'";
                }

                String state = postalAddress.getStateOrProvince();

                if (state != null) {
                    state = "'" + state + "'";
                }

                String street = postalAddress.getStreet();

                if (street != null) {
                    street = "'" + street + "'";
                }

                String streetNum = postalAddress.getStreetNumber();

                if (streetNum != null) {
                    streetNum = "'" + streetNum + "'";
                }

                String str = "INSERT INTO PostalAddress " + "VALUES( " + city +
                    ", " + country + ", " + postalCode + ", " + state + ", " +
                    street + ", " + streetNum + ", " + "'" + parentId + "' )";
                log.trace("SQL = " + str);
                stmt.addBatch(str);
            }

            // end looping all Organizations 
            if (registryObjects.size() > 0) {
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
        List postalAddresss) throws RegistryException {
        Statement stmt = null;
        log.debug(ServerResourceBundle.getInstance().getString("message.InsertingPostalAddresss", new Object[]{new Integer(postalAddresss.size())}));

        if (postalAddresss.size() == 0) {
            return;
        }

        try {
            stmt = context.getConnection().createStatement();

            Iterator iter = postalAddresss.iterator();

            while (iter.hasNext()) {
                PostalAddressType postalAddress = (PostalAddressType) iter.next();

                //Log.print(Log.TRACE, 8, "\tDATABASE EVENT: storing PostalAddress " );
                String city = postalAddress.getCity();

                if (city != null) {
                    city = "'" + city + "'";
                }

                String country = postalAddress.getCountry();

                if (country != null) {
                    country = "'" + country + "'";
                }

                String postalCode = postalAddress.getPostalCode();

                if (postalCode != null) {
                    postalCode = "'" + postalCode + "'";
                }

                String state = postalAddress.getStateOrProvince();

                if (state != null) {
                    state = "'" + state + "'";
                }

                String street = postalAddress.getStreet();

                if (street != null) {
                    street = "'" + street + "'";
                }

                String streetNum = postalAddress.getStreetNumber();

                if (streetNum != null) {
                    streetNum = "'" + streetNum + "'";
                }

                String str = "INSERT INTO PostalAddress " + "VALUES( " + city +
                    ", " + country + ", " + postalCode + ", " + state + ", " +
                    street + ", " + streetNum + ", " + "'" + parentId + "' )";
                log.trace("SQL = " + str);
                stmt.addBatch(str);
            }

            if (postalAddresss.size() > 0) {
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
        List postalAddresss) throws RegistryException {
        Statement stmt = null;
        log.debug(ServerResourceBundle.getInstance().getString("message.UpdatingPostalAddresss", new Object[]{new Integer(postalAddresss.size())}));

        try {
            stmt = context.getConnection().createStatement();

            Iterator iter = postalAddresss.iterator();

            while (iter.hasNext()) {
                PostalAddressType postalAddress = (PostalAddressType) iter.next();

                String city = postalAddress.getCity();

                if (city != null) {
                    city = "'" + city + "'";
                }

                String country = postalAddress.getCountry();

                if (country != null) {
                    country = "'" + country + "'";
                }

                String postalCode = postalAddress.getPostalCode();

                if (postalCode != null) {
                    postalCode = "'" + postalCode + "'";
                }

                String state = postalAddress.getStateOrProvince();

                if (state != null) {
                    state = "'" + state + "'";
                }

                String street = postalAddress.getStreet();

                if (street != null) {
                    street = "'" + street + "'";
                }

                String streetNum = postalAddress.getStreetNumber();

                if (streetNum != null) {
                    streetNum = "'" + streetNum + "'";
                }

                String str = "UPDATE PostalAddress " + "SET city = " + city +
                    ", " + "SET country = " + country + ", " +
                    "SET postalCode = " + postalCode + ", " + "SET state = " +
                    state + ", " + "SET street = " + street + ", " +
                    "SET streetNum = " + streetNum + " " + " WHERE parent = '" +
                    parentId + "' ";
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

    protected void loadObject( Object obj,
        ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof PostalAddressType)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.PostalAddressTypeExpected",
                        new Object[]{obj}));
            }

            PostalAddressType addr = (PostalAddressType) obj;

            String city = rs.getString("city");
            addr.setCity(city);

            String country = rs.getString("country");
            addr.setCountry(country);

            String postalCode = rs.getString("postalCode");
            addr.setPostalCode(postalCode);

            String stateOrProvince = rs.getString("state");
            addr.setStateOrProvince(stateOrProvince);

            String street = rs.getString("street");
            addr.setStreet(street);

            String streetNumber = rs.getString("streetNumber");
            addr.setStreetNumber(streetNumber);
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        PostalAddressType obj = bu.rimFac.createPostalAddressType();
        
        return obj;
    }
}
