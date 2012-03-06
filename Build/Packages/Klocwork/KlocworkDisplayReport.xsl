<?xml version="1.0"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="html"/>

  <xsl:param name="URL" select="/cruisecontrol/build/buildresults//target[@name='Publish.SetUp']//target[@name='Publish.EchoWebPath']/task[@name='echo']/message"  />

  <xsl:template match="/">

    <script language="javascript" type="text/javascript">
      function inject()
      {
      var doc;
      if (typeof document.getElementById('buffer').document != 'undefined')
      {
      doc = window.frames['buffer'].document;
      }
      else if (typeof document.getElementById('buffer').contentDocument != 'undefined')
      {
      doc = document.getElementById('buffer').contentDocument;
      }

      if (typeof doc.body.innerHTML != 'undefined')
      {
      document.getElementById('injectionpoint').innerHTML = doc.body.innerHTML;
      }
      else
      {
      setTimeout(function(){inject()}, 1000);
      }
      }
    </script>

    <div id="injectionpoint">
      <p>Loading...</p>
    </div>


    <iframe
      onload="inject();"
      id="buffer"
      style="display: none;"
      frameborder="0">
      <xsl:attribute name="src">
        <xsl:value-of select="$URL"/>http://localhost:8074/klocwork/insight-review.html#permalink-PLIQ8EW39VT2AAPVUJ62
      </xsl:attribute>
      This option will not work correctly.  Unfortunately, your browser does not support Inline Frames
    </iframe>

  </xsl:template>

</xsl:stylesheet>