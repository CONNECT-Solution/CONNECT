/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docretrieve.nhin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryDocumentRetrieveLogger;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.AdapterDocRetrieveProxy;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditLogger;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.InboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import java.util.Properties;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

import org.junit.Before;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class InboundDocRetrieveStrategyImplTest {

    private AdapterDocRetrieveProxy adapterproxy;
    private PolicyTransformer pt;
    private AuditTransformer at;
    private InboundDelegate d;
    private DocRetrieveAuditLogger auditLog;
       NhinTargetSystemType nhinTargetSystemType;
        private final    RetrieveDocumentSetRequestType    retrieveDocumentSetRequestType= mock(RetrieveDocumentSetRequestType.class);
      Properties  webContextProp =null;
      RetrieveDocumentSetRequestType request = null;
      RetrieveDocumentSetResponseType response = null;
      AssertionType assertion= null;
         RetrieveDocumentSetResponseType     retrieveDocumentSetResponseType=null;
        AssertionType assertionType = mock(AssertionType.class);

    @Before
    public void setup() {
          nhinTargetSystemType =null;
        adapterproxy = mock(AdapterDocRetrieveProxy.class);
        pt = mock(PolicyTransformer.class);
        at = mock(AuditTransformer.class);
        d = mock(InboundDelegate.class);
        auditLog = mock(DocRetrieveAuditLogger.class);
               

          
    }

    @Test
    public void testExecuteMethodForPassthru() {

        InboundPassthroughDocRetrieveOrchestratable passthrough = new InboundPassthroughDocRetrieveOrchestratable(pt,
                at, d);

        InboundDocRetrieveStrategyImpl inboundDocRetrieve = new InboundDocRetrieveStrategyImpl(adapterproxy,auditLog) {
            @Override
            public RetrieveDocumentSetResponseType sendToAdapter(InboundDocRetrieveOrchestratable message) {
               retrieveDocumentSetResponseType=  new RetrieveDocumentSetResponseType();
               return retrieveDocumentSetResponseType;
            }
             @Override
              protected NhinTargetSystemType getTargetNhinTargetSystemType(InboundDocRetrieveOrchestratable message){
                  return nhinTargetSystemType;
              }

        };

        inboundDocRetrieve.execute(passthrough);
        
        
       verify(auditLog,never()).auditRequestMessage(eq(request), eq(assertion), eq(nhinTargetSystemType), eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE), eq(Boolean.FALSE), eq(webContextProp), eq("RetrieveDocuments"));
       verify(auditLog,never()).auditResponseMessage(eq(request),eq(retrieveDocumentSetResponseType), eq(assertion), eq(nhinTargetSystemType), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),  eq(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE), eq(Boolean.FALSE),eq(webContextProp), eq(new String("RetrieveDocuments")));
    }

    @Test
    public void testExceuteMethodForStandard() {

        InboundStandardDocRetrieveOrchestratable standard = new InboundStandardDocRetrieveOrchestratable(pt, at, d);

        InboundDocRetrieveStrategyImpl inboundDocRetrieve = new InboundDocRetrieveStrategyImpl(adapterproxy, auditLog) {
            @Override
            public RetrieveDocumentSetResponseType sendToAdapter(InboundDocRetrieveOrchestratable message) {
                retrieveDocumentSetResponseType=  new RetrieveDocumentSetResponseType();
               return retrieveDocumentSetResponseType;
            }
             @Override
              protected NhinTargetSystemType getTargetNhinTargetSystemType(InboundDocRetrieveOrchestratable message){
                  return nhinTargetSystemType;
              }
        };
 
     inboundDocRetrieve.execute(standard);
      verify(auditLog).auditRequestMessage(eq(request), eq(assertion), eq(nhinTargetSystemType), eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE), eq(Boolean.FALSE), eq(webContextProp), eq("RetrieveDocuments"));
      verify(auditLog).auditResponseMessage(eq(request),eq(retrieveDocumentSetResponseType), eq(assertion), eq(nhinTargetSystemType), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),  eq(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE), eq(Boolean.FALSE),eq(webContextProp), eq(new String("RetrieveDocuments")));
    
    }
}
