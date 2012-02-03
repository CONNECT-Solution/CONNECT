package gov.hhs.fha.nhinc.webserviceproxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.IPropertyAcessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

public class WebServiceProxyHelperProperties {
	public static final String CONFIG_FILE = "gateway";
	public static final String CONFIG_KEY_TIMEOUT = "webserviceproxy.timeout";
	public static final String CONFIG_KEY_RETRYATTEMPTS = "webserviceproxy.retryattempts";
	public static final String CONFIG_KEY_RETRYDELAY = "webserviceproxy.retrydelay";
	public static final String CONFIG_KEY_EXCEPTION = "webserviceproxy.exceptionstext";
	
	private Log log = LogFactory.getLog(WebServiceProxyHelperProperties.class);

	
	private IPropertyAcessor propertyAccessor;
	
	
	private int retryDelay = 0;
	private String exceptionText = "";
	private int retryAttempts = 0;
	private int timeout = 0;
	
	private static WebServiceProxyHelperProperties INSTANCE = new WebServiceProxyHelperProperties();

	
	WebServiceProxyHelperProperties() {
		this(new PropertyAccessor(CONFIG_FILE));
	}
	

	public WebServiceProxyHelperProperties(IPropertyAcessor propertyAccessor) {
		this.propertyAccessor = propertyAccessor;
		this.retryDelay = getRetryDelayFromConfig();
		this.exceptionText = getExceptionTextFromConfig();
		this.retryAttempts = getRetryAttemptsFromConfig();
		this.timeout = getTimeoutFromConfig();
	}
	
	public WebServiceProxyHelperProperties(IPropertyAcessor propertyAccessor, int retryDelay, String exceptionText, int retryAttempts, int timeout) {
		this.propertyAccessor = propertyAccessor;
		this.retryDelay = retryDelay;
		this.exceptionText = exceptionText;
		this.retryAttempts = retryAttempts;
		this.timeout = timeout;
		
	}
	
	public static WebServiceProxyHelperProperties getInstance() {
		return INSTANCE;
	}




	/**
	 * This retrieves the text to scan for in the exception. This allows the
	 * exceptions to be considered for retry to be configured in the
	 * gateway.properties file.
	 * 
	 * @return String The string of exception text. This is a comma delimited
	 *         list of text strings to look for in the exception. If any one of
	 *         the strings are
	 */
	public String getExceptionText() {
		return exceptionText;
	}
	
	
	/**
	 * This retrieves the text to scan for in the exception. This allows the
	 * exceptions to be considered for retry to be configured in the
	 * gateway.properties file.
	 *
	 * @return String The string of exception text. This is a comma delimited
	 *         list of text strings to look for in the exception. If any one of
	 *         the strings are
	 */
	private String getExceptionTextFromConfig() {
		String configValue = "";
		try {
			configValue = propertyAccessor.getProperty(CONFIG_KEY_EXCEPTION);
			log.debug("Retrieved from config file (" + CONFIG_FILE
					+ ".properties) " + CONFIG_KEY_EXCEPTION + "='"
					+ configValue + "')");
		} catch (PropertyAccessException ex) {
			log.warn("Error occurred reading retry attempts value from config file ("
					+ CONFIG_FILE
					+ ".properties).  Exception = "
					+ ex.toString());
		}
		return configValue;
	}
	

	/**
	 * Retrieve the value for the number of retry attempts from the properties
	 * file.
	 * 
	 * @return The number of retry attemps that should be done.
	 */
	public int getRetryAttempts() {
		
		return retryAttempts;
	}

	private int getRetryAttemptsFromConfig() {
		int retryAttempts = 0;
		try {
			String sValue = propertyAccessor.getProperty(CONFIG_KEY_RETRYATTEMPTS);
			log.debug("Retrieved from config file (" + CONFIG_FILE
					+ ".properties) " + CONFIG_KEY_RETRYATTEMPTS + "='"
					+ sValue + "')");
			if (NullChecker.isNotNullish(sValue)) {
				retryAttempts = Integer.parseInt(sValue);
			}
		} catch (PropertyAccessException ex) {
			log.warn("Error occurred reading property: "
					+ CONFIG_KEY_RETRYATTEMPTS + " value from config file ("
					+ CONFIG_FILE + ".properties).  Exception: "
					+ ex.toString());
		} catch (NumberFormatException nfe) {
			log.warn("Error occurred converting property: "
					+ CONFIG_KEY_RETRYATTEMPTS
					+ " value to integer from config file (" + CONFIG_FILE
					+ ".properties).  Exception: " + nfe.toString());
		}
		return retryAttempts;

	}
	
	/**
	 * Retrieve the retry delay setting from the properties file.
	 * 
	 * @return The retry delay setting.
	 */
	public int getRetryDelay() {
		return retryDelay;
	}
	
	private int getRetryDelayFromConfig() {
		int retryDelay = 0;
		try {
			String sValue = propertyAccessor.getProperty(CONFIG_KEY_RETRYDELAY);
			log.debug("Retrieved from config file (" + CONFIG_FILE
					+ ".properties) " + CONFIG_KEY_RETRYDELAY + "='"
					+ sValue + "')");
			if (NullChecker.isNotNullish(sValue)) {
				retryDelay = Integer.parseInt(sValue);
			}
		} catch (PropertyAccessException ex) {
			log.warn("Error occurred reading property: "
					+ CONFIG_KEY_RETRYDELAY + " value from config file ("
					+ CONFIG_FILE + ".properties).  Exception: "
					+ ex.toString());
		} catch (NumberFormatException nfe) {
			log.warn("Error occurred converting property: "
					+ CONFIG_KEY_RETRYDELAY
					+ " value to integer from config file (" + CONFIG_FILE
					+ ".properties).  Exception: " + nfe.toString());
		}
		return retryDelay;

	}

	/**
	 * Retrieve the timeout value from the properties file.
	 * 
	 * @return
	 */
	public int getTimeout() {
		
		return timeout;
	}
	
	private int getTimeoutFromConfig () {
		int timeout = 0;
		try {
			String sValue = propertyAccessor.getProperty(CONFIG_KEY_TIMEOUT);
			log.debug("Retrieved from config file (" + CONFIG_FILE
					+ ".properties) " + CONFIG_KEY_TIMEOUT + "='" + sValue
					+ "')");
			if (NullChecker.isNotNullish(sValue)) {
				timeout = Integer.parseInt(sValue);
			}
		} catch (PropertyAccessException ex) {
			log.warn("Error occurred reading property: " + CONFIG_KEY_TIMEOUT
					+ " value from config file (" + CONFIG_FILE
					+ ".properties).  Exception: " + ex.toString());
		} catch (NumberFormatException nfe) {
			log.warn("Error occurred converting property: "
					+ CONFIG_KEY_TIMEOUT
					+ " value to integer from config file (" + CONFIG_FILE
					+ ".properties).  Exception: " + nfe.toString());
		}
		return timeout;
	}

}
