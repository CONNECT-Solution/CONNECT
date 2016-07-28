/**
 * 
 */
package gov.hhs.fha.nhinc.admingui.servlet;

import gov.hhs.fha.nhinc.admingui.services.exception.SanitizationException;

import java.io.IOException;
import java.util.Enumeration;

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
    private FilterConfig filterConfig = null;
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
      LOG.debug("Preapre to go inside doFilter version 5 ");
      if (!(request instanceof HttpServletRequest)) {
          filterChain.doFilter(request, response);
          return;
      }
      if (request instanceof HttpServletRequest){
          HttpServletRequest hrequest = (HttpServletRequest)request;
          HttpServletResponse hresponse = (HttpServletResponse)response;
          LOG.debug("SErvlet Request: "+hrequest.getServletPath());
          try {             
              checkHeaders(hrequest);              
          } catch (SanitizationException e) {
              LOG.error("Error in SanitilizeMessage {}: ",e.getLocalizedMessage(), e );
              hrequest.getSession().invalidate();
              filterConfig.getServletContext().getRequestDispatcher("/customerror.xhtml").forward(hrequest,hresponse);
              //need to route to error page              
              //throw new ServletException(e);
              
          } finally {
              ESAPI.httpUtilities().clearCurrent();
          }
          if (hresponse.isCommitted()){
              return;
          }
          filterChain.doFilter(hrequest,hresponse);
      }else{
          LOG.debug("request is not instance of HttpServletRequest");
          filterChain.doFilter(request, response);
          return;
      }      
      LOG.debug("Done filter");
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
       LOG.debug("init my own filter");
       this.filterConfig = filterConfig;
        
    }
    private void checkHeaders(final HttpServletRequest request) throws SanitizationException {
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String name = headerNames.nextElement();
            final String value = request.getHeader(name);
            try{
                LOG.debug(name+"-->"+value);
                boolean isValid = ESAPI.validator().isValidInput("HTTP header value: " + value, value, "HTTPHeaderValue", 200, true);
                if (!isValid){
                    StringBuilder strBuilder = new StringBuilder();
                    strBuilder.append("Detect malious code -->orignal name/values: ");
                    strBuilder.append(name);
                    strBuilder.append("/");
                    strBuilder.append(value);
                    LOG.error(strBuilder.toString());                    
                    throw new SanitizationException(strBuilder.toString());
                }
            }catch(IntrusionException ex){
                throw new SanitizationException(ex);
            }
            

        }
    }
    }
