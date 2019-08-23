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

package eu.europa.ec.fisheries.uvms.commons.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class RectangleCoordinates {

	@Column(name = "south")
	private double south;

	@Column(name = "west")
	private double west;

	@Column(name = "north")
	private double north;

	@Column(name = "east")
	private double east;

	public double getSouth() {
		return south;
	}

	public void setSouth(double south) {
		this.south = south;
	}

	public double getWest() {
		return west;
	}

	public void setWest(double west) {
		this.west = west;
	}

	public double getNorth() {
		return north;
	}

	public void setNorth(double north) {
		this.north = north;
	}

	public double getEast() {
		return east;
	}

	public void setEast(double east) {
		this.east = east;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RectangleCoordinates that = (RectangleCoordinates) o;
		return Double.compare(that.south, south) == 0 &&
				Double.compare(that.west, west) == 0 &&
				Double.compare(that.north, north) == 0 &&
				Double.compare(that.east, east) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(south, west, north, east);
	}

	@Override
	public String toString() {
		return "RectangleCoordinates{" +
				"south=" + south +
				", west=" + west +
				", north=" + north +
				", east=" + east +
				'}';
	}
}