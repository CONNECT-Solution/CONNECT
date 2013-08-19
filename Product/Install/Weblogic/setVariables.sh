export JAVA_HOME=/nhin/jdk1.7.0_07
export MW_HOME=/nhin/weblogic
export WLS_DOMAIN_HOME=/usr/nhin/weblogic/user_projects/domains/base_domain
export JAVA_VENDOR=Sun
export CLASSPATH=/nhin/CI/nhinproperties:/nhin/CI/keystores
export JAVA_OPTIONS="-Xmx3000m -XX:MaxPermSize=1024m -XX:+PrintGCTimeStamps -XX:NewRatio=3 -Dnhinc.properties.dir=/nhin/CI/nhinproperties -Djavax.net.ssl.keyStorePassword=changeit -Djavax.net.ssl.trustStorePassword=changeit -Djavax.net.ssl.keyStore=/nhin/CI/keystores/gateway.jks -Djavax.net.ssl.keyStoreType=JKS -Djavax.net.ssl.trustStoreType=JKS -Djavax.net.ssl.trustStore=/nhin/CI/keystores/cacerts.jks -DCLIENT_KEY_ALIAS=gateway -Dweblogic.ThreadPoolSize=100  -Djava.security.egd=file:/dev/./urandom  -Dsecurerandom.source=file:/dev/./urandom"

# set the BUILD PARAMS
export EARFILENAME=`find  /home/jenkins/workspace/CONNECT_CI_LINUX_WEBLOGIC/.repository/org/connectopensource/CONNECT-WL/ -name "*.ear"`
export APPLICATION_NAME=connect

export ADMIN_USERNAME=weblogic
export ADMIN_PASSWORD=weblogic1
export WLS_ADMIN_URL=t3://localhost:7001

. $MW_HOME/wlserver/server/bin/setWLSEnv.sh
