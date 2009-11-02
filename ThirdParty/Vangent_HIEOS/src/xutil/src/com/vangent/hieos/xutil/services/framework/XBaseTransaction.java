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

import com.vangent.hieos.xutil.response.Response;
import com.vangent.hieos.xutil.atna.XATNALogger;

import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.exception.XdsFormatException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;

/**
 *
 * @author thumbe
 */
public class XBaseTransaction {

    private final static Logger logger = Logger.getLogger(XBaseTransaction.class);
    /**
     *
     */
    public Response response = null;
    /**
     *
     */
    public XLogMessage log_message = null;
    /**
     *
     */
    public static final short xds_none = 0;
    /**
     *
     */
    public static final short xds_a = 2;
    /**
     *
     */
    public static final short xds_b = 3;
    /**
     *
     */
    public short xds_version = xds_none;
    MessageContext messageContext = null;

    //Added these 2 boolean values for success flag which is needed to be sent to audit message
    /**
     *
     */
    protected final static boolean SUCCESS = true;
    /**
     * 
     */
    protected final static boolean FAILURE = false;

    /* BHT: Removed:
    static {
    BasicConfigurator.configure();
    }
     */
    /**
     *
     * @return
     */
    public MessageContext getMessageContext() {
        return messageContext;
    }

    /**
     *
     * @param response
     * @param xds_version
     * @param messageContext
     */
    protected void init(Response response, short xds_version, MessageContext messageContext) {
        this.response = response;
        this.xds_version = xds_version;
        this.messageContext = messageContext;
    }

    /**
     *
     * @return
     */
    public boolean getStatus() {
        return !response.has_errors();
    }

    /**
     *
     */
    protected void log_status() {
        try {
            String e_and_w = response.getErrorsAndWarnings();
            if (e_and_w != null && !e_and_w.equals("")) {
                log_message.addErrorParam("Error", e_and_w);
            }
        } catch (Exception e) {
            response.error("Internal Error: cannot set final status in test log on transaction");
        }
    }

    /**
     *
     */
    protected void init_log() {
    }

    /**
     *
     * @param e
     * @return
     */
    public static String logger_exception_details(Exception e) {
        if (e == null) {
            return "";
        }
        return e.getClass().getName() + "  " + e.getMessage() + ExceptionUtil.exception_details(e, 10);
    }

    /**
     *
     */
    protected void log_response() {
        if (log_message == null) {
            System.out.println("\nFATAL ERROR: XdsCommon.log_response(): log_message is null\n");
            return;
        }
        try {
            if (response.has_errors()) {
                log_message.setPass(false);
                log_message.addErrorParam("Errors", response.getErrorsAndWarnings());
            } else {
                log_message.setPass(true);
            }

            log_message.addOtherParam("Response", response.getResponse().toString());
        } catch (XdsInternalException e) {
            System.out.println("**************ERROR: Internal exception attempting to return to user");
        }
    }

    /**
     *
     * @throws com.vangent.hieos.xutil.exception.XdsFormatException
     */
    protected void mustBeSimpleSoap() throws XdsFormatException {
        if (getMessageContext().isDoingMTOM()) {
            throw new XdsFormatException("This transaction must use SIMPLE SOAP, MTOM found");
        }
    }

    /**
     *
     * @throws com.vangent.hieos.xutil.exception.XdsFormatException
     */
    protected void mustBeMTOM() throws XdsFormatException {
        if (!getMessageContext().isDoingMTOM()) {
            throw new XdsFormatException("This transaction must use MTOM, SIMPLE SOAP found");
        }
    }

    /**
     *  This is the method which calls XAtnaLogger class to pass the audit messages
     * This is a good place to put this kind of logic since this is a super class
     * The inherited classes should have to just call this method from where ever they want to do audit
     * @param transactionNumber
     * @param metadata
     * @param targetEndpoint
     * @param outcome 
     * @param actor
     */
    protected void performAudit(String transactionNumber, OMElement metadata, String targetEndpoint, XATNALogger.ActorType actor, XATNALogger.OutcomeIndicator outcome) {
        try {
            //Instantiate the audit class
            XATNALogger xATNALogger = new XATNALogger(transactionNumber, actor);
            //get the endpoint from message context
            //AxisEndpoint axisEndPoint = (AxisEndpoint) messageContext.getProperty("endpoint");
            //String endPoint = axisEndPoint.getEndpointURL();
            //get the remote address from where the call was made
            //String fromAddress = (String) messageContext.getProperty("REMOTE_ADDR");
            //Call the audit method which does the actual logic of generating and storing messages

            xATNALogger.performAudit(metadata, targetEndpoint, outcome);
        } catch (Exception e) {
            logger.error("Could not perform ATNA audit", e);
        }
    }
}
