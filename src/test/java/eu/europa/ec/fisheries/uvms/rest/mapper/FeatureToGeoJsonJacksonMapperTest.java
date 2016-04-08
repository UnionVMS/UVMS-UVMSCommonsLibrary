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
