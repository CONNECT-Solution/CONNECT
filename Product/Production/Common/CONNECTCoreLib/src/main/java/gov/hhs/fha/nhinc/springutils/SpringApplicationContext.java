/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.springutils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author jeff
 */
public class SpringApplicationContext extends FileSystemXmlApplicationContext {
    public SpringApplicationContext(String path) {
        super(path);
    }
    @Override
    public Resource getResourceByPath(String path) {
        return new FileSystemResource(path);
    }

    public SpringApplicationContext() {
    }
}
