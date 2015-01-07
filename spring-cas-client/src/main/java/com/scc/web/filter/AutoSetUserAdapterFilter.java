package com.scc.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by rolyer on 15-1-7.
 */
public class AutoSetUserAdapterFilter implements Filter {
    public AutoSetUserAdapterFilter() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        Object object = httpRequest.getSession().getAttribute("_const_cas_assertion_");
        if(object != null) {
            ;
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}