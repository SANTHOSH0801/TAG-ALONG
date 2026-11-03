package com.tagalong.Constants;

import com.tagalong.util.EntityReference;

public enum Role {
    ADMIN("admin"),
    USER("user");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public  String getName() {
        return this.name;
    }

    public EntityReference.Type getInitiatorLabel() throws IllegalArgumentException {
        switch(this.name) {
            case "admin":
                return EntityReference.Type.ADMIN;
            case "user":
                return  EntityReference.Type.USER;
            default:
                throw new IllegalArgumentException("Unkown role:" + this.name);
        }
    }
}