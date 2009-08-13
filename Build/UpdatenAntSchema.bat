CALL SetEnv.bat
nant.exe -buildfile:UpdatenAntSchema.xml
@echo off
SET /P variable="Hit Enter to exit."