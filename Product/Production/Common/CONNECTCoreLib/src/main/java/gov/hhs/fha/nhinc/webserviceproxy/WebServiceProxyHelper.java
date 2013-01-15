/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.developer.WSBindingProvider;

import gov.hhs.fha.nhinc.async.AddressingHeaderCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.AdapterEndpointManager;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.IPropertyAcessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.tools.ws.processor.generator.ServicePropertyLoader;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;

/**
 * This class is used as a helper in each of the Web Service Proxies. Since the bulk of the work being done in each web
 * service proxy is the same, it is encapsulated in this helper class.
 *
 * @author Jason Ray, Sai Valluripalli, Les Westberg
 */
public class WebServiceProxyHelper {

    public static final String KEY_CONNECT_TIMEOUT = "com.sun.xml.ws.connect.timeout";
    public static final String KEY_REQUEST_TIMEOUT = "com.sun.xml.ws.request.timeout";
    public static final String KEY_URL = javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
    private static final String UUID_TAG = "urn:uuid:";
    private Log log = null;
    private final WebServiceProxyHelperProperties properties;
    private final SamlTokenCreator samlTokenCreator = new SamlTokenCreator();

    public WebServiceProxyHelper() {
        log = createLogger();
        properties = WebServiceProxyHelperProperties.getInstance();

    }

    /**
     * DI constructor.
     *
     * @param log
     * @param propertyAccessor
     */
    public WebServiceProxyHelper(Log log, IPropertyAcessor propertyAccessor) {
        this.log = log;
        properties = new WebServiceProxyHelperProperties(propertyAccessor);
    }

    /**
     * Create a logger object.
     *
     * @return The logger object.
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * This method returns the URL endpoint of the passed in service name from the given target
     *
     * @param oTargetSystem The target system for the call.
     * @param sServiceName The name of the service to locate.
     * @return The endpoint URL.
     * @throws Exception An exception if one occurs.
     */
    protected String getEndPointFromConnectionManagerByGatewayAPILevel(NhinTargetSystemType oTargetSystem,
            String sServiceName, GATEWAY_API_LEVEL level) throws ConnectionManagerException {

        String url = ConnectionManagerCache.getInstance().getEndpointURLFromNhinTarget(oTargetSystem, sServiceName);
        return url;
    }

    /**
     * This method returns the URL endpoint of the passed in adapter service name with the given api level
     *
     * @param sServiceName The name of the service to locate.
     * @param level The adapter api level.
     * @return The endpoint URL.
     * @throws Exception An exception if one occurs.
     */
    public String getEndPointFromConnectionManagerByAdapterAPILevel(String sServiceName, ADAPTER_API_LEVEL level)
            throws ConnectionManagerException {
        String url = ConnectionManagerCache.getInstance().getAdapterEndpointURL(sServiceName, level);
        return url;
    }

    /**
     * This method returns the URL endpoint of the passed in adapter service name
     *
     * @param sServiceName The name of the service to locate.
     * @return The endpoint URL.
     * @throws Exception An exception if one occurs.
     */
    public String getAdapterEndPointFromConnectionManager(String sServiceName) throws ConnectionManagerException {
        AdapterEndpointManager adapterEndpointManager = new AdapterEndpointManager();
        ADAPTER_API_LEVEL level = adapterEndpointManager.getApiVersion(sServiceName);

        return getEndPointFromConnectionManagerByAdapterAPILevel(sServiceName, level);
    }

    /**
     * This method returns the URL endpoint of the passed in adapter service name
     *
     * @param sServiceName The name of the service to locate.
     * @return The endpoint URL.
     * @throws Exception An exception if one occurs.
     */
    public String getAdapterEndPointFromConnectionManager(String sHomeCommunityId, String sServiceName)
            throws ConnectionManagerException {
        AdapterEndpointManager adapterEndpointManager = new AdapterEndpointManager();
        ADAPTER_API_LEVEL level = adapterEndpointManager.getApiVersion(sServiceName);

        String url = ConnectionManagerCache.getInstance().getAdapterEndpointURL(sHomeCommunityId, sServiceName, level);
        return url;
    }

    /**
     * This method returns the endpoint url of the passed in service name with the given hcid
     *
     * @param sHomeCommunityId The home community Id for the target system.
     * @param sServiceName The name of the service to locate.
     * @return The endpoint URL.
     * @throws Exception An exception if one occurs.
     */
    protected String getEndPointFromConnectionManager(String sHomeCommunityId, String sServiceName)
            throws ConnectionManagerException {
        String url = ConnectionManagerCache.getInstance().getDefaultEndpointURLByServiceName(sHomeCommunityId,
                sServiceName);
        return url;
    }

