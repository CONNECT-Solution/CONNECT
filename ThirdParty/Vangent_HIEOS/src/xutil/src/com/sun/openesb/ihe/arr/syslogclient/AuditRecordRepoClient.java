/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.openesb.ihe.arr.syslogclient;

import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author yoga
 */
public class AuditRecordRepoClient {
    private final static Logger logger = Logger.getLogger(AuditRecordRepoClient.class);

    SyslogWriter logWriter = null;

    public AuditRecordRepoClient(String host, int port) {
        logWriter = new SyslogWriter(host, port);
        logWriter.setLevel(6);
    }
     
    public AuditRecordRepoClient(String host, int port, int level) {
        logWriter = new SyslogWriter(host, port);
        logWriter.setLevel(level);
    }
    public AuditRecordRepoClient() {
        logWriter = new SyslogWriter("localhost", 4010);
        logWriter.setLevel(6);
    }
    
    public void sendAuditMessage(String msg) {
        if (logWriter == null) {
            logger.error("ERROR: Fatal: SyslogWriter is null. Will not writer audit message");
        }
        try {
            logWriter.write(msg);
        } catch (IOException ex) {
            logger.error("ERROR: failed to Writer Audit Message");
        }
    }

    
}
