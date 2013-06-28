# Set the CONNECT and WLS env variables
.  /nhin/CI/setVariables.sh
# Start the application server
$WLS_DOMAIN_HOME/bin/startWebLogic.sh &
# Sleep for 60 seconds  
sleep 60

# Undeploy the CONNECT application
/nhin/CI/unDeployApp.sh server1

# Start the Managed server "server1"
$WLS_DOMAIN_HOME/bin/startManagedWebLogic.sh $1 $WLS_ADMIN_URL &
# Sleep for 60 seconds
sleep 60

