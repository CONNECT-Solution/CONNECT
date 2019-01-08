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
package gov.hhs.fha.nhinc.patientdiscovery.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author kongar
 */
public class VerifyModeTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final TrustMode mockTrustMode = context.mock(TrustMode.class);

    public VerifyModeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testProcessResponse_ResponseParams() {

        // first name, last name, gender, birthTime, ssn
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
            "M", null, null);

        // person, patientId, aauthority
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");

        // patient, senderOID, receiverOID, localDeviceId
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");
        ProxyPRPAIN201305UVProxySecuredRequestType prpainRequest = new ProxyPRPAIN201305UVProxySecuredRequestType();
        prpainRequest.setPRPAIN201305UV02(request);
        prpainRequest.setNhinTargetSystem(null);

        // patient, senderOID, receiverAAID, receiverOID, localDeviceId, query
        PRPAIN201306UV02 response = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2",
            request);

        AssertionType assertion = new AssertionType();

        ResponseParams params = new ResponseParams();
        params.origRequest = prpainRequest;
        params.assertion = assertion;
        params.response = response;

        VerifyMode verifyMode = new VerifyMode() {
            @Override
            protected II patientExistsLocally(List<II> localPatientIds, AssertionType assertion,
                PRPAIN201306UV02 response, NhinTargetSystemType target) {
                return null;
            }
        };

        PRPAIN201306UV02 verifyResponse = verifyMode.processResponse(params);
        assertEquals(response, verifyResponse);

        verifyMode = new VerifyMode() {
            @Override
            protected II patientExistsLocally(List<II> localPatientIds, AssertionType assertion,
                PRPAIN201306UV02 response, NhinTargetSystemType target) {
                II ii = new II();
                ii.setExtension("extension");
                ii.setRoot("root");
                return ii;
            }

            @Override
            protected TrustMode getTrustMode() {
                return mockTrustMode;
            }
        };

        context.checking(new Expectations() {
            {
                exactly(1).of(mockTrustMode).processResponse(with(any(PRPAIN201306UV02.class)),
                    with(any(AssertionType.class)), with(any(II.class)));
            }
        });

        verifyResponse = verifyMode.processResponse(params);
        assertEquals(response, verifyResponse);
    }

    @Test
    public void testProcessResponse_LocalPatientId() {
        PRPAIN201306UV02 response = null;
        AssertionType assertion = null;
        II localPatId = new II();

        VerifyMode verifyMode = new VerifyMode() {
            @Override
            protected II patientExistsLocally(List<II> localPatientIds, AssertionType assertion,
                PRPAIN201306UV02 response, NhinTargetSystemType target) {
                II ii = new II();
                ii.setExtension("extension");
                ii.setRoot("root");
                return ii;
            }

            @Override
            protected TrustMode getTrustMode() {
                return mockTrustMode;
            }

        };

        context.checking(new Expectations() {
            {
                exactly(1).of(mockTrustMode).processResponse(with(aNull(PRPAIN201306UV02.class)),
                    with(aNull(AssertionType.class)), with(any(II.class)));
            }
        });

        PRPAIN201306UV02 result = verifyMode.processResponse(response, assertion, localPatId);
        assertEquals(null, result);
    }

    @Test
    public void testGetLocalHomeCommunityId() {
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
            "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");
        PRPAIN201306UV02 response = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2",
            request);

        VerifyMode verifyMode = new VerifyMode();
        String senderCommunityId = verifyMode.getSenderCommunityId(response);
        assertEquals("2.2", senderCommunityId);
    }

    @Test
    public void testGetTrustMode() {
        VerifyMode verifyMode = new VerifyMode();
        TrustMode trustMode = verifyMode.getTrustMode();
        assertNotNull(trustMode);
    }

    @Test
    public void testGetSenderCommunityId() {
        // first name, last name, gender, birthTime, ssn
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
            "M", null, null);

        // person, patientId, aauthority
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");

        // patient, senderOID, receiverOID, localDeviceId
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        // patient, senderOID, receiverAAID, receiverOID, localDeviceId, query
        PRPAIN201306UV02 response = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2",
            request);

        VerifyMode verifyMode = new VerifyMode();
        String senderId = verifyMode.getSenderCommunityId(response);
        assertEquals("2.2", senderId);
    }

    @Test
    public void testExtractPatient() {
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
            "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");
        PRPAIN201306UV02 response = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2",
            request);

        VerifyMode verifyMode = new VerifyMode();
        PRPAMT201301UV02Patient patientResult = verifyMode.extractPatient(response);
        assertEquals("1234", patientResult.getId().get(0).getExtension());
    }

    /**
     * Test of convert201306to201305 method, of class VerifyMode.
     */
    @Test
    public void testConvert201306to201305() {
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
            "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");
        PRPAIN201306UV02 response = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2",
            request);

        VerifyMode verifyMode = new VerifyMode() {
            @Override
            protected String getLocalHomeCommunityId() {
                return "1.1";
            }

            @Override
            protected String getSenderCommunityId(PRPAIN201306UV02 response) {
                return "2.2";
            }
        };

        PRPAIN201305UV02 result = verifyMode.convert201306to201305(response);
        assertEquals("1234", result.getControlActProcess().getQueryByParameter().getValue().getParameterList()
            .getLivingSubjectId().get(0).getValue().get(0).getExtension());
    }

    @Test
    public void testPatientExistsLocally() {
        // List<II> localPatientIds, AssertionType assertion, PRPAIN201306UV02 response

        List<II> localPatientIds = new ArrayList<>();
        II patientId = new II();
        patientId.setExtension("1234");
        patientId.setRoot("1.1.1");
        localPatientIds.add(patientId);

        VerifyMode verifyMode = new VerifyMode() {
            @Override
            protected TrustMode getTrustMode() {
                return mockTrustMode;
            }

            @Override
            protected PRPAIN201305UV02 convert201306to201305(PRPAIN201306UV02 response) {
                return null;
            }

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion, NhinTargetSystemType target) {
                JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe",
                    "Smith", "M", null, null);
                PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
                PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");
                return HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1",
                    "2.2.2", request);
            }
        };

        context.checking(new Expectations() {
            {
                exactly(1).of(mockTrustMode).processResponse(with(any(PRPAIN201306UV02.class)),
                    with(any(AssertionType.class)), with(any(II.class)));
            }
        });

        II patId = verifyMode.patientExistsLocally(localPatientIds, null, null, null);
        assertEquals("1234", patId.getExtension());
    }

    @Test
    public void testCompareId() {
        PRPAMT201306UV02LivingSubjectId localSubjectId = new PRPAMT201306UV02LivingSubjectId();
        II localId = new II();
        localId.setExtension("extension");
        localId.setRoot("root");
        localSubjectId.getValue().add(localId);

        PRPAMT201306UV02LivingSubjectId remoteSubjectId = new PRPAMT201306UV02LivingSubjectId();
        II remoteId = new II();
        remoteId.setExtension("extension");
        remoteId.setRoot("root");
        remoteSubjectId.getValue().add(remoteId);

        VerifyMode verifyMode = new VerifyMode();

        boolean result = verifyMode.compareId(localSubjectId, remoteSubjectId);
        assertTrue(result);
    }

    @Test
    public void testCompareId_List() {
        List<PRPAMT201306UV02LivingSubjectId> localSubjectIds = new ArrayList<>();
        PRPAMT201306UV02LivingSubjectId localSubjectId = new PRPAMT201306UV02LivingSubjectId();
        II localId = new II();
        localId.setExtension("extension");
        localId.setRoot("root");
        localSubjectId.getValue().add(localId);
        localSubjectIds.add(localSubjectId);

        List<PRPAMT201306UV02LivingSubjectId> remoteSubjectIds = new ArrayList<>();
        PRPAMT201306UV02LivingSubjectId remoteSubjectId = new PRPAMT201306UV02LivingSubjectId();
        II remoteId = new II();
        remoteId.setExtension("extension");
        remoteId.setRoot("root");
        remoteSubjectId.getValue().add(remoteId);
        remoteSubjectIds.add(remoteSubjectId);

        VerifyMode verifyMode = new VerifyMode();

        boolean result = verifyMode.compareId(localSubjectIds, remoteSubjectIds);
        assertTrue(result);
    }

}
