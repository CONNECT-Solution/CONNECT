package gov.hhs.fha.nhinc.docretrieve.aspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.event.ArgTransformerEventDescriptionBuilder;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

import org.junit.Before;
import org.junit.Test;

public abstract class ArgTransformerTest<T extends ArgTransformerEventDescriptionBuilder> {
    private ArgTransformerEventDescriptionBuilder builder;

    @Before
    public void before() {
        builder = getBuilder();
    }

    protected abstract T getBuilder();

    @Test
    public void delegateIsCorrectType() {
        assertEquals(RetrieveDocumentSetRequestTypeDescriptionBuilder.class, builder.getDelegate().getClass());
    }

    @Test
    public void transformArguments() {
        RetrieveDocumentSetRequestType mockRequest = mock(RetrieveDocumentSetRequestType.class);
        AssertionType mockAssertion = mock(AssertionType.class);

        Object request = getArgument(mockRequest, mockAssertion);

        Object[] transformArguments = builder.transformArguments(new Object[] { request });
        assertNotNull(transformArguments);
        assertTrue(2 >= transformArguments.length);
        assertEquals(mockRequest, transformArguments[0]);
        if (transformArguments.length > 1) {
            assertEquals(mockAssertion, transformArguments[1]);
        }
    }

    protected abstract Object getArgument(RetrieveDocumentSetRequestType mockRequest, AssertionType mockAssertion);

    @Test
    public void transformReturnValue() {
        Object o = new Object();
        assertEquals(o, builder.transformReturnValue(o));
    }
}
