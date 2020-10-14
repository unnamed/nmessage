package me.yushust.message.internal;

import me.yushust.message.MessageRepository;
import me.yushust.message.ProvideStrategy;
import me.yushust.message.StringList;
import me.yushust.message.holder.NodeFile;
import me.yushust.message.holder.allocate.NodeFileAllocator;
import me.yushust.message.holder.allocate.SimpleFileAllocator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

final class MessageRepositoryImpl implements MessageRepository {

  private static final Logger LOGGER = Logger.getLogger(MessageRepository.class.getSimpleName());
  private final NodeFileAllocator fileAllocator;

  private final ProvideStrategy provideStrategy;
  private final String fileFormat;
  private final String defaultLanguageFilename;

  MessageRepositoryImpl(MessageRepositoryBuilder builder) {
    this.fileAllocator = new SimpleFileAllocator(builder.nodeFileLoader, builder.loadSource);
    this.provideStrategy = builder.provideStrategy;
    this.fileFormat = builder.fileFormat;
    this.defaultLanguageFilename = getFilename(builder.defaultLanguage);
  }

  @Override
  public String getMessage(@Nullable String language, String messagePath) {

    requireNonNull(messagePath);

    Optional<NodeFile> nodeFile = getNodeFileFor(language);

    if (!nodeFile.isPresent()) {
      return provideStrategy.getNotFoundMessage(language, messagePath);
    }

    Optional<String> optionalMessage = nodeFile.get().getString(messagePath);

    if (!optionalMessage.isPresent()) {
      Optional<NodeFile> defaultLanguage = fileAllocator.find(defaultLanguageFilename);
      if (defaultLanguage.isPresent()) {
        optionalMessage = defaultLanguage.get().getString(messagePath);
      }
    }

    return optionalMessage.orElseGet(
        () -> provideStrategy.getNotFoundMessage(language, messagePath)
    );
  }

  @Override
  public StringList getMessages(@Nullable String language, String messagePath) {

    Optional<NodeFile> nodeFile = getNodeFileFor(language);

    return nodeFile.map(file -> new StringList(file.getStringList(messagePath)))
        .orElseGet(
            () -> StringList.singleton(provideStrategy.getNotFoundMessage(language, messagePath))
        );
  }

  private Optional<NodeFile> getNodeFileFor(@Nullable String language) {
    Optional<NodeFile> nodeFile = Optional.empty();
    if (language != null) {
      nodeFile = fileAllocator.find(getFilename(language));
    }
    if (language == null || !nodeFile.isPresent()) {
      nodeFile = fileAllocator.find(defaultLanguageFilename);
      if (!nodeFile.isPresent()) {
        LOGGER.warning("There's no a file with the default language!");
      }
    }
    return nodeFile;
  }

  private String getFilename(String language) {
    return fileFormat.replace("%lang%", language);
  }

}
