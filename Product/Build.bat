..\Build\nant\bin\nant.exe -buildfile:Personal.Build.xml UpdateSource
IF NOT %ERRORLEVEL%==0 exit /B %ERRORLEVEL%
..\Build\nant\bin\nant.exe -buildfile:Personal.Build.xml %1 %2 %3 %4 %5 %6 %7 %8
SET /P variable="Hit Enter to continue."