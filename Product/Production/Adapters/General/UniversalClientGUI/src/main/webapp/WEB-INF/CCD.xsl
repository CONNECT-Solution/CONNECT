<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:n3="http://www.w3.org/1999/xhtml" xmlns:n1="urn:hl7-org:v3" xmlns:n2="urn:hl7-org:v3/meta/voc" xmlns:voc="urn:hl7-org:v3/voc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:section="urn:gov.va.med">

	<xsl:output method="html" indent="yes" version="4.01" encoding="ISO-8859-1" doctype-public="-//W3C//DTD HTML 4.01//EN"/>

	<!-- CDA document -->

	<xsl:variable name="tableWidth">50%</xsl:variable>
	<xsl:variable name="snomedCode">2.16.840.1.113883.6.96</xsl:variable>
	<xsl:variable name="snomedProblemCode">55607006</xsl:variable>
	<xsl:variable name="snomedProblemCode2">404684003</xsl:variable>
	<xsl:variable name="snomedProblemCode3">418799008</xsl:variable>
	<xsl:variable name="snomedAllergyCode">416098002</xsl:variable>
	
	<xsl:variable name="loincCode">2.16.840.1.113883.6.1</xsl:variable>
	<xsl:variable name="loincProblemCode">11450-4</xsl:variable>
	<xsl:variable name="loincAllergyCode">48765-2</xsl:variable>
	<xsl:variable name="loincMedCode">10160-0</xsl:variable>

	<xsl:variable name="allergyTemplateCode">2.16.840.1.113883.10.20.1.18</xsl:variable>

	<xsl:variable name="title">
			<xsl:choose>
					 <xsl:when test="/n1:ClinicalDocument/n1:title">
							 <xsl:value-of select="/n1:ClinicalDocument/n1:title"/>
					 </xsl:when>
							 <xsl:otherwise>Clinical Document</xsl:otherwise>
			</xsl:choose>
	</xsl:variable>

	<xsl:template match="/">
		<xsl:apply-templates select="n1:ClinicalDocument"/>
	</xsl:template>

