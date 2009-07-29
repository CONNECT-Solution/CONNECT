<?xml version="1.0"?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "..\..\Entities.xml">

  %entities;
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html"/>
  <xsl:param name="applicationPath"/>
  <xsl:variable name="modification.list" select="/cruisecontrol/modifications/modification"/>
  <xsl:key name="changeset" match="/cruisecontrol/modifications/modification" use="changeNumber/text()"/>

  <xsl:template match="/">
    <table class="section-table" cellpadding="2" cellspacing="0" border="0" width="98%">
      <tr>
        <td height="42" class="sectionheader-container">
          <img src="http://&HostName;/&ProjectName;-&ProjectCodeLineName;/images/SourceControl.gif" class="sectionheader-title-image" />
          <div class="sectionheader"  >
            Source Control Revision History For This Build
          </div>
          </td>
      </tr>
        <xsl:for-each select="/cruisecontrol/modifications/modification[generate-id(.)=generate-id(key('changeset', changeNumber/text())[1])]">
          <xsl:sort select="changeNumber" order="descending" data-type="number"/>
          <xsl:call-template name="changeset" />
        </xsl:for-each>
      <xsl:if test="count($modification.list) = 0">
        <tr>
          <td>There were no changes made since the last build.</td>
        </tr>
      </xsl:if>
    </table>
  </xsl:template>

  <!-- Changeset template -->
  <xsl:template name="changeset">
    <tr>
      <td class="section-data">
        <xsl:if test="position() mod 2=0">
          <xsl:attribute name="style">border-top: #808286 1px dotted;</xsl:attribute>
        </xsl:if>
        <span >
          Changeset # <xsl:value-of select="changeNumber" />
        </span>
        
        
        <table rules="groups" cellpadding="2" cellspacing="0" border="0">
          <tbody>
            <tr >
              <th>
                Author: <xsl:value-of select="user"/>
              </th>
              <th>
                Date: <xsl:value-of select="date"/>
              </th>
            </tr>
            <tr>
              <td colspan="2">
                <em>
                  <xsl:value-of select="comment"/>
                </em>
              </td>
            </tr>
          </tbody>
        </table>

        <div>
					<a style="color: 7bcf15; TEXT-DECORATION: NONE;" href="javascript:void(0)" class="dsphead" onclick="dsp(this, '+ Click to Show Changes', '+ Click to Hide Changes')">
            <span class="dspchar">+ Click to Show Changes</span>
          </a>
        </div>
        <div class="dspcont">
          <table rules="groups" cellpadding="2" cellspacing="0" border="0">
            <tbody>
              <xsl:for-each select="key('changeset', changeNumber/text())">
                <xsl:call-template name="modification"/>
              </xsl:for-each>
            </tbody>
          </table>
        </div>
      </td>
    </tr>
  </xsl:template>

  <!-- Modifications template -->
  <xsl:template name="modification">
    <tr>
      <xsl:if test="position() mod 2=0">
        <xsl:attribute name="class">shaded</xsl:attribute>
      </xsl:if>
      <td>
        <img class="statusimage">
          <xsl:attribute name="title">
            <xsl:value-of select="@type"/>
          </xsl:attribute>
          <xsl:attribute name="src">
            <xsl:choose>
              <xsl:when test="@type = 'Added'">
                <xsl:value-of select="'http://&HostName;/&ProjectName;-&ProjectCodeLineName;/images/add.png'"/>
              </xsl:when>
              <xsl:when test="@type = 'Modified'">
                <xsl:value-of select="'http://&HostName;/&ProjectName;-&ProjectCodeLineName;/images/edit.png'"/>
              </xsl:when>
              <xsl:when test="@type = 'Deleted'">http://&HostName;/&ProjectName;-&ProjectCodeLineName;/images/delete.png</xsl:when>
              <xsl:otherwise>http://&HostName;/&ProjectName;-&ProjectCodeLineName;/images/document_text.png</xsl:otherwise>
            </xsl:choose>
          </xsl:attribute>
        </img>&#160;<xsl:value-of select="@type"/>
      </td>
      <td>
        <xsl:choose>
          <xsl:when test="count(url) = 1 ">
            <a>
              <xsl:attribute name="href">
                <xsl:value-of select="url" />
              </xsl:attribute>
              <xsl:if test="project != ''">
                <xsl:value-of select="project"/><xsl:value-of select="'/'"/>
              </xsl:if>
              <xsl:value-of select="filename"/>
            </a>
          </xsl:when>
          <xsl:otherwise>
              <xsl:if test="project != ''">
                <xsl:value-of select="project"/>
                <xsl:value-of select="'/'"/>
              </xsl:if>
              <xsl:value-of select="filename"/>
          </xsl:otherwise>
        </xsl:choose>
      </td>
    </tr>
  </xsl:template>
</xsl:stylesheet>
