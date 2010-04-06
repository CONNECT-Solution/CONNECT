<?xml version="1.0"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="xml" omit-xml-declaration="yes"/>
	
    <xsl:variable name="junit.suite.list" select="//testsuite"/>
    <xsl:variable name="junit.case.count" select="sum($junit.suite.list/@tests)"/>
    <xsl:variable name="junit.time" select="sum($junit.suite.list/@time)"/>
    <xsl:variable name="junit.failure.list" select="$junit.suite.list//failure"/>
    <xsl:variable name="junit.failure.count" select="count($junit.failure.list)"/>
    <xsl:variable name="junit.error.list" select="$junit.suite.list//error"/>
    <xsl:variable name="junit.error.count" select="count($junit.error.list)"/>

    <xsl:variable name="total.time" select="$junit.time"/>
    <xsl:variable name="total.run.count" select="$junit.case.count"/>
    <xsl:variable name="total.failure.count" select="$junit.failure.count + $junit.error.count"/>

    <xsl:template match="/">
      <junitsummary>
        <xsl:attribute name="testcount">
          <xsl:value-of select="$total.run.count"/>
        </xsl:attribute>
        <xsl:attribute name="failures">
          <xsl:value-of select="$total.failure.count"/>
        </xsl:attribute>
        <xsl:attribute name="notrun">
          <xsl:value-of select="0"/>
        </xsl:attribute>
        <xsl:attribute name="time">
          <xsl:value-of select="$total.time"/>
        </xsl:attribute>

        <xsl:choose>
          <xsl:when test="$total.run.count = 0">
            <tr>
              <td colspan="2" class="section-data">No Tests Run</td>
            </tr>
            <tr>
              <td colspan="2" class="section-error">This project doesn't have any tests</td>
            </tr>
          </xsl:when>

          <xsl:when test="$total.failure.count = 0">
            <tr>
              <td colspan="2" class="section-data">All Tests Passed</td>
            </tr>
          </xsl:when>
        </xsl:choose>

        <xsl:apply-templates select="$junit.error.list"/>
        <xsl:apply-templates select="$junit.failure.list"/>

        <tr>
          <td colspan="2"> </td>
        </tr>

        <xsl:if test="$total.failure.count > 0">
          <tr>
            <td class="sectionheader-container-error" colspan="2">
              Unit Test Failure and Error Details (<xsl:value-of select="$total.failure.count"/>)
            </td>
          </tr>

          <xsl:call-template name="junittestdetail">
            <xsl:with-param name="detailnodes" select="//testsuite[.//error]"/>
          </xsl:call-template>

          <xsl:call-template name="junittestdetail">
            <xsl:with-param name="detailnodes" select="//testsuite[.//failure]"/>
          </xsl:call-template>

          <tr>
            <td colspan="2"> </td>
          </tr>
        </xsl:if>
      </junitsummary>
    </xsl:template>

    <!-- Unit Test Errors -->
    <xsl:template match="error">
        <tr>
            <xsl:if test="position() mod 2 = 0">
                <xsl:attribute name="class">section-oddrow</xsl:attribute>
            </xsl:if>
            <td class="section-data">Error</td>
            <td class="section-data"><xsl:value-of select="../@name"/></td>
        </tr>
    </xsl:template>

    <!-- Unit Test Failures -->
    <xsl:template match="failure">
        <tr>
            <xsl:if test="($junit.error.count + position()) mod 2 = 0">
                <xsl:attribute name="class">section-oddrow</xsl:attribute>
            </xsl:if>
            <td class="section-data">Failure</td>
            <td class="section-data"><xsl:value-of select="../@name"/></td>
        </tr>
    </xsl:template>

    <!-- Unit Test Warnings -->
    <xsl:template match="reason">
        <tr>
            <xsl:if test="($total.failure.count + position()) mod 2 = 0">
                <xsl:attribute name="class">section-oddrow</xsl:attribute>
            </xsl:if>
            <td class="section-data">Warning</td>
            <td class="section-data"><xsl:value-of select="../@name"/></td>
        </tr>
    </xsl:template>

    <!-- JUnit Test Errors And Failures Detail Template -->
    <xsl:template name="junittestdetail">
      <xsl:param name="detailnodes"/>

      <xsl:for-each select="$detailnodes">

        <tr><td class="section-data">Test:</td><td class="section-data"><xsl:value-of select="@name"/></td></tr>
       
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

    <!-- NUnit Test Failures And Warnings Detail Template -->
    <xsl:template name="nunit2testdetail">
        <xsl:param name="detailnodes"/>

        <xsl:for-each select="$detailnodes">
        
            <xsl:if test="failure">
            <tr><td class="section-data">Test:</td><td class="section-data"><xsl:value-of select="@name"/></td></tr>
            <tr><td class="section-data">Type:</td><td class="section-data">Failure</td></tr>
            <tr><td class="section-data">Message:</td><td class="section-data"><xsl:value-of select="failure//message"/></td></tr>
            <tr>
                <td></td>
                <td class="section-error">
                    <pre><xsl:value-of select="failure//stack-trace"/></pre>
                </td>
            </tr>
            </xsl:if>

            <xsl:if test="reason">
            <tr><td class="section-data">Test:</td><td class="section-data"><xsl:value-of select="@name"/></td></tr>
            <tr><td class="section-data">Type:</td><td class="section-data">Warning</td></tr>
            <tr><td class="section-data">Message:</td><td class="section-data"><xsl:value-of select="reason//message"/></td></tr>
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
