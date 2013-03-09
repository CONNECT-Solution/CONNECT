package gov.hhs.fha.nhinc.callback.openSAML;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SAMLAssertionBuilderFactoryImplTest {

	@Test
	public void getBuilderTest() {
		SAMLAssertionBuilderFactoryImpl builderFactory = new SAMLAssertionBuilderFactoryImpl();
		SAMLAssertionBuilder hokBuilder = builderFactory
				.getBuilder(SAMLAssertionBuilderFactory.HOK_ASSERTION_TYPE);
		assertTrue(hokBuilder instanceof HOKSAMLAssertionBuilder);
		SAMLAssertionBuilder svBuilder = builderFactory
				.getBuilder(SAMLAssertionBuilderFactory.SV_ASSERTION_TYPE);
		assertTrue(svBuilder instanceof SVSAMLAssertionBuilder);
	}
}
