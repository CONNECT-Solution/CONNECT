<?xml version='1.0'?>

<!DOCTYPE xsl:stylesheet [
	<!ENTITY CDA-Stylesheet
	  '-//HL7//XSL HL7 V1.1 CDA Stylesheet: 2000-08-03//EN'>
	]>
<xsl:stylesheet version='1.0'
	xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>

<xsl:output method='html' indent='yes' version='4.01'
	doctype-public='-//W3C//DTD HTML 4.01//EN'/>

<xsl:variable name='docType'
	select='/levelone/clinical_document_header/document_type_cd/@DN'/>
<xsl:variable name='orgName'
	select='/levelone/clinical_document_header/originating_organization/organization/organization.nm/@V'/>
<xsl:variable name='title'>
	<xsl:value-of select='$orgName'/>
	<xsl:text> </xsl:text>
	<xsl:value-of select='$docType'/>
</xsl:variable>

<xsl:template match='/levelone'>
	<html>
		<head>
			<meta name='Generator' content='&CDA-Stylesheet;'/>
			<xsl:comment>
				do NOT edit this HTML directly, it was generated
				via an XSLT transformation from the original Level 1
				document.
			</xsl:comment>
			<style>
				<xsl:comment>
					body {background-color: white; color: black; }
					<!--div div { margin: 4px 0 4px 0.1in; padding: 0; }-->
					<!--ul { margin: -4px 0 -8px 0 ; padding: 0; }-->
					p { margin: 10px 0 10px 0.25in; }
					div.caption { font-weight: bold; text-decoration: underline; }
					span.caption { font-weight: bold; }
					div.title { font-size: 18pt; font-weight: bold }
					div.demographics { text-align: center ; }
				</xsl:comment>
			</style>
			
			<title>
				<xsl:value-of select='$title'/>
			</title>
		</head>
		<body>
			<div class='title'>
				<xsl:value-of select='$title'/>
			</div>
			<br/>
			<xsl:apply-templates select='clinical_document_header'/>
			<br/>
			<xsl:apply-templates select='body'/>
			<xsl:call-template name='signature'/>
		</body>
	</html>
</xsl:template>

<!--
     generate a table at the top of the document containing
	 encounter and patient information.  Encounter info is
	 rendered in the left column(s) and patient info is
	 rendered in the right column(s).
	 
	 This assumes several things about the source document which
	 won't be true in the general case:
	 
	 	1. there is only 1 of everything (i.e., physcian, patient, etc.)
		2. I haven't bothered to map all HL7 table values
		   (e.g., actor.type_cd and administrative_gender_cd)
		   and have only those that are used in the sample document
		
		I tried to do the table formting with CSS2 rules, but Netscape
		doesn't seem to handle the table rules well (or at all:-( so I
		just gave up
  -->
<xsl:template match='clinical_document_header'>
	<div class='demographics'>
		<table border='0'>
			<tbody>
				<xsl:call-template name='encounter'/>
				<xsl:call-template name='patient'/>
			</tbody>
		</table>
	</div>
</xsl:template>

<xsl:template name='encounter'>
	<tr>
		<xsl:apply-templates select='provider'/>
	</tr>
	<tr>
		<xsl:apply-templates select='patient_encounter/encounter_tmr'/>
	</tr>
</xsl:template>

<xsl:template match='encounter_tmr'>
	<th align='left' width='16.67%'>Date:</th>
	<td align='left' width='16.67%'>
		<xsl:call-template name='date'>
			<xsl:with-param name='date' select='@V'/>
		</xsl:call-template>
	</td>
</xsl:template>

<xsl:template match='provider'>
	<th align='left' width='16.67%'>
		<xsl:call-template name='provider_type_cd'>
			<xsl:with-param name='type_cd' select='provider.type_cd/@V'/>
		</xsl:call-template>
		<xsl:text>:</xsl:text>
	</th>
	<td align='left' width='16.67%'>
		<xsl:variable name='ptr' select='person/id/@EX'/>

		<xsl:for-each select='/levelone/clinical_document_header/originator/person[id/@EX=$ptr]'>
			<xsl:call-template name='getName'/>
		</xsl:for-each>
	</td>
</xsl:template>

