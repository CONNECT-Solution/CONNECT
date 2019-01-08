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
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generates a report on duplicate jar dependencies. Searches through the provided paths and identifies directories with
 * multiple versions of the same jar.
 */
public class MultiJarVersionFinder {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            usage();
        }

        for (String dirString : args) {
            handleSingleDir(dirString);

            String[] subDirs = findSubdirs(dirString);
            if (subDirs.length > 0) {
                main(subDirs);
            }
        }
    }

    private static void usage() {
        System.out.println("usage: java MultiJarVersionFinder <roots>\n"
                + "  roots must be 1 or more directories to search recursively");
    }

    private static String[] findSubdirs(String cwd) {
        File curDir = new File(cwd);
        File[] subDirs = curDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        List<String> subDirList = new ArrayList<>();
        for (File subDir : subDirs) {
            subDirList.add(subDir.getAbsolutePath());
        }
        return subDirList.toArray(new String[] {});
    }

    private static void handleSingleDir(String dirString) {
        String[] dirList = listSingleDir(dirString);
        Map<String, Set<String>> map = findDups(dirList);
        printDups(dirString, map);
    }

    private static String[] listSingleDir(String dirString) {
        File dir = new File(dirString);
        return dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowerName = name.toLowerCase();
                return lowerName.endsWith(".jar") && !lowerName.endsWith("-sources.jar")
                        && !lowerName.endsWith("-tests.jar");
            }
        });
    }

    private static Map<String, Set<String>> findDups(String[] dirList) {
        Map<String, Set<String>> map = new HashMap<>();
        Pattern p = Pattern.compile("(.*)-([0-9][0-9.]*.*)\\.jar");
        for (String entry : dirList) {
            Matcher m = p.matcher(entry);

            if (m.matches()) {
                String library = m.group(1);
                String version = m.group(2);

                Set<String> curVersions = map.get(library);
                if (curVersions == null) {
                    curVersions = new HashSet();
                    map.put(library, curVersions);
                }
                curVersions.add(version);
            }
        }
        return map;
    }

    private static void printDups(String directory, Map<String, Set<String>> map) {
        boolean firstEntry = true;
        for (String entry : map.keySet()) {
            if (map.get(entry).size() > 1) {
                if (firstEntry) {
                    System.out.println(directory);
                    firstEntry = false;
                }

                System.out.print("- " + entry + ":");
                boolean first = true;
                for (String version : map.get(entry)) {
                    if (first) {
                        first = false;
                    } else {
                        System.out.print(",");
                    }
                    System.out.print(" " + version);
                }
                System.out.println();
            }
        }
    }
}
