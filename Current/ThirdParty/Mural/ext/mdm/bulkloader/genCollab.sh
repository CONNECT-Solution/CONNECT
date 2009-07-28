#!/bin/sh
clear

# ********************************
# * BULK LOADER SETTINGS [START] *
# ********************************

	# **** NETBEANS AND JAVA ****
NB_HOME="<Set Netbeans Home e.g. /openesb/netbeans-6.1-build-200803100002>"
JAVA_PATH="<Set Java Home e.g. /usr/jdk/instances/jdk1.5.0/jre/bin>"

    #  Use sqlserverJdbcDriver.jar under lib for connecting with SQL Server
DB_DRIVER_PATH="<./lib>"
DB_DRIVER_NAME=<Set DB Driver Name e.g. sqlserverdriver-1.0.jar>

	# **** SOURCE DATABASE ****
SOURCE_LOC="<Specify Source Dir e.g. /bl_derbytest/DATA>"
FIELD_DELIMITER="|"
RECORD_DELIMITER="\$\$\$"

	# **** TARGET DATABASE ****
# Specify from following options (ORACLE=1, DERBY=2, SQLSERVER=3)
    TARGET_DB_TYPE=<Set Database Type e.g. 3>
    TARGET_LOC=<DataBase Host/IP e.g. localhost>
# Note : 1521 (Oracle), 1527 (Derby), 1433 (SQLServer)
    TARGET_PORT=<Specify Port No e.g. 1433>
# Note : Specify ID as 'SID'(SystemId) for Oracle, 'DB Name' for Derby, 'Database Name' for SQL Server
    TARGET_ID=<Specify Sid/DBname/databaseName e.g. orcl or eindex etc.>
# Note: SCHEMA name is case-sersitive for all other databases supported except Oracle
    TARGET_SCHEMA=<Specify Schema e.g. APP, Blank for null>
TARGET_CATALOG=<Specify Catalog e.g. APP, Blank for null>
TARGET_LOGIN=<Specify Target DB Login>
TARGET_PW=<Specify Target DB Passwd>
# ********************************
# * BULK LOADER SETTINGS [END]   *
# ********************************

# *****************************
#   DO NOT EDIT THIS [START]
# *****************************

echo "Pre-execution cleanup ..."
 test -f ./etl-loader.zip && rm -rf ./etl-loader.zip
 test -d ./BulkLoaderWorkDir && rm -rf ./BulkLoaderWorkDir
 test -d ./usrdir && rm -rf ./usrdir
echo Completed.

DB_DRIVER_JAR="$DB_DRIVER_PATH/$DB_DRIVER_NAME"
if  test -f $DB_DRIVER_JAR
then
	cp $DB_DRIVER_JAR ./lib/$DB_DRIVER_NAME
	BLK=`pwd`

	USER_LIBS="$BLK/lib/avalon-framework-4.1.3.jar:$BLK/lib/axion-1.0.jar:$BLK/lib/etl-editor-1.0.jar:$BLK/lib/etl-engine-1.0.jar:$BLK/lib/ETLEngineInvoker-1.0.jar:$BLK/lib/i18n-1.0.jar:$BLK/lib/ojdbc14-10.1.0.2.0.jar:$BLK/lib/org-netbeans-modules-db-1.0.jar:$BLK/lib/velocity-1.4.jar:$BLK/lib/velocity-dep-1.4.jar:$BLK/lib/bulkloader-1.0.jar"

	OPENIDE_LIB_MODULE=$NB_HOME/platform8/modules
	OPENIDE_LIB_LIB=$NB_HOME/platform8/lib
	OPENIDE_LIB_CORE=$NB_HOME/platform8/core
	OPENIDE_LIB_IDE9_MOD=$NB_HOME/ide9/modules
	OPENIDE_LIB_XML2_MOD=$NB_HOME/xml2/modules
	OPENIDE_LIB_SOA2_MOD=$NB_HOME/soa2/modules
	OPENIDE_LIBS="$OPENIDE_LIB_MODULE/org-openide-nodes.jar:$OPENIDE_LIB_MODULE/org-openide-text.jar:$OPENIDE_LIB_MODULE/org-openide-loaders.jar:$OPENIDE_LIB_MODULE/org-openide-windows.jar:$OPENIDE_LIB_MODULE/org-openide-dialogs.jar:$OPENIDE_LIB_MODULE/org-openide-awt.jar:$OPENIDE_LIB_CORE/org-openide-filesystems.jar:$OPENIDE_LIB_LIB/org-openide-util.jar:$OPENIDE_LIB_IDE9_MOD/org-netbeans-modules-db.jar:$OPENIDE_LIB_XML2_MOD/org-netbeans-modules-xml-validation.jar:$OPENIDE_LIB_IDE9_MOD/org-netbeans-api-xml.jar:$OPENIDE_LIB_SOA2_MOD/org-netbeans-modules-soa-ui.jar:$OPENIDE_LIB_XML2_MOD/org-netbeans-modules-xml-xam-ui.jar"
	ALL_LIBS="$USER_LIBS:$OPENIDE_LIBS:$DB_DRIVER_JAR"
	
	$JAVA_PATH/java -Xms128m -Xmx512m -Dsourcedb.loc=$SOURCE_LOC -Dfield.delimiter=$FIELD_DELIMITER -Drecord.delimiter=$RECORD_DELIMITER -Dtarget.type=$TARGET_DB_TYPE -Dtarget.host=$TARGET_LOC -Dtarget.port=$TARGET_PORT -Dtarget.id=$TARGET_ID -Dtarget.schema=$TARGET_SCHEMA -Dtarget.catalog=$TARGET_CATALOG -Dtarget.login=$TARGET_LOGIN -Dtarget.pw=$TARGET_PW -Dmyjava.path=$JAVA_PATH -Ddbdriver.name=$DB_DRIVER_NAME -cp $ALL_LIBS com.sun.dm.di.bulkloader.LoaderMain

else
	echo " Unable to locate Database Driver on Specified Path."
	echo " Check Path - ${DB_DRIVER_JAR}"
	exit
fi

# Cleanup
echo "Cleaning up temporary files and artifacts ..."
test -d ./ETLLoader && rm -rf ./ETLLoader
test -d ./ETLProcess && rm -rf ./ETLProcess
echo "Completed."
# *****************************
#   DO NOT EDIT THIS [END]
# *****************************
