package org.avwa.system.authentication.oAuth;

import java.io.IOException;

import org.apache.oltu.oauth2.common.OAuthProviderType;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/socialauth/returnurl")
public class ReturnServletFacebook extends HttpServlet {

    @Inject
    ReturnServletUtil returnServletUtil;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        returnServletUtil.doGet(request, response, OAuthProviderType.FACEBOOK);
    }
}
