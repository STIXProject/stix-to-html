# stix-to-html steps to add new child element to top level item

## Individual steps

1. edit cybox_common.xsl's process*Whatever*Contents template to add an xsl:if block for new child element

  example:

  ```
  <xsl:if test="report:TTPs/report:TTP">
    <xsl:variable name="contents">
      <xsl:apply-templates select="report:TTPs/report:TTP" mode="cyboxProperties" />
    </xsl:variable>
    <xsl:copy-of select="stix:printNameValueTable('TTPs', $contents)" />
  </xsl:if>  
  ```

2. in stix_to_html.xsl, search for "HELP_UPDATE_STEP_1D", and add element to match expression (e.g. report:TTP)

  example:

  ```
  <xsl:template
  match="cybox:Observable|stixCommon:Observable|...|report:TTP|...|*:Description|*:Short_Description"
  mode="printReference">

  ```

3. in stix_to_html.xsl find the printGenericItemForReferenceList template and then adjust the xsl:when block for the new child element (e.g. if there's already a stixCommon:TTP, then add a block to the test expression for report:TTP)

  example:

  ```
  <xsl:when test="self::stix:TTP|self::stixCommon:TTP|self::report:TTP">
    <div class="containerTtp">
      <xsl:call-template name="processTTPContents"/>
    </div>
  </xsl:when>
  ```
