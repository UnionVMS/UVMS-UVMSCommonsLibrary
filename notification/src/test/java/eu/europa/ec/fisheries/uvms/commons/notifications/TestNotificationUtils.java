/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.commons.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;

import junit.framework.TestCase;

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