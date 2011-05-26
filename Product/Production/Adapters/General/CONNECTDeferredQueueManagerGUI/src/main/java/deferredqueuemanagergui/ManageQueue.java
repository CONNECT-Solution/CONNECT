/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package deferredqueuemanagergui;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Tab;
import com.sun.webui.jsf.component.TabSet;
import com.sun.webui.jsf.component.TextField;
import gov.hhs.fha.nhinc.adapter.deferred.queue.PatientDiscoveryDeferredReqQueueClient;
import gov.hhs.fha.nhinc.adapter.deferred.queue.QueryForDocumentsDeferredReqQueueClient;
import gov.hhs.fha.nhinc.adapter.deferred.queue.RetrieveDocumentsDeferredReqQueueClient;
import gov.hhs.fha.nhinc.adapter.deferred.queue.gui.UserSession;
import gov.hhs.fha.nhinc.adapter.deferred.queue.gui.servicefacade.DeferredQueueManagerFacade;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.gateway.entitydocqueryreqqueueprocess.DocQueryDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.gateway.entitydocretrievereqqueueprocess.DocRetrieveDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.gateway.entitypatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.util.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.component.html.HtmlSelectOneRadio;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @version ManageQueue.java
 * @version Created on May 13, 2011, 11:55:16 PM
 *
 * @author richard.ettema,narendra.reddy
 */
public class ManageQueue extends AbstractPageBean {

    private static Log log = LogFactory.getLog(ManageQueue.class);
    private static final String PATIENT_DISCOVERY = "PatientDiscovery";
    private static final String QUERY_FOR_DOCUMENT = "QueryForDocument";
    private static final String RETRIEVE_DOCUMENT = "RetrieveDocument";

    private void _init() throws Exception {
    }
    private TabSet processTabSet = new TabSet();

    public TabSet getProcessTabSet() {
        return processTabSet;
    }

    public void setProcessTabSet(TabSet ts) {
        this.processTabSet = ts;
    }
    private Tab processQueueTab = new Tab();
    private Tab unProcessQueueTab = new Tab();

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
    private HtmlSelectOneRadio autoEnabled = new HtmlSelectOneRadio();

    public HtmlSelectOneRadio getAutoEnabled() {
        return autoEnabled;
    }

    public void setAutoEnabled(HtmlSelectOneRadio hsor) {
        this.autoEnabled = hsor;
    }
    private StaticText errorMessages = new StaticText();

    public StaticText getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(StaticText st) {
        this.errorMessages = st;
    }
    private TextField startCreationDate = new TextField();
    private TextField stopCreationDate = new TextField();

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
    private String errors;

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
    private StaticText messageId = new StaticText();
    private StaticText serviceName = new StaticText();

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

    /** Creates a new instance of ManageQueue */
    public ManageQueue() {
    }

    /**
     * <p>Callback method that is called whenever a page is navigated to,
     * either directly via a URL, or indirectly via page navigation.
     * Customize this method to acquire resources that will be needed
     * for event handlers and lifecycle methods, whether or not this
     * page is performing post back processing.</p>
     *
     * <p>Note that, if the current request is a postback, the property
     * values of the components do <strong>not</strong> represent any
     * values submitted with this request.  Instead, they represent the
     * property values that were saved for this view when it was rendered.</p>
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
     * <p>Callback method that is called after the component tree has been
     * restored, but before any event processing takes place.  This method
     * will <strong>only</strong> be called on a postback request that
     * is processing a form submit.  Customize this method to allocate
     * resources that will be required in your event handlers.</p>
     */
    @Override
    public void preprocess() {
    }

    /**
     * <p>Callback method that is called just before rendering takes place.
     * This method will <strong>only</strong> be called for the page that
     * will actually be rendered (and not, for example, on a page that
     * handled a postback and then navigated to a different page).  Customize
     * this method to allocate resources that will be required for rendering
     * this page.</p>
     */
    @Override
    public void prerender() {
    }

    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called (regardless of whether
     * or not this was the page that was actually rendered).  Customize this
     * method to release resources acquired in the <code>init()</code>,
     * <code>preprocess()</code>, or <code>prerender()</code> methods (or
     * acquired during execution of an event handler).</p>
     */
    @Override
    public void destroy() {
    }

    public String processQueueTab_action() {
        // Process the action. Return value is a navigation
        // case name where null will return to the same page.
        this.errorMessages.setText("");
        return null;
    }

    public String unProcessQueueTab_action() {
        // Process the action. Return value is a navigation
        // case name where null will return to the same page.
        this.errorMessages.setText("");
        return null;
    }