<xsl:template name='patient'>
	<tr>
		<xsl:apply-templates select='patient'/>
	</tr>

	<tr>
		<xsl:apply-templates select='patient/birth_dttm'/>
	</tr>
</xsl:template>

<xsl:template match='birth_dttm'>
	<th align='left' width='16.67%'>Birthdate:</th>
	<td align='left' width='16.67%'>
		<xsl:call-template name='date'>
			<xsl:with-param name='date' select='@V'/>
		</xsl:call-template>
	</td>
</xsl:template>

<xsl:template match='patient'>
	<th align='left' width='16.67%'>Patient:</th>
	<td align='left' width='16.67%'>
		<xsl:for-each select='person'>
			<xsl:call-template name='getName'/>
		</xsl:for-each>
	</td>
	<th align='right' width='16.67%'>
		<xsl:text>MRN:</xsl:text>
	</th>
	<td align='left' width='16.67%'>
		<xsl:value-of select='person/id/@EX'/>				
	</td>
	<th align='right' width='16.66%'>Sex:</th>
	<td align='left' width='16.66%'>
		<xsl:call-template name='administrative_gender_cd'>
			<xsl:with-param name='gender_cd' select='administrative_gender_cd/@V'/>
		</xsl:call-template>
	</td>
</xsl:template>

<!--
    just apply the default template for these
  -->
<xsl:template match='body|caption|content'>
	<xsl:apply-templates/>
</xsl:template>

<!--
    spit out the caption (in the 'caption' style)
	followed by applying whatever templates we
	have for the applicable children
  -->
<xsl:template match='section'>
	<div>
		<div class='caption'>
			<xsl:apply-templates select='caption'/>
		</div>
		<xsl:apply-templates select='paragraph|list|table|section'/>
	</div>
</xsl:template>

<xsl:template match='section/section'>
	<ul>
		<li>
			<span class='caption'>
				<xsl:apply-templates select='caption'/>
				<xsl:text> :: </xsl:text>
			</span>
			<xsl:apply-templates select='paragraph|list|table|section'/>
		</li>
	</ul>
</xsl:template>

<!--
    currently ignores paragraph captions...
	
	I need samples of the use description and render
	to know what really should be done with them
  -->
<xsl:template match='paragraph'>
	<p>
		<xsl:apply-templates select='content'/>
	</p>
</xsl:template>

<!--
    currently ignore caption's on the list itself,
	but handles them on the list items
  -->
<xsl:template match='list'>
	<ul>
		<xsl:for-each select='item'>
			<li>
				<xsl:if test='caption'>
					<xsl:apply-templates select='caption'/>
					<xsl:text> :: </xsl:text>
				</xsl:if>
				<xsl:apply-templates select='content'/>
			</li>
		</xsl:for-each>
	</ul>
</xsl:template>

<!--
    currently ignore caption's on the list itself,
	but handles them on the list items
  -->
<xsl:template match='section/section/list'>
	<xsl:for-each select='item'>
		<xsl:apply-templates select='content'/>
		<xsl:if test='position()!=last()'>
			<xsl:text>; </xsl:text>
		</xsl:if>
	</xsl:for-each>
</xsl:template>

<xsl:template match='section/section/paragraph'>
	<span>
		<xsl:apply-templates/>
	</span>
</xsl:template>

<!-- 
     Tables
	 
     just copy over the entire subtree, as is
	 except that the children of CAPTION are possibly handled by
	 other templates in this stylesheet
  -->
<xsl:template match='table|table/caption|thead|tfoot|tbody|colgroup|col|tr|th|td'>
	<xsl:copy>
		<xsl:apply-templates select='*|@*|text()'/>
	</xsl:copy>
</xsl:template>
<xsl:template match='table/@*|thead/@*|tfoot/@*|tbody/@*|colgroup/@*|col/@*|tr/@*|th/@*|td/@*'>
	<xsl:copy>
		<xsl:apply-templates/>
	</xsl:copy>
</xsl:template>

<!--
     this currently only handles GIF's and JPEG's.  It could, however,
	 be extended by including other image MIME types in the predicate
	 and/or by generating <object> or <applet> tag with the correct
	 params depending on the media type
  -->
