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
package gov.hhs.fha.nhinc.direct.xdr.audit;

import gov.hhs.fha.nhinc.direct.xdr.SoapEdgeContext;
import gov.hhs.fha.nhinc.direct.xdr.SoapEdgeContextMapImpl;
import java.util.UUID;
import org.junit.Test;

/**
 * @author mweaver
 *
 */
public class DirectRIAuditorTest {

    @Test
    public void testWithNoProperties() {
        DirectRIAuditor auditor = getDirectRIAuditor();
        auditor.audit("this is my principal.", "name", "type", null);
    }

    @Test
    public void testWithProperties() {
        DirectRIAuditor auditor = getDirectRIAuditor();
        auditor.audit("this is my principal.", "name", "type", getAuditable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithAllNulls() {
        DirectRIAuditor auditor = getDirectRIAuditor();
        auditor.audit(null, null, null, null);
    }

    protected DirectRIAuditor getDirectRIAuditor() {
        return new DirectRIAuditor();
    }


    protected SoapEdgeContext getAuditable() {
        SoapEdgeContext auditable = new SoapEdgeContextMapImpl();
        auditable.setEndpoint("test@direct.connectopensource.org");
        auditable.setMessageId(UUID.randomUUID().toString());
        auditable.setPatId("1234");
        auditable.setPid("80341");
        auditable.setRemoteHost("www.connectopensource.org");
        auditable.setThisHost("direct.connectopensource.org");
        auditable.setTo("drtony@direct.connectopensource.org");
        return auditable;
    }

}
