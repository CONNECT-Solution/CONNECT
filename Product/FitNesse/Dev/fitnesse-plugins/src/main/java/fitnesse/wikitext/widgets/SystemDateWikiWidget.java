package fitnesse.wikitext.widgets;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fitnesse.wikitext.WikiWidget;

public class SystemDateWikiWidget extends WikiWidget {
   private static final String SUBTRACT = "-";
   private static final String ADD = "+";
   private static final String DATE_FORMAT_REGEX = "\\(\\w+.*\\)";
   public static final String REGEXP = "!sysdate(\\(\\w+.*?\\))?(-\\d+|\\+\\d+)?";
   public static final Pattern pattern = Pattern.compile(REGEXP);

   private static final Pattern dateMethodPattern = Pattern.compile(DATE_FORMAT_REGEX);
   private static final Pattern subtractionPattern = Pattern.compile("(-\\d+|\\+\\d+)$");
   private String originalText = "";

   private String token = null;

   public SystemDateWikiWidget(ParentWidget parent, String text) {
      super(parent);
      originalText = text;
      Matcher match = pattern.matcher(text);
      if (match.find()) {
         token = match.group(0);
      }
   }

   private String getCurrentDate(String token) {

      SimpleDateFormat formatter = getDateFormatter(token);
      Date date = getTheDate(token);
      return formatter.format(date);
   }

   private Date getTheDate(String token) {
      Matcher match = subtractionPattern.matcher(token);
      Date date = new Date(System.currentTimeMillis());
      int days = 0;

      if (match.find()) {
         String result = match.group(0);
         if (0 == result.indexOf(SUBTRACT)) {
            days = getIntegerValue(result, SUBTRACT);
         }
         else if (0 == result.indexOf(ADD)) {
            days = getIntegerValue(result, ADD);
         }
         date = getDateDaysFromDate(date, days);
      }
      return date;
   }

   private Date getDateDaysFromDate(Date date, int days) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cal.add(Calendar.DATE, days);
      return cal.getTime();
   }

   private SimpleDateFormat getDateFormatter(String token) {
      SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
      Matcher match = dateMethodPattern.matcher(token);
      if (match.find()) {
         String format = match.group(0);
         format = removeParenthesis(format);
         try {
            formatter = new SimpleDateFormat(format);
         }
         catch (IllegalArgumentException e) {

         }

      }

      return formatter;
   }

   private int getIntegerValue(String result, String modifier) {
      result = result.replaceAll("\\" + modifier, "");
      return (SUBTRACT.equals(modifier)) ? -Integer.parseInt(result) : Integer.parseInt(result);
   }

   private String removeParenthesis(String format) {
      return format.replaceAll("\\(", "").replaceAll("\\)", "");
   }

   public String render() throws Exception {
      return (token != null) ? getCurrentDate(token) : originalText;
   }
}
