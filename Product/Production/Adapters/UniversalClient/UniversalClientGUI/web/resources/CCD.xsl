<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:n3="http://www.w3.org/1999/xhtml" xmlns:n1="urn:hl7-org:v3" xmlns:n2="urn:hl7-org:v3/meta/voc" xmlns:voc="urn:hl7-org:v3/voc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:section="urn:gov.va.med">
    <xsl:output method="html" indent="yes" version="4.01" encoding="ISO-8859-1" doctype-public="-//W3C//DTD HTML 4.01//EN"/>

	<!-- CDA document -->

	<xsl:variable name="tableWidth">95%</xsl:variable>
	<xsl:variable name="snomedCode">2.16.840.1.113883.6.96</xsl:variable>
	<xsl:variable name="snomedProblemCode">55607006</xsl:variable>
	<xsl:variable name="snomedAllergyCode">416098002</xsl:variable>	
	<xsl:variable name="loincCode">2.16.840.1.113883.6.1</xsl:variable>
	<xsl:variable name="loincProblemCode">11450-4</xsl:variable>
	<xsl:variable name="loincAllergyCode">48765-2</xsl:variable>
	<xsl:variable name="loincMedCode">10160-0</xsl:variable>
        <xsl:variable name="loincImmunCode">11369-6</xsl:variable>
	<xsl:variable name="allergyObservationTemplateID">2.16.840.1.113883.10.20.1.18</xsl:variable> <!-- HL7 CCD Code -->
	<xsl:variable name="substanceAdministrationTemplateID">2.16.840.1.113883.10.20.1.24</xsl:variable> <!-- HL7 CCD Code -->
	<xsl:variable name="actTemplateID">2.16.840.1.113883.10.20.1.27</xsl:variable> <!-- HL7 CCD Code -->
	<xsl:variable name="problemObservationTemplateID">2.16.840.1.113883.10.20.1.28</xsl:variable> <!-- HL7 CCD Code -->        
        
        <xsl:variable name="title">
            <xsl:choose>
                <xsl:when test="/n1:ClinicalDocument/n1:title">
                    <xsl:value-of select="/n1:ClinicalDocument/n1:title"/>
		</xsl:when>
                <xsl:otherwise>
                    Clinical Document
                </xsl:otherwise>
            </xsl:choose>
	</xsl:variable>

<xsl:template match="/">
    <xsl:apply-templates select="n1:ClinicalDocument"/>
</xsl:template>

<!-- Template: Clinical Document -->

