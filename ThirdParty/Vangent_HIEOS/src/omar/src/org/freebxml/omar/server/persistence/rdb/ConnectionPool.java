/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/ConnectionPool.java,v 1.16 2005/12/14 18:25:35 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.persistence.rdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.freebxml.omar.server.util.ServerResourceBundle;

/*
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/persistence/rdb/ConnectionPool.java,v 1.16 2005/12/14 18:25:35 farrukh_najmi Exp $
 *
*/
class ConnectionPool {
    private Log log = LogFactory.getLog(this.getClass());
    private String name;
    private String URL;
    private String user;
    private String password;
    private int transactionIsolation;
    private int initConns;
    private int maxConns;
    private int timeOut;
    private HashMap checkedOutConnections = new HashMap();
    private ArrayList freeConnections = new ArrayList();

    /**
    @param timeOut is the time in seconds after this has been elasped but the
    connection cannot be returned, getConnection() will return SQLException
    */
    public ConnectionPool(String name, String URL, String user,
        String password, int maxConns, int initConns, int timeOut, int transactionIsolation) {
        this.name = name;
        this.URL = URL;
        this.user = user;
        this.password = password;
        this.initConns = initConns;
        this.maxConns = maxConns;
        this.timeOut = (timeOut > 0) ? timeOut : 5;
        this.transactionIsolation = transactionIsolation;
        
        // initialise the pool
        for (int i = 0; i < initConns; i++) {
            try {
                Connection pc = newConnection();
                freeConnections.add(pc);
            } catch (SQLException e) {
                throw new java.lang.reflect.UndeclaredThrowableException(e,
                    ServerResourceBundle.
		       getInstance().getString("message.noConnectionAvailable",
			 new Object[]{ new Integer(freeConnections.size()),
				       new Integer(initConns)}));
            }
        }

        if (log.isInfoEnabled()) {
            log.info(ServerResourceBundle.getInstance().getString("message.DatabaseConnectionPoolingEnabled"));
            log.info(getStats());
        }
    }

    public Connection getConnection(String contextId) throws SQLException {
        try {
            //System.err.println("**********getConnection: contextId=" + contextId + " " + getStats());
            if ((maxConns < initConns) || (maxConns <= 0)) {
                throw new SQLException(
                    ServerResourceBundle.getInstance().getString("message.invalidSizeOfConnectionPool"));
            }

            if ((freeConnections.size() == 0) && (initConns != 0)) {
                // for some reasons the pool cannot be initialised
                throw new SQLException(
                    ServerResourceBundle.getInstance().getString("message.noConnectionAvailable",
                        new Object[]{new Integer(freeConnections.size()),new Integer(initConns)}));
            }

            Connection conn = getConnection(contextId, timeOut * 1000);

            return conn;
        } catch (SQLException e) {
            log.trace(getStats());
            throw e;
        }
    }

    private synchronized Connection getConnection(String contextId, long timeout)
        throws SQLException {
        // Get a pooled Connection from the cache or a new one.
        // Wait if all are checked out and the max limit has
        // been reached.
        long startTime = System.currentTimeMillis();
        long remaining = timeout;
        Connection conn = null;

        while ((conn = getPooledConnection(contextId)) == null) {
            try {
                wait(remaining);
            } catch (InterruptedException e) {
            }

            remaining = timeout - (System.currentTimeMillis() - startTime);

            if (remaining <= 0) {
                // Timeout has expired
                throw new SQLException(ServerResourceBundle.getInstance().getString("message.databaseConnectionTimedOut"));
            }
        }

        // Check if the Connection is still OK
        if (!isConnectionOK(conn)) {
            // It was bad. Try again with the remaining timeout
            return getConnection(contextId, remaining);
        }

        //Got a good connection
        checkedOutConnections.put(conn, contextId);

        return conn;
    }

    private boolean isConnectionOK(Connection connection) {
        Statement testStmt = null;

        try {
            if (!connection.isClosed()) {
                // Try to createStatement to see if it's really alive
                testStmt = connection.createStatement();
                testStmt.close();
            } else {
                return false;
            }
        } catch (SQLException e) {
            if (testStmt != null) {
                try {
                    testStmt.close();
                } catch (SQLException se) {
                }
            }

            return false;
        }

        return true;
    }

    private Connection getPooledConnection(String contextId) throws SQLException {
        Connection conn = null;

        if (freeConnections.size() > 0) {
            // Pick the first Connection in the Vector
            // to get round-robin usage
            conn = (Connection) freeConnections.remove(0);
        } else if (checkedOutConnections.size() < maxConns) {
            conn = newConnection();
        }

        return conn;
    }

    private Connection newConnection() throws SQLException {
        Connection conn = null;

        if ((user==null) || (user.length() == 0)) {
            conn = DriverManager.getConnection(URL);
        } else {
            conn = DriverManager.getConnection(URL, user, password);
        }

        // Set Transaction Isolation and AutoComit
        // WARNING: till present Oracle dirvers (9.2.0.5) do not accept
        // setTransactionIsolation being called after setAutoCommit(false)
        conn.setTransactionIsolation(transactionIsolation);
        conn.setAutoCommit(false);
        
        return conn;
    }

    public synchronized void freeConnection(Connection conn)
        throws SQLException {

        String contextId = (String)checkedOutConnections.get(conn);
        checkedOutConnections.remove(conn);
        //System.err.println("    **********freeConnection: contextId=" + contextId + " " + getStats());
        
        // Put the connection at the end of the Vector
        freeConnections.add(conn);
        notifyAll();
    }

    private String getStats() {
        return "Total connections: " + (freeConnections.size() + checkedOutConnections.size()) +
        " Available: " + freeConnections.size() + " Checked-out: " +
        checkedOutConnections.size() + " " + checkedOutConnections.values().toString();
    }
}
