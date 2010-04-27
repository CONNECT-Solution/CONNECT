@echo off

rem //*****************************************************************************
rem // FILE: win_source_build_connect.bat
rem //
rem // Copyright (C) 2010 TBD
rem //
rem // CLASSIFICATION: Unclassified
rem //
rem // DESCRIPTION: Perform a Build of CONNECT Source
rem //              ARG 1 : NHIN_SOURCE_BUILD_PATH
rem //
rem // LIMITATIONS: None
rem //
rem // CHANGE HISTORY:
rem //
rem // Date         Proj   Act    Assign     Desc
rem // ============ ====== ====== ========== ===================================
rem // 2010/04/20   964G   1000   ckempton   Initial Coding.
rem //
rem //*****************************************************************************

@echo on



cd %1
ant clean package.create
