Step 1 - Place the perfrepository.sql file in c:\temp folder
Step 2 - Goto MySQL / bin folder using command prompt
Step 3 - execute the below step to install perfrepo schema to your MySql database
C:\Program Files\MySQL\MySQL Server 5.0\bin>mysql -uroot -pNHIE-Gateway perfrepo < c:\temp\perfrepository.sql
Step 4 - Login to MySQL Administrator using root/NHIE-Gateway username and passwords.
Step 5 - Verify nhincuser/nhincpass account has been created. If it has not, create the user.
Step 6 - Add perfrepo schema to nhincuser user.
Step 7 - Verify the nhincuser has SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, GRANT, EXECUTE privileges on perfrepo Schema.

