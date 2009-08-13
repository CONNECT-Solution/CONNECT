CALL SetEnv.bat

set ENVBAT="%ProgramFiles%\Microsoft Visual Studio 9.0\VC\vcvarsall.bat"
IF DEFINED ProgramFiles(x86) set ENVBAT="%ProgramFiles(x86)%\Microsoft Visual Studio 9.0\VC\vcvarsall.bat"
call %ENVBAT%

devenv "NHINCBuildScripts.sln"
