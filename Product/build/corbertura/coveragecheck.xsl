<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:fn="fn" exclude-result-prefixes="xs fn">
  <xsl:output method="xml" indent="yes" />

  <xsl:template match="/">
    <xsl:element name="thresholds">
      <xsl:apply-templates select="//class">
        <xsl:sort select="@name"/>
      </xsl:apply-templates>
    </xsl:element>
  </xsl:template>

   <xsl:variable name="set.coverage.threshold.linerate" select="0.8" />
   <xsl:variable name="set.coverage.threshold.branchrate" select="0.8" />

  <xsl:template match="class">
    <xsl:variable name="threshold" select="/root/thresholds/threshold[@class = current()/@name]" />
    <xsl:variable name="current.branchcount" select="count(lines/line/conditions/condition) * 2" />
    <xsl:variable name="current.linecount" select="count(lines/line)" />
    <xsl:variable name="current.linerate" select="@line-rate" />
    <xsl:variable name="current.branchrate" select="@branch-rate" />
    <xsl:variable name="threshold.file.linecount" select="$threshold/@linecount" />
    <xsl:variable name="threshold.file.linesexecuted" select="$threshold/@linesexecuted" />
    <xsl:variable name="threshold.file.branchcount" select="$threshold/@branchcount" />
    <xsl:variable name="threshold.file.branchsexecuted" select="$threshold/@branchsexecuted" />
    <xsl:variable name="threshold.file.branchsrate" select="$threshold/@branchrate" />
    <xsl:variable name="name" select="@name" />

  <xsl:choose>
      <xsl:when test="count($threshold/@class) > 0">
      <xsl:choose>
        <xsl:when test="($threshold.file.branchcount != $current.branchcount) or ($threshold.file.linecount != $current.linecount)">
          <xsl:element name="threshold-error">        
            <xsl:call-template name="classNew">
              <xsl:with-param name="current.branchcount" select="$current.branchcount" />
              <xsl:with-param name="current.linecount" select="$current.linecount" />
              <xsl:with-param name="coverage.threshold.linerate" select="$set.coverage.threshold.linerate" />
              <xsl:with-param name="coverage.threshold.branchrate" select="$set.coverage.threshold.branchrate" />
              <xsl:with-param name="current.linerate" select="$current.linerate" />
              <xsl:with-param name="current.branchrate" select="$current.branchrate" />
              <xsl:with-param name="threshold.linecount" select="$threshold.file.linecount" />
              <xsl:with-param name="threshold.linesexecuted" select="$threshold.file.linesexecuted" />
              <xsl:with-param name="threshold.branchcount" select="$threshold.file.branchcount" />
              <xsl:with-param name="threshold.branchsexecuted" select="$threshold.file.branchsexecuted" />
              <xsl:with-param name="name" select="$name" />
              <xsl:with-param name="file.branchsexecuted" select="$threshold.file.branchsexecuted" />
              <xsl:with-param name="file.linesexecuted" select="$threshold.file.linesexecuted" />
              <xsl:with-param name="file.branchsrate" select="$threshold.file.branchsrate" />
            </xsl:call-template>
          </xsl:element>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="threshold">        
            <xsl:call-template name="classNew">
              <xsl:with-param name="current.branchcount" select="$current.branchcount" />
              <xsl:with-param name="current.linecount" select="$current.linecount" />
              <xsl:with-param name="coverage.threshold.linerate" select="$set.coverage.threshold.linerate" />
              <xsl:with-param name="coverage.threshold.branchrate" select="$set.coverage.threshold.branchrate" />
              <xsl:with-param name="current.linerate" select="$current.linerate" />
              <xsl:with-param name="current.branchrate" select="$current.branchrate" />
              <xsl:with-param name="threshold.linecount" select="$threshold.file.linecount" />
              <xsl:with-param name="threshold.linesexecuted" select="$threshold.file.linesexecuted" />
              <xsl:with-param name="threshold.branchcount" select="$threshold.file.branchcount" />
              <xsl:with-param name="threshold.branchsexecuted" select="$threshold.file.branchsexecuted" />
              <xsl:with-param name="name" select="$name" />
              <xsl:with-param name="file.branchsexecuted" select="$threshold.file.branchsexecuted" />
              <xsl:with-param name="file.linesexecuted" select="$threshold.file.linesexecuted" />
              <xsl:with-param name="file.branchsrate" select="$threshold.file.branchsrate" />              
            </xsl:call-template>        
          </xsl:element>
        </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="($current.linerate &lt; $set.coverage.threshold.linerate) or ($current.branchrate &lt; $set.coverage.threshold.branchrate)" >    
        <xsl:element name="threshold-error-new">
          <xsl:call-template name="classNew">
            <xsl:with-param name="current.branchcount" select="$current.branchcount" />
            <xsl:with-param name="current.linecount" select="$current.linecount" />
            <xsl:with-param name="coverage.threshold.linerate" select="$set.coverage.threshold.linerate" />
            <xsl:with-param name="coverage.threshold.branchrate" select="$set.coverage.threshold.branchrate" />
            <xsl:with-param name="current.linerate" select="$current.linerate" />
            <xsl:with-param name="current.branchrate" select="$current.branchrate" />
            <xsl:with-param name="threshold.linecount" select="$threshold.file.linecount" />
            <xsl:with-param name="threshold.linesexecuted" select="0.0" />
            <xsl:with-param name="threshold.branchcount" select="$current.branchcount" />
            <xsl:with-param name="threshold.branchsexecuted" select="$threshold.file.branchsexecuted" />
            <xsl:with-param name="name" select="$name" />
             <xsl:with-param name="file.branchsexecuted" select="$threshold.file.branchsexecuted" />
             <xsl:with-param name="file.linesexecuted" select="$threshold.file.linesexecuted" />
             <xsl:with-param name="file.branchsrate" select="$threshold.file.branchsrate" />
          </xsl:call-template>
        </xsl:element>
      </xsl:when>
      <xsl:otherwise/>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="classNew">
    <xsl:param name="current.branchcount" />
    <xsl:param name="current.linecount" />
    <xsl:param name="coverage.threshold.linerate" />
    <xsl:param name="coverage.threshold.branchrate" />
    <xsl:param name="current.linerate" />
    <xsl:param name="current.branchrate" />
    <xsl:param name="threshold.linecount" />
    <xsl:param name="threshold.linesexecuted" />
    <xsl:param name="threshold.branchcount" />
    <xsl:param name="threshold.branchsexecuted" />
    <xsl:param name="name" select="@name" />
    <xsl:param name="file.branchsexecuted"/>
    <xsl:param name="file.linesexecuted"/>
    <xsl:param name="file.branchsrate" />
    
    

    <xsl:variable name="newBranchesExecuted" select="(((($current.branchcount - $threshold.branchcount)*$coverage.threshold.branchrate) div 100) + $threshold.branchsexecuted)" />
    <xsl:variable name="newLinesExecuted" select="(((($current.linecount - $threshold.linecount)*$coverage.threshold.linerate) div 100) + $threshold.linesexecuted)" />

    <xsl:attribute name="branchcount">
      <xsl:copy-of select="$current.branchcount" />
    </xsl:attribute>

    <xsl:choose>
			<xsl:when test="$threshold.branchcount != $current.branchcount">
				<xsl:attribute name="branchrate">
		       <xsl:value-of select="$newBranchesExecuted div $current.branchcount" />
		     </xsl:attribute>
				<xsl:attribute name="branchsexecuted">
		       <xsl:value-of select="$newBranchesExecuted" />
		     </xsl:attribute>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="branchrate">
		       <xsl:value-of select="$current.branchrate" />
		     </xsl:attribute>
				<xsl:attribute name="branchsexecuted">
		       <xsl:value-of select="($threshold.branchcount * $current.branchrate) div 100" />
		     </xsl:attribute>
			</xsl:otherwise>
		</xsl:choose>
  
    <xsl:attribute name="class">
      <xsl:value-of select="$name" />
    </xsl:attribute>            

    <xsl:attribute name="linecount">
      <xsl:copy-of select="$current.linecount" />
    </xsl:attribute>

    <xsl:variable name="newLinesExecuted" select="(((($current.linecount - $threshold.linecount)*$coverage.threshold.linerate) div 100) + $threshold.linesexecuted)" />
    
    <xsl:choose>
      <xsl:when test="$threshold.linecount != $current.linecount">
		    <xsl:attribute name="linerate">
		      <xsl:value-of select="$newLinesExecuted div $current.linecount" />
		    </xsl:attribute>
		    <xsl:attribute name="linesexecuted">
		      <xsl:value-of select="$newLinesExecuted" />
		    </xsl:attribute>    
      </xsl:when>
      <xsl:otherwise>
        <xsl:attribute name="linerate">
          <xsl:value-of select="$current.linerate" />
        </xsl:attribute>
        <xsl:attribute name="linesexecuted">
          <xsl:value-of select="($threshold.linecount * $threshold.linecount) div 100" />
        </xsl:attribute>    
      </xsl:otherwise>
    </xsl:choose>

  </xsl:template>
  
</xsl:stylesheet>