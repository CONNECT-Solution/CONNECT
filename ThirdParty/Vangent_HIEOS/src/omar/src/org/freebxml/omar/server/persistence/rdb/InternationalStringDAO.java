/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/InternationalStringDAO.java,v 1.25 2006/08/24 20:42:29 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.InternationalStringType;
import org.oasis.ebxml.registry.bindings.rim.LocalizedString;


public abstract class InternationalStringDAO extends AbstractDAO {
    private static final Log log = LogFactory.getLog(InternationalStringDAO.class);

    /**
     * Use this constructor only.
     */
    InternationalStringDAO(ServerRequestContext context) {
        super(context);
    }
        
    /*
     * Initialize a binding object from specified ResultSet.
     */
    protected void loadObject(Object obj, ResultSet rs) throws RegistryException {
        throw new RegistryException(ServerResourceBundle.getInstance().getString("message.unimplementedMethod"));
    }
    
    InternationalStringType getInternationalStringByParent(
        String parentId) throws RegistryException {
        InternationalStringType is = null;
        PreparedStatement stmt = null;

        try {
            String tableName = getTableName();

            if (tableName.equalsIgnoreCase("Name_")) {
                is = bu.rimFac.createName();
            } else if (tableName.equalsIgnoreCase("Description")) {
                is = bu.rimFac.createDescription();
            }else if (tableName.equalsIgnoreCase("UsageDescription")) {
                is = bu.rimFac.createUsageDescription();
            }
            String sql = "SELECT * FROM " + getTableName() + " WHERE parent = ?";
            stmt = context.getConnection().prepareStatement(sql);
            stmt.setString(1, parentId);
            log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String charsetName = rs.getString("charset");
                String lang = rs.getString("lang");
                String value = rs.getString("value");
                if (value != null) {
                    LocalizedString ls = bu.rimFac.createLocalizedString();
                    ls.setCharset(charsetName);
                    ls.setLang(lang);
                    ls.setValue(value);
                    is.getLocalizedString().add(ls);
                }
            }
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } catch (JAXBException j) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), j);
            throw new RegistryException(j);
        } finally {
            closeStatement(stmt);
        }

        return is;
    }
    
    public void insert(String parentId,
        InternationalStringType is) throws RegistryException {
        PreparedStatement pstmt = null;

        try {
            String str = "INSERT INTO " + getTableName() + " VALUES(?, " + // charsetName
                "?," + // lang
                "?, " + // value
                "?)"; // parentId
            pstmt = context.getConnection().prepareStatement(str);

            if (is != null) {
                Iterator lsItems = is.getLocalizedString().iterator();

                while (lsItems.hasNext()) {
                    LocalizedString ls = (LocalizedString) lsItems.next();
                    String charset = ls.getCharset();
                    String lang = ls.getLang();
                    String value = ls.getValue();
                    String charsetName = ls.getCharset();
                    
                    if (value != null && value.length() > 0) {
                        pstmt.setString(1, charsetName);
                        pstmt.setString(2, lang);
                        pstmt.setString(3, value);
                        pstmt.setString(4, parentId);
                        log.trace("SQL = " + str);  // HIEOS/BHT: DEBUG (fix)
                        pstmt.addBatch();
                    }
                }
            }

            if (is != null) {
                int[] updateCounts = pstmt.executeBatch();
            }
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } finally {
            closeStatement(pstmt);
        }
    }

    
}
