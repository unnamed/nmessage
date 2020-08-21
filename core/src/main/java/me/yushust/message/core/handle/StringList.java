package me.yushust.message.core.handle;

import java.util.AbstractList;
import java.util.List;

public final class StringList extends AbstractList<String> {

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


    // delegate methods to original string list
    @Override
    public String get(int index) {
        return stringList.get(index);
    }

    @Override
    public int size() {
        return stringList.size();
    }

    @Override
    public String set(int index, String element) {
        return stringList.set(index, element);
    }

    @Override
    public void add(int index, String element) {
        stringList.add(index, element);
    }

    @Override
    public String remove(int index) {
        return stringList.remove(index);
    }

}
