<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "..\..\..\Entities.xml">

  %entities;
]>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt"
    xmlns:ms="urn:Scripts"
    exclude-result-prefixes="ms msxsl"
>
  <xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>

  <msxsl:script implements-prefix="ms" language="C#">
    <![CDATA[
    
    public bool FileExists(string filePath)
    {
      System.IO.FileInfo file = new System.IO.FileInfo(filePath);
      return file.Exists && file.Length > 0;
    }
    
    ]]>
  </msxsl:script>
  <xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>

  <xsl:param name="ArtifactDirectoryPath" select="/cruisecontrol/build/buildresults//target[@name='Publish.SetUp']//target[@name='Publish.EchoArtifactPath']/task[@name='echo']/message"  />
  <!--<xsl:param name="ArtifactDirectoryPath" select="'C:\Temp'"  />-->
  <xsl:variable name="analyticsFile" select="concat($ArtifactDirectoryPath, '\MainAnalyticsReport.xml')" />
  
  <xsl:template match="/">
    <xsl:if test="ms:FileExists($analyticsFile)">
      <xsl:variable name="analyticsdoc" select="document($analyticsFile)"/>
      <xsl:if test="boolean(($analyticsdoc)/statistics/child::*)">
        <div id="Analitics" style="float: right; margin-left: 30px;">
        <div>
          <div style="margin: 4px;">
            <xsl:variable name="MostRecentIntegration" select="($analyticsdoc)/statistics/integration[position() = last()]" />
            <xsl:variable name="day" select="$MostRecentIntegration/@day"/>
            <xsl:variable name="month" select="$MostRecentIntegration/@month"/>
            <xsl:variable name="year" select="$MostRecentIntegration/@year"/>
            <xsl:variable name="week" select="$MostRecentIntegration/@week"/>
            <xsl:variable name="dayofyear" select="$MostRecentIntegration/@dayofyear"/>
            <xsl:variable name="iteration" select="$MostRecentIntegration/statistic[@name='IterationName']"/>
            
            <xsl:variable name="totalCount" select="count(($analyticsdoc)/statistics/integration)"/>
            <xsl:variable name="successCount" select="count(($analyticsdoc)/statistics/integration[@status='Success'])"/>
            <xsl:variable name="exceptionCount" select="count(($analyticsdoc)/statistics/integration[@status='Exception'])"/>
            <xsl:variable name="failureCount" select="$totalCount - ($successCount + $exceptionCount)"/>

            <xsl:variable name="totalIterationCount" select="count(($analyticsdoc)/statistics/integration[statistic[@name='IterationName' and text() = $iteration]])"/>
            <xsl:variable name="successIterationCount" select="count(($analyticsdoc)/statistics/integration[@status='Success' and statistic[@name='IterationName' and text() = $iteration]])"/>
            <xsl:variable name="exceptionIterationCount" select="count(($analyticsdoc)/statistics/integration[@status='Exception' and statistic[@name='IterationName' and text() = $iteration]])"/>
            <xsl:variable name="failureIterationCount" select="$totalIterationCount - ($successIterationCount + $exceptionIterationCount)"/>

            <xsl:variable name="totalCountForTheLast7Day" select="count(($analyticsdoc)/statistics/integration[@dayofyear > $dayofyear - 7])"/>
            <xsl:variable name="successCountForTheLast7Day" select="count(($analyticsdoc)/statistics/integration[@status='Success' and @dayofyear > $dayofyear - 7 and @year = $year])"/>
            <xsl:variable name="exceptionCountForTheLast7Day" select="count(($analyticsdoc)/statistics/integration[@status='Exception' and @dayofyear > $dayofyear - 7 and @year = $year])"/>
            <xsl:variable name="failureCountForTheLast7Day" select="$totalCountForTheLast7Day - ($successCountForTheLast7Day + $exceptionCountForTheLast7Day)"/>


            <xsl:variable name="totalCountForTheDay" select="count(($analyticsdoc)/statistics/integration[@day=$day and @month=$month and @year=$year])"/>
            <xsl:variable name="successCountForTheDay" select="count(($analyticsdoc)/statistics/integration[@status='Success' and @day=$day and @month=$month and @year=$year])"/>
            <xsl:variable name="exceptionCountForTheDay" select="count(($analyticsdoc)/statistics/integration[@status='Exception' and @day=$day and @month=$month and @year=$year])"/>
            <xsl:variable name="failureCountForTheDay" select="$totalCountForTheDay - ($successCountForTheDay + $exceptionCountForTheDay)"/>

            <table border="1" cellpadding="0" cellspacing="0" style="border-style: solid; border-collapse: collapse; border-color: #403F8D; border-width: thin; color: #403F8D;">
              <tbody style="border: inherit;">
                <tr style="border: inherit;">
                  <th style="border: inherit;">Integration Summary</th>
                  <th style="border: inherit;">For Today</th>
                  <th style="border: inherit;">For Last 7 Days</th>
                  <th style="border: inherit;">
                    <xsl:value-of select="$iteration"/>
                  </th>
                  <th style="border: inherit;">Overall</th>
                </tr>
                <tr style="border: inherit;">
                  <th style="border: inherit;" align="left">Total Builds</th>
                  <td style="border: inherit;">
                    <xsl:value-of select="$totalCountForTheDay"/>
                  </td>
                  <td style="border: inherit;">
                    <xsl:value-of select="$totalCountForTheLast7Day"/>
                  </td>
                  <td style="border: inherit;">
                    <xsl:value-of select="$totalIterationCount"/>
                  </td>
                  <td style="border: inherit;">
                    <xsl:value-of select="$totalCount"/>
                  </td>
                </tr>
                <tr style="border: inherit;">
                  <th style="border: inherit;" align="left">Number of Successful</th>
                  <td style="border: inherit;">
                    <xsl:value-of select="$successCountForTheDay"/>
                  </td>
                  <td style="border: inherit;">
                    <xsl:value-of select="$successCountForTheLast7Day"/>
                  </td>
                  <td style="border: inherit;">
                    <xsl:value-of select="$successIterationCount"/>
                  </td>
                  <td style="border: inherit;">
                    <xsl:value-of select="$successCount"/>
                  </td>
                </tr>
                <tr style="border: inherit;">
                  <th style="border: inherit;" align="left">Number of Failed</th>
                  <td style="border: inherit;">
                    <xsl:value-of select="$failureCountForTheDay"/>
                  </td>
                  <td style="border: inherit;">
                    <xsl:value-of select="$failureCountForTheLast7Day"/>
                  </td>
                  <td style="border: inherit;">
                    <xsl:value-of select="$failureIterationCount"/>
                  </td>
                  <td style="border: inherit;">
                    <xsl:value-of select="$failureCount"/>
                  </td>
                </tr>
                <tr style="border: inherit;">
                  <th style="border: inherit;" align="left">Number of Exceptions</th>
                  <td style="border: inherit;">
                    <xsl:value-of select="$exceptionCountForTheDay"/>
                  </td>
                  <td style="border: inherit;">
                    <xsl:value-of select="$exceptionCountForTheLast7Day"/>
                  </td>
                  <td style="border: inherit;">
                    <xsl:value-of select="$exceptionIterationCount"/>
                  </td>
                  <td style="border: inherit;">
                    <xsl:value-of select="$exceptionCount"/>
                  </td>
                </tr>
              </tbody>
            </table>

          </div>
        </div>
      </div>
      </xsl:if>
    </xsl:if>
  </xsl:template>
  
</xsl:stylesheet>
