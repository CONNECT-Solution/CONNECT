<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html"/>
	<xsl:template match="/">
		<script type="text/javascript">
			function toggleDiv(imgId, divId)
			{
			eDiv = document.getElementById(divId);
			eImg = document.getElementById(imgId);
			if ( eDiv.style.display == "none" )
			{
			eDiv.style.display="block";
			eImg.src="images/arrow_minus_small.gif";
			}
			else
			{
			eDiv.style.display = "none";
			eImg.src="images/arrow_plus_small.gif";
			}
			}
		</script>
		<div id="NAntTimingReport">
			<h1>NAnt Build Timing Report</h1>
			<xsl:variable name="buildresults" select="//build/build" />
			<xsl:choose>
				<xsl:when test="count($buildresults) > 0">
					<xsl:apply-templates select="$buildresults" />
				</xsl:when>
				<xsl:otherwise>
					<h2>Log does not contain any Xml output from Ant.</h2>
				</xsl:otherwise>
			</xsl:choose>
		</div>
	</xsl:template>
	<xsl:template match="build">
		<div id="Summary">
			<h3>Summary</h3>
			<table>
				<tbody>
					<tr>
						<td>Total Build Time:</td>
						<td>
							<xsl:value-of select="//build/build/@time"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="Details">
			<h3>Details</h3>
			<table width="70%">
				<thead>
					<tr>
						<th align="left">Target</th>
						<th align="right"></th>
					</tr>
				</thead>
				<tbody>
					<xsl:apply-templates select="target[number(substring(@time, 1, 2)) > 0 and contains(@time, 'minutes')]">
						<xsl:sort select="substring(@time, 1, 2)" order="descending" data-type="number"/>
					</xsl:apply-templates>
					<xsl:apply-templates select="target[number(substring(@time, 1, 2)) > 0 and not(contains(@time, 'minutes'))]">
						<xsl:sort select="substring(@time, 1, 2)" order="descending" data-type="number"/>
					</xsl:apply-templates>
				</tbody>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="target">
		<tr>
			<td valign="top">
				<xsl:variable name="divId">
					<xsl:value-of select="generate-id()" />
				</xsl:variable>
				<img src="images/arrow_plus_small.gif" alt="Toggle to see tasks in this target">
					<xsl:attribute name="id">
						<xsl:text>img-</xsl:text>
						<xsl:value-of select="$divId" />
					</xsl:attribute>
					<xsl:attribute name="onclick">
						toggleDiv('img-<xsl:value-of select="$divId" />','<xsl:value-of select="$divId" />')
					</xsl:attribute>
				</img>&#0160;
				<xsl:value-of select="@name" />
				<div>
					<xsl:attribute name="id">
						<xsl:value-of select="$divId" />
					</xsl:attribute>
					<xsl:attribute name="style">
						<xsl:text>display:none;</xsl:text>
					</xsl:attribute>
					<ul>
						<xsl:apply-templates select="task[number(substring(@time, 1, 2)) > 0 and contains(@time, 'minutes')]">
							<xsl:sort select="substring(@time, 1, 2)" order="descending" data-type="number"/>
						</xsl:apply-templates>
						<xsl:apply-templates select="task[number(substring(@time, 1, 2)) > 0 and not(contains(@time, 'minutes'))]">
							<xsl:sort select="substring(@time, 1, 2)" order="descending" data-type="number"/>
						</xsl:apply-templates>
					</ul>
				</div>
			</td>
			<td valign="top" align="right">
				<xsl:choose>
					<xsl:when test="substring(@time, 3, 1) = 'm'">
						<xsl:variable name="minutes" select="substring(@time, 1, 1)" />
						<xsl:variable name="seconds" select="substring(@time, 10, 2)" />
						<xsl:value-of select="$minutes * 60 + $seconds" />
					</xsl:when>
					<xsl:when test="substring(@time, 4, 1) = 'm'">
						<xsl:variable name="minutes" select="substring(@time, 1, 2)" />
						<xsl:variable name="seconds" select="substring(@time, 12, 2)" />
						<xsl:value-of select="$minutes * 60 + $seconds" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="seconds" select="substring(@time, 1, 2)" />
						<xsl:value-of select="$seconds" />
					</xsl:otherwise>
				</xsl:choose>
				- seconds
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="task">
		<li>
			<xsl:value-of select="@name" />
			<div style="text-align:right">
				<xsl:choose>
					<xsl:when test="substring(@time, 3, 1) = 'm'">
						<xsl:variable name="minutes" select="substring(@time, 1, 1)" />
						<xsl:variable name="seconds" select="substring(@time, 10, 2)" />
						<xsl:value-of select="$minutes * 60 + $seconds" />
					</xsl:when>
					<xsl:when test="substring(@time, 4, 1) = 'm'">
						<xsl:variable name="minutes" select="substring(@time, 1, 2)" />
						<xsl:variable name="seconds" select="substring(@time, 12, 2)" />
						<xsl:value-of select="$minutes * 60 + $seconds" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="seconds" select="substring(@time, 1, 2)" />
						<xsl:value-of select="$seconds" />
					</xsl:otherwise>
				</xsl:choose>
				- seconds
			</div>
			<ul>
				<li>
					<xsl:value-of select="@location"/>
				</li>
				<xsl:if test="target[number(substring(@time, 1, 2)) > 0]">
					<li>
						<div id="Details">
							<table width="70%">
								<thead>
									<tr>
										<th align="left">Target</th>
										<th align="right"></th>
									</tr>
								</thead>
								<tbody>
									<xsl:apply-templates select="target[number(substring(@time, 1, 2)) > 0 and contains(@time, 'minutes')]">
										<xsl:sort select="substring(@time, 1, 2)" order="descending" data-type="number"/>
									</xsl:apply-templates>
									<xsl:apply-templates select="target[number(substring(@time, 1, 2)) > 0 and not(contains(@time, 'minutes'))]">
										<xsl:sort select="substring(@time, 1, 2)" order="descending" data-type="number"/>
									</xsl:apply-templates>
								</tbody>
							</table>
						</div>
					</li>
				</xsl:if>
			</ul>
		</li>
	</xsl:template>
</xsl:stylesheet>