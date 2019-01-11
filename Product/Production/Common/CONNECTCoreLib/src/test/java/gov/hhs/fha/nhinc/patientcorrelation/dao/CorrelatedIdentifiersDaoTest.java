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
package gov.hhs.fha.nhinc.patientcorrelation.dao;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.CorrelatedIdentifiersDao;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.CorrelatedIdentifiersDaoImpl;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.CorrelatedIdentifiers;
import java.util.Date;
import org.junit.Test;

/**
 *
 * @author svalluripalli
 */
public class CorrelatedIdentifiersDaoTest {

    public CorrelatedIdentifiersDaoTest() {
    }

    @Test
    public void testAddPatientCorrelation() {
        CorrelatedIdentifiersDao dao = new CorrelatedIdentifiersDaoImpl() {
            @Override
            public void addPatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {

            }
        };

        CorrelatedIdentifiers ci = new CorrelatedIdentifiers();
        ci.setCorrelatedPatientAssigningAuthorityId("2.16.840.1.113883.3.198");
        ci.setCorrelatedPatientId("D123401");
        ci.setPatientAssigningAuthorityId("2.16.840.1.113883.4.1");
        ci.setPatientId("123456789");
        ci.setCorrelationExpirationDate(new Date());
        dao.addPatientCorrelation(ci);
        CorrelatedIdentifiers ci1 = new CorrelatedIdentifiers();
        ci1.setCorrelatedPatientAssigningAuthorityId("2.16.840.1.113883.3.192");
        ci1.setCorrelatedPatientId("1018");
        ci1.setPatientAssigningAuthorityId("2.16.840.1.113883.3.200");
        ci1.setPatientId("500000000");
        ci1.setCorrelationExpirationDate(new Date());
        dao.addPatientCorrelation(ci1);
        CorrelatedIdentifiers ci2 = new CorrelatedIdentifiers();
        ci2.setCorrelatedPatientAssigningAuthorityId("2.16.840.1.113883.3.200");
        ci2.setCorrelatedPatientId("500000000");
        ci2.setPatientAssigningAuthorityId("2.16.840.1.113883.3.192");
        ci2.setPatientId("1018");
        ci2.setCorrelationExpirationDate(new Date());
        dao.addPatientCorrelation(ci2);
        CorrelatedIdentifiers ci3 = new CorrelatedIdentifiers();
        ci3.setCorrelatedPatientAssigningAuthorityId("2.16.840.1.113883.4.1");
        ci3.setCorrelatedPatientId("123456789");
        ci3.setPatientAssigningAuthorityId("2.16.840.1.113883.3.200");
        ci3.setPatientId("500000000");
        ci3.setCorrelationExpirationDate(new Date());
        dao.addPatientCorrelation(ci3);
        CorrelatedIdentifiers ci4 = new CorrelatedIdentifiers();
        ci4.setCorrelatedPatientAssigningAuthorityId("2.16.840.1.113883.3.200");
        ci4.setCorrelatedPatientId("500000000");
        ci4.setPatientAssigningAuthorityId("2.16.840.1.113883.4.1");
        ci4.setPatientId("123456789");
        ci4.setCorrelationExpirationDate(new Date());
        dao.addPatientCorrelation(ci4);
    }

}
