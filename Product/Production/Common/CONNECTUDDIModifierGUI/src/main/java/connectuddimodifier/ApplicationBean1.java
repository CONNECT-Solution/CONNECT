/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
//********************************************************************
// FILE: ApplicationBean1.java
//
// 2009 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: ApplicationBean1
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY: //
//> 22OCT09 D. Cannon
// Initial Coding.
//<
//********************************************************************

package connectuddimodifier;

import com.sun.rave.web.ui.appbase.AbstractApplicationBean;
import javax.faces.FacesException;

/**
 * <p>Application scope data bean for your application.  Create properties
 *  here to represent cached data that should be made available to all users
 *  and pages in the application.</p>
 *
 * <p>An instance of this class will be created for you automatically,
 * the first time your application evaluates a value binding expression
 * or method binding expression that references a managed bean using
 * this class.</p>
 *
 * @version ApplicationBean1.java
 * @version Created on Oct 20, 2009, 10:19:32 AM
 * @author dcannon
 */

public class ApplicationBean1 extends AbstractApplicationBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }
    // </editor-fold>

    /**
     * <p>Construct a new application data bean instance.</p>
     */
    public ApplicationBean1() {
    }

    /**
     * <p>This method is called when this bean is initially added to
     * application scope.  Typically, this occurs as a result of evaluating
     * a value binding or method binding expression, which utilizes the
     * managed bean facility to instantiate this bean and store it into
     * application scope.</p>
     * 
     * <p>You may customize this method to initialize and cache application wide
     * data values (such as the lists of valid options for dropdown list
     * components), or to allocate resources that are required for the
     * lifetime of the application.</p>
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
            log("ApplicationBean1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e: new FacesException(e);
        }
        
        // </editor-fold>
        // Perform application initialization that must complete
        // *after* managed components are initialized
        // TODO - add your own initialization code here
    }

    /**
     * <p>This method is called when this bean is removed from
     * application scope.  Typically, this occurs as a result of
     * the application being shut down by its owning container.</p>
     * 
     * <p>You may customize this method to clean up resources allocated
     * during the execution of the <code>init()</code> method, or
     * at any later time during the lifetime of the application.</p>
     */
    @Override
    public void destroy() {
    }

    /**
     * <p>Return an appropriate character encoding based on the
     * <code>Locale</code> defined for the current JavaServer Faces
     * view.  If no more suitable encoding can be found, return
     * "UTF-8" as a general purpose default.</p>
     *
     * <p>The default implementation uses the implementation from
     * our superclass, <code>AbstractApplicationBean</code>.</p>
     */
    @Override
    public String getLocaleCharacterEncoding() {
        return super.getLocaleCharacterEncoding();
    }

    protected String homeCommunityID = "";

    /**
     * Get the value of homeCommunityID
     *
     * @return the value of homeCommunityID
     */
    public String getHomeCommunityID()
    {
        return homeCommunityID;
    }

    /**
     * Set the value of homeCommunityID
     *
     * @param homeCommunityID new value of homeCommunityID
     */
    public void setHomeCommunityID(String homeCommunityID)
    {
        this.homeCommunityID = homeCommunityID;
    }
    protected String endPoint;

    /**
     * Get the value of endPoint
     *
     * @return the value of endPoint
     */
    public String getEndPoint()
    {
        return endPoint;
    }

    /**
     * Set the value of endPoint
     *
     * @param endPoint new value of endPoint
     */
    public void setEndPoint(String endPoint)
    {
        this.endPoint = endPoint;
    }
    protected String errorText = "";

    /**
     * Get the value of errorText
     *
     * @return the value of errorText
     */
    public String getErrorText()
    {
        return errorText;
    }

    /**
     * Set the value of errorText
     *
     * @param errorText new value of errorText
     */
    public void setErrorText(String errorText)
    {
        this.errorText = errorText;
    }
    protected boolean EntryDuplicate = false;

    /**
     * Get the value of EntryDuplicate
     *
     * @return the value of EntryDuplicate
     */
    public boolean isEntryDuplicate()
    {
        return EntryDuplicate;
    }

    /**
     * Set the value of EntryDuplicate
     *
     * @param EntryDuplicate new value of EntryDuplicate
     */
    public void setEntryDuplicate(boolean EntryDuplicate)
    {
        this.EntryDuplicate = EntryDuplicate;
    }

}
