@echo off

rem //*****************************************************************************
rem // FILE: deploy.bat
rem //
rem // Copyright (C) 2010 TBD
rem //
rem // CLASSIFICATION: Unclassified
rem //
rem // DESCRIPTION: Deploy the CONNECT WARs
rem //              ARG 1 : AS_HOME 
rem //              ARG 2 : IDE_HOME 
rem //              ARG 3 : ANT_HOME 
rem //              ARG 4 : ANT_OPTS 
rem //              ARG 5 : NHINC_PROPERTIES_DIR 
rem //              ARG 6 : JAVA_HOME 
rem //              ARG 7 : NHIN extract dir 
rem //              ARG 8 : NHINC_THIRDPARTY_DIR 
rem //
rem // LIMITATIONS: None
rem //
rem // CHANGE HISTORY:
rem //
rem // Date         Proj   Act    Assign     Desc
rem // ============ ====== ====== ========== ===================================
rem // 2010/03/20   964G   1000   bgrantha   Initial Coding.
rem //
rem //*****************************************************************************

@echo on

SET _TMP=%1
SET AS_HOME=%_TMP:"=%

SET _TMP=%2
SET IDE_HOME=%_TMP:"=%

SET _TMP=%3
SET ANT_HOME=%_TMP:"=%

SET _TMP=%4
SET ANT_OPTS=%_TMP:"=%

SET _TMP=%5
SET NHINC_PROPERTIES_DIR=%_TMP:"=%

SET _TMP=%6
SET JAVA_HOME=%_TMP:"=%

SET _TMP=%8
SET NHINC_THIRDPARTY_DIR=%_TMP:"=%

SET PATH=%PATH%;%JAVA_HOME%\bin;%ANT_HOME%\bin

cd %7
ant -f deploy.xml && exit

echo not_getting_here