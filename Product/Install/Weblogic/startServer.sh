# Set the CONNECT and WLS env variables
.  /nhin/CI/setVariables.sh
# Start the application server
/usr/nhin/weblogic/user_projects/domains/base_domain/bin/startWebLogic.sh &
# Sleep for 60 seconds  
sleep 60

# Start the Managed server "server1"
/nhin/weblogic/user_projects/domains/base_domain/bin/startManagedWebLogic.sh $1 $ADMIN_URL &
# Sleep for 60 seconds
sleep 60

