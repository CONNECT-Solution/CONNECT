<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:n1="http://nhinc.services.com/schema/auditmessage">
    <xsl:template match="n1:AuditMessage">
        <html>
            <body>
                <xsl:call-template name="EventIdentification" />
                <xsl:call-template name="ActiveParticipant" />
                <xsl:call-template name="AuditSourceIdentification" />
                <xsl:call-template name="ParticipantObjectIdentification" />
            </body>
        </html>
    </xsl:template>
    <xsl:template name="EventIdentification">
        <h4>Event Identification:</h4>
        <table>
            <xsl:attribute name="class">table table-bordered table-condensed</xsl:attribute>
            <tr>
                <xsl:attribute name="bgcolor">#e3ebf5</xsl:attribute>
                <th>EventActionCode</th>
                <th>EventDateTime</th>
                <th>EventOutcomeIndicator</th>
                <th>EventIDCode</th>
                <th>EventIDDisplayName</th>
                <th>CodeSystemName</th>
                <th>EventTypeCode</th>
                <th>EventTypeCodeDisplayName</th>
                <th>EventTypeCodeSystemName</th>
            </tr>
            <xsl:for-each select="/n1:AuditMessage/n1:EventIdentification">
                <tr>
                    <td>
                        <xsl:choose>
                            <xsl:when test="@EventActionCode">
                                <xsl:value-of select="@EventActionCode" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="@EventDateTime">
                                <xsl:value-of select="@EventDateTime" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="@EventOutcomeIndicator">
                                <xsl:value-of select="@EventOutcomeIndicator" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:EventID/@code">
                                <xsl:value-of select="n1:EventID/@code" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:EventID/@displayName">
                                <xsl:value-of select="n1:EventID/@displayName" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:EventID/@displayName">
                                <xsl:value-of select="n1:EventID/@codeSystemName" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:EventTypeCode/@code">
                                <xsl:value-of select="n1:EventTypeCode/@code" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:EventTypeCode/@displayName">
                                <xsl:value-of select="n1:EventTypeCode/@displayName" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:EventTypeCode/@codeSystemName">
                                <xsl:value-of select="n1:EventTypeCode/@codeSystemName" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
    <xsl:template name="ActiveParticipant">
        <h4>Active Participant:</h4>
        <table>
            <xsl:attribute name="class">table table-bordered table-condensed</xsl:attribute>
            <tr>
                <xsl:attribute name="bgcolor">#e3ebf5</xsl:attribute>
                <th>UserID</th>
                <th>AlternativeUserID</th>
                <th>UserName</th>
                <th>UserIsRequestor</th>
                <th>AccessPointID</th>
                <th>AccessPointTypeCode</th>
                <th>RoleIDCode</th>
                <th>CodeSystemName</th>
                <th>Type</th>
            </tr>
            <xsl:for-each select="/n1:AuditMessage/n1:ActiveParticipant">
                <tr>
                    <td>
                        <xsl:value-of select="@UserID" />
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="@AlternativeUserID">
                                <xsl:value-of select="@AlternativeUserID" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="@UserName">
                                <xsl:value-of select="@UserName" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="@UserIsRequestor">
                                <xsl:value-of select="@UserIsRequestor" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="@NetworkAccessPointID">
                                <xsl:value-of select="@NetworkAccessPointID" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="@NetworkAccessPointID">
                                <xsl:value-of select="@NetworkAccessPointTypeCode" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:RoleIDCode/@code">
                                <xsl:value-of select="n1:RoleIDCode/@code" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:RoleIDCode/@code">
                                <xsl:value-of select="n1:RoleIDCode/@codeSystemName" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:RoleIDCode/@code">
                                <xsl:value-of select="n1:RoleIDCode/@displayName" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
    <xsl:template name="AuditSourceIdentification">
        <h4>Audit Source Identification:</h4>
        <table>
            <xsl:attribute name="class">table table-bordered table-condensed</xsl:attribute>
            <tr>
                <xsl:attribute name="bgcolor">#e3ebf5</xsl:attribute>
                <th>AuditEnterpriseSiteID</th>
                <th>AuditSourceID</th>
            </tr>
            <xsl:for-each select="/n1:AuditMessage/n1:AuditSourceIdentification">
                <tr>
                    <td>
                        <xsl:choose>
                            <xsl:when test="@AuditEnterpriseSiteID">
                                <xsl:value-of select="@AuditEnterpriseSiteID" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="@AuditSourceID">
                                <xsl:value-of select="@AuditSourceID" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
    <xsl:template name="ParticipantObjectIdentification">
        <h4>Participant Object Identification:</h4>
        <table>
            <xsl:attribute name="class">table table-bordered table-condensed</xsl:attribute>
            <tr>
                <xsl:attribute name="bgcolor">#e3ebf5</xsl:attribute>
                <th>ParticipantObjectID</th>
                <th>ParticipantObjectTypeCode</th>
                <th>ParticipantObjectTypeCodeRole</th>
                <th>ParticipantObjectIDTypeCode</th>
                <th>DisplayName</th>
                <th>CodeSystemName</th>
                <th>ParticipantObjectQuery</th>
                <th>ObjectDetailRepType</th>
                <th>ObjectDetailRepValue</th>
                <th>ObjectDetailHcidType</th>
                <th>ObjectDetailHcidValue</th>
            </tr>
            <xsl:for-each select="/n1:AuditMessage/n1:ParticipantObjectIdentification">
                <tr>
                    <td>
                        <xsl:choose>
                            <xsl:when test="@ParticipantObjectID">
                                <xsl:value-of select="@ParticipantObjectID" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="@ParticipantObjectTypeCode">
                                <xsl:value-of select="@ParticipantObjectTypeCode" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="@ParticipantObjectTypeCodeRole">
                                <xsl:value-of select="@ParticipantObjectTypeCodeRole" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:ParticipantObjectIDTypeCode/@code">
                                <xsl:value-of select="n1:ParticipantObjectIDTypeCode/@code" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:ParticipantObjectIDTypeCode/@displayName">
                                <xsl:value-of select="n1:ParticipantObjectIDTypeCode/@displayName" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:ParticipantObjectIDTypeCode/@codeSystemName">
                                <xsl:value-of select="n1:ParticipantObjectIDTypeCode/@codeSystemName" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:ParticipantObjectQuery">
                                <xsl:value-of select="n1:ParticipantObjectQuery" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:ParticipantObjectDetail[@type ='Repository Unique Id']/@type">
                                <xsl:value-of select="n1:ParticipantObjectDetail[@type ='Repository Unique Id']/@type" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:ParticipantObjectDetail[@type ='Repository Unique Id']">
                                <xsl:value-of select="n1:ParticipantObjectDetail[@type ='Repository Unique Id']/@value" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:ParticipantObjectDetail[@type = 'ihe:homeCommunityID']/@type">
                                <xsl:value-of select="n1:ParticipantObjectDetail[@type = 'ihe:homeCommunityID']/@type" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:ParticipantObjectDetail[@type = 'ihe:homeCommunityID']">
                                <xsl:value-of select="n1:ParticipantObjectDetail[@type = 'ihe:homeCommunityID']/@value" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
</xsl:stylesheet>