<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:fn="fn"
  xmlns:con="con"
  exclude-result-prefixes="xs fn">
  <xsl:output method="xml" indent="yes" />

  <xsl:variable name="set.coverage.threshold.linerate" select="80" />
  <xsl:variable name="set.coverage.threshold.branchrate" select="80" />

  <xsl:function name="fn:getRate" as="xs:double">
    <xsl:param name="current.count" as="xs:double" />
    <xsl:param name="threshold.count" as="xs:double" />
    <xsl:param name="threshold.executed" as="xs:double" />    
    <xsl:value-of select="con:round((($current.count - $threshold.count) * ($set.coverage.threshold.linerate div 100)) + number($threshold.executed))" />    
  </xsl:function>
  
  <xsl:function name="fn:divValues" as="xs:double">
    <xsl:param name="cnt" as="xs:double" />
    <xsl:param name="rate" as="xs:double" />
    <xsl:value-of select="con:round((($cnt * $rate) div 100))" />    
  </xsl:function>

  <xsl:function name="con:round" as="xs:double">
    <xsl:param name="val" as="xs:double"/>
    <xsl:value-of select="(round($val *100) div 100)" />    
  </xsl:function>

  <xsl:function name="con:getPercnt" as="xs:double">
    <xsl:param name="val" as="xs:double"/>
    <xsl:value-of select="$val div 100" />    
  </xsl:function>
      
  <xsl:template match="/">
    <xsl:element name="thresholds">
      <xsl:apply-templates select="//class">
        <xsl:sort select="@name"/>
      </xsl:apply-templates>
    </xsl:element>
  </xsl:template>

  <xsl:template match="class">
    <xsl:variable name="threshold" select="/root/thresholds/threshold[@class = current()/@name]" />
    <xsl:variable name="current.branchcount" select="con:round(count(lines/line/conditions/condition) * 2)" />
    <xsl:variable name="current.linecount" select="con:round(count(lines/line))"/>
    <xsl:variable name="current.linerate" select="con:round(@line-rate)" />
    <xsl:variable name="current.branchrate" select="con:round(@branch-rate)" />
    <xsl:variable name="name" select="@name" />


  <xsl:choose>
      <xsl:when test="count($threshold/@class) > 0">
	    <xsl:variable name="threshold.file.linecount" select="$threshold/@linecount" />
	    <xsl:variable name="threshold.file.linerate" select="$threshold/@linerate" />
	    <xsl:variable name="threshold.file.linesexecuted" select="$threshold/@linesexecuted" />
	    <xsl:variable name="threshold.file.branchcount" select="$threshold/@branchcount" />
	    <xsl:variable name="threshold.file.branchsexecuted" select="$threshold/@branchsexecuted" />
	    <xsl:variable name="threshold.file.branchsrate" select="$threshold/@branchrate" />
	    
	    <xsl:variable name="newLinesExecuted" select="fn:getRate($current.linecount,$threshold.file.linecount, $threshold.file.linesexecuted)" as="xs:double" />
	    <xsl:variable name="newBranchesLinesExecuted" select="fn:getRate($current.branchcount,$threshold.file.branchcount, $threshold.file.branchsexecuted)" as="xs:double" />
	    <xsl:variable name="newRequiredLineRate" select="con:round($newLinesExecuted div $current.linecount)"/>
	    <xsl:variable name="newRequiredBranchRate" select="con:round($newBranchesLinesExecuted div $current.branchcount)"/>

      <xsl:choose>
        <xsl:when test="($current.branchrate &lt;  $newRequiredBranchRate) or ($current.linerate &lt; $newRequiredLineRate)">
          <xsl:element name="threshold-error">        
            <xsl:call-template name="classNew">
              <xsl:with-param name="branchcount" select="$current.branchcount" />
              <xsl:with-param name="branchrate" select="$current.branchrate"/>
              <xsl:with-param name="branchsexecuted" select="$newBranchesLinesExecuted" />
              <xsl:with-param name="name" select="$name" />
              <xsl:with-param name="linecount" select="$current.linecount" />
              <xsl:with-param name="linerate" select="$current.linerate"/>
              <xsl:with-param name="linesexecuted" select="$newLinesExecuted" />
              <xsl:with-param name="actualLineRate" select="$newRequiredLineRate" />
              <xsl:with-param name="actualBrnchRate" select="$newRequiredBranchRate" />
              <xsl:with-param name="actualBrnchExecuted" select="fn:divValues($threshold.file.branchcount,$threshold.file.branchsrate)" />
              <xsl:with-param name="actualLineExecuted" select="fn:divValues($threshold.file.linecount,$threshold.file.linerate)" />
            </xsl:call-template>
          </xsl:element>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="threshold">        
            <xsl:call-template name="classNew">
              <xsl:with-param name="branchcount" select="$current.branchcount" />
              <xsl:with-param name="branchrate" select="$current.branchrate" />
              <xsl:with-param name="branchsexecuted" select="$threshold.file.branchsexecuted" />
              <xsl:with-param name="name" select="$name" />
              <xsl:with-param name="linecount" select="$current.linecount" />
              <xsl:with-param name="linerate" select="$current.linerate" />
              <xsl:with-param name="linesexecuted" select="$threshold.file.linesexecuted" />
              <xsl:with-param name="actualLineRate" select="$set.coverage.threshold.branchrate" />
              <xsl:with-param name="actualBrnchRate" select="$set.coverage.threshold.branchrate" />
              <xsl:with-param name="actualBrnchExecuted" select="fn:divValues($threshold.file.branchcount,$threshold.file.branchsrate)" />
              <xsl:with-param name="actualLineExecuted" select="fn:divValues($threshold.file.linecount,$threshold.file.linerate)" />
            </xsl:call-template>        
          </xsl:element>
        </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="($current.linerate &lt; $set.coverage.threshold.linerate) or ($current.branchrate &lt; $set.coverage.threshold.branchrate)" >    
        <xsl:element name="threshold-error">
          <xsl:call-template name="classNew">
            <xsl:with-param name="branchcount" select="$current.branchcount" />
            <xsl:with-param name="branchrate" select="$current.branchrate" />
            <xsl:with-param name="branchsexecuted" select="fn:divValues($current.branchcount,$current.branchrate)" />
            <xsl:with-param name="name" select="$name" />
            <xsl:with-param name="linecount" select="$current.linecount" />
            <xsl:with-param name="linerate" select="$current.linerate" />
            <xsl:with-param name="linesexecuted" select="fn:divValues($current.linecount,$current.linerate)" />
             <xsl:with-param name="actualLineRate" select="$set.coverage.threshold.branchrate" />
             <xsl:with-param name="actualBrnchRate" select="$set.coverage.threshold.branchrate" />
             <xsl:with-param name="actualBrnchExecuted" select="fn:divValues($current.branchcount,$current.branchrate)" />
             <xsl:with-param name="actualLineExecuted" select="fn:divValues($current.linecount,$current.linerate)" />
          </xsl:call-template>
        </xsl:element>
      </xsl:when>
      <xsl:otherwise/>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="classNew">
    <xsl:param name="branchcount" />
    <xsl:param name="branchrate" />
    <xsl:param name="branchsexecuted" />
    <xsl:param name="name" select="@name" />
    <xsl:param name="linecount" />
    <xsl:param name="linerate" />
    <xsl:param name="linesexecuted" />
    <xsl:param name="actualLineRate"/>
    <xsl:param name="actualBrnchRate"/>
    <xsl:param name="actualBrnchExecuted"/>
    <xsl:param name="actualLineExecuted"/>
    
    <xsl:attribute name="branchcount">
      <xsl:copy-of select="$branchcount" />
    </xsl:attribute>

		<xsl:attribute name="branchrate">
       <xsl:value-of select="$branchrate" />
     </xsl:attribute>

		<xsl:attribute name="branchsexecuted">
       <xsl:value-of select="$branchsexecuted" />
     </xsl:attribute>
  
    <xsl:attribute name="class">
      <xsl:value-of select="$name" />
    </xsl:attribute>            

    <xsl:attribute name="linecount">
      <xsl:copy-of select="$linecount" />
    </xsl:attribute>

    <xsl:attribute name="linerate">
      <xsl:value-of select="$linerate" />
    </xsl:attribute>

    <xsl:attribute name="linesexecuted">
      <xsl:value-of select="$linesexecuted" />
    </xsl:attribute>    

    <xsl:attribute name="actualLineRate">
      <xsl:value-of select="$actualLineRate" />
    </xsl:attribute>    

    <xsl:attribute name="actualBrnchRate">
      <xsl:value-of select="$actualBrnchRate" />
    </xsl:attribute>    
    <xsl:attribute name="actualBrnchExecuted">
      <xsl:value-of select="$actualBrnchExecuted" />
    </xsl:attribute> 
        <xsl:attribute name="actualLineExecuted">
      <xsl:value-of select="$actualLineExecuted" />
    </xsl:attribute> 
  </xsl:template>
  
</xsl:stylesheet>