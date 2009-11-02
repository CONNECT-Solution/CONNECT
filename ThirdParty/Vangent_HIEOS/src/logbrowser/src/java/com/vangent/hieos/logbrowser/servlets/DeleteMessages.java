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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DeleteMessages extends HttpServlet {

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
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        Log log = null;
        HttpSession session = req.getSession(true);

        String systemType = req.getParameter("system");
        if (systemType == null) {
            systemType = (String) session.getAttribute("systemType");
        }
        String date = req.getParameter("date");
        String date1 = req.getParameter("date1");
        String date2 = req.getParameter("date2");
        log = new Log();
        if (session.getAttribute("isAdmin") != null && (Boolean) session.getAttribute("isAdmin"))// if IS ADMIN .......ADMIN
        {
            if (req.getParameter("number") != null) {
                int number = new Integer(req.getParameter("number"));
                for (int i = 0; i < number; i++) {
                    try {
                        log.deleteMessage(req.getParameter("message" + i));
                    } catch (LoggerException e) {
                    }
                }
            } else if (date != null || (date1 != null && date2 != null)) {
                try {
                    if (date != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                        sdf.parse(date);
                        ResultSet result = log.getConnection().createStatement().executeQuery("select messageid from main where timereceived like '" + date + "%';");
                        while (result.next()) {
                            log.deleteMessage(result.getString(1));
                        }
                    } else if (date1 != null && date2 != null) {
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
                        Date dateDate1 = sdf1.parse(date1);

                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
                        Date dateDate2 = sdf2.parse(date2);

                        if (dateDate1.before(dateDate2)) {
                            GregorianCalendar calendar = new GregorianCalendar();
                            calendar.setTime(dateDate2);
                            calendar.add(GregorianCalendar.MONTH, 1);
                            sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                            String sql = "select messageid from main where timereceived >='" + sdf1.format(dateDate1) + "' and timereceived <'" + sdf1.format(calendar.getTime()) + "' ";
                            System.out.println(sql);
                            ResultSet result = log.getConnection().createStatement().executeQuery(sql);

                            while (result.next()) {
                                log.deleteMessage(result.getString(1));
                            }

                        } else if (dateDate1.after(dateDate2)) {
                            GregorianCalendar calendar = new GregorianCalendar();
                            calendar.setTime(dateDate1);
                            calendar.add(GregorianCalendar.MONTH, 1);
                            sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                            String sql = "select messageid from main where timereceived >='" + sdf1.format(dateDate2) + "' and timereceived <'" + sdf1.format(calendar.getTime()) + "' ";
                            System.out.println(sql);
                            ResultSet result = log.getConnection().createStatement().executeQuery(sql);

                            while (result.next()) {
                                //System.out.println( result.getString( 1 ) ) ;
                                log.deleteMessage(result.getString(1));
                            }
                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                            sdf.parse(date1);
                            ResultSet result = log.getConnection().createStatement().executeQuery("select messageid from main where timereceived like '" + date1 + "%';");
                            while (result.next()) {
                                //System.out.println( result.getString( 1 ) ) ;
                                log.deleteMessage(result.getString(1));
                            }
                        }
                    }
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                } catch (ParseException e) {
                    System.err.println(e.getMessage());
                } catch (LoggerException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    try {
                        log.closeConnection();
                    } catch (LoggerException ex) {
                        Logger.getLogger(DeleteMessages.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        try {
            log.closeConnection();  // Better cleanup.
        } catch (LoggerException ex) {
            Logger.getLogger(DeleteMessages.class.getName()).log(Level.SEVERE, null, ex);
        }
        res.setContentType("text/xml");
        try {
            res.getWriter().write("<response></response>");
        } catch (IOException e) {
        }
    }
}
