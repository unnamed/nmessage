package me.yushust.message.config;

/**
 * Responsible of configuring
 * {@link ConfigurationHandle}
 *
 * @see ConfigurationHandle
 */
public interface ConfigurationModule {

    /**
     * Configures the given {@code config} with
     * the module configurations
     */
    void configure(ConfigurationHandle config);

}
