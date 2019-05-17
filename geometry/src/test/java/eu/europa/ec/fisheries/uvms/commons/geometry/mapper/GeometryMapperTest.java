package eu.europa.ec.fisheries.uvms.commons.geometry.mapper;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import eu.europa.ec.fisheries.uvms.commons.geometry.model.FeatureCollectionWrapper;
import eu.europa.ec.fisheries.uvms.commons.geometry.model.GeometryWrapper;
import eu.europa.ec.fisheries.uvms.commons.geometry.model.SimpleFeatureWrapper;
import lombok.SneakyThrows;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.StringWriter;
import java.util.ArrayList;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class GeometryMapperTest {

    @Test
    @SneakyThrows
    public void testJsonToGeometryJson(){

        String json = "{\"type\":\"Point\"," +
                       "\"coordinates\":[100.1,0.1]" +
                      "}";

        GeometryWrapper geometryWrapper = GeometryMapper.INSTANCE.jsonToGeometryJson(json);

        Geometry value = geometryWrapper.getValue();

        assertTrue(value instanceof Point);
    }

    @Test
    @SneakyThrows
    public void testGeoJsonSimpleToSimpleFeature(){

        String feature = "{\n" +
                "    \"type\": \"Feature\",\n" +
                "    \"geometry\": {\n" +
                "        \"type\": \"Polygon\",\n" +
                "        \"coordinates\": [\n" +
                "            [\n" +
                "                [\n" +
                "                    100,\n" +
                "                    0\n" +
                "                ],\n" +
                "                [\n" +
                "                    101,\n" +
                "                    0\n" +
                "                ],\n" +
                "                [\n" +
                "                    101,\n" +
                "                    1\n" +
                "                ],\n" +
                "                [\n" +
                "                    100,\n" +
                "                    1\n" +
                "                ],\n" +
                "                [\n" +
                "                    100,\n" +
                "                    0\n" +
                "                ]\n" +
                "            ]\n" +
                "        ]\n" +
                "    },\n" +
                "    \"properties\": {\n" +
                "        \"prop0\": \"value0\",\n" +
                "        \"prop1\": {\n" +
                "            \"this\": \"that\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        SimpleFeatureWrapper simpleFeatureWrapper = GeometryMapper.INSTANCE.geoJsonFeatureToSimpleFeature(feature);

        assertEquals("value0", simpleFeatureWrapper.getValue().getProperty("prop0").getValue());
        assertEquals(null, simpleFeatureWrapper.getValue().getProperty("prop1").getValue());
        assertEquals(Polygon.class, simpleFeatureWrapper.getValue().getDefaultGeometry().getClass());

    }

    @Test
    @SneakyThrows
    public void testGeoJsonFeatureCollectionToFeatureCollection(){

        String featureCollection = "{ \"type\": \"FeatureCollection\",\n" +
                "    \"features\": [\n" +
                "      { \"type\": \"Feature\",\n" +
                "        \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]},\n" +
                "        \"properties\": {\"prop0\": \"value0\"}\n" +
                "        },\n" +
                "      { \"type\": \"Feature\",\n" +
                "        \"geometry\": {\n" +
                "          \"type\": \"LineString\",\n" +
                "          \"coordinates\": [\n" +
                "            [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]\n" +
                "            ]\n" +
                "          },\n" +
                "        \"properties\": {\n" +
                "          \"prop0\": \"value0\",\n" +
                "          }\n" +
                "        },\n" +
                "      { \"type\": \"Feature\",\n" +
                "         \"geometry\": {\n" +
                "           \"type\": \"Polygon\",\n" +
                "           \"coordinates\": [\n" +
                "             [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0],\n" +
                "               [100.0, 1.0], [100.0, 0.0] ]\n" +
                "             ]\n" +
                "         },\n" +
                "         \"properties\": {\n" +
                "           \"prop0\": \"value0\",\n" +
                "           }\n" +
                "         }\n" +
                "       ]\n" +
                "     }";


        FeatureCollectionWrapper wrapper = GeometryMapper.INSTANCE.geoJsonToFeatureCollection(featureCollection);

        assertEquals(100.5, wrapper.getValue().getBounds().getMinX(), 0.1);
        assertEquals(103.5, wrapper.getValue().getBounds().getMaxX(), 0.1);
        assertEquals(0.5, wrapper.getValue().getBounds().getMaxY(), 0.1);
        assertEquals(0.5, wrapper.getValue().getBounds().getMinY(), 0.1);

    }

    @Test
    @SneakyThrows
    public void testSimpleFeatureToGeoJson(){

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

        SimpleFeature simpleFeature = simpleFeatureBuilder.buildFeature(null);

        StringWriter sw = new StringWriter();
        GeometryMapper.INSTANCE.simpleFeatureToGeoJson(simpleFeature, sw);

        assertTrue(sw.toString().contains("{\"type\":\"Feature\",\"properties\":{\"arrayType\":\"[R, FDG]\"},"));

    }
}
