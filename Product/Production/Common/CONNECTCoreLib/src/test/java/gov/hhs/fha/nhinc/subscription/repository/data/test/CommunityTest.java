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
package gov.hhs.fha.nhinc.subscription.repository.data.test;

import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.subscription.repository.data.Community;
import org.junit.Ignore;

/**
 * Unit test for the Community data class
 * 
 * @author Neil Webb
 */
@Ignore
public class CommunityTest {
    @Test
    public void testGettersAndSetters() {
        System.out.println("Begin testGettersAndSetters");
        try {
            String communityId = "CommunityId";
            String communityName = "CommunityName";

            Community comm = new Community();

            // Set values using setters
            comm.setCommunityId(communityId);
            comm.setCommunityName(communityName);

            // Validate getters
            assertEquals("Community id", communityId, comm.getCommunityId());
            assertEquals("Community name", communityName, comm.getCommunityName());

        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testGettersAndSetters");
    }

    @Test
    public void testEquals() {
        System.out.println("Begin testEquals");
        try {
            // Equals - both values populated
            Community c1 = new Community();
            c1.setCommunityId("CommunityId1");
            c1.setCommunityName("CommunityName1");
            Community c2 = new Community();
            c2.setCommunityId("CommunityId1");
            c2.setCommunityName("CommunityName1");
            assertTrue("Equals - both values populated", c1.equals(c2));

            // Equals - only id
            c1 = new Community();
            c1.setCommunityId("CommunityId1");
            c2 = new Community();
            c2.setCommunityId("CommunityId1");
            assertTrue("Equals - only id", c1.equals(c2));

            // Equals - only name
            c1 = new Community();
            c1.setCommunityName("CommunityName1");
            c2 = new Community();
            c2.setCommunityName("CommunityName1");
            assertTrue("Equals - only name", c1.equals(c2));

            // Equals - all null
            c1 = new Community();
            c2 = new Community();
            assertTrue("Equals - all null", c1.equals(c2));

            // Not equals - Only first id
            c1 = new Community();
            c1.setCommunityId("CommunityId1");
            c2 = new Community();
            assertFalse("Not equals - Only first id", c1.equals(c2));

            // Not equals - Only second id
            c1 = new Community();
            c2 = new Community();
            c2.setCommunityId("CommunityId2");
            assertFalse("Not equals - Only second id", c1.equals(c2));

            // Not equals - Only first name
            c1 = new Community();
            c1.setCommunityName("CommunityName1");
            c2 = new Community();
            assertFalse("Not equals - Only first name", c1.equals(c2));

            // Not equals - Only second name
            c1 = new Community();
            c2 = new Community();
            c2.setCommunityName("CommunityName2");
            assertFalse("Not equals - Only second name", c1.equals(c2));

            // Not equals - Only first id and name
            c1 = new Community();
            c1.setCommunityId("CommunityId1");
            c1.setCommunityName("CommunityName1");
            c2 = new Community();
            assertFalse("", c1.equals(c2));

            // Not equals - Only second id and name
            c1 = new Community();
            c2 = new Community();
            c2.setCommunityId("CommunityId2");
            c2.setCommunityName("CommunityName2");
            assertFalse("Not equals - Only second id and name", c1.equals(c2));

            // Not equals - First id and name, only second id
            c1 = new Community();
            c1.setCommunityId("CommunityId1");
            c1.setCommunityName("CommunityName1");
            c2 = new Community();
            c2.setCommunityId("CommunityId1");
            assertFalse("Not equals - First id and name, only second id", c1.equals(c2));

            // Not equals - First id and name, only second name
            c1 = new Community();
            c1.setCommunityId("CommunityId1");
            c1.setCommunityName("CommunityName1");
            c2 = new Community();
            c2.setCommunityName("CommunityName1");
            assertFalse("Not equals - First id and name, only second name", c1.equals(c2));

            // Not equals - Second id and name, only first id
            c1 = new Community();
            c1.setCommunityId("CommunityId1");
            c2 = new Community();
            c2.setCommunityId("CommunityId1");
            c2.setCommunityName("CommunityName1");
            assertFalse("Not equals - Second id and name, only first id", c1.equals(c2));

            // Not equals - Second id and name, only first name
            c1 = new Community();
            c1.setCommunityName("CommunityName1");
            c2 = new Community();
            c2.setCommunityId("CommunityId1");
            c2.setCommunityName("CommunityName1");
            assertFalse("Not equals - Second id and name, only first name", c1.equals(c2));

            // Not equals - Only id, no match
            c1 = new Community();
            c1.setCommunityId("CommunityId1");
            c2 = new Community();
            c2.setCommunityId("CommunityId2");
            assertFalse("Not equals - Only id, no match", c1.equals(c2));

            // Not equals - Only name, no match
            c1 = new Community();
            c1.setCommunityName("CommunityName1");
            c2 = new Community();
            c2.setCommunityName("CommunityName2");
            assertFalse("Not equals - Only name, no match", c1.equals(c2));

            // Not equals - Both populated, both different
            c1 = new Community();
            c1.setCommunityId("CommunityId1");
            c1.setCommunityName("CommunityName1");
            c2 = new Community();
            c2.setCommunityId("CommunityId2");
            c2.setCommunityName("CommunityName2");
            assertFalse("Not equals - Both populated, both different", c1.equals(c2));

            // Not equals - Both populated, id different
            c1 = new Community();
            c1.setCommunityId("CommunityId1");
            c1.setCommunityName("CommunityName1");
            c2 = new Community();
            c2.setCommunityId("CommunityId2");
            c2.setCommunityName("CommunityName1");
            assertFalse("Not equals - Both populated, id different", c1.equals(c2));

            // Not equals - Both populated, name different
            c1 = new Community();
            c1.setCommunityId("CommunityId1");
            c1.setCommunityName("CommunityName1");
            c2 = new Community();
            c2.setCommunityId("CommunityId1");
            c2.setCommunityName("CommunityName2");
            assertFalse("Not equals - Both populated, name different", c1.equals(c2));

        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testEquals");
    }
}