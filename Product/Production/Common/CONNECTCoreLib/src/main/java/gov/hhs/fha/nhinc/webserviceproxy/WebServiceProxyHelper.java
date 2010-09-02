/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.webserviceproxy;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.developer.WSBindingProvider;
import gov.hhs.fha.nhinc.async.AsyncHeaderCreator;
import gov.hhs.fha.nhinc.tools.ws.processor.generator.ServicePropertyLoader;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Method;
import java.util.StringTokenizer;
import java.net.URL;
import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class is used as a helper in each of the Web Service Proxies.  Since the
 * bulk of the work being done in each web service proxy is the same, it is
 * encapsulated in this helper class.
 *
 * @author Jason Ray, Sai Valluripalli, Les Westberg
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
    private Log log = null;

    /**
     * Force users to get an instance of this class from the getIntance method.
     */
    public WebServiceProxyHelper() {
        log = createLogger();
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
     * This is a helper class for unit testing purposes only.  It allows me to
     * mock out the connection manager call in the unit test.
     *
     * @param oTargetSystem The target system for the call.
     * @param sServiceName The name of the service to locate.
     * @return The endpoint URL.
     * @throws Exception An exception if one occurs.
     */
    protected String getEndPointFromConnectionManager(NhinTargetSystemType oTargetSystem, String sServiceName)
            throws ConnectionManagerException {
        return ConnectionManagerCache.getEndpontURLFromNhinTarget(oTargetSystem, sServiceName);
    }

    /**
     * This is a helper class for unit testing purposes only.  It allows me to
     * mock out the connection manager call in the unit test.
     *
     * @param sHomeCommunityId The home community Id for the target system.
     * @param sServiceName The name of the service to locate.
     * @return The endpoint URL.
     * @throws Exception An exception if one occurs.
     */
    protected String getEndPointFromConnectionManager(String sHomeCommunityId, String sServiceName)
            throws ConnectionManagerException {
        return ConnectionManagerCache.getEndpointURLByServiceName(sHomeCommunityId, sServiceName);
    }

    /**
     * This is a helper class for unit testing purposes only.  It allows me to
     * mock out the connection manager call in the unit test.  This
     *
     * @param sServiceName The name of the service to locate.
     * @return The endpoint URL.
     * @throws Exception An exception if one occurs.
     */
    protected String getLocalEndPointFromConnectionManager(String sServiceName)
            throws ConnectionManagerException {
        return ConnectionManagerCache.getLocalEndpointURLByServiceName(sServiceName);
    }

    /**
     * This method retrieves the URl from the ConnectionMananager for the
     * given TargetSystem.
     *
     * @param oTargetSystem The target system containing the information needed
     *                      to retrieve the endpoint URL.
     * @param sServiceName The name of the service for which the endpoint URL is desired.
     * @return The URL retrieved from the connection manager.
     */
    public String getUrlFromTargetSystem(NhinTargetSystemType oTargetSystem, String sServiceName)
            throws IllegalArgumentException, ConnectionManagerException, Exception {
        String sURL = "";

        if (oTargetSystem != null) {
            try {
                if (oTargetSystem.getHomeCommunity() != null) {
                    HomeCommunityType oHomeCommunity = oTargetSystem.getHomeCommunity();
                    log.info("Target Sys properties Home Comm ID:" + oHomeCommunity.getHomeCommunityId());
                    log.info("Target Sys properties Home Comm Description" + oHomeCommunity.getDescription());
                    log.info("Target Sys properties Home Comm Name" + oHomeCommunity.getName());
                }
                sURL = getEndPointFromConnectionManager(oTargetSystem, sServiceName);
            } catch (ConnectionManagerException e) {
                log.error("Error: Failed to retrieve url for service: " + sServiceName + ".  Exception: " + e.getMessage(), e);
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
     * This method retrieves the URl from the ConnectionMananager for the
     * given home community ID.
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
                log.error("Error: Failed to retrieve url for service: " + sServiceName + ".  Exception: " + e.getMessage(), e);
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
     * This method retrieves the URl from the ConnectionMananager for the
     * given home community ID.
     *
     * @param sHomeCommunity The home community id needed to retrieve the endpoint URL.
     * @param sServiceName The name of the service for which the endpoint URL is desired.
     * @return The URL retrieved from the connection manager.
     */
    public String getUrlLocalHomeCommunity(String sServiceName)
            throws IllegalArgumentException, ConnectionManagerException, Exception {
        String sURL = "";

        try {
            sURL = getLocalEndPointFromConnectionManager(sServiceName);
        } catch (ConnectionManagerException e) {
            log.error("Error: Failed to retrieve url for service: " + sServiceName + ".  Exception: " + e.getMessage(), e);
            throw (e);
        }

        return sURL;
    }

    /**
     * This method returns the given property from the gateway properties file.
     *
     * @param sKey The name of the property to retrieve.
     * @return The value of the property.
     * @throws PropertyAccessException The exception if one occurs.
     */
    protected String getGatewayProperty(String sKey)
            throws PropertyAccessException {
        return PropertyAccessor.getProperty(CONFIG_FILE, sKey);
    }

    /**
     * This retrieves the text to scan for in the exception.  This allows
     * the exceptions to be considered for retry to be configured in the
     * gateway.properties file.
     *
     * @return String The string of exception text.  This is a comma delimited list of
     *                text strings to look for in the exception.  If any one of the strings
     *                are
     */
    public String getExceptionText() {
        String configValue = "";
        try {
            configValue = getGatewayProperty(CONFIG_KEY_EXCEPTION);
            log.debug("Retrieved from config file (" + CONFIG_FILE + ".properties) " + CONFIG_KEY_EXCEPTION + "='" + configValue + "')");
        } catch (PropertyAccessException ex) {
            log.warn("Error occurred reading retry attempts value from config file (" + CONFIG_FILE + ".properties).  Exception = " + ex.toString());
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
        int retryAttempts = 0;
        try {
            String sValue = getGatewayProperty(CONFIG_KEY_RETRYATTEMPTS);
            log.debug("Retrieved from config file (" + CONFIG_FILE + ".properties) " + CONFIG_KEY_RETRYATTEMPTS + "='" + sValue + "')");
            if (NullChecker.isNotNullish(sValue)) {
                retryAttempts = Integer.parseInt(sValue);
            }
        } catch (PropertyAccessException ex) {
            log.warn("Error occurred reading property: " + CONFIG_KEY_RETRYATTEMPTS + " value from config file (" + CONFIG_FILE + ".properties).  Exception: " + ex.toString());
        } catch (NumberFormatException nfe) {
            log.warn("Error occurred converting property: " + CONFIG_KEY_RETRYATTEMPTS + " value to integer from config file (" + CONFIG_FILE + ".properties).  Exception: " + nfe.toString());
        }

        return retryAttempts;
    }

    /**
     * Retrieve the retry delay setting from the properties file.
     *
     * @return The retry delay setting.
     */
    public int getRetryDelay() {
        int retryDelay = 0;
        try {
            String sValue = getGatewayProperty(CONFIG_KEY_RETRYDELAY);
            log.debug("Retrieved from config file (" + CONFIG_FILE + ".properties) " + CONFIG_KEY_RETRYDELAY + "='" + sValue + "')");
            if (NullChecker.isNotNullish(sValue)) {
                retryDelay = Integer.parseInt(sValue);
            }
        } catch (PropertyAccessException ex) {
            log.warn("Error occurred reading property: " + CONFIG_KEY_RETRYDELAY + " value from config file (" + CONFIG_FILE + ".properties).  Exception: " + ex.toString());
        } catch (NumberFormatException nfe) {
            log.warn("Error occurred converting property: " + CONFIG_KEY_RETRYDELAY + " value to integer from config file (" + CONFIG_FILE + ".properties).  Exception: " + nfe.toString());
        }

        return retryDelay;
    }

    /**
     * Retrieve the timeout value from the properties file.
     *
     * @return
     */
    public int getTimeout() {
        int timeout = 0;
        try {
            String sValue = getGatewayProperty(CONFIG_KEY_TIMEOUT);
            log.debug("Retrieved from config file (" + CONFIG_FILE + ".properties) " + CONFIG_KEY_TIMEOUT + "='" + sValue + "')");
            if (NullChecker.isNotNullish(sValue)) {
                timeout = Integer.parseInt(sValue);
            }
        } catch (PropertyAccessException ex) {
            log.warn("Error occurred reading property: " + CONFIG_KEY_TIMEOUT + " value from config file (" + CONFIG_FILE + ".properties).  Exception: " + ex.toString());
        } catch (NumberFormatException nfe) {
            log.warn("Error occurred converting property: " + CONFIG_KEY_TIMEOUT + " value to integer from config file (" + CONFIG_FILE + ".properties).  Exception: " + nfe.toString());
        }

        return timeout;
    }

    /**
     * This method returns the request context from the port.  It is here mainly
     * to facilitate mock unit testing.
     *
     * @param port The port containing the request context.
     * @return The request context.
     */
    protected Map getRequestContextFromPort(BindingProvider port) {
        return port.getRequestContext();
    }

    /**
     * This method returns an instance of the SamlTokenCreator class.  This method
     * is here to facilitate mock unit testing.
     * @return instance of the SamlTokenCreator
     */
    protected SamlTokenCreator getSamlTokenCreator() {
        return new SamlTokenCreator();
    }

    /**
     * This method returns the the request context with the information extracted
     * from the assertion class and the URL and service action.
     *
     * @param oTokenCreator The SamlTokenCreator object that will create the request context.
     * @param oAssertion The assertion information to be used in the context.
     * @param sUrl The URL of the web service.
     * @param sServiceAction The action for the web service.
     * @return The request context with the SAML information.
     */
    protected Map createSamlRequestContext(SamlTokenCreator oTokenCreator, AssertionType oAssertion, String sUrl, String sServiceAction) {
        return oTokenCreator.CreateRequestContext(oAssertion, sUrl, sServiceAction);
    }

    /**
     * This method returns an instance of the AsyncHeaderCreator class.  This method
     * is here to facilitate mock unit testing.
     * @return instance of the AsyncHeaderCreator
     */
    protected AsyncHeaderCreator getAsyncHeaderCreator() {
        return new AsyncHeaderCreator();
    }

    /**
     * This method retrieves the message identifier stored in the assertion
     * If the message ID is null or empty, this method will generate a new UUID
     * to use for the message ID.
     *
     * @param assertion The assertion information containing the SAML assertion
     *        to be assigned to the message.
     * @return The message identifier
     */
    protected String getMessageId(AssertionType assertion) {
        if ((assertion != null) &&
                (assertion.getMessageId() != null) &&
                (assertion.getMessageId().trim().length() > 0)) {
            return assertion.getMessageId();
        } else {
            UUID oUuid = UUID.randomUUID();
            String sUuid = oUuid.toString();
            log.warn("Assertion did not contain a message ID.  Generating one now...  Message ID = " + sUuid);
            if (assertion != null) {
                assertion.setMessageId(sUuid);
            }
            return sUuid;
        }
    }

    /**
     * This method retrieves the list of relatesTo identifiers stored in the
     * assertion.
     * @param assertion  The assertion information containing the SAML assertion
     *        to be assigned to the message.
     * @return The list of relatesTo identifiers
     */
    protected List<String> getRelatesTo(AssertionType assertion) {
        List<String> allRelatesTo = new ArrayList();
        if (assertion != null &&
                NullChecker.isNotNullish(assertion.getRelatesToList())) {
            allRelatesTo.addAll(assertion.getRelatesToList());
        }
        return allRelatesTo;
    }

    /**
     * This method gset the WS-Addressing headers to be initialized on the port
     * @param url The endpoint url defining <To>
     * @param wsAddressingAction The action defining <Action>
     * @param assertion The assertion whic contains the messageId and the
     *        relatesTo identifiers
     * @return The list of WS-Addressing headers
     */
    protected List getWSAddressingHeaders(String url, String wsAddressingAction, AssertionType assertion) {

        AsyncHeaderCreator hdrCreator = getAsyncHeaderCreator();
        String messageId = getMessageId(assertion);
        List<String> allRelatesTo = getRelatesTo(assertion);

        List<Header> createdHeaders = hdrCreator.createOutboundHeaders(url, wsAddressingAction, messageId, allRelatesTo);
        return createdHeaders;
    }

    /**
     * This method sets the provided WS-Addressing headers to the outbound headers on the port
     * @param port The port to be initialized
     * @param createdHeaders The listing of WS-Addressing headers.
     */
    protected void setOutboundHeaders(BindingProvider port, List<Header> createdHeaders) {
        ((WSBindingProvider) port).setOutboundHeaders(createdHeaders);
    }

    /**
     * This method initializes the port and sets various values that are required
     * for processing - like timeout, URL, etc.  This should not be used in any new code
     * it was only placed here as a stop gap during refactor for old code.  After
     * the refactor this should not be used at all.
     *
     * @param port The port to be initialized.
     * @param url The URL to be assigned to the port.
     * @deprecated
     */
    public void initializePort(BindingProvider port, String url) {
        initializePort(false, port, url, null, null, null);
    }

    /**
     * This method initializes the port for an unsecure interface call
     * and sets various values that are required for processing - like timeout,
     * URL, etc.
     *
     * @param port The port to be initialized.
     * @param url The URL of the web service to be assigned to the port.
     * @param wsAddressingAction The WS-Addressing action associated with this
     *        web service call.  If this is null, the construction of the
     *        WS-Address header will depend on wsdl policy statements.
     * @param assertion The assertion information.
     */
    public void initializeUnsecurePort(BindingProvider port, String url, String wsAddressingAction, AssertionType assertion) {
        initializePort(false, port, url, null, wsAddressingAction, assertion);
    }

    /**
     * This method initializes the port for an secure interface call
     * and sets various values that are required for processing - like timeout,
     * URL, etc.
     *
     * @param port The port to be initialized.
     * @param url The URL of the web service to be assigned to the port.
     * @param serviceAction The action for the web service.
     * @param wsAddressingAction The WS-Addressing action associated with this
     *        web service call.  If this is null, the construction of the
     *        WS-Address header will depend on wsdl policy statements.
     * @param assertion The assertion information.
     */
    public void initializeSecurePort(BindingProvider port, String url, String serviceAction,
            String wsAddressingAction, AssertionType assertion) {
        initializePort(true, port, url, serviceAction, wsAddressingAction, assertion);
    }

    /**
     * This method initializes the port and sets various values that are required
     * for processing - like timeout, URL, etc.
     *
     * @param port The port to be initialized.
     * @param url The URL of the web service to be assigned to the port.
     * @param serviceAction The action for the web service.
     * @param wsAddressingAction The WS-Addressing action associated with this
     *        web service call.  If this is null, the construction of the
     *        WS-Address header will depend on wsdl policy statements.
     * @param assertion The assertion information containing the SAML assertion
     *        to be assigned to the message.
     * @deprecated
     */
    public void initializePort(BindingProvider port, String url, String serviceAction,
            String wsAddressingAction, AssertionType assertion) {
        boolean bIsSecure = true;
        if (assertion == null) {
            bIsSecure = false;
        }

        initializePort(bIsSecure, port, url, serviceAction, wsAddressingAction, assertion);
    }

    /**
     * This method initializes the port and sets various values that are required
     * for processing - like timeout, URL, etc.
     *
     * @param isSecure If TRUE set this up as a secure call.
     * @param port The port to be initialized.
     * @param url The URL of the web service to be assigned to the port.
     * @param serviceAction The action for the web service.
     * @param wsAddressingAction The WS-Addressing action associated with this 
     *        web service call.  If this is null, the construction of the
     *        WS-Address header will depend on wsdl policy statements.
     * @param assertion The assertion information containing the SAML assertion
     *        to be assigned to the message.
     */
    private void initializePort(boolean isSecure, BindingProvider port, String url, String serviceAction,
            String wsAddressingAction, AssertionType assertion) {
        log.info("begin initializePort");
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

        Map requestContext = getRequestContextFromPort(port);
        if (requestContext == null) {
            throw new RuntimeException("Unable to retrieve request context from the port.");
        }

        int timeout = getTimeout();
        log.info("initializing port [url=" + url + "][timeout=" + timeout + "][port=" + port.toString() + "]");

        log.debug("setting url " + url);
        if ((requestContext.containsKey(KEY_URL)) &&
                (NullChecker.isNotNullish((String) requestContext.get(KEY_URL)))) {
            log.info("fyi: url was already set [" + requestContext.get(KEY_URL) + "], however, will re-set it");
        }
        requestContext.put(KEY_URL, url);

        if (timeout > 0) {
            log.debug("setting timeout " + timeout);
            requestContext.put(KEY_CONNECT_TIMEOUT, timeout);
            requestContext.put(KEY_REQUEST_TIMEOUT, timeout);
        } else {
            log.warn("port timeout not set.  This may lead to undesirable behavior under heavy load");
        }

        // If we have been passed the assertion information, then create the SAML information for this...
        //------------------------------------------------------------------------------------------------
        if (isSecure) {
            SamlTokenCreator oTokenCreator = getSamlTokenCreator();
            Map samlMap = createSamlRequestContext(oTokenCreator, assertion, url, serviceAction);
            requestContext.putAll(samlMap);
        }

        // Create the WS-Addressing Headers - action must be supplied
        //-----------------------------------------------------------
        if (wsAddressingAction != null) {
            List createdHeaders = getWSAddressingHeaders(url, wsAddressingAction, assertion);
            setOutboundHeaders(port, createdHeaders);
        } else {
            log.warn("WS-Addressing information is unavailable, relying on wsdl policy");
        }
        log.info("end initializePort");
    }

    /**
     * This method will return the reflection method object for the
     * given class and methodName.  
     *
     * @param portClass  The class containing the method.
     * @param methodName The name of the method to find.
     * @return The Method object for that method.
     */
    protected Method getMethod(Class portClass, String methodName) {
        Method oReturnMethod = null;

        // Note that there is an assumption here for what we are working on
        // that names of methods are unique and there is no overloading
        // of method names.   We are looking only by method name.  Since
        // these are specifically for web services - we are fine because
        // the method names are unique there.
        //---------------------------------------------------------------
        Method[] oaMethod = portClass.getDeclaredMethods();
        for (Method oMethod : oaMethod) {
            if (oMethod.getName().equals(methodName)) {
                oReturnMethod = oMethod;
            }
        }   // for (Method oMethod : oaMethod)

        return oReturnMethod;
    }

    /**
     * This method is used to invoke a method using reflection.  This method's
     * primary purpose is to allow us to override this for unit testing purposes
     * and simualate an execption to test that code.
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
    private String listParameters(Class[] parameterTypes)
    {
        StringBuffer sbParams = new StringBuffer();

        for (Class oClass : parameterTypes)
        {
            if (sbParams.length() > 0)
            {
                sbParams.append(", ");
            }
            sbParams.append(oClass.getCanonicalName());
        }

        return sbParams.toString();
    }

    /**
     * Method to invoke an operation on a port using reflection.
     *
     * Information found at: http://download.oracle.com/docs/cd/E17409_01/javase/tutorial/reflect/member/methodInvocation.html
     *
     * @param portObject Concrete port object.
     * @param portClass Port class
     * @param methodName Name of the method to be invoked on the port object to consume the web service operation.
     * @param operationInput Single parameter passed to the operation containing the web service request parameter.
     * @return Web service response - may be null if one way operation (Assumption).
     */
    public Object invokePort(Object portObject, Class portClass, String methodName, Object operationInput)
            throws Exception {
        log.debug("Begin invokePort");

        if (portObject == null)
        {
            log.error("portObject was null");
        }
        
        Object oResponse = null;
        int iRetryCount = getRetryAttempts();
        int iRetryDelay = getRetryDelay();
        String sExceptionText = getExceptionText();
        javax.xml.ws.WebServiceException eCatchExp = null;

        Method oMethod = getMethod(portClass, methodName);
        if (oMethod != null) {
            if ((iRetryCount > 0) &&
                    (iRetryDelay > 0) &&
                    (sExceptionText != null) &&
                    (sExceptionText.length() > 0)) {
                int i = 1;
                while (i <= iRetryCount) {
                    try {
                        log.debug("Invoking " + portClass.getCanonicalName() + "." + oMethod.getName() + ": Try #" + i);
                        log.debug("with parameters:" + listParameters(oMethod.getParameterTypes()));
                        oResponse = invokeTheMethod(oMethod, portObject, operationInput);
                        break;
                    } catch (javax.xml.ws.WebServiceException e) {
                        eCatchExp = e;

                        // If we have tried our maximum number of times, then let's get out of here
                        // there is no need to sleep again if we are done.
                        //-------------------------------------------------------------------------
                        if (i < iRetryCount) {
                            boolean bFlag = false;
                            StringTokenizer st = new StringTokenizer(sExceptionText, ",");
                            while (st.hasMoreTokens()) {
                                if (e.getMessage().contains(st.nextToken())) {
                                    bFlag = true;
                                }
                            }
                            if (bFlag) {
                                log.warn("Exception calling ... web service: " + e.getMessage());
                                log.info("Retrying attempt [ " + i + " ] the connection after [ " + iRetryDelay + " ] seconds");
                                i++;
                                try {
                                    Thread.sleep(iRetryDelay);
                                } catch (InterruptedException iEx) {
                                    log.error("Thread Got Interrupted while waiting on call: " + portClass.getCanonicalName() + ".  " +
                                            "Exception: " + iEx.getMessage(), iEx);
                                } catch (IllegalArgumentException iaEx) {
                                    log.error("Thread Got Interrupted while waiting on call: " + portClass.getCanonicalName() + ".  " +
                                            "Exception: " + iaEx.getMessage(), iaEx);
                                }
                                iRetryDelay = iRetryDelay + iRetryDelay; // Customer requested graceful degradation - want to slow it down more each timeout.
                                } else {
                                log.error("Unable to call " + portClass.getCanonicalName() + " Webservice due to  : " + e.getMessage(), e);
                                throw e;
                            }
                        } // if (i < iRetryCount)
                        else {
                            i++;            // We need to get out of this loop.
                            }
                    } // catch (javax.xml.ws.WebServiceException e)
                    catch (IllegalArgumentException e) {
                        String sErrorMessage = "The method was called with incorrect arguments. " +
                                "This assumes that the method should have exactly one " +
                                "argument and it must be of the correct type for this method. " +
                                "Exception: " + e.getMessage();
                        log.error(sErrorMessage, e);
                        throw e;
                    } catch (Exception e) {
                        // As near as we can tell based on the way we are using this, I do not
                        // believe there is any other exception we will see - but we want to
                        // log them if we see them.
                        //---------------------------------------------------------------------
                        String sErrorMessage = "An unexpected exception occurred of type: " +
                                e.getClass().getCanonicalName() + ". Exception: " +
                                e.getMessage();
                        log.error(sErrorMessage, e);
                        throw e;
                    }

                }   // while (i <= iRetryCount)

                // We have tried our max times - so we need to get out of here.
                //--------------------------------------------------------------
                if (i >= iRetryCount) {
                    log.error("Failed to call " + portClass.getCanonicalName() + "." + oMethod.getName() +
                            " Webservice " + iRetryCount + " times.  " +
                            "Stopping processing of this call.  Exception: " + eCatchExp.getMessage(), eCatchExp);
                    throw eCatchExp;
                }
            } // if ((iRetryCount > 0) && (iRetryDelay > 0))
            else {
                try {
                    log.debug("Invoking " + portClass.getCanonicalName() + "." + oMethod.getName() + ": Retry is not being used");
                    oResponse = invokeTheMethod(oMethod, portObject, operationInput);
                } catch (IllegalArgumentException e) {
                    String sErrorMessage = "The method was called with incorrect arguments. " +
                            "This assumes that the method should have exactly one " +
                            "argument and it must be of the correct type for this method. " +
                            "Exception: " + e.getMessage();
                    log.error(sErrorMessage, e);
                    throw e;
                } catch (Exception e) {
                    // As near as we can tell based on the way we are using this, I do not
                    // believe there is any other exception we will see - but we want to
                    // log them if we see them.
                    //---------------------------------------------------------------------
                    String sErrorMessage = "An unexpected exception occurred of type: " +
                            e.getClass().getCanonicalName() + ". Exception: " +
                            e.getMessage();
                    log.error(sErrorMessage, e);
                    throw e;
                }
            }   // else
        }   // if (oMethod != null)

        log.debug("End invokePort");
        return oResponse;
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
     * @param namespaceURI  The namespace URI of the web service
     * @param serviceLocalPart The local portion of the service name.
     * @return The service that was constructed.
     * @throws MalformedURLException If the method is unable to create the
     *         service object because of a malformed URL, this exception is returned.
     */
    protected Service constructService(String wsdlURL, String namespaceURI, String serviceLocalPart) throws MalformedURLException {
        return Service.create(new URL(wsdlURL), new QName(namespaceURI, serviceLocalPart));
    }

    /**
     * Create the service.
     *
     * @param wsdlFile  The URL for the WSDL.
     * @param namespaceURI The namespace URI of the web service.
     * @param serviceLocalPart The local portion of the service name.
     * @return The service that was constructed.
     * @throws MalformedURLException If the method is unable to create the
     *         service object because of a malformed URL, this exception is returned.
     */
    public Service createService(String wsdlFile, String namespaceURI, String serviceLocalPart) throws MalformedURLException {
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
}
