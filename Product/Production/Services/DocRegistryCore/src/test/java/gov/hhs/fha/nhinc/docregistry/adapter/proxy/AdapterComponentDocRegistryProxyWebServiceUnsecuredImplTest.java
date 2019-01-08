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
package gov.hhs.fha.nhinc.docregistry.adapter.proxy;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.docquery.AdhocQueryResponseAsserter;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.junit.Test;
import org.mockito.Mockito;

public class AdapterComponentDocRegistryProxyWebServiceUnsecuredImplTest {

    @SuppressWarnings("unchecked")
    @Test
    public void errorResponseHasRegistryObjectList() throws Exception {
        // Note on the mocking here: the class under test is not well suited to unit testing.
        // Using calls_real_methods and an exception uses knowledge of the try/catch block's structure.
        WebServiceProxyHelper proxyMock = mock(WebServiceProxyHelper.class);
        when(proxyMock.getAdapterEndPointFromConnectionManager(anyString())).thenThrow(RuntimeException.class);

        AdapterComponentDocRegistryProxyWebServiceUnsecuredImpl impl = mock(
                AdapterComponentDocRegistryProxyWebServiceUnsecuredImpl.class, Mockito.CALLS_REAL_METHODS);
        when(impl.createWebServiceProxyHelper()).thenReturn(proxyMock);

        try {
            impl.registryStoredQuery(null, null);
            fail("Did not throw ErrorEventException");
        } catch (Exception e) {
            assertTrue(e instanceof ErrorEventException);
            ErrorEventException error = (ErrorEventException) e;
            AdhocQueryResponseAsserter.assertSchemaCompliant((AdhocQueryResponse) error.getReturnOverride());
        }


    }
}
