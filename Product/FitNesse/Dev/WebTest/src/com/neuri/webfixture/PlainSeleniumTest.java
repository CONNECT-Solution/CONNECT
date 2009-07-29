package com.neuri.webfixture;

import com.thoughtworks.selenium.*;

import fit.Fixture;
import fit.Parse;
import fitlibrary.SequenceFixture;
import fitlibrary.table.Row;
import fitlibrary.traverse.workflow.SequenceTraverse;
import fitlibrary.utility.TestResults;

public class PlainSeleniumTest extends SequenceFixture {

	public Row currentRow;
	public TestResults currentTestResults;
	private SequenceTraverse sequenceTraverse;
	private DefaultSelenium browser;
	private String baseUrl;

	/****************************** Begin-> Overrides to grab Row and TestResults ********************************/
	public PlainSeleniumTest() {
		sequenceTraverse = new SequenceTraverse(this) {
			@Override
			public Object interpretRow(Row row, TestResults testResults,
					Fixture fixtureByName) {
				((PlainSeleniumTest) this.getSystemUnderTest()).currentRow = row;
				((PlainSeleniumTest) this.getSystemUnderTest()).currentTestResults = testResults;
				return super.interpretRow(row, testResults, fixtureByName);
			}
		};
		setTraverse(sequenceTraverse);
	}

	public PlainSeleniumTest(Object sut) {
		this();
		setSystemUnderTest(sut);
	}
	/****************************** End-> Overrides to grab Row and TestResults ********************************/
	
	/****************************** Begin-> Browser Control ********************************/
	private String getBrowserCode(String browser) {
		if ("IE".equalsIgnoreCase(browser))
			return "*iehta";
		if ("FIREFOX".equalsIgnoreCase(browser))
			return "*chrome";
		return browser;
	}

	public void startBrowser(String browserType, String rcServer, int rcPort,
			String seleniumURL) {
		baseUrl = seleniumURL;
		browser = new DefaultSelenium(rcServer, rcPort,
				getBrowserCode(browserType), seleniumURL);
		browser.start();
		this.setSystemUnderTest(browser);
	}

	public void startBrowser(String handle, String browserType,
			String rcServer, int rcPort, String seleniumURL) {
		this.startBrowser(browserType, rcServer, rcPort, seleniumURL);
		Fixture.setSymbol(handle, browser);
	}

	public void reuseBrowser(String handle) {
		browser = (DefaultSelenium) Fixture.getSymbol(handle);
		this.setSystemUnderTest(browser);
	}

	public void shutdownBrowser() {
		browser.stop();
	}

	public void shutdownBrowser(String handle) {
		browser = (DefaultSelenium) Fixture.getSymbol(handle);
		this.shutdownBrowser();
	}

	// override open to allow for relative URLs
	public void open(String url) {
		if (!url.startsWith("http://"))
			browser.open(baseUrl + url);
		else
			browser.open(url);
	}
	
	public void open(String url, String ignore) {
		this.checkComment(ignore);
		open(url);
	}
	
	/****************************** End-> Browser Control ********************************/
	
	// general timeout for waiters
	public int timeout = 10;

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/****************************** Begin-> Wait Classes ********************************/
	private class WaitForTextToAppear extends Wait {
		private String text;

		public WaitForTextToAppear(String text) {
			this.text = text;
		}

		public boolean until() {
			return browser.isTextPresent(text);
		}
	}

	private class WaitForChecked extends Wait {
		private String locator;
		private boolean checked;

		public WaitForChecked(String locator, boolean checked) {
			this.locator = locator;
			this.checked = checked;
		}

		public boolean until() {
			if (!browser.isElementPresent(locator))
				return false;
			return browser.isChecked(locator) == checked;
		}
	}

	private class WaitForElementToAppear extends Wait {
		private String text;

		public WaitForElementToAppear(String text) {
			this.text = text;
		}

		public boolean until() {
			return browser.isElementPresent(text);
		}
	}

	private class WaitForElementToDisappear extends Wait {
		private String text;

		public WaitForElementToDisappear(String text) {
			this.text = text;
		}

		public boolean until() {
			return (!browser.isElementPresent(text));
		}
	}

	private class WaitForFieldValue extends Wait {
		private String text;
		private String elementLocator;

		public WaitForFieldValue(String element, String value) {
			this.text = value;
			this.elementLocator = element;
		}

		public boolean until() {
			String value = browser.getValue(elementLocator);
			if (value == null)
				return false;
			return value.indexOf(text) >= 0;
		}
	}
	/****************************** End-> Wait Classes ********************************/