    public String retrieveProcessButton_action() throws Exception {
        // Process the action. Return value is a navigation
        // case name where null will return to the same page.
        this.errorMessages.setText("");

        DeferredQueueManagerFacade deferredQueueManagerFacade = new DeferredQueueManagerFacade();

        List<AsyncMsgRecord> processQueueResults = deferredQueueManagerFacade.queryForDeferredQueueProcessing();

        if (processQueueResults == null || processQueueResults.size() == 0) {
            this.errorMessages.setText("No records found to process.");
        }



        UserSession userSession = (UserSession) getBean("UserSession");
        userSession.setProcessQueueResults(null); // reset to null to force lazy load
        userSession.getProcessQueueResults().addAll(processQueueResults);

        return null;
    }

    public String retrieveUnProcessButton_action() throws Exception {
        // Process the action. Return value is a navigation
        // case name where null will return to the same page.

        this.errorMessages.setText("");

        if (!isDateSearchCriteriaValid()) {
            log.error("Error Message: " + errors);
            this.errorMessages.setText(errors);
            return null;
        }

        Date d1 = new Date();
        Date d2 = new Date();
        String startCreationTime = "";
        String stopCreationTime = "";
        try {
            startCreationTime = (String) startCreationDate.getText();
            stopCreationTime = (String) stopCreationDate.getText();
            if (startCreationTime == null) {
                startCreationTime = "";
            }
            if (stopCreationTime == null) {
                stopCreationTime = "";
            }
            Calendar cal1 = Format.getCalendarInstance(Format.MMDDYYYYHHMMSS_DATEFORMAT, startCreationTime);
            Calendar cal2 = Format.getCalendarInstance(Format.MMDDYYYYHHMMSS_DATEFORMAT, stopCreationTime);

            if (cal1 != null) {
                d1 = cal1.getTime();
            }
            if (cal2 != null) {
                d2 = cal2.getTime();
            }
        } catch (Exception ex) {
            log.error("Error Message: " + ex);
            this.errorMessages.setText("Unable to parse given input dates, please recheck the date formats and retry agian... ");
            return null;
        }
        DeferredQueueManagerFacade deferredQueueManagerFacade = new DeferredQueueManagerFacade();

        List<AsyncMsgRecord> unProcessQueueResults = null;

        if ((startCreationTime.equals("") && stopCreationTime.equals(""))) {
            unProcessQueueResults = deferredQueueManagerFacade.queryForDeferredQueueSelected();
        } else {
            unProcessQueueResults = deferredQueueManagerFacade.queryByCreationStartAndStopTime(d1, d2);
        }

        if (unProcessQueueResults == null || unProcessQueueResults.size() == 0) {
            this.errorMessages.setText("No records found to process.");
        }

        UserSession userSession = (UserSession) getBean("UserSession");
        userSession.setUnProcessQueueResults(null); // reset to null to force lazy load
        userSession.getUnProcessQueueResults().addAll(unProcessQueueResults);

        return null;
    }

    private boolean isDateSearchCriteriaValid() {
        StringBuffer message = new StringBuffer();
        boolean isValid = true;

        if (this.startCreationDate == null || this.stopCreationDate == null) {
            message.append("Earliest Date and Most Recent Date should not be null");
            isValid = false;
        } else if (this.startCreationDate == null || this.getStopCreationDate() == null) {
            message.append("Earliest Date and Most Recent Date should not be null");
            isValid = false;
        } //else if (this.startCreationDate.after(this.getStopCreationDate())) {
        //message.append("Earliest Date should not be after Most Recent Date");
        //isValid = false;
        //}

        errors = message.toString();

        return isValid;
    }

    public String process_action(javax.faces.event.ActionEvent event) throws Exception {

        String asyncMsgId = (String) this.messageId.getText();
        String serviceName = (String) this.serviceName.getText();
        if (serviceName.trim().equals(PATIENT_DISCOVERY)) {
            PatientDiscoveryDeferredReqQueueClient pdClient = new PatientDiscoveryDeferredReqQueueClient();
            PatientDiscoveryDeferredReqQueueProcessResponseType response = pdClient.processPatientDiscoveryDeferredReqQueue(asyncMsgId);
        } else if (serviceName.trim().equals(QUERY_FOR_DOCUMENT)) {
            QueryForDocumentsDeferredReqQueueClient qdClient = new QueryForDocumentsDeferredReqQueueClient();
            DocQueryDeferredReqQueueProcessResponseType response = qdClient.processDocQueryDeferredReqQueue(asyncMsgId);
        } else if (serviceName.trim().equals(RETRIEVE_DOCUMENT)) {
            RetrieveDocumentsDeferredReqQueueClient rdClient = new RetrieveDocumentsDeferredReqQueueClient();
            DocRetrieveDeferredReqQueueProcessResponseType response = rdClient.processDocRetrieveDeferredReqQueue(asyncMsgId);
        }

        return null;
    }
}
