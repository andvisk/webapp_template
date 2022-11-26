
package org.avwa.system;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.avwa.system.authentication.oAuth.SecurityConfiguration;
import org.avwa.utils.PathUtils;
import org.slf4j.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.ResourceHandler;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ApplicationScoped
@WebFilter(urlPatterns = "/*")
public class ServletFilter implements Filter {

    @Inject
    Logger log;

    FilterConfig filterConfig;

    private final String xhtmlExtension = ".xhtml";
    private final String defaultDirectoryPage = "index.xhtml";
    private final String pageNotFoundUri = "pageNotFound.xhtml";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) resp;

        ServletContext servletContext = filterConfig.getServletContext();

        String requestPath = httpRequest.getRequestURI();
        String reguestPathInitial = requestPath;

        int indexOfSecondSlash = requestPath.indexOf("/", 1);
        String rootUri = requestPath.substring(0, indexOfSecondSlash);
        String uri = requestPath.substring(indexOfSecondSlash);

        // Set<String> resourcePaths = servletContext.getResourcePaths("/");

        if (requestPath.endsWith("/")) {
            // requested directory
            Set<String> paths = servletContext.getResourcePaths(uri);

            if (PathUtils.containsPage(paths, defaultDirectoryPage))
                requestPath += "index.xhtml";
            else
                requestPath = rootUri + "/" + pageNotFoundUri;

        } else {
            if (!requestPath.contains(ResourceHandler.RESOURCE_IDENTIFIER)) {
                int lastSlashIndex = uri.lastIndexOf("/");
                String path = uri.substring(0, lastSlashIndex + 1);
                String fileName = uri.substring(lastSlashIndex + 1);
                if (fileName.indexOf(".") < 0) {
                    fileName += xhtmlExtension;
                }
                Set<String> paths = servletContext.getResourcePaths(path);
                if (!PathUtils.containsPage(paths, fileName))
                    requestPath = rootUri + "/" + pageNotFoundUri;
                else
                    requestPath = rootUri + path + fileName;

                String login = req.getParameter("login");
                String requestPathWithoutRoot = requestPath.substring(indexOfSecondSlash);
                if (login != null && requestPathWithoutRoot.equalsIgnoreCase("/login.xhtml")) {

                    log.debug("OAuth login with " + login);
                    /*if (login.compareTo("facebook") == 0) {
                        oltuAuthenticator.getCode((HttpServletResponse) res, OAuthProviderType.FACEBOOK,
                                SecurityConfiguration.facebookClientID, null, SecurityConfiguration.facebookReturnURL);
                    }

                    if (login.compareTo("google") == 0) {
                        oltuAuthenticator.getCode((HttpServletResponse) res, OAuthProviderType.GOOGLE,
                                SecurityConfiguration.googleClientID, "email profile",
                                SecurityConfiguration.googleReturnURL);
                    }*/
                }
            }
        }

        if (!requestPath.equalsIgnoreCase(reguestPathInitial)) {
            if (indexOfSecondSlash > 0) {
                requestPath = requestPath.substring(indexOfSecondSlash);
                RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(requestPath);
                try {
                    dispatcher.forward(httpRequest, httpResponse);
                } catch (ServletException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        // set headers for resources caching
        if (requestPath.contains(ResourceHandler.RESOURCE_IDENTIFIER)) {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 1);
            ((HttpServletResponse) resp).setHeader("Expires", String.valueOf(calendar.getTimeInMillis()));
        }
        chain.doFilter(req, resp);// sends request to next resource
    }

    public void destroy() {
    }
}
