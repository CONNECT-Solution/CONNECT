/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.util;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Suppresses any appenders on the root logger for the duration of the test. Adds a mock appender that can be used
 * to verify logging behavior (if desired.) Useful if you log scary warnings or errors for unusual conditions that are
 * exercised by the unit tests.
 */
public class AbstractSuppressRootLoggerTest {
    private static final Logger ROOT_LOGGER = Logger.getRootLogger();
    private static List<Appender> ORIG_APPENDERS;
    private Appender mockAppender;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void beforeClass() {
        ORIG_APPENDERS = new ArrayList<Appender>();
        Enumeration<Appender> appenderEnum = ROOT_LOGGER.getAllAppenders();
        while (appenderEnum.hasMoreElements()) {
            ORIG_APPENDERS.add(appenderEnum.nextElement());
        }

        ROOT_LOGGER.removeAllAppenders();
    }

    @AfterClass
    public static void afterClass() {
        ROOT_LOGGER.removeAllAppenders();
        for (Appender a : ORIG_APPENDERS) {
            ROOT_LOGGER.addAppender(a);
        }
    }

    @Before
    public final void setupMockAppender() {
        mockAppender = mock(Appender.class);
        ROOT_LOGGER.addAppender(mockAppender);
    }

    /**
     * @return the mockito-created appender on the root logger
     */
    protected final Appender getMockAppender() {
        return mockAppender;
    }
}

