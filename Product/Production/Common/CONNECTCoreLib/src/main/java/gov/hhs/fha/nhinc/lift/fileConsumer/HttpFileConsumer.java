/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
//*****************************************************************************
// FILE: HttpFileConsumer.java
//
// Copyright (C) 2010 TBD
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: HTTP File Consumer
//
// LIMITATIONS: None
//
// CHANGE HISTORY:
//
// Date         Proj   Act    Assign     Desc
// ============ ====== ====== ========== ======================================
// 2010/02/24                 R.Robinson Initial Coding.
// 2010/05/06   964G   1000   bgrantha   Renamed to HttpFileConsumer
//
//*****************************************************************************
package gov.hhs.fha.nhinc.lift.fileConsumer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Http file downloader
 * 
 * @author rrobin20
 */
public class HttpFileConsumer {

    private Log log = null;

    public HttpFileConsumer() {
        log = createLogger();
    }

    protected Log createLogger() {
        if (log == null) {
            log = LogFactory.getLog(getClass());
        }
        return log;
    }

    /**
     * Consume a file via HTTP
     *
     * @param url The file URL
     * @param dest The file destination
     * @param bufferSize The buffer size
     * @return true for success, false if a problem occurred.
     * @throws IOException
     */
    public URI consumeFile(String url, String dest, int bufferSize) throws IOException {

        URI outURI = null;
        File outfile = null;
        InputStream in = null;
        FileOutputStream out = null;
        try {
            long time = System.nanoTime();
            System.out.println("Requesting file: " + url);
            log.debug("URL provided: " + url);
            url = url.replace('\\', '/');
            URL fileUrl = new URL(url);
            URLConnection urlCon = fileUrl.openConnection();
            in = urlCon.getInputStream();
            String fileName = fileUrl.getFile();
            if (fileName.lastIndexOf('/') >= 0) {
                fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            }
            String fileDest = dest;
            outfile = new File(fileDest, fileName);
            out = new FileOutputStream(outfile);
            log.debug("File created: " + outfile.getAbsolutePath());
            byte[] buf = new byte[bufferSize];
            while (true) {
                int length = in.read(buf);
                if (length < 0) {
                    break;
                }
                out.write(buf, 0, length);
            }
            outURI = outfile.toURI();
            log.debug("Finished writing to file " + outURI);
            log.debug("Time taken: " + (System.nanoTime() - time) / 1000000);
            System.out.println("Generated file: " + outURI);
        } catch (IOException ex) {
            throw new IOException("Unable to use file url: " + url + ":" + ex.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    log.debug("Unable to close " + out + ":" + ex.getMessage());
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    log.debug("Unable to close " + in + ":" + ex.getMessage());
                }
            }
        }
        return outURI;
    }
}
