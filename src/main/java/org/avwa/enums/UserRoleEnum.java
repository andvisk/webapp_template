package org.avwa.enums;

public enum UserRoleEnum {

    ADMIN("Administratorius"), REGULAR("Paprastas");

    private final String label;

    UserRoleEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