<xsl:template match="n1:ClinicalDocument">
    <html>
        <head>

            <!-- <meta name='Generator' content='&CDA-Stylesheet;'/> -->

            <title>
                <xsl:value-of select="$title"/>
            </title>
        </head>

        <xsl:comment>		 		 		 		 
            Derived from HL7 Finland R2 Tyylitiedosto: Tyyli_R2_B3_01.xslt
            Updated by: Calvin E. Beebe,   Mayo Clinic - Rochester Mn.
            Revised by: Joaquin A. Marquez,  Socratic Grid - San Diego, CA
        </xsl:comment>

        <body background="http://nhinint01.asu.edu:8080/UniversalClientGUI/faces/resources/images/c32Background.jpg">
            
            <!-- Document Header: NHIN Logo, Source, Title, Author, & Creation Dates -->
            
            <table width="95%" border="0" cellspacing="5" cellpadding="0">
                <tr>                   
                    
               <!-- Document Source -->                    
                    
                    <td  colspan="2" height="1%" align="left" valign="top">
                        <strong><font color="#000099" size="3" face="Arial, Helvetica, sans-serif">
                                <xsl:call-template name="documentTitle">
                                <xsl:with-param name="root" select="."/>
                            </xsl:call-template>
                        </font></strong>
                    </td>
                                                                  
                <!-- NHIN Logo Text --> 
                
                    <td width="1%" rowspan="3" align="right" valign="center">
                        <font color="#000099" size="1" face="Arial, Helvetica, sans-serif">
                            Nationwide<br/>Health<br/>Information<br/>Network
                        </font>
                        <br/><br/>
                    </td>  
                    
                <!-- NHIN Logo -->                    
                    
                    <td width="1%" rowspan="3" align="right" valign="top">
                        <img src="http://nhinint01.asu.edu:8080/UniversalClientGUI/faces/resources/images/NHIN_Logo.jpg"></img>
                    </td>
               </tr>               
               <tr> 
               
               <!-- Document Title -->     
               
                    <td colspan="2" align="left" valign="top">               
                       <strong><font color="#000099" size="3" face="Arial, Helvetica, sans-serif">
                           <xsl:value-of select="n1:code/@displayName"/>
                       </font></strong>
                   </td>
                   <td></td> <!-- Column Spacer  -->
                </tr>

                <!-- Creation Date, Author, & Report ID#'s -->  
           
                <tr>
                   <td width = "20"></td>                  
                    <td align="left" valign="bottom">
                        <font size="1" color="#999999" face="Arial, Helvetica, sans-serif">
                            <xsl:if test="/n1:ClinicalDocument/n1:effectiveTime/@value">
                                Created on 
                                <xsl:call-template name="formatDate">
                                <xsl:with-param name="date" 
                                    select="/n1:ClinicalDocument/n1:effectiveTime/@value"/>
                                </xsl:call-template>
                            </xsl:if>                                
                                <xsl:call-template name="generatedBy"/>
                        </font>
                    </td>
                    <td></td> <!-- Column Spacer  -->
                </tr>
                <tr> 
                    <td colspan="4" align="center" valign="top">
                        <hr size="1" color="#999999" noshade="true"/>
                    </td>
                </tr>
            </table>

            <!-- Person Information --> 
            
            <table width="95%" border="0" cellspacing="5" cellpadding="0">
                <xsl:variable name="patientRole" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole"/>
                <tr> 
                
                    <!-- Patient Name and MRN -->
                
                    <td width="15%" align="right"><strong>
                        <font size="1"  color="#666666" face="Arial, Helvetica, sans-serif">
                            PATIENT:
                        </font></strong>
                    </td>
                    <td width="50%" align="left">
                        <font size="2" face="Arial, Helvetica, sans-serif"><strong>
                            <xsl:call-template name="getName">
                                <xsl:with-param name="name" select="$patientRole/n1:patient/n1:name"/>
                            </xsl:call-template>
                        </strong></font></td>
                    <td width="15%" align="right">
                        <font size="1" color="#666666" face="Arial, Helvetica, sans-serif"><strong>
                            MRN:
                        </strong></font></td>
                    <td width="20%" align="left">
                        <font size="2" face="Arial, Helvetica, sans-serif">
                            <xsl:value-of select="$patientRole/n1:id/@extension"/>
                        </font>
                    </td>
                </tr>
                <tr></tr> <!-- Row Spacer  -->
                <tr>
                    
                    <!-- Patient Address & Phone Numbers (telecom) -->
                    
                    <td width="15%" align="right" valign="top">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            ADDRESS:
                        </font></strong>
                    </td>
                    <td rowspan="3" width="35%" align="left" valign="top">
                        <font size="2" face="Arial, Helvetica, sans-serif">
                            <xsl:if test="$patientRole/n1:addr">
                                <xsl:call-template name="getAddress"> 
                                    <xsl:with-param name="addr" select="$patientRole/n1:addr"/>
                                </xsl:call-template>
                            </xsl:if>
                        </font>                            
                            <xsl:if test="$patientRole/n1:telecom">
                                <xsl:apply-templates select="$patientRole/n1:telecom"/>                                                           
                            </xsl:if>                               
                    </td>
                    
                    <!-- Patient Birthdate -->
                    
                    <td width="15%" align="right">
                            <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                                BIRTHDATE:
                            </font></strong>               
                    </td>
                    <td width="35%" align="left">                        
                        <font size="2" face="Arial, Helvetica, sans-serif">
                            <xsl:choose>
                                <xsl:when test = "/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:birthTime">
                                    <xsl:call-template name="formatMediumDate">
                                        <xsl:with-param name="date" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:birthTime/@value"/>
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:otherwise>Unknown</xsl:otherwise>
                            </xsl:choose>
                        </font>
                    </td>
                </tr>
                <tr>                 
                    <td width="15%" align="right"></td> <!-- Column Spacer  -->
                    
                    <!-- Patient Sex -->
                    
                    <td width="15%" align="right">
                        <font size="1" color="#666666" face="Arial, Helvetica, sans-serif"><strong>
                            SEX:
                        </strong></font>
                    </td>
                    <td width="35%" align="left">
                        <font size="2" face="Arial, Helvetica, sans-serif">
                            <xsl:variable name="sex" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:administrativeGenderCode/@code"/>
				<xsl:choose>
                                    <xsl:when test="$sex='M'">Male</xsl:when>
                                    <xsl:when test="$sex='F'">Female</xsl:when>
                                    <xsl:otherwise>Unknown</xsl:otherwise>
                                </xsl:choose>
                        </font>
                    </td>
                </tr>
                <tr>
                    <td width="15%" align="right" valign="top"></td> <!-- Column Spacer  -->   
                    
                    <!-- Patient Languages -->
                    
                    <td width="15%" align="right" valign="top">
                        <font size="1" color="#666666" face="Arial, Helvetica, sans-serif"><strong>
                            LANGUAGES:
                        </strong></font>
                    </td>
                    <td width="35%" align="left" valign="top">                        
                        <font size="2" face="Arial, Helvetica, sans-serif">
                            <xsl:choose>
                                <xsl:when test="$patientRole/n1:patient/n1:languageCommunication/n1:languageCode">
                                    <xsl:apply-templates select="$patientRole/n1:patient/n1:languageCommunication/n1:languageCode"/>
                                </xsl:when>
                                <xsl:otherwise>Unknown</xsl:otherwise>
                            </xsl:choose>
                        </font>
                    </td>
                </tr>
            </table>

            <!-- Table of Contents --> 
            
                <p/>
            <table width="95%" border="0" cellspacing="5" cellpadding="0">
                <tr>
                    <td colspan="2" align="left" valign="bottom">
                        <strong><font size="2" color="#000099" face="Arial, Helvetica, sans-serif">
                            Table of Contents
                        </font></strong>
                    </td>
                </tr>
                <tr align="center"> 
                    <td colspan="2" align="center" valign="top">
                        <hr size="1" noshade="true" color="#999999"/>
                    </td>
                </tr>
                <tr> 
                    <td width="12%" align="left" valign="top"> 
                        <p/>
                    </td>
                    <td align="left" valign="top">
                        <ul>
                           <strong><font size="1" color="#000099" face="Arial, Helvetica, sans-serif">       
                                <xsl:for-each select="n1:component/n1:structuredBody/n1:component/n1:section/n1:title">
                                    <li>
                                        <a href="#{generate-id(.)}">
                                            <xsl:value-of select="."/>
                                        </a>
                                    </li>
                                </xsl:for-each>
                            </font></strong>
                        </ul>
                    </td>
                </tr>
            </table>

            <!-- Component Sections -->  

            <xsl:apply-templates select="n1:component/n1:structuredBody"/>

            <!-- Family/Support Information:  Next of Kin / Guardian -->
            
                <p/>
            <table width="95%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top"> 
                    <td colspan="6">
                        <strong><font size="2" color="#000099" face="Arial, Helvetica, sans-serif">
                            Family/Support Information
                        </font></strong>
                    </td>
                </tr>
                <tr> 
                    <td colspan="6" align="center" valign="top">
                        <hr size="1" noshade="true" color="#999999"/>
                    </td>
                </tr>
                <tr> 
                    <td width="15%"></td>  <!-- Column Spacer -->
                    <td width="1%"></td>  <!-- Column Spacer -->
                    <td width="35%" align="left" valign="top">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            NEXT OF KIN
                        </font></strong>
                    </td>
                    <td width="15%"></td>  <!-- Column Spacer -->
                    <td width="1%"></td>  <!-- Column Spacer -->
                    <td width="35%" align="left" valign="top">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            GUARDIAN
                        </font></strong>
                    </td>
                </tr>
                <tr>
                    <td colspan="6" align="center" valign="top">
                        <hr size="1" noshade="true" color="#999999"/>
                    </td>
                </tr>                        
                <tr cellspacing="5">
                    
                    <!-- Next of Kin Contact Information --> 
                                        
                    <td align="right" valign="top">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">                        
                            <xsl:if test="/n1:ClinicalDocument/n1:participant[@typeCode='IND']/n1:associatedEntity[@classCode='NOK']">
                                <xsl:variable name="vNOK" select="/n1:ClinicalDocument/n1:participant/n1:associatedEntity[@classCode='NOK']/n1:code/@code"/>
                                    <xsl:choose>
                                        <xsl:when test="$vNOK='MTH'"><xsl:text>MOTHER: </xsl:text></xsl:when>
                                        <xsl:when test="$vNOK='FTH'"><xsl:text>FATHER: </xsl:text></xsl:when>
                                        <xsl:otherwise/>
                                    </xsl:choose>
                            </xsl:if>
                        </font></strong> 
                    </td>
                    <td></td> <!-- Column Spacer -->
                    <td align="left" valign="top">                        
                        <xsl:call-template name="getParticipant">
                            <xsl:with-param name="participant" select="/n1:ClinicalDocument/n1:participant[@typeCode='IND']/n1:associatedEntity[@classCode='NOK']"/>
                        </xsl:call-template>
                    </td> 
                      
                    <!-- Guarian Contact Information -->
                                       
                    <td align="right" valign="top">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">                        
                            <xsl:if test="/n1:ClinicalDocument/n1:participant[@typeCode='IND']/n1:associatedEntity[@classCode='GUAR']">
                                <xsl:variable name="vGUAR" select="/n1:ClinicalDocument/n1:participant/n1:associatedEntity[@classCode='GUAR']/n1:code/@code"/>
                                    <xsl:choose>
                                        <xsl:when test="$vGUAR='MTH'">MOTHER:</xsl:when>
                                        <xsl:when test="$vGUAR='FTH'">FATHER:</xsl:when>
                                        <xsl:otherwise/>
                                    </xsl:choose>
                            </xsl:if>
                        </font></strong> 
                    </td>
                    <td></td> <!-- Column Spacer -->
                    <td align="left" valign="top">                        
                        <xsl:call-template name="getParticipant">
                            <xsl:with-param name="participant" select="/n1:ClinicalDocument/n1:participant[@typeCode='IND']/n1:associatedEntity[@classCode='GUAR']"/>
                        </xsl:call-template>
                    </td>
                </tr>
                <tr> 
                    <td colspan="6" align="center" valign="top">
                        <hr size="1" noshade="true" color="#999999"/>
                    </td>
                </tr>
            </table>

            <!-- Credit: Author & Organization -->

            <xsl:if test="n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name/text()">
                <font size="1" color="#999999" face="Arial, Helvetica, sans-serif"><strong>
                    <xsl:text>Source: </xsl:text>
                    <xsl:value-of select="n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name/text()"/>
                    <br/>
                </strong></font>  
            </xsl:if>
            <xsl:if test="n1:author/n1:assignedAuthor/n1:assignedPerson">
                <font size="1" color="#999999" face="Arial, Helvetica, sans-serif"><strong>
                    <xsl:text>Author: </xsl:text>
                    <xsl:call-template name="getName">
                        <xsl:with-param name="name" select="n1:author/n1:assignedAuthor/n1:assignedPerson"/>
                    </xsl:call-template>
                </strong></font>
            </xsl:if>
        </body>
    </html>
