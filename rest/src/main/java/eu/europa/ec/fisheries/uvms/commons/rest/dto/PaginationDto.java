/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.commons.rest.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class PaginationDto {

    @NotNull
    private Integer offset;

    @NotNull
    private Integer pageSize;

    public PaginationDto() {
    }

    public PaginationDto(@NotNull Integer offset, @NotNull Integer pageSize) {
        this.offset = offset;
        this.pageSize = pageSize;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaginationDto that = (PaginationDto) o;
        return offset.equals(that.offset) &&
                pageSize.equals(that.pageSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, pageSize);
    }

    @Override
    public String toString() {
        return "PaginationDto{" +
                "offset=" + offset +
                ", pageSize=" + pageSize +
                '}';
    }
}
