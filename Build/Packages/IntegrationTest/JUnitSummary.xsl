<?xml version="1.0"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="xml" omit-xml-declaration="yes"/>
	
    <xsl:variable name="junit.suite.list" select="//testsuite"/>
    <xsl:variable name="junit.case.list" select="$junit.suite.list/testcase"/>
    <xsl:variable name="junit.case.count" select="count($junit.case.list)"/>
    <xsl:variable name="junit.time" select="sum($junit.suite.list/@time)"/>
    <xsl:variable name="junit.failure.list" select="$junit.case.list/failure"/>
    <xsl:variable name="junit.failure.count" select="count($junit.failure.list)"/>
    <xsl:variable name="junit.error.list" select="$junit.case.list/error"/>
    <xsl:variable name="junit.error.count" select="count($junit.error.list)"/>

    <xsl:template match="/">
      <integrationtestsummary>
        <xsl:attribute name="testcount">
          <xsl:value-of select="$junit.case.count"/>
        </xsl:attribute>
        <xsl:attribute name="failures">
          <xsl:value-of select="$junit.failure.count + $junit.error.count"/>
        </xsl:attribute>
        <xsl:attribute name="time">
          <xsl:value-of select="$junit.time"/>
        </xsl:attribute>

        <xsl:choose>
          <xsl:when test="$junit.case.count = 0">
            <tr>
              <td colspan="2" class="section-data">No  Integration Tests Run</td>
            </tr>
            <tr>
              <td colspan="2" class="section-error">This project doesn't have any Integration tests</td>
            </tr>
          </xsl:when>

          <xsl:when test="$junit.failure.count + $junit.error.count = 0">
            <tr>
              <td colspan="2" class="section-data">All Integration Tests Passed</td>
            </tr>
          </xsl:when>
        </xsl:choose>

        <xsl:apply-templates select="$junit.error.list"/>
        <xsl:apply-templates select="$junit.failure.list"/>

        <tr>
          <td colspan="2"> </td>
        </tr>

        <xsl:if test="$junit.failure.count + $junit.error.count > 0">
          <tr>
            <td class="sectionheader-container-error" colspan="2">
              Integration Test Failure and Error Details (<xsl:value-of select="$junit.failure.count + $junit.error.count"/>)
            </td>
          </tr>

          <xsl:call-template name="junittestdetail">
            <xsl:with-param name="detailnodes" select="//testsuite/testcase[.//error]"/>
          </xsl:call-template>

          <xsl:call-template name="junittestdetail">
            <xsl:with-param name="detailnodes" select="//testsuite/testcase[.//failure]"/>
          </xsl:call-template>

          <tr>
            <td colspan="2"> </td>
          </tr>
        </xsl:if>
      </integrationtestsummary>
    </xsl:template>

    <xsl:template match="error">
        <tr>
            <xsl:if test="position() mod 2 = 0">
                <xsl:attribute name="class">section-oddrow</xsl:attribute>
            </xsl:if>
            <td class="section-data">Error</td>
            <td class="section-data"><xsl:value-of select="../@name"/></td>
        </tr>
    </xsl:template>

    <xsl:template match="failure">
        <tr>
            <xsl:if test="($junit.error.count + position()) mod 2 = 0">
                <xsl:attribute name="class">section-oddrow</xsl:attribute>
            </xsl:if>
            <td class="section-data">Failure</td>
            <td class="section-data"><xsl:value-of select="../@name"/></td>
        </tr>
    </xsl:template>

    <xsl:template match="reason">
        <tr>
            <xsl:if test="($junit.failure.count + $junit.error.count + position()) mod 2 = 0">
                <xsl:attribute name="class">section-oddrow</xsl:attribute>
            </xsl:if>
            <td class="section-data">Warning</td>
            <td class="section-data"><xsl:value-of select="../@name"/></td>
        </tr>
    </xsl:template>

    <xsl:template name="junittestdetail">
      <xsl:param name="detailnodes"/>

      <xsl:for-each select="$detailnodes">

        <tr><td class="section-data">IntegrationTest:</td><td class="section-data"><xsl:value-of select="@name"/></td></tr>
       
        <xsl:if test="error">
        <tr><td class="section-data">Type:</td><td class="section-data">Error</td></tr>
        <tr><td class="section-data">Message:</td><td class="section-data"><xsl:value-of select="error/@message"/></td></tr>
        <tr>
            <td></td>
            <td class="section-error">
                <pre><xsl:call-template name="br-replace">
                        <xsl:with-param name="word" select="error"/>
                    </xsl:call-template></pre>
            </td>
        </tr>
        </xsl:if>

        <xsl:if test="failure">
        <tr><td class="section-data">Type:</td><td class="section-data">Failure</td></tr>
        <tr><td class="section-data">Message:</td><td class="section-data"><xsl:value-of select="failure/@message"/></td></tr>
        <tr>
            <td></td>
            <td class="section-error">
                <pre><xsl:call-template name="br-replace">
                        <xsl:with-param name="word" select="failure"/>
                    </xsl:call-template></pre>
            </td>
        </tr>
        </xsl:if>

        <tr><td colspan="2"><hr size="1" width="100%" color="#888888"/></td></tr>
        
      </xsl:for-each>
    </xsl:template>

    <xsl:template name="br-replace">
        <xsl:param name="word"/>
        <xsl:variable name="cr"><xsl:text>
        <!-- </xsl:text> on next line on purpose to get newline -->
        </xsl:text></xsl:variable>
        <xsl:choose>
            <xsl:when test="contains($word,$cr)">
                <xsl:value-of select="substring-before($word,$cr)"/>
                <br/>
                <xsl:call-template name="br-replace">
                    <xsl:with-param name="word" select="substring-after($word,$cr)"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$word"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
