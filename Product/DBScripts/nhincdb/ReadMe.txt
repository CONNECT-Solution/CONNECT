This schema is used to create all of the needed schemas for the NHIN Connect
gateway and adapter.  The steps are outlined below.

Step 1 - Launch MySQL Administration window and create a schema called nhincdb
Step 2 - Place the nhincdb.sql file in c:\temp\ folder.
Step 3 - Go to MySQL bin directory and import the schema using the command below:
         C:\Program Files\MySQL\MySQL Server 5.0\bin>mysql -uroot -pNHIE-Gateway nhincdb < c:\temp\nhincdb.sql
Step 4 - If the user nhincuser has not been created yet, launch MySQL Administration window and create 
         a user with name "nhincuser" and password "nhincpass"
Step 5 - Assign read, write, update, delete, execute privilage for all schemas to the nhincuser. 

(Note - if you want to create a specific schema you can follow the directions in the ReadMe.txt file 
        in the directory for that schema.)