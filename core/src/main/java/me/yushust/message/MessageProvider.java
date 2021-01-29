package me.yushust.message;

import me.yushust.message.config.ConfigurationContainer;
import me.yushust.message.config.ConfigurationModule;
import me.yushust.message.config.ConfigurationHandle;
import me.yushust.message.impl.MessageProviderImpl;
import me.yushust.message.source.MessageSource;
import me.yushust.message.track.TrackingContext;
import me.yushust.message.util.ReplacePack;
import me.yushust.message.util.StringList;

public interface MessageProvider {

  String format(Object entity, String text);

  String format(
    TrackingContext context,
    String path
  );

  StringList formatMany(
    TrackingContext context,
    String path
  );

  String format(
      Object entity,
      String path,
      ReplacePack replacements,
      Object[] jitEntities
  );

  StringList formatMany(
      Object entity,
      String path,
      ReplacePack replacements,
      Object[] jitEntities
  );

  String getMessage(String path);

  StringList getMessages(String path);

  String get(Object entity, String path, Object... jitEntities);

  String replacing(Object entity, String path, Object... replacements);

  StringList getMany(Object entity, String messagePath, Object... jitEntities);

  StringList replacingMany(Object entity, String messagePath, Object... replacements);

  Object resolve(Object entity);

  String getLanguage(Object entity);

  ConfigurationContainer getConfig();

  MessageSource getSource();

  static MessageProvider create(MessageSource source, ConfigurationModule... specifiers) {
    ConfigurationHandle configurationHandle = new ConfigurationHandle();
    for (ConfigurationModule specifier : specifiers) {
      specifier.configure(configurationHandle);
    }
    return new MessageProviderImpl(source, configurationHandle);
  }

}
