package eu.europa.ec.fisheries.uvms.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author kovian
 *
 */
@SuppressWarnings("serial")
@Embeddable
@EqualsAndHashCode
@ToString
public class RectangleCoordinates implements Serializable {

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

}
