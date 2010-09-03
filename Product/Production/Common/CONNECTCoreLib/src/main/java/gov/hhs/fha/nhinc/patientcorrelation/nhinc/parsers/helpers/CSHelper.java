/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

import org.hl7.v3.CS;

/**
 *
 * @author rayj
 */
public class CSHelper {
    public static CS buildCS(String code) {
        CS csCode = new CS();
        csCode.setCode(code);
        return csCode;
    }

}
