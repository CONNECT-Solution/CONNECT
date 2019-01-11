# UnInstall the application. The following arguments passed to the undeploy script
# param1 --> application Name

. /nhin/CI/setVariables.sh


${WEBSPHERE_PROFILE_DIR}/bin/wsadmin.sh -f /nhin/CI/unDeployApp.jacl $APPLICATION_NAME

