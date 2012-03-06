<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
        xmlns:lxslt="http://xml.apache.org/xslt"
>
  <xsl:output method="xml" omit-xml-declaration="yes"
    doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN" version="1.0"/>
  <xsl:decimal-format decimal-separator="." grouping-separator="," />
  <!--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 -->

  <!--

 Sample stylesheet to be used with soapUI LoadTest output.

 It creates a non-framed report that can be useful to send via
 e-mail or such.
 
 Based on sample stylesheet to be used with Ant JUnitReport output.

-->

  <xsl:template match="/">
    <xsl:apply-templates select="//testsuites" />
  </xsl:template>


  <xsl:template match="testsuites">
    <style type="text/css">
      table.details tr th{
      font-weight: bold;
      text-align:left;
      background:#a6caf0;
      }
      table.details tr td{
      background:#eeeee0;
      }
      .Error {
      font-weight:bold; color:red;
      }
      .Failure {
      font-weight:bold; color:purple;
      }
      .Properties {
      text-align:right;
      }
    </style>
    <script type="text/javascript" language="JavaScript">
      var TestCases = new Array();
      var cur;
      <xsl:for-each select="./testsuite">
        <xsl:apply-templates select="properties"/>
      </xsl:for-each>

    </script>
    <script type="text/javascript" language="JavaScript">
      <![CDATA[
        function displayProperties (name) {
          var win = window.open('','JUnitSystemProperties','scrollbars=1,resizable=1');
          var doc = win.document;
          doc.open();
          doc.write("<html><head><title>Properties of " + name + "</title>");
          doc.write("<style>")
          doc.write("body {font:normal 68% verdana,arial,helvetica; color:#000000; }");
          doc.write("table tr td, table tr th { font-size: 68%; }");
          doc.write("table.properties { border-collapse:collapse; border-left:solid 1 #cccccc; border-top:solid 1 #cccccc; padding:5px; }");
          doc.write("table.properties th { text-align:left; border-right:solid 1 #cccccc; border-bottom:solid 1 #cccccc; background-color:#eeeeee; }");
          doc.write("table.properties td { font:normal; text-align:left; border-right:solid 1 #cccccc; border-bottom:solid 1 #cccccc; background-color:#fffffff; }");
          doc.write("h3 { margin-bottom: 0.5em; font: bold 115% verdana,arial,helvetica }");
          doc.write("</style>");
          doc.write("</head><body>");
          doc.write("<h3>Properties of " + name + "</h3>");
          doc.write("<div align=\"right\"><a href=\"javascript:window.close();\">Close</a></div>");
          doc.write("<table class='properties'>");
          doc.write("<tr><th>Name</th><th>Value</th></tr>");
          for (prop in TestCases[name]) {
            doc.write("<tr><th>" + prop + "</th><td>" + TestCases[name][prop] + "</td></tr>");
          }
          doc.write("</table>");
          doc.write("</body></html>");
          doc.close();
          win.focus();
        }
      ]]>
    </script>
    <a name="top"></a>
    <xsl:call-template name="pageHeader"/>

    <!-- Summary part -->
    <xsl:call-template name="summary"/>
    <hr size="1" width="95%" align="left"/>

    <!-- Package List part -->
    <xsl:call-template name="packagelist"/>
    <hr size="1" width="95%" align="left"/>

    <!-- For each package create its part -->
    <xsl:call-template name="packages"/>
    <hr size="1" width="95%" align="left"/>

    <!-- For each class create the  part -->
    <xsl:call-template name="classes"/>
  </xsl:template>



  <!-- ================================================================== -->
  <!-- Write a list of all packages with an hyperlink to the anchor of    -->
  <!-- of the package name.                                               -->
  <!-- ================================================================== -->
  <xsl:template name="packagelist">
    <h2>Packages</h2>
    Note: package statistics are not computed recursively, they only sum up all of its testsuites numbers.
    <table class="details" border="0" cellpadding="5" cellspacing="2" width="95%">
      <tr valign="top">
        <th width="80%">Name</th>
        <th>Tests</th>
        <th>Errors</th>
        <th nowrap="nowrap">Ratio</th>
      </tr>
      <!-- list all packages recursively -->
      <xsl:for-each select="./testsuite[not(./@package = preceding-sibling::testsuite/@package)]">
        <xsl:sort select="@package"/>
        <xsl:variable name="testsuites-in-package" select="//testsuites/testsuite[./@package = current()/@package]"/>
        <xsl:variable name="errorCount" select="sum($testsuites-in-package/@err)"/>
        <xsl:variable name="testsCount" select="count($testsuites-in-package/@package)"/>
        <!-- write a summary for the package -->
        <tr valign="top">
          <!-- set a nice color depending if there is an error/failure -->
          <xsl:attribute name="class">
            <xsl:choose>
              <xsl:when test="$errorCount &gt; 0">Error</xsl:when>
            </xsl:choose>
          </xsl:attribute>
          <td>
            <a href="#{@package}">
              <xsl:value-of select="@package"/>
            </a>
          </td>
          <td>
            <xsl:value-of select="$testsCount"/>
          </td>
          <td>
            <xsl:value-of select="$errorCount"/>
          </td>
          <td>
            <xsl:call-template name="display-percent">
              <xsl:with-param name="value" select="sum($testsuites-in-package/@rat) div $testsCount"/>
            </xsl:call-template>
          </td>
        </tr>
      </xsl:for-each>
    </table>
  </xsl:template>


  <!-- ==================================================================  -->
  <!-- Write a package level report                                        -->
  <!-- It creates a table with values from the document:                   -->
  <!-- Name | min | max | avg | last | cnt | tps | bytes | bps | err | rat -->
  <!-- ==================================================================  -->
  <xsl:template name="packages">
    <!-- create an anchor to this package name -->
    <xsl:for-each select="//testsuites/testsuite[not(./@package = preceding-sibling::testsuite/@package)]">
      <xsl:sort select="@package"/>
      <a name="{@package}"></a>
      <h3>
        Package <xsl:value-of select="@package"/>
      </h3>

      <table class="details" border="0" cellpadding="5" cellspacing="2" width="95%">
        <xsl:call-template name="testsuite.test.header"/>

        <!-- match the testsuites of this package -->
        <xsl:apply-templates select="/testsuites/testsuite[./@package = current()/@package]" mode="print.test"/>
      </table>
      <a href="#top">Back to top</a>
      <p/>
      <p/>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="classes">
    <xsl:for-each select="testsuite">
      <xsl:sort select="@name"/>
      <!-- create an anchor to this class name -->
      <a name="{@name}"></a>
      <h3>
        TestCase <xsl:value-of select="@name"/>
      </h3>

      <table class="details" border="0" cellpadding="5" cellspacing="2" width="95%">
        <xsl:call-template name="testcase.test.header"/>
        <!--
              test can even not be started at all (failure to load the class)
              so report the error directly
              -->
        <xsl:if test="./error">
          <tr class="Error">
            <td colspan="4">
              <xsl:apply-templates select="./error"/>
            </td>
          </tr>
        </xsl:if>
        <xsl:apply-templates select="./testcase" mode="print.test"/>
      </table>
      <div class="Properties">
        <a>
          <xsl:attribute name="href">
            <xsl:value-of select="@name"/>
          </xsl:attribute>
          Java Profiler Snapshot &#187;
        </a>
      </div>
      <div class="Properties">
        <a>
          <xsl:attribute name="href">
            javascript:displayProperties('<xsl:value-of select="@package"/>.<xsl:value-of select="@name"/>');
          </xsl:attribute>
          Properties &#187;
        </a>
      </div>
      <p/>

      <a href="#top">Back to top</a>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="summary">
    <h2>Summary</h2>   
    <xsl:variable name="testCount" select="count(testsuite)"/>
    <xsl:variable name="errorCount" select="sum(testsuite/@err)"/>
    <xsl:variable name="failureCount" select="sum(testsuite/@failures)"/>
    <xsl:variable name="timeCount" select="sum(testsuite/@time)"/>
    <xsl:variable name="successRate" select="100 - (sum(testsuite/@rat) div $testCount)"/>
    <table class="details" border="0" cellpadding="5" cellspacing="2" width="95%">
      <tr valign="top">
        <th>Tests</th>
        <th>Failures</th>
        <th>Errors</th>
        <th>Success rate</th>
      </tr>
      <tr valign="top">
        <xsl:attribute name="class">
          <xsl:choose>
            <xsl:when test="$errorCount &gt; 0">Error</xsl:when>
          </xsl:choose>
        </xsl:attribute>
        <td>
          <xsl:value-of select="$testCount"/>
        </td>
        <td>
          <xsl:value-of select="$failureCount"/>
        </td>
        <td>
          <xsl:value-of select="$errorCount"/>
        </td>
        <td>
          <xsl:call-template name="display-percent">
            <xsl:with-param name="value" select="$successRate"/>
          </xsl:call-template>
        </td>
      </tr>
    </table>
    <table border="0" width="95%">
      <tr>
        <td style="text-align: justify;">
          Note: <i>failures</i> are anticipated and checked for with assertions while <i>errors</i> are unanticipated.
        </td>
      </tr>
    </table>
  </xsl:template>

  <!--
   Write properties into a JavaScript data structure.
   This is based on the original idea by Erik Hatcher (ehatcher@apache.org)
   -->
  <xsl:template match="properties">
    cur = TestCases['<xsl:value-of select="../@package"/>.<xsl:value-of select="../@name"/>'] = new Array();
    <xsl:for-each select="property">
      <xsl:sort select="@name"/>
      <xsl:variable name="value">
        <xsl:call-template name="escape-quotes">
          <xsl:with-param name="string" select="@value"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:choose>
        <xsl:when test="substring(@value,string-length(@value)) = '\'">
          cur['<xsl:value-of select="@name"/>'] = "<xsl:value-of select="$value"/>\";
        </xsl:when>
        <xsl:otherwise>
          cur['<xsl:value-of select="@name"/>'] = "<xsl:value-of select="$value"/>";
        </xsl:otherwise>
      </xsl:choose>

    </xsl:for-each>
  </xsl:template>

  <xsl:template name="escape-quotes">
    <xsl:param name="string"/>
    <xsl:choose>
      <xsl:when test="contains($string, '&#34;')">
        <xsl:variable name="prefix" select="substring-before($string, '&#34;')"/>
        <xsl:variable name="postfix" select="substring($string, string-length($prefix)+string-length('&#34;')+1)"/>
        <xsl:value-of select="concat($prefix, '\&#34;')"/>
        <xsl:call-template name="escape-quotes">
          <xsl:with-param name="string" select="$postfix"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$string"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Page HEADER -->
  <xsl:template name="pageHeader">
    <h1>SoapUI Load Test Results</h1>
    <table width="100%">
      <tr>
        <td align="left"></td>
        <td align="right">
          Designed for use with <a href='http://www.soapui.org/'>soapUI</a> and <a href='http://ant.apache.org/'>Ant</a>.
        </td>
      </tr>
    </table>
    <table border="0" width="100%">
      <tr>
        <td style="text-align: justify;">
          "YourKit is kindly supporting open source projects with its full-featured Java Profiler.
          YourKit, LLC is the creator of innovative and intelligent tools for profiling Java and .NET applications. Take a look at YourKit's leading software products:
          <a href="http://www.yourkit.com/java/profiler/index.jsp">YourKit Java Profiler</a> and <a href="http://www.yourkit.com/.net/profiler/index.jsp">YourKit .NET Profiler</a>."
        </td>
      </tr>
    </table>
    <hr size="1"/>
  </xsl:template>

  <xsl:template match="testsuite" mode="header">
    <tr valign="top">
      <th width="80%">Name</th>
      <th>Tests</th>
      <th>Errors</th>
      <th>Failures</th>
    </tr>
  </xsl:template>

  <!-- class header -->
  <xsl:template name="testsuite.test.header">
    <tr valign="top">
      <th width="80%">Name</th>
      <th>Min</th>
      <th>Max</th>
      <th>Avg</th>
      <th>last</th>
      <th>cnt</th>
      <th>tps</th>
      <th>bytes</th>
      <th>bps</th>
      <th>err</th>
      <th nowrap="nowrap">rat</th>
    </tr>
  </xsl:template>

  <!-- method header -->
  <xsl:template name="testcase.test.header">
    <tr valign="top">
      <th width="80%">Name</th>
      <th>Min</th>
      <th>Max</th>
      <th>Avg</th>
      <th>last</th>
      <th>cnt</th>
      <th>tps</th>
      <th>bytes</th>
      <th>bps</th>
      <th>err</th>
      <th nowrap="nowrap">rat</th>
    </tr>
  </xsl:template>


  <!-- class information -->
  <xsl:template match="testsuite" mode="print.test">
    <tr valign="top">
      <!-- set a nice color depending if there is an error/failure -->
      <xsl:attribute name="class">
        <xsl:choose>
          <xsl:when test="@failures[.&gt; 0]">Failure</xsl:when>
          <xsl:when test="@err[.&gt; 0]">Error</xsl:when>
        </xsl:choose>
      </xsl:attribute>

      <!-- print testsuite information -->
      <td>
        <a href="#{@name}">
          <xsl:value-of select="@name"/>
        </a>
      </td>
      <td>
        <xsl:value-of select="@min"/>
      </td>
      <td>
        <xsl:value-of select="@max"/>
      </td>
      <td>
        <xsl:value-of select="@avg"/>
      </td>
      <td>
        <xsl:value-of select="@last"/>
      </td>
      <td>
        <xsl:value-of select="@cnt"/>
      </td>
      <td>
        <xsl:value-of select="@tps"/>
      </td>
      <td>
        <xsl:value-of select="@bytes"/>
      </td>
      <td>
        <xsl:value-of select="@bps"/>
      </td>
      <td>
        <xsl:value-of select="@err"/>
      </td>
      <td>
        <xsl:value-of select="@rat"/>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="testcase" mode="print.test">
    <tr valign="top">
      <xsl:attribute name="class">
        <xsl:choose>
          <xsl:when test="failure | error">Error</xsl:when>
        </xsl:choose>
      </xsl:attribute>
      <td>
        <xsl:value-of select="@name"/>
      </td>
      <xsl:choose>
        <xsl:when test="failure">
          <td>Failure</td>
          <td>
            <xsl:apply-templates select="failure"/>
          </td>
        </xsl:when>
        <xsl:when test="error">
          <td>Error</td>
          <td>
            <xsl:apply-templates select="error"/>
          </td>
        </xsl:when>
        <xsl:otherwise>
          <td>Success</td>
          <td></td>
        </xsl:otherwise>
      </xsl:choose>
      <td>
        <xsl:call-template name="display-time">
          <xsl:with-param name="value" select="@time"/>
        </xsl:call-template>
      </td>
    </tr>
  </xsl:template>


  <xsl:template match="failure">
    <xsl:call-template name="display-failures"/>
  </xsl:template>

  <xsl:template match="error">
    <xsl:call-template name="display-failures"/>
  </xsl:template>

  <!-- Style for the error and failure in the tescase template -->
  <xsl:template name="display-failures">
    <xsl:choose>
      <xsl:when test="not(@message)">N/A</xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="@message"/>
      </xsl:otherwise>
    </xsl:choose>
    <!-- display the stacktrace -->
    <code>
      <br/>
      <br/>
      <xsl:call-template name="br-replace">
        <xsl:with-param name="word" select="."/>
      </xsl:call-template>
    </code>
    <!-- the later is better but might be problematic for non-21" monitors... -->
    <!--pre><xsl:value-of select="."/></pre-->
  </xsl:template>

  <xsl:template name="JS-escape">
    <xsl:param name="string"/>

    <xsl:value-of select="string"/>
  </xsl:template>


  <!--
    template that will convert a carriage return into a br tag
    @param word the text from which to convert CR to BR tag
-->

  <xsl:template name="br-replace">
    <xsl:param name="word"/>
    <xsl:variable name="cr">
      <xsl:text>
        <!-- </xsl:text> on next line on purpose to get newline -->
        </xsl:text>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="contains($word,$cr)">
        <xsl:value-of select="substring-before($word,$cr)"/>
        <br/>
        <xsl:call-template name="br-replace">
          <xsl:with-param name="word" select="substring-after($word,$cr)"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$word"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="display-time">
    <xsl:param name="value"/>
    <xsl:value-of select="format-number($value,'0.000')"/>
  </xsl:template>

  <xsl:template name="display-percent">
    <xsl:param name="value"/>
    <xsl:value-of select="format-number($value,'0.00')"/>
    <xsl:text>%</xsl:text>
  </xsl:template>

</xsl:stylesheet>
