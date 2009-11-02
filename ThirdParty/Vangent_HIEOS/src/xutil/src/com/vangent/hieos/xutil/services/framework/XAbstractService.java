/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vangent.hieos.xutil.services.framework;

import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.exception.XdsWSException;
import com.vangent.hieos.xutil.response.AdhocQueryResponse;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
//import com.vangent.hieos.xutil.registry.Properties;
import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.response.RegistryErrorList;
import com.vangent.hieos.xutil.response.RegistryResponse;
import com.vangent.hieos.xutil.response.RetrieveMultipleResponse;
import com.vangent.hieos.xutil.services.framework.Fields;
import com.vangent.hieos.xutil.xlog.client.XLogger;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;
import com.vangent.hieos.xutil.xconfig.XConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.TransportHeaders;
//import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

// Axis2 LifeCycle support:
import org.apache.axis2.engine.ServiceLifeCycle;
import org.apache.axis2.service.Lifecycle;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;

// XATNALogger
import com.vangent.hieos.xutil.atna.XATNALogger;

/**
 *
 * @author NIST
 * @author Bernie Thuman (BHT) - Comments, lifecycle logging, streamlining.
 */
public class XAbstractService implements ServiceLifeCycle, Lifecycle {

    protected XLogMessage log_message = null;
    public static short registry_actor = 1;
    public static short repository_actor = 2;
    private final static Logger logger = Logger.getLogger(XAbstractService.class);
    protected String service_name;
    boolean is_secure;
    protected MessageContext return_message_context = null;


    /*static {
    BasicConfigurator.configure();
    }*/
    /**
     *
     * @return
     */
    protected boolean isSecure() {
        return is_secure;
    }

    /**
     *
     * @return
     */
    protected MessageContext getMessageContext() {
        return MessageContext.getCurrentMessageContext();
    }

    /**
     *
     * @param return_context
     */
    public void setReturnMessageContext(MessageContext return_context) {
        this.return_message_context = return_context;
    }

    /**
     *
     */
    public void useXop() {
        this.return_message_context = MessageContext.getCurrentMessageContext();
        if (return_message_context != null) {
            return_message_context.getOptions().setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
        }
    }

    /**
     *
     * @param service_name
     * @param request
     * @param actor
     * @return
     */
    protected OMElement beginTransaction(String service_name, OMElement request, short actor) {

        // This gets around a bug in Leopard (MacOS X 10.5) on Macs
        System.setProperty("http.nonProxyHosts", "");
        this.service_name = service_name;
        String remoteIP = (String) this.getMessageContext().getProperty(MessageContext.REMOTE_ADDR);
        logger.info("Start " + service_name + " : " + remoteIP + " : " + getMessageContext().getTo().toString());
        startTestLog();
        XLogger xlogger = XLogger.getInstance();
        log_message = xlogger.getNewMessage(remoteIP);

        // Log basic parameters:
        log_message.addOtherParam(Fields.service, service_name);
        is_secure = getMessageContext().getTo().toString().indexOf("https://") != -1;
        log_message.addHTTPParam(Fields.isSecure, (is_secure) ? "true" : "false");
        log_message.addHTTPParam(Fields.date, getDateTime());
        log_message.setSecureConnection(is_secure);
        if (request != null) {
            log_message.addOtherParam("Request", request.toString());
        } else {
            log_message.addErrorParam("Error", "Cannot access request body in XdsService.begin_service()");
            return start_up_error(request, null, actor, "Request body is null");
        }

        // Log HTTP header:
        TransportHeaders transportHeaders = (TransportHeaders) getMessageContext().getProperty("TRANSPORT_HEADERS");
        for (Object o_key : transportHeaders.keySet()) {
            String key = (String) o_key;
            String value = (String) transportHeaders.get(key);
            Vector<String> thdrs = new Vector<String>();
            thdrs.add(key + " : " + value);
            addHttp("HTTP Header", thdrs);
        }

        // Log SOAP header:
        if (getMessageContext().getEnvelope().getHeader() != null) {
            try {
                addSoap("Soap Header", getMessageContext().getEnvelope().getHeader().toStringWithConsume());
            } catch (OMException e) {
            } catch (XMLStreamException e) {
            }
        }

        // Log SOAP envelope:
        if (getMessageContext().getEnvelope().getBody() != null) {
            try {
                addSoap("Soap Envelope", getMessageContext().getEnvelope().toStringWithConsume());
            } catch (OMException e) {
            } catch (XMLStreamException e) {
            }
        }
        log_message.addHTTPParam(Fields.fromIpAddress, remoteIP);
        log_message.addHTTPParam(Fields.endpoint, getMessageContext().getTo().toString());

        return null;  // No error.
    }

    /**
     *
     * @param status
     */
    protected void endTransaction(boolean status) {
        logger.info("End " + service_name + " " +
                ((log_message == null) ? "null" : log_message.getMessageID()) + " : " +
                ((status) ? "Pass" : "Fail"));

        stopTestLog();
    }