    /**
     * This method returns the endpoint url of the passed in service name in the local gateway (hcid)
     *
     * @param sServiceName The name of the service to locate.
     * @return The endpoint URL.
     * @throws Exception An exception if one occurs.
     */
    protected String getLocalEndPointFromConnectionManager(String sServiceName) throws ConnectionManagerException {
        String url = ConnectionManagerCache.getInstance().getInternalEndpointURLByServiceName(sServiceName);
        return url;
    }

    /**
     * This method retrieves the URl from the ConnectionMananager for the given TargetSystem.
     *
     * @param oTargetSystem The target system containing the information needed to retrieve the endpoint URL.
     * @param sServiceName The name of the service for which the endpoint URL is desired.
     * @return The URL retrieved from the connection manager.
     */
    public String getUrlFromTargetSystemByGatewayAPILevel(NhinTargetSystemType oTargetSystem, String sServiceName,
            GATEWAY_API_LEVEL level) throws IllegalArgumentException, ConnectionManagerException, Exception {
        String sURL = "";

        if (oTargetSystem != null) {
            try {
                if (oTargetSystem.getHomeCommunity() != null) {
                    HomeCommunityType oHomeCommunity = oTargetSystem.getHomeCommunity();
                    log.info("Target Sys properties Home Comm ID: " + oHomeCommunity.getHomeCommunityId());
                    log.info("Target Sys properties Home Comm Description: " + oHomeCommunity.getDescription());
                    log.info("Target Sys properties Home Comm Name: " + oHomeCommunity.getName());
                }
                sURL = getEndPointFromConnectionManagerByGatewayAPILevel(oTargetSystem, sServiceName, level);
            } catch (ConnectionManagerException e) {
                log.error(
                        "Error: Failed to retrieve url for service: " + sServiceName + ".  Exception: "
                                + e.getMessage(), e);
                throw (e);
            }
        } else {
            String sErrorMessage = "Target system passed into the proxy is null";
            log.error(sErrorMessage);
            throw new IllegalArgumentException(sErrorMessage);
        }

        return sURL;
    }

    /**
     * This method retrieves the URl from the ConnectionMananager for the given TargetSystem.
     *
     * @param oTargetSystem The target system containing the information needed to retrieve the endpoint URL.
     * @param sServiceName The name of the service for which the endpoint URL is desired.
     * @return The URL retrieved from the connection manager.
     */
    public String getUrlFromTargetSystemByAdapterAPILevel(NhinTargetSystemType oTargetSystem, String sServiceName,
            ADAPTER_API_LEVEL level) throws IllegalArgumentException, ConnectionManagerException, Exception {
        String sURL = "";

        if (oTargetSystem != null) {
            try {
                if (oTargetSystem.getHomeCommunity() != null) {
                    HomeCommunityType oHomeCommunity = oTargetSystem.getHomeCommunity();
                    log.info("Target Sys properties Home Comm ID:" + oHomeCommunity.getHomeCommunityId());
                    log.info("Target Sys properties Home Comm Description" + oHomeCommunity.getDescription());
                    log.info("Target Sys properties Home Comm Name" + oHomeCommunity.getName());
                }
                sURL = getEndPointFromConnectionManagerByAdapterAPILevel(sServiceName, level);
            } catch (ConnectionManagerException e) {
                log.error(
                        "Error: Failed to retrieve url for service: " + sServiceName + ".  Exception: "
                                + e.getMessage(), e);
                throw (e);
            }
        } else {
            String sErrorMessage = "Target system passed into the proxy is null";
            log.error(sErrorMessage);
            throw new IllegalArgumentException(sErrorMessage);
        }

        return sURL;
    }

    /**
     * This method retrieves the URl from the ConnectionMananager for the given home community ID.
     *
     * @param sHomeCommunityId The home community id needed to retrieve the endpoint URL.
     * @param sServiceName The name of the service for which the endpoint URL is desired.
     * @return The URL retrieved from the connection manager.
     */
    public String getUrlFromHomeCommunity(String sHomeCommunityId, String sServiceName)
            throws IllegalArgumentException, ConnectionManagerException, Exception {
        String sURL = "";

        if (NullChecker.isNotNullish(sHomeCommunityId)) {
            try {
                log.info("Home Comm ID:" + sHomeCommunityId);
                sURL = getEndPointFromConnectionManager(sHomeCommunityId, sServiceName);
            } catch (ConnectionManagerException e) {
                log.error(
                        "Error: Failed to retrieve url for service: " + sServiceName + ".  Exception: "
                                + e.getMessage(), e);
                throw (e);
            }
        } else {
            String sErrorMessage = "Home community passed into the WebServiceProxyHelper is null or empty";
            log.error(sErrorMessage);
            throw new IllegalArgumentException(sErrorMessage);
        }

        return sURL;
    }

