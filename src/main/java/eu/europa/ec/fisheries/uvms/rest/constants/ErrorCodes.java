/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.rest.constants;

public interface ErrorCodes {
	String DELETE_FAILED = "error_delete_failed";
	String ENTRY_NOT_FOUND = "error_entry_not_found";
	String INPUT_NOT_SUPPORTED = "INPUT_NOT_SUPPORTED";
    String UPDATE_FAILED = "UPDATE_FAILED";
	String NOT_AUTHORIZED = "error_user_not_authorized";
	String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
	String INPUT_VALIDATION_FAILED = "INPUT_VALIDATION_FAILED";
	String USER_SCOPE_MISSING = "USER_SCOPE_MISSING";
	String CREATE_ENTITY_ERROR = "error_create_report";
}