<xsl:template match='observation_media'>
	<xsl:if test='observation_media.value[
			@MT="image/gif" or @MT="image/jpeg"
			]'>
		<br clear='all'/>
		<xsl:element name='img'>
			<xsl:attribute name='src'>
				<xsl:value-of select='observation_media.value/REF/@V'/>
			</xsl:attribute>
		</xsl:element>
	</xsl:if>
</xsl:template>

<!--
	turn the link_html subelement into an HTML a element,
	complete with any attributes and content it may have,
	while stripping off any CDA specific attributes
  -->
<xsl:template match='link'>
	<xsl:element name='a'>
		<xsl:for-each select='link_html/@*'>
   			<xsl:if test='not(name()="originator" or name()="confidentiality")'>
				<xsl:attribute name='{name()}'>
					<xsl:value-of select='.'/>
				</xsl:attribute>
			</xsl:if>
		</xsl:for-each>
		<xsl:value-of select='link_html'/>
	</xsl:element>
</xsl:template>

<!--
    this doesn't do anything with the description
	or render attributes...it simply decides whether
	to remove the entire subtree or just pass the
	content thru
	
	I need samples of the use description and render
	to know what really should be done with them
  -->
<!--
<xsl:template match='local_markup'>
	<xsl:choose>
		<xsl:when test='@ignore="markup"'>
			<xsl:apply-templates/>
		</xsl:when>
	</xsl:choose>
</xsl:template>
-->
<xsl:template match='@ignore'>
	<xsl:choose>
		<xsl:when test='.="markup"'>
			<xsl:apply-templates select='../text()'/>
		</xsl:when>
	</xsl:choose>
</xsl:template>

<!--
     elements to ignore
  -->
<xsl:template match='coded_entry'>
</xsl:template>

<xsl:template match='caption_cd'>
</xsl:template>


<!--
     template(s) to output signature block

     Assumes there is only one signer at this time
  -->
<xsl:template name='signature'>
	<xsl:variable name='signers' select='/levelone/clinical_document_header/legal_authenticator/person'/>
	<xsl:if test='$signers'>
		<div>
			<span class='caption'>Signed by:</span>
			<xsl:for-each select='$signers'>
				<xsl:call-template name='getName'>
					<xsl:with-param name='person' select='.'/>
				</xsl:call-template>
				<xsl:text> on </xsl:text>	
				<xsl:call-template name='date'>
					<xsl:with-param name='date' select='../participation_tmr/@V'/>
               </xsl:call-template>
			</xsl:for-each>
		</div>
	</xsl:if>
</xsl:template>

<!--
     general purpose (named) templates used in multiple places
  -->
<!--
     assumes current node is a <person_name> node

     Does not handle nearly all of the complexity of the person datatype,
	 but is illustritive of what would be required to do so in the future
  -->
<xsl:template name='getName'>
	<xsl:apply-templates select='person_name[person_name.type_cd/@V="L"]'/>
</xsl:template>
<xsl:template match='person_name'>
	<xsl:choose>
		<xsl:when test='nm/GIV[@QUAL="RE"]/@V'>
			<xsl:value-of select='nm/GIV[@QUAL="RE"]/@V'/>
		</xsl:when>
		<xsl:when test='nm/GIV[@CLAS="N"]/@V'>
			<xsl:value-of select='nm/GIV[@CLAS="N"]/@V'/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select='nm/GIV/@V'/>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:text> </xsl:text>
	<xsl:choose>
		<xsl:when test='nm/FAM[@QUAL="RE"]/@V'>
			<xsl:value-of select='nm/FAM[@QUAL="RE"]/@V'/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select='nm/FAM/@V'/>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:choose>
		<xsl:when test='nm/SFX[@QUAL="RE"]/@V'>
			<xsl:text>, </xsl:text>
			<xsl:value-of select='nm/SFX[@QUAL="RE"]/@V'/>
		</xsl:when>
		<xsl:when test='nm/SFX/@V'>
			<xsl:text>, </xsl:text>
			<xsl:value-of select='nm/SFX/@V'/>
		</xsl:when>
	</xsl:choose>
</xsl:template>

<!--
     outputs a date in Month Day, Year form
	 
	 e.g., 19991207  ==> December 07, 1999
  -->
