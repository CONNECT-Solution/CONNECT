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
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import org.hl7.v3.PRPAIN201306UV02;
import javax.xml.ws.WebServiceContext;
/**
 *
 * @author dunnek
 */
public class ResponseParams {
    public ProxyPRPAIN201305UVProxySecuredRequestType origRequest;
    public PRPAIN201306UV02 response;
    //public WebServiceContext context;
    public AssertionType assertion;
}
