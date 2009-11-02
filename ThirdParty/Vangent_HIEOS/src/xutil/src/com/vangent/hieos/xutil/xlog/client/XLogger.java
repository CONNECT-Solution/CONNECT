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

import com.vangent.hieos.xutil.exception.XdsInternalException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.QueueConnectionFactory;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.ObjectMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.GregorianCalendar;

import com.vangent.hieos.xutil.xconfig.XConfig;

/**
 *
 * @author Bernie Thuman
 */
public class XLogger {

    static XLogger _instance = null;
    boolean logEnabled = false;  // Default (no logging).

    /**
     *
     * @return
     */
    synchronized static public XLogger getInstance() {
        if (_instance == null) {
            _instance = new XLogger();
        }
        return _instance;
    }

    /**
     *
     */
    private XLogger() {
        this.setLogEnabled();
    }

    /**
     * 
     * @return
     */
    public XLogMessage getNewMessage(String ipAddress) {
        // Create message id from timestamp and ipAddress.
        GregorianCalendar cal = new GregorianCalendar();
        long currentTime = cal.getTimeInMillis();
        String id = new Long(currentTime).toString() + ipAddress;
        XLogMessage m = new XLogMessage(this, id);
        m.setIpAddress(ipAddress);
        m.setTimeStamp(currentTime);
        return m;
    }

    /**
     *
     * @param messageData
     */
    protected void store(XLogMessage messageData) {
        if (this.logEnabled == true) {
            try {
                this.sendJMSMessageToXLogger(messageData);
            } catch (NamingException ex) {
                Logger.getLogger(XLogger.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JMSException ex) {
                Logger.getLogger(XLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param messageData
     * @throws javax.naming.NamingException
     * @throws javax.jms.JMSException
     */
    private void sendJMSMessageToXLogger(XLogMessage messageData) throws NamingException, JMSException {
        Context ctx = new InitialContext();
        QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("jms/XLoggerFactory");
        QueueConnection connection = null;
        QueueSession session = null;
        try {
            connection = factory.createQueueConnection();
            session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue) ctx.lookup("jms/XLogger");
            MessageProducer mp = session.createProducer(queue);
            mp.send(createJMSMessageForjmsXLogger(session, messageData));
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     *
     */
    private void setLogEnabled() {
        try {
            this.logEnabled = XConfig.getInstance().getHomeCommunityPropertyAsBoolean("LogEnabled");
        } catch (XdsInternalException ex) {
            Logger.getLogger(XLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param session
     * @param messageData
     * @return
     * @throws javax.jms.JMSException
     */
    private Message createJMSMessageForjmsXLogger(Session session, XLogMessage messageData) throws JMSException {
        //TextMessage tm = session.createTextMessage();
        //tm.setText(messageData.toString());
        //return tm;
        ObjectMessage om = session.createObjectMessage(messageData);
        return om;
    }
}
