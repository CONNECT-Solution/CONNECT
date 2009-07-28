package fitnesse.wikitext.widgets;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fitnesse.wikitext.WikiWidget;

public class LastFridayWikiWidget extends WikiWidget {

	private Date today;
	
	protected LastFridayWikiWidget(ParentWidget parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	public static final String REGEXP = "!lastfriday";
	public static final Pattern pattern = Pattern.compile(REGEXP);
	
	private String originalText = "";
	private String token = null;
	
	public LastFridayWikiWidget(ParentWidget parent, String text) {
		super(parent);
	      originalText = text;
	      Matcher match = pattern.matcher(text);
	      if (match.find()) {
	         token = match.group(0);
	      }
	}

	@Override
	public String render() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getToday());
        cal.add(cal.DATE ,-(cal.get(Calendar.DAY_OF_WEEK)+1));
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = cal.getTime();
        return format.format(date);
	}
	


	public Date getToday(){
		if(this.today == null){
			this.today = new Date();
		}
		return this.today;
	}
	
	public void setToday(Date someDate) {
		this.today = someDate;
	}

}
