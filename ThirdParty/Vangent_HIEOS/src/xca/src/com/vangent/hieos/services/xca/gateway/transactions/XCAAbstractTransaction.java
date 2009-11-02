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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vangent.hieos.services.xca.gateway.transactions;

import com.vangent.hieos.xutil.services.framework.ContentValidationService;
import com.vangent.hieos.xutil.services.framework.XBaseTransaction;
import com.vangent.hieos.xutil.services.framework.XAbstractService;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;
import com.vangent.hieos.xutil.response.Response;

// Exceptions.
import com.vangent.hieos.xutil.exception.SchemaValidationException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;

import com.vangent.hieos.services.xca.gateway.controller.XCARequestController;
import com.vangent.hieos.xutil.atna.XATNALogger;

// Third-party.
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import org.apache.axiom.om.OMElement;

/**
 *
 * @author Bernie Thuman
 */
abstract public class XCAAbstractTransaction extends XBaseTransaction {
    private final static Logger logger = Logger.getLogger(XCAAbstractTransaction.class);

    // So the gateway transaction can operate in either mode.
    public enum GatewayType {

        InitiatingGateway, RespondingGateway, Unknown
    };

    private ContentValidationService validater;
    private XAbstractService service;
    private XCARequestController requestController = null;
    private GatewayType gatewayType = GatewayType.Unknown;  // Default.
    private boolean errorDetected = false;

    // Subclass responsibilities:
    abstract void prepareValidRequests(OMElement request) throws XdsInternalException;

    abstract void validateRequest(OMElement request);

    abstract boolean consolidateResponses(ArrayList<OMElement> allResponses) throws XdsInternalException;

    /**
     * 
     * @return
     */
    public GatewayType getGatewayType() {
        return this.gatewayType;
    }

    /**
     * 
     * @param log_message
     * @param response
     * @param messageContext
     */
    protected void init(GatewayType gatewayType, XLogMessage log_message, Response response, MessageContext messageContext) {
        this.gatewayType = gatewayType;
        this.log_message = log_message;
        this.requestController = new XCARequestController(response, this.log_message);
        super.init(response, XBaseTransaction.xds_b, messageContext); // Initialize superclass whole.
    }

    /**
     * 
     * @param request
     * @param validater
     * @param service
     */
    public OMElement run(OMElement request, ContentValidationService validater, XAbstractService service) throws SchemaValidationException, XdsInternalException {
        this.validater = validater;
        this.service = service;
        try {
            validateRequest(request);     // Concrete class responsibility.
            if (response.has_errors()) {
                return response.getResponse();      // Get out early.
            }
            run(request);  // Do the real work.
        } catch (XdsException e) {
            // FIXME (Repository/Registry).
            response.add_error(MetadataSupport.XDSRepositoryError, e.getMessage(),
                    this.getClass().getName(), log_message);
            logger.fatal(logger_exception_details(e));
        }

        OMElement finalResponse = null;
        try {
            finalResponse = response.getResponse();  // This should only be called once.
        } catch (XdsInternalException e) {
            logger.fatal(logger_exception_details(e));
            log_message.addErrorParam("Internal Error", "Error generating response from XCA");
        }
        this.log_response();
        if (this.errorDetected == true)
        {
            log_message.setPass(false);  // Force this for internal errors detected.
        }
        return finalResponse;
    }

    /**
     *
     * @param request
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private void run(OMElement request) throws XdsInternalException {
        prepareValidRequests(request);
        ArrayList<OMElement> allResponses = requestController.sendRequests();
        boolean atLeastOneSuccess = consolidateResponses(allResponses);
        if (response.has_errors() && atLeastOneSuccess) {
            // This implies that we were able to successfully make at least one request.
            response.forcePartialSuccessStatus();
        }
    }

    /**
     *
     * @param ATNAtxn
     * @param request
     * @param endpoint
     * @param successFlag
     * @param actor
     */
    protected void performAudit(String ATNAtxn, OMElement request, String endpoint, XATNALogger.OutcomeIndicator outcome, XATNALogger.ActorType actorType) {
        try {
            XATNALogger xATNALogger = new XATNALogger(ATNAtxn, actorType);
            xATNALogger.performAudit(request, endpoint, outcome);
        } catch (Exception e) {
            // Eat exception.
            logger.error("Could not perform ATNA audit", e);
        }
    }

    /**
     *
     * @param errorString
     */
    protected void logError(String errorString) {
        this.errorDetected = true;  // Make note of a problem.
        try {
            log_message.addErrorParam("Errors", errorString);
            logger.error(errorString);
        } catch (Exception e) {
            // We really need to ignore this exception to avoid indirect recursion.
            logger.error("XCA ERROR: Could not log info", e);
        }
    }

    /**
     * 
     * @param logLabel
     * @param infoString
     */
    protected void logInfo(String logLabel, String infoString) {
        try {
            log_message.addOtherParam(logLabel, infoString);
            logger.info(logLabel + " : " + infoString);
        } catch (Exception e) {
            logger.error("XCA ERROR: Could not log info", e);
        }
    }

    /**
     *
     * @return
     */
    protected XCARequestController getRequestController() {
        return this.requestController;
    }
}
