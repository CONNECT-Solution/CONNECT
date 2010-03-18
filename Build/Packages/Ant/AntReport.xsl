<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="xml" omit-xml-declaration="yes"/>

  <xsl:template match="/">
    <xsl:variable name="buildresults" select="//antbuildresults" />
    <xsl:choose>
      <xsl:when test="count($buildresults) > 0">
        <xsl:choose>
          <xsl:when test="$buildresults/failure">
            <h2>
              Build Failed (<a href="#failure">click to see error</a>)
            </h2>
          </xsl:when>
          <xsl:otherwise>
            <h2>Build Succeeded</h2>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:apply-templates select="$buildresults" />
      </xsl:when>
      <xsl:otherwise>
        <h2>Log does not contain any Xml output from Ant.</h2>
        <p>
          Please make sure that Ant is executed using the XmlLogger (use the argument: <b>-logger com.agilex.ant.GoodXmlLogger</b>).
        </p>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="antbuildresults">
    <hr/>
    <xsl:variable name="ProjectName" select="@project"/>
    <xsl:variable name="BuildFile" select="message[1]/text()"/>
    <h2>
      <xsl:text>Begin Project </xsl:text>
      <xsl:value-of select="$ProjectName"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="$BuildFile"/>
    </h2>
    <xsl:apply-templates />
    <h2>
      <xsl:text>End Project </xsl:text>
      <xsl:value-of select="$ProjectName"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="$BuildFile"/>
    </h2>
    <hr/>
  </xsl:template>

  <xsl:template match="target">
    <p>
      <xsl:value-of select="@name"/>:
    </p>
    <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="task">
    <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="message">
    <span>
      <xsl:if test="@level='Error'">
        <xsl:attribute name="class">error</xsl:attribute>
      </xsl:if>
      <xsl:if test="@level='Warning'">
        <xsl:attribute name="class">warning</xsl:attribute>
      </xsl:if>
      <xsl:if test="../@name != ''">
        [<xsl:value-of select="../@name"/>]
      </xsl:if>
      <xsl:value-of select="text()" />
    </span>
    <br />
  </xsl:template>

  <xsl:template match="failure">
    <a name="failure">
      <xsl:apply-templates />
    </a>
  </xsl:template>

  <xsl:template match="builderror">
    <br/>
    <span class="error">
      Build Error: <xsl:value-of select="type"/><br/>
      <xsl:value-of select="message"/><br/>
      <xsl:apply-templates select="location" />
      <pre>
        <xsl:value-of select="stacktrace" />
      </pre>
    </span>
  </xsl:template>

  <xsl:template match="internalerror">
    <br/>
    <span class="error">
      Internal Error: <xsl:value-of select="type"/><br/>
      <xsl:value-of select="message"/><br/>
      <xsl:apply-templates select="location" />
      <pre>
        <xsl:value-of select="stacktrace" />
      </pre>
    </span>
  </xsl:template>

  <xsl:template match="location">
    in <xsl:value-of select="filename"/>
    line: <xsl:value-of select="linenumber"/>
    col: <xsl:value-of select="columnnumber"/><br/>
  </xsl:template>

  <xsl:template match="duration" />
</xsl:stylesheet>
