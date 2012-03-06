<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl"
>
  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>

  <xsl:template match="/">
    <xsl:for-each select="/alerts/alert">
      <xsl:if test="boolean(./@name = 'Coverage')">
        <xsl:variable name="message" select="./messages/message/text()"/>
        <xsl:variable name="spin" select="./messages/message/@spin"/>
        <xsl:variable name="details" select="./details/child::*"/>

        <div>
          <script type="text/javascript">
            <xsl:text>
        $(function() {
            $("#CoverageAlert").cluetip({
            width: 600,
            local: true,
            sticky: true
          });
        });
        </xsl:text>
          </script>
          <div style="display: none;">
            <div id="CoverageDetails">
              <xsl:choose>
                <xsl:when test="boolean($details)">
                  <xsl:copy-of select="$details"/>
                </xsl:when>
                <xsl:otherwise>
                  <p>There was an error locating the details!</p>
                </xsl:otherwise>
              </xsl:choose>
            </div>
          </div>
          <p>
            <span
              id="CoverageAlert"
              title="Coverage Alert"
              rel="#CoverageDetails"
        >
              <xsl:attribute name="class">
                <xsl:choose>
                  <xsl:when test="$spin = 'positive'">
                    <xsl:text>Alert-Positive</xsl:text>
                  </xsl:when>
                  <xsl:when test="$spin = 'negative'">
                    <xsl:text>Alert-Negative</xsl:text>
                  </xsl:when>
                </xsl:choose>
              </xsl:attribute>

              <xsl:value-of select="$message"/>
            </span>
          </p>
        </div>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
