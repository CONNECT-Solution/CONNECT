<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="html"/>

	<xsl:template match="/">
    <coveragesummary>
      <xsl:attribute name="linecoverage">
        <xsl:value-of select="format-number(/coverage/@line-rate, '#.00%')"/>
      </xsl:attribute>
      <xsl:attribute name="branchcoverage">
        <xsl:value-of select="format-number(/coverage/@branch-rate, '#.00%')"/>
      </xsl:attribute>
    </coveragesummary>
	</xsl:template>
	
</xsl:stylesheet>
