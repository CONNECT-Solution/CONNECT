CALL ..\Build\SetEnv.bat
nant.exe -listener:NAnt.Core.XmlLogger -buildfile:Personal.Build.xml Selenium.StartServer