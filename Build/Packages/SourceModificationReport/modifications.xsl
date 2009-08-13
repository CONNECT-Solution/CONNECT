<?xml version="1.0"?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "..\..\Entities.xml">

  %entities;
]>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:msxsl="urn:schemas-microsoft-com:xslt"
  xmlns:ms="urn:Scripts"
  exclude-result-prefixes="ms msxsl"
>
  <xsl:output method="html"/>

  <msxsl:script implements-prefix="ms" language="C#">
    <![CDATA[
    
    public bool FileExists(string filePath)
    {
      System.IO.FileInfo file = new System.IO.FileInfo(filePath);
      return file.Exists && file.Length > 0;
    }
    ]]>
  </msxsl:script>
  <xsl:param name="applicationPath"/>

  <xsl:param name="URL" select="/cruisecontrol/build/buildresults//target[@name='Publish.SetUp']//target[@name='Publish.EchoArtifactPath']/task[@name='echo']/message"  />
  <xsl:key name="changeset" match="//Modification" use="ChangeNumber/text()"/>

  <xsl:template match="/">

    <xsl:if test="$URL != ''">
      <xsl:variable name="ChangesDocPath" select="concat($URL, '\SourceModificationReport.xml')"/>

      <xsl:if test="ms:FileExists($ChangesDocPath)">
        <xsl:variable name="ChangesDoc" select="document($ChangesDocPath)"/>
        <xsl:variable name="modification.list" select="($ChangesDoc)/Modification"/>

        <xsl:if test="/cruisecontrol/build/@lastintegrationstatus != 'Success' and /cruisecontrol/build/@lastintegrationstatus != 'Unknown'">
          <xsl:if test="count(/cruisecontrol/modifications/modification) &lt; count(($ChangesDoc)//Modification)">
            <table class="section-table" cellpadding="2" cellspacing="0" border="0" width="98%">
              <tr>
                <td height="42" class="sectionheader-container">
                  <img src="http://&HostName;/&ProjectName;-&ProjectCodeLineName;/images/SourceControl.gif" class="sectionheader-title-image" />
                  <div class="sectionheader"  >
                    Source Control Revision History Since Last Good Build
                  </div>
                </td>
              </tr>
              <xsl:for-each select="($ChangesDoc)//Modification[generate-id(.)=generate-id(key('changeset', ChangeNumber/text())[1])]">
                <xsl:sort select="ChangeNumber" order="descending" data-type="number"/>
                <xsl:call-template name="changeset" />
              </xsl:for-each>
            </table>
          </xsl:if>
        </xsl:if>
      </xsl:if>
    </xsl:if>
  </xsl:template>

  <!-- Changeset template -->
  <xsl:template name="changeset">
    <tr>
      <td class="section-data">
        <xsl:if test="position() mod 2=0">
          <xsl:attribute name="style">border-top: #808286 1px dotted;</xsl:attribute>
        </xsl:if>
        <span >
          Changeset # <xsl:value-of select="ChangeNumber" />
        </span>


        <table rules="groups" cellpadding="2" cellspacing="0" border="0">
          <tbody>
            <tr >
              <th>
                Author: <xsl:value-of select="UserName"/>
              </th>
              <th>
                Date: <xsl:value-of select="ModifiedTime"/>
              </th>
            </tr>
            <tr>
              <td colspan="2">
                <em>
                  <xsl:value-of select="Comment"/>
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
              <xsl:for-each select="key('changeset', ChangeNumber/text())">
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
            <xsl:value-of select="Type"/>
          </xsl:attribute>
          <xsl:attribute name="src">
            <xsl:choose>
              <xsl:when test="Type = 'Added'">
                <xsl:value-of select="'http://&HostName;/&ProjectName;-&ProjectCodeLineName;/images/add.png'"/>
              </xsl:when>
              <xsl:when test="Type = 'Modified'">
                <xsl:value-of select="'http://&HostName;/&ProjectName;-&ProjectCodeLineName;/images/edit.png'"/>
              </xsl:when>
              <xsl:when test="Type = 'Deleted'">http://&HostName;/&ProjectName;-&ProjectCodeLineName;/images/delete.png</xsl:when>
              <xsl:otherwise>http://&HostName;/&ProjectName;-&ProjectCodeLineName;/images/document_text.png</xsl:otherwise>
            </xsl:choose>
          </xsl:attribute>
        </img>&#160;<xsl:value-of select="Type"/>
      </td>
      <td>
        <xsl:choose>
          <xsl:when test="count(Url) = 1 ">
            <a>
              <xsl:attribute name="href">
                <xsl:value-of select="Url" />
              </xsl:attribute>
              <xsl:if test="FolderName != ''">
                <xsl:value-of select="FolderName"/><xsl:value-of select="'/'"/>
              </xsl:if>
              <xsl:value-of select="FileName"/>
            </a>
          </xsl:when>
          <xsl:otherwise>
              <xsl:if test="FolderName != ''">
                <xsl:value-of select="FolderName"/>
                <xsl:value-of select="'/'"/>
              </xsl:if>
              <xsl:value-of select="FileName"/>
          </xsl:otherwise>
        </xsl:choose>
      </td>
    </tr>
  </xsl:template>
</xsl:stylesheet>