</xsl:template>


<!-- Templates -->

    <!-- Document Title -->

<xsl:template name="documentTitle">
    <xsl:param name="root"/>
        <xsl:choose>
            <xsl:when test="$root/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name and string-length($root/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name)>0">
                <xsl:value-of select="$root/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name"/>
            </xsl:when>
            <xsl:when test="not($root/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name) or string-length($root/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name)&lt;1">
                <xsl:value-of select="$root/n1:author[1]/n1:assignedAuthor/n1:representedOrganization/n1:name"/>
            </xsl:when>
     </xsl:choose>
</xsl:template>

    <!-- Electronically Generated By  -->
		 
<xsl:template name="generatedBy">
        <xsl:if test="/n1:ClinicalDocument/n1:legalAuthenticator/n1:assignedEntity/n1:representedOrganization/n1:name">
            <br/>
            <xsl:text>Electronically generated by </xsl:text>
            <xsl:call-template name="getName">
                <xsl:with-param name="name" select="/n1:ClinicalDocument/n1:legalAuthenticator/n1:assignedEntity/n1:representedOrganization/n1:name"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="/n1:ClinicalDocument/n1:legalAuthenticator/n1:time/@value">
            <xsl:text> on </xsl:text>
            <xsl:call-template name="formatDate">
                <xsl:with-param name="date" select="/n1:ClinicalDocument/n1:legalAuthenticator/n1:time/@value"/>               
            </xsl:call-template>
        </xsl:if>
</xsl:template>

    <!-- Participant:  Name, Address, & Telcom -->

<xsl:template name="getParticipant">
    <xsl:param name="participant"/>
        <font size="2" face="Arial, Helvetica, sans-serif">
            <xsl:call-template name="getName">
                <xsl:with-param name="name" select="$participant/n1:associatedPerson/n1:name"/>
            </xsl:call-template><br/>
                <xsl:if test="$participant/n1:addr">
                    <xsl:call-template name="getAddress"> 
                        <xsl:with-param name="addr" select="$participant/n1:addr"/>
                    </xsl:call-template>
                </xsl:if> 
            </font>
            <xsl:if test="$participant/n1:telecom">
                <xsl:apply-templates select="$participant/n1:telecom"/>
            </xsl:if>            
</xsl:template>

    <!-- Name  -->

<xsl:template name="getName">
    <xsl:param name="name"/>
        <xsl:choose>
            <xsl:when test="$name/n1:family">
                <xsl:value-of select="$name/n1:given"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="$name/n1:family"/>
                <xsl:text> </xsl:text>
                <xsl:if test="$name/n1:suffix">
                    <xsl:value-of select="$name/n1:suffix"/>
                </xsl:if>
            </xsl:when>
        <xsl:otherwise>
            <xsl:value-of select="$name"/>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

    <!-- Address -->

<xsl:template name="getAddress">
    <xsl:param name="addr"/>
        <font size="2" face="sans-serif">    
            <xsl:value-of select="$addr/n1:streetAddressLine"/>
            <br/>
            <xsl:value-of select="$addr/n1:city"/>, 
            <xsl:value-of select="$addr/n1:state"/><xsl:text> </xsl:text>
            <xsl:value-of select="$addr/n1:postalCode"/>
            <p/>
        </font>
</xsl:template>

    <!-- Telcom -->

