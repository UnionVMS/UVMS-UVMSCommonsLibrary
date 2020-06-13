/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.commons.message.context;

/**
 * A holder of potentially nested {@link FluxEnvelopePropagatedData} objects.
 * <p>
 * The backing store of the data for this object should be thread-local.
 */
public interface FluxEnvelopeHolder {
	/**
	 * Get the current {@code FluxEnvelopePropagatedData}, or {@code null} if none, without affecting the stack.
	 *
	 * @return The current {@code FluxEnvelopePropagatedData}, or {@code null} if none
	 */
	FluxEnvelopePropagatedData get();
}
