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
public class Addresses extends ArrayList<Address>
        implements java.io.Serializable {
    private static Log log = LogFactory.getLog(Addresses.class);
    static final long serialVersionUID =5100000000000000000L;

    public Addresses() {
        log.info("PhoneNumbers Initialized");
    }
}
