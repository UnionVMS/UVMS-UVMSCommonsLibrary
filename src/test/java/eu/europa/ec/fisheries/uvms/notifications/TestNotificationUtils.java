package eu.europa.ec.fisheries.uvms.notifications;

import junit.framework.TestCase;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TestNotificationUtils extends TestCase {

	public void testStringProperty() throws JsonProcessingException {
		String textMessage = NotificationUtils.getTextMessage(new NotificationMessage("testKey", "testValue"));
		assertEquals("{\"testKey\":\"testValue\"}", textMessage);
	}

	public void testIntegerProperty() throws JsonProcessingException {
		String textMessage = NotificationUtils.getTextMessage(new NotificationMessage("testKey", 12));
		assertEquals("{\"testKey\":12}", textMessage);
	}

	public void testBooleanProperty() throws JsonProcessingException {
		String textMessage = NotificationUtils.getTextMessage(new NotificationMessage("testKey", false));
		assertEquals("{\"testKey\":false}", textMessage);
	}

}
