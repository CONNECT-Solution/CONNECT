@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

SET "REPOS=%~1"
SET "TXN=%~2"

SET TMPCOUNTFILE=C:\Temp\pvtcount.dat

ECHO 0 > countpvt.out
SET FOUNDCOUNT=0
:: Make sure that the log message contains some text.
FOR /F "tokens=*" %%i in ('C:\Progra~1\VISUAL~1\bin\svnlook.exe changed -t "%TXN%" "%REPOS%"') do (
  ECHO %%i | FINDSTR /B /R "^[AU] "| FIND /C "nbproject/private" >> %TMPCOUNTFILE%
)

FOR /F "tokens=*" %%c in ('type %TMPCOUNTFILE%') do (
 SET /A FOUNDCOUNT=!FOUNDCOUNT!+%%c
)
DEL /F /Q %TMPCOUNTFILE%

IF NOT %FOUNDCOUNT%==0 (
  ECHO ---------------------------------------------------------- 1>&2
  ECHO Found nbproject/private in commit items : line count = %FOUNDCOUNT% 1>&2
  ECHO Do not commit nbproject/private dirs. Please see wiki. 1>&2
  ECHO ---------------------------------------------------------- 1>&2
  EXIT /B 1
) ELSE (
REM  echo Commit clean : count = %FOUNDCOUNT%
  EXIT /B 0
)

