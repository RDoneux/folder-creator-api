package com.filecreatorapi.api;

public class KeyPairBuilder {

    public String name;
    public String value;

    public KeyPair build() {
        return new KeyPair(this);
    }

    public KeyPairBuilder name(String name) {
        this.name = name;
        return this;
    }

    public KeyPairBuilder value(String value) {
        this.value = value;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
