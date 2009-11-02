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
package com.vangent.hieos.xutil.xlog.client;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author Bernie Thuman
 */
public class XLogMessage implements Serializable {

    private String messageID;   // Message identifier.
    private String ipAddress;   // Source of message.
    private String testMessage; // Transaction being logged.
    private boolean pass;    // Transaction failed or completed.
    private boolean secureConnection;   // True if secure transaction.
    private long timeStamp;     // Time when message was created (current time in milliseconds).
    private XLogger xlogger;    // Reference back to creator.
    private HashMap<String, Vector<XLogMessageNameValue>> entries = new HashMap<String, Vector<XLogMessageNameValue>>();

    /**
     *
     * @param xlogger
     * @param messageID
     */
    public XLogMessage(XLogger xlogger, String messageID) {
        this.messageID = messageID;
        this.xlogger = xlogger;
    }

    /**
     *
     * @return
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * 
     * @return
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     *
     * @param ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     *
     * @return
     */
    public String getTestMessage() {
        return testMessage;
    }

    /**
     *
     * @param testMessage
     */
    public void setTestMessage(String testMessage) {
        this.testMessage = testMessage;
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
    public boolean isSecureConnection() {
        return secureConnection;
    }

    /**
     *
     * @param secureConnection
     */
    public void setSecureConnection(boolean secureConnection) {
        this.secureConnection = secureConnection;
    }

    /**
     *
     * @return
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     *
     * @param timeStamp
     */
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     *
     * @param name
     * @param value
     */
    public void addHTTPParam(String name, String value) {
        this.addParam("http", name, value);
    }

    /**
     *
     * @param name
     * @param value
     */
    public void addSOAPParam(String name, String value) {
        this.addParam("soap", name, value);
    }

    /**
     *
     * @param name
     * @param value
     */
    public void addErrorParam(String name, String value) {
        this.addParam("error", name, value);
    }

    /**
     *
     * @param name
     * @param value
     */
    public void addOtherParam(String name, String value) {
        this.addParam("other", name, value);
    }

    /**
     * 
     * @return
     */
    public HashMap<String, Vector<XLogMessageNameValue>> getEntries()
    {
        return this.entries;
    }

    /**
     *
     * @param messageType
     * @param name
     * @param value
     */
    private void addParam(String messageType, String name, String value) {
        Vector<XLogMessageNameValue> nameValues;
        // Does the messageType (e.g. other, error, soap, http) exist?
        if (entries.containsKey(messageType)) {
            // Found the message type.
            nameValues = entries.get(messageType);
        } else {
            // Did not find the message type, create vector to hold name/value pairs.
            nameValues = new Vector<XLogMessageNameValue>();
            entries.put(messageType, nameValues);
        }
        // Now save the log entry.
        XLogMessageNameValue nameValue = new XLogMessageNameValue(name, value);
        nameValues.add(nameValue);
    }

    /**
     * 
     */
    public void store() {
        XLogger delegate = xlogger;
        xlogger = null;  // So not to serialize.
        //if (true) return;  // DEBUG (BERNIE -- get out).
        delegate.store(this);  // Delegate back to the creator.
    }

    /**
     *
     */
    public class XLogMessageNameValue implements Serializable {

        private String name;
        private String value;

        /**
         *
         * @param name
         * @param value
         */
        public XLogMessageNameValue(String name, String value) {
            this.name = name;
            this.value = value;
        }

        /**
         *
         * @return
         */
        public String getName()
        {
            return this.name;
        }

        /**
         * 
         * @return
         */
        public String getValue()
        {
            return this.value;
        }
    }
}
