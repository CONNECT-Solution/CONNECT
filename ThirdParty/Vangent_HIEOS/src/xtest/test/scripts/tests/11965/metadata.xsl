<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:variable name="ss-class"
        select="//*[local-name()='Classification'][@classificationNode='urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd']"/>
    <xsl:variable name="ss" select="//*[local-name()='RegistryPackage'][@id=$ss-class/@classifiedObject]"/>
    <xsl:variable name="ss-id" select="$ss/@id"/>
    <xsl:variable name="docs" select="//*[local-name()='ExtrinsicObject']"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>XDM</title>
            </head>
            <body>
                <xsl:for-each select="//*[local-name()='ExtrinsicObject']">
                    <xsl:if test="position()=1">
                        <xsl:apply-templates select="." mode="sourcePatientInfo"/>
                    </xsl:if>
                </xsl:for-each>
                <xsl:apply-templates select="$ss"/>
                <xsl:apply-templates select="$docs"/>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="*[local-name()='ExtrinsicObject']">
        <h1>Document Description</h1>
        
        <a>
            <xsl:attribute name="href">
                <xsl:value-of select="*[local-name()='Slot'][@name='URI']/*[local-name()='ValueList']/*[local-name()='Value']"/>
            </xsl:attribute>
            View Document
        </a>
        
        <table CELLPADDING="2" CELLSPACING="2" WIDTH="100%" >
            <tr BGCOLOR="#CCCCCC">
                <td>Title</td><td><xsl:value-of select="*[local-name()='Name']/*[local-name()='LocalizedString']/@value"/></td>
            </tr>
            <tr BGCOLOR="#CCCCCC">
                <td>Description</td><td><xsl:value-of select="*[local-name()='Description']/*[local-name()='LocalizedString']/@value"/></td>
            </tr>
            <tr BGCOLOR="#CCCCCC">
                <td>Time</td><td><xsl:value-of select="*[local-name()='Slot'][@name='creationTime']/*[local-name()='ValueList']/*[local-name()='Value']"/></td>
            </tr>
            <xsl:for-each select="*[local-name()='Classification'][@classificationScheme='urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d']">
                <tr BGCOLOR="#CCCCCC"><td>Author</td><td>Name</td><td><xsl:value-of select="*[local-name()='Slot'][@name='authorPerson']/*[local-name()='ValueList']/*[local-name()='Value']"/></td></tr>
                <tr BGCOLOR="#CCCCCC"><td></td><td>Institution</td>
                    <xsl:for-each select="*[local-name()='Slot'][@name='authorInstitution']/*[local-name()='ValueList']/*[local-name()='Value']">
                    <td><xsl:value-of select="."/></td>
                    </xsl:for-each>
                </tr>
                <tr BGCOLOR="#CCCCCC"><td></td><td>Role</td>
                    <xsl:for-each select="*[local-name()='Slot'][@name='authorRole']/*[local-name()='ValueList']/*[local-name()='Value']">
                        <td><xsl:value-of select="."/></td>
                    </xsl:for-each>
                </tr>
                <tr BGCOLOR="#CCCCCC"><td></td><td>Specialty</td>
                    <xsl:for-each select="*[local-name()='Slot'][@name='authorSpecialty']/*[local-name()='ValueList']/*[local-name()='Value']">
                        <td><xsl:value-of select="."/></td>
                    </xsl:for-each>
                </tr>
            </xsl:for-each>
            <xsl:for-each select="*[local-name()='Classification'][@classificationScheme='urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a']">
                <tr BGCOLOR="#CCCCCC">
                    <td>Class Code</td>
                    <td><xsl:value-of select="@nodeRepresentation"/></td>
                    <td><xsl:value-of select="*[local-name()='Name']/*[local-name()='LocalizedString']/@value"/></td>
                    <td><xsl:value-of select="*[local-name()='Slot'][@name='codingScheme']/*[local-name()='ValueList']/*[local-name()='Value']"/></td>
                </tr>
            </xsl:for-each>
            <xsl:for-each select="*[local-name()='Classification'][@classificationScheme='urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f']">
                <tr BGCOLOR="#CCCCCC">
                    <td>Confidentiality Code</td>
                    <td><xsl:value-of select="@nodeRepresentation"/></td>
                    <td><xsl:value-of select="*[local-name()='Name']/*[local-name()='LocalizedString']/@value"/></td>
                    <td><xsl:value-of select="*[local-name()='Slot'][@name='codingScheme']/*[local-name()='ValueList']/*[local-name()='Value']"/></td>
                </tr>
            </xsl:for-each>
            <xsl:for-each select="*[local-name()='Classification'][@classificationScheme='urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1']">
                <tr BGCOLOR="#CCCCCC">
                    <td>Healthcare Facility Type Code</td>
                    <td><xsl:value-of select="@nodeRepresentation"/></td>
                    <td><xsl:value-of select="*[local-name()='Name']/*[local-name()='LocalizedString']/@value"/></td>
                    <td><xsl:value-of select="*[local-name()='Slot'][@name='codingScheme']/*[local-name()='ValueList']/*[local-name()='Value']"/></td>
                </tr>
            </xsl:for-each>
            <xsl:for-each select="*[local-name()='Classification'][@classificationScheme='urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead']">
                <tr BGCOLOR="#CCCCCC">
                    <td>Practice Setting Code</td>
                    <td><xsl:value-of select="@nodeRepresentation"/></td>
                    <td><xsl:value-of select="*[local-name()='Name']/*[local-name()='LocalizedString']/@value"/></td>
                    <td><xsl:value-of select="*[local-name()='Slot'][@name='codingScheme']/*[local-name()='ValueList']/*[local-name()='Value']"/></td>
                </tr>
            </xsl:for-each>
            <xsl:for-each select="*[local-name()='Classification'][@classificationScheme='urn:uuid:f0306f51-975f-434e-a61c-c59651d33983']">
                <tr BGCOLOR="#CCCCCC">
                    <td>Type Code</td>
                    <td><xsl:value-of select="@nodeRepresentation"/></td>
                    <td><xsl:value-of select="*[local-name()='Name']/*[local-name()='LocalizedString']/@value"/></td>
                    <td><xsl:value-of select="*[local-name()='Slot'][@name='codingScheme']/*[local-name()='ValueList']/*[local-name()='Value']"/></td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
    <xsl:template match="*[local-name()='RegistryPackage']">
        <h1>Document Group Overview</h1>
        <table>
            <tr BGCOLOR="#CCCCCC">
                <td>Title</td><td><xsl:value-of select="*[local-name()='Name']/*[local-name()='LocalizedString']/@value"/></td>
            </tr>
            <tr BGCOLOR="#CCCCCC">
                <td>Description</td><td><xsl:value-of select="*[local-name()='Description']/*[local-name()='LocalizedString']/@value"/></td>
            </tr>
            <tr BGCOLOR="#CCCCCC">
                <td>Time</td><td><xsl:value-of select="*[local-name()='Slot'][@name='submissionTime']/*[local-name()='ValueList']/*[local-name()='Value']"/></td>
            </tr>
            <xsl:for-each select="*[local-name()='Classification'][@classificationScheme='urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d']">
                <tr BGCOLOR="#CCCCCC"><td>Author</td><td>Name</td><td><xsl:value-of select="*[local-name()='Slot'][@name='authorPerson']/*[local-name()='ValueList']/*[local-name()='Value']"/></td></tr>
                <tr BGCOLOR="#CCCCCC"><td></td><td>Institution</td>
                    <xsl:for-each select="*[local-name()='Slot'][@name='authorInstitution']/*[local-name()='ValueList']/*[local-name()='Value']">
                        <td><xsl:value-of select="."/></td>
                    </xsl:for-each>
                </tr>
                <tr BGCOLOR="#CCCCCC"><td></td><td>Role</td>
                    <xsl:for-each select="*[local-name()='Slot'][@name='authorRole']/*[local-name()='ValueList']/*[local-name()='Value']">
                        <td><xsl:value-of select="."/></td>
                    </xsl:for-each>
                </tr>
                <tr BGCOLOR="#CCCCCC"><td></td><td>Specialty</td>
                    <xsl:for-each select="*[local-name()='Slot'][@name='authorSpecialty']/*[local-name()='ValueList']/*[local-name()='Value']">
                        <td><xsl:value-of select="."/></td>
                    </xsl:for-each>
                </tr>
            </xsl:for-each>
            <xsl:for-each select="*[local-name()='Classification'][@classificationScheme='urn:uuid:aa543740-bdda-424e-8c96-df4873be8500']">
                <tr BGCOLOR="#CCCCCC">
                    <td>Content Type</td>
                    <td><xsl:value-of select="@nodeRepresentation"/></td>
                    <td><xsl:value-of select="*[local-name()='Name']/*[local-name()='LocalizedString']/@value"/></td>
                    <td><xsl:value-of select="*[local-name()='Slot'][@name='codingScheme']/*[local-name()='ValueList']/*[local-name()='Value']"/></td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
    <xsl:template match="//*[local-name()='ExtrinsicObject']" mode="sourcePatientInfo">
        <xsl:for-each select="*[local-name()='Slot'][@name='sourcePatientInfo']">
            <xsl:for-each select="*[local-name()='ValueList']">
                <xsl:for-each select="*[local-name()='Value'][starts-with(.,'PID-5')]">
                    <xsl:variable name="name-field" select="substring-after(.,'|')"/>
                    <xsl:variable name="last-name" select="substring-before($name-field, '^')"/>
                    <xsl:variable name="last-name-rest" select="substring-after($name-field, '^')"/>
                    <xsl:variable name="first-name" select="substring-before($last-name-rest, '^')"/>
                    <center><h1><xsl:value-of select="concat($first-name,' ',$last-name)"/></h1></center>
                </xsl:for-each>
                <xsl:for-each select="*[local-name()='Value'][starts-with(.,'PID-8')]">
                    <xsl:variable name="sex-field" select="substring-after(.,'|')"/>
                    <center>
                        <xsl:choose>
                            <xsl:when test="$sex-field='M'">Male</xsl:when>
                            <xsl:when test="$sex-field='F'">Female</xsl:when>
                            <xsl:otherwise><xsl:value-of select="concat('Sex: ',$sex-field)"/></xsl:otherwise>
                        </xsl:choose>
                    </center>
                </xsl:for-each>
                <xsl:for-each select="*[local-name()='Value'][starts-with(.,'PID-7')]">
                    <xsl:variable name="bday" select="substring-after(.,'|')"/>
                    <center><xsl:value-of select="concat('Birthday: ',$bday)"/></center>
                </xsl:for-each>
                <xsl:for-each select="*[local-name()='Value'][starts-with(.,'PID-11')]">
                    <xsl:variable name="addr-field" select="substring-after(.,'|')"/>
                    <xsl:variable name="street-addr" select="substring-before($addr-field,'^')"/>
                    <xsl:variable name="street-addr-rest" select="substring-after($addr-field,'^')"/>
                    <xsl:variable name="street-addr-rest-rest" select="substring-after($street-addr-rest,'^')"/>
                    <xsl:variable name="city" select="substring-before($street-addr-rest-rest,'^')"/>
                    <xsl:variable name="city-rest" select="substring-after($street-addr-rest-rest,'^')"/>
                    <xsl:variable name="state" select="substring-before($city-rest,'^')"/>
                    <xsl:variable name="state-rest" select="substring-after($city-rest,'^')"/>
                    <xsl:variable name="zip" select="substring-before($state-rest,'^')"/>
                    <center><xsl:value-of select="$street-addr"/></center>
                    <center><xsl:value-of select="concat($city,', ',$state,' ',$zip)"/></center>
                </xsl:for-each>
            </xsl:for-each>    
        </xsl:for-each>  
    </xsl:template>
    
</xsl:stylesheet>
