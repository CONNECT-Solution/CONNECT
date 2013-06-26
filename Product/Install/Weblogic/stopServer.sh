# set the CONNECT and WAS env variables
.  /nhin/CI/setVariables.sh

# Stop the Management Server. The following parameters are passed:
# param 1 --> Server Name
# param 2 --> Admin URL
/nhin/weblogic/user_projects/domains/base_domain/bin/stopManagedWebLogic.sh $1 $ADMIN_URL 
# Stop the Admin Server
/nhin/weblogic/user_projects/domains/base_domain/bin/stopWebLogic.sh