<xsl:template match="n1:ClinicalDocument">
        <html>
            <head>
		 		 <!-- <meta name='Generator' content='&CDA-Stylesheet;'/> -->
                <xsl:comment>
                        Do NOT edit this HTML directly, it was generated via an XSLt
                        transformation from the original release 2 CDA Document.
                </xsl:comment>
                <title>
                     <xsl:value-of select="$title"/>
                </title>
		     </head>
            <xsl:comment>		 		 		 		 
              Derived from HL7 Finland R2 Tyylitiedosto: Tyyli_R2_B3_01.xslt
		       Updated by   Calvin E. Beebe,   Mayo Clinic - Rochester Mn.
            </xsl:comment>
            <body>
							<!-- source -->
              <h2 align="center">
								<xsl:call-template name="documentTitle">
									<xsl:with-param name="root" select="."/>
								</xsl:call-template>
							</h2>
							
							<!-- title -->
							<div style="text-align:center;">
								<span style="font-size:larger;font-weight:bold;"><xsl:value-of select="n1:code/@displayName"/></span>
							</div>
							
							<!-- Report ID#'s -->
							<p align='center'><b><xsl:text>Created On: </xsl:text></b>
                		 <xsl:call-template name="formatDate">
		 		    		 		 <xsl:with-param name="date" 
		 		  		 		 		  select="/n1:ClinicalDocument/n1:effectiveTime/@value"/>
                        </xsl:call-template></p>
                <hr/>
		 		 <table width='100%'>
		 		    <xsl:variable name="patientRole" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole"/>
		 		    <tr><td width='15%' valign="top"><b><xsl:text>Patient: </xsl:text></b></td>
		 		        <td width='35%' valign="top"><xsl:call-template name="getName">
   		 		                             <xsl:with-param name="name" 
 		 		                      select="$patientRole/n1:patient/n1:name"/>
                                 </xsl:call-template>
		 		 		 		 <xsl:if test="$patientRole/n1:addr">
		 		 		 		 		 <xsl:call-template name="getAddress"> 
		 		 		 		 		 		 <xsl:with-param name="addr" select="$patientRole/n1:addr"/>
		 		 		 		 		 </xsl:call-template>
		 		 		 		 </xsl:if>
		 		 		 		 <xsl:if test="$patientRole/n1:telecom">
		 		 		 		 		 <xsl:call-template name="getTelecom"> 
		 		 		 		 		 		 <xsl:with-param name="telecom" 
		 		 		 		 		 		    select="$patientRole/n1:telecom"/>
		 		 		 		 		 </xsl:call-template>
		 		 		 		 </xsl:if>
                                 </td>
                       <td width='15%' align='right' valign="top"><b><xsl:text>MRN: </xsl:text></b></td>
		 		        <td width='35%' valign="top">
									<xsl:value-of select="$patientRole/n1:id/@extension"/>
								</td>		 		        
		 		    </tr>

		 		    <tr><td width='15%' valign="top"><b><xsl:text>Birthdate: </xsl:text></b></td>
                <td width='35%' valign="top"><xsl:call-template name="formatDate">
   		 		                        <xsl:with-param name="date" 
 		 		                 select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:birthTime/@value"/>
                              </xsl:call-template>
								</td>
                <td width='15%' align='right' valign="top"><b><xsl:text>Sex: </xsl:text></b></td>
		 						<td width='35%' valign="top"><xsl:variable name="sex" 
                                select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:administrativeGenderCode/@code"/>
									<xsl:choose>
										<xsl:when test="$sex='M'">Male</xsl:when>
										<xsl:when test="$sex='F'">Female</xsl:when>
									</xsl:choose>
								</td>
		 		    </tr>

					 <xsl:if test="$patientRole/n1:patient/n1:languageCommunication">
					 <tr>
						 <td width="15%" valign="top"><b><xsl:text>Language(s):</xsl:text></b></td>
						 <td width="35%" valign="top">
							 <ul>
								 <xsl:apply-templates select="$patientRole/n1:patient/n1:languageCommunication"/>
							 </ul>
						 </td>
						 <td width="15%" valign="top"></td>
						 <td width="35%" valign="top"></td>
					 </tr>
					 </xsl:if>

		 		 </table>

							<xsl:if test="n1:author">
							<hr/>
							
							<table width="100%">
								<tr>
									<td width="15%">
										<b>Source:</b>
									</td>
									<td width="85%">
										<xsl:value-of select="n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name/text()"/>
									</td>
								</tr>
								<xsl:if test="n1:author/n1:assignedAuthor/n1:assignedPerson">
								<tr>
									<td width="15%" valign="top"></td>
									<td width="85%" valign="top">Author: 
										<xsl:call-template name="getName">
											<xsl:with-param name="name" select="n1:author/n1:assignedAuthor/n1:assignedPerson"/>
										</xsl:call-template>
									</td>
								</tr>
								</xsl:if>
							</table>
							</xsl:if>
							
							<hr/>
              <div>
              		 <h3><a name="toc">Table of Contents</a></h3>
              		 <ul>
										 <xsl:for-each select="n1:component/n1:structuredBody/n1:component/n1:section/n1:title">
											 <li>
												 <a href="#{generate-id(.)}">
													 <xsl:value-of select="."/>
												 </a>
											 </li>
										 </xsl:for-each>
									 </ul>
								 </div>
								 <xsl:apply-templates select="n1:component/n1:structuredBody"/>
								 <br></br>
								 <br></br>
								 <hr/>
								 <table width='100%'>
		 		    <tr>
		 		 		 <td width="15%" valign="top"><b>Guardian: </b></td>
		 		 		 <td width="35%" valign="top">
		 		 		 		 <xsl:call-template name="getParticipant">
		 		 		 		 		 <xsl:with-param name="participant" select="/n1:ClinicalDocument/n1:participant[@typeCode='IND']/n1:associatedEntity[@classCode='GUAR']"/>
		 		 		 		 </xsl:call-template>
		 		 		 </td>
		 		 		 <td width="15%" valign="top" align='right'><b>Next of Kin: </b></td>
		 		 		 <td width="35%" valign="top">
		 		 		 		 <xsl:call-template name="getParticipant">
		 		 		 		 		 <xsl:with-param name="participant" select="/n1:ClinicalDocument/n1:participant[@typeCode='IND']/n1:associatedEntity[@classCode='NOK']"/>
		 		 		 		 </xsl:call-template>
		 		 		 </td>
		 		    </tr>
				</table>
				<hr/>
		 		 <xsl:call-template name="bottomline"/>
            </body>
        </html>
</xsl:template>

