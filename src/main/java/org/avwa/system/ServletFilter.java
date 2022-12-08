
package org.avwa.system;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.avwa.entities.User;
import org.avwa.enums.UserRoleEnum;
import org.avwa.system.authentication.oAuth.OAuth;
import org.avwa.system.authentication.oAuth.OAuthProviderType;
import org.avwa.system.authorization.Authorization;
import org.avwa.utils.AnnotationsUtils;
import org.avwa.utils.PathUtils;
import org.slf4j.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.ResourceHandler;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
import jakarta.servlet.http.HttpSession;

@ApplicationScoped
@WebFilter(urlPatterns = "/*")
public class ServletFilter implements Filter {

    @Inject
    Logger log;

    @PersistenceContext
    private EntityManager em;

    @Inject
    OAuth oAuth;

    @Inject
    Authorization authorization;

    @Inject
    JsfUtilsEJB jsfUtilsEJB;

    @Inject
    ApplicationEJB applicationEJB;

    @Inject
    SessionEJB sessionEJB;

    @Inject
    TodoJobManager todoJobManager;

    FilterConfig filterConfig;

    private final String xhtmlExtension = ".xhtml";
    private final String defaultDirectoryPage = "index.xhtml";
    private final String pageNotFoundUri = "pageNotFound.xhtml";
    private final String unauthorizedPage = "unauthorized.xhtml";
    private final String resourcePath = "/public/assets";

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

        log.debug("requested path:" + requestPath);

        int indexOfSecondSlash = requestPath.indexOf("/", 1);
        String rootUri = requestPath.substring(0, indexOfSecondSlash);
        String uri = requestPath.substring(indexOfSecondSlash);

        String requestedDirectoryWithoutRoot = "";

        boolean requestedResource = false;

        forDevelopementEnvAutoLogin(httpRequest);

        HttpSession session = httpRequest.getSession(false);

        todoJobManager.doJobsForSession(session.getId());

        if (!PathUtils.accessingOneOfThePaths(uri, authorization.getRestfulEndPooints())) {// skip restful requests

            if (requestPath.endsWith("/")) {

                // requested directory

                Set<String> paths = servletContext.getResourcePaths(uri);

                if (PathUtils.containsPage(paths, defaultDirectoryPage))
                    requestPath += "index.xhtml";
                else
                    requestPath = rootUri + "/" + pageNotFoundUri;

                requestedDirectoryWithoutRoot = uri;

            } else {

                // requested file

                if (!isResourceReq(requestPath)) { // if request is not for resource
                    int lastSlashIndex = uri.lastIndexOf("/");
                    String path = uri.substring(0, lastSlashIndex + 1);

                    String fileName = uri.substring(lastSlashIndex + 1);
                    if (fileName.indexOf(".") < 0) { // requested file name (with extension)
                        fileName += xhtmlExtension;
                    }
                    Set<String> paths = servletContext.getResourcePaths(path);
                    if (!PathUtils.containsPage(paths, fileName)) {
                        // no page for request
                        requestPath = rootUri + "/" + pageNotFoundUri;
                        requestedDirectoryWithoutRoot = "/";
                    } else {
                        // final request path
                        requestPath = rootUri + path + fileName;
                        requestedDirectoryWithoutRoot = path;
                    }

                    // OAuth2.0
                    String login = req.getParameter("login"); // value = oauth provider type
                    String requestPathWithoutRoot = requestPath.substring(indexOfSecondSlash);
                    if (login != null && requestPathWithoutRoot.equalsIgnoreCase("/login.xhtml")) {
                        log.debug("OAuth login with " + login);
                        OAuthProviderType providerType = OAuthProviderType.valueOf(login.toUpperCase());
                        if (providerType != null) {
                            oAuth.getCode(httpResponse, providerType);
                            return;
                        }
                    }
                } else {
                    requestedResource = true;
                }
            }
        } else {
            log.debug("servlet filter skipped restful end point:" + requestPath);
        }

        // check authorization for directory
        if (!requestedResource) {
            setHttpSessionInfoValues(httpRequest);
            if (!authorization.hasPermissionOnDir(requestedDirectoryWithoutRoot)) {
                log.debug("unauthorized directory:" + requestedDirectoryWithoutRoot);
                jsfUtilsEJB.forwardWithDispatcher(httpRequest, httpResponse, "/" + unauthorizedPage);
                return;
            }
        }

        if (!requestPath.equalsIgnoreCase(reguestPathInitial)) {
            // on request with only application path without leading slash
            // request is redirected with added leading slash to the request path
            // - all other requests have indexOfSecondFlash
            if (indexOfSecondSlash > 0) {
                requestPath = requestPath.substring(indexOfSecondSlash);
                log.debug("dispatch forward to:" + requestPath);
                RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(requestPath);
                try {
                    setHeadersForNotCaching(resp);
                    dispatcher.forward(httpRequest, httpResponse);
                    return;
                } catch (ServletException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        // set headers for resources caching
        if (requestedResource) {
            setHeadersForResources(resp);
        } else {
            setHeadersForNotCaching(resp);
        }
        chain.doFilter(req, resp);// sends request to next resource
    }

    private void setHttpSessionInfoValues(HttpServletRequest request) {
        long start = System.currentTimeMillis();
        String ipAddress = request.getRemoteAddr();
        HttpSession httpSession = request.getSession(false);
        HttpSessionInfo info = applicationEJB.getHttpsessions().get(httpSession.getId());
        info.setIp(ipAddress);
        info.setUser(sessionEJB.getUser());
        long stop = System.currentTimeMillis();

        log.debug("timing: " + (stop-start));

    }

    private boolean isResourceReq(String uri) {
        if (uri.contains(ResourceHandler.RESOURCE_IDENTIFIER) ||
                uri.startsWith(applicationEJB.getContextPath() + resourcePath)) {
            return true;
        }
        return false;
    }

    private void setHeadersForResources(ServletResponse resp) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        ((HttpServletResponse) resp).setHeader("Expires", String.valueOf(calendar.getTimeInMillis()));
    }

    private void setHeadersForNotCaching(ServletResponse resp) {
        HttpServletResponse response = (HttpServletResponse) resp;
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
    }

    @Deprecated
    public void forDevelopementEnvAutoLogin(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        log.debug("remote ip address:" + ipAddress);
        if (sessionEJB.getUser().getRole().equals(UserRoleEnum.PUBLIC) && "127.0.0.1".equals(ipAddress)) {
            TypedQuery<User> query = em.createQuery(
                    "SELECT e FROM " + AnnotationsUtils.getEntityName(User.class)
                            + " e WHERE e.email = :email",
                    User.class);
            List<User> list = query.setParameter("email", "neratokio@sages.lt").getResultList();
            if (list.size() > 0) {
                sessionEJB.setUser(list.get(0));
            }

        }
    }

    public void destroy() {
    }
}
