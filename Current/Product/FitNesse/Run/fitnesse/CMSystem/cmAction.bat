::start nant /f:%ProductDirectory%\Personal.Build.xml FitNesse.CMSystem.Action -D:FitNesse.CMSystem.Action.Type=%1 -D:FitNesse.CMSystem.Action.Item=%~f2
::echo off
::echo %~f2
::echo %~d2
::echo %~p2
::echo %~n2
::echo %~dp2
::echo %~nx2

set ParentDirectory=%~dp2
set Target=%~f2
set TargetName=%~n2

set Type=Directory
IF %~nx2==content.txt set Type=File
IF %~nx2==properties.xml set Type=File

cd "%ParentDirectory%"

svn status %Target%

FOR /F %%A IN ('svn status %Target%') DO set Code=%%A

echo CODE="%CODE%"
IF [%CODE%]==[] GOTO end

GOTO %1

:cmUpdate
IF %CODE%==? GOTO add
GOTO end

:cmEdit
GOTO end

:cmDelete
IF %CODE%==A GOTO revert
IF %CODE%==? GOTO end
GOTO delete

:delete
IF %CODE%==! svn update %Target%
svn delete %Target%
GOTO end

:add
svn add %Target%
IF %Type%==Directory GOTO ignore
GOTO end

:ignore
svn propset svn:ignore *.zip %TargetName%
GOTO end

:revert
svn revert %Target%
GOTO end

:end