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
package gov.hhs.fha.nhinc.patientlocationquery.adapter.dao;

import static org.junit.Assert.assertEquals;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.CorrelatedIdentifiersDaoImpl;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.CorrelatedIdentifiers;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.QualifiedPatientIdentifier;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.RecordLocatorService;
import gov.hhs.fha.nhinc.patientlocationquery.dao.RecordLocationServiceDAO;
import gov.hhs.fha.nhinc.test.DAOIntegrationTest;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

//UnIgnore to run DAO Integration test which will use a MySQL DB connection.
@Ignore
public class PatientLocationQueryDaoIntTest extends DAOIntegrationTest {

    @Test
    public void testGetAllByRLSId() {
        //Tests the default insertions from the populate test data script.

        List<RecordLocatorService> result = RecordLocationServiceDAO.getAllPatientsBy("38273N234");
        assertEquals(2, result.size());

        result = RecordLocationServiceDAO.getAllPatientsBy("38273N233");
        assertEquals(1, result.size());

        result = RecordLocationServiceDAO.getAllPatientsBy("38273N244");
        assertEquals(2, result.size());

        result = RecordLocationServiceDAO.getAllPatientsBy("N0n-Ex!sT@nT");
        assertEquals(0, result.size());
    }

    @Test
    public void testPLQResponseOutbound() {
        //Tests the Patient Location Query insertions to correlated identifiers table.
        CorrelatedIdentifiersDaoImpl dao = new CorrelatedIdentifiersDaoImpl();

        CorrelatedIdentifiers rec = new CorrelatedIdentifiers();
        QualifiedPatientIdentifier newrec = new QualifiedPatientIdentifier();

        rec.setRlsId("2.2");
        rec.setCorrelatedPatientId("355443");
        rec.setPatientAssigningAuthorityId("1.1");
        rec.setPatientId("D254321");
        rec.setCorrelatedPatientAssigningAuthorityId("3.3");


        dao.addPatientCorrelation(rec);
        newrec.setAssigningAuthority("1.1");
        newrec.setPatientId("D254321");

        List<QualifiedPatientIdentifier> result = dao.retrievePatientCorrelation(newrec);
        assertEquals(1, result.size());
        dao.removePatientCorrelation(rec);

    }

}
