package eu.europa.ec.fisheries.uvms.rest;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.geom.GeometryJSON;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class FeatureToGeoJsonMapper {

    public String convert(FeatureCollection featureCollection) {
        JSONObject jsonFeatureCollection = buildFeatureCollection(featureCollection);
        return jsonFeatureCollection.toJSONString();
    }

    public String convert(SimpleFeature feature) {
        JSONObject jsonFeatureCollection = buildFeature(feature);
        return jsonFeatureCollection.toJSONString();
    }

    @SuppressWarnings("unchecked")
    private JSONObject buildFeatureCollection(FeatureCollection featureCollection) {
        List<JSONObject> features = new LinkedList<>();
        FeatureIterator simpleFeatureIterator = featureCollection.features();
        while (simpleFeatureIterator.hasNext()) {
            Feature simpleFeature = simpleFeatureIterator.next();
            features.add(buildFeature((SimpleFeature) simpleFeature));
        }

        JSONObject obj = new JSONObject();
        obj.put("type", "FeatureCollection");
        obj.put("features", features);
        return obj;
    }

    @SuppressWarnings("unchecked")
    private JSONObject buildFeature(SimpleFeature simpleFeature) {
        JSONObject obj = new JSONObject();
        obj.put("type", "Feature");
        obj.put("geometry", buildGeometry((Geometry) simpleFeature.getDefaultGeometry()));
        obj.put("properties", buildProperties(simpleFeature));
        return obj;

    }

    @SuppressWarnings("unchecked")
    private JSONObject buildProperties(SimpleFeature simpleFeature) {
        JSONObject obj = new JSONObject();
        Collection<Property> properties = simpleFeature.getProperties();
        for (Property property : properties) {
            if (!property.getName().getLocalPart().equals("geometry")){
                obj.put(property.getName().toString(), property.getValue() == null ? "" : property.getValue());
            }
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    private JSONObject buildGeometry(Geometry geometry) {
        GeometryJSON gjson = new GeometryJSON();
        Object obj = JSONValue.parse(gjson.toString(geometry));
        return (JSONObject) obj;
    }
}
