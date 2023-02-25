/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.filter;

import com.jaio360.utils.Constantes;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.xhtml"})
public class AuthFilter implements Filter {

    private static Log log = LogFactory.getLog(AuthFilter.class);

    public AuthFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {

            HttpServletRequest req = (HttpServletRequest) request;

            HttpSession ses = req.getSession(false);

            if (permissionToAccessFreely(req)) {
                chain.doFilter(request, response);
            } else {

                if (ses == null) {

                    redirect(request, response);

                } else {

                    if (ses.getAttribute("usuarioInfoProxy") == null) {

                        if (ses.getAttribute("usuarioInfo") == null) {
                            redirect(request, response);
                        } else {
                            chain.doFilter(request, response);
                        }

                    } else {

                        if (permissionOnlyToUserProxy(req)) {
                            chain.doFilter(request, response);
                        } else {
                            redirect(request, response);
                        }

                    }
                }

            }

        } catch (IOException | ServletException t) {
            log.error(t);
        }
    } //doFilter

    @Override
    public void destroy() {

    }

    private void redirect(ServletRequest request, ServletResponse response) {
        try {

            HttpServletResponse res = (HttpServletResponse) response;
            HttpServletRequest req = (HttpServletRequest) request;

            HttpSession session = req.getSession(false);

            session.removeAttribute("usuarioInfoProxy");
            session.removeAttribute("usuarioInfo");
            session.removeAttribute("proyectoInfo");

            res.sendRedirect(req.getContextPath() + "/ui/sesionExpirada.jsf");  // Anonymous user. Redirect to login page
        } catch (IOException e) {
            log.error(e);
        }
    }

    private boolean permissionToAccessFreely(HttpServletRequest req) {

        String strUri = req.getServletPath();

        return strUri.endsWith(Constantes.STR_CSS)
                || strUri.endsWith(Constantes.STR_GIF)
                || strUri.endsWith(Constantes.STR_PNG)
                || strUri.endsWith(Constantes.STR_JPG)
                || strUri.endsWith(Constantes.STR_HTM)
                || strUri.endsWith(Constantes.STR_JS)
                || strUri.endsWith(Constantes.STR_SWF)
                || strUri.contains("/ui/login.jsf")
                || strUri.contains("/ui/accountVerified.jsf")
                || strUri.contains("/ui/accountNotExist.jsf")
                || strUri.contains("/ui/recoveryPassword.jsf")
                || strUri.contains("/ui/sesionExpirada.jsf")
                || strUri.contains("/ui/test.jsf")
                || strUri.contains("/ui/accountVerifiedSuccess.jsf")
                || strUri.contains("/ui/verifyAccount.jsf")
                || strUri.contains("/public/")
                || strUri.contains("javax.faces.resource");
    }

    private boolean permissionOnlyToUserProxy(HttpServletRequest req) {

        String strUri = req.getServletPath();

        return strUri.contains("/ui/stepOne.jsf")
                || strUri.contains("/ui/stepTwo.jsf")
                || strUri.contains("/ui/stepThree.jsf")
                || strUri.contains("/ui/stepFour.jsf")
                || strUri.contains("/ui/stepFive.jsf")
                || strUri.contains("/ui/stepSix.jsf");
    }
}
