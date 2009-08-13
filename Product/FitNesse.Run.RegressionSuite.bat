CALL ..\Build\SetEnv.bat
nant.exe -listener:NAnt.Core.XmlLogger -buildfile:Personal.Build.xml FitNesse.Run.RegressionSuite
@echo off
SET /P variable="Hit Enter to exit."