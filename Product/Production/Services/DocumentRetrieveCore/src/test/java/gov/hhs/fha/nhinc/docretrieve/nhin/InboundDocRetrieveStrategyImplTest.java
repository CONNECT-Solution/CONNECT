package gov.hhs.fha.nhinc.docretrieve.nhin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryDocumentRetrieveLogger;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.AdapterDocRetrieveProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.InboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;

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
    private AuditRepositoryDocumentRetrieveLogger auditLog;

    @Before
    public void setup() {
        adapterproxy = mock(AdapterDocRetrieveProxy.class);
        pt = mock(PolicyTransformer.class);
        at = mock(AuditTransformer.class);
        d = mock(InboundDelegate.class);
        auditLog = mock(AuditRepositoryDocumentRetrieveLogger.class);
    }

    @Test
    public void testExecuteMethodForPassthru() {

        InboundPassthroughDocRetrieveOrchestratable passthrough = new InboundPassthroughDocRetrieveOrchestratable(pt,
                at, d);

        InboundDocRetrieveStrategyImpl inboundDocRetrieve = new InboundDocRetrieveStrategyImpl(adapterproxy, auditLog) {
            @Override
            public RetrieveDocumentSetResponseType sendToAdapter(InboundDocRetrieveOrchestratable message) {
                return new RetrieveDocumentSetResponseType();
            }

        };

        inboundDocRetrieve.execute(passthrough);

        verify(auditLog, never()).logDocRetrieve(any(DocRetrieveMessageType.class), anyString(), anyString(),
                anyString());

        verify(auditLog, never()).logDocRetrieveResult(any(DocRetrieveResponseMessageType.class), anyString(),
                anyString(), anyString());

    }

    @Test
    public void testExceuteMethodForStandard() {

        InboundStandardDocRetrieveOrchestratable standard = new InboundStandardDocRetrieveOrchestratable(pt, at, d);

        InboundDocRetrieveStrategyImpl inboundDocRetrieve = new InboundDocRetrieveStrategyImpl(adapterproxy, auditLog) {
            @Override
            public RetrieveDocumentSetResponseType sendToAdapter(InboundDocRetrieveOrchestratable message) {
                return new RetrieveDocumentSetResponseType();
            }
        };

        inboundDocRetrieve.execute(standard);
        verify(auditLog).logDocRetrieve(any(DocRetrieveMessageType.class),
                eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE),
                anyString());

        verify(auditLog).logDocRetrieveResult(any(DocRetrieveResponseMessageType.class),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE),
                anyString());
    }
}
