module portal {
	exports com.tauhka.portal.web.exeptions;
	exports com.tauhka.portal.pojos;
	exports com.tauhka.portal.web.filter;
	exports com.tauhka.portal.web.resources;
	exports com.tauhka.portal.ejb;
	exports com.tauhka.portal.web.security;

	requires jakarta.annotation;
	requires jakarta.ejb.api;
	requires jakarta.inject;
	requires jakarta.json.bind;
	requires jakarta.servlet;
	requires jakarta.ws.rs;
	requires java.logging;
	requires java.sql;
}