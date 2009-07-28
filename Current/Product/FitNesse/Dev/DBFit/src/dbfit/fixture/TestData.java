package dbfit.fixture;

import java.sql.Connection;
import java.util.ArrayList;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTableIterator;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import dbfit.environment.DBEnvironment;
import dbfit.environment.DbEnvironmentFactory;
import dbfit.util.FileHelper;
import dbfit.util.Log;
import fit.ColumnFixture;

/**
 * Fixture for integrating FitNesse with dbUnit.
 * 
 * @author Björn Beskow, Callista Enterprise AB
 * @author Pål Brattberg, Acando AB
 * @since 1.1.2
 */
public class TestData extends ColumnFixture {
	/**
	 * Filter table names from {@link IDataSet}.
	 */
	public class FilteredDataSet implements IDataSet {
		private IDataSet delegate;
		private String[] tableNames;

		public FilteredDataSet(IDataSet dataSet, String tableName) {
			this.delegate = dataSet;
			this.tableNames = tableName.split(",\\s+");
		}

		public ITable getTable(String s) throws DataSetException {
			return delegate.getTable(s);
		}

		public ITableMetaData getTableMetaData(String s)
				throws DataSetException {
			return delegate.getTableMetaData(s);
		}

		public String[] getTableNames() throws DataSetException {
			return tableNames;
		}

		public ITable[] getTables() throws DataSetException {
			ArrayList<ITable> tableList = new ArrayList<ITable>();
			for (String tname : getTableNames()) {
				tableList.add(delegate.getTable(tname));
			}
			return tableList.toArray(new ITable[0]);
		}

		public ITableIterator iterator() throws DataSetException {
			return new DefaultTableIterator(getTables());
		}

		public ITableIterator reverseIterator() throws DataSetException {
			return new DefaultTableIterator(getTables());
		}
	}

	private DBEnvironment dbEnvironment;
	private String fileType = "xls";
	public String operation;
	public String file;
	public String schema;
	public String table = "";
	public String tables = "";

	public TestData() {
		dbEnvironment = DbEnvironmentFactory.getDefaultEnvironment();
		Log.log("Using default file type: %s", fileType);
	}

	public TestData(DBEnvironment environment, String fileType) {
		this.dbEnvironment = environment;
		this.fileType = fileType;
	}

	/**
	 * Perform a DbUnit operation on a dataset from an external file.
	 */
	@Override
	public void execute() throws Exception {
		IDataSet dataSet = null;
		if ("xls".equalsIgnoreCase(fileType)) {
			dataSet = new XlsDataSet(FileHelper.getFileAsStream(file));
		} else if ("xml".equalsIgnoreCase(fileType)) {
			dataSet = new FlatXmlDataSet(FileHelper.getFileAsStream(file));
		} else {
			throw new IllegalArgumentException(String.format(
					"Cannot handle files of type '%s'. "
							+ "Use one of 'xls' or 'xml'.", fileType));
		}
		// make sure we only use specified table names
		if (tables != null && !"".equals(tables.trim())) {
			dataSet = new FilteredDataSet(dataSet, tables);
		} else if (table != null && !"".equals(table.trim())) {
			dataSet = new FilteredDataSet(dataSet, table);
		}
		performOperation(operation, dataSet);
	}

	protected void performOperation(String operation, IDataSet dataSet)
			throws Exception {
		Log.log("Running operation %s with content from file %s", operation,
				file);
		performOperation(getDbUnitOperation(operation), dataSet);
	}

	protected void performOperation(DatabaseOperation operation,
			IDataSet dataSet) throws Exception {
		ReplacementDataSet replacement = new ReplacementDataSet(dataSet);
		replacement.addReplacementObject("null", null);
		operation.execute(getConnection(dbEnvironment.getConnection()),
				replacement);
	}

	protected DatabaseOperation getDbUnitOperation(String operation) {
		String upcaseOperation = operation.toUpperCase();
		if ("CLEAN_INSERT".equals(upcaseOperation)) {
			return DatabaseOperation.CLEAN_INSERT;
		} else if ("INSERT".equals(upcaseOperation)) {
			return DatabaseOperation.INSERT;
		} else if ("UPDATE".equals(upcaseOperation)) {
			return DatabaseOperation.UPDATE;
		} else if ("REFRESH".equals(upcaseOperation)) {
			return DatabaseOperation.REFRESH;
		} else if ("DELETE".equals(upcaseOperation)) {
			return DatabaseOperation.DELETE;
		} else if ("DELETE_ALL".equals(upcaseOperation)) {
			return DatabaseOperation.DELETE_ALL;
		} else if ("TRUNCATE_TABLE".equals(upcaseOperation)) {
			return DatabaseOperation.TRUNCATE_TABLE;
		} else {
			throw new IllegalArgumentException("Invalid operation " + operation);
		}
	}

	protected IDatabaseConnection getConnection(Connection jdbcConnection)
			throws Exception {
		IDatabaseConnection connection = null;
		if (schema != null && !"".equals(schema.trim())) {
			Log.log("Connection using schema %s", schema);
			connection = new DatabaseConnection(jdbcConnection, schema);
		} else {
			Log.log("Connection using no schema");
			connection = new DatabaseConnection(jdbcConnection);
		}
		return connection;
	}
}