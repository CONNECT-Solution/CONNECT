/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vangent.hieos.services.xds.repository.storage;

import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.db.support.SQLConnectionWrapper;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import java.io.ByteArrayInputStream;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 *  Handles storage and retrieval of XDS documents into an RDBMS.
 *
 * @author Bernie Thuman
 */
public class XDSRepositoryStorageSQL extends XDSRepositoryStorage {
    private final static Logger logger = Logger.getLogger(XDSRepositoryStorageSQL.class);

    /**
     *  Stores an XDS document into RDBMS storage.
     *
     * @param doc Holds the document vitals.
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    public void store(XDSDocument doc) throws XdsInternalException {
        // Get the database connection.
        Connection connection = this.getConnection();

        // Store document in database.
        try {
            connection.setAutoCommit(false);
            if (!this.documentExists(connection, doc)) {
                this.insertDocument(connection, doc);
            } else {
                // FIXME (BHT): Should we update?  Need to investigate.
                this.updateDocument(connection, doc);
            }
            connection.commit();
        } catch (SQLException ex) {
            throw new XdsInternalException("Failure storing document in database [id = " + doc.getDocumentId() + "]: " + ex.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                //Do nothing.
            }
        }
    }

    /**
     *  Returns true if document already exists in the repository.
     *
     * @param connection Database connection instance.
     * @param doc Holds uniqueid to lookup.
     * @return true if uniqueid already exists.
     * @throws java.sql.SQLException
     */
    private boolean documentExists(Connection connection, XDSDocument doc) throws SQLException {
        String sql = "SELECT uniqueid from document where uniqueid = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, doc.getUniqueId());
        logger.trace("SQL(repo) = " + sql);
        ResultSet result = stmt.executeQuery();
        return result.next();
    }

    /**
     *  Inserts a document into the RDBMS.
     *
     * @param connection Database connection instance.
     * @param doc Document to store.
     * @throws java.sql.SQLException
     */
    private void insertDocument(Connection connection, XDSDocument doc) throws SQLException {
        String sql = "INSERT INTO document (uniqueid, documentid, repositoryid, hash, size, mimetype, bytes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, doc.getUniqueId());
        stmt.setString(2, doc.getDocumentId());
        stmt.setString(3, doc.getRepositoryId());
        stmt.setString(4, doc.getHash());
        stmt.setInt(5, doc.getLength());
        stmt.setString(6, doc.getMimeType());

        // Store the blob.
        stmt.setBinaryStream(7, new ByteArrayInputStream(doc.getBytes()), doc.getLength());
        logger.trace("SQL(repo) = " + sql);
        stmt.execute();
    }

    /**
     *  Updates a document in the RDBMS.
     *
     * @param connection Database connection instance.
     * @param doc Document to store.
     * @throws java.sql.SQLException
     */
    private void updateDocument(Connection connection, XDSDocument doc) throws SQLException {
        String sql = "UPDATE document SET documentid=?, repositoryid=?, hash=?, size=?, mimetype=?, bytes=? WHERE uniqueid = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, doc.getDocumentId());
        stmt.setString(2, doc.getRepositoryId());
        stmt.setString(3, doc.getHash());
        stmt.setInt(4, doc.getLength());
        stmt.setString(5, doc.getMimeType());

        // Store the blob.
        stmt.setBinaryStream(6, new ByteArrayInputStream(doc.getBytes()), doc.getLength());

        stmt.setString(7, doc.getUniqueId());
        logger.trace("SQL(repo) = " + sql);
        stmt.executeUpdate();
    }

    /**
     *  Retrieves a document from RDBMS.
     *
     * @param doc Holds uniqueid, repositoryid.
     * @return A filled out XDSDocument instance (reuses passed in value).
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    public XDSDocument retrieve(XDSDocument doc) throws XdsInternalException {
        // Get the database connection.
        Connection connection = this.getConnection();

        // Read blob from database.
        ResultSet rs;
        try {
            String sql = "SELECT documentid, repositoryid, hash, size, mimetype, bytes FROM document WHERE uniqueid = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, doc.getUniqueId());
            logger.trace("SQL(repo) = " + sql);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new XdsInternalException("No document found for uniqueid = " + doc.getUniqueId());
            } else {
                doc.setDocumentId(rs.getString(1));
                doc.setHash(rs.getString(3));
                doc.setLength(rs.getInt(4));
                doc.setMimeType(rs.getString(5));
                // Set blob result.
                // AMS 06/04 - getBlob() was replaced
                // with getBytes() - owing to issues with
                // postgreSQL driver (org.postgresql.util.PSQLException: Bad value for type long)
                // Blob blob = rs.getBlob(6);
                // InputStream is = blob.getBinaryStream();
                // blob.getBytes(1, (int) blob.length());
                byte[] bytes = rs.getBytes(6);
                doc.setBytes(bytes);
            }
        } catch (SQLException ex) {
            throw new XdsInternalException("Failure reading document from database [uniqueid = " + doc.getUniqueId() + "]: " + ex.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                //Do nothing.
            }
        }
        return doc;
    }

    /**
     * Get repository JDBC connection instance from connection pool.
     *
     * @return Database connection instance from pool.
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private Connection getConnection() throws XdsInternalException {
        return new SQLConnectionWrapper().getConnection(SQLConnectionWrapper.repoJNDIResourceName);
    }
}