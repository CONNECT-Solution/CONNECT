/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package universalclientgui;

import com.sun.rave.web.ui.appbase.AbstractApplicationBean;
import javax.faces.FacesException;

/**
 * <p>
 * Application scope data bean for your application. Create properties here to represent cached data that should be made
 * available to all users and pages in the application.
 * </p>
 * 
 * <p>
 * An instance of this class will be created for you automatically, the first time your application evaluates a value
 * binding expression or method binding expression that references a managed bean using this class.
 * </p>
 * 
 * @version ApplicationBean1.java
 * @version Created on Aug 7, 2009, 5:38:41 PM
 * @author vvickers
 */

public class ApplicationBean1 extends AbstractApplicationBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

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
     * Construct a new application data bean instance.
     * </p>
     */
    public ApplicationBean1() {
    }

    /**
     * <p>
     * This method is called when this bean is initially added to application scope. Typically, this occurs as a result
     * of evaluating a value binding or method binding expression, which utilizes the managed bean facility to
     * instantiate this bean and store it into application scope.
     * </p>
     * 
     * <p>
     * You may customize this method to initialize and cache application wide data values (such as the lists of valid
     * options for dropdown list components), or to allocate resources that are required for the lifetime of the
     * application.
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
            log("ApplicationBean1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }

        // </editor-fold>
        // Perform application initialization that must complete
        // *after* managed components are initialized
        // TODO - add your own initialization code here
    }

    /**
     * <p>
     * This method is called when this bean is removed from application scope. Typically, this occurs as a result of the
     * application being shut down by its owning container.
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

    /**
     * <p>
     * Return an appropriate character encoding based on the <code>Locale</code> defined for the current JavaServer
     * Faces view. If no more suitable encoding can be found, return "UTF-8" as a general purpose default.
     * </p>
     * 
     * <p>
     * The default implementation uses the implementation from our superclass, <code>AbstractApplicationBean</code>.
     * </p>
     */
    @Override
    public String getLocaleCharacterEncoding() {
        return super.getLocaleCharacterEncoding();
    }
}
