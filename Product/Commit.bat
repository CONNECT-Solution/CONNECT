CALL ..\Build\SetEnv.bat
cd %Common.Directory.Build.Path%
svn update --non-interactive

cd %Common.Directory.Product.Path%

nant.exe -listener:NAnt.Core.XmlLogger -buildfile:Personal.Build.xml UpdateSource
IF NOT %ERRORLEVEL%==0 exit /B %ERRORLEVEL%
nant.exe -listener:NAnt.Core.XmlLogger -buildfile:Personal.Build.xml Commit
@echo off
SET /P variable="Hit Enter to exit."