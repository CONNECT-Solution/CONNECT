<?xml version="1.0"?>
<?altova_samplexml C:\MyData\Projects\NHIE 2009\SD Pilot\CCDs\KP\CHDRONE_VA_ccd.xml?>
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
	<xsl:variable name="problemTemplateCode">2.16.840.1.113883.10.20.1.28</xsl:variable>
	<xsl:variable name="title">
		<xsl:choose>
			<xsl:when test="string-length(/n1:ClinicalDocument/n1:title)=0">
				<xsl:text>Clinical Document</xsl:text>
			</xsl:when>
			<xsl:when test="/n1:ClinicalDocument/n1:title">
				<xsl:value-of select="/n1:ClinicalDocument/n1:title"/>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:template match="/">
		<xsl:apply-templates select="n1:ClinicalDocument"/>
	</xsl:template>
	<xsl:template match="n1:ClinicalDocument">
		<html>
			<head>
				<link href="CCDStyleFrank.css" rel="stylesheet"/>
				<!-- <meta name='Generator' content='&CDA-Stylesheet;'/> -->
				<xsl:comment>
                        Do NOT edit this HTML directly, it was generated via an XSLt
                        transformation from the original release 2 CDA Document.
                </xsl:comment>
				<title>
					<xsl:value-of select="$title"/>
				</title>
				<style type="text/css">

body {
	border-right-width: 0px;
	border-top-width: 0px;
	border-left-width: 0px;
	border-bottom-width: 0px;
	padding-top: 0px;
	padding-bottom: 0px;
	padding-left: 0px;
	padding-right: 0px;
	margin-top: 0px;
	margin-bottom: 0px;
	margin-left: 0px;
	margin-right: 0px;
	border-collapse: collapse
}

table.first {
	text-align: left;
	vertical-align: top;
	background-color: #CCCCff;
	border-right: 3px solid #002452;
	border-top: 3px solid #002452;
	border-left: 3px solid #002452;
	border-bottom: 3px solid #002452;
	padding-top: 0px;
	padding-bottom: 0px;
	padding-left: 0px;
	padding-right: 0px;
	margin-top: 0px;
	margin-bottom: 0px;
	margin-left: 0px;
	margin-right: 0px;
	font: 95% "Times New Roman";
	border-collapse: collapse
}

table.second {
	text-align: left;
	vertical-align: top;
	background-color: #CCCCff;
	border-right: 3px solid #002452;
	border-top: 0px solid #002452;
	border-left: 3px solid #002452;
	border-bottom: 3px solid #002452;
	padding-top: 0px;
	padding-bottom: 0px;
	padding-left: 0px;
	padding-right: 0px;
	margin-top: 0px;
	margin-bottom: 0px;
	margin-left: 0px;
	margin-right: 0px;
	font: 95% "Times New Roman";
	border-collapse: collapse
}

th.first {
	text-align: left;
	vertical-align: top;
	color: black;
	xbackground-color: #002452;
	font: bold 95% "Times New Roman";
	padding-left: 3px;
	padding-right: 3px;
	border-collapse: collapse
}


tr.first {
	text-align: left;
	vertical-align: top;
	color: black;
	<!--background-color: #E2E0E0;-->
	xbackground-color: #E8F0F0;
	padding-top: 3px;
	padding-bottom: 3px;
	padding-left: 9px;
	padding-right: 3px;
	border-collapse: collapse
}

td.first  {
	padding-left: 3px;
	padding-right: 3px;
	padding-top: 2px;
	padding-bottom: 3px;
	color: white;
	xbackground-color: #002452;
}

tr.second {
	text-align: left;
	vertical-align: top;
	color: black;
	<!--background-color: #F9F4EF;  F0F5F5-->
	background-color: #CCCCff;
	padding-top: 3px;
	padding-bottom: 3px;
	padding-left: 9px;
	padding-right: 3px;
	border-collapse: collapse
}


#smenu {
    z-index: 1;
    position: absolute;
    top: 45px;
    left: 685px;
	width: 100%;
	float: left;
	text-align: right;
	color: #000;
}
                </style>
				<style type="text/css">
#menu {
	position: absolute;
	top: 45px;
	left: 0px;
    z-index: 1;
	float: left;
	text-align: right;
	color: #000;
	list-style: none;
	line-height: 1;
}
                </style>
				<xsl:comment><![CDATA[[if lt IE 7]>
<style type="text/css">
#menu {
	display: none;
}
</style>
<![endif]]]></xsl:comment>
				<style type="text/css">

#menu ul {
	list-style: none;
	margin: 0;
	padding: 0;
	width: 12em;
	float: right;
	text-align: right;
	color: #000;
}

#menu a, #menu h2 {
	font: bold 11px/16px arial, helvetica, sans-serif;
	text-align: right;
	display: block;
	border-width: 0px;
	border-style: solid;
	border-color: #ccc #888 #555 #bbb;
	margin: 0;
	padding: 2px 3px;
	color: #000;
}

#menu h2 {
	color: #fff;
	text-transform: uppercase;
	text-align: right;
}

#menu a {
	text-decoration: none;
	text-align: right;
	border-width: 1px;
	border-style: solid;
	border-color: #fff #777 #777 #777;
}

