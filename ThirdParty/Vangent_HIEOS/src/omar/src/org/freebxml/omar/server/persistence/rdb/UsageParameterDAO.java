/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/UsageParameterDAO.java,v 1.21 2006/08/24 20:42:29 farrukh_najmi Exp $
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
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;


/**
 * DAO for UsageParameter
 *
 * @see <{User}>
 * @author Farrukh S. Najmi
 */
class UsageParameterDAO extends AbstractDAO {
    private static final Log log = LogFactory.getLog(UsageParameterDAO.class);

    /**
     * Use this constructor only.
     */
    UsageParameterDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "UsageParameter";
    }

    public String getTableName() {
        return getTableNameStatic();
    }

    /**
     * Does a bulk insert of a Collection of objects that match the type for this persister.
     *
     */
    public void insert(String parentId, List usageParams)
        throws RegistryException {
        Statement stmt = null;

        if (usageParams.size() == 0) {
            return;
        }

        try {
            stmt = context.getConnection().createStatement();

            Iterator iter = usageParams.iterator();

            while (iter.hasNext()) {
                String value = (String) iter.next();

                String str = "INSERT INTO UsageParameter " + "VALUES( " + "'" +
                    value + "', " + "'" + parentId + "' )";
                log.trace("SQL = " + str);
                stmt.addBatch(str);
            }

            if (usageParams.size() > 0) {
                int[] updateCounts = stmt.executeBatch();
            }
        } catch (SQLException e) {
            RegistryException exception = new RegistryException(e);
            throw exception;
        } finally {
            closeStatement(stmt);
        }
    }
    //TODO:Need to review the usability of this method.
    protected void loadObject( Object obj,
        ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof java.lang.String)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.javaLangStringExpected",
                        new Object[]{obj}));
            }

            String value = (String) obj;
            value.concat(rs.getString("value"));
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }
    }

    List getUsageParametersByParent(String parentId)
        throws RegistryException {
        List usageParams = new ArrayList();
        PreparedStatement stmt = null;

        try {
            String sql = "SELECT * FROM UsageParameter WHERE parent = ?";
            stmt = context.getConnection().prepareStatement(sql);
            stmt.setString(1, parentId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String usageParam = new String();
                loadObject( usageParam, rs);
                usageParams.add(rs.getString("value"));
            }
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }

        return usageParams;
    }
    
    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        String obj = new String();
        
        return obj;
    }
    
}
