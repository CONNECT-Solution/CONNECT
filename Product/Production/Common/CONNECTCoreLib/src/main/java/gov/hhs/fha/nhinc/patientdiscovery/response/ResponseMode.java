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

package gov.hhs.fha.nhinc.patientdiscovery.response;
import org.hl7.v3.PRPAIN201306UV02;
/**
 *
 * @author dunnek
 */
public interface ResponseMode {    
    public PRPAIN201306UV02 processResponse(ResponseParams params);
}