#menu a:hover {
	color: #000;
	background: #fff;
	text-align: right;
}

#menu li {
	position: relative;
}

#menu ul ul {
	position: relative;
	z-index: 500;
	text-align: left;
	color: #000;
	background-color: #E0E5E5;
	float: right;
}

#menu ul ul ul {
	position: absolute;
	top: 0;
	left: 100%;
	text-align: right;
	float: right;
}

div#menu ul ul,
div#menu ul li:hover ul ul,
div#menu ul ul li:hover ul ul
{display: none;}

div#menu ul li:hover ul,
div#menu ul ul li:hover ul,
div#menu ul ul ul li:hover ul
{display: block;}

                </style>
			</head>
			<xsl:comment>

            </xsl:comment>
			<body>
				<script type="text/javascript">
var TipBoxID = "TipBox";
var tip_box_id;
function findPosX(obj)
{
   var curleft = 0;
   if(obj.offsetParent)
   while(1)
   {
      curleft += obj.offsetLeft;
      if(!obj.offsetParent)
         break;
      obj = obj.offsetParent;
   }
   else if(obj.x)
      curleft += obj.x;
   return curleft;
}

function findPosY(obj)
{
   var curtop = 0;
   if(obj.offsetParent)
   while(1)
   {
      curtop += obj.offsetTop;
      if(!obj.offsetParent)
         break;
      obj = obj.offsetParent;
   }
   else if(obj.y)
      curtop += obj.y;
   return curtop;
}

function HideTip() {
 tip_box_id.style.display = "none";
}

