/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/ExternalLinkDAO.java,v 1.27 2006/08/24 20:42:29 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.exceptions.UnresolvedURLsException;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.common.Utility;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.ExternalLink;
import org.oasis.ebxml.registry.bindings.rim.ExternalLinkType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;


/**
 *
 * @see <{RegistryObject}>
 * @author Farrukh S. Najmi
 * @author Adrian Chong
 */
class ExternalLinkDAO extends RegistryObjectDAO {
    private static final Log log = LogFactory.getLog(ExternalLinkDAO.class);

    /**
     * Use this constructor only.
     */
    ExternalLinkDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "ExternalLink";
    }

    public String getTableName() {
        return getTableNameStatic();
    }
    
    protected void prepareToInsert(Object object) throws RegistryException {        
        ExternalLinkType ro = (ExternalLinkType)object;
        
        super.prepareToInsert(object);
        validateURI(ro);
    }
                
    protected void prepareToUpdate(Object object) throws RegistryException {        
        ExternalLinkType ro = (ExternalLinkType)object;
        
        super.prepareToUpdate(object);
        validateURI(ro);
    }
    
    protected void validateURI(ExternalLinkType object) throws RegistryException {        
        
        // check those ExternalLink with http url
        String check = RegistryProperties.getInstance().getProperty("omar.persistence.rdb.ExternalLinkDAO.checkURLs");

        if (check.equalsIgnoreCase("true")) {
            ArrayList objs = new ArrayList();
            objs.add(object);
            List invalidExtLinks = Utility.getInstance().validateURIs(objs);

            if (invalidExtLinks.size() > 0) {
                throw new UnresolvedURLsException(invalidExtLinks);
            }
        }

    }
                
    /**
     * Returns the SQL fragment string needed by insert or update statements 
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object ro) throws RegistryException {

        ExternalLinkType extLink = (ExternalLinkType)ro;
            
        String stmtFragment = null;
        String extURI = extLink.getExternalURI();        
        
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO ExternalLink " +
                super.getSQLStatementFragment(ro) +
                    ", '" + extURI + 
                    "' ) ";
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE ExternalLink SET " +
                super.getSQLStatementFragment(ro) +
                    ", externalURI='" + extURI + 
                    "' WHERE id = '" + ((RegistryObjectType)ro).getId() + "' ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        
        return stmtFragment;
    }
    
    


    protected void loadObject( Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof org.oasis.ebxml.registry.bindings.rim.ExternalLink)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.ExternalLinkTypeExpected",
                        new Object[]{obj}));
            }

            ExternalLink el = (ExternalLink) obj;
            super.loadObject( obj, rs);

            String externalURI = rs.getString("externalURI");
            el.setExternalURI(externalURI);
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        ExternalLink obj = bu.rimFac.createExternalLink();
        
        return obj;
    }
}
