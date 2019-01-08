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
package deferredqueuemanagergui;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.DropDown;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Tab;
import com.sun.webui.jsf.component.TabSet;
import com.sun.webui.jsf.component.TextField;
import com.sun.webui.jsf.model.Option;
import gov.hhs.fha.nhinc.adapter.deferred.queue.PatientDiscoveryDeferredReqQueueClient;
import gov.hhs.fha.nhinc.adapter.deferred.queue.gui.UserSession;
import gov.hhs.fha.nhinc.adapter.deferred.queue.gui.servicefacade.DeferredQueueManagerFacade;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.gateway.adapterpatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.util.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.FacesException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and
 * initialization code) for all components that you have defined on this page, as well as lifecycle methods and event
 * handlers where you may add behavior to respond to incoming events.
 * </p>
 *
 * @version ManageQueue.java
 * @version Created on May 13, 2011, 11:55:16 PM
 *
 * @author richard.ettema,narendra.reddy
 */
public class ManageQueue extends AbstractPageBean {

    private static final Logger LOG = LoggerFactory.getLogger(ManageQueue.class);
    private static final String PATIENT_DISCOVERY = "PatientDiscovery";
    private static final String DATE_PARSING_ERROR
        = "Unable to parse given input dates, please recheck the given dates and retry with the sample format(MMDDYYYY HH:MM:SS)";
    private TabSet processTabSet = new TabSet();
    private Tab processQueueTab = new Tab();
    private Tab unProcessQueueTab = new Tab();
    private StaticText errorMessages = new StaticText();
    private TextField startCreationDate = new TextField();
    private TextField stopCreationDate = new TextField();
    private String errors;
    private StaticText messageId = new StaticText();
    private StaticText serviceName = new StaticText();
    private StaticText userInfo = new StaticText();
    private Button processButton = new Button();
    private DropDown status = new DropDown();

    private void _init() throws Exception {
    }

    public TabSet getProcessTabSet() {
        return processTabSet;
    }

    public void setProcessTabSet(TabSet ts) {
        processTabSet = ts;
    }

    public Tab getProcessQueueTab() {
        return processQueueTab;
    }

    public void setProcessQueueTab(Tab processQueueTab) {
        this.processQueueTab = processQueueTab;
    }

    public Tab getUnProcessQueueTab() {
        return unProcessQueueTab;
    }

    public void setUnProcessQueueTab(Tab unProcessQueueTab) {
        this.unProcessQueueTab = unProcessQueueTab;
    }

    public StaticText getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(StaticText st) {
        errorMessages = st;
    }

    public TextField getStartCreationDate() {
        return startCreationDate;
    }

    public void setStartCreationDate(TextField startCreationDate) {
        this.startCreationDate = startCreationDate;
    }

    public TextField getStopCreationDate() {
        return stopCreationDate;
    }

