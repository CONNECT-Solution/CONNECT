package com.neuri.webfixture;

import fitlibrary.DoFixture;

import com.thoughtworks.selenium.*;

public class WebTest extends DoFixture {
	public void getWebfixtureDotWebTest() {
	}

	private Selenium instance;
	private boolean locatorLookup = true;

	public void setLocatorLookup(boolean lookup) {
		this.locatorLookup = lookup;
	}

	public String cleanup(String what) {
		return what.replace(" ", "");
	}

	private String getBrowserCode(String browser) {
		if ("IE".equalsIgnoreCase(browser))
			return "*iehta";
		if ("FIREFOX".equalsIgnoreCase(browser))
			return "*chrome";
		return browser;
	}

	public void startBrowserWithSeleniumConsoleOnAtPortAndScriptsAt(
			String browser, String rcServer, int rcPort, String seleniumURL) {
		instance = new DefaultSelenium(rcServer, rcPort,
				getBrowserCode(browser), seleniumURL);
		instance.start();
	}

	public void shutdownBrowser() {
		instance.stop();
	}

	public static final String[] buttonLocators = new String[] {
			"xpath=//input[@type='submit' and @id='%1$s']",
			"xpath=//input[@type='button' and @id='%1$s']",
			"xpath=//button[@id='%1$s']", "link='%1$s'",
			"xpath=//a[@id='%1$s']",
			"xpath=//input[@type='button' and @name='%1$s']",
			"xpath=//input[@type='submit' and @name='%1$s']",
			"xpath=//button[@name='%1$s']", "xpath=//a[@name='%1$s']",
			"xpath=//input[@type='submit' and @value='%1$s']",
			"xpath=//input[@type='button' and @value='%1$s']",
			"xpath=//button[@value='%1$s']", "xpath=//a[@title='%1$s']", };

	public static final String[] textFieldLocators = new String[] {
			"xpath=//input[@type='text' and @id='%1$s']",
			"xpath=//input[not(@type) and @id='%1$s']",
			"xpath=//input[@type='password' and @id='%1$s']",
			"xpath=//textarea[@id='%1$s']",
			"xpath=//input[@type='text' and @name='%1$s']",
			"xpath=//input[@type='password' and @name='%1$s']",
			"xpath=//textarea[@name='%1$s']",
			"xpath=//input[not(@type) and @name='%1$s']" };
	public static final String[] selectLocators = new String[] {
			"xpath=//select[@id='%1$s']", "xpath=//select[@name='%1$s']", };
	public static final String[] elementLocators = new String[] {
			"identifier=%1$s", "xpath=//*[@title='%1$s']",
			"xpath=//*[text()='%1$s']", "link=%1$s", "xpath=//*[@id='%1$s']" };
	public static final String[] checkboxLocators = new String[] {
			"xpath=//input[@type='checkbox' and @id='%1$s']",
			"xpath=//input[@type='radio' and @id='%1$s']",
			"xpath=//input[@type='checkbox' and @name='%1$s']",
			"xpath=//input[@type='radio' and @name='%1$s']",
			"xpath=//input[@type='checkbox' and @value='%1$s']",
			"xpath=//input[@type='radio' and @value='%1$s']", };

	public String getLocatorIfExists(String caption, String[] possibleFormats) {
		if (!locatorLookup)
			return instance.isElementPresent(caption) ? caption : null;
		for (String s : possibleFormats) {
			String locator = String.format(s, caption);
			System.out.println("Checking:" + locator);
			if (instance.isElementPresent(locator))
				return locator;
			locator = String.format(s, "_" + caption);
			if (instance.isElementPresent(locator))
				return locator;
			locator = String.format(s, cleanup(caption));
			if (instance.isElementPresent(locator))
				return locator;
			locator = String.format(s, "_" + cleanup(caption));
			if (instance.isElementPresent(locator))
				return locator;
		}
		return null;
	}

	public String getLocator(String caption, String[] possibleFormats) {
		if (!locatorLookup)
			return caption;
		String loc = getLocatorIfExists(caption, possibleFormats);
		if (loc == null)
			throw new LocatorError("Cannot find element by " + caption);
		return loc;
	}

	public void userOpensURL(String s) {
		instance.open(s);
	}

	public void userTypesIntoField(String what, String where) {
		String locator = getLocator(where, textFieldLocators);
		instance.fireEvent(locator, "focus");
		instance.type(locator, what);
		instance.fireEvent(locator, "blur");
		instance.fireEvent(locator, "change");
	}

	public void fireEventFor(String event, String locator) {
		instance.fireEvent(getLocator(locator, elementLocators), event);
	}

