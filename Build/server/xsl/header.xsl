<?xml version="1.0"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:output method="html"/>

    <xsl:template match="/">
      <script type="text/javascript">
        <![CDATA[
function dsp(loc, showMessage, hiddenMessage){
   if(document.getElementById){
      var foc = loc.firstChild;
      foc = loc.firstChild.innerHTML ? loc.firstChild : loc.firstChild.nextSibling;
      foc.innerHTML = foc.innerHTML == showMessage ? hiddenMessage : showMessage;
      foc = loc.parentNode.nextSibling.style ? loc.parentNode.nextSibling : loc.parentNode.nextSibling.nextSibling;
      foc.style.display = foc.style.display == 'block' ? 'none' : 'block';
    }
}  

if(!document.getElementById)
   document.write('<style type="text/css"><!--\n.dspcont{display:block;}\n//--></style>');

      ]]>
      </script>

      <noscript>
        <style type="text/css">
          .dspcont{display:block;}
        </style>
      </noscript>
      
        <xsl:variable name="modification.list" select="/cruisecontrol/modifications/modification"/>

        <table class="section-table" cellpadding="2" cellspacing="0" border="0">

            <xsl:if test="/cruisecontrol/exception">
                <tr><td class="header-title-error" colspan="2">BUILD EXCEPTION</td></tr>
                <tr>
                    <td class="header-label"><nobr>Error Message:</nobr></td>
                    <td class="header-data-error"><xsl:value-of select="/cruisecontrol/exception"/></td>
                </tr>
            </xsl:if>
            
            <xsl:if test="/cruisecontrol/build/@error">
                <tr><td class="header-title-error" colspan="2">BUILD FAILED</td></tr>
            </xsl:if>
            
            <xsl:if test="not (/cruisecontrol/build/@error) and not (/cruisecontrol/exception)">
                <tr><td class="header-title" colspan="2">BUILD SUCCESSFUL <xsl:value-of select="/cruisecontrol/build/@label"/> </td></tr>
            </xsl:if>

			<tr>
                <td class="header-label"><nobr>Project:</nobr></td>
                <td class="header-data"><xsl:value-of select="/cruisecontrol/@project"/></td>
			</tr>
            <tr>
                <td class="header-label"><nobr>Date of build:</nobr></td>
                <td class="header-data"><xsl:value-of select="/cruisecontrol/build/@date"/></td>
            </tr>
            <tr>
                <td class="header-label"><nobr>Running time:</nobr></td>
                <td class="header-data"><xsl:value-of select="/cruisecontrol/build/@buildtime"/></td>
            </tr>
            <tr>
                <td class="header-label"><nobr>Build condition:</nobr></td>
                <td class="header-data">
					<xsl:if test="/cruisecontrol/build/@buildcondition='ForceBuild'">
						Forced Build
					</xsl:if>
          <xsl:if test="/cruisecontrol/build/ForcedBuildInformation/@UserName != ''">
            By <xsl:value-of select="/cruisecontrol/build/ForcedBuildInformation/@UserName"/>
          </xsl:if>
					<xsl:if test="/cruisecontrol/build/@buildcondition='IfModificationExists'">
						Modifications Detected
					</xsl:if>
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
