package fitnesse.wikitext.widgets;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import fitnesse.wiki.*;  
	import fitnesse.wikitext.WidgetBuilder; 
import fitnesse.wikitext.WikiWidget;

	public class UserStoryWikiWidgetUnitTest {

		private WikiPage root;
	    private PageCrawler crawler;
	    private WikiPage page;
	    private WidgetRoot widgetRoot;

	    @Before
	    public void setUp() throws Exception
	    {
	        root = InMemoryPage.makeRoot("root");
	        crawler = root.getPageCrawler();
	        page = crawler.addPage(root, PathParser.parse("MyPage"));
	        widgetRoot = new WidgetRoot("", page);
	    }

	    @Test
	    public void shouldMatch() throws Exception
	    {
	        assertMatches("!userStory(1)");
	        assertMatches("!userStory(12)");
	        assertMatches("!userStory(123)");
	    }

		protected String getRegexp()
	    {
	        return UserStoryWikiWidget.REGEXP;
	    }

		@Test
	    public void shouldRenderStoryTitleAndDescription() throws Exception{
	        WikiPage parent = crawler.addPage(root, PathParser.parse("ParentPage"), "!userStory(1)");
	        WikiPage child = crawler.addPage(parent, PathParser.parse("ChildPage"), "ick");

	        WidgetRoot widgetRoot = new WidgetRoot("", child, WidgetBuilder.htmlWidgetBuilder);
	        WikiWidget w = new UserStoryWikiWidget(widgetRoot, "!userStory(12)"){
	        	protected String retriveStory(int id) {
	        		return "Boo";
	        	}
	        };
	        
	        assertEquals("Boo", w.render());
	    }

		@Test
	    public void shouldRenderRealStoryTitleAndDescription() throws Exception{
	        WikiPage parent = crawler.addPage(root, PathParser.parse("ParentPage"), "!userStory(1)");
	        WikiPage child = crawler.addPage(parent, PathParser.parse("ChildPage"), "ick");

	        WidgetRoot widgetRoot = new WidgetRoot("", child, WidgetBuilder.htmlWidgetBuilder);
	        WikiWidget w = new UserStoryWikiWidget(widgetRoot, "!userStory(36)");
	        
	        assertEquals("Boo", w.render());
	    }
	    
	    private void assertMatches(String s) {
	    	assertMatchEquals(s,s);
		}
	
	    protected void assertMatchEquals(String value, String expected)
		{
			Matcher match = Pattern.compile(getRegexp(), Pattern.DOTALL | Pattern.MULTILINE).matcher(value);
			if(expected != null)
			{
				assertTrue("pattern not found in: " + value, match.find());
				assertEquals(expected, match.group());
			}
			else
			{
				boolean found = match.find();
				assertTrue((found ? match.group() : "nothing") + " was found in: " + value, !found);
			}
		}
}
