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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.HashMap;

enum MainTableFields {

    messageid, ip, timereceived, test, pass, is_secure
};

/**
 * Class reprensenting the main informations to display in the table of messages.
 * @author jbmeyer
 *
 */
public class MainTable extends AbstractLogTable {

    public static final String MESSAGE_ID = "messageid";
    public static final String IP = "ip";
    public static final String TIMESTAMP = "timereceived";
    public static final String TEST = "test";
    public static final String PASS = "pass";
    public static final String IS_SECURE = "is_secure";
    public static final String TABLE_NAME = "main";

    public String writeSqlCommand = null;
    public String readSqlCommand = null;
    public String deleteMessageCommand = null;
    private PreparedStatement readPreparedStatement;
    private PreparedStatement writePreparedStatement;
    private PreparedStatement deletePreparedStatement;
    private InetAddress ipAddress;
    private Timestamp timestamp;
    private String test;
    private boolean pass;
    private boolean isSecure;

    /**
     *
     * @param c
     * @throws LoggerException
     */
    public MainTable(Connection c) throws LoggerException {
        conn = c;
        tableName = TABLE_NAME;
        //createTableSqlCommand = "create table " + TABLE_NAME + " ( " + MESSAGE_ID + " varchar(255) not null , " + IS_SECURE + " bool, " + IP + " varchar(100) not null REFERENCES " + IpCompanyTable.TABLE_NAME + "(" + IpCompanyTable.IP_ADDRESS + "), " + TIMESTAMP + " timestamp not null default 'now' , " + TEST + " text not null , " + PASS + " bool , PRIMARY KEY (" + MESSAGE_ID + ")  );";
        readSqlCommand = "select " + MESSAGE_ID + " , " + IS_SECURE + " , " + IP + " ," + TIMESTAMP + " , " + TEST + " , " + PASS + " FROM " + TABLE_NAME + " where " + MESSAGE_ID + " = ? ;";
        writeSqlCommand = "insert into " + TABLE_NAME + " values (?,?,?,?,?,?);";
        deleteMessageCommand = "delete FROM " + TABLE_NAME + " WHERE " + MESSAGE_ID + " =? ;";
        try {
            if (conn == null || conn.isClosed()) {
                throw new LoggerException("Database null or closed");
            }

            /** TODO PUT THAT ( preparedStatement ) into a static method in order to do it once and not to each constructor call **/
            readPreparedStatement = conn.prepareStatement(readSqlCommand);
            writePreparedStatement = conn.prepareStatement(writeSqlCommand);
            deletePreparedStatement = conn.prepareStatement(deleteMessageCommand);
            test = new String();
        } catch (SQLException sqlException) {
            throw new LoggerException("Database problem (SqlException ) " + sqlException.getMessage());
        }
    }

    /*************GETTERS AND SETTERS*********************/
    /**
     * 
     * @return
     */
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    /**
     *
     * @param ipAddress
     */
    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     *
     * @return
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     *
     * @param messageId
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     *
     * @return
     */
    public boolean isPass() {
        return pass;
    }

    /**
     *
     * @param pass
     */
    public void setPass(boolean pass) {
        this.pass = pass;
    }

    /**
     *
     * @return
     */
    public String getTest() {
        return test;
    }

    /**
     *
     * @param test
     */
    public void setTest(String test) {
        this.test = test;
    }

    /**
     *
     * @return
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @param timestamp
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     *
     * @param inMessageId
     * @return
     * @throws LoggerException
     */
    public int readFromDB(String inMessageId) throws LoggerException {

        try {
            if (conn == null || conn.isClosed()) {
                throw new LoggerException("Database null or closed");
            }

            if (readSqlCommand != null) {
                if (messageExist(inMessageId, MainTable.TABLE_NAME)) {
                    if (readPreparedStatement != null) {
                        readPreparedStatement.setString(1, inMessageId);
                        ResultSet result = readPreparedStatement.executeQuery();
                        //"select messageid , ip , timereceived , test , pass FROM " + tableName + " where messageid = ? ;"  ;
                        result.next();
                        messageId = result.getString(1);
                        isSecure = result.getBoolean(2);
                        try {
                            ipAddress = InetAddress.getByName(result.getString(3));
                        } catch (UnknownHostException e) {
                        }
                        timestamp = result.getTimestamp(4);
                        test = result.getString(5);
                        pass = result.getBoolean(6);
                    }
                } else {
                    return -1;
                }
            } else {
                return -1;
            }

            return 0;
        } catch (SQLException sqlException) {
            throw new LoggerException("Database problem (SqlException ) " + sqlException.getMessage());
        }
    }

