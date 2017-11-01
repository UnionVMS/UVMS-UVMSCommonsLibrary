/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.commons.rest.dto;

/**
 * Created by sanera on 01/12/2016.
 * * This class must be use in ALL search REST APIs in order to provide a standard JSON input.
 *
 * This is a sample JSON structure which gets deserialized as SearchRequestDto:
 * <pre>{
 *        "pagination": {
 *           "offset": 0,
 *           "pageSize": 100
 *       },
 *
 *      "sorting": {
 *           "sortBy": "code",
 *           "isReversed": true
 *       },
 *
 *       "criteria": {
 *           "acronym": "FAO_SPECIES",
 *           "filter": "*",
 *           "searchAttribute": "code"
 *       }
 *  }
 * </pre>
 */
import java.util.Map;


public class SearchRequestDto {

    private PaginationDto pagination;

    private SortingDto sorting;

    private Map<String, Object> criteria;

    public PaginationDto getPagination() {
        return pagination;
    }

    public void setPagination(PaginationDto pagination) {
        this.pagination = pagination;
    }

    public SortingDto getSorting() {
        return sorting;
    }

    public void setSorting(SortingDto sorting) {
        this.sorting = sorting;
    }

    public Map<String, Object> getCriteria() {
        return criteria;
    }

    public void setCriteria(Map<String, Object> criteria) {
        this.criteria = criteria;
    }


}
