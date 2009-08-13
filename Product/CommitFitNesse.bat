CALL ..\Build\SetEnv.bat
nant.exe -listener:NAnt.Core.XmlLogger -buildfile:Personal.Build.xml FitNesse.CommitToSubversion
@echo off
SET /P variable="Hit Enter to exit."