    /**
     *
     * @return
     * @throws LoggerException
     */
    int writeToDB() throws LoggerException {
        try {
            if (messageId == null) {
                throw new LoggerException("MaintTable:writeToDB(): messageId is null");
            }
            writePreparedStatement.setString(1, messageId);
            writePreparedStatement.setBoolean(2, isSecure);
            // Test Ip
            if (ipAddress == null) {
                try {
                    ipAddress = InetAddress.getByName("localhost");
                } catch (UnknownHostException e) {
                }
            }
            writePreparedStatement.setString(3, ipAddress.getHostAddress());
            // Added 11/14/2007
            if (!IpCompanyTable.IpExist(this.conn, ipAddress.getHostAddress())) {
                IpCompanyTable ipCompanyTable = new IpCompanyTable(this.conn);
                {
                    ipCompanyTable.writeToDB(ipAddress.getHostAddress());
                }
            }

            // timereceived
            if (timestamp == null) {
                timestamp = new Timestamp(new GregorianCalendar().getTimeInMillis());
            }
            writePreparedStatement.setTimestamp(4, timestamp);
            // test
            writePreparedStatement.setString(5, test);

            //pass
            writePreparedStatement.setBoolean(6, pass);
            //System.out.println(writePreparedStatement.toString());
            writePreparedStatement.execute();

        } catch (SQLException e1) {
            throw new LoggerException("MainTable:writeToDB() problem : (" + e1.getErrorCode() + " ) " + e1.getMessage());
        }
        return 0;
    }

    /**
     *
     * @return
     */
    public String toString() {
        return "MessageId:" + messageId +
                "\nIP :" + ipAddress.getHostAddress() +
                "\nTimestamp:" + timestamp.toString() +
                "\nPass :" + pass +
                "\nTest :" + test + "\n";
    }

    /**
     *
     * @return
     */
    public String toXml() {
        StringBuffer stringBuff = new StringBuffer();
        stringBuff.append("<mainMessage>");
        stringBuff.append("	<node name=\"MessageId\" value=\"" + messageId + "\" />");
        stringBuff.append("	<node name=\"IP\" value=\"" + ipAddress.getHostAddress() + "\" />");
        stringBuff.append("	<node name=\"Timestamp\" value=\"" + timestamp.toString() + "\" />");
        stringBuff.append("	<node name=\"Pass\" value=\"" + pass + "\" />");
        stringBuff.append("	<node name=\"Test\" value=\"" + test + "\" />");
        stringBuff.append("</mainMessage>");
        return stringBuff.toString();
    }

    /**
     *
     * @return
     */
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("MessageId", messageId);
        map.put("IP", ipAddress.getHostAddress());
        map.put("Timestamp", timestamp.toString());
        map.put("Pass", (pass) ? "Pass" : "Fail");
        map.put("Test", test);
        return map;
    }

    /**
     *
     * @return
     */
    public String toJSon() {
        StringBuffer stringBuff = new StringBuffer();
        stringBuff.append("{ \"name\"  : \"mainMessage\"  , \n");
        stringBuff.append("  \"values\" : [\n ");
        stringBuff.append(" [ \"MessageId\" , \"" + messageId + "\"],\n ");
        if (ipAddress != null) {
            stringBuff.append("[ \"IP\"        , \"" + ipAddress.getHostAddress() + "\"] ,\n ");
        }
        if (timestamp != null) {
            stringBuff.append("[ \"Timestamp\" , \"" + timestamp.toString() + "\"],\n ");
        }
        stringBuff.append("[ \"Pass\"      , \"" + pass + "\" ], \n ");
        stringBuff.append("[ \"Test\"      , \"" + test + "\" ]\n]} ");
        return stringBuff.toString();
    }

    /**
     *
     * @return
     */
    public boolean isSecure() {
        return isSecure;
    }

    /**
     *
     * @param isSecure
     */
    public void setSecure(boolean isSecure) {
        this.isSecure = isSecure;
    }

    /**
     *
     * @param messageId
     */
    public void deleteMessage(String messageId) {
        deleteMessage(messageId, deletePreparedStatement);
    }
}