    /**
     * This method retrieves the URl from the ConnectionMananager for the given home community ID.
     *
     * @param sHomeCommunity The home community id needed to retrieve the endpoint URL.
     * @param sServiceName The name of the service for which the endpoint URL is desired.
     * @return The URL retrieved from the connection manager.
     */
    public String getUrlLocalHomeCommunity(String sServiceName) throws IllegalArgumentException,
            ConnectionManagerException, Exception {
        String sURL = "";

        try {
            sURL = getLocalEndPointFromConnectionManager(sServiceName);
        } catch (ConnectionManagerException e) {
            log.error("Error: Failed to retrieve url for service: " + sServiceName + ".  Exception: " + e.getMessage(),
                    e);
            throw (e);
        }

        return sURL;
    }

    /**
     * This retrieves the text to scan for in the exception. This allows the exceptions to be considered for retry to be
     * configured in the gateway.properties file.
     *
     * @return String The string of exception text. This is a comma delimited list of text strings to look for in the
     *         exception. If any one of the strings are
     */
    public String getExceptionText() {
        return properties.getExceptionText();
    }

    /**
     * Retrieve the value for the number of retry attempts from the properties file.
     *
     * @return The number of retry attempts that should be done.
     */
    public int getRetryAttempts() {
        return properties.getRetryAttempts();
    }

    /**
     * Retrieve the retry delay setting from the properties file.
     *
     * @return The retry delay setting.
     */
    public int getRetryDelay() {
        return properties.getRetryDelay();
    }

    /**
     * Retrieve the timeout value from the properties file.
     *
     * @return
     */
    public int getTimeout() {

        return properties.getTimeout();
    }

    /**
     * This method returns the request context from the port. It is here mainly to facilitate mock unit testing.
     *
     * @param port The port containing the request context.
     * @return The request context.
     */
    public Map<String, Object> getRequestContextFromPort(BindingProvider port) {
        return port.getRequestContext();
    }

    /**
     * This method returns an instance of the SamlTokenCreator class. This method is here to facilitate mock unit
     * testing.
     *
     * @return instance of the SamlTokenCreator
     */
    protected SamlTokenCreator getSamlTokenCreator() {
        return samlTokenCreator;
    }

    /**
     * This method returns the the request context with the information extracted from the assertion class and the URL
     * and service action.
     *
     * @param oTokenCreator The SamlTokenCreator object that will create the request context.
     * @param oAssertion The assertion information to be used in the context.
     * @param sUrl The URL of the web service.
     * @param sServiceAction The action for the web service.
     * @return The request context with the SAML information.
     */
    protected Map createSamlRequestContext(SamlTokenCreator oTokenCreator, AssertionType oAssertion, String sUrl,
            String sServiceAction) {
        return oTokenCreator.CreateRequestContext(oAssertion, sUrl, sServiceAction);
    }

    /**
     * This method retrieves the message identifier stored in the assertion If the message ID is null or empty, this
     * method will generate a new UUID to use for the message ID.
     *
     * @param assertion The assertion information containing the SAML assertion to be assigned to the message.
     * @return The message identifier
     */
    protected String getMessageId(AssertionType assertion) {
        if ((assertion != null) && (NullChecker.isNotNullish(assertion.getMessageId()))) {
            if (hasProperMessageIDPrefix(assertion.getMessageId()) == false) {
                fixMessageIDPrefix(assertion);
            }
            return assertion.getMessageId();
        } else {
            UUID oUuid = UUID.randomUUID();
            String sUuid = UUID_TAG + oUuid.toString();
            log.warn("Assertion did not contain a message ID.  Generating one now...  Message ID = " + sUuid);
            if (assertion != null) {
                assertion.setMessageId(sUuid);
            }
            return sUuid;
        }
    }

    /**
     * @param messageId
     * @return
     */
    private boolean hasProperMessageIDPrefix(String messageId) {
        return messageId.trim().startsWith("urn:uuid:");
    }

    /**
     * @param assertion
     */
    private void fixMessageIDPrefix(AssertionType assertion) {
        String messageId = assertion.getMessageId();
        if (illegalUUID(messageId, "uuid:")) {
            assertion.setMessageId("urn:" + messageId);
        } else {
            assertion.setMessageId("urn:uuid:" + messageId);
        }
    }

    /**
     * @param messageId
     * @param string
     * @return
     */
    private boolean illegalUUID(String messageId, String illegalPrefix) {
        return messageId.trim().startsWith(illegalPrefix);
    }

