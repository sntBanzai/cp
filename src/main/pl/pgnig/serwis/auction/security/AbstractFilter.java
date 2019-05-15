/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.security;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jboss.logging.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pl.pgnig.serwis.auction.service.SessionAuthorizationService;

/**
 *
 * @author a6jmalyszko
 */
public abstract class AbstractFilter implements Filter {
    
    static final String FORBIDDEN_403_MESSAGE = "forbidden_403_message";

    protected SessionAuthorizationService authService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        authService = (SessionAuthorizationService) WebApplicationContextUtils.
                getRequiredWebApplicationContext(filterConfig.getServletContext()).
                getBean("sessionAuthorizationService");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Logger.getLogger(this.getClass(), "filtering");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String name = req.getUserPrincipal().getName();
        try {
            if (!name.startsWith("PGN")) {
                throw new FilteringFailedException();
            }
            name = name.replace("PGN", "").replace("/", "").replace("\\", "");
            actualFiltering(name);
        } catch (FilteringFailedException ffe) {
            Logger.getLogger(this.getClass()).info("Unauthorized access attempt: resource" + req.getRequestURI() + ", principal: " + name, ffe);
            res.sendError(ERR_CODE, ResourceBundle.getBundle("messages").getString(FORBIDDEN_403_MESSAGE));
            return;
        }
        chain.doFilter(request, response);
    }
    private static final int ERR_CODE = 403;


    @Override
    public void destroy() {
    }


    protected abstract void actualFiltering(String userName) throws FilteringFailedException;

}
