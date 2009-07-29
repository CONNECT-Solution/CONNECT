using System;
using System.Xml;
using System.Xml.XPath;
using System.Xml.Xsl;
using System.Text;

public class Class1
{
    public XPathNavigator ProcessAlertScripts(String alertsFilePath)
    {
        String alertsRootDirPath = "";
        System.IO.DirectoryInfo alertsRootDir = new System.IO.DirectoryInfo(alertsRootDirPath);
        foreach (System.IO.DirectoryInfo alertScriptDir in alertsRootDir.GetDirectories())
        {
            XmlDocument document = new XmlDocument();
            document.LoadXml(document.CreateElement("Entity").OuterXml);
            XmlElement rootElement = document.DocumentElement;

            String summaryXslPath = System.IO.Path.Combine(alertScriptDir.FullName, alertScriptDir.Name + "Summary.xsl");
            if (System.IO.File.Exists(summaryXslPath))
            {
                XslCompiledTransform transform = NewXslTransform(summaryXslPath);
                StringBuilder buffer = new StringBuilder();
                XmlWriter xmlWriter = XmlWriter.Create(buffer);
                using (System.IO.StringReader inputReader = new System.IO.StringReader(alertsFilePath))
                {
                    XPathDocument navigator = new XPathDocument(inputReader);
                    transform.Transform(navigator, xmlWriter);
                    //transform.Transform(navigator, CreateXsltArgs(xslParams), buffer);
                }
                XmlDocument reportPart = new XmlDocument();
                reportPart.LoadXml(buffer.ToString());
                XmlNode node = document.ImportNode(reportPart.DocumentElement, true);
                rootElement.AppendChild(node);
            }

            return rootElement.CreateNavigator().SelectChildren(XPathNodeType.Element);
        }
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
            readerSettings.ConformanceLevel = ConformanceLevel.Auto;
            XmlReader xslReader = XmlReader.Create(xslFileName, readerSettings);
            transform.Load(xslReader, settings, new XmlUrlResolver());
        }
        catch (FileNotFoundException)
        {
            throw new CruiseControlException(string.Format("XSL stylesheet file not found: {0}", xslFileName));
        }
        catch (XsltException ex)
        {
            throw new CruiseControlException("Unable to load transform: " + xslFileName, ex);
        }
    }
}
