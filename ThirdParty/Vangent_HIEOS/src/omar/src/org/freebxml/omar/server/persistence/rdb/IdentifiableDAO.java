/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/IdentifiableDAO.java,v 1.11 2006/08/24 20:42:29 farrukh_najmi Exp $
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

import org.freebxml.omar.common.BindingUtility;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.IdentifiableType;


/**
 *
 * @author Farrukh S. Najmi
 * @author Adrian Chong
 */
abstract class IdentifiableDAO extends AbstractDAO {
    private static final Log log = LogFactory.getLog(IdentifiableDAO.class);

    static int identifiableExistsBatchCount = Integer.parseInt(RegistryProperties.getInstance()
            .getProperty("omar.persistence.rdb.IdentifiableDAO.identifiableExistsBatchCount", "100"));    
    
    /**
     * Use this constructor only.
     */
    IdentifiableDAO(ServerRequestContext context) {
        super(context);
    }
                        
    /**
     * Delete composed objects that have the specified identifiable
     * as parent.
     */
    protected void deleteComposedObjects(Object object) throws RegistryException {
        super.deleteComposedObjects(object);
        
        if (object instanceof IdentifiableType) {
            IdentifiableType identifiable = (IdentifiableType)object;
            String id = identifiable.getId();

            SlotDAO slotDAO = new SlotDAO(context);
            slotDAO.setParent(identifiable);

            //Delete Slots
            slotDAO.deleteByParent();
        }
        else {
            int i=0;
        }
    }
    
    /**
     * Insert the composed objects for the specified identifiable
     */
    protected void insertComposedObjects(Object object) throws RegistryException {
        super.insertComposedObjects(object);

        if (object instanceof IdentifiableType) {
            IdentifiableType identifiable= (IdentifiableType)object;
            SlotDAO slotDAO = new SlotDAO(context);
            slotDAO.setParent(identifiable.getId());

            //Now insert Slots for this object
            List slots = identifiable.getSlot();

            if (slots.size() > 0) {            
                slotDAO.insert(slots, true);
            }
        }
        else {
            int i=0;
        }
    }
            
    /**
     * Returns the SQL fragment string needed by insert or update statements
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object object)
    throws RegistryException {
        
        IdentifiableType ident = (IdentifiableType)object;
        
        String stmtFragment = null;
        
        String id = ident.getId();
        String home = ident.getHome();
        
        if (home != null) {
            home = "'" + home + "'";
        }
               
        if (action == DAO_ACTION_INSERT) {
            stmtFragment =
            " VALUES('" + id + "', " + home + " ";
        }
        else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = " id='" + id + "', home=" + home + " ";
        }
        else if (action == DAO_ACTION_DELETE) {
            stmtFragment = "DELETE from " + getTableName() +
                " WHERE id = '" + id + "' ";
        }
        
        return stmtFragment;
    }
    
    protected List getIdentifiablesIds(List identifiables)
    throws RegistryException {
        List ids = new ArrayList();
        
        try {
            //log.info("size: "  + identifiables.size());
            Iterator iter = identifiables.iterator();
            
            while (iter.hasNext()) {
                String id = BindingUtility.getInstance().getObjectId(iter.next());
                ids.add(id);
            }
        } catch (JAXRException e) {
            throw new RegistryException(e);
        }
        
        return ids;
    }
        
    /**
     * Return true if the Identifiable exist
     */
    public boolean identifiableExist(String id)
    throws RegistryException {
        return identifiableExist(id, "identifiable");
    }
    
    /**
     * Check whether the object exists in the specified table.
     */
    public boolean identifiableExist(String id,
    String tableName) throws RegistryException {
        PreparedStatement stmt = null;
        
        try {
            String sql = "SELECT id FROM " + tableName + " WHERE id=?";
            stmt = context.getConnection().prepareStatement(sql);
            stmt.setString(1, id);
            log.trace("SQL = " + sql);
            ResultSet rs = stmt.executeQuery();
            boolean result = false;
            
            if (rs.next()) {
                result = true;
            }
            
            return result;
        } catch (SQLException e) {
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }
    }
    
    /**
     * Returns List of ids of non-existent Identifiable.
     */
    public List identifiablesExist(List ids)
    throws RegistryException {
        List notExistIdList = identifiablesExist(ids, "identifiable");
                
        return notExistIdList;
    }
    
    /**
     * Returns List of ids of non-existent Identifiable.
     */    
    public List identifiablesExist(List ids, String tableName)
    throws RegistryException {
        List notExistIdList = new ArrayList();
        
        if (ids.size() == 0) {
            return notExistIdList;
        }
        
        Iterator iter = ids.iterator();
        Statement stmt = null;
        
        try {
            stmt = context.getConnection().createStatement();
            
            StringBuffer sql = new StringBuffer("SELECT id FROM " + tableName + " WHERE id IN (");
            List existingIdList = new ArrayList();
            
            /* We need to count the number of item in "IN" list. We need to split the a single
            SQL Strings if it is too long. Some database such as Oracle, does not
            allow the IN list is too long*/
            int listCounter = 0;
            
            while (iter.hasNext()) {
                String id = (String) iter.next();
                
                if (iter.hasNext() && (listCounter < identifiableExistsBatchCount)) {
                    sql.append("'" + id + "',");
                } else {
                    sql.append("'" + id + "')");
                    
                    //log.info("!!!!!!!!!!!!!!!!!!!" + sql.toString());
                    log.trace("SQL = " + sql.toString());
                    ResultSet rs = stmt.executeQuery(sql.toString());
                    
                    while (rs.next()) {
                        existingIdList.add(rs.getString("id"));
                    }
                    
                    sql = new StringBuffer("SELECT id FROM " + tableName + " WHERE id IN (");
                    listCounter = 0;
                }
                
                listCounter++;
            }
            
            for (int i = 0; i < ids.size(); i++) {
                String id = (String) ids.get(i);
                
                if (!existingIdList.contains(id)) {
                    notExistIdList.add(id);
                }
            }
        } catch (SQLException e) {
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }
        
        return notExistIdList;
    }
    
    
    protected void loadObject(Object obj, ResultSet rs)
    throws RegistryException {
        try {
            if (!(obj instanceof IdentifiableType)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.IdentifiableTypeExpected",
                        new Object[]{obj}));
            }
            
            IdentifiableType ident = (IdentifiableType) obj;
            
            SlotDAO slotDAO = new SlotDAO(context);
            slotDAO.setParent(ident);
            
            String id = rs.getString("id");
            ident.setId(id);
            
            String home = rs.getString("home");
            if (home != null) {
                ident.setHome(home);
            }
                        
            
            boolean returnComposedObjects = context.getResponseOption().isReturnComposedObjects();
            
            if (returnComposedObjects) {
                List slots = slotDAO.getSlotsByParent(id);
                ident.getSlot().addAll(slots);
            }
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }
    }
    
                                                                                                         
}
