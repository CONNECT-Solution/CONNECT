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

import com.vangent.hieos.logbrowser.log.db.Log;
import com.vangent.hieos.logbrowser.log.db.LoggerException;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetTestNameServlet extends HttpServlet {

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
        PreparedStatement queryTestName = null;
        PreparedStatement queryTestNameWithIP = null;
        Log log = new Log();
        try {
            queryTestNameWithIP = log.getConnection().prepareStatement("select distinct test from main where ip=? order by test asc ;");
            queryTestName = log.getConnection().prepareStatement("select distinct test from main order by test asc ;");
        } catch (SQLException e) {
            getError(e, res);
        } catch (LoggerException e) {
            getError(e, res);
        }

        // Make sure that it is OK to proceed.
        if (queryTestNameWithIP == null || queryTestName == null) {
            try {
                log.closeConnection();
            } catch (LoggerException ex) {
                Logger.getLogger(GetTestNameServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;  // EARLY EXIT: Can not continue processing.
        }

        // Write XML to response.
        res.setContentType("text/xml");
        try {
            ResultSet result = null;

            if (req.getParameter("ip") != null) {
                queryTestNameWithIP.setString(1, req.getParameter("ip"));
                result = queryTestNameWithIP.executeQuery();
            } else {
                result = queryTestName.executeQuery();
            }

            res.getWriter().write("<result>");
            while (result.next()) {
                res.getWriter().write(
                        "<test >" + result.getString(1) + "</test>");
            }
            res.getWriter().write("</result>");

        } catch (SQLException e) {
            getError(e, res);
        } catch (IOException e) {
            getError(e, res);
        } finally {
            try {
                log.closeConnection();
            } catch (LoggerException ex) {
                Logger.getLogger(GetTestNameServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param e
     * @param response
     */
    private void getError(Exception e, HttpServletResponse response) {
        PrintWriter print;

        try {
            print = response.getWriter();
            response.setContentType("text/xml");
            StringBuffer toPrint = new StringBuffer();
            StringBuffer toPrint2 = new StringBuffer();
            toPrint.append("<result>");
            toPrint.append("<error>");

            toPrint.append(e.getClass().toString() + ":" + e.getMessage());
            toPrint2.append(e.getClass().toString() + ":" + e.getMessage() + "\n");

            StackTraceElement[] stack = e.getStackTrace();
            for (int i = 0; i < stack.length; i++) {
                toPrint2.append(stack[i].toString() + "\n");
            }
            toPrint.append("</error>");
            toPrint.append("</result>");
            print.write(toPrint.toString());

        } catch (IOException e1) {
        }
    }
}
