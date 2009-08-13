<?xml version="1.0"?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "entities.dtd">
  <!ENTITY % FitNesse SYSTEM "FitNesse.dtd">

  %entities;
  %FitNesse;
]>
<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:exsl="http://exslt.org/common"
  exclude-result-prefixes="exsl">


  <xsl:output method="html"/>

  <xsl:template match="/">
    <xsl:variable name="CurrentIterationName" select="/Builds/integration[position()=last()]/statistic[@name='IterationName']/text()"/>
    <exsl:document href="Artifacts\&CCNetProject;\FitNesseCountsHistoryTitle.xml" fragment="yes" append="yes" >
      <text color='000000' size='35' x='250' y='1' width='200' height='80' h_align='center' v_align='bottom'>
        <xsl:value-of select="$CurrentIterationName"/>
      </text>
    </exsl:document>
    <xsl:for-each select="/Builds/integration[statistic[@name='IterationName']/text()=$CurrentIterationName]">
      <xsl:variable name="Total" select="statistic[@name='Test Failure Count']/text()" />
      <xsl:variable name="Failed" select="statistic[@name='Test Failure Count']/text()" />
      <xsl:variable name="Exception" select="statistic[@name='Test Exception Count']/text()" />
      <xsl:variable name="Passed" select="statistic[@name='Test Correct Count']/text()" />
      <number>
        <xsl:value-of select="position()"/>
      </number>
      <exsl:document href="Artifacts\&CCNetProject;\FitNesseCountsHistoryTotal.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$Total"/>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\FitNesseCountsHistoryPassed.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$Passed"/>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\FitNesseCountsHistoryException.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$Exception"/>
        </number>
      </exsl:document>
      <exsl:document href="Artifacts\&CCNetProject;\FitNesseCountsHistoryFailed.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="$Failed"/>
        </number>
      </exsl:document>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
