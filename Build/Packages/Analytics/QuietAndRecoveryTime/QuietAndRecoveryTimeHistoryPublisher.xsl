<?xml version="1.0"?>
<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:exsl="http://exslt.org/common"
  xmlns:msxsl="urn:schemas-microsoft-com:xslt"
  xmlns:ms="urn:DateScripts"
  exclude-result-prefixes="ms msxsl exsl">

  <msxsl:script implements-prefix="ms" language="C#">
    <![CDATA[
    public static string CalulateQuiteTime(string oldStartTimeString, string oldDurationString, string newStartTimeString)
    {
        System.DateTime OldStartTime = System.DateTime.Parse(oldStartTimeString);
        System.TimeSpan OldDuration = System.TimeSpan.Parse(oldDurationString);
        System.DateTime NewStartTime = System.DateTime.Parse(newStartTimeString);
        OldStartTime.Add(OldDuration);
        System.TimeSpan QuiteTime = NewStartTime.Subtract(OldStartTime);
        return QuiteTime.TotalMinutes.ToString();
    }
    
    public static string CalulateRecoveryTime(string oldStartTimeString, string newDurationString, string newStartTimeString)
    {
        System.DateTime OldStartTime = System.DateTime.Parse(oldStartTimeString);
        System.TimeSpan NewDuration = System.TimeSpan.Parse(newDurationString);
        System.DateTime NewStartTime = System.DateTime.Parse(newStartTimeString);
        NewStartTime.Add(NewDuration);
        System.TimeSpan RecoveryTime = NewStartTime.Subtract(OldStartTime);
        return RecoveryTime.TotalMinutes.ToString();
    }
    
    public string FormatTime(string time)
    {
        return System.TimeSpan.Parse(time).TotalMinutes.ToString();
    }
    
    public static bool LookingForEndRecoveryBuild;
    public static int BeginingFailurePosition;
    
    public static void SetBeginingFailurePosition(int value)
    {
      BeginingFailurePosition = value;
    }
    
    public static int GetBeginingFailurePosition()
    {
      return BeginingFailurePosition;
    }
    
    public static void SetLookingForEndRecoveryBuild(string value)
    {
      LookingForEndRecoveryBuild = Boolean.Parse(value);
    }
    
    public static bool GetLookingForEndRecoveryBuild()
    {
      return LookingForEndRecoveryBuild;
    }
    ]]>
  </msxsl:script>

  <xsl:output method="html"/>

  <xsl:template match="/">
      <xsl:for-each select="/statistics/integration[@status != 'Exception']">
        <xsl:variable name="OldIntegration" select="(preceding-sibling::integration)[last()]" />

        <xsl:if test="position() > 1 and $OldIntegration/@day = @day and $OldIntegration/@status = 'Success'">
          <xsl:variable name="NewStartTime" select="statistic[@name='StartTime']/text()"/>
          <xsl:variable name="OldStartTime" select="$OldIntegration/statistic[@name='StartTime']/text()"/>
          <xsl:variable name="OldDuration" select="$OldIntegration/statistic[@name='Duration']/text()"/>
          
          <xsl:variable name="QuietTime" select="ms:CalulateQuiteTime($OldStartTime, $OldDuration, $NewStartTime)"/>
          <quiettime>
            <xsl:attribute name="duration">
              <xsl:value-of select="$QuietTime"/>
            </xsl:attribute>
            <between>
              <build>
                <xsl:attribute name="possition">
                  <xsl:value-of select="1"/>
                </xsl:attribute>
                <xsl:attribute name="starttime">
                  <xsl:value-of select="$OldStartTime"/>
                </xsl:attribute>
                <xsl:attribute name="duration">
                  <xsl:value-of select="ms:FormatTime($OldDuration)"/>
                </xsl:attribute>
                <xsl:attribute name="status">
                  <xsl:value-of select="$OldIntegration/@status"/>
                </xsl:attribute>

                <xsl:attribute name="day">
                  <xsl:value-of select="$OldIntegration/@day"/>
                </xsl:attribute>
                <xsl:attribute name="month">
                  <xsl:value-of select="$OldIntegration/@month"/>
                </xsl:attribute>
                <xsl:attribute name="year">
                  <xsl:value-of select="$OldIntegration/@year"/>
                </xsl:attribute>
                <xsl:attribute name="week">
                  <xsl:value-of select="$OldIntegration/@week"/>
                </xsl:attribute>
                <xsl:attribute name="dayofyear">
                  <xsl:value-of select="$OldIntegration/@dayofyear"/>
                </xsl:attribute>
                <xsl:attribute name="hourofday">
                  <xsl:value-of select="$OldIntegration/@hourofday"/>
                </xsl:attribute>
              </build>
              <build>
                <xsl:attribute name="possition">
                  <xsl:value-of select="2"/>
                </xsl:attribute>
                <xsl:attribute name="starttime">
                  <xsl:value-of select="$NewStartTime"/>
                </xsl:attribute>
                <xsl:attribute name="duration">
                  <xsl:value-of select="ms:FormatTime(statistic[@name='Duration']/text())"/>
                </xsl:attribute>
                <xsl:attribute name="status">
                  <xsl:value-of select="@status"/>
                </xsl:attribute>

                <xsl:attribute name="day">
                  <xsl:value-of select="@day"/>
                </xsl:attribute>
                <xsl:attribute name="month">
                  <xsl:value-of select="@month"/>
                </xsl:attribute>
                <xsl:attribute name="year">
                  <xsl:value-of select="@year"/>
                </xsl:attribute>
                <xsl:attribute name="week">
                  <xsl:value-of select="@week"/>
                </xsl:attribute>
                <xsl:attribute name="dayofyear">
                  <xsl:value-of select="@dayofyear"/>
                </xsl:attribute>
                <xsl:attribute name="hourofday">
                  <xsl:value-of select="@hourofday"/>
                </xsl:attribute>
              </build>
            </between>
          </quiettime>
        </xsl:if>

        <xsl:if test="position() > 1 and @status = 'Success' and $OldIntegration/@status = 'Failure'">
          <xsl:variable name="NewStartTime" select="statistic[@name='StartTime']/text()"/>
          <xsl:variable name="NewDuration" select="statistic[@name='Duration']/text()"/>
          <xsl:variable name="BeginingFailurePosition" select="ms:GetBeginingFailurePosition()" />
          <xsl:variable name="BeginingFailure" select="(preceding-sibling::integration)[$BeginingFailurePosition]"/>
          <xsl:variable name="OldStartTime" select="$BeginingFailure/statistic[@name='StartTime']/text()"/>
          
          <xsl:variable name="RecoveryTime" select="ms:CalulateRecoveryTime($OldStartTime, $NewDuration, $NewStartTime)"/>

          <xsl:variable name="BuildAttempts" select="preceding-sibling::integration[position() > $BeginingFailurePosition - 1]"/>
          <recoverytime>
            <xsl:attribute name="duration">
              <xsl:value-of select="$RecoveryTime"/>
            </xsl:attribute>
            <between>
              <build>
                <xsl:attribute name="possition">
                  <xsl:value-of select="1"/>
                </xsl:attribute>
                <xsl:attribute name="starttime">
                  <xsl:value-of select="$OldStartTime"/>
                </xsl:attribute>
                <xsl:attribute name="duration">
                  <xsl:value-of select="ms:FormatTime($BeginingFailure/statistic[@name='Duration']/text())"/>
                </xsl:attribute>
                <xsl:attribute name="status">
                  <xsl:value-of select="$BeginingFailure/@status"/>
                </xsl:attribute>

                <xsl:attribute name="day">
                  <xsl:value-of select="$BeginingFailure/@day"/>
                </xsl:attribute>
                <xsl:attribute name="month">
                  <xsl:value-of select="$BeginingFailure/@month"/>
                </xsl:attribute>
                <xsl:attribute name="year">
                  <xsl:value-of select="$BeginingFailure/@year"/>
                </xsl:attribute>
                <xsl:attribute name="week">
                  <xsl:value-of select="$BeginingFailure/@week"/>
                </xsl:attribute>
                <xsl:attribute name="dayofyear">
                  <xsl:value-of select="$BeginingFailure/@dayofyear"/>
                </xsl:attribute>
                <xsl:attribute name="hourofday">
                  <xsl:value-of select="$BeginingFailure/@hourofday"/>
                </xsl:attribute>
              </build>
              <build>
                <xsl:attribute name="possition">
                  <xsl:value-of select="2"/>
                </xsl:attribute>
                <xsl:attribute name="starttime">
                  <xsl:value-of select="$NewStartTime"/>
                </xsl:attribute>
                <xsl:attribute name="duration">
                  <xsl:value-of select="ms:FormatTime(statistic[@name='Duration']/text())"/>
                </xsl:attribute>
                <xsl:attribute name="status">
                  <xsl:value-of select="@status"/>
                </xsl:attribute>

                <xsl:attribute name="day">
                  <xsl:value-of select="@day"/>
                </xsl:attribute>
                <xsl:attribute name="month">
                  <xsl:value-of select="@month"/>
                </xsl:attribute>
                <xsl:attribute name="year">
                  <xsl:value-of select="@year"/>
                </xsl:attribute>
                <xsl:attribute name="week">
                  <xsl:value-of select="@week"/>
                </xsl:attribute>
                <xsl:attribute name="dayofyear">
                  <xsl:value-of select="@dayofyear"/>
                </xsl:attribute>
                <xsl:attribute name="hourofday">
                  <xsl:value-of select="@hourofday"/>
                </xsl:attribute>
              </build>
            </between>
          </recoverytime>
          <xsl:variable name="Execute1" select="ms:SetLookingForEndRecoveryBuild('False')"/>
        </xsl:if>

        <xsl:if test="not (ms:GetLookingForEndRecoveryBuild()) and @status = 'Failure'">
          <xsl:variable name="Execute1" select="ms:SetLookingForEndRecoveryBuild('True')"/>
          <xsl:variable name="Execute2" select="ms:SetBeginingFailurePosition(count(preceding-sibling::integration)+1)"/>
        </xsl:if>
      </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
