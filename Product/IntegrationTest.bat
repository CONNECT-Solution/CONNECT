CALL ..\Build\SetEnv.bat
nant.exe -listener:NAnt.Core.XmlLogger -buildfile:Personal.Build.xml IntegrationTest
@echo off
SET /P variable="Hit Enter to exit."