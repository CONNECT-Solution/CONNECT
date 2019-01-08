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
package gov.hhs.fha.nhinc.event;

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.event.builder.AssertionDescriptionExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder that pulls NPI and InitiatingHCID from an Assertion. To use, subclasses should dispatch the call to
 * <code>setArguments</code> to <code>extractAssertion</code>.
 */
public abstract class AssertionEventDescriptionBuilder extends BaseEventDescriptionBuilder {

    private AssertionDescriptionExtractor assertionExtractor = new AssertionDescriptionExtractor();
    protected Optional<AssertionType> assertion = Optional.absent();
    private static final Logger LOG = LoggerFactory.getLogger(AssertionEventDescriptionBuilder.class);

    @Override
    public final void buildNPI() {
        if (assertion.isPresent()) {
            setNpi(assertionExtractor.getNPI(assertion.get()));
        }
    }

    @Override
    public final void buildInitiatingHCID() {
        if (assertion.isPresent()) {
            setInitiatingHCID(assertionExtractor.getInitiatingHCID(assertion.get()));
        }
    }

    /**
     * Find the AssertionType from the provided argument list.
     *
     * @param arguments argument list to search. may be null.
     */
    protected final void extractAssertion(Object... arguments) {
        if (arguments != null) {
            AssertionType assertObj = getAssertion(arguments);
            if (assertObj != null) {
                assertion = Optional.of(assertObj);
                return;
            }
        }
        // Extracts if AssertionType was not availbale as an argument but the context is.
        try {
            if (assertion == null || !assertion.isPresent()) {
                AssertionType contextAssertion = SAML2AssertionExtractor.getInstance()
                    .extractSamlAssertion(getContext());
                if (contextAssertion != null) {
                    assertion = Optional.of(contextAssertion);
                    return;
                }
            }
        } catch (Exception e) {
            LOG.warn("Unable to extract assertion from context.", e);
        }

        assertion = Optional.absent();
    }

    /**
     * For testing, to make sure subclasses called extractAssertion.
     *
     * @return current assertion value
     */
    public final Optional<AssertionType> getAssertion() {
        return assertion;
    }

    /**
     * Dependency injection (for tests).
     *
     * @param assertionExtractor new extractor
     */
    public final void setAssertionExtractor(AssertionDescriptionExtractor assertionExtractor) {
        this.assertionExtractor = assertionExtractor;
    }

    protected WebServiceContext getContext() {
        return new WebServiceContextImpl();
    }

    private AssertionType getAssertion(Object... arguments) {
        AssertionType assertObj = null;
        for (Object obj : arguments) {
            assertObj = assertionExtractor.getAssertion(obj);
            if (assertObj != null) {
                return assertObj;
            }
        }
        return assertObj;
    }
}