<xsl:template match="n1:telecom">
    <xsl:param name="telecom" select="."/>
        <xsl:variable name="telecomUse" select="$telecom/@use"/>
            <xsl:choose>
                <xsl:when test="$telecomUse='H'">home </xsl:when>
                <xsl:when test="$telecomUse='WP'">work </xsl:when>
                <xsl:when test="$telecomUse='MC'">mobile </xsl:when>
                <xsl:when test="$telecomUse='HV'">vacation home </xsl:when> 
                <xsl:otherwise/>
            </xsl:choose>  
            <xsl:value-of select="$telecom/@value"/><br/>
 </xsl:template>   

    <!-- Patient Languages Spoken -->

<xsl:template match="n1:languageCode">
    <xsl:param name="languageCode" select="."/>
    <xsl:variable name="langCode" select="$languageCode/@code"/>
        <xsl:choose>
            <xsl:when test="$langCode='en-US'">English (US)<br/></xsl:when>
            <xsl:when test="$langCode='es-US'">Spanish (US)<br/></xsl:when>
            <xsl:otherwise>
                Unknown code <xsl:value-of select="n1:languageCode/@code"/>
            </xsl:otherwise>
        </xsl:choose>
</xsl:template>

    <!-- Structured Body -->

<xsl:template match="n1:component/n1:structuredBody">
    
        <!-- Problem Section: CCD template identifier 2.16.840.1.113883.10.20.1.11 -->        
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.11']"/>   
        
        <!-- Allergies & Alerts Section: CCD template identifier 2.16.840.1.113883.10.20.1.2 -->      
  	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.2']"/>
        
        <!-- Immunizations Section: CCD template identifier 2.16.840.1.113883.10.20.1.6 -->        
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.6']"/>  
        
        <!-- Medications Section: CCD template identifier 2.16.840.1.113883.10.20.1.8 -->        
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.8']"/>        
        
        <!-- Advance Directives Section: CCD template identifier 2.16.840.1.113883.10.20.1.1 -->
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.1']"/>
        
        <!-- Encounters Section: CCD template identifier 2.16.840.1.113883.10.20.1.3 -->        
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.3']"/>
        
    <!-- Payers Section: CCD template identifier 2.16.840.1.113883.10.20.1.9 -->        
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.9']"/>        

    <!-- Results Section: CCD template identifier 2.16.840.1.113883.10.20.1.14 -->        
	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.14']"/>
        
    <!-- Vital Signs Section: CCD template identifier 2.16.840.1.113883.10.20.1.16 -->        
 	<xsl:apply-templates select="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.16']"/> 
                
    <!-- Module: template identifier blank -->        
	 <!-- <xsl:apply-templates select="n1:component/n1:section[not(n1:templateId)]"/> -->
        
</xsl:template>

    <!-- Section Title -->
    
<xsl:template match="n1:title">
    <pre/>
    <table border="0" cellspacing="0" width="95%">
        <tr>
            <td align="left" valign="bottom">                 
                <font size="2" color="#000099" face="Arial, Helvetica, sans-serif"><strong>
                    <a name="{generate-id(.)}"><xsl:value-of select="."/></a>
                </strong></font>
            </td>
            <td align="right" valign="bottom">
                <font size="1" color="#000099" face="Arial, Helvetica, sans-serif"><strong>
                    <a href="#top">return to top</a>
                </strong></font>
            </td>
        </tr>
    </table>        
</xsl:template>

    <!-- Component/Sections -->  
        
<xsl:template match="n1:component/n1:section" name="detailSection">

        <xsl:choose>
            
            <!-- Display Problem Section Only if LOINC Problem Code Is Referenced in One of Document's <section> Elements 
                 and ( the CCD 'Problem Act' Template ID is referenced in the Associated <act> Element or the CCD 'Problem Observation' Template ID is referenced in the Associated <observation> Element ) -->            
            
            <xsl:when test="n1:code[@code=$loincProblemCode] and
                           (n1:entry/n1:act/n1:templateId/@root=$actTemplateID or n1:entry/n1:observation/n1:templateId/@root=$problemObservationTemplateID)">
                <xsl:apply-templates select="n1:title"/>
                <xsl:call-template name="problemDetails">
                    <xsl:with-param select="." name="section"/>
                </xsl:call-template>
            </xsl:when>
            
            <!-- Display Allergy Section Only if LOINC Allergy Code Is Referenced in One of Document's <section> Elements
                 and ( the CCD 'Problem Act' Template ID is referenced in the Associated <act> Element or the CCD 'Allergy Observation' Template ID is referenced in the Associated <observation> Element ) -->           
            
            <xsl:when test="n1:code[@code=$loincAllergyCode] and
                           (n1:entry/n1:act/n1:templateId/@root=$actTemplateID or n1:entry/n1:observation/n1:templateId/@root=$allergyObservationTemplateID)">
                <xsl:apply-templates select="n1:title"/>
                <xsl:call-template name="allergyDetails">
                    <xsl:with-param select="." name="section"/>
                </xsl:call-template>
            </xsl:when>
            
            <!-- Display Medication Section Only if LOINC Medication Code Is Referenced in One of Document's <section> Elements            
                 and the CCD 'Medication Activity' Template ID is referenced in the Associated <substanceAdministration> Element -->
                 
            <xsl:when test="n1:code[@code=$loincMedCode]and 
                            n1:entry/n1:substanceAdministration/n1:templateId/@root=$substanceAdministrationTemplateID">
                <xsl:apply-templates select="n1:title"/>
                <xsl:call-template name="medDetails">
                    <xsl:with-param select="." name="section"/>
                </xsl:call-template>
            </xsl:when>
            
            <!-- Display Immunization Section Only if LOINC Immunization Code Is Referenced in One of Document's <section> Elements
                 and the CCD 'Medication Activity' Template ID is referenced in the Associated <substanceAdministration> Element -->
                 
            <xsl:when test="n1:code[@code=$loincImmunCode] and 
                            n1:entry/n1:substanceAdministration/n1:templateId/@root=$substanceAdministrationTemplateID">
                <xsl:apply-templates select="n1:title"/>
                <xsl:call-template name="immunDetails">
                    <xsl:with-param select="." name="section"/>
                </xsl:call-template>
            </xsl:when>
            
            <!-- Display Any Other Sections Without Stylesheet Formatting -->
            
        <!-- <xsl:otherwise>
                <xsl:apply-templates select="n1:title"/>
                <xsl:apply-templates select="n1:text"/>
            </xsl:otherwise>   -->
            
        </xsl:choose>
    <xsl:apply-templates select="n1:component/n1:section"/>
</xsl:template>

    <!-- Medication Detail Section -->

