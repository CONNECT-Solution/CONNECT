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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import java.util.ArrayList;
import java.util.List;
import org.hl7.v3.PRPAIN201309UV02;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class PixRetrieveBuilderTest {

    @Test
    public void testcreatePixRetrieve() {
        PixRetrieveBuilder builder = new PixRetrieveBuilder();
        PRPAIN201309UV02 pixRetrieve = new PRPAIN201309UV02();
        pixRetrieve = builder.createPixRetrieve(createRetrievePatientCorrelationsRequest());
        assertEquals(pixRetrieve.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedPerson()
            .getValue().getId().get(0).getNullFlavor().get(0), "NA");
        assertEquals(pixRetrieve.getInteractionId().getExtension(), "PRPA_IN201309");
        assertEquals(pixRetrieve.getId().getRoot(), "1.1");
    }

    @Test
    public void testcreatePixRetrieveWhenAANull() {
        PixRetrieveBuilder builder = new PixRetrieveBuilder();
        PRPAIN201309UV02 pixRetrieve = new PRPAIN201309UV02();
        pixRetrieve = builder.createPixRetrieve(createRetrievePatientCorrelationsRequestWhenAANull());
        assertEquals(pixRetrieve.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedPerson()
            .getValue().getId().get(0).getNullFlavor().get(0), "NA");
        assertEquals(pixRetrieve.getInteractionId().getExtension(), "PRPA_IN201309");
        assertEquals(pixRetrieve.getId().getRoot(), "1.1");

    }

    @Test
    public void testcreatePixRetrieveWithTargetCommunityPrefix() {
        PixRetrieveBuilder builder = new PixRetrieveBuilder();
        List<String> homeCommIds;

        homeCommIds = builder.stripCommunityIdsPrefix(null);
        assertEquals(homeCommIds, null);

        homeCommIds = builder.stripCommunityIdsPrefix(new ArrayList<String>());
        assertEquals(homeCommIds, null);

        homeCommIds = builder.stripCommunityIdsPrefix(getHomeCommunitiesIdsWithPrefix());
        assertEquals(homeCommIds.get(0), "1.1");
        assertEquals(homeCommIds.get(1), "2.2");
    }

    private RetrievePatientCorrelationsRequestType createRetrievePatientCorrelationsRequest() {
        RetrievePatientCorrelationsRequestType patcorrReq = new RetrievePatientCorrelationsRequestType();
        patcorrReq.setQualifiedPatientIdentifier(createQualifiedSubjectIdentifier());
        patcorrReq.setAssertion(createAssertion());
        patcorrReq.getTargetAssigningAuthority().add("2.2");
        return patcorrReq;
    }

    private RetrievePatientCorrelationsRequestType createRetrievePatientCorrelationsRequestWhenAANull() {
        RetrievePatientCorrelationsRequestType patcorrReq = new RetrievePatientCorrelationsRequestType();
        patcorrReq.setQualifiedPatientIdentifier(createQualifiedSubjectIdentifier());
        patcorrReq.setAssertion(createAssertion());
        return patcorrReq;
    }

    private QualifiedSubjectIdentifierType createQualifiedSubjectIdentifier() {
        QualifiedSubjectIdentifierType subject = new QualifiedSubjectIdentifierType();
        subject.setAssigningAuthorityIdentifier("1.1");
        subject.setSubjectIdentifier("D123401");
        return subject;
    }

    private AssertionType createAssertion() {
        return new AssertionType();
    }

    private List<String> getHomeCommunitiesIdsWithPrefix() {
        List<String> homeCommunityIds = new ArrayList<>();
        homeCommunityIds.add("urn:oid:1.1");
        homeCommunityIds.add("2.2");
        return homeCommunityIds;
    }

}
