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
package gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Adapter policy engine orchestrator proxy object factory. Used to obtain a proxy object for sending messages to the
 * adapter policy engine orchestration service.
 *
 * @author Les westberg
 */
public class AdapterPolicyEngineOrchProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "PolicyEngineProxyConfig.xml";
    private static final String BEAN_NAME_ADAPTER_POLICY_ENGINE_ORCHESTRATOR = "adapterpolicyengineorchestrator";

    @Override
    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    /**
     * Retrieve an adapter policy engine orchestrator implementation using the IOC framework. This method retrieves the
     * object from the framework that has an identifier of "adapterpolicyengineorchestrator."
     *
     * @return AdapterPolicyEngineOrchestratorProxy instance
     */
    public AdapterPolicyEngineOrchProxy getAdapterPolicyEngineOrchProxy() {
        return getBean(BEAN_NAME_ADAPTER_POLICY_ENGINE_ORCHESTRATOR, AdapterPolicyEngineOrchProxy.class);
    }
}
