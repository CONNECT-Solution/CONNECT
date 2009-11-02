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

package com.vangent.hieos.logbrowser.log.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.json.JSONArray;

/**
 * A generic table is a table containing 3 fields :
 *  <ui>
 *    <li>messageid</li>
 *    <li>name</li>
 *    <li>value</li>
 *   </ui>
 *  This table allow to store for a message , several parameters with their values
 * @author jbmeyer
 * @author Bernie Thuman (BHT) Clean up, more documentation, removed "on-the-fly" thinking.
 *
 */
public class GenericTable extends AbstractLogTable {

    public static String MESSAGE_ID = "messageid";
    public static String SEQUENCE_ID = "seqid";  // Added (BHT)
    public static String NAME = "name";
    public static String VALUE = "value";
    private String writeSqlCommand = null;
    private String readSqlCommand = null;
    private String deleteMessageCommand = null;
    private PreparedStatement readPreparedStatement;
    private PreparedStatement writePreparedStatement;
    private PreparedStatement deletePreparedStatement;
    private String parameterName;
    private String parameterValue;
    private int sequenceId = 0;

    private GenericTable() {
    }

    /**
     * Initiate the generic table in the specified database ( Connection c ) , with a specified name
     * @param c
     * @param inTableName
     * @throws LoggerException
     * @throws SQLException
     */
    public GenericTable(Message m, String inTableName) throws LoggerException {
        tableName = inTableName;
        conn = m.getConnection();

        //createTableSqlCommand = "create table " + tableName + " ( " + MESSAGE_ID + " varchar(255) not null REFERENCES " + MainTable.TABLE_NAME + "(" + MainTable.MESSAGE_ID + ") ON DELETE CASCADE ON UPDATE CASCADE , " + NAME + " varchar(255) not null , " + VALUE + " text   );";
        readSqlCommand = "select " + MESSAGE_ID + " , " + NAME + " ," + VALUE + " FROM " + tableName + " where " + MESSAGE_ID + " = ?" + " ORDER BY " + SEQUENCE_ID + " ;";
        writeSqlCommand = "insert into " + tableName + " values (?,?,?,?);";
        deleteMessageCommand = "delete FROM " + inTableName + " WHERE " + MESSAGE_ID + " =? ;";
        try {
            if (conn == null || conn.isClosed()) {
                throw new LoggerException("Database null or closed");
            }
            readPreparedStatement = conn.prepareStatement(readSqlCommand);
            writePreparedStatement = conn.prepareStatement(writeSqlCommand);
            deletePreparedStatement = conn.prepareStatement(deleteMessageCommand);
        } catch (SQLException sqlException) {
            throw new LoggerException("Database problem (SqlException ) " + sqlException.getMessage());
        }
    }

    /**
     * Read a message in the detabase and return an array of parameters and values
     * @param inMessageId
     * @return GenericTable[], an array of generic table containing the messageId, parameters names and values
     * @throws SQLException
     */
    public Vector<GenericTable> readFromDB(String inMessageId) throws LoggerException {
        Vector<GenericTable> vector = null;
        if (inMessageId != null) {
            vector = new Vector<GenericTable>();
            try {
                readPreparedStatement.setString(1, inMessageId);
                ResultSet res = readPreparedStatement.executeQuery();
                while (res.next()) {
                    GenericTable gt = new GenericTable();
                    gt.setParameterName(res.getString(2));
                    gt.setParameterValue(res.getString(3));
                    vector.add(gt);
                }
            } catch (SQLException sqlException) {
                throw new LoggerException("Database problem (SqlException ) " + sqlException.getMessage());
            }
        }
        return vector;
    }

    /**
     *  write the parameter and its value for the current message. The current name and value are used.
     */
    public int writeToDB() throws LoggerException {
        if (writePreparedStatement != null) {
            try {
                if (messageId == null) {
                    throw new LoggerException("MainTable:writeToDB() : messageId is null");
                }
                writePreparedStatement.setString(1, messageId);
                writePreparedStatement.setString(2, parameterName);
                writePreparedStatement.setString(3, parameterValue);
                writePreparedStatement.setInt(4, sequenceId);
                writePreparedStatement.execute();
            } catch (SQLException sqlException) {
                throw new LoggerException("Database problem (SqlException ) " + sqlException.getMessage() + "\n" + writePreparedStatement.toString());
            }
            return 0;
        } else {
            return -1;
        }
    }

    public void setMessageID(String messageID) {
        messageId = messageID;
    }

    public String getMessageID(String messageID) {
        return messageId;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String toString() {
        return parameterName + ":" + parameterValue + "\n";
    }

    public String toXml() {
        return "<node name=\"" + parameterName + ":\" xvalue=\"" + parameterValue + "\" />";
    }

    public String[] toStringArray() {
        String[] vals = {parameterName, parameterValue};
        return vals;
    }

    public String toJSon() {
        JSONArray array = new JSONArray();
        array.put(parameterName);
        array.put(parameterValue);
        return array.toString();
    }

    public void deleteMessage(String messageId) {
        deleteMessage(messageId, deletePreparedStatement);
    }
}
