package eu.europa.ec.fisheries.uvms.commons.date;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.config.PropertyVisibilityStrategy;
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
                .setProperty(JsonbConfig.DATE_FORMAT, JsonbDateFormat.TIME_IN_MILLIS)
                .withPropertyVisibilityStrategy(new CustomPropertyVisibility());
    }

    @Override
    public Jsonb getContext(Class<?> type) {
        return JsonbBuilder.newBuilder()
                .withConfig(config)
                .build();
    }

    private class CustomPropertyVisibility implements PropertyVisibilityStrategy {

        @Override
        public boolean isVisible(Field field) {
            return true;
        }

        @Override
        public boolean isVisible(Method method) {
            return false;
        }
    }
}
