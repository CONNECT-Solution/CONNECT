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
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.docquery.outbound.StandardOutboundDocQueryHelper;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.logging.transaction.TransactionLogger;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.PixRetrieveBuilder;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyObjectFactory;
import java.util.Set;
import org.hl7.v3.II;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * @author akong
 *
 */
public class AggregationServiceTest {

    @Test
    public void ensureNoDuplicates() {
        II id1 = new II();
        id1.setRoot("1.1");
        id1.setExtension("patientId");
        id1.setAssigningAuthorityName("AA");

        II id2 = new II();
        id2.setRoot("1.1");
        id2.setExtension("patientId");
        id2.setAssigningAuthorityName("AA");

        II[] idArray = new II[2];
        idArray[0] = id1;
        idArray[1] = id2;

        TransactionLogger transactionLogger = mock(TransactionLogger.class);

        AggregationService service = new AggregationService(ExchangeManager.getInstance(),
            new PatientCorrelationProxyObjectFactory(), new PixRetrieveBuilder(), new StandardOutboundDocQueryHelper(),
            transactionLogger);
        Set<II> idSet = service.removeDuplicates(java.util.Arrays.asList(idArray));

        assertEquals(1, idSet.size());

        II[] noDupesArray = idSet.toArray(new II[0]);
        assertEquals(id1.getAssigningAuthorityName(), noDupesArray[0].getAssigningAuthorityName());
        assertEquals(id1.getExtension(), noDupesArray[0].getExtension());
        assertEquals(id1.getRoot(), noDupesArray[0].getRoot());
        assertEquals(id1.isDisplayable(), noDupesArray[0].isDisplayable());

    }
}
