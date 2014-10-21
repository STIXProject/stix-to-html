@echo off
rem 
rem  Copyright (c) 2014, The MITRE Corporation. All rights reserved.
rem  See LICENSE.txt for complete terms.
rem
rem Windows launcher script.  Shortcut to calling `java -jar stix-to-html.jar -i INPUT.XML -o OUTPUT.HTML`
rem
rem Usage:
rem     stix-to-html.bat -i INPUT.XML -o OUTPUT.HTML
rem
rem Arguments: 
rem     -i, --infile            input STIX xml document filename
rem 		--indir				input directory (must be used with --outdir)
rem     -o, --outfile           output HTML document filename
rem     	--outdir			output directory (must be used with --indir)
rem     -d, --debug             print debug information
rem
rem Examples:
rem     stix-to-html -i C:\path\to\stix.xml -o C:\path\to\stix.out.html
rem 	stix-to-html --indir C:\path\to\stix\ --outdir C:\path\to\output\
rem
set _JAVA=java.exe
if exist "%JAVA_HOME%\bin\java.exe" set _JAVA="%JAVA_HOME%\bin\java.exe"
%_JAVA% -jar stix-to-html.jar %* 
