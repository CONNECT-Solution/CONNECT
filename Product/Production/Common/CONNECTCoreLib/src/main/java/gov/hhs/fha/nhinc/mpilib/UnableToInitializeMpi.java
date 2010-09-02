/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.mpilib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rayj
 */
class UnableToInitializeMpi extends RuntimeException {
    private static Log log = LogFactory.getLog(UnableToInitializeMpi.class);
    
    public UnableToInitializeMpi() {
        super();
        log.info("UnableToInitializeMpi Initialized with no arguments");
    }

    public UnableToInitializeMpi(String message) {
        super(message);
        log.info("UnableToInitializeMpi Initialized with message argument");
    }

    public UnableToInitializeMpi(String message, Throwable cause) {
        super(message, cause);
        log.info("UnableToInitializeMpi Initialized with message and cause arguments");
    }
}
