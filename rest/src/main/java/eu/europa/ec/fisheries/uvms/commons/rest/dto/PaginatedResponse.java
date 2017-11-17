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

package eu.europa.ec.fisheries.uvms.commons.rest.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by georgige on 19/09/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginatedResponse<T>  {

    private List<T> resultList;
    private int totalItemsCount;
    private int code;
    private String msg;

    public List<T> getResultList() {
        return resultList;
    }

    public PaginatedResponse<T> setResultList(List<T> resultList) {
        this.resultList = resultList;
        return this;
    }

    public int getTotalItemsCount() {
        return totalItemsCount;
    }

    public PaginatedResponse<T> setTotalItemsCount(int totalItemsCount) {
        this.totalItemsCount = totalItemsCount;
        return this;
    }

    public int getCode() {
        return code;
    }

    public PaginatedResponse<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public PaginatedResponse<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
