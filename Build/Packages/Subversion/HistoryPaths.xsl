<?xml version="1.0"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:lxslt="http://xml.apache.org/xslt">

  <xsl:output method="text"/>

  <xsl:variable name="Count" select="count(/log/logentry)"/>

  <xsl:template match="/">
    <xsl:if test="$Count > 0">
      true
    </xsl:if>
    <xsl:if test="$Count = 0">
      false
    </xsl:if>
  </xsl:template>
  
</xsl:stylesheet>