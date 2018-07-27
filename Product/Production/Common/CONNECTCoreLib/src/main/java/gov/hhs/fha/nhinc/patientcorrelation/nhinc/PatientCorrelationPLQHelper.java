/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.CorrelatedIdentifiersDao;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.CorrelatedIdentifiersDaoImpl;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.CorrelatedIdentifiers;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType.PatientLocationResponse;

/**
 *
 * @author ptambellini
 */
public class PatientCorrelationPLQHelper {

    private PatientCorrelationPLQHelper() {

    }

    public static boolean addPatientCorrelationPLQRecords(PatientLocationQueryResponseType plqRecords) {
        CorrelatedIdentifiersDao dao = new CorrelatedIdentifiersDaoImpl();
        boolean result = false;
        for(PatientLocationResponse rec : plqRecords.getPatientLocationResponse()){
            if(null != rec.getCorrespondingPatientId() && null != rec.getRequestedPatientId()){
                AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
                CorrelatedIdentifiers newId = new CorrelatedIdentifiers();
                newId.setRlsId(rec.getRequestedPatientId().getRoot());

                newId.setCorrelatedPatientId(rec.getCorrespondingPatientId().getExtension());
                newId.setCorrelatedPatientAssigningAuthorityId(rec.getCorrespondingPatientId().getRoot());

                newId.setPatientId(rec.getRequestedPatientId().getExtension());
                newId.setPatientAssigningAuthorityId(rec.getRequestedPatientId().getRoot());
                dao.addPatientCorrelation(newId);
                mappingDao.storeMapping(rec.getHomeCommunityId(), rec.getHomeCommunityId());
                result = true;
            }
        }


        return result;

    }

}

