/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.utils;

import gov.hhs.fha.nhinc.util.hash.SHA1HashCode;

/**
 *
 * @author kim
 */
public class HashCodeUtil {

    /**
     * Calculate the hash code.
     * @param rawData
     * @return
     */
    public static String calculateHashCode(byte[] rawData) {
        String sHash = "";
        if (rawData != null) {
            try {
                sHash = SHA1HashCode.calculateSHA1(new String(rawData));
            } catch (Throwable t) {
                System.err.println("Failed to create SHA-1 Hash code.  Error: " + t.getMessage() +
                    "Data Text: " + new String(rawData));
            }
        } else {
            System.out.println("No SHA-1 Hash Code created because document was null.");
        }

        return sHash;
    }
}
