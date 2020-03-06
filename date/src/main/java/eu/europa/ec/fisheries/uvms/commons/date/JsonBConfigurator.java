package eu.europa.ec.fisheries.uvms.commons.date;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonBConfigurator implements ContextResolver<Jsonb> {

    protected JsonbConfig config;

    public JsonBConfigurator() {
        config = new JsonbConfig()
                .withAdapters(
                        new JsonBInstantAdapter(),
                        new JsonBDurationAdapter(),
                        new JsonBDateAdapter(),
                        new JsonBXmlGregorianCalendarAdapter())
                .setProperty(JsonbConfig.DATE_FORMAT, JsonbDateFormat.TIME_IN_MILLIS);
    }

    @Override
    public Jsonb getContext(Class<?> type) {
        return JsonbBuilder.newBuilder()
                .withConfig(config)
                .build();
    }
}
