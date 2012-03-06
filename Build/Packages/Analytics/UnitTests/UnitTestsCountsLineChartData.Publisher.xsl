<?xml version="1.0"?>
<!DOCTYPE chart [
  <!ENTITY % entities SYSTEM "entities.dtd">

  %entities;
]>
<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:exsl="http://exslt.org/common"
  exclude-result-prefixes="exsl">


  <xsl:param name="CCNetLabel" />
  <xsl:param name="CCNetLogFilePath" />
  <xsl:param name="CCNetProject" />

  <xsl:output method="html"/>

  <xsl:template match="/">
    <xsl:for-each select="/Builds/integration[position() > last()-200]">
      <xsl:variable name="Total" select="statistic[@name='Total Test Count']/text()" />
      <xsl:variable name="Run" select="statistic[@name='Test Run Count']/text()" />
      <xsl:variable name="Failed" select="statistic[@name='Test Failure Count']/text()" />
      <xsl:variable name="Ignored" select="$Total - $Run" />
      <xsl:variable name="Passed" select="$Run - $Failed" />
      <number>
        <xsl:value-of select="position()"/>
      </number>
      <exsl:document href="Artifacts\&CCNetProject;\UnitTestsCountsHistoryTotal.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$Total"/>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\UnitTestsCountsHistoryPassed.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$Passed"/>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\UnitTestsCountsHistoryIgnored.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$Ignored"/>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\UnitTestsCountsHistoryFailed.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$Failed"/>
        </number>
      </exsl:document>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
