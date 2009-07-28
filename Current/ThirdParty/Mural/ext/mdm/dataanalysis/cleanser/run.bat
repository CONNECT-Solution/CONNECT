@echo off

REM java -jar Cleanser-1.0.jar <Rule_Config_File> <System_Code> <Local_ID> <Update_Date> < User_Code>

java -server -Xms128M -Xmx1024M -jar Cleanser-1.0.jar sampleConfig.xml 11 -1 -1 22