<xsl:template name="getParticipant">
		 <xsl:param name="participant"/>

		 <p>
		 		 <xsl:call-template name="getName">
		 		    <xsl:with-param name="name" 
 		 		    select="$participant/n1:associatedPerson/n1:name"/>
 		 		 </xsl:call-template>
 		 		 <xsl:if test="$participant/n1:addr">
 		 		 		 <xsl:call-template name="getAddress"> 
 		 		 		 		 <xsl:with-param name="addr" select="$participant/n1:addr"/>
 		 		 		 </xsl:call-template>
 		 		 </xsl:if>
 		 		 <xsl:if test="$participant/n1:telecom">
		 		 		 <xsl:call-template name="getTelecom"> 
		 		 		 		 <xsl:with-param name="telecom" 
		 		 		 		    select="$participant/n1:telecom"/>
		 		 		 </xsl:call-template>
 		 		 </xsl:if>
		 
		 </p>
</xsl:template>

<xsl:template name="getAddress">
		 <xsl:param name="addr"/>
		 <br/><xsl:value-of select="$addr/n1:streetAddressLine"/>
		 <br/><xsl:value-of select="$addr/n1:city"/>, <xsl:value-of select="$addr/n1:state"/>, <xsl:value-of select="$addr/n1:postalCode"/>
</xsl:template>

<xsl:template name="getTelecom">
		 <xsl:param name="telecom"/>
		 <br/><xsl:value-of select="$telecom/@value"/>
</xsl:template>
    
<!-- Get a Name  -->
<xsl:template name="getName">
    <xsl:param name="name"/>
    <xsl:choose>
         <xsl:when test="$name/n1:family">
              <xsl:value-of select="$name/n1:given"/>
              <xsl:text> </xsl:text>
              <xsl:value-of select="$name/n1:family"/>
              <xsl:text> </xsl:text>
              <xsl:if test="$name/n1:suffix">
                  <xsl:text>, </xsl:text>
                  <xsl:value-of select="$name/n1:suffix"/>
              </xsl:if>
          </xsl:when>
          <xsl:otherwise>
               <xsl:value-of select="$name"/>
          </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<!--  Format Date 
    
      outputs a date in Month Day, Year form
      e.g., 19991207  ==> December 07, 1999
-->
<xsl:template name="formatDate">
        <xsl:param name="date"/>
        <xsl:variable name="month" select="substring ($date, 5, 2)"/>
        <xsl:choose>
                <xsl:when test="$month='01'">
                        <xsl:text>January </xsl:text>
                </xsl:when>
                <xsl:when test="$month='02'">
                        <xsl:text>February </xsl:text>
                </xsl:when>
                <xsl:when test="$month='03'">
                        <xsl:text>March </xsl:text>
                </xsl:when>
                <xsl:when test="$month='04'">
                        <xsl:text>April </xsl:text>
                </xsl:when>
                <xsl:when test="$month='05'">
                        <xsl:text>May </xsl:text>
                </xsl:when>
                <xsl:when test="$month='06'">
                        <xsl:text>June </xsl:text>
                </xsl:when>
                <xsl:when test="$month='07'">
                        <xsl:text>July </xsl:text>
                </xsl:when>
                <xsl:when test="$month='08'">
                        <xsl:text>August </xsl:text>
                </xsl:when>
                <xsl:when test="$month='09'">
                        <xsl:text>September </xsl:text>
                </xsl:when>
                <xsl:when test="$month='10'">
                        <xsl:text>October </xsl:text>
                </xsl:when>
                <xsl:when test="$month='11'">
                        <xsl:text>November </xsl:text>
                </xsl:when>
                <xsl:when test="$month='12'">
                        <xsl:text>December </xsl:text>
                </xsl:when>
        </xsl:choose>
        <xsl:choose>
                <xsl:when test='substring ($date, 7, 1)="0"'>
                        <xsl:value-of select="substring ($date, 8, 1)"/>
                        <xsl:text>, </xsl:text>
                </xsl:when>
                <xsl:otherwise>
                        <xsl:value-of select="substring ($date, 7, 2)"/>
                        <xsl:text>, </xsl:text>
                </xsl:otherwise>
        </xsl:choose>
        <xsl:value-of select="substring ($date, 1, 4)"/>
</xsl:template>

