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

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.AdapterEndpointManager;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.properties.IPropertyAcessor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import javax.xml.ws.BindingProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    private Log log = null;
    private final WebServiceProxyHelperProperties properties;
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
    private String getEndPointFromConnectionManagerByGatewayAPILevel(NhinTargetSystemType oTargetSystem,
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
     * This method returns the endpoint url of the passed in service name in the local gateway (hcid)
     *
     * @param sServiceName The name of the service to locate.
     * @return The endpoint URL.
     * @throws Exception An exception if one occurs.
     */
    private String getLocalEndPointFromConnectionManager(String sServiceName) throws ConnectionManagerException {
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
     * This method will return the reflection method object for the given class and methodName.
     *
     * @param portClass The class containing the method.
     * @param methodName The name of the method to find.
     * @return The Method object for that method.
     */
    protected Method getMethod(Class<?> portClass, String methodName) {
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
    private Object invokeTheMethod(Method oMethod, Object portObject, Object operationInput)
            throws IllegalAccessException, InvocationTargetException {
        return oMethod.invoke(portObject, operationInput);
    }

    /**
     * Return the list of parameters.
     *
     * @param parameterTypes The parameter class list
     * @return An array listing the parameters.
     */
    private String listParameters(Class<?>[] parameterTypes) {
        StringBuffer sbParams = new StringBuffer();

        for (Class<?> oClass : parameterTypes) {
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
    public Object invokePort(Object portObject, Class<?> portClass, String methodName, Object operationInput)
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
    private Object invokePort(Object portObject, Class<?> portClass, Object operationInput, Object oResponse,
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
    public Object invokePortWithRetry(Object portObject, Class<?> portClass, Object operationInput, int iRetryCount,
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
    private void retryDelay(Class<?> portClass, int iRetryDelay) {
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
    private void handleInvokePortRetryFailure(Class<?> portClass, int iRetryDelay, int i, String sExceptionText,
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
     * Add a target home community to the port object.
     * @param port The port to add the property to
     * @param targetSystem The targetSystem of the request
     */
    public void addTargetCommunity(BindingProvider port, NhinTargetSystemType targetSystem) {
        port.getRequestContext().put(NhincConstants.WS_SOAP_TARGET_HOME_COMMUNITY_ID,
            HomeCommunityMap.getCommunityIdFromTargetSystem(targetSystem));
    }

    /**
     * Add a target api level to the port object.
     * @param port The port to add the property to
     * @param apiLevel the target api level to add.
     */
    public void addTargetApiLevel(BindingProvider port, GATEWAY_API_LEVEL apiLevel) {
        port.getRequestContext().put(NhincConstants.TARGET_API_LEVEL, apiLevel);
    }

    /**
     * Add service name to the port object.
     * @param port The port to add the property to
     * @param apiLevel the target api level to add.
     */
    public void addServiceName(BindingProvider port, String serviceName) {
        port.getRequestContext().put(NhincConstants.SERVICE_NAME, serviceName);
    }
    
    
}
