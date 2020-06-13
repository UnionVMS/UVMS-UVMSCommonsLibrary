/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.commons.message.context;

import javax.enterprise.context.ApplicationScoped;
import javax.jms.JMSException;
import javax.jms.Message;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageRuntimeException;

/**
 * Implementation of the {@link JmsFluxEnvelopeHelper}.
 */
@ApplicationScoped
public class JmsFluxEnvelopeHelperImpl implements JmsFluxEnvelopeHelper {

	private static final String NAME_MESSAGE_GUID = FluxEnvelopeConstants.PREFIX + '_' + FluxEnvelopeConstants.MESSAGE_GUID;
	private static final String NAME_DATAFLOW = FluxEnvelopeConstants.PREFIX + '_' + FluxEnvelopeConstants.DF;
	private static final String NAME_SENDER_OR_RECEIVER = FluxEnvelopeConstants.PREFIX + '_' + FluxEnvelopeConstants.FR;

	@Override
	public FluxEnvelopePropagatedData extract(Message message) {
		try {
			String messageGuid = message.getStringProperty(NAME_MESSAGE_GUID);
			String dataflow = message.getStringProperty(NAME_DATAFLOW);
			String senderOrReceiver = message.getStringProperty(NAME_SENDER_OR_RECEIVER);
			if (allNull(messageGuid, dataflow, senderOrReceiver)) {
				return null;
			} else {
				return new FluxEnvelopePropagatedData(messageGuid, dataflow, senderOrReceiver);
			}
		} catch (JMSException e) {
			throw new MessageRuntimeException(e);
		}
	}

	@Override
	public void setHeaders(FluxEnvelopePropagatedData data, Message message) {
		if (data == null) {
			return;
		}
		try {
			if (data.getMessageGuid() != null) {
				message.setStringProperty(NAME_MESSAGE_GUID, data.getMessageGuid());
			}
			if (data.getDataflow() != null) {
				message.setStringProperty(NAME_DATAFLOW, data.getDataflow());
			}
			if (data.getSenderOrReceiver() != null) {
				message.setStringProperty(NAME_SENDER_OR_RECEIVER, data.getSenderOrReceiver());
			}
		} catch (JMSException e) {
			throw new MessageRuntimeException(e);
		}
	}

	private boolean allNull(Object o1, Object o2, Object o3) {
		return o1 == null && o2 == null && o3 == null;
	}
}