function DisplayTip(me,offX,offY) {
   var content = me.innerHTML;
   var tdLength = me.parentNode.offsetWidth;
   var textLength = me.innerHTML.length;
       if(((textLength-1)*10) > tdLength) {
          var tipO = me;
          tip_box_id = document.getElementById(TipBoxID);
          var x = findPosX(me);
          var y = findPosY(me);
          var left = x + offX - 100;

          if( left &lt; 0) {
            left = 0;
          }
          var top = y + offY - 10;

          tip_box_id.style.left = String(parseInt(left) + 'px');
          tip_box_id.style.top = String(parseInt(top) + 'px');
          tip_box_id.innerHTML = content;
          tip_box_id.style.display = "block";
          tipO.onmouseout = HideTip;
       }

}


                </script>
				<!-- source -->
				<!-- title -->
				<div style="text-align:center;">
					<span style="font-size:larger;font-weight:bold;">
						<xsl:call-template name="documentTitle">
							<xsl:with-param name="root" select="."/>
						</xsl:call-template>
					</span>
					<span align="center" style="; margin-left: 50px;">
						<b>
							<xsl:text>Created On: </xsl:text>
						</b>
						<xsl:choose>
							<xsl:when test="string-length(/n1:ClinicalDocument/n1:effectiveTime/@value)=0">
								<xsl:text>-- Not Available --</xsl:text>
							</xsl:when>
							<xsl:when test="starts-with(/n1:ClinicalDocument/n1:effectiveTime/@value,' ')">
								<xsl:text>-- Not Available --</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="formatDateFull">
									<xsl:with-param name="date" select="/n1:ClinicalDocument/n1:effectiveTime/@value"/>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:if test="/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:effectiveTime/n1:low/@value">
							<xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;</xsl:text>
							<b>
								<xsl:text>Date Range: </xsl:text>
							</b>
							<xsl:choose>
								<xsl:when test="string-length(/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:effectiveTime/n1:low/@value)=0">
									<xsl:text>-- Not Available --</xsl:text>
								</xsl:when>
								<xsl:when test="starts-with(/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:effectiveTime/n1:low/@value,' ')">
									<xsl:text>-- Not Available --</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="formatDateFull">
										<xsl:with-param name="date" select="/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:effectiveTime/n1:low/@value"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<b>
								<xsl:text disable-output-escaping="yes"> - </xsl:text>
							</b>
							<xsl:choose>
								<xsl:when test="string-length(/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:effectiveTime/n1:high/@value)=0">
									<xsl:text>-- Not Available --</xsl:text>
								</xsl:when>
								<xsl:when test="starts-with(/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:effectiveTime/n1:high/@value,' ')">
									<xsl:text>-- Not Available --</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="formatDateFull">
										<xsl:with-param name="date" select="/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:effectiveTime/n1:high/@value"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
					</span>
				</div>
				<!-- Uncomment below to enable hover over of disclaimer 091014 jhc-->
				<!--p align="center" onmouseover="DisplayTip(this,25,-20)" style="height:20px; overflow:hidden"091014 jhc-->
				
				<div style="
      font-size:10px;
      font-weight:bold;
      font-family:verdana;
      padding:15px;
      color:black;
      background-color:#FFFFFF;">
				<p align="left" style="height:20px; ">
					<!--br>Disclaimer (hover over to read)<br/ 091014 jhc-->
                        IMPORTANT:  All information below should be verified for current accuracy.  In particular, some behavioral health and chemical dependency diagnoses may not be displayed.
                </p></div>
				<!-- Report ID#'s -->
				<table width="100%" class="first">
					<xsl:variable name="patientRole" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole"/>
					<tr>
						<td width="15%" valign="top">
							<b>
								<xsl:text>Patient: </xsl:text>
							</b>
						</td>
						<td width="35%" valign="top">
							<xsl:call-template name="getName">
								<xsl:with-param name="name" select="$patientRole/n1:patient/n1:name"/>
							</xsl:call-template>
							<xsl:if test="$patientRole/n1:addr">
								<xsl:call-template name="getAddress">
									<xsl:with-param name="addr" select="$patientRole/n1:addr"/>
								</xsl:call-template>
							</xsl:if>
							
							<xsl:choose>
								<xsl:when test="string-length($patientRole/n1:telecom/@value)=0">
									<br/>
									<b>
										<xsl:text>tel: PATIENT PHONE MISSING</xsl:text>
									</b>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="$patientRole/n1:telecom">
    									<xsl:call-template name="getTelecom">
										<xsl:with-param name="telecom" select="$patientRole/n1:telecom"/>
										</xsl:call-template>
									</xsl:for-each>
								</xsl:otherwise>
							 </xsl:choose>
						</td>
						<td width="15%" align="right" valign="top">
							<b>
								<xsl:text>Patient ID: </xsl:text>
							</b>
						</td>
						<td width="35%" valign="top">
							<xsl:if test="string-length($patientRole/n1:id/@extension)>0">
								<xsl:value-of select="$patientRole/n1:id/@extension"/>
							</xsl:if>
						</td>
					</tr>
					<tr>
						<td width="15%" valign="top">
							<b>
								<xsl:text>Birthdate: </xsl:text>
							</b>
						</td>
						<td width="35%" valign="top">
							<xsl:call-template name="formatDateFull">
								<xsl:with-param name="date" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:birthTime/@value"/>
							</xsl:call-template>
						</td>
						<td width="15%" align="right" valign="top">
							<b>
								<xsl:text>Sex: </xsl:text>
							</b>
						</td>
						<td width="35%" valign="top">
							<xsl:variable name="sex" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:administrativeGenderCode/n1:originalText"/>
							<xsl:choose>
								<xsl:when test="$sex='M'">Male</xsl:when>
								<xsl:when test="$sex='m'">Male</xsl:when>
								<xsl:when test="$sex='Male'">Male</xsl:when>
								<xsl:when test="$sex='male'">Male</xsl:when>
								<xsl:when test="$sex='F'">Female</xsl:when>
								<xsl:when test="$sex='f'">Female</xsl:when>
								<xsl:when test="$sex='Female' ">Female</xsl:when>
								<xsl:when test="$sex='female' ">Female</xsl:when>
								<xsl:otherwise>
									<xsl:text>Unknown</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<xsl:if test="starts-with($patientRole/n1:patient/n1:languageCommunication/n1:languageCode/@nullFlavor,'UNK') != 'true'">
						<tr>
							<td width="15%" valign="top">
								<b>
									<xsl:text>Language(s):</xsl:text>
								</b>
							</td>
							<td width="35%" valign="top">
								<xsl:apply-templates select="$patientRole/n1:patient/n1:languageCommunication"/>
							</td>
							<td width="15%" valign="top"/>
							<td width="35%" valign="top"/>
						</tr>
					</xsl:if>
				</table>
				<xsl:if test="n1:author">
					<table width="100%" class="second">
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
								<td width="15%" valign="top"/>
								<td width="85%" valign="top">Author:
                                    <xsl:call-template name="getName">
										<xsl:with-param name="name" select="n1:author/n1:assignedAuthor/n1:assignedPerson"/>
									</xsl:call-template>
								</td>
							</tr>
						</xsl:if>
					</table>
				</xsl:if>
				<div>
					<h3>
						<a name="toc">Table of Contents</a>
					</h3>
					<table>
						<tr>
							<xsl:for-each select="n1:component/n1:structuredBody/n1:component/n1:section/n1:title">
								
								<td>
									<xsl:value-of select="n1:component/n1:structuredBody/n1:component/n1:section/n1:title"/>
									<a href="#{generate-id(.)}">
										<xsl:value-of select="."/>
									</a>
								</td>
							</xsl:for-each>
						</tr>
					</table>
				</div>
				<xsl:apply-templates select="n1:component/n1:structuredBody"/>
				<br/>
				<br/>
				<xsl:choose>
					<xsl:when test="string-length(/n1:ClinicalDocument/n1:participant[@typeCode='IND']/n1:associatedEntity[@classCode='NOK'])>0">
						<table class="first">
							<tr>
								<td width="100px" valign="top" align="left">
									<b>Emergency Contact: </b>
								</td>
								<td valign="top">
									<xsl:call-template name="getParticipant">
										<xsl:with-param name="participant" select="/n1:ClinicalDocument/n1:participant[@typeCode='IND']/n1:associatedEntity[@classCode='NOK']"/>
									</xsl:call-template>
								</td>
								<td width="50px"> </td>
							</tr>
						</table>
					</xsl:when>
					<xsl:otherwise>
						<b><xsl:text>EMERGENCY CONTACT INFO MISSING!</xsl:text></b>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:call-template name="bottomline"/>
				<div id="TipBox" style="
      display:none;
      position:absolute;
      font-size:12px;
      font-weight:bold;
      font-family:verdana;
      border:#72B0E6 solid 1px;
      padding:15px;
      color:black;
      background-color:#FFFFFF;">
                </div>
			</body>
		</html>
	</xsl:template>
	<xsl:template name="getParticipant">
		<xsl:param name="participant"/>
		<p>
			<xsl:call-template name="getName">
				<xsl:with-param name="name" select="$participant/n1:associatedPerson/n1:name"/>
			</xsl:call-template>
			<xsl:if test="$participant/n1:addr">
				<xsl:call-template name="getSingleAddress">
					<xsl:with-param name="addr" select="$participant/n1:addr"/>
				</xsl:call-template>
			</xsl:if>
			<xsl:choose>
				<xsl:when test="string-length($participant/n1:telecom/@value)=0">
					<br/>
						<b>
							<xsl:text>tel: CONTACT PHONE MISSING</xsl:text>
						</b>
				</xsl:when>
				<xsl:otherwise>
				<!--/ClinicalDocument/participant/associatedEntity/telecom[2]/@value-->
					<xsl:for-each select="$participant/n1:telecom">
						<!--br/-->
    					<xsl:call-template name="getTelecom">
						<xsl:with-param name="telecom" select="$participant/n1:telecom"/>
						<!--xsl:with-param name="telecom" select="."/-->
						</xsl:call-template>
						<!--xsl:value-of select="$participant/n1:telecom/@value"/-->
					</xsl:for-each>
				</xsl:otherwise>
			 </xsl:choose>
			 
			<xsl:if test="$participant/n1:code/n1:originalText">
				<br/>
            <b>Relationship:  </b>
			<xsl:value-of select="$participant/n1:code/n1:originalText"/>
			</xsl:if>
		</p>
	</xsl:template>
	<xsl:template name="getSingleAddress">
		<xsl:param name="addr"/>
		<xsl:if test="$addr/n1:streetAddressLine != ' '">
			<br/>
			<xsl:if test="string-length($addr/n1:streetAddressLine)>0">
				<xsl:value-of select="$addr/n1:streetAddressLine"/>
			</xsl:if>
			<br/>
			<xsl:value-of select="$addr/n1:city"/>,
            <xsl:value-of select="$addr/n1:state"/>,
            <xsl:value-of select="$addr/n1:postalCode"/>
		</xsl:if>
	</xsl:template>
	<xsl:template name="getAddress">
		<xsl:param name="addr"/>
		<xsl:if test="$addr/n1:streetAddressLine != ' '">
			<xsl:for-each select="$addr/n1:streetAddressLine">
				<br/>
				<xsl:if test="string-length($addr/n1:streetAddressLine)>0">
					<xsl:value-of select="."/>
				</xsl:if>
			</xsl:for-each>
			<br/>
			<xsl:value-of select="$addr/n1:city"/>,
            <xsl:value-of select="$addr/n1:state"/>,
            <xsl:value-of select="$addr/n1:postalCode"/>
		</xsl:if>
	</xsl:template>
	
	<!-- Get a Telephone  -->
	<xsl:template name="getTelecom">
		<xsl:param name="telecom"/>
		<br/>
		<xsl:if test="string-length(./@value)>0">
			<xsl:value-of select="./@value"/>
			<xsl:choose>
				<xsl:when test="./@use='HP' ">
					<b><xsl:text> Home</xsl:text></b>
				</xsl:when>
				<xsl:when test="./@use='WP' ">
					<b><xsl:text> Work</xsl:text></b>
				</xsl:when>
				<xsl:when test="./@use='HV' ">
					<b><xsl:text> Vacation</xsl:text></b>
				</xsl:when>
				<xsl:when test="./@use='MC' ">
					<b><xsl:text> Mobile</xsl:text></b>
				</xsl:when>
				<xsl:otherwise>
					<b><xsl:text> Unknown</xsl:text></b>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>		
	</xsl:template>
	
	<!-- Get a Name  -->
	<xsl:template name="getName">
		<xsl:param name="name"/>
		<xsl:choose>
			<xsl:when test="string-length($name/n1:family)=0">
            </xsl:when>
			<xsl:when test="$name/n1:family">
				<xsl:if test="string-length($name/n1:given)>0">
					<xsl:value-of select="$name/n1:given"/>
				</xsl:if>
				<xsl:text> </xsl:text>
				<xsl:if test="string-length($name/n1:family)>0">
					<xsl:value-of select="$name/n1:family"/>
				</xsl:if>
				<xsl:text> </xsl:text>
				<xsl:if test="string-length($name/n1:suffix)>0">
					<xsl:if test="$name/n1:suffix != ' '">
						<xsl:text>, </xsl:text>
						<xsl:value-of select="$name/n1:suffix"/>
					</xsl:if>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$name"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Get a Legal Authorization Name  -->
	<xsl:template name="getAuthName">
		<xsl:param name="name"/>
		<xsl:choose>
			<xsl:when test="string-length($name)=0">
            </xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$name"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--  Format Date

      outputs a date in Month Day, Year form
      e.g., 19991207  ==> Dec 07, 99
