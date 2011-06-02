/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.adapter.deferred.queue.gui;

import com.sun.webui.jsf.model.Option;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import com.sun.rave.web.ui.appbase.AbstractSessionBean;
import java.util.ArrayList;
import java.util.List;
import javax.faces.FacesException;

/**
 *
 * @author richard.ettema
 */
public class UserSession extends AbstractSessionBean {

    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    private boolean authenticationServiceAvail;

    public boolean isAuthenticationServiceAvail() {
        return authenticationServiceAvail;
    }

    public void setAuthenticationServiceAvail(boolean authenticationServiceAvail) {
        this.authenticationServiceAvail = authenticationServiceAvail;
    }

    private void _init() throws Exception {
    }

    /** Creates a new instance of UserSession */
    public UserSession() {
    }

    /**
     * <p>This method is called when this bean is initially added to
     * session scope.  Typically, this occurs as a result of evaluating
     * a value binding or method binding expression, which utilizes the
     * managed bean facility to instantiate this bean and store it into
     * session scope.</p>
     *
     * <p>You may customize this method to initialize and cache data values
     * or resources that are required for the lifetime of a particular
     * user session.</p>
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
            log("UserSession Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }

        // </editor-fold>
        // Perform application initialization that must complete
        // *after* managed components are initialized
        // TODO - add your own initialization code here
    }

    /**
     * <p>This method is called when the session containing it is about to be
     * passivated.  Typically, this occurs in a distributed servlet container
     * when the session is about to be transferred to a different
     * container instance, after which the <code>activate()</code> method
     * will be called to indicate that the transfer is complete.</p>
     *
     * <p>You may customize this method to release references to session data
     * or resources that can not be serialized with the session itself.</p>
     */
    @Override
    public void passivate() {
    }

    /**
     * <p>This method is called when the session containing it was
     * reactivated.</p>
     *
     * <p>You may customize this method to reacquire references to session
     * data or resources that could not be serialized with the
     * session itself.</p>
     */
    @Override
    public void activate() {
    }

    /**
     * <p>This method is called when this bean is removed from
     * session scope.  Typically, this occurs as a result of
     * the session timing out or being terminated by the application.</p>
     *
     * <p>You may customize this method to clean up resources allocated
     * during the execution of the <code>init()</code> method, or
     * at any later time during the lifetime of the application.</p>
     */
    @Override
    public void destroy() {
    }
    private List<AsyncMsgRecord> processQueueResults;
    private List<AsyncMsgRecord> unProcessQueueResults;
    private List<Option> statusItems;

    public List<AsyncMsgRecord> getProcessQueueResults() {
        if (processQueueResults == null) {
            processQueueResults = new ArrayList<AsyncMsgRecord>();
        }
        return processQueueResults;
    }

    public void setProcessQueueResults(List<AsyncMsgRecord> processQueueResults) {
        this.processQueueResults = processQueueResults;
    }

    public List<AsyncMsgRecord> getUnProcessQueueResults() {
        if (unProcessQueueResults == null) {
            unProcessQueueResults = new ArrayList<AsyncMsgRecord>();
        }
        return unProcessQueueResults;
    }

    public void setUnProcessQueueResults(List<AsyncMsgRecord> unProcessQueueResults) {
        this.unProcessQueueResults = unProcessQueueResults;
    }

    public List<Option> getStatusItems() {
        if (statusItems == null) {
            statusItems = new ArrayList<Option>();
        }
        return statusItems;
    }

    public void setStatusItems(List<Option> statusItems) {
        this.statusItems = statusItems;
    }
}
