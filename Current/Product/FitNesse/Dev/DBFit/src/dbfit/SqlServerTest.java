package dbfit;

import dbfit.environment.*;
public class SqlServerTest extends DatabaseTest {
	public SqlServerTest(){
		super(new SqlServerEnvironment());
	}
	public void dbfitDotSqlServerTest() {
		// required by fitnesse release 20080812
	}

}
