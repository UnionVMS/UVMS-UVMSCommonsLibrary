/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.commons.message.impl;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import eu.europa.ec.fisheries.schema.config.types.v1.SettingType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.config.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.config.model.mapper.ModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.config.model.mapper.ModuleResponseMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kovian on 28/02/2018.
 *
 * <p>
 * <p>
 * This abstract class will serve as a base to be extended from any module that needs to get settings from
 * Config module (it retrieves settings that are : global and related to the module @see getModuleName();).
 * <p>
 * The following are a minimal set of methods that need to be overriten :
 *
 * <p>
 *
 *   abstract AbstractConsumer getConsumer() : The consumer that will consume the response messages in the related modules Queue.
 *                                             An implementation of eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConsumer
 *
 *   abstract AbstractProducer getProducer() : The producer which will send the request messages to >>Config<< module.
 *                                             An implementation of eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractProducer
 *
 *   abstract String getModuleName()         : The module name which will be used as a parameter to get the settings.
 *                                             This will be used to fetch from config all this module configurations plus all the global ones.
 *
 *   abstract String getDestinationName()    : The name of this modules internal Queue. Must take it from MessageConstants class residing inside
 *                                             commons-message artifact.
 * <p>
 * <p>
 *  Example of implementation in Rules module :
 * <code>
 *          @Singleton
 *          @Startup
 *          @Slf4j
 *          public class RulesConfigurationCache extends AbstractConfigSettingsBean {
 *
 *          @EJB
 *          private RulesResponseConsumerBean consumer;
 *
 *          @EJB
 *          private RulesProducerBean producer;
 *
 *          @Override
 *          protected RulesResponseConsumerBean getConsumer() {
 *              return consumer;
 *          }
 *
 *          @Override
 *          protected String getModuleName() {
 *              return "rules";
 *         }
 *
 *          @Override
 *          public String getDestinationName() {
 *              return MessageConstants.QUEUE_RULES;
 *          }
 *
 *              @Override
 *          protected AbstractProducer getProducer() {
 *              return producer;
 *          }
 *     }
 * </code>
 *
 *  By just injecting RulesConfigurationCache the settings of your module ( getModuleName() ) can be retrieved by calling :
 *
 *      1. getSingleConfig("key_of_the_setting_of_this_module_or_global_one");
 *      2. getAllSettingsForModule();
 *
 *  Note : If config module is not deployed this bean cannot fetch the configs and it will "fail fast" in 5s.
 *         The deployment won't fail though, if the Implementing bean is a Singleton Startup bean...
 */
public abstract class AbstractConfigSettingsBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConsumer.class);

    private LoadingCache<String, Map<String, String>> cache;

    private Queue configQueue;

    /**
     * Initializes the Config Settings cache.
     * To be called in the extending class in a @PostConstruct block to load the settings for this module.
     */
    public AbstractConfigSettingsBean() {
        configQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_CONFIG);
        if (cache == null) {
            LOGGER.info("[START] Loading settings for module : [" + getModuleName() + "].");
            initCacheObject();
            LOGGER.info("[END] Finished loading settings for module : [" + getModuleName() + "].");
        }
    }

    private void initCacheObject() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(new CacheLoader<String, Map<String, String>>() {
                           @Override
                           public Map<String, String> load(String moduleName) {
                               return getAllModuleConfigurations(moduleName);
                           }
                       }
                );
    }


    /**
     * Get a single Setting related to this module from the cache.
     *
     * @param configKey
     * @return String (single Setting)
     */
    public String getSingleConfig(String configKey) {
        if (configKey == null) {
            throw new IllegalArgumentException("[ERROR] configKey cannot be null!");
        }
        Map<String, String> moduleConfigsMap = getAllSettingsForModule();
        return moduleConfigsMap.get(configKey);
    }

    /**
     * Calls Config module and gets all the settings related to the module with name = moduleName.
     *
     * @param moduleName
     * @return Map<String                               ,                                                               String> the object (Settings map) to cache.
     */
    private Map<String, String> getAllModuleConfigurations(String moduleName) {
        Map<String, String> settingsMap = new HashMap<>();
        if (StringUtils.isNotEmpty(moduleName)) {
            try {
                LOGGER.info("[INFO] Going to fetch settings for module [ " + moduleName + " ]");
                List<SettingType> settingTypeList = getSettingTypes(moduleName);
                if (CollectionUtils.isNotEmpty(settingTypeList)) {
                    LOGGER.info("[INFO] Got [ " + settingTypeList.size() + " ] settings for module [ " + moduleName + " ]");
                    for (SettingType setting : settingTypeList) {
                        settingsMap.put(setting.getKey(), setting.getValue());
                    }
                    LOGGER.info("[INFO] ConfigSettingsBean has just finished refreshing the " + getModuleName() + " Configuration cache.");
                } else {
                    LOGGER.warn("[WARN] No settings found for module : " + moduleName);
                }
            } catch (MessageException e) {
                LOGGER.error("[ERROR] Error while trying to fetch settings for module [" + getModuleName() + "]. {}", e);
            }
        } else {
            LOGGER.error("[ERROR] Module name cannot be null when fetching settings for it!");
        }
        return settingsMap;
    }

    /**
     * Returns all the settings related to this module - getModuleName() -.
     *
     * @return
     */
    public Map<String, String> getAllSettingsForModule() {
        Map<String, String> result = null;
        String moduleName = getModuleName();
        if (moduleName != null) {
            if(cache == null){
                initCacheObject();
            }
            result = cache.getUnchecked(moduleName);
            if (MapUtils.isEmpty(result)) {
                LOGGER.info("[INFO] Cache was empty.. Going to refresh..");
                cache.refresh(getModuleName());
            }
        }
        return result != null ? result : new HashMap<String, String>();
    }

    /**
     * Calls the Config module to get all the settings for the related Module with name = moduleName.
     * Along with the module setting will also come the global settings, as per Config implementation of this!
     *
     * @param moduleName
     * @return List<SettingType>, the setting of the module we requested
     * @throws MessageException
     * @throws ModelMapperException
     * @throws JMSException
     */
    private List<SettingType> getSettingTypes(String moduleName) throws MessageException {
        try {
            String jmsMessageID = getProducer().sendMessageToSpecificQueue(ModuleRequestMapper.toPullSettingsRequest(moduleName), getConfigQueue(), getConsumer().getDestination());
            TextMessage message = getConsumer().getMessage(jmsMessageID, TextMessage.class, 10000L);
            return ModuleResponseMapper.getSettingsFromPullSettingsResponse(message);
        } catch (JMSException | ModelMapperException e) {
            throw new MessageException("[ERROR] Error while trying to fetch settings from CONFIG module. Is this module deployed?", e);
        }
    }

    protected abstract AbstractConsumer getConsumer();

    protected abstract AbstractProducer getProducer();

    protected abstract String getModuleName();

    private Destination getConfigQueue() {
        return configQueue;
    }

}
