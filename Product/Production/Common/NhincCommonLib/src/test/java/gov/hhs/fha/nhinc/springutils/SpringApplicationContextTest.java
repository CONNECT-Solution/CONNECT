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
    private static boolean isWindows = false;
    @BeforeClass
    public static void setup()
    {
        sac = new SpringApplicationContext();
        String os = System.getProperty("os.name").toLowerCase();
        isWindows = (os.indexOf( "win" ) >= 0);
    }

    @Test
    public void testAbsoluteUnixPath()
    {
        String path = "/some/path/to/a/file.xml";
        // replaceFirst needs regular expression of file \[[a-zA-Z]: to match "file [c:" and "file [d:"
        assertEquals(make_resource_desc(path), sac.getResource(path).getDescription().replaceFirst("file \\[[a-zA-Z]:", "file [drive:"));

    }

    @Test
    public void testWorkaroundAbsoluteUnixPath()
    {
        // Test for unix backward-compatibility.  NOT a valid
        String path = "//some/workaround/path/to/a/file.xml";
        assertEquals(make_resource_desc(path), sac.getResource(path).getDescription().replaceFirst("file \\[[a-zA-Z]:", "file [drive:"));
    }

    @Test
    public void testAbsoluteWindowsPath()
    {
        String path = "\\some\\path\\to\\a\\file.xml";
        if (isWindows) {
          assertEquals(make_resource_desc(path), sac.getResource(path).getDescription().replaceFirst("file \\[[a-zA-Z]:", "file [drive:"));
        }
    }
    @Test
    public void testAbsoluteWindowsPathWithDrive()
    {
        String path = "c:\\some\\path\\to\\a\\file.xml";
        String desc = sac.getResource(path).getDescription();
        if (isWindows) {
          assertEquals("file [drive:\\some\\path\\to\\a\\file.xml]", sac.getResource(path).getDescription().replaceFirst("file \\[[a-zA-Z]:", "file [drive:"));
        }
    }

    private static String make_resource_desc(String path)
    {
        if (isWindows) {
            String backslashPath = path.replace('/', '\\');
            if (backslashPath.startsWith("\\\\")) {
              return "file [" + backslashPath + "]";
            } else if (backslashPath.startsWith("\\")) {
              // We'll substitute actual value to match above
              return "file [drive:" + backslashPath + "]";
            } else if (backslashPath.matches("[a-z]:")) {
              return "file [drive:" + backslashPath + "]";
            } else {
              return "file [" + backslashPath + "]";
            }
        } else {
            if (path.startsWith("//")) {
              return "file [" + path.substring(1) + "]";
            } else {
              return "file [" + path + "]";
            }
        }
    }
}
