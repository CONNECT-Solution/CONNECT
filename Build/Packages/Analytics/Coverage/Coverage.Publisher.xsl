<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:msxsl="urn:schemas-microsoft-com:xslt"
  xmlns:ms="urn:DateScripts"
  exclude-result-prefixes="ms msxsl">

  <msxsl:script implements-prefix="ms" language="C#">
    <![CDATA[
    public string FormatDate(string dateTime, string format)
    {
      return System.DateTime.Parse(dateTime).ToString(format);
    }
    
    public static int DayOfYear(string dateTimeString)
    {
      DateTime dt = System.DateTime.Parse(dateTimeString);
      // Set Year
      int yyyy=dt.Year;

      // Set Month
      int mm=dt.Month;
      
      // Set Day
      int dd=dt.Day;

      // Declare other required variables
      int DayOfYearNumber;

      int[] Mnth = new int[12] {0,31,59,90,120,151,181,212,243,273,304,334};
      
      // Set DayofYear Number for yyyy mm dd
      DayOfYearNumber = dd + Mnth[mm-1];
      return DayOfYearNumber;
    }
    
    public static int WeekNumber(string dateTimeString)
    {
      DateTime dt = System.DateTime.Parse(dateTimeString);
      // Set Year
      int yyyy=dt.Year;

      // Set Month
      int mm=dt.Month;
      
      // Set Day
      int dd=dt.Day;

      // Declare other required variables
      int DayOfYearNumber;
      int Jan1WeekDay;
      int WeekNumber=0, WeekDay;


      int i,j,k,l,m,n;
      int[] Mnth = new int[12] {0,31,59,90,120,151,181,212,243,273,304,334};

      int YearNumber;

      
      // Set DayofYear Number for yyyy mm dd
      DayOfYearNumber = dd + Mnth[mm-1];

      // Increase of Dayof Year Number by 1, if year is leapyear and month is february
      if ((IsLeapYear(yyyy) == true) && (mm == 2))
          DayOfYearNumber += 1;

      // Find the Jan1WeekDay for year 
      i = (yyyy - 1) % 100;
      j = (yyyy - 1) - i;
      k = i + i/4;
      Jan1WeekDay = 1 + (((((j / 100) % 4) * 5) + k) % 7);

      // Calcuate the WeekDay for the given date
      l= DayOfYearNumber + (Jan1WeekDay - 1);
      WeekDay = 1 + ((l - 1) % 7);

      // Find if the date falls in YearNumber set WeekNumber to 52 or 53
      if ((DayOfYearNumber <= (8 - Jan1WeekDay)) && (Jan1WeekDay > 4))
        {
          YearNumber = yyyy - 1;
          if ((Jan1WeekDay == 5) || ((Jan1WeekDay == 6) && (Jan1WeekDay > 4)))
              WeekNumber = 53;
          else
              WeekNumber = 52;
        }
      else
          YearNumber = yyyy;
      

      // Set WeekNumber to 1 to 53 if date falls in YearNumber
      if (YearNumber == yyyy)
        {
          if (IsLeapYear(yyyy)==true)
              m = 366;
          else
              m = 365;
          if ((m - DayOfYearNumber) < (4-WeekDay))
          {
              YearNumber = yyyy + 1;
              WeekNumber = 1;
          }
        }
      
      if (YearNumber==yyyy) {
          n=DayOfYearNumber + (7 - WeekDay) + (Jan1WeekDay -1);
          WeekNumber = n / 7;
          if (Jan1WeekDay > 4)
              WeekNumber -= 1;
      }

      return (WeekNumber);
    }
    
    public static bool IsLeapYear(int yyyy)
    {
        if ((yyyy % 4 == 0 && yyyy % 100 != 0) || (yyyy % 400 == 0))
            return true;
        else
            return false;
    } 
    ]]>
  </msxsl:script>

  <xsl:output method="html"/>

  <xsl:param name="CCNetLabel" />
  <xsl:param name="CCNetLogFilePath" />
  <xsl:variable name="CCNetLog" select="document($CCNetLogFilePath)"/>

  <xsl:template match="/">
    
    <integration>
      <xsl:attribute name="build-label">
        <xsl:value-of select="($CCNetLog)/cruisecontrol/build/@label"/>
      </xsl:attribute>

      <xsl:attribute name="status">
        <xsl:choose>
          <xsl:when test="($CCNetLog)/cruisecontrol/exception">
            <xsl:value-of select="'Exception'"/>
          </xsl:when>
          <xsl:when test="($CCNetLog)/cruisecontrol/build/@error">
            <xsl:value-of select="'Failure'"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="'Success'"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
      
      <xsl:attribute name="day">
        <xsl:value-of select="ms:FormatDate(($CCNetLog)/cruisecontrol/build/@date, 'dd')"/>
      </xsl:attribute>
      <xsl:attribute name="month">
        <xsl:value-of select="ms:FormatDate(($CCNetLog)/cruisecontrol/build/@date, 'MM')"/>
      </xsl:attribute>
      <xsl:attribute name="year">
        <xsl:value-of select="ms:FormatDate(($CCNetLog)/cruisecontrol/build/@date, 'yyyy')"/>
      </xsl:attribute>
      <xsl:attribute name="week">
        <xsl:value-of select="ms:WeekNumber(($CCNetLog)/cruisecontrol/build/@date)"/>
      </xsl:attribute>
      <xsl:attribute name="dayofyear">
        <xsl:value-of select="ms:DayOfYear(($CCNetLog)/cruisecontrol/build/@date)"/>
      </xsl:attribute>
      <xsl:attribute name="hourofday">
        <xsl:value-of select="ms:FormatDate(($CCNetLog)/cruisecontrol/build/@date, '%H')"/>
      </xsl:attribute>

      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'StartTime'"/>
        <xsl:with-param name="StatisticValue" select="($CCNetLog)/cruisecontrol/build/@date"/>
      </xsl:call-template>

      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'Duration'"/>
        <xsl:with-param name="StatisticValue" select="($CCNetLog)/cruisecontrol/build/@buildtime"/>
      </xsl:call-template>
      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'ProjectName'"/>
        <xsl:with-param name="StatisticValue" select="($CCNetLog)/cruisecontrol/@project"/>
      </xsl:call-template>

      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'IterationName'"/>
        <xsl:with-param name="StatisticValue" select="($CCNetLog)//TargetProcess/Iteration/@name[1]"/>
      </xsl:call-template>

      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'IterationStartDate'"/>
        <xsl:with-param name="StatisticValue" select="($CCNetLog)//TargetProcess/Iteration/@startdate[1]"/>
      </xsl:call-template>

      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'IterationEndDate'"/>
        <xsl:with-param name="StatisticValue" select="($CCNetLog)//TargetProcess/Iteration/@enddate[1]"/>
      </xsl:call-template>


      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'mainsubmitter'"/>
        <xsl:with-param name="StatisticValue" select="($CCNetLog)/cruisecontrol/modifications/modification/user[1]"/>
      </xsl:call-template>
      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'buildcondition'"/>
        <xsl:with-param name="StatisticValue" select="($CCNetLog)/cruisecontrol/build/@buildcondition"/>
      </xsl:call-template>
      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'forcedby'"/>
        <xsl:with-param name="StatisticValue" select="($CCNetLog)/cruisecontrol/build/ForcedBuildInformation/@UserName"/>
      </xsl:call-template>

      
      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'linecoverage'"/>
        <xsl:with-param name="StatisticValue" select="format-number(/coverage/@line-rate, '#.0000')*100"/>
      </xsl:call-template>

      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'branchcoverage'"/>
        <xsl:with-param name="StatisticValue" select="format-number(/coverage/@branch-rate, '#.0000')*100"/>
      </xsl:call-template>

      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'linesexecuted'"/>
        <xsl:with-param name="StatisticValue" select="/coverage/@lines-covered"/>
      </xsl:call-template>

      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'linecount'"/>
        <xsl:with-param name="StatisticValue" select="/coverage/@lines-valid"/>
      </xsl:call-template>

      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'branchesexecuted'"/>
        <xsl:with-param name="StatisticValue" select="/coverage/@branches-covered"/>
      </xsl:call-template>

      <xsl:call-template name="AddStatistic">
        <xsl:with-param name="StatisticName" select="'branchcount'"/>
        <xsl:with-param name="StatisticValue" select="/coverage/@branches-valid"/>
      </xsl:call-template>

    </integration>
  </xsl:template>

  <xsl:template name="AddStatistic">
    <xsl:param name="StatisticName"/>
    <xsl:param name="StatisticValue"/>
    <statistic>
      <xsl:attribute name="name">
        <xsl:value-of select="$StatisticName"/>
      </xsl:attribute>
      <xsl:value-of select="$StatisticValue"/>
    </statistic>
  </xsl:template>
  
</xsl:stylesheet>