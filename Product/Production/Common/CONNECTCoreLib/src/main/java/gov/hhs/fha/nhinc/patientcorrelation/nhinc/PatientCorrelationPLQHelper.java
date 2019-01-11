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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_PROPERTY_FILE_NAME;
import static gov.hhs.fha.nhinc.patientcorrelation.nhinc.PatientCorrelationOrchImpl.calculateCorrelationExpirationDate;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.CorrelatedIdentifiersDao;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.CorrelatedIdentifiersDaoImpl;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.CorrelatedIdentifiers;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.QualifiedPatientIdentifier;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType.PatientLocationResponse;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hl7.v3.II;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ptambellini
 */
public class PatientCorrelationPLQHelper {
    private static final Logger LOG = LoggerFactory.getLogger(PatientCorrelationPLQHelper.class);

    private PatientCorrelationPLQHelper() {

    }

    public static boolean addPatientCorrelationPLQRecords(PatientLocationQueryResponseType plqRecords) {
        CorrelatedIdentifiersDao dao = new CorrelatedIdentifiersDaoImpl();
        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();

        if (null != plqRecords && CollectionUtils.isEmpty(plqRecords.getPatientLocationResponse())) {
            LOG.info("No Patient-Location-Response");
            return true;
        }

        List<String> aaidList = new ArrayList<>();
        String localAAID = getLocalAAID();
        if (StringUtils.isBlank(localAAID)) {
            LOG.error("local-AAID is blank.");
            return false;
        }
        aaidList.add(localAAID);

        QualifiedPatientIdentifier qpIdentifier = getQualifiedPatientIdentifier(plqRecords);
        if (null == qpIdentifier) {
            LOG.error("RequestedPatientId is not found in the Patient-Location-Query records");
            return false;
        }

        List<QualifiedPatientIdentifier> qList = dao.retrievePatientCorrelation(qpIdentifier, aaidList);
        if (CollectionUtils.isEmpty(qList)) {
            LOG.error("Local-QualifiedPatientIdentifier is not found for the RequestedPatient: {}, {}",
                qpIdentifier.getPatientId(), qpIdentifier.getAssigningAuthorityId());
            return false;
        }

        QualifiedPatientIdentifier localPatient = qList.get(0);

        for(PatientLocationResponse rec : plqRecords.getPatientLocationResponse()){
            if(null != rec.getCorrespondingPatientId() && null != rec.getRequestedPatientId()){

                CorrelatedIdentifiers newId = new CorrelatedIdentifiers();
                newId.setRlsId(rec.getRequestedPatientId().getRoot());

                newId.setCorrelatedPatientId(rec.getCorrespondingPatientId().getExtension());
                newId.setCorrelatedPatientAssigningAuthorityId(rec.getCorrespondingPatientId().getRoot());

                newId.setPatientId(localPatient.getPatientId());
                newId.setPatientAssigningAuthorityId(localPatient.getAssigningAuthorityId());

                newId.setCorrelationExpirationDate(
                    calculateCorrelationExpirationDate(rec.getCorrespondingPatientId().getRoot()));

                dao.addPatientCorrelation(newId);
                mappingDao.storeMapping(rec.getHomeCommunityId(), rec.getHomeCommunityId());
            }
        }

        return true;

    }

    private static QualifiedPatientIdentifier getQualifiedPatientIdentifier(PatientLocationQueryResponseType plqRecords){
        if(null != plqRecords && CollectionUtils.isNotEmpty(plqRecords.getPatientLocationResponse())){
            II requestedPatient = plqRecords.getPatientLocationResponse().get(0).getRequestedPatientId();
            QualifiedPatientIdentifier qId = new QualifiedPatientIdentifier();
            qId.setAssigningAuthority(requestedPatient.getRoot());
            qId.setPatientId(requestedPatient.getExtension());
            return qId;
        }
        return null;
    }

    private static String getLocalAAID() {
        try {
            return PropertyAccessor.getInstance().getProperty(ADAPTER_PROPERTY_FILE_NAME, "assigningAuthorityId");
        } catch (PropertyAccessException ex) {
            LOG.error("error while trying to access adpater.properties--assigningAuthorityId: {}", ex);
        }
        return null;
    }

}

