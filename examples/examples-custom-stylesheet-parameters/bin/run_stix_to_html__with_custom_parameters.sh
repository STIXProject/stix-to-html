#!/bin/bash
#
# purpose: demonstrate how to run stix-to-html with different stylesheet
#   parameters. By default, this script runs with the non-default boolean
#   stylesheet parameters.  There's also a copy of the commandline (commented
#   out) that includes all the default parameters.
#
# requirements:
#  - SAXON_HOME should be the directory containing saxon9he.jar
#  - STIX_TO_HTML_HOME should be the directory where the github stix-to-html
#    project was checked out (or where the zip file was extracted)
#

###
### THIS SCRIPT IS SETUP TO RUN WITH THESE NONDEFAULT VALUES
### 
java \
  -jar $SAXON_HOME/saxon9he.jar \
  -xsl:$STIX_TO_HTML_HOME/stix_to_html.xsl \
  -s:input/stix_sample.xml \
  -o:output/stix_sample.output_with_nondefault_parameters.html \
  '?includeFileMetadataHeader=false()' \
  '?includeStixHeader=false()' \
  '?enablePreformattedDescriptions=true()' \
  '?displayConstraints=false()'
### END NONDEFAULT VALUES

###
### THESE ARE THE DEFAULT VALUES
### 
# java \
#   -jar $SAXON_HOME/saxon9he.jar \
#   -xsl:$STIX_TO_HTML_HOME/stix_to_html.xsl \
#   -s:input/stix_sample.xml \
#   -o:output/stix_sample.output_with_default_parameters.html \
#   '?includeFileMetadataHeader=true()' \
#   '?includeStixHeader=true()' \
#   '?enablePreformattedDescriptions=false()' \
#   '?displayConstraints=true()'
### END DEFAULT VALUES
