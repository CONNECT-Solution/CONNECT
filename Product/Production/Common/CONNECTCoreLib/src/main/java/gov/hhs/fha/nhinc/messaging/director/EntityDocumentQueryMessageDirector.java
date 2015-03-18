/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.messaging.director;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.messaging.builder.AdhocQueryRequestBuilder;
import gov.hhs.fha.nhinc.messaging.builder.AssertionBuilder;
import gov.hhs.fha.nhinc.messaging.builder.Builder;
import gov.hhs.fha.nhinc.messaging.builder.NhinTargetCommunitiesBuilder;

/**
 *
 * @author tjafri
 */
public interface EntityDocumentQueryMessageDirector extends Builder {

    /**
     * Gets the message.
     *
     * @return the message
     */
    public RespondingGatewayCrossGatewayQueryRequestType getMessage();

    /**
     * Sets the document query builder.
     *
     * @param dqBuilder the new document query builder
     */
    public void setDocumentQueryBuilder(AdhocQueryRequestBuilder dqBuilder);

    /**
     * Sets the assertion builder.
     *
     * @param assertionBuilder the new assertion builder
     */
    public void setAssertionBuilder(AssertionBuilder assertionBuilder);

    /**
     * Sets the target communities builder.
     *
     * @param targetBuilder the new target communities builder
     */
    public void setTargetCommunitiesBuilder(NhinTargetCommunitiesBuilder targetBuilder);

}
