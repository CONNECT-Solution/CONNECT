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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vangent.hieos.xlog.server;

import com.vangent.hieos.xutil.db.support.SQLConnectionWrapper;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;
import com.vangent.hieos.xutil.xlog.client.XLogMessage.XLogMessageNameValue;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.ObjectMessage;
import javax.jms.JMSException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;
import java.util.Set;
import java.util.Iterator;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.ResultSet;

/**
 *
 * @author Bernie Thuman
 */
@MessageDriven(mappedName = "jms/XLogger", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class XLoggerBean implements MessageListener {

    /**
     *
     */
    public XLoggerBean() {
    }

    /**
     *
     * @param message
     */
    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage m = (ObjectMessage) message;
                Object so = m.getObject();  // Serialized object.
                if (so instanceof XLogMessage) {
                    XLogMessage logMessage = (XLogMessage) so;
                    this.persist(logMessage);
                }
            } else if (message instanceof TextMessage) {
                TextMessage m = (TextMessage) message;
                System.out.println("--- Received message ");
                System.out.println(m.getText());
                System.out.println("----------");
            } else {
                System.out.println(
                        "Received message of type " +
                        message.getClass().getName());
            }
        } catch (JMSException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }
    }

    /**
     * 
     * @param logMessage
     */
    private void persist(XLogMessage logMessage) {
        Connection conn = this.getConnection();
        if (conn == null) {
            // Keep going..
            return;
        }
        try {
            Statement stmt = conn.createStatement();
            persistIp(conn, logMessage, stmt);
            persistMain(logMessage, stmt);
            persistEntries(logMessage, stmt);
            int[] updateCounts = stmt.executeBatch();
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(XLoggerBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                // Keep going.
                Logger.getLogger(XLoggerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 
     * @param conn
     * @param logMessage
     * @param stmt
     * @throws java.sql.SQLException
     */
    private void persistIp(Connection conn, XLogMessage logMessage, Statement stmt) throws SQLException {
        // First see if the IP table needs to have an entry.
        if (this.ipExists(conn, logMessage.getIpAddress()) == false) {
            // Get the SQL statement to place into batch.
            String sql = this.getSQLInsertStatementForIp(logMessage);
            //System.out.println("LOG IP SQL: " + sql);
            stmt.addBatch(sql);
        }
    }

    /**
     * 
     * @param conn
     * @param ipAddress
     * @return
     * @throws java.sql.SQLException
     */
    private boolean ipExists(Connection conn, String ipAddress) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ip WHERE ip = " + this.getSQLQuotedString(ipAddress) + ";";
        //System.out.println("LOG LOOKUP IP = " + sql);
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        res.next();
        boolean result = false;
        if (res.getInt(1) == 0) {
            //System.out.println(" ... IP NOT FOUND");
            result = false;
        } else if (res.getInt(1) > 0) {
            //System.out.println(" ... IP FOUND");
            result = true;
        }
        stmt.close();
        return result;
    }

    /**
     *
     * @param logMessage
     */
    private void persistMain(XLogMessage logMessage, Statement stmt) throws SQLException {
        String sql = this.getSQLInsertStatementForMain(logMessage);
        //System.out.println("LOG MAIN SQL: " + sql);
        stmt.addBatch(sql);
    }

    /**
     *
     * @param logMessage
     */
    private void persistEntries(XLogMessage logMessage, Statement stmt) throws SQLException {
        HashMap<String, Vector<XLogMessageNameValue>> entries = logMessage.getEntries();

        // Now iterate over each detailed entry in the hashmap.
        Set<String> keys = entries.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            //System.out.println("Log processing - " + key);
            // Now, process all entries.
            Vector<XLogMessageNameValue> nameValues = entries.get(key);
            Iterator nameValueIterator = nameValues.iterator();
            int seqId = 0;
            while (nameValueIterator.hasNext()) {
                XLogMessageNameValue nameValue = (XLogMessageNameValue) nameValueIterator.next();
                String sql = this.getSQLInsertStatementForParam(logMessage, key, nameValue, ++seqId);
                //System.out.println("LOG PARAM SQL: " + sql);
                stmt.addBatch(sql);
            }
        }
    }

    /**
     *
     * @param logMessage
     * @return
     */
    private String getSQLInsertStatementForIp(XLogMessage logMessage) {
        return "INSERT INTO IP (ip,company_name,email) VALUES (" +
                this.getSQLQuotedString(logMessage.getIpAddress()) + "," +
                this.getSQLQuotedString(logMessage.getIpAddress()) + "," +
                "'UNKNOWN')";
    }

    /**
     *
     * @param logMessage
     * @return
     */
    private String getSQLInsertStatementForMain(XLogMessage logMessage) {
        // Get the timestamp properly formatted.
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(logMessage.getTimeStamp());
        Timestamp timestamp = new Timestamp(gc.getTimeInMillis());
        return "INSERT INTO MAIN (messageid,is_secure,ip,timereceived,test,pass) VALUES (" +
                getSQLQuotedString(logMessage.getMessageID()) + "," +
                logMessage.isSecureConnection() + "," +
                getSQLQuotedString(logMessage.getIpAddress()) + "," +
                getSQLQuotedString(timestamp.toString()) + "," +
                getSQLQuotedString(logMessage.getTestMessage()) + "," +
                logMessage.isPass() +
                ")";
    }

    /**
     *
     * @param logMessage
     * @param tableName
     * @param nameValue
     * @param seqId
     * @return
     */
    private String getSQLInsertStatementForParam(XLogMessage logMessage, String tableName, XLogMessageNameValue nameValue, int seqId) {
        return "INSERT INTO " + tableName + " (messageid,name,value,seqid) VALUES (" +
                getSQLQuotedString(logMessage.getMessageID()) + "," +
                getSQLQuotedString(nameValue.getName()) + "," +
                getSQLQuotedString(nameValue.getValue()) + "," +
                (new Integer(seqId)).toString() + ")";
    }

    /**
     *
     * @param toReplace
     * @return
     */
    private String replaceQuotes(String toReplace) {
        StringBuffer buff = new StringBuffer(toReplace);
        for (int i = 0; i < buff.length(); i++) {
            if (buff.charAt(i) == '\'') {
                buff.insert(i, '\'');
                i++;
            }
        }
        return new String(buff);
    }

    /**
     *
     * @param val
     * @return
     */
    private String getSQLQuotedString(String val) {
        return "'" + replaceQuotes(val) + "'";
    }

    /**
     *
     * @return Database connection on success.  Otherwise, null.
     */
    private Connection getConnection() {
        Connection con = null;
        SQLConnectionWrapper conWrapper = new SQLConnectionWrapper();
        try {
            con = conWrapper.getConnection(SQLConnectionWrapper.logJNDIResourceName);
        } catch (XdsInternalException ex) {
            Logger.getLogger(XLoggerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
}