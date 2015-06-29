<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:stix="http://stix.mitre.org/stix-1"
    exclude-result-prefixes="xs fn"
    version="2.0">
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="*:Raw_Artifact/text()">
        <xsl:value-of select="fn:substring(fn:string(), 1, 50)" />
    </xsl:template>
    
    
    <xsl:function name="stix:following-siblings-with-same-name">
        <xsl:param name="currentName" as="xs:QName" />
        <xsl:param name="followingSiblingList" as="node()*" />
        
        
    </xsl:function>
    
    <xsl:template match="*:Description|*:Short_Description" />

    <xsl:template match="*:Description[(following-sibling::*[1])[self::*:Description]][count(((preceding-sibling::*)[last()])/self::*:Description) = 0]">
        <xsl:variable name="n" select="name()" />
        <xsl:variable name="siblingsUnsorted" select=".|following-sibling::*[name() = $n]" />
        <xsl:variable name="siblingsSorted">
            <xsl:perform-sort select="$siblingsUnsorted">
                <xsl:sort select="xs:integer(./@ordinality)" />
            </xsl:perform-sort>
        </xsl:variable>
        <xsl:element name="{concat(prefix-from-QName(node-name()), ':', local-name(), '-list')}" namespace="{namespace-uri()}">
            <xsl:for-each select="$siblingsSorted/*" >
                <xsl:copy-of select="."/>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="*:Short_Description[(following-sibling::*[1])[self::*:Short_Description]][count(((preceding-sibling::*)[last()])/self::*:Short_Description) = 0]">
        <xsl:variable name="n" select="name()" />
        <xsl:variable name="siblingsUnsorted" select=".|following-sibling::*[name() = $n]" />
        <xsl:variable name="siblingsSorted">
            <xsl:perform-sort select="$siblingsUnsorted">
                <xsl:sort select="xs:integer(./@ordinality)" />
            </xsl:perform-sort>
        </xsl:variable>
        <xsl:element name="{concat(prefix-from-QName(node-name()), ':', local-name(), '-list')}" namespace="{namespace-uri()}">
            <xsl:for-each select="$siblingsSorted/*" >
                <xsl:copy-of select="."/>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>
    
</xsl:stylesheet>