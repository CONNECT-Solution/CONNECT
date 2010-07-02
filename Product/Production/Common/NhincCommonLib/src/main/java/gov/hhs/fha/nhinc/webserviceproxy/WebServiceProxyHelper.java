/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public static final String KEY_CONNECT_TIMEOUT = "com.sun.xml.ws.connect.timeout";
    public static final String KEY_REQUEST_TIMEOUT = "com.sun.xml.ws.request.timeout";
    public static final String KEY_URL = javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
    private static Log log = LogFactory.getLog(WebServiceProxyHelper.class);

    static public WebServiceProxyHelper getInstance() {
        //note: I am forcing to use factory method so that I can allow for multiple methods here depending on platform
        return new WebServiceProxyHelper();
    }

    private WebServiceProxyHelper() {
    }

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
