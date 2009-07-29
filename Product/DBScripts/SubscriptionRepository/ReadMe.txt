Overview
The subscription repository provides storage for subscriptions as part fo the HIEM specification. It is assumed that the repository is located on the same server as the executing code. If the location is different, the database URL will need to be modified in ...\Gateway\SubscriptionRepository\conf\hibernate.cfg.xml.

Prerequisites
These instructions assume that MySQL is already installed using the instructions found on the "Installing MySQL" page.

Create the Database Schema
Open MySQL Administrator and log in as the root user. Create a new schema named "subscriptionrepository" in the Catalogs section.

Create the Gateway User
The "nhincuser" is used to access the subscription repository. If this user has not been created, Create the user in the "User Administration" section of MySQL Administrator. Assign a password of "nhincpass". On the schema priviliges tab for this user, assign SELECT, INSERT, UPDATE, and DELETE rights to the subscriptionrepository schema to the user. Close MySQL Administrator.

Create Tables
Log into MySQL Query Browser as the root (the nhincuser does not have permission to create tables) user. Switch to the subscriptionrepository schema by double-clicking the schema in the "Schemata" tab on the right of the window. Open the SubscriptionRepository.sql script. Run the script and verify that there were no errors. Close MySQL Query Browser.

This process will need to be completed for each gateway that will use the subscription repository.