    /**
     * This method retrieves the list of relatesTo identifiers stored in the assertion.
     *
     * @param assertion The assertion information containing the SAML assertion to be assigned to the message.
     * @return The list of relatesTo identifiers
     */
    protected List<String> getRelatesTo(AssertionType assertion) {
        List<String> allRelatesTo = new ArrayList<String>();
        if (assertion != null && NullChecker.isNotNullish(assertion.getRelatesToList())) {
            allRelatesTo.addAll(assertion.getRelatesToList());
        }
        return allRelatesTo;
    }

    /**
     * This method gset the WS-Addressing headers to be initialized on the port
     *
     * @param url The endpoint url defining <To>
     * @param wsAddressingAction The action defining <Action>
     * @param assertion The assertion whic contains the messageId and the relatesTo identifiers
     * @return The list of WS-Addressing headers
     */
    protected List<Header> getWSAddressingHeaders(String url, String wsAddressingAction, AssertionType assertion) {

        String messageId = getMessageId(assertion);
        List<String> allRelatesTo = getRelatesTo(assertion);

        AddressingHeaderCreator hdrCreator = new AddressingHeaderCreator(url, wsAddressingAction, messageId,
                allRelatesTo);

        List<Header> createdHeaders = hdrCreator.build();

        return createdHeaders;
    }

    /**
     * This method sets the provided WS-Addressing headers to the outbound headers on the port
     *
     * @param port The port to be initialized
     * @param createdHeaders The listing of WS-Addressing headers.
     */
    protected void setOutboundHeaders(BindingProvider port, List<Header> createdHeaders) {
        ((WSBindingProvider) port).setOutboundHeaders(createdHeaders);
    }

    /**
     * This method initializes the port and sets various values that are required for processing - like timeout, URL,
     * etc. This should not be used in any new code it was only placed here as a stop gap during refactor for old code.
     * After the refactor this should not be used at all.
     *
     * @param port The port to be initialized.
     * @param url The URL to be assigned to the port.
     * @deprecated
     */
    @Deprecated
    public void initializePort(BindingProvider port, String url) {
        initializePort(false, port, url, null, null, null);
    }

    /**
     * This method initializes the port for an unsecured interface call and sets various values that are required for
     * processing - like timeout, URL, etc.
     *
     * @param port The port to be initialized.
     * @param url The URL of the web service to be assigned to the port.
     * @param wsAddressingAction The WS-Addressing action associated with this web service call. If this is null, the
     *            construction of the WS-Address header will depend on wsdl policy statements.
     * @param assertion The assertion information.
     */
    public void initializeUnsecurePort(BindingProvider port, String url, String wsAddressingAction,
            AssertionType assertion) {
        initializePort(false, port, url, null, wsAddressingAction, assertion);
    }

    /**
     * This method initializes the port for an secure interface call and sets various values that are required for
     * processing - like timeout, URL, etc.
     *
     * @param port The port to be initialized.
     * @param url The URL of the web service to be assigned to the port.
     * @param serviceAction The action for the web service.
     * @param wsAddressingAction The WS-Addressing action associated with this web service call. If this is null, the
     *            construction of the WS-Address header will depend on wsdl policy statements.
     * @param assertion The assertion information.
     */
    public void initializeSecurePort(BindingProvider port, String url, String serviceName, String wsAddressingAction,
            AssertionType assertion) {
        initializePort(true, port, url, serviceName, wsAddressingAction, assertion);
    }

    /**
     * This method initializes the port and sets various values that are required for processing - like timeout, URL,
     * etc.
     *
     * @param port The port to be initialized.
     * @param url The URL of the web service to be assigned to the port.
     * @param serviceName The action for the web service.
     * @param wsAddressingAction The WS-Addressing action associated with this web service call. If this is null, the
     *            construction of the WS-Address header will depend on wsdl policy statements.
     * @param assertion The assertion information containing the SAML assertion to be assigned to the message.
     * @deprecated
     */
    @Deprecated
    public void initializePort(BindingProvider port, String url, String serviceName, String wsAddressingAction,
            AssertionType assertion) {
        boolean bIsSecure = true;
        if (assertion == null) {
            bIsSecure = false;
        }

        initializePort(bIsSecure, port, url, serviceName, wsAddressingAction, assertion);
    }

