@echo off
cls
REM ********************************
REM * BULK LOADER SETTINGS [START] *
REM ********************************
	REM #### NETBEANS AND JAVA ####
set NB_HOME="<Set Netbeans Home e.g. D:\JavaCAPS6\netbeans>"
set JAVA_PATH="<Set Java Home e.g. D:\Software\jre1.5.0_11\bin>"

REM  Use sqlserverJdbcDriver.jar under lib for connecting with SQL Server
    set DB_DRIVER_PATH="<Set DB Driver Path e.g. .\lib>"
    set DB_DRIVER_NAME=<Set DB Driver Name e.g. sqlserverdriver-1.0.jar>

	REM #### SOURCE DATABASE ####
set SOURCE_LOC="<Specify Source Dir e.g. D:\temp\mural\masterindextest>"
set FIELD_DELIMITER="|"
set RECORD_DELIMITER="$$$"

	REM #### TARGET DATABASE ####
REM Specify from following options (ORACLE=1, DERBY=2, SQLSERVER=3)
    set TARGET_DB_TYPE=<Set Database Type e.g. 1>
    set TARGET_LOC=<DataBase Host/IP e.g. localhost>
REM Note : 1521 (Oracle), 1527 (Derby), 1433 (SQLServer)
    set TARGET_PORT=<Specify Port No e.g. 1521>
REM Note : Specify ID as 'SID'(SystemId) for Oracle, 'DB Name' for Derby, 'Database Name' for SQL Server
    set TARGET_ID=<Specify Sid/DBname/databaseName e.g. orcl or type4 etc.>
REM Note: SCHEMA name is case-sersitive for all other databases supported except Oracle
    set TARGET_SCHEMA=<Specify Schema e.g. OE, Blank for null>
set TARGET_CATALOG=<Specify Catalog e.g. OE, Blank for null>
set TARGET_LOGIN=<Specify Target DB Login>
set TARGET_PW=<Specify Target DB Passwd>
REM ********************************
REM * BULK LOADER SETTINGS [END] *
REM ********************************

REM *****************************
REM   DO NOT EDIT THIS [START]
REM *****************************
echo Pre-execution cleanup ...
if exist .\\etl-loader.zip DEL .\\etl-loader.zip /s/q
if exist .\\BulkLoaderWorkDir RMDIR .\\BulkLoaderWorkDir /s/q
if exist .\\usrdir RMDIR .\\usrdir /s/q
echo Completed.

set RUNSTAT=START
set DB_DRIVER_JAR=%DB_DRIVER_PATH%\%DB_DRIVER_NAME%
if NOT exist %DB_DRIVER_JAR% GOTO GetExit

if NOT exist .\lib\%DB_DRIVER_NAME% copy %DB_DRIVER_JAR% .\lib\%DB_DRIVER_NAME%

set BLK=%CD%
set USER_LIBS=%BLK%\lib\avalon-framework-4.1.3.jar;%BLK%\lib\axion-1.0.jar;%BLK%\lib\etl-editor-1.0.jar;%BLK%\lib\etl-engine-1.0.jar;%BLK%\lib\ETLEngineInvoker-1.0.jar;%BLK%\lib\i18n-1.0.jar;%BLK%\lib\ojdbc14-10.1.0.2.0.jar;%BLK%\lib\org-netbeans-modules-db-1.0.jar;%BLK%\lib\velocity-1.4.jar;%BLK%\lib\velocity-dep-1.4.jar;%BLK%\lib\bulkloader-1.0.jar
set OPENIDE_LIB_MODULE=%NB_HOME%\platform8\modules
set OPENIDE_LIB_LIB=%NB_HOME%\platform8\lib
set OPENIDE_LIB_CORE=%NB_HOME%\platform8\core
set OPENIDE_LIB_IDE9_MOD=%NB_HOME%\ide9\modules
set OPENIDE_LIB_XML2_MOD=%NB_HOME%\xml2\modules
set OPENIDE_LIB_SOA2_MOD=%NB_HOME%\soa2\modules
set OPENIDE_LIBS=%OPENIDE_LIB_MODULE%\org-openide-nodes.jar;%OPENIDE_LIB_MODULE%\org-openide-text.jar;%OPENIDE_LIB_MODULE%\org-openide-loaders.jar;%OPENIDE_LIB_MODULE%\org-openide-windows.jar;%OPENIDE_LIB_MODULE%\org-openide-dialogs.jar;%OPENIDE_LIB_MODULE%\org-openide-awt.jar;%OPENIDE_LIB_CORE%\org-openide-filesystems.jar;%OPENIDE_LIB_LIB%\org-openide-util.jar;%OPENIDE_LIB_IDE9_MOD%\org-netbeans-modules-db.jar;%OPENIDE_LIB_XML2_MOD%\org-netbeans-modules-xml-validation.jar;%OPENIDE_LIB_IDE9_MOD%\org-netbeans-api-xml.jar;%OPENIDE_LIB_SOA2_MOD%\org-netbeans-modules-soa-ui.jar;%OPENIDE_LIB_XML2_MOD%\org-netbeans-modules-xml-xam-ui.jar
set ALL_LIBS=%USER_LIBS%;%OPENIDE_LIBS%;%DB_DRIVER_JAR%;

REM Execution
	%JAVA_PATH%\java -Xms128m -Xmx512m -Dsourcedb.loc=%SOURCE_LOC% -Dfield.delimiter=%FIELD_DELIMITER% -Drecord.delimiter=%RECORD_DELIMITER% -Dtarget.type=%TARGET_DB_TYPE% -Dtarget.host=%TARGET_LOC% -Dtarget.port=%TARGET_PORT% -Dtarget.id=%TARGET_ID% -Dtarget.schema=%TARGET_SCHEMA% -Dtarget.catalog=%TARGET_CATALOG% -Dtarget.login=%TARGET_LOGIN% -Dtarget.pw=%TARGET_PW% -Dmyjava.path=%JAVA_PATH% -Ddbdriver.name=%DB_DRIVER_NAME% -cp %ALL_LIBS% com.sun.dm.di.bulkloader.LoaderMain
REM Execution

REM Cleanup
echo Cleaning up temporary files and artifacts ...
if exist .\\ETLLoader RMDIR .\\ETLLoader /s/q
if exist .\\ETLProcess RMDIR .\\ETLProcess /s/q
echo Completed.

set RUNSTAT=SUCCESS
:GetExit
if (%RUNSTAT%) == (START) echo Unable to Locate Database Driver on Specified Path. Check Path : %DB_DRIVER_JAR%
REM *****************************
REM   DO NOT EDIT THIS [END]
REM *****************************