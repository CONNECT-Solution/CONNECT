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
package gov.hhs.fha.nhinc.subscription.repository.roottopicextractor;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author rayj
 */
@Ignore
public class removeNamespaceHelperTest {

    public removeNamespaceHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void removeNamespaceHolderSimple() {
        assertEquals("value", RootTopicExtractorHelper.removeNamespaceHolder("{namespace}value"));
    }

    @Test
    public void removeNamespaceHolderPreData() {
        assertEquals("xxvalue", RootTopicExtractorHelper.removeNamespaceHolder("xx{namespace}value"));
    }

    @Test
    public void removeNamespaceHolderMultipleParts() {
        assertEquals("value/value2",
                RootTopicExtractorHelper.removeNamespaceHolder("{namespace}value/{namespace2}value2"));
    }

    @Test
    public void removeNamespaceHolderEmptyNamespace() {
        assertEquals("value", RootTopicExtractorHelper.removeNamespaceHolder("{}value"));
    }

    @Test
    public void removeNamespaceHolderNoNamespace() {
        assertEquals("value", RootTopicExtractorHelper.removeNamespaceHolder("value"));
    }

    @Test
    public void testReplaceNamespacePrefixesWithNamespaces() throws Exception {
    }
}