    /**
     * This method initializes the port and sets various values that are required for processing - like timeout, URL,
     * etc.
     *
     * @param isSecure If TRUE set this up as a secure call.
     * @param port The port to be initialized.
     * @param url The URL of the web service to be assigned to the port.
     * @param serviceName The action for the web service.
     * @param wsAddressingAction The WS-Addressing action associated with this web service call. If this is null, the
     *            construction of the WS-Address header will depend on wsdl policy statements.
     * @param assertion The assertion information containing the SAML assertion to be assigned to the message.
     */
    private void initializePort(boolean isSecure, BindingProvider port, String url, String serviceName,
            String wsAddressingAction, AssertionType assertion) {
        log.info("begin initializePort");

        validateInitializePort(isSecure, port, url, serviceName, assertion);

        setPortUrl(port, url);

        setPortTimeout(port);

        if (isSecure) {
            setSAMLForPort(port, serviceName, assertion);
        }

        // Create the WS-Addressing Headers - action must be supplied
        // -----------------------------------------------------------
        if (wsAddressingAction != null) {
            setOutboundHeaders(port, wsAddressingAction, assertion);
        } else {
            if (assertion != null && (NullChecker.isNotNullish(assertion.getMessageId()))
                    && !hasProperMessageIDPrefix(assertion.getMessageId())) {
                fixMessageIDPrefix(assertion);
            }
            log.warn("WS-Addressing information is unavailable, relying on wsdl policy");
        }

        log.info("end initializePort");
    }

    /**
     * @param port
     * @param url
     * @param wsAddressingAction
     * @param assertion
     */
    private void setOutboundHeaders(BindingProvider port, String wsAddressingAction, AssertionType assertion) {
        List<Header> createdHeaders = createWSAddressingHeaders(port, wsAddressingAction, assertion);
        setOutboundHeaders(port, createdHeaders);
    }

    public List<Header> createWSAddressingHeaders(BindingProvider port, String wsAddressingAction,
            AssertionType assertion) {
        String url = getUrlFormPort(port);
        return getWSAddressingHeaders(url, wsAddressingAction, assertion);
    }

    /**
     * @param port
     * @return
     */
    private String getUrlFormPort(BindingProvider port) {
        Map<String, Object> requestContext = getRequestContextFromPort(port);
        String url = (String) requestContext.get(KEY_URL);
        return url;
    }

    /**
     * @param url
     * @param serviceAction
     * @param assertion
     * @param requestContext
     */
    private void setSAMLForPort(BindingProvider port, String serviceAction, AssertionType assertion) {
        SamlTokenCreator oTokenCreator = getSamlTokenCreator();
        String url = getUrlFormPort(port);
        Map samlRequestContext = createSamlRequestContext(oTokenCreator, assertion, url, serviceAction);
        
        setSAMLRequestContext(port, samlRequestContext);
    }

    /**
     * @param port
     * @param samlRequestContext
     */
    private void setSAMLRequestContext(BindingProvider port, Map samlRequestContext) {
        Map<String, Object> requestContext = getRequestContextFromPort(port);
        requestContext.putAll(samlRequestContext);
    }

    /**
     * Sets the webservice port request timeout for the given serviceName webservice request timeouts are set in
     * gateway.properties as ServiceName.webserviceproxy.request.timeout=xxxxx where ServiceName is specified in
     * NhincConstants
     *
     * @param port
     * @param serviceName
     */
    public void setPortTimeoutByService(BindingProvider port, String serviceName) {
        setPortRequestTimeout(port, properties.getTimeoutFromConfig(serviceName));
    }

    /**
     * The com.sun.xml.ws.request.timeout corresponds to the socket read timeout, and so is the amount of time the
     * client will wait for an http response to be written to the socket.
     *
     * This timeout is best used to handle the case of a "hung server" where server accepts socket and http request
     * stream but never writes the http response to the response stream (it just hangs indefinitely). In this case, the
     * client will terminate the socket connection after the requestTimeout has elapsed.
     *
     * @param port
     * @param requestTimeout
     */
    private void setPortRequestTimeout(BindingProvider port, int requestTimeout) {
        if (requestTimeout > 0) {
            Map<String, Object> requestContext = getRequestContextFromPort(port);
            requestContext.put(KEY_REQUEST_TIMEOUT, requestTimeout);
            log.debug("set requestTimeout=" + requestTimeout);
        }
    }

    /**
     * The com.sun.xml.ws.request.timeout corresponds to the socket read timeout, and so is the amount of time the
     * client will wait for an http response to be written to the socket
     *
     * The com.sun.xml.ws.connect.timeout corresponds to the time the client/server will wait for the http request to be
     * completely written to the socket. This can generally be around 10 seconds except for xdr, which may require a
     * large connect timeout to send/push a large doc in http request
     *
     * @param port
     * @param url
     * @param requestContext
     */
    private void setPortTimeout(BindingProvider port) {
        Map<String, Object> requestContext = getRequestContextFromPort(port);

        int timeout = getTimeout();

        if (log.isInfoEnabled()) {
            String url = (String) requestContext.get(KEY_URL);
            log.info("initializing port [url=" + url + "][timeout=" + timeout + "][port=" + port.toString() + "]");
        }

        if (timeout > 0) {
            log.debug("setting timeout " + timeout);
            requestContext.put(KEY_CONNECT_TIMEOUT, timeout);
            requestContext.put(KEY_REQUEST_TIMEOUT, timeout);
        } else {
            log.warn("port timeout not set.  This may lead to undesirable behavior under heavy load");
        }
    }

