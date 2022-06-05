package com.filecreatorapi.api;

public class KeyPair {

    public final String name;
    public final String value;

    public KeyPair(KeyPairBuilder builder) {
        this.name = builder.getName();
        this.value = builder.getValue();
    }

    public static KeyPairBuilder builder() {
        return new KeyPairBuilder();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
