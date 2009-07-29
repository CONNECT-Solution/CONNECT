In order to run this example, OpenSSO needs to be installed.

Once OpenSSO has been installed, you need to create a user that you
can use to test the authentication from the web page.   Create a user
in OpenSSO using the following steps:

OpenSSO Admin Console Default URL:  http://localhost:8080/opensso
Login as: amadmin with password adminadmin   
    (If you chose a different user or password for this user when
	 you installed, then you will need that user name and password).
	 
1.  Select "Access Control" tab.
2.  Select "/ (Top Level Realm)" link
3.  Select "Subjects" tab.
4.  Select "New..." button.
5.  Enter the following Fields:
	ID: user1
	First Name: User
	Last Name: 1
	Full Name: User 1
	Password: password
	Password (confirm): password
	User Status: Active
6.  Select "Ok" button.

You can now use this user to login to the example.

Deploy the IdSvcsClient project to GlassFish.

Run the sample: http://localhost:8080/IdSvcsClient.  This will
prompt you for a user name and password with two different authentication
mechanisms.  You can test each one out.
