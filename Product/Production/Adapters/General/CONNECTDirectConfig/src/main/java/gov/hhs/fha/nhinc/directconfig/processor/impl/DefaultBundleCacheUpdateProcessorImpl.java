/* 
Copyright (c) 2010, NHIN Direct Project
All rights reserved.

Authors:
   Greg Meyer      gm2552@cerner.com
 
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
in the documentation and/or other materials provided with the distribution.  Neither the name of the The NHIN Direct Project (nhindirect.org). 
nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
THE POSSIBILITY OF SUCH DAMAGE.
*/

package gov.hhs.fha.nhinc.directconfig.processor.impl;

import java.util.Calendar;
import java.util.Collection;

import org.apache.camel.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.directconfig.processor.BundleCacheUpdateProcessor;
import gov.hhs.fha.nhinc.directconfig.processor.BundleRefreshProcessor;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundle;
import gov.hhs.fha.nhinc.directconfig.dao.TrustBundleDao;

/**
 * Camel based implementation of the {@linkplain BundleCacheUpdateProcessor} interface.
 * <p>
 * This implementation can be triggered on a regular interval to check if a bundle needs to be refreshed.
 * The implementation iterates through the entire list of configured trust bundles in the system checking
 * each bundle's last refresh time.
 * If a bundles refresh interval has not been exceeded since its last update, then it will not checked
 * for updates.
 * @author Greg Meyer
 * @since 1.3
 */
public class DefaultBundleCacheUpdateProcessorImpl implements BundleCacheUpdateProcessor
{
    private static final Log log = LogFactory.getLog(DefaultBundleCacheUpdateProcessorImpl.class);
	
    /**
     * The trust bundle dao
     */
	protected TrustBundleDao dao;

	/**
	 * The bundle refresh processor.
	 */
	protected BundleRefreshProcessor refreshProcessor;
	
	/**
	 * Default constructor
	 */
	public DefaultBundleCacheUpdateProcessorImpl()
	{
		
	}
	
	/**
	 * Sets the trust bundle dao used to get the last refresh date/time.
	 * @param dao
	 */
	public void setDao(TrustBundleDao dao)
	{
		this.dao = dao;
	}
	
	/**
	 * Sets the {@link BundleRefreshProcessor} used to refresh a bundle the bundle's refresh interval
	 * has been exceeded.
	 * @param refreshProcessor The {@link BundleRefreshProcessor}.
	 */
	public void setRefreshProcessor(BundleRefreshProcessor refreshProcessor)
	{
		this.refreshProcessor = refreshProcessor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Handler
	public void updateBundleCache()
	{
		Collection<TrustBundle> bundles;
		try
		{
			bundles = dao.getTrustBundles();
			for (TrustBundle bundle : bundles)
			{
				boolean refresh = false;
				
				// if the refresh interval is 0 or less, then we won't ever auto refresh the bundle
				if (bundle.getRefreshInterval() <= 0)
					continue;  
				
				// see if this bundle needs to be checked for updating
				final Calendar lastAttempt = bundle.getLastSuccessfulRefresh();
			
				if (lastAttempt == null)
					// never been attempted successfully... better go get it
					refresh = true;
				else
				{
					// check the the last attempt date against now and see if we need to refresh
					long now = System.currentTimeMillis();
					Calendar lastAttemptCheck = (Calendar)lastAttempt.clone();
					lastAttemptCheck.add(Calendar.SECOND, bundle.getRefreshInterval());
					
					if (lastAttemptCheck.getTimeInMillis() <= now)
						refresh = true;
				}
				
				if (refresh)
				{
					// refresh the bundle
					try
					{
						refreshProcessor.refreshBundle(bundle);		
					}
					catch (Exception e)
					{
						log.warn("Failed to check the status of bundle " + bundle.getBundleName(), e);
					}
				}
			}
		}
		catch (Exception e)
		{
			log.warn("Failed to check the status of trust bundles ", e);
		}
		
	}
}
