package gov.hhs.fha.nhinc.webserviceproxy;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rayj
 */
public class WebServiceProxyHelper {

    public static final String CONFIG_FILE = "gateway";
    public static final String CONFIG_KEY_TIMEOUT = "webserviceproxy.timeout";
    public static final String CONFIG_KEY_RETRYATTEMPTS = "webserviceproxy.retryattempts";
    public static final String CONFIG_KEY_RETRYDELAY = "webserviceproxy.retrydelay";
    public static final String CONFIG_KEY_EXCEPTION = "webserviceproxy.exceptionstext";
    public static final String KEY_CONNECT_TIMEOUT = "com.sun.xml.ws.connect.timeout";
    public static final String KEY_REQUEST_TIMEOUT = "com.sun.xml.ws.request.timeout";
    public static final String KEY_URL = javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
    private static Log log = LogFactory.getLog(WebServiceProxyHelper.class);

    static public WebServiceProxyHelper getInstance() {
        //note: I am forcing to use factory method so that I can allow for multiple methods here depending on platform
        return new WebServiceProxyHelper();
    }

    /**
     * 
     */
    private WebServiceProxyHelper() {
    }

    /**
     *
     * @return String
     */
    public String getExceptionText() {
        //replace with config read
        String configValue = "";
        try {
            configValue = PropertyAccessor.getProperty(CONFIG_FILE, CONFIG_KEY_EXCEPTION);
            log.debug("retrieved retry attempts from config file (" + CONFIG_FILE + "." + CONFIG_KEY_EXCEPTION + "=" + configValue + ")");
        } catch (PropertyAccessException ex) {
            log.warn("error occurred reading retry attempts value from config [" + ex.toString() + "]");
        }
        return configValue;
    }

    /**
     *
     * @return int
     */
    public int getRetryAttempts() {
        //replace with config read
        String configValue = "0";
        try {
            configValue = PropertyAccessor.getProperty(CONFIG_FILE, CONFIG_KEY_RETRYATTEMPTS);
            log.debug("retrieved retry attempts from config file (" + CONFIG_FILE + "." + CONFIG_KEY_RETRYATTEMPTS + "=" + configValue + ")");
        } catch (PropertyAccessException ex) {
            log.warn("error occurred reading retry attempts value from config [" + ex.toString() + "]");
        }

        int retryAttempts = 0;
        if (NullChecker.isNotNullish(configValue)) {
            retryAttempts = Integer.parseInt(configValue);
        }
        return retryAttempts;
    }

    /**
     *
     * @return int
     */
    public int getRetryDelay() {
        //replace with config read
        String configValue = "0";
        try {
            configValue = PropertyAccessor.getProperty(CONFIG_FILE, CONFIG_KEY_RETRYDELAY);
            log.debug("retrieved retry delay from config file (" + CONFIG_FILE + "." + CONFIG_KEY_RETRYDELAY + "=" + configValue + ")");
        } catch (PropertyAccessException ex) {
            log.warn("error occurred reading retry delay value from config [" + ex.toString() + "]");
        }

        int retryDelay = 0;
        if (NullChecker.isNotNullish(configValue)) {
            retryDelay = Integer.parseInt(configValue);
        }
        return retryDelay;
    }

    /**
     *
     * @return int
     */
    private int getTimeout() {
        //replace with config read
        String configValue = "0";
        try {
            configValue = PropertyAccessor.getProperty(CONFIG_FILE, CONFIG_KEY_TIMEOUT);
            log.debug("retrieved timeout from config file (" + CONFIG_FILE + "." + CONFIG_KEY_TIMEOUT + "=" + configValue + ")");
        } catch (PropertyAccessException ex) {
            log.warn("error occurred reading timeout value from config [" + ex.toString() + "]");
        }

        int timeout = 0;
        if (NullChecker.isNotNullish(configValue)) {
            timeout = Integer.parseInt(configValue);
        }
        return timeout;
    }

    /**
     * 
     * @param port
     * @param url
     */
    public void initializePort(BindingProvider port, String url) {
        log.info("begin initializePort");
        if (port == null) {
            throw new RuntimeException("Unable to initialize port (port null)");
        }
        if (NullChecker.isNullish(url)) {
            throw new RuntimeException("Unable to initialize port (url null)");
        }

        int timeout = getTimeout();
        log.info("initializing port [url=" + url + "][timeout=" + timeout + "][port=" + port.toString() + "]");

        log.debug("setting url " + url);
        if (port.getRequestContext().containsKey(KEY_URL) && NullChecker.isNotNullish(port.getRequestContext().get(KEY_URL).toString())) {
            log.info("fyi: url was already set [" + port.getRequestContext().get(KEY_URL) + "], however, will re-set it");
        }

        port.getRequestContext().put(KEY_URL, url);
        if (timeout > 0) {
            log.debug("setting timeout " + timeout);
            ((BindingProvider) port).getRequestContext().put(KEY_CONNECT_TIMEOUT, timeout);
            ((BindingProvider) port).getRequestContext().put(KEY_REQUEST_TIMEOUT, timeout);
        } else {
            log.warn("port timeout not set.  This may lead to undesirable behavior under heavy load");
        }
        log.info("end initializePort");
    }
}
