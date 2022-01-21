package com.tauhka.portal.web.resources;

import static com.tauhka.portal.util.Constants.LOG_PREFIX_PORTAL;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.tauhka.portal.ejb.FeedbackEJB;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/feedback")
public class FeedBackResource {
	private static final Logger LOGGER = Logger.getLogger(FeedBackResource.class.getName());
	@Inject
	private FeedbackEJB feedbackEJB;

	@POST
	public Response feedback(String feedback) {

		LOGGER.entering(LOG_PREFIX_PORTAL + FeedbackEJB.class.getName(), " login");
		if (feedback == null || feedback.length() < 1 || feedback.length() > 400) {
			LOGGER.exiting(LOG_PREFIX_PORTAL + FeedbackEJB.class.getName(), " login");
			return Response.status(Status.BAD_REQUEST).build();
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			LOGGER.fine("FeedBackResource: login thread wokeup");
		}
		try {
			feedbackEJB.writeFeedBack(feedback);
			LOGGER.exiting(LOG_PREFIX_PORTAL + FeedbackEJB.class.getName(), " login2");
			return Response.ok().build();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "FeedBackResource error ", e);
		}
		LOGGER.exiting(LOG_PREFIX_PORTAL + FeedbackEJB.class.getName(), " login3");
		return Response.status(Status.BAD_REQUEST).build();
	}

}
