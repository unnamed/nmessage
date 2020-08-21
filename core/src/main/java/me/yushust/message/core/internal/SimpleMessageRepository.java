package me.yushust.message.core.internal;

import me.yushust.message.core.MessageRepository;
import me.yushust.message.core.ProvideStrategy;
import me.yushust.message.core.handle.StringList;
import me.yushust.message.core.holder.NodeFile;
import me.yushust.message.core.holder.allocate.NodeFileAllocator;
import me.yushust.message.core.intercept.DefaultInterceptManager;
import me.yushust.message.core.intercept.InterceptManager;
import me.yushust.message.core.localization.LanguageProvider;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class SimpleMessageRepository<T> implements MessageRepository<T> {

    private final Logger logger = Logger.getLogger(MessageRepository.class.getSimpleName());
    private final InterceptManager<T> interceptManager = new DefaultInterceptManager<>();
    private final NodeFileAllocator fileAllocator;

    private final ProvideStrategy provideStrategy;
    private final String fileFormat;
    private final String defaultLanguageFilename;

    private LanguageProvider<T> languageProvider;

    public SimpleMessageRepository(NodeFileAllocator fileAllocator, LanguageProvider<T> languageProvider,
                                   ProvideStrategy provideStrategy, String fileFormat, String defaultLanguage) {
        this.fileAllocator = fileAllocator;
        this.provideStrategy = provideStrategy;
        this.fileFormat = fileFormat;
        this.defaultLanguageFilename = getFilename(defaultLanguage);
        this.languageProvider = languageProvider;
    }

    @Override
    public String getMessage(T propertyHolder, String messagePath) {

        requireNonNull(propertyHolder);
        requireNonNull(messagePath);

        Optional<NodeFile> nodeFile = getNodeFileFor(propertyHolder);

        if (!nodeFile.isPresent()) {
            return getNotFoundValue(messagePath);
        }

        Optional<String> optionalMessage = nodeFile.get().getString(messagePath);

        if (!optionalMessage.isPresent()) {
            Optional<NodeFile> defaultLanguage = fileAllocator.find(defaultLanguageFilename);

            if (defaultLanguage.isPresent()) {
                optionalMessage = defaultLanguage.get().getString(messagePath);
            }
        }

        if (!optionalMessage.isPresent()) {
            return getNotFoundValue(messagePath);
        }

        return interceptManager.convert(propertyHolder, optionalMessage.get());
    }

    @Override
    public StringList getMessages(T propertyHolder, String messagePath) {

        Optional<NodeFile> nodeFile = getNodeFileFor(propertyHolder);

        if (!nodeFile.isPresent()) {
            return new StringList(
                    Collections.singletonList(getNotFoundValue(messagePath))
            );
        }

        List<String> messages = nodeFile.get().getStringList(messagePath);
        messages.replaceAll(line -> interceptManager.convert(propertyHolder, line));

        return new StringList(messages);

    }

    @Override
    public void useLanguageProvider(LanguageProvider<T> languageProvider) {
        requireNonNull(languageProvider);
        this.languageProvider = languageProvider;
    }

    @Override
    public InterceptManager<T> getInterceptionManager() {
        return this.interceptManager;
    }

    private Optional<NodeFile> getNodeFileFor(T propertyHolder) {
        String language = languageProvider.getLanguage(propertyHolder);
        Optional<NodeFile> nodeFile = Optional.empty();
        if (language != null) {
            nodeFile = fileAllocator.find(getFilename(language));
        }
        if (language == null || !nodeFile.isPresent()) {
            nodeFile = fileAllocator.find(defaultLanguageFilename);
            if (!nodeFile.isPresent()) {
                logger.warning("There's no a file with the default language!");
            }
        }
        return nodeFile;
    }

    private String getFilename(String language) {
        return fileFormat.replace("%lang%", language);
    }

    private String getNotFoundValue(String path) {
        if (provideStrategy == ProvideStrategy.RETURN_NULL) {
            return null;
        } else {
            return path;
        }
    }

}
