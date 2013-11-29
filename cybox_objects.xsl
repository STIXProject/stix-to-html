<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:fn="http://www.w3.org/2005/xpath-functions"
  

  xmlns:cybox="http://cybox.mitre.org/cybox-2"
  xmlns:Common="http://cybox.mitre.org/common-2"
  xmlns:stixCommon="http://stix.mitre.org/common-1"

  xmlns:AddressObject='http://cybox.mitre.org/objects#AddressObject-2'
  xmlns:URIObject='http://cybox.mitre.org/objects#URIObject-2'
  xmlns:EmailMessageObj="http://cybox.mitre.org/objects#EmailMessageObject-2"
  exclude-result-prefixes="cybox Common xsi fn EmailMessageObj AddressObject URIObject xs"
  
  version="2.0">
  
  <!--
    ····························································
  -->
  
  <!--
      Output URI & Link value without unnecessary nested schema tree structure
    -->
  <!-- //cybox:Observable//cybox:Properties[fn:resolve-QName(fn:data(@xsi:type), .)=fn:QName("http://cybox.mitre.org/objects#URIObject-2", "URIObjectType")]  -->
  <!-- <xsl:template match="cybox:Properties[fn:resolve-QName(fn:data(@xsi:type), .)=fn:QName('http://cybox.mitre.org/objects#URIObject-2', 'URIObjectType')]|cybox:Properties[fn:resolve-QName(fn:data(@xsi:type), .)=fn:QName('http://cybox.mitre.org/objects#URIObject-2', 'LinkObjectType')]"> -->
  <!-- fn:local-name(fn:resolve-QName(fn:data(@xsi:type), .)) -->
  <!-- <xsl:template match="cybox:Properties[some $currentXsiType in ('URIObjectType', 'LinkObjectType') satisfies $currentXsiType = fn:local-name(fn:resolve-QName(fn:data(@xsi:type), .))]"> -->
  <!-- <xsl:template match="cybox:Properties['URIObjectType' = fn:local-name(fn:resolve-QName(fn:data(@xsi:type), .))]"> -->
  
  <xsl:template match="cybox:Properties[fn:resolve-QName(fn:data(@xsi:type), .)=fn:QName('http://cybox.mitre.org/objects#URIObject-2', 'URIObjectType')]|cybox:Properties[fn:resolve-QName(fn:data(@xsi:type), .)=fn:QName('http://cybox.mitre.org/objects#URIObject-2', 'LinkObjectType')]"><fieldset>
    <legend>URI</legend>
    <div class="container cyboxPropertiesContainer cyboxProperties">
      <div class="heading cyboxPropertiesHeading cyboxProperties">
        <xsl:apply-templates select="URIObject:Value" mode="cyboxProperties" />
      </div>
    </div>
  </fieldset>
  </xsl:template>
  <xsl:template match="URIObject:Value" mode="cyboxProperties">
    Value <xsl:value-of select="Common:Defanged(@is_defanged, @defanging_algorithm_ref)" />
    <xsl:choose>
      <xsl:when test="@condition!=''"><xsl:value-of select="Common:ConditionType(@condition)" /></xsl:when>
      <xsl:otherwise> = </xsl:otherwise>
    </xsl:choose>
    <xsl:value-of select="." />
  </xsl:template>
  
  <!--
    ····························································
  -->
  
  
</xsl:stylesheet>