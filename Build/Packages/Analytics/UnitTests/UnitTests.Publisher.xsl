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

      <xsl:choose>
        <xsl:when test="boolean(//testsuites/testsuite/testcase)">
          <xsl:variable name="UnitTestsWereExecuted" select="boolean(//testsuites/testsuite/testcase)"/>

          <xsl:call-template name="AddStatistic">
            <xsl:with-param name="StatisticName" select="'Executed'"/>
            <xsl:with-param name="StatisticValue" select="$UnitTestsWereExecuted"/>
          </xsl:call-template>

          <xsl:variable name="UnitTestsDuration" select="sum(//testsuite/@time)"/>

          <xsl:call-template name="AddStatistic">
            <xsl:with-param name="StatisticName" select="'UnitTestsDuration'"/>
            <xsl:with-param name="StatisticValue" select="$UnitTestsDuration"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="UnitTestsWereExecuted" select="boolean(($CCNetLog)//task[@name='call']/target[@name='UnitTest.RunTests'])"/>

          <xsl:call-template name="AddStatistic">
            <xsl:with-param name="StatisticName" select="'Executed'"/>
            <xsl:with-param name="StatisticValue" select="$UnitTestsWereExecuted"/>
          </xsl:call-template>

          <xsl:variable name="UnitTestsDuration" select="($CCNetLog)//task[@name='call']/target[@name='UnitTest.RunTests']/parent::node()/duration/text()"/>

          <xsl:call-template name="AddStatistic">
            <xsl:with-param name="StatisticName" select="'UnitTestsDuration'"/>
            <xsl:with-param name="StatisticValue" select="format-number($UnitTestsDuration div 1000,'##0.00')"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>

      <!--Are we runnign JUnit?-->
      <xsl:if test="boolean(//testsuites/testsuite/testcase)">

        <xsl:call-template name="AddStatistic">
          <xsl:with-param name="StatisticName" select="'Total Test Count'"/>
          <xsl:with-param name="StatisticValue" select="sum(//testsuites/testsuite/@tests)"/>
        </xsl:call-template>

        <xsl:call-template name="AddStatistic">
          <xsl:with-param name="StatisticName" select="'Test Run Count'"/>
          <xsl:with-param name="StatisticValue" select="sum(//testsuites/testsuite/@tests)"/>
        </xsl:call-template>

        <xsl:call-template name="AddStatistic">
          <xsl:with-param name="StatisticName" select="'Test Failure Count'"/>
          <xsl:with-param name="StatisticValue" select="sum(//testsuites/testsuite/@failures) + sum(($CCNetLog)/cruisecontrol/build/testsuites/testsuite/testcase/@errors)"/>
        </xsl:call-template>

        <xsl:call-template name="AddStatistic">
          <xsl:with-param name="StatisticName" select="'Test Assertion Count'"/>
          <xsl:with-param name="StatisticValue" select="0"/>
        </xsl:call-template>

      </xsl:if>

      <!--Are we runnign MbUnit?-->
      <xsl:if test="//report-result/counter">

        <xsl:variable name="mbunit.result.list" select="//report-result/counter"/>
        <xsl:variable name="mbunit.assertcount" select="sum($mbunit.result.list/@assert-count)"/>
        <xsl:variable name="mbunit.executedcount" select="sum($mbunit.result.list/@run-count)"/>
        <xsl:variable name="mbunit.failurecount" select="sum($mbunit.result.list/@failure-count)"/>
        <xsl:variable name="mbunit.notrun" select="sum($mbunit.result.list/@skip-count) + sum($mbunit.result.list/@ignore-count)"/>

        <xsl:call-template name="AddStatistic">
          <xsl:with-param name="StatisticName" select="'Total Test Count'"/>
          <xsl:with-param name="StatisticValue" select="$mbunit.executedcount + $mbunit.notrun"/>
        </xsl:call-template>

        <xsl:call-template name="AddStatistic">
          <xsl:with-param name="StatisticName" select="'Test Run Count'"/>
          <xsl:with-param name="StatisticValue" select="$mbunit.executedcount"/>
        </xsl:call-template>

        <xsl:call-template name="AddStatistic">
          <xsl:with-param name="StatisticName" select="'Test Failure Count'"/>
          <xsl:with-param name="StatisticValue" select="$mbunit.failurecount"/>
        </xsl:call-template>

        <xsl:call-template name="AddStatistic">
          <xsl:with-param name="StatisticName" select="'Test Assertion Count'"/>
          <xsl:with-param name="StatisticValue" select="$mbunit.assertcount"/>
        </xsl:call-template>
        
      </xsl:if>

      <!--Are we running MSTest?-->
      <xsl:if test="//Tests/TestRun/result">

        <xsl:variable name="mstest.resultnodes" select="//Tests/TestRun/result" />
        <xsl:variable name="mstest.testcount" select="sum($mstest.resultnodes/totalTestCount)" />

        <xsl:call-template name="AddStatistic">
          <xsl:with-param name="StatisticName" select="'Total Test Count'"/>
          <xsl:with-param name="StatisticValue" select="$mstest.testcount"/>
        </xsl:call-template>
        
        <xsl:variable name="mstest.executedcount" select="sum($mstest.resultnodes/executedTestCount)" />

        <xsl:call-template name="AddStatistic">
          <xsl:with-param name="StatisticName" select="'Test Run Count'"/>
          <xsl:with-param name="StatisticValue" select="$mstest.executedcount"/>
        </xsl:call-template>

        <xsl:variable name="mstest.failurecount" select="$mstest.executedcount - sum($mstest.resultnodes/passedTestCount)" />

        <xsl:call-template name="AddStatistic">
          <xsl:with-param name="StatisticName" select="'Test Failure Count'"/>
          <xsl:with-param name="StatisticValue" select="$mstest.failurecount"/>
        </xsl:call-template>

        <xsl:call-template name="AddStatistic">
          <xsl:with-param name="StatisticName" select="'Test Assertion Count'"/>
          <xsl:with-param name="StatisticValue" select="0"/>
        </xsl:call-template>

      </xsl:if>

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