    public void setStopCreationDate(TextField stopCreationDate) {
        this.stopCreationDate = stopCreationDate;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public StaticText getMessageId() {
        return messageId;
    }

    public void setMessageId(StaticText messageId) {
        this.messageId = messageId;
    }

    public StaticText getServiceName() {
        return serviceName;
    }

    public void setServiceName(StaticText serviceName) {
        this.serviceName = serviceName;
    }

    public Button getProcessButton() {
        return processButton;
    }

    public void setProcessButton(Button processButton) {
        this.processButton = processButton;
    }

    public StaticText getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(StaticText userInfo) {
        this.userInfo = userInfo;
    }

    public DropDown getStatus() {
        return status;
    }

    public void setStatus(DropDown status) {
        this.status = status;
    }

    /**
     * Creates a new instance of ManageQueue
     */
    public ManageQueue() {
    }

    /**
     * <p>
     * Callback method that is called whenever a page is navigated to, either directly via a URL, or indirectly via page
     * navigation. Customize this method to acquire resources that will be needed for event handlers and lifecycle
     * methods, whether or not this page is performing post back processing.
     * </p>
     * <p>
     * <p>
     * Note that, if the current request is a postback, the property values of the components do <strong>not</strong>
     * represent any values submitted with this request. Instead, they represent the property values that were saved for
     * this view when it was rendered.
     * </p>
     */
    @Override
    public void init() {
        // Perform initializations inherited from our superclass
        super.init();
        // Perform application initialization that must complete
        // *before* managed components are initialized
        // TODO - add your own initialiation code here

        // <editor-fold defaultstate="collapsed" desc="Managed Component Initialization">
        // Initialize automatically managed components
        // *Note* - this logic should NOT be modified
        try {
            _init();
        } catch (Exception e) {
            log("ManageQueue Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }

        // </editor-fold>
        // Perform application initialization that must complete
        // *after* managed components are initialized
        // TODO - add your own initialization code here
    }

    /**
     * <p>
     * Callback method that is called after the component tree has been restored, but before any event processing takes
     * place. This method will <strong>only</strong> be called on a postback request that is processing a form submit.
     * Customize this method to allocate resources that will be required in your event handlers.
     * </p>
     */
    @Override
    public void preprocess() {
    }

    /**
     * <p>
     * Callback method that is called just before rendering takes place. This method will <strong>only</strong> be
     * called for the page that will actually be rendered (and not, for example, on a page that handled a postback and
     * then navigated to a different page). Customize this method to allocate resources that will be required for
     * rendering this page.
     * </p>
     */
    @Override
    public void prerender() {
    }

    /**
     * <p>
     * Callback method that is called after rendering is completed for this request, if <code>init()</code> was called
     * (regardless of whether or not this was the page that was actually rendered). Customize this method to release
     * resources acquired in the <code>init()</code>, <code>preprocess()</code>, or <code>prerender()</code> methods (or
     * acquired during execution of an event handler).
     * </p>
     */
    @Override
    public void destroy() {
    }

    public String processQueueTab_action() {
        // Process the action. Return value is a navigation
        // case name where null will return to the same page.
        errorMessages.setText("");
        return null;
    }

    public String unProcessQueueTab_action() {
        // Process the action. Return value is a navigation
        // case name where null will return to the same page.
        errorMessages.setText("");
        DeferredQueueManagerFacade deferredQueueManagerFacade = new DeferredQueueManagerFacade();
        List<Option> deferredQueueStatuses = deferredQueueManagerFacade.queryForDeferredQueueStatuses();
        UserSession userSession = (UserSession) getBean("UserSession");
        userSession.setStatusItems(null); // reset to null to force lazy load
        userSession.getStatusItems().addAll(deferredQueueStatuses);
        return null;
    }

    public String retrieveProcessButtonAction() {
        // Process the action. Return value is a navigation
        // case name where null will return to the same page.
        errorMessages.setText("");
        DeferredQueueManagerFacade deferredQueueManagerFacade = new DeferredQueueManagerFacade();

        List<AsyncMsgRecord> processQueueResults = deferredQueueManagerFacade.queryForDeferredQueueProcessing();

        if (processQueueResults == null || processQueueResults.isEmpty()) {
            errorMessages.setText("No records found to process.");
        }

        UserSession userSession = (UserSession) getBean("UserSession");
        userSession.setProcessQueueResults(null); // reset to null to force lazy load
        userSession.getProcessQueueResults().addAll(processQueueResults);

        return null;
    }

    public String retrieveUnProcessButton_action() {
        // Process the action. Return value is a navigation
        // case name where null will return to the same page.

        errorMessages.setText("");

        if (!isDateSearchCriteriaValid()) {
            LOG.error("Error Message: " + errors);
            errorMessages.setText(errors);
            return null;
        }

        String startCreationTime = (String) startCreationDate.getText();
        String stopCreationTime = (String) stopCreationDate.getText();
        String statusValue = (String) status.getValue();

        Date startDate = getDate(startCreationTime);
        Date stopDate = getDate(stopCreationTime);
        if (startDate == null || stopDate == null) {
            return null;
        }
        DeferredQueueManagerFacade deferredQueueManagerFacade = new DeferredQueueManagerFacade();

        List<AsyncMsgRecord> unProcessQueueResults;
        if (StringUtils.isBlank(startCreationTime) && StringUtils.isBlank(stopCreationTime) && StringUtils.
            isBlank(statusValue)) {
            unProcessQueueResults = deferredQueueManagerFacade.queryForDeferredQueueSelected();
        } else {
            unProcessQueueResults = deferredQueueManagerFacade.queryBySearchCriteria(startDate, stopDate, statusValue);
        }

        if (unProcessQueueResults == null || unProcessQueueResults.isEmpty()) {
            errorMessages.setText("No records found to process.");
        }

        UserSession userSession = (UserSession) getBean("UserSession");
        userSession.setUnProcessQueueResults(null); // reset to null to force lazy load
        userSession.getUnProcessQueueResults().addAll(unProcessQueueResults);

        return null;
    }

    private boolean isDateSearchCriteriaValid() {
        StringBuffer message = new StringBuffer();
        boolean isValid = true;

        if (startCreationDate == null || stopCreationDate == null) {
            message.append("Earliest Date and Most Recent Date should not be null");
            isValid = false;
        }

        errors = message.toString();

        return isValid;
    }

    public String process_action(javax.faces.event.ActionEvent event) {
        String asyncMsgId = (String) messageId.getText();
        String serviceName = (String) this.serviceName.getText();
        PatientDiscoveryDeferredReqQueueProcessResponseType pdResponse;

        if (serviceName.trim().equals(PATIENT_DISCOVERY)) {
            PatientDiscoveryDeferredReqQueueClient pdClient = new PatientDiscoveryDeferredReqQueueClient();
            pdResponse = pdClient.processPatientDiscoveryDeferredReqQueue(asyncMsgId);
            gov.hhs.fha.nhinc.gateway.adapterpatientdiscoveryreqqueueprocess.SuccessOrFailType sfpd = pdResponse
                .getSuccessOrFail();
            if (sfpd.isSuccess()) {
                userInfo.setText("Succesfully Patient Discovery Deferred Response Msg got Processed.");
            } else {
                errorMessages.setText(
                    "Unable to process the Patient Discovery Deferred Response, Please contact system administrator for further details.");
            }
        }

        return null;
    }

    private Date getDate(String dateString) {
        String datetime = dateString == null ? "" : dateString;
        try {
            Calendar cal1 = Format.getCalendarInstance(Format.MMDDYYYYHHMMSS_DATEFORMAT, datetime);
            if (cal1 == null) {
                errorMessages.setText(DATE_PARSING_ERROR);
                return null;
            }
            return cal1.getTime();
        } catch (Exception ex) {
            LOG.error("Error Message: {}", ex.getMessage(), ex);
            errorMessages.setText(DATE_PARSING_ERROR);
            return null;
        }
    }
}
