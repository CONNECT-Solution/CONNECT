<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "..\..\Entities.xml">

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
    
    public XPathNodeIterator ProcessAlertScripts(XPathNavigator navigator, String alertsRootDirPath)
    {
        XmlDocument document = new XmlDocument();
        document.LoadXml(document.CreateElement("Entity").OuterXml);
        XmlElement rootElement = document.DocumentElement;
        
        System.IO.DirectoryInfo alertsRootDir = new System.IO.DirectoryInfo(alertsRootDirPath);
        foreach (System.IO.DirectoryInfo alertScriptDir in alertsRootDir.GetDirectories())
        {
            String summaryXslPath = System.IO.Path.Combine(alertScriptDir.FullName, alertScriptDir.Name + "Summary.xsl");
            if (System.IO.File.Exists(summaryXslPath))
            {
                XslCompiledTransform transform = NewXslTransform(summaryXslPath);
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
                //transform.Transform(navigator, CreateXsltArgs(xslParams), buffer);

                if (buffer.Length > 0){
                  XmlDocument reportPart = new XmlDocument();
                  reportPart.LoadXml(buffer.ToString());
                  XmlNode node = document.ImportNode(reportPart.DocumentElement, true);
                  rootElement.AppendChild(node);
                }
            }
        }
        return rootElement.CreateNavigator().SelectChildren(XPathNodeType.Element);
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

  <xsl:param name="ArtifactDirectoryPath" select="/cruisecontrol/build/buildresults//target[@name='Publish.SetUp']//target[@name='Publish.EchoArtifactPath']/task[@name='echo']/message"  />
  <!--<xsl:param name="ArtifactDirectoryPath" select="'C:\Temp'"  />-->
  <xsl:variable name="alertsFile" select="concat($ArtifactDirectoryPath, '\Alerts.xml')" />
  
  <xsl:template match="/">
    <xsl:if test="ms:FileExists($alertsFile)">
      <xsl:variable name="alertsdoc" select="document($alertsFile)"/>
      <xsl:if test="boolean(($alertsdoc)/alerts/child::*)">
        <div id="Alerts" style="border-style: dotted; border-color: #FF7700; border-width: 1.5px; float: right;">
        <div style="border-style: dotted; border-color: #FF7700; border-width: 1.5px;width: 100%; background-color: #FFDBBB;">
          <div style="font-weight:bold; color: #FF7700; font-size:18px; text-align: center;">Alerts!</div>

          <div style="margin: 4px;">
            <style type="text/css">
              #cluetip-title {
                overflow: hidden;
              }
              #cluetip-title #cluetip-close {
                float: right;
                position: relative;
              }
              .cluetip-default {
                background-color: #FFDBBB;
              }
              .cluetip-default #cluetip-outer {
                position: relative;
                margin: 0;
                background-color: #FFDBBB;
              }
              .cluetip-default h3#cluetip-title {
                margin: 0 0 5px;
                padding: 8px 10px 4px;
                font-size: 1.1em;
                font-weight: normal;
                background-color: #FF7700;
                color: #fff;
              }
              .cluetip-default #cluetip-title a {
                color: #FFDBBB;
                font-size: 0.95em;
              }
              .cluetip-default #cluetip-inner {
                padding: 10px;
              }
              .cluetip-default div#cluetip-close {
                text-align: right;
                margin: 0 5px 5px;
                color: #900;
              }

              #tooltip {
              position: absolute;
              z-index: 3000;
              border: 1px solid #111;
              background-color: #eee;
              padding: 5px;
              opacity: 0.85;
              }
              #tooltip h3, #tooltip div { margin: 0; }
              #tooltip.Alert-Positive-Tooltip
              {
              color: #267A17;
              border-style: dotted;
              border-color: #41B32D;
              border-width: 1.5px;
              }
              .Alert-Positive
              {
              color: #267A17;
              font-size: 14px;
              font-weight: bold;
              }
              #tooltip.Alert-Negative-Tooltip
              {
              color: #D13535;
              border-style: dotted;
              border-color: #D13535;
              border-width: 1.5px;
              }
              .Alert-Negative
              {
              color: #D13535;
              font-size: 14px;
              font-weight: bold;
              }
            </style>

            <xsl:variable name="AlertReport" select="ms:ProcessAlertScripts(($alertsdoc), '&Common.Directory.Packages.Path;\Alerts')" />
            <xsl:copy-of select="$AlertReport"/>

          </div>
        </div>
      </div>
      </xsl:if>
    </xsl:if>
  </xsl:template>
  
</xsl:stylesheet>
