package org.avwa.enums;

public enum UserRoleEnum {

    ADMIN("Administratorius"), 
    REGULAR("Paprastas"),
    PUBLIC("Viešas");

    private final String label;

    UserRoleEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
