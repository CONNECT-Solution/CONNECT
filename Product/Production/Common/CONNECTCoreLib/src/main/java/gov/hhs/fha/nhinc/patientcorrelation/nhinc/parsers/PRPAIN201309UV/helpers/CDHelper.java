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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers;

import org.hl7.v3.CD;

/**
 *
 * @author rayj
 */
public class CDHelper {

    public static CD CDFactory(String codeValue, String codeSystem) {
                CD code = new CD();
        code.setCode( codeValue);
        code.setCodeSystem(codeSystem);
return code;
    }
}
