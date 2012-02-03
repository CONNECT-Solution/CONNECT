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
package gov.hhs.fha.nhinc.adapter.reidentification.data;

import com.thoughtworks.xstream.XStream;

/**
 * This class is used to serialize the POJO reidentification classes into an 
 * XML string and also to de-serialize an XML string into a set of POJO 
 * reidentification classes.
 */
public class PseudonymMapsXML {

    /**
     * XStream initialized for PseudonymMaps
     */
    private static XStream xStream;

    /**
     * Lazy initializer for the definition of the xstream
     */
    private static XStream getXStream() {
        if (xStream == null) {
            xStream = new XStream();
            xStream.alias("pseudonymMaps", PseudonymMaps.class);
            xStream.addImplicitCollection(PseudonymMaps.class, "pseudonymMaps");
            xStream.alias("pseudonymMap", PseudonymMap.class);
        }
        return xStream;
    }

    /**
     * This method serializes a PseudonymsMaps object into an XML string and 
     * returns that string.
     */
    public static String serialize(PseudonymMaps pseudonymMaps) {
        return getXStream().toXML(pseudonymMaps);
    }

    /**
     * This method takes an XML representation of the PseudonymMaps and 
     * returns the POJO objects that represent that same data.
     */
    public static PseudonymMaps deserialize(String xml) {

        PseudonymMaps pseudonymMaps = new PseudonymMaps();
        Object obj = getXStream().fromXML(xml);
        if (obj instanceof PseudonymMaps) {
            pseudonymMaps = (PseudonymMaps) obj;
        }

        return pseudonymMaps;
    }
}