<xsl:template name="medDetails">
    <xsl:param name="section"/>
        <table border="0" cellspacing="0" width="95%">
            
            <!-- Section Head -->
             <thead>
                <tr>
                    <td colspan="7"> <hr size="1" color="#999999" noshade="true"/>
                    </td>
                 </tr>                 
                 <tr>
                     
                    <!-- Column Headers -->
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            NAME
                        </font></strong>
                    </th>
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            ROUTE
                        </font></strong>
                    </th>
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            DOSE
                        </font></strong>
                    </th>
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            INTERVAL
                        </font></strong>
                    </th>
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            START DATE
                        </font></strong>
                    </th>
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            EXPIRE DATE
                        </font></strong>
                    </th>
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                           STATUS
                        </font></strong>
                    </th>
                </tr>
                <tr>
                    <td colspan="7">
                        <hr size="1" color="#999999" noshade="true"/>
                    </td>
                </tr>                             
            </thead>
            
            <!-- Section Body -->
            <tbody>
                <xsl:choose>
                    <xsl:when test="$section/n1:entry/n1:substanceAdministration/n1:effectiveTime/n1:high">
                        <xsl:apply-templates select="$section/n1:entry">
                            <xsl:with-param name="sectionCode" select="$section/n1:code/@code"/>
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
        <br/><br/>
</xsl:template>

    <!-- Problem Detail Section -->

<xsl:template name="problemDetails">
    <xsl:param name="section"/>
        <table border="0" cellspacing="0" width="95%">
            
            <!-- Section Head -->
            <thead>
                <tr>
                    <td colspan="3">
                        <hr size="1" color="#999999" noshade="true"/>
                    </td>
                </tr>                            
                <tr>
                    
                    <!-- Column Headers -->
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            TYPE
                        </font></strong>
                    </th>
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            DESCRIPTION
                        </font></strong>
                    </th>
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            DATE
                        </font></strong>
                    </th>                                                               
                </tr>
                <tr>
                    <td colspan="3">
                        <hr size="1" color="#999999" noshade="true"/>
                    </td>
                </tr>                                
            </thead>
            
            <!-- Section Body -->
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
        <br/><br/>
</xsl:template>

    <!-- Allergy Detail Section -->

<xsl:template name="allergyDetails">
    <xsl:param name="section"/>
        <table border="0"  cellspacing="0" width="95%">
            
            <!-- Section Head -->
            <thead>
                <tr>
                    <td colspan="5">
                        <hr size="1" color="#999999" noshade="true"/>
                    </td>
                </tr>
                <tr>
                    
                    <!-- Column Headers -->
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            SUBSTANCE
                        </font></strong>
                    </th>
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            EVENT TYPE
                        </font></strong>
                    </th>
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            REACTION
                        </font></strong>
                    </th>
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            SEVERITY
                        </font></strong>
                    </th>
                    <th align="left">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            STATUS
                        </font></strong>
                    </th>
                </tr>
                <tr>
                    <td colspan="5">
                        <hr size="1" color="#999999" noshade="true"/>
                    </td>
                </tr>
            </thead>
            
            <!-- Section Body -->
            <tbody>
                <xsl:apply-templates select="$section/n1:entry">
                    <xsl:sort select="n1:act/n1:entryRelationship/n1:observation/n1:participant/n1:participantRole/n1:playingEntity/n1:code/@displayName"/>
                </xsl:apply-templates>
            </tbody>
        </table>
        <br/><br/>
</xsl:template>

    <!-- Immuniation Detail Section -->

<xsl:template name="immunDetails">
    <xsl:param name="section"/>
        <table border="0" cellspacing="0" width="95%">
            
            <!-- Section Head -->
            <thead>
                <tr>
                    <td colspan="3">
                        <hr size="1" color="#999999" noshade="true"/>
                    </td>
                </tr>                             
                <tr>
                    
                    <!-- Column Headers -->
                    <th align="left" valign="top">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            VACCINE
                        </font></strong>
                    </th>
                    <th align="left" valign="top">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            DATE
                        </font></strong>
                    </th>
                    <th align="left" valign="top">
                        <strong><font size="1" color="#666666" face="Arial, Helvetica, sans-serif">
                            STATUS
                        </font></strong>
                    </th> 
                </tr>
                <tr>
                    <td colspan="3">
                        <hr size="1" color="#999999" noshade="true"/>
                    </td>
                </tr>                             
            </thead>
            
            <!-- Section Body -->
            <tbody>
               <xsl:apply-templates select="$section/n1:entry">
				   <xsl:with-param name="sectionCode" select="$section/n1:code/@code"/>
                   <xsl:sort select="$section/n1:entry/n1:substanceAdministration/n1:effectiveTime" order="descending"/>
               </xsl:apply-templates>            
            </tbody>
        </table>
        <br/><br/>        
</xsl:template>            

    <!-- Entry Processing -->

