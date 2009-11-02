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
package com.vangent.hieos.logbrowser.util;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TableModel extends AbstractTableModel
        implements TableModelListener {

    private Vector<Vector<Object>> dataVector = new Vector<Vector<Object>>();
    private Vector<String> headerVector = new Vector<String>();
    private String xmlString;
    private Map fieldsAndFormats = null;
    private Log log = LogFactory.getLog(this.getClass());
    private static final long serialVersionUID = 1L;

    public TableModel() throws SQLException {
    }

    /* AMS - FIXME

    Description: Changed the CTOR to accept a Map, fieldsAndFormats, implying that the TableModel
    now creates formatted data in the dataVector.

    Fix:
    Use a different data structure instead of Map.

     */
    public TableModel(String sqlRequest, Map fieldsAndFormats, Connection c) throws SQLException {
        this.fieldsAndFormats = fieldsAndFormats;
        ResultSet statementResult;
        log.debug("TABLE_MODEL_SYSLOG: database connection created\n");

        Statement statement = c.createStatement();
        log.debug("TABLE_MODEL_SYSLOG: statement created\n");
        statementResult = statement.executeQuery(sqlRequest);
        log.debug("TABLE_MODEL_SYSLOG: Query executed\n");
        log.debug("<--" + new GregorianCalendar().getTime() + " TableModel close Database \n");

        ResultSetMetaData metaData = statementResult.getMetaData();
        int columnCount = metaData.getColumnCount();

        dataVector = new Vector<Vector<Object>>();
        headerVector = new Vector<String>();

        log.debug("TABLE_MODEL_SYSLOG: colomn count : " + columnCount + "\n");
        log.debug("TABLE_MODEL_SYSLOG: Table--------------------------------------");
        for (int i = 0; i < columnCount; i++) {
            headerVector.add(metaData.getColumnName((i + 1)));
            log.debug(metaData.getColumnName((i + 1)) + "\t");
        }

        while (statementResult.next()) {
            Vector<Object> tmp = new Vector<Object>(columnCount);
            for (int j = 0; j < columnCount; j++) {
                String columnName = getColumnName(j);
                Object columnData = statementResult.getObject(columnName);
                columnData = getFormattedData(columnName, columnData);
                tmp.add(columnData);
                log.debug(columnData + "\t");
            }
            log.debug("\n");
            dataVector.add(tmp);
        }
    }

    /**
     *
     * @return
     */
    public int getColumnCount() {
        return headerVector.size();
    }

    /**
     *
     * @return
     */
    public int getRowCount() {
        return dataVector.size();
    }

    /**
     * 
     * @param col
     * @return
     */
    public String getColumnName(int col) {
        return (String) headerVector.get(col);
    }

    /**
     *
     * @param c
     * @return
     */
    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((Vector) (dataVector.get(rowIndex))).get(columnIndex);
    }

    /**
     *
     * @return
     */
    public Vector<Vector<Object>> getDataVector() {
        return dataVector;
    }

    /**
     *
     * @return
     */
    public Vector getHeaderVector() {
        return headerVector;
    }

    /**
     *
     * @param row
     * @param col
     * @return
     */
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
     *
     * @param e
     */
    public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
    }

    Color curColor;

    /**
     *
     * @return
     */
    public String getXmlString() {
        return xmlString;
    }

    /**
     *
     * @param fieldName
     * @param fieldData
     * @return
     */
    private Object getFormattedData(String fieldName, Object fieldData) {
        Object formattedData = fieldData;
        Format fmt = (Format) fieldsAndFormats.get(fieldName);
        if (fmt != null) {
            /* AMS - FIXME - Cannot assume that fieldData is always a string.
            Will work for now */

            formattedData = (String) fmt.format(fieldData);
        }
        return formattedData;
    }
}
