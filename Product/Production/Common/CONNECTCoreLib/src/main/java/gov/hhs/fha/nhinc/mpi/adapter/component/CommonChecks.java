/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.mpi.adapter.component;

import gov.hhs.fha.nhinc.mpilib.Patients;
import gov.hhs.fha.nhinc.patientdb.model.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rayj
 */
public class CommonChecks {

    // MPI XML Checks

    public static boolean isSingleSearchResult(Patients patients) {
        return ((patients != null) && (patients.size() == 1));
    }

    public static boolean isMultipleSearchResult(Patients patients) {
        return ((patients != null) && (patients.size() > 1));
    }

    public static boolean isZeroSearchResult(Patients patients) {
        return ((patients == null) || (patients.size() == 0));
    }

    // MPI DB Checks

    public static boolean isSinglePerAASearchResult(List<Patient> patients) {
        boolean result = true;

        Map testMap = new HashMap();
        if (patients != null) {
            for (Patient patient : patients) {
                if (patient.getIdentifiers() != null &&
                        patient.getIdentifiers().size() > 0 &&
                        patient.getIdentifiers().get(0) != null) {
                    String orgId = patient.getIdentifiers().get(0).getOrganizationId();
                    if (testMap.containsKey(orgId)) {
                        result = false;
                        break;
                    }
                }
            }
        }

        return result;
    }

    public static boolean isZeroPerAASearchResult(List<Patient> patients) {
        return ((patients == null) || (patients.size() == 0));
    }

}