<xsl:template match="n1:entry">
	<xsl:param name="sectionCode" select="."/>
    <xsl:choose>
        
        <!-- Display Problem Entry Row Only if SNOWMED Problem Code Is Referenced in One of Document's <section><entry> Elements -->
        
        <xsl:when test="n1:act/n1:entryRelationship/n1:observation/n1:code/@code=$snomedProblemCode">
            <xsl:call-template name="problemRow">
                <xsl:with-param name="row" select="."/>
            </xsl:call-template>
        </xsl:when>
        
        <!-- Display Allergy Entry Row Only if the CCD 'Allergy Observation' Template ID Is Referenced in the <section><entry> Elements -->
        
        <xsl:when test="n1:act/n1:entryRelationship/n1:observation/n1:templateId/@root=$allergyObservationTemplateID">
            <xsl:call-template name="allergyRow">
                <xsl:with-param name="row" select="."/>
            </xsl:call-template>
        </xsl:when>
        
        <!-- Display Medication entry Row Only if LOINC Medication Code Is Referenced in the <section><entry> Elements
        and the CCD 'Medication Activity' Template ID is referenced in the Associated <substanceAdministration> Element -->
        
        <xsl:when test="n1:substanceAdministration/n1:templateId/@root=$substanceAdministrationTemplateID and $sectionCode=$loincMedCode">         
            <xsl:call-template name="medRow">
                <xsl:with-param name="row" select="."/>
            </xsl:call-template>
        </xsl:when>
        
        <!-- Display Immunization Entry Row Only if LOINC Immunization Code Is Referenced Is Referenced in the <section><entry> Elements
        and the CCD 'Medication Activity' Template ID is Referenced in the Associated <substanceAdministration> Element -->
        
        <xsl:when test="n1:substanceAdministration/n1:templateId/@root=$substanceAdministrationTemplateID and $sectionCode=$loincImmunCode">
            <xsl:call-template name="immunRow">
                <xsl:with-param name="row" select="."/>
            </xsl:call-template>
        </xsl:when>
        
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
                                <xsl:when test="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/@displayName">
                                    <font size="1" face="Arial, Helvetica, sans-serif">
                                        <xsl:value-of select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/@displayName"/>
                                    </font>
                                </xsl:when>
                                <xsl:otherwise>
                                    <font size="1" face="Arial, Helvetica, sans-serif">
                                        <xsl:value-of select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/n1:originalText"/>
                                    </font>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:when test="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:name">
                            <font size="1" face="Arial, Helvetica, sans-serif">
                                <xsl:value-of select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:name"/>
                            </font>
                        </xsl:when>
                    </xsl:choose>
                </td>

                <!-- Route -->
                <td>
                    <xsl:if test="$row/n1:substanceAdministration/n1:routeCode/@displayName">                
                        <font size="1" face="Arial, Helvetica, sans-serif">
                            <xsl:value-of select="$row/n1:substanceAdministration/n1:routeCode/@displayName"/>
                        </font>
                    </xsl:if>
                </td>

                <!-- Dose -->
                <td>
                    <xsl:if test="$row/n1:substanceAdministration/n1:doseQuantity/@value">
                        <font size="1" face="Arial, Helvetica, sans-serif">
                            <xsl:value-of select="$row/n1:substanceAdministration/n1:doseQuantity/@value"/>
                        </font>
                    </xsl:if>
                </td>

                <!-- Interval -->
                <td>
                    <xsl:choose>
                        <xsl:when test="$row/n1:substanceAdministration/n1:effectiveTime/n1:period/@value">
                            <font size="1" face="Arial, Helvetica, sans-serif">
                                <xsl:value-of select="$row/n1:substanceAdministration/n1:effectiveTime/n1:period/@value"/>
                            </font>
                            <font size="1" face="Arial, Helvetica, sans-serif">
                                <xsl:value-of select="$row/n1:substanceAdministration/n1:effectiveTime/n1:period/@unit"/>
                            </font>
                        </xsl:when>                          
                        <xsl:when test="$row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:quantity">
                            <font size="1" face="Arial, Helvetica, sans-serif">                                
                                <xsl:value-of select="$row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:quantity/@value"/>
                            </font>
                            <font size="1" face="Arial, Helvetica, sans-serif">
                                <xsl:value-of select="$row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:id/@root"/>
                            </font>
                        </xsl:when>
                    </xsl:choose>
                </td>

                <!-- Start Date -->
                <td>
                    <xsl:if test="$row/n1:substanceAdministration/n1:effectiveTime/n1:low/@value">
                        <font size="1" face="Arial, Helvetica, sans-serif">
                            <xsl:call-template name="formatMediumDate">
                                <xsl:with-param name="date" select="$row/n1:substanceAdministration/n1:effectiveTime/n1:low/@value"/>
                            </xsl:call-template>
                        </font>
                    </xsl:if>
                </td>

            <!-- Expire Date -->
            <td>
                <xsl:choose>
                    <xsl:when test="$row/n1:substanceAdministration/n1:effectiveTime/n1:high/@value">
                        <font size="1" face="Arial, Helvetica, sans-serif">
                            <xsl:call-template name="formatMediumDate">
                                <xsl:with-param name="date" select="$row/n1:substanceAdministration/n1:effectiveTime/n1:high/@value"/>
                            </xsl:call-template>
                        </font>
                    </xsl:when>
                    <xsl:when test="$row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:effectiveTime/@value">
                        <font size="1" face="Arial, Helvetica, sans-serif">
                            <xsl:call-template name="formatMediumDate">
                                <xsl:with-param name="date" select="$row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:effectiveTime/@value"/>
                            </xsl:call-template>
                        </font>                           
                    </xsl:when>
                </xsl:choose>
            </td>

            <!-- Status -->
            <td>
                <xsl:choose>
                    <xsl:when test="$row/n1:substanceAdministration/n1:entryRelationship[@typeCode='REFR']/n1:observation/n1:value/@displayName">
                        <font size="1" face="Arial, Helvetica, sans-serif">
                            <xsl:value-of select="$row/n1:substanceAdministration/n1:entryRelationship[@typeCode='REFR']/n1:observation/n1:value/@displayName"/>
                        </font>
                    </xsl:when>
                    <xsl:when test="$row/n1:substanceAdministration/n1:statusCode/@code">
                        <font size="1" face="Arial, Helvetica, sans-serif">
                            <xsl:value-of select="$row/n1:substanceAdministration/n1:statusCode/@code"/>
                        </font>
                    </xsl:when>
                </xsl:choose>
            </td>
        </tr>
</xsl:template>

    <!-- Problem Entry row -->
        
<xsl:template name="problemRow">
    <xsl:param name="row"/>
        <xsl:variable name="observation" select="$row/n1:act/n1:entryRelationship/n1:observation"/>
            <tr>

                <!-- Problem Name -->
                <td>
                    <xsl:if test="$observation/n1:code/@displayName">
                        <font size="1" face="Arial, Helvetica, sans-serif">
                            <xsl:value-of select="$observation/n1:code/@displayName"/>
                        </font>
                    </xsl:if>
                </td>			

                <!-- Problem Description -->
                <td>
                    <xsl:if test="$observation/n1:value/@displayName">
                        <font size="1" face="Arial, Helvetica, sans-serif">   
                            <xsl:value-of select="$observation/n1:value/@displayName"/>
                        </font>
                    </xsl:if>
                </td>			

                <!-- Problem Effective Date -->
                <td>
                    <xsl:choose>
                        <xsl:when test="$row/n1:act/n1:effectiveTime/n1:low">
                            <font size="1" face="Arial, Helvetica, sans-serif">                                    
                                <xsl:call-template name="formatMediumDate">
                                    <xsl:with-param name="date" select="$row/n1:act/n1:effectiveTime/n1:low/@value"/>
                                </xsl:call-template>
                            </font>
                        </xsl:when>
                        <xsl:when test="$observation/n1:effectiveTime/n1:low/@value">
                            <font size="1" face="Arial, Helvetica, sans-serif">                                    
                                <xsl:call-template name="formatMediumDate">
                                    <xsl:with-param name="date" select="$observation/n1:effectiveTime/n1:low/@value"/>
                                </xsl:call-template>
                            </font>
                        </xsl:when>
                    </xsl:choose>
                </td>
            </tr>
