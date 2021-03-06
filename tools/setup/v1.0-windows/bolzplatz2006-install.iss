; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

[Setup]
AppName=Bolzplatz 2006
AppVerName=Bolzplatz 2006, v1.0.3
AppPublisher=Xenoage Software
AppPublisherURL=http://www.xenoage.com
AppSupportURL=http://www.bolzplatz2006.de
AppUpdatesURL=http://www.bolzplatz2006.de
DefaultDirName={pf}\Bolzplatz 2006
DefaultGroupName=Bolzplatz 2006
AllowNoIcons=yes
LicenseFile=D:\Werkstatt\Bolzplatz2006\setup\v1.0-windows\bolzplatz2006\license.txt
InfoBeforeFile=D:\Werkstatt\Bolzplatz2006\setup\v1.0-windows\other\info.txt
OutputDir=D:\Werkstatt\Bolzplatz2006\setup\v1.0-windows\output
OutputBaseFilename=bolzplatz2006-1.0.3-install
Compression=lzma
SolidCompression=yes

[Languages]
Name: "german"; MessagesFile: "compiler:Languages\German.isl"
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "D:\Werkstatt\Bolzplatz2006\setup\v1.0-windows\other\de\bolzplatz2006.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Werkstatt\Bolzplatz2006\setup\v1.0-windows\other\de\einstellungen.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Werkstatt\Bolzplatz2006\setup\v1.0-windows\other\de\input.xml"; DestDir: "{app}\data\config"; Flags: ignoreversion
Source: "D:\Werkstatt\Bolzplatz2006\setup\v1.0-windows\other\de\input-default.xml"; DestDir: "{app}\data\config"; Flags: ignoreversion
Source: "D:\Werkstatt\Bolzplatz2006\setup\v1.0-windows\other\de\language.xml"; DestDir: "{app}\data\config"; Flags: ignoreversion
Source: "D:\Werkstatt\Bolzplatz2006\setup\v1.0-windows\other\de\language-default.xml"; DestDir: "{app}\data\config"; Flags: ignoreversion
Source: "D:\Werkstatt\Bolzplatz2006\setup\v1.0-windows\other\de\anleitung.html"; DestDir: "{app}"; Flags: ignoreversion
;exclude: gold files, temp files, savegame files, input config, language config
Source: "D:\Werkstatt\Bolzplatz2006\setup\v1.0-windows\bolzplatz2006\*"; DestDir: "{app}"; Excludes: "gold.jar,license-gold.txt,data\temp\*,data\savegames\career\*,data\savegames\worldcup\*,data\config\input.xml,data\config\input-default.xml,data\config\language.xml,data\config\language-default.xml"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[INI]
Filename: "{app}\bolzplatz2006.url"; Section: "InternetShortcut"; Key: "URL"; String: "http://www.bolzplatz2006.de"

[Icons]
Name: "{group}\Bolzplatz 2006"; Filename: "{app}\bolzplatz2006.exe"
Name: "{group}\Einstellungen"; Filename: "{app}\einstellungen.exe"
Name: "{group}\{cm:ProgramOnTheWeb,Bolzplatz 2006}"; Filename: "{app}\bolzplatz2006.url"
Name: "{group}\{cm:UninstallProgram,Bolzplatz 2006}"; Filename: "{uninstallexe}"
Name: "{userdesktop}\Bolzplatz 2006"; Filename: "{app}\bolzplatz2006.exe"; Tasks: desktopicon
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\Bolzplatz 2006"; Filename: "{app}\bolzplatz2006.exe"; Tasks: quicklaunchicon

[Run]
Filename: "{app}\bolzplatz2006.exe"; Description: "{cm:LaunchProgram,Bolzplatz 2006}"; Flags: nowait postinstall skipifsilent

[UninstallDelete]
Type: files; Name: "{app}\bolzplatz2006.url"
Type: files; Name: "{app}\*.exe"
Type: files; Name: "{app}\*.jar"
Type: files; Name: "{app}\*.txt"
Type: files; Name: "{group}\*.*"

