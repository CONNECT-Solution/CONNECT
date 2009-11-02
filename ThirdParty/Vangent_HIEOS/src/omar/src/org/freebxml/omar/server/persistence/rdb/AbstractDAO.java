/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/AbstractDAO.java,v 1.24 2006/08/24 20:42:29 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.RepositoryItem;
import org.freebxml.omar.common.exceptions.ObjectNotFoundException;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.common.Utility;
import org.freebxml.omar.server.repository.RepositoryManager;
import org.freebxml.omar.server.repository.RepositoryManagerFactory;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.ExtrinsicObject;
import org.oasis.ebxml.registry.bindings.rim.VersionInfoType;

/**
 * @author Farruks S. Najmi
 *
 * Base class for all DAOs
 */
abstract class AbstractDAO implements OMARDAO {

    protected static BindingUtility bu = BindingUtility.getInstance();
    protected static Utility util = Utility.getInstance();
    protected static RepositoryManager rm = RepositoryManagerFactory.getInstance().getRepositoryManager();
    protected static int DAO_ACTION_QUERY = 0;
    protected static int DAO_ACTION_INSERT = 1;
    protected static int DAO_ACTION_UPDATE = 2;
    protected static int DAO_ACTION_DELETE = 3;
    private static final Log log = LogFactory.getLog(AbstractDAO.class);
    protected ServerRequestContext context = null;
    protected int action = DAO_ACTION_QUERY;
    protected Object parent;
    static int inClauseTermLimit = Integer.parseInt(RegistryProperties.getInstance().getProperty("omar.persistence.rdb.AbstractDAO.inClauseTermLimit", "100"));

    /**
     * Some DAOs are for objects composed with another parent object.
     * This method is to set the parent object.
     */
    public void setParent(Object parent) {
        this.parent = parent;
    }

    /**
     * Use this constructor only.
     */
    AbstractDAO(ServerRequestContext context) {
        this.context = context;
    }

    /*
     * Should not be used.
     */
    private AbstractDAO() {
    }

    /*
     * Initialize a binding object from specified ResultSet.
     */
    abstract protected void loadObject(Object obj, ResultSet rs) throws RegistryException;

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    abstract Object createObject() throws JAXBException;

