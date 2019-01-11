# set the custom environmental variables
. /nhin/CI/setVariables.sh

# Stop the server. The following arguments are passed
# param 1 --> serverName
${WEBSPHERE_PROFILE_DIR}/bin/stopServer.sh $1 
