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
package gov.hhs.fha.nhinc.admingui.exceptionhandler;

import gov.hhs.fha.nhinc.admingui.constant.MessageConstant;
import gov.hhs.fha.nhinc.admingui.constant.NavigationConstant;
import gov.hhs.fha.nhinc.admingui.services.exception.SanitizationException;
import java.util.Iterator;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles run-time exceptions and ViewExpiredException. The user is redirected either to custom-error page
 * or to login page based on the exception occurred.
 *
 * @author tjafri
 */
public class CustomExceptionHandlerWrapper extends ExceptionHandlerWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(CustomExceptionHandlerWrapper.class);
    private ExceptionHandler wrapped;

    CustomExceptionHandlerWrapper(ExceptionHandler exception) {
        wrapped = exception;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() {
        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            // get the exception from context
            Throwable t = context.getException();
            LOG.error("An exception occurred while performing user request", t);
            final FacesContext fc = FacesContext.getCurrentInstance();
            final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            final NavigationHandler nav = fc.getApplication().getNavigationHandler();

            HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
            Object servletEx = request.getAttribute("javax.servlet.error.exception");
            try {
                RequestContext.getCurrentInstance().execute("PF('dialog').hide()");
                if (servletEx instanceof SanitizationException) {
                    // SanitizationFilter has redirected to customerror page
                    fc.addMessage("errorMessage", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        MessageConstant.EXCEPTION_MSG, ""));
                    requestMap.put("exceptionMessage", ((SanitizationException) servletEx).getMessage());
                    nav.handleNavigation(fc, null, NavigationConstant.CUSTOM_ERROR_XHTML);
                } else if (t instanceof ViewExpiredException) {
                    fc.addMessage("loginErrors", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        MessageConstant.SESSION_EXPIRED_MSG, ""));
                    nav.handleNavigation(fc, null, NavigationConstant.LOGIN_XHTML);
                } else {
                    // any run-time exception will use the else block
                    fc.addMessage("errorMessage", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        MessageConstant.EXCEPTION_MSG, ""));
                    requestMap.put("exceptionMessage", t.getMessage());
                    fc.getExternalContext().invalidateSession();
                    nav.handleNavigation(fc, null, NavigationConstant.CUSTOM_ERROR_XHTML);
                }
                fc.renderResponse();
            } finally {
                // remove it from queue
                i.remove();
            }
        }
        //parent handle
        getWrapped().handle();
    }
}
