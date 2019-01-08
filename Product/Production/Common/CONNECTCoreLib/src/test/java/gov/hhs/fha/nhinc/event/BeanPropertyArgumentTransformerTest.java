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
package gov.hhs.fha.nhinc.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BeanPropertyArgumentTransformerTest {

    private final String foo = "foo";
    private final int bar = 1;
    private BeanPropertyArgumentTransformer builder;
    private Object bean;

    @Before
    public void before() {
        builder = mock(BeanPropertyArgumentTransformer.class, Mockito.CALLS_REAL_METHODS);
        bean = new Object() {
            public void setFoo(String foo) {

            }

            public String getFoo() {
                return foo;
            }

            public void setBar(int bar) {

            }

            public int getBar() {
                return bar;
            }
        };
    }

    @Test
    public void classExistsOfCorrectType() {
        assertTrue(builder instanceof ArgTransformerEventDescriptionBuilder);
    }

    @Test
    public void transformsBeanPropertiesToArguments() {
        Object[] transformedArguments = builder.transformArguments(new Object[] { bean });
        assertTrue(transformedArguments.length >= 2);
        assertTrue(ArrayUtils.contains(transformedArguments, foo));
        assertTrue(ArrayUtils.contains(transformedArguments, bar));
    }

    @Test
    public void transformsAllArgumentsBeanProperties() {
        Object[] transformedArguments = builder.transformArguments(new Object[] { new Object(), bean });
        assertTrue(transformedArguments.length >= 2);
        assertTrue(ArrayUtils.contains(transformedArguments, foo));
        assertTrue(ArrayUtils.contains(transformedArguments, bar));
    }

    @Test
    public void transformsReturnValue() {
        Object o = new Object();
        assertEquals(o, builder.transformReturnValue(o));
    }

    @Test
    public void handlesNullArguments() {
        Object[] transformedArguments = builder.transformArguments(new Object[] { null });
        assertEquals(0, transformedArguments.length);
    }

    @Test
    public void handlesNullArray() {
        Object[] transformedArguments = builder.transformArguments(null);
        assertEquals(0, transformedArguments.length);
    }
}
