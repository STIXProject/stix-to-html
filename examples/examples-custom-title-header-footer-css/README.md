Purpose of "examples-custom-title-header-footer-css"
===

This directory contains an example of how the title, header, footer, and css can be customized without digging into the gits of stix-to-html.

The script bin/run_stix_to_html__with_custom_title_header_footer_css.sh does the following simple steps:
 * copies the core stix-to-html files (*.xsl, *.js, *.css, and images/) to a temp directory
 * copies the customized stix_to_html__customized.xsl to the same temp directory
 * run stix_to_html__customized.xsl on a sample stix xml document.
 
If you want to do this without the script, just edit stix_to_html__customized.xsl and either put it in the main stix-to-html directory or copy the main stix-to-html files and your custom file into a separate directory.

Essentially what this does is to run the normal templates in stix_to_html.xsl but some named templates (for the title, header, footer, and css) are overridden in stix_to_html__customized.xsl.
