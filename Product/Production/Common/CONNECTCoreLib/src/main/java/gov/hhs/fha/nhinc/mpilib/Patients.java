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

import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rayj
 */
public class Patients extends ArrayList<Patient>
        implements java.io.Serializable {
    private static Log log = LogFactory.getLog(Patients.class);
    static final long serialVersionUID = -8509797581456649328L;

    public Patients() {
        log.info("Patients Initialized");
    }
}
