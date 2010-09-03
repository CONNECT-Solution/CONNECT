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
package gov.hhs.fha.nhinc.mpi.adapter.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers.HL7Parser201305;
import gov.hhs.fha.nhinc.mpilib.*;
import org.hl7.v3.*;

/**
 *
 * @author Jon Hoppesch
 */
public class MpiDataAccess {
    private static Log log = LogFactory.getLog(MpiDataAccess.class);
    public static Patients LookupPatients(PRPAMT201306UV02ParameterList queryParams) {
        return LookupPatients(queryParams, true);
    }

    public static Patients LookupPatients(PRPAMT201306UV02ParameterList queryParams, boolean AllowSearchByDemographics) {
        return LookupPatients(HL7Parser201305.ExtractMpiPatientFromQueryParams(queryParams), AllowSearchByDemographics);
    }

    public static Patients LookupPatients(Patient searchParams) {
        return LookupPatients(searchParams,true);
    }
    public static Patients LookupPatients(Patient searchParams, boolean AllowSearchByDemographics) {
        MiniMpi mpi = MiniMpi.GetMpiInstance();
        return mpi.Search(searchParams,AllowSearchByDemographics);
    }
}
