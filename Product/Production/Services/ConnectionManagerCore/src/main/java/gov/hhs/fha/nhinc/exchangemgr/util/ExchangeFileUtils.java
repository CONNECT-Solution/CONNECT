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
package gov.hhs.fha.nhinc.exchangemgr.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class ExchangeFileUtils {

    private static final String DATE_FORMAT = "MM-dd-yyyy_HH-mm-ss";
    private static final String FILE_NAME_REGEX = "exchangeInfo\\.xml\\.\\d{2}-\\d{2}-\\d{4}_\\d{2}-\\d{2}-\\d{2}";
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeFileUtils.class);

    public String generateUniqueFilename(String fileLocation) {
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return fileLocation + "." + sdf.format(currentTime.getTime());
    }

    public void deleteOldBackups(File directory, int noOfBackups) {
        if (null != directory && directory.isDirectory()) {
            Collection<File> fileList = fetchBackupFiles(directory);
            if (CollectionUtils.isNotEmpty(fileList)) {
                File[] fileArray = fileList.toArray(new File[fileList.size()]);
                Arrays.sort(fileArray, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
                int count = numOfFilesToDelete(noOfBackups, fileArray.length);
                for (int i = 0; i < count; i++) {
                    fileArray[i].delete();
                }
            }
        }
    }

    private static Collection<File> fetchBackupFiles(File directory) {
        return FileUtils.listFiles(directory, getBackupFileNameFilter(),
            DirectoryFileFilter.INSTANCE);
    }

    private static IOFileFilter getBackupFileNameFilter() {
        return new AbstractFileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().matches(FILE_NAME_REGEX) && file.isFile();
            }
        };
    }

    private static int numOfFilesToDelete(int noOfBackups, int existingFilesCount) {
        int filesToDelete;
        if (noOfBackups == 0 && existingFilesCount > 0) {
            filesToDelete = existingFilesCount;
        } else if (existingFilesCount > noOfBackups) {
            filesToDelete = existingFilesCount - noOfBackups + 1;
        } else if (existingFilesCount > 0 && noOfBackups > 0 && existingFilesCount == noOfBackups) {
            filesToDelete = 1;
        } else {
            filesToDelete = 0;
        }
        LOG.info("Number of Existing backup files to delete {}", filesToDelete);
        return filesToDelete;
    }
}
