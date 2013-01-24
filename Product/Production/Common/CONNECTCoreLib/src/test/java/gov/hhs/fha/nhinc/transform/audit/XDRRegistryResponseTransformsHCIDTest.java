/*
 * Copyright (c) 2009-13, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.transform.audit;

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.junit.Before;
import org.junit.Test;


public class XDRRegistryResponseTransformsHCIDTest {

    private static final String LOCAL_HCID = "localHCID";
    private static final String ASSERTION_HCID = "assertionHCID";
    private static final String TARGET_HCID = "targetHCID";
    
    private XDRMessageHelper messageHelper = new XDRMessageHelper();

    private XDRTransforms xdrTransform;
    private AssertionType assertion;
    private RegistryResponseType registryResponse;
    private NhinTargetSystemType target;

    @Before
    public void setup() {
        xdrTransform = createXDRTransforms();
        assertion = messageHelper.createAssertion(ASSERTION_HCID);
        target = messageHelper.createNhinTargetSystem(TARGET_HCID);
        registryResponse = messageHelper.createRegistryResponseType();
    }

    @Test
    public void getHcidNhinOutboundInRequestingSide() {
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_NHIN_INTERFACE;
        
        LogEventRequestType result = xdrTransform.transformResponseToAuditMsg(registryResponse, assertion, target,
                direction, _interface, true);
        
        assertEquals(TARGET_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
    }
    
    @Test
    public void getHcidNhinInboundInRequestingSide() { 
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_NHIN_INTERFACE;
        
        LogEventRequestType result = xdrTransform.transformResponseToAuditMsg(registryResponse, assertion, target,
                direction, _interface, true);
        
        assertEquals(TARGET_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
    }
    
    @Test
    public void getHcidNhinOutboundInRespondingSide() {
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_NHIN_INTERFACE;
        
        LogEventRequestType result = xdrTransform.transformResponseToAuditMsg(registryResponse, assertion, target,
                direction, _interface, false);
        
        assertEquals(ASSERTION_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
    }
    
    @Test
    public void getHcidNhinInboundInRespondingSide() { 
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_NHIN_INTERFACE;
        
        LogEventRequestType result = xdrTransform.transformResponseToAuditMsg(registryResponse, assertion, target,
                direction, _interface, false);
        
        assertEquals(ASSERTION_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
    }
    
    @Test
    public void getHcidEntityInboundInRequestingSide() { 
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ENTITY_INTERFACE;
        
        LogEventRequestType result = xdrTransform.transformResponseToAuditMsg(registryResponse, assertion, target,
                direction, _interface, true);        
        assertEquals(LOCAL_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
    }
    
    @Test
    public void getHcidEntityInboundInRespondingSide() { 
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ENTITY_INTERFACE;
        
        LogEventRequestType result = xdrTransform.transformResponseToAuditMsg(registryResponse, assertion, target,
                direction, _interface, false);        
        assertEquals(LOCAL_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
    }
    
    @Test
    public void getHcidEntityOutboundInRequestingSide() { 
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ENTITY_INTERFACE;
        
        LogEventRequestType result = xdrTransform.transformResponseToAuditMsg(registryResponse, assertion, target,
                direction, _interface, true);        
        assertEquals(LOCAL_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
    }
    
    @Test
    public void getHcidEntityOutboundInRespondingSide() { 
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ENTITY_INTERFACE;
        
        LogEventRequestType result = xdrTransform.transformResponseToAuditMsg(registryResponse, assertion, target,
                direction, _interface, false);        
        assertEquals(LOCAL_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
    }
    
    @Test
    public void getHcidAdapterInboundInRequestingSide() { 
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE;
        
        LogEventRequestType result = xdrTransform.transformResponseToAuditMsg(registryResponse, assertion, target,
                direction, _interface, true);       
        assertEquals(LOCAL_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
    }
    
    @Test
    public void getHcidAdapterInboundInRespondingSide() { 
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE;
        
        LogEventRequestType result = xdrTransform.transformResponseToAuditMsg(registryResponse, assertion, target,
                direction, _interface, false);       
        assertEquals(LOCAL_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
    }
    
    @Test
    public void getHcidAdapterOutboundInRequestingSide() { 
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE;
        
        LogEventRequestType result = xdrTransform.transformResponseToAuditMsg(registryResponse, assertion, target,
                direction, _interface, true);       
        assertEquals(LOCAL_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
    }
    
    @Test
    public void getHcidAdapterOutboundInRespondingSide() { 
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE;
        
        LogEventRequestType result = xdrTransform.transformResponseToAuditMsg(registryResponse, assertion, target,
                direction, _interface, false);       
        assertEquals(LOCAL_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
    }

    private XDRTransforms createXDRTransforms() {

        return new XDRTransforms() {
            @Override
            protected String getLocalHomeCommunityId() {
                return LOCAL_HCID;
            }
        };
    }
}
