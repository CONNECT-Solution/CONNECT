/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This is a JUNIT test that is used to test the AdapterPIPProxyObjectFactory class.
 *
 * @author Les Westberg
 */
@Ignore
// ToDo: Move this test to Integration Suite
public class AdapterPIPProxyObjectFactoryTest {

    /**
     * Default constructor.
     */
    public AdapterPIPProxyObjectFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getAdapterPIPProxy method, of class AdapterPIPProxyObjectFactory.
     */
    @Test
    public void testAdapterPIPProxyObjectFactory() {
        System.out.println("Begin testAdapterPIPProxyObjectFactory");
        try {
            AdapterPIPProxyObjectFactory oFactory = new AdapterPIPProxyObjectFactory();
            AdapterPIPProxy oAdapterPIPProxy = oFactory.getAdapterPIPProxy();
            assertNotNull(oAdapterPIPProxy);
            assertTrue("Adapter PIP was not a web service proxy.",
                    (oAdapterPIPProxy instanceof AdapterPIPProxyNoOpImpl));
        } catch (Throwable t) {
            System.out.println("Exception in testAdapterPIPProxyObjectFactory: " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }

        System.out.println("End testAdapterPIPProxyObjectFactory");

    }

}