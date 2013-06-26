# UnInstall the application. The following arguments passed to the undeploy script
# param1 --> application Name

. /nhin/CI/setVariables.sh


/home2/gfish/IBM/WebSphere/AppServer/profiles/AppSrv02/bin/wsadmin.sh -f /nhin/CI/unDeployApp.jacl $APPLICATION_NAME

