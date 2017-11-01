/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.commons.geometry.model;

import org.opengis.feature.simple.SimpleFeature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * This class is required by Mapstruct, which requires target attribute.
 * See {@link GeometryWrapper} for more details.
 *
 * Eventual improvement is to generalize the Wrappers or even to remove them.
 */
public class SimpleFeatureWrapper {

    private SimpleFeature value;

    public SimpleFeature getValue() {
        return value;
    }

    public void setValue(SimpleFeature value) {
        this.value = value;
    }
}
