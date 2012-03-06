<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl"
>
  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>

  <xsl:template match="/">
    <xsl:if test="boolean(/alerts/alert[@name = 'UnitTests'])">
      <xsl:variable name="message" select="/alerts/alert[@name = 'UnitTests']/messages/message/text()"/>
      <xsl:variable name="spin" select="/alerts/alert[@name = 'UnitTests']/messages/message/@spin"/>
      <xsl:variable name="count" select="/alerts/alert[@name = 'UnitTests']/statistics/statistic[@name = 'count']/@value"/>
      <xsl:variable name="previouscount" select="/alerts/alert[@name = 'UnitTests']/statistics/statistic[@name = 'previouscount']/@value"/>
      <xsl:variable name="details" select="/alerts/alert[@name = 'UnitTests']/details/child::*"/>
      
      <div>
        <script type="text/javascript">
          <xsl:text>
        $(function() {
            $("#UnitTestAlert").cluetip({
            width: 600,
            local: true,
            sticky: true
          });
        });
        </xsl:text>
        </script>
        <div style="display: none;">
          <div id="UnitTestDetails">
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
            id="UnitTestAlert"
            title="Unit Test Alert"
            rel="#UnitTestDetails"
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
  </xsl:template>

</xsl:stylesheet>
