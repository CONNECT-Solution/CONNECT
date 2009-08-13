<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "..\..\Entities.xml">

  %entities;
]>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="xml" omit-xml-declaration="yes"/>
  <xsl:param name = "applicationPath"/>
  <xsl:param name="CCNetServer"/>
  <xsl:param name="CCNetProject"/>
  <xsl:param name="CCNetBuild"/>

  <xsl:template match="/">

    <xsl:variable name="anterror.messages" select="//antbuildresults//message[(parent::task/@name='javac' and boolean(following-sibling::message[contains(text(), 'error ') or contains(text(), ' error')])) or (contains(text(), 'error '))  or (contains(text(), ' error')) or @level='Error'] | //antbuildresults//builderror/message"/>
    <xsl:variable name="anterror.count" select="count($anterror.messages)"/>

    <xsl:if test="$anterror.count > 0">
      <table class="section-table" cellpadding="2" cellspacing="0" border="0" width="98%">
        <tbody>
          <tr>
            <td height="42" class="sectionheader-container-error" colSpan="2">
              <a STYLE="TEXT-DECORATION: NONE; color: D13535;" onmouseover="this.style.color = '#403F8D'" onmouseout="this.style.color = '#D13535'">
                <xsl:attribute name="href">
                  http://&HostName;/&ProjectName;-&ProjectCodeLineName;/default.aspx?_action_AntReport=true&amp;server=<xsl:value-of select="$CCNetServer" />&amp;project=<xsl:value-of select="$CCNetProject" />&amp;build=<xsl:value-of select="$CCNetBuild" />
                </xsl:attribute>
                <img src="http://&HostName;/&ProjectName;-&ProjectCodeLineName;/Packages/Ant/logo.gif" class="sectionheader-title-image"/>
                <div class="sectionheader-text">
                  Ant Errors
                </div>
              </a>
            </td>
          </tr>
            <tr>
              <td>
                <xsl:for-each select="$anterror.messages" >
                  <pre class="section-error">
                    <xsl:value-of select="text()"/>
                  </pre>
                </xsl:for-each>
              </td>
            </tr>
        </tbody>
      </table>
    </xsl:if>


  </xsl:template>


</xsl:stylesheet>
