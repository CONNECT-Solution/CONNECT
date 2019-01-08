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
package gov.hhs.fha.nhinc.docrepository.adapter.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;

public class DocumentQueryParamsTest {

    @Test
    public void testEquals() {
        DocumentQueryParams param1 = getNewDQParams();
        DocumentQueryParams param2 = getNewDQParams();

        assertTrue(param1.equals(param2));
    }

    @Test
    public void testHashCode() {
        DocumentQueryParams param = new DocumentQueryParams();

        String patientId = "Patient ID 1";
        param.setPatientId(patientId);

        assertEquals(param.hashCode(), patientId.hashCode());
    }

    private DocumentQueryParams getNewDQParams() {
        DocumentQueryParams docQueryParams = new DocumentQueryParams();

        String patientId = "Patient_1";
        docQueryParams.setPatientId(patientId);

        List<String> classCodeList = new ArrayList<>();
        String classCode = "classCode";
        classCodeList.add(classCode);
        docQueryParams.setClassCodes(classCodeList);

        String classCodeScheme = "class code scheme";
        docQueryParams.setClassCodeScheme(classCodeScheme);

        Date createTimeFrom = new Date();
        createTimeFrom.setTime(11111L);
        docQueryParams.setCreationTimeFrom(createTimeFrom);

        Date createTimeTo = new Date();
        createTimeTo.setTime(11111L);
        docQueryParams.setCreationTimeTo(createTimeTo);

        List<String> docUniqueIdList = new ArrayList<>();
        String docUniqueId = "Doc_Unique_Id 1";
        docUniqueIdList.add(docUniqueId);
        docQueryParams.setDocumentUniqueId(docUniqueIdList);

        List<String> statusList = new ArrayList<>();
        String status = "status";
        statusList.add(status);
        docQueryParams.setStatuses(statusList);

        Date serviceStartTimeFrom = new Date();
        serviceStartTimeFrom.setTime(11111L);
        docQueryParams.setServiceStartTimeFrom(serviceStartTimeFrom);

        Date serviceStartTimeTo = new Date();
        serviceStartTimeTo.setTime(11111L);
        docQueryParams.setServiceStartTimeTo(serviceStartTimeTo);

        Date serviceStopTimeFrom = new Date();
        serviceStopTimeFrom.setTime(11111L);
        docQueryParams.setServiceStopTimeFrom(serviceStopTimeFrom);

        Date serviceStopTimeTo = new Date();
        serviceStopTimeTo.setTime(11111L);
        docQueryParams.setServiceStopTimeTo(serviceStopTimeTo);

        List<EventCodeParam> eventCodeParamList = new ArrayList<>();
        EventCodeParam eventCodeParam = new EventCodeParam();
        eventCodeParam.setEventCode("event code");
        eventCodeParam.setEventCodeScheme("event code scheme");
        eventCodeParamList.add(eventCodeParam);
        docQueryParams.setEventCodeParams(eventCodeParamList);

        return docQueryParams;
    }
}
