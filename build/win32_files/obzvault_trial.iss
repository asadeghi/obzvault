; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{8F0FDA2B-6B1A-4716-9469-491803AD8A8B}}
AppName=OffByZero Vault Trial
AppVerName=OffByZero Vault [VERSION] Trial
AppPublisher=Duncan Bayne & Armin Sadeghi
AppPublisherURL=https://github.com/asadeghi/obzvault
AppSupportURL=https://github.com/asadeghi/obzvault
AppUpdatesURL=https://github.com/asadeghi/obzvault
DefaultDirName={pf}\OffByZero\OBZVault [VERSION] Trial
DefaultGroupName=OffByZero Vault [VERSION] Trial
LicenseFile=..\shared_files\licence.rtf
OutputBaseFilename=obzvault_[VERSION]_trial
OutputDir=.
SetupIconFile=app.ico
Compression=lzma
SolidCompression=yes
ChangesAssociations=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "vault.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "[OUTPUTDIR]\obzvault.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "[OUTPUTDIR]\lib\AbsoluteLayout.jar"; DestDir: "{app}\lib"; Flags: ignoreversion
Source: "[OUTPUTDIR]\lib\appframework-1.0.3.jar"; DestDir: "{app}\lib"; Flags: ignoreversion
Source: "[OUTPUTDIR]\lib\swing-layout-1.0.3.jar"; DestDir: "{app}\lib"; Flags: ignoreversion
Source: "[OUTPUTDIR]\lib\swing-worker-1.1.jar"; DestDir: "{app}\lib"; Flags: ignoreversion
Source: "[OUTPUTDIR]\lib\ui.jar"; DestDir: "{app}\lib"; Flags: ignoreversion
Source: "doc.ico"; DestDir: "{app}"; Flags: ignoreversion
Source: "app.ico"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\shared_files\licence.rtf"; DestDir: "{app}"; Flags: ignoreversion
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\OBZVault Trial"; Filename: "{app}\vault.bat"; IconFilename: "{app}\app.ico"; WorkingDir: "{app}"; Flags: runminimized
Name: "{group}\Uninstall OffByZero Vault [VERSION] Trial"; Filename: "{uninstallexe}"
Name: "{commondesktop}\OBZVault Trial"; Filename: "{app}\vault.bat"; Tasks: desktopicon; WorkingDir: "{app}"; IconFilename: "{app}\app.ico"; Flags: runminimized
Name: "{group}\Licence"; Filename: "{app}\licence.rtf"
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\OBZVault Trial"; Filename: "{app}\vault.bat"; Tasks: quicklaunchicon; WorkingDir: "{app}"; IconFilename: "{app}\app.ico"; Flags: runminimized

[Run]
Filename: "{app}\vault.bat"; Description: "{cm:LaunchProgram,OBZVault}"; WorkingDir: "{app}"; Flags: nowait postinstall skipifsilent runminimized

[Registry]
Root: HKCR; Subkey: ".vault"; ValueType: string; ValueName: ""; ValueData: "OBZVault[VERSION]"; Flags: uninsdeletekey
Root: HKCR; Subkey: "OBZVault[VERSION]"; ValueType: string; ValueName: ""; ValueData: "OffByZero Vault Encrypted Text Document"; Flags: uninsdeletekey
Root: HKCR; Subkey: "OBZVault[VERSION]\DefaultIcon"; ValueType: string; ValueName: ""; ValueData: "{app}\doc.ico"
Root: HKCR; Subkey: "OBZVault[VERSION]\shell\open\command"; ValueType: string; ValueName: ""; ValueData: "javaw -jar ""{app}\obzvault.jar"" ""%1"""

[Code]
function InitializeSetup(): Boolean;
var
  ResultCode: Integer;
  Version: String;
  JavaFound: Boolean;
begin
  JavaFound := False;
  if RegQueryStringValue(HKEY_LOCAL_MACHINE, 'Software\JavaSoft\Java Runtime Environment', 'CurrentVersion', Version) then
  begin
    if ((CompareText('1.5', Version) = 0) OR (CompareText('1.6', Version) = 0)) then
    begin
      JavaFound := True;
    end
  end
  
  if (not JavaFound) then
  begin
    MsgBox('You must install Java 1.5 or Java 1.6 before installing OBZVault.', mbCriticalError, MB_OK);
    ShellExec('', 'http://www.java.com/en/download/manual.jsp', '', '', SW_SHOW, ewNoWait, ResultCode);
  end

  Result := JavaFound;
end;
