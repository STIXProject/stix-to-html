@echo off
rem
rem Windows launcher script.  Shortcut to calling `java -jar stix-to-html.jar -i INPUT.XML -o OUTPUT.HTML`
rem
rem Usage:
rem     stix-to-html.bat -i INPUT.XML -o OUTPUT.HTML
rem
rem Arguments: 
rem     -i, --infile            input STIX xml document filename
rem     -o, --outfile           output HTML document filename
rem
rem Example:
rem     stix-to-html -i C:\path\to\stix.xml -o C:\path\to\stix.out.html
rem
set _JAVA=java.exe
if exist "%JAVA_HOME%\bin\java.exe" set _JAVA="%JAVA_HOME%\bin\java.exe"
%_JAVA% -jar stix-to-html.jar %* 
