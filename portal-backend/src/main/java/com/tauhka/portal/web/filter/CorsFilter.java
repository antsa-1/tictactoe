package com.tauhka.portal.web.filter;

import java.io.IOException;
import java.util.logging.Logger;

import com.tauhka.portal.util.Constants;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {
	private static final String ENVIRONMENT = System.getProperty("Server_Environment");
	private static final Logger LOGGER = Logger.getLogger(CorsFilter.class.getName());

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		//System.out.println("CorsFilter: Environment" + ENVIRONMENT);
		if (ENVIRONMENT == null) {
			throw new IllegalArgumentException("Environment is missing");
		}
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		if (ENVIRONMENT.equalsIgnoreCase(Constants.ENVIRONMENT_PRODUCTION)) {
			// No CORS settings required in prod -> same domain
			chain.doFilter(request, servletResponse);
			return;
		}
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", " http://localhost:8080"); //For localhost development with different 8080 port.
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, POST");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers", "content-type, authorization");

		if (request.getMethod().equals("OPTIONS")) {

			return;
		}

		chain.doFilter(request, servletResponse);
	}
}
