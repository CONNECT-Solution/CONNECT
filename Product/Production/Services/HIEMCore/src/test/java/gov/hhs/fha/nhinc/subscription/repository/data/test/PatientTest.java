/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.subscription.repository.data.test;

import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.subscription.repository.data.Community;
import gov.hhs.fha.nhinc.subscription.repository.data.Patient;
import org.junit.Ignore;

/**
 * Unit test for the patient class
 * 
 * @author Neil Webb
 */
@Ignore
public class PatientTest {
    @Test
    public void testGettersAndSetters() {
        System.out.println("Begin testGettersAndSetters");
        try {
            String patientId = "PatientId";
            String communityId = "CommunityId";
            String communityName = "CommunityName";

            // Set values using setters
            Community comm = new Community();
            comm.setCommunityId(communityId);
            comm.setCommunityName(communityName);
            Patient pat = new Patient();
            pat.setPatientId(patientId);
            pat.setAssigningAuthority(comm);

            // Validate getters
            assertEquals("Patient id", patientId, pat.getPatientId());
            assertNotNull("Assigning authority null", pat.getAssigningAuthority());
            assertEquals("Community id", communityId, pat.getAssigningAuthority().getCommunityId());
            assertEquals("Community name", communityName, pat.getAssigningAuthority().getCommunityName());

        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testGettersAndSetters");
    }

    @Test
    public void testEquals() {
        System.out.println("Begin testEquals");
        try {
            // Equals - patient id and community
            Community comm = new Community();
            comm.setCommunityId("CommunityId");
            comm.setCommunityName("CommunityName");
            Patient pat1 = new Patient();
            pat1.setPatientId("PatientId");
            pat1.setAssigningAuthority(comm);
            comm = new Community();
            comm.setCommunityId("CommunityId");
            comm.setCommunityName("CommunityName");
            Patient pat2 = new Patient();
            pat2.setPatientId("PatientId");
            pat2.setAssigningAuthority(comm);
            assertTrue("Equals - patient id and community", pat1.equals(pat2));

            // Equals - patient id only
            pat1 = new Patient();
            pat1.setPatientId("PatientId");
            pat2 = new Patient();
            pat2.setPatientId("PatientId");
            assertTrue("Equals - patient id only", pat1.equals(pat2));

            // Not equal - both created, patient id different
            comm = new Community();
            comm.setCommunityId("CommunityId");
            comm.setCommunityName("CommunityName");
            pat1 = new Patient();
            pat1.setPatientId("PatientId");
            pat1.setAssigningAuthority(comm);
            comm = new Community();
            comm.setCommunityId("CommunityId");
            comm.setCommunityName("CommunityName");
            pat2 = new Patient();
            pat2.setPatientId("PatientId2");
            pat2.setAssigningAuthority(comm);
            assertFalse("Not equal - both created, patient id different", pat1.equals(pat2));

            // Not equal - both created, community id different
            comm = new Community();
            comm.setCommunityId("CommunityId");
            comm.setCommunityName("CommunityName");
            pat1 = new Patient();
            pat1.setPatientId("PatientId");
            pat1.setAssigningAuthority(comm);
            comm = new Community();
            comm.setCommunityId("CommunityId2");
            comm.setCommunityName("CommunityName");
            pat2 = new Patient();
            pat2.setPatientId("PatientId");
            pat2.setAssigningAuthority(comm);
            assertFalse("Not equal - both created, community id different", pat1.equals(pat2));

            // Not equal - patient 1 full, patient 2 null
            comm = new Community();
            comm.setCommunityId("CommunityId");
            comm.setCommunityName("CommunityName");
            pat1 = new Patient();
            pat1.setPatientId("PatientId");
            pat1.setAssigningAuthority(comm);
            pat2 = null;
            assertFalse("Not equal - patient 1 full, patient 2 null", pat1.equals(pat2));

            // Not equal - patient 1 full, only patient id on second
            comm = new Community();
            comm.setCommunityId("CommunityId");
            comm.setCommunityName("CommunityName");
            pat1 = new Patient();
            pat1.setPatientId("PatientId");
            pat1.setAssigningAuthority(comm);
            pat2 = new Patient();
            pat2.setPatientId("PatientId");
            assertFalse("Not equal - patient 1 full, only patient id on second", pat1.equals(pat2));

            // Not equal - patient 1 full, only community on second
            comm = new Community();
            comm.setCommunityId("CommunityId");
            comm.setCommunityName("CommunityName");
            pat1 = new Patient();
            pat1.setPatientId("PatientId");
            pat1.setAssigningAuthority(comm);
            comm = new Community();
            comm.setCommunityId("CommunityId1");
            comm.setCommunityName("CommunityName");
            pat2 = new Patient();
            pat2.setAssigningAuthority(comm);
            assertFalse("Not equal - patient 1 full, only community on second", pat1.equals(pat2));

            // Not equal - patient 2 full, only patient id on first
            pat1 = new Patient();
            pat1.setPatientId("PatientId");
            comm = new Community();
            comm.setCommunityId("CommunityId");
            comm.setCommunityName("CommunityName");
            pat2 = new Patient();
            pat2.setPatientId("PatientId");
            pat2.setAssigningAuthority(comm);
            assertFalse("Not equal - patient 2 full, only patient id on first", pat1.equals(pat2));

            // Not equal - patient 2 full, only community on first
            comm = new Community();
            comm.setCommunityId("CommunityId");
            comm.setCommunityName("CommunityName");
            pat1 = new Patient();
            pat1.setAssigningAuthority(comm);
            comm = new Community();
            comm.setCommunityId("CommunityId");
            comm.setCommunityName("CommunityName");
            pat2 = new Patient();
            pat2.setPatientId("PatientId");
            pat2.setAssigningAuthority(comm);
            assertFalse("Not equal - patient 2 full, only community on first", pat1.equals(pat2));

        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testEquals");
    }
}