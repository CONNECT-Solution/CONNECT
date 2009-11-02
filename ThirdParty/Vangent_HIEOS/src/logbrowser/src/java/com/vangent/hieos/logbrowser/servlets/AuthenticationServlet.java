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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
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
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;

/**
 *  
 * This class is a servlet used to display xds messages received by the server implemented     <br/>
 * in the xdslog project. This servlet implement strictly a doPost method. This servlet should <br/>
 * receive several paramaters such as : <br/>
 * <ul>
 *	<li>sort : column number to sort  </li>
 *	<li>option : the word "count" can be used to have the number of result</li>
 *	<li>page : page number </li>
 *  <li>nbResByPage : number of result by pages (50 by default)</li>
 *  <li>optioni : option number i  </li>
 *  <li>valuei  :  value number i . Thess values are the result of the user's choice in the web interface </li>
 * </ul>
 * 
 * @author jbmeyer
 *
 */
public class AuthenticationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private org.apache.commons.logging.Log logger = LogFactory.getLog(this.getClass());
    private String passwordFile;
    private String passwordRead;
    private String url;
    private String login;
    private String password;

    /**
     *
     * @param config
     * @throws javax.servlet.ServletException
     */
    public void init(ServletConfig config) throws ServletException {
    }

    /**
     *
     * @param req
     * @param res
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        PreparedStatement updateCompany = null;
        PreparedStatement selectCompanyName = null;
        Log log = new Log();
        try {
            Connection con = log.getConnection();
            updateCompany = con.prepareStatement(
                    "update ip set company_name=? , email=? where ip=? ;");
            selectCompanyName = con.prepareStatement(
                    "SELECT company_name,email FROM ip where ip = ? ; ");
        } catch (SQLException e) {
        } catch (LoggerException e) {
        }
        // Check to see if processing should continue.
        if (updateCompany == null || selectCompanyName == null) {
            try {
                log.closeConnection();
            } catch (LoggerException ex) {
                Logger.getLogger(AuthenticationServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;  // EARLY EXIT: Can not continue.
        }
        res.setContentType("text/xml");
        String company = req.getParameter("company").replaceAll("'", "\\\\'");
        String email = req.getParameter("email");
        String ipFrom = req.getRemoteAddr();
        try {
            updateCompany.setString(1, company);
            updateCompany.setString(2, email);
            updateCompany.setString(3, ipFrom);
            boolean ret = updateCompany.execute();
            logger.fatal(ipFrom);
            logger.fatal(email);
            if (ret) {
                res.getWriter().write("<response update='ok' />");
            } else {
                res.getWriter().write("<response update='error' />");
            }
        } catch (SQLException e2) {
            try {
                res.getWriter().write("<error>" + e2.toString() + "</error>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                log.closeConnection();
            } catch (LoggerException ex) {
                Logger.getLogger(AuthenticationServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 
     *  Entry point of the servlet
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        res.setContentType("text/xml");
        HttpSession session = req.getSession(true);
        String passwordInput = req.getParameter("password");
        String newPassword = req.getParameter("chgPassword");
        String getIsAdmin = req.getParameter("isAdmin");
        String logout = req.getParameter("logout");
        String ipFrom = req.getRemoteAddr();
        String company = null;
        try {
            InetAddress address = InetAddress.getByName(ipFrom);
            if (address instanceof Inet6Address) {
                if (address.isLoopbackAddress()) {
                    ipFrom = "127.0.0.1";
                } else {
                    ipFrom = "null";
                }
            }
        } catch (UnknownHostException e) {
        }

        if (ipFrom != null && !ipFrom.equals("null")) {
            Log log = new Log();
            try {
                PreparedStatement selectCompanyName = null;
                Connection con = log.getConnection();
                selectCompanyName = con.prepareStatement(
                        "SELECT company_name,email FROM ip where ip = ? ; ");
                selectCompanyName.setString(1, ipFrom);
                ResultSet result = selectCompanyName.executeQuery();
                if (result.next()) {
                    company = result.getString(1).replaceAll("'", "&quot;");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (LoggerException e) {
                e.printStackTrace();
            } finally {
                try {
                    log.closeConnection();
                } catch (LoggerException ex) {
                    Logger.getLogger(AuthenticationServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        String pageNumber = (String) session.getAttribute("page");
        String numberResultsByPage = (String) session.getAttribute("numberResultsByPage");
        session.setAttribute("isAdmin", true);  // BHT (HACK).

        // DISABLED (BHT)
        // readFile();
        if (passwordInput != null) {
            try {
                if (passwordRead.equals(passwordInput)) {
                    session.setAttribute("isAdmin", true);
                    if (newPassword != null) {
                        FileWriter fstream;
                        try {
                            fstream = new FileWriter(passwordFile);
                            BufferedWriter out = new BufferedWriter(fstream);
                            out.write(newPassword);
                            out.close();
                            res.getWriter().write(
                                    "<response isChanged='true' isAuthenticated='true' page ='" + pageNumber + "' numberResultByPage='" + numberResultsByPage + "'></response>");

                        } catch (IOException e) {
                            try {
                                res.getWriter().write(
                                        "<response isChanged='false' page ='" + pageNumber + "' numberResultByPage='" + numberResultsByPage + "' ip='" + ipFrom + "' +" + " company='" + company + "' > " + e.getMessage() + "</response>");
                            } catch (IOException e1) {
                            }
                        }

                    } else {
                        res.getWriter().write(
                                "<response isAuthenticated='true' page ='" + pageNumber + "' numberResultByPage='" + numberResultsByPage + "'></response>");
                    }
                } else {
                    res.getWriter().write(
                            "<response isAuthenticated='false' ip='" + ipFrom + "' page ='" + pageNumber + "' numberResultByPage='" + numberResultsByPage + "'></response>");
                }
            } catch (Exception e) {
            }
        } else if (getIsAdmin != null && getIsAdmin.equals("get")) {
            try {
                Boolean isAuthenticated = (Boolean) session.getAttribute("isAdmin");
                String sysType = (String) session.getAttribute("systemType");

                if (sysType == null) {
                    sysType = "new";
                }

                if (isAuthenticated != null && isAuthenticated.booleanValue()) {
                    res.getWriter().write(
                            "<response isAuthenticated='true' systemType='" + sysType + "' page ='" + pageNumber + "' numberResultByPage='" + numberResultsByPage + "'></response>");
                /*} else if (authorizedIPs.contains(ipFrom)) {
                res.getWriter().write(
                "<response isAuthenticated='true'" + " page ='" + pageNumber + "' numberResultByPage='" + numberResultsByPage + "'></response>");
                session.setAttribute("isAdmin", true);
                }*/
                } else {
                    res.getWriter().write(
                            "<response isAuthenticated='false' ip='" + ipFrom + "' systemType ='" + sysType + "' company    ='" + company + "' page       ='" + pageNumber + "' numberResultByPage='" + numberResultsByPage + "'></response>");
                }
            } catch (IOException e) {
            }
        } else if (logout != null && logout.equals("yes")) {
            session.invalidate();
            try {
                res.getWriter().write("<response/>");
            } catch (IOException e) {
            }
        }
    }
}
