package me.yushust.message.core.handle;

import java.util.List;

public final class StringList {

    private final List<String> stringList;

    public StringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public StringList replace(String key, Object replacement) {
        if (key != null) {
            stringList.replaceAll(line ->
                    line.replace(key, String.valueOf(replacement))
            );
        }
        return this;
    }

    public String join(String delimiter) {
        return String.join(delimiter, stringList);
    }

    public List<String> getContents() {
        return stringList;
    }

}
