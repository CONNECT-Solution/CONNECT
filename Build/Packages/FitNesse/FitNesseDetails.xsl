<?xml version="1.0"?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "..\..\Entities.xml">

  %entities;
]>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="xml" omit-xml-declaration="yes"/>

  <xsl:template match="/">
    <script type="text/javascript">
      <![CDATA[
      var collapsableOpenCss = "collapsable";
      var collapsableClosedCss = "hidden";
      var collapsableOpenImg = "Packages/FitNesse/images/collapsableOpen.gif";
      var collapsableClosedImg = "Packages/FitNesse/images/collapsableClosed.gif";

      function toggleCollapsable(id)
      {
        var div = document.getElementById(id);
        var img = document.getElementById("img" + id);
        if (div.className.indexOf(collapsableClosedCss) != -1)
        {
          div.className = collapsableOpenCss;
          img.src = collapsableOpenImg;
        }
        else
        {
          div.className = collapsableClosedCss;
          img.src = collapsableClosedImg;
        }
      }

      function expandOrCollapseAll(cssClass)
      {
      divs = document.getElementsByTagName("div");
      for (var i in  divs)
        {
          div = divs[i];
          if (div.className == cssClass)
          {
            toggleCollapsable(div.id);
          }
        }
      }

      function collapseAll()
      {
        expandOrCollapseAll(collapsableOpenCss);
      }

      function expandAll()
      {
        expandOrCollapseAll(collapsableClosedCss);
      }
      ]]>
    </script>
    <style type="text/css">
      #FitNesseDetails.table, #FitNesseDetails.td, #FitNesseDetails.th
      {
        border: solid #777777;
      }
      #FitNesseDetails.table
      {
        background: #F8F8F8;
        border-width: 1px 0px 0px 1px;
        padding: 0;
        margin: 0;
      }
      #FitNesseDetails.td, #FitNesseDetails.th
      {
        border-width: 0px 1px 1px 0px;
        padding: 5px;
      }
      .hidden {
        display: none;
      }
      .collapsable {
        margin: 0px 0px 0px 15px;
        display: block;
      }
      img{
        border: 0px;
      }
      .right {
        float: right;
      }

      .left {
        float: left;
      }

      .strike {
        text-decoration: line-through;
      }
    </style>

    <xsl:variable name="Test.Correct.Count" select="count(//result/counts[right[text() &gt; 0] and not(ignores[text() &gt; 0]) and not(wrong[text() &gt; 0]) and not(exceptions[text() &gt; 0])])"/>
    <xsl:variable name="Test.Failure.Count" select="count(//result/counts/wrong[text() &gt; 0])"/>
    <xsl:variable name="Test.Exception.Count" select="count(//result/counts/exceptions[text() &gt; 0])"/>
    <xsl:variable name="Test.Ignore.Count" select="count(//result/counts[ignores[text() &gt; 0] and not(wrong[text() &gt; 0]) and not(exceptions[text() &gt; 0])])"/>

    <xsl:variable name="Assertion.Correct.Count" select="sum(//result/counts/right)"/>
    <xsl:variable name="Assertion.Failure.Count" select="sum(//result/counts/wrong)"/>
    <xsl:variable name="Assertion.Exception.Count" select="sum(//result/counts/exceptions)"/>
    <xsl:variable name="Assertion.Ignore.Count" select="sum(//result/counts/ignores)"/>

    <xsl:variable name="Url" select="/testResults/url" />


    <div id="FitNesseTitle">
      <br/>
      <h2>
        <a class="header-title">
          <xsl:attribute name="href">
            <xsl:value-of select="/testResults/url"/>  
          </xsl:attribute>
          <xsl:value-of select="/testResults/rootPath"/>
          <xsl:text> Results</xsl:text>
        </a>
      </h2>
      <br/>
    </div>
    
    <div id="FitNesseSummary">
      <div>
        <xsl:choose>
          <xsl:when test="$Test.Failure.Count &gt; 0">
            <xsl:attribute name="class">
              <xsl:text>fail sectionheader-container-error</xsl:text>
            </xsl:attribute>
          </xsl:when>
          <xsl:when test="$Test.Exception.Count &gt; 0">
            <xsl:attribute name="class">
              <xsl:text>exception sectionheader-container-error</xsl:text>
            </xsl:attribute>
          </xsl:when>
          <xsl:when test="$Test.Ignore.Count &gt; 0">
            <xsl:attribute name="class">
              <xsl:text>unknown sectionheader-container-warning</xsl:text>
            </xsl:attribute>
          </xsl:when>
          <xsl:otherwise>
            <xsl:attribute name="class">
              <xsl:text>pass sectionheader-container</xsl:text>
            </xsl:attribute>
          </xsl:otherwise>
        </xsl:choose>
        <strong>Test Pages:</strong> <xsl:value-of select="$Test.Correct.Count"/> right, <xsl:value-of select="$Test.Failure.Count"/> wrong, <xsl:value-of select="$Test.Ignore.Count"/> ignored, <xsl:value-of select="$Test.Exception.Count"/> exceptions     <strong>Assertions:</strong> <xsl:value-of select="$Assertion.Correct.Count"/> right, <xsl:value-of select="$Assertion.Failure.Count"/> wrong, <xsl:value-of select="$Assertion.Ignore.Count"/> ignored, <xsl:value-of select="$Assertion.Exception.Count"/> exceptions
      </div>

      <xsl:for-each select="/testResults/result">
        <div>
          <xsl:choose>
            <xsl:when test="position() mod 2 = 0">
              <xsl:attribute name="class">
                <xsl:text>section-evenrow sectionheader-text</xsl:text>
              </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
              <xsl:attribute name="class">
                <xsl:text>section-oddrow sectionheader-text</xsl:text>
              </xsl:attribute>
            </xsl:otherwise>
          </xsl:choose>
          
          <span>
            <xsl:choose>
              <xsl:when test="counts/wrong &gt; 0">
                <xsl:attribute name="class">
                  <xsl:text>fail</xsl:text>
                </xsl:attribute>
              </xsl:when>
              <xsl:when test="counts/exceptions &gt; 0">
                <xsl:attribute name="class">
                  <xsl:text>exception</xsl:text>
                </xsl:attribute>
              </xsl:when>
              <xsl:when test="counts/ignores &gt; 0">
                <xsl:attribute name="class">
                  <xsl:text>unknown</xsl:text>
                </xsl:attribute>
              </xsl:when>
              <xsl:otherwise>
                <xsl:attribute name="class">
                  <xsl:text>pass</xsl:text>
                </xsl:attribute>
              </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="counts/right"/> right, <xsl:value-of select="counts/wrong"/> wrong, <xsl:value-of select="counts/ignores"/> ignored, <xsl:value-of select="counts/exceptions"/> exceptions</span>
          
          <a>
            <xsl:attribute name="href">
              <xsl:text>#</xsl:text>
              <xsl:value-of select="relativePageName"/>
              <xsl:value-of select="position()"/>
            </xsl:attribute>
            <xsl:value-of select="relativePageName"/>
          </a>
        </div>
      </xsl:for-each>
      
    </div>

    <div id="FitNesseDetails">
      <xsl:for-each select="/testResults/result">
        <br/>
        <br/>
        <div class="sectionheader-container">
          <a>
            <xsl:attribute name="id">
              <xsl:value-of select="relativePageName"/>
              <xsl:value-of select="position()"/>
            </xsl:attribute>
            <xsl:attribute name="href">
              <xsl:value-of select="$Url"/>
              <xsl:text>.</xsl:text>
              <xsl:value-of select="relativePageName"/>
            </xsl:attribute>
            <xsl:value-of select="relativePageName"/>
          </a>
        </div>

        <div>
          <xsl:copy-of select="content/*"/>
        </div>
      </xsl:for-each>
    </div>
  </xsl:template>

</xsl:stylesheet>
