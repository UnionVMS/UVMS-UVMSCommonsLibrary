package eu.europa.ec.fisheries.uvms.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NotificationUtils {

	public static String getTextMessage(NotificationMessage message) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(message.getProperties());
	}

}
