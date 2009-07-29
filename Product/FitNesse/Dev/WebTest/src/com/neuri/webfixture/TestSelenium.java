package com.neuri.webfixture;


public class TestSelenium {
	public static void main(String[] args) {

/*		 Selenium sel = new DefaultSelenium(
	             "localhost", 4444, "*chrome /opt/firefox/firefox-bin", "http://www.google.com");
	           sel.start();
	           sel.open("http://www.google.com/");
	           sel.type("q", "FitNesse");
	           sel.click("btnG");
	           sel.waitForPageToLoad("3000");
*/
		WebTest wt=new WebTest();
		wt.startBrowserWithSeleniumConsoleOnAtPortAndScriptsAt("*chrome /opt/firefox/firefox-bin", "localhost", 4444,"http://www.google.com");
		wt.userOpensURL("http://www.google.com");
		System.out.println(wt.getLocator("q",wt.textFieldLocators));
		wt.shutdownBrowser();
	}
}