<!-- StructuredBody -->
<xsl:template match="n1:component/n1:structuredBody">
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.11']"/>
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.16']"/>
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.14']"/>
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.3']"/>
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.2']"/>
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.8']"/>
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.6']"/>
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.1']"/>
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.9']"/>
	<xsl:apply-templates select="n1:component/n1:section[not(n1:templateId)]"/>
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root!='2.16.840.1.113883.10.20.1.9' and n1:templateId/@root!='2.16.840.1.113883.10.20.1.11' and n1:templateId/@root!='2.16.840.1.113883.10.20.1.16' and n1:templateId/@root!='2.16.840.1.113883.10.20.1.14' and n1:templateId/@root!='2.16.840.1.113883.10.20.1.3' and n1:templateId/@root!='2.16.840.1.113883.10.20.1.2' and n1:templateId/@root!='2.16.840.1.113883.10.20.1.8' and n1:templateId/@root!='2.16.840.1.113883.10.20.1.6' and n1:templateId/@root!='2.16.840.1.113883.10.20.1.1']"/>
</xsl:template>

	<!-- Component/Section -->    
	<xsl:template match="n1:component/n1:section" name="detailSection">
		<xsl:apply-templates select="n1:title"/>
		<xsl:choose>
			<xsl:when test="n1:code[@code=$loincProblemCode] and count(n1:text/n1:table/n1:thead/n1:tr/n1:th)!=3">
				<xsl:call-template name="problemDetails">
					<xsl:with-param select="." name="section"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:code[@code=$loincAllergyCode]">
				<xsl:call-template name="allergyDetails">
					<xsl:with-param select="." name="section"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:code[@code=$loincMedCode]">
				<xsl:call-template name="medDetails">
					<xsl:with-param select="." name="section"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="n1:text"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:apply-templates select="n1:component/n1:section"/>
	</xsl:template>
	
	<!-- Meds Detail Section -->
	<xsl:template name="medDetails">
		<xsl:param name="section"/>
		<table border="1" width="100%">
			<thead>
				<tr>
					<th>Name</th>
					<th>Route</th>
					<th>Interval</th>
					<th>Start Date</th>
					<th>Expire Date</th>
					<th>Status</th>
				</tr>
			</thead>
			<tbody>
				<xsl:choose>
					<xsl:when test="$section/n1:entry/n1:substanceAdministration/n1:effectiveTime/n1:high">
						<xsl:apply-templates select="$section/n1:entry">
							<xsl:sort select="$section/n1:entry/n1:substanceAdministration/n1:effectiveTime/n1:high/@value"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="$section/n1:entry">
							<xsl:sort select="$section/n1:entry/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:effectiveTime/@value"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</tbody>
		</table>
	</xsl:template>

	<!-- Problem Detail Section -->
	<xsl:template name="problemDetails">
		<xsl:param name="section"/>
		<table border="1" width="100%">
			<thead>
				<tr>
					<th>Problem Name</th>
					<th>Problem Type</th>
					<th>Problem Date</th>
				</tr>
			</thead>
			<tbody>
				<xsl:choose>
					<xsl:when test="$section/n1:entry/n1:act/n1:entryRelationship/n1:observation/n1:effectiveTime/n1:low">
						<xsl:apply-templates select="$section/n1:entry">
							<xsl:sort select="n1:act/n1:entryRelationship/n1:observation/n1:effectiveTime/n1:low/@value" order="descending"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="$section/n1:entry">
							<xsl:sort select="n1:act/n1:effectiveTime/n1:low/@value" order="descending"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</tbody>
		</table>
	</xsl:template>

	<!-- Allergy Detail Section -->
	<xsl:template name="allergyDetails">
		<xsl:param name="section"/>
		<table border="1" width="100%">
			<thead>
				<tr>
					<th>Substance</th>
					<th>Event Type</th>
					<th>Reaction</th>
					<th>Severity</th>
					<th>Status</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="$section/n1:entry">
					<xsl:sort select="n1:act/n1:entryRelationship/n1:observation/n1:participant/n1:participantRole/n1:playingEntity/n1:code/@displayName"/>
				</xsl:apply-templates>
			</tbody>
		</table>
	</xsl:template>

	<!-- entry processing -->
	<xsl:template match="n1:entry">
		<xsl:choose>
			<xsl:when test="n1:act/n1:entryRelationship/n1:observation/n1:code[@code=$snomedProblemCode or @code=$snomedProblemCode2 or @code=$snomedProblemCode3]">
				<xsl:call-template name="problemRow">
					<xsl:with-param name="row" select="."/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:act/n1:entryRelationship/n1:observation/n1:templateId/@root=$allergyTemplateCode">
				<xsl:call-template name="allergyRow">
					<xsl:with-param name="row" select="."/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:substanceAdministration">
				<xsl:call-template name="medRow">
					<xsl:with-param name="row" select="."/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise/>
		</xsl:choose>
	</xsl:template>
	
	<!-- Medication Entry row -->
	<xsl:template name="medRow">
		<xsl:param name="row"/>
		<tr>
			<!-- Name -->
			<td>
				<xsl:choose>
					<xsl:when test="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial[n1:code/n1:originalText or n1:code/@displayName]">
						<xsl:choose>
							<xsl:when test="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/n1:originalText">
								<xsl:value-of select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/n1:originalText"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/@displayName"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:name"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>

			<!-- Route -->
			<td>
				<xsl:value-of select="$row/n1:substanceAdministration/n1:routeCode/@displayName"/>
			</td>

			<!-- Interval -->
			<td>
				<xsl:choose>
					<xsl:when test="$row/n1:substanceAdministration/n1:effectiveTime/n1:period/@value">
						<xsl:value-of select="$row/n1:substanceAdministration/n1:effectiveTime/n1:period/@value"/>
						<xsl:value-of select="$row/n1:substanceAdministration/n1:effectiveTime/n1:period/@unit"/>
					</xsl:when>
					<xsl:when test="$row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:quantity">
						<xsl:value-of select="$row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:quantity/@value"/>
						<xsl:value-of select="$row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:id/@root"/>
					</xsl:when>
				</xsl:choose>
			</td>

			<!-- Start Date -->
			<td>
				<xsl:if test="$row/n1:substanceAdministration/n1:effectiveTime/n1:low/@value">
					<xsl:call-template name="formatDate">
						<xsl:with-param name="date" select="$row/n1:substanceAdministration/n1:effectiveTime/n1:low/@value"/>
					</xsl:call-template>
				</xsl:if>
			</td>

			<!-- Expire Date -->
			<td>
				<xsl:choose>
					<xsl:when test="$row/n1:substanceAdministration/n1:effectiveTime/n1:high/@value">
						<xsl:call-template name="formatDate">
							<xsl:with-param name="date" select="$row/n1:substanceAdministration/n1:effectiveTime/n1:high/@value"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="$row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:effectiveTime/@value">
							<xsl:call-template name="formatDate">
								<xsl:with-param name="date" select="$row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:effectiveTime/@value"/>
							</xsl:call-template>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</td>

			<!-- Status -->
			<td>
				<xsl:choose>
					<xsl:when test="$row/n1:substanceAdministration/n1:statusCode/@code">
						<xsl:value-of select="$row/n1:substanceAdministration/n1:statusCode/@code"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$row/n1:substanceAdministration/n1:entryRelationship[@typeCode='REFR']/n1:observation/n1:value/@displayName"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>

	<!-- problem entry row -->
	<xsl:template name="problemRow">
		<xsl:param name="row"/>
		<xsl:variable name="rowData" select="$row/n1:act/n1:entryRelationship/n1:observation"/>
		<tr>
			<td>
				<!-- problem display name -->
				<xsl:value-of select="$rowData/n1:code/@displayName"/>
			</td>
			<td>
				<!-- problem description -->
				<xsl:value-of select="$rowData/n1:value/@displayName"/>
			</td>
			<td>
				<!-- problem effective date -->
				<xsl:choose>
					<xsl:when test="$row/n1:act/n1:effectiveTime/n1:low">
						<xsl:call-template name="formatDate">
							<xsl:with-param name="date" select="$row/n1:act/n1:effectiveTime/n1:low/@value"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="formatDate">
							<xsl:with-param name="date" select="$rowData/n1:effectiveTime/n1:low/@value"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>

	<xsl:template name="allergyRow">
		<xsl:param name="row"/>
		<xsl:variable name="observation" select="$row/n1:act/n1:entryRelationship/n1:observation"/>
		<tr>
			<!-- Substance -->
			<td>
				<xsl:value-of select="$observation/n1:participant/n1:participantRole/n1:playingEntity/n1:code/@displayName"/>
			</td>
			
			<!-- Event Type-->
			<td>
				<xsl:value-of select="$observation/n1:code/@displayName"/>
			</td>
			
			<!-- Reaction-->
			<td>
				<xsl:value-of select="$observation/n1:entryRelationship[@typeCode='MFST']/n1:observation/n1:value/@displayName"/>
			</td>
			
			<!-- Severity-->
			<td>
				<xsl:value-of select="$observation/n1:entryRelationship[@typeCode='SUBJ']/n1:observation/n1:value/@displayName"/>
			</td>
			
			<!-- Status-->
			<td>
				<xsl:value-of select="$observation/n1:entryRelationship[@typeCode='REFR']/n1:observation/n1:value/@displayName"/>
			</td>
		</tr>
	</xsl:template>
	
