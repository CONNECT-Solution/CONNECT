@echo off

rem //*****************************************************************************
rem // FILE: create-binary-installer.bat
rem //
rem // Copyright (C) 2010 TBD
rem //
rem // CLASSIFICATION: Unclassified
rem //
rem // DESCRIPTION: Create the AntInstaller JAR
rem //
rem // LIMITATIONS: None
rem //
rem // CHANGE HISTORY:
rem //
rem // Date         Proj   Act    Assign     Desc
rem // ============ ====== ====== ========== ===================================
rem // 2010/03/20   964G   1000   bgrantha   Initial Coding.
rem // 2010/04/02   964G   1000   bgrantha   Removed absolute paths
rem // 2010/04/22   964G   1000   ckempton   Updated create installer name
rem //
rem //*****************************************************************************

@echo on

ant -buildfile win.source.create.installer.xml
