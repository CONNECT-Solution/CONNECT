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
package gov.hhs.fha.nhinc.docsubmission.aspect;

import gov.hhs.fha.nhinc.event.ArgTransformerEventDescriptionBuilder;
import javax.xml.ws.Holder;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public class RegistryResponseTypeHolderBuilder extends ArgTransformerEventDescriptionBuilder {

    public RegistryResponseTypeHolderBuilder() {
        setDelegate(new DeferredResponseDescriptionBuilder());
    }

    @Override
    public Object[] transformArguments(Object[] arguments) {
        if (arguments == null || arguments[0] == null) {
            return new Object[] {};
        }
        @SuppressWarnings("unchecked")
        Holder<RegistryResponseType> holder = (Holder<RegistryResponseType>) arguments[0];
        return new Object[] { holder.value };
    }

    @Override
    public Object transformReturnValue(Object returnValue) {
        return returnValue;
    }

}
