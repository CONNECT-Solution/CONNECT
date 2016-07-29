/**
 * 
 */
package gov.hhs.fha.nhinc.admingui.servlet;

import gov.hhs.fha.nhinc.admingui.constant.NavigationConstant;
import gov.hhs.fha.nhinc.admingui.services.exception.SanitizationException;

import java.io.IOException;
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

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.IntrusionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mpnguyen
 *
 */
public class SanitizationFilter implements Filter{
    private static final Logger LOG = LoggerFactory.getLogger(SanitizationFilter.class);
    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        LOG.debug("Destroying my filter");
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
            ServletException {
      LOG.debug("Prepare to interceptor request in Filter");
      if (!(request instanceof HttpServletRequest)) {
          filterChain.doFilter(request, response);
          return;
      }
      if (request instanceof HttpServletRequest){
          HttpServletRequest hrequest = (HttpServletRequest)request;
          HttpServletResponse hresponse = (HttpServletResponse)response;
          final String servletPath = hrequest.getServletPath();
          LOG.debug("servlet Request: "+servletPath);
          try {
              //skip to check for js/image/css so that the custom error page can render properly
              if (!servletPath.startsWith(ResourceHandler.RESOURCE_IDENTIFIER)){
                  checkHeaders(hrequest);
              }
          } catch (SanitizationException e) {
              LOG.error("Error in SanitilizeMessage {}: ",e.getLocalizedMessage(), e );
              hrequest.getSession().invalidate();
              hrequest.getServletContext().getRequestDispatcher(NavigationConstant.CUSTOM_ERROR_XHTML).forward(hrequest, hresponse);
          } finally {
              ESAPI.httpUtilities().clearCurrent();
          }
          if (hresponse.isCommitted()){
                /*
                 * since we already forward to another request, we need to return immediately to prevent "Response
                 * already commit
                 */
              return;
          }
          filterChain.doFilter(hrequest,hresponse);
      }else{
          LOG.debug("request is not instance of HttpServletRequest");
          filterChain.doFilter(request, response);
          return;
      }
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
       LOG.debug("init my own filter");
    }

    private void checkHeaders(final HttpServletRequest request) throws SanitizationException {
        final Enumeration<String> headerNames = request.getHeaderNames();
        LOG.debug("About to check header malious code");
        while (headerNames.hasMoreElements()) {
            final String name = headerNames.nextElement();
            final String value = request.getHeader(name);
            try {
                boolean isValid = ESAPI.validator().isValidInput("HTTP header value: " + value, value,
                        "HTTPHeaderValue", 200, true);
                if (!isValid) {
                    StringBuilder strBuilder = new StringBuilder();
                    strBuilder.append("Detect malious code -->orignal name/values: ");
                    strBuilder.append(name);
                    strBuilder.append("/");
                    strBuilder.append(value);
                    LOG.error(strBuilder.toString());
                    throw new SanitizationException(strBuilder.toString());
                }
            } catch (IntrusionException ex) {
                throw new SanitizationException(ex);
            }
        }
        LOG.debug("End of checking header malious code");
    }
    }
