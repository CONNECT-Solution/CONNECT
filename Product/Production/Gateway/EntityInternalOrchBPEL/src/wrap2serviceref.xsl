<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" 
    xmlns:sref="http://docs.oasis-open.org/wsbpel/2.0/serviceref">
    <xsl:template match="/">
        <xsl:element name="sref:service-ref">
            <xsl:copy-of select="/"/>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
