/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.commons.message.context;

import javax.jms.Message;

/**
 * Extract a {@link FluxEnvelopePropagatedData} object from  JMS message or set the message headers from one.
 */
public interface JmsFluxEnvelopeHelper {
	/**
	 * Extract a {@code FluxEnvelopePropagatedData} object from the headers, returning {@code null}
	 * if no header is set.
	 *
	 * @param message The message
	 * @return The data, can be {@code null} if no relevant header is set
	 */
	FluxEnvelopePropagatedData extract(Message message);

	/**
	 * Set the message headers from the given data, does nothing if data is null.
	 *
	 * @param data    The data object containing the header values, can be {@code null}
	 * @param message The message to set the headers
	 */
	void setHeaders(FluxEnvelopePropagatedData data, Message message);
}
