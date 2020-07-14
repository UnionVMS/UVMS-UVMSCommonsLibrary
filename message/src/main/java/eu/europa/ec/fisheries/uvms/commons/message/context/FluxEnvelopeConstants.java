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
 * Constants related to thFLUX envelope headers, to be used as keys in the headers of the messages
 * that the Flux FMC modules use to communicate with each other.
 */
public final class FluxEnvelopeConstants {
	private FluxEnvelopeConstants() {
		// DISABLE INSTANTIATION
	}

	/**
	 * A prefix to namespace the FLUX-related headers; each transport will combine the prefic with the actual name
	 * with its own separator character (or string).
	 * <p>
	 * E.g. it is required for JMS headers to be valid Java identifiers, so the separator character would be
	 * the underscore, resulting in header names like {@code "FLUX_DF"}.
	 */
	public static final String PREFIX = "FLUX";

	/**
	 * The message GUID header name.
	 */
	public static final String MESSAGE_GUID = "GUID";

	/**
	 * The dataflow header name.
	 */
	public static final String DF = "DF";

	/**
	 * The sender or receiver header name.
	 */
	public static final String FR = "FR";
}
