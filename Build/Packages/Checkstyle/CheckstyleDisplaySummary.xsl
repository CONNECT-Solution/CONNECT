<?xml version="1.0"?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "..\..\Entities.xml">

  %entities;
]>
<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="xml"/>

  <xsl:param name="applicationPath"/>
  <xsl:param name="CCNetServer"/>
  <xsl:param name="CCNetProject"/>
  <xsl:param name="CCNetBuild"/>

  <xsl:template match="/">
    <xsl:variable name="stuff" select="//checkstyle" />
    <xsl:if test="$stuff/node()">
      <table class="section-table" cellSpacing="0" cellPadding="2" width="98%" border="0">
        <tr>
          <td height="42" class="sectionheader-container" colSpan="2">
            <a style="TEXT-DECORATION:NONE; color:403F8D;" onmouseover="this.style.color='#7bcf15'" onmouseout="this.style.color='#403F8D'">
              <xsl:attribute name="href">
                http://&HostName;/&ProjectName;-&ProjectCodeLineName;/default.aspx?_action_CheckStyleReport=true&amp;server=<xsl:value-of select="$CCNetServer" />&amp;project=<xsl:value-of select="$CCNetProject" />&amp;build=<xsl:value-of select="$CCNetBuild" />
              </xsl:attribute>
              <img src="http://&HostName;/&ProjectName;-&ProjectCodeLineName;/Packages/Checkstyle/logo.gif" class="sectionheader-title-image"/>
              <div class="sectionheader-text">
                CheckStyle Summary (<xsl:value-of select="count(file/error)" /> Errors)
              </div>
            </a>
          </td>
        </tr>
        <xsl:copy-of select="//checkstyle"/>
      </table>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
