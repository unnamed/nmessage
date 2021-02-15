package me.yushust.message.config;

import me.yushust.message.format.MessageInterceptor;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Contract;

/**
 * Represents a configuration binder used
 * to ease the use of the {@link ConfigurationContainer}
 * methods
 */
public final class ConfigurationHandle {

  /** Internal class that holds all the configuration data */
  private final ConfigurationContainer configurationContainer =
    new ConfigurationContainer();

  /** The initial delimiter to recognise placeholders */
  private String startDelimiter = "%";

  /** The final delimiter to recognise placeholders */
  private String endDelimiter = "%";

  /**
   * Specifies the delimiters used by the placeholder
   * replacer to recognise placeholders
   * @param start The initial delimiter
   * @param end The end delimiter
   * @return A reference to {@code this}, for a fluent api
   */
  public ConfigurationHandle delimiting(String start, String end) {
    this.startDelimiter = Validate.isNotEmpty(start);
    this.endDelimiter = Validate.isNotEmpty(end);
    return this;
  }

  /**
   * Registers the given {@code interceptor} to the
   * interceptors list
   * @return A reference to {@code this}, for a fluent api
   */
  @Contract("null -> fail; _ -> this")
  public ConfigurationHandle addInterceptor(MessageInterceptor interceptor) {
    Validate.isNotNull(interceptor, "interceptor");
    configurationContainer.registerInterceptor(interceptor);
    return this;
  }

  /**
   * Registers the given {@code interceptor} to the
   * interceptors list
   * @return A reference to {@code this}, for a fluent api
   * @deprecated Invalid name, it doesn't intercept anything, it registers
   * an interceptors. So, use {@link ConfigurationHandle#addInterceptor}
   */
  @Deprecated
  public ConfigurationHandle intercept(MessageInterceptor interceptor) {
    return addInterceptor(interceptor);
  }

  /**
   * Creates a new specific configuration handle for
   * the given {@code entityType}. Used to avoid some
   * repeated code when registering many handlers
   * @param entityType The specifying entity type
   * @param <E> The entity type
   * @return The created specific configuration handle
   */
  @Contract("null -> fail; _ -> new")
  public <E> SpecificConfigurationHandle<E> specify(Class<E> entityType) {
    Validate.isNotNull(entityType, "entityType");
    return new SpecificConfigurationHandle<>(
        configurationContainer,
        entityType
    );
  }

  /**
   * Installs the specified {@code modules} by calling
   * {@link ConfigurationModule#configure} passing the
   * {@code this} reference as argument
   * @param modules The installed modules
   * @return Reference to {@code this}, for a fluent api
   */
  @Contract("null -> fail")
  public ConfigurationHandle install(ConfigurationModule... modules) {
    for (ConfigurationModule module : modules) {
      module.configure(this);
    }
    return this;
  }

  /**
   * Returns the internal configuration container
   * @deprecated The configuration handle and the
   * configuration container will be merged, there's
   * no necessity to separate them
   */
  @Deprecated
  public ConfigurationContainer getWiringContainer() {
    return configurationContainer;
  }

  /** Returns the initial delimiter for detecting placeholders */
  public String getStartDelimiter() {
    return startDelimiter;
  }

  /** Returns the final delimiter for detecting placeholders */
  public String getEndDelimiter() {
    return endDelimiter;
  }

}
