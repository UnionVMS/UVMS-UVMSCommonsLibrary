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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Embeddable
@Indexed
public class DateRange implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String END_DATE = "end_date";
    public static final String START_DATE = "start_date";

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = START_DATE)
    @JsonSerialize(using = DateUiFormatSerializer.class)
    @Field(name = "startDate")
    @DateBridge(resolution= Resolution.SECOND)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = END_DATE)
    @Field(name = "endDate")
    @JsonSerialize(using = DateUiFormatSerializer.class)
    @DateBridge(resolution= Resolution.SECOND)
    private Date endDate;

    public DateRange() {
    }

    public DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        Date startDate = null;
        if (this.startDate != null) {
            startDate = new Date(this.startDate.getTime());
        }
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (startDate != null) {
            this.startDate = new Date(startDate.getTime());
        }
        this.startDate = startDate;
    }

    public Date getEndDate() {
        Date endDate = null;
        if (this.endDate != null) {
            endDate = new Date(this.endDate.getTime());
        }
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate != null) {
            this.endDate = new Date(endDate.getTime());
        }
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateRange dateRange = (DateRange) o;
        return startDate.equals(dateRange.startDate) &&
                endDate.equals(dateRange.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

    @Override
    public String toString() {
        return "DateRange{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
