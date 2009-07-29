package fitnesse.wikitext.widgets;

import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.security.wss4j.WSS4JOutHandler;
import org.codehaus.xfire.util.dom.DOMOutHandler;

import com.targetprocess.integration.PasswordHandler;
import com.targetprocess.integration.userstory.UserStoryDTO;
import com.targetprocess.integration.userstory.UserStoryServiceClient;
import com.targetprocess.integration.userstory.UserStoryServiceSoap;

import fitnesse.html.HtmlElement;
import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;
import fitnesse.html.RawHtml;
import fitnesse.wikitext.WikiWidget;

public class UserStoryWikiWidget extends WikiWidget {

	protected UserStoryWikiWidget(ParentWidget parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	private static Random random = new Random();
	public boolean expanded = false;
	public boolean invisible = false;
	private static final String collapsableOpenCss = "collapsable";
	private static final String collapsableInvisibleCss = "invisible";
	private String cssClass = "collapse_rim";
	private static final String collapsableClosedCss = "hidden";
	private static final String collapsableOpenImg = "/files/images/collapsableOpen.gif";
	private static final String collapsableClosedImg = "/files/images/collapsableClosed.gif";
	private static final String collapseAllLink = "<a href=\"javascript:collapseAll();\">Collapse All</a>";
	private static final String expandAllLink = "<a href=\"javascript:expandAll();\">Expand All</a>";

	public static final String REGEXP = "!userStory\\(\\d+\\)";
	public static final Pattern pattern = Pattern.compile(REGEXP);
	private static final Pattern idPattern = Pattern.compile("\\((\\d+)\\)$");

	private String originalText = "";
	private String token = null;
	private String Url;

	public UserStoryWikiWidget(ParentWidget parent, String text) {
		super(parent);
		originalText = text;
		Matcher match = pattern.matcher(text);
		if (match.find()) {
			token = match.group(0);
		}
	}

	@Override
	public String render() throws Exception {
		Matcher match = idPattern.matcher(this.token);
		if (match.find()) {
			int id = Integer.parseInt(match.group(1));
			PasswordHandler.setUserName(this.getWikiPage().getData()
					.getVariable("TP.UserName"));
			PasswordHandler.setPassword(this.getWikiPage().getData()
					.getVariable("TP.Password"));
			this.Url = this.getWikiPage().getData().getVariable("TP.Url");

			return this.retriveStory(id);
		} else {
			return "Could not find find the id for the story!";
		}
	}

	private final UserStoryServiceClient userStoryServiceClient = new UserStoryServiceClient();
	private UserStoryServiceSoap userStoryService;

	public synchronized UserStoryServiceSoap getUserStoryService() {
		if (userStoryService == null) {
			userStoryService = userStoryServiceClient
					.getUserStoryServiceSoap(Url
							+ "/Services/UserStoryService.asmx");
			configureSecurity(userStoryService);
		}
		return userStoryService;
	}

	private void configureSecurity(Object service) {
		Client client = Client.getInstance(service);
		client.addOutHandler(new DOMOutHandler());
		Properties properties = new Properties();
		properties.setProperty(WSHandlerConstants.ACTION,
				WSHandlerConstants.USERNAME_TOKEN);
		properties.setProperty(WSHandlerConstants.PASSWORD_TYPE,
				WSConstants.PW_TEXT);
		properties.setProperty(WSHandlerConstants.USER, PasswordHandler
				.getUserName());
		properties.setProperty(WSHandlerConstants.PW_CALLBACK_CLASS,
				PasswordHandler.class.getName());
		client.addOutHandler(new WSS4JOutHandler(properties));
	}

	protected String retriveStory(int id) throws Exception {
		UserStoryServiceSoap testSubject = getUserStoryService();
		UserStoryDTO userStory = (UserStoryDTO) testSubject.getByID(id);

		String body = "<table><tr><td>fitlibrary.CommentFixture</td><td>true</td></tr><tr><td>"
				+ userStory.getDescription() + "</td></tr></table>";

	    HtmlElement titleElement = new RawHtml("&nbsp;<a href=\"" + Url
				+ "/Project/Planning/UserStory/View.aspx?UserStoryID="
				+ Integer.toString(id) + "\" >Story " + Integer.toString(id)
				+ " " + userStory.getName()
				+ "</a>");
	    HtmlElement bodyElement = new RawHtml(body);
	    HtmlElement html = makeCollapsableSection(titleElement, bodyElement);
	    return html.html();

	}

	public HtmlTag makeCollapsableSection(HtmlElement title, HtmlElement content) {
		String id = random.nextLong() + "";
		// invisible: < recognition
		HtmlTag outerDiv;
		if (invisible) {
			outerDiv = HtmlUtil.makeDivTag(collapsableInvisibleCss);
			outerDiv.add(content);
			return outerDiv;
		}

		outerDiv = HtmlUtil.makeDivTag(cssClass);

		HtmlTag image = new HtmlTag("img");
		image.addAttribute("src", imageSrc());
		image.addAttribute("class", "left");
		image.addAttribute("id", "img" + id);

		HtmlTag anchor = new HtmlTag("a", image);
		anchor.addAttribute("href", "javascript:toggleCollapsable('" + id
				+ "');");

		HtmlTag links = new HtmlTag("div");
		links.addAttribute("style", "float: right;");
		links.addAttribute("class", "meta");
		links.add(expandAllLink + " | " + collapseAllLink);

		outerDiv.add(links);
		outerDiv.add(anchor);
		outerDiv.add(title);

		HtmlTag collapsablediv = makeCollapsableDiv();
		collapsablediv.addAttribute("id", id);
		collapsablediv.add(content);
		outerDiv.add(collapsablediv);

		return outerDiv;
	}

	private HtmlTag makeCollapsableDiv() {
		if (!expanded)
			return HtmlUtil.makeDivTag(collapsableClosedCss);
		else
			return HtmlUtil.makeDivTag(collapsableOpenCss);
	}

	private String imageSrc() {
		if (expanded)
			return collapsableOpenImg;
		else
			return collapsableClosedImg;
	}

}
