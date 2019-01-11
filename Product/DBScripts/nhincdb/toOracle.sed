s/com.mysql.jdbc.Driver/oracle.jdbc.driver.OracleDriver/g
s/jdbc:mysql:\/\/localhost\/aggregator/jdbc:oracle:thin:@localhost:1521:XE/g
s/jdbc:mysql:\/\/localhost\/assigningauthoritydb/jdbc:oracle:thin:@localhost:1521:XE/g
s/jdbc:mysql:\/\/\/auditrepo/jdbc:oracle:thin:@localhost:1521:XE/g
s/jdbc:mysql:\/\/localhost\/patientcorrelationdb/jdbc:oracle:thin:@localhost:1521:XE/g
s/jdbc:mysql:\/\/localhost\/docrepository/jdbc:oracle:thin:@localhost:1521:XE/g
s/jdbc:mysql:\/\/localhost\/subscriptionrepository/jdbc:oracle:thin:@localhost:1521:XE/g
s/MySQLDialect/OracleDialect/g
