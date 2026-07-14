@echo off
REM Exemplo de empacotamento com Java Runtime embutido usando jpackage.
REM Ajuste o nome do JAR principal conforme o arquivo exportado pelo Eclipse.

set APP_NAME=SistemaObras
set MAIN_JAR=SistemaObras.jar
set MAIN_CLASS=main.Main

jpackage ^
  --type exe ^
  --name %APP_NAME% ^
  --input dist ^
  --main-jar %MAIN_JAR% ^
  --main-class %MAIN_CLASS% ^
  --app-version 2.0 ^
  --vendor "Claitom Rodrigues" ^
  --win-shortcut ^
  --win-menu ^
  --dest instalador

pause
