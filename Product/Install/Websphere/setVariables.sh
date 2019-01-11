# set custom environment variables
WEBSPHERE_PROFILE_DIR=/home2/gfish/IBM/WebSphere/AppServer/profiles/AppSrv02

APPLICATION_NAME=CONNECT-WAS

EARFILENAME=`find  /var/lib/jenkins/workspace/CONNECT_CI_SOLARIS_WEBSPHERE/.repository/org/connectopensource/CONNECT-WAS/ -name "*.ear"`

export WEBSPHERE_PROFILE_DIR
export APPLICATION_NAME
export EARFILENAME
