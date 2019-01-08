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
package gov.hhs.fha.nhinc.mpi.adapter.component;

import gov.hhs.fha.nhinc.mpilib.Patients;

/**
 *
 * @author rayj
 */
public class CommonChecks {

    /**
     * Method to determine if the Patient object has only one patient within.
     * @param patients the Patients object to check
     * @return true if patients only has one Patient. False otherwise
     */
    public static boolean isSingleSearchResult(Patients patients) {
        return ((patients != null) && (patients.size() == 1));
    }

    /**
     * Method to determine if the Patient object has more than one Patient.
     * @param patients the Patients object to check.
     * @return true if patients has more than one Patient within. False otherwise
     */
    public static boolean isMultipleSearchResult(Patients patients) {
        return ((patients != null) && (patients.size() > 1));
    }

    /**
     * Method to determine if the Patient object is null or has no members.
     * @param patients the Patients object to check.
     * @return true if null or has no size. False otherwise
     */
    public static boolean isZeroSearchResult(Patients patients) {
        return ((patients == null) || (patients.isEmpty()));
    }

}
