<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="html"/>

	<xsl:variable name="simian.root" select="//simian"/>
	<xsl:variable name="simian.version" select="$simian.root/@version" />

	<xsl:template match="/">
    <simiansummary>
      <xsl:if test="$simian.version!=''">
        <xsl:attribute name="percentduplication">
          <xsl:value-of select="format-number((sum(//check/summary/@duplicateLineCount) - sum(//set/@lineCount)) div sum(//check/summary/@totalSignificantLineCount), '#.00%')"/>
        </xsl:attribute>
        <tr>
          <td>Total number of duplicate lines</td>
          <td>
            <xsl:value-of select="sum(//check/summary/@duplicateLineCount)"/>
          </td>
        </tr>
        <tr>
          <td>Total number of duplicate blocks</td>
          <td>
            <xsl:value-of select="sum(//check/summary/@duplicateBlockCount)"/>
          </td>
        </tr>
        <tr>
          <td>Total number of files with duplicates</td>
          <td>
            <xsl:value-of select="sum(//check/summary/@duplicateFileCount)"/>
          </td>
        </tr>
        <tr>
          <td>Total number of files</td>
          <td>
            <xsl:value-of select="sum(//check/summary/@totalFileCount)"/>
          </td>
        </tr>
        <tr>
          <td>Total number of significant lines</td>
          <td>
            <xsl:value-of select="sum(//check/summary/@totalSignificantLineCount)"/>
          </td>
        </tr>
        <tr>
          <td>% Duplication</td>
          <td>
            <xsl:value-of select="format-number((sum(//check/summary/@duplicateLineCount) - sum(//set/@lineCount)) div sum(//check/summary/@totalSignificantLineCount), '#.00%')"/>
          </td>
        </tr>
		  </xsl:if>
    </simiansummary>
	</xsl:template>
	
</xsl:stylesheet>