<!--   Title  -->
<xsl:template match="n1:title">

		 <h3><span style="font-weight:bold;">		 
		 <a name="{generate-id(.)}" href="#toc"><xsl:value-of select="."/></a>
		 </span></h3>

</xsl:template>

<!--   Text   -->
<xsl:template match="n1:text">		 
		 <xsl:apply-templates />
</xsl:template>

<!--   paragraph  -->
<xsl:template match="n1:paragraph">
		 <p><xsl:apply-templates/></p>
</xsl:template>

<!--     Content w/ deleted text is hidden -->
<xsl:template match="n1:content[@revised='delete']"/>

<!--   content  -->
<xsl:template match="n1:content">
		 <xsl:apply-templates/>
</xsl:template>


<!--   list  -->
<xsl:template match="n1:list">
    <xsl:if test="n1:caption">
        <span style="font-weight:bold; ">
        <xsl:apply-templates select="n1:caption"/>
        </span>
    </xsl:if>
   <ul>
    <xsl:for-each select="n1:item">
		 <li>
          <xsl:apply-templates />
		 </li>
     </xsl:for-each>
    </ul>		 
</xsl:template>

<xsl:template match="n1:list[@listType='ordered']">
    <xsl:if test="n1:caption">
        <span style="font-weight:bold; ">
        <xsl:apply-templates select="n1:caption"/>
        </span>
    </xsl:if>
   <ol>
    <xsl:for-each select="n1:item">
		 <li>
          <xsl:apply-templates />
		 </li>
     </xsl:for-each>
    </ol>		 
