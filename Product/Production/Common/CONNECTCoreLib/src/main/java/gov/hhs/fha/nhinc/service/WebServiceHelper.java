package gov.hhs.fha.nhinc.service;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
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

    private LoggingContextHelper loggingContextHelper = new LoggingContextHelper();
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
     * @return The return value of the method.
     * @throws IllegalAccessException Exceptions thrown by invoke - passed on.
     * @throws InvocationTargetException Exceptions thrown by invoke - passed on.
     */
    protected Object invokeTheMethod(Method oMethod, Object webOrchObject, Object operationInput, Object assertion)
            throws IllegalAccessException, InvocationTargetException
    {
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
    protected Object invokeTheMethod(Method oMethod, Object webOrchObject, Object operationInput)
            throws IllegalAccessException, InvocationTargetException
    {
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
     * @param context The web service context used to initialize the assertion
     * @return The response object from the web orchestrator invocation
     * @throws Exception Any exceptions are passed back up.
     */
    public Object invokeSecureWebService(Object webOrchObject, Class webOrchClass, String methodName, Object operationInput, WebServiceContext context) throws Exception
    {

        Object oResponse = null;

        try
        {
            loggingContextHelper.setContext(context);

            // Collect assertion
            AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
            log.debug("Initializing the SAML assertion information: " + assertion);

            // Extract the message id value from the WS-Addressing Header and
            // place it in the Assertion Class
            if (assertion != null)
            {
                String messageId = AsyncMessageIdExtractor.GetAsyncMessageId(context);
                if (messageId != null && !messageId.isEmpty())
                {
                    log.debug("Setting messageId: " + messageId);
                    assertion.setMessageId(messageId);
                }
                List<String> relatesToList = assertion.getRelatesToList();
                if (relatesToList != null)
                {
                    relatesToList.addAll(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
                    StringBuffer sBuf = new StringBuffer("Setting relatesToList: ");
                    for (String relatesToItem : relatesToList)
                    {
                        sBuf.append(relatesToItem + " ");
                    }
                    log.debug(sBuf.toString());
                }
            }

            Method oMethod = getMethod(webOrchClass, methodName);
            log.debug("Invoke " + webOrchClass + "." + methodName + " with " + operationInput);
            oResponse = invokeTheMethod(oMethod, webOrchObject, operationInput, assertion);

        } catch (IllegalArgumentException e)
        {
            String sErrorMessage = "The method was called with incorrect arguments. " +
                    "This assumes that the method should have exactly one request" +
                    "argument and the assertion object. " +
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
            loggingContextHelper.clearContext();
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
    public Object invokeSecureDeferredResponseWebService(Object webOrchObject, Class webOrchClass, String methodName, Object operationInput, WebServiceContext context) throws Exception
    {

        Object oResponse = null;

        try
        {
            loggingContextHelper.setContext(context);

            // Collect assertion
            AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
            log.debug("Initializing the SAML assertion information: " + assertion);

            // Extract the relatesTo value from the WS-Addressing Header and
            // place it in the Assertion Class as the messageId
            if (assertion != null)
            {

                List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
                if (relatesToList != null && !relatesToList.isEmpty())
                {
                    StringBuffer sBuf = new StringBuffer("Extracting relatesToList: ");
                    for (String relatesToItem : relatesToList)
                    {
                        sBuf.append(relatesToItem + " ");
                    }
                    log.debug(sBuf.toString());

                    log.debug("Setting messageId to first relatesToId: " + relatesToList.get(0));
                    assertion.setMessageId(relatesToList.get(0));
                }
            }

            Method oMethod = getMethod(webOrchClass, methodName);
            log.debug("Invoke " + webOrchClass + "." + methodName + " with " + operationInput);
            oResponse = invokeTheMethod(oMethod, webOrchObject, operationInput, assertion);

        } catch (IllegalArgumentException e)
        {
            String sErrorMessage = "The method was called with incorrect arguments. " +
                    "This assumes that the method should have exactly one request" +
                    "argument and the assertion object. " +
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
            loggingContextHelper.clearContext();
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
     * @param operationInput The parameters for the web orchestrator method
     * @param context The web service context used to initialize the assertion
     * @return The response object from the web orchestrator invocation
     * @throws Exception Any exceptions are passed back up.
     */
    public Object invokeDeferredResponseWebService(Object webOrchObject, Class webOrchClass, String methodName, AssertionType assertion, Object operationInput, WebServiceContext context) throws Exception
    {

        Object oResponse = null;

        try
        {
            loggingContextHelper.setContext(context);

            // Extract the relatesTo value from the WS-Addressing Header and
            // place it in the Assertion Class as the messageId
            if (assertion != null)
            {

                List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
                if (relatesToList != null && !relatesToList.isEmpty())
                {

                    StringBuffer sBuf = new StringBuffer("Extracting relatesToList: ");
                    for (String relatesToItem : relatesToList)
                    {
                        sBuf.append(relatesToItem + " ");
                    }
                    log.debug(sBuf.toString());

                    log.debug("Setting messageId to first relatesToId: " + relatesToList.get(0));
                    assertion.setMessageId(relatesToList.get(0));
                }
            }
            Method oMethod = getMethod(webOrchClass, methodName);
            log.debug("Invoke " + webOrchClass + "." + methodName + " with " + operationInput);
            oResponse = invokeTheMethod(oMethod, webOrchObject, operationInput);

        } catch (IllegalArgumentException e)
        {
            String sErrorMessage = "The method was called with incorrect arguments. " +
                    "This assumes that the method should have exactly one request" +
                    "argument and the assertion object. " +
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
            loggingContextHelper.clearContext();
        }
        return oResponse;
    }
}
