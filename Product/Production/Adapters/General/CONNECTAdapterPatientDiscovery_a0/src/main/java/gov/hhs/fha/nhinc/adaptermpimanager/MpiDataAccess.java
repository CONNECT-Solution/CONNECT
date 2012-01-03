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

package gov.hhs.fha.nhinc.adaptermpimanager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;
import gov.hhs.fha.nhinc.mpilib.*;
import gov.hhs.fha.nhinc.adaptermpimanager.HL7Parsers.*;

/**
 *
 * @author mflynn02
 */
public class MpiDataAccess {
    private static Log log = LogFactory.getLog(MpiDataAccess.class);
    public static Patients LookupPatients(PRPAMT201301UV02Patient patient) {
        return LookupPatients(patient, true);
    }

    public static Patients LookupPatients(PRPAMT201301UV02Patient patient, boolean AllowSearchByDemographics) {
        return LookupPatients(HL7Parser201301.ExtractMpiPatientFromHL7Patient(patient), AllowSearchByDemographics);
    }

    public static Patients LookupPatients(Patient searchParams) {
        return LookupPatients(searchParams,true);
    }
    public static Patients LookupPatients(Patient searchParams, boolean AllowSearchByDemographics) {
        MiniMpi mpi = MiniMpi.GetMpiInstance();
        return mpi.Search(searchParams,AllowSearchByDemographics);
    }
    public static Patients LookupPatients(Patient searchParams, boolean AllowSearchByDemographics, boolean includeOptOutPatient) {
        MiniMpi mpi = MiniMpi.GetMpiInstance();
        return mpi.Search(searchParams,AllowSearchByDemographics,includeOptOutPatient);
    }
    public static Patient SavePatient(Patient patient) {
        MiniMpi mpi = MiniMpi.GetMpiInstance();
        return mpi.AddUpdate(patient);
    }
    
    public static void DeletePatient(Patient patient, String homeCommunityId) {
        MiniMpi mpi = MiniMpi.GetMpiInstance();
        mpi.Delete(patient, homeCommunityId);
    }

}
