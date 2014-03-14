<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  exclude-result-prefixes="xs"
  version="2.0">
  
  <xsl:template match="/">
    <xsl:apply-templates mode="identifyAnonymousItems" />
  </xsl:template>
  
  <xsl:template match="@*|node()" mode="identifyAnonymousItems">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" mode="#current"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="*:Exploit_Target[not(@id) and not(@idref)]" mode="identifyAnonymousItems">
    <xsl:copy>
      <xsl:variable name="newId" select="generate-id(.)" />
      <xsl:attribute name="id" select="concat('AUTO_GENERATED_ID_', $newId)" />
      <xsl:apply-templates select="@*|node()" mode="#current"/>
    </xsl:copy>
  </xsl:template>
  
</xsl:stylesheet>