Step 1 - Launch MySQL Administration window and create a schema called assigningauthoritydb
Step 2 - Place the asyncmsgsdb.sql file in c:\temp\ folder.
Step 3 - Go to MySQL bin directory and import the schema using the command below:
C:\Program Files\MySQL\MySQL Server 5.0\bin>mysql -uroot -pNHIE-Gateway asyncmsgs < c:\temp\asyncmsgsdb.sql
Step 4 - Launch MySQL Administration window and create a user with name "nhincuser" and password "nhincpass"
Step 5 - Assign all the privilages and add asyncmsgs db Schema for this user.