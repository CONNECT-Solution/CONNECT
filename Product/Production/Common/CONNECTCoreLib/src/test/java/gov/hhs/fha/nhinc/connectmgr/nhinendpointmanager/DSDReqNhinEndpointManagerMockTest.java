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
package gov.hhs.fha.nhinc.connectmgr.nhinendpointmanager;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants.NHIN_SERVICE_NAMES;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mweaver
 *
 */
public class DSDReqNhinEndpointManagerMockTest extends AbstractNhinEndpointManagerMockTest {

    /*-----------------Setup Methods---------------*/

    @Override
    protected NHIN_SERVICE_NAMES getService() {
        return NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION_DEFERRED_REQUEST;
    }

    /**
     * Setup expectations for 1.1 specs
     */
    @Override
    protected void set2010Expectations() {
        expectConnectionManagerCache11();
    }

    /**
     * Setup expectations for 2.0 specs
     */
    @Override
    protected void set2011Expectations() {
        expectConnectionManagerCache20();
    }

    /**
     * Setup for both DSDReq specs
     */
    @Override
    protected void expectConnectionManagerCacheBoth() {
        List<UDDI_SPEC_VERSION> list = new ArrayList<>();
        list.add(UDDI_SPEC_VERSION.SPEC_1_1);
        list.add(UDDI_SPEC_VERSION.SPEC_2_0);

        expectConnectionManagerCache(list);
    }

}
