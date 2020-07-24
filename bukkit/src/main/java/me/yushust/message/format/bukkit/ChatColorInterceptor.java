package me.yushust.message.format.bukkit;

import me.yushust.message.core.intercept.MessageInterceptor;

public class ChatColorInterceptor implements MessageInterceptor {

    public static final String COLORS = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    public static final char TRANSLATING_CODE = '&';
    public static final char COLOR_CODE = '§';

    @Override
    public String intercept(String text) {
        char[] chars = text.toCharArray();
        for (int i = 0; i < text.length() - 1; i++) {
            char current = chars[i];
            char next = chars[i + 1];
            if (current == TRANSLATING_CODE && COLORS.indexOf(next) > -1) {
                chars[i] = COLOR_CODE;
                chars[i+1] = Character.toLowerCase(next);
            }
        }
        return new String(chars);
    }

}
