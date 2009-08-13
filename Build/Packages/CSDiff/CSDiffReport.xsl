<?xml version="1.0"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:output method="html"/>

  <xsl:param name="URL" select="//buildresults//target[@name='CSDiff.GenerateHtmlReports']//target[@name='Publish.EchoWebFilePath']/task[@name='echo']/message"  />

    <xsl:template match="/">

      <script language="javascript" type="text/javascript">
        function iFrameHeight() {
        var h = 0;
        if ( !document.all ) {
        h = document.getElementById('blockrandom').contentDocument.height;
        document.getElementById('blockrandom').style.height = h + 60 + 'px';
        } else if( document.all ) {
        h = document.frames('blockrandom').document.body.scrollHeight;
        document.all.blockrandom.style.height = h + 20 + 'px';
        }
        }
      </script>

      
        <iframe
          onload="iFrameHeight()"		id="blockrandom"
          name="iframe"
          width="100%"
          height="10000"
          scrolling="auto"
          align="top"
          frameborder="0"
          class="wrapper">
          <xsl:attribute name="src"><xsl:value-of select="$URL"/>/DiffReport.htm</xsl:attribute>
          This option will not work correctly.  Unfortunately, your browser does not support Inline Frames
        </iframe>

    </xsl:template>
    
</xsl:stylesheet>