	public void userClicksOn(String elementCaption) {
		instance.click(getLocator(elementCaption, elementLocators));
	}

	public void userClicksOnButton(String elementCaption) {
		instance.click(getLocator(elementCaption, buttonLocators));
	}

	public void userClicksOnCheckbox(String checkboxCaption) {
		instance.click(getLocator(checkboxCaption, checkboxLocators));
	}

	public void userClicksOnRadioButton(String checkboxCaption) {
		instance.click(getLocator(checkboxCaption, checkboxLocators));
	}

	public void pageReloadsInLessThanSeconds(String sec) {
		instance.waitForPageToLoad(sec + "000");
	}

	public boolean pageContainsText(String s) {
		return instance.isTextPresent(s);
	}

	public boolean pageDoesNotContainText(String s) {
		return !pageContainsText(s);
	}

	public boolean pageContainsElement(String s) {
		return getLocatorIfExists(s, elementLocators) != null;
	}

	public boolean pageDoesNotContainElement(String s) {
		return !pageContainsElement(s);
	}

	public boolean elementIsVisible(String element) {
		return instance.isVisible(getLocator(element, elementLocators));
	}

	public boolean elementIsNotVisible(String element) {
		return !elementIsVisible(element);
	}

	public boolean pageURLIs(String s) {
		return s.equals(instance.getLocation());
	}

	public void userSelectsFrom(String what, String where) {
		instance.select(getLocator(where, selectLocators), what);
	}

