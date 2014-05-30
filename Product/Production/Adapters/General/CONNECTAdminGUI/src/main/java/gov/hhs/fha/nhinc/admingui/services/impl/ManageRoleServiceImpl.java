/*
 *  Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above
 *        copyright notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of the United States Government nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.admingui.services.impl;

import gov.hhs.fha.nhinc.admingui.model.Role;
import gov.hhs.fha.nhinc.admingui.model.Role.PageAccessMapping;
import gov.hhs.fha.nhinc.admingui.services.ManageRoleService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.springframework.stereotype.Service;

/**
 * The Class ManageRoleServiceImpl.
 *
 * @author msw
 */
@Service
public class ManageRoleServiceImpl implements ManageRoleService {

    /** The roles. */
    private Set<Role> roles;

    /**
     * Instantiates a new manage role service impl.
     */
    public ManageRoleServiceImpl() {

    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        Role r = new Role();

        List<PageAccessMapping> pages = new ArrayList<PageAccessMapping>();
        pages.add(r.new PageAccessMapping("acctmanagePrime.xhtml"));
        pages.add(r.new PageAccessMapping("DashboardPrime.xhtml"));
        pages.add(r.new PageAccessMapping("ManageRole.xhtml"));
        pages.add(r.new PageAccessMapping("StatusPrime.xhtml"));
        pages.add(r.new PageAccessMapping("direct.xhtml"));
        pages.add(r.new PageAccessMapping("trust-bundle-anchor1.xhtml"));
        pages.add(r.new PageAccessMapping("trust-bundle-anchor2.xhtml"));
        
        DataModel<PageAccessMapping> pagesModel = new ListDataModel<PageAccessMapping>(pages);

        r.setName("Admin");
        r.setPageMappings(pagesModel);

        Role r2 = new Role();
        List<PageAccessMapping> pages2 = new ArrayList<PageAccessMapping>();
        pages2.add(r2.new PageAccessMapping("acctmanagePrime.xhtml"));
        pages2.add(r2.new PageAccessMapping("DashboardPrime.xhtml"));
        pages2.add(r2.new PageAccessMapping("ManageRole.xhtml"));
        pages2.add(r2.new PageAccessMapping("StatusPrime.xhtml"));
        pages2.add(r2.new PageAccessMapping("direct.xhtml"));
        pages2.add(r2.new PageAccessMapping("trust-bundle-anchor1.xhtml"));
        pages2.add(r2.new PageAccessMapping("trust-bundle-anchor2.xhtml"));

        DataModel<PageAccessMapping> pagesModel2 = new ListDataModel<PageAccessMapping>(pages2);
        r2.setName("User");
        r2.setPageMappings(pagesModel2);

        roles = new HashSet<Role>();
        roles.add(r);
        roles.add(r2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.admingui.services.ManageRoleService#getRoles()
     */
    @Override
    public Set<Role> getRoles() {
        return roles;
    }

}
