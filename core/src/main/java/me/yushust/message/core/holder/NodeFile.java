package me.yushust.message.core.holder;

import java.util.List;
import java.util.Optional;

public interface NodeFile {

    Optional<String> getString(String node);

    List<String> getStringList(String node);

}
