package eu.europa.ec.fisheries.uvms.notifications;

import java.util.HashMap;
import java.util.Map;

public class NotificationMessage {

	private Map<String, Object> properties = new HashMap<>();

	public NotificationMessage(String msg) {
		// do nothing
	}

	public NotificationMessage(String propertyName, Object propertyValue) {
		setProperty(propertyName, propertyValue);
	}

	public void setProperty(String propertyName, Object propertyValue) {
		properties.put(propertyName, propertyValue);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}
}
