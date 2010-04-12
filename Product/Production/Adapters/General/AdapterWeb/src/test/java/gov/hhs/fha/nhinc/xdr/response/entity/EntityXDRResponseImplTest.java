package gov.hhs.fha.nhinc.xdr.response.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.response.EntityXDRSecuredResponsePortType;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.response.EntityXDRSecuredResponseService;
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
public class EntityXDRResponseImplTest
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
    public void testProvideAndRegisterDocumentSetBResponse()
    {
        final Log mockLogger = context.mock(Log.class);
        final EntityXDRSecuredResponseService mockService = context.mock(EntityXDRSecuredResponseService.class);

        EntityXDRResponseImpl sut = new EntityXDRResponseImpl()
        {
            @Override
            protected void setRequestContext(AssertionType assertion, String url, EntityXDRSecuredResponsePortType port)
            {
            }

            @Override
            protected Log createLogger()
            {
                return mockLogger;
            }

            @Override
            protected EntityXDRSecuredResponseService createService()
            {
                return mockService;
            }

            @Override
            protected String getURL()
            {
                return "Mock Sucess";
            }

            @Override
            protected EntityXDRSecuredResponsePortType getPort(String url)
            {
                EntityXDRSecuredResponsePortType port = new EntityXDRSecuredResponsePortType()
                {
                    @Override
                    public AcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType arg0)
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

        RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType();

        AcknowledgementType ack = sut.provideAndRegisterDocumentSetBResponse(request);

        assertNotNull("Ack was null", ack);
        assertEquals("Response message incorrect", "Mock Success", ack.getMessage());
    }

}