HealthReveal CONNECT
========================

Pre-requisites:

1) Java 8
2) Maven 3.3.3 (latest)
3) Ant 1.9.6 (latest)
4) Mysql 5.5.5 (latest)

Note:
Maven 3.3.3 and Ant 1.9.6 wasn't installed by yum or brew. Done by tar-ball method.

5) Change environment variables:

JAVA_HOME=[Your java home]

ANT_HOME=[Your ant home]
ANT_OPTS="-Xmx1200m -Dcom.sun.aas.instanceName=server"
PATH=$PATH:$ANT_HOME/bin

MAVEN_HOME=[Your maven home]
MAVEN_OPTS="-Xmx5000m"
PATH=$PATH:$MAVEN_HOME/bin

6) Change mysql settings:

Change root password for mysql to NHIN-Gateway

7) Change maven settings:

In $ANT_HOME/conf/settings.xml, add inside <profiles>:

<profile>
	<id>connect</id>
	<activation>
	  	<activeByDefault>true</activeByDefault>
	</activation>
	<properties>
	  	<mysql.root.password>NHIN-Gateway</mysql.root.password>
	</properties>
</profile>

8) In the root folder of connect: mvn clean install

TODO: Fix Direct.

9) Enjoy!
