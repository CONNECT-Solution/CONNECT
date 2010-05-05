<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="fn"
	exclude-result-prefixes="xs fn">

  <xsl:output indent="yes" encoding="UTF-8"/>

  <xsl:function name="fn:getTokens" as="xs:string+">
    <xsl:param name="str" as="xs:string"/>
    <xsl:analyze-string select="concat($str, ',')" regex='(("[^"]*")+|[^,]*),'>
      <xsl:matching-substring>
        <xsl:sequence select='replace(regex-group(1), "^""|""$|("")""", "$1")'/>
      </xsl:matching-substring>
    </xsl:analyze-string>
  </xsl:function>

  <xsl:template match="/">
    <tests>
    <xsl:apply-templates/>
    </tests>
  </xsl:template>
  
  <xsl:template match="file">
    <xsl:variable name="filePath" select="concat('file:///', replace(text(),'\\','/'))"/>
    <xsl:choose>
      <xsl:when test="unparsed-text-available($filePath)">
        <xsl:variable name="csv" select="unparsed-text($filePath)"/>
        <xsl:variable name="lines" select="tokenize($csv, '&#xD;&#xa;')" as="xs:string+"/>
        <xsl:variable name="elemNames" select="fn:getTokens($lines[1])" as="xs:string+"/>
        <xsl:for-each select="$lines[position() > 1]">
          <xsl:variable name="elValue" select="."/>
          <xsl:if test="starts-with($elValue,'TestCase:,')">
            <test file="{$filePath}">
            <element name="name"><xsl:value-of select="$filePath"/></element>
            <xsl:variable name="lineItems" select="fn:getTokens(.)" as="xs:string+"/>
            <xsl:for-each select="$elemNames">
              <xsl:variable name="elName" select="."/>
              <xsl:variable name="pos" select="position()"/>
              <element name="{$elName}"><xsl:value-of select="$lineItems[$pos]"/></element>
            </xsl:for-each>
            </test>
          </xsl:if>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>Cannot locate : </xsl:text><xsl:value-of select="$filePath"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>