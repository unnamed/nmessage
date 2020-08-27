package me.yushust.message.test;

import me.yushust.message.core.intercept.InterceptContext;
import me.yushust.message.core.placeholder.PlaceholderProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Stupid, but it's for testing
 */
public class HashCodePlaceholderProvider implements PlaceholderProvider<ConsoleEntity> {

    @Override
    public String[] getPlaceholders() {
        return new String[] {"hashcode"};
    }

    @Override
    public @Nullable String replace(InterceptContext<ConsoleEntity> context, String placeholder) {

        return String.valueOf(
                context.getEntity().hashCode()
        );
    }

}
