/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */
package com.sun.mdm.index.ejb.report;


import com.sun.mdm.index.report.AssumedMatchReport;
import com.sun.mdm.index.report.AssumedMatchReportConfig;
import com.sun.mdm.index.report.DeactivateReport;
import com.sun.mdm.index.report.DeactivateReportConfig;
import com.sun.mdm.index.report.KeyStatisticsReport;
import com.sun.mdm.index.report.KeyStatisticsReportConfig;
import com.sun.mdm.index.report.MergeReport;
import com.sun.mdm.index.report.MergeReportConfig;
import com.sun.mdm.index.report.PotentialDuplicateReport;
import com.sun.mdm.index.report.PotentialDuplicateReportConfig;
import com.sun.mdm.index.report.ReportException;
import com.sun.mdm.index.report.UnmergeReport;
import com.sun.mdm.index.report.UnmergeReportConfig;
import com.sun.mdm.index.report.UpdateReport;
import com.sun.mdm.index.report.UpdateReportConfig;
import com.sun.mdm.index.util.ConnectionUtil;
import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import java.sql.Connection;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;


/**
 * Session bean for batchreports
 */
@Stateless(mappedName="ejb/PatientBatchReportGenerator")
@Remote(BatchReportGeneratorRemote.class)
@Resource(name="jdbc/BBEDataSource", 
          type=javax.sql.DataSource.class,
          mappedName="jdbc/PatientDataSource" )
public class BatchReportGeneratorEJB implements BatchReportGeneratorRemote {
   
    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    static final String DBDATEFORMAT = "yyyy-MM-dd hh24:MI:ss";
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    private BatchReportGeneratorImpl mBatchReportGeneratorImpl;
    
    /**
     * Creates a new instance of ReportGeneratorEJB
     */
    public BatchReportGeneratorEJB() {
    }
    
    @PostConstruct
    public void initialize(){
        mBatchReportGeneratorImpl= new BatchReportGeneratorImpl();
    }
    
    /**
     * To get the range of Potential Duplicate records
     * @return PotentialDuplicateReport Returns the object of potential
     *         duplicate report
     * @throws ReportException an error occured
     */
    public PotentialDuplicateReport getNextPotDupRecords()
                throws ReportException {
        return mBatchReportGeneratorImpl.getNextPotDupRecords();        
    }

    /**
     * To get the Assumed Match records page wise
     * @return Assumed Match Report
     * @throws ReportException an error occured
     */
    public AssumedMatchReport getNextAssumedMatchRecords()
                throws ReportException {
        return mBatchReportGeneratorImpl.getNextAssumedMatchRecords();        
    }

