..\Build\nant\bin\nant.exe -buildfile:Personal.Build.xml UpdateSource
IF NOT %ERRORLEVEL%==0 exit /B %ERRORLEVEL%
..\Build\nant\bin\nant.exe -buildfile:Personal.Build.xml UpdateWorkspace
SET /P variable="Hit Enter to continue."