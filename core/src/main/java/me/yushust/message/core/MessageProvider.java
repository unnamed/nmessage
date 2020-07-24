package me.yushust.message.core;

import me.yushust.message.core.handle.StringList;
import me.yushust.message.core.intercept.ReplacingMessageInterceptor;

public interface MessageProvider<T> {

    String DEFAULT_LANGUAGE = "def";

    String getMessage(T propertyHolder, String messagePath);

    StringList getMessages(T propertyHolder, String messagePath);

    LanguageProvider<T> getLanguageProvider();

    void useLanguageProvider(LanguageProvider<T> languageProvider);

    ReplacingMessageInterceptor<T> getInterceptor();

}
