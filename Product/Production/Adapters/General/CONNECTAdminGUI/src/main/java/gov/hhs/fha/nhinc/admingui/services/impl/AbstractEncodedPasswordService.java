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
package gov.hhs.fha.nhinc.admingui.services.impl;

import gov.hhs.fha.nhinc.admingui.services.PasswordService;
import gov.hhs.fha.nhinc.admingui.services.exception.PasswordServiceException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * The Class AbstractPasswordService.
 *
 * @author msw
 */
public abstract class AbstractEncodedPasswordService implements PasswordService {

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.services.PasswordService#checkPassword(byte[], byte[], byte[])
     */
    @Override
    public boolean checkPassword(byte[] passwordHash, byte[] candidatePassword, byte[] salt)
            throws PasswordServiceException {
        boolean passwordsMatch = false;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] candidateHash = null;
        try {
            outputStream.write(salt);
            outputStream.write(candidatePassword);
            candidateHash = calculateHash(outputStream.toByteArray());
            passwordsMatch = Arrays.equals(passwordHash, candidateHash);
        } catch (IOException e) {
            throw new PasswordServiceException("Unable to construct candidate hash from candidate password and salt.",
                    e);
        }

        return passwordsMatch;
    }

    /**
     * Encode.
     *
     * @param input the input
     * @return the byte[]
     */
    public abstract byte[] encode(byte[] input);

}
