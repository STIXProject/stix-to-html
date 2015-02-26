# STIX XML -> HTML Transform v1.0

~~~
Copyright (c) 2015 - The MITRE Corporation
All rights reserved. See LICENSE.txt for complete terms.
~~~

~~~
BY USING THE STIX XML to HTML TRANSFORM, YOU SIGNIFY YOUR ACCEPTANCE OF THE 
TERMS AND CONDITIONS OF USE.  IF YOU DO NOT AGREE TO THESE TERMS, DO NOT USE 
THE STIX XML to HTML TRANSFORM.

For more information, please refer to the terms.txt file.
~~~

## STIX XML to HTML transform v1.0
Compatible with STIX v1.1

This is an xslt to transform a stix 1.0/1.0.1/1.1 document containing metadata and
different categories of top level items into html for easy viewing.

In addition to stix documents, cybox and maec documents are also supported.

The first two tables on the output html page are 1) a metadata table
showing information about the input document and when the content was
converted, and 2) information obtained from the stix header element.

Regarding the stix header, we only include very simple support for
the handling instructions for this release.  It will include the text contents
of the simpleMarking:Statement.  The marking:Controlled_Structure with its
xpath is not used yet. 

Currently we support the following top level entities and sub-entities:
- **Observables**
  - All (except no support for MAEC objects yet)
- **Indicators**
  - Title
  - Description
  - Valid_Time_Position
  - Suggested_COAs
  - Alternative_ID
  - Observable
  - Composite_Indicator_Expression
  - Indicated_TTP
  - Kill_Chain_Phases
  - Confidence
  - Sighting
  - Test_Mechanisms
  - Likely_Impact
  - Suggested_COAs
  - Handling
  - Related_Indicators
  - Producer
- **TTPs**
  - Description
  - Intended_Effect
  - Behavior
  - Resources
  - Victim_Targeting
  - Exploit_Targets
  - Related_TTPs
  - Kill_Chain_Phases
  - Information_Source
  - Handling
- **Exploit Targets**
  - Title
  - Vulnerability
  - Weakness
  - Configuration
  - Potential_COAs
  - Information_Source
  - Handling
- **Incidents**
  - Time
  - Description
  - Categories
  - Reporter
  - Responder
  - Coordinator
  - Victim
  - Affected_Assets
  - Impact_Assessment
  - Status
  - Related_Indicators
  - Related_Observables
  - Leveraged_TTPs
  - Attributed_Threat_Actors
  - Intended_Effect
  - Security_Compromise
  - Discovery_Method
  - Related_Incidents
  - COA_Requested
  - COA_Taken
  - Confidence
  - Contact
  - History
  - Handling
- **Courses of Action**
  - Stage
  - Type
  - Description
  - Objective
  - Structured_COA
  - Impact
  - Cost
  - Efficacy
  - Handling
- **Campaigns**
  - Title
  - Names
  - Status
  - Intended_Effect
  - Related_Incidents
  - Related_TTPs
  - Related_Indicators
  - Attribution
  - Associated_Campaigns
  - Confidence
  - Activity
  - Information_Source
  - Handling
- **Threat Actors**
  - Title
  - Identity
  - Type
  - Motivation
  - Intended_Effect
  - Planning_And_Operational_Support
  - Observed_TTPs
  - Associated_Campaign
  - Associated_Actors
  - Handling
  - Confidence
  - Information_Source
- **MAEC Bundle**
  - Malware_Object_Instance_Attributes
  - AV_Classification
  - Action
  - Behavior
  - Capability
  - Collection
  - Object
- **MAEC Package**
  - Malware_Subject
    - Malware_Object_Instance_Attributes
    - Label
    - Analyses
    - Finding_Bundles
- **MAEC General Support**
  - Process Tree

Each category of top level "items" is turned into a main table on the page.
The item itself is expandable and other nested content pointing to other
"items" and objects are also expandable.

At the moment, "items" are expandable when they have inline content with an
id attribute or when it references content in another part of the document with
an idref attribute.  (Content "item" without an id or idref is displayed
inline.)

Indicator and observable composition is supported.

The contents of description elements are treated as text.  This release does
not handle embedded html documents; they will be displayed as escaped text.

This is a work in progress.  Feedback is most welcome!

requirements:
 - XSLT 2.0 engine (this has been tested with Saxon 9.5)
 - a STIX 1.0/1.0.1/1.1 input xml document

## Releases
### current release
 * v1.0
   2014-09-30  
   https://github.com/STIXProject/stix-to-html/issues?direction=desc&milestone=4&page=1&sort=updated&state=closed

### previous releases
 * v1.0b5 alpha
   2014-08-11  
   https://github.com/STIXProject/stix-to-html/issues?direction=desc&milestone=3&page=1&sort=updated&state=closed
 * v1.0beta4
   2014-05-15  
   https://github.com/STIXProject/stix-to-html/issues?direction=desc&milestone=2&page=1&sort=updated&state=closed
 * v1.0beta3
   2014-01-15  
   https://github.com/STIXProject/stix-to-html/issues?direction=desc&milestone=1&page=1&sort=updated&state=closed
 * v1.0beta2  
   2013-10-24  
   https://github.com/STIXProject/Tools/issues?milestone=1&state=open
 * v1.0beta1  
   2013-08-28


## More information on STIX

STIX - http://stix.mitre.org

