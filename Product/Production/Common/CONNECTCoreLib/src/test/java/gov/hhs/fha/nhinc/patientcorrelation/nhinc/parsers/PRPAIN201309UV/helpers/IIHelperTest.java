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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import org.hl7.v3.II;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class IIHelperTest {

    @Test
    public void IIFactoryCreateNull() {
        II ii = new II();
        IIHelper helper = new IIHelper();
        String nullFlavor = "NOT Available";
        ii = helper.IIFactoryCreateNull(nullFlavor);
        assertEquals(ii.getNullFlavor().get(0), "NA");
    }

    @Test
    public void testIIFactoryCreateNull() {
        II ii = new II();
        IIHelper helper = new IIHelper();
        ii = helper.IIFactoryCreateNull();
        assertEquals(ii.getNullFlavor().get(0), "NA");
    }

    @Test
    public void testIIFactory() {
        II ii = new II();
        IIHelper helper = new IIHelper();
        String root = "1.1";
        String extension = "1.16.17.19";
        ii= helper.IIFactory(root, extension);
        assertEquals(ii.getExtension(), extension);
        assertEquals(ii.getRoot(), root);
    }

    @Test
    public void IIFactory() {
        II ii = new II();
        IIHelper helper = new IIHelper();
        ii = helper.IIFactory(createQualifiedSubjectIdentifierType());
        assertEquals(ii.getExtension(), "D123401");
        assertEquals(ii.getRoot(), "1.1");
    }

    private QualifiedSubjectIdentifierType createQualifiedSubjectIdentifierType() {
        QualifiedSubjectIdentifierType identifier = new QualifiedSubjectIdentifierType();
        identifier.setAssigningAuthorityIdentifier("1.1");
        identifier.setSubjectIdentifier("D123401");
        return identifier;
    }

}
