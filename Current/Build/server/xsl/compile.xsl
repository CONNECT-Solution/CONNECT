<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html"/>

    <xsl:template match="/">

      <xsl:variable name="builderror.messages" select="/cruisecontrol//buildresults//message[(contains(text(), 'error ')) or @level='Error'] | /cruisecontrol//buildresults//builderror/message | /cruisecontrol//internalerror/message" />
      <xsl:variable name="builderror.count" select="count($builderror.messages)"/>

      <xsl:if test="$builderror.count > 0">
        <table class="section-table" cellpadding="2" cellspacing="0" border="0" width="98%">
          <tr>
            <td  height="42" class="sectionheader-container-error">
              <div class="sectionheader-text">
                Build Errors: (<xsl:value-of select="$builderror.count"/>)
              </div>
            </td>
          </tr>
          <tr>
            <td>
              <xsl:for-each select="$builderror.messages" >
                <pre class="section-error">
                  <xsl:value-of select="text()"/>
                </pre>
              </xsl:for-each>
            </td>
          </tr>
        </table>
      </xsl:if>


      <xsl:variable name="warning.messages" select="/cruisecontrol//buildresults//message[(contains(text(), 'warn ')) or @level='Warning']" />
      <xsl:variable name="warning.count" select="count($warning.messages)"/>

      <xsl:if test="$warning.count > 0">
        <table class="section-table" cellpadding="2" cellspacing="0" border="0" width="98%">
          <tr>
            <td  height="42" class="sectionheader-container-warning">
              <div class="sectionheader-text">
                Build Warnings: (<xsl:value-of select="$warning.count"/>)
              </div>
            </td>
          </tr>
          <tr>
            <td>
              <div>
                <a href="javascript:void(0)" class="dsphead" onclick="dsp(this, '+ Show Warnings', '+ Hide Warnings')">
                  <span class="dspchar">+ Show Warnings</span>
                </a>
              </div>
              <div class="dspcont">
                <xsl:for-each select="$warning.messages" >
                  <pre class="section-warning">
                    <xsl:value-of select="text()"/>
                  </pre>
                </xsl:for-each>
              </div>
            </td>
          </tr>
        </table>
      </xsl:if>

    </xsl:template>


</xsl:stylesheet>
