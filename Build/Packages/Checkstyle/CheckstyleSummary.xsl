<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="html"/>

	<xsl:template match="checkstyle">
    <checkstylesummary>
        <tr>
          <td>Total number of files with errors</td>
          <td>
            <xsl:value-of select="count(file[@name and generate-id(.) = generate-id(key('files', @name))])"/>
          </td>
        </tr>
        <tr>
          <td>Total number of errors</td>
          <td>
            <xsl:value-of select="count(file/error)"/>
          </td>
        </tr>
    </checkstylesummary>
	</xsl:template>
	
</xsl:stylesheet>