</xsl:template>

    <!-- Allergy Entry row -->
  
<xsl:template name="allergyRow">
    <xsl:param name="row"/>
        <xsl:variable name="observation" select="$row/n1:act/n1:entryRelationship/n1:observation"/>
            <tr>

                <!-- Substance -->                        
                <td>
                    <xsl:if test="$observation/n1:participant/n1:participantRole/n1:playingEntity/n1:code/@displayName">
                        <font size="1" face="Arial, Helvetica, sans-serif">    
                            <xsl:value-of select="$observation/n1:participant/n1:participantRole/n1:playingEntity/n1:code/@displayName"/>
                        </font>
                    </xsl:if>                    
                </td>

                <!-- Event Type-->
                <td>
                    <xsl:if test="$observation/n1:code/@displayName">
                        <font size="1" face="Arial, Helvetica, sans-serif"> 
                            <xsl:value-of select="$observation/n1:code/@displayName"/>
                        </font>
                    </xsl:if>
                </td>

                <!-- Reaction-->                        
                <td>
                    <xsl:if test="$observation/n1:entryRelationship[@typeCode='MFST']/n1:observation/n1:value/@displayName">                    
                        <font size="1" face="Arial, Helvetica, sans-serif"> 
                            <xsl:value-of select="$observation/n1:entryRelationship[@typeCode='MFST']/n1:observation/n1:value/@displayName"/>
                        </font>
                    </xsl:if>                    
                </td>

                <!-- Severity-->                        
                <td>
                    <xsl:if test="$observation/n1:entryRelationship[@typeCode='SUBJ']/n1:observation/n1:value/@displayName">
                        <font size="1" face="Arial, Helvetica, sans-serif"> 
                            <xsl:value-of select="$observation/n1:entryRelationship[@typeCode='SUBJ']/n1:observation/n1:value/@displayName"/>
                        </font> 
                    </xsl:if>
                </td>

                <!-- Status-->                        
                <td>
                    <xsl:if test="$observation/n1:entryRelationship[@typeCode='REFR']/n1:observation/n1:value/@displayName">
                        <font size="1" face="Arial, Helvetica, sans-serif"> 
                            <xsl:value-of select="$observation/n1:entryRelationship[@typeCode='REFR']/n1:observation/n1:value/@displayName"/>
                        </font>
                    </xsl:if>
                </td>
            </tr>
</xsl:template>
        
    <!-- Immunization Entry row -->
        
<xsl:template name="immunRow">
    <xsl:param name="row"/>
        <xsl:variable name="code" select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code"/>
        <tr>

        <!-- Vaccine -->                        
            <td>
              <xsl:choose>
                <xsl:when test="$code/@displayName"> 
                    <font size="1" face="Arial, Helvetica, sans-serif">
                        <xsl:value-of select="$code/@displayName"/>
                    </font>
                </xsl:when>
                <xsl:when test="$code/n1:originalText">
                    <font size="1" face="Arial, Helvetica, sans-serif">
                        <xsl:value-of select="$code/n1:originalText"/>
                    </font>
                </xsl:when>
            </xsl:choose>
            </td>

            <!-- Immunization Date -->                    
            <td>
                <xsl:if test="$row/n1:substanceAdministration/n1:effectiveTime/n1:center/@value">                 
                    <font size="1" face="Arial, Helvetica, sans-serif">
                        <xsl:call-template name="formatMediumDate">
                            <xsl:with-param name="date" select="$row/n1:substanceAdministration/n1:effectiveTime/n1:center/@value"/>
                        </xsl:call-template>
                    </font>
                </xsl:if>
            </td>
            
            <!-- Status -->                    
            <td>
                <xsl:if test="$row/n1:substanceAdministration/n1:statusCode/@code">
                    <font size="1" face="Arial, Helvetica, sans-serif">
                        <xsl:value-of select="$row/n1:substanceAdministration/n1:statusCode/@code"/>
                    </font>
                </xsl:if>
            </td>
        </tr>
</xsl:template>

    <!-- Sort - Medications Section: CCD template identifier 2.16.840.1.113883.10.20.1.8 -->        

<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.8']/n1:text/n1:table/n1:tbody">
    <xsl:apply-templates>
        <xsl:sort select="n1:td[5]" order="descending"/>
    </xsl:apply-templates>
</xsl:template>

    <!-- Format Date - Medications Section: CCD template identifier 2.16.840.1.113883.10.20.1.8 --> 

<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.8']/n1:text/n1:table/n1:tbody/n1:tr/n1:td[5]">
    <td>
        <xsl:call-template name="formatDate">
            <xsl:with-param name="date"
                select="text()"/>
        </xsl:call-template>
    </td>
</xsl:template>

    <!-- Sort - Problem Section: CCD template identifier 2.16.840.1.113883.10.20.1.11 -->        

<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.11']/n1:text/n1:table/n1:tbody">
    <xsl:apply-templates>
        <xsl:sort select="n1:td[3]" order="descending"/>
    </xsl:apply-templates>
</xsl:template>

    <!-- Format Date - Problem Section: CCD template identifier 2.16.840.1.113883.10.20.1.11 -->  

<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.11']/n1:text/n1:table/n1:tbody/n1:tr/n1:td[3]">
    <td>
        <xsl:call-template name="formatDate">
            <xsl:with-param name="date"
                select="text()"/>
        </xsl:call-template>
    </td>
</xsl:template>

    <!-- Sort - Immunizations Section: CCD template identifier 2.16.840.1.113883.10.20.1.6 -->        

<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.6']/n1:text/n1:table/n1:tbody">
    <xsl:apply-templates>
        <xsl:sort select="n1:td[3]" order="descending"/>
    </xsl:apply-templates>
</xsl:template>

    <!-- Format Date - Immunizations Section: CCD template identifier 2.16.840.1.113883.10.20.1.6 -->  

<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.6']/n1:text/n1:table/n1:tbody/n1:tr/n1:td[3]">
    <td>
        <xsl:call-template name="formatDate">
            <xsl:with-param name="date"
                select="text()"/>
        </xsl:call-template>
    </td>
</xsl:template>


    <!-- Sort - Vital Signs Section: CCD template identifier 2.16.840.1.113883.10.20.1.16
                Results Section: CCD template identifier 2.16.840.1.113883.10.20.1.14
                Encounters Section: CCD template identifier 2.16.840.1.113883.10.20.1.3 -->

