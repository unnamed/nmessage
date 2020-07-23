package me.yushust.message.core.holder;

import java.util.Map;
import java.util.Optional;

public interface NodeFile {

    Optional<String> getString(String node);

    String getString(String node, String defaultValue);

    Map<String, Object> getValues();

}
