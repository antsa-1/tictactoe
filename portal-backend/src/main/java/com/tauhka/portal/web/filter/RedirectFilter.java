package com.tauhka.portal.web.filter;

import static com.tauhka.portal.util.Constants.LOG_PREFIX_PORTAL;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RedirectFilter implements Filter {
	private static final Logger LOGGER = Logger.getLogger(RedirectFilter.class.getName());

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		LOGGER.entering(LOG_PREFIX_PORTAL + RedirectFilter.class.getName(), " doFilter");
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse resp = (HttpServletResponse) servletResponse;
		String reqURI = request.getRequestURI();
		chain.doFilter(request, servletResponse);
		LOGGER.exiting(LOG_PREFIX_PORTAL + RedirectFilter.class.getName(), " doFilter");
	}

}