-->
	<xsl:template name="formatDate">
		<xsl:param name="date"/>
		<xsl:if test="string-length($date)>0">
			<xsl:variable name="month" select="substring ($date, 5, 2)"/>
			<xsl:choose>
				<xsl:when test="$month='01'">
					<xsl:text>Jan </xsl:text>
				</xsl:when>
				<xsl:when test="$month='02'">
					<xsl:text>Feb </xsl:text>
				</xsl:when>
				<xsl:when test="$month='03'">
					<xsl:text>Mar </xsl:text>
				</xsl:when>
				<xsl:when test="$month='04'">
					<xsl:text>Apr </xsl:text>
				</xsl:when>
				<xsl:when test="$month='05'">
					<xsl:text>May </xsl:text>
				</xsl:when>
				<xsl:when test="$month='06'">
					<xsl:text>Jun </xsl:text>
				</xsl:when>
				<xsl:when test="$month='07'">
					<xsl:text>Jul </xsl:text>
				</xsl:when>
				<xsl:when test="$month='08'">
					<xsl:text>Aug </xsl:text>
				</xsl:when>
				<xsl:when test="$month='09'">
					<xsl:text>Sep </xsl:text>
				</xsl:when>
				<xsl:when test="$month='10'">
					<xsl:text>Oct </xsl:text>
				</xsl:when>
				<xsl:when test="$month='11'">
					<xsl:text>Nov </xsl:text>
				</xsl:when>
				<xsl:when test="$month='12'">
					<xsl:text>Dec </xsl:text>
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
			<xsl:value-of select="substring ($date, 3, 2)"/>
		</xsl:if>
	</xsl:template>
	<!--  Format Date

      outputs a date in Month Day, Year form
      e.g., 19991207  ==> December 07, 1999
