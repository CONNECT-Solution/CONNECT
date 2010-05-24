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
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * Consume a file via HTTP
     *
     * @param url
     *            The file URL
     * @param dest
     *            The file destination
     * @param bufferSize
     *            The buffer size
     * @throws IOException
     */
    public void consumeFile(String url, String dest, int bufferSize)
            throws IOException {
        long time = System.nanoTime();

        log.debug("URL provided: " + url);
        url = url.replace('\\', '/');

        URL fileUrl = new URL(url);

        URLConnection urlCon = fileUrl.openConnection();

        InputStream in = urlCon.getInputStream();

        String fileName = fileUrl.getFile();

        if (fileName.lastIndexOf('/') >= 0) {
            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
        }

        String fileDest = dest;

        File f = new File(fileDest, fileName);

        FileOutputStream out = new FileOutputStream(f);
        log.debug("File name: " + f.getAbsolutePath());

        byte[] buf = new byte[bufferSize];

        log.debug("Starting to read resource.");
        while (true) {
            int length = in.read(buf);

            System.out.println(length);

            if (length < 0) {
                break;
            }

            out.write(buf, 0, length);
        }
        log.debug("Finished reading resource");
        log.debug("Time taken: " + (System.nanoTime() - time) / 1000000);

        out.close();
        in.close();
    }
}
