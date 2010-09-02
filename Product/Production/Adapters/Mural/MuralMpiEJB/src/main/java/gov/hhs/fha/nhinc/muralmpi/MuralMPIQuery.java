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

package gov.hhs.fha.nhinc.muralmpi;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;

import com.sun.mdm.index.webservice.PatientEJBService;
import com.sun.mdm.index.webservice.PatientEJB;
import com.sun.mdm.index.webservice.SystemPatient;
import com.sun.mdm.index.webservice.EnterprisePatient;
import com.sun.mdm.index.webservice.SearchPatientResult;
import com.sun.mdm.index.webservice.MatchColResult;
import com.sun.mdm.index.webservice.PatientBean;
import com.sun.mdm.index.webservice.SystemPatientPK;
import java.util.List;


/**
 *
 * @author dunnek
 */
public class MuralMPIQuery
{
    private static Log log = LogFactory.getLog(MuralMPIQuery.class);
    private static final String PROPERTY_FILE = "adapter";
    private static final String GW_PROPERTY_FILE = "gateway";
    private static final String AA_PROPERTY_NAME = "assigningAuthorityId";
    private static final String OID_PROPERTY_NAME = "localHomeCommunityId";
    private static final String DEFAULT_AA_OID = "1.1";
    private static final String MURAL_SERVICE_NAME = "muralmpi";

    public static org.hl7.v3.PRPAIN201306UV02 query(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest)
    {
       PatientEJBService mpiService = new PatientEJBService ();
       PatientEJB mpiPort =  mpiService.getPatientEJBPort();
       String receiverAA = "";
       String receiverOID = "";
       String muralEndPoint = "";
       org.hl7.v3.PRPAIN201306UV02 retVal = new org.hl7.v3.PRPAIN201306UV02();

       log.debug("Begin MuralMPIQuery.query");

       java.util.List<SearchPatientResult> result;
       receiverAA = getLocalAssigningAuthorityId();
       receiverOID = getLocalOID();
       muralEndPoint = getMuralEndpoint(receiverOID);
       
       PatientBean patient = HL7Parser.extractPatientSearchCritieria(findCandidatesRequest);

       try
       {
            ((javax.xml.ws.BindingProvider) mpiPort).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, muralEndPoint);

            log.debug("Call Mural Search Service");
            result = mpiPort.searchExact(patient);
            log.debug("Mural Search Service call complete");
            log.debug(result.size());

            if(result.size() == 1)
            {
                String euid = result.get(0).getEUID();
                
                //List<SystemPatientPK> sysList = mpiPort.getLIDs(euid);
                              
                retVal = HL7Parser.createPRPA201306(result.get(0), findCandidatesRequest, euid, receiverAA, getLocalOID());
            }
       }
       catch (Exception ex)
       {
            log.debug("MuralMPIQuery.query - took an error");
            log.error(ex.getStackTrace().toString());
            log.error(ex.getMessage());

            retVal = null;
       }
       finally
       {
            log.debug("End MuralMPIQuery.query");
            log.debug(retVal);
            return retVal;
       }

     
   }

    private static SystemPatientPK getLocalSystemPatient(List<SystemPatientPK> patient, String localOID)
    {
        SystemPatientPK result = null;
        
        for(SystemPatientPK sysPatPK : patient)
        {
            if(sysPatPK.getSystemCode().equals(localOID))
            {
                result = sysPatPK;
            }
        }

        return result;
    }
    private static String getLocalAssigningAuthorityId()
    {
        String result = "";

        try
        {
            result = PropertyAccessor.getProperty(PROPERTY_FILE, AA_PROPERTY_NAME);
        }
        catch (Exception ex)
        {
            result = DEFAULT_AA_OID;
        }

        return result;
    }
    private static String getLocalOID()
    {
        String result = "";

        try
        {
            result = PropertyAccessor.getProperty(GW_PROPERTY_FILE, OID_PROPERTY_NAME);
        }
        catch (Exception ex)
        {
            result = DEFAULT_AA_OID;
        }

        return result;
    }
    private static String getMuralEndpoint(String localOID)
    {
        String sEndpointURL = "";

         try {
               sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName(localOID, MURAL_SERVICE_NAME);
           }
           catch (Exception e) {
               log.error("Failed to retrieve endpoint URL for service:" + MURAL_SERVICE_NAME +
                         " from connection manager.  Error: " + e.getMessage(), e);
           }

        return sEndpointURL;
    }
}
