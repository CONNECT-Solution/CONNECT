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

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.AdapterEndpointManager;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.exchangemgr.InternalExchangeManager;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import gov.hhs.fha.nhinc.properties.IPropertyAcessor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.ws.BindingProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOG = LoggerFactory.getLogger(WebServiceProxyHelper.class);
    private final WebServiceProxyHelperProperties properties;

    public WebServiceProxyHelper() {
        properties = WebServiceProxyHelperProperties.getInstance();

    }

    /**
     * DI constructor.
     *
     * @param propertyAccessor
     */
    public WebServiceProxyHelper(IPropertyAcessor propertyAccessor) {
        properties = new WebServiceProxyHelperProperties(propertyAccessor);
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
        String sServiceName, GATEWAY_API_LEVEL level) throws ExchangeManagerException {

        return ExchangeManager.getInstance().getEndpointURLFromNhinTarget(oTargetSystem, sServiceName);
    }

    /**
     * This method returns the URL endpoint of the passed in adapter service name with the given api level
     *
     * @param sServiceName The name of the service to locate.
     * @param level The adapter api level.
     * @return The endpoint URL.
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public String getEndPointFromConnectionManagerByAdapterAPILevel(String sServiceName, ADAPTER_API_LEVEL level)
        throws ExchangeManagerException {

        return InternalExchangeManager.getInstance().getEndpointURL(sServiceName, level);
    }

    public String getEndpointFromConnectionManagerByEntitySpecLevel(String sServiceName, UDDI_SPEC_VERSION version) throws ExchangeManagerException {
        return InternalExchangeManager.getInstance().getEndpointURL(sServiceName, version);
    }

    /**
     * This method returns the URL endpoint of the passed in adapter service name
     *
     * @param sServiceName The name of the service to locate.
     * @return The endpoint URL.
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public String getAdapterEndPointFromConnectionManager(String sServiceName) throws ExchangeManagerException {
        AdapterEndpointManager adapterEndpointManager = new AdapterEndpointManager();
        ADAPTER_API_LEVEL level = adapterEndpointManager.getApiVersion(sServiceName);

        return getEndPointFromConnectionManagerByAdapterAPILevel(sServiceName, level);
    }

    /**
     * This method returns the URL endpoint of the passed in adapter service name
     *
     * @param sHomeCommunityId
     * @param sServiceName The name of the service to locate.
     * @return The endpoint URL.
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public String getAdapterEndPointFromConnectionManager(String sHomeCommunityId, String sServiceName)
        throws ExchangeManagerException {

        AdapterEndpointManager adapterEndpointManager = new AdapterEndpointManager();
        ADAPTER_API_LEVEL level = adapterEndpointManager.getApiVersion(sServiceName);

        return InternalExchangeManager.getInstance().getEndpointURL(sHomeCommunityId, sServiceName, level);
    }

    /**
     * This method returns the endpoint url of the passed in service name in the local gateway (hcid)
     *
     * @param sServiceName The name of the service to locate.
     * @return The endpoint URL.
     * @throws Exception An exception if one occurs.
     */
    private String getLocalEndPointFromConnectionManager(String sServiceName) throws ExchangeManagerException {
        String url = "";
        url = ExchangeManager.getInstance().getEndpointURL(sServiceName);
        if (StringUtils.isEmpty(url)) {
            url = InternalExchangeManager.getInstance().getEndpointURL(sServiceName);
        }
        return url;
    }

    /**
     * This method retrieves the URl from the ConnectionMananager for the given TargetSystem.
     *
     * @param oTargetSystem The target system containing the information needed to retrieve the endpoint URL.
     * @param sServiceName The name of the service for which the endpoint URL is desired.
     * @param level
     * @return The URL retrieved from the connection manager.
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public String getUrlFromTargetSystemByGatewayAPILevel(NhinTargetSystemType oTargetSystem, String sServiceName,
        GATEWAY_API_LEVEL level) throws IllegalArgumentException, ExchangeManagerException, Exception {

        String sURL;

        if (oTargetSystem != null) {
            try {
                if (oTargetSystem.getHomeCommunity() != null) {
                    HomeCommunityType oHomeCommunity = oTargetSystem.getHomeCommunity();
                    LOG.info("Target Sys properties Home Comm ID: {}", oHomeCommunity.getHomeCommunityId());
                    LOG.info("Target Sys properties Home Comm Description: {}", oHomeCommunity.getDescription());
                    LOG.info("Target Sys properties Home Comm Name: {}", oHomeCommunity.getName());
                }
                LOG.info("Exchange Name in the Outbound request: {}", oTargetSystem.getExchangeName());
                sURL = getEndPointFromConnectionManagerByGatewayAPILevel(oTargetSystem, sServiceName, level);
            } catch (ExchangeManagerException e) {
                LOG.error("Error: Failed to retrieve url for service {}: {}", sServiceName, e.getLocalizedMessage(), e);
                throw e;
            }
        } else {
            String sErrorMessage = "Target system passed into the proxy is null";
            LOG.error(sErrorMessage);
            throw new IllegalArgumentException(sErrorMessage);
        }

        return sURL;
    }

    /**
     * This method retrieves the URl from the ConnectionMananager for the given home community ID.
     *
     * @param sServiceName The name of the service for which the endpoint URL is desired.
     * @return The URL retrieved from the connection manager.
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public String getUrlLocalHomeCommunity(String sServiceName)
        throws IllegalArgumentException, ExchangeManagerException, Exception {

        String sURL;

        try {
            sURL = getLocalEndPointFromConnectionManager(sServiceName);
        } catch (ExchangeManagerException e) {
            LOG.error("Failed to retrieve url for service {}: {}", sServiceName, e.getLocalizedMessage(), e);
            throw e;
        }

        return sURL;
    }

    /**
     * This retrieves the text to scan for in the exception. This allows the exceptions to be considered for retry to be
     * configured in the gateway.properties file.
     *
     * @return String The string of exception text. This is a comma delimited list of text strings to look for in the
     * exception. If any one of the strings are
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
        }

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
    private Object invokeTheMethod(Method oMethod, Object portObject, Object... operationInput)
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
        StringBuilder sbParams = new StringBuilder();

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
     * <p>
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
    public Object invokePort(Object portObject, Class<?> portClass, String methodName, Object... operationInput)
        throws Exception {
        LOG.debug("Begin invokePort");

        if (portObject == null) {
            LOG.error("portObject was null");
        }

        Object oResponse = null;
        int iRetryCount = getRetryAttempts();
        int iRetryDelay = getRetryDelay();
        String sExceptionText = getExceptionText();

        Method oMethod = getMethod(portClass, methodName);
        if (oMethod == null) {
            throw new IllegalArgumentException(methodName + " not found for class " + portClass.getCanonicalName());
        }

        if (iRetryCount > 0 && iRetryDelay > 0 && StringUtils.isNotEmpty(sExceptionText)) {
            oResponse = invokePortWithRetry(portObject, portClass, iRetryCount, iRetryDelay, oMethod, operationInput);
        } else {
            LOG.debug("Invoking {}.{}: Retry is not being used", portClass.getCanonicalName(), oMethod.getName());

            oResponse = invokePort(portObject, portClass, oResponse, oMethod, operationInput);
        }

        LOG.debug("End invokePort");
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
    private Object invokePort(Object portObject, Class<?> portClass, Object oResponse, Method oMethod,
        Object... operationInput) throws Exception {

        try {
            LOG.debug("with parameters: {}", listParameters(oMethod.getParameterTypes()));

            oResponse = invokeTheMethod(oMethod, portObject, operationInput);
        } catch (IllegalArgumentException e) {
            LOG.error(
                "The method was called with incorrect arguments. This assumes that the method should have "
                    + "exactly one argument and it must be of the correct type for this method: {}",
                    e.getLocalizedMessage(), e);
            throw e;
        } catch (InvocationTargetException e) {
            Exception cause = e;
            Throwable throwable = e.getCause();
            if (throwable != null && throwable instanceof Exception) {
                cause = (Exception) throwable;
            }

            LOG.error("An unexpected exception occurred of type {}: {}", cause.getClass().getCanonicalName(),
                cause.getLocalizedMessage(), cause);
            throw cause;
        } catch (IllegalAccessException e) {
            // just log exception and throw it back out
            LOG.error("WebServiceProxyHelper::invokePort Exception: {}", e.getLocalizedMessage(), e);
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
    public Object invokePortWithRetry(Object portObject, Class<?> portClass, int iRetryCount, int iRetryDelay,
        Method oMethod, Object... operationInput) throws Exception {

        Object oResponse = null;
        int i = 1;
        Exception eCatchExp = new Exception();
        String sExceptionText = getExceptionText();

        while (i <= iRetryCount) {
            try {
                LOG.debug("Invoking " + portClass.getCanonicalName() + "." + oMethod.getName() + ": Try #" + i);

                // invokePort will log any exception and throw back out
                oResponse = invokePort(portObject, portClass, oResponse, oMethod, operationInput);
                break;
            } catch (InvocationTargetException e) {
                LOG.error("Failed to invoke service, attempt #{}: ", e.getLocalizedMessage(), e);
                Throwable throwable = e.getCause();
                if (throwable != null && throwable instanceof Exception) {
                    eCatchExp = (Exception) throwable;
                }

                // If we have tried our maximum number of times, there is no need to sleep again
                if (i++ < iRetryCount) {
                    handleInvokePortRetryFailure(portClass, iRetryDelay, i, sExceptionText,
                        (InvocationTargetException) eCatchExp);
                    retryDelay(portClass, iRetryDelay);

                    iRetryDelay = increaseRetryDelay(iRetryDelay);
                } else {
                    LOG.error("Failed to call {}.{} web service after {} attempts, stop processing of this call: {}",
                        portClass.getCanonicalName(), oMethod.getName(), iRetryCount,
                        eCatchExp.getLocalizedMessage(), eCatchExp);

                    throw eCatchExp;
                }
            }

        }

        return oResponse;
    }

    /**
     * Customer requested graceful degradation want to slow it down more each timeout.
     *
     * @param iRetryDelay
     * @return
     */
    private int increaseRetryDelay(int iRetryDelay) {
        return iRetryDelay * 2;
    }

    /**
     * @param portClass
     * @param iRetryDelay
     */
    private void retryDelay(Class<?> portClass, int iRetryDelay) {
        try {
            Thread.sleep(iRetryDelay);
        } catch (InterruptedException | IllegalArgumentException iEx) {
            LOG.error("Thread Got Interrupted while waiting on call {}: {}", portClass.getCanonicalName(),
                iEx.getLocalizedMessage(), iEx);
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
            LOG.warn("Exception calling ... web service: {}", eCatchExp.getMessage());
            LOG.info("Retrying attempt [ {} ] the connection after [ {} ] seconds", i, iRetryDelay);

        } else {
            LOG.error("Unable to call {} web service: {}", portClass.getCanonicalName(),
                eCatchExp.getLocalizedMessage(), eCatchExp);
            throw eCatchExp;
        }
    }

    /**
     * Add a target home community to the port object.
     *
     * @param port The port to add the property to
     * @param targetSystem The targetSystem of the request
     */
    public void addTargetCommunity(BindingProvider port, NhinTargetSystemType targetSystem) {
        port.getRequestContext().put(NhincConstants.WS_SOAP_TARGET_HOME_COMMUNITY_ID,
            HomeCommunityMap.getCommunityIdFromTargetSystem(targetSystem));
    }

    /**
     * Add a target api level to the port object.
     *
     * @param port The port to add the property to
     * @param apiLevel the target api level to add.
     */
    public void addTargetApiLevel(BindingProvider port, GATEWAY_API_LEVEL apiLevel) {
        port.getRequestContext().put(SamlConstants.TARGET_API_LEVEL, apiLevel);
    }

    /**
     * Add service name to the port object.
     *
     * @param port The port to add the property to
     * @param serviceName
     */
    public void addServiceName(BindingProvider port, String serviceName) {
        port.getRequestContext().put(NhincConstants.SERVICE_NAME, serviceName);
    }

    // static method
    public static boolean updateAdapterServiceUrlBy(String serviceName, String url) throws Exception {
        return InternalExchangeManager.getInstance().updateServiceUrl(serviceName, url);
    }

    public static String getAdapterEndpointURLBy(String sServiceName) throws ExchangeManagerException {
        AdapterEndpointManager adapterEndpointManager = new AdapterEndpointManager();
        ADAPTER_API_LEVEL level = adapterEndpointManager.getApiVersion(sServiceName);

        return getAdapterEndpointURLBy(sServiceName, level);
    }

    public static String getAdapterEndpointURLBy(String sServiceName, ADAPTER_API_LEVEL level)
        throws ExchangeManagerException {
        return InternalExchangeManager.getInstance().getEndpointURL(sServiceName, level);
    }

    public static String getAdapterEndpointURLBy(String sServiceName, String sHomeCommunityId)
        throws ExchangeManagerException {

        AdapterEndpointManager adapterEndpointManager = new AdapterEndpointManager();
        ADAPTER_API_LEVEL level = adapterEndpointManager.getApiVersion(sServiceName);

        return InternalExchangeManager.getInstance().getEndpointURL(sHomeCommunityId, sServiceName, level);
    }

    public static List<OrganizationType> getGatewayAllOrganizations() throws ExchangeManagerException {
        return ExchangeManager.getInstance().getAllOrganizations();
    }

}
