Step 1 - Launch MySQL Administration window and create a schema called patientcorrelationdb
Step 2 - Place the PatientCorrelationDB.sql file in c:\temp\ folder.
Step 3 - Go to MySQL bin directory and import the schema using the command below:
C:\Program Files\MySQL\MySQL Server 5.0\bin>mysql -uroot -pNHIE-Gateway patientcorrelationdb < c:\temp\PatientCorrelationDB.sql
Step 4 - Launch MySQL Administration window and create a user with name "epcuser" and password "epcpass"
Step 5 - Assign all the privilages and add PatientCorrelationSchema to for this user.

