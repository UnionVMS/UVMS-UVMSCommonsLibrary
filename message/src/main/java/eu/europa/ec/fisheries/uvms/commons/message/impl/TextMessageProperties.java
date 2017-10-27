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
package eu.europa.ec.fisheries.uvms.commons.message.impl;

/**
 * Created by sanera on 13/04/2017.
 */
public class TextMessageProperties {
	private String adVal;
	private String dfVal;
	private String arVal;
	private String frVal;
	private String businessUUId;
	private String creationDate;
	private String vbVal;
	private String ctVal;
	private String toVal;

	public TextMessageProperties() {

	}

	public TextMessageProperties(final String adVal, final String dfVal, final String arVal, final String frVal,
			final String businessUUId, final String creationDate) {
		this.adVal = adVal;
		this.dfVal = dfVal;
		this.arVal = arVal;
		this.frVal = frVal;
		this.businessUUId = businessUUId;
		this.creationDate = creationDate;
	}

	public String getAdVal() {
		return adVal;
	}

	public void setAdVal(final String adVal) {
		this.adVal = adVal;
	}

	public String getDfVal() {
		return dfVal;
	}

	public void setDfVal(final String dfVal) {
		this.dfVal = dfVal;
	}

	public String getArVal() {
		return arVal;
	}

	public void setArVal(final String arVal) {
		this.arVal = arVal;
	}

	public String getBusinessUUId() {
		return businessUUId;
	}

	public void setBusinessUUId(final String businessUUId) {
		this.businessUUId = businessUUId;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(final String creationDate) {
		this.creationDate = creationDate;
	}

	public String getFrVal() {
		return frVal;
	}

	public void setFrVal(final String frVal) {
		this.frVal = frVal;
	}

	@Override
	public String toString() {
		return "TextMessageProperties{" + "adVal='" + adVal + '\'' + ", dfVal='" + dfVal + '\'' + ", arVal='" + arVal
				+ '\'' + ", frVal='" + frVal + '\'' + ", businessUUId='" + businessUUId + '\'' + ", creationDate='"
				+ creationDate + '\'' + '}';
	}

	public String getVbVal() {
		return vbVal;
	}

	public void setVbVal(final String vbVal) {
		this.vbVal = vbVal;
	}

	public String getCtVal() {
		return ctVal;
	}

	public void setCtVal(final String ctVal) {
		this.ctVal = ctVal;
	}

	public String getToVal() {
		return toVal;
	}

	public void setToVal(final String toVal) {
		this.toVal = toVal;
	}
}