    /**
     *
     * @param request
     * @param e
     * @param actor
     * @param message
     * @return
     */
    protected OMElement endTransaction(OMElement request, Exception e, short actor, String message) {
        if (message == null || message.equals("")) {
            message = e.getMessage();
        }
        logger.error("Exception thrown while processing web service request", e);
        endTransaction(false);
        return start_up_error(request, e, actor, message);
    }

    /**
     *
     * @param request
     * @param e
     * @param actor
     * @param message
     * @return
     */
    protected OMElement start_up_error(OMElement request, Exception e, short actor, String message) {
        return start_up_error(request, e, actor, message, false);
    }

    /**
     *
     * @param request
     * @param e
     * @param actor
     * @param message
     * @param log
     * @return
     */
    public OMElement start_up_error(OMElement request, Object e, short actor, String message, boolean log) {
        String error_type = (actor == registry_actor) ? MetadataSupport.XDSRegistryError : MetadataSupport.XDSRepositoryError;
        try {
            String request_type = (request != null) ? request.getLocalName() : "None";
            OMNamespace ns = (request != null) ? request.getNamespace() : MetadataSupport.ebRSns2;

            if (ns.getNamespaceURI().equals(MetadataSupport.ebRSns2.getNamespaceURI())) {
                // xds.a submitobjectsrequest
                RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_2, log);
                rel.add_error(error_type, message, exception_details(e), log_message);
                return new RegistryResponse(RegistryErrorList.version_2, rel).getResponse();
            }
            if (ns.getNamespaceURI().equals(MetadataSupport.ebRSns3.getNamespaceURI())) {
                // xds.b submitobjectsrequest (could be xds.b retrieve)
                RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3, log);
                rel.add_error(error_type, message, exception_details(e), log_message);
                return new RegistryResponse(RegistryErrorList.version_3, rel).getResponse();
            }
            if (ns.getNamespaceURI().equals(MetadataSupport.xdsB.getNamespaceURI())) {
                // RetrieveDocumentSet
                RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3, log);
                rel.add_error(error_type, message, exception_details(e), log_message);
                if (request.getLocalName().equals("RetrieveDocumentSetRequest")) {
                    OMElement res = new RetrieveMultipleResponse(rel).getResponse();
                    return res;
                } else {
                    return new RegistryResponse(RegistryErrorList.version_3, rel).getResponse();
                }

            }
            if (ns.getNamespaceURI().equals(MetadataSupport.ebQns3.getNamespaceURI())) {
                // stored query
                RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3, log);
                rel.add_error(error_type, message, exception_details(e), log_message);
                return new AdhocQueryResponse(RegistryErrorList.version_3, rel).getResponse();
            }
            if (ns.getNamespaceURI().equals(MetadataSupport.ebQns2.getNamespaceURI())) {
                // sql query
                RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_2, log);
                rel.add_error(error_type, message, exception_details(e), log_message);
                return new AdhocQueryResponse(RegistryErrorList.version_2, rel).getResponse();
            }

            // the default when all else fails
            RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_2, log);
            rel.add_error(error_type, message, exception_details(e), log_message);
            return new RegistryResponse(RegistryErrorList.version_2, rel).getResponse();

        } catch (XdsInternalException e1) {
            try {
                RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_2, log);
                rel.add_error(error_type, e1.getMessage(), exception_details(e1), log_message);
                return new RegistryResponse(RegistryErrorList.version_2, rel).getResponse();
            } catch (Exception e2) {
                return null;
            }
        }

    }

    /**
     *
     */
    protected void startTestLog() {
        //logger.info("+++ start log [service = " + service_name + "] +++");
    }

    /**
     * Stop the test log facility.
     */
    protected void stopTestLog() {
        if (log_message != null) {
            log_message.store();
            log_message = null;
        }
    //logger.info("+++ stop log [service = " + service_name + "] +++");
    }

    /**
     *
     * @param e
     * @return
     */
    protected String exception_details(Object e) {
        if (e == null) {
            return "No Additional Details Available";
        }
        if (e instanceof Exception) {
            return exception_details((Exception) e);
        }
        if (e instanceof String) {
            return exception_details((String) e);
        }
        return exception_details(e.toString());
    }

    /**
     *
     * @param e
     * @return
     */
    protected String exception_details(Exception e) {
        return ExceptionUtil.exception_details(e);
    }

    /**
     *
     * @param e
     * @return
     */
    protected String exception_details(String e) {
        return e;
    }

    /**
     *
     * @param title
     * @param t
     */
    private void addHttp(String title, Vector<String> t) {
        StringBuffer buffer = new StringBuffer();
        for (String s : t) {
            buffer.append(s + "  ");
        }
        log_message.addHTTPParam(title, buffer.toString());
    }

    /**
     *
     * @param t
     * @param s
     */
    private void addSoap(String t, String s) {
        log_message.addSOAPParam(t, s);
    }

    /**
     *
     * @param s
     */
    protected void addError(String s) {
        log_message.addErrorParam("Error", s);
    }

    /**
     *
     * @param name
     * @param s
     */
    protected void addOther(String name, String s) {
        log_message.addOtherParam(name, s);
    }

    /**
     *
     * @param inMessage
     */
    public void setMessageContextIn(MessageContext inMessage) {
        //currentMessageContext = inMessage ;
    }

    /**
     *
     * @return
     */
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     *
     * @throws com.vangent.hieos.xutil.exception.XdsWSException
     */
    protected void checkSOAP12() throws XdsWSException {
        if (MessageContext.getCurrentMessageContext().isSOAP11()) {
            throwFault("SOAP 1.1 not supported");
        }
        SOAPEnvelope env = MessageContext.getCurrentMessageContext().getEnvelope();
        if (env == null) {
            throwFault("No SOAP envelope found");
        }
        SOAPHeader hdr = env.getHeader();
        if (hdr == null) {
            throwFault("No SOAP header found");
        }
        if (!hdr.getChildrenWithName(new QName("http://www.w3.org/2005/08/addressing", "Action")).hasNext()) {
            throwFault("WS-Action required in header");
        }
    }

    /**
     *
     * @throws com.vangent.hieos.xutil.exception.XdsWSException
     */
    protected void checkSOAP11() throws XdsWSException {

        if (!MessageContext.getCurrentMessageContext().isSOAP11()) {
            throwFault("SOAP 1.2 not supported");
        }
        SOAPEnvelope env = MessageContext.getCurrentMessageContext().getEnvelope();
        if (env == null) {
            throwFault("No SOAP envelope found");
        }
    }

    /**
     *
     * @throws com.vangent.hieos.xutil.exception.XdsWSException
     */
    protected void checkSOAPAny() throws XdsWSException {
        if (MessageContext.getCurrentMessageContext().isSOAP11()) {
            checkSOAP11();
        } else {
            checkSOAP12();
        }
    }

    /**
     *
     * @param msg
     * @throws com.vangent.hieos.xutil.exception.XdsWSException
     */
    private void throwFault(String msg) throws XdsWSException {
        if (log_message != null) {
            log_message.addErrorParam("SOAPError", msg);
            log_message.addOtherParam("Response", "SOAPFault: " + msg);
            endTransaction(false);
        }
        throw new XdsWSException(msg);
    }

    /**
     *
     * @return
     */
    protected boolean isAsync() {
        MessageContext mc = getMessageContext();
        return mc.getMessageID() != null &&
                !mc.getMessageID().equals("") &&
                mc.getReplyTo() != null &&
                !mc.getReplyTo().hasAnonymousAddress();
    }

    /**
     *
     * @return
     */
    boolean isSync() {
        return !isAsync();
    }

    // BHT (Added AXIS2 LifeCycle methods - can be overridden by subclasses.
    /**
     * This is called when a new instance of the implementing class has been created.
     * This occurs in sync with session/ServiceContext creation. This method gives classes
     * a chance to do any setup work (grab resources, establish connections, etc) before
     * they are invoked by a service request.
     */
    public void init(ServiceContext serviceContext) throws AxisFault {
        logger.info("XdsService:::init() - NOOP (not overridden)");
    }

    /**
     * This is called when Axis2 decides that it is finished with a particular instance
     * of the back-end service class. It allows classes to clean up resources.
     */
    public void destroy(ServiceContext serviceContext) {
        logger.info("XdsService:::destroy() - NOOP (not overridden)");
    }

    /**
     * This will be called during the deployment time of the service.
     * Irrespective of the service scope this method will be called
     */
    public void startUp(ConfigurationContext configctx, AxisService service) {
        logger.info("XdsService:::startUp() - NOOP (not overridden)");
    }

    /**
     * This will be called during the system shut down time. Irrespective
     * of the service scope this method will be called
     */
    public void shutDown(ConfigurationContext configctx, AxisService service) {
        logger.info("XdsService:::shutDown() - NOOP (not overridden)");
    }

    /**
     *
     * @param actorType
     */
    public void ATNAlogStop(XATNALogger.ActorType actorType) {
        try {
            XATNALogger xATNALogger = new XATNALogger(XATNALogger.TXN_STOP, actorType);
            xATNALogger.performAudit(null, null, XATNALogger.OutcomeIndicator.SUCCESS);
        } catch (Exception e) {
            logger.error("Could not perform ATNA audit", e);
        }
    }

    /**
     *
     * @param actorType
     */
    public void ATNAlogStart(XATNALogger.ActorType actorType) {
        try {
            XATNALogger xATNALogger = new XATNALogger(XATNALogger.TXN_START, actorType);
            xATNALogger.performAudit(null, null, XATNALogger.OutcomeIndicator.SUCCESS);
        } catch (Exception e) {
            logger.error("*** Internal Error occured in XdsService::ATNAlogStop() method ***", e);
        }
    }
}