-->
	<xsl:template name="formatDateFull">
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
	<!-- Medications Detail Section -->
	<xsl:template name="medDetails">
		<xsl:param name="section"/>
		<table border="1" style="font-size:14px">
			<thead>
				<tr>
					<th class="first">Generic Name 
                                        (Active -
                        <xsl:value-of select="count(n1:entry/n1:substanceAdministration)"/>)
                    </th>
					<!--
					<th class="first">Route</th>
					<th class="first">Interval</th>
					<th class="first">Start Date</th>
					<th class="first">Expire Date</th>
					<th class="first">Status</th>
                                        -->
					<!--th class="first"                   >Status</th-->
					<th class="first">Brand Name</th>
					<th class="first" style="width:30px">Route</th>
					<th class="first" style="width:40px">Interval</th>
					<!--th class="first">Rx NBR</th-->
					<th class="first">Instructions</th>
					<th class="first" style="width:60px">Last Disp Date</th>
					<th class="first">Ordering Provider</th>
					<!--th class="first">Source</th-->
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
		<table border="1" style="font-size:14px">
			<thead>
				<tr>
					<th class="first">Condition
                        <!--xsl:if test="n1:entry/n1:act/n1:entryRelationship/n1:observation/n1:effectiveTime/n1:low"-->
                                            (Active -
                            <!--xsl:value-of select="count(n1:entry/n1:act/n1:entryRelationship/n1:observation/n1:effectiveTime/n1:low)"/-->
                            <xsl:value-of select="count(n1:entry/n1:act/n1:entryRelationship/n1:observation)"/>)
                        <!--/xsl:if-->
					</th>
					<th class="first">Problem Code</th>
					<th class="first">Reported/Onset Date</th>
					<th class="first">Last Update</th>
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
		<!--table border="1" width="860px" style="font-size:14px"-->
		<table border="1" style="font-size:14px">
			<thead>
				<tr>
					<th class="first" style="width:250px">Substance 
                                        (Active -
                        <xsl:value-of select="count(n1:entry/n1:act/n1:entryRelationship/n1:observation)"/>)
                    </th>
					<th class="first">Reaction Type</th>
					<th class="first">Severity</th>
					<th class="first">Comments</th>
					<th class="first">Onset Date</th>
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
			<xsl:when test="n1:act/n1:entryRelationship/n1:observation/n1:templateId/@root=$problemTemplateCode">
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
		<tr class="second">
			<!-- Generic Name -->
			<td>
				<div style="overflow:hidden; white-space:nowrap; width:160px;">
					<xsl:choose>
						<xsl:when test="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial[n1:code/n1:originalText or n1:code/@displayName]">
							<xsl:choose>
								<xsl:when test="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/n1:originalText">
									<xsl:call-template name="flyoverSpan">
										<xsl:with-param name="data" select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/n1:originalText"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="flyoverSpan">
										<xsl:with-param name="data" select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/@displayName"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="flyoverSpan">
								<xsl:with-param name="data" select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:name"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</td>
			<!-- Brand Name -->
			<td>
				<div style="overflow:hidden; white-space:nowrap;">
					<xsl:choose>
						<xsl:when test="string-length($row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:name)=0">
							<xsl:text>-</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<!--xsl:value-of select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:name"/-->
							<xsl:call-template name="flyoverSpan">
								<xsl:with-param name="data" select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:name"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</td>
			<!--
                         Instructions
			<td>
                            <div style="overflow:hidden; white-space:nowrap; width:160px;">
                            <xsl:choose>
                                    <xsl:when test="starts-with($row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:text/n1:content[@ID='patient instruction'],'??')">
                                            <xsl:text>-</xsl:text>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:call-template name="flyoverSpan">
					<xsl:with-param name="data" select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:text/n1:content[@ID='patient instruction']"/>
                                        </xsl:call-template>
                                    </xsl:otherwise>
                            </xsl:choose>
                            </div>
                        </td>