    /**
     * Gets a List of binding objects from specified ResultSet.
     */
    public List getObjects(ResultSet rs, int startIndex, int maxResults) throws RegistryException {
        List res = new java.util.ArrayList();

        try {
            if (startIndex > 0) {
                // calling rs.next() is a workaround for some drivers, such
                // as Derby's, that do not set the cursor during call to 
                // rs.relative(...)
                rs.next();
                boolean onRow = rs.relative(startIndex - 1);
            }

            int cnt = 0;
            while (rs.next()) {
                Object obj = createObject();
                loadObject(obj, rs);
                res.add(obj);

                if (++cnt == maxResults) {
                    break;
                }
            }
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } catch (JAXBException j) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), j);
            throw new RegistryException(j);
        }

        return res;
    }

    /**
     * Does a bulk delete of a Collection of objects that match the type for this persister.
     *
     */
    public void delete(List objects) throws RegistryException {
        //Return immediatley if no objects to insert
        if (objects.size() == 0) {
            return;
        }

        log.trace(ServerResourceBundle.getInstance().getString("message.DeletingRowsInTable", new Object[]{new Integer(objects.size()), getTableName()}));
        action = DAO_ACTION_DELETE;

        Statement stmt = null;

        try {
            stmt = context.getConnection().createStatement();
            Iterator iter = objects.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();

                prepareToDelete(obj);

                String str = getSQLStatementFragment(obj);
                log.trace("SQL = " + str);
                stmt.addBatch(str);

                // HIEOS/BHT (Added block to get rid of MySQL performance bug with DB views).
                String mirrorImageStr = this.getSQLStatementFragmentForMirrorImage(obj);
                if (mirrorImageStr != null)
                {
                    log.trace("SQL = " + mirrorImageStr);  // HIEOS/BHT (DEBUG)
                    stmt.addBatch(mirrorImageStr);  // Now, DELETE the mirror.
                }
            }
            
            int[] updateCounts = stmt.executeBatch();

            iter = objects.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                onDelete(obj);
            }

        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }
    }

    protected String getParentId() throws JAXRException {
        String parentId = BindingUtility.getInstance().getObjectId(parent);
        return parentId;
    }

    /*
     * Gets the column name that is foreign key ref into parent table.
     * Must be overridden by derived class if it is not 'parent'
     */
    protected String getParentAttribute() {
        return "parent";
    }

    /*
     * Indicate whether the type for this DAO has composed objects or not.
     * Used in deciding whether to deleteComposedObjects or not during delete.
     *
     */
    protected boolean hasComposedObject() {
        return false;
    }

    /**
     * Does a bulk delete of objects based upon parent set for this DAO
     *
     */
    public void deleteByParent()
            throws RegistryException {
        PreparedStatement stmt = null;

        try {

            if (!hasComposedObject()) {
                //Do simple deletion if there are no composed objects for this type

                String str = "DELETE from " + getTableName() +
                        " WHERE " + getParentAttribute() + " = ? ";
                log.trace("SQL = " + str);
                stmt = context.getConnection().prepareStatement(str);
                stmt.setString(1, getParentId());
                stmt.execute();
            } else {
                //If there are composed objects for this type then
                //we must first fetch the objects and then use the
                //delete(List objects) method so that composed objects
                //are deleted.
                List objects = getByParent();
                delete(objects);
            }
        } catch (SQLException e) {
            RegistryException exception = new RegistryException(e);
            throw exception;
        } catch (JAXRException e) {
            RegistryException exception = new RegistryException(e);
            throw exception;
        } finally {
            closeStatement(stmt);
        }
    }

    /**
     * Gets objects based upon parent set for this DAO
     *
     */
    public List getByParent() throws RegistryException {
        List objects = new ArrayList();
        PreparedStatement stmt = null;
        try {
            String sql = "SELECT * FROM " + getTableName() +
                    " WHERE " + getParentAttribute() + " = ? ";
            stmt = context.getConnection().prepareStatement(sql);
            stmt.setString(1, getParentId());
            log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
            ResultSet rs = stmt.executeQuery();

            objects = getObjects(rs, 0, -1);
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } catch (JAXRException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }

        return objects;
    }

    /**
     * @see org.freebxml.omar.server.persistence.rdb.OMARDAO#getTableName()
     */
    public abstract String getTableName();

    /**
     * @see org.freebxml.omar.server.persistence.rdb.OMARDAO#insert(org.oasis.ebxml.registry.bindings.rim.UserType, java.sql.Connection, java.util.List, java.util.HashMap)
     */
    public void insert(List objects) throws RegistryException {
        //Return immediatley if no objects to insert
        if (objects.size() == 0) {
            return;
        }

        //First process any objects that may already exists in persistence layer
// BHT: OPTIMIZE ME!!!!!!!!!!!!!
        objects = processExistingObjects(objects);

        //Return immediately if no objects to insert
        if (objects.size() == 0) {
            return;
        }

        log.trace(ServerResourceBundle.getInstance().getString("message.InsertingRowsInTable", new Object[]{new Integer(objects.size()), getTableName()}));
        action = DAO_ACTION_INSERT;

        Statement stmt = null;
        try {
            stmt = context.getConnection().createStatement();

            Iterator iter = objects.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();

                String str = getSQLStatementFragment(obj);
                log.trace("SQL = " + str);
                stmt.addBatch(str);

                // HIEOS/BHT (Added block to get rid of MySQL performance bug with DB views).
                String mirrorImageStr = this.getSQLStatementFragmentForMirrorImage(obj);
                if (mirrorImageStr != null)
                {
                    log.trace("SQL = " + mirrorImageStr);   // HIEOS/BHT (DEBUG)
                    stmt.addBatch(mirrorImageStr);  // Now, insert into the mirror.
                }

                prepareToInsert(obj);
            }
            long startTime = System.currentTimeMillis();
            log.trace("AbstractDAO.insert: doing executeBatch");
            int[] updateCounts = stmt.executeBatch();
            long endTime = System.currentTimeMillis();
            log.trace("AbstractDAO.insert: done executeBatch elapedTimeMillis=" + (endTime - startTime));
            iter = objects.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                onInsert(obj);
            }

        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }
    }

    protected List processExistingObjects(List objects) throws RegistryException {
        return objects;
    }

    protected void prepareToInsert(Object object) throws RegistryException {
    }

    protected void prepareToUpdate(Object object) throws RegistryException {
        deleteComposedObjects(object);
    }

    protected void prepareToDelete(Object object) throws RegistryException {
        deleteComposedObjects(object);
    }

    protected void onInsert(Object object) throws RegistryException {
        insertComposedObjects(object);
    }

    protected void onUpdate(Object object) throws RegistryException {
        insertComposedObjects(object);
    }

    protected void onDelete(Object object) throws RegistryException {
    }

    protected void deleteComposedObjects(Object object) throws RegistryException {
    }

    protected void insertComposedObjects(Object object) throws RegistryException {
    }

    protected String getSQLStatementFragment(Object object)
            throws RegistryException {
        throw new RegistryException(ServerResourceBundle.getInstance().getString("message.getSQLStatementFragmentMissing",
                new Object[]{getTableName()}));
    }

    /**
     * @see org.freebxml.omar.server.persistence.rdb.OMARDAO#update(org.oasis.ebxml.registry.bindings.rim.UserType, java.sql.Connection, java.util.List, java.util.HashMap)
     */
    public void update(List objects) throws RegistryException {

        //Return immediatley if no objects to insert
        if (objects.size() == 0) {
            return;
        }

        log.trace(ServerResourceBundle.getInstance().getString("message.UpdatingRowsInTable", new Object[]{new Integer(objects.size()), getTableName()}));
        action = DAO_ACTION_UPDATE;

        Statement stmt = null;

        try {
            stmt = context.getConnection().createStatement();
            Iterator iter = objects.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();

                prepareToUpdate(obj);

                String str = getSQLStatementFragment(obj);
                log.trace("SQL = " + str);
                stmt.addBatch(str);

                // HIEOS/BHT (Added block to get rid of MySQL performance bug with DB views).
                String mirrorImageStr = this.getSQLStatementFragmentForMirrorImage(obj);
                if (mirrorImageStr != null)
                {
                    log.trace("SQL = " + mirrorImageStr);  // HIEOS/BHT (DEBUG)
                    stmt.addBatch(mirrorImageStr);  // Now, update the mirror.
                }
            }

            int[] updateCounts = stmt.executeBatch();

            iter = objects.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();

                onUpdate(obj);
            }

        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }
    }

    /**
     * A convenience method to close a <code>Statement</code> stmt.
     * Calls <code>close()</code> method if stmt is not NULL. Logs
     * <code>SQLException</code> as error.
     *
     * @param stmt Statement to be closed.
     */
    public final void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException sqle) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), sqle);
        }
    }

    /**
     * Return the id for the repository item used to hold information that
     * could not fit in a db column and therefor "spilled over" into a repository item.
     * The returned id is then stored instead of the content that "spilled over" into 
     * the repository item.
     *
     * @param parentId the id of the parent object whose column data is being "spilled over"
     * @param columnInfo contains information on the table and column whose data is being spilledOver. Format SHOULD be tableName:columnName)
     *
     * @return the id of the "spill over" repository item
     */
    String getSpillOverRepositoryItemId(String parentId, String columnInfo) throws RegistryException {
        String spillOverId = null;
        if ((columnInfo == null) || (columnInfo.length() == 0)) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.error.columnInfoUnspecified"));
        } else if (!columnInfo.endsWith(":")) {
            columnInfo += ":";
        }

        spillOverId = "urn:freebxml:registry:spillOverId:" + columnInfo + parentId;

        if (!org.freebxml.omar.common.Utility.getInstance().isValidRegistryId(spillOverId)) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.error.spillOverIdTooLong", new Object[]{parentId}));
        }

        return spillOverId;
    }

    /**
     * Saves content of a db column to an ExtrinsicObject / RepositoryItem pair.
     *
     * TODO: 
     *
     * Need to enforce referencial integrity by deleting spillover EO/RI when parent is deleted.
     * This should be done as part of the fix to prevent deletions when there are existing references
     * by treating spillover objects as if they are referenced by their parent.      
     *
     * @param parentId the id of the parent object whose column data is being "spilled over"
     * @param columnInfo contains information on the table and column whose data is being spilledOver. Format SHOULD be tableName:columnName)
     * @param columnData contains the column data being "spilled over" into RepositoryItem
     *
     * @return the id of the "spill over" repository item
     */
    protected String marshalToRepositoryItem(
            String parentId,
            String columnInfo,
            String columnData) throws RegistryException {

        String spillOverId = getSpillOverRepositoryItemId(parentId, columnInfo);

        try {
            ExtrinsicObject eo = bu.rimFac.createExtrinsicObject();
            eo.setId(spillOverId);
            eo.setLid(spillOverId);
            eo.setMimeType("text/plain");

            VersionInfoType versionInfo = bu.rimFac.createVersionInfoType();
            versionInfo.setVersionName("1.1");
            eo.setVersionInfo(versionInfo);
            eo.setContentVersionInfo(versionInfo);

            ExtrinsicObjectDAO extrinsicObjectDAO = new ExtrinsicObjectDAO(context);

            //Delete any previous eo and ri
            removeSpillOverRepositoryItem(parentId, columnInfo);

            RepositoryItem ri = org.freebxml.omar.common.Utility.getInstance().
                    createRepositoryItem(spillOverId, columnData);

            //Insert the extrinsic objects

            //Need to place ri in RepositoryItemsMap otherwise ExtrinsicObjectDAO will not set contentVersionInfo.versionName
            context.getRepositoryItemsMap().put(spillOverId, ri);
            ArrayList extrinsicObjects = new ArrayList();
            extrinsicObjects.add(eo);
            extrinsicObjectDAO.insert(extrinsicObjects);

            //Inserting the repository item
            rm.insert(((ServerRequestContext) context), ri);

            //Remove ri from RepositoryItemsMap to otherwise it will create problems in lcm.submitObjects wrapUp
            context.getRepositoryItemsMap().remove(spillOverId);
        } catch (RegistryException e) {
            throw e;
        } catch (Exception e) {
            throw new RegistryException(e);
        }

        return spillOverId;
    }

    /**
     * Remove specified spillover ExtrinsicObject and ReositoryItem.
     */
    protected void removeSpillOverRepositoryItem(String parentId, String columnInfo) throws RegistryException {
        String spillOverId = getSpillOverRepositoryItemId(parentId, columnInfo);

        try {
            ExtrinsicObject eo = bu.rimFac.createExtrinsicObject();
            eo.setId(spillOverId);
            eo.setLid(spillOverId);
            eo.setMimeType("text/plain");

            VersionInfoType versionInfo = bu.rimFac.createVersionInfoType();
            versionInfo.setVersionName("1.1");
            eo.setVersionInfo(versionInfo);
            eo.setContentVersionInfo(versionInfo);

            ExtrinsicObjectDAO extrinsicObjectDAO = new ExtrinsicObjectDAO(context);

            //Delete any previous copy
            try {
                //Delete existing eo and ri
                rm.delete(spillOverId);
            } catch (ObjectNotFoundException e) {
                //Also catches RepositoryItemNotFoundException
                //Does not exist. All is well
            }

            try {
                //Delete existing eo and ri
                extrinsicObjectDAO.delete(Collections.singletonList(eo));
            } catch (ObjectNotFoundException e) {
                //Does not exist. All is well
            }
        } catch (JAXBException e) {
            throw new RegistryException(e);
        }
    }

    /**
     * Unmarshalls a "spilled over" RepositoryItem into a String
     *
     * @param spillOverId the id of the ExtrinsicObject associated with the RepositoryItem 
     * used to unmarshal the "spilled over" content from.
     */
    public String unmarshallFromRepositoryItem(String spillOverId) throws RegistryException {
        String content = spillOverId;

        InputStream is = null;
        try {
            RepositoryItem ri = rm.getRepositoryItem(spillOverId);

            content = org.freebxml.omar.common.Utility.getInstance().
                    unmarshalInputStreamToString(ri.getDataHandler().getInputStream());
        } catch (IOException e) {
            throw new RegistryException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    log.error(e, e);
                }
            }
        }

        return content;
    }

    /**
     * Executes a Select statment that has an IN clause while
     * taking care to execute it in chunks to avoid scalability limits
     * in some databases (Oracle 10g limits terms in IN clause to 1000)
     *
     * Note: Caller is responsible for closing statement associated with each resultSet
     * in resultSets. 
     *
     * @param selectStmtTemplate a string representing the SELECT statment in a parameterized format consistent withebRR parameterized queries.
     * @return a List of Objects
     */
    public List executeBufferedSelectWithINClause(String selectStmtTemplate, List terms, int termLimit)
            throws RegistryException {
        List resultSets = new ArrayList();

        if (terms.size() == 0) {
            return resultSets;
        }

        Iterator iter = terms.iterator();

        try {
            //We need to count the number of terms in "IN" list. 
            //We need to split the SQL Strings into chunks if there are too many terms. 
            //Reason is that some database such as Oracle, do not allow the IN list is too long
            int termCounter = 0;

            StringBuffer inTerms = new StringBuffer();
            while (iter.hasNext()) {
                String term = (String) iter.next();

                if (iter.hasNext() && (termCounter < termLimit)) {
                    inTerms.append("'" + term + "',");
                } else {
                    inTerms.append("'" + term + "' ");
                    String sql = selectStmtTemplate.replaceAll("\\$InClauseTerms", inTerms.toString());

                    Statement stmt = context.getConnection().createStatement();
                    log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                    ResultSet rs = stmt.executeQuery(sql);
                    resultSets.add(rs);

                    termCounter = 0;
                    inTerms = new StringBuffer();
                }

                termCounter++;
            }

        } catch (SQLException e) {
            throw new RegistryException(e);
        } finally {
            //Do not close stmt as that will close resultSet. Caller needs to do this after reading resultSets.
        }

        return resultSets;
    }

    // HIEOS/BHT (ADDED): To work around mySQL view bug (can be overridden).
    public String getSQLStatementFragmentForMirrorImage(Object obj) throws RegistryException {
        return null;
    }
}
