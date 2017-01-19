/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.model.GeometryWrapper;

import java.io.IOException;

import static eu.europa.ec.fisheries.uvms.CommonConstants.DEFAULT_SRID;

public class GeometryDeserializer extends JsonDeserializer<Geometry> {

    @Override
    public Geometry deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {

        Geometry geom;

        try {

            String json = parser.readValueAsTree().toString();
            GeometryWrapper wrapper = GeometryMapper.INSTANCE.jsonToGeometryJson(json);
            geom = wrapper.getValue();

            if (geom.getSRID() == 0) {
                geom.setSRID(DEFAULT_SRID);
            }

        } catch (ParseException e) {
            throw new IOException(e);
        }

        return geom;
    }
}