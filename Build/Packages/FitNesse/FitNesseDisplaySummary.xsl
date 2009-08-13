<?xml version="1.0"?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "..\..\Entities.xml">

  %entities;
]>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method = "xml" omit-xml-declaration="yes"/>

  <xsl:param name = "applicationPath"/>
  <xsl:param name="CCNetServer"/>
  <xsl:param name="CCNetProject"/>
  <xsl:param name="CCNetBuild"/>

  <xsl:template match = "/">
    <xsl:if test="boolean(//fitnessesummary)">
      <xsl:for-each select="//fitnessesummary">
        <xsl:call-template name="show">
          <xsl:with-param name="stuff" select="."/>
        </xsl:call-template>
      </xsl:for-each>
    </xsl:if>
  </xsl:template>


  <xsl:template name="show">
    <xsl:param name="stuff"/>

    <table
          class = "section-table"
          cellSpacing = "0"
          cellPadding = "2"
          width = "98%"
          border = "0">
      <tr>
        <td height="42" colSpan="2">
          <xsl:attribute name="class">
            <xsl:choose>
              <xsl:when test="$stuff/@tests-with-failures > 0 or $stuff/@tests-with-exceptions > 0">
                <xsl:text>sectionheader-container-error"</xsl:text>
              </xsl:when>
              <xsl:otherwise>
                <xsl:text>sectionheader-container"</xsl:text>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:attribute>
          <a>
            <xsl:attribute name="style">
              <xsl:text>TEXT-DECORATION: NONE;</xsl:text>
              <xsl:choose>
                <xsl:when test="$stuff/@tests-with-failures > 0 or $stuff/@tests-with-exceptions > 0">
                  <xsl:text>color: #D13535;</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:text>color: #403F8D;</xsl:text>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="onmouseover">
              <xsl:text>this.style.color = </xsl:text>
              <xsl:choose>
                <xsl:when test="$stuff/@tests-with-failures > 0 or $stuff/@tests-with-exceptions > 0">
                  <xsl:text>'#403F8D'</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:text>'#7bcf15'</xsl:text>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="onmouseout">
              <xsl:text>this.style.color = </xsl:text>
              <xsl:choose>
                <xsl:when test="$stuff/@tests-with-failures > 0 or $stuff/@tests-with-exceptions > 0">
                  <xsl:text>'#D13535'</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:text>'#403F8D'</xsl:text>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>

            <xsl:attribute name="href">
              http://&HostName;/&ProjectName;-&ProjectCodeLineName;/default.aspx?_action_FitNesse<xsl:value-of select="$stuff/@suite"/>DetailsBuildReport=true&amp;server=<xsl:value-of select="$CCNetServer" />&amp;project=<xsl:value-of select="$CCNetProject" />&amp;build=<xsl:value-of select="$CCNetBuild" />
            </xsl:attribute>
            <img src="http://&HostName;/&ProjectName;-&ProjectCodeLineName;/Packages\FitNesse\logo.gif" class="sectionheader-title-image"/>
            <div class="sectionheader-text">
              FitNesse <xsl:value-of select="$stuff/@suite"/> Tests Run: <xsl:value-of select="$stuff/@testcount"/>, Failed Tests: <xsl:value-of select="$stuff/@tests-with-failures"/>, Tests w/ Exceptions: <xsl:value-of select="$stuff/@tests-with-exceptions"/>, Correct Validations: <xsl:value-of select="$stuff/@correct-validations"/>, Failed Validations: <xsl:value-of select="$stuff/@failed-validations"/>, Exceptions: <xsl:value-of select="$stuff/@exceptions"/>
            </div>
          </a>
        </td>
      </tr>
      <xsl:copy-of select="$stuff"/>
    </table>
  </xsl:template>
  
</xsl:stylesheet>
