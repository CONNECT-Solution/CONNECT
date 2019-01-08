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
package gov.hhs.fha.nhinc.admingui.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.uddi.api_v3.BusinessEntity;

/**
 *
 * @author achidamb
 */
public class ConnectionHelperTest {

    public ConnectionHelperTest() {
    }

    /**
     * Test of getRemoteHcidFromUUID method, of class RemoteOrganizationIdentifier. HappyPath TestCase
     */
    @Test
    public void testGetRemoteHcidFromUUID() {
        ConnectionHelper instance = new ConnectionHelper();
        List<String> actual = new ArrayList<>();
        Map<String, OrganizationType> result = instance.getRemoteHcidFromUUID();
        for (Map.Entry<String, OrganizationType> entry : result.entrySet()) {
            actual.add(entry.getKey());
        }
        assertTrue("Error communityname not available", actual.contains("urn:oid:1.1")); // DOD
        assertTrue("Error communityname not available", actual.contains("urn:oid:2.2")); // SSA
    }

    /**
     * * Test for Negative case
     *
     */
    @Test
    public void testGetRemoteHcidFromUUIDFromEmptyUDDIFile() {
        ConnectionHelper instance = new ConnectionHelper() {
            @Override
            protected List<OrganizationType> getAllOrganizations() {
                return null;
            }
        };
        Map<String, String> expResult = new HashMap<>();
        Map<String, OrganizationType> result = instance.getRemoteHcidFromUUID();
        assertEquals("Error empty UDDI file should not have entries", result, expResult);
    }

    @Test
    public void testGetExternalEntitiesMap() {
        List<String> actual = new ArrayList<>();
        ConnectionHelper instance = new ConnectionHelper();
        HashMap<String, OrganizationType> result = instance.getExternalOrganizationsMap();
        for (Map.Entry<String, OrganizationType> entry : result.entrySet()) {
            actual.add(entry.getKey());
        }
        assertTrue("Error communityname not available", actual.contains("urn:oid:2.2")); // SSA
    }

    @Test
    public void testGetExternalEntitiesMapFromEmptyFile() {
        ConnectionHelper instance = new ConnectionHelper() {
            @Override
            protected List<OrganizationType> getAllOrganizations() {
                return null;
            }
        };
        Map<String, BusinessEntity> expResult = new HashMap<>();
        HashMap<String, OrganizationType> result = instance.getExternalOrganizationsMap();
        assertEquals("Error empty UDDI file should not have entries", expResult, result);

    }

}
