<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xd="http://schemas.microsoft.com/xmltools/2002/xmldiff"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt"
    xmlns:ms="urn:StringScripts"
    exclude-result-prefixes="xsl xd msxsl ms">

  <msxsl:script implements-prefix="ms" language="C#">
    <![CDATA[
    
    public string Replace(string subject, string replace, string with)
    {
      return subject.Replace(replace, with);
    }
    
    ]]>
  </msxsl:script>

  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>

  <xsl:param name="PathPrefix" />
  <xsl:param name="OriginalDocPath" />
  <xsl:variable name="OriginalDoc" select="document($OriginalDocPath)"/>

  <xsl:template match="/">

    <xsl:call-template name="GenerateAddedDisplay" />
    <br/>
    <br/>
    <xsl:call-template name="GenerateRemovedDisplay" />

  </xsl:template>

  <xsl:template name="GenerateRemovedDisplay">
    <xsl:if test="count(/xd:xmldiff//xd:remove) != 0">



      <div style="border-bottom:1px dotted #403F8D; color:#403F8D; font-size:13px; font-weight:bold;">Blocks Removed</div>
      <table rules="groups" cellpadding="2" cellspacing="0" border="0">
        <tbody>
          <tr>
            <th>SourceFile</th>
            <th>Starting Line</th>
            <th>Ending Line</th>
          </tr>

          <xsl:for-each select="/xd:xmldiff/child::*">
            <xsl:call-template name="ProcessRemovedNodes">
              <xsl:with-param name="ReplacedNode" select="($OriginalDoc)/*[position() = 1]"/>
            </xsl:call-template>
          </xsl:for-each>

        </tbody>
      </table>
    </xsl:if>
  </xsl:template>

  <xsl:template name="ProcessRemovedNodes">
    <xsl:param name="ReplacedNode"/>

    <xsl:for-each select="./child::*">
      <xsl:variable name="match" select="@match" />
      <xsl:choose>
        <xsl:when test="local-name() = 'remove'">
          <tr>
            <td>
              <xsl:value-of select="ms:Replace(($ReplacedNode)/*[position() = $match]/@sourceFile, $PathPrefix, '')"/>
            </td>
            <td>
              <xsl:value-of select="($ReplacedNode)/*[position() = $match]/@startLineNumber"/>
            </td>
            <td>
              <xsl:value-of select="($ReplacedNode)/*[position() = $match]/@endLineNumber"/>
            </td>
          </tr>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="ProcessRemovedNodes">
            <xsl:with-param name="ReplacedNode" select="($ReplacedNode)/*[position() = $match]"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="GenerateAddedDisplay">
    <xsl:if test="count(//xd:add//block) != 0">
      <div style="border-bottom:1px dotted #403F8D; color:#403F8D; font-size:13px; font-weight:bold;">Blocks Added</div>
      <table rules="groups" cellpadding="2" cellspacing="0" border="0">
        <tbody>
          <tr>
            <th>SourceFile</th>
            <th>Starting Line</th>
            <th>Ending Line</th>
          </tr>
          <xsl:for-each select="//xd:add//block">
            <tr>
              <td>
                <xsl:value-of select="ms:Replace(@sourceFile, $PathPrefix, '')"/>
              </td>
              <td>
                <xsl:value-of select="@startLineNumber"/>
              </td>
              <td>
                <xsl:value-of select="@endLineNumber"/>
              </td>
            </tr>
          </xsl:for-each>
        </tbody>
      </table>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>