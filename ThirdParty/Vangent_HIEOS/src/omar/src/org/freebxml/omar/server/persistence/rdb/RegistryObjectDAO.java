/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/RegistryObjectDAO.java,v 1.71 2006/11/30 23:44:52 dougb62 Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import org.freebxml.omar.common.CanonicalSchemes;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.IterativeQueryParams;
import org.freebxml.omar.common.exceptions.ObjectNotFoundException;
import org.freebxml.omar.server.cache.ServerCache;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.AuditableEventType;
import org.oasis.ebxml.registry.bindings.rim.ClassificationNodeType;
import org.oasis.ebxml.registry.bindings.rim.Description;
import org.oasis.ebxml.registry.bindings.rim.ExternalLinkType;
import org.oasis.ebxml.registry.bindings.rim.ExtrinsicObjectType;
import org.oasis.ebxml.registry.bindings.rim.InternationalStringType;
import org.oasis.ebxml.registry.bindings.rim.Name;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObject;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.VersionInfoType;

/**
 *
 * @author Farrukh S. Najmi
 * @author Adrian Chong
 */
class RegistryObjectDAO extends IdentifiableDAO {

    private static final Log log = LogFactory.getLog(RegistryObjectDAO.class);

    /**
     * Use this constructor only.
     */
    RegistryObjectDAO(ServerRequestContext context) {
        super(context);
    }

    public static String getTableNameStatic() {
        return "RegistryObject";
    }

    public String getTableName() {
        return getTableNameStatic();
    }

    protected void prepareToInsert(Object object) throws RegistryException {
        super.prepareToInsert(object);
        RegistryObjectType ro = (RegistryObjectType) object;

        //Need to distinguish between Created and Versioned events 
        //An original object (Create) has either no versionName or
        //versionName of "1.1"

        String versionName = null;
        VersionInfoType versionInfo = ro.getVersionInfo();
        if (versionInfo != null) {
            versionName = versionInfo.getVersionName();
        }

        if (!(ro instanceof AuditableEventType)) {
            //Careful not to include event as affected by itself            
            AuditableEventType ae = null;
            if ((versionName != null) && (!versionName.equals("1.1"))) {
                //Add to affectedObjects of versionEvent
                ae = context.getVersionEvent();
            } else {
                //Add to affectedObjects of createEvent
                ae = context.getCreateEvent();
            }
            context.addAffectedObjectToAuditableEvent(ae, ro);
        }
    }

    protected void prepareToUpdate(Object object) throws RegistryException {
        super.prepareToUpdate(object);
        RegistryObjectType ro = (RegistryObjectType) object;

        if (!(ro instanceof AuditableEventType)) {
            //Add to affectedObjects of updateEvent
            //But careful not to include event as affected by itself
            AuditableEventType ae = context.getUpdateEvent();
            context.addAffectedObjectToAuditableEvent(ae, ro);
        }
    }

    protected void prepareToDelete(Object object) throws RegistryException {
        super.prepareToDelete(object);
        RegistryObjectType ro = (RegistryObjectType) object;

        if (!(ro instanceof AuditableEventType)) {
            //Add to affectedObjects of deleteEvent
            //Careful not to include event as affected by itself
            AuditableEventType ae = context.getDeleteEvent();
            context.addAffectedObjectToAuditableEvent(ae, ro);
        }
    }

    /**
     * Get the objectType for specified object.
     * If it is an ExtrinsicObject or ExternalLink then get it from the object.
     * Otherwise ignore the value in the object and get from
     * the DAO the hardwired value.
     */
    protected String getObjectType(RegistryObjectType ro) throws RegistryException {
        String objectType = null;

        try {
            String roClassName = ro.getClass().getName();
            String rimName = roClassName.substring(roClassName.lastIndexOf('.') + 1, roClassName.length() - 4);
            Field field = CanonicalSchemes.class.getDeclaredField("CANONICAL_OBJECT_TYPE_ID_" + rimName);
            objectType = field.get(null).toString();
        } catch (Exception e) {
            throw new RegistryException(e);
        }

        //TODO Get object type from leaf DAO if not ExtrinsicObject
        if ((ro instanceof ExtrinsicObjectType) || (ro instanceof ExternalLinkType)) {
            String _objectType = ro.getObjectType();
            if (_objectType != null) {
                objectType = _objectType;
            }

            //Make sure that objectType is a ref to a ObjectType ClassificationNode
            context.checkClassificationNodeRefConstraint(objectType, bu.CANONICAL_CLASSIFICATION_SCHEME_ID_ObjectType, "objectType");
        }

        return objectType;
    }

    /*
     * Indicate whether the type for this DAO has composed objects or not.
     * Used in deciding whether to deleteComposedObjects or not during delete.
     *
     */
    protected boolean hasComposedObject() {
        return true;
    }