    /**
     * To close the connection
     * @param con - Connection object
     */
    private void releaseConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            mLogger.warn(mLocalizer.x("RPE001: BatchReportGeneratorEJB.releaseConnection(): could not " +
                                      "close JDBC connection: {0}", e.getMessage()));
        }
    }

    /**
     * Execute the assumed match report
     * @param config Assumed Match Report Configuration
     * @return Assumed Match Report
     * @throws ReportException an error occured
     */
    public AssumedMatchReport execAssumedMatchReport(AssumedMatchReportConfig config)
            throws ReportException {

        Connection conn = getConnection();                
        AssumedMatchReport amreport= mBatchReportGeneratorImpl.execAssumedMatchReport(config, conn);
        releaseConnection(conn);
        return amreport;
    }

    /**
     * Execute the potential duplicate report
     * @param config Potential Duplicate Report Configuration
     * @return Potential Duplicate Report
     * @throws ReportException an error occured
     */
    public PotentialDuplicateReport execPotentialDuplicateReport(
        PotentialDuplicateReportConfig config)
            throws ReportException {
        Connection conn = getConnection();                
        PotentialDuplicateReport report= mBatchReportGeneratorImpl.execPotentialDuplicateReport(config, conn);
        releaseConnection(conn);
        return report;        
    }

    /**
     * Execute the merge report
     * @param config Merge Report Configuration
     * @return Merge Report
     * @throws ReportException an error occured
     */
    public MergeReport execMergeReport(MergeReportConfig config)
            throws ReportException {
        Connection conn = getConnection();                
        MergeReport report= mBatchReportGeneratorImpl.execMergeReport(config, conn);
        releaseConnection(conn);
        return report;             
    }

    /**
     * To get the Assumed Match records page wise
     * @return Next Merge Records
     * @throws ReportException an error occured
     */
    public MergeReport getNextMergeRecords()
        throws ReportException {
        return mBatchReportGeneratorImpl.getNextMergeRecords();
    }

    /**
     * Execute the unmerge report
     * @param config Unmerge Report Configuration
     * @return Unmerge Report
     * @throws ReportException an error occured
     */
    public UnmergeReport execUnmergeReport(UnmergeReportConfig config)
            throws ReportException {

        Connection conn = getConnection();                
        UnmergeReport report= mBatchReportGeneratorImpl.execUnmergeReport(config, conn);
        releaseConnection(conn);
        return report;        
    }

    /**
     * To get the UnmergeReport records page wise
     * @return Next Unmerge Records
     * @throws ReportException an error occured
     */
    public UnmergeReport getNextUnmergeRecords()
            throws ReportException {                       
        return mBatchReportGeneratorImpl.getNextUnmergeRecords();
    }

    /**
     * Execute the deactivate report
     * @param config Deactivate Report Configuration
     * @return Deactivate Report
     * @throws ReportException an error occured
     */
    public DeactivateReport execDeactivateReport(DeactivateReportConfig config)
            throws ReportException {
        Connection conn = getConnection();                
        DeactivateReport report= mBatchReportGeneratorImpl.execDeactivateReport(config, conn);
        releaseConnection(conn);
        return report;             
    }

    /**
     * Execute the deactivate report
     * @return Next Deactivate Records
     * @throws ReportException an error occured
     */
    public DeactivateReport getNextDeactivateRecords()
            throws ReportException {
        return mBatchReportGeneratorImpl.getNextDeactivateRecords();
    }
    
    /**
     * Get the next part of the Update Report
     * @return Next Update Records
     * @throws ReportException an error occured
     */
    public UpdateReport getNextUpdateRecords()
            throws ReportException {
        return mBatchReportGeneratorImpl.getNextUpdateRecords();
    }

    /**
     * Execute the update report
     * @param config UpdateReportConfig
     * @return Update Report
     * @throws ReportException an error occured
     */
    public UpdateReport execUpdateReport(UpdateReportConfig config)
            throws ReportException {
        Connection conn = getConnection();                
        UpdateReport report= mBatchReportGeneratorImpl.execUpdateReport(config, conn);
        releaseConnection(conn);
        return report;        
    }

    /**
     * Execute the weekly key statistics report
     * @param config Key Statistics Report Configuration
     * @return Weekly key statistics report
     * @throws ReportException an error occured
     */
    public KeyStatisticsReport execWeeklyKeyStatisticsReport(
        KeyStatisticsReportConfig config) throws ReportException {
        Connection conn = getConnection();                
        KeyStatisticsReport report= 
                mBatchReportGeneratorImpl.execWeeklyKeyStatisticsReport(config, conn);
        releaseConnection(conn);
        return report; 
    }

    /**
     * Execute the monthly key statistics report
     * @param config Key Statistics Report Configuration
     * @return Monthly key statistics report
     * @throws ReportException an error occured
     */
    public KeyStatisticsReport execMonthlyKeyStatisticsReport(
        KeyStatisticsReportConfig config)
        throws ReportException {
        Connection conn = getConnection();                
        KeyStatisticsReport report= 
                mBatchReportGeneratorImpl.execMonthlyKeyStatisticsReport(config, conn);
        releaseConnection(conn);
        return report; 
    }

    /**
     * Execute the yearly key statistics report
     * @param config Key Statistics Report Configuration
     * @return Yearly key statistics report
     * @throws ReportException an error occured
     */
    public KeyStatisticsReport execYearlyKeyStatisticsReport(
        KeyStatisticsReportConfig config)
        throws ReportException {
        Connection conn = getConnection();                
        KeyStatisticsReport report= 
                mBatchReportGeneratorImpl.execYearlyKeyStatisticsReport(config, conn);
        releaseConnection(conn);
        return report; 
    }

    /**
     * Get JDBC connection
     *
     * @return JDBC connection from pool.
     * @throws ReportException An error occured.
     */
    private Connection getConnection() throws ReportException {
        try {
            Connection con = ConnectionUtil.getConnection();
            return con;
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE500: Failed to get JDBC connection: {0}", e));
        }
    }
}
