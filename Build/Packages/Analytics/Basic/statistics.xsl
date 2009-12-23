<?xml version="1.0"?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "..\..\..\Entities.xml">

  %entities;
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:msxsl="urn:schemas-microsoft-com:xslt"
  xmlns:ms="urn:DateScripts"
  xmlns:NumVar="urn:NumVarScripts"
  xmlns:StringVar="urn:StringVarScripts"
  exclude-result-prefixes="StringVar NumVar ms msxsl">


  <msxsl:script implements-prefix="StringVar" language="C#">
    <![CDATA[
     private static System.Collections.Hashtable NumberVariables = new System.Collections.Hashtable();

        public string Set(string name, string value)
        {
            if (!NumberVariables.Contains(name))
                NumberVariables.Add(name, value);
            NumberVariables[name] = value;
            return NumberVariables[name].ToString();
        }
        
        public string Value(string name)
        {
            if (!NumberVariables.Contains(name))
                NumberVariables.Add(name, "");
            return NumberVariables[name].ToString();
        }
    ]]>
  </msxsl:script>

  <msxsl:script implements-prefix="NumVar" language="C#">
    <![CDATA[
     private static System.Collections.Hashtable NumberVariables = new System.Collections.Hashtable();

        public string Add(string name, double value)
        {
            if (!NumberVariables.Contains(name))
                NumberVariables.Add(name, (double)0);
            NumberVariables[name] = (double)NumberVariables[name] + value;
            return NumberVariables[name].ToString();
        }

        public string Set(string name, double value)
        {
            if (!NumberVariables.Contains(name))
                NumberVariables.Add(name, (double)0);
            NumberVariables[name] = value;
            return NumberVariables[name].ToString();
        }
        
        public string Value(string name)
        {
            if (!NumberVariables.Contains(name))
                NumberVariables.Add(name, (double)0);
            return NumberVariables[name].ToString();
        }
    ]]>
  </msxsl:script>

  <msxsl:script implements-prefix="ms" language="C#">
    <![CDATA[
    
    public bool FileExists(string filePath)
    {
      System.IO.FileInfo file = new System.IO.FileInfo(filePath);
      return file.Exists && file.Length > 0;
    }
    
    public string FormatDate(string dateTime, string format)
    {
      return System.DateTime.Parse(dateTime).ToString(format);
    }
    
    public string SumTimes(System.Xml.XPath.XPathNodeIterator nodes)
    {
        System.TimeSpan Total = new System.TimeSpan();
        while (nodes.MoveNext())
        {
            string DurationString = nodes.Current.Value;
            System.TimeSpan Duration = System.TimeSpan.Parse(DurationString);
            Total = Total.Add(Duration);
        }
        return Total.TotalMinutes.ToString();
    }
    
    ]]>
  </msxsl:script>

  <xsl:variable name="true" select="boolean(/)"/>
  <xsl:variable name="false" select="not($true)"/>
  
  <xsl:param name="CCNetServer"/>
  
  <xsl:output method="html"/>

  <xsl:template match="/statistics">
    <xsl:variable name="MostRecentIntegration" select="/statistics/integration[position() = last()]" />
    <xsl:variable name="ArtifactFolderName" select="ms:FormatDate($MostRecentIntegration/statistic[@name='StartTime']/text(), 'yyyyMMddHHmmss')" />
    <xsl:variable name="ArtifactDirectoryPath" select="concat('&Common.Directory.ArtifactRoot.Path;\', $ArtifactFolderName)"  />

    <xsl:variable name="simianfile" select="concat($ArtifactDirectoryPath, '\Simian.Statistics.xml')"/>
    <xsl:variable name="simianfileExists" select="ms:FileExists($simianfile)"/>
    <xsl:variable name="simiandoc" select="document($simianfile)"/>

    <xsl:variable name="unittestfile" select="concat($ArtifactDirectoryPath, '\UnitTests.Statistics.xml')"/>
    <xsl:variable name="unittestfileExists" select="ms:FileExists($unittestfile)"/>
    <xsl:variable name="unittestdoc" select="document($unittestfile)"/>

    <xsl:variable name="coveragefile" select="concat($ArtifactDirectoryPath, '\Coverage.Statistics.xml')"/>
    <xsl:variable name="coveragefileExists" select="ms:FileExists($coveragefile)"/>
    <xsl:variable name="coveragedoc" select="document($coveragefile)"/>

    <xsl:variable name="DevelopmentIterationSuiteFitNesseFile" select="concat($ArtifactDirectoryPath, '\DevelopmentIterationSuiteFitNesse.Statistics.xml')"/>
    <xsl:variable name="DevelopmentIterationSuiteFitNesseFileExists" select="ms:FileExists($DevelopmentIterationSuiteFitNesseFile)"/>
    <xsl:variable name="DevelopmentIterationSuiteFitNesseDoc" select="document($DevelopmentIterationSuiteFitNesseFile)"/>

    <xsl:variable name="RegressionSuiteFitNesseFile" select="concat($ArtifactDirectoryPath, '\RegressionSuiteFitNesse.Statistics.xml')"/>
    <xsl:variable name="RegressionSuiteFitNesseFileExists" select="ms:FileExists($RegressionSuiteFitNesseFile)"/>
    <xsl:variable name="RegressionSuiteFitNesseDoc" select="document($RegressionSuiteFitNesseFile)"/>

    <style>
      *.pass{
      background-color: #41B32D;
      }
      *.fail{
      background-color: #D13535;
      }
      *.unknown{
      background-color: #FF7700;
      }
      *.exception{
      background-color: #000000;
      color: #ffffff;
      }
      a.dsphead{
      text-decoration:none;
      margin-left:1.5em;
      }
      a.dsphead:hover{
      text-decoration:underline;
      }
      a.dsphead span.dspchar{
      font-family:monospace;
      font-weight:normal;
      }
      .dspcont{
      display:none;
      margin-left:1.5em;
      }
    </style>


    <script type="text/javascript">
      <![CDATA[
function dsp(loc){
   if(document.getElementById){
      var foc = loc.firstChild;
      foc = loc.firstChild.innerHTML ? loc.firstChild : loc.firstChild.nextSibling;
      foc.innerHTML = foc.innerHTML == '+ Show Chart' ? '- Hide Chart' : '+ Show Chart';
      foc = loc.parentNode.nextSibling.style ? loc.parentNode.nextSibling : loc.parentNode.nextSibling.nextSibling;
      foc.style.display = foc.style.display == 'block' ? 'none' : 'block';
    }
}  

if(!document.getElementById)
   document.write('<style type="text/css"><!--\n.dspcont{display:block;}\n//--></style>');

      ]]>
    </script>

    <noscript>
      <style type="text/css">
        .dspcont{display:block;}
      </style>
    </noscript>

    <xsl:variable name="day" select="$MostRecentIntegration/@day"/>
    <xsl:variable name="month" select="$MostRecentIntegration/@month"/>
    <xsl:variable name="year" select="$MostRecentIntegration/@year"/>
    <xsl:variable name="week" select="$MostRecentIntegration/@week"/>
    <xsl:variable name="dayofyear" select="$MostRecentIntegration/@dayofyear"/>
    <xsl:variable name="iteration" select="$MostRecentIntegration/statistic[@name='IterationName']"/>

    <xsl:variable name="totalCount" select="count(integration)"/>
    <xsl:variable name="successCount" select="count(integration[@status='Success'])"/>
    <xsl:variable name="exceptionCount" select="count(integration[@status='Exception'])"/>
    <xsl:variable name="failureCount" select="$totalCount - ($successCount + $exceptionCount)"/>

    <xsl:variable name="totalIterationCount" select="count(integration[statistic[@name='IterationName' and text() = $iteration]])"/>
    <xsl:variable name="successIterationCount" select="count(integration[@status='Success' and statistic[@name='IterationName' and text() = $iteration]])"/>
    <xsl:variable name="exceptionIterationCount" select="count(integration[@status='Exception' and statistic[@name='IterationName' and text() = $iteration]])"/>
    <xsl:variable name="failureIterationCount" select="$totalIterationCount - ($successIterationCount + $exceptionIterationCount)"/>

    <xsl:variable name="totalCountForTheLast7Day" select="count(integration[@dayofyear > $dayofyear - 7])"/>
    <xsl:variable name="successCountForTheLast7Day" select="count(integration[@status='Success' and @dayofyear > $dayofyear - 7 and @year = $year])"/>
    <xsl:variable name="exceptionCountForTheLast7Day" select="count(integration[@status='Exception' and @dayofyear > $dayofyear - 7 and @year = $year])"/>
    <xsl:variable name="failureCountForTheLast7Day" select="$totalCountForTheLast7Day - ($successCountForTheLast7Day + $exceptionCountForTheLast7Day)"/>

    <xsl:variable name="totalCountForTheDay"      select="count(integration[@day=$day and @month=$month and @year=$year])"/>
    <xsl:variable name="successCountForTheDay"    select="count(integration[@status='Success' and @day=$day and @month=$month and @year=$year])"/>
    <xsl:variable name="exceptionCountForTheDay"  select="count(integration[@status='Exception' and @day=$day and @month=$month and @year=$year])"/>
    <xsl:variable name="failureCountForTheDay"    select="$totalCountForTheDay - ($successCountForTheDay + $exceptionCountForTheDay)"/>

    <p>
      Today is

      <xsl:value-of select="$month"/>/<xsl:value-of select="$day"/>/<xsl:value-of select="$year"/> <br />
      <table class="section-table" cellpadding="2" cellspacing="0" border="1">
        <tr class="sectionheader">
          <th>Integration Summary</th>
          <th>For Today</th>
          <th>For Last 7 Days</th>
          <th>
            <xsl:value-of select="$iteration"/>
          </th>
          <th>Overall</th>
        </tr>
        <tr>
          <th align="left">Total Builds</th>
          <td>
            <xsl:value-of select="$totalCountForTheDay"/>
          </td>
          <td>
            <xsl:value-of select="$totalCountForTheLast7Day"/>
          </td>
          <td>
            <xsl:value-of select="$totalIterationCount"/>
          </td>
          <td>
            <xsl:value-of select="$totalCount"/>
          </td>
        </tr>
        <tr>
          <th align="left">Number of Successful</th>
          <td>
            <xsl:value-of select="$successCountForTheDay"/>
          </td>
          <td>
            <xsl:value-of select="$successCountForTheLast7Day"/>
          </td>
          <td>
            <xsl:value-of select="$successIterationCount"/>
          </td>
          <td>
            <xsl:value-of select="$successCount"/>
          </td>
        </tr>
        <tr>
          <th align="left">Number of Failed</th>
          <td>
            <xsl:value-of select="$failureCountForTheDay"/>
          </td>
          <td>
            <xsl:value-of select="$failureCountForTheLast7Day"/>
          </td>
          <td>
            <xsl:value-of select="$failureIterationCount"/>
          </td>
          <td>
            <xsl:value-of select="$failureCount"/>
          </td>
        </tr>
        <tr>
          <th align="left">Number of Exceptions</th>
          <td>
            <xsl:value-of select="$exceptionCountForTheDay"/>
          </td>
          <td>
            <xsl:value-of select="$exceptionCountForTheLast7Day"/>
          </td>
          <td>
            <xsl:value-of select="$exceptionIterationCount"/>
          </td>
          <td>
            <xsl:value-of select="$exceptionCount"/>
          </td>
        </tr>
      </table>
    </p>

    <hr/>

    
    <p>
      <xsl:variable name="Submitters" select="/statistics/integration/statistic[@name='mainsubmitter' and boolean(text()) and not(text()=preceding::statistic[@name='mainsubmitter']/text())]"/>
      
      <xsl:variable name="trashmembercolspan" select="NumVar:Add('membercolspan', 1)"/>
      
      <xsl:if test="$DevelopmentIterationSuiteFitNesseFileExists">
        <xsl:variable name="trash" select="NumVar:Add('membercolspan', 2)"/>

        <xsl:call-template name="GetStatCounts">
          <xsl:with-param name="doc" select="$DevelopmentIterationSuiteFitNesseDoc"/>
          <xsl:with-param name="statName" select="'Correct Validation Count'"/>
          <xsl:with-param name="countName" select="'fitcorrectValidation'"/>
          <xsl:with-param name="ignore-initial-value" select="$false"/>
          <xsl:with-param name="debug" select="$false"/>
          <xsl:with-param name="day" select="$day"/>
          <xsl:with-param name="month" select="$month"/>
          <xsl:with-param name="year" select="$year"/>
          <xsl:with-param name="dayofyear" select="$dayofyear"/>
          <xsl:with-param name="iteration" select="$iteration"/>
        </xsl:call-template>
        
      <xsl:call-template name="GetStatCounts">
          <xsl:with-param name="doc" select="$DevelopmentIterationSuiteFitNesseDoc"/>
          <xsl:with-param name="statName" select="'Validation Count'"/>
          <xsl:with-param name="countName" select="'fitValidation'"/>
          <xsl:with-param name="ignore-initial-value" select="$false"/>
          <xsl:with-param name="debug" select="$false"/>
          <xsl:with-param name="day" select="$day"/>
          <xsl:with-param name="month" select="$month"/>
          <xsl:with-param name="year" select="$year"/>
          <xsl:with-param name="dayofyear" select="$dayofyear"/>
          <xsl:with-param name="iteration" select="$iteration"/>
        </xsl:call-template>

        <xsl:call-template name="GetStatCounts">
          <xsl:with-param name="doc" select="$RegressionSuiteFitNesseDoc"/>
          <xsl:with-param name="statName" select="'Correct Validation Count'"/>
          <xsl:with-param name="countName" select="'fitcorrectValidation'"/>
          <xsl:with-param name="ignore-initial-value" select="$false"/>
          <xsl:with-param name="debug" select="$false"/>
          <xsl:with-param name="day" select="$day"/>
          <xsl:with-param name="month" select="$month"/>
          <xsl:with-param name="year" select="$year"/>
          <xsl:with-param name="dayofyear" select="$dayofyear"/>
          <xsl:with-param name="iteration" select="$iteration"/>
        </xsl:call-template>

        <xsl:call-template name="GetStatCounts">
          <xsl:with-param name="doc" select="$RegressionSuiteFitNesseDoc"/>
          <xsl:with-param name="statName" select="'Validation Count'"/>
          <xsl:with-param name="countName" select="'fitValidation'"/>
          <xsl:with-param name="ignore-initial-value" select="$false"/>
          <xsl:with-param name="debug" select="$false"/>
          <xsl:with-param name="day" select="$day"/>
          <xsl:with-param name="month" select="$month"/>
          <xsl:with-param name="year" select="$year"/>
          <xsl:with-param name="dayofyear" select="$dayofyear"/>
          <xsl:with-param name="iteration" select="$iteration"/>
        </xsl:call-template>
      </xsl:if>

      <xsl:if test="$unittestfileExists">
        <xsl:variable name="trash" select="NumVar:Add('membercolspan', 1)"/>
        
        <xsl:call-template name="GetSuccessStatCounts">
        <xsl:with-param name="doc" select="$unittestdoc"/>
        <xsl:with-param name="statName" select="'Total Test Count'"/>
        <xsl:with-param name="countName" select="'unitest'"/>
        <xsl:with-param name="ignore-initial-value" select="$false"/>
        <xsl:with-param name="debug" select="$true"/>
        <xsl:with-param name="day" select="$day"/>
        <xsl:with-param name="month" select="$month"/>
        <xsl:with-param name="year" select="$year"/>
        <xsl:with-param name="dayofyear" select="$dayofyear"/>
        <xsl:with-param name="iteration" select="$iteration"/>
      </xsl:call-template>
      </xsl:if>
      
      <xsl:if test="$simianfileExists">
        <xsl:variable name="trash" select="NumVar:Add('membercolspan', 1)"/>

        <xsl:call-template name="GetSuccessStatCounts">
        <xsl:with-param name="doc" select="$simiandoc"/>
        <xsl:with-param name="statName" select="'totalSignificantLineCount'"/>
        <xsl:with-param name="countName" select="'line'"/>
        <xsl:with-param name="ignore-initial-value" select="$false"/>
        <xsl:with-param name="debug" select="$false"/>
        <xsl:with-param name="day" select="$day"/>
        <xsl:with-param name="month" select="$month"/>
        <xsl:with-param name="year" select="$year"/>
        <xsl:with-param name="dayofyear" select="$dayofyear"/>
        <xsl:with-param name="iteration" select="$iteration"/>
      </xsl:call-template>
      </xsl:if>

      <xsl:if test="$coveragefileExists">
        <xsl:variable name="trash" select="NumVar:Add('membercolspan', 1)"/>

        <xsl:call-template name="GetSuccessStatCounts">
        <xsl:with-param name="doc" select="$coveragedoc"/>
        <xsl:with-param name="statName" select="'linecoverage'"/>
        <xsl:with-param name="countName" select="'linecoverage'"/>
        <xsl:with-param name="ignore-initial-value" select="$false"/>
        <xsl:with-param name="debug" select="$false"/>
        <xsl:with-param name="day" select="$day"/>
        <xsl:with-param name="month" select="$month"/>
        <xsl:with-param name="year" select="$year"/>
        <xsl:with-param name="dayofyear" select="$dayofyear"/>
        <xsl:with-param name="iteration" select="$iteration"/>
      </xsl:call-template>
      </xsl:if>

      <p>
        This is only a best guess.  When multiple team members commit to a build, a single build or committing to a failing build, 
        this can cause some issue with determining who gets credit for a statistic.
      </p>
      <br/>
      <p>The Build column is read: 
        <strong>
          Total
        </strong>
        <xsl:text>/</xsl:text>
        <span style="color:#41B32D;">
          Success
        </span>
        <xsl:text>/</xsl:text>
        <span style="color:#D13535;">
          Failure
        </span>
        <xsl:text>/</xsl:text>
        Exception
      </p>
      <p>
        Other Stats are read: Added Count/Deleted Count
      </p>
      <table class="section-table" cellpadding="2" cellspacing="0" border="1">
        <tr class="sectionheader">
          <th></th>
          <th>
            <xsl:attribute name="colspan">
              <xsl:value-of select="NumVar:Value('membercolspan')"/>
            </xsl:attribute>
            For Today
          </th>
          <th>
            <xsl:attribute name="colspan">
              <xsl:value-of select="NumVar:Value('membercolspan')"/>
            </xsl:attribute>
            For Last 7 Days
          </th>
          <th>
            <xsl:attribute name="colspan">
              <xsl:value-of select="NumVar:Value('membercolspan')"/>
            </xsl:attribute>
            <xsl:value-of select="$iteration"/>
          </th>
          <th>
            <xsl:attribute name="colspan">
              <xsl:value-of select="NumVar:Value('membercolspan')"/>
            </xsl:attribute>
            Overall
          </th>
        </tr>
        <tr>
          <th>Team Member</th>
          <th>Builds</th>
          <xsl:if test="$unittestfileExists">
          <th>Unit Test Count</th>
          </xsl:if>
          <xsl:if test="$coveragefileExists">
          <th>Line Coverage Percentage</th>
          </xsl:if>
          <xsl:if test="$simianfileExists">
          <th>Line Count</th>
          </xsl:if>
          <xsl:if test="$DevelopmentIterationSuiteFitNesseFileExists">
            <th>Fit Validation Count</th>
            <th>Fit Correct Validation Count</th>
          </xsl:if>
          <th>Builds</th>
          <xsl:if test="$unittestfileExists">
          <th>Unit Test Count</th>
          </xsl:if>
          <xsl:if test="$coveragefileExists">
          <th>Line Coverage Percentage</th>
          </xsl:if>
          <xsl:if test="$simianfileExists">
          <th>Line Count</th>
          </xsl:if>
          <xsl:if test="$DevelopmentIterationSuiteFitNesseFileExists">
            <th>Fit Validation Count</th>
            <th>Fit Correct Validation Count</th>
          </xsl:if>
          <th>Builds</th>
          <xsl:if test="$unittestfileExists">
          <th>Unit Test Count</th>
          </xsl:if>
          <xsl:if test="$coveragefileExists">
          <th>Line Coverage Percentage</th>
          </xsl:if>
          <xsl:if test="$simianfileExists">
          <th>Line Count</th>
          </xsl:if>
          <xsl:if test="$DevelopmentIterationSuiteFitNesseFileExists">
            <th>Fit Validation Count</th>
            <th>Fit Correct Validation Count</th>
          </xsl:if>
          <th>Builds</th>
          <xsl:if test="$unittestfileExists">
          <th>Unit Test Count</th>
          </xsl:if>
          <xsl:if test="$coveragefileExists">
          <th>Line Coverage Percentage</th>
          </xsl:if>
          <xsl:if test="$simianfileExists">
          <th>Line Count</th>
          </xsl:if>
          <xsl:if test="$DevelopmentIterationSuiteFitNesseFileExists">
            <th>Fit Validation Count</th>
            <th>Fit Correct Validation Count</th>
          </xsl:if>
        </tr>
          <xsl:for-each select="$Submitters">
            <xsl:variable name="current.submitter" select="text()"/>
            <xsl:choose>
              <xsl:when test="text()='build'"></xsl:when>
              <xsl:when test="text()='flowersj'"></xsl:when>
              <xsl:otherwise>
                <tr>
                  <td>
                    <xsl:value-of select="text()"/>
                  </td>
                  <td>
                    <xsl:attribute name="title">
                      <xsl:value-of select="$current.submitter"/>
                    </xsl:attribute>
                    <xsl:variable name="submitterTotalCountForTheDay"      select="count(/statistics/integration[@day=$day and @month=$month and @year=$year and statistic[@name='mainsubmitter' and text()=$current.submitter]])"/>
                    <xsl:variable name="submitterSuccessCountForTheDay"    select="count(/statistics/integration[@status='Success' and @day=$day and @month=$month and @year=$year and statistic[@name='mainsubmitter' and text()=$current.submitter]])"/>
                    <xsl:variable name="submitterExceptionCountForTheDay"  select="count(/statistics/integration[@status='Exception' and @day=$day and @month=$month and @year=$year and statistic[@name='mainsubmitter' and text()=$current.submitter]])"/>
                    <xsl:variable name="submitterFailureCountForTheDay"    select="$submitterTotalCountForTheDay - ($submitterSuccessCountForTheDay + $submitterExceptionCountForTheDay)"/>

                    <strong>
                    <xsl:value-of select="$submitterTotalCountForTheDay"/>
                    </strong>
                    <xsl:text>/</xsl:text>
                    <span style="color:#41B32D;">
                    <xsl:value-of select="$submitterSuccessCountForTheDay"/>
                    </span>
                    <xsl:text>/</xsl:text>
                    <span style="color:#D13535;">
                    <xsl:value-of select="$submitterFailureCountForTheDay"/>
                    </span>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="$submitterExceptionCountForTheDay"/>
                  </td>
                  <xsl:if test="$unittestfileExists">
                  <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.today.unitest.count'))"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.today.unitest.negative.count'))"/>
                  </td>
                  </xsl:if>
                  <xsl:if test="$coveragefileExists">
                  <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                    <xsl:value-of select="format-number(NumVar:Value(concat(text(), '.today.linecoverage.count')), '#.00')"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="format-number(NumVar:Value(concat(text(), '.today.linecoverage.negative.count')), '#.00')"/>
                  </td>
                  </xsl:if>
                  <xsl:if test="$simianfileExists">
                  <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.today.line.count'))"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.today.line.negative.count'))"/>
                  </td>
                  </xsl:if>
                  <xsl:if test="$DevelopmentIterationSuiteFitNesseFileExists">
                    <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.today.fitValidation.count'))"/>
                      <xsl:text>/</xsl:text>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.today.fitValidation.negative.count'))"/>
                    </td>
                    <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.today.fitcorrectValidation.count'))"/>
                      <xsl:text>/</xsl:text>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.today.fitcorrectValidation.negative.count'))"/>
                    </td>
                  </xsl:if>
                  <td>
                    <xsl:attribute name="title">
                      <xsl:value-of select="$current.submitter"/>
                    </xsl:attribute>
                    <xsl:variable name="submitterTotalCountForTheLast7Day"      select="count(/statistics/integration[@dayofyear > $dayofyear - 7 and @year = $year and statistic[@name='mainsubmitter' and text()=$current.submitter]])"/>
                    <xsl:variable name="submitterSuccessCountForTheLast7Day"    select="count(/statistics/integration[@status='Success' and @dayofyear > $dayofyear - 7 and @year = $year and statistic[@name='mainsubmitter' and text()=$current.submitter]])"/>
                    <xsl:variable name="submitterExceptionCountForTheLast7Day"  select="count(/statistics/integration[@status='Exception' and @dayofyear > $dayofyear - 7 and @year = $year and statistic[@name='mainsubmitter' and text()=$current.submitter]])"/>
                    <xsl:variable name="submitterFailureCountForTheLast7Day"    select="$submitterTotalCountForTheLast7Day - ($submitterSuccessCountForTheLast7Day + $submitterExceptionCountForTheLast7Day)"/>

                    <strong>
                      <xsl:value-of select="$submitterTotalCountForTheLast7Day"/>
                    </strong>
                    <xsl:text>/</xsl:text>
                    <span style="color:#41B32D;">
                      <xsl:value-of select="$submitterSuccessCountForTheLast7Day"/>
                    </span>
                    <xsl:text>/</xsl:text>
                    <span style="color:#D13535;">
                      <xsl:value-of select="$submitterFailureCountForTheLast7Day"/>
                    </span>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="$submitterExceptionCountForTheLast7Day"/>
                  </td>
                  <xsl:if test="$unittestfileExists">
                  <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.sevendays.unitest.count'))"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.sevendays.unitest.negative.count'))"/>
                  </td>
                  </xsl:if>
                  <xsl:if test="$coveragefileExists">
                  <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                    <xsl:value-of select="format-number(NumVar:Value(concat(text(), '.sevendays.linecoverage.count')), '#.00')"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="format-number(NumVar:Value(concat(text(), '.sevendays.linecoverage.negative.count')), '#.00')"/>
                  </td>
                  </xsl:if>
                  <xsl:if test="$simianfileExists">
                  <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.sevendays.line.count'))"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.sevendays.line.negative.count'))"/>
                  </td>
                  </xsl:if>
                  <xsl:if test="$DevelopmentIterationSuiteFitNesseFileExists">
                    <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.sevendays.fitValidation.count'))"/>
                      <xsl:text>/</xsl:text>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.sevendays.fitValidation.negative.count'))"/>
                    </td>
                    <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.sevendays.fitcorrectValidation.count'))"/>
                      <xsl:text>/</xsl:text>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.sevendays.fitcorrectValidation.negative.count'))"/>
                    </td>
                  </xsl:if>
                  <td>
                    <xsl:attribute name="title">
                      <xsl:value-of select="$current.submitter"/>
                    </xsl:attribute>
                    <xsl:variable name="submitterTotalCountForTheIteration"      select="count(/statistics/integration[statistic[@name='IterationName' and text() = $iteration] and statistic[@name='mainsubmitter' and text()=$current.submitter]])"/>
                    <xsl:variable name="submitterSuccessCountForTheIteration"    select="count(/statistics/integration[@status='Success' and statistic[@name='IterationName' and text() = $iteration] and statistic[@name='mainsubmitter' and text()=$current.submitter]])"/>
                    <xsl:variable name="submitterExceptionCountForTheIteration"  select="count(/statistics/integration[@status='Exception' and statistic[@name='IterationName' and text() = $iteration] and statistic[@name='mainsubmitter' and text()=$current.submitter]])"/>
                    <xsl:variable name="submitterFailureCountForTheIteration"    select="$submitterTotalCountForTheIteration - ($submitterSuccessCountForTheIteration + $submitterExceptionCountForTheIteration)"/>

                    <strong>
                      <xsl:value-of select="$submitterTotalCountForTheIteration"/>
                    </strong>
                    <xsl:text>/</xsl:text>
                    <span style="color:#41B32D;">
                      <xsl:value-of select="$submitterSuccessCountForTheIteration"/>
                    </span>
                    <xsl:text>/</xsl:text>
                    <span style="color:#D13535;">
                      <xsl:value-of select="$submitterFailureCountForTheIteration"/>
                    </span>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="$submitterExceptionCountForTheIteration"/>
                  </td>
                  <xsl:if test="$unittestfileExists">
                  <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.iteration.unitest.count'))"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.iteration.unitest.negative.count'))"/>
                  </td>
                  </xsl:if>
                  <xsl:if test="$coveragefileExists">
                  <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                    <xsl:value-of select="format-number(NumVar:Value(concat(text(), '.iteration.linecoverage.count')), '#.00')"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="format-number(NumVar:Value(concat(text(), '.iteration.linecoverage.negative.count')), '#.00')"/>
                  </td>
                  </xsl:if>
                  <xsl:if test="$simianfileExists">
                  <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.iteration.line.count'))"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.iteration.line.negative.count'))"/>
                  </td>
                  </xsl:if>
                  <xsl:if test="$DevelopmentIterationSuiteFitNesseFileExists">
                    <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.iteration.fitValidation.count'))"/>
                      <xsl:text>/</xsl:text>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.iteration.fitValidation.negative.count'))"/>
                    </td>
                    <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.iteration.fitcorrectValidation.count'))"/>
                      <xsl:text>/</xsl:text>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.iteration.fitcorrectValidation.negative.count'))"/>
                    </td>
                  </xsl:if>
                  <td>
                    <xsl:attribute name="title">
                      <xsl:value-of select="$current.submitter"/>
                    </xsl:attribute>
                    <xsl:variable name="submitterTotalCount"      select="count(/statistics/integration[statistic[@name='mainsubmitter' and text()=$current.submitter]])"/>
                    <xsl:variable name="submitterSuccessCount"    select="count(/statistics/integration[@status='Success' and statistic[@name='mainsubmitter' and text()=$current.submitter]])"/>
                    <xsl:variable name="submitterExceptionCount"  select="count(/statistics/integration[@status='Exception' and statistic[@name='mainsubmitter' and text()=$current.submitter]])"/>
                    <xsl:variable name="submitterFailureCount"    select="$submitterTotalCount - ($submitterSuccessCount + $submitterExceptionCount)"/>

                    <strong>
                      <xsl:value-of select="$submitterTotalCount"/>
                    </strong>
                    <xsl:text>/</xsl:text>
                    <span style="color:#41B32D;">
                      <xsl:value-of select="$submitterSuccessCount"/>
                    </span>
                    <xsl:text>/</xsl:text>
                    <span style="color:#D13535;">
                      <xsl:value-of select="$submitterFailureCount"/>
                    </span>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="$submitterExceptionCount"/>
                  </td>
                  <xsl:if test="$unittestfileExists">
                  <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.overall.unitest.count'))"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.overall.unitest.negative.count'))"/>
                  </td>
                  </xsl:if>
                  <xsl:if test="$coveragefileExists">
                  <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                    <xsl:value-of select="format-number(NumVar:Value(concat(text(), '.overall.linecoverage.count')), '#.00')"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="format-number(NumVar:Value(concat(text(), '.overall.linecoverage.negative.count')), '#.00')"/>
                  </td>
                  </xsl:if>
                  <xsl:if test="$simianfileExists">
                  <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.overall.line.count'))"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="NumVar:Value(concat(text(), '.overall.line.negative.count'))"/>
                  </td>
                  </xsl:if>
                  <xsl:if test="$DevelopmentIterationSuiteFitNesseFileExists">
                    <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.overall.fitValidation.count'))"/>
                      <xsl:text>/</xsl:text>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.overall.fitValidation.negative.count'))"/>
                    </td>
                    <td>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$current.submitter"/>
                      </xsl:attribute>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.overall.fitcorrectValidation.count'))"/>
                      <xsl:text>/</xsl:text>
                      <xsl:value-of select="NumVar:Value(concat(text(), '.overall.fitcorrectValidation.negative.count'))"/>
                    </td>
                  </xsl:if>
                </tr>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:for-each>
      </table>
    </p>
    
    <hr/>

    <xsl:variable name="BaseChartUrl" select="concat('/&ProjectName;-&ProjectCodeLineName;/&Common.Directory.Packages.Name;/Analytics/charts.swf?library_path=/&ProjectName;-&ProjectCodeLineName;/&Common.Directory.Packages.Name;/Analytics/charts_library&amp;xml_source=/&ProjectName;-&ProjectCodeLineName;/&Common.Directory.ArtifactRoot.Name;/', $ArtifactFolderName)"/>

    <table cellpadding="0" cellspacing="0" border="0">
      <tr>
        <td>
          <p style="width: 25em;">
            This chart displays the total number of builds over all time.
            The count is not as important as the slope of the line.  You would like to see the yellow line moving up and the other lines flat.
          </p>
        </td>
        <td>
          <xsl:call-template name="ShowChart">
            <xsl:with-param name="Url" select="concat($BaseChartUrl, '/SuccessProgress.xml')"/>
          </xsl:call-template>
        </td>
      </tr>
    </table>

    <br/>
    <hr/>

    <table cellpadding="0" cellspacing="0" border="0">
      <tr>
        <td>
          <p style="width: 25em;">
            This chart displays the last 200 build times.  The yellow line is the total build time.
            The blue line is the compile time.  The others are shown as area instead of lines.  Generally they are small.
            There are other parts of the build that are not shown on the chart.  The parts depiced are the ones that have
            the greatest chance of causing long build times.
          </p>
        </td>
        <td>
          <xsl:call-template name="ShowChart">
            <xsl:with-param name="Url" select="concat($BaseChartUrl, '/BuildTimeHistoryChartData.xml')"/>
          </xsl:call-template>
        </td>
      </tr>
    </table>

    <br/>
    <hr/>

    <xsl:if test="ms:FileExists(concat($ArtifactDirectoryPath, '\DevelopmentIterationSuiteFitNesseCountsLineChartData.xml'))">
      <table cellpadding="0" cellspacing="0" border="0">
        <tr>
          <td>
            <p style="width: 25em;">
              This chart displays a trend of FitNesse tests counts for the current development iteration.
            </p>
          </td>
          <td>
            <xsl:call-template name="ShowChart">
              <xsl:with-param name="Url" select="concat($BaseChartUrl, '/DevelopmentIterationSuiteFitNesseCountsLineChartData.xml')"/>
            </xsl:call-template>
          </td>
        </tr>
      </table>

      <br/>
      <hr/>
    </xsl:if>

    <xsl:if test="ms:FileExists(concat($ArtifactDirectoryPath, '\RegressionSuiteFitNesseCountsLineChartData.xml'))">
      <table cellpadding="0" cellspacing="0" border="0">
        <tr>
          <td>
            <p style="width: 25em;">
              This chart displays a trend of FitNesse tests counts for the regression suite.
            </p>
          </td>
          <td>
            <xsl:call-template name="ShowChart">
              <xsl:with-param name="Url" select="concat($BaseChartUrl, '/RegressionSuiteFitNesseCountsLineChartData.xml')"/>
            </xsl:call-template>
          </td>
        </tr>
      </table>

      <br/>
      <hr/>
    </xsl:if>

    <xsl:if test="ms:FileExists(concat($ArtifactDirectoryPath, '\BugSuiteFitNesseCountsLineChartData.xml'))">
      <table cellpadding="0" cellspacing="0" border="0">
        <tr>
          <td>
            <p style="width: 25em;">
              This chart displays a trend of FitNesse tests counts for the bug suite.
            </p>
          </td>
          <td>
            <xsl:call-template name="ShowChart">
              <xsl:with-param name="Url" select="concat($BaseChartUrl, '/BugSuiteFitNesseCountsLineChartData.xml')"/>
            </xsl:call-template>
          </td>
        </tr>
      </table>

      <br/>
      <hr/>
    </xsl:if>

    <xsl:if test="ms:FileExists(concat($ArtifactDirectoryPath, '\UnitTestsCountsLineChartData.xml'))">
      <table cellpadding="0" cellspacing="0" border="0">
        <tr>
          <td>
            <p style="width: 25em;">
              This chart displays a trend of unit test counts.
            </p>
          </td>
          <td>
            <xsl:call-template name="ShowChart">
              <xsl:with-param name="Url" select="concat($BaseChartUrl, '/UnitTestsCountsLineChartData.xml')"/>
            </xsl:call-template>
          </td>
        </tr>
      </table>

      <br/>
      <hr/>
    </xsl:if>

    <xsl:if test="$coveragefileExists">
      <table cellpadding="0" cellspacing="0" border="0">
        <tr>
          <td>
            <p style="width: 25em;">
              This chart displays a trend of code coverage.
            </p>
          </td>
          <td>
            <xsl:call-template name="ShowChart">
              <xsl:with-param name="Url" select="concat($BaseChartUrl, '/CoveragePercentLineChartData.xml')"/>
            </xsl:call-template>
          </td>
        </tr>
      </table>

      <br/>
      <hr/>
    </xsl:if>

    <xsl:if test="ms:FileExists(concat($ArtifactDirectoryPath, '\Simian.ChartData.xml'))">
      <table cellpadding="0" cellspacing="0" border="0">
        <tr>
          <td>
            <p style="width: 25em;">
              This chart displays a trend of duplication.
            </p>
          </td>
          <td>
            <xsl:call-template name="ShowChart">
              <xsl:with-param name="Url" select="concat($BaseChartUrl, '/Simian.ChartData.xml')"/>
            </xsl:call-template>
          </td>
        </tr>
      </table>

      <br/>
      <hr/>
    </xsl:if>

    <hr/>

    <table  class="section-table" cellpadding="2" cellspacing="0" border="1" width="98%">
      <tr class="sectionheader">
        <th>Build Label</th>
        <th>Status</th>
        <xsl:for-each select="./integration[last()]/statistic">
          <th>
            <xsl:value-of select="./@name" />
          </th>
        </xsl:for-each>
        <xsl:if test="$unittestfileExists">
          <th>Unit Test Count</th>
        </xsl:if>
        <xsl:if test="$coveragefileExists">
          <th>Line Coverage</th>
        </xsl:if>
        <xsl:if test="$simianfileExists">
          <th>Percent Duplication</th>
        </xsl:if>
        <xsl:if test="$DevelopmentIterationSuiteFitNesseFileExists">
          <th>Fit Dev Test Count</th>
          <th>Fit Dev Test Correct Count</th>
          <th>Fit Dev Test Failure Count</th>
          <th>Fit Dev Test Exception Count</th>
          <th>Fit Dev Correct Validation Count</th>
          <th>Fit Dev Failed Validation Count</th>
          <th>Fit Dev Exception Count</th>
        </xsl:if>
        <xsl:if test="$RegressionSuiteFitNesseFileExists">
          <th>Fit Regession Test Count</th>
          <th>Fit Regession Test Correct Count</th>
          <th>Fit Regession Test Failure Count</th>
          <th>Fit Regession Test Exception Count</th>
          <th>Fit Regession Correct Validation Count</th>
          <th>Fit Regession Failed Validation Count</th>
          <th>Fit Regession Exception Count</th>
        </xsl:if>
      </tr>
      <xsl:for-each select="./integration">
        <xsl:sort select="position()" data-type="number" order="descending"/>
        <xsl:variable name="StartTime" select="statistic[@name = 'StartTime']/text()" />
        <xsl:variable name="colorClass">
          <xsl:choose>
            <xsl:when test="./@status = 'Success'">pass</xsl:when>
            <xsl:when test="./@status = 'Unknown'" >unknown</xsl:when>
            <xsl:when test="./@status = 'Exception'" >exception</xsl:when>
            <xsl:otherwise>fail</xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="ProjectName" select="statistic[@name = 'ProjectName']/text()" />
        <xsl:variable name="BuildTimeStamp" select="ms:FormatDate(statistic[@name='StartTime']/text(), 'yyyyMMddHHmmss')" />
        <xsl:variable name="BuildUrl">
          <xsl:choose>
            <xsl:when test="./@status = 'Success'">
              <xsl:value-of select="concat('http://&HostName;/&ProjectName;-&ProjectCodeLineName;/default.aspx?_action_ViewBuildReport=true&amp;server=', $CCNetServer, '&amp;project=', $ProjectName, '&amp;build=log', $BuildTimeStamp, 'Lbuild.', @build-label, '.xml')"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="concat('http://&HostName;/&ProjectName;-&ProjectCodeLineName;/default.aspx?_action_ViewBuildReport=true&amp;server=', $CCNetServer, '&amp;project=', $ProjectName, '&amp;build=log', $BuildTimeStamp, '.xml')"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <tr>
          <xsl:if test="(position()) mod 2 = 0">
            <xsl:choose>
              <xsl:when test="./@status = 'Success'">
                <xsl:attribute name="class">section-oddrowpassing</xsl:attribute>
              </xsl:when>
              <xsl:when test="./@status = 'Unknown'" >
                <xsl:attribute name="class">section-oddrow</xsl:attribute>
              </xsl:when>
              <xsl:when test="./@status = 'Exception'" >
                <xsl:attribute name="class">section-oddrowexception</xsl:attribute>
              </xsl:when>
              <xsl:otherwise>
                <xsl:attribute name="class">section-oddrowfailing</xsl:attribute>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:if>
          <xsl:if test="(position()) mod 2 != 0">
            <xsl:choose>
              <xsl:when test="./@status = 'Success'">
                <xsl:attribute name="class">section-evenrowpassing</xsl:attribute>
              </xsl:when>
              <xsl:when test="./@status = 'Unknown'" >
                <xsl:attribute name="class">section-evenrow</xsl:attribute>
              </xsl:when>
              <xsl:when test="./@status = 'Exception'" >
                <xsl:attribute name="class">section-evenrowexception</xsl:attribute>
              </xsl:when>
              <xsl:otherwise>
                <xsl:attribute name="class">section-evenrowfailing</xsl:attribute>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:if>
          <th>
            <a>
              <xsl:attribute name="href">
                <xsl:value-of select="$BuildUrl"/>
              </xsl:attribute>
              <xsl:value-of select="./@build-label"/>
            </a>
          </th>
          <th class="{$colorClass}">
            <xsl:value-of select="./@status"/>
          </th>
          <xsl:for-each select="./statistic">
            <td>
              <xsl:value-of select="."/>
            </td>
          </xsl:for-each>
          <xsl:if test="$unittestfileExists">
            <td>
              <xsl:value-of select="($unittestdoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Total Test Count']/text()"/>
            </td>
          </xsl:if>
          <xsl:if test="$coveragefileExists">
            <td>
              <xsl:value-of select="($coveragedoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'linecoverage']/text()"/>%
            </td>
          </xsl:if>
          <xsl:if test="$simianfileExists">
            <td>
              <xsl:variable name="SimianIntegration" select="($simiandoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]"/>
              <xsl:value-of select="format-number(($SimianIntegration/statistic[@name = 'duplicateLineCount']/text() - $SimianIntegration/statistic[@name = 'duplicateBlockLineCount']/text()) div $SimianIntegration/statistic[@name = 'totalSignificantLineCount']/text(), '#.00%')"/>
            </td>
          </xsl:if>

          <xsl:if test="$DevelopmentIterationSuiteFitNesseFileExists">
            <td>
              <xsl:value-of select="($DevelopmentIterationSuiteFitNesseDoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Test Count']/text()"/>
            </td>
            <td>
              <xsl:value-of select="($DevelopmentIterationSuiteFitNesseDoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Test Correct Count']/text()"/>
            </td>
            <td>
              <xsl:value-of select="($DevelopmentIterationSuiteFitNesseDoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Test Failure Count']/text()"/>
            </td>
            <td>
              <xsl:value-of select="($DevelopmentIterationSuiteFitNesseDoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Test Exception Count']/text()"/>
            </td>
            <td>
              <xsl:value-of select="($DevelopmentIterationSuiteFitNesseDoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Correct Validation Count']/text()"/>
            </td>
            <td>
              <xsl:value-of select="($DevelopmentIterationSuiteFitNesseDoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Failed Validation Count']/text()"/>
            </td>
            <td>
              <xsl:value-of select="($DevelopmentIterationSuiteFitNesseDoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Exception Count']/text()"/>
            </td>
          </xsl:if>

          <xsl:if test="$RegressionSuiteFitNesseFileExists">
            <td>
              <xsl:value-of select="($RegressionSuiteFitNesseDoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Test Count']/text()"/>
            </td>
            <td>
              <xsl:value-of select="($RegressionSuiteFitNesseDoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Test Correct Count']/text()"/>
            </td>
            <td>
              <xsl:value-of select="($RegressionSuiteFitNesseDoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Test Failure Count']/text()"/>
            </td>
            <td>
              <xsl:value-of select="($RegressionSuiteFitNesseDoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Test Exception Count']/text()"/>
            </td>
            <td>
              <xsl:value-of select="($RegressionSuiteFitNesseDoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Correct Validation Count']/text()"/>
            </td>
            <td>
              <xsl:value-of select="($RegressionSuiteFitNesseDoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Failed Validation Count']/text()"/>
            </td>
            <td>
              <xsl:value-of select="($RegressionSuiteFitNesseDoc)/Builds/integration[statistic[@name = 'StartTime' and text() = $StartTime]]/statistic[@name = 'Exception Count']/text()"/>
            </td>
          </xsl:if>
        </tr>
      </xsl:for-each>
    </table>
  </xsl:template>


  <xsl:template name="GetStatCount">
    <xsl:param name="doc"/>
    <xsl:param name="statName"/>
    <xsl:param name="countName"/>
    <xsl:param name="day"/>
    <xsl:param name="month"/>
    <xsl:param name="year"/>
    <xsl:param name="dayofyear"/>
    <xsl:param name="iteration"/>
    <xsl:param name="debug"/>
    <xsl:param name="ignore-initial-value"/>

      <xsl:variable name="trash1" select="StringVar:Set('submitter.name', '')"/>
      <xsl:call-template name="GetSubmitter">
        <xsl:with-param name="integration" select="."/>
      </xsl:call-template>
      <xsl:variable name="submitter.name" select="StringVar:Value('submitter.name')"/>
      <xsl:variable name="submitter.count" select="statistic[@name=$statName]"/>
      <xsl:choose>
        <xsl:when test="NumVar:Value(concat($countName, '.previous.count')) > 0">
          <xsl:variable name="previous.count" select="NumVar:Value(concat($countName, '.previous.count'))"/>
          <xsl:variable name="submitter.count.increase" select="$submitter.count - $previous.count"/>

          <xsl:choose>
            <xsl:when test="$submitter.count.increase > 0">
              <xsl:call-template name="RecordStatCount">
                <xsl:with-param name="submitter.name" select="$submitter.name"/>
                <xsl:with-param name="countName" select="$countName"/>
                <xsl:with-param name="countValue" select="$submitter.count.increase"/>
                <xsl:with-param name="day" select="$day"/>
                <xsl:with-param name="month" select="$month"/>
                <xsl:with-param name="year" select="$year"/>
                <xsl:with-param name="dayofyear" select="$dayofyear"/>
                <xsl:with-param name="iteration" select="$iteration"/>
              </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
              <xsl:call-template name="RecordStatCount">
                <xsl:with-param name="submitter.name" select="$submitter.name"/>
                <xsl:with-param name="countName" select="concat($countName, '.negative')"/>
                <xsl:with-param name="countValue" select="$submitter.count.increase"/>
                <xsl:with-param name="day" select="$day"/>
                <xsl:with-param name="month" select="$month"/>
                <xsl:with-param name="year" select="$year"/>
                <xsl:with-param name="dayofyear" select="$dayofyear"/>
                <xsl:with-param name="iteration" select="$iteration"/>
              </xsl:call-template>
            </xsl:otherwise>
          </xsl:choose>

          <xsl:if test="$debug">
            <p>
              <xsl:value-of select="$countName"/> In Build <xsl:value-of select="@build-label"/> - <xsl:value-of select="statistic[@name='StartTime']"/> - <xsl:value-of select="$submitter.name"/> added <xsl:value-of select="$submitter.count.increase"/> previous <xsl:value-of select="$previous.count"/> count <xsl:value-of select="$submitter.count"/>
            </p>
          </xsl:if>
          <xsl:variable name="trash" select="NumVar:Set(concat($countName, '.previous.count'), $submitter.count)"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="trash2" select="NumVar:Set(concat($countName, '.previous.count'), $submitter.count)"/>
          <xsl:if test="not($ignore-initial-value)">
            <xsl:variable name="trash3" select="NumVar:Add(concat($submitter.name, '.overall.', $countName, '.count'), $submitter.count)"/>
            <xsl:if test="$debug">
              <p>
                <xsl:value-of select="$countName"/> In Build <xsl:value-of select="@build-label"/> - <xsl:value-of select="statistic[@name='StartTime']"/> - <xsl:value-of select="$submitter.name"/> added <xsl:value-of select="$submitter.count"/>
              </p>
            </xsl:if>
          </xsl:if>
        </xsl:otherwise>
      </xsl:choose>
  </xsl:template>

  <xsl:template name="GetSuccessStatCounts">
    <xsl:param name="doc"/>
    <xsl:param name="statName"/>
    <xsl:param name="countName"/>
    <xsl:param name="day"/>
    <xsl:param name="month"/>
    <xsl:param name="year"/>
    <xsl:param name="dayofyear"/>
    <xsl:param name="iteration"/>
    <xsl:param name="debug"/>
    <xsl:param name="ignore-initial-value"/>

    <xsl:for-each select="($doc)/Builds/integration[@status='Success' and statistic[@name=$statName and boolean(text()) and not(text()=preceding::integration[@status='Success']/statistic[@name=$statName]/text())]]">
      <xsl:call-template name="GetStatCount">
        <xsl:with-param name="doc"                  select="$doc"                  />
        <xsl:with-param name="statName"             select="$statName"             />
        <xsl:with-param name="countName"            select="$countName"            />
        <xsl:with-param name="day"                  select="$day"                  />
        <xsl:with-param name="month"                select="$month"                />
        <xsl:with-param name="year"                 select="$year"                 />
        <xsl:with-param name="dayofyear"            select="$dayofyear"            />
        <xsl:with-param name="iteration"            select="$iteration"            />
        <xsl:with-param name="debug"                select="$debug"                />
        <xsl:with-param name="ignore-initial-value" select="$ignore-initial-value" />
      </xsl:call-template>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="GetStatCounts">
    <xsl:param name="doc"/>
    <xsl:param name="statName"/>
    <xsl:param name="countName"/>
    <xsl:param name="day"/>
    <xsl:param name="month"/>
    <xsl:param name="year"/>
    <xsl:param name="dayofyear"/>
    <xsl:param name="iteration"/>
    <xsl:param name="debug"/>
    <xsl:param name="ignore-initial-value"/>

    <xsl:for-each select="($doc)/Builds/integration[statistic[@name=$statName and boolean(text())]]">
      <xsl:call-template name="GetStatCount">
        <xsl:with-param name="doc"                  select="$doc"                  />
        <xsl:with-param name="statName"             select="$statName"             />
        <xsl:with-param name="countName"            select="$countName"            />
        <xsl:with-param name="day"                  select="$day"                  />
        <xsl:with-param name="month"                select="$month"                />
        <xsl:with-param name="year"                 select="$year"                 />
        <xsl:with-param name="dayofyear"            select="$dayofyear"            />
        <xsl:with-param name="iteration"            select="$iteration"            />
        <xsl:with-param name="debug"                select="$debug"                />
        <xsl:with-param name="ignore-initial-value" select="$ignore-initial-value" />
      </xsl:call-template>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="GetSubmitter">
    <xsl:param name="integration"/>
    <xsl:choose>
      <xsl:when test="boolean(normalize-space($integration/statistic[@name='forcedby']/text()))">
        <xsl:variable name="trash" select="StringVar:Set('submitter.name', $integration/statistic[@name='forcedby']/text())"/>
      </xsl:when>
      <xsl:when test="boolean(normalize-space($integration/statistic[@name='mainsubmitter']/text()))">
        <xsl:variable name="trash" select="StringVar:Set('submitter.name', $integration/statistic[@name='mainsubmitter']/text())"/>
      </xsl:when>
      <xsl:when test="boolean($integration/preceding::integration)">
        <xsl:call-template name="GetSubmitter">
          <xsl:with-param name="integration" select="$integration/preceding::integration[1]"/>
        </xsl:call-template>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="RecordStatCount">
    <xsl:param name="submitter.name"/>
    <xsl:param name="countName"/>
    <xsl:param name="countValue"/>
    <xsl:param name="day"/>
    <xsl:param name="month"/>
    <xsl:param name="year"/>
    <xsl:param name="dayofyear"/>
    <xsl:param name="iteration"/>

    <xsl:if test="@day=$day and @month=$month and @year=$year">
      <xsl:variable name="trash" select="NumVar:Add(concat($submitter.name, '.today.', $countName, '.count'), $countValue)"/>
    </xsl:if>

    <xsl:if test="@dayofyear > $dayofyear - 7">
      <xsl:variable name="trash" select="NumVar:Add(concat($submitter.name, '.sevendays.', $countName, '.count'), $countValue)"/>
    </xsl:if>

    <xsl:if test="statistic[@name='IterationName' and text() = $iteration]">
      <xsl:variable name="trash" select="NumVar:Add(concat($submitter.name, '.iteration.', $countName, '.count'), $countValue)"/>
    </xsl:if>

    <xsl:variable name="trash" select="NumVar:Add(concat($submitter.name, '.overall.', $countName, '.count'), $countValue)"/>
  </xsl:template>

    <xsl:template name="ShowChart">
    <xsl:param name="Url" />

    <div>
      <a href="javascript:void(0)" class="dsphead" onclick="dsp(this)">
        <span class="dspchar">+ Show Chart</span>
      </a>
    </div>
    <div class="dspcont">
      <OBJECT classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
          codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0"
          WIDTH="400"
          HEIGHT="250"
          id="charts"
          ALIGN="">

        <PARAM NAME="movie" >
          <xsl:attribute name="value">
            <xsl:value-of select="$Url"/>
          </xsl:attribute>
        </PARAM>
        <PARAM NAME="quality" VALUE="high"/>
        <PARAM NAME="bgcolor" VALUE="#666666"/>

        <EMBED
               quality="high"
               bgcolor="#666666"
               WIDTH="700"
               HEIGHT="350"
               NAME="charts"
               ALIGN=""
               swLiveConnect="true"
               TYPE="application/x-shockwave-flash"
               PLUGINSPAGE="http://www.macromedia.com/go/getflashplayer">
          <xsl:attribute name="src">
            <xsl:value-of select="$Url"/>
          </xsl:attribute>
        </EMBED>
      </OBJECT>
    </div>
  </xsl:template>
</xsl:stylesheet>

  