<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.16' or n1:templateId/@root='2.16.840.1.113883.10.20.1.14' or n1:templateId/@root='2.16.840.1.113883.10.20.1.3']/n1:text/n1:table/n1:tbody">
    <xsl:apply-templates>
        <xsl:sort select="n1:td[2]" order="descending"/>
    </xsl:apply-templates>
</xsl:template>

    <!-- Format Date - Vital Signs Section: CCD template identifier 2.16.840.1.113883.10.20.1.16
                       Results Section: CCD template identifier 2.16.840.1.113883.10.20.1.14
                       Encounters Section: CCD template identifier 2.16.840.1.113883.10.20.1.3 -->

<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.16' or n1:templateId/@root='2.16.840.1.113883.10.20.1.14' or n1:templateId/@root='2.16.840.1.113883.10.20.1.3']/n1:text/n1:table/n1:tbody/n1:tr/n1:td[2]">
    <td>
        <xsl:call-template name="formatDate">
            <xsl:with-param name="date" select="concat(substring(text(),1,4),substring(text(),6,2),substring(text(),9,2))"/>
        </xsl:call-template>
    </td>
</xsl:template>

    <!--  Format Date 
    
      outputs a date in Month Day, Year form
      e.g., 19991207  ==> December 07, 1999 -->

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

    <!--  Format Date 
    
      outputs a date in Month Day, Year form
      e.g., 19991207  ==> December 07, 1999 -->
        
<xsl:template name="formatMediumDate">
    <xsl:param name="date"/>
        <xsl:variable name="month" select="substring ($date, 5, 2)"/>
            <xsl:value-of select="substring ($date, 7, 2)"/>
            <xsl:text>-</xsl:text>
            <xsl:choose>
                <xsl:when test="$month='01'">
                    <xsl:text>JAN</xsl:text>
                </xsl:when>
                <xsl:when test="$month='02'">
                    <xsl:text>FEB</xsl:text>
                </xsl:when>
                <xsl:when test="$month='03'">
                    <xsl:text>MAR</xsl:text>
                </xsl:when>
                <xsl:when test="$month='04'">
                    <xsl:text>APR</xsl:text>
                </xsl:when>
                <xsl:when test="$month='05'">
                    <xsl:text>MAY</xsl:text>
                </xsl:when>
                <xsl:when test="$month='06'">
                    <xsl:text>JUN</xsl:text>
                </xsl:when>
                <xsl:when test="$month='07'">
                    <xsl:text>JUL</xsl:text>
                </xsl:when>
                <xsl:when test="$month='08'">
                    <xsl:text>AUG</xsl:text>
                </xsl:when>
                <xsl:when test="$month='09'">
                    <xsl:text>SEP</xsl:text>
                </xsl:when>
                <xsl:when test="$month='10'">
                    <xsl:text>OCT</xsl:text>
                </xsl:when>
                <xsl:when test="$month='11'">
                    <xsl:text>NOV</xsl:text>
                </xsl:when>
                <xsl:when test="$month='12'">
                    <xsl:text>DEC</xsl:text>
                </xsl:when>
            </xsl:choose>
                <xsl:text>-</xsl:text>
                <xsl:value-of select="substring ($date, 3, 2)"/>
</xsl:template>    

<!-- Text -->

<xsl:template match="n1:text">		 
    <xsl:apply-templates/>
</xsl:template>

    <!-- Paragraph -->
    
<xsl:template match="n1:paragraph">
    <p>
        <xsl:apply-templates/>
    </p>
</xsl:template>

    <!-- Content w/ Deleted Text is Hidden -->
    
<xsl:template match="n1:content[@revised='delete']"/>

    <!-- Content -->

<xsl:template match="n1:content">
    <xsl:apply-templates/>
</xsl:template>

    <!-- List -->
    
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
		 
    <!-- Caption -->

<xsl:template match="n1:caption">  
    <xsl:apply-templates/>
    <xsl:text>: </xsl:text>
</xsl:template>
		 
    <!-- Tables  -->
 
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

    <!-- RenderMultiMedia 

         this currently only handles GIF's and JPEG's.  It could, however,
	 be extended by including other image MIME types in the predicate
	 and/or by generating <object> or <applet> tag with the correct
	 params depending on the media type  @ID  =$imageRef     referencedObject    -->
         
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

    <!-- Stylecode Processing: Supports Bold, Underline and Italics display -->

<xsl:template match="//n1:*[@styleCode]">

        <!-- Bold -->
    <xsl:if test="@styleCode='Bold'">
        <xsl:element name='b'>		 		 		 		 
           <xsl:apply-templates/>
        </xsl:element>		 
    </xsl:if> 
    
        <!-- Italics -->
    <xsl:if test="@styleCode='Italics'">
        <xsl:element name='i'>		 		 		 		 
            <xsl:apply-templates/>
        </xsl:element>		 
    </xsl:if>

        <!-- Underline -->
    <xsl:if test="@styleCode='Underline'">
        <xsl:element name='u'>		 		 		 		 
            <xsl:apply-templates/>
        </xsl:element>		 
    </xsl:if>

        <!-- Bold & Italics -->
    <xsl:if test="contains(@styleCode,'Bold') and contains(@styleCode,'Italics') and not (contains(@styleCode, 'Underline'))">
        <xsl:element name='b'>
            <xsl:element name='i'>		 		 		 		 
                <xsl:apply-templates/>
            </xsl:element>
        </xsl:element>		 
    </xsl:if>

        <!-- Bold & Underline -->
    <xsl:if test="contains(@styleCode,'Bold') and contains(@styleCode,'Underline') and not (contains(@styleCode, 'Italics'))">
        <xsl:element name='b'>
            <xsl:element name='u'>		 		 		 		 
                <xsl:apply-templates/>
            </xsl:element>
        </xsl:element>		 
    </xsl:if>

        <!-- Italics & Underline -->
    <xsl:if test="contains(@styleCode,'Italics') and contains(@styleCode,'Underline') and not (contains(@styleCode, 'Bold'))">
        <xsl:element name='i'>
            <xsl:element name='u'>		 		 		 		 
                <xsl:apply-templates/>
            </xsl:element>
        </xsl:element>		 
    </xsl:if>
    
        <!-- Bold, Italics & Underline -->
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

    <!-- Superscript or Subscript   -->

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

</xsl:stylesheet>