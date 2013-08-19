Connect maintains a local Maven Repository for artifact dependencies that cannot be resolved using the standard repos. 

### Where is the local repository?
The repo is stored here:

    <CONNECT_CLONE_DIR>/ThirdParty/repo
    
Artifacts from the local repository are installed into your local maven cache during the __maven-install-plugin__ executions specified in _&lt;CONNECT_CLONE_DIR&gt;/ThirdParty/pom.xml_.   

### How do I add a new artifact to the local maven repository?

Storing artifacts in the CONNECT local repository should be a last resort. Always prefer the standard repositories which are configured under _&lt;CONNECT_CLONE_DIR&gt;/Product/pom.xml_.

First, you will need a jar file that you want to deploy as a Maven Repository entry. Run the mvn deploy:deploy-file to deploy the jar into the local repository. 

    mvn deploy:deploy-file -Durl=file:///<CONNECT_CLONE_DIR>/ThirdParty/repo/ -Dfile=/path/to/my/example.jar -DgroupId=org.example -DartifactId=example -Dversion=1.1 -Dpackaging=jar

Then add an entry in _&lt;CONNECT_CLONE_DIR&gt;/ThirdParty/pom.xml_ which installs the artifacts into your local maven cache. 

    ...
    <execution>
        <id>javax.jms:jms</id>
        <phase>initialize</phase>
        <goals>
            <goal>install-file</goal>
        </goals>
        <configuration>
            <pomFile>${basedir}/repo/path/to/deployed/example.pom</pomFile>
            <file>${basedir}/repo/path/to/deployed/example.jar</file>
        </configuration>
    </execution>
    ...

Add a dependency entries in the poms where appropriate.


## Links

[https://devcenter.heroku.com/articles/local-maven-dependencies](https://devcenter.heroku.com/articles/local-maven-dependencies)  
[http://maven.apache.org/plugins/maven-install-plugin/install-file-mojo.html](http://maven.apache.org/plugins/maven-install-plugin/install-file-mojo.html)
