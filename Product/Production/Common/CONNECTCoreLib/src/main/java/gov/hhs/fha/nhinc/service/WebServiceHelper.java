/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.service;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.nhinclib.LoggingContextHelper;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Method;
import java.util.List;
import javax.xml.ws.WebServiceContext;

/**
 * This class is used as a helper in each of the Web Services. The template work
 * for the web service is encapsulated in this helper class.
 *
 */
public class WebServiceHelper
{

    private LoggingContextHelper loggingContextHelper = null;
    private Log log = null;

    /**
     * Default constructor, creates the logger instance.
     */
    public WebServiceHelper()
    {
        log = createLogger();
    }

    /**
     * Create a logger object.
     *
     * @return The logger object.
     */
    protected Log createLogger()
    {
        if (log == null)
        {
            log = LogFactory.getLog(getClass());
        }
        return log;
    }

    /**
     * This method will return the reflection method object for the
     * given class and methodName.  
     *
     * @param webOrchClass  The class containing the method.
     * @param methodName The name of the method to find.
     * @return The Method object for that method.
     */
    protected Method getMethod(Class webOrchClass, String methodName)
    {
        Method oReturnMethod = null;

        // Note that there is an assumption here for what we are working on
        // that names of methods are unique and there is no overloading
        // of method names.   We are looking only by method name.  Since
        // these are specifically for web services - we are fine because
        // the method names are unique there.
        //---------------------------------------------------------------
        log.debug("Locating reflection method " + webOrchClass + "." + methodName);
        Method[] oaMethod = webOrchClass.getDeclaredMethods();
        for (Method oMethod : oaMethod)
        {
            if (oMethod.getName().equals(methodName))
            {
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
     * @param webOrchObject The instance of the object.
     * @param operationInput The input parameter for the method.
     * @param assertion The assertion object as extracted from the web context
     * @param targets The NHIN target communities
     * @param urlInfo The url information
     * @return The return value of the method.
     * @throws IllegalAccessException Exceptions thrown by invoke - passed on.
     * @throws InvocationTargetException Exceptions thrown by invoke - passed on.
     */
    protected Object invokeTheMethod4(Method oMethod, Object webOrchObject,
            Object operationInput, AssertionType assertion, Object targets, Object urlInfo)
            throws IllegalAccessException, InvocationTargetException
    {
        log.debug("Invoke with " + operationInput + " assertion " + assertion + " targets " + targets + " url " + urlInfo);
        return oMethod.invoke(webOrchObject, operationInput, assertion, targets, urlInfo);
    }

    /**
     * This method is used to invoke a method using reflection.  This method's
     * primary purpose is to allow us to override this for unit testing purposes
     * and simualate an execption to test that code.
     *
     * @param oMethod The reflection method object.
     * @param webOrchObject The instance of the object.
     * @param operationInput The input parameter for the method.
     * @param assertion The assertion object as extracted from the web context
     * @param targets The NHIN target communities
     * @return The return value of the method.
     * @throws IllegalAccessException Exceptions thrown by invoke - passed on.
     * @throws InvocationTargetException Exceptions thrown by invoke - passed on.
     */
    protected Object invokeTheMethod3(Method oMethod, Object webOrchObject,
            Object operationInput, AssertionType assertion, Object targets)
            throws IllegalAccessException, InvocationTargetException
    {
        log.debug("Invoke with " + operationInput + " assertion " + assertion + " targets " + targets);
        return oMethod.invoke(webOrchObject, operationInput, assertion, targets);
    }

    /**
     * This method is used to invoke a method using reflection.  This method's
     * primary purpose is to allow us to override this for unit testing purposes
     * and simualate an execption to test that code.
     *
     * @param oMethod The reflection method object.
     * @param webOrchObject The instance of the object.
     * @param operationInput The input parameter for the method.
     * @param assertion The assertion object as extracted from the web context
     * @return The return value of the method.
     * @throws IllegalAccessException Exceptions thrown by invoke - passed on.
     * @throws InvocationTargetException Exceptions thrown by invoke - passed on.
     */
    protected Object invokeTheMethod2(Method oMethod, Object webOrchObject,
            Object operationInput, AssertionType assertion)
            throws IllegalAccessException, InvocationTargetException
    {
        log.debug("Invoke with " + operationInput + " assertion " + assertion);
        return oMethod.invoke(webOrchObject, operationInput, assertion);
    }

    /**
     * This method is used to invoke a method using reflection.  This method's
     * primary purpose is to allow us to override this for unit testing purposes
     * and simualate an execption to test that code.
     *
     * @param oMethod The reflection method object.
     * @param webOrchObject The instance of the object.
     * @param operationInput The input parameter for the method.
     * @return The return value of the method.
     * @throws IllegalAccessException Exceptions thrown by invoke - passed on.
     * @throws InvocationTargetException Exceptions thrown by invoke - passed on.
     */
    protected Object invokeTheMethod1(Method oMethod, Object webOrchObject, Object operationInput)
            throws IllegalAccessException, InvocationTargetException
    {
        log.debug("Invoke with " + operationInput);
        return oMethod.invoke(webOrchObject, operationInput);
    }

    /**
     * This method will establish the context logging, create the Assertion
     * object including SAML information and the WS-Addressing messageId and
     * relatesTo information, invoke the orchestration method, and then clear
     * the logging context.
     *
     * @param webOrchObject The instance of the web orchestrator
     * @param webOrchClass The class for the web orchestrator
     * @param methodName The web orchestrator method to call
     * @param operationInput The parameters for the web orchestrator method
     * @param targets The nhin targets
     * @param context The web service context used to initialize the assertion
     * @return The response object from the web orchestrator invocation
     * @throws Exception Any exceptions are passed back up.
     */
    public Object invokeSecureWebService(Object webOrchObject, Class webOrchClass,
            String methodName, Object operationInput, Object targets,
            WebServiceContext context) throws Exception
    {
        Integer numParam = new Integer(3);
        return handleInvokeSecureWebService(webOrchObject, webOrchClass,
                methodName, operationInput, targets, null, context, numParam);
    }

    /**
     * This method will establish the context logging, create the Assertion
     * object including SAML information and the WS-Addressing messageId and
     * relatesTo information, invoke the orchestration method, and then clear
     * the logging context.
     *
     * @param webOrchObject The instance of the web orchestrator
     * @param webOrchClass The class for the web orchestrator
     * @param methodName The web orchestrator method to call
     * @param operationInput The parameters for the web orchestrator method
     * @param targets The nhin targets
     * @param urlInfo The URL information
     * @param context The web service context used to initialize the assertion
     * @return The response object from the web orchestrator invocation
     * @throws Exception Any exceptions are passed back up.
     */
    public Object invokeSecureWebService(Object webOrchObject, Class webOrchClass,
            String methodName, Object operationInput, Object targets, Object urlInfo,
            WebServiceContext context) throws Exception
    {
        Integer numParam = new Integer(4);
        return handleInvokeSecureWebService(webOrchObject, webOrchClass,
                methodName, operationInput, targets, urlInfo, context, numParam);
    }

    /**
     * This method will establish the context logging, create the Assertion
     * object including SAML information and the WS-Addressing messageId and
     * relatesTo information, invoke the orchestration method, and then clear
     * the logging context.
     *
     * @param webOrchObject The instance of the web orchestrator
     * @param webOrchClass The class for the web orchestrator
     * @param methodName The web orchestrator method to call
     * @param operationInput The parameters for the web orchestrator method
     * @param targets The nhin targets
     * @param urlInfo The URL information
     * @param context The web service context used to initialize the assertion
     * @param numParam Allows control over the number of parameters in the invocation
     * @return The response object from the web orchestrator invocation
     * @throws Exception Any exceptions are passed back up.
     */
    private Object handleInvokeSecureWebService(Object webOrchObject, Class webOrchClass,
            String methodName, Object operationInput, Object targets, Object urlInfo,
            WebServiceContext context, Integer numParam) throws Exception
    {

        Object oResponse = null;

        try
        {
            getLoggingContextHelper().setContext(context);

            // Collect assertion
            AssertionType assertion = getSamlAssertion(context);

            // Extract the message id value from the WS-Addressing Header and
            // place it in the Assertion Class
            String contextMessageId = getMessageId(context);
            populateAssertionWithMessageId(assertion, contextMessageId);

            // Extract the relatesTo values from the WS-Addressing Header and
            // place them in the Assertion Class
            List<String> contextRelatesTo = getRelatesToList(context);
            populateAssertionWithRelatesToList(assertion, contextRelatesTo);

            Method oMethod = getMethod(webOrchClass, methodName);

            if (numParam == 4)
            {
                oResponse = invokeTheMethod4(oMethod, webOrchObject, operationInput, assertion, targets, urlInfo);
            } else
            {
                oResponse = invokeTheMethod3(oMethod, webOrchObject, operationInput, assertion, targets);
            }

        } catch (IllegalArgumentException e)
        {
            String sErrorMessage = "The method was called with incorrect arguments. " +
                    "Exception: " + e.getMessage();
            log.error(sErrorMessage, e);
            throw e;
        } catch (Exception e)
        {
            // As near as we can tell based on the way we are using this, I do not
            // believe there is any other exception we will see - but we want to
            // log them if we see them.
            //---------------------------------------------------------------------
            String sErrorMessage = "An unexpected exception occurred of type: " +
                    e.getClass().getCanonicalName() + ". Exception: " +
                    e.getMessage();
            log.error(sErrorMessage, e);
            throw e;

        } finally
        {
            getLoggingContextHelper().clearContext();
        }
        return oResponse;
    }

    /**
     * This method will establish the context logging, create the Assertion
     * object including SAML information and the WS-Addressing messageId and
     * relatesTo information, invoke the orchestration method, and then clear
     * the logging context.
     *
     * @param webOrchObject The instance of the web orchestrator
     * @param webOrchClass The class for the web orchestrator
     * @param methodName The web orchestrator method to call
     * @param assertion Assertion is not pulled from the contex in the unsecure scenario
     * @param operationInput The parameters for the web orchestrator method
     * @param targets The nhin targets
     * @param context The web service context used to initialize the assertion
     * @return The response object from the web orchestrator invocation
     * @throws Exception Any exceptions are passed back up.
     */
    public Object invokeUnsecureWebService(Object webOrchObject, Class webOrchClass,
            String methodName, Object operationInput, AssertionType assertion,
            Object targets, WebServiceContext context) throws Exception
    {
        Integer numParam = new Integer(3);
        return handleInvokeUnsecureWebService(webOrchObject, webOrchClass,
                methodName, operationInput, assertion, targets, null, context, numParam);
    }

    /**
     * This method will establish the context logging, create the Assertion
     * object including SAML information and the WS-Addressing messageId and
     * relatesTo information, invoke the orchestration method, and then clear
     * the logging context.
     *
     * @param webOrchObject The instance of the web orchestrator
     * @param webOrchClass The class for the web orchestrator
     * @param methodName The web orchestrator method to call
     * @param assertion Assertion is not pulled from the contex in the unsecure scenario
     * @param operationInput The parameters for the web orchestrator method
     * @param targets The nhin targets
     * @param urlInfo The URL information
     * @param context The web service context used to initialize the assertion
     * @return The response object from the web orchestrator invocation
     * @throws Exception Any exceptions are passed back up.
     */
    public Object invokeUnsecureWebService(Object webOrchObject, Class webOrchClass,
            String methodName, Object operationInput, AssertionType assertion,
            Object targets, Object urlInfo, WebServiceContext context) throws Exception
    {
        Integer numParam = new Integer(4);
        return handleInvokeUnsecureWebService(webOrchObject, webOrchClass,
                methodName, operationInput, assertion, targets, urlInfo, context, numParam);

    }

    /**
     * This method will establish the context logging, create the Assertion
     * object including SAML information and the WS-Addressing messageId and
     * relatesTo information, invoke the orchestration method, and then clear
     * the logging context.
     *
     * @param webOrchObject The instance of the web orchestrator
     * @param webOrchClass The class for the web orchestrator
     * @param methodName The web orchestrator method to call
     * @param assertion Assertion is not pulled from the contex in the unsecure scenario
     * @param operationInput The parameters for the web orchestrator method
     * @param targets The nhin targets
     * @param urlInfo The URL information
     * @param context The web service context used to initialize the assertion
     * @param numParam The number of paramteters in the invocation
     * @return The response object from the web orchestrator invocation
     * @throws Exception Any exceptions are passed back up.
     */
    private Object handleInvokeUnsecureWebService(Object webOrchObject, Class webOrchClass,
            String methodName, Object operationInput, AssertionType assertion, Object targets,
            Object urlInfo, WebServiceContext context, Integer numParam) throws Exception
    {

        Object oResponse = null;

        try
        {
            getLoggingContextHelper().setContext(context);

            // Extract the message id value from the WS-Addressing Header and
            // place it in the Assertion Class
            String contextMessageId = getMessageId(context);
            populateAssertionWithMessageId(assertion, contextMessageId);

            // Extract the relatesTo values from the WS-Addressing Header and
            // place them in the Assertion Class
            List<String> contextRelatesTo = getRelatesToList(context);
            populateAssertionWithRelatesToList(assertion, contextRelatesTo);

            Method oMethod = getMethod(webOrchClass, methodName);

            if (numParam == 4)
            {
                oResponse = invokeTheMethod4(oMethod, webOrchObject, operationInput, assertion, targets, urlInfo);
            } else
            {
                oResponse = invokeTheMethod3(oMethod, webOrchObject, operationInput, assertion, targets);
            }

        } catch (IllegalArgumentException e)
        {
            String sErrorMessage = "The method was called with incorrect arguments. " +
                    "Exception: " + e.getMessage();
            log.error(sErrorMessage, e);
            throw e;
        } catch (Exception e)
        {
            // As near as we can tell based on the way we are using this, I do not
            // believe there is any other exception we will see - but we want to
            // log them if we see them.
            //---------------------------------------------------------------------
            String sErrorMessage = "An unexpected exception occurred of type: " +
                    e.getClass().getCanonicalName() + ". Exception: " +
                    e.getMessage();
            log.error(sErrorMessage, e);
            throw e;

        } finally
        {
            getLoggingContextHelper().clearContext();
        }
        return oResponse;
    }

    /**
     * This method will establish the context logging, create the Assertion
     * object including SAML information and the WS-Addressing messageId from the
     * relatesTo information, invoke the orchestration method, and then clear
     * the logging context.
     *
     * @param webOrchObject The instance of the web orchestrator
     * @param webOrchClass The class for the web orchestrator
     * @param methodName The web orchestrator method to call
     * @param operationInput The parameters for the web orchestrator method
     * @param context The web service context used to initialize the assertion
     * @return The response object from the web orchestrator invocation
     * @throws Exception Any exceptions are passed back up.
     */
    public Object invokeSecureDeferredResponseWebService(Object webOrchObject,
            Class webOrchClass, String methodName, Object operationInput,
            WebServiceContext context) throws Exception
    {

        Object oResponse = null;

        try
        {
            getLoggingContextHelper().setContext(context);

            // Collect assertion
            AssertionType assertion = getSamlAssertion(context);

            // Extract the relatesTo values from the WS-Addressing Header
            List<String> contextRelatesTo = getRelatesToList(context);

            // place the first one from the list into the Assertion Class as the message id
            if (contextRelatesTo != null && !contextRelatesTo.isEmpty())
            {
                log.debug("Setting messageId to first relatesToId: " + contextRelatesTo.get(0));
                String messageId = contextRelatesTo.get(0);
                populateAssertionWithMessageId(assertion, messageId);
            }

            Method oMethod = getMethod(webOrchClass, methodName);

            oResponse = invokeTheMethod2(oMethod, webOrchObject, operationInput, assertion);

        } catch (IllegalArgumentException e)
        {
            String sErrorMessage = "The method was called with incorrect arguments. " +
                    "Exception: " + e.getMessage();
            log.error(sErrorMessage, e);
            throw e;
        } catch (Exception e)
        {
            // As near as we can tell based on the way we are using this, I do not
            // believe there is any other exception we will see - but we want to
            // log them if we see them.
            //---------------------------------------------------------------------
            String sErrorMessage = "An unexpected exception occurred of type: " +
                    e.getClass().getCanonicalName() + ". Exception: " +
                    e.getMessage();
            log.error(sErrorMessage, e);
            throw e;

        } finally
        {
            getLoggingContextHelper().clearContext();
        }
        return oResponse;
    }

    /**
     * This method will establish the context logging, create the WS-Addressing
     * messageId from the relatesTo information, invoke the orchestration method,
     * and then clear the logging context.
     *
     * @param webOrchObject The instance of the web orchestrator
     * @param webOrchClass The class for the web orchestrator
     * @param methodName The web orchestrator method to call
     * @param assertion Assertion is not pulled from the contex in the unsecure scenario
     * @param operationInput The parameters for the web orchestrator method
     * @param context The web service context used to initialize the assertion
     * @return The response object from the web orchestrator invocation
     * @throws Exception Any exceptions are passed back up.
     */
    public Object invokeDeferredResponseWebService(Object webOrchObject,
            Class webOrchClass, String methodName, AssertionType assertion,
            Object operationInput, WebServiceContext context) throws Exception
    {

        Object oResponse = null;

        try
        {
            getLoggingContextHelper().setContext(context);

            // Extract the relatesTo values from the WS-Addressing Header
            List<String> contextRelatesTo = getRelatesToList(context);

            // place the first one from the list into the Assertion Class as the message id
            if (contextRelatesTo != null && !contextRelatesTo.isEmpty())
            {
                log.debug("Setting messageId to first relatesToId: " + contextRelatesTo.get(0));
                String messageId = contextRelatesTo.get(0);
                populateAssertionWithMessageId(assertion, messageId);
            }

            Method oMethod = getMethod(webOrchClass, methodName);
            oResponse = invokeTheMethod1(oMethod, webOrchObject, operationInput);

        } catch (IllegalArgumentException e)
        {
            String sErrorMessage = "The method was called with incorrect arguments. " +
                    "Exception: " + e.getMessage();
            log.error(sErrorMessage, e);
            throw e;
        } catch (Exception e)
        {
            // As near as we can tell based on the way we are using this, I do not
            // believe there is any other exception we will see - but we want to
            // log them if we see them.
            //---------------------------------------------------------------------
            String sErrorMessage = "An unexpected exception occurred of type: " +
                    e.getClass().getCanonicalName() + ". Exception: " +
                    e.getMessage();
            log.error(sErrorMessage, e);
            throw e;

        } finally
        {
            getLoggingContextHelper().clearContext();
        }
        return oResponse;
    }

    /**
     * Extracts the SAML information from the context to populate the Assertion object
     * 
     * @param context The web service context used to initialize the assertion
     * @return The assertion object populated with SAML information
     */
    protected AssertionType getSamlAssertion(WebServiceContext context)
    {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        log.debug("Initializing the SAML assertion information: " + assertion);
        return assertion;
    }

    /**
     * Extracts the message id from the web service context
     * @param context The web service context that contains the message id
     * @return the extracted message id
     */
    protected String getMessageId(WebServiceContext context)
    {
        String messageId = AsyncMessageIdExtractor.GetAsyncMessageId(context);
        log.debug("Extracted messageId: " + messageId + " from the context");
        return messageId;
    }

    /**
     * Sets the given messageId into the assertion
     * @param assertion The assertion object to populate with the given message id
     * @param messageId The message id
     */
    protected void populateAssertionWithMessageId(AssertionType assertion, String messageId)
    {
        if (assertion != null)
        {
            if (messageId != null && !messageId.isEmpty())
            {
                log.debug("Setting assertion messageId: " + messageId);
                assertion.setMessageId(messageId);
            }
        }
    }

    /**
     * Extracts the relatesTo ids from the web service context
     * @param context The web service context that contains the ids
     * @return the extracted list of relatesTo ids
     */
    protected List<String> getRelatesToList(WebServiceContext context)
    {
        List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
        StringBuffer sBuf = new StringBuffer("Extracting relatesToList: ");
        for (String relatesToItem : relatesToList)
        {
            sBuf.append(relatesToItem + " ");
        }
        sBuf.append("from the context");
        log.debug(sBuf.toString());
        return relatesToList;
    }

    /**
     * Sets the given list of relatesTo ids into the assertion
     * @param assertion The assertion object to populate with the given relatesTo ids
     * @param relatesToIds The list of the relatesTo ids
     */
    protected void populateAssertionWithRelatesToList(AssertionType assertion, List<String> relatesToIds)
    {
        if (assertion != null)
        {
            List<String> relatesToList = assertion.getRelatesToList();
            if (relatesToList != null && relatesToIds != null)
            {
                relatesToList.addAll(relatesToIds);
                StringBuffer sBuf = new StringBuffer("Setting assertion relatesToList: ");
                for (String relatesToItem : relatesToList)
                {
                    sBuf.append(relatesToItem + " ");
                }
                log.debug(sBuf.toString());
            }
        }
    }

    /**
     * Accessor for the Logging context helper object, creates this object on first need.
     * @return instance of the LoggingContextHelper
     */
    protected LoggingContextHelper getLoggingContextHelper()
    {
        if (loggingContextHelper == null)
        {
            loggingContextHelper = new LoggingContextHelper();
        }
        return loggingContextHelper;
    }
}
