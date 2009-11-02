/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/SQLPersistenceManagerImpl.java,v 1.88 2007/06/06 14:40:15 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.registry.RegistryException;
import javax.xml.registry.JAXRException;
import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import org.freebxml.omar.common.CommonResourceBundle;
import org.freebxml.omar.common.exceptions.ObjectNotFoundException;
import org.freebxml.omar.common.exceptions.ReferencesExistException;
import org.freebxml.omar.server.cache.ServerCache;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.freebxml.omar.common.IterativeQueryParams;
import org.freebxml.omar.server.security.authentication.AuthenticationServiceImpl;
import org.oasis.ebxml.registry.bindings.lcm.ApproveObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.DeprecateObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.SetStatusOnObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.UndeprecateObjectsRequest;
import org.oasis.ebxml.registry.bindings.query.ResponseOption;
import org.oasis.ebxml.registry.bindings.query.ResponseOptionType;
import org.oasis.ebxml.registry.bindings.query.ReturnType;
import org.oasis.ebxml.registry.bindings.rim.ClassificationNodeType;
import org.oasis.ebxml.registry.bindings.rim.IdentifiableType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefListType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefType;
import org.oasis.ebxml.registry.bindings.rim.PersonType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.RegistryType;
import org.oasis.ebxml.registry.bindings.rim.UserType;

/**
 * Class Declaration for SQLPersistenceManagerImpl.
 * @see
 * @author Farrukh S. Najmi
 * @author Adrian Chong
 */
