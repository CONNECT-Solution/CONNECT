# set the custom environmental variables
. /nhin/CI/setVariables.sh

echo ${WEBSPHERE_PROFILE_DIR} 

# Start the Server. The following arguments are passed
# param 1 --> serverName
${WEBSPHERE_PROFILE_DIR}/bin/startServer.sh $1 
