package me.yushust.message.format;

import me.yushust.message.track.TrackingContext;
import me.yushust.message.util.StringList;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Responsible of obtaining the value
 * for the given placeholders. The default
 * implementation searches for the {@link PlaceholderProvider}
 * linked to the specified {@code identifier}
 */
public interface PlaceholderValueProvider {

  /**
   * Gets the value for a placeholder with
   * the specified {@code identifier} and
   * its {@code parameters}.
   */
  @Nullable
  String getValue(
    TrackingContext context,
    String identifier,
    String parameters
  );

  /**
   * Gets the value for a placeholder with
   * the specified {@code identifier}, this
   * method doesn't receive {@code parameters}
   */
  @Nullable
  default String getValue(
    TrackingContext context,
    String identifier
  ) {
    return null;
  }

  /** Converts the given {@code object} to a {@link StringList} */
  @Nullable
  default StringList convertObjectToStringList(@Nullable String path, Object object) {
    if (object == null) {
      return new StringList(Collections.singletonList(path));
    } else if (object instanceof List) {
      @SuppressWarnings("unchecked")
      List<String> list = (List<String>) object;
      return new StringList(list);
    } else {
      return new StringList(
        Arrays.asList(object.toString().split("\n"))
      );
    }
  }

  /** Converts the given {@code object} to a string */
  @Nullable
  default String convertObjectToString(@Nullable String path, Object object) {
    if (object == null) {
      return path;
    } else if (object instanceof List) {
      @SuppressWarnings("unchecked")
      List<String> list = (List<String>) object;
      return String.join("\n", list);
    } else {
      return object.toString();
    }
  }

}

