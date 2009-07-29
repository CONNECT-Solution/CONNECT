<?xml version="1.0"?>
<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:exsl="http://exslt.org/common" exclude-result-prefixes="exsl">

  <xsl:output method="html"/>

  <xsl:template match="/">
    <xsl:for-each select="/quiettimes/recoverytime[position() > last()-200 and 121 > @duration and (between/build[@possition = 1]/@hourofday > 7) and (18 > between/build[@possition = 2]/@hourofday)]">
      <number>
        <xsl:value-of select="position()"/>
      </number>
      <exsl:document href="Artifacts\RecoveryTimeHistoryDurations.xml" fragment="yes" append="yes" >
        <number>
          <xsl:value-of select="@duration"/>
        </number>
      </exsl:document>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