</xsl:template>
		 

<!--   caption  -->
<xsl:template match="n1:caption">  
		 <xsl:apply-templates/>
		 <xsl:text>: </xsl:text>
</xsl:template>
		 
		 <!--      Tables   -->
		 <xsl:template match="n1:table/@*|n1:thead/@*|n1:tfoot/@*|n1:tbody/@*|n1:colgroup/@*|n1:col/@*|n1:tr/@*|n1:th/@*|n1:td/@*">
		 		 <xsl:copy>
		 		 
		 		 		 <xsl:copy-of select="@*"/>
		 		 		 <xsl:apply-templates/>
		 		 </xsl:copy>
		 </xsl:template>

		 <xsl:template match="n1:table">
		 		 <table>		 
		 		 
		 		 		 <xsl:copy-of select="@*"/>
		 		 		 <xsl:apply-templates/>
		 		 </table>		 
		 </xsl:template>
		 
		 <xsl:template match="n1:thead">
		 		 <thead>		 
		 		 
		 		 		 <xsl:copy-of select="@*"/>
		 		 		 <xsl:apply-templates/>
		 		 </thead>		 
		 </xsl:template>

		 <xsl:template match="n1:tfoot">
		 		 <tfoot>		 
		 		 
		 		 		 <xsl:copy-of select="@*"/>
		 		 		 <xsl:apply-templates/>
		 		 </tfoot>		 
		 </xsl:template>

		 <xsl:template match="n1:tbody">
		 		 <tbody>		 
		 		 
		 		 		 <xsl:copy-of select="@*"/>
		 		 		 <xsl:apply-templates/>
		 		 </tbody>		 
		 </xsl:template>

		 <xsl:template match="n1:colgroup">
		 		 <colgroup>		 
		 		 
		 		 		 <xsl:copy-of select="@*"/>
		 		 		 <xsl:apply-templates/>
		 		 </colgroup>		 
		 </xsl:template>

		 <xsl:template match="n1:col">
		 		 <col>		 
		 		 
		 		 		 <xsl:copy-of select="@*"/>
		 		 		 <xsl:apply-templates/>
		 		 </col>		 
		 </xsl:template>

		 <xsl:template match="n1:tr">
		 		 <tr>		 
		 		 
		 		 		 <xsl:copy-of select="@*"/>
		 		 		 <xsl:apply-templates/>
		 		 </tr>		 
		 </xsl:template>

		 <xsl:template match="n1:th">
		 		 <th>		 
		 		 		 <xsl:copy-of select="@*"/>
		 		 		 <xsl:apply-templates/>
		 		 </th>		 
		 </xsl:template>

		 <xsl:template match="n1:td">
		 		 <td>		 
		 		 		 <xsl:copy-of select="@*"/>
		 		 		 <xsl:apply-templates/>
		 		 </td>		 
		 </xsl:template>

		 <xsl:template match="n1:table/n1:caption">
		 		 <span style="font-weight:bold; ">		 
		 		 		 <xsl:apply-templates/>
		 		 </span>		 
		 </xsl:template>