	public void pauseSeconds(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException iex) {
			//nothing important, just silently ignore
		}
	}

	private boolean disabled(String what, String[] locators) {
		/* String s=instance.getAttribute(getLocator(what,locators)+"@disabled");
		 if (s==null||s.trim().length()==0 || "FALSE".equals((s.toUpperCase()))) return false;
		 return true;*/
		return !instance.isEditable(getLocator(what, locators));
	}

	public boolean buttonIsDisabled(String buttonLocator) {
		return disabled(buttonLocator, buttonLocators);
	}

	public boolean buttonIsEnabled(String buttonLocator) {
		return !disabled(buttonLocator, buttonLocators);
	}

	public boolean checkboxIsDisabled(String checkboxLocator) {
		return disabled(checkboxLocator, checkboxLocators);
	}

	public boolean checkboxIsEnabled(String checkboxLocator) {
		return !disabled(checkboxLocator, checkboxLocators);
	}

	public boolean elementIsDisabled(String elementLocator) {
		return disabled(elementLocator, elementLocators);
	}

	public boolean elementIsEnabled(String elementLocator) {
		return !disabled(elementLocator, elementLocators);
	}

	public boolean checkboxIsChecked(String checkboxLocator) {
		return instance
				.isChecked(getLocator(checkboxLocator, checkboxLocators));
	}

	public boolean checkboxIsNotChecked(String checkboxLocator) {
		return !instance
				.isChecked(getLocator(checkboxLocator, checkboxLocators));
	}

	public boolean radioButtonIsChecked(String checkboxLocator) {
		return checkboxIsChecked(checkboxLocator);
	}

	public boolean radioButtonIsNotChecked(String checkboxLocator) {
		return checkboxIsNotChecked(checkboxLocator);
	}

	public boolean checkboxIsSelected(String checkboxLocator) {
		return checkboxIsChecked(checkboxLocator);
	}

	public boolean checkboxIsNotSelected(String checkboxLocator) {
		return checkboxIsNotChecked(checkboxLocator);
	}

	public boolean radioButtonIsSelected(String checkboxLocator) {
		return radioButtonIsChecked(checkboxLocator);
	}

	public boolean radioButtonIsNotSelected(String checkboxLocator) {
		return radioButtonIsNotChecked(checkboxLocator);
	}

	public boolean fieldIsEditable(String inputLocator) {
		return !disabled(inputLocator, textFieldLocators);
	}

	public boolean fieldIsNotEditable(String inputLocator) {
		return disabled(inputLocator, textFieldLocators);
	}

	public boolean fieldIsEmpty(String inputLocator) {
		String val = instance.getValue(getLocator(inputLocator,
				textFieldLocators));
		return (val == null || val.trim().length() == 0);
	}

	public boolean fieldContainsText(String inputLocator, String text) {
		String value = instance.getValue(getLocator(inputLocator,
				textFieldLocators));
		if (value == null)
			return false;
		return value.indexOf(text) >= 0;
	}

	public boolean elementContainsText(String elementLocator, String text) {
		String value = instance.getText(getLocator(elementLocator,
				elementLocators));
		if (value == null)
			return false;
		return value.indexOf(text) >= 0;
	}

	public String valueOfIs(String elementLocator) {
		return instance.getValue(getLocator(elementLocator, elementLocators));
	}

	public String textInIs(String elementLocator) {
		return instance.getText(getLocator(elementLocator, elementLocators));
	}

	public String alert() {
		return instance.getAlert();
	}

	public String confirmation() {
		return instance.getConfirmation();
	}

	public boolean alertIs(String s) {
		return s.equals(instance.getAlert());
	}

	private class WaitForElementToAppear extends Wait {
		private String elementId;

		public WaitForElementToAppear(String elementId) {
			this.elementId = elementId;
		}

		public boolean until() {
			return getLocatorIfExists(elementId, elementLocators) != null;
		}
	}

	private class WaitForTextToAppear extends Wait {
		private String text;

		public WaitForTextToAppear(String text) {
			this.text = text;
		}

		public boolean until() {
			return pageContainsText(text);
		}
	}

	private class WaitForElementToDisappear extends Wait {
		private String elementId;

		public WaitForElementToDisappear(String elementId) {
			this.elementId = elementId;
		}

		public boolean until() {
			return getLocatorIfExists(elementId, elementLocators) == null;
		}
	}

	private class WaitForTextToDisappear extends Wait {
		private String text;

		public WaitForTextToDisappear(String text) {
			this.text = text;
		}

		public boolean until() {
			return !pageContainsText(text);
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
			String value = instance.getValue(elementLocator);
			if (value == null)
				return false;
			return value.indexOf(text) >= 0;
		}
	}

	private class WaitForElementContent extends Wait {
		private String text;
		private String effectiveElementLocator;
		private String element;

		public WaitForElementContent(String element, String value) {
			this.text = value;
			this.element = element;
		}

		public boolean until() {
			if (effectiveElementLocator == null)
				effectiveElementLocator = getLocatorIfExists(element,
						elementLocators);
			if (effectiveElementLocator == null)
				return false;
			String value = instance.getText(effectiveElementLocator);
			if (value == null)
				return false;
			return value.indexOf(text) >= 0;
		}
	}

	public boolean waitSecondsForElementToAppear(int timeout, String elementId) {
		Wait x = new WaitForElementToAppear(elementId);
		x.wait("Cannot find element " + elementId + " after " + timeout
				+ " seconds", timeout * 1000);
		return true;
	}

	public boolean waitSecondsForTextToAppear(int timeout, String text) {
		Wait x = new WaitForTextToAppear(text);
		x.wait("Cannot find text " + text + " after " + timeout + " seconds",
				timeout * 1000);
		return true;
	}

	public boolean waitSecondsForElementToDisappear(int timeout,
			String elementId) {
		Wait x = new WaitForElementToDisappear(elementId);
		x.wait("Element " + elementId + " still appearing after " + timeout
				+ " seconds", timeout * 1000);
		return true;
	}

	public boolean waitSecondsForTextToDisappear(int timeout, String text) {
		Wait x = new WaitForTextToDisappear(text);
		x.wait(
				"Text " + text + " still apearing after " + timeout
						+ " seconds", timeout * 1000);
		return true;
	}

	public boolean waitSecondsForFieldToContainText(int timeout, String field,
			String text) {
		String locator = getLocator(field, textFieldLocators);
		Wait x = new WaitForFieldValue(locator, text);
		x.wait("Cannot find text " + text + " in " + field + " after "
				+ timeout + " seconds", timeout * 1000);
		return true;
	}

	public boolean waitSecondsForElementToContainText(int timeout,
			String element, String text) {
		Wait x = new WaitForElementContent(element, text);
		x.wait("Cannot find text " + text + " in " + element + " after "
				+ timeout + " seconds", timeout * 1000);
		return true;
	}

	public static class SelectValue {
		public String value;

		public SelectValue(String s) {
			this.value = s;
		}

		public static SelectValue[] buildArray(String[] arr) {
			SelectValue[] sv = new SelectValue[arr.length];
			for (int i = 0; i < arr.length; i++)
				sv[i] = new SelectValue(arr[i]);
			return sv;
		}
	}

	public String selectedOptionInIs(String elementLocator) {
		return instance.getSelectedLabel(getLocator(elementLocator,
				selectLocators));
	}

	public SelectValue[] listContainsOptions(String s) {
		return SelectValue.buildArray(instance.getSelectOptions(getLocator(s,
				selectLocators)));
	}

	public SelectValue[] dropDownContainsOptions(String s) {
		return SelectValue.buildArray(instance.getSelectOptions(getLocator(s,
				selectLocators)));
	}
}
