package gov.hhs.fha.nhinc.xdr.request.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.request.EntityXDRSecuredRequestPortType;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.request.EntityXDRSecuredRequestService;
import ihe.iti.xdr._2007.AcknowledgementType;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
public class EntityXDRRequestImplTest
{
    private Mockery context;

    @Before
    public void setUp()
    {
        context = new Mockery()
        {
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testProvideAndRegisterDocumentSetBRequest()
    {
        final Log mockLogger = context.mock(Log.class);
        final EntityXDRSecuredRequestService mockService = context.mock(EntityXDRSecuredRequestService.class);

        EntityXDRRequestImpl sut = new EntityXDRRequestImpl()
        {
            @Override
            protected void setRequestContext(AssertionType assertion, String url, EntityXDRSecuredRequestPortType port)
            {
            }

            @Override
            protected Log createLogger()
            {
                return mockLogger;
            }

            @Override
            protected EntityXDRSecuredRequestService createService()
            {
                return mockService;
            }

            @Override
            protected String getURL()
            {
                return "Mock Sucess";
            }

            @Override
            protected EntityXDRSecuredRequestPortType getPort(String url)
            {
                EntityXDRSecuredRequestPortType port = new EntityXDRSecuredRequestPortType()
                {
                    @Override
                    public AcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType arg0)
                    {
                        AcknowledgementType ack = new AcknowledgementType();
                        ack.setMessage("Mock Success");
                        return ack;
                    }
                };
                return port;
            }
        };

        context.checking(new Expectations()
        {
            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
                will(returnValue(null));
            }
        });

        RespondingGatewayProvideAndRegisterDocumentSetRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetRequestType();

        AcknowledgementType ack = sut.provideAndRegisterDocumentSetBRequest(request);

        assertNotNull("Ack was null", ack);
        assertEquals("Response message incorrect", "Mock Success", ack.getMessage());
    }

}