-->
			<!-- Route -->
			<td>
				<div style="overflow:hidden; white-space:nowrap;">
					<xsl:choose>
						<!--xsl:when test="string-length($row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:quantity/@value)=0"-->
						<xsl:when test="string-length($row//n1:substanceAdministration/n1:routeCode/@displayName)=0">
							<xsl:text>-</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<!--xsl:value-of select="$row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:quantity/@value"/-->
							<xsl:value-of select="$row/n1:substanceAdministration/n1:routeCode/@displayName"/>
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</td>
			<!-- Interval -->
			<td>
				<div style="overflow:hidden; white-space:nowrap;">
					<xsl:choose>
						<!-- Test if Interval value is populated -->
						<xsl:when test="string-length($row/n1:substanceAdministration/n1:effectiveTime/n1:period/@value)=0">
							<!-- Filler '-' if Interval null -->
							<xsl:text>-</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$row/n1:substanceAdministration/n1:effectiveTime/n1:period/@value"/>
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</td>
			<!-- Prescription ID (Nbr) -->
			<!--td-->
			<!--div style="overflow:hidden; white-space:nowrap;"-->
			<!--xsl:choose-->
			<!--xsl:when test="string-length($row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:id/@extension)=0"-->
			<!--xsl:text>-</xsl:text-->
			<!--/xsl:when-->
			<!--xsl:otherwise-->
			<!--xsl:value-of select="$row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:id/@extension"/-->
			<!--/xsl:otherwise-->
			<!--/xsl:choose-->
			<!--/div-->
			<!--/td-->
			<!-- Sig -->
			<td>
				<div style="overflow:hidden; white-space:nowrap; width:100px;">
					<xsl:variable name="sigReference" select="substring-after(n1:substanceAdministration/n1:text/n1:reference/@value,'#')"/>
					<xsl:variable name="sig" select="../n1:text/n1:list/n1:item/n1:content[@ID=$sigReference]"/>
					<xsl:call-template name="flyoverSpan">
						<xsl:with-param name="data" select="$sig"/>
					</xsl:call-template>
					<!--
                            <xsl:call-template name="flyoverSpan">
                                <xsl:with-param name="data" select="$row/n1:substanceAdministration/n1:text/n1:content[@ID='sig-1']"/>
                            </xsl:call-template>
-->
				</div>
			</td>
			<!-- Last dispense day/time -->
			<td>
				<div style="overflow:hidden; white-space:nowrap;">
					<xsl:choose>
						<xsl:when test="string-length($row/n1:substanceAdministration/n1:entryRelationship/n1:supply[@classCode='SPLY' and @moodCode='EVN']/n1:effectiveTime/@value)=0">
							<xsl:text>-</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="formatDate">
								<xsl:with-param name="date" select="$row/n1:substanceAdministration/n1:entryRelationship/n1:supply[@classCode='SPLY' and @moodCode='EVN']/n1:effectiveTime/@value"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</td>
			<!-- Ordering Provider -->
			<td>
				<div style="overflow:hidden; white-space:nowrap;">
					<xsl:choose>
						<xsl:when test="string-length($row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name)=0">
							<xsl:text>-</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="flyoverSpan">
								<xsl:with-param name="data" select="$row/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</td>
			<!-- Medications source -->
			<!--td-->
			<!--div style="overflow:hidden; white-space:nowrap; width:100px;"-->
			<!--xsl:call-template name="flyoverSpan"-->
			<!--xsl:with-param name="data" select="n1:substanceAdministration/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name"/-->
			<!--/xsl:call-template-->
			<!--
                    <xsl:call-template name="flyoverSpan">
                        <xsl:with-param name="data" select="$row/n1:substanceAdministration/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name"/>
                    </xsl:call-template>
