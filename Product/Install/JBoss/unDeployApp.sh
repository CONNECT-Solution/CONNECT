#Command to set JBOSS Environment Variable
. /nhin/CI/JBOSS_Variables.sh

# Remove Connect.ear from /nhin/CI
rm /nhin/CI/CONNECT.ear

#Command to undeploy CONNECT ear from JBoss 7.1.1.final
/nhin/jboss-as-7.1.1.Final/bin/jboss-cli.sh -c "undeploy CONNECT.ear"
