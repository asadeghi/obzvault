@echo off
setlocal

set OUTPUTDIR=%1
set VERSION=%2

if "%OUTPUTDIR%" == "" goto usage
if "%VERSION%" == "" goto usage
if "%OBZROOT%" == "" goto obzerror

set TARGETDIR="%OUTPUTDIR%\trial"
set INST_SUFFIX=_trial
set TARGET=Trial
call :build
if not "%ERRORLEVEL%"=="0" goto error

set TARGETDIR="%OUTPUTDIR%\release"
set INST_SUFFIX=
set TARGET=Release
call :build
if not "%ERRORLEVEL%"=="0" goto error

GOTO success

:obzerror
  echo This script must be run from an OBZ command prompt.
  goto finished

:usage
  echo usage: build_windows.bat outputdir version
  echo.
  echo example: build_windows.bat \\obz1\build\vault 3.2.506
  goto finished

:error
  echo FAILED!!!!!!!!!!!!
  goto finished

:success
  echo Success!
  goto finished

:build
  echo Escaping %TARGETDIR% ...
  echo %TARGETDIR% > dir.tmp
  sed s/.$//g dir.tmp > dir_del.tmp
  sed s/\\/\\\\/g dir_del.tmp > dir_esc.tmp
  set /p ESCAPED_TARGETDIR=<dir_esc.tmp
  set  ESCAPED_TARGETDIR="%ESCAPED_TARGETDIR%"
  del /q *.tmp

  echo Building installer ...
  pushd win32_files
  if exist obzvault_working*.iss del /q obzvault_working*.iss
  copy obzvault%INST_SUFFIX%.iss obzvault_working_0.iss
  sed s/\[VERSION\]/%VERSION%/g obzvault_working_0.iss > obzvault_working_1.iss
  if not "%ERRORLEVEL%"=="0" goto :EOF
  sed s/\[OUTPUTDIR\]/%ESCAPED_TARGETDIR%/g obzvault_working_1.iss > obzvault_working_2.iss
  if not "%ERRORLEVEL%"=="0" goto :EOF
  %OBZROOT%\tools\windows\bin\innosetup\iscc.exe obzvault_working_2.iss
  if not "%ERRORLEVEL%"=="0" goto :EOF
  del /q obzvault_working*.iss

  echo Moving installer to %TARGETDIR%...
  copy obzvault_%VERSION%%INST_SUFFIX%.exe %OUTPUTDIR%\obzvault_%VERSION%%INST_SUFFIX%.exe
  if not "%ERRORLEVEL%"=="0" goto :EOF
  
  :tryagain
    del /q obzvault_%VERSION%.exe
    if exist obzvault_%VERSION%.exe goto tryagain

  popd
    
  goto :EOF

:finished
  goto :EOF

