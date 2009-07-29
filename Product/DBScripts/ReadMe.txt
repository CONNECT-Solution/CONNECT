General Notes on Database Creation

Create a user called nhincuser with password nhincpass - This should be used
for all programmatic access to the database (i.e. from the gateway software).

To create all of the necessary schemas, refer to the Readme.txt file located
in the nhincdb directory.

When creating a new schema
	1.  Create a user that has root privileges to your schema and give it all 
	    privileges to access your schema.
	2.  Make sure that you grant insert, select, update, delete, execute 
	    privileges to the user: nhincuser for your new schema.

