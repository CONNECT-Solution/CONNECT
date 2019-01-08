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
package gov.hhs.fha.nhinc.corex12.ds.genericbatch.callback.cxf.largefile;

import gov.hhs.fha.nhinc.callback.cxf.largefile.TimestampValidator;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class overrides the default CONNECT Core TimestampValidator to get the the TimeStampTimeToLive, TimeStampStrict
 * and FutureTimeToLive properties specific to the CORE X12 Batch Services(Request and Response).
 *
 * @author Naresh Subramanyan
 */
public class X12BatchTimestampValidator extends TimestampValidator {

    private static final Logger LOG = LoggerFactory.getLogger(X12BatchTimestampValidator.class);

    @Override
    protected long getTimeStampTTL() {
        try {
            return PropertyAccessor.getInstance().getPropertyLong(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.X12_GENERIC_BATCH_TIMESTAMP_TIME_TO_LIVE);
        } catch (PropertyAccessException pae) {
            LOG.warn("Failed to determine timestamp TTL in gateway.properties, will use default values: {}",
                pae.getLocalizedMessage(), pae);
        }
        return INVALID_LONG_VALUE;
    }

    @Override
    protected long getTimeStampFutureTTL() {
        try {
            return PropertyAccessor.getInstance().getPropertyLong(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.X12_GENERIC_BATCH_TIMESTAMP_FUTURE_TIME_TO_LIVE);
        } catch (PropertyAccessException pae) {
            LOG.warn("Failed to determine timestamp future TTL in gateway.properties, will use default values: {}",
                pae.getLocalizedMessage(), pae);
        }
        return INVALID_LONG_VALUE;
    }

    @Override
    protected boolean getTimeStampStrict() {
        try {
            return PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.X12_GENERIC_BATCH_TIMESTAMP_STRICT);
        } catch (PropertyAccessException pae) {
            LOG.warn("Failed to determine timestamp strict in gateway.properties, will use default values: {}",
                pae.getLocalizedMessage(), pae);
        }
        return true;
    }
}
