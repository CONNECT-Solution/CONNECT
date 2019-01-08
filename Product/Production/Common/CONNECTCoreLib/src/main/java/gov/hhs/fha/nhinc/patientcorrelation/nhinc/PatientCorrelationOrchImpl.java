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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.ack.AckBuilder;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.config.ConfigurationManager;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.config.Expiration;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.config.ExpirationConfiguration;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.CorrelatedIdentifiersDao;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.CorrelatedIdentifiers;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.QualifiedPatientIdentifier;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201301UV.PRPAIN201301UVParser;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.PRPAIN201309UVParser;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.PixRetrieveResponseBuilder;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201307UV02DataSource;
import org.hl7.v3.PRPAMT201307UV02ParameterList;
import org.hl7.v3.PRPAMT201307UV02PatientIdentifier;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 *
 * @author jhoppesc
 */
public class PatientCorrelationOrchImpl implements PatientCorrelationOrch {

    private static final Logger LOG = LoggerFactory.getLogger(PatientCorrelationOrchImpl.class);
    private final CorrelatedIdentifiersDao dao;

    public PatientCorrelationOrchImpl(CorrelatedIdentifiersDao dao) {
        this.dao = dao;
    }

    @Override
    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(
        PRPAIN201309UV02 retrievePatientCorrelationsRequest, AssertionType assertion) {
        PRPAMT201307UV02PatientIdentifier patIdentifier = PRPAIN201309UVParser
            .parseHL7PatientPersonFrom201309Message(retrievePatientCorrelationsRequest);
        if (patIdentifier == null) {
            return null;
        }
        List<II> listII = patIdentifier.getValue();
        if (listII.isEmpty()) {
            LOG.warn("patient identifier was null");
            return null;
        }

        II inputPatientId = listII.get(0);
        List<String> dataSourceList = extractDataSourceList(retrievePatientCorrelationsRequest);
        QualifiedPatientIdentifier inputQualifiedPatientIdentifier = qualifiedPatientIdentifierFactory(inputPatientId);
        // only non-expired patient correlation records will be returned
        // expired correlation records will be removed by the following call.
        List<QualifiedPatientIdentifier> qualifiedPatientIdentifiers = retrieveQualifiedPatientIdentifiers(
            inputQualifiedPatientIdentifier, dataSourceList);
        List<II> iiList = buildList(qualifiedPatientIdentifiers);
        PRPAIN201310UV02 IN201310 = PixRetrieveResponseBuilder
            .createPixRetrieveResponse(retrievePatientCorrelationsRequest, iiList);
        RetrievePatientCorrelationsResponseType result = new RetrievePatientCorrelationsResponseType();
        result.setPRPAIN201310UV02(IN201310);
        return result;
    }

    protected List<QualifiedPatientIdentifier> retrieveQualifiedPatientIdentifiers(
        QualifiedPatientIdentifier inputQualifiedPatientIdentifier, List<String> dataSourceList) {

        return dao.retrievePatientCorrelation(inputQualifiedPatientIdentifier, dataSourceList);
    }

    @Override
    public AddPatientCorrelationResponseType addPatientCorrelation(PRPAIN201301UV02 addPatientCorrelationRequest,
        AssertionType assertion) {
        PRPAMT201301UV02Patient patient = PRPAIN201301UVParser
            .parseHL7PatientPersonFrom201301Message(addPatientCorrelationRequest);
        String patientId;
        String patientAssigningAuthId;
        String correlatedPatientId;
        String correlatedPatientAssigningAuthId;
        if (patient == null) {
            LOG.warn("Patient was null");
            return null;
        }
        List<II> ids = patient.getId();

        if (ids.get(0) == null) {
            LOG.warn("id(0) was null");
            return null;
        }
        if (ids.get(1) == null) {
            LOG.warn("id(1) was null");
            return null;
        }
        patientId = ids.get(0).getExtension();
        if (patientId != null && !patientId.isEmpty()) {
        } else {
            LOG.warn("patient id was not supplied");
            return null;
        }
        patientAssigningAuthId = ids.get(0).getRoot();
        if (patientAssigningAuthId != null && !patientAssigningAuthId.isEmpty()) {
            LOG.warn("patientAssigningAuthId: " + patientAssigningAuthId);
        } else {
            LOG.warn("patient assigning authority was not supplied");
            return null;
        }
        correlatedPatientId = ids.get(1).getExtension();
        if (correlatedPatientId == null || correlatedPatientId.isEmpty()) {
            LOG.warn("correlatedPatientId was not supplied");
            return null;
        }
        correlatedPatientAssigningAuthId = ids.get(1).getRoot();
        if (correlatedPatientAssigningAuthId != null && !correlatedPatientAssigningAuthId.isEmpty()) {
            LOG.warn("correlatedPatientAssigningAuthId: " + correlatedPatientAssigningAuthId);
        } else {
            LOG.warn("correlatedPatientId assigning authority was not supplied");
            return null;
        }

        // calculate the correlation expiration date
        Date newExpirationDate = calculateCorrelationExpirationDate(correlatedPatientAssigningAuthId);
        CorrelatedIdentifiers correlatedIdentifers = new CorrelatedIdentifiers();
        correlatedIdentifers.setCorrelatedPatientAssigningAuthorityId(correlatedPatientAssigningAuthId);
        correlatedIdentifers.setCorrelatedPatientId(correlatedPatientId);
        correlatedIdentifers.setPatientId(patientId);
        correlatedIdentifers.setPatientAssigningAuthorityId(patientAssigningAuthId);
        correlatedIdentifers.setCorrelationExpirationDate(newExpirationDate);
        dao.addPatientCorrelation(correlatedIdentifers);
        AddPatientCorrelationResponseType result = new AddPatientCorrelationResponseType();
        result.setMCCIIN000002UV01(AckBuilder.buildAck(addPatientCorrelationRequest));
        return result;
    }

