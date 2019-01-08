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
package gov.hhs.fha.nhinc.asyncmsgs.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Blob;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author JHOPPESC
 */
public class AsyncMsgRecordTest {

    public AsyncMsgRecordTest() {
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
     * Test of getId and setMessageId methods, of class AsyncMsgRecord.
     */
    @Test
    public void testSetGetId() {
        System.out.println("testSetGetMessageId");

        AsyncMsgRecord instance = new AsyncMsgRecord();

        Long expResult = Long.valueOf(2);

        instance.setId(expResult);

        Long result = instance.getId();

        assertEquals(expResult, result);
    }

    /**
     * Test of getId method, of class AsyncMsgRecord.
     */
    @Test
    public void testGetInitId() {
        System.out.println("testGetInitMessageId");

        AsyncMsgRecord instance = new AsyncMsgRecord();

        Long result = instance.getId();

        assertNull(result);
    }

    /**
     * Test of getMessageId and setMessageId methods, of class AsyncMsgRecord.
     */
    @Test
    public void testSetGetMessageId() {
        System.out.println("testSetGetMessageId");

        AsyncMsgRecord instance = new AsyncMsgRecord();

        String expResult = "uuid:1234567890";
        instance.setMessageId(expResult);

        String result = instance.getMessageId();

        assertEquals(expResult, result);
    }

    /**
     * Test of getMessageId method, of class AsyncMsgRecord.
     */
    @Test
    public void testGetInitMessageId() {
        System.out.println("testGetInitMessageId");

        AsyncMsgRecord instance = new AsyncMsgRecord();

        String result = instance.getMessageId();

        assertNull(result);
    }

    /**
     * Test of getCreationTime and setCreationTime methods, of class AsyncMsgRecord.
     */
    @Test
    public void testSetGetCreationTime() {
        System.out.println("testSetGetCreationTime");

        AsyncMsgRecord instance = new AsyncMsgRecord();
        Date expResult = new Date();
        instance.setCreationTime(expResult);

        Date result = instance.getCreationTime();

        assertEquals(expResult, result);
    }

    /**
     * Test of getCreationTime method, of class AsyncMsgRecord.
     */
    @Test
    public void testGetInitCreationTime() {
        System.out.println("testGetInitCreationTime");

        AsyncMsgRecord instance = new AsyncMsgRecord();
        Date result = instance.getCreationTime();

        assertNull(result);
    }

    /**
     * Test of getServiceName and setServiceName methods, of class AsyncMsgRecord.
     */
    @Test
    public void testSetGetServiceName() {
        System.out.println("testSetGetServiceName");

        AsyncMsgRecord instance = new AsyncMsgRecord();
        String expResult = "PatientDiscovery";
        instance.setServiceName(expResult);

        String result = instance.getServiceName();

        assertEquals(expResult, result);
    }

    /**
     * Test of getServiceName method, of class AsyncMsgRecord.
     */
    @Test
    public void testGetInitServiceName() {
        System.out.println("testGetInitServiceName");

        AsyncMsgRecord instance = new AsyncMsgRecord();

        String result = instance.getServiceName();

        assertNull(result);
    }

    /**
     * Test of getMsgData method, of class AsyncMsgRecord.
     */
    @Test
    public void testGetInitMsgData() {
        System.out.println("testGetInitMsgData");
        AsyncMsgRecord instance = new AsyncMsgRecord();
        Blob result = instance.getMsgData();
        assertNull(result);
    }

    @Test
    public void testGetDirection() {
        System.out.println("testGetDirection");
        AsyncMsgRecord instance = new AsyncMsgRecord();
        String result = instance.getDirection();
        assertNull(result);
    }

    @Test
    public void testGetStatus() {
        System.out.println("testGetStatus");
        AsyncMsgRecord instance = new AsyncMsgRecord();
        instance.setStatus("Test");
        String result = instance.getStatus();
        assertEquals(result, "Test");
    }

    @Test
    public void testGetResponseType() {
        System.out.println("testGetResponseType");
        AsyncMsgRecord instance = new AsyncMsgRecord();
        instance.setResponseType("Success");
        String result = instance.getResponseType();
        assertEquals(result, "Success");
    }

    @Test
    public void testGetReserved() {
        System.out.println("testGetResponseType");
        AsyncMsgRecord instance = new AsyncMsgRecord();
        instance.setReserved("T");
        String result = instance.getReserved();
        assertEquals(result, "T");
    }

    @Test
    public void testGetRspData() {
        System.out.println("testGetRspData");
        AsyncMsgRecord instance = new AsyncMsgRecord();
        Blob result = instance.getRspData();
        assertNull(result);
    }

    @Test
    public void testGetDuration() {
        System.out.println("testGetDuration");
        AsyncMsgRecord instance = new AsyncMsgRecord();
        Long value = Long.valueOf("30");
        instance.setDuration(value);
        Long result = instance.getDuration();
        assertEquals(result, value);
    }

    @Test
    public void testGetAckData() {
        System.out.println("testGetAckData");
        AsyncMsgRecord instance = new AsyncMsgRecord();
        Blob result = instance.getAckData();
        assertNull(result);
    }
}
