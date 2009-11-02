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

import com.vangent.hieos.logbrowser.util.TableModel;
import com.vangent.hieos.logbrowser.util.TableSorter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

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
 *  <li>valuei  :  value number i . These values are the result of the user's choice in the web interface </li>
 * </ul>
 * 
 * @author jbmeyer
 *
 */
public class GetTableServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private org.apache.commons.logging.Log logger = LogFactory.getLog(this.getClass());
    private final int MAX_RESULTS_BY_PAGE = 50;
    private TableModel tableModel;
    private TableSorter sorter;
    private ServletConfig currentConfig;
    private HashMap<String, String> map;
    private boolean isAdmin;
    private String currentIP;

    /**
     *
     * @param config
     * @throws javax.servlet.ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        currentConfig = config;
    }

    /**
     *
     * @param req
     * @param res
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        Log log = new Log();
        try {
            HttpSession session = req.getSession(true);
            session.setAttribute("systemType", "new");
            //String currentSqlCommand_ = (String)session.getAttribute("currentSqlCommand") ;
            Boolean isAdmin_ = (Boolean) session.getAttribute("isAdmin");

            if (isAdmin_ == null) {
                isAdmin = false;
            } else {
                isAdmin = isAdmin_.booleanValue();
            }
            currentIP = req.getRemoteAddr();
            String sort = req.getParameter("sort");
            String option = req.getParameter("option");
            String page = req.getParameter("page");
            if (page == null) {
                page = (String) session.getAttribute("page");
            }

            String numberResultsByPage = req.getParameter("nbResByPage");
            String currentSqlCommand;
            Integer numberOfResults = -1;
            // Write XML to response.
            res.setContentType("text/javascript");
            if (option != null && option.equals("count")) {
                countResults(session, log.getConnection());
                numberOfResults = (Integer) session.getAttribute("numberOfResults");
                numberResultsByPage = (String) session.getAttribute("numberResultsByPage");
                page = (String) session.getAttribute("page");
                StringBuffer buffer = new StringBuffer();
                buffer.append("{ \"result\" : \n");
                buffer.append(new JSONStringer().object().key("pageNumber").value(page).key("numberResultsByPage").value(numberResultsByPage).key("numberOfResults").value(numberOfResults).endObject().toString());

                buffer.append("}");
                res.getWriter().println(buffer.toString());
            } else if (sort == null) {

                getOptions(req, session);
                sqlCommandProcessing(session, (Integer) session.getAttribute("optionNumber"), page, numberResultsByPage);
                currentSqlCommand = (String) (session.getAttribute("currentSqlCommand"));
                logger.debug("GetTableServlet: sqlRequest : " + currentSqlCommand + "\n");
                session.setAttribute("page", page);
                session.setAttribute("numberResultsByPage",
                        numberResultsByPage);

                /* AMS - FIXME.

                Description:
                The response fields returned by the query, currentSqlCommand, are  formatted using
                fieldsAndFormats - a map where field name is the key and the referenced object is a java.text.Format
                object. Currently, the "Timestamp" field is being formatted using "java.text.DateFormat".

                Fix:
                1) Setting up fields and formats should not be done here. It should be done in the init method.
                2) fieldsAndFormats are related to the query and they are currently not in cohesive units -
                fieldsAndFormats is being set up in this class whereas the query is being pulled from
                web.xml's init-param.

                 */
                Map fieldsAndFormats = new HashMap();
                Format fmt = new SimpleDateFormat("EEE d MMM - HH:mm:ss");
                fieldsAndFormats.put("Timestamp", fmt);

                Connection con = log.getConnection();
                executeCurrentSqlCommand(con, currentSqlCommand, fieldsAndFormats);
                res.getWriter().write(
                        toJSon(-1, -2));

            } else if (sort != null) {
                res.getWriter().write(sortColomn(sort));
            } else {
                throw new Exception(
                        "Error case unknown not sort either display table");
            }
        } catch (SQLException e) {
            getError(e, res);
            logger.fatal(e.getMessage());
        } catch (FileNotFoundException e) {
            getError(e, res);
            logger.fatal(e.getMessage());
        } catch (NumberFormatException e) {
            getError(e, res);
            logger.fatal(e.getMessage());
        } catch (IOException e) {
            getError(e, res);
            logger.fatal(e.getMessage());
        } catch (Exception e) {
            getError(e, res);
            logger.fatal(e.getMessage());
        } finally {
            try {
                log.closeConnection();
            } catch (LoggerException ex) {
                Logger.getLogger(GetTableServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * <b>sqlCommandProcessing</b><br/>
     * Create the sql command beginning with the sql command specified in the web.xml file and applying all the options <br/>
     * specified by the user. This command also apply a limit in the results to avoid to overload the server
     *
     */
    private void sqlCommandProcessing(HttpSession session, int optionNumberInt, String page, String numberResultsByPage) {
        String limitOffset = null;
        int parameterNumber = 1;
        String currentSqlCommand = null;
        reInitSqlCommand(session);
        String lastPartCommand = new String();
        currentSqlCommand = (String) session.getAttribute("currentSqlCommand");
        if (map != null && optionNumberInt > 0) {
            String commandTemp = new String();
            if (currentSqlCommand.toLowerCase().indexOf("where") > -1) {
                if (currentSqlCommand.toLowerCase().indexOf("order") > -1) {
                    commandTemp = currentSqlCommand.substring(0,
                            currentSqlCommand.toLowerCase().indexOf("order"));
                    lastPartCommand = currentSqlCommand.substring(currentSqlCommand.toLowerCase().indexOf(
                            "order"));
                    lastPartCommand = lastPartCommand.replace(';', ' ');
                } else if (currentSqlCommand.indexOf(";") > -1) {
                    commandTemp = currentSqlCommand.substring(0,
                            currentSqlCommand.indexOf(";"));
                }
                commandTemp += " AND ";
            } else {
                commandTemp += " WHERE ";
            }
            while (parameterNumber <= optionNumberInt) {
                if (map.containsKey("option" + parameterNumber)) {
                    if (map.containsKey("and-or" + parameterNumber)) {
                        commandTemp += " " + map.get("and-or" + parameterNumber) + " ";
                    }

                    String parameterName = map.get("option" + parameterNumber).toString();

                    if (parameterName.equals("ip")) {
                        commandTemp += " main.ip LIKE '%" + map.get("value" + parameterNumber).toString() + "%' ";

                    } else if (parameterName.equals("pass")) {
                        commandTemp += " main.pass = " + map.get("value" + parameterNumber).toString() + " ";

                    } else if (parameterName.equals("test")) {
                        String value = map.get("value" + parameterNumber).toString();
                        commandTemp += "  main.test LIKE '%" + value + "%' ";
                    } else if (parameterName.equals("company")) {
                        String value = map.get("value" + parameterNumber).toString();
                        commandTemp += "  ip.company_name LIKE '%" + value + "%' ";
                    } else if (parameterName.equals("date1")) {
                        String date1 = map.get("value" + parameterNumber).toString().trim();
                        SimpleDateFormat sdf = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        java.util.Date d1;
                        try {
                            d1 = sdf.parse(date1);
                            if (map.containsKey("option" + (parameterNumber + 1)) && map.get(
                                    "option" + (parameterNumber + 1)).toString().equals("date2")) {
                                if (map.containsKey("value" + (parameterNumber + 1))) {
                                    String date2 = map.get(
                                            "value" + (parameterNumber + 1)).toString().trim();
                                    java.util.Date d2 = sdf.parse(date2);
                                    if (d2.getTime() > d1.getTime()) {
                                        commandTemp += " timereceived > '" + date1 + "%' and  timereceived < '" + date2 + "%' ";
                                    } else if (d2.getTime() < d1.getTime()) {
                                        commandTemp += " timereceived < '" + date1 + "%' and  timereceived > '" + date2 + "%' ";
                                    } else {
                                        commandTemp += " timereceived like '" + date1 + "%' ";
                                    }

                                    map.remove("value" + (parameterNumber + 1));
                                    map.remove("option" + (parameterNumber + 1));
                                    if (map.containsKey("and-or" + (parameterNumber + 1))) {
                                        map.remove("and-or" + (parameterNumber + 1));
                                    }
                                }
                            } else {
                                commandTemp += " timereceived like '" + date1 + "%' ";
                            }

                        } catch (ParseException e) {
                        }
                    } else if (parameterName.equals("date2")) {
                        String date1 = map.get("value" + parameterNumber).toString();

                        commandTemp += " timereceived like '" + date1 + "%' ";

                    } else if (parameterName.equals("date")) {
                        String value = map.get("value" + parameterNumber).toString();
                        GregorianCalendar today = new GregorianCalendar();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        if (value.equals("today")) {
                            commandTemp += " timereceived like '" + dateFormat.format(today.getTime()) + "%' ";
                        } else if (value.equals("yesterday")) {
                            today.roll(Calendar.DAY_OF_MONTH, -1);
                            commandTemp += " timereceived like '" + dateFormat.format(today.getTime()) + "%' ";
                        } else if (value.equals("2days")) {
                            today.roll(Calendar.DAY_OF_MONTH, -2);
                            commandTemp += " timereceived like '" + dateFormat.format(today.getTime()) + "%' ";
                        } else if (value.equals("3days")) {
                            today.roll(Calendar.DAY_OF_MONTH, -3);
                            commandTemp += " timereceived like '" + dateFormat.format(today.getTime()) + "%' ";
                        }
                    }
                }
                parameterNumber++;
            }
            currentSqlCommand = commandTemp;
        }

        if (page != null) {
            int pageNumber = new Integer(page).intValue();
            if (numberResultsByPage != null) {

                int nbResByPage = new Integer(numberResultsByPage).intValue();

                limitOffset = " LIMIT " + nbResByPage + " OFFSET " + (nbResByPage * pageNumber) + " ;";
            } else {
                limitOffset = " LIMIT " + MAX_RESULTS_BY_PAGE + " OFFSET " + (MAX_RESULTS_BY_PAGE * pageNumber) + " ;";
            }

        } else {
            if (numberResultsByPage != null) {
                int nbResByPage = new Integer(numberResultsByPage).intValue();
                limitOffset = " LIMIT " + nbResByPage + " ;";
            } else {
                limitOffset = " LIMIT " + MAX_RESULTS_BY_PAGE + " ;";
            }
        }
        logger.debug("GetTableServlet: sqlRequest before treatment: >" + currentSqlCommand + "<\n");
        System.out.println("LogBrowser SQL (before treatment): " + currentSqlCommand);
        currentSqlCommand = currentSqlCommand.replace(';', ' ');
        if (currentSqlCommand.toLowerCase().indexOf(" limit") > -1) {
            currentSqlCommand = currentSqlCommand.substring(0,
                    currentSqlCommand.toLowerCase().indexOf(" limit"));
        }
        currentSqlCommand += lastPartCommand + " " + limitOffset;
        session.setAttribute("currentSqlCommand", currentSqlCommand);
        logger.debug(currentSqlCommand);
        System.out.println("-- page: " + page);
        System.out.println("LogBrowser SQL (after treatment): " + currentSqlCommand);
    }

    /**
     * 
     * @param sqlCommand
     * @param fieldsAndFormats
     * @throws com.vangent.hieos.logbrowser.log.db.LoggerException
     */
    private void executeCurrentSqlCommand(Connection con, String sqlCommand, Map fieldsAndFormats) throws SQLException {
        tableModel = new TableModel(sqlCommand, fieldsAndFormats, con);
        sorter = new TableSorter(tableModel);
    }

    /**
     *
     * @param session
     */
    private void reInitSqlCommand(HttpSession session) {
        session.setAttribute("currentSqlCommand", currentConfig.getInitParameter("sqlCommand"));
    }

    /**
     *
     * Use the current sql command with a Count(*) to return the number of results of the current <br/>
     * sql command
     * @throws SQLException
     */
    private void countResults(HttpSession session, Connection con) throws SQLException {
        String currentSqlCommand = (String) session.getAttribute("currentSqlCommand");
        int fromPosition = currentSqlCommand.toLowerCase().indexOf("from");
        String secondPart = currentSqlCommand.substring(fromPosition);

        int tmp;
        if ((tmp = secondPart.toLowerCase().indexOf("order")) != -1) {
            secondPart = secondPart.substring(0, tmp);
        }
        if ((tmp = secondPart.toLowerCase().indexOf("group")) != -1) {
            secondPart = secondPart.substring(0, tmp);
        }
        if ((tmp = secondPart.toLowerCase().indexOf("limit")) != -1) {
            secondPart = secondPart.substring(0, tmp);
        }

        // AMS - MySQL does not like spaces between COUNT and (*)
        String SQLCommandCountStar = "SELECT COUNT(*) " + secondPart;

        logger.debug("GetTableServlet: SQLCommandCountStar:\n" + SQLCommandCountStar);

        Statement st = con.createStatement();
        ResultSet resultSet = st.executeQuery(SQLCommandCountStar);
        resultSet.next();
        int numberOfResults = resultSet.getInt(1);
        session.setAttribute("numberOfResults", new Integer(numberOfResults));
    }

    /**
     *
     * @param sort, the column number to sort
     * @return the new xml string of the array sorted
     * @throws NumberFormatException
     *
     * The sort of this array is cyclic : it begins by UNSORTED, then if the user click again the array will be <br />
     * sorted ASCENDING, then DESCENDING, and finally UNSORTED.
     */
    private String sortColomn(String sort) throws NumberFormatException {
        if (tableModel != null && sorter != null) {
            int sortingColumn = new Integer(sort).intValue();
            int sortingStatus = -2;

            logger.debug("GetTableServlet: Sorting procedure\n");
            logger.debug("GetTableServlet: Sort column :" + sortingColumn);
            if (sorter.getSortingStatus(sortingColumn) == TableSorter.ASCENDING) {
                for (int i = 0; i < sorter.getColumnCount(); i++) {
                    sorter.setSortingStatus(i, TableSorter.NOT_SORTED);
                }
                sorter.setSortingStatus(sortingColumn, TableSorter.DESCENDING);
                sortingStatus = TableSorter.DESCENDING;
            } else if (sorter.getSortingStatus(sortingColumn) == TableSorter.DESCENDING) {
                for (int i = 0; i < sorter.getColumnCount(); i++) {
                    sorter.setSortingStatus(i, TableSorter.NOT_SORTED);
                }
                sorter.setSortingStatus(sortingColumn, TableSorter.NOT_SORTED);
                sortingStatus = TableSorter.NOT_SORTED;
            } else if (sorter.getSortingStatus(sortingColumn) == TableSorter.NOT_SORTED) {
                for (int i = 0; i < sorter.getColumnCount(); i++) {
                    sorter.setSortingStatus(i, TableSorter.NOT_SORTED);
                }
                sorter.setSortingStatus(sortingColumn, TableSorter.ASCENDING);
                sortingStatus = TableSorter.ASCENDING;
            }
            logger.debug("GetTableServlet: sorting status " + sortingStatus);

            return toJSon(sortingColumn, sortingStatus);
        }
        return null;
    }

    /**
     * Allows to get all options given by the user and passed to the server with the post method
     * @param req
     */
    private void getOptions(HttpServletRequest req, HttpSession session) {
        String optionNumber = req.getParameter("optionsNumber");
        int optionNumberInt = 0;
        if (optionNumber != null) {
            map = new HashMap<String, String>();
            optionNumberInt = new Integer(optionNumber).intValue();
            for (int i = 1; i < optionNumberInt + 1; i++) {
                if (req.getParameter("option" + i) != null) {
                    map.put("option" + i, req.getParameter("option" + i));
                }
                if (req.getParameter("value" + i) != null) {
                    map.put("value" + i, req.getParameter("value" + i));
                }

                if (i > 1 && req.getParameter("and-or" + i) != null) {
                    map.put("and-or" + i, req.getParameter("and-or" + i));
                }
            }
        }

        if (!isAdmin && currentIP != null && !currentIP.equals("")) {
            optionNumberInt++;
            if (!map.containsValue("ip")) {
                map.put("option" + optionNumberInt, "ip");
                map.put("value" + optionNumberInt, currentIP);
                if (optionNumberInt > 1) {
                    map.put("and-or" + optionNumberInt, "and");
                }
            }
        }
        session.setAttribute("optionNumber", (Integer) optionNumberInt);
    }

    /**
     *
     * @param column
     * @param sortingStatus
     * @return
     */
    private String toJSon(int column, int sortingStatus) {
        try {
            JSONObject response = new JSONObject();
            JSONObject content = new JSONObject();
            JSONArray array = new JSONArray();
            if (column == -1 || sortingStatus == -2) {
                array.put(tableModel.getHeaderVector());
            } else if (column > -1 && sortingStatus > -2 && sortingStatus < 2 /*-1,0 or 1 */) {
                Vector<String> vectorCopy = (Vector<String>) tableModel.getHeaderVector().clone();
                for (int header = 0; header < tableModel.getHeaderVector().size(); header++) {
                    if (header == column) {
                        switch (sortingStatus) {
                            case -1:
                                vectorCopy.set(header, vectorCopy.elementAt(header) + " &#8595;");
                                break;
                            case 0:
                                vectorCopy.set(header, vectorCopy.elementAt(header) + " &#8593;&#8595;");
                                break;
                            case 1:
                                vectorCopy.set(header, vectorCopy.elementAt(header) + " &#8593;");
                                break;
                        }
                    }
                }
                array.put(vectorCopy);
            }
            for (int row = 0; row < tableModel.getDataVector().size(); row++) {
                array.put((Vector<Object>) tableModel.getDataVector().get(row));
            }
            content.put("table", array);
            content.put("isAdmin", new Boolean(isAdmin).toString());
            response.put("result", content);
            return response.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
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
            response.setContentType("text/javascript");
            StringBuffer toPrint = new StringBuffer();
            StringBuffer toPrint2 = new StringBuffer();
            toPrint.append("{ \"result\":");

            JSONStringer stringer = new JSONStringer();
            stringer.object();
            stringer.key("error");
            stringer.value(e.getClass().toString() + ":" + e.getMessage());
            stringer.endObject();
            toPrint.append(stringer.toString());
            toPrint2.append(e.getClass().toString() + ":" + e.getMessage() + "\n");

            StackTraceElement[] stack = e.getStackTrace();
            for (int i = 0; i < stack.length; i++) {
                toPrint2.append(stack[i].toString() + "\n");

            }
            toPrint.append("}");
            print.write(toPrint.toString());
            logger.fatal(toPrint2.toString());
        } catch (IOException e1) {
        } catch (JSONException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
    }
}
