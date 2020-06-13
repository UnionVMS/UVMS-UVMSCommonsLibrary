/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.commons.message.context;

import java.util.function.Consumer;

/**
 * A stack of {@link FluxEnvelopePropagatedData} to allow nested calls.
 */
public interface FluxEnvelopeStack {
	/**
	 * Push the given data to the stack, even if it is {@code null}.
	 *
	 * @param data The data to push, can be {@code null}, but is always pushed to the stack
	 */
	void push(FluxEnvelopePropagatedData data);

	/**
	 * Pop the top data from the stack.
	 *
	 * @return The top data, {@code null} if none
	 */
	FluxEnvelopePropagatedData pop();

	/**
	 * Push the given data and run the given consumer, and pop the data when the consumer is finished in a {@code finally} block.
	 *
	 * @param data     The data
	 * @param consumer The consumer
	 */
	void withContext(FluxEnvelopePropagatedData data, Consumer<FluxEnvelopePropagatedData> consumer);
}
