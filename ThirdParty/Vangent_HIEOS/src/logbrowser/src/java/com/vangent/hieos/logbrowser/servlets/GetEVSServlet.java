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
package com.vangent.hieos.logbrowser.servlets;

import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.logbrowser.log.db.Log;
import com.vangent.hieos.logbrowser.log.db.LoggerException;
import com.vangent.hieos.logbrowser.log.db.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;

public class GetEVSServlet extends HttpServlet {

    /**
     *
     * @param config
     * @throws javax.servlet.ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        // Do nothing.
    }

    /**
     *
     * @param req
     * @param res
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        res.setContentType("application/download-xml-file");
        String messageId = req.getParameter("message");
        OMElement report = null;
        Log log = new Log();
        try {
            Message m = log.readMessage(messageId);
            report = buildReport(m);
        } catch (LoggerException e) {
            report = MetadataSupport.om_factory.createOMElement(new QName("NoReportAvailable"));
            report.setText(e.getMessage().replaceAll("<", "&lt;"));
        } catch (ServletException e) {
            report = MetadataSupport.om_factory.createOMElement(new QName("NoReportAvailable"));
            report.setText(e.getMessage().replaceAll("<", "&lt;"));
        } finally {
            try {
                log.closeConnection();
            } catch (LoggerException ex) {
                Logger.getLogger(GetEVSServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            res.getWriter().write(report.toString());
        } catch (IOException e) {
        }
    }

    /**
     *
     * @param m
     * @return
     */
    private String index(Message m) {
        HashMap<String, HashMap<String, Object>> mm = m.toHashMap();
        StringBuffer buf = new StringBuffer();
        for (String key : mm.keySet()) {
            buf.append("\n" + key + "\n" + mm.get(key).keySet());
        }
        return buf.toString();
    }

    /**
     *
     * @param mm
     * @param section
     * @param parm
     * @return
     */
    private Object getParm(HashMap<String, HashMap<String, Object>> mm, String section, String parm) {
        return mm.get(section).get(parm);
    }

    /**
     *
     * @param report
     * @param mm
     * @param name
     * @param section
     * @param parm
     */
    private void addParm(OMElement report, HashMap<String, HashMap<String, Object>> mm, String name, String section, String parm) {
        Object value = getParm(mm, section, parm);
        if (value instanceof String) {
            OMElement ele = MetadataSupport.om_factory.createOMElement(new QName(name));
            String txt = (String) value;
            txt = extractPrefix(txt);
            ele.setText(txt);
            report.addChild(ele);
        } else if (value instanceof ArrayList) {
            ArrayList<String> txts = (ArrayList<String>) value;
            for (String txt : txts) {
                OMElement ele = MetadataSupport.om_factory.createOMElement(new QName(name));
                txt = extractPrefix(txt);
                ele.setText(txt);
                report.addChild(ele);
            }
        }
    }

    /**
     *
     * @param txt
     * @return
     */
    private String extractPrefix(String txt) {
        int colonI = txt.indexOf(':');
        if (colonI != -1) {
            if (txt.charAt(colonI + 1) == ' ' || txt.charAt(colonI + 1) == '\t') {
                txt = txt.substring(colonI + 2).trim();
            }
        }
        return txt;
    }

    /**
     *
     * @param m
     * @return
     * @throws javax.servlet.ServletException
     */
    private OMElement buildReport(Message m) throws ServletException {
        System.out.println(index(m));
        HashMap<String, HashMap<String, Object>> mm = m.toHashMap();
        OMElement report = MetadataSupport.om_factory.createOMElement(new QName("XdsEvsResult"));
        addParm(report, mm, "Test", "other", "Service");
        addParm(report, mm, "Date", "main", "Timestamp");
        addParm(report, mm, "Source", "http", "IP_address_From");
        OMElement ele = MetadataSupport.om_factory.createOMElement(new QName("Target"));
        ele.setText(localIPAddress());
        report.addChild(ele);
        addParm(report, mm, "Endpoint", "http", "URI_To");
        addParm(report, mm, "Result", "main", "Pass");
        addParm(report, mm, "LogEvent", "main", "MessageId");
        addParm(report, mm, "Error", "error", "Error");

        //System.out.println("errors are:\n" + mm.get("error"));
        return report;
    }

    /**
     *
     * @return
     */
    private String localIPAddress() {
        try {
            java.net.InetAddress localMachine =
                    java.net.InetAddress.getLocalHost();
            return localMachine.getHostAddress();
        } catch (java.net.UnknownHostException uhe) {
            //handle exception
        }
        return "unknown";
    }
}
