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

import java.util.Collection;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.nhindirect.common.audit.AuditEvent;
import org.nhindirect.common.audit.Auditor;

/**
 * @author mweaver
 *
 */
public class MockDirectRIAuditorTest extends DirectRIAuditorTest {

    private Auditor mockAuditor = mock(Auditor.class);

    @Test
    @Override
    public void testWithNoProperties() {
        super.testWithNoProperties();
        verify(mockAuditor).audit(eq("this is my principal."), any(AuditEvent.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    @Override
    public void testWithProperties() {
        super.testWithProperties();
        verify(mockAuditor).audit(eq("this is my principal."), any(AuditEvent.class), any(Collection.class));
    }

    @Test(expected = IllegalArgumentException.class)
    @Override
    public void testWithAllNulls() {
        DirectRIAuditor auditor = getDirectRIAuditor();
        auditor.audit(null, null, null, null);
    }

    @Override
    protected DirectRIAuditor getDirectRIAuditor() {
        return new DirectRIAuditor() {
            @Override
            protected Auditor getAuditor() {
                return mockAuditor;
            }
        };
    }

}
