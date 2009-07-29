package fitnesse.wikitext.widgets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fitnesse.wiki.PageData;
import fitnesse.wikitext.WidgetBuilder;
import fitnesse.wikitext.WikiWidget;

public class ToUpperCaseWikiWidget extends WikiWidget {

	public static final String REGEXP = "!upper\\((.*)\\)";
	public static final Pattern pattern = Pattern.compile(REGEXP);

	private String originalText = "";

	private String token = null;

	public ToUpperCaseWikiWidget(ParentWidget parent, String text) {
		super(parent);
		originalText = text;
		Matcher match = pattern.matcher(text);
		if (match.find()) {
			token = match.group(1);
		}
	}
	
	private String process(String token) throws Exception{
		String expandedToken = new VariableExpandingWidgetRoot(this.getParent(), token).childHtml();
		ParentWidget root = new WidgetRoot(expandedToken, this.getWikiPage(), WidgetBuilder.htmlWidgetBuilder);
        String expanded = root.render();
		return expanded.toUpperCase();
	}

	@Override
	public String render() throws Exception {
		return (token != null) ? this.process(token) : originalText;
	}

}
