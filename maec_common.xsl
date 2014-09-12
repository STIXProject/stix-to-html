<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="2.0"
  
  xmlns:fn="http://www.w3.org/2005/xpath-functions"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:stix="http://stix.mitre.org/stix-1"
  xmlns:stixCommon="http://stix.mitre.org/common-1"
  xmlns:cybox="http://cybox.mitre.org/cybox-2"
  xmlns:ttp="http://stix.mitre.org/TTP-1"
  
  xmlns:maecBundle="http://maec.mitre.org/XMLSchema/maec-bundle-4"
  xmlns:maecPackage="http://maec.mitre.org/XMLSchema/maec-package-2"
  xmlns:maecInstance="http://stix.mitre.org/extensions/Malware#MAEC4.0-1"
  
>
  
  <xsl:template name="processMaecCapabilityContents">
    <xsl:if test="maecBundle:Strategic_Objective">
      <xsl:variable name="contents">
        <xsl:apply-templates select="maecBundle:Strategic_Objective" />
      </xsl:variable>
      <xsl:copy-of select="stix:printNameValueTable('Strategic Objective', $contents)" />
    </xsl:if>  
    <xsl:if test="maecBundle:Tactical_Objective">
      <xsl:variable name="contents">
        <xsl:apply-templates select="maecBundle:Tactical_Objective" />
      </xsl:variable>
      <xsl:copy-of select="stix:printNameValueTable('Tactical Objective', $contents)" />
    </xsl:if>  
  </xsl:template>
  
  <xsl:template name="processMaecStrategicObjectiveContents">
    <xsl:apply-templates select="." />
  </xsl:template>
  
  <xsl:template name="processMaecTacticalObjectiveContents">
    <xsl:apply-templates select="." />
  </xsl:template>
  
  <xsl:template name="processMaecProcessContents">
    <xsl:apply-templates select="*" mode="cyboxProperties" />
  </xsl:template>
  
  <xsl:template name="processMaecBehaviorContents">
    <xsl:apply-templates select="*" mode="cyboxProperties" />
  </xsl:template>
  
  <xsl:template name="processMaecObjectContents">
    <xsl:apply-templates select="." />
  </xsl:template>
  
  <xsl:template name="processMaecActionContents">
    <xsl:apply-templates select="." />
  </xsl:template>
  
  <xsl:template name="processMaecFindingBundlesContents">
    <xsl:apply-templates select="." />
  </xsl:template>
  
  <xsl:template name="processMaecActionEquivalenceContents">
    <div>Action references:</div>
    <xsl:apply-templates select="maecPackage:Action_Reference" mode="#default" />
  </xsl:template>
  
  <xsl:template name="processMaecInstanceInsideStixContents">
    <xsl:apply-templates select="." mode="cyboxProperties" />
  </xsl:template>
  
  <xsl:template match="maecBundle:Strategic_Objective|maecBundle:Tactical_Objective">
    <xsl:apply-templates select="*" mode="cyboxProperties" />
  </xsl:template>
  
  <xsl:template match="maecPackage:Malware_Instance_Object_Attributes">
    <xsl:apply-templates select="*" mode="cyboxProperties" />
  </xsl:template>
  
  <xsl:template match="maecPackage:Analysis">
    <xsl:apply-templates select="*" mode="cyboxProperties" />
  </xsl:template>
  
  <xsl:template match="maecPackage:Tool">
    <xsl:apply-templates select="*" mode="cyboxProperties" />
  </xsl:template>
  
  <xsl:template match="maecPackage:Finding_Bundles">
    <xsl:apply-templates select="*" mode="cyboxProperties" />
  </xsl:template>
  
  <xsl:template match="maecPackage:Bundle">
    <xsl:apply-templates select="*" mode="cyboxProperties" />
  </xsl:template>
  
  <xsl:template match="maecPackage:Meta_Analysis">
    <div class="metadataBlock">
      <div class="metadataCaption">Meta Analysis</div>
      <xsl:apply-templates select="*" mode="cyboxProperties" />
    </div>
  </xsl:template>
  
  <xsl:template match="maecPackage:Action_Equivalence">
    <xsl:apply-templates mode="cyboxProperties" />
  </xsl:template>
  
  <xsl:template match="maecInstance:MAEC">
    <xsl:apply-templates mode="cyboxProperties" />
  </xsl:template>
  
  <xsl:template match="maecBundle:Object_Collections">
    <div>Object Collections:</div>
    <xsl:apply-templates />
  </xsl:template>
  
  <xsl:template match="maecBundle:Action_Collections">
    <div>Action Collections:</div>
    <xsl:apply-templates />
  </xsl:template>
  
  <xsl:template match="maecBundle:Initiated_Actions" mode="cyboxProperties">
    <div>Initiated Actions:</div>
    <xsl:apply-templates mode="#default" />
  </xsl:template>
  
  <xsl:template match="maecBundle:Action_Reference[@idref]|maecBundle:Injected_Process[@idref]">
    <xsl:call-template name="headerAndExpandableContent">
      <xsl:with-param name="targetId" select="fn:data(@idref)" />
      <xsl:with-param name="relationshipOrAssociationType" select="()" />
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template match="maecBundle:Spawned_Process[@idref]">
    <xsl:call-template name="headerAndExpandableContent">
      <xsl:with-param name="targetId" select="fn:data(@idref)" />
      <xsl:with-param name="relationshipOrAssociationType" select="()" />
    </xsl:call-template>
  </xsl:template>
  
  
  <xsl:template match="maecBundle:Capability|maecBundle:Behavior|maecBundle:Action" mode="cyboxProperties">
    <xsl:apply-templates select="." mode="#default" />
  </xsl:template>
  
  <xsl:template match="maecPackage:Development_Environment|maecPackage:Configuration_Details">
    <xsl:apply-templates select="." mode="cyboxProperties" />
  </xsl:template>
  
  <xsl:template match="maecPackage:Dynamic_Analysis_Metadata">
    <xsl:apply-templates select="." mode="cyboxProperties" />
  </xsl:template>
  
  <xsl:template match="maecPackage:Action_Equivalence" mode="cyboxProperties">
    <xsl:apply-templates select="." />
  </xsl:template>
  
  <xsl:template match="maecPackage:Action_Equivalence">
    <div>Action References</div>
    <xsl:apply-templates />
  </xsl:template>
  
  
  
</xsl:stylesheet>