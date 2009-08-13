<?xml version="1.0"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="html"/>

  <xsl:param name="URL" select="/cruisecontrol/build/buildresults//target[@name='Publish.SetUp']//target[@name='Publish.EchoWebPath']/task[@name='echo']/message"  />

  <xsl:template match="/">

    <iframe
      frameborder="0"
      width="100%"
      height="1300">
      <xsl:attribute name="src">
        <xsl:value-of select="$URL"/><xsl:text>/CoverageReport/frame-summary.html</xsl:text>
      </xsl:attribute>
      This option will not work correctly.  Unfortunately, your browser does not support Inline Frames
    </iframe>

  </xsl:template>

</xsl:stylesheet>