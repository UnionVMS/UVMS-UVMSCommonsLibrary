package eu.europa.ec.fisheries.uvms.rest;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geojson.geom.GeometryJSON;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * //TODO create temp
 */
public class FeatureToGeoJson {

    public String convert(SimpleFeatureCollection featureCollection) {
        JSONObject jsonFeatureCollection = buildFeatureCollection(featureCollection);
        return jsonFeatureCollection.toJSONString();
    }

    @SuppressWarnings("unchecked")
    private JSONObject buildFeatureCollection(SimpleFeatureCollection featureCollection) {
        List<JSONObject> features = new LinkedList<>();
        SimpleFeatureIterator simpleFeatureIterator = featureCollection.features();
        while (simpleFeatureIterator.hasNext()) {
            SimpleFeature simpleFeature = simpleFeatureIterator.next();
            features.add(buildFeature(simpleFeature));
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
        JSONObject jsonObj = (JSONObject) obj;
        return jsonObj;
    }
}
