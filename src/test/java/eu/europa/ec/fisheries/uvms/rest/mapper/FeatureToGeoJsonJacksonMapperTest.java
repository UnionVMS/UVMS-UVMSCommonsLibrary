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
package eu.europa.ec.fisheries.uvms.rest.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonJacksonMapper;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import java.io.IOException;
import java.util.ArrayList;

import static java.util.Arrays.*;
import static org.junit.Assert.assertEquals;

public class FeatureToGeoJsonJacksonMapperTest {

    private FeatureToGeoJsonJacksonMapper mapper = new FeatureToGeoJsonJacksonMapper();

    @Test
    public void testConvert() throws IOException {

        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.setName("test");
        sb.add("geometry", Point.class);
        sb.add("arrayType", ArrayList.class);
        sb.add("stringType", String.class);
        sb.add("doubleType", Double.class);
        SimpleFeatureType simpleFeatureType = sb.buildFeatureType();

        SimpleFeatureBuilder simpleFeatureBuilder = new SimpleFeatureBuilder(simpleFeatureType);
        simpleFeatureBuilder.set("arrayType", asList("R", "FDG"));
        ObjectNode convert = mapper.convert(simpleFeatureBuilder.buildFeature(null));

        ObjectMapper objectMapper = new ObjectMapper();
        assertEquals("{\"type\":\"Feature\",\"geometry\":null,\"properties\":{\"arrayType\":[\"R\",\"FDG\"],\"stringType\":\"\",\"doubleType\":0.0}}", objectMapper.writeValueAsString(convert));

    }
}