<!--   RenderMultiMedia 

         this currently only handles GIF's and JPEG's.  It could, however,
	 be extended by including other image MIME types in the predicate
	 and/or by generating <object> or <applet> tag with the correct
	 params depending on the media type  @ID  =$imageRef     referencedObject
 -->
     <xsl:template match="n1:renderMultiMedia">
		 <xsl:variable name="imageRef" select="@referencedObject"/>
        <xsl:choose>
             <xsl:when test="//n1:regionOfInterest[@ID=$imageRef]">
             <!-- Here is where the Region of Interest image referencing goes -->
                  <xsl:if test='//n1:regionOfInterest[@ID=$imageRef]//n1:observationMedia/n1:value[@mediaType="image/gif" or @mediaType="image/jpeg"]'>
		 		 		 <br clear='all'/>
		 		        <xsl:element name='img'>
		 		 		     <xsl:attribute name='src'>
		 		 		 		 <xsl:value-of select='//n1:regionOfInterest[@ID=$imageRef]//n1:observationMedia/n1:value/n1:reference/@value'/>
		 		 		     </xsl:attribute>
		 		        </xsl:element>
		           </xsl:if>
             </xsl:when>
             <xsl:otherwise>
             <!-- Here is where the direct MultiMedia image referencing goes -->
                  <xsl:if test='//n1:observationMedia[@ID=$imageRef]/n1:value[@mediaType="image/gif" or @mediaType="image/jpeg"]'>
		 		 		 <br clear='all'/>
		 		        <xsl:element name='img'>
		 		 		     <xsl:attribute name='src'>
		 		 		 		 <xsl:value-of select='//n1:observationMedia[@ID=$imageRef]/n1:value/n1:reference/@value'/>
		 		 		     </xsl:attribute>
		 		        </xsl:element>
		           </xsl:if>              
             </xsl:otherwise>
        </xsl:choose>		 
     </xsl:template>

<!-- 	Stylecode processing   
	  Supports Bold, Underline and Italics display

-->

		 <xsl:template match="//n1:*[@styleCode]">

		 <xsl:if test="@styleCode='Bold'">
		      <xsl:element name='b'>		 		 		 		 
		           <xsl:apply-templates/>
		      </xsl:element>		 
		 </xsl:if> 

		 <xsl:if test="@styleCode='Italics'">
		      <xsl:element name='i'>		 		 		 		 
		           <xsl:apply-templates/>
		      </xsl:element>		 
		 </xsl:if>

		 <xsl:if test="@styleCode='Underline'">
		      <xsl:element name='u'>		 		 		 		 
		           <xsl:apply-templates/>
		      </xsl:element>		 
		 </xsl:if>

		    <xsl:if test="contains(@styleCode,'Bold') and contains(@styleCode,'Italics') and not (contains(@styleCode, 'Underline'))">
		      <xsl:element name='b'>
		 		 <xsl:element name='i'>		 		 		 		 
		           <xsl:apply-templates/>
		 		 </xsl:element>
		      </xsl:element>		 
		    </xsl:if>

		    <xsl:if test="contains(@styleCode,'Bold') and contains(@styleCode,'Underline') and not (contains(@styleCode, 'Italics'))">
		      <xsl:element name='b'>
		 		 <xsl:element name='u'>		 		 		 		 
		           <xsl:apply-templates/>
		 		 </xsl:element>
		      </xsl:element>		 
		    </xsl:if>

		    <xsl:if test="contains(@styleCode,'Italics') and contains(@styleCode,'Underline') and not (contains(@styleCode, 'Bold'))">
		      <xsl:element name='i'>
		 		 <xsl:element name='u'>		 		 		 		 
		           <xsl:apply-templates/>
		 		 </xsl:element>
		      </xsl:element>		 
		    </xsl:if>

		    <xsl:if test="contains(@styleCode,'Italics') and contains(@styleCode,'Underline') and contains(@styleCode, 'Bold')">
		      		 <xsl:element name='b'>
		 		 <xsl:element name='i'>
		 		 <xsl:element name='u'>		 		 		 		 
		             <xsl:apply-templates/>
		 		 </xsl:element>
		 		 </xsl:element>
		      		 </xsl:element>		 
		    </xsl:if>

		 </xsl:template>

