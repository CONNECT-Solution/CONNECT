
## Local Maven Repository

Connect maintains a local Maven Repository for artifacts that cannot be resolved using the standard repos. The repo is stored here:

    <CONNECT_CLONE_DIR>/ThirdParty/repo
    
Artifacts from the local repository are installed into your local maven cache as during the __maven-install-plugin__ executions specified in _<CONNECT_CLONE_DIR>/ThirdParty/pom.xml_.   

### How do I add a new artifact to the local maven repository?

Storing artifacts in the CONNECT local repository should be a last resort. First, you will need a jar file that you want to deploy as a Maven Repository entry. Use mvn deploy:deploy-file to deploy the jar into the local repository. Then add an entry in the ThirdParty pom.xml which installs the artifacts into your local maven cache. Finally add a dependency entry in the pom where appropriate.

## Links

[https://devcenter.heroku.com/articles/local-maven-dependencies](https://devcenter.heroku.com/articles/local-maven-dependencies)  
[http://maven.apache.org/plugins/maven-install-plugin/install-file-mojo.html](http://maven.apache.org/plugins/maven-install-plugin/install-file-mojo.html)

## Log

    mvn install:install-file -Dfile=jms1.1/lib/javax.jms.jar -DgroupId=javax.jms -DartifactId=jms -Dversion=1.1 -Dpackaging=jar
    mvn install:install-file -Dfile=JTA/javax.transaction.jar -DgroupId=javax.transaction -DartifactId=jta -Dversion=1.0.1B -Dpackaging=jar


    mvn install:install-file -Dfile=jmx-1_2_1-bin/lib/jmxri.jar -DgroupId=com.sun.jmx -DartifactId=jmxri -Dversion=1.2.1 -Dpackaging=jar
    mvn install:install-file -Dfile=jmx-1_2_1-bin/lib/jmxtools.jar -DgroupId=com.sun.jdmk -DartifactId=jmxtools -Dversion=1.2.1 -Dpackaging=jar

    mvn install:install-file -Dfile=OpenSSO/ssoAdminTools/lib/opensso.jar -DgroupId=com.sun.identity -DartifactId=opensso -Dversion=1.0 -Dpackaging=jar
    mvn install:install-file -Dfile=OpenSSO/ssoAdminTools/lib/opensso-sharedlib.jar -DgroupId=com.sun.identity -DartifactId=opensso-sharedlib -Dversion=1.0 -Dpackaging=jar
    mvn install:install-file -Dfile=OpenSSO/openssoclientsdk.jar -DgroupId=com.sun.identity -DartifactId=openssoclientsdk -Dversion=1.0 -Dpackaging=jar

##### install of the JSF-related untraceable libraries    
    mvn install:install-file -Dfile=jsf/jsfcl.jar -DgroupId=org.connectopensource.thirdparty.com.sun.jsf -DartifactId=jsfcl -Dversion=1.0 -Dpackaging=jar
    mvn install:install-file -Dfile=jsf/appbase.jar -DgroupId=org.connectopensource.thirdparty.com.sun.jsf -DartifactId=appbase -Dversion=1.0 -Dpackaging=jar    
    mvn install:install-file -Dfile=jsf/sqlx.jar -DgroupId=org.connectopensource.thirdparty.com.sun.jsf -DartifactId=sqlx -Dversion=1.0 -Dpackaging=jar        
    mvn install:install-file -Dfile=jsf/dataprovider.jar -DgroupId=org.connectopensource.thirdparty.com.sun.jsf -DartifactId=dataprovider -Dversion=1.0 -Dpackaging=jar    
    mvn install:install-file -Dfile=jsf/errorhandler.jar -DgroupId=org.connectopensource.thirdparty.com.sun.jsf -DartifactId=errorhandler -Dversion=1.0 -Dpackaging=jar    
    mvn install:install-file -Dfile=jsf/webui-jsf.jar -DgroupId=org.connectopensource.thirdparty.com.sun.jsf -DartifactId=webui-jsf -Dversion=1.0 -Dpackaging=jar    
    mvn install:install-file -Dfile=jsf/webui-jsf-suntheme.jar -DgroupId=org.connectopensource.thirdparty.com.sun.jsf -DartifactId=webui-jsf-suntheme -Dversion=1.0 -Dpackaging=jar  