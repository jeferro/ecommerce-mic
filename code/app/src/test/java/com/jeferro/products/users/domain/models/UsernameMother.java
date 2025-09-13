package com.jeferro.products.users.domain.models;

public abstract class UsernameMother {

    public static Username john() {
        return new Username("john");
    }

    public static Username james() {
        return new Username("james");
    }

    public static Username emily() {
        return new Username("emily");
    }

    public static Username unknown() {
        return new Username("unknown");
    }
}
