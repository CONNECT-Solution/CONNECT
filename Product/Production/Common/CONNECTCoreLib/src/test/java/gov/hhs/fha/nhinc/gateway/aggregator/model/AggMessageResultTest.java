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
package gov.hhs.fha.nhinc.gateway.aggregator.model;

import org.junit.*;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by User: ralph Date: May 17, 2010 Time: 4:12:00 PM
 */
public class AggMessageResultTest {
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

    @Test
    public void testMessageId() {
        AggMessageResult messageResult;

        messageResult = new AggMessageResult();

        messageResult.setMessageId("xxx123");

        assertEquals(messageResult.getMessageId(), "xxx123");
    }

    @Test
    public void testMessageKey() {
        AggMessageResult messageResult;

        messageResult = new AggMessageResult();

        messageResult.setMessageKey("yyy456");

        assertEquals(messageResult.getMessageKey(), "yyy456");
    }

    @Test
    public void testMessageOutTime() {
        AggMessageResult messageResult;
        Date date;

        messageResult = new AggMessageResult();
        date = new Date();

        messageResult.setMessageOutTime(date);

        assertEquals(messageResult.getMessageOutTime(), date);
    }

    @Test
    public void testResponseReceivedTime() {
        AggMessageResult messageResult;
        Date date;

        messageResult = new AggMessageResult();
        date = new Date();

        messageResult.setResponseReceivedTime(date);

        assertEquals(messageResult.getResponseReceivedTime(), date);
    }

    @Test
    public void testResponseMessageType() {
        AggMessageResult messageResult;
        String responseType = "Response Type 1";

        messageResult = new AggMessageResult();

        messageResult.setResponseMessageType(responseType);

        assertEquals(messageResult.getResponseMessageType(), responseType);
    }

    @Test
    public void testResponseMessage() {
        AggMessageResult messageResult;
        String message = "A simulated ResponseMessage";

        messageResult = new AggMessageResult();

        messageResult.setResponseMessage(message);

        assertEquals(messageResult.getResponseMessage(), message);
    }

    @Test
    public void testResponseMessageAsBytes() {
        AggMessageResult messageResult;
        byte[] message = "A simulated ResponseMessage".getBytes();

        messageResult = new AggMessageResult();

        messageResult.setResponseMessageAsByteArray(message);

        assertEquals(messageResult.getResponseMessageAsByteArray(), message);
    }

    @Test
    public void testAggTransaction() {
        AggMessageResult messageResult;
        AggTransaction aggTrans;

        messageResult = new AggMessageResult();
        aggTrans = new AggTransaction();

        messageResult.setAggTransaction(aggTrans);

        assertEquals(messageResult.getAggTransaction(), aggTrans);
    }

}
