HealthReveal CONNECT
========================

#Pre-requisites:

## Java 8
## Maven 3.3.3 (latest)
## Ant 1.9.6 (latest)
## Mysql 5.5.5 (latest)

## Note:
Maven 3.3.3 and Ant 1.9.6 wasn't installed by yum or brew. Done by tar-ball method.

## Change environment variables:

```
JAVA_HOME=[Your java home]

ANT_HOME=[Your ant home]
ANT_OPTS="-Xmx1200m -Dcom.sun.aas.instanceName=server"
PATH=$PATH:$ANT_HOME/bin

MAVEN_HOME=[Your maven home]
MAVEN_OPTS="-Xmx5000m"
PATH=$PATH:$MAVEN_HOME/bin
```



## Change mysql settings:

Change root password for mysql to NHIN-Gateway

## Change maven settings:

In $ANT_HOME/conf/settings.xml, add inside <profiles>:

```
<profile>
	<id>connect</id>
	<activation>
	  	<activeByDefault>true</activeByDefault>
	</activation>
	<properties>
	  	<mysql.root.password>NHIN-Gateway</mysql.root.password>
	</properties>
</profile>
```



## In the root folder of connect: mvn clean install

TODO: Fix Direct.

## Enjoy!
