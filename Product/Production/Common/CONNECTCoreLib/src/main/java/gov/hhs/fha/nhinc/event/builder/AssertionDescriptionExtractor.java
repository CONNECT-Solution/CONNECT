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
package gov.hhs.fha.nhinc.event.builder;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author akong
 *
 */
public class AssertionDescriptionExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(AssertionDescriptionExtractor.class);

    /**
     * Extracts the NPI from the AssertionType
     *
     * @param assertion the AssertionType whose values are to be extracted from
     * @return string containing the NPI value, null if it doesn't exists
     */
    public String getNPI(AssertionType assertion) {
        String npi = null;
        if (assertion != null) {
            npi = assertion.getNationalProviderId();
        }

        return npi;
    }

    /**
     * Extracts the initiating HCID from the AssertionType
     *
     * @param assertion the AssertionType whose values are to be extracted from
     * @return string containing the initiating hcid value, null if it doesn't exists
     */
    public String getInitiatingHCID(AssertionType assertion) {
        String hcid = null;
        if (hasHomeCommunity(assertion)) {
            return assertion.getHomeCommunity().getHomeCommunityId();
        }

        return hcid;
    }

    private static boolean hasHomeCommunity(AssertionType assertion) {
        return assertion != null && assertion.getHomeCommunity() != null;
    }

    public AssertionType getAssertion(Object obj) {
        try {
            if (obj != null) {
                if (obj instanceof AssertionType) {
                    return (AssertionType) obj;
                } else {
                    return getAssertionType(obj.getClass().getDeclaredMethods(), obj);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ex) {
            LOG.info("Unable to extract AssertionType from Object", ex);
        }
        return null;
    }

    private AssertionType getAssertionType(Method[] methods, Object obj) throws IllegalAccessException, InvocationTargetException {
        for (Method m : methods) {
            if (("getAssertion").equals(m.getName())) {
                return (AssertionType) m.invoke(obj);
            }
        }
        return null;
    }
}
