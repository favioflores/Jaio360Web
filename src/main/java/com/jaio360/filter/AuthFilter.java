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
import org.apache.log4j.Logger;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.xhtml"})
public class AuthFilter implements Filter {

    private static Logger log = Logger.getLogger(AuthFilter.class);

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

            try {
                session.removeAttribute("usuarioInfoProxy");
            } catch (Exception e) {
            }
            try {
                session.removeAttribute("usuarioInfo");
            } catch (Exception e) {
            }
            try {
                session.removeAttribute("proyectoInfo");
            } catch (Exception e) {
            }

            res.sendRedirect(req.getContextPath() + "/login.jsf");  // Anonymous user. Redirect to login page

        } catch (IOException e) {
            log.error(e);
        }
    }

    private Boolean permissionToAccessFreely(HttpServletRequest req) {

        String strUri = req.getServletPath();

        return strUri.endsWith(Constantes.STR_CSS)
                || strUri.endsWith(Constantes.STR_GIF)
                || strUri.endsWith(Constantes.STR_PNG)
                || strUri.endsWith(Constantes.STR_JPG)
                || strUri.endsWith(Constantes.STR_HTM)
                || strUri.endsWith(Constantes.STR_JS)
                || strUri.endsWith(Constantes.STR_MP4)
                
                || strUri.contains("/login.jsf")
                || strUri.contains("/accountVerified.jsf")
                || strUri.contains("/accountNotExist.jsf")
                || strUri.contains("/recoveryPassword.jsf")
                || strUri.contains("/sesionExpirada.jsf")
                || strUri.contains("/accountVerifiedSuccess.jsf")
                || strUri.contains("/verifyAccount.jsf")
                || strUri.contains("/resourceNotFound.jsf")
                || strUri.contains("/help.jsf")
                || strUri.contains("/landing.jsf")
                || strUri.contains("/public/")
                || strUri.contains("javax.faces.resource");
    }

    private Boolean permissionOnlyToUserProxy(HttpServletRequest req) {

        String strUri = req.getServletPath();

        return strUri.contains("/stepOne.jsf")
                || strUri.contains("/stepTwo.jsf")
                || strUri.contains("/stepThree.jsf")
                || strUri.contains("/stepFour.jsf")
                || strUri.contains("/stepFive.jsf")
                || strUri.contains("/stepSix.jsf")
                || strUri.contains("/dashboardDetail.jsf")
                || strUri.contains("/dashboard.jsf");
    }
}