public class SQLPersistenceManagerImpl
        implements org.freebxml.omar.server.persistence.PersistenceManager {

    /**
     * @link
     * @shapeType PatternLink
     * @pattern Singleton
     * @supplierRole Singleton factory
     */

    /*# private SQLPersistenceManagerImpl _sqlPersistenceManagerImpl; */
    private static SQLPersistenceManagerImpl instance = null;
    private static final Log log = LogFactory.getLog(SQLPersistenceManagerImpl.class);
    private BindingUtility bu = BindingUtility.getInstance();
    private int numConnectionsOpen = 0;
    /**
     *
     * @associates <{org.freebxml.omar.server.persistence.rdb.ExtrinsicObjectDAO}>
     */
    String databaseURL = null;
    java.sql.DatabaseMetaData metaData = null;
    private String driver;
    private String user;
    private String password;
    private boolean useConnectionPool;
    private boolean dumpStackOnQuery;
    private boolean skipReferenceCheckOnRemove;
    private ConnectionPool connectionPool;
    private int transactionIsolation;
    private DataSource ds = null;

    private SQLPersistenceManagerImpl() {
        loadUsernamePassword();
        constructDatabaseURL();

        // define transaction isolation
        if ("TRANSACTION_READ_COMMITTED".equalsIgnoreCase(RegistryProperties.getInstance().getProperty("omar.persistence.rdb.transactionIsolation"))) {
            transactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
        } else {
            transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED;
        }

        useConnectionPool = Boolean.valueOf(RegistryProperties.getInstance().getProperty("omar.persistence.rdb.useConnectionPooling", "true")).booleanValue();
        skipReferenceCheckOnRemove = Boolean.valueOf(RegistryProperties.getInstance().getProperty("omar.persistence.rdb.skipReferenceCheckOnRemove", "false")).booleanValue();
        dumpStackOnQuery = Boolean.valueOf(RegistryProperties.getInstance().getProperty("omar.persistence.rdb.dumpStackOnQuery", "false")).booleanValue();
        boolean debugConnectionPool = Boolean.valueOf(RegistryProperties.getInstance().getProperty("omar.persistence.rdb.pool.debug", "false")).booleanValue();


        //Create JNDI context
        if (useConnectionPool) {
            if (!debugConnectionPool) {
                // Use Container's connection pooling
                String omarName = RegistryProperties.getInstance().getProperty("omar.name", "omar");
                String envName = "java:comp/env";
                String dataSourceName = "jdbc/" + omarName + "-registry";
                Context ctx = null;

                try {
                    ctx = new InitialContext();
                    if (null == ctx) {
                        log.info(ServerResourceBundle.getInstance().
                                getString("message.UnableToGetInitialContext"));
                    }
                } catch (NamingException e) {
                    log.info(ServerResourceBundle.getInstance().
                            getString("message.UnableToGetInitialContext"), e);
                    ctx = null;
                }

                /* HIEOS/BHT: DISABLED
                if (null != ctx) {
                try {
                ctx = (Context)ctx.lookup(envName);
                if (null == ctx) {
                log.info(ServerResourceBundle.getInstance().
                getString("message.UnableToGetJNDIContextForDataSource",
                new Object[]{envName}));
                }
                } catch (NamingException e) {
                log.info(ServerResourceBundle.getInstance().
                getString("message.UnableToGetJNDIContextForDataSource",
                new Object[]{envName}), e);
                ctx = null;
                }
                }
                 */

                if (null != ctx) {
                    try {
                        ds = (DataSource) ctx.lookup(dataSourceName);
                        if (null == ds) {
                            log.info(ServerResourceBundle.getInstance().
                                    getString("message.UnableToGetJNDIContextForDataSource",
                                    new Object[]{envName + "/" + dataSourceName}));
                        }
                    } catch (NamingException e) {
                        log.info(ServerResourceBundle.getInstance().
                                getString("message.UnableToGetJNDIContextForDataSource",
                                new Object[]{envName + "/" + dataSourceName}),
                                e);
                        ds = null;
                    }
                }

                if (null != ds) {
                    // Create a test connection to make sure all is well with DataSource
                    Connection connection = null;
                    try {
                        connection = ds.getConnection();
                    } catch (Exception e) {
                        log.info(ServerResourceBundle.getInstance().
                                getString("message.UnableToCreateTestConnectionForDataSource",
                                new Object[]{envName + "/" + dataSourceName}), e);
                        ds = null;
                    } finally {
                        if (connection != null) {
                            try {
                                connection.close();
                            } catch (Exception e1) {
                                //Do nothing.
                            }
                        }
                    }
                }
            }

            if (ds == null) {
                // No DataSource available so create our own ConnectionPool
                loadDatabaseDriver();
                createConnectionPool();
            }
        } else {
            loadDatabaseDriver();
        }
    }

    /** Look up the driver name and load the database driver */
    private void loadDatabaseDriver() {
        try {
            driver = RegistryProperties.getInstance().getProperty("omar.persistence.rdb.databaseDriver");
            Class.forName(driver);

            log.debug("Loaded jdbc driver: " + driver);
        } catch (ClassNotFoundException e) {
            log.error(e);
        }
    }

    /** Lookup up the db URL fragments and form the complete URL */
    private void constructDatabaseURL() {
        databaseURL = RegistryProperties.getInstance().getProperty("omar.persistence.rdb.databaseURL");

        log.info(ServerResourceBundle.getInstance().getString("message.dbURLEquals", new Object[]{databaseURL}));
    }

    /**Load the username and password for database access*/
    private void loadUsernamePassword() {
        user = RegistryProperties.getInstance().getProperty("omar.persistence.rdb.databaseUser");
        password = RegistryProperties.getInstance().getProperty("omar.persistence.rdb.databaseUserPassword");
    }

    private void createConnectionPool() {
        try {
            RegistryProperties registryProperties = RegistryProperties.getInstance();
            String initialSize = registryProperties.getProperty(
                    "omar.persistence.rdb.pool.initialSize");
            int initConns = 1;

            if (initialSize != null) {
                initConns = Integer.parseInt(initialSize);
            }

            String maxSize = registryProperties.getProperty(
                    "omar.persistence.rdb.pool.maxSize");
            int maxConns = 1;

            if (maxSize != null) {
                maxConns = Integer.parseInt(maxSize);
            }

            String connectionTimeOut = registryProperties.getProperty(
                    "omar.persistence.rdb.pool.connectionTimeOut");
            int timeOut = 0;

            if (connectionTimeOut != null) {
                timeOut = Integer.parseInt(connectionTimeOut);
            }

            connectionPool = new ConnectionPool("ConnectionPool", databaseURL,
                    user, password, maxConns, initConns, timeOut, transactionIsolation);
        } catch (java.lang.reflect.UndeclaredThrowableException t) {
            log.error(ServerResourceBundle.getInstance().getString("message.FailedToCreateConnectionPool",
                    new Object[]{t.getClass().getName(), t.getMessage()}), t);
            throw t;
        }
    }

    /**
     * Get a database connection. The connection is of autocommit off and with
     * transaction isolation level "transaction read committed"
     */
    public Connection getConnection(ServerRequestContext context) throws RegistryException {
        Connection connection = null;
        if (log.isTraceEnabled()) {
            log.debug("SQLPersistenceManagerImpl.getConnection");
            numConnectionsOpen++;
        }
        try {
            if (useConnectionPool) {
                if (ds != null) {
                    connection = ds.getConnection();
                    if (connection == null) {
                        log.info(ServerResourceBundle.getInstance().getString("message.ErrorUnableToOpenDbConnctionForDataSource=", new Object[]{ds}));
                    }
                }

                if (connection == null) {
                    //Default to registry server ConnectionPool
                    connection = connectionPool.getConnection(context.getId());
                }
                connection.setTransactionIsolation(transactionIsolation);
                connection.setAutoCommit(false);
            } else {
                // create connection directly
                if ((user != null) && (user.length() > 0)) {
                    connection = java.sql.DriverManager.getConnection(databaseURL,
                            user, password);
                } else {
                    connection = java.sql.DriverManager.getConnection(databaseURL);
                }
                // Set Transaction Isolation and AutoComit
                // WARNING: till present Oracle dirvers (9.2.0.5) do not accept
                // setTransactionIsolation being called after setAutoCommit(false)
                connection.setTransactionIsolation(transactionIsolation);
                connection.setAutoCommit(false);
            }
        } catch (SQLException e) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.connectToDatabaseFailed"), e);
        }

        return connection;
    }

    public void releaseConnection(ServerRequestContext context, Connection connection) throws RegistryException {
        if (log.isTraceEnabled()) {
            log.debug("SQLPersistenceManagerImpl.releaseConnection");
            numConnectionsOpen--;
            log.debug("Number of connections open:" + numConnectionsOpen);
        }
        try {
            if (connection != null) {
                if (!connection.isClosed() && ((!useConnectionPool) || (ds != null))) {
                    connection.close();
                } else if (useConnectionPool) {
                    connectionPool.freeConnection(connection);
                }
            }
        } catch (Exception e) {
            throw new RegistryException(e);
        }
    }

    public synchronized static SQLPersistenceManagerImpl getInstance() {
        if (instance == null) {
            instance = new SQLPersistenceManagerImpl();
        }

        return instance;
    }

    //Sort objects by their type.
    private void sortRegistryObjects(List registryObjects, List associations,
            List auditableEvents, List classifications, List schemes,
            List classificationNodes, List externalIds, List externalLinks,
            List extrinsicObjects,
            List federations,
            List objectRefs, List organizations, List packages,
            List serviceBindings, List services, List specificationLinks,
            List adhocQuerys,
            List subscriptions,
            List users,
            List persons,
            List registrys)
            throws RegistryException {
        associations.clear();
        auditableEvents.clear();
        classifications.clear();
        schemes.clear();
        classificationNodes.clear();
        externalIds.clear();
        externalLinks.clear();
        extrinsicObjects.clear();
        federations.clear();
        objectRefs.clear();
        organizations.clear();
        packages.clear();
        serviceBindings.clear();
        services.clear();
        specificationLinks.clear();
        adhocQuerys.clear();
        subscriptions.clear();
        users.clear();
        persons.clear();
        registrys.clear();

        java.util.Iterator objIter = registryObjects.iterator();

        while (objIter.hasNext()) {
            IdentifiableType obj = (IdentifiableType) objIter.next();

            if (obj instanceof org.oasis.ebxml.registry.bindings.rim.AssociationType1) {
                associations.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.AuditableEventType) {
                auditableEvents.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.ClassificationType) {
                classifications.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.ClassificationSchemeType) {
                schemes.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.ClassificationNodeType) {
                classificationNodes.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.ExternalIdentifierType) {
                externalIds.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.ExternalLinkType) {
                externalLinks.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.ExtrinsicObjectType) {
                extrinsicObjects.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.FederationType) {
                federations.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.ObjectRefType) {
                objectRefs.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.OrganizationType) {
                organizations.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.RegistryType) {
                registrys.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.RegistryPackageType) {
                packages.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.ServiceBindingType) {
                serviceBindings.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.ServiceType) {
                services.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.AdhocQueryType) {
                adhocQuerys.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.query.FilterQueryType) {
                adhocQuerys.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.SpecificationLinkType) {
                specificationLinks.add(obj);
            } else if (obj instanceof org.oasis.ebxml.registry.bindings.rim.SubscriptionType) {
                subscriptions.add(obj);
            } else if (obj instanceof UserType) {
                users.add(obj);
            } else if (obj instanceof PersonType) {
                persons.add(obj);
            } else if (obj instanceof RegistryType) {
                registrys.add(obj);
            } else {
                throw new RegistryException(CommonResourceBundle.getInstance().getString("message.unexpectedObjectType",
                        new Object[]{obj.getClass().getName(), "org.oasis.ebxml.registry.bindings.rim.IdentifiableType"}));
            }
        }
    }

    /**
     * Does a bulk insert of a heterogeneous Collection of RegistrObjects.
     *
     */
    public void insert(ServerRequestContext context, List registryObjects)
            throws RegistryException {

        List associations = new java.util.ArrayList();
        List auditableEvents = new java.util.ArrayList();
        List classifications = new java.util.ArrayList();
        List schemes = new java.util.ArrayList();
        List classificationNodes = new java.util.ArrayList();
        List externalIds = new java.util.ArrayList();
        List externalLinks = new java.util.ArrayList();
        List extrinsicObjects = new java.util.ArrayList();
        List federations = new java.util.ArrayList();
        List objectRefs = new java.util.ArrayList();
        List organizations = new java.util.ArrayList();
        List packages = new java.util.ArrayList();
        List serviceBindings = new java.util.ArrayList();
        List services = new java.util.ArrayList();
        List specificationLinks = new java.util.ArrayList();
        List adhocQuerys = new java.util.ArrayList();
        List subscriptions = new java.util.ArrayList();
        List users = new java.util.ArrayList();
        List persons = new java.util.ArrayList();
        List registrys = new java.util.ArrayList();

        sortRegistryObjects(registryObjects, associations, auditableEvents,
                classifications, schemes, classificationNodes, externalIds,
                externalLinks, extrinsicObjects, federations, objectRefs, organizations, packages,
                serviceBindings, services, specificationLinks, adhocQuerys, subscriptions, users, persons, registrys);


        if (auditableEvents.size() > 0) {
            AuditableEventDAO auditableEventDAO = new AuditableEventDAO(context);
            auditableEventDAO.insert(auditableEvents);
        }

        if (associations.size() > 0) {
            AssociationDAO associationDAO = new AssociationDAO(context);
            associationDAO.insert(associations);
        }

        if (classifications.size() > 0) {
            ClassificationDAO classificationDAO = new ClassificationDAO(context);
            classificationDAO.insert(classifications);
        }

        if (schemes.size() > 0) {
            ClassificationSchemeDAO classificationSchemeDAO = new ClassificationSchemeDAO(context);
            classificationSchemeDAO.insert(schemes);
        }

        if (classificationNodes.size() > 0) {
            ClassificationNodeDAO classificationNodeDAO = new ClassificationNodeDAO(context);
            classificationNodeDAO.insert(classificationNodes);
        }

        if (externalIds.size() > 0) {
            ExternalIdentifierDAO externalIdentifierDAO = new ExternalIdentifierDAO(context);
            externalIdentifierDAO.insert(externalIds);
        }

        if (externalLinks.size() > 0) {
            ExternalLinkDAO externalLinkDAO = new ExternalLinkDAO(context);
            externalLinkDAO.insert(externalLinks);
        }

        if (extrinsicObjects.size() > 0) {
            ExtrinsicObjectDAO extrinsicObjectDAO = new ExtrinsicObjectDAO(context);
            extrinsicObjectDAO.insert(extrinsicObjects);
        }

        if (federations.size() > 0) {
            FederationDAO federationDAO = new FederationDAO(context);
            federationDAO.insert(federations);
        }

        if (registrys.size() > 0) {
            RegistryDAO registryDAO = new RegistryDAO(context);
            registryDAO.insert(registrys);
        }

        if (objectRefs.size() > 0) {
            ObjectRefDAO objectRefDAO = new ObjectRefDAO(context);
            objectRefDAO.insert(objectRefs);
        }

        if (organizations.size() > 0) {
            OrganizationDAO organizationDAO = new OrganizationDAO(context);
            organizationDAO.insert(organizations);
        }

        if (packages.size() > 0) {
            RegistryPackageDAO registryPackageDAO = new RegistryPackageDAO(context);
            registryPackageDAO.insert(packages);
        }

        if (serviceBindings.size() > 0) {
            ServiceBindingDAO serviceBindingDAO = new ServiceBindingDAO(context);
            serviceBindingDAO.insert(serviceBindings);
        }

        if (services.size() > 0) {
            ServiceDAO serviceDAO = new ServiceDAO(context);
            serviceDAO.insert(services);
        }

        if (specificationLinks.size() > 0) {
            SpecificationLinkDAO specificationLinkDAO = new SpecificationLinkDAO(context);
            specificationLinkDAO.insert(specificationLinks);
        }

        if (adhocQuerys.size() > 0) {
            AdhocQueryDAO adhocQueryDAO = new AdhocQueryDAO(context);
            adhocQueryDAO.insert(adhocQuerys);
        }

        if (subscriptions.size() > 0) {
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO(context);
            subscriptionDAO.insert(subscriptions);
        }

        if (users.size() > 0) {
            UserDAO userDAO = new UserDAO(context);
            userDAO.insert(users);
        }

        if (persons.size() > 0) {
            PersonDAO personDAO = new PersonDAO(context);
            personDAO.insert(persons);
        }

        if (registrys.size() > 0) {
            RegistryDAO registryDAO = new RegistryDAO(context);
            registryDAO.insert(registrys);
        }

    }

    /**
     * Does a bulk update of a heterogeneous Collection of RegistrObjects.
     *
     */
    public void update(ServerRequestContext context, List registryObjects)
            throws RegistryException {


        List associations = new java.util.ArrayList();
        List auditableEvents = new java.util.ArrayList();
        List classifications = new java.util.ArrayList();
        List schemes = new java.util.ArrayList();
        List classificationNodes = new java.util.ArrayList();
        List externalIds = new java.util.ArrayList();
        List externalLinks = new java.util.ArrayList();
        List extrinsicObjects = new java.util.ArrayList();
        List federations = new java.util.ArrayList();
        List objectRefs = new java.util.ArrayList();
        List organizations = new java.util.ArrayList();
        List packages = new java.util.ArrayList();
        List serviceBindings = new java.util.ArrayList();
        List services = new java.util.ArrayList();
        List specificationLinks = new java.util.ArrayList();
        List adhocQuerys = new java.util.ArrayList();
        List subscriptions = new java.util.ArrayList();
        List users = new java.util.ArrayList();
        List persons = new java.util.ArrayList();
        List registrys = new java.util.ArrayList();

        sortRegistryObjects(registryObjects, associations, auditableEvents,
                classifications, schemes, classificationNodes, externalIds,
                externalLinks, extrinsicObjects, federations, objectRefs, organizations, packages,
                serviceBindings, services, specificationLinks, adhocQuerys, subscriptions, users, persons, registrys);

        if (associations.size() > 0) {
            AssociationDAO associationDAO = new AssociationDAO(context);
            associationDAO.update(associations);
        }

        if (classifications.size() > 0) {
            ClassificationDAO classificationDAO = new ClassificationDAO(context);
            classificationDAO.update(classifications);
        }

        if (schemes.size() > 0) {
            ClassificationSchemeDAO classificationSchemeDAO = new ClassificationSchemeDAO(context);
            classificationSchemeDAO.update(schemes);
        }

        if (classificationNodes.size() > 0) {
            ClassificationNodeDAO classificationNodeDAO = new ClassificationNodeDAO(context);
            classificationNodeDAO.update(classificationNodes);
        }

        if (externalIds.size() > 0) {
            // ExternalId is no longer the first level, right?
            ExternalIdentifierDAO externalIdentifierDAO = new ExternalIdentifierDAO(context);
            externalIdentifierDAO.update(externalIds);
        }

        if (externalLinks.size() > 0) {
            ExternalLinkDAO externalLinkDAO = new ExternalLinkDAO(context);
            externalLinkDAO.update(externalLinks);
        }

        if (extrinsicObjects.size() > 0) {
            ExtrinsicObjectDAO extrinsicObjectDAO = new ExtrinsicObjectDAO(context);
            extrinsicObjectDAO.update(extrinsicObjects);
        }

        if (objectRefs.size() > 0) {
            ObjectRefDAO objectRefDAO = new ObjectRefDAO(context);
            objectRefDAO.update(objectRefs);
        }

        if (organizations.size() > 0) {
            OrganizationDAO organizationDAO = new OrganizationDAO(context);
            organizationDAO.update(organizations);
        }

        if (packages.size() > 0) {
            RegistryPackageDAO registryPackageDAO = new RegistryPackageDAO(context);
            registryPackageDAO.update(packages);
        }

        if (serviceBindings.size() > 0) {
            ServiceBindingDAO serviceBindingDAO = new ServiceBindingDAO(context);
            serviceBindingDAO.update(serviceBindings);
        }

        if (services.size() > 0) {
            ServiceDAO serviceDAO = new ServiceDAO(context);
            serviceDAO.update(services);
        }

        if (specificationLinks.size() > 0) {
            SpecificationLinkDAO specificationLinkDAO = new SpecificationLinkDAO(context);
            specificationLinkDAO.update(specificationLinks);
        }

        if (adhocQuerys.size() > 0) {
            AdhocQueryDAO adhocQueryDAO = new AdhocQueryDAO(context);
            adhocQueryDAO.update(adhocQuerys);
        }

        if (subscriptions.size() > 0) {
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO(context);
            subscriptionDAO.update(subscriptions);
        }

        if (users.size() > 0) {
            UserDAO userDAO = new UserDAO(context);
            userDAO.update(users);
        }

        if (persons.size() > 0) {
            PersonDAO personDAO = new PersonDAO(context);
            personDAO.update(persons);
        }

        if (registrys.size() > 0) {
            RegistryDAO registryDAO = new RegistryDAO(context);
            registryDAO.insert(registrys);
        }

        //Special case handling for AuditableEvents as they can only be created 
        //by this class and not by clients of this class.

        //Ignore any client supplied AuditableEvents 
        auditableEvents.clear();

    }

    /**
     * Update the status of specified objects to the specified status.
     *
     */
    public void updateStatus(ServerRequestContext context, List registryObjectsIds,
            String status)
            throws RegistryException {
        try {
            //Make sure that status is a ref to a StatusType ClassificationNode
            context.checkClassificationNodeRefConstraint(status, bu.CANONICAL_CLASSIFICATION_SCHEME_ID_StatusType, "status");

            ObjectRefListType orefList = bu.rimFac.createObjectRefList();

            List refs = bu.getObjectRefsFromRegistryObjectIds(registryObjectsIds);
            Iterator iter = refs.iterator();
            while (iter.hasNext()) {
                ObjectRefType ref = (ObjectRefType) iter.next();
                // HIEOS/AMS/BHT: Removed next line of code (to speed up process).
                // RegistryObjectType ro = getRegistryObject(context, ref);
                // HIEOS/AMS/BHT: Now, calling new method (again, to speed up process).
                RegistryObjectType ro = getRegistryObjectForStatusUpdate(context, ref);
                RegistryObjectDAO roDAO = (RegistryObjectDAO) getDAOForObject(ro, context);
                roDAO.updateStatus(ro, status);
                orefList.getObjectRef().add(ref);
            }

            // repeat behaviour of DAO.prepareToXXX methdos for direct/indirect setStatus
            if (context.getCurrentRegistryRequest() instanceof ApproveObjectsRequest) {
                context.getApproveEvent().setAffectedObjects(orefList);
                context.addAffectedObjectsToAuditableEvent(context.getApproveEvent(), orefList);
            } else if (context.getCurrentRegistryRequest() instanceof DeprecateObjectsRequest) {
                context.getDeprecateEvent().setAffectedObjects(orefList);
                context.addAffectedObjectsToAuditableEvent(context.getDeprecateEvent(), orefList);
            } else if (context.getCurrentRegistryRequest() instanceof UndeprecateObjectsRequest) {
                context.getUnDeprecateEvent().setAffectedObjects(orefList);
                context.addAffectedObjectsToAuditableEvent(context.getUnDeprecateEvent(), orefList);
            } else if (context.getCurrentRegistryRequest() instanceof SetStatusOnObjectsRequest) {
                SetStatusOnObjectsRequest req = (SetStatusOnObjectsRequest) context.getCurrentRegistryRequest();
                context.getSetStatusEvent().setAffectedObjects(orefList);
                context.getSetStatusEvent().setEventType(req.getStatus());
                context.addAffectedObjectsToAuditableEvent(context.getSetStatusEvent(), orefList);
            }
        } catch (JAXBException e) {
            throw new RegistryException(e);
        } catch (JAXRException e) {
            throw new RegistryException(e);
        }
    }

    /**
     * Given a Binding object returns the OMARDAO for that object.
     *
     */
    private OMARDAO getDAOForObject(RegistryObjectType ro, ServerRequestContext context) throws RegistryException {
        OMARDAO dao = null;

        try {
            String bindingClassName = ro.getClass().getName();
            String daoClassName = bindingClassName.substring(bindingClassName.lastIndexOf('.') + 1, bindingClassName.length() - 4);

            //Construct the corresonding DAO instance using reflections
            Class daoClazz = Class.forName("org.freebxml.omar.server.persistence.rdb." + daoClassName + "DAO");

            Class[] conParameterTypes = new Class[1];
            conParameterTypes[0] = context.getClass();
            Object[] conParameterValues = new Object[1];
            conParameterValues[0] = context;
            Constructor[] cons = daoClazz.getDeclaredConstructors();

            //Find the constructor that takes RequestContext as its only arg
            Constructor con = null;
            for (int i = 0; i < cons.length; i++) {
                con = cons[i];
                if ((con.getParameterTypes().length == 1) && (con.getParameterTypes()[0] == conParameterTypes[0])) {
                    dao = (OMARDAO) con.newInstance(conParameterValues);
                    break;
                }
            }

        } catch (Exception e) {
            throw new RegistryException(e);
        }

        return dao;

    }

    /**
     * Does a bulk delete of a heterogeneous Collection of RegistrObjects. If
     * any RegistryObject cannot be found, it will make no change to the
     * database and throw RegistryException
     *
     */
    public void delete(ServerRequestContext context, List orefs)
            throws RegistryException {
        //Return if nothing specified to delete
        if (orefs.size() == 0) {
            return;
        }

        List idList = new ArrayList();
        idList.addAll(context.getObjectRefsMap().keySet());

        //First fetch the objects and then delete them
        String query = "SELECT * FROM RegistryObject ro WHERE ro.id IN ( " +
                bu.getIdListFromIds(idList) +
                " ) ";
        List objs = getRegistryObjectsMatchingQuery(context, query, null, "RegistryObject");
        List userAliases = null;
        Iterator iter = objs.iterator();
        while (iter.hasNext()) {
            RegistryObjectType ro = (RegistryObjectType) iter.next();
            if (ro instanceof UserType) {
                if (userAliases == null) {
                    userAliases = new ArrayList();
                }
                userAliases.add(((UserType) ro).getId());
            }
            OMARDAO dao = getDAOForObject(ro, context);

            //Now call delete method
            List objectsToDelete = new ArrayList();
            objectsToDelete.add(ro);
            dao.delete(objectsToDelete);
        }

        //Now delete from ObjectRef table
        ObjectRefDAO dao = new ObjectRefDAO(context);
        dao.delete(orefs);
        //Now, if any of the deleted ROs were of UserType, delete the credentials
        //from the server keystore
        if (userAliases != null) {
            Iterator aliasItr = userAliases.iterator();
            String alias = null;
            while (aliasItr.hasNext()) {
                try {
                    alias = (String) aliasItr.next();
                    AuthenticationServiceImpl.getInstance().deleteUserCertificate(alias);
                } catch (Throwable t) {
                    ServerResourceBundle.getInstance().getString("message.couldNotDeleteCredentials",
                            new Object[]{alias});
                }
            }
        }
    }

    private java.sql.DatabaseMetaData getMetaData(Connection connection)
            throws SQLException {
        if (metaData == null) {
            metaData = connection.getMetaData();
        }

        return metaData;
    }

    /**
     * Executes an SQL Query with default values for IterativeQueryParamHolder.
     */
    public List executeSQLQuery(ServerRequestContext context, String sqlQuery,
            ResponseOptionType responseOption, String tableName, List objectRefs)
            throws RegistryException {

        IterativeQueryParams paramHolder = new IterativeQueryParams(0, -1);
        return executeSQLQuery(context, sqlQuery, responseOption, tableName, objectRefs, paramHolder);
    }

    /**
     * Executes and SQL query using specified parameters.
     * This variant is used to invoke fixed queries without PreparedStatements.
     *
     * @return An List of RegistryObjectType instances
     */
    public List executeSQLQuery(ServerRequestContext context, String sqlQuery,
            ResponseOptionType responseOption, String tableName, List objectRefs,
            IterativeQueryParams paramHolder)
            throws RegistryException {

        return executeSQLQuery(context, sqlQuery, null, responseOption, tableName, objectRefs, paramHolder);
    }

    /**
     * Executes an SQL Query.
     */
    public List executeSQLQuery(ServerRequestContext context, String sqlQuery, List queryParams,
            ResponseOptionType responseOption, String tableName, List objectRefs)
            throws RegistryException {


        IterativeQueryParams paramHolder = new IterativeQueryParams(0, -1);
        return executeSQLQuery(context, sqlQuery, queryParams, responseOption, tableName, objectRefs, paramHolder);
    }

    /**
     * Executes an SQL Query.
     */
    public List executeSQLQuery(ServerRequestContext context, String sqlQuery, List queryParams,
            ResponseOptionType responseOption, String tableName, List objectRefs,
            IterativeQueryParams paramHolder)
            throws RegistryException {
        List res = null;
        Connection connection = null;
        int startIndex = paramHolder.startIndex;
        int maxResults = paramHolder.maxResults;
        int totalResultCount = -1;
        Statement stmt = null;

        try {
            connection = context.getConnection();

            java.sql.ResultSet rs = null;

            tableName = org.freebxml.omar.common.Utility.getInstance().mapTableName(tableName);

            ReturnType returnType = responseOption.getReturnType();
            boolean returnComposedObjects = responseOption.isReturnComposedObjects();

            if (maxResults < 0) {
                if (queryParams == null) {
                    stmt = connection.createStatement();
                } else {
                    stmt = connection.prepareStatement(sqlQuery);
                }
            } else {
                if (queryParams == null) {
                    stmt = connection.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
                } else {
                    stmt = connection.prepareStatement(sqlQuery, java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("Executing query: '" + sqlQuery + "'");
                if (dumpStackOnQuery) {
                    Thread.currentThread().dumpStack();
                }
            }
            log.trace("SQL = " + sqlQuery);  // HIEOS/BHT: (DEBUG)

            if (queryParams == null) {

                rs = stmt.executeQuery(sqlQuery);
            } else {
                Iterator iter = queryParams.iterator();
                int paramCount = 0;
                while (iter.hasNext()) {
                    Object param = iter.next();
                    ((PreparedStatement) stmt).setObject(++paramCount, param);
                    // HIEOS/BHT (DEBUG):
                    log.trace("  -> param(" + new Integer(paramCount).toString() + "): " + (String) param);
                }
                rs = ((PreparedStatement) stmt).executeQuery();
            }
            if (maxResults >= 0) {
                rs.last();
                totalResultCount = rs.getRow();
                // Reset back to before first row so that DAO can correctly scroll
                // through the result set
                rs.beforeFirst();
            }
            java.util.Iterator iter = null;

            if (returnType == ReturnType.OBJECT_REF) {
                res = new java.util.ArrayList();

                if (startIndex > 0) {
                    rs.last();
                    totalResultCount = rs.getRow();
                    rs.beforeFirst();
                    // calling rs.next() is a workaround for some drivers, such
                    // as Derby's, that do not set the cursor during call to 
                    // rs.relative(...)
                    rs.next();
                    boolean onRow = rs.relative(startIndex - 1);
                    // HIEOS/BHT (DEBUG):
                    log.trace(" -> Total Result Count: " + totalResultCount);
                }

                int cnt = 0;
                while (rs.next()) {
                    org.oasis.ebxml.registry.bindings.rim.ObjectRef or = bu.rimFac.createObjectRef();
                    String id = rs.getString(1);
                    or.setId(id);
                    res.add(or);

                    if (++cnt == maxResults) {
                        break;
                    }
                }
                // HIEOS/BHT (DEBUG):
                log.trace(" -> cnt: " + totalResultCount);
            } else if (returnType == ReturnType.REGISTRY_OBJECT) {
                context.setResponseOption(responseOption);
                RegistryObjectDAO roDAO = new RegistryObjectDAO(context);
                res = roDAO.getObjects(rs, startIndex, maxResults);
                // HIEOS/BHT (DEBUG):
                log.trace(" -> Object Size: " + res.size());
            } else if ((returnType == ReturnType.LEAF_CLASS) ||
                    (returnType == ReturnType.LEAF_CLASS_WITH_REPOSITORY_ITEM)) {
                res = getObjects(context, connection, rs, tableName, responseOption,
                        objectRefs, startIndex, maxResults);
                // HIEOS/BHT (DEBUG):
                log.trace(" -> Object Size: " + res.size());
            } else {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.invalidReturnType",
                        new Object[]{returnType}));
            }

        } catch (SQLException e) {
            throw new RegistryException(e);
        } catch (javax.xml.bind.JAXBException e) {
            throw new RegistryException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqle) {
                log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), sqle);
            }
        }

        paramHolder.totalResultCount = totalResultCount;

        return res;
    }

    private List getObjects(ServerRequestContext context, Connection connection, java.sql.ResultSet rs,
            String tableName, ResponseOptionType responseOption, List objectRefs,
            int startIndex, int maxResults)
            throws RegistryException {
        List res = null;

        context.setResponseOption(responseOption);

        if (tableName.equalsIgnoreCase(AdhocQueryDAO.getTableNameStatic())) {
            AdhocQueryDAO adhocQueryDAO = new AdhocQueryDAO(context);
            res = adhocQueryDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(AssociationDAO.getTableNameStatic())) {
            AssociationDAO associationDAO = new AssociationDAO(context);
            res = associationDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                AuditableEventDAO.getTableNameStatic())) {
            AuditableEventDAO auditableEventDAO = new AuditableEventDAO(context);
            res = auditableEventDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                ClassificationDAO.getTableNameStatic())) {
            ClassificationDAO classificationDAO = new ClassificationDAO(context);
            res = classificationDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                ClassificationSchemeDAO.getTableNameStatic())) {
            ClassificationSchemeDAO classificationSchemeDAO = new ClassificationSchemeDAO(context);
            res = classificationSchemeDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                ClassificationNodeDAO.getTableNameStatic())) {
            ClassificationNodeDAO classificationNodeDAO = new ClassificationNodeDAO(context);
            res = classificationNodeDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                ExternalIdentifierDAO.getTableNameStatic())) {
            ExternalIdentifierDAO externalIdentifierDAO = new ExternalIdentifierDAO(context);
            res = externalIdentifierDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                ExternalLinkDAO.getTableNameStatic())) {
            ExternalLinkDAO externalLinkDAO = new ExternalLinkDAO(context);
            res = externalLinkDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                ExtrinsicObjectDAO.getTableNameStatic())) {
            ExtrinsicObjectDAO extrinsicObjectDAO = new ExtrinsicObjectDAO(context);
            res = extrinsicObjectDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                FederationDAO.getTableNameStatic())) {
            FederationDAO federationDAO = new FederationDAO(context);
            res = federationDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                ObjectRefDAO.getTableNameStatic())) {
            ObjectRefDAO objectRefDAO = new ObjectRefDAO(context);
            res = objectRefDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                OrganizationDAO.getTableNameStatic())) {
            OrganizationDAO organizationDAO = new OrganizationDAO(context);
            res = organizationDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                RegistryObjectDAO.getTableNameStatic())) {
            RegistryObjectDAO registryObjectDAO = new RegistryObjectDAO(context);
            //TODO: Use reflection instead in future
            res = registryObjectDAO.getObjectsHetero(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                RegistryPackageDAO.getTableNameStatic())) {
            RegistryPackageDAO registryPackageDAO = new RegistryPackageDAO(context);
            res = registryPackageDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                ServiceBindingDAO.getTableNameStatic())) {
            ServiceBindingDAO serviceBindingDAO = new ServiceBindingDAO(context);
            res = serviceBindingDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(ServiceDAO.getTableNameStatic())) {
            ServiceDAO serviceDAO = new ServiceDAO(context);
            res = serviceDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                SpecificationLinkDAO.getTableNameStatic())) {
            SpecificationLinkDAO specificationLinkDAO = new SpecificationLinkDAO(context);
            res = specificationLinkDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                SubscriptionDAO.getTableNameStatic())) {
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO(context);
            res = subscriptionDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(UserDAO.getTableNameStatic())) {
            UserDAO userDAO = new UserDAO(context);
            res = userDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(PersonDAO.getTableNameStatic())) {
            PersonDAO personDAO = new PersonDAO(context);
            res = personDAO.getObjects(rs, startIndex, maxResults);
        } else if (tableName.equalsIgnoreCase(
                RegistryDAO.getTableNameStatic())) {
            RegistryDAO registryDAO = new RegistryDAO(context);
            res = registryDAO.getObjects(rs, startIndex, maxResults);
        }

        return res;
    }

    /**
     * Gets the specified objects using specified query and className
     *
     */
    public List getRegistryObjectsMatchingQuery(ServerRequestContext context, String query, List queryParams, String tableName)
            throws RegistryException {
        List objects = null;

        try {
            ResponseOption responseOption = bu.queryFac.createResponseOption();
            responseOption.setReturnType(ReturnType.LEAF_CLASS);
            responseOption.setReturnComposedObjects(true);

            objects = getIdentifiablesMatchingQuery(context, query, queryParams, tableName, responseOption);

        } catch (javax.xml.bind.JAXBException e) {
            throw new RegistryException(e);
        }

        return objects;
    }

    /**
     * Gets the specified objects using specified query and className
     *
     */
    public List getIdentifiablesMatchingQuery(ServerRequestContext context, String query, List queryParams, String tableName, ResponseOption responseOption)
            throws RegistryException {
        List objects = null;

        List objectRefs = new java.util.ArrayList();
        IterativeQueryParams paramHolder = new IterativeQueryParams(0, -1);
        objects = executeSQLQuery(context, query, queryParams, responseOption, tableName,
                objectRefs, paramHolder);
        return objects;
    }

    /**
     * Gets the first object matching specified query.
     * TODO: This is a dangerous query to use and it should eventually be
     *   eliminated.
     */
    public RegistryObjectType getRegistryObjectMatchingQuery(ServerRequestContext context, String query, List queryParams, String tableName)
            throws RegistryException {
        RegistryObjectType ro = null;

        List al = getRegistryObjectsMatchingQuery(context, query, queryParams, tableName);

        if (al.size() >= 1) {
            ro = (RegistryObjectType) al.get(0);
        }

        return ro;
    }

    /**
     * Gets the specified object using specified id and className
     *
     */
    public IdentifiableType getIdentifiableMatchingQuery(ServerRequestContext context, String query, List queryParams, String tableName, ResponseOption responseOption)
            throws RegistryException {
        IdentifiableType obj = null;

        List al = getIdentifiablesMatchingQuery(context, query, queryParams, tableName, responseOption);

        if (al.size() == 1) {
            obj = (IdentifiableType) al.get(0);
        }

        return obj;
    }

    // HEIOS/AMS/BHT Added new method to optimize status update operations.
    /**
     * Return a concrete RegistryObjectType (ExtrinsicObjectType or RegistryPackageType)
     * depending on the "objectType" found in the "RegistryObject" table.
     * 
     * @param context Holds the context for request processing.
     * @param ref Holds the object reference that we are interested in.
     * @return A concrete RegistryObjectType (ExtrinsicObjectType or RegistryPackageType).
     * @throws javax.xml.registry.RegistryException
     */
    public RegistryObjectType getRegistryObjectForStatusUpdate(ServerRequestContext context, ObjectRefType ref)
            throws RegistryException {
        Connection connection = context.getConnection();
        PreparedStatement stmt = null;
        try {
            // First get the type of registry object:
            String tableName = RegistryObjectDAO.getTableNameStatic();
            String sql = "SELECT objectType FROM " + tableName + " WHERE id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, ref.getId());
            log.trace("SQL = " + sql.toString());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            String objectType = rs.getString(1);

            // Now instantiate the proper concrete registry object type:
            RegistryObjectType concreteRegistryObject = null;
            if (BindingUtility.CANONICAL_OBJECT_TYPE_ID_RegistryPackage.equalsIgnoreCase(objectType)) {
                concreteRegistryObject = bu.rimFac.createRegistryPackage();
            } else {
                concreteRegistryObject = bu.rimFac.createExtrinsicObject();
            }
            concreteRegistryObject.setId(ref.getId());  // Just to be in sync.
            return concreteRegistryObject;
        } catch (SQLException e) {
            throw new RegistryException(e);
        } catch (JAXBException e) {
            throw new RegistryException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqle) {
                    log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), sqle);
                }
            }
        }
    }

    /**
     * Gets the specified object using specified id and className
     *
     */
    public RegistryObjectType getRegistryObject(ServerRequestContext context, String id, String className)
            throws RegistryException {
        RegistryObjectType ro = null;

        try {
            ResponseOption responseOption = bu.queryFac.createResponseOption();
            responseOption.setReturnType(ReturnType.LEAF_CLASS);
            responseOption.setReturnComposedObjects(true);
            ro = (RegistryObjectType) getIdentifiable(context, id, className, responseOption);
        } catch (JAXBException e) {
            throw new RegistryException(e);
        }

        return ro;
    }

    /**
     * Gets the specified object using specified id and className
     *
     */
    public IdentifiableType getIdentifiable(ServerRequestContext context, String id, String className, ResponseOption responseOption)
            throws RegistryException {
        IdentifiableType obj = null;

        String tableName = org.freebxml.omar.common.Utility.getInstance().mapTableName(className);
        String sqlQuery = "SELECT * FROM " + tableName + " WHERE id = ?";
        ArrayList queryParams = new ArrayList();
        queryParams.add(id);

        obj = getIdentifiableMatchingQuery(context, sqlQuery, queryParams, tableName, responseOption);

        return obj;
    }

    /**
     * Gets the specified object using specified ObjectRef
     *
     */
    public RegistryObjectType getRegistryObject(ServerRequestContext context, ObjectRefType ref)
            throws RegistryException {

        return getRegistryObject(context, ref.getId(), "RegistryObject");
    }

    /**
     * Get a HashMap with registry object id as key and owner id as value
     */
    public HashMap getOwnersMap(ServerRequestContext context, List ids) throws RegistryException {
        RegistryObjectDAO roDAO = new RegistryObjectDAO(context);

        HashMap ownersMap = roDAO.getOwnersMap(ids);

        return ownersMap;
    }

    /**
     * Sets the owner on the specified objects based upon RequestContext.
     */
    public void changeOwner(ServerRequestContext context, List objects) throws RegistryException {
        try {
            ObjectRefListType orefList = bu.rimFac.createObjectRefList();
            orefList.getObjectRef().addAll(bu.getObjectRefsFromRegistryObjects(objects));
            context.getRelocateEvent().setAffectedObjects(orefList);
        } catch (JAXBException e) {
            throw new RegistryException(e);
        } catch (JAXRException e) {
            throw new RegistryException(e);
        }
    }

    /**
     * Updates the idToLidMap in context entries with RegistryObject id as Key and RegistryObject lid as value 
     * for each object that matches specified id.
     *
     */
    public void updateIdToLidMap(ServerRequestContext context, Set ids, String tableName) throws RegistryException {
        if ((ids != null) && (ids.size() >= 0)) {

            Iterator iter = ids.iterator();
            Statement stmt = null;

            try {
                stmt = context.getConnection().createStatement();

                StringBuffer sql = new StringBuffer("SELECT id, lid FROM " + tableName + " WHERE id IN (");
                List existingIdList = new ArrayList();

                /* We need to count the number of item in "IN" list. 
                 * We need to split the a single SQL Strings if it is too long. Some database such as Oracle, 
                 * does not allow the IN list is too long
                 */
                int listCounter = 0;

                while (iter.hasNext()) {
                    String id = (String) iter.next();

                    if (iter.hasNext() && (listCounter < IdentifiableDAO.identifiableExistsBatchCount)) {
                        sql.append("'" + id + "',");
                    } else {
                        sql.append("'" + id + "')");

                        //log.info("sql string=" + sql.toString());
                        log.trace("SQL = " + sql.toString());
                        ResultSet rs = stmt.executeQuery(sql.toString());

                        while (rs.next()) {
                            String _id = rs.getString(1);
                            String lid = rs.getString(2);
                            context.getIdToLidMap().put(_id, lid);
                        }

                        sql = new StringBuffer("SELECT id, lid FROM " + tableName + " WHERE id IN (");
                        listCounter = 0;
                    }

                    listCounter++;
                }
            } catch (SQLException e) {
                throw new RegistryException(e);
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException sqle) {
                    log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), sqle);
                }
            }
        }
    }

    /**
     * Checks each object being deleted to make sure that it does not have any currently existing references.
     * Objects must be fetched from the Cache or Server and not from the RequestContext??
     *
     * @throws ReferencesExistException if references exist to any of the RegistryObject ids specified in roIds
     *
     */
    public void checkIfReferencesExist(ServerRequestContext context, List roIds) throws RegistryException {
        if (skipReferenceCheckOnRemove) {
            return;
        }

        Iterator iter = roIds.iterator();

        HashMap idToReferenceSourceMap = new HashMap();
        while (iter.hasNext()) {
            String id = (String) iter.next();


            StringBuffer query = new StringBuffer();
            query.append("SELECT id FROM RegistryObject WHERE objectType = ? UNION ");
            query.append("SELECT id FROM ClassificationNode WHERE parent = ? UNION ");
            query.append("SELECT id FROM Classification WHERE classificationNode = ? OR classificationScheme = ? OR classifiedObject = ? UNION ");
            query.append("SELECT id FROM ExternalIdentifier WHERE identificationScheme = ? OR registryObject = ? UNION ");
            query.append("SELECT id FROM Association WHERE associationType = ? OR sourceObject = ? OR targetObject= ?  UNION ");
            query.append("SELECT id FROM AuditableEvent WHERE user_ = ? OR requestId = ? UNION ");
            query.append("SELECT id FROM Organization WHERE parent = ? UNION ");
            query.append("SELECT id FROM Registry where operator = ? UNION ");
            query.append("SELECT id FROM ServiceBinding WHERE service = ? OR targetBinding = ? UNION ");
            query.append("SELECT id FROM SpecificationLink WHERE serviceBinding = ? OR specificationObject = ? UNION ");
            query.append("SELECT id FROM Subscription WHERE selector = ?  UNION ");
            query.append("SELECT s.parent FROM Slot s WHERE s.slotType = '" + bu.CANONICAL_DATA_TYPE_ID_ObjectRef + "' AND s.value = ?");

            PreparedStatement stmt = null;
            try {
                stmt = context.getConnection().prepareStatement(query.toString());
                stmt.setString(1, id);
                stmt.setString(2, id);
                stmt.setString(3, id);
                stmt.setString(4, id);
                stmt.setString(5, id);
                stmt.setString(6, id);
                stmt.setString(7, id);
                stmt.setString(8, id);
                stmt.setString(9, id);
                stmt.setString(10, id);
                stmt.setString(11, id);
                stmt.setString(12, id);
                stmt.setString(13, id);
                stmt.setString(14, id);
                stmt.setString(15, id);
                stmt.setString(16, id);
                stmt.setString(17, id);
                stmt.setString(18, id);
                stmt.setString(19, id);
                stmt.setString(20, id);
                log.trace("SQL = " + query.toString());  // HIEOS/BHT: (DEBUG)
                ResultSet rs = stmt.executeQuery();
                boolean result = false;

                ArrayList referenceSourceIds = new ArrayList();
                while (rs.next()) {
                    String referenceSourceId = rs.getString(1);
                    if (!roIds.contains(referenceSourceId)) {
                        referenceSourceIds.add(referenceSourceId);
                    }
                }

                if (!referenceSourceIds.isEmpty()) {
                    idToReferenceSourceMap.put(id, referenceSourceIds);
                }
            } catch (SQLException e) {
                throw new RegistryException(e);
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException sqle) {
                    log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), sqle);
                }
            }
        }

        if (!idToReferenceSourceMap.isEmpty()) {
            //At least one ref exists to at least one object so throw exception
            String msg = ServerResourceBundle.getInstance().getString("message.referencesExist");
            msg += "\n" + idToReferenceSourceMap.toString();

            throw new ReferencesExistException(msg);
        }
    }
}
