@echo off

rem //*****************************************************************************
rem // FILE: setenv.bat
rem //
rem // Copyright (C) 2010 TBD
rem //
rem // CLASSIFICATION: Unclassified
rem //
rem // DESCRIPTION: Set the environment variable passed as an agrument
rem //              ARG : NAME=VALUE
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

WindowsHelper.exe %*
SET %*
