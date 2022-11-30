package org.avwa.enums;

public enum UserTypeEnum {

    LOCAL("Lokalus"), SOCIAL("Socialinių tinklų");

    private final String label;

    UserTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
