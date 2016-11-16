/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by georgige on 19/09/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginatedResponse<T>  {

    @JsonProperty("resultList")
    private List<T> resultList;

    @JsonProperty("totalItemsCount")
    private int totalItemsCount;

    public PaginatedResponse(List<T> resultList, int totalItemsCount) {
        this.setResultList(resultList);
        this.setTotalItemsCount(totalItemsCount);
    }

    public PaginatedResponse(List<T> resultList) {
        this.setResultList(resultList);
        this.setTotalItemsCount(resultList.size());
    }

    @JsonProperty("resultList")
    public List<T> getResultList() {
        return resultList;
    }

    @JsonProperty("resultList")
    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }

    @JsonProperty("totalItemsCount")
    public int getTotalItemsCount() {
        return totalItemsCount;
    }

    @JsonProperty("totalItemsCount")
    public void setTotalItemsCount(int totalItemsCount) {
        this.totalItemsCount = totalItemsCount;
    }
}
