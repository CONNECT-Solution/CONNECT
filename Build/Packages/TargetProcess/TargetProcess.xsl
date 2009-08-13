<?xml version="1.0"?>
<!DOCTYPE stylesheet [
  <!ENTITY % entities SYSTEM "..\..\Entities.xml">

  %entities;
]>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="html"/>

  <xsl:template match="/">

    <table class="section-table" cellpadding="2" cellspacing="0" border="0" width="98%">
			<tbody>
				<tr>
					<td height="42" class="sectionheader-container">
						<img src="http://&HostName;/&ProjectName;-&ProjectCodeLineName;/Packages/TargetProcess/logo.gif" class="sectionheader-title-image" />
						<div class="sectionheader"  >
							TargetProcess Associated Items (<xsl:value-of select="count(//TargetProcess/Entity)"/>)
						</div>
					</td>
				</tr>
				<xsl:call-template name="Entities">
					<xsl:with-param name="Entities" select="//TargetProcess/Entity" />
				</xsl:call-template>
			</tbody>
    </table>
  </xsl:template>

  <xsl:template name="Entities">
    <xsl:param name="Entities"/>
    <xsl:for-each select="$Entities">
			<tr>
				<td class="section-data-bold">
					<span >
						<a style="color: 403F8D; font-size: 11px; font-weight: bold;" onmouseover="this.style.color = '#7bcf15'" onmouseout="this.style.color = '#403F8D'">
							<xsl:attribute name="href">
								<xsl:value-of select="@HyperLink" />
							</xsl:attribute>
							<xsl:value-of select="@Type" /> # <xsl:value-of select="@Id" /> - <xsl:value-of select="@Name" />
						</a>
					</span>
				</td>
			</tr>
			<tr>
				<td class="section-data">
					<div>
						<a style="color: 7bcf15; TEXT-DECORATION: NONE;" href="javascript:void(0)" class="dsphead" onclick="dsp(this, '+ Click to Show Description', '+ Click to Hide Description')">
							<span class="dspchar">+ Click to Show Description</span>
						</a>
					</div>
					<div class="dspcont">
						<xsl:choose>
							<xsl:when test="text() = '' and boolean(child::*)">
								<xsl:value-of select="'No Description Available'"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="boolean(child::*)">
										<xsl:copy-of select="Description/child::*" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="Description/text()" />
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</div>
				</td>
			</tr>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>