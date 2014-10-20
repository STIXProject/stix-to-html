#!/bin/sh
# Copyright (c) 2014, The MITRE Corporation. All rights reserved.
# See LICENSE.txt for complete terms.
#
# Linux/OSX launcher script.  Shortcut to calling `java -jar stix-to-html.jar -i INPUT.XML -o OUTPUT.HTML`
#
# Usage:
#     stix-to-html.sh -i INPUT.XML -o OUTPUT.HTML
#
# Arguments: 
#     -i, --infile            input STIX xml document filename
#     -o, --outfile           output HTML document filename
#
# Example:
#     stix-to-html.sh -i C:\path\to\stix.xml -o C:\path\to\stix.out.html
#

_JAVA=java

if [ -f "${JAVA_HOME}/bin/java" ]; then
  _JAVA="${JAVA_HOME}/bin/java"
fi

"${_JAVA}" -Xmx512m -jar stix-to-html.jar "$@"
