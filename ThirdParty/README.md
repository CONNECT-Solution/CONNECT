

mvn install:install-file -Dfile=jms1.1/lib/javax.jms.jar -DgroupId=javax.jms \
    -DartifactId=jms -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=JTA/javax.transaction.jar -DgroupId=javax.transaction \
    -DartifactId=jta -Dversion=1.0.1B -Dpackaging=jar


mvn install:install-file -Dfile=jmx-1_2_1-bin/lib/jmxri.jar -DgroupId=com.sun.jmx \
    -DartifactId=jmxri -Dversion=1.2.1 -Dpackaging=jar
mvn install:install-file -Dfile=jmx-1_2_1-bin/lib/jmxtools.jar -DgroupId=com.sun.jdmk \
    -DartifactId=jmxtools -Dversion=1.2.1 -Dpackaging=jar

mvn install:install-file -Dfile=OpenSSO/ssoAdminTools/lib/opensso.jar -DgroupId=com.sun.identity \
    -DartifactId=opensso -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=OpenSSO/ssoAdminTools/lib/opensso-sharedlib.jar -DgroupId=com.sun.identity \
    -DartifactId=opensso-sharedlib -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=OpenSSO/openssoclientsdk.jar -DgroupId=com.sun.identity \
    -DartifactId=openssoclientsdk -Dversion=1.0 -Dpackaging=jar

# install of the JSF-related untraceable libraries    
mvn install:install-file -Dfile=jsf/jsfcl.jar -DgroupId=org.connectopensource.thirdparty.com.sun.jsf \
    -DartifactId=jsfcl -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=jsf/appbase.jar -DgroupId=org.connectopensource.thirdparty.com.sun.jsf \
    -DartifactId=appbase -Dversion=1.0 -Dpackaging=jar    
mvn install:install-file -Dfile=jsf/sqlx.jar -DgroupId=org.connectopensource.thirdparty.com.sun.jsf \
    -DartifactId=sqlx -Dversion=1.0 -Dpackaging=jar        
mvn install:install-file -Dfile=jsf/dataprovider.jar -DgroupId=org.connectopensource.thirdparty.com.sun.jsf \
    -DartifactId=dataprovider -Dversion=1.0 -Dpackaging=jar    
mvn install:install-file -Dfile=jsf/errorhandler.jar -DgroupId=org.connectopensource.thirdparty.com.sun.jsf \
    -DartifactId=errorhandler -Dversion=1.0 -Dpackaging=jar    
mvn install:install-file -Dfile=jsf/webui-jsf.jar -DgroupId=org.connectopensource.thirdparty.com.sun.jsf \
    -DartifactId=webui-jsf -Dversion=1.0 -Dpackaging=jar    
mvn install:install-file -Dfile=jsf/webui-jsf-suntheme.jar -DgroupId=org.connectopensource.thirdparty.com.sun.jsf \
    -DartifactId=webui-jsf-suntheme -Dversion=1.0 -Dpackaging=jar  