    /**
     * @param url
     * @param port
     * @param url
     * @param requestContext
     */
    private void setPortUrl(BindingProvider port, String url) {
        Map<String, Object> requestContext = getRequestContextFromPort(port);

        log.debug("setting url " + url);

        if ((requestContext.containsKey(KEY_URL)) && (NullChecker.isNotNullish((String) requestContext.get(KEY_URL)))) {
            log.info("fyi: url was already set [" + requestContext.get(KEY_URL) + "], however, will re-set it");
        }
        requestContext.put(KEY_URL, url);
    }

    /**
     * @param isSecure
     * @param port
     * @param url
     * @param serviceAction
     * @param assertion
     */
    private void validateInitializePort(boolean isSecure, BindingProvider port, String url, String serviceAction,
            AssertionType assertion) {
        if (port == null) {
            throw new RuntimeException("Unable to initialize port (port null)");
        }
        if (NullChecker.isNullish(url)) {
            throw new RuntimeException("Unable to initialize port (url null)");
        }
        if ((isSecure) && (assertion == null)) {
            throw new RuntimeException("Unable to initialize secure port (assertion null)");
        }
        if ((isSecure) && (NullChecker.isNullish(serviceAction))) {
            throw new RuntimeException("Unable to initialize secure port (serviceAction null)");
        }

        Map<String, Object> requestContext = getRequestContextFromPort(port);
        if (requestContext == null) {
            throw new RuntimeException("Unable to retrieve request context from the port.");
        }

    }

    /**
     * This method will return the reflection method object for the given class and methodName.
     *
     * @param portClass The class containing the method.
     * @param methodName The name of the method to find.
     * @return The Method object for that method.
     */
    protected Method getMethod(Class portClass, String methodName) {
        Method oReturnMethod = null;

        // Note that there is an assumption here for what we are working on
        // that names of methods are unique and there is no overloading
        // of method names. We are looking only by method name. Since
        // these are specifically for web services - we are fine because
        // the method names are unique there.
        // ---------------------------------------------------------------
        Method[] oaMethod = portClass.getDeclaredMethods();
        for (Method oMethod : oaMethod) {
            if (oMethod.getName().equals(methodName)) {
                oReturnMethod = oMethod;
            }
        } // for (Method oMethod : oaMethod)

        return oReturnMethod;
    }

    /**
     * This method is used to invoke a method using reflection. This method's primary purpose is to allow us to override
     * this for unit testing purposes and simulate an exception to test that code.
     *
     * @param oMethod The reflection method object.
     * @param portObject The instance of the object.
     * @param operationInput The input parameter for the method.
     * @return The return value of the method.
     * @throws IllegalAccessException Exceptions thrown by invoke - passed on.
     * @throws InvocationTargetException Exceptions thrown by invoke - passed on.
     */
    protected Object invokeTheMethod(Method oMethod, Object portObject, Object operationInput)
            throws IllegalAccessException, InvocationTargetException {
        return oMethod.invoke(portObject, operationInput);
    }

    /**
     * Return the list of parameters.
     *
     * @param parameterTypes The parameter class list
     * @return An array listing the parameters.
     */
    private String listParameters(Class[] parameterTypes) {
        StringBuffer sbParams = new StringBuffer();

        for (Class oClass : parameterTypes) {
            if (sbParams.length() > 0) {
                sbParams.append(", ");
            }
            sbParams.append(oClass.getCanonicalName());
        }

        return sbParams.toString();
    }

