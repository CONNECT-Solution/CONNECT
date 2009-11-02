/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/SlotDAO.java,v 1.23 2006/08/24 20:42:29 farrukh_najmi Exp $
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
import java.util.Vector;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.exceptions.DuplicateSlotsException;
import org.freebxml.omar.common.exceptions.SlotNotExistException;
import org.freebxml.omar.common.exceptions.SlotsExistException;
import org.freebxml.omar.common.exceptions.SlotsParentNotExistException;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.common.Utility;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.Slot;
import org.oasis.ebxml.registry.bindings.rim.Value;
import org.oasis.ebxml.registry.bindings.rim.ValueList;


class SlotDAO extends AbstractDAO {
    private static final Log log = LogFactory.getLog(SlotDAO.class);

    /**
     * Use this constructor only.
     */
    SlotDAO(ServerRequestContext context) {
        super(context);
    }
    
    public static String getTableNameStatic() {
        return "Slot";
    }
    
    public String getTableName() {
        return getTableNameStatic();
    }
    
    /**
     * Returns the SQL fragment string needed by insert or update statements
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object obj) throws RegistryException {
        throw new RegistryException(ServerResourceBundle.getInstance().getString("message.methodNotSupported"));
    }
    
    /*
     * Initialize a binding object from specified ResultSet.
     */
    protected void loadObject(Object obj, ResultSet rs) throws RegistryException {
        throw new RegistryException(ServerResourceBundle.getInstance().getString("message.unimplementedMethod"));
    }        
    
