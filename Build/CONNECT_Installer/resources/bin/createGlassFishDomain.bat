@echo off

rem //*****************************************************************************
rem // FILE: createGlassFishDomain.bat
rem //
rem // Copyright (C) 2010 TBD
rem //
rem // CLASSIFICATION: Unclassified
rem //
rem // DESCRIPTION: Create the GlassFish domain
rem //              ARG 1 : AS_HOME
rem //              ARG 2 : NHIN extract dir
rem //              ARG 3 : ANT_HOME
rem //              ARG 4 : ANT_OPTS
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

SET _TMP=%3
SET ANT_HOME=%_TMP:"=%

SET _TMP=%4
SET ANT_OPTS=%_TMP:"=%

SET PATH=%PATH%;%ANT_HOME%\bin

cd %2
ant -f deploy.xml recreate.glassfish.domain
