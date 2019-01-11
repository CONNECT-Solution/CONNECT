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

package gov.hhs.fha.nhinc.directconfig.dao;

import gov.hhs.fha.nhinc.directconfig.entity.Anchor;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import java.util.List;

/**
 * Anchor data access methods.
 */
public interface AnchorDao {

    /**
     * Load an Anchor.
     *
     * @param owner
     *            The Anchor owner.
     * @return an Anchor.
     */
    public Anchor load(String owner);

    /**
     * Get a collection of all Anchors.
     *
     * @return a collection of all Anchors.
     */
    public List<Anchor> listAll();

    /**
     * Get a collection of Anchors.
     *
     * @param owners
     *            A collection of owners.
     * @return a collection of Anchors.
     */
    public List<Anchor> list(List<String> owners);

    /**
     * Add an anchor
     *
     * @param anchor
     *            The anchor to add.
     */
    public void add(Anchor anchor);

    /**
     * Save an Anchor.
     *
     * @param anchor
     *            The Anchor.
     */
    public void save(Anchor anchor);

    /**
     * Save a collection of Anchors.
     *
     * @param anchorList
     *            The collection of Anchors.
     */
    public void save(List<Anchor> anchorList);

    /**
     * Get a list by anchor ids
     * @param anchorIds
     *    List of ids to retrieved anchors for.
     * @return  Collection of anchors matching the anchorIds
     */
    public List<Anchor> listByIds(List<Long> anchorIds);

    /**
     * Set the status for a collection of Anchors.
     *
     * @param anchorIDs
     *            The Anchor IDs.
     * @param status
     *            The Anchor status.
     */
    public void setStatus(List<Long> anchorIDs, EntityStatus status);

    /**
     * Set the status of an Anchor.
     *
     * @param owner
     *            The Anchor owner.
     * @param status
     *            The Anchor status.
     */
    public void setStatus(String owner, EntityStatus status);

    /**
     * Delete a collection of Anchors.
     *
     * @param idList
     *            The collection of Anchor IDs.
     */
    public void delete(List<Long> idList);

    /**
     * Delete a collection of Anchors.
     *
     * @param owner
     *            The Anchor owner.
     */
    public void delete(String owner);

}
