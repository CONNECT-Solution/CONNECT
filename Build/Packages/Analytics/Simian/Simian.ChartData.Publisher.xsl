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
      <xsl:variable name="DuplicateFileCount" select="statistic[@name='duplicateFileCount']/text()" />
      <xsl:variable name="DuplicateLineCount" select="statistic[@name='duplicateLineCount']/text()" />
      <xsl:variable name="DuplicateBlockCount" select="statistic[@name='duplicateBlockCount']/text()" />
      <xsl:variable name="TotalFileCount" select="statistic[@name='totalFileCount']/text()" />
      <xsl:variable name="TotalSignificantLineCount" select="statistic[@name='totalSignificantLineCount']/text()" />
      <number>
        <xsl:value-of select="position()"/>
      </number>
      <exsl:document href="Artifacts\&CCNetProject;\Simian.DuplicateFileCount.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$DuplicateFileCount"/>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\Simian.DuplicateLineCount.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$DuplicateLineCount"/>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\Simian.DuplicateBlockCount.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$DuplicateBlockCount"/>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\Simian.TotalFileCount.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$TotalFileCount"/>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\Simian.TotalSignificantLineCount.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$TotalSignificantLineCount"/>
        </number>
      </exsl:document>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
