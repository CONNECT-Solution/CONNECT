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
      <xsl:variable name="linecoverage" select="statistic[@name='linecoverage']/text()" />
      <xsl:variable name="branchcoverage" select="statistic[@name='branchcoverage']/text()" />
      <number>
        <xsl:value-of select="position()"/>
      </number>
      <exsl:document href="Artifacts\&CCNetProject;\CoveragePercentHistoryLine.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$linecoverage"/>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\CoveragePercentHistoryBranch.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$branchcoverage"/>
        </number>
      </exsl:document>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
