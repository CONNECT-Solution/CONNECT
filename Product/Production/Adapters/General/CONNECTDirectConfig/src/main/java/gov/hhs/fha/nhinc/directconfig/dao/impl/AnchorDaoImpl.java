/*
 Copyright (c) 2010, NHIN Direct Project
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
 in the documentation and/or other materials provided with the distribution.
 3. Neither the name of the The NHIN Direct Project (nhindirect.org) nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 THE POSSIBILITY OF SUCH DAMAGE.
 */

package gov.hhs.fha.nhinc.directconfig.dao.impl;

import java.security.cert.X509Certificate;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.directconfig.entity.Anchor;
import gov.hhs.fha.nhinc.directconfig.entity.CertificateException;
import gov.hhs.fha.nhinc.directconfig.entity.EntityStatus;
import gov.hhs.fha.nhinc.directconfig.dao.AnchorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementing class for Anchor DAO methods.
 *
 * @author ppyette
 */
@Repository
public class AnchorDaoImpl implements AnchorDao {

    @PersistenceContext
    @Autowired
    private EntityManager entityManager;

    private static final Log log = LogFactory.getLog(AnchorDaoImpl.class);

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AnchorDao#load(java.lang.String)
     */
    @Transactional(readOnly = true)
    public Anchor load(String owner)
    {
    	// not sure what this will accomplish...  multiple anchors are always possible for an owner

    	Collection<Anchor> anchors =  this.list(Arrays.asList(owner));

    	if (anchors != null && anchors.size() > 0)
    		return anchors.iterator().next();

    	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AnchorDao#listAll()
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Anchor> listAll()
    {

        log.debug("Enter");

        List<Anchor> result = Collections.emptyList();

        Query select = null;
        select = entityManager.createQuery("SELECT a from Anchor a");


        @SuppressWarnings("rawtypes")
		List rs = select.getResultList();
        if (rs != null && (rs.size() != 0) && (rs.get(0) instanceof Anchor))
        {
            result = (List<Anchor>) rs;
        }


        log.debug("Exit");
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AnchorDao#list(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Anchor> list(List<String> owners)
    {

        log.debug("Enter");

        if (owners == null || owners.size() == 0)
        	return listAll();

        List<Anchor> result = Collections.emptyList();

        Query select = null;
        StringBuffer nameList = new StringBuffer("(");
        for (String owner : owners)
        {
            if (nameList.length() > 1)
            {
                nameList.append(", ");
            }
            nameList.append("'").append(owner.toUpperCase(Locale.getDefault())).append("'");
        }
        nameList.append(")");
        String query = "SELECT a from Anchor a WHERE UPPER(a.owner) IN " + nameList.toString();

        select = entityManager.createQuery(query);
        @SuppressWarnings("rawtypes")
		List rs = select.getResultList();
        if (rs != null && (rs.size() != 0) && (rs.get(0) instanceof Anchor))
        {
            result = (List<Anchor>) rs;
        }


        log.debug("Exit");

        return result;
    }

    /**
     * Add an anchor
     *
     * @param anchor
     *            The anchor to add.
     */
    @Transactional(readOnly = false)
    public void add(Anchor anchor)
    {


        log.debug("Enter");

        if (anchor != null)
        {

        	anchor.setCreateTime(Calendar.getInstance());

        	try
        	{
        		X509Certificate cert = anchor.toCertificate();

        		if (anchor.getValidStartDate() == null)
        		{
        			Calendar startDate = Calendar.getInstance();
        			startDate.setTime(cert.getNotBefore());
        			anchor.setValidStartDate(startDate);
        		}
        		if (anchor.getValidEndDate() == null)
        		{
        			Calendar endDate = Calendar.getInstance();
        			endDate.setTime(cert.getNotAfter());
        			anchor.setValidEndDate(endDate);
        		}

        		if (anchor.getStatus() == null)
        			anchor.setStatus(EntityStatus.NEW);
        	}
        	catch (CertificateException e)
        	{

        	}



                log.debug("Calling JPA to persist the Anchor");

            entityManager.persist(anchor);
            entityManager.flush();



                log.debug("Returned from JPA: Anchor ID=" + anchor.getId());
        }


        log.debug("Exit");
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AnchorDao#save(gov.hhs.fha.nhinc.directconfig.entity.Anchor)
     */
    @Transactional(readOnly = false)
    public void save(Anchor anchor)
    {
    	if (anchor != null)
    	{
    		List<Anchor> anchors = new ArrayList<Anchor>();
    		anchors.add(anchor);
    		save(anchors);
    	}
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AnchorDao#save(java.util.List)
     */
    @Transactional(readOnly = false)
    public void save(List<Anchor> anchorList)
    {
        if (anchorList != null && anchorList.size() > 0)
        {
            for (Anchor anchor : anchorList)
            {
                save(anchor);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Anchor> listByIds(List<Long> anchorIds)
    {

        log.debug("Enter");

        if (anchorIds == null || anchorIds.size() == 0)
        	return Collections.emptyList();

        List<Anchor> result = Collections.emptyList();

        Query select = null;
        StringBuffer ids = new StringBuffer("(");
        for (Long id : anchorIds)
        {
            if (ids.length() > 1)
            {
            	ids.append(", ");
            }
            ids.append(id);
        }
        ids.append(")");
        String query = "SELECT a from Anchor a WHERE a.id IN " + ids.toString();

        select = entityManager.createQuery(query);
        @SuppressWarnings("rawtypes")
		List rs = select.getResultList();
        if (rs != null && (rs.size() != 0) && (rs.get(0) instanceof Anchor))
        {
            result = (List<Anchor>) rs;
        }


        log.debug("Exit");

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AnchorDao#setStatus(java.util.List, gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
     */
    @Transactional(readOnly = false)
    public void setStatus(List<Long> anchorIDs, EntityStatus status)
    {

        log.debug("Enter");

        List<Anchor> anchors = listByIds(anchorIDs);
        if (anchors == null || anchors.size() == 0)
        	return;

        for (Anchor anchor : anchors)
        {
        	anchor.setStatus(status);
        		entityManager.merge(anchor);
        }


        log.debug("Exit");

    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AnchorDao#setStatus(java.lang.String, gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
     */
    @Transactional(readOnly = false)
    public void setStatus(String owner, EntityStatus status)
    {

        log.debug("Enter");

        if (owner == null)
        	return;

        List<String> owners = new ArrayList<String>();
        owners.add(owner);
        List<Anchor> anchors = list(owners);
        if (anchors == null || anchors.size() == 0)
        	return;

        for (Anchor anchor : anchors)
        {
        	anchor.setStatus(status);
        		entityManager.merge(anchor);
        }


        log.debug("Exit");

    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AnchorDao#delete(java.util.List)
     */
    @Transactional(readOnly = false)
    public void delete(List<Long> idList)
    {

        log.debug("Enter");

        if (idList != null && idList.size() > 0)
        {


	        StringBuffer ids = new StringBuffer("(");
	        for (Long id : idList)
	        {
	            if (ids.length() > 1)
	            {
	            	ids.append(", ");
	            }
	            ids.append(id);
	        }
	        ids.append(")");
	        String query = "DELETE FROM Anchor a WHERE a.id IN " + ids.toString();

	        int count = 0;
	        Query delete = entityManager.createQuery(query);
	        count = delete.executeUpdate();


	            log.debug("Exit: " + count + " anchor records deleted");
        }


        log.debug("Exit");
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.directconfig.dao.AnchorDao#delete(java.lang.String)
     */
    @Transactional(readOnly = false)
    public void delete(String owner)
    {

        log.debug("Enter");

        if (owner == null)
        	return;

        int count = 0;
        if (owner != null) {
            Query delete = entityManager.createQuery("DELETE FROM Anchor a WHERE UPPER(a.owner) = ?1");
            delete.setParameter(1, owner.toUpperCase(Locale.getDefault()));
            count = delete.executeUpdate();
        }


            log.debug("Exit: " + count + " anchor records deleted");
    }

}
