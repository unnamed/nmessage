package me.yushust.message.format.bukkit;

import me.yushust.message.core.intercept.InterceptContext;
import me.yushust.message.core.intercept.MessageInterceptor;
import org.jetbrains.annotations.NotNull;

public class ChatColorInterceptor<T> implements MessageInterceptor<T> {

    public static final String COLORS = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    public static final char COLOR_CODE = '\u00a7';
    private final char code;

    public ChatColorInterceptor(char code) {
        this.code = code;
    }

    @Override
    @NotNull
    public String replace(InterceptContext<T> context, String text) {
        char[] chars = text.toCharArray();
        for (int i = 0; i < text.length() - 1; i++) {
            char current = chars[i];
            char next = chars[i + 1];
            if (current == code && COLORS.indexOf(next) > -1) {
                chars[i] = COLOR_CODE;
                chars[i+1] = Character.toLowerCase(next);
            }
        }
        return new String(chars);
    }

}
