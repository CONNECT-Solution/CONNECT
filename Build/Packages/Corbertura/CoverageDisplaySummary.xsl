<?xml version="1.0"?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "..\..\Entities.xml">

  %entities;
]>
<xsl:stylesheet
    version = "1.0"
    xmlns:xsl = "http://www.w3.org/1999/XSL/Transform">

  <xsl:output method = "xml" omit-xml-declaration="yes"/>

  <xsl:param name = "applicationPath"/>
  <xsl:param name="CCNetServer"/>
  <xsl:param name="CCNetProject"/>
  <xsl:param name="CCNetBuild"/>

  <xsl:template match = "/">
    <xsl:variable name="stuff" select="//coveragesummary" />
    <xsl:choose>
      <xsl:when test="boolean($stuff)">
        <xsl:call-template name="show">
          <xsl:with-param name="stuff.existed" select="boolean($stuff)"/>
          <xsl:with-param name="linecoverage" select="$stuff/@linecoverage" />
          <xsl:with-param name="branchcoverage" select="$stuff/@branchcoverage" />
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="show">
          <xsl:with-param name="stuff.existed" select="boolean($stuff)"/>
          <xsl:with-param name="linecoverage" select="NaN" />
          <xsl:with-param name="branchcoverage" select="NaN" />
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template name="show">
    <xsl:param name="stuff.existed"/>
    <xsl:param name="linecoverage"/>
    <xsl:param name="branchcoverage"/>
    
    <xsl:if test="$stuff.existed or boolean(//antbuildresults//target[@name='-coverage.check.project']//message[@level='Error'])">
      <table
                    class = "section-table"
                    cellSpacing = "0"
                    cellPadding = "2"
                    width = "98%"
                    border = "0">
        <tr>
          <td height="42" class="sectionheader-container" colSpan="2">
            <a STYLE="TEXT-DECORATION: NONE; color: 403F8D;" onmouseover="this.style.color = '#7bcf15'" onmouseout="this.style.color = '#403F8D'">
              <xsl:attribute name="href">
                http://&HostName;/&ProjectName;-&ProjectCodeLineName;/default.aspx?_action_CoverageReport=true&amp;server=<xsl:value-of select="$CCNetServer" />&amp;project=<xsl:value-of select="$CCNetProject" />&amp;build=<xsl:value-of select="$CCNetBuild" />
              </xsl:attribute>
              <img src="http://&HostName;/&ProjectName;-&ProjectCodeLineName;/Packages/Corbertura/logo.gif" class="sectionheader-title-image"/>
              <div class="sectionheader-text">
                Coverage Summary line coverage <xsl:value-of select="$linecoverage" />, branch coverage  <xsl:value-of select="$branchcoverage" />
              </div>
            </a>
          </td>
        </tr>
        <xsl:copy-of select="//coveragesummary"/>
        <tr>
          <td>
              <xsl:for-each select="//antbuildresults//target[@name='-coverage.check.project']//message[@level='Error']" >
                <pre class="section-error-bold">
                  <xsl:value-of select="text()"/>
                </pre>
              </xsl:for-each>
          </td>
        </tr>
      </table>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
