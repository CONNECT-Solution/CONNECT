<?xml version="1.0"?>
<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:exsl="http://exslt.org/common"
  exclude-result-prefixes="exsl">

  <xsl:output method="html"/>
  
  <xsl:template match="/">
    <xsl:variable name="Last20" select="/quiettimes/quiettime[position() > last()-20 and '121' > @duration]"/>
    <xsl:variable name="Last200" select="/quiettimes/quiettime[position() > last()-200 and '121' > @duration]"/>
    <xsl:variable name="All" select="/quiettimes/quiettime['121' > @duration]"/>

    <null/>
    <string>
      <xsl:value-of select="concat('Last ', count($Last20), '/', count(/quiettimes/quiettime[position() > last()-20]))"/>
    </string>
    <string>
      <xsl:value-of select="concat('Last ', count($Last200), '/', count(/quiettimes/quiettime[position() > last()-200]))"/>
    </string>
    <string>
      <xsl:value-of select="concat('All ', count($All), '/', count(/quiettimes/quiettime))"/>
    </string>

    <exsl:document href="Artifacts\QuietTimeMaxHistoryDurations.xml" fragment="yes" append="yes">
      <string>
        <xsl:value-of select="'max'"/>
      </string>
    </exsl:document>

    <exsl:document href="Artifacts\QuietTimeMinHistoryDurations.xml" fragment="yes" append="yes">
      <string>
        <xsl:value-of select="'min'"/>
      </string>
    </exsl:document>

    <exsl:document href="Artifacts\QuietTimeOpenHistoryDurations.xml" fragment="yes" append="yes">
      <string>
        <xsl:value-of select="'open'"/>
      </string>
    </exsl:document>

    <exsl:document href="Artifacts\QuietTimeCloseHistoryDurations.xml" fragment="yes" append="yes">
      <string>
        <xsl:value-of select="'close'"/>
      </string>
    </exsl:document>

    <xsl:for-each select="$Last20">
      <xsl:sort select="@duration" order="descending" data-type="number"/>
      <xsl:choose>
        <xsl:when test="position() = 1">
          <exsl:document href="Artifacts\QuietTimeMaxHistoryDurations.xml" fragment="yes" append="yes">
            <number>
              <xsl:value-of select="@duration"/>
            </number>
          </exsl:document>
        </xsl:when>
        <xsl:when test="position() = last()">
          <exsl:document href="Artifacts\QuietTimeMinHistoryDurations.xml" fragment="yes" append="yes">
            <number>
              <xsl:value-of select="@duration"/>
            </number>
          </exsl:document>
        </xsl:when>
        <xsl:when test="position() = round(count($Last20) div 4)">
          <exsl:document href="Artifacts\QuietTimeOpenHistoryDurations.xml" fragment="yes" append="yes">
            <number>
              <xsl:value-of select="@duration"/>
            </number>
          </exsl:document>
        </xsl:when>
        <xsl:when test="position() = round(count($Last20) div 4) * 3">
          <exsl:document href="Artifacts\QuietTimeCloseHistoryDurations.xml" fragment="yes" append="yes">
            <number>
              <xsl:value-of select="@duration"/>
            </number>
          </exsl:document>
        </xsl:when>
      </xsl:choose>
    </xsl:for-each>

    <xsl:for-each select="$Last200">
      <xsl:sort select="@duration" order="descending" data-type="number"/>
      <xsl:choose>
        <xsl:when test="position() = 1">
          <exsl:document href="Artifacts\QuietTimeMaxHistoryDurations.xml" fragment="yes" append="yes">
            <number>
              <xsl:value-of select="@duration"/>
            </number>
          </exsl:document>
        </xsl:when>
        <xsl:when test="position() = last()">
          <exsl:document href="Artifacts\QuietTimeMinHistoryDurations.xml" fragment="yes" append="yes">
            <number>
              <xsl:value-of select="@duration"/>
            </number>
          </exsl:document>
        </xsl:when>
        <xsl:when test="position() = round(count($Last200) div 4)">
          <exsl:document href="Artifacts\QuietTimeOpenHistoryDurations.xml" fragment="yes" append="yes">
            <number>
              <xsl:value-of select="@duration"/>
            </number>
          </exsl:document>
        </xsl:when>
        <xsl:when test="position() = round(count($Last200) div 4) * 3">
          <exsl:document href="Artifacts\QuietTimeCloseHistoryDurations.xml" fragment="yes" append="yes">
            <number>
              <xsl:value-of select="@duration"/>
            </number>
          </exsl:document>
        </xsl:when>
      </xsl:choose>
    </xsl:for-each>

    <xsl:for-each select="$All">
      <xsl:sort select="@duration" order="descending" data-type="number"/>
      <xsl:choose>
        <xsl:when test="position() = 1">
          <exsl:document href="Artifacts\QuietTimeMaxHistoryDurations.xml" fragment="yes" append="yes">
            <number>
              <xsl:value-of select="@duration"/>
            </number>
          </exsl:document>
        </xsl:when>
        <xsl:when test="position() = last()">
          <exsl:document href="Artifacts\QuietTimeMinHistoryDurations.xml" fragment="yes" append="yes">
            <number>
              <xsl:value-of select="@duration"/>
            </number>
          </exsl:document>
        </xsl:when>
        <xsl:when test="position() = round(count($All) div 4)">
          <exsl:document href="Artifacts\QuietTimeOpenHistoryDurations.xml" fragment="yes" append="yes">
            <number>
              <xsl:value-of select="@duration"/>
            </number>
          </exsl:document>
        </xsl:when>
        <xsl:when test="position() = round(count($All) div 4) * 3">
          <exsl:document href="Artifacts\QuietTimeCloseHistoryDurations.xml" fragment="yes" append="yes">
            <number>
              <xsl:value-of select="@duration"/>
            </number>
          </exsl:document>
        </xsl:when>
      </xsl:choose>
    </xsl:for-each>

  </xsl:template>
  
</xsl:stylesheet>