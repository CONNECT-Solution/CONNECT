/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.springutils;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jeff
 */
public class SpringApplicationContextTest {

    private static SpringApplicationContext sac;
    private static boolean isWindows = false;

    @BeforeClass
    public static void setup() {
        sac = new SpringApplicationContext();
        String os = System.getProperty("os.name").toLowerCase();
        isWindows = (os.indexOf("win") >= 0);
    }

    @Test
    public void testAbsoluteUnixPath() {
        String path = "/some/path/to/a/file.xml";
        // replaceFirst needs regular expression of file \[[a-zA-Z]: to match "file [c:" and "file [d:"
        assertEquals(make_resource_desc(path),
                sac.getResource(path).getDescription().replaceFirst("file \\[[a-zA-Z]:", "file [drive:"));

    }

    @Test
    public void testWorkaroundAbsoluteUnixPath() {
        // Test for unix backward-compatibility. NOT a valid
        String path = "//some/workaround/path/to/a/file.xml";
        assertEquals(make_resource_desc(path),
                sac.getResource(path).getDescription().replaceFirst("file \\[[a-zA-Z]:", "file [drive:"));
    }

    @Test
    public void testAbsoluteWindowsPath() {
        String path = "\\some\\path\\to\\a\\file.xml";
        if (isWindows) {
            assertEquals(make_resource_desc(path),
                    sac.getResource(path).getDescription().replaceFirst("file \\[[a-zA-Z]:", "file [drive:"));
        }
    }

    @Test
    public void testAbsoluteWindowsPathWithDrive() {
        String path = "c:\\some\\path\\to\\a\\file.xml";
        String desc = sac.getResource(path).getDescription();
        if (isWindows) {
            assertEquals("file [drive:\\some\\path\\to\\a\\file.xml]", sac.getResource(path).getDescription()
                    .replaceFirst("file \\[[a-zA-Z]:", "file [drive:"));
        }
    }

    private static String make_resource_desc(String path) {
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