	// utility method for pausing
	public void pause(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException iex) {
			// nothing important, just silently ignore
		}
	}

	public void pause(int milliseconds, String ignore) {
		this.checkComment(ignore);
		pause(milliseconds);
	}

	public void click(String locator, String ignore) {
		this.checkComment(ignore);
		locator = this.getExpandedValue(locator);
		try{
			browser.click(locator);
		} catch (Exception ex) {
			this.fail(locator, ex.getMessage());
		}
	}

	public void clickAndWait(String locator, String ignore) {
		this.checkComment(ignore);
		locator = this.getExpandedValue(locator);
		try{
			browser.click(locator);
			browser.waitForPageToLoad(Integer.toString(timeout * 1000));
		} catch (Exception ex) {
			this.fail(locator, ex.getMessage());
		}
	}

	public void clickAndWait(String locator) {
		locator = this.getExpandedValue(locator);
		try{
			browser.click(locator);
			browser.waitForPageToLoad(Integer.toString(this.timeout * 1000));
		} catch (Exception ex) {
			this.fail(locator, ex.getMessage());
		}
	}

	public boolean waitForTextPresent(String text, String ignore) {
		this.checkComment(ignore);
		return waitForTextPresent(text);
	}

	public boolean waitForTextPresent(String text) {
		text = this.getExpandedValue(text);
		Wait x = new WaitForTextToAppear(text);
		try{
			x.wait("Cannot find text " + text + " after " + timeout + " seconds",
				timeout * 1000);
		} catch (Exception ex) {
			this.colorFail(text, ex.getMessage());
			return false;
		}
		return true;
	}

	public boolean waitForElementPresent(String locator, String ignore) {
		this.checkComment(ignore);
		return waitForElementPresent(locator);
	}

	public boolean waitForElementPresent(String locator) {
		locator = this.getExpandedValue(locator);
		Wait x = new WaitForElementToAppear(locator);
		try{
			x.wait("Cannot find element " + locator + " after " + timeout
				+ " seconds", timeout * 1000);

		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
		return true;
	}

	public boolean waitForElementNotPresent(String text, String ignore) {
		this.checkComment(ignore);
		return waitForElementNotPresent(text);
	}

	public boolean waitForElementNotPresent(String locator) {
		locator = this.getExpandedValue(locator);
		Wait x = new WaitForElementToDisappear(locator);
		try{
			x.wait("Element " + locator + " found after " + timeout + " seconds",
				timeout * 1000);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
		return true;
	}

	public boolean waitForFieldValue(String field, String value) {
		field = this.getExpandedValue(field);
		Wait x = new WaitForFieldValue(field, value);
		try{
			x.wait("Cannot find value" + value + " in " + field + " after "
				+ timeout + " seconds", timeout * 1000);
		} catch (Exception ex) {
			this.colorFail(field, ex.getMessage());
			return false;
		}
		return true;
	}

	public boolean waitForChecked(String locator, boolean checked) {
		locator = this.getExpandedValue(locator);
		Wait x = new WaitForChecked(locator, checked);
		try{
			x.wait("Checkbox " + locator + " is " + (checked ? "not" : "still")
					+ " checked after " + timeout + " seconds", timeout * 1000);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
		return true;
	}

	public boolean waitForChecked(String locator) {
		return waitForChecked(locator, true);
	}

	// //////// verify methods
	public boolean verifyElementPresent(String locator) {
		locator = this.getExpandedValue(locator);
		try {
			return assertTrue(browser.isElementPresent(locator) ,"Location was not found: " + locator, 1);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
	}

	public boolean verifyElementPresent(String locator, String ignore) {
		this.checkComment(ignore);
		return verifyElementPresent(locator);
	}

	public boolean verifyElementNotPresent(String locator) {
		locator = this.getExpandedValue(locator);
		try {
			return assertFalse(browser.isElementPresent(locator) ,"Location was found: " + locator, 1);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
	}

	public boolean verifyElementNotPresent(String locator, String ignore) {
		this.checkComment(ignore);
		return verifyElementNotPresent(locator);
	}

	public boolean verifyTextPresent(String text) {
		text = this.getExpandedValue(text);
		try{
			return this.assertTrue(browser.isTextPresent(text), "Could not find the text '" + text + "'.", 1);
		} catch (Exception ex) {
			this.colorFail(ex.getMessage(), 1);
			return false;
		}
	}

	public boolean verifyTextPresent(String text, String ignore) {
		this.checkComment(ignore);
		return verifyTextPresent(text);
	}

	public boolean verifyTextNotPresent(String text) {
		text = this.getExpandedValue(text);
		try{
			return this.assertFalse(browser.isTextPresent(text), "The text was found '" + text + "'.", 1);
		} catch (Exception ex) {
			this.colorFail(ex.getMessage(), 1);
			return false;
		}
	}

	public boolean verifyTextNotPresent(String text, String ignore) {
		this.checkComment(ignore);
		return verifyTextNotPresent(text);
	}

	public boolean verifySelectedLabel(String locator, String label) {
		locator = this.getExpandedValue(locator);
		label = this.getExpandedValue(label);
		try{
			return this.assertEquals(label, browser.getSelectedLabel(locator), 2);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
	}

	public boolean verifyValue(String locator, String value) {
		locator = this.getExpandedValue(locator);
		value = this.getExpandedValue(value);
		try{
			return this.assertEquals(value, browser.getValue(locator), 2);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
	}

	public boolean verifyText(String locator, String value) {
		locator = this.getExpandedValue(locator);
		value = this.getExpandedValue(value);
		try{
			return this.assertEquals(value, browser.getText(locator), 2);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
	}

	public boolean verifyTable(String locator, String value) {
		locator = this.getExpandedValue(locator);
		value = this.getExpandedValue(value);
		try{
			String foundValue = browser.getTable(locator);
			return this.assertEquals(value, foundValue, 2);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
	}

	public boolean verifyVisible(String locator) {
		locator = this.getExpandedValue(locator);
		try {
			return assertTrue(browser.isVisible(locator) ,"Location was not visible: " + locator, 1);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
	}

	public boolean verifyVisible(String locator, String ignore) {
		this.checkComment(ignore);
		return verifyVisible(locator);
	}

	public boolean verifyNotVisible(String locator) {
		locator = this.getExpandedValue(locator);
		try {
			return assertFalse(browser.isVisible(locator) ,"Location was visible: " + locator, 1);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
	}

	public boolean verifyNotVisible(String locator, String ignore) {
		this.checkComment(ignore);
		return verifyNotVisible(locator);
	}

	public boolean verifyEditable(String locator) {
		locator = this.getExpandedValue(locator);
		try {
			return assertTrue(browser.isEditable(locator) ,"Location was not editable: " + locator, 1);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
	}

	public boolean verifyEditable(String locator, String ignore) {
		this.checkComment(ignore);
		return verifyEditable(locator);
	}

	public boolean verifyNotEditable(String locator) {
		locator = this.getExpandedValue(locator);
		try {
			return assertFalse(browser.isEditable(locator) ,"Location was editable: " + locator, 1);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
	}

	public boolean verifyNotEditable(String locator, String ignore) {
		this.checkComment(ignore);
		return verifyNotEditable(locator);
	}

	public boolean verifyChecked(String locator) {
		locator = this.getExpandedValue(locator);
		try {
			return assertTrue(browser.isChecked(locator) ,"Location was not checked: " + locator, 1);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
	}

	public boolean verifyChecked(String locator, String ignore) {
		this.checkComment(ignore);
		return verifyChecked(locator);
	}

	public boolean verifyNotChecked(String locator) {
		locator = this.getExpandedValue(locator);
		try {
			return assertFalse(browser.isChecked(locator) ,"Location was checked: " + locator, 1);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
	}

	public boolean verifyNotChecked(String locator, String ignore) {
		this.checkComment(ignore);
		return verifyNotChecked(locator);
	}

	// map to allow typing in an empty string
	public void type(String locator) {
		locator = this.getExpandedValue(locator);
		try{
			browser.type(locator, "");
		} catch (Exception ex) {
			this.fail(locator, ex.getMessage());
		}
	}

	public void type(String locator, String value) {
		locator = this.getExpandedValue(locator);
		value = this.getExpandedValue(value);
		try{
			browser.type(locator, value);
		} catch (Exception ex) {
			this.fail(locator, ex.getMessage());
		}
	}
	
	public void checkBox(String locator){
		locator = this.getExpandedValue(locator);
		try{
			browser.check(locator);
		}catch (Exception ex){
			this.fail(locator, ex.getMessage());
		}
	}
	
	public void checkBox(String locator, String ignore){
		this.checkComment(ignore);
		this.checkBox(locator);
	}
	
	public void uncheck(String locator, String ignore){
		this.checkComment(ignore);
		this.uncheck(locator);
	}
	
	public void uncheck(String locator){
		locator = this.getExpandedValue(locator);
		try{
			browser.uncheck(locator);
		}catch (Exception ex){
			this.fail(locator, ex.getMessage());
		}
	}
	
	public void clickAt(String locator, String coordString){
		locator = this.getExpandedValue(locator);
		coordString = this.getExpandedValue(coordString);
		try{
			browser.clickAt(locator, coordString);
		}catch (Exception ex){
			this.fail(locator, ex.getMessage());
		}
	}

	// /// alerts and confirmations
	public boolean assertAlertPresent(String text) {
		text = this.getExpandedValue(text);
		try{
			return assertEquals(text, browser.getAlert(), 1);
		} catch (Exception ex) {
			this.colorFail(ex.getMessage(), 1);
			return false;
		}
	}

	public boolean assertConfirmation(String text) {
		text = this.getExpandedValue(text);
		try{
			return assertEquals(text, browser.getConfirmation(), 1);
		} catch (Exception ex) {
			this.colorFail(ex.getMessage(), 1);
			return false;
		}
	}

	// /// select options
	public boolean assertSelectOptions(String locator, String options) {
		locator = this.getExpandedValue(locator);
		options = this.getExpandedValue(options);
		try {
			String selectOptions = getSelectOptions(locator);
			return this.assertEquals(selectOptions, options, 2);
		} catch (Exception ex) {
			this.colorFail(locator, ex.getMessage());
			return false;
		}
	}

	public String getSelectOptions(String locator) {
		locator = this.getExpandedValue(locator);
		StringBuilder sb = new StringBuilder();
		boolean notfirst = false;
		for (String s : browser.getSelectOptions(locator)) {
			if (notfirst)
				sb.append(", ");
			sb.append(s);
			notfirst = true;
		}
		return sb.toString();
	}

	public void select(String locator, String option) {
		locator = this.getExpandedValue(locator);
		option = this.getExpandedValue(option);
		try {
			browser.select(locator, option);
		} catch (Exception ex) {
			this.fail(locator, ex.getMessage());
		}
	}
	
	private void fail(String locator, String message){
		int cellIndex = this.findBadCell(locator, message);
		currentRow.cell(cellIndex).fail(currentTestResults);
		this.colorFail(locator, message);
	}
	
	private void colorFail(String locator, String message){
		int cellIndex = this.findBadCell(locator, message);
		this.colorFail(message, cellIndex);
	}
	
	private void colorFail(String message, int cellIndex){
		currentRow.cell(cellIndex).setText(message);
		currentRow.cell(cellIndex).parse.addToTag(" class=\"fail\"");
	}
	
	private int findBadCell(String locator, String message){
		int cellIndex = 1;
		if (currentRow.cellExists(2) && !message.equals(
				"ERROR: Element " + locator + " not found"))
			cellIndex = 2;
		return cellIndex;
	}
	
	private boolean assertEquals(String expected, String actual, int cellIndex){
		if (!expected.equals(actual)) {
			currentRow.cell(cellIndex).parse.addToTag(" class=\"fail\"");
			currentRow.cell(cellIndex).parse.addToBody(label("expected") + "<hr>"
					+ Fixture.escape(actual) + label("actual"));
			return false;
		}
		return true;
	}

	private boolean assertFalse(Boolean actual, String message, int cellIndex){
		if (actual) {
			this.colorFail(message, cellIndex);
			return false;
		}
		return true;
	}

	private boolean assertTrue(Boolean actual, String message, int cellIndex){
		if (!actual) {
			this.colorFail(message, cellIndex);
			return false;
		}
		return true;
	}
	
	private String symbolPrfix = "<<";
	public void setSymbolPrefix(String prefix){
		this.symbolPrfix = prefix;
	}
	
	private String getExpandedValue(String value){
		if (value.startsWith(symbolPrfix)){
			String symbolName = value.substring(symbolPrfix.length()).trim();
			Object symbolValue = Fixture.getSymbol(symbolName);
			if (symbolValue == null)
				throw new RuntimeException("Could not find a symbol named: '"
						+ symbolName + "'");
			
			value = symbolValue.toString();
		}
		return value;
	}
	
	private void checkComment(String comment){
		if (!comment.trim().equals("")){
			currentRow.cell(2).parse.addToTag(" class=\"ignore\"");
			currentRow.cell(2).setText("Comment: " + comment);
		}
	}

}
