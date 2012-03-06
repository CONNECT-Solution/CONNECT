<?xml version="1.0"?>
<!DOCTYPE chart [
  <!ENTITY % entities SYSTEM "entities.dtd">

  %entities;
]>
<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:exsl="http://exslt.org/common"
  xmlns:msxsl="urn:schemas-microsoft-com:xslt"
  xmlns:ms="urn:DateScripts"
  exclude-result-prefixes="ms msxsl exsl">


  <xsl:param name="CCNetLabel" />
  <xsl:param name="CCNetLogFilePath" />
  <xsl:param name="CCNetProject" />

  <msxsl:script implements-prefix="ms" language="C#">
    <![CDATA[
    public string FormatTime(string time)
    {
        return System.TimeSpan.Parse(time).TotalSeconds.ToString();
    }
    ]]>
  </msxsl:script>

  <xsl:output method="html"/>

  <xsl:template match="/">
    <xsl:for-each select="/statistics/integration[position() > last()-200]">
      <number>
        <xsl:value-of select="position()"/>
      </number>
      <exsl:document href="Artifacts\&CCNetProject;\BuildHistoryTotal.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="ms:FormatTime(statistic[@name='Duration']/text())"/>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\BuildHistoryCompile.xml" fragment="yes" append="yes" >
        <number>
          <xsl:choose>
            <xsl:when test="statistic[@name='CompileTime']/text() = 'NaN'">
              <xsl:value-of select="'0'"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="statistic[@name='CompileTime']/text()"/>
            </xsl:otherwise>
          </xsl:choose>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\BuildHistoryProduceSourceUpdate.xml" fragment="yes" append="yes" >
        <number>
          <xsl:choose>
            <xsl:when test="statistic[@name='ProduceSourceUpdate']/text() = 'NaN'">
              <xsl:value-of select="'0'"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="statistic[@name='ProduceSourceUpdate']/text()"/>
            </xsl:otherwise>
          </xsl:choose>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\BuildHistoryThirdPartySourceUpdate.xml" fragment="yes" append="yes" >
        <number>
          <xsl:choose>
            <xsl:when test="statistic[@name='ThirdPartySourceUpdate']/text() = 'NaN'">
              <xsl:value-of select="'0'"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="statistic[@name='ThirdPartySourceUpdate']/text()"/>
            </xsl:otherwise>
          </xsl:choose>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\BuildHistoryUnitTests.xml" fragment="yes" append="yes" >
        <number>
          <xsl:choose>
            <xsl:when test="statistic[@name='UnitTests']/text() = 'NaN'">
              <xsl:value-of select="'0'"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="statistic[@name='UnitTests']/text()"/>
            </xsl:otherwise>
          </xsl:choose>
        </number>
      </exsl:document>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