<xsl:template name='date'>
	<xsl:param name='date'/>
	<xsl:variable name='month' select='substring ($date, 6, 2)'/>
	<xsl:choose>
		<xsl:when test='$month=01'>
			<xsl:text>January </xsl:text>
		</xsl:when>
		<xsl:when test='$month=02'>
			<xsl:text>February </xsl:text>
		</xsl:when>
		<xsl:when test='$month=03'>
			<xsl:text>March </xsl:text>
		</xsl:when>
		<xsl:when test='$month=04'>
			<xsl:text>April </xsl:text>
		</xsl:when>
		<xsl:when test='$month=05'>
			<xsl:text>May </xsl:text>
		</xsl:when>
		<xsl:when test='$month=06'>
			<xsl:text>June </xsl:text>
		</xsl:when>
		<xsl:when test='$month=07'>
			<xsl:text>July </xsl:text>
		</xsl:when>
		<xsl:when test='$month=08'>
			<xsl:text>August </xsl:text>
		</xsl:when>
		<xsl:when test='$month=09'>
			<xsl:text>September </xsl:text>
		</xsl:when>
		<xsl:when test='$month=10'>
			<xsl:text>October </xsl:text>
		</xsl:when>
		<xsl:when test='$month=11'>
			<xsl:text>November </xsl:text>
		</xsl:when>
		<xsl:when test='$month=12'>
			<xsl:text>December </xsl:text>
		</xsl:when>
	</xsl:choose>
	<xsl:choose>
		<xsl:when test='substring ($date, 9, 1)="0"'>
			<xsl:value-of select='substring ($date, 10, 1)'/><xsl:text>, </xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select='substring ($date, 9, 2)'/><xsl:text>, </xsl:text>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:value-of select='substring ($date, 1, 4)'/>
</xsl:template>

<!--
     table lookups
  -->
<xsl:template name='provider_type_cd'>
	<xsl:param name='type_cd'/>
	<xsl:choose>
		<xsl:when test='$type_cd="CON"'>
			<xsl:text>Consultant</xsl:text>
		</xsl:when>
		<xsl:when test='$type_cd="PRISURG"'>
			<xsl:text>Primary surgeon</xsl:text>
		</xsl:when>
		<xsl:when test='$type_cd="FASST"'>
			<xsl:text>First assistant</xsl:text>
		</xsl:when>
		<xsl:when test='$type_cd="SASST"'>
			<xsl:text>Second assistant</xsl:text>
		</xsl:when>
		<xsl:when test='$type_cd="SNRS"'>
			<xsl:text>Scrub nurse</xsl:text>
		</xsl:when>
		<xsl:when test='$type_cd="TASST"'>
			<xsl:text>Third assistant</xsl:text>
		</xsl:when>
		<xsl:when test='$type_cd="NASST"'>
			<xsl:text>Nurse assistant</xsl:text>
		</xsl:when>
		<xsl:when test='$type_cd="ANEST"'>
			<xsl:text>Anesthetist</xsl:text>
		</xsl:when>
		<xsl:when test='$type_cd="ANRS"'>
			<xsl:text>Anesthesia nurse</xsl:text>
		</xsl:when>
		<xsl:when test='$type_cd="MDWF"'>
			<xsl:text>Midwife</xsl:text>
		</xsl:when>
		<xsl:when test='$type_cd="ATTPHYS"'>
			<xsl:text>Attending physician</xsl:text>
		</xsl:when>
		<xsl:when test='$type_cd="ADMPHYS"'>
			<xsl:text>Admitting physician</xsl:text>
		</xsl:when>
		<xsl:when test='$type_cd="DISPHYS"'>
			<xsl:text>Discharging physician</xsl:text>
		</xsl:when>
		<xsl:when test='$type_cd="RNDPHYS"'>
			<xsl:text>Rounding physician</xsl:text>
		</xsl:when>
		<xsl:when test='$type_cd="PCP"'>
			<xsl:text>Primary care provider</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select='$type_cd'/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name='administrative_gender_cd'>
	<xsl:param name='gender_cd'/>
	<xsl:choose>
		<xsl:when test='$gender_cd="M"'>
			<xsl:text>Male</xsl:text>
		</xsl:when>
		<xsl:when test='$gender_cd="F"'>
			<xsl:text>Female</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select='$gender_cd'/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

</xsl:stylesheet>
 