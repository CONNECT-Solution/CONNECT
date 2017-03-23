/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cmay
 *
 */
public class StreamUtils {

    private static final Logger LOG = LoggerFactory.getLogger(StreamUtils.class);

    public static OutputStreamWriter openOutputStream(String sPropFile) throws Exception {
        OutputStreamWriter propWriter = null;
        FileOutputStream propFOS = null;

        try {
            propFOS = new FileOutputStream(sPropFile);
            propWriter = new OutputStreamWriter(propFOS, StringUtil.UTF8_CHARSET);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            closeReaderSilently(propFOS);
            closeWriterSilently(propWriter);

            throw new Exception("Failed to open property file: '" + sPropFile + "' for reading", e);
        }

        return propWriter;
    }

    public static InputStreamReader openInputStream(File propFile) throws Exception {
        InputStreamReader propReader = null;
        FileInputStream propFIS = null;

        try {
            propFIS = new FileInputStream(propFile);
            propReader = new InputStreamReader(propFIS, StringUtil.UTF8_CHARSET);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            closeStreamSilently(propFIS);
            closeFileSilently(propReader);

            throw new Exception("Failed to open property file: '" + propFile + "' for reading", e);
        }

        return propReader;
    }

    public static void closeStreamSilently(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException ioe) {
            LOG.warn("Failed to close file: {}", ioe.getLocalizedMessage(), ioe);
        }
    }

    public static void closeReaderSilently(OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException ioe) {
            LOG.warn("Failed to close file: {}", ioe.getLocalizedMessage(), ioe);
        }
    }

    public static void closeWriterSilently(Writer writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException ioe) {
            LOG.warn("Failed to close file: {}", ioe.getLocalizedMessage(), ioe);
        }
    }

    public static void closeFileSilently(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException ioe) {
            LOG.warn("Failed to close file: {}", ioe.getLocalizedMessage(), ioe);
        }
    }
}
