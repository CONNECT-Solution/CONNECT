Overview
The document repository provides document storage for the Agency Adapter reference implementation. The reference agency adapter assumes that the repository is located on the same server. If the location is different, the database URL will need to be modified in ...\Adapters\General\DocumentRepository\conf\hibernate.cfg.xml.

Prerequisites
These instructions assume that MySQL is already installed using the instructions found on the "Installing MySQL" page.

Create the Database Schema
Open MySQL Administrator and log in as the root user. Create a new schema named "docrepository" in the Catalogs section.

Create the Gateway User
The "nhincuser" is used to access the document repository. If this user has not been created, Create the user in the "User Administration" section of MySQL Administrator. Assign a password of "nhincpass". On the schema priviliges tab for this user, assign SELECT, INSERT, UPDATE, and DELETE rights to the docrepository schema to the user. Close MySQL Administrator.

Create Tables
Log into MySQL Query Browser as the root (the nhincuser does not have permission to create tables) user. Switch to the docrepository schema by double-clicking the schema in the "Schemata" tab on the right of the window. Open the DocumentRepository.sql script. Run the script and verify that there were no errors. Close MySQL Query Browser.

This process will need to be completed for each gateway that will use the document repository.

--------------------------------------------------------------------------------

Retrieved from "http://nhiewiki.atdom-hc.com/Development/Setting_up_the_Document_Repository"
