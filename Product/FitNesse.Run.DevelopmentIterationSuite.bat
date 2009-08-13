CALL ..\Build\SetEnv.bat
nant.exe -listener:NAnt.Core.XmlLogger -buildfile:Personal.Build.xml FitNesse.Run.DevelopmentIterationSuite
@echo off
SET /P variable="Hit Enter to exit."