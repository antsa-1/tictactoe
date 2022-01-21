package com.tauhka.portal.web.filter;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SecurityFilter implements Filter {
	private static final Logger LOGGER = Logger.getLogger(SecurityFilter.class.getName());

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse resp = (HttpServletResponse) servletResponse;
		resp.addHeader("X-Frame-Options", "DENY");
		resp.addHeader("X-Content-Type-Options", "nosniff");

		chain.doFilter(request, servletResponse);
	}

}
