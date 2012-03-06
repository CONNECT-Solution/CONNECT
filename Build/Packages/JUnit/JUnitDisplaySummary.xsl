<?xml version="1.0"?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "..\..\Entities.xml">

  %entities;
]>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt"
    xmlns:ms="urn:Scripts"
    exclude-result-prefixes="ms msxsl"
>
  <msxsl:script implements-prefix="ms" language="C#">
    <![CDATA[
    
    public bool FileExists(string filePath)
    {
      return System.IO.File.Exists(filePath);
    }
    
    public XPathNavigator ProcessAlertScripts(XPathNavigator navigator, String junitSummaryXslPath)
    {
        XslCompiledTransform transform = NewXslTransform(junitSummaryXslPath);
        StringBuilder buffer = new StringBuilder();
        XmlWriter xmlWriter = XmlWriter.Create(buffer);
        try
        {
          transform.Transform(navigator, xmlWriter);
        }
        catch (Exception ex)
        {
          System.Diagnostics.Debug.WriteLine(ex.Message);
        }

        XmlDocument reportPart = new XmlDocument();
        reportPart.LoadXml(buffer.ToString());
        
        return reportPart.DocumentElement.CreateNavigator();
    }

    private static XslCompiledTransform NewXslTransform(string transformerFileName)
    {
        XslCompiledTransform transform = new XslCompiledTransform();
        LoadStylesheet(transform, transformerFileName);
        return transform;
    }

    private static XsltArgumentList CreateXsltArgs(System.Collections.Generic.Dictionary<string, string> xsltArgs)
    {
        XsltArgumentList args = new XsltArgumentList();
        if (xsltArgs != null)
        {
            foreach (string key in xsltArgs.Keys)
            {
                args.AddParam(key.ToString(), "", xsltArgs[key]);
            }
        }
        return args;
    }

    private static void LoadStylesheet(XslCompiledTransform transform, string xslFileName)
    {
        XsltSettings settings = new XsltSettings(true, true);

        try
        {
            XmlReaderSettings readerSettings = new XmlReaderSettings();
            readerSettings.ProhibitDtd = false;
            readerSettings.ConformanceLevel = ConformanceLevel.Fragment;
            XmlReader xslReader = XmlReader.Create(xslFileName, readerSettings);
            transform.Load(xslReader, settings, new XmlUrlResolver());
        }
        catch
        {
            throw new Exception(string.Format("XSL stylesheet file not found: {0}", xslFileName));
        }
    }
    
    ]]>
  </msxsl:script>


  <xsl:output method = "xml" omit-xml-declaration="yes"/>

  <xsl:param name = "applicationPath"/>
  <xsl:param name="CCNetServer"/>
  <xsl:param name="CCNetProject"/>
  <xsl:param name="CCNetBuild"/>

  <xsl:template match = "/">
    <xsl:if test="boolean(//junitsummary)">
      <xsl:call-template name="show">
        <xsl:with-param name="stuff" select="//junitsummary"/>
      </xsl:call-template>
    </xsl:if>
    
    <xsl:if test="not(boolean(//junitsummary)) and boolean(//testsuite)">
      <xsl:call-template name="show">
        <xsl:with-param name="stuff" select="ms:ProcessAlertScripts(/, '&Common.Directory.Packages.Path;\JUnit\JUnitSummary.xsl')"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>


  <xsl:template name="show">
    <xsl:param name="stuff"/>

    <table
          class = "section-table"
          cellSpacing = "0"
          cellPadding = "2"
          width = "98%"
          border = "0">
      <tr>
        <td height="42" colSpan="2">
          <xsl:attribute name="class">
            <xsl:choose>
              <xsl:when test="$stuff/@failures > 0">
                <xsl:text>sectionheader-container-error"</xsl:text>
              </xsl:when>
              <xsl:otherwise>
                <xsl:text>sectionheader-container"</xsl:text>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:attribute>
          <a>
            <xsl:attribute name="style">
              <xsl:text>TEXT-DECORATION: NONE;</xsl:text>
              <xsl:choose>
                <xsl:when test="$stuff/@failures > 0">
                  <xsl:text>color: #D13535;</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:text>color: #403F8D;</xsl:text>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="onmouseover">
              <xsl:text>this.style.color = </xsl:text>
              <xsl:choose>
                <xsl:when test="$stuff/@failures > 0">
                  <xsl:text>'#403F8D'</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:text>'#7bcf15'</xsl:text>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="onmouseout">
              <xsl:text>this.style.color = </xsl:text>
              <xsl:choose>
                <xsl:when test="$stuff/@failures > 0">
                  <xsl:text>'#D13535'</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:text>'#403F8D'</xsl:text>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>

            <xsl:attribute name="href">
              http://&HostName;/&ProjectName;-&ProjectCodeLineName;/default.aspx?_action_UnitTestDetailsBuildReport=true&amp;server=<xsl:value-of select="$CCNetServer" />&amp;project=<xsl:value-of select="$CCNetProject" />&amp;build=<xsl:value-of select="$CCNetBuild" />
            </xsl:attribute>
            <img src="http://&HostName;/&ProjectName;-&ProjectCodeLineName;/Packages\JUnit\logo.gif" class="sectionheader-title-image"/>
            <div class="sectionheader-text">
              Unit Tests run: <xsl:value-of select="$stuff/@testcount"/>, Failures: <xsl:value-of select="$stuff/@failures"/>, Not run: <xsl:value-of select="$stuff/@notrun"/>, Time: <xsl:value-of select="$stuff/@time"/> seconds
            </div>
          </a>
        </td>
      </tr>
      <xsl:copy-of select="$stuff"/>
    </table>
  </xsl:template>
  
</xsl:stylesheet>
