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
package gov.hhs.fha.nhinc.admingui.servlet;

import gov.hhs.fha.nhinc.admingui.constant.NavigationConstant;
import gov.hhs.fha.nhinc.admingui.services.exception.SanitizationException;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.collections.CollectionUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mpnguyen
 *
 */
public class SanitizationFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(SanitizationFilter.class);
    /**
     * AntiSammy policy
     */
    private AntiSamy antiSamy;
    private Policy antiSamyPolicy = null;

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        LOG.debug("Destroying my filter");
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     * javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        LOG.debug("Prepare to intercept request in Filter");
        if (request instanceof HttpServletRequest) {
            HttpServletRequest hrequest = (HttpServletRequest) request;
            final String servletPath = hrequest.getServletPath();
            LOG.debug("servlet Request: {}", servletPath);
            // Prevent rendering of JSESSIONID in URLs for all outgoing links
            HttpServletResponseWrapper hresponse = new CustomHttpServletResponseWrapper((HttpServletResponse) response);
            try {
                // skip to check for js/image/css so that the custom error page can render properly
                if (!servletPath.startsWith(ResourceHandler.RESOURCE_IDENTIFIER)) {
                    checkHeaders(hrequest);
                }
            } catch (SanitizationException e) {
                LOG.error("Error in SanitizeMessage {}: ", e.getLocalizedMessage(), e);
                hrequest.setAttribute("javax.servlet.error.exception", e);
                hrequest.getSession().invalidate();
                hrequest.getServletContext().getRequestDispatcher(NavigationConstant.CUSTOM_ERROR_XHTML)
                    .forward(hrequest, hresponse);
            }
            if (hresponse.isCommitted()) {
                /*
                 * since we already forward to another request, we need to return immediately to prevent "Response
                 * already commit
                 */
                return;
            }
            filterChain.doFilter(hrequest, hresponse);
        } else {
            LOG.debug("request is not instance of HttpServletRequest");
            filterChain.doFilter(request, response);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.debug("init my own filter");
        // copy code from HTMLValidationRule
        final URL urlResource = loadResource("antisamy-esapi.xml");
        if (urlResource != null) {
            try {
                antiSamyPolicy = Policy.getInstance(urlResource);
                antiSamy = new AntiSamy(antiSamyPolicy);
            } catch (PolicyException e) {
                throw new ServletException("Couldn't parse antisamy policy", e);
            }
        } else {
            throw new ServletException("Unable to locate antisamy-esapi.xml");
        }
    }

    private void checkHeaders(final HttpServletRequest request) throws SanitizationException {
        final Enumeration<String> headerNames = request.getHeaderNames();
        LOG.debug("About to check header malicious code");
        while (headerNames.hasMoreElements()) {
            final String name = headerNames.nextElement();
            final String value = request.getHeader(name);
            sanitizeInput(value);
        }
        LOG.debug("End of checking header malicious code");
    }

    /**
     * Sanitize value. will throw exception if detect malicious code
     *
     * @param value
     * @throws SanitizationException
     */
    private void sanitizeInput(final String value) throws SanitizationException {
        try {
            CleanResults result = antiSamy.scan(value);
            if (CollectionUtils.isNotEmpty(result.getErrorMessages())) {
                throw new SanitizationException("Malicious code has been detected on the HTTP header");
            }
        } catch (ScanException | PolicyException | SanitizationException ex) {
            throw new SanitizationException(ex);
        }
    }

    /**
     * Load Resource from Thread Context Loader/ classloader and classpath Adop from DefaultSecurityConfiguration class
     *
     * @param fileName
     * @return
     */
    private URL loadResource(String fileName) {
        URL urlResource = null;
        ClassLoader[] loaders = new ClassLoader[]{Thread.currentThread().getContextClassLoader(),
            ClassLoader.getSystemClassLoader(), this.getClass().getClassLoader()};
        for (int i = 0; i < loaders.length; i++) {
            if (loaders[i] != null) {
                urlResource = loaders[i].getResource(fileName);
                if (urlResource != null) {
                    break;
                }
            }
        }
        return urlResource;

    }

    //This class will prevent to embedded jsessionid into browser url
    private class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {

        /**
         * @param arg0
         */
        public CustomHttpServletResponseWrapper(HttpServletResponse servletResponse) {
            super(servletResponse);
        }

        @Override
        public String encodeRedirectUrl(String url) {
            return url;
        }

        @Override
        public String encodeRedirectURL(String url) {
            return url;
        }

        @Override
        public String encodeUrl(String url) {
            return url;
        }

        @Override
        public String encodeURL(String url) {
            return url;
        }
    }
}
