/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/RegistryPackageDAO.java,v 1.26 2006/08/24 20:42:29 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.sql.ResultSet;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.RegistryPackage;
import org.oasis.ebxml.registry.bindings.rim.RegistryPackageType;


/**
 * Package instances are RegistryEntries that group logically related
 * RegistryEntries together. One use of a Package is to allow operations to
 * be performed on an entire package of objects. For example all objects belonging
 * to a Package may be deleted in a single request.
 *
 * <p><DL><DT><B>Capability Level: 1 </B><DD>This interface is required to be implemented by JAXR Providers at or above capability level 1.</DL>
 *
 *
 */
class RegistryPackageDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(RegistryPackageDAO.class);

    /**
     * Use this constructor only.
     */
    public RegistryPackageDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "RegistryPackage";
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

        RegistryPackageType pkg = (RegistryPackageType)ro;
            
        String stmtFragment = null;
                        
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO RegistryPackage " +
                super.getSQLStatementFragment(ro) +
                    " ) ";
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE RegistryPackage SET " +
                super.getSQLStatementFragment(ro) +
                    " WHERE id = '" + pkg.getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }    
    

    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof RegistryPackage)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.RegistryPackageExpected",
                        new Object[]{obj}));
            }

            RegistryPackage pkg = (RegistryPackage) obj;
            super.loadObject( obj, rs);

        } catch (Exception e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException"), e);
            throw new RegistryException(e);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        RegistryPackage obj = bu.rimFac.createRegistryPackage();
        
        return obj;
    }
}
