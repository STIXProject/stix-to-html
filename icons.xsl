<?xml version="1.0" encoding="UTF-8"?>
<!--
  Copyright (c) 2013 – The MITRE Corporation
  All rights reserved. See LICENSE.txt for complete terms.
  
  This styleshseet has logic that can be reused by various components in the
  stix-to-html transformation.
 -->

<xsl:stylesheet 
    version="2.0"
    xmlns:stix="http://stix.mitre.org/stix-1"
    xmlns:cybox="http://cybox.mitre.org/cybox-2"
    
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    
    xmlns:ns_extend="http://ns.adobe.com/Extensibility/1.0/"
    xmlns:ns_ai="http://ns.adobe.com/AdobeIllustrator/10.0/"
    xmlns:ns_graphs="http://ns.adobe.com/Graphs/1.0/"
    xmlns:ns_vars="http://ns.adobe.com/Variables/1.0/"
    xmlns:ns_imrep="http://ns.adobe.com/ImageReplacement/1.0/"
    xmlns:ns_sfw="http://ns.adobe.com/SaveForWeb/1.0/"
    xmlns:ns_custom="http://ns.adobe.com/GenericCustomNamespace/1.0/"
    xmlns:ns_adobe_xpath="http://ns.adobe.com/XPath/1.0/">

    <xsl:template name="iconGeneric">
      <xsl:param name="class" />
      <xsl:param name="baseFilename" />
      
      <div>
        <xsl:attribute name="class" select="string-join(('itemCategoryIcon', $class), ' ')" />
        <xsl:copy-of select="doc(concat($baseFilename, '.svg'))" />
      </div>
    </xsl:template>
  
    <xsl:template name="iconCampaigns">
      <xsl:call-template name="iconGeneric">
        <xsl:with-param name="class" select="'iconCampaigns'" />
        <xsl:with-param name="baseFilename" select="'images/campaign'" />
      </xsl:call-template>
    </xsl:template>
    
    <xsl:template name="iconCOAs">
      <xsl:call-template name="iconGeneric">
        <xsl:with-param name="class" select="'iconCoa'" />
        <xsl:with-param name="baseFilename" select="'images/course_of_action'" />
      </xsl:call-template>
    </xsl:template>

    <xsl:template name="iconDataMarkings">
      <xsl:call-template name="iconGeneric">
        <xsl:with-param name="class" select="'iconDataMarkings'" />
        <xsl:with-param name="baseFilename" select="'images/data_marking'" />
      </xsl:call-template>
    </xsl:template>
  
    <xsl:template name="iconExploitTargets">
      <xsl:call-template name="iconGeneric">
        <xsl:with-param name="class" select="'iconExploitTargets'" />
        <xsl:with-param name="baseFilename" select="'images/explit_target'" />
      </xsl:call-template>
    </xsl:template>
  
    <xsl:template name="iconIncidents">
      <xsl:call-template name="iconGeneric">
        <xsl:with-param name="class" select="'iconIncident'" />
        <xsl:with-param name="baseFilename" select="'images/incident'" />
      </xsl:call-template>
    </xsl:template>
  
    <xsl:template name="iconIndicators">
      <xsl:call-template name="iconGeneric">
        <xsl:with-param name="class" select="'iconIndicators'" />
        <xsl:with-param name="baseFilename" select="'images/indicator'" />
      </xsl:call-template>
    </xsl:template>
  
    <xsl:template name="iconObservables">
      <xsl:call-template name="iconGeneric">
        <xsl:with-param name="class" select="'iconObservables'" />
        <xsl:with-param name="baseFilename" select="'images/observable'" />
      </xsl:call-template>
    </xsl:template>
  
    <xsl:template name="iconThreatActors">
      <xsl:call-template name="iconGeneric">
        <xsl:with-param name="class" select="'iconThreatActors'" />
        <xsl:with-param name="baseFilename" select="'images/threat_actor'" />
      </xsl:call-template>
    </xsl:template>
  
    <xsl:template name="iconTTPs">
      <xsl:call-template name="iconGeneric">
        <xsl:with-param name="class" select="'iconTTPs'" />
        <xsl:with-param name="baseFilename" select="'images/ttp'" />
      </xsl:call-template>
    </xsl:template>
  
</xsl:stylesheet>