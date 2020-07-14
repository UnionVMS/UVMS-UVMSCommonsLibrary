/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.commons.message.context;

import javax.enterprise.inject.Vetoed;
import java.util.Objects;

/**
 * Data object containing all the fields of the FLUX envelope the system needs to propagate in calls between modules.
 */
@Vetoed
public class FluxEnvelopePropagatedData {
	private final String messageGuid;
	private final String dataflow;
	private final String senderOrReceiver;

	/**
	 * Construct from all fields.
	 *
	 * @param messageGuid The message GUID
	 * @param dataflow The dataflow
	 * @param senderOrReceiver The sender or receiver
	 */
	public FluxEnvelopePropagatedData(String messageGuid, String dataflow, String senderOrReceiver) {
		this.messageGuid = messageGuid;
		this.dataflow = dataflow;
		this.senderOrReceiver = senderOrReceiver;
	}

	@Override
	public int hashCode() {
		return Objects.hash(messageGuid, dataflow, senderOrReceiver);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		FluxEnvelopePropagatedData other = (FluxEnvelopePropagatedData) obj;
		return Objects.equals(messageGuid, other.messageGuid) && Objects.equals(dataflow, other.dataflow) && Objects.equals(senderOrReceiver, other.senderOrReceiver);
	}

	/**
	 * The message GUID.
	 *
	 * @return The message GUID
	 */
	public String getMessageGuid() {
		return messageGuid;
	}

	/**
	 * Get the dataflow (attribute {@code DF}).
	 *
	 * @return The dataflow
	 */
	public String getDataflow() {
		return dataflow;
	}

	/**
	 * Get the sender or receiver of the message (attribute {@code FR}).
	 *
	 * @return The sender or receiver
	 */
	public String getSenderOrReceiver() {
		return senderOrReceiver;
	}
}
