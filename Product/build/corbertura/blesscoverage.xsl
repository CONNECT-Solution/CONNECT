<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="xml" indent="yes" />
  
  <xsl:template match="/">
    <xsl:element name="thresholds">
      <xsl:apply-templates select="//threshold-error" />
      <xsl:apply-templates select="//threshold" />
    </xsl:element>
  </xsl:template>

  <xsl:template match="threshold-error">
    <xsl:element name="threshold">
	     <xsl:attribute name="branchcount">
	      <xsl:copy-of select="@branchcount" />
	    </xsl:attribute>
	
      <xsl:attribute name="branchrate">
         <xsl:value-of select="@actualBrnchRate" />
       </xsl:attribute>
       
      <xsl:attribute name="branchsexecuted">
         <xsl:value-of select="@actualBrnchExecuted" />
       </xsl:attribute>
	  
	    <xsl:attribute name="class">
	      <xsl:value-of select="@class" />
	    </xsl:attribute>            
	
	    <xsl:attribute name="linecount">
	      <xsl:copy-of select="@linecount" />
	    </xsl:attribute>
	
      <xsl:attribute name="linerate">
        <xsl:value-of select="@actualLineRate" />
      </xsl:attribute>
      
      <xsl:attribute name="linesexecuted">
        <xsl:value-of select="@actualLineExecuted" />
      </xsl:attribute>    
    </xsl:element>
  </xsl:template>

  <xsl:template match="threshold">
    <xsl:element name="threshold">
       <xsl:attribute name="branchcount">
        <xsl:copy-of select="@branchcount" />
      </xsl:attribute>
  
      <xsl:attribute name="branchrate">
         <xsl:value-of select="@actualBrnchRate" />
       </xsl:attribute>
       
      <xsl:attribute name="branchsexecuted">
         <xsl:value-of select="@actualBrnchExecuted" />
       </xsl:attribute>
    
      <xsl:attribute name="class">
        <xsl:value-of select="@class" />
      </xsl:attribute>            
  
      <xsl:attribute name="linecount">
        <xsl:copy-of select="@linecount" />
      </xsl:attribute>
  
      <xsl:attribute name="linerate">
        <xsl:value-of select="@actualLineRate" />
      </xsl:attribute>
      
      <xsl:attribute name="linesexecuted">
        <xsl:value-of select="@actualLineExecuted" />
      </xsl:attribute>    
    </xsl:element>
  </xsl:template>  
</xsl:stylesheet>