package me.yushust.message.model;

import me.yushust.message.util.ReplacePack;

public interface Text {

    static Text newEmpty() {
        return new DefaultText();
    }

    static Text of(String path) {
        return newEmpty().path(path);
    }

    static Text of(Text text) {
        return of(text.getPath())
                .mode(text.getMode())
                .replace(text.getReplacements())
                .withEntities(text.getEntities());
    }

    Text path(String path);

    Text mode(String mode);

    default Text replace(Object... objects) {
        return replace(ReplacePack.make(objects));
    }

    Text replace(ReplacePack pack);

    default Text addReplace(Object... objects) {
        return addReplace(ReplacePack.make(objects));
    }

    Text addReplace(ReplacePack pack);

    Text withEntities(Object... entities);

    Text addEntities(Object... entities);

    String getPath();

    String getMode();

    ReplacePack getReplacements();

    Object[] getEntities();
}