<!-- 	Superscript or Subscript   -->
		 <xsl:template match="n1:sup">
		      <xsl:element name='sup'>		 		 		 		 
		           <xsl:apply-templates/>
		      </xsl:element>		 
		 </xsl:template>
		 <xsl:template match="n1:sub">
		      <xsl:element name='sub'>		 		 		 		 
		           <xsl:apply-templates/>
		      </xsl:element>		 
		 </xsl:template>

		 <!--  Bottomline  -->
		 
     <xsl:template name="bottomline">
     <p>
     <b><xsl:text>Electronically generated by: </xsl:text></b>
		 <xsl:call-template name="getName">
           <xsl:with-param name="name" 
                select="/n1:ClinicalDocument/n1:legalAuthenticator/n1:assignedEntity/n1:representedOrganization/n1:name"/>
        </xsl:call-template>
        <xsl:text> on </xsl:text>
		        <xsl:call-template name="formatDate">
   		            <xsl:with-param name="date" 
 		                select="//n1:ClinicalDocument/n1:legalAuthenticator/n1:time/@value"/>
        </xsl:call-template>
       </p>
      </xsl:template>

	<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.11']/n1:text/n1:table/n1:tbody">
		<xsl:apply-templates>
			<xsl:sort select="n1:td[3]" order="descending"/>
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.8']/n1:text/n1:table/n1:tbody">
		<xsl:apply-templates>
			<xsl:sort select="n1:td[5]" order="descending"/>
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.16' or n1:templateId/@root='2.16.840.1.113883.10.20.1.14' or n1:templateId/@root='2.16.840.1.113883.10.20.1.6' or n1:templateId/@root='2.16.840.1.113883.10.20.1.3']/n1:text/n1:table/n1:tbody">
		<xsl:apply-templates>
			<xsl:sort select="n1:td[2]" order="descending"/>
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.11']/n1:text/n1:table/n1:tbody/n1:tr/n1:td[3]">
		<td>
		<xsl:call-template name="formatDate">
			<xsl:with-param name="date"
				 select="text()"/>
		</xsl:call-template>
		</td>
	</xsl:template>

	<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.8']/n1:text/n1:table/n1:tbody/n1:tr/n1:td[5]">
		<td>
			<xsl:call-template name="formatDate">
				<xsl:with-param name="date"
					 select="text()"/>
			</xsl:call-template>
		</td>
	</xsl:template>

	<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.16' or n1:templateId/@root='2.16.840.1.113883.10.20.1.14' or n1:templateId/@root='2.16.840.1.113883.10.20.1.6' or n1:templateId/@root='2.16.840.1.113883.10.20.1.3']/n1:text/n1:table/n1:tbody/n1:tr/n1:td[2]">
		<td>
			<xsl:call-template name="formatDate">
				<xsl:with-param name="date"
					 select="concat(substring(text(),1,4),substring(text(),6,2),substring(text(),9,2))"/>
			</xsl:call-template>
		</td>
	</xsl:template>

	<xsl:template match="n1:languageCommunication">
		<li>
		<xsl:variable name="langCode" select="substring(n1:languageCode/@code,1,2)"/>
			<xsl:choose>
				<xsl:when test="$langCode='en'">
					<xsl:text>English</xsl:text>
				</xsl:when>
				<xsl:when test="$langCode='es'">
					<xsl:text>Spanish</xsl:text>
				</xsl:when>
				<xsl:otherwise>Unknown code '<xsl:value-of select="n1:languageCode/@code"/>'</xsl:otherwise>
			</xsl:choose>
		</li>
	</xsl:template>

	<xsl:template name="documentTitle">
		<xsl:param name="root"/>

		<xsl:choose>
			<xsl:when test="$root/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name and string-length($root/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name)>0">
				<xsl:value-of select="$root/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$root/n1:author[1]/n1:assignedAuthor/n1:representedOrganization/n1:name"/>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

</xsl:stylesheet>