## Included Files

 - **README.md**: this file.
 - **stix_to_html.xsl**: the top-level STIX XML to HTML XSL Transform.
 - **stix_to_hmtl__customized.xsl**: [not required] example of how to use the stix stylesheet and override the default title, header, footer, and css to customize the output.
 - **common.css**: common css styles
 - **common.js**: common javascript code
 - **common_top_level_tables.xsl**: the xslt that produces the top level item tables (observables, TTPs, etc)
 - **cybox_common.xsl**: common CybOX transformations used by stix_to_html.xsl.
 - **cybox_objects.xsl**: cybox object-specific templates (primarily provides templates for objects that show up in cybox:Properties)
 - **cybox_objects__customized.xsl**: recommended place for end users to add custom cybox templates without modifying core css files.
 - **cybox_util.xsl**: commonly used functions
 - **icons.xsl**: stylesheet code to read in the svg icons for the main item type logos
 - **images/*.svg and *.svgz**: svg vector images for the main item type logos (the *.svg files are used and pulled in via the xsl and included in the output html inline)
 - **maec_common.xsl**: maec specific xslt templates
 - **normalize.xsl**: used by stix
 - **stix_common.xsl**: common stix transformations used by stix_to_html.xsl.
 - **stix_objects.xsl**: stix object-specific templates
 - **stix_objects__customized.xsl**: recommended place for end users to add custom stix templates without modifying core css files.
 - **theme_default.css**: css styles used for main item type background colors (observables, ttps, indicators, etc)
 - **wgxpath.install.js**: xpath support in javascript for browsers that don't support it (IE)  [source: http://code.google.com/p/wicked-good-xpath/]

## Usage Notes

Use with your favorite XML Editor or XSLT 2.0 interpreter (e.g. Saxon).

Here are commands to run a single document or multiple documents through the
stylesheet with Saxon. 

Single file:

~~~
  java -jar /opt/saxon/saxon9he.jar -xsl:xslt/stix_to_html.xsl -s:input.xml -o:output.html
~~~

Directory of files ("inputdir" and "outputdir" are directories):

~~~
  java -jar /opt/saxon/saxon9he.jar -xsl:stix_to_html.xsl -s:inputdir -o:outputdir
~~~


## Customization

### More Information on Customization

In addition to the following summary, the wiki has more information and examples of how to customize stix-to-html at https://github.com/STIXProject/stix-to-html/wiki#customizing-the-stix-to-html-transform

In particular look at these two wiki pages:
 * [Customizing stix-to-html transform](https://github.com/STIXProject/stix-to-html/wiki/Customizing-stix-to-html-transform)
 * [Supporting new stix and cybox elements](https://github.com/STIXProject/stix-to-html/wiki/Supporting-new-stix-and-cybox-elements)

### Quick Summary of Customization

The header, footer, title, and css can easily be customized.

NOTE: There is an example of customizing these elements in the git repo at [stix-to-html/examples/examples-custom-title-header-footer-css](https://github.com/STIXProject/stix-to-html/tree/master/examples/examples-custom-title-header-footer-css).


Inside stix_to_html.xsl there are three corresponding named templates:
 * customHeader
 * customFooter
 * customTitle
 * customCss
 
The easiest and cleanest way to customize this is to create your own
stylesheet, import stix_to_html.xsl, and then define your own named templates
with the above names.

There is an example of this in stix_to_html__customized.xsl

The css that determines the color scheme for the main top level items
(Observables, TTPs, Indicators, etc) is contained in theme_default.css.  The
colors are defined with css's hsl() color definitions.  The general color can
be tweaked by just adjusting the first parameter, the hue (which is specified
in degrees and varies from 0-359).  By adjusting the saturation and lightness,
the darness or lightness can be adjusted.

There are also three stylesheet parameters that may be used to control the
output.  Each one is a boolean and can be passed in on the command-line or
programatically.

_NOTE: There is an example of passing in custom parameters in the git repo at [stix-to-html/examples/examples-custom-stylesheet-parameters](https://github.com/STIXProject/stix-to-html/tree/beta3/examples/examples-custom-stylesheet-parameters)_

Here are the three stylesheet parameters.  Each one defaults to true except enablePreformattedDescriptions which defaults to false.

 * includeFileMetadataHeader
 * includeStixHeader
 * displayConstraints
 * enablePreformattedDescriptions

"includeFileMetadataHeader" determines if the header with the stix version,
filename, and html generation date is going to be displayed.

"includeStixHeader" determines if the header table (generated from the
stix:STIX_Header contents) is dislayed.

"displayContraints" determines if the contraint properties on cybox:Properties
(and other similary formatted data elements) will be displayed.  This includes
such information what type of matching is performed (equals or string match).

"enablePreformattedDescriptions" should be used if the stix xml document has descriptions fields that are pre-formatted plain text where the line wrapping should be preserved.

### List of Objects With Object-specific Templates

 * cybox
   * EmailMessageObjectType (commented-out example in cybox_objects.xsl)
   * Hash (cybox_objects.xsl)
   * HTTP_Request_Response (cybox_objects.xsl)
   * [not really object-specific] any cybox:Properties text node with ##comma## delimited range or list (cybox_objects.xsl)
 * indicator
   * sighting (stix_objects.xsl)
   * any description element in any namespace (stix_objects.xsl)
