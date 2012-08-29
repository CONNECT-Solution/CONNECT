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

import gov.hhs.fha.nhinc.subscription.repository.data.Criterion;
import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.subscription.repository.data.Criterion;
import org.junit.Ignore;

/**
 * Unit test for criterion class
 * 
 * @author Neil Webb
 */
@Ignore
public class CriterionTest {
    @Test
    public void testGettersAndSetters() {
        System.out.println("Begin testGettersAndSetters");
        try {
            String key = "CriterionKey";
            String value = "CriterionValue";
            Criterion crit = new Criterion();
            crit.setKey(key);
            crit.setValue(value);

            assertEquals("Key", key, crit.getKey());
            assertEquals("Value", value, crit.getValue());
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
            // Equals - both
            Criterion crit1 = new Criterion();
            crit1.setKey("Key1");
            crit1.setValue("Value1");
            Criterion crit2 = new Criterion();
            crit2.setKey("Key1");
            crit2.setValue("Value1");
            assertTrue("Equals - both", crit1.equals(crit2));

            // Equals - only key
            crit1 = new Criterion();
            crit1.setKey("Key1");
            crit2 = new Criterion();
            crit2.setKey("Key1");
            assertTrue("Equals - only key", crit1.equals(crit2));

            // Equals - only value
            crit1 = new Criterion();
            crit1.setValue("Value1");
            crit2 = new Criterion();
            crit2.setValue("Value1");
            assertTrue("Equals - only value", crit1.equals(crit2));

            // Not equal - both populated, key different
            crit1 = new Criterion();
            crit1.setKey("Key1");
            crit1.setValue("Value1");
            crit2 = new Criterion();
            crit2.setKey("Key2");
            crit2.setValue("Value1");
            assertFalse("Not equal - both populated, key different", crit1.equals(crit2));

            // Not equal - both populated, value different
            crit1 = new Criterion();
            crit1.setKey("Key1");
            crit1.setValue("Value1");
            crit2 = new Criterion();
            crit2.setKey("Key1");
            crit2.setValue("Value2");
            assertFalse("Not equal - both populated, value different", crit1.equals(crit2));

            // Not equal - c1 full, c2 key only
            crit1 = new Criterion();
            crit1.setKey("Key1");
            crit1.setValue("Value1");
            crit2 = new Criterion();
            crit2.setKey("Key1");
            assertFalse("Not equal - c1 full, c2 key only", crit1.equals(crit2));

            // Not equal - c1 full, c2 value only
            crit1 = new Criterion();
            crit1.setKey("Key1");
            crit1.setValue("Value1");
            crit2 = new Criterion();
            crit2.setValue("Value1");
            assertFalse("Not equal - c1 full, c2 value only", crit1.equals(crit2));

            // Not equal - c2 full, c1 key only
            crit1 = new Criterion();
            crit1.setKey("Key1");
            crit2 = new Criterion();
            crit2.setKey("Key1");
            crit2.setValue("Value1");
            assertFalse("Not equal - c2 full, c1 key only", crit1.equals(crit2));

            // Not equal - c2 full, c1 value only
            crit1 = new Criterion();
            crit1.setValue("Value1");
            crit2 = new Criterion();
            crit2.setKey("Key1");
            crit2.setValue("Value1");
            assertFalse("Not equal - c2 full, c1 value only", crit1.equals(crit2));

            // Not equal - c1 full, c2 null
            crit1 = new Criterion();
            crit1.setKey("Key1");
            crit1.setValue("Value1");
            crit2 = null;
            assertFalse("Not equal - c1 full, c2 null", crit1.equals(crit2));

        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("Begin testEquals");
    }
}