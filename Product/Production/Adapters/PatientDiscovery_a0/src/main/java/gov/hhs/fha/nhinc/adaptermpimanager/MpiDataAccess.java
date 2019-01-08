/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.adaptermpimanager;

import gov.hhs.fha.nhinc.adaptermpimanager.HL7Parsers.*;
import gov.hhs.fha.nhinc.mpilib.*;
import org.hl7.v3.*;

/**
 *
 * @author mflynn02
 */
public class MpiDataAccess {
    public static Patients LookupPatients(PRPAMT201301UV02Patient patient) {
        return LookupPatients(patient, true);
    }

    public static Patients LookupPatients(PRPAMT201301UV02Patient patient, boolean AllowSearchByDemographics) {
        return LookupPatients(HL7Parser201301.ExtractMpiPatientFromHL7Patient(patient), AllowSearchByDemographics);
    }

    public static Patients LookupPatients(Patient searchParams) {
        return LookupPatients(searchParams, true);
    }

    public static Patients LookupPatients(Patient searchParams, boolean AllowSearchByDemographics) {
        MiniMpi mpi = MiniMpi.getInstance();
        return mpi.search(searchParams, AllowSearchByDemographics);
    }

    public static Patients LookupPatients(Patient searchParams, boolean AllowSearchByDemographics,
            boolean includeOptOutPatient) {
        MiniMpi mpi = MiniMpi.getInstance();
        return mpi.search(searchParams, AllowSearchByDemographics, includeOptOutPatient);
    }

    public static Patient SavePatient(Patient patient) {
        MiniMpi mpi = MiniMpi.getInstance();
        return mpi.addUpdate(patient);
    }

    public static void DeletePatient(Patient patient, String homeCommunityId) {
        MiniMpi mpi = MiniMpi.getInstance();
        mpi.delete(patient, homeCommunityId);
    }

}
