/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.webserviceproxy;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.IPropertyAcessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServiceProxyHelperProperties {

    public static final String CONFIG_FILE = "gateway";
    public static final String CONFIG_KEY_TIMEOUT = "webserviceproxy.timeout";
    public static final String CONFIG_KEY_RETRYATTEMPTS = "webserviceproxy.retryattempts";
    public static final String CONFIG_KEY_RETRYDELAY = "webserviceproxy.retrydelay";
    public static final String CONFIG_KEY_EXCEPTION = "webserviceproxy.exceptionstext";
    public static final String CONFIG_KEY_REQUESTTIMEOUT = "webserviceproxy.request.timeout";

    private static final Logger LOG = LoggerFactory.getLogger(WebServiceProxyHelperProperties.class);

    private IPropertyAcessor propertyAccessor;

    private int retryDelay = 0;
    private String exceptionText = "";
    private int retryAttempts = 0;
    private int timeout = 0;

    private static WebServiceProxyHelperProperties INSTANCE = new WebServiceProxyHelperProperties();

    WebServiceProxyHelperProperties() {
        this(PropertyAccessor.getInstance());
    }

    public WebServiceProxyHelperProperties(IPropertyAcessor propertyAccessor) {
        this.propertyAccessor = propertyAccessor;
        retryDelay = getRetryDelayFromConfig();
        exceptionText = getExceptionTextFromConfig();
        retryAttempts = getRetryAttemptsFromConfig();
        timeout = getTimeoutFromConfig();
    }

    public WebServiceProxyHelperProperties(IPropertyAcessor propertyAccessor, int retryDelay, String exceptionText,
            int retryAttempts, int timeout) {
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
     * This retrieves the text to scan for in the exception. This allows the exceptions to be considered for retry to be
     * configured in the gateway.properties file.
     *
     * @return String The string of exception text. This is a comma delimited list of text strings to look for in the
     *         exception. If any one of the strings are
     */
    public String getExceptionText() {
        return exceptionText;
    }

    /**
     * This retrieves the text to scan for in the exception. This allows the exceptions to be considered for retry to be
     * configured in the gateway.properties file.
     *
     * @return String The string of exception text. This is a comma delimited list of text strings to look for in the
     *         exception. If any one of the strings are
     */
    private String getExceptionTextFromConfig() {
        String configValue = "";
        try {
            configValue = propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, CONFIG_KEY_EXCEPTION);
            LOG.debug("Retrieved from config file (" + CONFIG_FILE + ".properties) " + CONFIG_KEY_EXCEPTION + "='"
                    + configValue + "')");
        } catch (PropertyAccessException ex) {
            LOG.warn("Error occurred reading retry attempts value from config file ({}.properties): {}", CONFIG_FILE,
                    ex.getLocalizedMessage());
            LOG.trace("Error occurred reading retry attempts value from config file ({}.properties): {}", CONFIG_FILE,
                    ex.getLocalizedMessage(), ex);
        }
        return configValue;
    }

    /**
     * Retrieve the value for the number of retry attempts from the properties file.
     *
     * @return The number of retry attempts that should be done.
     */
    public int getRetryAttempts() {

        return retryAttempts;
    }

    private int getRetryAttemptsFromConfig() {
        int retryAttemptsProp = 0;
        try {
            String sValue = propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    CONFIG_KEY_RETRYATTEMPTS);
            LOG.debug("Retrieved from config file (" + CONFIG_FILE + ".properties) " + CONFIG_KEY_RETRYATTEMPTS + "='"
                    + sValue + "')");
            if (NullChecker.isNotNullish(sValue)) {
                retryAttemptsProp = Integer.parseInt(sValue);
            }
        } catch (PropertyAccessException ex) {
            LOG.warn("Error occurred reading property {} value from config file ({}.properties): {}",
                    CONFIG_KEY_RETRYATTEMPTS, CONFIG_FILE, ex.getLocalizedMessage());
            LOG.trace("Error occurred reading property {} value from config file ({}.properties): {}",
                    CONFIG_KEY_RETRYATTEMPTS, CONFIG_FILE, ex.getLocalizedMessage(), ex);
        } catch (NumberFormatException nfe) {
            LOG.warn("Error occurred converting property {} value to integer from config file ({}.properties): {}",
                    CONFIG_KEY_RETRYATTEMPTS, CONFIG_FILE, nfe.getLocalizedMessage());
            LOG.trace("Error occurred converting property {} value to integer from config file ({}.properties): {}",
                    CONFIG_KEY_RETRYATTEMPTS, CONFIG_FILE, nfe.getLocalizedMessage(), nfe);
        }
        return retryAttemptsProp;
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
        int retryDelayProp = 0;
        try {
            String sValue = propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, CONFIG_KEY_RETRYDELAY);
            LOG.debug("Retrieved from config file (" + CONFIG_FILE + ".properties) " + CONFIG_KEY_RETRYDELAY + "='"
                    + sValue + "')");
            if (NullChecker.isNotNullish(sValue)) {
                retryDelayProp = Integer.parseInt(sValue);
            }
        } catch (PropertyAccessException ex) {
            LOG.warn("Error occurred reading property {} value from config file ({}.properties): {}",
                    CONFIG_KEY_RETRYDELAY, CONFIG_FILE, ex.getLocalizedMessage());
            LOG.trace("Error occurred reading property {} value from config file ({}.properties): {}",
                    CONFIG_KEY_RETRYDELAY, CONFIG_FILE, ex.getLocalizedMessage(), ex);
        } catch (NumberFormatException nfe) {
            LOG.warn("Error occurred converting property {} value to integer from config file ({}.properties): {}",
                    CONFIG_KEY_RETRYDELAY, CONFIG_FILE, nfe.getLocalizedMessage());
            LOG.trace("Error occurred converting property {} value to integer from config file ({}.properties): {}",
                    CONFIG_KEY_RETRYDELAY, CONFIG_FILE, nfe.getLocalizedMessage(), nfe);
        }
        return retryDelayProp;
    }

    /**
     * Retrieve the timeout value from the properties file.
     *
     * @return
     */
    public int getTimeout() {

        return timeout;
    }

    private int getTimeoutFromConfig() {
        int timeoutProp = 0;
        try {
            String sValue = propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, CONFIG_KEY_TIMEOUT);
            LOG.debug("Retrieved from config file (" + CONFIG_FILE + ".properties) " + CONFIG_KEY_TIMEOUT + "='"
                    + sValue + "')");
            if (NullChecker.isNotNullish(sValue)) {
                timeoutProp = Integer.parseInt(sValue);
            }
        } catch (PropertyAccessException ex) {
            LOG.warn("Error occurred reading property {} value from config file ({}.properties): {}",
                    CONFIG_KEY_TIMEOUT, CONFIG_FILE, ex.getLocalizedMessage());
            LOG.trace("Error occurred reading property {} value from config file ({}.properties): {}",
                    CONFIG_KEY_TIMEOUT, CONFIG_FILE, ex.getLocalizedMessage(), ex);
        } catch (NumberFormatException nfe) {
            LOG.warn("Error occurred converting property {} value to integer from config file ({}.properties): {}",
                    CONFIG_KEY_TIMEOUT, CONFIG_FILE, nfe.getLocalizedMessage());
            LOG.trace("Error occurred converting property {} value to integer from config file ({}.properties): {}",
                    CONFIG_KEY_TIMEOUT, CONFIG_FILE, nfe.getLocalizedMessage(), nfe);
        }
        return timeoutProp;
    }

    /**
     * Gets the webservice request timeout property for serviceName (ServiceName.webserviceproxy.request.timeout=xxxxx)
     * from gateway.properties Will return 0 if there is no request timeout for serviceName in gateway.properties
     *
     * @param serviceName
     * @return timeout in milliseconds
     */
    public int getTimeoutFromConfig(String serviceName) {
        int timeoutProp = timeout;
        String propertyName = "";
        try {
            propertyName = serviceName + "." + CONFIG_KEY_REQUESTTIMEOUT;
            String sValue = propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, propertyName);
            LOG.debug("Retrieved from config file (" + CONFIG_FILE + ".properties) " + propertyName + "='" + sValue
                    + "')");
            if (NullChecker.isNotNullish(sValue)) {
                timeoutProp = Integer.parseInt(sValue);
            }
        } catch (PropertyAccessException ex) {
            LOG.warn("Error occurred reading property {} value from config file ({}.properties): {}", propertyName,
                    CONFIG_FILE, ex.getLocalizedMessage());
            LOG.trace("Error occurred reading property {} value from config file ({}.properties): {}", propertyName,
                    CONFIG_FILE, ex.getLocalizedMessage(), ex);
        } catch (NumberFormatException nfe) {
            LOG.warn("Error occurred converting property {} value to integer from config file ({}.properties): {}",
                    propertyName, CONFIG_FILE, nfe.getLocalizedMessage());
            LOG.trace("Error occurred converting property {} value to integer from config file ({}.properties): {}",
                    propertyName, CONFIG_FILE, nfe.getLocalizedMessage(), nfe);
        }
        return timeoutProp;
    }

}
