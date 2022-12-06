package org.avwa.enums;

public enum UserRoleEnum {

    ADMIN("Administratorius"), 
    REGULAR("Paprastas"),
    PUBLIC_REGISTERED_USER("Viešas, registruotas vartotojas"),
    PUBLIC("Viešas");

    private final String label;

    UserRoleEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
