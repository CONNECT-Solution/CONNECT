package fitnesse.wikitext.widgets;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fitnesse.wiki.*;  
import fitnesse.wikitext.WidgetBuilder; 
import fitnesse.wikitext.WikiWidget;

public class LastFridayWikiWidgetTest extends WidgetTestCase {

	private WikiPage root;
    private PageCrawler crawler;
    private WikiPage page;
    private WidgetRoot widgetRoot;

    public void setUp() throws Exception
    {
        root = InMemoryPage.makeRoot("root");
        crawler = root.getPageCrawler();
        page = crawler.addPage(root, PathParser.parse("MyPage"));
        widgetRoot = new WidgetRoot("", page);
    }

    public void testMatches() throws Exception
    {
        assertMatch("!lastfriday");
    }

    protected String getRegexp()
    {
        return LastFridayWikiWidget.REGEXP;
    }

    public void testDateVariable() throws Exception{
        WikiPage parent = crawler.addPage(root, PathParser.parse("ParentPage"), "!lastfriday");
        WikiPage child = crawler.addPage(parent, PathParser.parse("ChildPage"), "ick");

        WidgetRoot widgetRoot = new WidgetRoot("", child, WidgetBuilder.htmlWidgetBuilder);
        WikiWidget w = new LastFridayWikiWidget(widgetRoot, "!lastfriday");
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(cal.DATE ,-(cal.get(Calendar.DAY_OF_WEEK)+1));
        
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = cal.getTime();
        String dateString = format.format(date);
        
        assertEquals(dateString, w.render());
    }

    public void testDateVariableFromYesterdaysDate() throws Exception{
        WikiPage parent = crawler.addPage(root, PathParser.parse("ParentPage"), "!lastfriday");
        WikiPage child = crawler.addPage(parent, PathParser.parse("ChildPage"), "ick");

        WidgetRoot widgetRoot = new WidgetRoot("", child, WidgetBuilder.htmlWidgetBuilder);
        WikiWidget testSubject = new LastFridayWikiWidget(widgetRoot, "!lastfriday");
        
        Calendar cal = Calendar.getInstance();
        
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -8);
        Date oldToday = cal.getTime();
        
        ((LastFridayWikiWidget)testSubject).setToday(oldToday);
        
		cal.setTime(oldToday);
        cal.add(cal.DATE ,-(cal.get(Calendar.DAY_OF_WEEK)+1));
        
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = cal.getTime();
        String dateString = format.format(date);
        
        assertEquals(dateString, testSubject.render());
    }
}
