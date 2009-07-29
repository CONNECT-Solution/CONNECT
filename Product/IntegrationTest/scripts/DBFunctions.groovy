package nhinc
import groovy.sql.Sql;

class DBFunctions
{
   DBFunctions() {}
   
   static void clearTable(host, port, dbName, user, password, table) {
   
   def sqlConnect = 'jdbc:mysql://' + host + ':' + port + '/' + dbName;
   def sqlExecute = 'delete from ' + table;
   def sql = Sql.newInstance(sqlConnect, user, password, "com.mysql.jdbc.Driver");
   def result = sql.execute(sqlExecute);
   }
}