package me.yushust.message.config;

import me.yushust.message.format.MessageInterceptor;
import me.yushust.message.util.Validate;

public class ConfigurationHandle {

  private final ConfigurationContainer configurationContainer =
    new ConfigurationContainer();
  private String startDelimiter = "%";
  private String endDelimiter = "%";

  public ConfigurationHandle delimiting(String start, String end) {
    this.startDelimiter = start;
    this.endDelimiter = end;
    return this;
  }

  public ConfigurationHandle intercept(MessageInterceptor interceptor) {
    Validate.isNotNull(interceptor, "interceptor");
    configurationContainer.registerInterceptor(interceptor);
    return this;
  }

  public <E> SpecificConfigurationHandle<E> specify(Class<E> entityType) {
    Validate.isNotNull(entityType, "entityType");
    return new SpecificConfigurationHandle<>(
        configurationContainer,
        entityType
    );
  }

  public void install(ConfigurationModule... modules) {
    for (ConfigurationModule module : modules) {
      module.configure(this);
    }
  }

  public ConfigurationContainer getWiringContainer() {
    return configurationContainer;
  }

  public String getStartDelimiter() {
    return startDelimiter;
  }

  public String getEndDelimiter() {
    return endDelimiter;
  }

}
