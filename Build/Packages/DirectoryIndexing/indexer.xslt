<!--
	- XSLT is a template based language to transform Xml documents
	It uses XPath to select specific nodes 
	for processing.
	
	- A XSLT file is a well formed Xml document
-->

<!-- every StyleSheet starts with this tag -->
<xsl:stylesheet
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
      version="1.0">

  <!-- indicates what our output type is going to be -->
  <xsl:output method="html" />


  <xsl:param name="param-shift-width" select="10" />
  <xsl:template match="/">

    <html>
      <head>
        <title>
          <xsl:value-of select="root/releaseinfo/projectname"/> Support CD
        </title>
      </head>
      <center>
        <table border="1" width="100%" height="124" bordercolordark="#3333CC" bordercolorlight="#6699FF">
          <tr>
            <td width="17%" height="118">
              <!--mstheme-->
              <font face="Verdana, Arial, Helvetica">
                <p align="center"/>
                <img    src="logo.png"    alt="logo.png (9116 bytes)" width="226px" height="185px"/>
                <!--mstheme-->
              </font>
            </td>
            <td width="65%" height="118">
              <!--mstheme-->
              <font face="Verdana, Arial, Helvetica">
                <p align="center"/>
                <font size="6">

                  <strong>
                    <xsl:value-of select="root/releaseinfo/title"/> (Build: <xsl:value-of select="root/releaseinfo/version"/>  )
                  </strong>
                  <br/>
                  <br/>

                </font>

                Release Date: <xsl:value-of select="root/releaseinfo/releasedate"/>

                <br/>
                <!--mstheme-->
              </font>
            </td>

          </tr>
        </table>
        <!--mstheme-->
      </center>

      <body>
        <p>
          <xsl:value-of select="root/releaseinfo/summary"/>
        </p>
        <h2>
          <xsl:value-of select="root/releaseinfo/tag"/>
        </h2>
        <br/>

        <!--
        <table border="1">
          <tr bgcolor="#9acd32">
            <th align="left">Title</th>
          </tr>
          <xsl:for-each select="root">
            <xsl:apply-templates select="directory"/>

          </xsl:for-each>
        </table>
-->
        <table border="0">
          <tr>
            <td>
              <xsl:apply-templates select="root">
                <xsl:with-param name="depth" select="1" />
              </xsl:apply-templates>
            </td>
          </tr>
        </table>

        <center>
          Auto-generated on <xsl:value-of select="root/releaseinfo/generateddate"/>
        </center>
      </body>

    </html>

  </xsl:template>
  <xsl:template match="root">
    <xsl:apply-templates select="directory">
      <xsl:with-param name="depth" select="1" />
    </xsl:apply-templates>
  </xsl:template>
  <xsl:template name="shift">
    <xsl:param name="shift" />
    <xsl:if test="$shift>1">
      <td width="{$param-shift-width} * $shift" ></td>
      <xsl:call-template name="shift">
        <xsl:with-param name="shift">
          <xsl:number value="$shift -1" />
        </xsl:with-param>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>


  <xsl:template match="directory">
    <xsl:param name="depth" />
    <table border="0" cellspacing="0" cellpadding="0">
      <tr>
        <!--  If first level of depth, do not shift of $param-shift-width
  -->
        <xsl:call-template name="shift">
          <xsl:with-param name="shift">
            <xsl:number value="$depth" />
          </xsl:with-param>
        </xsl:call-template>
        <xsl:variable name="relativePath" >
          <xsl:value-of select="@relpath"/>
        </xsl:variable>

        <td width="25">

          <a href="{$relativePath}">

            <xsl:value-of select="@name" />\
          </a>

        </td>
      </tr>

      <xsl:for-each select="file">
        <tr>
          <xsl:call-template name="shift">
            <xsl:with-param name="shift">
              <xsl:number value="$depth + 1" />
            </xsl:with-param>
          </xsl:call-template>
          <td>
            <xsl:value-of select="@name"/>
          </td>
        </tr>
      </xsl:for-each>

      <xsl:apply-templates select="directory">
        <xsl:with-param name="depth" select="$depth+1" />
      </xsl:apply-templates>



    </table>
  </xsl:template>
  <!--
  <xsl:template match="directory">
    <table>
    <tr>
      <td>
        <xsl:value-of select="@name"/>
      </td>
    </tr>
      <xsl:for-each select="file">
        <tr>
          <td></td>
        <td>
          <xsl:value-of select="@name"/>
        </td>
        </tr>
      </xsl:for-each>
    </table>
    <xsl:for-each select="../directory">
      <xsl:apply-templates select="directory"/>
      </xsl:for-each >
  </xsl:template>
-->
</xsl:stylesheet>