    List getSlotsByParent(String parentId)
    throws RegistryException {
        List slots = new ArrayList();
        PreparedStatement stmt = null;
        
        try {
            String sql = "SELECT * FROM " + getTableName() +
            " WHERE parent = ? ORDER BY name_, sequenceId ASC";
            stmt = context.getConnection().prepareStatement(sql);
            stmt.setString(1, parentId);

            log.trace("SQL= " + sql);  // HIEOS/BHT: (DEBUG)
            ResultSet rs = stmt.executeQuery();
            
            String lastName = "";
            Slot slot = null;
            ValueList valueList = null;
            
            while (rs.next()) {
                //int sequenceId = rs.getInt("sequenceId");
                String name = rs.getString("name_");
                String slotType = rs.getString("slotType");
                String value = rs.getString("value");
                
                if (!name.equals(lastName)) {
                    slot = bu.rimFac.createSlot();
                    slot.setName(name);
                    
                    if (slotType != null) {
                        slot.setSlotType(slotType);
                    }
                    
                    valueList = bu.rimFac.createValueList();
                    slot.setValueList(valueList);
                    slots.add(slot);
                }
                
                lastName = name;
                
                if (value != null) {
                    Value item = bu.rimFac.createValue();
                    item.setValue(value);
                    valueList.getValue().add(item);
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
        
        return slots;
    }
    
    /**
     * Get the List of the names of a Slots that already exist in database
     * @param parentId the id of the parent of those slots
     */
    public List slotsExist(String parentId, List slots)
    throws RegistryException {
        Statement stmt = null;
        List slotsNames = new ArrayList();
        
        if (slots.size() > 0) {
            try {
                Iterator slotsIter = slots.iterator();
                String names = "";
                
                while (slotsIter.hasNext()) {
                    names += ("'" +
                    Utility.escapeSQLChars(((Slot) slotsIter.next()).getName()) +
                    "'");
                    
                    if (slotsIter.hasNext()) {
                        names += ",";
                    }
                }
                
                String sql = "SELECT name_ FROM " + getTableName() + " WHERE" +
                " parent=" + "'" + parentId + "'" + " AND " +
                " name_ IN (" + names + ")";
                
                //log.trace("stmt= '" + sql + "'");
                stmt = context.getConnection().createStatement();
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    slotsNames.add(rs.getString(1));
                }
                
                return slotsNames;
            } catch (SQLException e) {
                throw new RegistryException(e);
            } finally {
                closeStatement(stmt);
            }
        } else {
            return slotsNames;
        }
    }
    
    /**
     *        It checks whether there exists more than a slot having the same name
     *        @returns List of duplidate slots names
     */
    public List getDuplicateSlots(List slots) {
        List duplicateSlotsNames = new ArrayList();
        List slotsNames = new ArrayList();
        
        if (slots.size() > 0) {
            Iterator iter = slots.iterator();
            
            while (iter.hasNext()) {
                String slotName = ((Slot) iter.next()).getName();
                
                if (slotsNames.contains(slotName)) {
                    duplicateSlotsNames.add(slotName);
                }
                
                slotsNames.add(slotName);
            }
            
            return duplicateSlotsNames;
        } else {
            return duplicateSlotsNames;
        }
    }
    
    public void insert(List slots) throws RegistryException {
        throw new RegistryException(ServerResourceBundle.getInstance().getString("message.methodNotSupported"));        
    }
    
    /**
     * @param parentInsert It should be set to true if Slot insert is part of new
     * RegistryObject insert (i.e. in the case        of SubmitObjectsRequest). It should
     * be set to false in the case of AddSlotsRequest because the parent of the
     * slot is expected to be already submitted by previous SubmitObjectRequest.
     * In the latter case whether the parents of the slots exist will be checked.
     */
    public void insert(List slots, boolean parentInsert) throws RegistryException {
        PreparedStatement pstmt = null;
        
        String parentId = (String)parent;
        
        if (slots.size() == 0) {
            return;
        }
        
        try {
            String sql = "INSERT INTO " + getTableName() + " (sequenceId, " +
            "name_, slotType, value, parent)" + " VALUES(?, ?, ?, ?, ?)";
            pstmt = context.getConnection().prepareStatement(sql);
            
            List duplicateSlotsNames = getDuplicateSlots(slots);
            
            if (duplicateSlotsNames.size() > 0) {
                // Some slots have duplicate name
                throw new DuplicateSlotsException(parentId, duplicateSlotsNames);
            }
            
            RegistryObjectDAO roDAO = new RegistryObjectDAO(context);
            
            // Check whether the parent exist in database, in case the parent
            // has been inserted by the previous SubmitObjectsRequest
            // (i.e. in the case of AddSlotsRequest)
            if (!parentInsert &&
            !roDAO.registryObjectExist(parentId)) {
                // The parent does not exist
                throw new SlotsParentNotExistException(parentId);
            }
            /* HIEOS/BHT: Disabled for performance purposes.
            List slotsNamesAlreadyExist = slotsExist(parentId, slots);
            
            if (slotsNamesAlreadyExist.size() > 0) {
                // Some slots for this RegistryObject already exist
                throw new SlotsExistException(parentId, slotsNamesAlreadyExist);
            }*/
            
            Iterator iter = slots.iterator();
            Vector slotNames = new Vector();
            
            while (iter.hasNext()) {
                Slot slot = (Slot) iter.next();
                String slotName = slot.getName();
                String slotType = slot.getSlotType();
                List values = slot.getValueList().getValue();
                int size = values.size();
                
                for (int j = 0; j < size; j++) {
                    String value = ((Value) values.get(j)).getValue();
                    pstmt.setInt(1, j);
                    pstmt.setString(2, slotName);
                    pstmt.setString(3, slotType);
                    pstmt.setString(4, value);
                    pstmt.setString(5, parentId);
                    log.trace("SQL = " + sql);   // HIEOS/BHT: DEBUG (fix)
                    pstmt.addBatch();
                }
            }
            
            if (slots.size() > 0) {
                int[] updateCounts = pstmt.executeBatch();
            }
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } finally {
            closeStatement(pstmt);
        }
    }
    
    
    public void deleteByParentIdAndSlots(
    String parentId, List slots) throws RegistryException {
        Statement stmt = null;
        
        try {
            stmt = context.getConnection().createStatement();
            
            String str = "DELETE from " + getTableName() + " WHERE parent = '" +
            parentId + "' AND (";
            Iterator iter = slots.iterator();
            
            while (iter.hasNext()) {
                Slot slot = (Slot) iter.next();
                String slotName = slot.getName();
                
                if (iter.hasNext()) {
                    str = str + "name_ = '" + Utility.escapeSQLChars(slotName) +
                    "' OR ";
                } else {
                    str = str + "name_ = '" + Utility.escapeSQLChars(slotName) +
                    "' )";
                }
            }
            
            log.trace("SQL = " + str);
            stmt.execute(str);
            
            int updateCount = stmt.getUpdateCount();
            
            if (updateCount < slots.size()) {
                throw new SlotNotExistException(parentId);
            }
        } catch (SQLException e) {
            RegistryException exception = new RegistryException(e);
            throw exception;
        } finally {
            closeStatement(stmt);
        }
    }
    
    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        Slot obj = bu.rimFac.createSlot();
        
        return obj;
    }
}
