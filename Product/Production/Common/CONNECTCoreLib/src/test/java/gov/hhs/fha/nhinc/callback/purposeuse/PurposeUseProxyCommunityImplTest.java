/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.callback.purposeuse;

import gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.IPropertyAcessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PurposeUseProxyCommunityImplTest extends PurposeUseProxyTest {

	@Test
	public void testPurposeForEnabledWhenHcidTrue()
			throws PropertyAccessException {
		PurposeUseProxy testPurposeUseProxy = new PurposeUseProxyCommunityImpl(
				mockPropertyAccessor);
		String hcid = "1234";
		setPropertyExpectation(Expectations.returnValue(null),
				NhincConstants.GATEWAY_PROPERTY_FILE, "purposeForUseEnabled");
		setPropertyExpectation(Expectations.returnValue("true"), "purposeUse",
				hcid);
		setTargetHcidExpectation(Expectations.returnValue(hcid));
		Assert.assertTrue(testPurposeUseProxy
				.isPurposeForUseEnabled(mockCallbackProperties));
	}

	@Test
	public void testPurposeForDisabledWhenHcidFalse()
			throws PropertyAccessException {
		PurposeUseProxy testPurposeUseProxy = new PurposeUseProxyCommunityImpl(
				mockPropertyAccessor);
		String hcid = "1234";
		setPropertyExpectation(Expectations.returnValue("true"),
				NhincConstants.GATEWAY_PROPERTY_FILE, "purposeForUseEnabled");
		setPropertyExpectation(Expectations.returnValue("false"), "purposeUse",
				hcid);
		setTargetHcidExpectation(Expectations.returnValue(hcid));
		Assert.assertFalse(testPurposeUseProxy
				.isPurposeForUseEnabled(mockCallbackProperties));
	}

	@Test
	public void testPurposeForDisabledWhenHcidPointless()
			throws PropertyAccessException {
		PurposeUseProxy testPurposeUseProxy = new PurposeUseProxyCommunityImpl(
				mockPropertyAccessor);
		String hcid = "1234";
		setPropertyExpectation(Expectations.returnValue("true"),
				NhincConstants.GATEWAY_PROPERTY_FILE, "purposeForUseEnabled");
		setPropertyExpectation(
				Expectations.returnValue("my cat's name is mittens."),
				"purposeUse", hcid);
		setTargetHcidExpectation(Expectations.returnValue(hcid));
		Assert.assertFalse(testPurposeUseProxy
				.isPurposeForUseEnabled(mockCallbackProperties));
	}

	@Test
	public void testPurposeForDisabledWhenHcidMissing()
			throws PropertyAccessException {
		PurposeUseProxy testPurposeUseProxy = new PurposeUseProxyCommunityImpl(
				mockPropertyAccessor);
		String hcid = "1234";
		setPropertyExpectation(Expectations.returnValue("true"),
				NhincConstants.GATEWAY_PROPERTY_FILE, "purposeForUseEnabled");
		setPropertyExpectation(Expectations.returnValue(null), "purposeUse",
				hcid);
		setTargetHcidExpectation(Expectations.returnValue(hcid));
		Assert.assertFalse(testPurposeUseProxy
				.isPurposeForUseEnabled(mockCallbackProperties));
	}

	@Test
	public void testPurposeForUseEnabled_Exception()
			throws PropertyAccessException {
		String hcid = "1234";
		IPropertyAcessor propAccessor = mock(IPropertyAcessor.class);
		PurposeUseProxy testPurposeUseProxy = new PurposeUseProxyCommunityImpl(
				propAccessor);
		CallbackProperties callbackProps = mock(CallbackProperties.class);

		when(callbackProps.getTargetHomeCommunityId()).thenReturn(hcid);
		when(propAccessor.getProperty("purposeUse", hcid)).thenThrow(
				new PropertyAccessException());

		Assert.assertFalse(testPurposeUseProxy
				.isPurposeForUseEnabled(callbackProps));
	}

}
