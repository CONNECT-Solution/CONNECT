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

import com.vangent.hieos.xutil.exception.XdsInternalException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import com.vangent.hieos.xutil.xconfig.XConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class describing a message to log in database. It contains several action log, read
 *  and delete a message.
 * @author jbmeyer
 *
 */
public class Message {

    private String messageID;
    private int sequenceId = 0;
    private MainTable mainMessage;
    private Hashtable<String, Vector<GenericTable>> miscVectors;
    //private HashSet<String> tableList;
    static Vector<String> tableList = new Vector<String>();
    private Connection connection;
    @SuppressWarnings("unused")
    private String companyName = null;

    @SuppressWarnings("unused")
    private Message() {
    }


    static {
        tableList.add("other");
        tableList.add("error");
        tableList.add("http");
        tableList.add("soap");
    }

    /**
     *
     * @param c Connection. The database connection.
     * @param id String. The identifier of the message.
     * @throws LoggerException
     */
    public Message(Connection c, String id) throws LoggerException {
        connection = c;
        mainMessage = new MainTable(connection);
        mainMessage.setMessageId(id);
        miscVectors = new Hashtable<String, Vector<GenericTable>>();
        //tableList = GenericTable.ListTable(connection);
        messageID = id;
    }

    /**
     *
     * @param c Connection. The database connection.
     * @param id String. The identifier of the message.
     * @throws LoggerException
     */
    public Message(Connection c) throws LoggerException {
        connection = c;
        mainMessage = new MainTable(connection);
        miscVectors = new Hashtable<String, Vector<GenericTable>>();
    //tableList = GenericTable.ListTable(connection);
    }

    public void setTimeStamp(Timestamp timestamp) {
        mainMessage.setTimestamp(timestamp);
    }

    public void setSecure(boolean isSecure) {
        mainMessage.setSecure(isSecure);
    }

    public void setTestMessage(String testMessage) {
        mainMessage.setTest(testMessage);
    }

    public void setPass(boolean pass) {
        mainMessage.setPass(pass);
    }

    public void setIP(String ip) throws LoggerException {
        try {
            mainMessage.setIpAddress(InetAddress.getByName(ip));
        } catch (UnknownHostException e) {
            throw new LoggerException("IP : setIP... : " + e.getMessage());
        }
    }

    /***
     *
     * @param companyName String. Make a pair in the IP table between an IP address and a company name. If this pair doesn't exist
     * in the IP table, it's logged, if it exists, the pair is updated with the new company name.
     * @throws LoggerException
     */
    public void setCompany(String companyName) throws LoggerException {
        if (mainMessage.getIpAddress() == null) {
            throw new LoggerException("Message:setCompany ( String companyName ):: Cannot set company name , the current IP adress associated is null");
        }
        try {
            IpCompanyTable ip = new IpCompanyTable(connection);
            ip.updateIp(mainMessage.getIpAddress().getHostAddress(), companyName);
            this.companyName = companyName;
        } catch (SQLException e) {
            throw new LoggerException("Message:setCompany ( String companyName ):: " + e.getMessage());
        }
    }