    /**
     * Delete composed objects that have the specified registryObject
     * as parent.
     */
    protected void deleteComposedObjects(Object object) throws RegistryException {
        super.deleteComposedObjects(object);

        if (object instanceof RegistryObjectType) {
            RegistryObjectType registryObject = (RegistryObjectType) object;

            ClassificationDAO classificationDAO = new ClassificationDAO(context);
            classificationDAO.setParent(registryObject);
            DescriptionDAO descriptionDAO = new DescriptionDAO(context);
            descriptionDAO.setParent(registryObject);
            ExternalIdentifierDAO externalIdentifierDAO = new ExternalIdentifierDAO(context);
            externalIdentifierDAO.setParent(registryObject);
            NameDAO nameDAO = new NameDAO(context);
            nameDAO.setParent(registryObject);

            //Delete name
            nameDAO.deleteByParent();

            //Delete description
            descriptionDAO.deleteByParent();

            //Delete ExternalIdentifier
            externalIdentifierDAO.deleteByParent();

            //Delete Classifications
            classificationDAO.deleteByParent();
        } else {
            int i = 0;
        }
    }

    /**
     * Insert the composed objects for the specified registryObject
     */
    protected void insertComposedObjects(Object object) throws RegistryException {
        super.insertComposedObjects(object);

        if (object instanceof RegistryObjectType) {
            RegistryObjectType registryObject = (RegistryObjectType) object;
            ClassificationDAO classificationDAO = new ClassificationDAO(context);
            classificationDAO.setParent(registryObject);
            DescriptionDAO descriptionDAO = new DescriptionDAO(context);
            descriptionDAO.setParent(registryObject);
            ExternalIdentifierDAO externalIdentifierDAO = new ExternalIdentifierDAO(context);
            externalIdentifierDAO.setParent(registryObject);
            NameDAO nameDAO = new NameDAO(context);
            nameDAO.setParent(registryObject);

            //Insert name
            InternationalStringType name = registryObject.getName();

            String id = registryObject.getId();

            if (name != null) {
                nameDAO.insert(id, name);
            }

            //Insert description
            InternationalStringType desc = registryObject.getDescription();

            if (desc != null) {
                descriptionDAO.insert(id, desc);
            }

            //Insert ExternalIdentifiers
            List extIds = registryObject.getExternalIdentifier();

            if (extIds.size() > 0) {
                externalIdentifierDAO.insert(extIds);
            }

            //Insert Classifications
            List classifications = registryObject.getClassification();

            if (classifications.size() > 0) {
                classificationDAO.insert(classifications);
            }
        } else {
            int i = 0;
        }
    }

    /* ADDED (HIEOS/BHT) -> To deal with major MySQL issue with view performance */
    /* Some code lifted from getSQLStatementFragment() below */
    public String getSQLStatementFragmentForMirrorImage(Object object)
            throws RegistryException {
        String stmtFragment = null;
        RegistryObjectType ro = (RegistryObjectType) object;
        if (object instanceof RegistryObjectType) {
            if (action == DAO_ACTION_INSERT) {
                stmtFragment = "INSERT INTO RegistryObject " + this.getSQLStatementFragmentInternal(ro) + ") ";
            } else if (action == DAO_ACTION_UPDATE) {
                stmtFragment = "UPDATE RegistryObject SET " + this.getSQLStatementFragmentInternal(ro) +
                        " WHERE id = '" + ro.getId() + "' " +
                        " AND objectType = '" + ro.getObjectType() + "' ";
            } else {
                stmtFragment = "DELETE from RegistryObject WHERE id = '" + ro.getId() + "' " +
                        " AND objectType = '" + ro.getObjectType() + "' ";
            }
        //System.out.println("*** MIRROR SQL: " + stmtFragment);
        }
        return stmtFragment;
    }

