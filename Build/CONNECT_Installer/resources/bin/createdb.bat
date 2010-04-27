@echo off

rem //*****************************************************************************
rem // FILE: createdb.bat
rem //
rem // Copyright (C) 2010 TBD
rem //
rem // CLASSIFICATION: Unclassified
rem //
rem // DESCRIPTION: Create the databases and schemas
rem //              ARG 1 : NHIN extract dir
rem //              ARG 2 : MYSQL_BIN
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

cd %1\DBScripts\nhincdb
%2\mysql.exe -uroot -pNHIE-Gateway < dropall.sql
%2\mysql.exe -uroot -pNHIE-Gateway < nhincdb.sql