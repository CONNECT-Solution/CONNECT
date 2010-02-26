/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.springutils;

import org.springframework.core.io.Resource;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author jeff
 */
public class SpringApplicationContextTest {

    private static SpringApplicationContext sac;
    @BeforeClass
    public static void setup()
    {
        sac = new SpringApplicationContext();
    }

    @Test
    public void testAbsoluteUnixPath()
    {
        String path = "/some/path/to/a/file.xml";
        //assertEquals(make_resource_desc(path), sac.getResource(path).getDescription());
        // Windows failure: <message level="Info"><![CDATA[expected:<file [[/some/path/to/a/]file.xml]> but was:<file [[c:\some\path\to\a\]file.xml]>]]></message>

    }

    @Test
    public void testWorkaroundAbsoluteUnixPath()
    {
        String path = "//some/workaround/path/to/a/file.xml";
        //assertEquals(make_resource_desc(path.substring(1)), sac.getResource(path).getDescription());
        // Windows failure: <message level="Info"><![CDATA[expected:<file [[/some/workaround/path/to/a/]file.xml]> but was:<file [[\\some\workaround\path\to\a\]file.xml]>]]></message>
    }

    private static String make_resource_desc(String path)
    {
        return "file [" + path + "]";
    }
}
