package me.yushust.message.model;

import me.yushust.message.util.ReplacePack;

import java.util.Arrays;

public class DefaultText implements Text, Cloneable {
    protected static final Object[] EMPTY_ENTITY = new Object[0];

    protected String path;
    protected String mode;
    protected ReplacePack replacePack = ReplacePack.EMPTY;
    protected Object[] entities;

    @Override
    public Text path(String path) {
        this.path = path;
        return this;
    }

    @Override
    public Text mode(String mode) {
        this.mode = mode;
        return this;
    }

    @Override
    public Text replace(ReplacePack pack) {
        this.replacePack = pack == null ? ReplacePack.EMPTY : pack;
        return this;
    }

    @Override
    public Text addReplace(ReplacePack pack) {
        this.replacePack = replacePack == null
                ? pack
                : replacePack.merge(pack);
        return this;
    }

    @Override
    public Text withEntities(Object... entities) {
        this.entities = entities == null ? EMPTY_ENTITY : entities;
        return this;
    }

    @Override
    public Text addEntities(Object... entities) {
        if (this.entities == null) {
            this.entities = entities;
        } else {
            int firstLength = this.entities.length;
            int secondLength = firstLength + entities.length;
            Object[] newEntities = Arrays.copyOf(
                    this.entities,
                    secondLength);
            System.arraycopy(entities, 0, newEntities, firstLength, secondLength);
            this.entities = newEntities;
        }

        return this;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getMode() {
        return mode;
    }

    @Override
    public ReplacePack getReplacements() {
        return replacePack;
    }

    @Override
    public Object[] getEntities() {
        return entities;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public DefaultText clone() {
        return (DefaultText) Text.of(path)
                .mode(mode)
                .replace(replacePack)
                .withEntities(entities);
    }
}
