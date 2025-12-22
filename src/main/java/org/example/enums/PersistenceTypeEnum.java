package org.example.enums;

public enum PersistenceTypeEnum {
    DB("DB"),
    FS("FS"),
    DEMO("DEMO");

    public final String value;

    PersistenceTypeEnum(String value) {
        this.value = value;
    }

    public static PersistenceTypeEnum getPersistenceTypeByValue(String value) {
        for (PersistenceTypeEnum persistenceTypes : values())
            if (persistenceTypes.value.equals(value))
                return persistenceTypes;
        return null;
    }
}
