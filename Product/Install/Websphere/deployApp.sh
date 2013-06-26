# Install the application. The following parameters are passed to the deployApp script 
# param1 --> ear file name
# param2 --> application Name
# param3 --> serverName
# param4 --> cellName
# param5 --> nodeName
 
. /nhin/CI/setVariables.sh

echo $EARFILENAME  
/home2/gfish/IBM/WebSphere/AppServer/profiles/AppSrv02/bin/wsadmin.sh -f /nhin/CI/deployApp.jacl $EARFILENAME $APPLICATION_NAME $1 $2 $3 