    /**
     * Method to invoke an operation on a port using reflection.
     *
     * Information found at: http://download.oracle.com/docs/cd/E17409_01/javase/
     * tutorial/reflect/member/methodInvocation.html
     *
     * @param portObject Concrete port object.
     * @param portClass Port class
     * @param methodName Name of the method to be invoked on the port object to consume the web service operation.
     * @param operationInput Single parameter passed to the operation containing the web service request parameter.
     * @return Web service response - may be null if one way operation (Assumption).
     * @throws Exception generic exception
     */
    public Object invokePort(Object portObject, Class portClass, String methodName, Object operationInput)
            throws Exception {
        log.debug("Begin invokePort");

        if (portObject == null) {
            log.error("portObject was null");
        }

        Object oResponse = null;
        int iRetryCount = getRetryAttempts();
        int iRetryDelay = getRetryDelay();
        String sExceptionText = getExceptionText();

        Method oMethod = getMethod(portClass, methodName);
        if (oMethod == null) {
            throw new IllegalArgumentException(methodName + " not found for class " + portClass.getCanonicalName());
        }

        if ((iRetryCount > 0) && (iRetryDelay > 0) && (sExceptionText != null) && (sExceptionText.length() > 0)) {
            oResponse = invokePortWithRetry(portObject, portClass, operationInput, iRetryCount, iRetryDelay, oMethod);
        } // if ((iRetryCount > 0) && (iRetryDelay > 0))
        else {
            log.debug("Invoking " + portClass.getCanonicalName() + "." + oMethod.getName()
                    + ": Retry is not being used");

            oResponse = invokePort(portObject, portClass, operationInput, oResponse, oMethod);
        }

        log.debug("End invokePort");
        return oResponse;
    }

    /**
     * @param portObject
     * @param portClass
     * @param operationInput
     * @param oResponse
     * @param oMethod
     * @return
     * @throws Exception
     */
    private Object invokePort(Object portObject, Class portClass, Object operationInput, Object oResponse,
            Method oMethod) throws Exception {
        try {

            log.debug("with parameters:" + listParameters(oMethod.getParameterTypes()));

            oResponse = invokeTheMethod(oMethod, portObject, operationInput);
        } catch (IllegalArgumentException e) {
            String sErrorMessage = "The method was called with incorrect arguments. "
                    + "This assumes that the method should have exactly one "
                    + "argument and it must be of the correct type for this method. " + "Exception: " + e.getMessage();
            log.error(sErrorMessage, e);
            throw e;
        } catch (InvocationTargetException e) {
            Exception cause = e;
            Throwable throwable = e.getCause();
            if (throwable != null && throwable instanceof Exception) {
                cause = (Exception) throwable;
            }

            String sErrorMessage = "An unexpected exception occurred of type: " + cause.getClass().getCanonicalName()
                    + ". Exception: " + cause.getMessage();
            log.error(sErrorMessage, cause);
            throw cause;
        } catch (IllegalAccessException e) {
            // just log exception and throw it back out
            log.error("WebServiceProxyHelper::invokePort Exception: ", e);
            throw e;
        }
        return oResponse;
    }

    /**
     * @param iRetryCount
     * @param iRetryDelay
     * @param oMethod the method to be invoked on the port object to consume the web service operation.
     * @param portObject Concrete port object.
     * @param portClass Port class
     * @param operationInput Single parameter passed to the operation containing the web service request parameter.
     * @return Web service response - may be null if one way operation (Assumption).
     * @throws Exception
     */
    public Object invokePortWithRetry(Object portObject, Class portClass, Object operationInput, int iRetryCount,
            int iRetryDelay, Method oMethod) throws Exception {
        Object oResponse = null;
        int i = 1;
        Exception eCatchExp = null;
        String sExceptionText = getExceptionText();
        while (i <= iRetryCount) {
            try {
                log.debug("Invoking " + portClass.getCanonicalName() + "." + oMethod.getName() + ": Try #" + i);

                // invokePort will log any exception and throw back out
                oResponse = invokePort(portObject, portClass, operationInput, oResponse, oMethod);
                break;
            } catch (InvocationTargetException e) {
                Throwable throwable = e.getCause();
                if (throwable != null && throwable instanceof Exception) {
                    eCatchExp = (Exception) throwable;
                }

                // If we have tried our maximum number of times, then let's get
                // out of here
                // there is no need to sleep again if we are done.
                // -------------------------------------------------------------------------
                if (i++ < iRetryCount) {
                    handleInvokePortRetryFailure(portClass, iRetryDelay, i, sExceptionText,
                            (InvocationTargetException) eCatchExp);
                    retryDelay(portClass, iRetryDelay);

                    iRetryDelay = increaseRetryDelay(iRetryDelay);

                }
            }

        } // while (i <= iRetryCount)

        // We have tried our max times - so we need to get out of here.
        // --------------------------------------------------------------
        if (i >= iRetryCount) {
            log.error(
                    "Failed to call " + portClass.getCanonicalName() + "." + oMethod.getName() + " Webservice "
                            + iRetryCount + " times.  " + "Stopping processing of this call.  Exception: "
                            + eCatchExp.getMessage(), eCatchExp);
            throw eCatchExp;
        }
        return oResponse;
    }

