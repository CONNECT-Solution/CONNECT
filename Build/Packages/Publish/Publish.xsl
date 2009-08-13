<?xml version="1.0"?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "..\..\Entities.xml">

  %entities;
]>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:lxslt="http://xml.apache.org/xslt">

  <xsl:output method="html"/>
  
  <xsl:template match="/">
    <xsl:if test="count(/cruisecontrol/build/buildresults//target[@name='Publish.Link']) &gt; 0">
      <table class="section-table" cellpadding="2" cellspacing="0" border="0" width="98%">
        <tr>
          <td height="42" class="sectionheader-container">
            <img src="http://&HostName;/&ProjectName;-&ProjectCodeLineName;/Packages/Publish/images/Publish.gif" class="sectionheader-title-image" />
            <div class="sectionheader">Deployment File(s)</div>
          </td>
        </tr>
        <xsl:for-each select="/cruisecontrol/build/buildresults//target[@name='Publish.Link']">
          <xsl:call-template name="DeploymentFile">
            <xsl:with-param name="DeploymentWebPath" select=".//target[@name='Publish.EchoWebFilePath']/task[@name='echo']/message"/>
            <xsl:with-param name="DeploymentWebName" select=".//target[@name='Private.Publish.EchoWebFileName']/task[@name='echo']/message"/>
          </xsl:call-template>
        </xsl:for-each>
        
      </table>
    </xsl:if>
  </xsl:template>
  
  <xsl:template name="DeploymentFile">
    <xsl:param name="DeploymentWebPath"/>
    <xsl:param name="DeploymentWebName"/>
    <tr>
      <td class="section-data">
        <a style="color: 403F8D; font-size: 11px; font-weight: bold;" onmouseover="this.style.color = '#7bcf15'" onmouseout="this.style.color = '#403F8D'">
          <xsl:attribute name="href">
            <xsl:value-of select="$DeploymentWebPath"/>
          </xsl:attribute>
          <img style="float: left; border-style: none" src="http://&HostName;/&ProjectName;-&ProjectCodeLineName;/Packages/Publish/images/file.gif" />
          <div style="position:relative; top: 2px;">
            <xsl:value-of select="$DeploymentWebName"/>
          </div>
        </a>
      </td>
    </tr>
  </xsl:template>

</xsl:stylesheet>
