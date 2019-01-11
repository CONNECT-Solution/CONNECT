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
package gov.hhs.fha.nhinc.mpilib;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * @author akong
 *
 */
public class IdentifierTest {

    @Test
    public void testEquals() {
        Identifier ident1 = new Identifier("123", "abc");
        assertTrue(ident1.equals(ident1));
        assertTrue(ident1.hashCode() == ident1.hashCode());

        Identifier ident2 = new Identifier("123", "abc");
        assertTrue(ident1.equals(ident2));
        assertTrue(ident1.hashCode() == ident2.hashCode());

        Identifier ident3 = new Identifier("456", "abc");
        assertFalse(ident1.equals(ident3));
        assertTrue(ident1.hashCode() != ident3.hashCode());

        Identifier ident4 = new Identifier("123", "def");
        assertFalse(ident1.equals(ident4));
        assertTrue(ident1.hashCode() != ident4.hashCode());

        String test = new String();
        assertFalse(ident1.equals(test));

        Identifier ident5 = new Identifier("123", null);
        Identifier ident6 = new Identifier("123", null);
        assertFalse(ident5.equals(ident6));
        assertFalse(ident5.hashCode() != ident6.hashCode());
    }

}