-->
			<!--/div-->
			<!--/td-->
		</tr>
	</xsl:template>
	<!-- problem entry row -->
	<xsl:template name="problemRow">
		<xsl:param name="row"/>
		<xsl:variable name="rowData" select="$row/n1:act/n1:entryRelationship/n1:observation"/>
		<tr class="second">
			<!-- name -->
			<td>
				<div style="overflow:hidden; white-space:nowrap;">
					<!--<xsl:variable name="probReference" select="substring-after($rowData/n1:code/n1:reference/@value,'#')"/>
					<xsl:variable name="prob" select="../n1:text/n1:paragraph[@ID=$probReference]"/>-->
					<xsl:variable name="prob" select="$rowData/n1:text"/>
					<xsl:call-template name="flyoverSpan">
						<xsl:with-param name="data" select="$prob"/>
					</xsl:call-template>
				</div>
			</td>
			<!-- Problem Code -->
			<td>
				<div style="overflow:hidden; white-space:nowrap; width:100%">
					<xsl:variable name="providerReference" select="$row/n1:act/n1:performer/n1:assignedEntity/n1:id/@extension"/>
					<xsl:variable name="provider" select="/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:assignedEntity/n1:id[@extension=$providerReference]/@assigningAuthorityName"/>
					<xsl:variable name="probcodenull" select="$rowData/n1:code/@nullFlavor"/>
					<xsl:variable name="probcode" select="$rowData/n1:code/@code"/>
					<!--xsl:value-of select="$probcode"/-->
					<xsl:choose>
						<xsl:when test="string($probcodenull)='UNK' ">
							<xsl:text>-- Not Available --</xsl:text>
						</xsl:when>
						<xsl:when test="$probcode">
							<xsl:value-of select="$probcode"/>
						</xsl:when>
					</xsl:choose>
					<!--xsl:call-template name="flyoverSpan"-->
					<!--xsl:with-param name="data" select="$probcode"/-->
					<!--/xsl:call-template-->
				</div>
			</td>
			<!-- problem effective date -->
			<td>
				<div style="overflow:hidden; white-space:nowrap;">
					<xsl:choose>
						<xsl:when test="string-length($row/n1:act/n1:effectiveTime/n1:low/@value)=0">
							<xsl:text>-- Not Available --</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="formatDate">
								<xsl:with-param name="date" select="$row/n1:act/n1:effectiveTime/n1:low/@value"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</td>
			<!-- Last update -->
			<td>
				<div style="overflow:hidden; white-space:nowrap; width:120px;">
					<!--xsl:value-of select="$row/n1:act/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name"/-->
					<xsl:text>-- Not Available --</xsl:text>
				</div>
			</td>
		</tr>
	</xsl:template>
	<!-- allergy entry row -->
	<xsl:template name="allergyRow">
		<xsl:param name="row"/>
		<xsl:variable name="observation" select="$row/n1:act/n1:entryRelationship/n1:observation"/>
		<tr class="second">
			<!-- Substance -->
			<td style="overflow:hidden; white-space:nowrap;">
				<xsl:value-of select="$observation/n1:participant/n1:participantRole/n1:playingEntity/n1:name"/>
			</td>
			<!-- Event Type-->
			<td style="overflow:hidden; white-space:nowrap;">
				<xsl:variable name="reacReference" select="substring-after($observation/n1:entryRelationship/n1:observation/n1:text/n1:reference/@value,'#')"/>
				<xsl:variable name="reaction" select="../n1:text/n1:content[@ID=$reacReference]"/>
				<!--xsl:value-of select="$reaction"/-->
				<!--xsl:call-template name="flyoverSpan"-->
				<!--xsl:with-param name="data" select="$reaction"/-->
				<xsl:choose>
					<xsl:when test="string-length($reaction)=0">
						<xsl:text>-- Not Available --</xsl:text>
					</xsl:when>
					<xsl:when test="$reaction">
						<xsl:value-of select="$reaction"/>
					</xsl:when>
				</xsl:choose>
			</td>
			<!-- Severity -->
			<td style="overflow:hidden; white-space:nowrap;">
				<!--xsl:value-of select="$row/n1:act/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name"/-->
				<xsl:variable name="sevrReference" select="substring-after($observation/n1:entryRelationship/n1:observation/n1:entryRelationship/n1:observation/n1:text/n1:reference/@value,'#')"/>
				<xsl:variable name="severity" select="../n1:text/n1:content[@ID=$sevrReference]"/>
				<!--xsl:value-of select="$severity"/-->
				<xsl:choose>
					<xsl:when test="string-length($severity)=0">
						<xsl:text>-- Not Available --</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$severity"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<!-- Comments-->
			<td style="overflow:hidden; white-space:nowrap;">
				<xsl:text>-- Not Available --</xsl:text>
			</td>
			<!-- Onset Date-->
			<td style="overflow:hidden; white-space:nowrap;">
				<xsl:choose>
					<xsl:when test="string-length($observation/n1:effectiveTime/n1:low/@value)=0">
						<xsl:text>-- Not Available --</xsl:text>
					</xsl:when>
					<xsl:when test="$observation/n1:effectiveTime/n1:low/@value">
						<xsl:call-template name="formatDate">
							<xsl:with-param name="date" select="$observation/n1:effectiveTime/n1:low/@value"/>
						</xsl:call-template>
					</xsl:when>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!--   flyover -->
	<xsl:template name="flyoverSpan">
		<xsl:param name="data"/>
		<span onmouseover="DisplayTip(this,25,-50)">
			<xsl:value-of select="$data"/>
		</span>
	</xsl:template>
	<!--   Title  -->
	<xsl:template match="n1:title">
		<h3>
			<span style="font-weight:bold;">
				<a name="{generate-id(.)}" href="#toc">
					<xsl:value-of select="."/>
				</a>
			</span>
		</h3>
	</xsl:template>
	<!--   Text   -->
	<xsl:template match="n1:text">
		<xsl:apply-templates/>
	</xsl:template>
	<!--   paragraph  -->
	<xsl:template match="n1:paragraph">
		<p>
			<xsl:apply-templates/>
		</p>
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
					<xsl:apply-templates/>
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
					<xsl:apply-templates/>
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
					<br clear="all"/>
					<xsl:element name="img">
						<xsl:attribute name="src"><xsl:value-of select="//n1:regionOfInterest[@ID=$imageRef]//n1:observationMedia/n1:value/n1:reference/@value"/></xsl:attribute>
					</xsl:element>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<!-- Here is where the direct MultiMedia image referencing goes -->
				<xsl:if test='//n1:observationMedia[@ID=$imageRef]/n1:value[@mediaType="image/gif" or @mediaType="image/jpeg"]'>
					<br clear="all"/>
					<xsl:element name="img">
						<xsl:attribute name="src"><xsl:value-of select="//n1:observationMedia[@ID=$imageRef]/n1:value/n1:reference/@value"/></xsl:attribute>
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
			<xsl:element name="b">
				<xsl:apply-templates/>
			</xsl:element>
		</xsl:if>
		<xsl:if test="@styleCode='Italics'">
			<xsl:element name="i">
				<xsl:apply-templates/>
			</xsl:element>
		</xsl:if>
		<xsl:if test="@styleCode='Underline'">
			<xsl:element name="u">
				<xsl:apply-templates/>
			</xsl:element>
		</xsl:if>
		<xsl:if test="contains(@styleCode,'Bold') and contains(@styleCode,'Italics') and not (contains(@styleCode, 'Underline'))">
			<xsl:element name="b">
				<xsl:element name="i">
					<xsl:apply-templates/>
				</xsl:element>
			</xsl:element>
		</xsl:if>
		<xsl:if test="contains(@styleCode,'Bold') and contains(@styleCode,'Underline') and not (contains(@styleCode, 'Italics'))">
			<xsl:element name="b">
				<xsl:element name="u">
					<xsl:apply-templates/>
				</xsl:element>
			</xsl:element>
		</xsl:if>
		<xsl:if test="contains(@styleCode,'Italics') and contains(@styleCode,'Underline') and not (contains(@styleCode, 'Bold'))">
			<xsl:element name="i">
				<xsl:element name="u">
					<xsl:apply-templates/>
				</xsl:element>
			</xsl:element>
		</xsl:if>
		<xsl:if test="contains(@styleCode,'Italics') and contains(@styleCode,'Underline') and contains(@styleCode, 'Bold')">
			<xsl:element name="b">
				<xsl:element name="i">
					<xsl:element name="u">
						<xsl:apply-templates/>
					</xsl:element>
				</xsl:element>
			</xsl:element>
		</xsl:if>
	</xsl:template>
	<!-- 	Superscript or Subscript   -->
	<xsl:template match="n1:sup">
		<xsl:element name="sup">
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="n1:sub">
		<xsl:element name="sub">
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
	<!--  Bottomline  -->
	<xsl:template name="bottomline">
		<p>
			<b>
				<xsl:text>Electronically generated by: </xsl:text>
			</b>
			<xsl:call-template name="getAuthName">
				<xsl:with-param name="name" select="/n1:ClinicalDocument/n1:legalAuthenticator/n1:assignedEntity/n1:representedOrganization/n1:name"/>
			</xsl:call-template>
			<xsl:text> on </xsl:text>
			<xsl:choose>
				<xsl:when test="string-length(/n1:ClinicalDocument/n1:effectiveTime/@value)=0">
					<xsl:text>-- Not Available --</xsl:text>
				</xsl:when>
				<xsl:when test="starts-with(/n1:ClinicalDocument/n1:effectiveTime/@value,' ')">
					<xsl:text>-- Not Available --</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="formatDateFull">
						<xsl:with-param name="date" select="/n1:ClinicalDocument/n1:effectiveTime/@value"/>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
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
				<xsl:with-param name="date" select="text()"/>
			</xsl:call-template>
		</td>
	</xsl:template>
	<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.8']/n1:text/n1:table/n1:tbody/n1:tr/n1:td[5]">
		<td>
			<xsl:call-template name="formatDate">
				<xsl:with-param name="date" select="text()"/>
			</xsl:call-template>
		</td>
	</xsl:template>
	<xsl:template match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.16' or n1:templateId/@root='2.16.840.1.113883.10.20.1.14' or n1:templateId/@root='2.16.840.1.113883.10.20.1.6' or n1:templateId/@root='2.16.840.1.113883.10.20.1.3']/n1:text/n1:table/n1:tbody/n1:tr/n1:td[2]">
		<td>
			<xsl:call-template name="formatDate">
				<xsl:with-param name="date" select="concat(substring(text(),1,4),substring(text(),6,2),substring(text(),9,2))"/>
			</xsl:call-template>
		</td>
	</xsl:template>
	<xsl:template match="n1:languageCommunication">
		<xsl:variable name="langCode" select="substring(n1:languageCode/@code,1,2)"/>
		<xsl:choose>
			<xsl:when test="string-length($langCode)=0">
            </xsl:when>
			<xsl:when test="$langCode='en'">
				<li>
					<xsl:text>English</xsl:text>
				</li>
			</xsl:when>
			<xsl:when test="$langCode='es'">
				<li>
					<xsl:text>Spanish</xsl:text>
				</li>
			</xsl:when>
			<xsl:otherwise>
				<li>
					<xsl:value-of select="n1:languageCode/@code"/>
				</li>
			</xsl:otherwise>
		</xsl:choose>
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
