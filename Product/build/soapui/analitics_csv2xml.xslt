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

  <xsl:function name="fn:getLines" as="xs:string+">
    <xsl:param name="str" as="xs:string"/>
    <xsl:choose>
    <xsl:when test="contains($str, '&#xD;')">
      <xsl:sequence select="tokenize($str, '&#xD;&#xa;')"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:sequence select="tokenize($str, '&#xa;')"/>
    </xsl:otherwise>
    </xsl:choose>
  </xsl:function>

  <xsl:template match="/">
    <testsuites>
    <xsl:apply-templates/>
    </testsuites>
  </xsl:template>

  <xsl:template match="file">
    <xsl:variable name="filePath" select="concat('file:///', replace(text(),'\\','/'))"/>
    <xsl:variable name="packages" select="tokenize(replace(substring-before(substring-after($filePath,'/soapui-test-reports/'),'-statistics.txt'),'/','.'), '\.')"/>
    <xsl:choose>
      <xsl:when test="unparsed-text-available($filePath)">
        <xsl:variable name="csv" select="unparsed-text($filePath)"/>
        <xsl:variable name="lines" select="fn:getLines($csv)" as="xs:string+"/>
        <xsl:variable name="elemNames" select="fn:getTokens($lines[1])" as="xs:string+"/>
        <xsl:for-each select="$lines[position() > 1]">
          <xsl:variable name="elValue" select="."/>
          <xsl:if test="starts-with($elValue,'TestCase:,')">
            <testsuite>
            <xsl:attribute name="name"><xsl:value-of select="$packages[2]"/></xsl:attribute>
            <xsl:attribute name="package"><xsl:value-of select="$packages[1]"/>.<xsl:value-of select="$packages[3]"/></xsl:attribute>
            <xsl:variable name="lineItems" select="fn:getTokens(.)" as="xs:string+"/>
            <xsl:for-each select="$elemNames">
              <xsl:variable name="elName" select="."/>
              <xsl:variable name="pos" select="position()"/>
              <xsl:if test="not(matches($elName,'Test Step'))">
                <xsl:attribute name="{$elName}"><xsl:value-of select="$lineItems[$pos]"/></xsl:attribute>
              </xsl:if>
            </xsl:for-each>
            </testsuite>
          </xsl:if>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>Cannot locate : </xsl:text><xsl:value-of select="$filePath"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>
