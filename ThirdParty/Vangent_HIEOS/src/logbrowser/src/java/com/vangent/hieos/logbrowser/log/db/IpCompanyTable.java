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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class storing a pair &lt; IP Address , Company Name &gt; in database.
 *
 * @author jbmeyer
 * @author Bernie Thuman (BHT): Streamlined, added more comments, removed "on-the-fly" thinking.
 *
 */
public class IpCompanyTable extends AbstractLogTable {

    // Column names.
    public static String IP_ADDRESS = "ip";
    public static String COMPANY = "company_name";
    public static String EMAIL = "email";
    public static String TABLE_NAME = "ip";

    // To support SQL commands (BHT: removed static usage).
    public String writeSqlCommand = null;
    public String readSqlCommand = null;
    public String deleteMessageCommand = null;
    private PreparedStatement readPreparedStatement;
    private PreparedStatement writePreparedStatement;
    private String updateSqlCommand2;

    // Internal variables.
    private String ipAddress;
    private String companyName;
    private String email;
    private PreparedStatement updatePreparedStatement2;

    /**
     * 
     */
    private IpCompanyTable() {
    }

    /**
     *
     * @param c
     * @throws java.sql.SQLException
     */
    public IpCompanyTable(Connection c) throws SQLException {
        tableName = TABLE_NAME;
        conn = c;
        readSqlCommand = "select " + IP_ADDRESS + " , " + COMPANY + "," + EMAIL + " FROM " + tableName + " where " + IP_ADDRESS + " = ? ;";
        writeSqlCommand = "insert into " + tableName + " values (?,? ,?);";
        updateSqlCommand2 = "UPDATE ip SET company_name = ? WHERE ip = ? ;";
        if (conn == null || conn.isClosed()) {
            throw new SQLException("Database null or closed");
        }
        readPreparedStatement = conn.prepareStatement(readSqlCommand);
        writePreparedStatement = conn.prepareStatement(writeSqlCommand);
        updatePreparedStatement2 = conn.prepareStatement(updateSqlCommand2);
    }

    /**
     * Query database for IP data.
     *
     * @param ipAddress
     * @throws java.sql.SQLException
     */
    void readToDB(String ipAddress) throws SQLException {
        if (ipAddress != null) {
            readPreparedStatement.setString(1, ipAddress);
            ResultSet res = readPreparedStatement.executeQuery();
            res.next();
            this.ipAddress = res.getString(1);
            this.companyName = res.getString(2);
        }
    }

    /**
     *
     * @throws java.sql.SQLException
     */
    void readToDB() throws SQLException {
        if (ipAddress != null) {
            readPreparedStatement.setString(1, ipAddress);
            ResultSet res = readPreparedStatement.executeQuery();
            res.next();
            ipAddress = res.getString(1);
            companyName = res.getString(2);
            email = res.getString(3);
        }
    }

    /**
     *  Write the ipAddress and the company Name in the table IP
     * @param inIPAdress
     * @param inCompany
     * @param inEmail
     * @return 0 if everything is ok , -1 if not
     * @throws LoggerException
     */
    private int writeToDB(String inIPAdress, String inCompany, String inEmail) throws LoggerException {
        if (writePreparedStatement != null) {
            try {
                writePreparedStatement.setString(1, inIPAdress);
                writePreparedStatement.setString(2, inCompany);
                writePreparedStatement.setString(3, inEmail);
                writePreparedStatement.execute();
            } catch (SQLException e) {
                throw new LoggerException("IpCompanyTable:writeToDB( String inIPAdress, String inCompany , String inEmail) : (" + e.getErrorCode() + " )" + e.getMessage());
            }
            return 0;
        } else {
            return -1;
        }
    }

    /**
     *  Write the ipAddress in the table IP with a default value "Unknown" for the company name
     * @param inIPAdress
     * @param inCompany
     * @return 0 if everything is ok , -1 if not
     * @throws LoggerException
     */
    int writeToDB(String inIPAdress) throws LoggerException {
        return writeToDB(inIPAdress, "Unknown", "Unknown");
    }

    /**
     * Write the ipAddress and the company Name in the table IP
     *
     * @param inIPAdress
     * @param inCompany
     * @return 0 if everything is ok , -1 if not
     * @throws LoggerException
     */
    int writeToDB(String inIPAdress, String inCompany) throws LoggerException {
        return writeToDB(inIPAdress, inCompany, "Unknown");
    }

    /**
     *  Write the current ipAddress  and company name in the table IP
     * @param inIPAdress
     * @param inCompany
     * @return 0 if everything is ok , -1 if not
     * @throws LoggerException
     */
    int writeToDB() throws LoggerException {
        return this.writeToDB(ipAddress, companyName, email);
    }

    /**
     *  Update the ipAddress  with company name in the table IP. If the ip address doesn't exist, it is created
     * @param inIPAdress
     * @param inCompany
     * @return 0 if everything is ok , -1 if not
     * @throws LoggerException
     */
    int updateIp(String inIPAdress, String inCompany) throws LoggerException {
        if (updatePreparedStatement2 != null) {
            try {
                if (ipExist(inIPAdress)) {
                    updatePreparedStatement2.setString(1, inCompany);
                    updatePreparedStatement2.setString(2, inIPAdress);
                    updatePreparedStatement2.execute();
                } else {
                    writeToDB(inIPAdress, inCompany);
                }
            } catch (SQLException sql) {
                throw new LoggerException("IPcompanyTable:updateIp( String inIPAdress, String inCompany ) problem : (" + sql.getErrorCode() + " )" + sql.getMessage());
            }
            return 0;
        } else {
            return -1;
        }
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * Test if the current ipAddress exists
     * @param ipAddress
     * @return
     * @throws LoggerException
     */
    public boolean ipExist(String ipAddress) throws LoggerException {
        try {
            if (conn == null || conn.isClosed()) {
                throw new LoggerException("Database null or closed");
            }
            String sqlRequest = "SELECT count(*) from " + TABLE_NAME + " where  " + IP_ADDRESS + "='" + ipAddress + "' ;";
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(sqlRequest);
            res.next();
            if (res.getInt(1) == 0) {
                return false;
            } else if (res.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new LoggerException("IPcompanyTable:ipExist( String ipAddress ) problem : (" + e.getErrorCode() + " )" + e.getMessage());
        }
        return false;
    }

    /**
     * 
     * @param c
     * @param ipAddress
     * @return
     * @throws LoggerException
     */
    public static boolean IpExist(Connection c, String ipAddress) throws LoggerException {
        try {
            if (c == null || c.isClosed()) {
                throw new LoggerException("Database null or closed");
            }
            String sqlRequest = "SELECT count(*) from " + TABLE_NAME + " where  " + IP_ADDRESS + "='" + ipAddress + "' ;";
            Statement st = c.createStatement();
            ResultSet res = st.executeQuery(sqlRequest);
            res.next();
            if (res.getInt(1) == 0) {
                return false;
            } else if (res.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new LoggerException("IPcompanyTable: IpExist ( Connection c , String ipAddress   ) problem : (" + e.getErrorCode() + " )" + e.getMessage());
        }
        return false;
    }
}
