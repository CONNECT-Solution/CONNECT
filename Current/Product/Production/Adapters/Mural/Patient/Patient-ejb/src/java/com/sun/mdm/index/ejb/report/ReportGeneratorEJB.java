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

import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchIterator;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
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
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.annotation.PostConstruct;

import com.sun.mdm.index.ejb.master.MasterControllerLocal;

/**
 * Session bean for reports
 */
@Stateless(mappedName="ejb/PatientReportGenerator")
@Remote(ReportGeneratorRemote.class)
@Resource(name="jdbc/BBEDataSource", 
          type=javax.sql.DataSource.class,
          mappedName="jdbc/PatientDataSource" )
public class ReportGeneratorEJB implements ReportGeneratorRemote{
    
    
    @EJB
    private MasterControllerLocal mMaster;
    
    private ReportGeneratorImpl mReportGeneratorImpl;
    
    public ReportGeneratorEJB() { 
    }
    
    @PostConstruct
    public void initialize(){
        mReportGeneratorImpl = new ReportGeneratorImpl(mMaster);
    }

    /** Execute the assumed match report
     * @param config Assumed Match Report Configuration
     * @return assumed match report
     * @throws ReportException an error occured
     */    
    public AssumedMatchReport execAssumedMatchReport(AssumedMatchReportConfig config) 
    throws ReportException{
        return mReportGeneratorImpl.execAssumedMatchReport(config);        
    }
    
    /** Execute the assumed match report
     * @param config Assumed Match Report Configuration
     * @return assumed match iterator
     * @throws ReportException an error occured
     */    
    public AssumedMatchIterator execAssumedMatchReportIterator(AssumedMatchReportConfig config) 
    throws ReportException {
        return mReportGeneratorImpl.execAssumedMatchReportIterator(config);
        
    }

    /** Execute the potential duplicate report
     * @param config Potential Duplicate Report Configuration
     * @return potential duplicate report
     * @throws ReportException an error occured
     */    
    public PotentialDuplicateReport execPotentialDuplicateReport(PotentialDuplicateReportConfig config) 
    throws ReportException{
        return mReportGeneratorImpl.execPotentialDuplicateReport(config);    
    }
    
    /** Execute the potential duplicate report
     * @param config Potential Duplicate Report Configuration
     * @return potential duplicate iterator
     * @throws ReportException an error occured
     */    
    public PotentialDuplicateIterator execPotentialDuplicateReportIterator(PotentialDuplicateReportConfig config) 
    throws ReportException{
        return mReportGeneratorImpl.execPotentialDuplicateReportIterator(config);
    }
    
    /** Execute the merge report
     * @param config Merge Report Configuration
     * @return mergereport
     * @throws ReportException an error occured
     */    
    public MergeReport execMergeReport(MergeReportConfig config) 
    throws ReportException{
        return mReportGeneratorImpl.execMergeReport(config);
    }
    
    /** Execute the unmerge report
     * @param config Unmerge Report Configuration
     * @return unmerge report
     * @throws ReportException an error occured
     */    
    public UnmergeReport execUnmergeReport(UnmergeReportConfig config) 
    throws ReportException{
        return mReportGeneratorImpl.execUnmergeReport(config);
    }
    
    /** Execute the deactivate report
     * @param config Deactivate Report Configuration
     * @return deactivate report
     * @throws ReportException an error occured
     */    
    public DeactivateReport execDeactivateReport(DeactivateReportConfig config) 
    throws ReportException{
        return mReportGeneratorImpl.execDeactivateReport(config);
    }
    
    /** Execute the update report
     * @param config Update Report Configuration
     * @return update report
     * @throws ReportException an error occured
     */    
    public UpdateReport execUpdateReport(UpdateReportConfig config) 
    throws ReportException{       
        return mReportGeneratorImpl.execUpdateReport(config);
    }
    
    /** Execute the update report
     * @param config Update Report Configuration
     * @return update report
     * @throws ReportException an error occured
     */    
    public TransactionIterator execUpdateReportIterator(UpdateReportConfig config) 
    throws ReportException {
        return mReportGeneratorImpl.execUpdateReportIterator(config);
        
    }    
    /** Execute the weekly key statistics report
     * @param config Key Statistics Report Configuration
     * @return Weekly key statistics report
     * @throws ReportException an error occured
     */    
    public KeyStatisticsReport execWeeklyKeyStatisticsReport(KeyStatisticsReportConfig config) 
    throws ReportException{
        return mReportGeneratorImpl.execWeeklyKeyStatisticsReport(config);
        
    }
    
    /** Execute the monthly key statistics report
     * @param config Key Statistics Report Configuration
     * @return Monthly key statistics report
     * @throws ReportException an error occured
     */    
    public KeyStatisticsReport execMonthlyKeyStatisticsReport(KeyStatisticsReportConfig config) 
    throws ReportException{
        return mReportGeneratorImpl.execMonthlyKeyStatisticsReport(config);
    }
    
    /** Execute the yearly key statistics report
     * @param config Key Statistics Report Configuration
     * @return Yearly key statistics report
     * @throws ReportException an error occured
     */    
    public KeyStatisticsReport execYearlyKeyStatisticsReport(KeyStatisticsReportConfig config) 
    throws ReportException{
        return mReportGeneratorImpl.execYearlyKeyStatisticsReport(config);
    }    
}