    /**
     * Generic function creating a pair &lt; parameter name , parameter value &gt; for the current messageID. <br />
     * If the Table Name doesn't exist , it is created before writing the message ( in the writeMessage method ).
     * The parameter is stored in a hashtable &lt; tableName , Vector &lt;GenericTable &gt; &gt;
     *
     * @param tableName, table name wherein the parameter name and value are logged
     * @param name , parameter name
     * @param value ,parameter value
     * @throws LoggerException
     */
    private void addParam(String tableName, String name, String value) throws LoggerException {
        if (tableName != null) {
            tableName = tableName.trim().replace(' ', '_').toLowerCase();
            GenericTable newGenericTable = new GenericTable(this, tableName.trim());
            newGenericTable.setMessageID(messageID);
            newGenericTable.setParameterName(name);
            newGenericTable.setSequenceId(++sequenceId);  // To allow messages to be queried in order.

            // ADDED (BHT): to be safe [only store up to 'maxLogBytes' bytes].
            String logString = value;
            if (value != null) {
                int logMaxBytes = 5000;  // DEFAULT.
                try {
                    XConfig xconf;
                    xconf = XConfig.getInstance();
                    logMaxBytes = (int) xconf.getHomeCommunityPropertyAsLong("LogMaxBytes");
                } catch (XdsInternalException ex) {
                    Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (logMaxBytes != -1) {  // -1 means no restrictions on size.
                    int valueSize = value.length();
                    if (valueSize > logMaxBytes) {
                        logString = value.substring(0, logMaxBytes - 1);
                        logString = "... only " + logMaxBytes + " bytes logged [actual bytes = " + valueSize + "]" + " ...\n" + logString;
                    }
                }
            }
            newGenericTable.setParameterValue(logString);

            if (miscVectors.containsKey(tableName)) {
                Vector<GenericTable> genTable = miscVectors.get(tableName);
                genTable.add(newGenericTable);
            } else {
                Vector<GenericTable> genTable = new Vector<GenericTable>();
                genTable.add(newGenericTable);
                miscVectors.put(tableName, genTable);
            }
        } else {
            throw new LoggerException("TableName is null");
        }
    }

    /**
     * Used for xdsTestLog. Same as addParam ( "http" ,  name ,  value ) ;
     * @param name
     * @param value
     * @throws LoggerException
     */
    public void addHTTPParam(String name, String value) throws LoggerException {
        addParam("http", name, value);
    }

    /**
     * Used for xdsTestLog. Same as addParam ( "soap" ,  name ,  value ) ;
     * @param name
     * @param value
     * @throws LoggerException
     */
    public void addSoapParam(String name, String value) throws LoggerException {
        addParam("soap", name, value);
    }

    /**
     * Used for xdsTestLog. Same as addParam ( "error" ,  name ,  value ) ;
     * @param name
     * @param value
     * @throws LoggerException
     */
    public void addErrorParam(String name, String value) throws LoggerException {
        addParam("error", name, value);
    }

    /**
     * Used for xdsTestLog. Same as addParam ( "other" ,  name ,  value ) ;
     * @param name
     * @param value
     * @throws LoggerException
     */
    public void addOtherParam(String name, String value) throws LoggerException {
        addParam("other", name, value);
    }

    /**
     * Read the message with the current MessageID and store all data in the mainMessage attribute and the hashmap miscVector
     * @throws LoggerException
     */
    public void readMessage() throws LoggerException {
        if (messageID == null) {
            throw new LoggerException("Message:readMessage() messageID is null");
        }
        mainMessage.readFromDB(messageID);
        Iterator<String> it = tableList.iterator();
        while (it.hasNext()) {
            String currentTable = it.next();
            //if (!currentTable.equals("main") && !currentTable.equals("ip")) {
            GenericTable gt = new GenericTable(this, currentTable);
            Vector<GenericTable> vectGenTable = gt.readFromDB(messageID);
            miscVectors.put(currentTable, vectGenTable);
        //}
        }
    }

    /** 
     * Write the message in the database 
     * @throws LoggerException
     */
    public void writeMessage() throws LoggerException {
        //synchronized (this) { BHT: Removed (No clear need for synchronization here).
        Long startTime = System.currentTimeMillis();
        System.out.println("Log Write ...");
        try {
            mainMessage.writeToDB();
        } catch (LoggerException l) {
        }
        Set<Entry<String, Vector<GenericTable>>> set = miscVectors.entrySet();
        Iterator<Entry<String, Vector<GenericTable>>> it = set.iterator();
        while (it.hasNext()) {
            Entry<String, Vector<GenericTable>> currentElement = it.next();
            Vector<GenericTable> v = currentElement.getValue();
            for (int i = 0; i < v.size(); i++) {
                v.elementAt(i).writeToDB();
            }
            v = null;
            currentElement = null;
        }
        System.out.println("Log Write ELAPSED TIME: " + new Long(System.currentTimeMillis() - startTime).toString());
    //}
    }

    /**
     * 
     * @throws LoggerException
     */
    public void deleteMessage() throws LoggerException {
        // No need more because of delete cascade
        mainMessage.deleteMessage(messageID);
    }

    /**
     * Method used to display the message in the xds log reader. This method format the message in XML displaying first the
     * list of table ( nodes ) available and then the content of the message.
     * @return
     */
    public String toXml() {
        StringBuffer buff = new StringBuffer();
        StringBuffer buffNodeNames = new StringBuffer();
        buffNodeNames.append("<Nodes>");
        buffNodeNames.append("<Node name='mainMessage' />");
        buff.append(mainMessage.toXml());
        Iterator<String> it = tableList.iterator();
        while (it.hasNext()) {
            String currentTable = it.next();
            //if (!currentTable.equals("main") && !currentTable.equals("ip")) {
            Vector<GenericTable> v = miscVectors.get(currentTable);
            buffNodeNames.append("<Node name='" + currentTable + "' />");
            buff.append("<" + currentTable + ">");
            for (int i = 0; i < v.size(); i++) {
                buff.append(v.elementAt(i).toXml());
            }
            buff.append("</" + currentTable + ">");
        //}
        }
        buffNodeNames.append("</Nodes>");
        return "<message number='" + messageID + "'>" + buffNodeNames.toString() + buff.toString() + "</message>";
    }

    /**
     * 
     * @return
     */
    public HashMap<String, HashMap<String, Object>> toHashMap() {
        HashMap<String, HashMap<String, Object>> values = new HashMap<String, HashMap<String, Object>>();
        values.put("main", mainMessage.toHashMap());
        Iterator<String> it = tableList.iterator();
        while (it.hasNext()) {
            String currentTable = it.next();
            //if (!currentTable.equals("main") && !currentTable.equals("ip")) {
            Vector<GenericTable> v = miscVectors.get(currentTable);
            HashMap<String, Object> thisTable = new HashMap<String, Object>();
            for (int i = 0; i < v.size(); i++) {
                String[] parm = v.elementAt(i).toStringArray();
                String key = parm[0].replaceAll(" ", "_");
                String value = parm[1];
                Object oldValueObject = thisTable.get(key);
                if (oldValueObject == null) {
                    thisTable.put(key, value);
                } else {
                    if (oldValueObject instanceof String) {
                        ArrayList<String> newValue = new ArrayList<String>();
                        newValue.add((String) oldValueObject);
                        newValue.add(value);
                        thisTable.put(key, newValue);
                    } else {
                        ArrayList<String> newValue = (ArrayList<String>) oldValueObject;
                        newValue.add(value);
                        thisTable.put(key, newValue);
                    }
                }
            }
            values.put(currentTable, thisTable);
        //}
        }
        return values;

    //return "<message number='"+   messageID  +"'>" + buffNodeNames.toString() + buff.toString() + "</message>" ;
    }

    /**
     * 
     * @return
     */
    public String toJSon() {
        StringBuffer buff = new StringBuffer();
        buff.append("{\"message\" : { \n" +
                "\"number\": \"" + messageID + "\" , \n  ");
        buff.append("\"table\": \n\t[");
        buff.append(mainMessage.toJSon());
        Iterator<String> it = tableList.iterator();
        while (it.hasNext()) {
            String currentTable = it.next();
            //if (!currentTable.equals("main") && !currentTable.equals("ip")) {
            Vector<GenericTable> v = miscVectors.get(currentTable);
            buff.append(",\n{\"name\" : \"" + currentTable + "\",\n");
            buff.append("\"values\" : [");
            for (int i = 0; i < v.size(); i++) {
                buff.append(v.elementAt(i).toJSon());
                if (i < v.size()) {
                    buff.append(",\n");
                }
            }
            buff.append("]\n}");
        //}
        }
        buff.append("]\n}\n}");
        return buff.toString();
    }

    /**
     * 
     * @return
     */
    public Connection getConnection() {
        return connection;
    }

    /*
    public HashSet<String> getTableList() {
    return tableList;
    }
     */

    /*
    public void setTableList(HashSet<String> tableList) {
    this.tableList = tableList;
    }
     */
    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }
}
