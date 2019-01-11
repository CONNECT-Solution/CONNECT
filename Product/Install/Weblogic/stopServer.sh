# set the CONNECT and WAS env variables
.  /nhin/CI/setVariables.sh

# Stop the Management Server. The following parameters are passed:
# param 1 --> Server Name
# param 2 --> Admin URL
$WLS_DOMAIN_HOME/bin/stopManagedWebLogic.sh $1 $WLS_ADMIN_URL 
# Stop the Admin Server
$WLS_DOMAIN_HOME/bin/stopWebLogic.sh
