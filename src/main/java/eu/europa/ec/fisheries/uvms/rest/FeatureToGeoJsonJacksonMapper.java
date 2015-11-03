package eu.europa.ec.fisheries.uvms.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.lang.StringUtils;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class FeatureToGeoJsonJacksonMapper {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String TYPE = "type";
    private static final String FEATURE_COLLECTION = "FeatureCollection";
    private static final String FEATURES = "features";
    private static final String FEATURE = "Feature";
    private static final String GEOMETRY = "geometry";
    private static final String PROPERTIES = "properties";

    public ObjectNode convert(FeatureCollection featureCollection) throws IOException {
        return buildFeatureCollection(featureCollection);
    }

    public ObjectNode convert(SimpleFeature feature) throws IOException {
        return buildFeature(feature);
    }

    @SuppressWarnings("unchecked")
    private ObjectNode buildFeatureCollection(FeatureCollection featureCollection) throws IOException {
        List<ObjectNode> features = new LinkedList<>();
        FeatureIterator simpleFeatureIterator = featureCollection.features();
        while (simpleFeatureIterator.hasNext()) {
            Feature simpleFeature = simpleFeatureIterator.next();
            features.add(buildFeature((SimpleFeature) simpleFeature));
        }

        ObjectNode obj = mapper.createObjectNode();
        obj.put(TYPE, FEATURE_COLLECTION);
        obj.putArray(FEATURES).addAll(features);
        return obj;
    }

    @SuppressWarnings("unchecked")
    private ObjectNode buildFeature(SimpleFeature simpleFeature) throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put(TYPE, FEATURE);
        node.set(GEOMETRY, buildGeometry((Geometry) simpleFeature.getDefaultGeometry()));
        node.set(PROPERTIES, buildProperties(simpleFeature));
        return node;

    }

    @SuppressWarnings("unchecked")
    private ObjectNode buildProperties(SimpleFeature simpleFeature) {
        ObjectNode obj = mapper.createObjectNode();
        Collection<Property> properties = simpleFeature.getProperties();
        for (Property property : properties) {
            final Object value = property.getValue();

            if (!property.getName().getLocalPart().equals(GEOMETRY)){

                if (Double.class.equals(property.getType().getBinding())) {
                    obj.put(property.getName().toString(), property.getValue() == null ?
                            0D : Double.parseDouble(value.toString()));
                }
                else {
                    obj.put(property.getName().toString(), property.getValue() == null ?
                            StringUtils.EMPTY : value.toString());
                }
            }
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    private JsonNode buildGeometry(Geometry geometry) throws IOException {
        return mapper.readTree(new GeometryJSON().toString(geometry));
    }
}
