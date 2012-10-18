/**
 * 
 */
package gov.hhs.fha.nhinc.largefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This FileInputStream will automatically delete itself once the stream has been closed.
 * 
 * @author akong
 *
 */
public class TempFileInputStream extends FileInputStream {
    File file = null;
    
    public TempFileInputStream(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }

    @Override
    public void close() throws IOException {
        super.close();
        file.delete();
    }
}