    /**
     * @param iRetryDelay
     * @return
     */
    private int increaseRetryDelay(int iRetryDelay) {
        iRetryDelay = iRetryDelay + iRetryDelay; // Customer
                                                 // requested
                                                 // graceful
                                                 // degradation -
                                                 // want to slow
                                                 // it down more
                                                 // each timeout.
        return iRetryDelay;
    }

    /**
     * @param portClass
     * @param iRetryDelay
     */
    private void retryDelay(Class portClass, int iRetryDelay) {
        try {
            Thread.sleep(iRetryDelay);
        } catch (InterruptedException iEx) {
            log.error("Thread Got Interrupted while waiting on call: " + portClass.getCanonicalName() + ".  "
                    + "Exception: " + iEx.getMessage(), iEx);
        } catch (IllegalArgumentException iaEx) {
            log.error("Thread Got Interrupted while waiting on call: " + portClass.getCanonicalName() + ".  "
                    + "Exception: " + iaEx.getMessage(), iaEx);
        }
    }

    /**
     * @param portClass
     * @param iRetryDelay
     * @param i
     * @param sExceptionText
     * @param eCatchExp
     * @return
     * @throws Exception
     */
    private void handleInvokePortRetryFailure(Class portClass, int iRetryDelay, int i, String sExceptionText,
            InvocationTargetException eCatchExp) throws InvocationTargetException {
        boolean bFlag = false;
        StringTokenizer st = new StringTokenizer(sExceptionText, ",");
        while (st.hasMoreTokens()) {
            if (eCatchExp.getMessage().contains(st.nextToken())) {
                bFlag = true;
            }
        }
        if (bFlag) {
            log.warn("Exception calling ... web service: " + eCatchExp.getMessage());
            log.info("Retrying attempt [ " + i + " ] the connection after [ " + iRetryDelay + " ] seconds");

        } else {
            log.error(
                    "Unable to call " + portClass.getCanonicalName() + " Webservice due to  : "
                            + eCatchExp.getMessage(), eCatchExp);
            throw eCatchExp;
        }

    }

    /**
     * Retrieve the path for where the WSDL files are located.
     *
     * @return The path where the WSDL files are located.
     */
    protected String getWsdlPath() {
        return ServicePropertyLoader.getBaseWsdlPath();
    }

    /**
     * Create the service object.
     *
     * @param wsdlURL The URL for the WSDL.
     * @param namespaceURI The namespace URI of the web service
     * @param serviceLocalPart The local portion of the service name.
     * @return The service that was constructed.
     * @throws MalformedURLException If the method is unable to create the service object because of a malformed URL,
     *             this exception is returned.
     */
    protected Service constructService(String wsdlURL, String namespaceURI, String serviceLocalPart)
            throws MalformedURLException {
        return Service.create(new URL(wsdlURL), new QName(namespaceURI, serviceLocalPart));
    }

    /**
     * Create the service.
     *
     * @param wsdlFile The URL for the WSDL.
     * @param namespaceURI The namespace URI of the web service.
     * @param serviceLocalPart The local portion of the service name.
     * @return The service that was constructed.
     * @throws MalformedURLException If the method is unable to create the service object because of a malformed URL,
     *             this exception is returned.
     */
    public Service createService(String wsdlFile, String namespaceURI, String serviceLocalPart)
            throws MalformedURLException {
        Service service = null;
        log.debug("Begin createService");

        if ((wsdlFile == null) || (wsdlFile.length() < 1)) {
            log.error("WSDL file name is required.");
        } else if ((namespaceURI == null) || (namespaceURI.length() < 1)) {
            log.error("Namespace URI is required.");
        } else if ((serviceLocalPart == null) || (serviceLocalPart.length() < 1)) {
            log.error("Service local part name is required.");
        } else {
            final String wsdlPath = getWsdlPath();
            if ((wsdlPath != null) && (wsdlPath.length() > 0)) {
                String wsdlURL = wsdlPath + wsdlFile;
                log.debug("Creating service using the URL: " + wsdlURL);
                service = constructService(wsdlURL, namespaceURI, serviceLocalPart);
            } else {
                log.error("Unable to retrieve the WSDL path.");
            }
        }

        log.debug("End createService");
        return service;
    }

    /**
     * Add a target home community to the port object.
     * @param port The port to add the property to
     * @param targetSystem The targetSystem of the request
     */
    public void addTargetCommunity(BindingProvider port, NhinTargetSystemType targetSystem) {
        ((BindingProvider)port).getRequestContext().put(NhincConstants.WS_SOAP_TARGET_HOME_COMMUNITY_ID,
            HomeCommunityMap.getCommunityIdFromTargetSystem(targetSystem));
    }
}
