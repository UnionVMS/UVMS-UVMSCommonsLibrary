package eu.europa.ec.fisheries.uvms.commons.date;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonBConfigurator implements ContextResolver<Jsonb> {

    Jsonb jsonb;
    public JsonBConfigurator(){
        JsonbConfig config = new JsonbConfig()
                .withAdapters(new JsonBInstantAdapter(), new JsonBDurationAdapter())
                .setProperty(JsonbConfig.DATE_FORMAT, JsonbDateFormat.TIME_IN_MILLIS);

        jsonb = JsonbBuilder.newBuilder().
                withConfig(config).
                build();
    }

    @Override
    public Jsonb getContext(Class type) {
        return jsonb;
    }
}
