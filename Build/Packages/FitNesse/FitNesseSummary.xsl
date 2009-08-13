<?xml version="1.0"?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "..\..\Entities.xml">

  %entities;
]>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="xml" omit-xml-declaration="yes"/>
  
  <xsl:param name="CCNetServer"/>
  <xsl:param name="CCNetProject"/>
  <xsl:param name="CCNetBuild"/>

    <xsl:template match="/">
      <xsl:variable name="SuiteName" select="/testResults/rootPath"/>
      <fitnessesummary>
        <xsl:attribute name="suite">
          <xsl:value-of select="$SuiteName"/>
        </xsl:attribute>
        <xsl:attribute name="testcount">
          <xsl:value-of select="count(//result)"/>
        </xsl:attribute>
        <xsl:attribute name="tests-with-failures">
          <xsl:value-of select="count(//result/counts/wrong[text() &gt; 0])"/>
        </xsl:attribute>
        <xsl:attribute name="tests-with-exceptions">
          <xsl:value-of select="count(//result/counts/exceptions[text() &gt; 0])"/>
        </xsl:attribute>
        <xsl:attribute name="failed-validations">
          <xsl:value-of select="sum(//result/counts/wrong)"/>
        </xsl:attribute>
        <xsl:attribute name="correct-validations">
          <xsl:value-of select="sum(//result/counts/right)"/>
        </xsl:attribute>
        <xsl:attribute name="exceptions">
          <xsl:value-of select="sum(//result/counts/exceptions)"/>
        </xsl:attribute>

        <xsl:for-each select="//result[counts/wrong &gt; 0 or counts/exceptions &gt; 0]">
          <tr>
            <td class="section-error">FitNesseTest:</td>
            <td class="section-error">
              <a>
                <xsl:attribute name="href">
                  <xsl:text>http://&HostName;/&ProjectName;-&ProjectCodeLineName;/default.aspx?_action_FitNesse</xsl:text>
                  <xsl:value-of select="$SuiteName" />
                  <xsl:text>DetailsBuildReport=true&amp;server=</xsl:text>
                  <xsl:value-of select="$CCNetServer" />
                  <xsl:text>&amp;project=</xsl:text>
                  <xsl:value-of select="$CCNetProject" />
                  <xsl:text>&amp;build=</xsl:text>
                  <xsl:value-of select="$CCNetBuild" />
                  <xsl:text>#</xsl:text>
                  <xsl:value-of select="relativePageName"/>
                  <xsl:value-of select="position()"/>
                </xsl:attribute>
                <xsl:value-of select="relativePageName"/>
              </a>
            </td>
          </tr>
        </xsl:for-each>
      </fitnessesummary>
    </xsl:template>
</xsl:stylesheet>
