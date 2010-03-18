<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="xml"/>

  <xsl:variable name="Threshold" select="'5000'"/>

  <xsl:template match="/">
    <script type="text/javascript">
      function toggleDiv(imgId, divId)
      {
      eDiv = document.getElementById(divId);
      eImg = document.getElementById(imgId);

      if ( eDiv.style.display == "none" )
      {
      eDiv.style.display="block";
      eImg.src="images/arrow_minus_small.gif";
      }
      else
      {
      eDiv.style.display = "none";
      eImg.src="images/arrow_plus_small.gif";
      }
      }
    </script>
    <div id="NAntTimingReport">
      <h1>Ant Project Timing Report</h1>

      <xsl:variable name="buildresults" select="//build/antbuildresults[target//task[@name='project']]" />
      <xsl:choose>
        <xsl:when test="count($buildresults) > 0">
          <xsl:apply-templates select="$buildresults" />
        </xsl:when>
        <xsl:otherwise>
          <h2>Log does not contain any Xml output from Ant.</h2>
          <p>
            Please make sure that Ant is executed using the XmlLogger (use the argument: <b>-logger com.agilex.ant.GoodXmlLogger</b>).
          </p>
        </xsl:otherwise>
      </xsl:choose>
    </div>
  </xsl:template>

  <xsl:template match="antbuildresults">
    <div id="Summary">
      <h3>Summary</h3>
      <table>
        <tbody>
          <tr>
            <td>Target:</td>
            <td>
              <xsl:value-of select="target[1]/@name"/>
            </td>
          </tr>
          <tr>
            <td>Total Build Time:</td>
            <td>
              <xsl:value-of select="format-number(duration div 1000,'##0.00')" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div id="Details">
      <h3>Details</h3>
      <table width="70%">
        <thead>
          <tr>
            <th align="left">Project</th>
            <th align="right">Duration (in seconds)</th>
          </tr>
        </thead>
        <tbody>
          <xsl:for-each select="target//task[@name='project']">
            <xsl:sort select="duration" order="descending" data-type="number" />
            <xsl:call-template name="project"/>
          </xsl:for-each>
        </tbody>
      </table>
    </div>
  </xsl:template>

  <xsl:template name="project">
    <tr>
      <td valign="top">
        <xsl:variable name="divId">
          <xsl:value-of select="generate-id()" />
        </xsl:variable>
        <img src="images/arrow_plus_small.gif" alt="Toggle to see targets and tasks in this project">
          <xsl:attribute name="id">
            <xsl:text>img-</xsl:text>
            <xsl:value-of select="$divId" />
          </xsl:attribute>
          <xsl:attribute name="onclick">
            toggleDiv('img-<xsl:value-of select="$divId" />','<xsl:value-of select="$divId" />')
          </xsl:attribute>
        </img>&#0160;
        <xsl:value-of select="task[@name='sequential']/task[@name='script']/message/name" /> - <xsl:value-of select="task[@name='sequential']/task[@name='script']/message/target" /> - <xsl:value-of select="task[@name='sequential']/task[@name='script']/message/order" />order
        <div>
          <xsl:attribute name="id">
            <xsl:value-of select="$divId" />
          </xsl:attribute>
          <xsl:attribute name="style">
            <xsl:text>display:none;</xsl:text>
          </xsl:attribute>
          <ul>
            <xsl:for-each select="./*">
              <xsl:apply-templates select="."/>
            </xsl:for-each>
          </ul>
        </div>
      </td>
      <td valign="top" align="right">
        <xsl:value-of select="format-number(duration div 1000,'##0.00')" />
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="target">
    <xsl:if test="$Threshold &lt; duration">
      <xsl:variable name="duration" select="format-number(duration div 1000,'##0.00')" />
      <li>
        <xsl:value-of select="@name" /> - <xsl:value-of select="$duration" /> seconds
        <ul>
          <xsl:for-each select="./*">
            <xsl:apply-templates select="."/>
          </xsl:for-each>
        </ul>
    </li>
    </xsl:if>
  </xsl:template>

  <xsl:template match="task">
    <xsl:if test="$Threshold &lt; duration">
      <xsl:variable name="duration" select="format-number(duration div 1000,'##0.00')" />
      <li>
        <xsl:value-of select="@name" /> - <xsl:value-of select="$duration" /> seconds
        <ul>
          <xsl:for-each select="./*">
            <xsl:apply-templates select="."/>
          </xsl:for-each>
        </ul>
      </li>
    </xsl:if>
  </xsl:template>

  <xsl:template match="duration"/>
  <xsl:template match="message"/>
</xsl:stylesheet>