    /**
     * Returns the SQL fragment string needed by insert or update statements
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object object)
            throws RegistryException {
        return this.getSQLStatementFragmentInternal(object);  // HIEOS/BHT: Replaced body.
    }

    /**
     * Returns the SQL fragment string needed by insert or update statements
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    // HIEOS/BHT: Added to support MySQL View bug.
    private String getSQLStatementFragmentInternal(Object object)
            throws RegistryException {

        RegistryObjectType ro = (RegistryObjectType) object;

        String stmtFragment = super.getSQLStatementFragment(ro);

        String lid = ro.getLid();
        String id = ro.getId();

        if (lid == null) {
            //TODO:versioning: Need to get lid of first version if not first version
            lid = id;
        }

        VersionInfoType versionInfo = ro.getVersionInfo();
        String versionName = null;
        String comment = null;

        if (versionInfo != null) {
            versionName = versionInfo.getVersionName();
            comment = versionInfo.getComment();
        }

        if ((versionName == null) || (versionName.length() == 0)) {
            versionName = "1.1";
        }
        versionName = "'" + versionName + "'";

        if ((comment != null) && (comment.length() > 0)) {
            comment = "'" + comment + "'";
        } else {
            comment = null;
        }

        String objectType = null;
        if (action == DAO_ACTION_INSERT) {
            objectType = getObjectType(ro);

            // Need to force the status to Submitted
            ro.setStatus(BindingUtility.CANONICAL_STATUS_TYPE_ID_Submitted);

            stmtFragment +=
                    ", '" + lid + "', '" + objectType +
                    "', '" + BindingUtility.CANONICAL_STATUS_TYPE_ID_Submitted +
                    "', " + versionName + ", " + comment;
        } else if (action == DAO_ACTION_UPDATE) {
            objectType = getObjectType(ro);

            // Following [ebRIM, 2.5.9] requirements, ignore any status
            // value which may have come from client -- updateStatus() will
            // change persisted status, not update()
            stmtFragment += ", lid='" + lid +
                    "', objectType='" + objectType +
                    "', versionName=" + versionName + ", comment_=" + comment + " ";
        } else if (action == DAO_ACTION_DELETE) {
            // ??? Should this branch do something?
        }

        return stmtFragment;
    }

    /**
     * Sort registryObjectIds by their objectType.
     * @return The HashMap storing the objectType String as keys and List of ids
     * as values. For ExtrinsicObject, the objectType key is stored as "ExtrinsicObject"
     * rather than the objectType of the repository items.
     */
    public HashMap sortIdsByObjectType(List registryObjectIds) throws RegistryException {
        HashMap map = new HashMap();
        Statement stmt = null;

        try {
            if (registryObjectIds.size() > 0) {
                stmt = context.getConnection().createStatement();

                StringBuffer str = new StringBuffer("SELECT id, objectType FROM " + getTableName() +
                        " WHERE id IN ( ");

                Iterator iter = registryObjectIds.iterator();

                while (iter.hasNext()) {
                    String id = (String) iter.next();
                    str.append("'");
                    str.append(id);

                    if (iter.hasNext()) {
                        str.append("', ");
                    } else {
                        str.append("' )");
                    }
                }

                log.trace(ServerResourceBundle.getInstance().getString("message.stmtEquals", new Object[]{str}));
                log.trace("SQL = " + str.toString());  // HIEOS/BHT: (DEBUG)
                ResultSet rs = stmt.executeQuery(str.toString());

                List adhocQuerysIds = new ArrayList();
                List associationsIds = new ArrayList();
                List auditableEventsIds = new ArrayList();
                List classificationsIds = new ArrayList();
                List classificationSchemesIds = new ArrayList();
                List classificationNodesIds = new ArrayList();
                List externalIdentifiersIds = new ArrayList();
                List externalLinksIds = new ArrayList();
                List extrinsicObjectsIds = new ArrayList();
                List federationsIds = new ArrayList();
                List organizationsIds = new ArrayList();
                List registrysIds = new ArrayList();
                List registryPackagesIds = new ArrayList();
                List serviceBindingsIds = new ArrayList();
                List servicesIds = new ArrayList();
                List specificationLinksIds = new ArrayList();
                List subscriptionsIds = new ArrayList();
                List usersIds = new ArrayList();

                while (rs.next()) {
                    String id = rs.getString(1);
                    String objectType = rs.getString(2);

                    // log.info(ServerResourceBundle.getInstance().getString("message.objectType!!!!!!!", new Object[]{objectType}));
                    if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_AdhocQuery)) {
                        adhocQuerysIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Association)) {
                        associationsIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_AuditableEvent)) {
                        auditableEventsIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Classification)) {
                        classificationsIds.add(id);
                    } else if (objectType.equalsIgnoreCase(
                            BindingUtility.CANONICAL_OBJECT_TYPE_ID_ClassificationScheme)) {
                        classificationSchemesIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ClassificationNode)) {
                        classificationNodesIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ExternalIdentifier)) {
                        externalIdentifiersIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ExternalLink)) {
                        externalLinksIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Federation)) {
                        federationsIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Organization)) {
                        organizationsIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Registry)) {
                        registrysIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_RegistryPackage)) {
                        registryPackagesIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ServiceBinding)) {
                        serviceBindingsIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Service)) {
                        servicesIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_SpecificationLink)) {
                        specificationLinksIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Subscription)) {
                        subscriptionsIds.add(id);
                    } else if (objectType.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_User)) {
                        usersIds.add(id);
                    } else {
                        //TODO: Fix dangerous assumption that is is an ExtrinsicObject
                        //Need to compare if objectType is a subType of ExtrinsicObject or not
                        extrinsicObjectsIds.add(id);
                    }
                }

                // end looping ResultSet
                // Now put the List of id of varios RO type into the HashMap
                if (adhocQuerysIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_AdhocQuery, adhocQuerysIds);
                }

                if (associationsIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Association, associationsIds);
                }

                if (auditableEventsIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_AuditableEvent, auditableEventsIds);
                }

                if (classificationsIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Classification, classificationsIds);
                }

                if (classificationSchemesIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ClassificationScheme, classificationSchemesIds);
                }

                if (classificationNodesIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ClassificationNode, classificationNodesIds);
                }

                if (externalIdentifiersIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ExternalIdentifier, externalIdentifiersIds);
                }

                if (externalLinksIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ExternalLink, externalLinksIds);
                }

                if (federationsIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Federation, federationsIds);
                }

                if (organizationsIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Organization, organizationsIds);
                }

                if (registrysIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Registry, registrysIds);
                }

                if (registryPackagesIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_RegistryPackage, registryPackagesIds);
                }

                if (serviceBindingsIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ServiceBinding, serviceBindingsIds);
                }

                if (servicesIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Service, servicesIds);
                }

                if (specificationLinksIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_SpecificationLink, specificationLinksIds);
                }

                if (subscriptionsIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Subscription, subscriptionsIds);
                }

                if (usersIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_User, usersIds);
                }

                if (extrinsicObjectsIds.size() > 0) {
                    map.put(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ExtrinsicObject, extrinsicObjectsIds);
                }
            }

        // end if checking the size of registryObjectsIds
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }

        return map;
    }

    /**
     * Return true if the RegistryObject exist
     */
    public boolean registryObjectExist(String id)
            throws RegistryException {
        PreparedStatement stmt = null;

        try {
            String sql = "SELECT id from RegistryObject where id=?";
            stmt = context.getConnection().prepareStatement(sql);
            stmt.setString(1, id);
            log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
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
     * Updates any passed RegistryObjects if they already exists.
     * Called by insert to handle implcit update of existing objects.
     *
     * @return List of RegistryObjects that are not existing
     */
    protected List processExistingObjects(List ros) throws RegistryException {
        BindingUtility bindingUtility = BindingUtility.getInstance();

        List notExistIds = identifiablesExist(
                bindingUtility.getIdsFromRegistryObjects(ros), getTableName()); // getTableName() is the one which is the overidding one of subclass DAO
        List notExistROs = bindingUtility.getRegistryObjectsFromIds(ros,
                notExistIds);
        List existingROs = new ArrayList();
        Iterator rosIter = ros.iterator();

        while (rosIter.hasNext()) {
            RegistryObjectType ro = (RegistryObjectType) rosIter.next();

            if (!notExistROs.contains(ro)) {
                existingROs.add(ro);
            }
        }

        update(existingROs);

        return notExistROs;
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        RegistryObject obj = BindingUtility.getInstance().rimFac.createRegistryObject();

        return obj;
    }

    protected void loadObject(Object obj, ResultSet rs)
            throws RegistryException {
        try {
            if (!(obj instanceof RegistryObjectType)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.RegistryobjectTypeExpected",
                        new Object[]{obj}));
            }

            RegistryObjectType ro = (RegistryObjectType) obj;

            super.loadObject(ro, rs);

            ClassificationDAO classificationDAO = new ClassificationDAO(context);
            classificationDAO.setParent(ro);
            DescriptionDAO descriptionDAO = new DescriptionDAO(context);
            descriptionDAO.setParent(ro);
            ExternalIdentifierDAO externalIdentifierDAO = new ExternalIdentifierDAO(context);
            externalIdentifierDAO.setParent(ro);
            NameDAO nameDAO = new NameDAO(context);
            nameDAO.setParent(ro);

            String id = rs.getString("id");

            String lid = rs.getString("lid");
            if (lid != null) {
                //TODO:versioning: remove this check as persistent objects should always have a lid
                ro.setLid(lid);
            }

            String objectType = rs.getString("objectType");
            ro.setObjectType(objectType);

            Name name = nameDAO.getNameByParent(id);
            ro.setName(name);

            Description desc = descriptionDAO.getDescriptionByParent(id);
            ro.setDescription(desc);

            String status = rs.getString("status");
            if (status.equals("null")) {
                // Ignore earlier corruption of the database, losing whatever
                // status was before that problem occurred
                status = BindingUtility.CANONICAL_STATUS_TYPE_ID_Submitted;

            // TODO: unfortunately, can't turn read operation into a
            // write...  (best fix probably a SQL script)
            // updateStatus(ro, status);
            }
            ro.setStatus(status);

            //Now set VersionInfo
            VersionInfoType versionInfo = BindingUtility.getInstance().rimFac.createVersionInfoType();
            String versionName = rs.getString("versionName");
            if (versionName != null) {
                versionInfo.setVersionName(versionName);
            }

            String comment = rs.getString("comment_");
            if (comment != null) {
                versionInfo.setComment(comment);
            }
            ro.setVersionInfo(versionInfo);

            boolean returnComposedObjects = context.getResponseOption().isReturnComposedObjects();

            if (returnComposedObjects) {
                List classifications = classificationDAO.getByParent();
                ro.getClassification().addAll(classifications);

                List extIds = externalIdentifierDAO.getByParent();
                ro.getExternalIdentifier().addAll(extIds);
            }
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } catch (javax.xml.bind.JAXBException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }
    }

    private void getRegistryObjectsIdsFromResultSet(ResultSet rs,
            int startIndex, int maxResults,
            StringBuffer adhocQuerys,
            StringBuffer associations, StringBuffer auEvents,
            StringBuffer classifications, StringBuffer schemes,
            StringBuffer classificationNodes, StringBuffer externalIds,
            StringBuffer externalLinks, StringBuffer extrinsicObjects,
            StringBuffer federations,
            StringBuffer organizations, StringBuffer registrys, StringBuffer packages,
            StringBuffer serviceBindings, StringBuffer services,
            StringBuffer specificationLinks, StringBuffer subscriptions, StringBuffer users, StringBuffer persons)
            throws SQLException, RegistryException {
        HashSet processed = new HashSet();

        if (startIndex > 0) {
            // calling rs.next() is a workaround for some drivers, such
            // as Derby's, that do not set the cursor during call to 
            // rs.relative(...)
            rs.next();
            boolean onRow = rs.relative(startIndex - 1);
        }

        int cnt = 0;
        while (rs.next()) {
            String id = rs.getString("id");

            //Only process if not already processed
            //This avoid OutOfMemoryError when huge number of objects match
            //Currently this happens when name and desc are null and their
            //predicates get pruned but the tablename stays.
            //TODO: Fix query pruning so tableName is pruned if not used.
            if (!(processed.contains(id))) {
                cnt++;
                String type = rs.getString("objectType");
                //System.err.println("id=" + id + " objectType=" + type + " extrinsicObjects=" + extrinsicObjects);


                //log.info(ServerResourceBundle.getInstance().getString("message.objectType=''", new Object[]{type}));
                if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_AdhocQuery)) {
                    if (adhocQuerys.length() == 0) {
                        adhocQuerys.append("'" + id + "'");
                    } else {
                        adhocQuerys.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Association)) {
                    if (associations.length() == 0) {
                        associations.append("'" + id + "'");
                    } else {
                        associations.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_AuditableEvent)) {
                    if (auEvents.length() == 0) {
                        auEvents.append("'" + id + "'");
                    } else {
                        auEvents.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Classification)) {
                    if (classifications.length() == 0) {
                        classifications.append("'" + id + "'");
                    } else {
                        classifications.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ClassificationNode)) {
                    if (classificationNodes.length() == 0) {
                        classificationNodes.append("'" + id + "'");
                    } else {
                        classificationNodes.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ClassificationScheme)) {
                    if (schemes.length() == 0) {
                        schemes.append("'" + id + "'");
                    } else {
                        schemes.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ExternalIdentifier)) {
                    if (externalIds.length() == 0) {
                        externalIds.append("'" + id + "'");
                    } else {
                        externalIds.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ExternalLink)) {
                    if (externalLinks.length() == 0) {
                        externalLinks.append("'" + id + "'");
                    } else {
                        externalLinks.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ExtrinsicObject)) {
                    if (extrinsicObjects.length() == 0) {
                        extrinsicObjects.append("'" + id + "'");
                    } else {
                        extrinsicObjects.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Federation)) {
                    if (federations.length() == 0) {
                        federations.append("'" + id + "'");
                    } else {
                        federations.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Organization)) {
                    if (organizations.length() == 0) {
                        organizations.append("'" + id + "'");
                    } else {
                        organizations.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Registry)) {
                    if (registrys.length() == 0) {
                        registrys.append("'" + id + "'");
                    } else {
                        registrys.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_RegistryPackage)) {
                    if (packages.length() == 0) {
                        packages.append("'" + id + "'");
                    } else {
                        packages.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_ServiceBinding)) {
                    if (serviceBindings.length() == 0) {
                        serviceBindings.append("'" + id + "'");
                    } else {
                        serviceBindings.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Service)) {
                    if (services.length() == 0) {
                        services.append("'" + id + "'");
                    } else {
                        services.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_SpecificationLink)) {
                    if (specificationLinks.length() == 0) {
                        specificationLinks.append("'" + id + "'");
                    } else {
                        specificationLinks.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Subscription)) {
                    if (subscriptions.length() == 0) {
                        subscriptions.append("'" + id + "'");
                    } else {
                        subscriptions.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_User)) {
                    if (users.length() == 0) {
                        users.append("'" + id + "'");
                    } else {
                        users.append(",'" + id + "'");
                    }
                } else if (type.equalsIgnoreCase(BindingUtility.CANONICAL_OBJECT_TYPE_ID_Person)) {
                    if (persons.length() == 0) {
                        persons.append("'" + id + "'");
                    } else {
                        persons.append(",'" + id + "'");
                    }
                } else {
                    //Type is user defined. Table could be either ExtrinsicObject or ExternalLink
                    SQLPersistenceManagerImpl pm = SQLPersistenceManagerImpl.getInstance();

                    ArrayList queryParams = new ArrayList();

                    // COMMENT 1:
                    // HIEOS/AMS: Commented the following two lines of code. No need to convert 'id' to upper case
                    // and subsequently compare using SQL's UPPER function (Using this prevents
                    /// evaluation of indices on 'id').
                    // queryParams.add(id.toUpperCase());
                    // ExtrinsicObjectType eo = (ExtrinsicObjectType) pm.getRegistryObjectMatchingQuery(context, "SELECT * from ExtrinsicObject where UPPER(id) = ?", queryParams, "ExtrinsicObject");
                    queryParams.add(id);
                    ExtrinsicObjectType eo = (ExtrinsicObjectType) pm.getRegistryObjectMatchingQuery(context, "SELECT * from ExtrinsicObject where id = ?", queryParams, "ExtrinsicObject");

                    if (eo != null) {
                        if (extrinsicObjects.length() == 0) {
                            extrinsicObjects.append("'" + id + "'");
                        } else {
                            extrinsicObjects.append(",'" + id + "'");
                        }
                    } else {
                        // HIEOS/AMS: See COMMENT 1.
                        // ExternalLinkType el = (ExternalLinkType) pm.getRegistryObjectMatchingQuery(context, "SELECT * from ExternalLink where UPPER(id) = ?", queryParams, "ExternalLink");
                        ExternalLinkType el = (ExternalLinkType) pm.getRegistryObjectMatchingQuery(context, "SELECT * from ExternalLink where id = ?", queryParams, "ExternalLink");

                        if (el != null) {
                            if (externalLinks.length() == 0) {
                                externalLinks.append("'" + id + "'");
                            } else {
                                externalLinks.append(",'" + id + "'");
                            }
                        } else {
                            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.unknownObjectType",
                                    new Object[]{type}));
                        }
                    }
                }
                processed.add(id);

                if (cnt == maxResults) {
                    break;
                }
            }
        }

        if (cnt > 1000) {
            log.warn(ServerResourceBundle.getInstance().getString("message.WarningExcessiveResultSetSizeQUery",
                    new Object[]{new Integer(cnt)}));
        }
    }

    /**
     * Gets the List of binding objects for specified ResultSet.
     * This method return leaf object types while the base class
     * version returns RegistryObjects. 
     *
     */
    public List getObjectsHetero(ResultSet rs,
            int startIndex, int maxResults) throws RegistryException {
        List res = new ArrayList();
        String sql = null;

        StringBuffer adhocQuerysIds = new StringBuffer();
        StringBuffer associationsIds = new StringBuffer();
        StringBuffer auditableEventsIds = new StringBuffer();
        StringBuffer classificationsIds = new StringBuffer();
        StringBuffer schemesIds = new StringBuffer();
        StringBuffer classificationNodesIds = new StringBuffer();
        StringBuffer externalIdsIds = new StringBuffer();
        StringBuffer externalLinksIds = new StringBuffer();
        StringBuffer extrinsicObjectsIds = new StringBuffer();
        StringBuffer federationsIds = new StringBuffer();
        StringBuffer organizationsIds = new StringBuffer();
        StringBuffer registrysIds = new StringBuffer();
        StringBuffer packagesIds = new StringBuffer();
        StringBuffer serviceBindingsIds = new StringBuffer();
        StringBuffer servicesIds = new StringBuffer();
        StringBuffer specificationLinksIds = new StringBuffer();
        StringBuffer subscriptionsIds = new StringBuffer();
        StringBuffer usersIds = new StringBuffer();
        StringBuffer personsIds = new StringBuffer();

        Statement stmt = null;

        try {
            stmt = context.getConnection().createStatement();

            getRegistryObjectsIdsFromResultSet(rs, startIndex, maxResults,
                    adhocQuerysIds, associationsIds,
                    auditableEventsIds, classificationsIds, schemesIds,
                    classificationNodesIds, externalIdsIds, externalLinksIds,
                    extrinsicObjectsIds, federationsIds, organizationsIds, registrysIds, packagesIds,
                    serviceBindingsIds, servicesIds, specificationLinksIds, subscriptionsIds, usersIds, personsIds);

            ResultSet leafObjectsRs = null;

            if (adhocQuerysIds.length() > 0) {
                AdhocQueryDAO ahqDAO = new AdhocQueryDAO(context);
                sql = "SELECT * FROM " + ahqDAO.getTableName() +
                        " WHERE id IN (" + adhocQuerysIds + ")";

                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(ahqDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (associationsIds.length() > 0) {
                AssociationDAO assDAO = new AssociationDAO(context);
                sql = "SELECT * FROM " + assDAO.getTableName() +
                        " WHERE id IN (" + associationsIds + ")";

                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(assDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (auditableEventsIds.length() > 0) {
                AuditableEventDAO aeDAO = new AuditableEventDAO(context);
                sql = "SELECT * FROM " + aeDAO.getTableName() +
                        " WHERE id IN (" + auditableEventsIds + ")";
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(aeDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (classificationsIds.length() > 0) {
                ClassificationDAO classificationDAO = new ClassificationDAO(context);
                sql = "SELECT * FROM " + classificationDAO.getTableName() +
                        " WHERE id IN (" + classificationsIds + ")";
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(classificationDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (classificationNodesIds.length() > 0) {
                ClassificationNodeDAO nodeDAO = new ClassificationNodeDAO(context);
                sql = "SELECT * FROM " + nodeDAO.getTableName() +
                        " WHERE id IN (" + classificationNodesIds + ")";
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(nodeDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (schemesIds.length() > 0) {
                ClassificationSchemeDAO schemeDAO = new ClassificationSchemeDAO(context);
                sql = "SELECT * FROM " + schemeDAO.getTableName() +
                        " WHERE id IN (" + schemesIds + ")";
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(schemeDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (externalIdsIds.length() > 0) {
                ExternalIdentifierDAO externalIdDAO = new ExternalIdentifierDAO(context);
                sql = "SELECT * FROM " + externalIdDAO.getTableName() +
                        " WHERE id IN (" + externalIdsIds + ")";
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(externalIdDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (externalLinksIds.length() > 0) {
                ExternalLinkDAO externalLinkDAO = new ExternalLinkDAO(context);
                sql = "SELECT * FROM " + externalLinkDAO.getTableName() +
                        " WHERE id IN (" + externalLinksIds + ")";
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(externalLinkDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (extrinsicObjectsIds.length() > 0) {
                ExtrinsicObjectDAO extrinsicObjectDAO = new ExtrinsicObjectDAO(context);
                sql = "SELECT * FROM " + extrinsicObjectDAO.getTableName() +
                        " WHERE id IN (" + extrinsicObjectsIds + ")";
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(extrinsicObjectDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (federationsIds.length() > 0) {
                FederationDAO fedDAO = new FederationDAO(context);
                sql = "SELECT * FROM " + fedDAO.getTableName() +
                        " WHERE id IN (" + federationsIds + ")";

                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(fedDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (organizationsIds.length() > 0) {
                OrganizationDAO organizationDAO = new OrganizationDAO(context);
                sql = "SELECT * FROM " + organizationDAO.getTableName() +
                        " WHERE id IN (" + organizationsIds + ")";
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(organizationDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (personsIds.length() > 0) {
                PersonDAO personDAO = new PersonDAO(context);
                sql = "SELECT * FROM " + personDAO.getTableName() +
                        " WHERE id IN (" + personsIds + ")";
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(personDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (registrysIds.length() > 0) {
                RegistryDAO regDAO = new RegistryDAO(context);
                sql = "SELECT * FROM " + regDAO.getTableName() +
                        " WHERE id IN (" + registrysIds + ")";

                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(regDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (packagesIds.length() > 0) {
                RegistryPackageDAO pkgDAO = new RegistryPackageDAO(context);
                sql = "SELECT * FROM " + pkgDAO.getTableName() +
                        " WHERE id IN (" + packagesIds + ")";
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(pkgDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (serviceBindingsIds.length() > 0) {
                ServiceBindingDAO serviceBindingDAO = new ServiceBindingDAO(context);
                sql = "SELECT * FROM " + serviceBindingDAO.getTableName() +
                        " WHERE id IN (" + serviceBindingsIds + ")";
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(serviceBindingDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (servicesIds.length() > 0) {
                ServiceDAO serviceDAO = new ServiceDAO(context);
                sql = "SELECT * FROM " + serviceDAO.getTableName() +
                        " WHERE id IN (" + servicesIds + ")";
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(serviceDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (specificationLinksIds.length() > 0) {
                SpecificationLinkDAO specLinkDAO = new SpecificationLinkDAO(context);
                sql = "SELECT * FROM " + specLinkDAO.getTableName() +
                        " WHERE id IN (" + specificationLinksIds + ")";
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(specLinkDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (subscriptionsIds.length() > 0) {
                SubscriptionDAO subDAO = new SubscriptionDAO(context);
                sql = "SELECT * FROM " + subDAO.getTableName() +
                        " WHERE id IN (" + subscriptionsIds + ")";

                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(subDAO.getObjects(leafObjectsRs, 0, -1));
            }

            if (usersIds.length() > 0) {
                UserDAO userDAO = new UserDAO(context);
                sql = "SELECT * FROM " + userDAO.getTableName() +
                        " WHERE id IN (" + usersIds + ")";
                log.trace("SQL = " + sql);  // HIEOS/BHT: (DEBUG)
                leafObjectsRs = stmt.executeQuery(sql);
                res.addAll(userDAO.getObjects(leafObjectsRs, 0, -1));
            }

        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException"), e);
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }

        return res;
    }

    /**
     * Get a HashMap with registry object id as key and owner id as value
     */
    public HashMap getOwnersMap(List ids)
            throws RegistryException {
        Statement stmt = null;
        List resultSets = null;
        HashMap ownersMap = new HashMap();

        final String prefixPred = "SELECT ao.id, ae.user_ FROM AuditableEvent ae, AffectedObject ao WHERE ao.eventId = ae.id";
        final String suffixPred = " AND (ae.eventType = '" + BindingUtility.CANONICAL_EVENT_TYPE_ID_Created +
                "' OR ae.eventType = '" + BindingUtility.CANONICAL_EVENT_TYPE_ID_Versioned +
                "' OR ae.eventType = '" + BindingUtility.CANONICAL_EVENT_TYPE_ID_Relocated + "') ORDER BY ae.timeStamp_ ASC ";

        if (ids.size() == 0) {
            return ownersMap;
        }

        try {

            if (ids.size() == 1) {
                //Optmization for 1 term case
                stmt = context.getConnection().createStatement();
                StringBuffer query = new StringBuffer(prefixPred);
                query.append(" AND ao.id = '").append(ids.get(0)).append("'");
                query.append(suffixPred);
                log.trace("SQL = " + query.toString());  // HIEOS/BHT: (DEBUG)
                ResultSet rs = stmt.executeQuery(query.toString());

                while (rs.next()) {
                    ownersMap.put(rs.getString(1), rs.getString(2));
                }
            } else {
                //This will handle unlimited terms using buffered Selects
                StringBuffer query = new StringBuffer(prefixPred);
                query.append(" AND ao.id IN ( $InClauseTerms ) ");
                query.append(suffixPred);

                resultSets = executeBufferedSelectWithINClause(query.toString(), ids, inClauseTermLimit);
                Iterator resultsSetsIter = resultSets.iterator();
                while (resultsSetsIter.hasNext()) {
                    ResultSet rs = (ResultSet) resultsSetsIter.next();
                    while (rs.next()) {
                        ownersMap.put(rs.getString(1), rs.getString(2));
                    }
                }
            }

            return ownersMap;
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException"), e);
            throw new RegistryException(e);
        } finally {
            if (stmt != null) {
                closeStatement(stmt);
            }

            if (resultSets != null) {
                Iterator resultsSetsIter = resultSets.iterator();
                while (resultsSetsIter.hasNext()) {
                    try {
                        ResultSet rs = (ResultSet) resultsSetsIter.next();
                        Statement stmt2 = rs.getStatement();
                        closeStatement(stmt2);
                    } catch (SQLException e) {
                        log.error(e, e);
                    }
                }
            }
        }
    }

    /**
     * Update the status of specified objects (homogenous collection) to the specified status.
     * @param statusUnchanged if an id in registryObjectIds is in this ArrayList, no AuditableEvent
     * generated for that RegistryObject
     */
    public void updateStatus(RegistryObjectType ro, String status)
            throws RegistryException {
        Statement stmt = null;

        // HIEOS/BHT/AMS: Changed to also update status in the RegistryObject table.
        try {
            stmt = context.getConnection().createStatement();
            String registryObjectTableName = getTableName();
            // First update the concrete table (e.g. ExtrinsicObject, RegistryPackage).
            String sql = this.getSQLStatementFragmentForStatusUpdate(registryObjectTableName, status, ro.getId());
            log.trace("SQL = " + sql);
            stmt.addBatch(sql);

            // Now, update the RegistryObject table (if not already updated above).
            if (!registryObjectTableName.equals(RegistryObjectDAO.getTableNameStatic())) {
                sql = this.getSQLStatementFragmentForStatusUpdate(RegistryObjectDAO.getTableNameStatic(), status, ro.getId());
                log.trace("SQL = " + sql);
                stmt.addBatch(sql);
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException"), e);
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }
    }

    // HIEOS/BHT/AMS: Added new method to support status update optimization.
    /**
     *
     * @param tableName
     * @return
     */
    private String getSQLStatementFragmentForStatusUpdate(String tableName, String status, String id) {
        return "UPDATE " + tableName + " SET status = '" + status + "' WHERE id = '" + id + "'";
    }
}
