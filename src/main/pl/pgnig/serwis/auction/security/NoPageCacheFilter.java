/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.security;

import java.io.IOException;
import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 *
 * @author jerzy.malyszko
 * https://stackoverflow.com/questions/3642919/javax-faces-application-viewexpiredexception-view-could-not-be-restored
 */
@Component
@RequestScope
public class NoPageCacheFilter implements Filter {

    private static final String EXPIRES = "Expires";
    private static final String NOCACHE = "no-cache";
    private static final String PRAGMA = "Pragma";
    private static final String NOCACHE_NOSTORE_MUSTREVALIDATE = "no-cache, no-store, must-revalidate";
    private static final String CACHE_CONTROL = "Cache-Control";

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain schain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (!req.getRequestURI().startsWith(req.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) { // Skip JSF resources (CSS/JS/Images/etc)
            res.setHeader(CACHE_CONTROL, NOCACHE_NOSTORE_MUSTREVALIDATE); // HTTP 1.1.
            res.setHeader(PRAGMA, NOCACHE); // HTTP 1.0.
            res.setDateHeader(EXPIRES, 0); // Proxies.
        }

        schain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
