package org.avwa.system;

import java.io.IOException;

import org.slf4j.Logger;

import jakarta.ejb.Stateless;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Stateless
public class JsfUtilsEJB {

    @Inject
    Logger log;

    public void redirectTo(String uri) {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/");
            log.debug("redirecting to:" + uri + ", context path:" + ec.getRequestContextPath());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void forwardWithDispatcher(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String requestPath) {
        RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(requestPath);
        try {
            dispatcher.forward(httpRequest, httpResponse);
        } catch (IOException|ServletException e) {
            log.error(e.getMessage(), e);
        }

    }
}
