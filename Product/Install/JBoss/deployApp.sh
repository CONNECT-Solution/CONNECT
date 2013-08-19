#Script to Deploy CONNECT APP to JBoss 7.1.1Final Scripts

#wait for Server to start up
sleep 60

#Command to set JBOSS Environment Variable
. /nhin/CI/JBOSS_Variables.sh

#Copy the ear to /nhin/CI. This CONNECT.ear can then be used to undeploy later
cp $EARFILENAME /nhin/CI/CONNECT.ear

#Command to deploy CONNECT App to JBoss 7.1.1.Final
/nhin/jboss-as-7.1.1.Final/bin/jboss-cli.sh -c "deploy  --force /nhin/CI/CONNECT.ear"
