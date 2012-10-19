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
package gov.hhs.fha.nhinc.docretrieve.entity;

import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author mweaver
 */
public class OutboundDocRetrieveOrchestratableImpl_a0Test {

    public OutboundDocRetrieveOrchestratableImpl_a0Test() {
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
     * Test of getResponse method, of class EntityDocRetrieveOrchestratableImpl_a0.
     */
    @Test
    public void testResponse() {
        OutboundDocRetrieveOrchestratableFactory factory = new OutboundDocRetrieveOrchestratableFactory();
        OutboundDocRetrieveOrchestratableImpl instance = factory.getEntityDocRetrieveOrchestratableImpl_a0();
        RetrieveDocumentSetResponseType expResult = factory.getRetrieveDocumentSetResponseType();
        instance.setResponse(expResult);
        RetrieveDocumentSetResponseType result = instance.getResponse();
        assertEquals(expResult, result);
    }
    @Test
    public void testResponsePartialSuccess(){
        OutboundDocRetrieveOrchestratableFactory factory = new OutboundDocRetrieveOrchestratableFactory();
        OutboundDocRetrieveOrchestratableImpl instance = factory.getEntityDocRetrieveOrchestratableImpl_a0();
        RetrieveDocumentSetResponseType expResult = factory.getRetrieveDocumentSetResponseTypePartialSuccess();
        instance.setResponse(expResult);
        RetrieveDocumentSetResponseType result = instance.getResponse();
        assertEquals(expResult, result); 
    }
}