/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.adapter.cppgui;

import com.sun.rave.web.ui.appbase.AbstractSessionBean;
import gov.hhs.fha.nhinc.adapter.cppgui.valueobject.PatientVO;
import java.util.List;
import javax.faces.FacesException;

/**
 * <p>
 * Session scope data bean for your application. Create properties here to represent cached data that should be made
 * available across multiple HTTP requests for an individual user.
 * </p>
 *
 * <p>
 * An instance of this class will be created for you automatically, the first time your application evaluates a value
 * binding expression or method binding expression that references a managed bean using this class.
 * </p>
 *
 */
public class UserSession extends AbstractSessionBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

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

    /**
     * <p>
     * Automatically managed component initialization. <strong>WARNING:</strong> This method is automatically generated,
     * so any user-specified code inserted here is subject to being replaced.
     * </p>
     */
    private void _init() throws Exception {
    }

    // </editor-fold>

    /**
     * <p>
     * Construct a new session data bean instance.
     * </p>
     */
    public UserSession() {
    }

    /**
     * <p>
     * This method is called when this bean is initially added to session scope. Typically, this occurs as a result of
     * evaluating a value binding or method binding expression, which utilizes the managed bean facility to instantiate
     * this bean and store it into session scope.
     * </p>
     *
     * <p>
     * You may customize this method to initialize and cache data values or resources that are required for the lifetime
     * of a particular user session.
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
            log("SessionBean1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }

        // </editor-fold>
        // Perform application initialization that must complete
        // *after* managed components are initialized
        // TODO - add your own initialization code here
    }

    /**
     * <p>
     * This method is called when the session containing it is about to be passivated. Typically, this occurs in a
     * distributed servlet container when the session is about to be transferred to a different container instance,
     * after which the <code>activate()</code> method will be called to indicate that the transfer is complete.
     * </p>
     *
     * <p>
     * You may customize this method to release references to session data or resources that can not be serialized with
     * the session itself.
     * </p>
     */
    @Override
    public void passivate() {
    }

    /**
     * <p>
     * This method is called when the session containing it was reactivated.
     * </p>
     *
     * <p>
     * You may customize this method to reacquire references to session data or resources that could not be serialized
     * with the session itself.
     * </p>
     */
    @Override
    public void activate() {
    }

    /**
     * <p>
     * This method is called when this bean is removed from session scope. Typically, this occurs as a result of the
     * session timing out or being terminated by the application.
     * </p>
     *
     * <p>
     * You may customize this method to clean up resources allocated during the execution of the <code>init()</code>
     * method, or at any later time during the lifetime of the application.
     * </p>
     */
    @Override
    public void destroy() {
    }

    private List<PatientVO> searchResults;

    public List<PatientVO> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<PatientVO> searchResults) {
        this.searchResults = searchResults;
    }

    private PatientVO patient;

    public PatientVO getPatient() {
        return patient;
    }

    public void setPatient(PatientVO patient) {
        this.patient = patient;
    }

    private String selectedPolicyOID;

    public String getSelectedPolicyOID() {
        return selectedPolicyOID;
    }

    public void setSelectedPolicyOID(String selectedPolicyOID) {
        this.selectedPolicyOID = selectedPolicyOID;
    }

}