    private static List<String> extractDataSourceList(PRPAIN201309UV02 IN201309) {
        List<String> dataSourceStringList = new ArrayList<>();
        PRPAMT201307UV02ParameterList parameterList = PRPAIN201309UVParser
            .parseHL7ParameterListFrom201309Message(IN201309);

        List<PRPAMT201307UV02DataSource> dataSources = parameterList.getDataSource();
        if (!dataSources.isEmpty()) {
            for (PRPAMT201307UV02DataSource datasource : dataSources) {
                for (II value : datasource.getValue()) {
                    dataSourceStringList.add(value.getRoot());
                }
            }
        }
        return dataSourceStringList;
    }

    private static List<II> buildList(List<QualifiedPatientIdentifier> qualifiedPatientIdentifiers) {
        if (qualifiedPatientIdentifiers == null) {
            return null;
        }
        List<II> iiList = new ArrayList<>();
        for (QualifiedPatientIdentifier qualifiedPatientIdentifier : qualifiedPatientIdentifiers) {
            iiList.add(iiFactory(qualifiedPatientIdentifier));
        }
        return iiList;
    }

    private static II iiFactory(QualifiedPatientIdentifier qualifiedPatientIdentifier) {
        II ii = new II();
        ii.setRoot(HomeCommunityMap.formatHomeCommunityId(qualifiedPatientIdentifier.getAssigningAuthorityId()));
        ii.setExtension(qualifiedPatientIdentifier.getPatientId());
        return ii;
    }

    private static QualifiedPatientIdentifier qualifiedPatientIdentifierFactory(II ii) {
        QualifiedPatientIdentifier qualifiedPatientIdentifier = new QualifiedPatientIdentifier();
        qualifiedPatientIdentifier.setAssigningAuthority(ii.getRoot());
        qualifiedPatientIdentifier.setPatientId(ii.getExtension());
        return qualifiedPatientIdentifier;
    }

    public static Date calculateCorrelationExpirationDate(String assigningAuthority) {
        ExpirationConfiguration pcConfig;
        pcConfig = new ConfigurationManager().loadExpirationConfiguration();
        LOG.debug("assigningAuthorityId = " + assigningAuthority);
        Expiration exp = new ConfigurationManager().loadConfiguration(pcConfig, assigningAuthority);
        return calculateCorrelationExpirationDate(exp);
    }

    public static Date calculateCorrelationExpirationDate(Expiration config) {
        Date result = null;
        if (config != null) {
            LOG.debug(" Expiration = " + config.getDuration());
            try {
                result = getExpirationDate(config.getUnits(), config.getDuration());
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
                result = null;
            }
        }
        LOG.debug("Expiration Date = {}", result);
        return result;
    }

    private static Date getExpirationDate(String expirationUnits, int expiration) throws Exception {
        Calendar calendar = Calendar.getInstance();
        if ("YEAR".equalsIgnoreCase(expirationUnits)) {
            calendar.add(Calendar.YEAR, expiration);
        } else if ("MONTH".equalsIgnoreCase(expirationUnits)) {
            calendar.add(Calendar.MONTH, expiration);
        } else if ("WEEK".equalsIgnoreCase(expirationUnits)) {
            calendar.add(Calendar.WEEK_OF_YEAR, expiration);
        } else if ("DAY".equalsIgnoreCase(expirationUnits)) {
            calendar.add(Calendar.DAY_OF_YEAR, expiration);
        } else if ("HOUR".equalsIgnoreCase(expirationUnits)) {
            calendar.add(Calendar.HOUR_OF_DAY, expiration);
        } else if ("MINUTE".equalsIgnoreCase(expirationUnits)) {
            calendar.add(Calendar.MINUTE, expiration);
        } else if ("SECOND".equalsIgnoreCase(expirationUnits)) {
            calendar.add(Calendar.SECOND, expiration);
        } else {
            throw new Exception("Invalid Expiration Units");
        }
        return calendar.getTime();
    }

    @Override
    public  void addPatientCorrelationPLQ(PatientLocationQueryResponseType plqRecords,
        AssertionType assertion) {

        if (plqRecords == null) {
            LOG.warn("PLQRecords was null");
            return;
        }
        PatientCorrelationPLQHelper.addPatientCorrelationPLQRecords(plqRecords);

    }
}
