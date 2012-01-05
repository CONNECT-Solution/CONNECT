/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mweaver
 */
public class EntityDocRetrieveAuditTransformer_a0Test {

    private Mockery mockingContext;
    private AuditRepositoryLogger mockedDependency;

    public EntityDocRetrieveAuditTransformer_a0Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        mockingContext = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
        mockedDependency = mockingContext.mock(AuditRepositoryLogger.class);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of transformRequest method, of class EntityDocRetrieveAuditTransformer_a0.
     */
    @Test
    public void testTransformRequest() {
        EntityDocRetrieveOrchestratableFactory factory = new EntityDocRetrieveOrchestratableFactory();
        Orchestratable message = factory.getEntityDocRetrieveOrchestratableImpl_a0();
        EntityDocRetrieveAuditTransformer_a0 instance = new EntityDocRetrieveAuditTransformer_a0();
        
        mockingContext.checking(new Expectations() {
            {
                one
                (mockedDependency).logDocRetrieve(with(any(DocRetrieveMessageType.class)), with(any(String.class)), with(any(String.class)), with(any(String.class)));
                will
                (returnValue(null));
            }
        });
        LogEventRequestType result = instance.transformRequest(message);
        assertEquals("Inbound", result.getDirection());
        assertEquals("Nhin", result.getInterface());
    }

    /**
     * Test of transformResponse method, of class EntityDocRetrieveAuditTransformer_a0.
     */
    @Test
    public void testTransformResponse() {
        EntityDocRetrieveOrchestratableFactory factory = new EntityDocRetrieveOrchestratableFactory();
        Orchestratable message = factory.getEntityDocRetrieveOrchestratableImpl_a0();
        EntityDocRetrieveAuditTransformer_a0 instance = new EntityDocRetrieveAuditTransformer_a0();

        mockingContext.checking(new Expectations() {
            {
                one
                (mockedDependency).logDocRetrieve(with(any(DocRetrieveMessageType.class)), with(any(String.class)), with(any(String.class)), with(any(String.class)));
                will
                (returnValue(null));
            }
        });
        LogEventRequestType result = instance.transformResponse(message);
        assertEquals("Outbound", result.getDirection());
        assertEquals("Nhin", result.getInterface());
    }
}
