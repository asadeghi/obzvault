rem ##########################################################################
rem Copyright (C) 2009 - 2011, Duncan Bayne & Armin Sadeghi
rem 
rem This program is free software; you can redistribute it and/or modify it
rem under the terms of version 3 of the GNU Lesser General Public License 
rem as published by the Free Software Foundation.
rem 
rem This program is distributed in the hope that it will be
rem useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
rem MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
rem Public License for more details.
rem 
rem The GNU Lesser General Public License can be viewed at
rem http://www.gnu.org/licenses/lgpl-3.0.txt
rem 
rem To find out more about ObzVault, visit
rem https://github.com/asadeghi/obzvault
rem ##########################################################################

@echo off
setlocal

set OUTPUTDIR=%1
set /p VERSION=<version.txt

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
  echo usage: build_windows.bat outputdir
  echo.
  echo example: build_windows.bat \\obz1\build\vault
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

