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

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $DIR/..

mkdir temp
cp -r ../../*.xsl ../../*.css ../../*.js ../../images temp
cp resources/*.xsl temp

java \
  -jar $SAXON_HOME/saxon9he.jar \
  -xsl:temp/stix_to_html.xsl \
  -s:input/stix_sample.xml \
  -o:output/stix_sample.output_with_custom_title_header_footer_css.html

