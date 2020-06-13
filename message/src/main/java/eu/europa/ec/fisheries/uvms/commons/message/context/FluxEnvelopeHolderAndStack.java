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
import java.util.ArrayDeque;
import java.util.function.Consumer;

/**
 * {@code ArrayDeque} based implementation of {@link FluxEnvelopeHolder}.
 * <p>
 * Adapts the {@code ArrayDeque} to support null values.
 */
@ApplicationScoped
public class FluxEnvelopeHolderAndStack implements FluxEnvelopeHolder, FluxEnvelopeStack {

	private static final ThreadLocal<ArrayDeque<FluxEnvelopePropagatedData>> STACK_HOLDER = ThreadLocal.withInitial(ArrayDeque::new);

	private static final FluxEnvelopePropagatedData NULL_ELEMENT = new FluxEnvelopePropagatedData(null, null, null) {
		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			return obj == this;
		}
	};

	@Override
	public FluxEnvelopePropagatedData get() {
		ArrayDeque<FluxEnvelopePropagatedData> stack = STACK_HOLDER.get();
		return checkNullElement(stack.peek());
	}

	@Override
	public void push(FluxEnvelopePropagatedData data) {
		ArrayDeque<FluxEnvelopePropagatedData> stack = STACK_HOLDER.get();
		stack.push(data == null ? NULL_ELEMENT : data);
	}

	@Override
	public FluxEnvelopePropagatedData pop() {
		ArrayDeque<FluxEnvelopePropagatedData> stack = STACK_HOLDER.get();
		return checkNullElement(stack.poll());
	}

	@Override
	public void withContext(FluxEnvelopePropagatedData data, Consumer<FluxEnvelopePropagatedData> consumer) {
		try {
			push(data);
			consumer.accept(data);
		} finally {
			pop();
		}
	}

	private FluxEnvelopePropagatedData checkNullElement(FluxEnvelopePropagatedData data) {
		return data == NULL_ELEMENT ? null : data;
	}
}
